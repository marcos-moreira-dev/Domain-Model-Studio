# Tanda 8 — Centrado, viewport y zoom

## Objetivo

Unificar la política de apertura cómoda de diagramas visuales para que los ejemplos, plantillas e importaciones no dependan de ajustes manuales del usuario.

El criterio de producto es simple: al abrir un diagrama, el contenido debe aparecer centrado, legible y con margen suficiente. El usuario puede mover o hacer zoom manualmente, pero el sistema no debe recentrar agresivamente después de esa interacción.

## Decisiones implementadas

### Política común de viewport

Se agregaron piezas transversales en `presentation.diagramcanvas`:

- `ViewportFitMode`: modos de encuadre (`FIT_TO_CONTENT`, `FIT_WIDTH`, `FIT_HEIGHT`, `CENTER_CONTENT`).
- `CanvasViewportFitPolicy`: cálculo puro de zoom según modo, viewport, contenido y `DiagramSurfaceConfig`.
- `CanvasInitialFitScheduler`: evita repetir banderas `fitScheduled` y claves de fit inicial en cada centro visual.

La política no conoce UML, BPMN, C4, wireframes ni módulos concretos. Cada centro visual solo elige el modo adecuado.

### Superficie zoomable

`ZoomableDiagramSurface` quedó ampliada para:

- aplicar fit por modo mediante `fitToContent(Bounds, ViewportFitMode)`;
- programar `fitToContentWhenReady(ViewportFitMode, Bounds)` esperando medición real de JavaFX;
- exponer `centerContent()` como comando de navegación;
- ofrecer `fitWidthToContent()` para diagramas horizontales;
- recordar si el usuario ajustó manualmente el viewport con zoom o paneo.

### Detección de ajuste manual

`DiagramSurfaceZoomController` y `DiagramSurfacePanController` notifican a la superficie cuando el usuario usa zoom o paneo. Esto permite que futuras integraciones, como el SideDock modular, no peleen contra la vista ajustada manualmente.

### Reducción de duplicación en centers

Los centros visuales especializados ahora delegan el ajuste inicial a `CanvasInitialFitScheduler`:

- `ArchitectureDiagramCenter`
- `BehaviorDiagramCenter`
- `ModuleMapDiagramCenter`
- `ScreenFlowDiagramCenter`
- `UmlClassDiagramCenter`
- `WireframeDiagramCenter`

`BehaviorDiagramCenter` usa `FIT_WIDTH` para UML Secuencia, porque suele ser horizontal. `ScreenFlowDiagramCenter` también usa `FIT_WIDTH` por su naturaleza de navegación horizontal. Los demás usan `FIT_TO_CONTENT`.

## Reglas por tipo

| Familia | Modo inicial |
|---|---|
| UML clases | `FIT_TO_CONTENT` |
| UML casos de uso / actividad / estados / BPMN | `FIT_TO_CONTENT` |
| UML secuencia | `FIT_WIDTH` |
| C4 / despliegue | `FIT_TO_CONTENT` |
| Mapa de módulos | `FIT_TO_CONTENT` |
| Flujo de pantallas | `FIT_WIDTH` |
| Wireframes | `FIT_TO_CONTENT` |
| Diccionario / roles / ayuda | No aplica como canvas libre |

## Criterios técnicos

- El fit inicial se dispara una vez por clave de contenido/proyecto/tipo.
- Si no hay contenido, el scheduler se reinicia.
- El fit espera un viewport útil antes de aplicar zoom y scroll.
- El cálculo de bounds renderizados sigue estando centralizado en la superficie.
- La exportación visual no se cambia en esta tanda; se prepara para Tanda 10.

## Fuera de alcance

- No se implementó SideDock único.
- No se cambió el PDF del diccionario.
- No se rehízo exportación PNG/SVG/PDF.
- No se migró el canvas conceptual.
- No se agregaron comandos visibles nuevos a menús/toolbars; solo se preparó la infraestructura.
