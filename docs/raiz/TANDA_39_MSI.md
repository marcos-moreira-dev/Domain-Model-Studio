# Tanda 39 — MSI

## Objetivo

Generar instalador Windows solo después de validar app-image.

## Flujo de ejecución

```bat
scripts\02-ejecutar-tests.bat
scripts\13-revalidacion-local-completa.bat
scripts\14-app-image-completa.bat
scripts\15-msi-completo.bat
```

## Salidas esperadas

```txt
dist\installer\*.msi
dist\installer\MSI_MANIFEST.txt
dist\logs\jpackage-msi.log
```

## Smoke obligatorio

Completar:

```txt
docs\testeo\MSI_SMOKE_TANDA_39.md
docs\testeo\reportes\REPORTE_MSI_TANDA_39.md
```

Validar instalación, apertura, importación de ejemplo, guardado/reapertura `.dms`, exportación Markdown/SVG documental, Recursos IA y desinstalación.

## Criterio de no avance

No pasar a Tanda 40 si el MSI no instala, la app instalada no abre, faltan recursos o no se puede guardar/reabrir `.dms`.

## Nota de continuidad

Este archivo resume la tanda para retomar el trabajo si se pierde la conversación. La fuente operativa completa está en `docs\desarrollo\TANDA_39_MSI.md`.
