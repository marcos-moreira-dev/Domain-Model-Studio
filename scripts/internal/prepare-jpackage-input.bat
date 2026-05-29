@echo off
setlocal
cd /d "%~dp0..\.."

set APP_JAR=domain-model-studio-0.0.1-SNAPSHOT.jar
set INPUT_DIR=target\jpackage-input
set PACKAGE_LOG=dist\logs\maven-package.log

echo == Domain Model Studio :: preparar entrada jpackage ==
echo Directorio: %CD%
echo.

if not exist dist mkdir dist
if not exist dist\logs mkdir dist\logs

echo Ejecutando Maven package/dependency:copy-dependencies...
echo Comando: mvn clean package dependency:copy-dependencies -DincludeScope=runtime -DoutputDirectory=target\dependency > "%PACKAGE_LOG%"
echo Directorio=%CD% >> "%PACKAGE_LOG%"
echo. >> "%PACKAGE_LOG%"
call mvn clean package dependency:copy-dependencies -DincludeScope=runtime -DoutputDirectory=target\dependency >> "%PACKAGE_LOG%" 2>&1
if errorlevel 1 (
  echo Error: Maven no pudo compilar/copiar dependencias.
  echo Revisa: %PACKAGE_LOG%
  exit /b 1
)

if exist "%INPUT_DIR%" rmdir /s /q "%INPUT_DIR%"
mkdir "%INPUT_DIR%"

if not exist "target\%APP_JAR%" (
  echo Error: no se encontro target\%APP_JAR%.
  exit /b 1
)

copy "target\%APP_JAR%" "%INPUT_DIR%\" >nul
if exist "target\dependency" (
  xcopy /y /q "target\dependency\*.jar" "%INPUT_DIR%\" >nul
)

echo Entrada preparada en %INPUT_DIR%.
echo Log Maven: %PACKAGE_LOG%
endlocal
