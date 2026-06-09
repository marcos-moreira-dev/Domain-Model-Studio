@echo off
setlocal
cd /d "%~dp0..\.."

set APP_NAME=Domain Model Studio
set APP_VERSION=0.0.1
set APP_JAR=domain-model-studio-0.0.1-SNAPSHOT.jar
set APP_ICON=src\main\resources\branding\domain-model-studio-icon.ico
set MAIN_CLASS=com.marcosmoreira.domainmodelstudio.DomainModelStudioLauncher
set INPUT_DIR=target\jpackage-input
set DEST_DIR=dist\installer
set APP_IMAGE_HOME=dist\staging\%APP_NAME%
set APP_IMAGE_EXE=%APP_IMAGE_HOME%\%APP_NAME%.exe
set APP_IMAGE_MANIFEST=dist\staging\APP_IMAGE_MANIFEST.txt
set MSI_MANIFEST=dist\installer\MSI_MANIFEST.txt
set JPACKAGE_LOG=dist\logs\jpackage-msi.log
set UPGRADE_UUID=36B38C4E-96A1-4C1D-BE39-55A48C23D1F1
set METADATA_SCRIPT=scripts\internal\write-artifact-metadata.ps1

echo == Domain Model Studio :: crear instalador MSI ==
echo Contrato: MSI solo despues de app-image validada, con log y manifiesto auditable.
echo Directorio: %CD%
echo.

rem where jpackage se valida en scripts\internal\require-packaging-env.bat
call scripts\internal\require-packaging-env.bat
if errorlevel 1 exit /b 1

if not exist "%APP_ICON%" (
  echo Error: no se encontro el icono esperado:
  echo %APP_ICON%
  exit /b 1
)

if not exist "%APP_IMAGE_MANIFEST%" (
  echo Error: falta %APP_IMAGE_MANIFEST%.
  echo Ejecuta y valida primero: scripts\14-app-image-completa.bat
  exit /b 1
)

if not exist "%APP_IMAGE_EXE%" (
  echo Error: falta la app-image validable:
  echo %APP_IMAGE_EXE%
  echo Ejecuta y valida primero: scripts\14-app-image-completa.bat
  exit /b 1
)

call scripts\internal\prepare-jpackage-input.bat
if errorlevel 1 exit /b 1

if not exist dist mkdir dist
if not exist dist\logs mkdir dist\logs
if exist "%DEST_DIR%" rmdir /s /q "%DEST_DIR%"
mkdir "%DEST_DIR%"

echo Ejecutando jpackage MSI... > "%JPACKAGE_LOG%"
echo APP_NAME=%APP_NAME% >> "%JPACKAGE_LOG%"
echo APP_VERSION=%APP_VERSION% >> "%JPACKAGE_LOG%"
echo UPGRADE_UUID=%UPGRADE_UUID% >> "%JPACKAGE_LOG%"
echo INPUT_DIR=%INPUT_DIR% >> "%JPACKAGE_LOG%"
echo DEST_DIR=%DEST_DIR% >> "%JPACKAGE_LOG%"
echo. >> "%JPACKAGE_LOG%"

jpackage ^
  --type msi ^
  --name "%APP_NAME%" ^
  --app-version %APP_VERSION% ^
  --input "%INPUT_DIR%" ^
  --main-jar "%APP_JAR%" ^
  --main-class "%MAIN_CLASS%" ^
  --icon "%APP_ICON%" ^
  --dest "%DEST_DIR%" ^
  --vendor "Marcos Moreira" ^
  --description "Desktop suite for editable analysis artifacts from Markdown" ^
  --win-menu ^
  --win-menu-group "%APP_NAME%" ^
  --win-shortcut ^
  --win-dir-chooser ^
  --win-upgrade-uuid %UPGRADE_UUID% >> "%JPACKAGE_LOG%" 2>&1

if errorlevel 1 (
  echo Error: jpackage no pudo crear el MSI.
  echo Revisa: %JPACKAGE_LOG%
  echo Nota: en Windows puede requerir herramientas MSI/WiX disponibles para jpackage.
  exit /b 1
)

set MSI_FILE=
for %%F in ("%DEST_DIR%\*.msi") do if exist "%%~fF" set MSI_FILE=%%~fF
if not defined MSI_FILE (
  echo Error: jpackage termino, pero no se encontro un archivo .msi en %DEST_DIR%.
  echo Revisa: %JPACKAGE_LOG%
  exit /b 1
)

(
  echo # Domain Model Studio - MSI manifest
  echo.
  echo CREATED_LOCAL=%DATE% %TIME%
  echo APP_NAME=%APP_NAME%
  echo APP_VERSION=%APP_VERSION%
  echo APP_JAR=%APP_JAR%
  echo MAIN_CLASS=%MAIN_CLASS%
  echo UPGRADE_UUID=%UPGRADE_UUID%
  echo MSI_FILE=%MSI_FILE%
  echo APP_IMAGE_MANIFEST=%APP_IMAGE_MANIFEST%
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
  echo scripts\internal\verify-msi-installer.bat
) > "%MSI_MANIFEST%"

powershell -NoProfile -ExecutionPolicy Bypass -File "%METADATA_SCRIPT%" -ArtifactPath "%MSI_FILE%" -ManifestPath "%MSI_MANIFEST%" -Label "MSI_FILE"
if errorlevel 1 (
  echo Error: no se pudo escribir metadata SHA-256 del MSI en %MSI_MANIFEST%.
  exit /b 1
)


echo.
echo MSI creado en:
echo %MSI_FILE%
echo.
echo Manifest:
echo %MSI_MANIFEST%
echo.
echo Siguiente paso:
echo    scripts\internal\verify-msi-installer.bat
endlocal
