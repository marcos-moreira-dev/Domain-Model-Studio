# PF03 — Contrato de workspace/editor activo y fachadas de acciones

Estado: **ejecutada como planificación**  
Tipo: **solo planificación, sin implementación**  
Prioridad: **crítica**

## 1. Objetivo de esta tanda

Definir un contrato común para los editores/workspaces de Domain Model Studio, de forma que la aplicación deje de depender de cadenas de `if` repartidas entre `MainShellView`, `MainShellCommandHandler`, `MainShellState`, `ActiveOutputResolver` y la toolbar.

Esta tanda no implementa código. Su función es dejar un diseño fino para que la implementación posterior respete:

```txt
principio de responsabilidad única
trazabilidad humana
clases pequeñas y legibles
métodos no gigantes
fachadas/adaptadores simples cuando mejoren lectura
cero parches masivos en MainShellView
```

La regla de producto sigue siendo:

```txt
Cada pestaña activa debe arrastrar su propio workspace, acciones, paneles, estado y exportaciones.
```

## 2. Problema actual que esta tanda ordena

El proyecto tiene editores especializados reales:

```txt
DiagramCanvasView / DiagramCanvasViewModel
DataDictionaryEditorView / DataDictionaryViewModel
ModuleMapEditorView / ModuleMapViewModel
UmlClassDiagramEditorView / UmlClassDiagramViewModel
RolesPermissionsEditorView / RolesPermissionsViewModel
ScreenFlowEditorView / ScreenFlowViewModel
WireframeEditorView / WireframeViewModel
BehaviorDiagramEditorView / BehaviorDiagramViewModel
ArchitectureDiagramEditorView / ArchitectureDiagramViewModel
```

Pero el shell los trata como casos sueltos. La consecuencia es que algunas rutas quedan conectadas y otras no. El caso más evidente es comportamiento:

```txt
BehaviorDiagramViewModel existe.
BehaviorDiagramEditorView existe.
Toolbar de comportamiento existe.
ActiveOutputResolver conoce comportamiento.
MainShellCommandHandler ejecuta acciones de comportamiento.
Pero MainShellView no monta BehaviorDiagramEditorView.
```

Eso demuestra que falta una pieza común que garantice:

```txt
si existe tipo visual disponible → existe workspace registrado → se monta en el centro → sus acciones se enrutan al workspace correcto.
```

## 3. Principio arquitectónico de PF03

No se debe convertir `MainShellView` en una clase todavía más grande. La corrección debe ir hacia este reparto:

```txt
MainShellView
→ compone la ventana y monta un root decidido por otra pieza.

WorkspaceRegistry
→ sabe qué workspace corresponde a cada DiagramTypeId.

WorkspaceRouter
→ resuelve el workspace activo según el tipo de la pestaña activa.

WorkspaceMountPresenter
→ decide cómo montar centro, panel izquierdo y panel derecho.

ActiveWorkspaceFacade
→ expone operaciones comunes del workspace activo.

WorkspaceActionDispatcher
→ recibe acciones globales/contextuales y las dirige al workspace activo.

WorkspaceStatusSummaryProvider
→ genera resumen de estado contextual.

WorkspaceExportPolicy
→ declara formatos exportables reales del workspace activo.
```

Ninguna de esas piezas debe hacer todo. La idea no es crear una arquitectura pesada, sino separar responsabilidades para que sea evidente dónde corregir cada cosa.

## 4. Contrato conceptual `ActiveWorkspace`

Nombre sugerido, no obligatorio:

```java
ActiveWorkspace
```

Responsabilidad: representar el editor real montable de un tipo o familia de tipos.

Contrato conceptual:

```txt
DiagramTypeId diagramTypeId()
Parent root()
boolean supports(DiagramTypeId type)
void activate(DiagramProject project)
void deactivate()
boolean active()
DiagramProject currentProject()
WorkspacePanelPolicy panelPolicy()
WorkspaceStatusSummary statusSummary()
WorkspaceExportSupport exportSupport()
WorkspaceActionSupport actionSupport()
```

No todos los workspaces necesitan implementar todas las operaciones con lógica compleja. Algunos pueden responder “no soportado” de forma honesta.

## 5. Tipos de workspace esperados

### 5.1 ConceptualWorkspace

Cubre:

```txt
CONCEPTUAL_MODEL
```

Responsabilidad:

```txt
mostrar lienzo conceptual;
soportar Chen / pata de gallo;
usar estructura y propiedades genéricas;
soportar SVG, PNG y Markdown;
operar con entidades, atributos y relaciones.
```

Debe ser el único que use lógica de notación Chen/pata de gallo.

### 5.2 DataDictionaryWorkspace

Cubre:

```txt
DATA_DICTIONARY
```

Responsabilidad:

