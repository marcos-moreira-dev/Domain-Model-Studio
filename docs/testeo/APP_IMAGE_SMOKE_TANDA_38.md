# Smoke app-image - Tanda 38

## Objetivo

Validar que la app-image generada con `jpackage` abre como aplicacion de escritorio portable y conserva las funciones principales sin depender de rutas de desarrollo.

## Precondiciones

Antes de ejecutar esta guia deben estar cumplidos:

```bat
scripts\02-ejecutar-tests.bat
scripts\13-revalidacion-local-completa.bat
scripts\03-generar-app-image.bat
```

No avanzar a Tanda 39 si la app-image no abre o si el reporte manual queda con bloqueos.

## Archivos esperados

```txt
dist\staging\Domain Model Studio\Domain Model Studio.exe
dist\staging\Domain Model Studio\app
dist\staging\Domain Model Studio\runtime
dist\staging\APP_IMAGE_MANIFEST.txt
```

## Contrato fuente para tests de empaquetado

Las pruebas de productización verifican también las rutas escapadas usadas por los scripts de Windows:

```txt
dist\\staging\\Domain Model Studio\\app
dist\\staging\\Domain Model Studio\\runtime
```

## Pasos de smoke manual

1. Ejecutar:

```bat
scripts\07-validar-app-image.bat
```

2. Confirmar que abre la aplicacion desde:

```txt
dist\staging\Domain Model Studio\Domain Model Studio.exe
```

3. Confirmar pantalla de bienvenida.
4. Abrir un ejemplo oficial importable.
5. Importar un Markdown desde `examples\markdown`.
6. Guardar un archivo `.dms` en una carpeta temporal.
7. Reabrir el `.dms` guardado.
8. Exportar Markdown.
9. Exportar SVG vectorial documental en un tipo visual.
10. Exportar PNG cuando el tipo visual lo soporte.
11. Revisar Recursos IA desde la app-image.
12. Confirmar que los recursos no dependen de rutas del proyecto fuente.

## Criterios de aceptacion

- La app abre desde `dist\staging`.
- La pantalla inicial aparece sin errores.
- Se puede abrir/importar al menos un ejemplo oficial.
- Se puede guardar y reabrir `.dms`.
- Se puede exportar Markdown.
- Se puede exportar SVG vectorial documental cuando aplique.
- No aparecen errores de recursos faltantes.

## Criterio de no avance a Tanda 39

No avanzar a Tanda 39 si ocurre cualquiera de estos puntos:

- La app-image no se genera.
- No existe `Domain Model Studio.exe`.
- La app no abre.
- Faltan recursos, CSS, iconos o ejemplos oficiales.
- El guardado/reapertura `.dms` falla.
- La exportacion basica falla.
- El reporte `REPORTE_APP_IMAGE_TANDA_38.md` queda con bloqueos no resueltos.
