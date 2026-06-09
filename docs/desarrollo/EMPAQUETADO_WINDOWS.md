# Empaquetado Windows

## Objetivo

Crear una app-image o instalador MSI de Domain Model Studio usando los scripts públicos vigentes.

## Requisitos

- Windows 10/11.
- JDK 21 completo con `jpackage` disponible.
- Maven 3.9+.
- Proyecto compilando con tests verdes.

Validar primero:

```bat
scripts\00-verificar-entorno.bat
scripts\02-ejecutar-tests.bat
```

## App-image

```bat
scripts\14-app-image-completa.bat
```

Salida esperada:

```txt
dist\staging\APP_IMAGE_MANIFEST.txt
dist\staging\Domain Model Studio\Domain Model Studio.exe
dist\logs\jpackage-app-image.log
```

El script también abre la app-image y los documentos de smoke manual cuando corresponde.

## MSI

```bat
scripts\15-msi-completo.bat
```

Salida esperada:

```txt
dist\installer\MSI_MANIFEST.txt
dist\installer\*.msi
dist\logs\jpackage-msi.log
```

El MSI solo debe generarse después de app-image validada. El flujo `15-msi-completo.bat` ejecuta esa precondición antes de llamar a `jpackage --type msi`.

## Release candidate local

```bat
scripts\16-release-candidate.bat
```

Salida esperada:

```txt
dist\release\RELEASE_CANDIDATE_MANIFEST.txt
```

Completar además:

```txt
docs\testeo\INSTALABLE_WINDOWS_RC_GUIA.md
docs\testeo\reportes\REPORTE_INSTALABLE_WINDOWS_RC.md
```

## Diagnóstico rápido

Si app-image falla:

1. Revisar `dist\logs\jpackage-app-image.log`.
2. Confirmar `jpackage --version`.
3. Confirmar que `target\jpackage-input` se prepara mediante Maven.

Si MSI falla pero app-image funciona:

1. Revisar `dist\logs\jpackage-msi.log`.
2. Verificar herramientas externas requeridas por `jpackage` en Windows.
3. Reintentar desde PowerShell normal y luego como administrador solo si es necesario.
4. Guardar el log completo para diagnóstico.

## Criterio de no avance

No declarar RC instalable si la app instalada no abre, faltan recursos, no guarda/reabre `.dms`, no exporta entregables básicos o no desinstala correctamente.