```txt
mostrar documento/diccionario;
soportar entidades y campos del diccionario;
exportar PDF y Markdown;
no prometer lienzo de diagrama si su salida principal es documental/tabular.
```

### 5.3 AdministrativeDiagramWorkspace

Puede ser uno por editor o una familia con adaptadores pequeños. Cubre:

```txt
ADMIN_MODULE_MAP
ROLES_PERMISSIONS_MAP
SCREEN_FLOW
ADMIN_WIREFRAMES
```

Recomendación:

```txt
no meter todos en una clase gigante;
usar adaptadores específicos si comparten contrato.
```

Ejemplo:

```txt
ModuleMapWorkspaceAdapter
RolesPermissionsWorkspaceAdapter
ScreenFlowWorkspaceAdapter
WireframeWorkspaceAdapter
```

### 5.4 UmlClassWorkspace

Cubre:

```txt
UML_CLASS
```

Responsabilidad adicional:

```txt
soportar agrupadores por módulo/directorio/paquete;
conservar coherencia para proyectos de código reales generados desde Markdown.
```

### 5.5 BehaviorWorkspace

Cubre:

```txt
BPMN_BASIC
OPERATIONAL_FLOW
UML_USE_CASE
UML_ACTIVITY
UML_SEQUENCE
UML_STATE
```

Esta familia debe quedar registrada sí o sí, porque es la brecha visual crítica.

Responsabilidad:

```txt
montar BehaviorDiagramEditorView;
cargar BehaviorDiagramDocument según el tipo;
exponer acciones específicas según BehaviorDiagramKind;
validar documento de comportamiento;
exportar PNG y Markdown si el documento está activo.
```

### 5.6 ArchitectureWorkspace

Cubre:

```txt
C4_CONTEXT
C4_CONTAINERS
TECHNICAL_DEPLOYMENT
```

Responsabilidad:

```txt
montar ArchitectureDiagramEditorView;
cambiar herramientas según tipo activo;
exportar PNG y Markdown;
validar nodos y relaciones de arquitectura/despliegue.
```

### 5.7 HomeWorkspace / EmptyWorkspace

La pantalla de inicio no debe fingir ser un proyecto. Debe ser un workspace especial:

```txt
HOME
sin proyecto activo
sin exportación
sin toolbar de diagrama
sin paneles laterales de estructura/propiedades
```

### 5.8 ErrorWorkspace

Si un tipo visible declara salida real pero no tiene workspace registrado, se debe mostrar una pantalla honesta:

```txt
Este tipo de proyecto está declarado como disponible, pero no tiene vista registrada.
```

Esto evita que la app caiga silenciosamente al canvas conceptual o a la pantalla de inicio.

## 6. `WorkspaceRegistry`

Responsabilidad única:

```txt
registrar workspaces y saber qué DiagramTypeId cubre cada uno.
```

Contrato conceptual:

```txt
register(DiagramTypeId type, ActiveWorkspace workspace)
resolve(DiagramTypeId type)
registeredTypes()
missingTypes(Set<DiagramTypeId> expectedTypes)
```

Regla de cierre:

```txt
Todo tipo con SHOW_VISUAL_OUTPUT o SHOW_DOCUMENT_OUTPUT debe aparecer en WorkspaceRegistry.
```

No debe hacer:

```txt
exportar;
validar;
abrir archivos;
crear proyectos;
mostrar alertas;
modificar la sesión activa.
```

## 7. `WorkspaceRouter`

Responsabilidad única:

```txt
recibir el tipo activo y devolver el workspace correcto.
```

Debe ser una pieza pequeña. Puede apoyarse en `WorkspaceRegistry`.

Flujo ideal:

```txt
activeDiagramType cambia
→ WorkspaceRouter.resolve(activeDiagramType)
→ ActiveWorkspaceFacade.activate(workspace, project)
→ WorkspaceMountPresenter monta root y panel policy
```

No debe conocer botones ni menús.

## 8. `ActiveWorkspaceFacade`

Responsabilidad: entregar al shell una API simple del workspace activo, sin que el shell pregunte editor por editor.

Contrato conceptual:

```txt
Optional<ActiveWorkspace> currentWorkspace()
Optional<DiagramProject> currentProject()
DiagramTypeId activeDiagramType()
boolean hasSaveableProject()
WorkspacePanelPolicy currentPanelPolicy()
WorkspaceStatusSummary currentStatus()
WorkspaceExportSupport currentExportSupport()
void activateSession(ProjectSession session)
void clearActiveWorkspace()
```

Uso esperado:

```txt
MainShellCommandHandler.requestSaveProject()
→ activeWorkspaceFacade.currentProject()

MainShellView.refreshWorkAreaPanels()
→ activeWorkspaceFacade.currentWorkspace().root()
→ activeWorkspaceFacade.currentPanelPolicy()
```

