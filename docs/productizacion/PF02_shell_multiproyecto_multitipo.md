# PF02 — Diseño del shell multiproyecto/multitipo por pestaña activa

Estado: **ejecutada como planificación**  
Tipo: **solo planificación, sin implementación**  
Prioridad: **crítica**

## 1. Objetivo de esta tanda

Diseñar cómo debe comportarse el shell de Domain Model Studio cuando existan varias pestañas abiertas de distintos tipos. Esta tanda no implementa código; define el contrato para que la implementación posterior no se resuelva agregando más `if` dentro de `MainShellView` o `MainShellCommandHandler`.

Regla central:

```txt
La pestaña activa manda.
```

Eso significa:

```txt
pestaña activa
→ sesión activa
→ proyecto activo
→ tipo de diagrama activo
→ workspace/editor activo
→ toolbar específica activa
→ paneles contextuales activos
→ comandos globales habilitados
→ exportaciones disponibles
→ resumen de estado inferior
```

## 2. Problema actual

El código ya tiene pestañas y sesiones, pero el ruteo visual todavía está repartido.

Evidencias:

```txt
MainShellCommandHandler.activateProjectSession() activa sesiones.
MainShellCommandHandler.showProjectInEditor() carga el ViewModel especializado correcto.
MainShellState.activeDiagramTypeProperty() publica el tipo activo.
MainToolbarViewModel cambia acciones según tipo activo.
MainShellView.refreshWorkAreaPanels() decide qué root visual mostrar.
```

La falla aparece porque esas piezas no están obligadas por un contrato común. Ejemplo real:

```txt
UML Casos de uso
→ MainShellCommandHandler carga behaviorDiagramViewModel
→ Toolbar muestra acciones de casos de uso
→ ActiveOutputResolver conoce salida de comportamiento
→ PERO MainShellView no monta BehaviorDiagramEditorView
→ pantalla central queda en inicio/canvas conceptual
```

Conclusión:

```txt
No basta con activar el ViewModel. El shell debe montar el workspace correspondiente al tipo activo.
```

## 3. Principios de diseño

### 3.1 Barra superior fija

La barra superior general se mantiene estable para operaciones transversales:

```txt
Archivo
Editar
Vista
Exportar
Ayuda
Nuevo proyecto
Abrir
Guardar
Cerrar
Importar
Ejemplos
Recursos IA
```

Pero cada acción debe resolverse contra la sesión activa, no contra un editor global.

### 3.2 Toolbars específicas por tipo

Las toolbars contextuales inferiores o secundarias dependen del tipo de la pestaña activa:

```txt
Modelo conceptual → Entidad, Atributo, Relación, Chen, Pata de gallo.
Diccionario → Entidad, Campo, PDF, Markdown.
UML Casos de uso → Actor, Caso, Sistema, Asociación, Include, Extend, Generalización.
BPMN → Inicio, Actividad, Decisión, Fin, Carril, Flujo.
Wireframes → Pantalla, Panel, Campo, Botón, Tabla, Modal, Tarjeta, Filtro.
C4 → Persona, Sistema, Contenedor, Base de datos, Servicio externo, Relación.
```

### 3.3 Un proyecto abierto no implica un único tipo global

El estado global `projectOpen` solo responde si hay una sesión activa guardable. No debe usarse como sustituto de `activeWorkspace`.

Regla:

```txt
projectOpen = hay algo guardable activo
activeDiagramType = tipo de la sesión activa
activeWorkspace = editor real montado para ese tipo
```

### 3.4 No usar el canvas conceptual como fallback silencioso

El canvas conceptual solo debe mostrarse si el tipo activo es `CONCEPTUAL_MODEL`.

Si un tipo visual no tiene workspace registrado, el shell debe mostrar una pantalla de error honesta de producto, por ejemplo:

```txt
Este tipo está declarado como disponible, pero no tiene vista registrada. Revisa la planificación de productización.
```

No debe caer al inicio ni al canvas conceptual.

## 4. Contrato propuesto de sesión activa

La sesión activa debe representar una pestaña abierta. Conceptualmente, `ProjectSession` debe poder responder:

