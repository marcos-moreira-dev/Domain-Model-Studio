# Tanda PKG-1 — Empaquetado auditable para app-image, MSI y RC

## Objetivo

Cerrar el endurecimiento mínimo del flujo Windows antes del release candidate final: validar entorno de empaquetado, conservar logs por etapa y escribir manifiestos con metadatos verificables de los artefactos.

## Alcance

Esta tanda no cambia código productivo, UI, catálogos, gramáticas, Recursos IA ni persistencia `.dms`. Solo toca scripts internos/públicos de empaquetado, documentación viva de release y tests fuente de guardarraíl.

## Cambios técnicos

- Se agrega `scripts\internal\require-packaging-env.bat` para validar Java runtime 21, `javac` 21, Maven, `jpackage` 21 y PowerShell antes de generar app-image/MSI.
- Se agrega `scripts\internal\write-artifact-metadata.ps1` para anexar tamaño, hash SHA-256 y fecha UTC de artefactos a manifiestos.
- `prepare-jpackage-input.bat` guarda la salida de Maven en `dist\logs\maven-package.log`.
- `create-app-image.bat` genera `dist\staging\APP_IMAGE_MANIFEST.txt` con datos de runtime, rutas, logs y SHA-256 del ejecutable.
- `create-msi-installer.bat` genera `dist\installer\MSI_MANIFEST.txt` con datos de runtime, rutas, logs y SHA-256 del MSI.
- `verify-release-candidate.bat` copia `dist\logs\*.log` a `dist\release\logs` y genera `dist\release\RELEASE_CANDIDATE_MANIFEST.txt` con hashes de cierre.
- `16-release-candidate.bat` evita regenerar dos veces la app-image: ejecuta `14-app-image-completa.bat` y luego invoca `15-msi-completo.bat` con `DMS_REUSE_APP_IMAGE=1`.

## Evidencia esperada

```txt
dist\logs\maven-package.log
dist\logs\jpackage-app-image.log
dist\logs\jpackage-msi.log
dist\staging\APP_IMAGE_MANIFEST.txt
dist\installer\MSI_MANIFEST.txt
dist\release\RELEASE_CANDIDATE_MANIFEST.txt
dist\release\logs\
```

Los manifiestos deben contener campos como:

```txt
*_BYTES
*_SHA256
*_LAST_WRITE_UTC
PACKAGE_LOG
JPACKAGE_LOG
RELEASE_LOG_DIR
```

## Documentación actualizada

- `scripts/README.md`
- `docs/tecnico/EMPAQUETADO_WINDOWS.md`
- `docs/tecnico/COMANDOS.md`
- `docs/testeo/INSTALABLE_WINDOWS_RC_GUIA.md`
- `docs/testeo/reportes/REPORTE_INSTALABLE_WINDOWS_RC.md`
- `docs/release/RELEASE_CANDIDATE_0_0_1.md`
- `docs/estado/ESTADO_ACTUAL.md`

## Validación local recomendada

```bat
scripts\00-verificar-entorno.bat
scripts\02-ejecutar-tests.bat
scripts\14-app-image-completa.bat
scripts\15-msi-completo.bat
scripts\16-release-candidate.bat
```

## Criterio de cierre

La tanda queda cerrada cuando los tests pasan y el flujo RC produce manifiestos con SHA-256/tamaño, logs persistidos y reporte manual pendiente de completar. El MSI solo se aprueba como instalable final después de smoke manual.
