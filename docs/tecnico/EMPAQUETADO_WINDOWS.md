# Empaquetado Windows con jpackage

El proyecto se distribuye como aplicación desktop JavaFX. El empaquetado debe ejecutarse en Windows con JDK 21 completo, Maven, `jpackage` y PowerShell disponibles para calcular hashes SHA-256 de los artefactos.

## Validar entorno

```bat
scripts\00-verificar-entorno.bat
```

## Validar base automatizada

```bat
scripts\02-ejecutar-tests.bat
scripts\13-revalidacion-local-completa.bat
```

## Crear app-image

```bat
scripts\14-app-image-completa.bat
```

Salidas esperadas:

```txt
dist\staging\APP_IMAGE_MANIFEST.txt
dist\staging\Domain Model Studio\Domain Model Studio.exe
dist\logs\maven-package.log
dist\logs\jpackage-app-image.log
```

## Crear instalador MSI

```bat
scripts\15-msi-completo.bat
```

Salidas esperadas:

```txt
dist\installer\MSI_MANIFEST.txt
dist\installer\*.msi
dist\logs\maven-package.log
dist\logs\jpackage-msi.log
```

## Preparar release candidate local

```bat
scripts\16-release-candidate.bat
```

Salida esperada:

```txt
dist\release\RELEASE_CANDIDATE_MANIFEST.txt
dist\release\logs\
```

## Manifiestos auditables

Los manifiestos generados por empaquetado deben conservar al menos:

```txt
APP_VERSION
APP_JAR
MAIN_CLASS
LOG_DIR
PACKAGE_LOG
JPACKAGE_LOG
*_BYTES
*_SHA256
*_LAST_WRITE_UTC
```

El RC copia los logs de `dist\logs` hacia `dist\release\logs` para que el paquete de cierre pueda revisarse sin depender de la consola que ejecutó el build.

## Evitar doble generación en RC

`16-release-candidate.bat` ejecuta `14-app-image-completa.bat` una vez y luego llama a `15-msi-completo.bat` con `DMS_REUSE_APP_IMAGE=1`. Así conserva la separación app-image/MSI, pero evita regenerar dos veces la app-image dentro del mismo flujo de release candidate.

## Diagnóstico

Si el MSI falla pero la app-image abre, el problema probablemente está en las herramientas Windows requeridas por `jpackage` para MSI o en permisos del sistema. Primero validar app-image, guardar `dist\logs\maven-package.log
dist\logs\jpackage-msi.log` y completar el reporte manual correspondiente.

## Helpers internos

El empaquetado usa internamente scripts de `scripts\internal\`, especialmente `prepare-jpackage-input.bat`, `create-app-image.bat`, `create-msi-installer.bat`, `verify-staged-app.bat`, `verify-msi-installer.bat` y `verify-release-candidate.bat`. No hace falta ejecutarlos manualmente salvo diagnóstico.
