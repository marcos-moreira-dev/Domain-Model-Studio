# Guía de validación del instalable Windows — Release Candidate

## Objetivo

Validar que Domain Model Studio puede empaquetarse como aplicación Windows distribuible y que el MSI resultante puede instalarse, ejecutarse y desinstalarse sin romper los flujos principales.

## Prerrequisitos

```txt
Windows
JDK 21 completo con jpackage en PATH
Maven disponible en PATH
PowerShell disponible para hashes SHA-256
Permisos para instalar/desinstalar MSI
Base automatizada verde con scripts\02-ejecutar-tests.bat
```

## Orden obligatorio

### 1. Validar base automatizada

```bat
scripts\02-ejecutar-tests.bat
```

Debe terminar en `BUILD SUCCESS`.

### 2. Generar app-image

```bat
scripts\14-app-image-completa.bat
```

Debe existir:

```txt
dist\staging\APP_IMAGE_MANIFEST.txt
dist\staging\Domain Model Studio\Domain Model Studio.exe
dist\logs\maven-package.log
dist\logs\jpackage-app-image.log
dist\staging\Domain Model Studio\app
dist\staging\Domain Model Studio\runtime
```

Smoke manual mínimo:

```txt
abrir Domain Model Studio.exe;
ver pantalla de inicio;
abrir ejemplo oficial;
abrir Grafo lógico UENS;
validar que SideDock, canvas y ayuda operativa cargan;
cerrar aplicación sin error.
```

### 3. Generar MSI

```bat
scripts\15-msi-completo.bat
```

Debe existir:

```txt
dist\installer\MSI_MANIFEST.txt
dist\installer\*.msi
dist\logs\jpackage-msi.log
```

Smoke manual MSI:

```txt
instalar MSI;
abrir aplicación desde acceso directo o menú inicio;
crear proyecto Grafo lógico;
importar ejemplo UENS;
guardar .dms;
cerrar y reabrir .dms;
exportar Markdown, PNG y SVG;
abrir guía académica;
abrir ayuda operativa del SideDock;
desinstalar;
confirmar que la desinstalación termina sin error.
```

### 4. Verificar Release Candidate

```bat
scripts\16-release-candidate.bat
```

Debe producir:

```txt
dist\release\RELEASE_CANDIDATE_MANIFEST.txt
dist\release\logs\
```

## Evidencia mínima de manifiestos

Antes de aprobar el RC, revisar que los manifiestos incluyan tamaño y hash SHA-256:

```txt
APP_EXE_BYTES
APP_EXE_SHA256
MSI_FILE_BYTES
MSI_FILE_SHA256
RC_MSI_FILE_SHA256
APP_IMAGE_MANIFEST_SHA256
```

Si falta alguno, el RC no queda aprobado aunque el MSI exista físicamente.

## Criterio de aprobación

El instalable queda aprobado solo si se cumple todo:

```txt
BUILD SUCCESS global;
app-image generada y abierta;
MSI generado;
MSI instalado correctamente;
app instalada abre;
flujos principales pasan;
exportaciones principales pasan;
MSI se desinstala correctamente;
reportes manuales completados.
```

## Reporte asociado

Completar:

```txt
docs\testeo\reportes\REPORTE_INSTALABLE_WINDOWS_RC.md
```

## Diagnóstico de arranque instalado

Desde RC-1A el ejecutable instalado debe apuntar a `DomainModelStudioLauncher`, no directamente a `DomainModelStudioApp`. Si `Domain Model Studio.exe` termina con código 1 o aparece el mensaje `faltan los componentes de JavaFX runtime`, regenerar app-image/MSI con los scripts vigentes y reinstalar el MSI nuevo después de desinstalar el anterior.
