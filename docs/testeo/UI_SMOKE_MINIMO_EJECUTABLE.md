# UI Smoke mínimo ejecutable

Estado: **aceptación UI mínima antes de refactor grande**  
Tanda: **28**  
Tipo: **manual formal con ruta automatizable futura**

## Objetivo

Este smoke no intenta probar todos los clics de la aplicación. Su objetivo es cubrir los flujos que pueden romperse si se refactorizan shell, toolbar, SideDock, canvas, exportaciones o ViewModels visuales.

Flujo base:

```txt
abrir app → importar/crear → interactuar → guardar → exportar → reabrir → registrar evidencia
```

## Precondiciones

Antes de iniciar:

```bat
scripts\00-verificar-entorno.bat
scripts\02-ejecutar-tests.bat
```

Luego abrir la app:

```bat
scripts\01-ejecutar-app.bat
```

El reporte debe copiarse desde:

```txt
docs\testeo\reportes\REPORTE_SMOKE_UI_MINIMO.md
```

y guardarse con fecha o nombre de tanda.

## Fakes obligatorios para automatización futura

Cuando este smoke se convierta a UI/E2E con robot JavaFX/TestFX o equivalente, no debe depender de diálogos nativos ni aplicaciones externas reales.

| Servicio | Regla para UI/E2E futuro |
|---|---|
| FileChooser/DirectoryChooser | Usar FileChooser falso o puerto equivalente con rutas temporales deterministas. |
| Confirmaciones/alertas | Usar dialog presenter falso o mecanismo verificable sin depender del sistema operativo. |
| CodeEditorLauncher | Usar CodeEditorLauncher falso; no debe abrir una aplicación real. |
| Exportación PNG/SVG/PDF/Markdown | Escribir a carpeta temporal conocida. |
| Ejemplos oficiales | Usar ejemplos pequeños y deterministas incluidos en el repositorio. |

## Suite mínima obligatoria

### UI-SMOKE-001 — Arranque y home

- [ ] La aplicación abre con `scripts\01-ejecutar-app.bat`.
- [ ] La pantalla inicial no muestra jerga interna como `parser`, `handler`, `provider`, `adapter`, `ViewModel` o `infrastructure`.
- [ ] Menús principales visibles: proyecto/importación/exportación/ayuda o equivalentes de usuario final.
- [ ] La aplicación puede cerrarse sin excepción visible.

### UI-SMOKE-002 — Crear/importar proyecto y pestañas

Usar al menos:

```txt
examples\markdown\diagramas\conceptual_model_colegio_minimo_importable.md
examples\markdown\diagramas\free_graph_minimo.md
```

Validar:

- [ ] El tipo importado se reconoce correctamente.
- [ ] Se abre una pestaña por proyecto.
- [ ] Al cambiar de pestaña, el contenido central cambia.
- [ ] El indicador de cambios pendientes corresponde a la pestaña activa, no a una pestaña anterior.

### UI-SMOKE-003 — Toolbar contextual

Con dos tipos distintos abiertos, por ejemplo Modelo conceptual y Grafo libre:

- [ ] La toolbar contextual cambia según el tipo activo.
- [ ] No aparecen acciones no aplicables al tipo activo.
- [ ] Exportar PDF no aparece para Grafo libre.
- [ ] Acciones de matriz no aparecen para Grafo libre.
- [ ] Acciones de conectores/nodos no aparecen para Roles y permisos.

### UI-SMOKE-004 — SideDock contextual

Usar:

```txt
examples\markdown\diagramas\admin_module_map_restaurante_minimo.md
examples\markdown\diagramas\roles_permissions_optica_minimo.md
examples\markdown\diagramas\admin_wireframes_ventas_minimo.md
```

Validar:

- [ ] El SideDock sigue al tab activo.
- [ ] Mapa de módulos muestra estructura/propiedades de módulos.
- [ ] Roles y permisos se presenta como matriz/documento, no como canvas libre.
- [ ] Wireframes muestra herramientas y propiedades de maqueta.
- [ ] Al volver a otra pestaña, el SideDock no conserva contenido incorrecto del tipo anterior.

### UI-SMOKE-005 — Canvas visual básico

Usar:

```txt
examples\markdown\diagramas\free_graph_minimo.md
```

Validar:

- [ ] Seleccionar nodo.
- [ ] Arrastrar nodo.
- [ ] Seleccionar relación/conector si aplica.
- [ ] Mover etiqueta si aplica.
- [ ] Usar zoom con rueda.
- [ ] Usar paneo.
- [ ] Centrar o ajustar contenido deja el grafo visible.

### UI-SMOKE-006 — Wireframes y resize

Usar:

```txt
examples\markdown\diagramas\admin_wireframes_ventas_minimo.md
```

Validar:

- [ ] Los botones/campos/tablas son figuras simuladas, no controles reales funcionales.
- [ ] Seleccionar componente.
- [ ] Redimensionar componente.
- [ ] Duplicar o agregar un componente si la acción está visible.
- [ ] Ajustar pantalla al contenido no pierde elementos fuera del viewport.

