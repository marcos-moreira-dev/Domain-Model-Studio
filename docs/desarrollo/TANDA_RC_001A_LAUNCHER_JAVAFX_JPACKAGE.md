# Tanda RC-1A — Launcher JavaFX compatible con jpackage

## Motivo

Durante el smoke manual del MSI instalado se detectó que `Domain Model Studio.exe` terminaba inmediatamente con código 1. La instalación dejaba correctamente `app`, `runtime` y el ejecutable, pero la ventana no abría.

El diagnóstico con Java 21 externo mostró el mensaje:

```text
Error: faltan los componentes de JavaFX runtime y son necesarios para ejecutar esta aplicación
```

La causa es el patrón clásico de empaquetado classpath con JavaFX: el lanzador de Java puede abortar cuando la clase principal configurada extiende directamente `javafx.application.Application`, aunque los JAR de JavaFX estén presentes en la carpeta `app`.

## Corrección

Se agrega `DomainModelStudioLauncher`, una clase principal simple que no extiende `Application` y delega en:

```java
Application.launch(DomainModelStudioApp.class, args);
```

Los scripts de `jpackage` y el `javafx-maven-plugin` pasan a usar:

```text
com.marcosmoreira.domainmodelstudio.DomainModelStudioLauncher
```

en vez de apuntar directamente a `DomainModelStudioApp`.

## Alcance

- No cambia dominio, aplicación, presentación ni recursos IA.
- No cambia el flujo de usuario.
- Solo cambia el punto de entrada usado por Maven/JavaFX y por los instaladores.

## Validación esperada

1. `scripts\02-ejecutar-tests.bat` debe pasar.
2. `scripts\16-release-candidate.bat` debe regenerar app-image y MSI.
3. Tras desinstalar el MSI anterior e instalar el nuevo, `C:\Program Files\Domain Model Studio\Domain Model Studio.exe` debe abrir la ventana principal.

## Nota de smoke manual

El MSI anterior no debe aprobarse. El RC aprobado debe ser el generado después de esta tanda.
