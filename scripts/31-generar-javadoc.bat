@echo off
setlocal
cd /d "%~dp0.."

echo == Domain Model Studio :: generar JavaDoc ==
echo Directorio: %CD%
echo.
echo Genera target\site\apidocs para revisar documentacion de codigo.
echo.

mvn -DskipTests javadoc:javadoc
if errorlevel 1 exit /b 1

if not exist "target\site\apidocs\index.html" (
  echo Error: no se encontro target\site\apidocs\index.html
  exit /b 1
)

echo.
echo JavaDoc generado en target\site\apidocs\index.html
start "" "target\site\apidocs\index.html"
endlocal
