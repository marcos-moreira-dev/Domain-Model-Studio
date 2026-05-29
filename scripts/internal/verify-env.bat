@echo off
setlocal
cd /d "%~dp0\..\.."

echo == Domain Model Studio :: verificar entorno ==
echo Directorio: %CD%
echo.

echo [1/5] Java runtime 21
where java >nul 2>nul
if errorlevel 1 (
  echo ERROR: java no esta disponible en PATH.
  echo Instala JDK 21 y vuelve a abrir PowerShell/CMD.
  exit /b 1
)
java --version
java --version 2>&1 | findstr /r /b /c:"openjdk 21" /c:"java 21" >nul
if errorlevel 1 (
  echo ERROR: el java activo no parece ser version 21.
  echo Configura JAVA_HOME/PATH hacia JDK 21.
  exit /b 1
)
echo.

echo [2/5] Java compiler 21
where javac >nul 2>nul
if errorlevel 1 (
  echo ERROR: javac no esta disponible. Verifica que instalaste JDK 21, no solo JRE.
  exit /b 1
)
javac --version
javac --version 2>&1 | findstr /r /b /c:"javac 21" >nul
if errorlevel 1 (
  echo ERROR: el javac activo no parece ser version 21.
  exit /b 1
)
echo.

echo [3/5] Maven
where mvn >nul 2>nul
if errorlevel 1 (
  echo ERROR: mvn no esta disponible en PATH.
  echo Instala Maven 3.9+ o configura PATH.
  exit /b 1
)
mvn -version
mvn -version 2>&1 | findstr /c:"Java version: 21" >nul
if errorlevel 1 (
  echo ADVERTENCIA: Maven no reporta Java runtime 21 directamente.
  echo Si usas Maven Toolchain correctamente configurado, mvn test/package validara la compilacion real.
)
echo.

echo [4/5] jpackage
where jpackage >nul 2>nul
if errorlevel 1 (
  echo ADVERTENCIA: jpackage no esta disponible. Podras ejecutar tests/app, pero no empaquetar instalador.
) else (
  jpackage --version
  jpackage --version 2>&1 | findstr /r /b /c:"21" >nul
  if errorlevel 1 (
    echo ADVERTENCIA: jpackage disponible, pero no parece ser version 21.
  ) else (
    echo jpackage 21 disponible.
  )
)
echo.

echo [5/5] Git
git --version
if errorlevel 1 (
  echo ADVERTENCIA: git no esta disponible en PATH. El build puede funcionar, pero el versionamiento no.
)
echo.

echo Entorno verificado para ejecucion local. Para empaquetar MSI se valida ademas PowerShell y jpackage con scripts\internal\require-packaging-env.bat.

endlocal
