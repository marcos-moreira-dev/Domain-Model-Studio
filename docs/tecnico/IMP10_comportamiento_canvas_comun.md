# IMP-10 — Comportamiento sobre canvas común

## Resumen técnico

La familia `presentation.behavior` dejó de depender de una construcción visual local basada en `Pane`, cálculo manual de coordenadas y flechas ad hoc. Ahora usa `InteractiveDiagramCanvasView` mediante un adapter y un renderkit propios.

## Nuevo adapter

```txt
BehaviorCanvasAdapter
```

Responsabilidades:

```txt
- Traducir BehaviorNode a InteractiveCanvasNode.
- Traducir BehaviorEdge a InteractiveCanvasConnector.
- Mapear IDs semánticos a IDs visuales estables:
  - behavior-node:<id>
  - behavior-edge:<id>
- Sincronizar selección entre canvas y ViewModel.
- Delegar movimiento de nodos al ViewModel.
- Delegar puntos intermedios al ViewModel.
```

No contiene reglas de negocio. No modifica el documento semántico directamente.

## Nuevo renderkit

```txt
BehaviorRenderKit
```

Responsabilidades:

```txt
- Dibujar nodos sobrios según kind.
- Dibujar conectores con polyline, etiqueta y punta de flecha.
- Usar puntos intermedios persistidos por ConnectorLayout.
- Mantener estilo simple, consistente y exportable.
```

## ViewModel

`BehaviorDiagramViewModel` ahora contiene operaciones de layout visual equivalentes a las familias ya migradas:

```txt
layoutForNode(...)
layoutForConnector(...)
moveNodeTo(...)
addConnectorBendPoint(...)
moveConnectorBendPointTo(...)
removeConnectorBendPoint(...)
selectNodeById(...)
selectEdgeById(...)
clearPropertySelection()
```

Estas operaciones usan `VisualLayoutService`; no se agregaron coordenadas al dominio `BehaviorNode` ni `BehaviorEdge`.

## Editor

`BehaviorDiagramEditorView` ahora compone:

```txt
BehaviorCanvasAdapter
BehaviorRenderKit
InteractiveDiagramCanvasView
```

El panel izquierdo sigue siendo estructura, el panel derecho sigue siendo propiedades/relaciones y el centro es el canvas editable.

## Reglas de interacción heredadas del canvas común

```txt
Scroll = zoom.
Clic derecho + arrastre = paneo.
Clic izquierdo en fondo + arrastre = selección rectangular.
Clic izquierdo sobre nodo = seleccionar / mover.
Puntos intermedios = operación del canvas común + comando/toolbars.
```

## Límite explícito

UML Secuencia no queda completo en esta tanda. Su editor especializado debe tratar tiempo vertical y líneas de vida en IMP-11.
