# Auditoría JavaDoc JD-4 — Presentation, workbenches, SideDock y canvas transversal

## Alcance

Esta tanda documenta la capa de presentación sin cambiar comportamiento funcional. El foco fue explicar cómo JavaFX monta la aplicación, cómo se activan workspaces, cómo se separa el SideDock transversal del sidebar conceptual y cómo el canvas común recibe adaptadores por tipo.

## Paquetes revisados

```txt
presentation/shell
presentation/workspace
presentation/sidedock
presentation/diagramcanvas
presentation/interactivecanvas
presentation/logicalbusinessgraph
```

## Contratos reforzados

```txt
- Shell: composición visual global, menú, tabs, toolbar, guardado y validación.
- Workspace: ruteo de tipo de proyecto a raíz JavaFX registrada.
- SideDock: módulos operativos por workspace especializado.
- Diagram canvas: viewport, zoom, paneo, workspace grande y capas.
- Interactive canvas: selección, drag, labels, resize, bendpoints y render transversal.
- Grafo lógico visual: proyección del dominio a canvas sin convertirse en grafo libre.
```

## Archivos con JavaDoc reforzado

```txt
MainShellView
SpecializedWorkspaceCoordinator
ProjectValidationCoordinator
WorkbenchSideDock
ZoomableDiagramSurface
InteractiveCanvasSurfaceView
InteractiveCanvasAdapter
CanvasBendPointController
LogicalBusinessGraphViewModel
LogicalBusinessGraphCanvasAdapter
LogicalBusinessGraphDiagramCenter
LogicalBusinessGraphWorkbenchContributor
package-info.java de shell, workspace, sidedock, diagramcanvas, interactivecanvas y logicalbusinessgraph
```

## Criterio de no intervención

No se tocó pantalla de inicio, modelo conceptual, canvas conceptual ni sidebar legacy conceptual. La tanda documenta las fronteras para que esas zonas puedan estudiarse sin mezclarse con los workspaces nuevos.

## Resultado

JD-4 deja una ruta de lectura clara para estudiar la capa de presentación como arquitectura de software: primero shell y workspace, luego SideDock, después superficie zoomable, después canvas interactivo y por último un tipo concreto como el Grafo lógico.
