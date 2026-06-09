@echo off
setlocal
cd /d "%~dp0.."

echo == Domain Model Studio :: revalidacion local completa ==
echo Directorio: %CD%
echo.
echo Este script ejecuta la parte automatizable de la revalidacion vigente.
echo Los smokes visuales siguen siendo manuales y quedan documentados en docs\testeo.
echo.

echo [1/4] Verificar entorno
call scripts\00-verificar-entorno.bat
if errorlevel 1 exit /b 1

echo.
echo [2/4] Ejecutar tests completos
call scripts\02-ejecutar-tests.bat
if errorlevel 1 exit /b 1

echo.
echo [3/4] Generar smoke render automatico
call scripts\internal\run-render-smoke.bat
if errorlevel 1 exit /b 1

echo.
echo [4/4] Recalcular metricas de refactor
call scripts\06-medir-refactor.bat
if errorlevel 1 exit /b 1

echo.
echo Revalidacion automatizable completada.
echo Completa manualmente, cuando aplique:
echo    docs\testeo\UI_SMOKE_MINIMO_EJECUTABLE.md
echo    docs\testeo\reportes\REPORTE_SMOKE_UI_MINIMO.md
echo    docs\testeo\REVALIDACION_LOCAL_COMPLETA_TANDA_37.md
echo    docs\testeo\reportes\REPORTE_REVALIDACION_LOCAL_COMPLETA.md
echo.
start "" "docs\testeo\reportes\REPORTE_REVALIDACION_LOCAL_COMPLETA.md"
endlocal