### UI-SMOKE-007 — Guardar/reabrir `.dms`

Para al menos dos tipos visuales y un tipo documental/tabular:

```txt
free-graph
admin-wireframes
roles-permissions-map
```

Validar:

- [ ] Guardar `.dms`.
- [ ] Cerrar proyecto.
- [ ] Reabrir `.dms`.
- [ ] El tipo se conserva.
- [ ] El contenido principal se conserva.
- [ ] Las posiciones visuales se conservan en diagramas visuales.
- [ ] Roles/permisos conserva roles, permisos y decisiones de matriz.

### UI-SMOKE-008 — Exportación PNG/SVG/Markdown/PDF

Validar:

- [ ] Exportar Markdown de un tipo visual.
- [ ] Exportar SVG de un tipo visual.
- [ ] Exportar PNG de un tipo visual escribiendo nombre sin extensión; debe terminar como `.png`.
- [ ] El PNG no sale blanco, vacío, recortado ni con toolbar/paneles.
- [ ] El SVG no contiene `<image` ni `data:image` como sustituto del diagrama vectorial.
- [ ] Confirmar que SVG se entiende como vectorial documental, no WYSIWYG universal.
- [ ] Registrar dimensiones extremas o legibilidad deficiente en PNG/SVG.
- [ ] Exportar PDF solo desde Diccionario de datos.
- [ ] El PDF del diccionario tiene portada/contenido legible.

### UI-SMOKE-009 — UML Clases desde código fuente

Usar un proyecto pequeño Java o TypeScript controlado.

Validar:

- [ ] Importar directorio fuente.
- [ ] La vista previa no bloquea la UI.
- [ ] Se genera proyecto UML Clases.
- [ ] Aparece al menos una vista interna: Resumen/Backend/Frontend/Integración API según aplique.
- [ ] La búsqueda localiza una clase por nombre.
- [ ] Abrir archivo fuente usa launcher falso en UI/E2E futuro; en smoke manual no debe bloquear el flujo.
- [ ] Guardar/reabrir `.dms` conserva clases, módulos y vistas.

### UI-SMOKE-010 — Ayuda y recursos IA

Validar:

- [ ] Abrir Guía académica.
- [ ] Buscar un tema.
- [ ] Abrir ayuda contextual de un tipo, si aplica.
- [ ] Las figuras didácticas se ven.
- [ ] Exportar recursos IA.
- [ ] La carpeta exportada incluye gramáticas, plantillas y ejemplos oficiales.

### UI-SMOKE-011 — Exportación por lote

Abrir al menos tres proyectos de tipos distintos:

```txt
conceptual-model
free-graph
data-dictionary
```

Validar:

- [ ] Ejecutar exportación por lote.
- [ ] Se crean carpetas `input/`, `editable/`, `output/`.
- [ ] Se exporta `.dms` por proyecto.
- [ ] Se exporta Markdown donde aplique.
- [ ] Se exporta SVG/PNG según capacidades reales.
- [ ] El tab activo se restaura o queda en un estado comprensible.

### UI-SMOKE-012 — Levantamiento lógico documental

Usar:

```txt
src\main\resources\ai-resources\official-markdown\levantamiento-logico\logical_business_intake_optica_gordito.md
```

Validar:

- [ ] El tipo `logical-business-intake` abre como expediente documental, no como canvas visual.
- [ ] Estructura muestra árbol navegable con orden natural.
- [ ] Workspace muestra formulario editable con contenido importado desde Markdown.
- [ ] Los elementos vinculados son navegables.
- [ ] Validación, trazabilidad, derivaciones y ayuda operativa funcionan desde SideDock.
- [ ] Exportar Markdown conserva cambios hechos desde controles.
- [ ] Al editar un elemento lógico, la pestaña queda marcada con `*`.
- [ ] Cerrar con cambios sin guardar muestra advertencia.
- [ ] Guardar y reabrir `.dms` conserva el documento lógico.


## Criterio de aprobación

La Tanda UI queda aprobada cuando:

```txt
UI-SMOKE-001 a UI-SMOKE-012 están completados o tienen hallazgo registrado.
No hay fallos bloqueantes sin clasificar.
Las exportaciones visuales principales se abrieron manualmente.
El reporte queda adjunto en docs/testeo/reportes/ o en un issue equivalente.
```

## Relación con refactoring

Antes de tocar zonas rojas del diagnóstico de refactoring, al menos deben estar aprobados:

```txt
UI-SMOKE-002
UI-SMOKE-003
UI-SMOKE-004
UI-SMOKE-005
UI-SMOKE-007
UI-SMOKE-008
```

Zonas rojas protegidas por este smoke:

```txt
MainShellCommandHandler
MainShellView
InteractiveCanvasSurfaceView
UmlClassDiagramViewModel
ModuleMapViewModel
WireframeViewModel
SpecializedVisualSvgWriter
ClientBatchExportCoordinator
```
