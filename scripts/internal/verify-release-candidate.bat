@echo off
setlocal
cd /d "%~dp0\..\.."

set APP_IMAGE_MANIFEST=dist\staging\APP_IMAGE_MANIFEST.txt
set MSI_MANIFEST=dist\installer\MSI_MANIFEST.txt
set MSI_DIR=dist\installer
set RENDER_REPORT=target\smoke-render\SMOKE_RENDER_AUTOMATICO.md
set RENDER_CONTACT=target\smoke-render\contact_sheet.html
set RELEASE_DOC=docs\release\RELEASE_CANDIDATE_0_0_1.md
set KNOWN_LIMITS=docs\release\LIMITACIONES_CONOCIDAS_0_0_1.md
set GUIDE=docs\testeo\INSTALABLE_WINDOWS_RC_GUIA.md
set REPORT=docs\testeo\reportes\REPORTE_INSTALABLE_WINDOWS_RC.md
set APP_IMAGE_REPORT=docs\testeo\reportes\REPORTE_APP_IMAGE_TANDA_38.md
set MSI_REPORT=docs\testeo\reportes\REPORTE_MSI_TANDA_39.md
set RELEASE_DIR=dist\release
set RELEASE_LOG_DIR=dist\release\logs
set RC_MANIFEST=dist\release\RELEASE_CANDIDATE_MANIFEST.txt
set METADATA_SCRIPT=scripts\internal\write-artifact-metadata.ps1

echo == Domain Model Studio :: verificar release candidate ==
echo Directorio: %CD%
echo.

if not exist "%APP_IMAGE_MANIFEST%" (
  echo Error: falta %APP_IMAGE_MANIFEST%.
  echo Ejecuta primero scripts\14-app-image-completa.bat
  exit /b 1
)

if not exist "%MSI_MANIFEST%" (
  echo Error: falta %MSI_MANIFEST%.
  echo Ejecuta primero scripts\15-msi-completo.bat
  exit /b 1
)

set MSI_FILE=
for %%F in ("%MSI_DIR%\*.msi") do if exist "%%~fF" set MSI_FILE=%%~fF
if not defined MSI_FILE (
  echo Error: no se encontro MSI en %MSI_DIR%.
  exit /b 1
)

if not exist "%RENDER_REPORT%" (
  echo Error: falta smoke render automatico: %RENDER_REPORT%.
  echo Ejecuta scripts\13-revalidacion-local-completa.bat
  exit /b 1
)

if not exist "%RENDER_CONTACT%" (
  echo Error: falta contact sheet: %RENDER_CONTACT%.
  exit /b 1
)

for %%F in ("%RELEASE_DOC%" "%KNOWN_LIMITS%" "%GUIDE%" "%REPORT%" "%APP_IMAGE_REPORT%" "%MSI_REPORT%") do (
  if not exist "%%~fF" (
    echo Error: falta archivo de release/smoke: %%~fF
    exit /b 1
  )
)

if not exist dist mkdir dist
if not exist "%RELEASE_DIR%" mkdir "%RELEASE_DIR%"
if not exist "%RELEASE_LOG_DIR%" mkdir "%RELEASE_LOG_DIR%"

for %%F in ("dist\logs\*.log") do (
  if exist "%%~fF" copy /y "%%~fF" "%RELEASE_LOG_DIR%\" >nul
)

(
  echo # Domain Model Studio - Release candidate manifest
  echo.
  echo CREATED_LOCAL=%DATE% %TIME%
  echo APP_IMAGE_MANIFEST=%APP_IMAGE_MANIFEST%
  echo MSI_MANIFEST=%MSI_MANIFEST%
  echo MSI_FILE=%MSI_FILE%
  echo RENDER_REPORT=%RENDER_REPORT%
  echo RENDER_CONTACT=%RENDER_CONTACT%
  echo RELEASE_DOC=%RELEASE_DOC%
  echo KNOWN_LIMITS=%KNOWN_LIMITS%
  echo GUIDE=%GUIDE%
  echo REPORT=%REPORT%
  echo SOURCE_LOG_DIR=dist\logs
  echo RELEASE_LOG_DIR=%RELEASE_LOG_DIR%
  echo.
  echo [runtime]
  java --version
  echo.
  echo [jpackage]
  jpackage --version
  echo.
  echo Decision manual pendiente:
  echo Completar %REPORT%
) > "%RC_MANIFEST%"

powershell -NoProfile -ExecutionPolicy Bypass -File "%METADATA_SCRIPT%" -ArtifactPath "%MSI_FILE%" -ManifestPath "%RC_MANIFEST%" -Label "RC_MSI_FILE"
if errorlevel 1 (
  echo Error: no se pudo escribir metadata SHA-256 del MSI en %RC_MANIFEST%.
  exit /b 1
)

powershell -NoProfile -ExecutionPolicy Bypass -File "%METADATA_SCRIPT%" -ArtifactPath "%APP_IMAGE_MANIFEST%" -ManifestPath "%RC_MANIFEST%" -Label "APP_IMAGE_MANIFEST"
if errorlevel 1 (
  echo Error: no se pudo escribir metadata SHA-256 del manifest app-image en %RC_MANIFEST%.
  exit /b 1
)

echo Release candidate verificable encontrado.
echo Manifest: %RC_MANIFEST%
echo MSI: %MSI_FILE%
echo Logs copiados a: %RELEASE_LOG_DIR%
echo.
echo Abriendo documentos de cierre.
start "" "%RELEASE_DIR%"
start "" "%RELEASE_DOC%"
start "" "%KNOWN_LIMITS%"
start "" "%GUIDE%"
start "" "%REPORT%"
endlocal
