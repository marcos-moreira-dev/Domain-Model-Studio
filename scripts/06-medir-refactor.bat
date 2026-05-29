@echo off
setlocal
set SCRIPT_DIR=%~dp0
set ROOT_DIR=%SCRIPT_DIR%..
cd /d "%ROOT_DIR%"

echo == Domain Model Studio :: medir refactor ==
echo Directorio: %CD%

python scripts\internal\medir-refactor.py
if errorlevel 1 (
  echo [ERROR] No se pudo ejecutar la medicion. Verifica que Python este disponible en PATH.
  exit /b 1
)

echo.
echo Metricas actualizadas en docs\desarrollo\refactor\METRICAS_PRE_REFACTOR.md
endlocal
