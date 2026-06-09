# Auditoría UX/UI — Inconsistencias por vista

Estado: **diagnóstico**  
Prioridad: **alta**  
Base: capturas de ejemplos UENS y lectura estática de código.

## 1. Hallazgo global

El producto ya tiene una estructura visual entendible:

```text
toolbar superior + toolbar contextual + tabs + panel izquierdo + centro + panel derecho + statusbar
```

La inconsistencia principal es que algunas vistas se ven como diagramas, pero no se comportan todavía como editores de diagramas completos.

## 2. Modelo conceptual / casos de uso

Estado visual: **referencia a imitar**.

Fortalezas:

```text
Paleta sobria.
Figuras simples.
Bordes claros.
Selección visible.
Interacción más madura.
Exportación SVG/PNG más cercana a producto real.
```

Criterio:

```text
Debe ser la referencia estética y funcional para los demás diagramas.
```

Deuda:

```text
La interacción madura está demasiado concentrada en DiagramCanvasView.
IMP-01 corrigió el conflicto: el clic derecho ya no abre menú de punto intermedio y queda reservado para paneo.
```

## 3. Mapa de módulos

Código revisado:

```text
presentation/modulemap/ModuleMapEditorView.java
```

Estado actual:

```text
Usa Pane + ScrollPane + tarjetas + líneas.
Exporta PNG por snapshot.
No usa canvas común.
No hay zoom/paneo común.
No hay drag persistente de módulos.
No hay SVG real.
```

Inconsistencia UX:

```text
Visualmente promete diagrama editable, pero opera más como render con selección.
```

Acción recomendada:

```text
Migrarlo primero al canvas común.
Debe ser el primer cliente posterior al modelo conceptual.
```

## 4. Flujo de pantallas

Código revisado:

```text
presentation/screenflow/ScreenFlowEditorView.java
```

Estado actual:

```text
Usa Pane + ScrollPane.
Muestra pantallas y transiciones.
Exporta PNG por snapshot.
No hay drag persistente de pantallas.
No hay zoom/paneo común.
No hay SVG real.
```

Inconsistencia UX:

```text
El panel derecho contiene relaciones y propiedades correctamente, pero el canvas central aún no actúa como lienzo interactivo pleno.
```

Acción recomendada:

```text
Usar el mismo contrato de nodos/conectores del canvas común.
```

## 5. Wireframes administrativos

Código revisado:

```text
presentation/wireframe/WireframeEditorView.java
```

Fortalezas:

```text
Es la vista especializada más clara visualmente.
Tiene pantallas, componentes, propiedades y listado.
El enfoque de geometrías simples es correcto.
```

Inconsistencias:

```text
El panel izquierdo mezcla estructura con herramienta de plantilla.
Las pantallas/componentes no se mueven como objetos de canvas.
El layout está basado en orden horizontal automático.
No hay SVG real.
```

Acción recomendada:

```text
Mover "Insertar plantilla" a toolbar contextual.
Dejar el panel izquierdo solo como lista/estructura de pantallas.
Permitir mover pantallas y, por etapa posterior, componentes.
```

## 6. UML clases

Código revisado:

```text
presentation/umlclass/UmlClassDiagramEditorView.java
```

Estado actual:

```text
Tiene módulos, clases, miembros y relaciones.
Visualmente ya se acerca a UML estructural.
No debe mezclarse con modelo conceptual ER.
```

Deuda probable:

```text
Necesita layout manual persistente por clase/módulo.
Necesita SVG real propio.
Debe conservar agrupación por módulo/carpeta.
```

Acción recomendada:

```text
Migrarlo al canvas común después de mapa de módulos y flujo de pantallas.
```

## 7. BPMN, flujo operativo, UML actividad, UML estados, UML casos de uso

Código revisado:

```text
presentation/behavior/BehaviorDiagramEditorView.java
```

Estado actual:

```text
Un solo editor genérico renderiza varios tipos de comportamiento.
Usa Pane + ScrollPane + nodos + líneas.
Exporta PNG por snapshot.
```

Inconsistencia UX:

```text
Compartir editor es útil, pero no debe borrar la teoría visual de cada notación.
BPMN necesita inicio/actividad/decisión/fin/carril.
UML estados necesita estados iniciales/finales/transiciones.
Casos de uso necesita actores, sistema y óvalos.
Actividad necesita flujo y decisiones.
Secuencia no encaja bien en este mismo render genérico.
```

Acción recomendada:

```text
Mantener infraestructura común, pero usar renderers/adapters por tipo.
UML secuencia debe separarse como editor especializado temporal.
```

## 8. C4 y despliegue técnico

Código revisado:

```text
presentation/architecture/ArchitectureDiagramEditorView.java
```

Estado actual:

```text
Un solo editor para C4 contexto, C4 contenedores y despliegue.
Usa columnas por tipo de nodo.
Exporta PNG por snapshot.
No hay drag persistente ni SVG real.
```

Inconsistencia UX:

```text
La columna automática ayuda, pero el usuario necesita mover recuadros y ordenar relaciones manualmente.
```

Acción recomendada:

```text
Mantener reglas de agrupación como auto-layout inicial.
Luego permitir mover nodos y guardar layout visual.
```

## 9. Roles y permisos

Código revisado:

```text
presentation/rolespermissions/RolesPermissionsEditorView.java
```

Estado correcto:

```text
No debe convertirse en canvas libre.
Es una matriz estructurada exportable.
```

Inconsistencia visual:

```text
El fondo gris/café observado en captura reduce limpieza visual.
La matriz puede mantenerse, pero debe usar tokens sobrios compartidos.
```

Acción recomendada:

```text
Mantenerlo como editor estructurado.
Mejorar paleta, bordes, contraste y exportación visual de matriz.
```

## 10. Diccionario de datos

Código revisado:

```text
presentation/datadictionary/DataDictionaryEditorView.java
```

Estado correcto:

```text
No es diagrama.
Debe ser documento/tabla estructurada.
```

Inconsistencia técnica visual:

```text
Usa token no estándar: -app-workspace-bg.
```

Acción recomendada:

```text
Normalizar tokens.
No prometer canvas ni SVG de diagrama.
Exportar Markdown/PDF/documento.
```

## 11. Pestañas de proyecto

Código revisado:

```text
MainShellView.buildEditorTabBar()
```

Estado actual:

```text
La barra de tabs es un HBox manual.
No está envuelta en ScrollPane.
```

Problema:

```text
Con muchos proyectos abiertos, las pestañas pueden desbordarse o quedar incómodas.
```

Acción recomendada:

```text
Crear ScrollableEditorTabBarView.
Usar ScrollPane horizontal sin barras invasivas, similar a la toolbar contextual.
Soportar rueda horizontal/vertical para desplazamiento de tabs.
```

## 12. Toolbar contextual

Código revisado:

```text
ContextualToolbarView
DefaultDiagramToolbarActionProvider
DiagramToolbarSection
```

Fortaleza:

```text
Ya existe toolbar contextual por tipo.
Ya es escrolleable por filas.
Ya tiene secciones.
```

Deuda:

```text
Las acciones son solo botones/toggles.
No existe todavía un control contextual tipo ComboBox para plantillas o modos.
VIEW está tratado como conceptual-only por política.
Cuando el canvas común exista, zoom/fit/centrar deben exponerse a todos los diagramas canvas.
```

Acción recomendada:

```text
Evolucionar de DiagramToolbarAction a DiagramToolbarControl.
Soportar Button, Toggle, ChoiceAction y Separator.
Agregar sección Plantillas o Modo cuando aplique.
```
