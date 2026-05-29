# Tanda 3 — Soportes comunes para adapters

## Objetivo

Reducir duplicación real en los `*CanvasAdapter` sin alterar todavía la semántica visual de los diagramas ni rediseñar el canvas.

La regla aplicada es:

```txt
El adapter concreto traduce su dominio al canvas.
La mecánica repetida de IDs, selección, dirty state, bounds y puntos intermedios debe vivir en soportes reutilizables.
```

## Soportes creados

### `CanvasElementIdCodec`

Centraliza normalización y extracción de IDs con prefijos.

Uso previsto:

- evitar métodos repetidos como `normalize` y `rawIdAfterPrefix` en cada adapter;
- distinguir IDs de nodos y conectores;
- aislar convenciones de prefijos de cada familia visual.

### `CanvasDirtyState`

Encapsula el estado de cambios pendientes del adapter.

Uso previsto:

- reemplazar booleanos `dirty` repetidos;
- dejar un punto común para limpiar o marcar cambios visuales.

### `CanvasSelectionSupport`

Mantiene selección normalizada de nodos, conectores y puntos intermedios.

Uso previsto:

- evitar que cada adapter reconstruya manualmente `InteractiveCanvasSelection`;
- conservar la regla transversal de no pisar selección manual de bendpoints;
- mantener la selección como mecánica de canvas, no como regla semántica del dominio.

### `CanvasContentBoundsCalculator`

Calcula límites de contenido para encuadre/exportación a partir de layouts de nodos o de un `CanvasReadPort`.

Uso previsto:

- evitar cálculos repetidos de `minX/minY/maxX/maxY`;
- aplicar padding y tamaños mínimos de forma consistente;
- preparar Tanda 8 y Tanda 10.

### `CanvasBendPointEditingSupport`

Soporte inicial para mantener consistente la selección/dirty state al editar puntos intermedios.

Uso previsto:

- preparar Tanda 4;
- evitar repetir la selección de bendpoints.

### `CanvasConnectorLabelEditingSupport`

Punto de extensión para mover etiquetas de conectores mediante `CanvasConnectorLabelPort`.

Uso previsto:

- preparar Tanda 4;
- centralizar la mecánica de marcar cambios cuando una etiqueta se mueve.

## Adapter piloto

Se migró parcialmente `ModuleMapCanvasAdapter` como piloto seguro porque representa un grafo administrativo de nodos/conectores y ya usaba el canvas común.

Ahora delega en:

- `CanvasElementIdCodec` para normalización/prefijos;
- `CanvasSelectionSupport` para selección;
- `CanvasDirtyState` para cambios pendientes;
- `CanvasBendPointEditingSupport` para selección de puntos intermedios;
- `CanvasContentBoundsCalculator` para bounds de exportación/encuadre.

## Adapters pendientes

Los siguientes adaptadores todavía deben migrarse de forma gradual:

```txt
ArchitectureCanvasAdapter
BehaviorCanvasAdapter
ScreenFlowCanvasAdapter
UmlClassCanvasAdapter
WireframeCanvasAdapter
SequenceCanvasAdapter
ConceptualCanvasAdapter
```

Orden sugerido:

1. `ScreenFlowCanvasAdapter`
2. `ArchitectureCanvasAdapter`
3. `BehaviorCanvasAdapter`
4. `UmlClassCanvasAdapter`
5. `WireframeCanvasAdapter`
6. `SequenceCanvasAdapter`
7. `ConceptualCanvasAdapter`

`SequenceCanvasAdapter`, `WireframeCanvasAdapter` y `ConceptualCanvasAdapter` son especiales y no deben migrarse de forma mecánica.

## Qué no se hizo

- No se rediseñaron diagramas.
- No se tocó el sidebar.
- No se cambió la exportación visual.
- No se migró el modelo conceptual.
- No se creó una clase abstracta gigante para adapters.

## Decisión arquitectónica

Se prefiere composición con soportes reutilizables antes que herencia pesada.

```txt
Adapter concreto
  usa soportes comunes
  conserva traducción del dominio
```

Si después de migrar varios adapters queda repetición inevitable, recién tendría sentido evaluar una clase abstracta mínima.
