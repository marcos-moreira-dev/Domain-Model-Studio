# IMP-08 — Flujo de pantallas y wireframes sobre canvas común

## Resumen técnico

Se extendió la migración iniciada con mapa de módulos a dos editores administrativos:

```text
SCREEN_FLOW
ADMIN_WIREFRAMES
```

Ambos dejan de depender de dibujos manuales rígidos y pasan a usar:

```text
presentation/interactivecanvas/InteractiveDiagramCanvasView
presentation/interactivecanvas/InteractiveCanvasAdapter
presentation/interactivecanvas/InteractiveCanvasRenderKit
application/visual/VisualLayoutService
```

## Flujo de pantallas

### Adapter

```text
ScreenFlowCanvasAdapter
```

Responsabilidad:

```text
ScreenNode        -> InteractiveCanvasNode
ScreenTransition  -> InteractiveCanvasConnector
screen:<id>       -> NodeLayout
transition:<id>   -> ConnectorLayout
```

No decide semántica. Solo traduce documento + layout + selección hacia el canvas.

### RenderKit

```text
ScreenFlowRenderKit
```

Responsabilidad:

```text
- Renderizar tarjetas de pantalla.
- Renderizar conectores de navegación con flecha.
- Mantener estilo sobrio y consistente con modelo conceptual/mapa de módulos.
```

## Wireframes administrativos

### Adapter

```text
WireframeCanvasAdapter
```

Responsabilidad:

```text
WireframeScreen     -> InteractiveCanvasNode
WireframeComponent  -> InteractiveCanvasNode
wireframe-screen:<id>     -> NodeLayout
wireframe-component:<id>  -> NodeLayout
```

En esta tanda no se crean conectores wireframe porque la promesa del módulo es maqueta administrativa simple, no flujo de navegación ni diseñador de interfaces completo.

### RenderKit

```text
WireframeRenderKit
```

Responsabilidad:

```text
- Renderizar pantallas como cajas grandes.
- Renderizar componentes como bloques simples.
- Mostrar pistas visuales mínimas para tablas/formularios/botones.
```

## Persistencia

`ScreenFlowViewModel` y `WireframeViewModel` ahora usan `VisualLayoutService` para:

```text
- asegurar layout inicial;
- mover nodos;
- guardar posiciones en DiagramLayouts;
- notificar cambios de proyecto;
- mantener modelos semánticos sin coordenadas.
```

## Exportación

PNG:

```text
snapshot del InteractiveDiagramCanvasView activo
```

SVG:

```text
infrastructure/svg/specialized/SpecializedVisualSvgDiagramExporter
```

El SVG sigue siendo vectorial y usa primitivas reales; no se añadió raster embebido.

## Archivos tocados

```text
src/main/java/com/marcosmoreira/domainmodelstudio/presentation/screenflow/ScreenFlowCanvasAdapter.java
src/main/java/com/marcosmoreira/domainmodelstudio/presentation/screenflow/ScreenFlowRenderKit.java
src/main/java/com/marcosmoreira/domainmodelstudio/presentation/screenflow/ScreenFlowEditorView.java
src/main/java/com/marcosmoreira/domainmodelstudio/presentation/screenflow/ScreenFlowViewModel.java
src/main/java/com/marcosmoreira/domainmodelstudio/presentation/wireframe/WireframeCanvasAdapter.java
src/main/java/com/marcosmoreira/domainmodelstudio/presentation/wireframe/WireframeRenderKit.java
src/main/java/com/marcosmoreira/domainmodelstudio/presentation/wireframe/WireframeEditorView.java
src/main/java/com/marcosmoreira/domainmodelstudio/presentation/wireframe/WireframeViewModel.java
src/main/resources/css/screen-flow.css
src/main/resources/css/wireframe.css
```

## Pendiente técnico

```text
- Extraer paneles de estructura/propiedades si vuelven a crecer.
- Mejorar layout inicial de wireframes para agrupar componentes cerca de su pantalla.
- Agregar handles visuales de puntos intermedios al canvas común.
- Ejecutar smoke JavaFX real en entorno local con Maven/JavaFX.
```
