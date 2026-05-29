# Plan de alineación UX/UI, canvas y exportación

Estado: **plan recomendado**  
Prioridad: **bloqueante antes de implementar diagramas masivos**

## 1. Objetivo

Alinear el programa para que los tipos de proyecto que prometen diagramas funcionen como diagramas editables y exportables, reutilizando la infraestructura madura del modelo conceptual sin duplicar vistas enormes.

## 2. Orden recomendado

```text
UI-01: Normalizar tokens visuales y fondo de workbench.
UI-02: Separar formalmente herramienta / estructura / propiedades.
UI-03: Crear ScrollableEditorTabBarView.
UI-04: Convertir toolbar contextual de acciones simples a controles contextuales.
UI-05: Retirar clic derecho de menús contextuales del canvas.
UI-06: Exponer eliminar punto intermedio desde toolbar + Suprimir.
UI-07: Crear contrato de canvas común reutilizable.
UI-08: Adaptar modelo conceptual como cliente del canvas común.
UI-09: Migrar mapa de módulos.
UI-10: Migrar flujo de pantallas.
UI-11: Migrar wireframes.
UI-12: Migrar UML/C4/BPMN por familias.
UI-13: Implementar SVG real por familia.
UI-14: Smoke test por tipo con imagen/exportación.
```

## 3. Clases nuevas sugeridas

```text
presentation/shell/tabs/ScrollableEditorTabBarView.java
presentation/toolbar/DiagramToolbarControl.java
presentation/toolbar/DiagramToolbarControlType.java
presentation/toolbar/DiagramToolbarControlProvider.java
presentation/interactivecanvas/InteractiveCanvasView.java
presentation/interactivecanvas/InteractiveCanvasViewport.java
presentation/interactivecanvas/CanvasSelectionController.java
presentation/interactivecanvas/CanvasPanZoomController.java
presentation/interactivecanvas/CanvasNodeDragController.java
presentation/interactivecanvas/CanvasConnectorInteractionController.java
presentation/interactivecanvas/CanvasExportSurface.java
presentation/interactivecanvas/CanvasToolMode.java
presentation/interactivecanvas/CanvasDiagramAdapter.java
```

## 4. Contratos por superficie

### Toolbar contextual

```text
Crea y ejecuta herramientas.
No conoce detalles del modelo interno.
Pregunta al ViewModel qué acciones están habilitadas.
Permite botones, toggles y combos.
```

### Panel izquierdo

```text
Navega estructura.
No crea elementos salvo casos muy justificados.
No reemplaza la toolbar.
```

### Panel derecho

```text
Edita propiedades.
Permite aplicar cambios.
Muestra tablas auxiliares de relaciones/asignaciones.
```

### Canvas

```text
Dibuja y permite interacción visual.
No parsea Markdown.
No guarda archivos directamente.
No contiene reglas específicas de todos los tipos.
```

### Exportadores

```text
PNG puede usar snapshot controlado.
SVG debe usar modelo + layout.
Markdown debe usar exportador textual por tipo.
```

## 5. Migración por prioridad

| Prioridad | Tipo | Motivo |
|---|---|---|
| 1 | Modelo conceptual | Es la referencia funcional actual. |
| 2 | Mapa de módulos | Diagrama de nodos/conectores más simple para validar canvas común. |
| 3 | Flujo de pantallas | Similar a mapa, pero orientado a navegación. |
| 4 | Wireframes | Requiere maquetas y componentes movibles. |
| 5 | UML clases / casos de uso / estados / actividad | Requieren respetar teoría de UML. |
| 6 | BPMN / flujo operativo | Requieren carriles y conectores limpios. |
| 7 | C4 / despliegue | Requieren agrupación, límites y relaciones arquitectónicas. |
| 8 | UML secuencia | Requiere editor especializado por líneas de vida y tiempo vertical. |
| 9 | Roles/permisos | Mantener matriz estructurada, no canvas libre. |
| 10 | Diccionario | Mantener documento/tabla, no canvas libre. |

## 6. Criterio anti-fachada

No se puede marcar un tipo como completo si no cumple:

```text
Crear desde toolbar.
Seleccionar desde canvas y paneles.
Mover elementos visuales cuando sea diagrama.
Editar propiedades.
Guardar y reabrir sin perder layout.
Exportar PNG.
Exportar SVG si el tipo se vende como diagrama vectorial.
Validar.
Probar con ejemplo UENS gordito.
```

## 7. Cierre de UX/UI

La app debe sentirse como una herramienta de escritorio sobria:

```text
inicio claro;
tabs escrolleables;
toolbars contextuales escrolleables;
paneles laterales como estructura/propiedades;
canvas central como espacio real de edición;
statusbar informativa;
colores sólidos y consistentes;
exportación fiel a lo visible.
```
