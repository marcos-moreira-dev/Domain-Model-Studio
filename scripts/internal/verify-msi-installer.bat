@echo off
setlocal
cd /d "%~dp0..\.."

set APP_NAME=Domain Model Studio
set DEST_DIR=dist\installer
set MSI_MANIFEST=dist\installer\MSI_MANIFEST.txt
set GUIDE=docs\testeo\MSI_SMOKE_TANDA_39.md
set REPORT=docs\testeo\reportes\REPORTE_MSI_TANDA_39.md

echo == Domain Model Studio :: validar instalador MSI ==
echo Directorio: %CD%
echo.

if not exist "%MSI_MANIFEST%" (
  echo Error: no existe %MSI_MANIFEST%.
  echo Genera primero el MSI con scripts\15-msi-completo.bat
  exit /b 1
)

set MSI_FILE=
for %%F in ("%DEST_DIR%\*.msi") do if exist "%%~fF" set MSI_FILE=%%~fF
if not defined MSI_FILE (
  echo Error: no se encontro ningun MSI en %DEST_DIR%.
  exit /b 1
)

if not exist "%GUIDE%" (
  echo Error: no existe la guia de smoke MSI: %GUIDE%
  exit /b 1
)

if not exist "%REPORT%" (
  echo Error: no existe el reporte de smoke MSI: %REPORT%
  exit /b 1
)

echo MSI encontrado:
echo %MSI_FILE%
echo.
echo Abriendo carpeta del instalador y guias de smoke manual.
echo Instala manualmente el MSI, abre la app instalada y completa el reporte antes de aprobar el RC.
echo.
start "" "%DEST_DIR%"
start "" "%GUIDE%"
start "" "%REPORT%"
endlocal
