@echo off
setlocal
cd /d "%~dp0.."

echo == Domain Model Studio :: release candidate local ==
echo Directorio: %CD%
echo.
echo Este flujo valida la base local, app-image y MSI antes de declarar RC.
echo No sustituye los smoke manuales: abre las guias y reportes para completarlos.
echo.

echo [1/4] Revalidacion local completa
call scripts\13-revalidacion-local-completa.bat
if errorlevel 1 exit /b 1

echo.
echo [2/4] App-image completa
call scripts\14-app-image-completa.bat
if errorlevel 1 exit /b 1

echo.
echo [3/4] MSI completo
set DMS_REUSE_APP_IMAGE=1
call scripts\15-msi-completo.bat
set MSI_EXIT=%ERRORLEVEL%
set DMS_REUSE_APP_IMAGE=
if not "%MSI_EXIT%"=="0" exit /b %MSI_EXIT%

echo.
echo [4/4] Verificar paquete release candidate
call scripts\internal\verify-release-candidate.bat
if errorlevel 1 exit /b 1

echo.
echo Release candidate preparado. Completa el reporte manual del instalable Windows RC.
endlocal
