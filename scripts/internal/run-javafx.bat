@echo off
setlocal
cd /d "%~dp0..\.."
echo == Domain Model Studio :: ejecutar JavaFX ==
echo Directorio: %CD%
echo.
mvn compile javafx:run
endlocal
