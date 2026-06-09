# IMP-03 — Auditoría técnica del contrato de canvas común

## Motivación

Los editores especializados venían renderizando con `Pane + ScrollPane + renderDiagram() + snapshot PNG`. Eso permite pantallas visuales, pero no garantiza edición real, persistencia visual ni SVG vectorial.

La solución es crear una capa común que solo entienda:

```txt
nodos
conectores
layout
selección
paneo
zoom
drag
puntos intermedios
export surface
```

El canvas común no debe conocer conceptos como BPMN, C4, UML, módulos, pantallas ni wireframes.

## Contrato central

```txt
InteractiveCanvasAdapter
```

Responsabilidades:

```txt
- entregar nodos/conectores normalizados;
- entregar layout por nodo/conector;
- recibir selección;
- mover nodos;
- seleccionar por área;
- agregar/mover/eliminar puntos intermedios;
- marcar dirty.
```

## Separación SRP

```txt
InteractiveDiagramCanvasView       composición JavaFX mínima
CanvasInteractionController        orquestación interna de gestos
CanvasPanController                solo paneo
CanvasZoomController               solo zoom
CanvasAreaSelectionController      solo selección rectangular
CanvasNodeDragController           solo movimiento de nodos
CanvasBendPointController          solo puntos intermedios
InteractiveCanvasViewport          escala/traslación/conversión de coordenadas
```

## Adaptador de memoria

`InMemoryInteractiveCanvasAdapter` existe como prueba de contrato y apoyo para migraciones pequeñas. No reemplaza los ViewModels reales; sirve para validar que el canvas común puede funcionar sin acoplarse a familias concretas.

## Regla anti-acoplamiento

El paquete `presentation.interactivecanvas` no debe importar:

```txt
presentation.modulemap
presentation.behavior
presentation.architecture
presentation.umlclass
presentation.wireframe
presentation.screenflow
presentation.rolespermissions
presentation.datadictionary
```

La prueba `InteractiveCanvasPackageBoundaryTest` protege esta regla.

## Riesgos pendientes

```txt
- InteractiveDiagramCanvasView todavía no reemplaza a DiagramCanvasView.
- El render kit default es sobrio y genérico; las familias reales necesitarán render kits/adaptadores propios.
- Las coordenadas y persistencia multitipo todavía no están conectadas al JSON .dms.
- SVG por tipos no conceptuales sigue pendiente.
```

## Criterio para continuar

Antes de migrar editores especializados se debe hacer IMP-04 con cuidado: el modelo conceptual debe seguir funcionando igual o mejor, y solo entonces usar el contrato común para mapa de módulos como primer cliente especializado.
