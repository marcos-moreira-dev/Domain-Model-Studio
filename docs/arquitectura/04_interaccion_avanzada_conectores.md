# Tanda 4 — Interacción avanzada de conectores

## Objetivo

Fortalecer el lienzo común para que las relaciones visuales de los diagramas especializados dejen de comportarse como líneas rígidas. La referencia funcional sigue siendo el modelo conceptual: conectores seleccionables, puntos intermedios visibles y etiquetas capaces de separarse de la línea cuando se superponen.

## Decisiones aplicadas

- La etiqueta del conector se renderiza como un elemento interactivo del overlay del canvas común.
- El desplazamiento de la etiqueta usa `ConnectorLayout.labelOffsetX` y `ConnectorLayout.labelOffsetY`, campos que ya forman parte del modelo de layout y de la persistencia JSON.
- La edición de etiquetas se canaliza mediante `CanvasConnectorLabelPort` y `CanvasConnectorLabelController`.
- No se obliga a todos los adapters a implementar etiquetas movibles. El comportamiento es opcional y se activa solo cuando el adapter implementa el puerto.
- `ModuleMapCanvasAdapter` queda como adapter piloto de esta tanda.
- Los conectores del render base ahora tienen una línea de hitbox invisible más gruesa para facilitar selección.

## Cambios de infraestructura

Clases agregadas:

- `CanvasConnectorLabelController`: coordina el movimiento de etiquetas desde el canvas hacia el puerto del adapter.
- `CanvasConnectorLabelPositioner`: calcula la posición base de etiqueta usando el punto medio geométrico de la ruta del conector y los offsets persistentes.

Clases modificadas:

- `InteractiveCanvasSurfaceView`: ahora pinta etiquetas de conectores en overlay, permite seleccionarlas y moverlas si el adapter soporta `CanvasConnectorLabelPort`.
- `DefaultInteractiveCanvasRenderKit`: ahora renderiza conectores como `Group` con línea visible e hitbox invisible.
- `VisualLayoutService`: agrega `moveConnectorLabelBy(...)` para persistir el desplazamiento de etiquetas.
- `ModuleMapViewModel`: agrega operación para mover etiqueta de conector usando `VisualLayoutService`.
- `ModuleMapCanvasAdapter`: implementa `CanvasConnectorLabelPort` como piloto.
- `interactive-canvas.css`: agrega estilos para etiquetas y hitbox de conectores.

## Matriz inicial de capacidades

| Tipo/familia | Selección de conector | Bendpoints | Etiquetas movibles | Nota |
|---|---:|---:|---:|---|
| Modelo conceptual | Sí | Sí | Sí | Canon existente, no tocado agresivamente. |
| Mapa de módulos | Sí | Sí | Sí, piloto | Implementado mediante `CanvasConnectorLabelPort`. |
| UML clases / BPMN / C4 / pantallas | Sí | Parcial según adapter | Preparado | Requieren migración gradual de adapter. |
| UML secuencia | Controlado | No libre | Visible/preparado | Mantener reglas temporales propias. |
| Wireframes | No central | No central | No central | Maqueta, no grafo relacional. |
| Diccionario / roles-permisos | No aplica | No aplica | No aplica | Documento/matriz. |

## Reglas de UX

- Clic sobre etiqueta: selecciona el conector asociado.
- Arrastre sobre etiqueta: actualiza el offset persistente cuando el adapter lo soporta.
- Soltar etiqueta: refresca el canvas para reubicarla según layout real.
- Doble clic sobre conector: conserva el comportamiento existente de agregar punto intermedio cuando el perfil lo permite.
- Los handles de puntos intermedios siguen apareciendo solo cuando el conector está seleccionado.

## Pendientes para siguientes tandas

- Migrar progresivamente adapters especializados a `CanvasConnectorLabelPort`.
- Afinar preview vivo para otros adapters con reglas propias.
- Mantener UML Secuencia como caso especial, sin bendpoints libres normales.
- Coordinar con Tanda 7 para que los conectores y etiquetas respeten la notación visual universitaria.