Esto reemplaza patrones frágiles como:

```txt
if canvas tiene proyecto...
else if diccionario activo...
else if module map activo...
else if uml class activo...
...
```

## 9. `WorkspaceActionDispatcher`

Responsabilidad: ejecutar acciones del usuario contra el workspace correcto.

Actualmente `MainToolbarViewModel.executeDiagramAction()` tiene un `switch` muy grande y `MainShellCommandHandler` contiene muchas funciones de acción específicas. Eso es entendible por crecimiento incremental, pero para productización se debe evitar que siga creciendo sin control.

Diseño recomendado:

```txt
DiagramToolbarActionId
→ WorkspaceActionDispatcher
→ ActionHandler registrado para workspace activo
→ ViewModel/editor correspondiente
```

Contrato conceptual:

```txt
boolean canExecute(DiagramToolbarActionId actionId)
void execute(DiagramToolbarActionId actionId)
```

Ejemplos:

```txt
ADD_ENTITY → ConceptualWorkspaceActions.addEntity()
ADD_USE_CASE → BehaviorWorkspaceActions.addUseCase()
ADD_C4_CONTAINER → ArchitectureWorkspaceActions.addContainer()
EXPORT_PNG → GlobalExportActions.exportPng()
```

Regla:

```txt
La toolbar muestra acciones.
El dispatcher verifica si el workspace activo realmente las soporta.
```

Así se evita que una acción aparezca activa aunque el editor visual no esté montado.

## 10. Fachadas/adaptadores por familia

No conviene obligar a todos los ViewModel actuales a implementar una interfaz grande. Una vía más segura es crear adaptadores pequeños:

```txt
ConceptualWorkspaceAdapter
DataDictionaryWorkspaceAdapter
ModuleMapWorkspaceAdapter
UmlClassWorkspaceAdapter
RolesPermissionsWorkspaceAdapter
ScreenFlowWorkspaceAdapter
WireframeWorkspaceAdapter
BehaviorWorkspaceAdapter
ArchitectureWorkspaceAdapter
```

Cada adaptador traduce el contrato común hacia el ViewModel existente.

Ventajas:

```txt
no se reescriben todos los ViewModel;
se reduce riesgo de romper editores que ya funcionan;
se concentra el ruteo en clases pequeñas;
se mejora trazabilidad para futuras auditorías.
```

Regla SRP:

```txt
El adaptador no debe convertirse en otro MainShellCommandHandler.
Solo expone lo mínimo necesario del workspace.
```

## 11. Política de paneles `WorkspacePanelPolicy`

Enum conceptual:

```txt
HOME_FULL_CENTER
ERROR_FULL_CENTER
CONCEPTUAL_WITH_GENERIC_SIDE_PANELS
SPECIALIZED_FULL_CENTER
DOCUMENT_FULL_CENTER
CUSTOM_SIDE_PANELS
```

Uso inicial recomendado:

| Tipo | Política inicial |
|---|---|
| Home | `HOME_FULL_CENTER` |
| Error | `ERROR_FULL_CENTER` |
| Modelo conceptual | `CONCEPTUAL_WITH_GENERIC_SIDE_PANELS` |
| Diccionario | `DOCUMENT_FULL_CENTER` |
| Resto visual especializado | `SPECIALIZED_FULL_CENTER` |

Más adelante, UML Clases o Wireframes podrían usar `CUSTOM_SIDE_PANELS`, pero no es necesario para la primera implementación.

## 12. Resumen de estado `WorkspaceStatusSummary`

Contrato conceptual:

```txt
String primaryText()
String secondaryText()
int mainElementCount()
int relationCount()
Optional<String> warningText()
```

Ejemplos:

```txt
Modelo conceptual: 8 entidades / 12 relaciones / Chen.
Diccionario: 8 entidades / 64 campos.
UML Casos de uso: 4 actores / 18 casos / 22 relaciones.
Wireframes: 6 pantallas / 48 componentes.
C4 Contenedores: 5 contenedores / 9 relaciones.
```

PF06 detallará la barra de estado, pero PF03 deja la interfaz común.

## 13. Soporte de exportación `WorkspaceExportSupport`

Contrato conceptual:

```txt
boolean supports(ExportFormat format)
Optional<ExportableOutput> exportableOutput()
Set<ExportFormat> formats()
```

Esto debe converger con `ActiveOutputResolver`. La meta no es duplicar dos sistemas, sino llegar a una única fuente consultable por menú, toolbar y exportación por lote.

PF04 detallará cómo usar esto en menús y toolbars.

## 14. Soporte de acciones `WorkspaceActionSupport`

Contrato conceptual:

