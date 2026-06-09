# Estado de base post Tanda 29

## Resumen

Base esperada tras Tanda 29:

```txt
arquitectura razonable
cobertura técnica alta
smoke UI mínimo formalizado
checklist final de cierre disponible
refactors grandes pendientes de evidencia UI
```

## Estado por área

| Área | Estado | Nota |
|---|---|---|
| Dominio | Estable | No requiere refactor general. |
| Aplicación | Estable medio-alto | Cuidar coordinadores y servicios agregadores. |
| Infraestructura `.dms` | Estable alto | Round-trips y payloads especializados cubiertos. |
| Markdown | Estable alto | Dispatcher y exporters por tipo. |
| SVG | Estable medio-alto | `SpecializedVisualSvgWriter` queda como candidato posterior. |
| PNG | Estable medio | Requiere smoke visual porque depende de JavaFX/snapshot. |
| UI runtime | Parcial | Ahora tiene smoke mínimo formal, pero no robot automatizado. |
| Ayuda/IA | Estable medio-alto | Catálogo, figuras y recursos cubiertos. |

## Base recomendada de continuidad

Usar la base más reciente que cumpla:

```txt
scripts\02-ejecutar-tests.bat → BUILD SUCCESS
scripts\09-smoke-ui-minimo.bat → reporte completado o hallazgos registrados
scripts\10-cierre-base.bat → checklist revisado
```

## Riesgos principales

```txt
1. refactorizar shell/canvas sin smoke UI;
2. asumir que source tests reemplazan interacción real;
3. agregar tipos nuevos sin actualizar catálogo, capacidades, toolbar, persistencia, Markdown, SVG/PNG y tests;
4. tocar modelo conceptual protegido por simetría;
5. confiar en PNG sin revisión visual.
```

## Orden de trabajo sugerido

```txt
1. Confirmar tests del ZIP actual.
2. Completar smoke UI mínimo.
3. Tanda R1 — toolbar contributors.
4. Tanda R2 — outputs exportables / ActiveOutputResolver.
5. Tanda R3 — comandos por familias.
6. Tanda R4 — SVG especializado.
7. Tanda Roadmap — nuevo tipo/proyecto.
```

## Decisión

La Tanda 29 no declara el producto como terminado. Declara la base como **ordenada para continuar**, siempre que el usuario confirme tests y smoke en Windows.