```txt
id de pestaña
título visible
dirty/sin guardar
si es home, placeholder o proyecto real
proyecto actual
DiagramTypeId
workspaceKey esperado
```

La pestaña de inicio no es proyecto. Debe tener su propio estado:

```txt
HOME_TAB
sin proyecto activo
sin toolbar específica de diagrama
sin exportación de proyecto
pantalla de bienvenida
```

Los placeholders solo deben existir para tipos realmente en preparación. Si el catálogo declara `AVAILABLE + SHOW_VISUAL_OUTPUT`, no debe abrir placeholder.

## 5. Contrato propuesto de workspace activo

Se recomienda introducir un contrato pequeño, no una mega-clase. Nombre sugerido:

```txt
ActiveWorkspace
```

Responsabilidades sugeridas:

```txt
DiagramTypeId diagramTypeId()
Parent root()
boolean active()
void loadProject(DiagramProject project)
void clear()
DiagramProject currentProject()
WorkspaceOutputKind outputKind()
WorkspacePanelPolicy panelPolicy()
WorkspaceStatusSummary statusSummary()
WorkspaceExportSupport exportSupport()
```

No todos los métodos tienen que llamarse exactamente así. La idea es evitar que `MainShellView` y `MainShellCommandHandler` conozcan a mano cada editor.

### 5.1 Tipos de salida

```txt
VISUAL_DIAGRAM
DOCUMENT_VIEW
HOME
PLANNING_VIEW
ERROR_VIEW
```

### 5.2 Política de paneles

```txt
CONCEPTUAL_WITH_SIDE_PANELS
SPECIALIZED_FULL_CENTER
DOCUMENT_FULL_CENTER
HOME_FULL_CENTER
ERROR_FULL_CENTER
```

Ejemplos:

```txt
Modelo conceptual → panel izquierdo + centro + panel derecho.
Diccionario → centro documental propio.
Mapa de módulos → centro visual propio.
UML Clases → centro visual propio.
Comportamiento → centro visual propio.
Arquitectura → centro visual propio.
```

## 6. Ruteador de workspaces

Se recomienda introducir una pieza pequeña:

```txt
WorkspaceRouter
```

Responsabilidad única:

```txt
Recibe un DiagramTypeId y devuelve el workspace/editor que debe montarse.
```

No debe guardar proyectos, no debe exportar, no debe validar. Solo enruta.

Mapa conceptual esperado:

| DiagramTypeId | Workspace esperado |
|---|---|
| `conceptual-model` | ConceptualWorkspace / canvas actual |
| `data-dictionary` | DataDictionaryWorkspace |
| `admin-module-map` | ModuleMapWorkspace |
| `roles-permissions-map` | RolesPermissionsWorkspace |
| `screen-flow` | ScreenFlowWorkspace |
| `admin-wireframes` | WireframeWorkspace |
| `uml-class` | UmlClassWorkspace |
| `bpmn-basic` | BehaviorWorkspace |
| `operational-flow` | BehaviorWorkspace |
| `uml-use-case` | BehaviorWorkspace |
| `uml-activity` | BehaviorWorkspace |
| `uml-sequence` | BehaviorWorkspace |
| `uml-state` | BehaviorWorkspace |
| `c4-context` | ArchitectureWorkspace |
| `c4-containers` | ArchitectureWorkspace |
| `technical-deployment` | ArchitectureWorkspace |

Regla de seguridad:

```txt
Todo tipo con SHOW_VISUAL_OUTPUT o SHOW_DOCUMENT_OUTPUT debe tener workspace registrado.
```

## 7. Composición visual recomendada

### 7.1 `MainShellView`

Debe quedarse como compositor de alto nivel:

```txt
construir menú
construir barra superior
construir área central
construir statusbar
montar root del workspace activo
mostrar/ocultar paneles según WorkspacePanelPolicy
```

No debería decidir a mano:

```txt
si el tipo es UML_CLASS entonces root X
si el tipo es C4 entonces root Y
si el tipo es wireframes entonces root Z
```

Esa decisión debe delegarse al ruteador.

### 7.2 Montaje del centro

