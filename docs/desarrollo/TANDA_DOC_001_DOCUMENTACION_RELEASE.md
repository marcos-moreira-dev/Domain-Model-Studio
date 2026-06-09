# DOC-1 — Documentación viva de release y empaquetado

## Objetivo

Alinear la documentación visible de ejecución, validación y empaquetado con la superficie real de scripts públicos antes de cerrar el MSI/RC.

## Cambios

- README raíz actualizado para usar `14-app-image-completa.bat`, `15-msi-completo.bat` y `16-release-candidate.bat`.
- `docs\raiz\README.md` sincronizado con el README raíz.
- `docs\tecnico\COMANDOS.md` y `docs\tecnico\EMPAQUETADO_WINDOWS.md` reescritos con scripts vigentes.
- Guías de desarrollo `EMPAQUETADO_WINDOWS.md`, `VALIDACION_LOCAL.md`, `validacion.md` y `ONBOARDING.md` alineadas con `13/14/15/16`.
- Release notes, RC y limitaciones conocidas actualizadas al estado post P0/RIA-1/RIA-1A.
- Scripts de RC dejan de anunciar Tanda 40 como flujo principal y apuntan al reporte neutral `REPORTE_INSTALABLE_WINDOWS_RC.md`.

## No cambia

- Código productivo Java.
- Formato `.dms`.
- Gramáticas Markdown.
- Recursos IA ya cerrados en RIA-1.
- Flujo real de jpackage, salvo textos/rutas de reporte en la verificación RC.

## Validación esperada

Ejecutar en Windows:

```bat
scripts\02-ejecutar-tests.bat
scripts\15-msi-completo.bat
```

Para RC completo:

```bat
scripts\16-release-candidate.bat
```
