# Tanda 29 — Refactor shell/comandos por coordinadores

## Objetivo

Reducir `MainShellCommandHandler` sin cambiar la UX visible ni el contrato público usado por menús, toolbar y workspaces.

La tanda no reescribe el shell completo. Extrae responsabilidades con bajo riesgo y mantiene el handler como fachada pública de comandos.

## Cambios realizados

1. Se agrega `ProjectCreationCoordinator`.
   - Encapsula `NewProjectDialog`.
   - Ejecuta `CreateWorkspaceUseCase`.
   - Usa `NewProjectFactory` para crear proyectos concretos.
   - Decide entre workspace productivo, placeholder de planificación o tipo no soportado.
   - Devuelve la apertura de pestaña al shell mediante un puerto pequeño.

2. Se agrega `ProjectOpenCoordinator`.
   - Encapsula `FileChooser` de apertura `.dms`.
   - Ejecuta `OpenProjectUseCase`.
   - Verifica que el tipo abierto tenga workspace disponible.
   - Conserva la ruta del archivo al abrir proyectos reales o placeholders.

3. `MainShellCommandHandler` queda más delgado.
   - `requestNewProject()` delega en `ProjectCreationCoordinator`.
   - `requestOpenProject()` delega en `ProjectOpenCoordinator`.
   - La activación de pestañas, sincronización de sesión y carga de workspaces especializados sigue en el shell.

4. Se evita cambiar comportamiento visible.
   - No se cambia menú.
   - No se cambia toolbar.
   - No se cambia SideDock.
   - No se cambia persistencia `.dms`.
   - No se cambia importación/exportación Markdown.

## Métrica

`MainShellCommandHandler` baja de 947 a 829 líneas.

## Tests y guardarraíles

- `MainShellSrpRefactorSourceTest` se rebaselinea para exigir coordinadores de creación/apertura.
- `MainShellProjectLifecycleCoordinatorSourceTest` verifica que:
  - el shell delega creación y apertura;
  - `ProjectCreationCoordinator` contiene la creación por tipo;
  - `ProjectOpenCoordinator` contiene FileChooser y apertura `.dms`;
  - el shell no vuelve a llamar directamente a `openProjectUseCase().open` ni al `FileChooser` de apertura.

## Fuera de alcance

- Cierre de pestañas y salida de aplicación.
- Navegación visual común.
- Contrato de workspace activo.
- Refactor del modelo conceptual.
- Cambios en catálogos oficiales.