Flujo deseado:

```txt
activeDiagramType cambia
→ WorkspaceRouter.resolve(activeDiagramType)
→ WorkspaceMountPresenter calcula root + panelPolicy
→ MainShellView actualiza SplitPane
```

Esto evita olvidar una familia como ocurrió con comportamiento.

### 7.3 Paneles laterales

La regla actual `genericSidePanelsAvailable()` debe reemplazarse por una política:

```txt
WorkspacePanelPolicy.SHOW_GENERIC_SIDE_PANELS
WorkspacePanelPolicy.HIDE_GENERIC_SIDE_PANELS
WorkspacePanelPolicy.CUSTOM_SIDE_PANELS
```

En la versión inmediata se puede usar solo:

```txt
Conceptual → SHOW_GENERIC_SIDE_PANELS
Todos los especializados → HIDE_GENERIC_SIDE_PANELS
```

Pero el diseño deja espacio para que en el futuro UML Clases o Wireframes tengan panel contextual propio.

## 8. Activación de pestañas

### 8.1 Al crear proyecto

Flujo recomendado:

```txt
Usuario elige tipo
→ CreateWorkspaceUseCase confirma PRODUCT_VIEW
→ ProjectFactory crea DiagramProject correcto
→ ProjectSession se agrega a colección
→ se activa pestaña
→ WorkspaceRouter resuelve workspace
→ workspace.loadProject(project)
→ toolbar específica se actualiza
→ estado inferior se actualiza
```

### 8.2 Al cambiar de pestaña

Flujo recomendado:

```txt
Usuario hace clic en pestaña
→ shell identifica ProjectSession
→ limpia workspace anterior sin destruir su proyecto
→ carga proyecto de sesión en workspace correcto
→ activeDiagramType se actualiza desde el proyecto
→ toolbar se recalcula
→ panel policy se aplica
→ estado contextual se muestra
```

### 8.3 Al cerrar pestaña

Flujo recomendado:

```txt
Confirmar si hay cambios sin guardar
→ remover ProjectSession
→ si era activa, activar otra sesión o home
→ limpiar workspace visual anterior
→ actualizar toolbar, estado y exportaciones
```

## 9. Fuente de verdad del proyecto activo

El proyecto activo no debe depender de preguntar editor por editor en cada comando.

Estado actual aproximado:

```txt
currentProjectForSaving() revisa canvas, diccionario, module map, UML, roles, screen flow, wireframe, behavior, architecture.
```

Eso funciona, pero es frágil.

Diseño recomendado:

```txt
ActiveWorkspaceFacade.currentProject()
```

La sesión activa guarda la versión canónica para la pestaña. El workspace modifica y notifica cambios; la sesión se actualiza. Los comandos consultan la sesión activa o el workspace activo, no una cadena de `if`.

## 10. Relación con `MainToolbarViewModel`

`MainToolbarViewModel` puede seguir pidiendo acciones a `DefaultDiagramToolbarActionProvider`, pero su `activeDiagramTypeProperty` debe provenir de la sesión activa.

Regla:

```txt
La toolbar no decide el tipo.
La toolbar refleja el tipo activo.
```

Cuando la pestaña activa cambia:

```txt
MainShellState.activeDiagramTypeProperty = session.project.metadata().diagramTypeId()
MainToolbarViewModel actualiza acciones contextuales
```

## 11. Relación con exportaciones

PF02 solo define la dependencia. PF04 deberá detallar menús/exportaciones.

Regla aquí:

```txt
Exportar debe consultar el workspace activo y las capacidades del tipo activo.
```

No debe repetirse una lista en:

```txt
MainShellView.pngExportUnavailable()
MainShellView.markdownExportUnavailable()
ActiveOutputResolver
DefaultDiagramCapabilityCatalog
```

## 12. Relación con ayuda

La ayuda debe poder abrirse desde la barra superior fija. Más adelante, PF07 debe permitir que la ayuda detecte el tipo activo y muestre una ficha contextual.

Ejemplo:

```txt
Si la pestaña activa es UML Casos de uso, F1 abre centro de ayuda con foco en UML Casos de uso.
```

