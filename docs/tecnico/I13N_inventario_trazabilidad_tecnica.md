# Inventario de trazabilidad técnica I13-N

## Propósito

Este documento registra deuda técnica visible sin convertir la tanda en refactor masivo. La regla es corregir por piezas pequeñas, con responsabilidad única y pruebas de regresión cuando aplique.

## Clases más grandes detectadas

| Líneas | Archivo | Recomendación |
|---:|---|---|
| 1602 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellCommandHandler.java` | seguir extrayendo coordinadores/fachadas de sesión, creación de proyectos y exportación por lote. |
| 1120 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/canvas/DiagramCanvasView.java` | separar construcción visual, interacciones de línea y menú contextual en factories/controladores pequeños. |
| 908 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/canvas/DiagramCanvasViewModel.java` | vigilar crecimiento y extraer responsabilidades cuando supere una nueva tanda funcional. |
| 885 | `src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/json/DmsProjectJsonReader.java` | separar lectores/escritores por documento especializado y mantener contrato JSON central. |
| 814 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/inspector/InspectorViewModel.java` | dividir edición de entidad, relación, estilo y apariencia en sub-modelos o servicios de presentación. |
| 737 | `src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/json/DmsProjectJsonWriter.java` | separar lectores/escritores por documento especializado y mantener contrato JSON central. |
| 664 | `src/main/java/com/marcosmoreira/domainmodelstudio/application/ApplicationServices.java` | agrupar servicios por familias de casos de uso sin ocultar dependencias críticas. |
| 641 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/datadictionary/DataDictionaryEditorView.java` | vigilar crecimiento y extraer responsabilidades cuando supere una nueva tanda funcional. |
| 637 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/wireframe/WireframeEditorView.java` | vigilar crecimiento y extraer responsabilidades cuando supere una nueva tanda funcional. |
| 625 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellView.java` | vigilar crecimiento y extraer responsabilidades cuando supere una nueva tanda funcional. |
| 511 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/modulemap/ModuleMapEditorView.java` | vigilar crecimiento y extraer responsabilidades cuando supere una nueva tanda funcional. |
| 498 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/behavior/BehaviorDiagramEditorView.java` | vigilar crecimiento y extraer responsabilidades cuando supere una nueva tanda funcional. |
| 493 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassDiagramEditorView.java` | vigilar crecimiento y extraer responsabilidades cuando supere una nueva tanda funcional. |
| 476 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/rolespermissions/RolesPermissionsEditorView.java` | vigilar crecimiento y extraer responsabilidades cuando supere una nueva tanda funcional. |
| 467 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/inspector/InspectorView.java` | vigilar crecimiento y extraer responsabilidades cuando supere una nueva tanda funcional. |

## Acción aplicada en I13-N

- Se extrajo `SpecializedWorkspaceCoordinator` para que `MainShellCommandHandler` no cargue, limpie ni consulte editores especializados con cadenas largas de condiciones.
- Se agregaron pruebas estáticas de guardarraíl para impedir que `showProjectInEditor` vuelva a concentrar el ruteo especializado.
- Se alinearon recursos teóricos/IA que todavía decían que BPMN/UML comportamiento no eran importables o no tenían editor visual.

## Regla de trabajo posterior

Antes de agregar un nuevo tipo de diagrama o una nueva acción global, crear primero una clase pequeña con nombre de producto y responsabilidad única. `MainShellView` y `MainShellCommandHandler` no deben ser el lugar por defecto para nuevas reglas.
