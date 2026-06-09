# Contrato de Render Kits

Estado: **vigente desde IMPW-11**

## Propósito

Un `RenderKit` convierte un modelo visual normalizado en nodos JavaFX dibujables. Debe conocer la teoría visual de su familia, pero no debe administrar layout general, scroll, zoom, paneles laterales ni exportación PNG.

## Entrada esperada

```txt
InteractiveCanvasNode
InteractiveCanvasConnector
CanvasBounds
InteractiveCanvasAdapter
DiagramDrawingFacade
```

El render kit puede usar el adaptador para resolver layouts de conectores, selección o metadatos mínimos del modelo visual, pero no debe ir directamente al shell ni al sistema de archivos.

## Salida esperada

```txt
javafx.scene.Node
```

La salida debe representar una figura de diagrama, no una interfaz funcional.

## Permitido

```txt
Group
Rectangle
Line
Polyline
Polygon
Text
Label liviano cuando sea solo rótulo
figuras compuestas de DrawingFacade
```

## Evitar

```txt
Button
TextField
ComboBox
TableView
ListView
ScrollPane interno
controles de formulario dentro del lienzo
```

Los controles reales pertenecen a paneles de propiedades, no al lienzo exportable.

## Relación con Drawing Facade

`DiagramDrawingFacade` centraliza primitivas compartidas:

```txt
nodos/tarjetas visuales
textos con política de elipsis/tooltip
conectores y flechas
decoración de selección
estilos comunes
```

Desde Implementación 9, los conectores visuales generales deben preferir `CanvasConnectorGeometry` para calcular puntos de borde, centro y puntos intermedios antes de llamar a `drawingFacade.connectors().polyline(...)`. Esa utilidad no decide semántica; solo evita duplicación geométrica entre familias.

Cada familia mantiene su semántica:

```txt
UML Clases     -> cajas con secciones, atributos, métodos y relaciones
Wireframes     -> simulaciones visuales de controles, no controles reales
C4             -> software systems, containers, personas, databases
BPMN/Actividad -> pasos, eventos, decisiones y flujos
Secuencia      -> participantes, lifelines y mensajes temporales
```

## Regla de no contaminación

Un render kit específico puede importar `presentation.drawing` e `interactivecanvas`, pero `drawing`, `diagramcanvas` y `workbench` no pueden importar familias concretas.
