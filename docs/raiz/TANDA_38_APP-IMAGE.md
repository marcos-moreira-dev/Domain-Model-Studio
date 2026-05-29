# Tanda 38 - App-image

## Estado

Implementada como flujo de empaquetado previo a MSI.

## Objetivo

Generar y validar una app de escritorio portable antes de pensar en instalador MSI.

## Scripts principales

```bat
scripts\03-generar-app-image.bat
scripts\07-validar-app-image.bat
scripts\14-app-image-completa.bat
```

## Alcance detallado

- Preparar `target\jpackage-input` con Maven y dependencias runtime.
- Ejecutar `jpackage --type app-image`.
- Validar que exista `Domain Model Studio.exe`.
- Validar estructura `app` y `runtime`.
- Generar `dist\staging\APP_IMAGE_MANIFEST.txt`.
- Abrir app-image para smoke manual.
- Completar `docs\testeo\reportes\REPORTE_APP_IMAGE_TANDA_38.md`.

## Criterios de aceptacion

- App-image se genera.
- App abre desde `dist\staging`.
- Importacion y exportacion basica funcionan desde app-image.
- Recursos, CSS, ejemplos oficiales y Recursos IA cargan sin depender de rutas de desarrollo.

## Documentacion relacionada

- `docs\desarrollo\TANDA_38_APP_IMAGE.md`
- `docs\testeo\APP_IMAGE_SMOKE_TANDA_38.md`
- `docs\testeo\reportes\REPORTE_APP_IMAGE_TANDA_38.md`
- `docs\desarrollo\EMPAQUETADO_WINDOWS.md`

## Criterio de no avance

No avanzar a Tanda 39 - MSI si la app-image no abre, no carga recursos o no permite guardar/reabrir `.dms`.
