# I13-O — Auditoría técnica de canvas, exportación SVG e interacción

Estado: **pendiente / auditoría previa a implementación visual**  
Tipo: **nota técnica de alineación**

## 1. Hallazgos principales

### Modelo conceptual

`presentation/canvas/DiagramCanvasView.java` concentra la interacción más madura:

```text
- ScrollEvent.SCROLL para zoom.
- MouseButton.SECONDARY para paneo.
- MouseButton.PRIMARY sobre fondo para selección rectangular.
- Drag de nodos seleccionados.
- Puntos intermedios en conectores.
- Selección de puntos intermedios.
- Exportación PNG registrada desde CanvasPngExporter.
```

Problema detectado:

```text
También existe menú contextual de clic derecho para eliminar punto intermedio.
```

Eso debe corregirse porque el clic derecho queda reservado para paneo.

### Editores especializados

Clases revisadas:

```text
ModuleMapEditorView
ScreenFlowEditorView
WireframeEditorView
BehaviorDiagramEditorView
ArchitectureDiagramEditorView
UmlClassDiagramEditorView
RolesPermissionsEditorView
```

Patrón observado:

```text
Pane + ScrollPane + renderDiagram() + JavaFX Nodes + snapshot PNG
```

Eso permite una visualización bonita, pero no equivale todavía a un canvas editable común.

### Exportación activa

`ActiveOutputResolver` ya resuelve salida activa por tipo, pero SVG está restringido en la práctica al modelo conceptual. El resto de diagramas visuales se registra principalmente como PNG/Markdown.

## 2. Correcciones requeridas

```text
1. Crear acción de toolbar para eliminar punto intermedio seleccionado.
2. Retirar menú contextual de clic derecho para puntos intermedios.
3. Mantener Suprimir/Backspace como atajo aceptable.
4. Migrar editores visuales a una infraestructura de canvas común.
5. Crear adaptadores SVG por familia de diagrama.
6. Hacer que los tabs del área de trabajo sean escrolleables.
7. Mantener diccionario como documento y roles/permisos como matriz estructurada.
```

## 3. Criterio técnico

La solución correcta no es convertir todo en `DiagramCanvasView`, sino extraer contratos:

```text
InteractiveDiagramCanvas
CanvasPanZoomController
CanvasSelectionController
CanvasNodeDragController
CanvasLayerRegistry
CanvasNodeAdapter
CanvasConnectorAdapter
CanvasExportSurface
SvgExportSurface
```

Cada familia de diagrama debe mapear su documento semántico hacia estos contratos.
