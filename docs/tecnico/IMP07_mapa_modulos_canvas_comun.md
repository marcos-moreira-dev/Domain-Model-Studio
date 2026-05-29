# IMP-07 — Nota técnica: mapa de módulos como primer cliente especializado del canvas común

## Decisión técnica

El mapa de módulos deja de ser un render manual aislado y pasa a consumir el contrato común:

```text
InteractiveCanvasAdapter
InteractiveCanvasRenderKit
InteractiveDiagramCanvasView
```

Esto prueba la ruta prevista para los demás diagramas:

```text
documento especializado
→ adapter por tipo
→ canvas común
→ render kit específico
→ layout persistente
→ exportación PNG/SVG/Markdown
```

## Adapter

`ModuleMapCanvasAdapter` traduce:

```text
ModuleNode       -> InteractiveCanvasNode
ModuleDependency -> InteractiveCanvasConnector
parentId         -> InteractiveCanvasConnector de contención
```

IDs visuales usados:

```text
module:<id>
dependency:<id>
module-containment:<parentId>-><childId>
```

Los IDs se generan mediante `VisualElementLayoutIds` para evitar mezclar texto visible con identidad técnica.

## Render kit

`ModuleMapRenderKit` dibuja nodos sobrios y conectores con etiqueta/flecha.

El canvas común no conoce el dominio `modulemap`; solo invoca el render kit.

## Persistencia

La posición de un módulo se conserva en:

```text
DiagramLayout.nodeFor(VisualElementLayoutIds.module(moduleId))
```

Los puntos intermedios de conectores quedan preparados en:

```text
DiagramLayout.connectorById(...).bendPoints()
```

## Ajuste al canvas común

También se corrigió el cálculo de coordenadas en `InteractiveDiagramCanvasView` para usar:

```text
contentLayer.sceneToLocal(sceneX, sceneY)
```

Esto evita mezclar coordenadas de vista con coordenadas de contenido cuando exista zoom/paneo.

## Restricción mantenida

El clic derecho sigue reservado para paneo.

No se reintrodujo menú contextual para acciones de edición.
