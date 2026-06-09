# Tanda 26 — Limpieza controlada de scripts

## Propósito

Reducir la superficie pública de `scripts/` sin cambiar comportamiento productivo de la aplicación. El objetivo es que una persona que descargue el repositorio vea pocos comandos vigentes y no una lista larga de validaciones históricas de tandas pasadas.

## Criterio aplicado

Se conserva un script en la raíz solo si cumple al menos una función operativa vigente:

1. ejecutar la aplicación;
2. correr la suite de tests;
3. medir el refactor seguro;
4. revalidar localmente antes de cambios grandes;
5. preparar app-image/MSI/release candidate;
6. generar JavaDoc cuando sea necesario.

Los scripts de tandas pasadas no se conservan por defecto. Si una verificación histórica sigue aportando valor, debe quedar absorbida por un flujo vigente, un test automatizado o documentación viva.

## Scripts públicos vigentes

- `00-verificar-entorno.bat`
- `01-ejecutar-app.bat`
- `02-ejecutar-tests.bat`
- `06-medir-refactor.bat`
- `13-revalidacion-local-completa.bat`
- `14-app-image-completa.bat`
- `15-msi-completo.bat`
- `16-release-candidate.bat`
- `31-generar-javadoc.bat`

## Cambios realizados

- Se eliminaron scripts públicos de validaciones históricas y wrappers duplicados.
- `13-revalidacion-local-completa.bat` ya no depende de wrappers públicos retirados; llama directamente a helpers internos cuando corresponde.
- `14-app-image-completa.bat` llama directamente a `scripts\internal\create-app-image.bat` y `scripts\internal\verify-staged-app.bat`.
- `15-msi-completo.bat` llama directamente a `scripts\internal\create-msi-installer.bat` y `scripts\internal\verify-msi-installer.bat`.
- `scripts\README.md` queda como índice corto de comandos vigentes.

## Qué no cambia

- No cambia código productivo.
- No cambia UX.
- No cambia parser/exporter Markdown.
- No cambia `.dms`.
- No elimina helpers internos requeridos por los scripts vigentes.
- No limpia todavía Markdown histórico; eso queda para la tanda documental.

## Guardarraíles

- `ScriptsPublicSurfaceSourceTest` verifica la lista pública vigente y que no vuelvan scripts de tandas pasadas.
- Tests de empaquetado y release candidate se ajustan para validar los flujos públicos vigentes, no wrappers retirados.