```txt
Set<DiagramToolbarActionId> supportedActions()
boolean canExecute(DiagramToolbarActionId actionId)
void execute(DiagramToolbarActionId actionId)
```

Debe poder diferenciar:

```txt
acción no existe para este tipo;
acción existe pero está deshabilitada por falta de selección;
acción existe y se puede ejecutar.
```

Ejemplo:

```txt
ADD_UML_ATTRIBUTE existe para UML Clases, pero puede requerir clase seleccionada.
ADD_USE_CASE no existe para Wireframes.
EXPORT_PNG existe para Wireframes si hay documento activo.
```

## 15. Política de selección

La selección no puede seguir siendo únicamente conceptual. Debe tener una capa común:

```txt
WorkspaceSelectionSummary
- hay selección
- tipo de elemento seleccionado
- puede eliminar
- puede duplicar
- puede centrar
- puede editar propiedades
```

Esto será necesario para:

```txt
Eliminar elemento seleccionado.
Eliminar punto intermedio de línea.
Centrar selección.
Mostrar propiedades.
Habilitar/deshabilitar botones.
```

No se implementa aquí, pero se deja como requisito para la futura tanda de eliminación de puntos intermedios.

## 16. Migración recomendada por pasos

### Paso 1 — Contratos mínimos

Crear clases/enums pequeños:

```txt
ActiveWorkspace
WorkspacePanelPolicy
WorkspaceStatusSummary
WorkspaceExportSupport
WorkspaceActionSupport
WorkspaceRegistry
WorkspaceRouter
```

### Paso 2 — Adaptadores sin cambiar ViewModels

Crear adaptadores para cada familia usando los ViewModel existentes.

Prioridad:

```txt
ConceptualWorkspaceAdapter
BehaviorWorkspaceAdapter
ArchitectureWorkspaceAdapter
DataDictionaryWorkspaceAdapter
ModuleMapWorkspaceAdapter
UmlClassWorkspaceAdapter
RolesPermissionsWorkspaceAdapter
ScreenFlowWorkspaceAdapter
WireframeWorkspaceAdapter
```

### Paso 3 — MainShellView monta por política

`MainShellView` deja de decidir familia por familia y solo pregunta:

```txt
root del workspace activo
panel policy
```

### Paso 4 — Commands consultan fachada

`MainShellCommandHandler` deja de buscar proyecto activo con cadenas largas y usa `ActiveWorkspaceFacade`.

### Paso 5 — Toolbar ejecuta por dispatcher

`MainToolbarViewModel.executeDiagramAction()` se reduce gradualmente.

No hace falta eliminar todo el switch en la primera implementación, pero sí dejar una ruta clara para que no siga creciendo.

## 17. Antipatrones que deben evitarse

### 17.1 Parche directo en `MainShellView`

No hacer únicamente:

```txt
agregar behaviorRoot;
agregar behaviorWorkspaceActive();
agregar otro ternario en refreshWorkAreaPanels();
agregar otro if en genericSidePanelsAvailable().
```

Eso arreglaría el síntoma, pero mantendría la causa.

### 17.2 Mega-fachada

No crear una clase llamada “facade” que haga todo:

```txt
abrir, guardar, validar, exportar, montar UI, parsear Markdown, manejar pestañas y mostrar alertas.
```

La fachada debe ser pequeña y delegar.

### 17.3 Interfaz demasiado grande

No obligar a todos los workspaces a implementar métodos que no aplican. Usar soportes separados:

```txt
ExportSupport
ActionSupport
StatusSupport
PanelPolicy
```

### 17.4 Lenguaje interno visible

El usuario no debe ver términos como:

```txt
workspace adapter
router
viewmodel
renderer
facade
estado interno
```

Esos términos pueden existir en código/documentación técnica, pero no en la interfaz final.

## 18. Criterios de cierre de PF03

La implementación posterior derivada de este plan se considerará correcta cuando:

```txt
MainShellView no tenga listas extensas de tipos para elegir root central.
MainShellCommandHandler no tenga que preguntar editor por editor para obtener proyecto activo.
Todo tipo visual/documental disponible tenga workspace registrado.
La familia Behavior esté montada por contrato, no por parche suelto.
La toolbar ejecute acciones contra el workspace activo o dispatcher.
Los comandos globales puedan consultar capacidades reales del workspace activo.
Las clases nuevas tengan responsabilidades pequeñas y nombres trazables.
```

## 19. Relación con las siguientes tandas

PF04 deberá usar este contrato para alinear:

```txt
menús;
toolbars;
comandos globales;
acciones contextuales;
capacidades reales;
exportaciones visibles.
```

PF05 usará la misma idea para importar Markdown y aplicar layout por tipo. PF06 la usará para estado inferior, estructura y propiedades contextuales.
