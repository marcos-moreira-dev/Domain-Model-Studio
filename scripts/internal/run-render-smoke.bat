@echo off
setlocal
cd /d "%~dp0..\.."

echo == Domain Model Studio :: smoke render automatico ==
echo Directorio: %CD%
echo.
echo Ejecutando generador automatico de evidencias SVG...

mvn -Dtest=AutomaticRenderSmokeTest test
if errorlevel 1 exit /b 1

echo.
echo Evidencias generadas en:
echo    target\smoke-render\SMOKE_RENDER_AUTOMATICO.md
echo    target\smoke-render\contact_sheet.html
echo    target\smoke-render\smoke-render.csv
echo.
echo Abriendo reporte si Windows tiene asociacion...
start "" "target\smoke-render\SMOKE_RENDER_AUTOMATICO.md"
start "" "target\smoke-render\contact_sheet.html"
endlocal
