# Tanda 33 — ViewModels visuales y soporte común de cambios de proyecto

## Objetivo

Refactorizar un patrón transversal real de los ViewModels de workspaces visuales/documentales sin cambiar comportamiento visible: la gestión de listener de cambios, bandera de carga y notificación del proyecto actualizado.

## Criterio aplicado

No se introdujo una superclase ni una abstracción de edición común forzada. Cada ViewModel conserva su semántica de dominio, selección, listas y comandos propios. La extracción se limita al contrato repetido de:

- registrar listener de cambio de proyecto;
- bloquear notificaciones durante `loadProject()` y `clear()`;
- notificar al shell cuando una edición modifica el `DiagramProject` activo.

## Cambios principales

### Nuevo soporte común

Se agrega:

- `presentation/workbench/ProjectChangeSupport.java`

Responsabilidades:

- `registerProjectChangeListener(...)` normaliza listener nulo a no-op;
- `runLoading(...)` ejecuta trabajo bajo estado de carga;
- `notifyChanged(...)` publica cambios solo si no se está cargando y el proyecto no es nulo;
- `loading()` permite lecturas puntuales del estado cuando haga falta.

### ViewModels migrados

Se actualizan para usar `ProjectChangeSupport`:

- `ArchitectureDiagramViewModel`
- `BehaviorDiagramViewModel`
- `DataDictionaryViewModel`
- `FreeGraphViewModelCore`
- `LogicalBusinessGraphViewModel`
- `ModuleMapViewModel`
- `RolesPermissionsViewModel`
- `ScreenFlowViewModel`
- `UmlClassDiagramViewModel`
- `WireframeViewModel`

Cada uno conserva su lógica de edición y sus listas observables. Solo se retira duplicación local de `loading` y `projectChangeListener`.

## Zonas no tocadas

No se toca el canvas conceptual legacy:

- `DiagramCanvasViewModel`
- `InspectorViewModel`
- renderers Chen/Crow's Foot
- dominio ER

Tampoco se modifican:

- parser/exporter Markdown;
- persistencia `.dms`;
- catálogos oficiales;
- recursos IA;
- SideDock;
- toolbars;
- UX visible.

## Tests agregados

- `ProjectChangeSupportTest`
- `VisualViewModelsProjectChangeSupportSourceTest`

Los tests verifican que el soporte común bloquea notificaciones durante carga, que los ViewModels migrados ya no declaran listener/loading propios y que el canvas conceptual legacy queda fuera de esta tanda.

## Validación local esperada

```bat
scripts\02-ejecutar-tests.bat
```