No se implementa en PF02, solo se reserva el diseño.

## 13. Conexión de comportamiento sin parche gigante

La implementación posterior debe corregir el bug de comportamiento, pero evitando este patrón:

```txt
agregar import
agregar Parent behaviorRoot
agregar behaviorWorkspaceActive()
agregar otro else-if en refreshWorkAreaPanels()
agregar otro if en genericSidePanelsAvailable()
agregar otro if en productWorkspaceMessage()
```

Ese parche resolvería el síntoma inmediato, pero mantendría la causa.

Mejor implementación mínima alineada con PF02:

```txt
1. Crear un registro central de workspaces.
2. Registrar BehaviorWorkspace junto con los demás especializados.
3. Hacer que MainShellView monte por router.
4. Agregar una prueba que falle si un tipo visual no tiene workspace registrado.
```

Si por urgencia se hace primero el parche directo, debe quedar como paso temporal y luego extraerse a router en la misma fase de implementación, no dejarlo como deuda nueva.

## 14. Responsabilidades propuestas

### 14.1 `WorkspaceRouter`

```txt
Entrada: DiagramTypeId.
Salida: WorkspaceRegistration / ActiveWorkspace.
No maneja UI compleja ni persistencia.
```

### 14.2 `WorkspaceRegistry`

```txt
Contiene los workspaces registrados.
Permite validar que todos los tipos disponibles tengan workspace.
```

### 14.3 `WorkspaceMountPresenter`

```txt
Decide cómo montar root, paneles laterales y tabs colapsadas.
Recibe WorkspacePanelPolicy.
```

### 14.4 `WorkspaceStatusSummaryProvider`

```txt
Convierte el proyecto/documento activo en texto de statusbar.
Ejemplo: 4 roles / 18 permisos / 32 asignaciones.
```

### 14.5 `WorkspaceActionDispatcher`

```txt
Recibe acciones globales: validar, eliminar, ajustar vista, exportar, etc.
Las dirige al workspace activo.
```

### 14.6 `WorkspaceExportPolicy`

```txt
Une capacidades declaradas con output real disponible.
Evita que el menú prometa formatos no soportados.
```

## 15. Plan de implementación derivado de PF02

Esta tanda no implementa, pero deja el orden recomendado:

1. Crear pruebas de registro de workspaces por tipo disponible.
2. Crear un registro/ruteador pequeño de workspaces.
3. Extraer de `MainShellView` las decisiones de tipo hacia el ruteador.
4. Registrar conceptual, diccionario, administrativos, UML clases, comportamiento y arquitectura.
5. Montar `BehaviorDiagramEditorView` a través de ese registro.
6. Sustituir `genericSidePanelsAvailable()` por política de paneles.
7. Cambiar la activación de pestañas para refrescar workspace, toolbar, paneles y estado desde la sesión activa.
8. Dejar `MainShellView` como compositor, no como catálogo de tipos.

## 16. Riesgos

### 16.1 Sobreingeniería

No crear una arquitectura enorme. El objetivo no es rehacer toda la app, sino eliminar el punto frágil que hizo fallar comportamiento.

### 16.2 Parche acumulativo

No seguir agregando `else if` por cada familia. Eso resolvería hoy, pero volvería a fallar cuando se agregue otro tipo.

### 16.3 Sesión activa vs ViewModel activo

No asumir que un ViewModel activo equivale a workspace visible. Ambos deben sincronizarse mediante contrato.

### 16.4 Estado global contaminado

No usar `projectOpen` como sustituto de tipo activo, workspace activo o exportaciones activas.

## 17. Criterio de cierre de PF02

PF02 queda cerrada cuando la implementación futura puede responder estas preguntas sin improvisar:

```txt
¿Quién decide qué editor se monta para cada tipo?
¿Quién garantiza que todo tipo disponible tenga workspace registrado?
¿Quién decide si aparecen paneles laterales?
¿De dónde sale el tipo activo cuando cambio de pestaña?
¿Quién recibe validar/exportar/eliminar según el workspace activo?
¿Cómo se evita volver a caer al canvas conceptual por accidente?
```

