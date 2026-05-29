# IMP-00 — Inventario técnico de riesgo: canvas, exportación y estilo

Estado: **implementado como inventario técnico / sin cambios funcionales**

## Propósito

Esta auditoría deja una fotografía verificable antes de iniciar las tandas con cambios de interfaz y canvas. Sirve para evitar que las siguientes implementaciones agreguen herramientas falsas, dupliquen canvas o aumenten clases gigantes.

## Regla de lectura

```text
No se considera terminado un tipo de diagrama solo porque tenga una pantalla visual o un botón.
Debe poder editarse, persistirse y exportarse según la promesa visible del producto.
```

## 1. Clases grandes detectadas

Comando usado:

```bash
find src/main/java -name "*.java" -print0 | xargs -0 wc -l | sort -nr | head -40
```

Resultado relevante:

| Líneas | Archivo | Riesgo principal |
|---:|---|---|
| 1602 | `presentation/shell/MainShellCommandHandler.java` | Orquestación demasiado grande; riesgo de mezclar comandos, exportación, importación y coordinación de workspace. |
| 1120 | `presentation/canvas/DiagramCanvasView.java` | Mezcla render, interacción, viewport, selección, puntos intermedios y coordinación visual. |
| 908 | `presentation/canvas/DiagramCanvasViewModel.java` | Demasiadas responsabilidades de estado, edición y exportación. |
| 885 | `infrastructure/json/DmsProjectJsonReader.java` | Persistencia multipropósito concentrada; riesgo al agregar layout multitipo. |
| 814 | `presentation/inspector/InspectorViewModel.java` | Inspector con demasiadas reglas de edición. |
| 737 | `infrastructure/json/DmsProjectJsonWriter.java` | Escritura `.dms` concentrada; riesgo al agregar layout por tipos. |
| 664 | `application/ApplicationServices.java` | Composición de servicios demasiado centralizada. |
| 641 | `presentation/datadictionary/DataDictionaryEditorView.java` | Vista documental grande; cuidar separación entre UI y lógica de documento. |
| 637 | `presentation/wireframe/WireframeEditorView.java` | Editor visual grande; riesgo de seguir creciendo antes de migrar a canvas común. |
| 625 | `presentation/shell/MainShellView.java` | Shell visual grande; riesgo con tabs escrolleables y toolbar. |
| 511 | `presentation/modulemap/ModuleMapEditorView.java` | Primer candidato a migración; todavía usa render propio. |
| 498 | `presentation/behavior/BehaviorDiagramEditorView.java` | Multipropósito para BPMN/UML conductual/flujo operativo; riesgo de mezclar teoría. |
| 493 | `presentation/umlclass/UmlClassDiagramEditorView.java` | Diagrama editable parcial; candidato a canvas común después de mapa de módulos. |
| 476 | `presentation/rolespermissions/RolesPermissionsEditorView.java` | Editor estructurado; no debe forzarse a canvas libre. |
| 461 | `presentation/architecture/ArchitectureDiagramEditorView.java` | C4/despliegue concentrado; migrar después de familia nodos/conectores. |
| 456 | `presentation/screenflow/ScreenFlowEditorView.java` | Diagrama de pantallas; candidato a canvas común junto con wireframes. |

### Lectura de riesgo

Las siguientes tandas no deberían aumentar directamente estas clases salvo para conectar colaboraciones pequeñas. Si una tanda necesita mucho código en una de estas clases, primero debe extraer un colaborador.

Candidatos de extracción:

```text
ScrollableEditorTabBarView
WorkspaceCommandDispatcher
CanvasViewportController
CanvasSelectionController
CanvasBendPointCommandController
CanvasExportCoordinator
VisualLayoutPersistenceMapper
SpecializedSvgExportRegistry
```

## 2. Uso de menú contextual en canvas

Comando usado:

```bash
grep -R "ContextMenuEvent\|ContextMenu" -n src/main/java/com/marcosmoreira/domainmodelstudio/presentation
```

Resultado:

```text
src/main/java/com/marcosmoreira/domainmodelstudio/presentation/canvas/DiagramCanvasView.java:19:import javafx.scene.control.ContextMenu;
src/main/java/com/marcosmoreira/domainmodelstudio/presentation/canvas/DiagramCanvasView.java:25:import javafx.scene.input.ContextMenuEvent;
src/main/java/com/marcosmoreira/domainmodelstudio/presentation/canvas/DiagramCanvasView.java:986:        installBendPointContextMenu(node, connectorId, bendPointIndex);
src/main/java/com/marcosmoreira/domainmodelstudio/presentation/canvas/DiagramCanvasView.java:1039:    private void installBendPointContextMenu(Node node, DiagramElementId connectorId, int bendPointIndex) {
src/main/java/com/marcosmoreira/domainmodelstudio/presentation/canvas/DiagramCanvasView.java:1040:        ContextMenu menu = new ContextMenu();
src/main/java/com/marcosmoreira/domainmodelstudio/presentation/canvas/DiagramCanvasView.java:1044:        node.addEventHandler(ContextMenuEvent.CONTEXT_MENU_REQUESTED, event -> {
```

### Decisión para la siguiente tanda

Esto debe corregirse en `IMP-01` o `IMP-04`, según convenga:

```text
Clic derecho = paneo exclusivamente.
Eliminar punto intermedio = toolbar contextual + Suprimir/Backspace.
```

No se debe agregar menú contextual de herramientas en el canvas.

## 3. Exportación PNG por snapshot

Comando usado:

```bash
grep -R "snapshot(" -n src/main/java/com/marcosmoreira/domainmodelstudio/presentation
```

Resultado:

| Archivo | Línea | Lectura |
|---|---:|---|
| `presentation/canvas/CanvasPngExporter.java` | 85 | Exportación PNG del canvas conceptual. Es válida como raster. |
| `presentation/modulemap/ModuleMapEditorView.java` | 401 | PNG por snapshot de pane propio; aún no canvas común. |
| `presentation/umlclass/UmlClassDiagramEditorView.java` | 387 | PNG por snapshot de tablero visual; aún no canvas común. |
| `presentation/rolespermissions/RolesPermissionsEditorView.java` | 332 | PNG por snapshot de matriz; aceptable como editor estructurado. |
| `presentation/screenflow/ScreenFlowEditorView.java` | 366 | PNG por snapshot de pane propio; aún no canvas común. |
| `presentation/wireframe/WireframeEditorView.java` | 512 | PNG por snapshot de pane propio; aún no canvas común. |
| `presentation/behavior/BehaviorDiagramEditorView.java` | 385 | PNG por snapshot de pane propio; aún no canvas común. |
| `presentation/architecture/ArchitectureDiagramEditorView.java` | 376 | PNG por snapshot de pane propio; aún no canvas común. |

### Lectura anti-fachada

PNG por snapshot no es un problema en sí. El problema es vender un editor de diagramas si el usuario no puede mover nodos, guardar posiciones y exportar SVG vectorial real.

## 4. Registro de acciones PNG

Comando usado:

```bash
grep -R "registerPngExportAction" -n src/main/java/com/marcosmoreira/domainmodelstudio/presentation
```

Resultado relevante:

| Familia | ViewModel / View | Estado actual |
|---|---|---|
| Modelo conceptual | `DiagramCanvasViewModel` + `DiagramCanvasView` | PNG conectado con `CanvasPngExporter`. |
| Mapa de módulos | `ModuleMapViewModel` + `ModuleMapEditorView` | PNG conectado por snapshot. |
| UML clases | `UmlClassDiagramViewModel` + `UmlClassDiagramEditorView` | PNG conectado por snapshot. |
| Roles/permisos | `RolesPermissionsViewModel` + `RolesPermissionsEditorView` | PNG conectado por snapshot de matriz. |
| Flujo de pantallas | `ScreenFlowViewModel` + `ScreenFlowEditorView` | PNG conectado por snapshot. |
| Wireframes | `WireframeViewModel` + `WireframeEditorView` | PNG conectado por snapshot. |
| Comportamiento | `BehaviorDiagramViewModel` + `BehaviorDiagramEditorView` | PNG conectado por snapshot. |
| Arquitectura | `ArchitectureDiagramViewModel` + `ArchitectureDiagramEditorView` | PNG conectado por snapshot. |

### Lectura

La acción PNG existe de forma transversal, pero eso no equivale a canvas común ni a SVG real.

## 5. Tokens CSS sospechosos

Comando usado:

```bash
grep -R --line-number -E -- "-dms-surface|-dms-panel|-app-workspace|-dms-canvas" src/main/resources/css
```

Resultado:

```text
src/main/resources/css/data-dictionary.css:6:    -fx-background-color: -app-workspace-bg;
src/main/resources/css/uml-class.css:6:    -fx-background-color: -dms-panel-bg;
src/main/resources/css/roles-permissions.css:6:    -fx-background-color: -dms-surface;
src/main/resources/css/roles-permissions.css:29:    -fx-background-color: -dms-panel;
src/main/resources/css/roles-permissions.css:43:    -fx-background-color: -dms-surface;
src/main/resources/css/roles-permissions.css:49:    -fx-background-color: -dms-panel;
src/main/resources/css/roles-permissions.css:56:    -fx-background: -dms-canvas;
src/main/resources/css/roles-permissions.css:57:    -fx-background-color: -dms-canvas;
src/main/resources/css/roles-permissions.css:63:    -fx-background-color: -dms-canvas;
src/main/resources/css/roles-permissions.css:67:    -fx-background-color: -dms-surface;
src/main/resources/css/roles-permissions.css:79:    -fx-background-color: derive(-dms-panel, 5%);
src/main/resources/css/screen-flow.css:6:    -fx-background-color: -dms-surface;
src/main/resources/css/screen-flow.css:31:    -fx-background-color: -dms-panel;
src/main/resources/css/wireframe.css:6:    -fx-background-color: -dms-surface;
src/main/resources/css/wireframe.css:32:    -fx-background-color: -dms-panel;
src/main/resources/css/behavior-diagram.css:5:    -fx-background-color: -dms-surface;
src/main/resources/css/architecture-diagram.css:6:    -fx-background-color: -dms-surface;
```

Tokens existentes verificados:

```text
-dms-bg-app
-dms-bg-panel
-dms-bg-panel-strong
-dms-bg-panel-soft
-dms-bg-workspace
-dms-bg-canvas
```

### Riesgo visual

Algunos módulos pueden heredar colores raros o inconsistentes porque usan nombres antiguos o aliases no definidos como fuente principal. Esto explica la percepción de fondos cafés/grises fuera de estilo.

### Decisión para `IMP-02`

No conviene corregir visualmente módulo por módulo a mano. Primero se deben definir aliases compatibles en tokens y después migrar CSS especializado a tokens oficiales:

```text
-dms-surface      -> -dms-bg-panel-soft o -dms-bg-workspace según contexto
-dms-panel        -> -dms-bg-panel
-dms-canvas       -> -dms-bg-canvas
-dms-panel-bg     -> -dms-bg-panel
-app-workspace-bg -> -dms-bg-workspace
```

## 6. Maven / wrapper

Verificación realizada:

```bash
ls -la mvnw mvnw.cmd 2>/dev/null || true
mvn -version || true
```

Resultado:

```text
No existe `mvnw` ni `mvnw.cmd` en la raíz.
El entorno actual no tiene `mvn` instalado.
```

### Consecuencia

En este entorno no se pudo compilar ni ejecutar tests. Las siguientes tandas deben verificarse en una máquina con Maven o agregar wrapper Maven si se decide estandarizar onboarding.

## 7. Pruebas de arquitectura existentes relacionadas

Archivos detectados:

```text
src/test/java/com/marcosmoreira/domainmodelstudio/architecture/ArchitectureBoundaryTest.java
src/test/java/com/marcosmoreira/domainmodelstudio/architecture/ArchitectureStrongAuditTest.java
```

Límites declarados:

```text
ArchitectureBoundaryTest: MAX_JAVA_FILE_LINES = 450
ArchitectureStrongAuditTest: MAX_CLASS_LINES_SOFT_LIMIT = 650
```

### Lectura honesta

El proyecto ya tiene varias clases por encima de esos límites. Antes de exigir verde total, las siguientes tandas deben decidir si se refactoriza por oleadas o se ajusta temporalmente el criterio con deuda explícita. Lo recomendable es refactor por oleadas, no esconder la deuda.

## 8. Conclusión operativa

Bloqueantes para empezar implementación real:

```text
IMP-01: separar herramientas reales en toolbar y retirar acciones delicadas del clic derecho.
IMP-02: normalizar tokens visuales antes de juzgar estilos por módulo.
IMP-03/IMP-04: crear contrato de canvas común y adaptar el modelo conceptual sin romperlo.
IMP-05: persistencia de layout visual por tipo antes de decir que los diagramas son editables completos.
IMP-06: SVG vectorial real por familia antes de activar esa promesa en todos los tipos.
```

Criterio anti-daño para siguientes tandas:

```text
Después de cada tanda, confirmar que src/main/java y src/main/resources compilan en entorno con Maven.
Si en este entorno no hay Maven, al menos documentar comandos ejecutados, archivos modificados y por qué no se pudo compilar.
```
