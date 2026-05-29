@echo off
setlocal
cd /d "%~dp0..\.."
echo == Domain Model Studio :: ejecutar tests ==
echo Directorio: %CD%
echo.
mvn clean test
endlocal
