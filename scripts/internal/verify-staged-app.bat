@echo off
setlocal
cd /d "%~dp0..\.."

set APP_NAME=Domain Model Studio
set APP_HOME=dist\staging\%APP_NAME%
set APP_EXE=%APP_HOME%\%APP_NAME%.exe
set APP_DIR=%APP_HOME%\app
set RUNTIME_DIR=%APP_HOME%\runtime
set MANIFEST=dist\staging\APP_IMAGE_MANIFEST.txt
set SMOKE_GUIDE=docs\testeo\APP_IMAGE_SMOKE_TANDA_38.md
set SMOKE_REPORT=docs\testeo\reportes\REPORTE_APP_IMAGE_TANDA_38.md

echo == Domain Model Studio :: verificar app-image ==
echo Directorio: %CD%
echo.

if not exist "%APP_EXE%" (
  echo No se encontro el ejecutable esperado:
  echo %APP_EXE%
  echo.
  echo Ejecuta primero:
  echo    scripts\14-app-image-completa.bat
  exit /b 1
)

if not exist "%APP_DIR%" (
  echo No se encontro la carpeta app esperada:
  echo %APP_DIR%
  exit /b 1
)

if not exist "%RUNTIME_DIR%" (
  echo No se encontro la carpeta runtime esperada:
  echo %RUNTIME_DIR%
  exit /b 1
)

if not exist "%MANIFEST%" (
  echo Advertencia: no se encontro manifest de app-image:
  echo %MANIFEST%
)

echo Ejecutable encontrado:
echo %APP_EXE%
echo.
echo Estructura app-image verificada:
echo - %APP_DIR%
echo - %RUNTIME_DIR%
echo.
echo Abriendo aplicacion para smoke test manual...
start "" "%APP_EXE%"

echo.
echo Guia de smoke app-image:
echo %SMOKE_GUIDE%
echo.
echo Reporte de smoke app-image:
echo %SMOKE_REPORT%
echo.
if exist "%SMOKE_GUIDE%" start "" "%SMOKE_GUIDE%"
if exist "%SMOKE_REPORT%" start "" "%SMOKE_REPORT%"
echo.
echo Checklist manual minimo:
echo 1. Ver bienvenida.
echo 2. Abrir un ejemplo oficial.
echo 3. Importar un Markdown oficial.
echo 4. Guardar .dms.
echo 5. Exportar Markdown y SVG documental cuando aplique.
echo 6. Reabrir el .dms guardado.
echo 7. Confirmar que no depende de rutas de desarrollo.
endlocal
