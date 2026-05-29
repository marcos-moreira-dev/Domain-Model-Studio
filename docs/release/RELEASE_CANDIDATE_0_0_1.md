# Domain Model Studio 0.0.1 — Release candidate local

## Estado

Este documento registra el cierre vigente para preparar un **release candidate instalable local en Windows**. No sustituye los logs ni los reportes manuales: la evidencia final debe quedar en los manifiestos de `dist\` y en `docs\testeo\reportes\REPORTE_INSTALABLE_WINDOWS_RC.md`.

## Alcance del release candidate

Incluye:

- Suite multiproyecto de artefactos de análisis.
- Importación Markdown por tipos oficiales.
- Recursos IA productizados y exportables.
- Levantamiento lógico como expediente documental y fuente lógica revisable.
- Grafo lógico del negocio como tipo productivo con catálogo, teoría, SideDock, validación integral, Recursos IA, Markdown, SVG, PNG y persistencia `.dms`.
- Modelo conceptual protegido con canvas propio.
- Canvas común especializado para módulos visuales.
- Exportación Markdown, PNG, SVG vectorial documental y PDF donde aplica.
- Persistencia `.dms` con payloads especializados.
- App-image y MSI como empaquetado Windows.

## Evidencia requerida

- `scripts\00-verificar-entorno.bat` completado.
- `scripts\02-ejecutar-tests.bat` con `BUILD SUCCESS`.
- `scripts\13-revalidacion-local-completa.bat` completado.
- `scripts\14-app-image-completa.bat` completado.
- `scripts\15-msi-completo.bat` completado.
- `scripts\16-release-candidate.bat` completado.
- `dist\staging\APP_IMAGE_MANIFEST.txt` generado con tamaño/hash SHA-256 del ejecutable.
- `dist\installer\MSI_MANIFEST.txt` generado con tamaño/hash SHA-256 del MSI.
- `dist\release\RELEASE_CANDIDATE_MANIFEST.txt` generado con hashes de cierre.
- `dist\release\logs\` generado con copia de logs de empaquetado.
- `docs\testeo\reportes\REPORTE_INSTALABLE_WINDOWS_RC.md` completado.

## Contratos de producto vigentes

- SVG: vectorial documental, no WYSIWYG; no copia exacta del canvas.
- PNG: salida visual rápida, no formato maestro para diagramas enormes.
- Wireframes: estructurales y conservadores.
- BPMN/UML/C4: documentación operativa simplificada, no herramienta industrial exhaustiva.
- Levantamiento lógico: fuente lógica revisable, no generador automático total de otros proyectos.
- Borradores compatibles internos: revisables, no importación automática obligatoria.
- Modelo conceptual: protegido, no migrado al canvas común.

## Criterio de publicación

Puede tratarse como base automatizada verde si:

- tests están verdes.

Puede aprobarse como release candidate instalable si además:

- app-image funciona;
- manifiestos incluyen `*_SHA256`, `*_BYTES` y `*_LAST_WRITE_UTC`;
- MSI instala y desinstala;
- no hay recursos faltantes;
- guardar/reabrir `.dms` funciona;
- exportaciones básicas funcionan;
- Recursos IA se exportan correctamente;
- las limitaciones conocidas están documentadas;
- los reportes manuales quedan completos.

## RC-1A — Corrección de launcher instalado

El RC instalable aprobado debe incluir `DomainModelStudioLauncher` como clase principal de `jpackage`. Esta corrección evita que el MSI instalado cierre inmediatamente por el diagnóstico de JavaFX runtime faltante cuando la clase principal extiende directamente `Application`.
