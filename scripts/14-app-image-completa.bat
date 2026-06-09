@echo off
setlocal
cd /d "%~dp0.."

echo == Domain Model Studio :: app-image completa ==
echo Directorio: %CD%
echo.
echo Este script genera la app-image con helpers internos y abre la validacion manual.
echo Requisito previo recomendado: scripts\13-revalidacion-local-completa.bat
echo.

echo [1/2] Generar app-image
call scripts\internal\create-app-image.bat
if errorlevel 1 exit /b 1

echo.
echo [2/2] Validar app-image generada
call scripts\internal\verify-staged-app.bat
if errorlevel 1 exit /b 1

echo.
echo App-image generada y abierta para smoke manual.
echo Completa:
echo    docs\testeo\reportes\REPORTE_APP_IMAGE_TANDA_38.md
endlocal
