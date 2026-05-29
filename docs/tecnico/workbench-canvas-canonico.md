# Workbench y canvas canónico

Estado: **vigente desde IMPW-11**

## Problema que resuelve

Antes de la normalización visual, el modelo conceptual tenía una experiencia madura:

```txt
shell principal
→ header cerrable
→ panel izquierdo colapsable
→ lienzo central grande con zoom/paneo
→ panel derecho colapsable
```

Los demás diagramas tendían a traer su propio `SplitPane`, header local, paneles internos y una superficie de dibujo distinta. El resultado era una aplicación visualmente inconsistente: el zoom no se comportaba igual, los paneles no se podían cerrar/restaurar de forma homogénea y algunas vistas parecían miniaplicaciones incrustadas.

La normalización IMPW deja una infraestructura transversal para que todos los diagramas visuales se monten como variantes de un mismo patrón, sin contaminar el núcleo con reglas específicas de módulos, UML, C4, BPMN o wireframes.

## Piezas principales

```txt
presentation.workbench
  DiagramWorkbenchView
  DiagramWorkbenchContributor
  DiagramWorkbenchDescriptor
  DiagramWorkbenchSlots
  WorkspaceHeaderView
  WorkbenchPanelPolicy
  WorkbenchPanelToggleController

presentation.diagramcanvas
  ZoomableDiagramSurface
  DiagramSurfaceConfig
  DiagramSurfaceLayers
  DiagramSurfaceViewportController
  DiagramSurfaceZoomController
  DiagramSurfacePanController
  DiagramSurfaceFitController
  DiagramSurfaceCoordinateMapper

presentation.drawing
  DiagramDrawingFacade
  DiagramNodeFactory
  DiagramTextFactory
  DiagramConnectorFactory
  DiagramSelectionDecorationFactory
```

## Regla de responsabilidad

El `Workbench` organiza el área de trabajo. La `Surface` ofrece el lienzo técnico. `Drawing` ofrece primitivas visuales. Los paquetes de familia solo traducen su documento a nodos, conectores, paneles y renderizado específico.

```txt
Workbench común       -> layout, header, slots y paneles
Surface común         -> scroll, zoom, pan, fit, capas y export node
Drawing común         -> cajas, textos, conectores, flechas y selección
Adapter específico    -> documento semántico a modelo visual
RenderKit específico  -> teoría visual propia de cada diagrama
Paneles específicos   -> estructura y propiedades del documento activo
```

## Qué no debe hacerse

```txt
No crear nuevos SplitPane internos para cada editor visual.
No reimplementar zoom/paneo por tipo de diagrama.
No meter lógica de módulos, UML, C4 o BPMN dentro de workbench/surface/drawing.
No dibujar formularios reales dentro del lienzo de diagramas.
No usar el canvas como pantalla de configuración; el resultado principal debe ser visual/exportable.
```

## Cómo agregar un nuevo diagrama visual

1. Crear un documento de dominio o reutilizar uno existente.
2. Crear un `ViewModel` específico del diagrama.
3. Crear un `CanvasAdapter` que implemente `InteractiveCanvasAdapter`.
4. Crear un `RenderKit` que use `DiagramDrawingFacade`.
5. Crear `StructurePanel` y `PropertiesPanel` si aplica.
6. Crear un `DiagramCenter` que monte `ZoomableDiagramSurface` + `InteractiveCanvasSurfaceView`.
7. Crear un `WorkbenchContributor` con header, paneles y centro.
8. Hacer que el `EditorView` sea un wrapper fino del `DiagramWorkbenchView`.
9. Agregar prueba de contrato para impedir `new SplitPane`, `InteractiveDiagramCanvasView` viejo o headers locales.

## Estado de migración

Migrados al patrón canónico:

```txt
Mapa de módulos
Flujo de pantallas
Wireframes administrativos
UML Clases
BPMN básico
Flujo operativo
UML Casos de uso
UML Actividad
UML Secuencia
UML Estados
C4 Contexto
C4 Contenedores
Despliegue técnico
```

El modelo conceptual sigue siendo la referencia visual histórica y debe terminar de converger en una tanda posterior más quirúrgica, porque todavía conserva clases grandes (`DiagramCanvasView`, `DiagramCanvasViewModel`) que no conviene tocar de golpe sin romper comportamiento maduro.
