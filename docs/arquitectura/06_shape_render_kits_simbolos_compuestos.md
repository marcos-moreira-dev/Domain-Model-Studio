# Tanda 6 — ShapeKit, RenderKit y símbolos compuestos

## Decisión

Los elementos del lienzo se separan en tres responsabilidades:

- **ShapeKit**: crea símbolos visuales primitivos o compuestos. No maneja selección, arrastre, persistencia, exportación ni reglas de negocio.
- **RenderKit**: arma el nodo visual completo del canvas: símbolo, textos, tamaño, estilo y conectores.
- **Canvas**: instala interacción sobre una raíz única: selección, arrastre, handles, zoom, paneo y edición.

La regla principal es que un símbolo compuesto se comporta como una sola unidad interactiva. Un actor UML puede estar formado por cabeza, cuerpo, brazos y piernas, pero para el usuario es un único nodo seleccionable y arrastrable.

## Contratos agregados

- `DiagramSymbol`: catálogo semántico de símbolos disponibles.
- `DiagramShapeKit`: contrato común para familias de símbolos.
- `AbstractPrimitiveShapeKit`: base ligera con helpers de primitivas JavaFX.
- `CanvasNodeViewFactory`: fábrica de raíz interactiva con hitbox transparente para que las primitivas internas no fragmenten la interacción.

## ShapeKits normalizados

Las familias existentes quedaron alineadas al contrato común:

- `UmlShapeKit`
- `BpmnShapeKit`
- `OperationalFlowShapeKit`
- `C4ShapeKit`
- `AdminShapeKit`
- `WireframeShapeKit`

Cada una declara qué símbolos soporta y expone `createShape(DiagramSymbol, DiagramShapeStyle)` sin perder sus métodos expresivos actuales, como `actorSymbol()` o `gatewaySymbol()`.

## Piloto aplicado

`BehaviorRenderKit` y `DefaultInteractiveCanvasRenderKit` ahora envuelven sus nodos mediante `CanvasNodeViewFactory`. Esto deja una raíz interactiva única con hitbox completa y evita que el usuario dependa de clicar exactamente una primitiva interna del símbolo.

Esto prepara la Tanda 7, donde la apariencia se acercará más a la notación universitaria primitiva, sin tarjetas decorativas donde no correspondan.

## Reglas para próximas tandas

1. Un ShapeKit no debe saber de adapters, viewmodels, exportadores ni selección.
2. Un RenderKit no debe instalar lógica profunda de arrastre o persistencia.
3. El canvas debe recibir un nodo raíz único por elemento.
4. Los símbolos UML/BPMN deben priorizar primitivas vectoriales propias sobre iconos externos.
5. C4, wireframes y módulos pueden usar cajas cuando su teoría o naturaleza visual lo justifique.
