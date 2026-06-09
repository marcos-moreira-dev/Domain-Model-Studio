@echo off
setlocal
cd /d "%~dp0\..\.."

echo == Domain Model Studio :: entorno de empaquetado ==
echo Directorio: %CD%
echo.

echo [1/5] Java runtime 21
where java >nul 2>nul
if errorlevel 1 (
  echo Error: java no esta disponible en PATH.
  echo Instala Eclipse Temurin JDK 21 o un JDK 21 completo y vuelve a abrir la terminal.
  exit /b 1
)
java --version
java --version 2>&1 | findstr /r /b /c:"openjdk 21" /c:"java 21" >nul
if errorlevel 1 (
  echo Error: el java activo no parece ser version 21.
  echo El empaquetado con jpackage debe ejecutarse con JDK 21 completo.
  exit /b 1
)
echo.

echo [2/5] Java compiler 21
where javac >nul 2>nul
if errorlevel 1 (
  echo Error: javac no esta disponible. Verifica que instalaste un JDK 21 completo, no solo JRE.
  exit /b 1
)
javac --version
javac --version 2>&1 | findstr /r /b /c:"javac 21" >nul
if errorlevel 1 (
  echo Error: el javac activo no parece ser version 21.
  exit /b 1
)
echo.

echo [3/5] Maven
where mvn >nul 2>nul
if errorlevel 1 (
  echo Error: mvn no esta disponible en PATH.
  echo Instala Maven 3.9+ o configura PATH antes de empaquetar.
  exit /b 1
)
mvn -version
mvn -version 2>&1 | findstr /c:"Java version: 21" >nul
if errorlevel 1 (
  echo Advertencia: Maven no reporta Java runtime 21 directamente.
  echo Si usas toolchains Maven correctamente configurados, mvn package/test sera la fuente de verdad.
  echo Si no usas toolchains, configura JAVA_HOME hacia JDK 21 antes de continuar.
)
echo.

echo [4/5] jpackage 21
where jpackage >nul 2>nul
if errorlevel 1 (
  echo Error: jpackage no esta disponible en PATH. Verifica JDK 21 completo.
  exit /b 1
)
jpackage --version
jpackage --version 2>&1 | findstr /r /b /c:"21" >nul
if errorlevel 1 (
  echo Error: jpackage activo no parece ser version 21.
  exit /b 1
)
echo.

echo [5/5] PowerShell para hashes SHA-256
where powershell >nul 2>nul
if errorlevel 1 (
  echo Error: PowerShell no esta disponible. Se requiere para escribir hashes SHA-256 en manifiestos.
  exit /b 1
)
powershell -NoProfile -ExecutionPolicy Bypass -Command "$PSVersionTable.PSVersion.ToString()"
if errorlevel 1 (
  echo Error: PowerShell no pudo ejecutarse.
  exit /b 1
)
echo.

echo Entorno de empaquetado validado para Java 21 + jpackage + manifiestos auditables.
endlocal
