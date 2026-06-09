@echo off
setlocal
cd /d "%~dp0..\.."

set APP_NAME=Domain Model Studio
set APP_VERSION=0.0.1
set APP_JAR=domain-model-studio-0.0.1-SNAPSHOT.jar
set APP_ICON=src\main\resources\branding\domain-model-studio-icon.ico
set MAIN_CLASS=com.marcosmoreira.domainmodelstudio.DomainModelStudioLauncher
set INPUT_DIR=target\jpackage-input
set DEST_DIR=dist\staging
set APP_HOME=%DEST_DIR%\%APP_NAME%
set APP_EXE=%APP_HOME%\%APP_NAME%.exe
set MANIFEST=dist\staging\APP_IMAGE_MANIFEST.txt
set METADATA_SCRIPT=scripts\internal\write-artifact-metadata.ps1

if not exist dist mkdir dist
if not exist dist\logs mkdir dist\logs

set JPACKAGE_LOG=dist\logs\jpackage-app-image.log

echo == Domain Model Studio :: crear app-image ==
echo Directorio: %CD%
echo.
echo Contrato: app-image portable antes de MSI, con log y manifiesto auditable.
echo.

rem where jpackage se valida en scripts\internal\require-packaging-env.bat
call scripts\internal\require-packaging-env.bat
if errorlevel 1 exit /b 1

if not exist "%APP_ICON%" (
  echo Error: no se encontro el icono esperado:
  echo %APP_ICON%
  exit /b 1
)

call scripts\internal\prepare-jpackage-input.bat
if errorlevel 1 exit /b 1

if exist "%DEST_DIR%" rmdir /s /q "%DEST_DIR%"
mkdir "%DEST_DIR%"

echo Ejecutando jpackage... > "%JPACKAGE_LOG%"
echo APP_NAME=%APP_NAME% >> "%JPACKAGE_LOG%"
echo APP_VERSION=%APP_VERSION% >> "%JPACKAGE_LOG%"
echo INPUT_DIR=%INPUT_DIR% >> "%JPACKAGE_LOG%"
echo DEST_DIR=%DEST_DIR% >> "%JPACKAGE_LOG%"
echo. >> "%JPACKAGE_LOG%"

jpackage ^
  --type app-image ^
  --name "%APP_NAME%" ^
  --app-version %APP_VERSION% ^
  --input "%INPUT_DIR%" ^
  --main-jar "%APP_JAR%" ^
  --main-class "%MAIN_CLASS%" ^
  --icon "%APP_ICON%" ^
  --dest "%DEST_DIR%" ^
  --vendor "Marcos Moreira" ^
  --description "Desktop suite for editable analysis artifacts from Markdown" >> "%JPACKAGE_LOG%" 2>&1

if errorlevel 1 (
  echo Error: jpackage no pudo crear la app-image.
  echo Revisa: %JPACKAGE_LOG%
  exit /b 1
)

if not exist "%APP_EXE%" (
  echo Error: jpackage termino, pero no se encontro el ejecutable esperado:
  echo %APP_EXE%
  echo Revisa: %JPACKAGE_LOG%
  exit /b 1
)

(
  echo # Domain Model Studio - app-image manifest
  echo.
  echo CREATED_LOCAL=%DATE% %TIME%
  echo APP_NAME=%APP_NAME%
  echo APP_VERSION=%APP_VERSION%
  echo APP_JAR=%APP_JAR%
  echo MAIN_CLASS=%MAIN_CLASS%
  echo APP_HOME=%APP_HOME%
  echo APP_EXE=%APP_EXE%
  echo INPUT_DIR=%INPUT_DIR%
  echo DEST_DIR=%DEST_DIR%
  echo LOG_DIR=dist\logs
  echo PACKAGE_LOG=dist\logs\maven-package.log
  echo JPACKAGE_LOG=%JPACKAGE_LOG%
  echo.
  echo [runtime]
  java --version
  echo.
  echo [jpackage]
  jpackage --version
  echo.
  echo Validacion siguiente:
  echo scripts\14-app-image-completa.bat
) > "%MANIFEST%"

powershell -NoProfile -ExecutionPolicy Bypass -File "%METADATA_SCRIPT%" -ArtifactPath "%APP_EXE%" -ManifestPath "%MANIFEST%" -Label "APP_EXE"
if errorlevel 1 (
  echo Error: no se pudo escribir metadata SHA-256 de app-image en %MANIFEST%.
  exit /b 1
)


echo.
echo App-image creada en:
echo %APP_HOME%
echo.
echo Ejecutable:
echo %APP_EXE%
echo.
echo Manifest:
echo %MANIFEST%
echo.
echo Siguiente paso:
echo    scripts\14-app-image-completa.bat
endlocal
