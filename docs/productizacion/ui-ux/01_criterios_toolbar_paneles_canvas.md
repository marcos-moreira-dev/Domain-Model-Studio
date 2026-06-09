# UX/UI — Criterios para toolbar, paneles y canvas

Estado: **criterio de producto**  
Prioridad: **alta / transversal**  
Base: revisión de capturas, `DefaultDiagramToolbarActionProvider`, `ContextualToolbarView`, `MainShellView`, workspaces visuales/documentales y editores especializados.

## 1. Principio de separación

Domain Model Studio debe separar las superficies de trabajo así:

```text
Menú superior      = comandos globales de aplicación.
Toolbar global     = proyecto, entrada, edición general y exportación frecuente.
Toolbar contextual = acciones operativas del artefacto activo.
SideDock/sidebar   = estructura, árbol, lista, navegación, propiedades, inspección y ayuda contextual.
Workspace          = resultado principal del artefacto activo: canvas, documento, matriz, ficha o vista exportable.
Statusbar          = estado, contexto, conteos, zoom, cambios sin guardar y ayuda breve.
```

La toolbar debe contener acciones. El SideDock debe contener orientación, estructura, inspección y configuración puntual. El workspace debe portar el resultado principal.

## 2. Qué debe ir en toolbar contextual

Van en toolbar contextual las acciones que crean, conectan, transforman, validan, derivan, cambian vista o exportan el artefacto activo.

Ejemplos:

```text
Agregar nodo / elemento.
Agregar relación / conector.
Eliminar elemento.
Eliminar punto intermedio seleccionado.
Duplicar elemento.
Cambiar notación cuando aplique.
Reorganizar / auto-layout.
Zoom, ajustar, centrar.
Validar.
Exportar PNG/SVG/Markdown.
Insertar plantilla cuando la plantilla cree elementos reales en el canvas.
```

## 3. Qué debe ir en SideDock / sidebar

El SideDock no debe comportarse como caja de herramientas principal. Su función es navegar, seleccionar estructura, inspeccionar, editar propiedades puntuales y mostrar ayuda contextual.

Debe contener:

```text
Árbol del modelo.
Lista de entidades, pantallas, módulos, roles o estados.
Agrupadores por módulo/carpeta/carril cuando aplique.
Filtros simples de navegación.
Resumen estructural del documento.
```

Puede contener selectores auxiliares solo si no ejecutan una acción destructiva o creativa directamente. Si un control crea elementos nuevos, debe revisarse para moverlo a toolbar contextual.

## 4. Qué debe ir en el inspector contextual

El inspector contextual pertenece preferentemente al SideDock como módulo alternable, no como tercera columna fija que comprima el workspace. Debe contener:

```text
Propiedades del elemento seleccionado.
Propiedades de la relación seleccionada.
Tablas de relaciones o asignaciones.
Campos de color, borde, etiqueta, descripción y notas.
Botones de aplicar cambios de propiedades.
```

Los botones `Aplicar` son aceptables dentro del módulo de propiedades del SideDock porque pertenecen a edición fina. No deben reemplazar acciones de creación como `Agregar pantalla`, `Agregar módulo` o `Agregar transición`.

## 5. Qué debe pasar en el workspace

El workspace debe ser el lugar del resultado principal. En diagramas, ese resultado es el canvas de edición visual real. En documentos o matrices, puede ser una ficha, tabla, expediente o vista documental amplia.

Interacción obligatoria en workspaces visuales tipo diagrama/canvas:

```text
Clic izquierdo sobre elemento       = seleccionar.
Clic izquierdo + arrastre elemento  = mover.
Clic izquierdo en fondo + arrastre  = selección rectangular.
Scroll                              = zoom in / zoom out.
Clic derecho + arrastre             = paneo.
Suprimir / Backspace                = eliminar selección cuando el foco esté en canvas.
Toolbar                             = acciones explícitas delicadas.
```

## 6. Clic derecho

El clic derecho queda reservado para paneo. No debe abrir menú contextual principal del diagrama.

Estado tras IMP-01:

```text
DiagramCanvasView ya no instala menú contextual para eliminar puntos intermedios.
El tooltip ya no menciona clic derecho.
Eliminar punto intermedio se hace con toolbar contextual y Suprimir/Backspace.
El doble clic sobre punto intermedio ya no elimina el punto.
```

## 7. Plantillas

Las plantillas de wireframes o ejemplos oficiales no son simples propiedades. Si insertan pantallas/componentes, son herramientas de creación.

Criterio recomendado:

```text
Selector de plantilla: puede vivir en toolbar contextual como ComboBox/menú desplegable.
Acción Insertar plantilla: debe vivir en toolbar contextual.
Panel izquierdo: queda solo para lista de pantallas/estructura.
```

Esto evita que el panel izquierdo mezcle navegación con herramientas.

## 8. Exportación

Todo tipo vendido como diagrama debe tener:

```text
PNG por snapshot o superficie de exportación controlada.
SVG por exportador vectorial del modelo, no por captura raster.
Markdown cuando el tipo tenga contrato textual.
```

PNG puede salir del árbol visual de JavaFX. SVG no debe depender de una captura de pantalla; debe generarse desde el modelo semántico + layout visual persistente.

## 9. Canvas JavaFX vs dibujo vectorial

No es obligatorio usar `javafx.scene.canvas.Canvas` pixel a pixel. Para esta app conviene el modo retenido de JavaFX:

```text
Pane / Group / Region / Shape / Line / Path / Label
```

Eso permite seleccionar, mover, aplicar CSS y exportar PNG fácilmente. La clave es que el SVG se genere desde un modelo vectorial propio y no desde una imagen.

## 10. Criterio de cierre

Una vista está alineada cuando se puede responder sí a esto:

```text
¿Las herramientas están en toolbar?
¿El SideDock se limita a estructura, propiedades, inspección y ayuda?
¿El workspace porta el resultado principal sin botoneras redundantes?
¿El canvas permite mover y seleccionar cuando el artefacto es visual?
¿La interacción coincide con el modelo conceptual?
¿El resultado exportado coincide con lo visible?
¿SVG existe si la app promete SVG para ese tipo?
```
