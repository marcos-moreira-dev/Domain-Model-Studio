from pathlib import Path
import datetime
import re

ROOT = Path(__file__).resolve().parents[2]
SRC = ROOT / 'src' / 'main' / 'java'
OUT = ROOT / 'docs' / 'desarrollo' / 'refactor' / 'METRICAS_PRE_REFACTOR.md'
LOG = ROOT / 'docs' / 'desarrollo' / 'logs' / 'tanda_015_metricas_entorno.txt'


def level(lines: int) -> str:
    if lines >= 700:
        return 'Crítico'
    if lines >= 450:
        return 'Muy alto'
    if lines >= 300:
        return 'Alto'
    if lines >= 180:
        return 'Observación'
    return 'Normal'


def code_lines(lines: list[str]) -> int:
    count = 0
    in_block = False
    for line in lines:
        stripped = line.strip()
        if not stripped:
            continue
        if in_block:
            if '*/' in stripped:
                in_block = False
                rest = stripped.split('*/', 1)[1].strip()
                if rest and not rest.startswith('//'):
                    count += 1
            continue
        if stripped.startswith('/*'):
            if '*/' not in stripped:
                in_block = True
            continue
        if stripped.startswith('*') or stripped.startswith('//'):
            continue
        count += 1
    return count


def analyze():
    files = sorted(SRC.rglob('*.java'))
    rows = []
    for path in files:
        text = path.read_text(encoding='utf-8', errors='ignore')
        lines = text.splitlines()
        pkg_match = re.search(r'package\s+([\w.]+);', text)
        method_count = len(re.findall(
            r'(?m)^\s*(public|protected|private|static|final|synchronized|abstract|native|default|\s)+'
            r'[\w<>\[\], ?]+\s+\w+\s*\([^;{}]*\)\s*(?:throws [^{]+)?\{',
            text,
        ))
        rows.append({
            'path': path.relative_to(ROOT).as_posix(),
            'package': pkg_match.group(1) if pkg_match else '(sin package)',
            'lines': len(lines),
            'code': code_lines(lines),
            'methods': method_count,
            'level': level(len(lines)),
        })
    return rows


def main():
    rows = analyze()
    now = datetime.datetime.now().isoformat(timespec='seconds')
    counts = {name: sum(1 for row in rows if row['level'] == name) for name in ['Crítico', 'Muy alto', 'Alto', 'Observación', 'Normal']}
    package_totals = {}
    for row in rows:
        item = package_totals.setdefault(row['package'], {'files': 0, 'lines': 0, 'code': 0})
        item['files'] += 1
        item['lines'] += row['lines']
        item['code'] += row['code']
    top = sorted(rows, key=lambda row: row['lines'], reverse=True)[:40]

    OUT.parent.mkdir(parents=True, exist_ok=True)
    LOG.parent.mkdir(parents=True, exist_ok=True)

    md = []
    md.append('# Métricas pre-refactor — Tanda 15\n\n')
    md.append(f'Generado: `{now}`\n\n')
    md.append('## Alcance\n\n')
    md.append('- Fuente analizada: `src/main/java`.\n')
    md.append('- Métricas estáticas aproximadas: líneas físicas, líneas de código sin comentarios simples y métodos detectados por patrón.\n')
    md.append('- No sustituye revisión humana; sirve para priorizar refactor seguro.\n\n')
    md.append('## Resumen\n\n')
    md.append(f'- Archivos Java: **{len(rows)}**\n')
    md.append(f'- Líneas físicas totales: **{sum(row["lines"] for row in rows)}**\n')
    md.append(f'- Líneas de código aproximadas: **{sum(row["code"] for row in rows)}**\n')
    md.append(f'- Métodos aproximados detectados: **{sum(row["methods"] for row in rows)}**\n\n')
    md.append('### Distribución por tamaño\n\n')
    md.append('| Nivel | Criterio | Archivos |\n|---|---:|---:|\n')
    criteria = {'Crítico': '>= 700 líneas', 'Muy alto': '>= 450 líneas', 'Alto': '>= 300 líneas', 'Observación': '>= 180 líneas', 'Normal': '< 180 líneas'}
    for name in ['Crítico', 'Muy alto', 'Alto', 'Observación', 'Normal']:
        md.append(f'| {name} | {criteria[name]} | {counts[name]} |\n')
    md.append('\n## Top 40 archivos por tamaño\n\n')
    md.append('| # | Nivel | Líneas | Código aprox. | Métodos aprox. | Archivo |\n|---:|---|---:|---:|---:|---|\n')
    for index, row in enumerate(top, 1):
        md.append(f'| {index} | {row["level"]} | {row["lines"]} | {row["code"]} | {row["methods"]} | `{row["path"]}` |\n')
    md.append('\n## Paquetes más pesados\n\n')
    md.append('| # | Líneas | Código aprox. | Archivos | Package |\n|---:|---:|---:|---:|---|\n')
    for index, (package, data) in enumerate(sorted(package_totals.items(), key=lambda item: item[1]['lines'], reverse=True)[:30], 1):
        md.append(f'| {index} | {data["lines"]} | {data["code"]} | {data["files"]} | `{package}` |\n')
    md.append('\n## Lectura rápida\n\n')
    md.append('- Priorizar primero clases `Crítico` y `Muy alto`, pero solo si hay guardarraíles de comportamiento.\n')
    md.append('- No partir clases por tamaño solamente: partir por responsabilidad observable y contratos existentes.\n')
    md.append('- Mantener el modelo conceptual protegido durante las primeras tandas de refactor.\n')
    OUT.write_text(''.join(md), encoding='utf-8')
    LOG.write_text(
        f'Tanda 15 métricas\nJava files: {len(rows)}\nPhysical LOC: {sum(row["lines"] for row in rows)}\nApprox code LOC: {sum(row["code"] for row in rows)}\nGenerated: {now}\n',
        encoding='utf-8',
    )
    print(f'Métricas escritas en {OUT.relative_to(ROOT)}')


if __name__ == '__main__':
    main()
