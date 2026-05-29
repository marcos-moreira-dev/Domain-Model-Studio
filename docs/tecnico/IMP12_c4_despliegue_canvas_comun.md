# IMP-12 — C4 y despliegue sobre canvas común

## Objetivo técnico

Alinear `ArchitectureDiagramEditorView` con la misma infraestructura visual usada por mapa de módulos, wireframes, UML clases y comportamiento.

## Contrato visual usado

```text
ArchitectureDiagramDocument
→ ArchitectureCanvasAdapter
→ InteractiveDiagramCanvasView
→ ArchitectureRenderKit
```

El dominio de arquitectura no recibe coordenadas. Las posiciones viven en:

```text
DiagramProject.layouts().activeLayout()
```

Los IDs de layout son:

```text
architecture-node:<nodeId>
architecture-edge:<edgeId>
```

## Responsabilidades

### ArchitectureCanvasAdapter

Responsable de traducir:

```text
ArchitectureNode → InteractiveCanvasNode
ArchitectureEdge → InteractiveCanvasConnector
```

También sincroniza selección, movimiento, puntos intermedios y limpieza de selección.

### ArchitectureRenderKit

Responsable de dibujar tarjetas y conectores sobrios. No conoce el ViewModel ni modifica dominio.

### ArchitectureDiagramViewModel

Responsable de:

- CRUD semántico mediante use cases.
- `VisualLayoutService` para layout persistente.
- notificar cambios al shell.
- exponer acción PNG registrada por la vista.

### ArchitectureDiagramEditorView

Responsable de componer UI:

```text
panel izquierdo = estructura
centro = canvas común
panel derecho = relaciones + propiedades
```

## Exportación

- PNG: snapshot del `InteractiveDiagramCanvasView` activo.
- SVG: exportador especializado vectorial, ya existente desde IMP-06.

## Decisiones de UX/UI

- Se mantiene el patrón visual sobrio de modelo conceptual/casos de uso.
- No se agregaron herramientas al panel izquierdo: la estructura queda como navegación.
- Las propiedades siguen en el panel derecho.
- Clic derecho sigue reservado al paneo por la infraestructura común.

## Riesgos pendientes

- El canvas común todavía puede mejorar con handles visibles de puntos intermedios.
- El layout inicial sigue siendo genérico por grilla; una futura tanda puede especializar posiciones iniciales por tipo C4/despliegue sin romper persistencia.
- `ArchitectureDiagramEditorView` y `ArchitectureDiagramViewModel` todavía pueden dividirse en subcomponentes si crecen más.
