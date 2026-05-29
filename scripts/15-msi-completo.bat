@echo off
setlocal
cd /d "%~dp0.."

echo == Domain Model Studio :: MSI completo ==
echo Este flujo requiere app-image validada antes de generar MSI.
echo.

echo [1/3] Generar y validar app-image
if /i "%DMS_REUSE_APP_IMAGE%"=="1" (
  echo Reutilizando app-image generada en una etapa anterior del mismo flujo.
  echo scripts\14-app-image-completa.bat debe haberse ejecutado antes en este proceso.
) else (
  call scripts\14-app-image-completa.bat
  if errorlevel 1 exit /b 1
)

echo.
echo [2/3] Generar instalador MSI
call scripts\internal\create-msi-installer.bat
if errorlevel 1 exit /b 1

echo.
echo [3/3] Abrir verificacion y reporte MSI
call scripts\internal\verify-msi-installer.bat
if errorlevel 1 exit /b 1

echo.
echo MSI generado. Completa el reporte manual antes de declarar RC.
endlocal
