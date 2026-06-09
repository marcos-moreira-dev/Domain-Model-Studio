# Matriz de casos de uso, trazabilidad y verificación anti-fachada

Estado: **documento de control funcional**  
Propósito: **corroborar que cada característica visible de Domain Model Studio tenga implementación real y no sea una fachada**  
Alcance: **producto escritorio JavaFX, importación Markdown, edición por tipo, persistencia `.dms`, exportación y ayuda integrada**  
Última lectura: **inspección estática del repositorio + revisión visual de pantallas recientes**

> Este documento no reemplaza los tests automáticos ni el smoke test manual. Su función es servir como mapa humano: cada promesa visible debe apuntar a dominio, caso de uso, vista, persistencia/exportación y prueba verificable.

> Aclaración: una `Facade` como patrón de diseño puede ser útil en el código si coordina subsistemas sin esconder deuda. En este documento “anti-fachada” significa evitar **utilidades visibles de la herramienta** que aparenten estar terminadas sin dominio, caso de uso, persistencia, exportación o prueba real.

---

## 1. Regla anti-fachada

Una característica visible **no se considera real** solo porque exista un botón, menú, texto, icono o pantalla.

Para marcarla como implementada debe cumplir, según aplique:

1. **Promesa visible clara**: el texto de UI dice exactamente lo que el usuario puede hacer.
2. **Ruta de entrada real**: menú, toolbar, diálogo, pestaña o acción disponible en el tipo correcto.
3. **Modelo de dominio o documento**: existe una estructura semántica donde guardar el resultado, no solo nodos JavaFX dibujados.
4. **Caso de uso de aplicación**: la edición o consulta pasa por `application/*UseCase` cuando modifica reglas de producto.
5. **Vista especializada o canvas adecuado**: el tipo abre el workspace correcto, no una pantalla genérica engañosa.
6. **Persistencia `.dms`**: guardar y abrir conserva el tipo y su contenido principal.
7. **Exportación coherente**: solo se exportan formatos realmente soportados por la salida activa.
8. **Validación o feedback**: hay validación, estado inferior o mensaje claro cuando algo falta.
9. **Prueba automática o smoke manual**: existe una forma concreta de corroborarlo.
10. **Trazas internas humana**: se puede ubicar qué clase, documento o test sostiene la característica.

Estados usados en este documento:

| Estado | Significado |
|---|---|
| **Real por código** | Hay dominio/casos de uso/vista/exportación o persistencia localizable por inspección. Falta ejecutar tests en este entorno. |
| **Parcial** | Hay base funcional, pero falta interacción visual, validación, exportación, persistencia o ergonomía para que cumpla totalmente la promesa. |
| **Planificado** | Está documentado o diseñado, pero no debe venderse como disponible. |
| **No aplica** | La característica no corresponde a ese tipo de proyecto. |

---

## 2. Contratos globales que sostienen el producto

| Contrato | Promesa | Anclaje actual | Criterio anti-fachada |
|---|---|---|---|
| Catálogo oficial de tipos | La app conoce todos los tipos visibles y sus nombres de producto. | `DefaultDiagramTypeRegistry`, `DiagramTypeId`, `DiagramTypeDescriptor` | Todo tipo seleccionable debe existir aquí y no en textos sueltos. |
| Capacidades reales | La app muestra importación/exportación/ayuda solo cuando el tipo lo soporta. | `DefaultDiagramCapabilityCatalog`, `DiagramCapabilityPresentationPolicy` | El menú o toolbar no debe ofrecer formatos inexistentes. |
| Ruteo por workspace | Cada tipo abre la familia visual correcta. | `WorkspaceTypeRoutingPolicy`, `WorkspaceRouteResolver`, `WorkspaceViewRegistry`, `DefaultWorkspaceDescriptorCatalog` | Ningún tipo especializado debe caer accidentalmente en el canvas conceptual. |
| Salida activa exportable | Exportar usa el proyecto o documento activo, no el primero abierto. | `ActiveOutputResolver`, `ExportableOutput`, `ExportCommandHandler` | PNG/Markdown/PDF/SVG deben salir del workspace activo correcto. |
| Shell multiproyecto | Cada pestaña conserva tipo, estado sucio y salida. | `MainShellCommandHandler`, `MainShellState`, `EditorTabViewState`, `SpecializedWorkspaceCoordinator` | Cambiar pestaña no debe mezclar modelo, toolbar ni exportación. |
| Importación Markdown | El archivo declara o permite inferir su tipo y se delega al parser correcto. | `DiagramMarkdownImportDispatcher`, parsers especializados | Un Markdown de BPMN, C4 o wireframe no debe importarse como conceptual. |
| Persistencia `.dms` | El archivo guarda el tipo y el documento especializado. | `OpenProjectUseCase`, `SaveProjectUseCase`, `DmsProjectJsonReader`, `DmsProjectJsonWriter` | Reabrir no debe perder documento, tipo, relaciones ni contenido editado. |
| Ayuda teórica | La ayuda explica los tipos sin revelar infraestructura interna. | `ManualDialog`, `ManualContent`, `DefaultTheoryCatalog`, `docs/teoria/*` | La ayuda no debe sustituir al editor real ni prometer funciones no disponibles. |
| Ejemplos oficiales | El selector abre ejemplos importables y marca referencias documentales. | `DefaultOfficialExampleCatalog`, `OfficialExampleSelectorDialog` | Los ejemplos no importables deben deshabilitar apertura o explicar la razón. |
| Recursos IA | La app entrega gramáticas/plantillas para generar Markdown compatible. | `ExportAiResourcesUseCase`, `src/main/resources/ai-resources/*` | Los recursos deben coincidir con IDs y gramáticas que el parser reconoce. |

---

## 3. Matriz global de casos de uso

### 3.1 Proyecto, shell y sesión

| ID | Caso de uso | Usuario quiere | Entrada UI | Anclaje técnico | Verificación mínima | Estado |
|---|---|---|---|---|---|---|
| UC-SHELL-01 | Crear proyecto nuevo por tipo | Elegir un tipo oficial y abrir una pestaña editable. | `Archivo > Nuevo` / botón `Nuevo` | `requestNewProject`, `CreateWorkspaceUseCase`, `WorkspaceTypeRoutingPolicy` | Crear uno de cada tipo y verificar título, toolbar y workspace. | Real por código |
| UC-SHELL-02 | Abrir proyecto `.dms` | Recuperar un proyecto guardado. | `Abrir` | `requestOpenProject`, `OpenProjectUseCase`, `openLoadedProjectInNewTab` | Abrir `.dms` de cada familia y verificar tipo/contenido. | Real por código |
| UC-SHELL-03 | Guardar proyecto `.dms` | Persistir el proyecto activo. | `Guardar` | `requestSaveProject`, `SaveProjectUseCase`, `currentProjectForSaving` | Guardar, cerrar, reabrir y comparar contenido. | Real por código |
| UC-SHELL-04 | Cerrar pestaña/proyecto | Cerrar sin perder cambios por accidente. | `Cerrar`, cerrar tab | `requestCloseProject`, `canCloseApplication`, estado dirty | Cambios sin guardar deben marcar `*` y pedir confirmación. | Real por código |
| UC-SHELL-05 | Cambiar entre pestañas de tipos distintos | Trabajar varios proyectos sin contaminación. | Tabs superiores | `projectSessions`, `activateProjectSession`, `SpecializedWorkspaceCoordinator` | Abrir wireframe + C4 + conceptual y verificar toolbar/status/exportación al cambiar. | Real por código / requiere smoke |
| UC-SHELL-06 | Deshacer / rehacer | Revertir cambios de edición. | Botones `Deshacer`, `Rehacer` | `requestUndo`, `requestRedo`, `DiagramCanvasViewModel` | Probar en modelo conceptual. En especializados no asumir si no hay historial conectado. | Parcial |
| UC-SHELL-07 | Barra inferior contextual | Saber qué tipo y estado está activo. | Status bar | `StatusBarViewModel`, `ProjectStatusSummaryFormatter` | Cambiar tipo y verificar resumen correcto. | Real por código |
| UC-SHELL-08 | Toolbar contextual | Ver herramientas del tipo activo. | Toolbar superior específica | `DefaultDiagramToolbarActionProvider`, `ContextualToolbarView` | Cada tipo debe mostrar solo acciones válidas. | Real por código / requiere smoke |
| UC-SHELL-09 | Pantalla de inicio | Orientar al usuario al abrir la app. | Tab `Pantalla de inicio` | `WelcomeWorkspaceView` | No debe sustituir diagramas ni mostrar jerga interna. | Real por código / ergonomía mejorable |

### 3.2 Entrada, ejemplos y recursos

| ID | Caso de uso | Usuario quiere | Entrada UI | Anclaje técnico | Verificación mínima | Estado |
|---|---|---|---|---|---|---|
| UC-IN-01 | Importar Markdown estructurado | Convertir Markdown oficial en proyecto editable. | `Importar` | `ImportCommandHandler`, `ImportMarkdownModelUseCase`, `DiagramMarkdownImportDispatcher` | Importar un ejemplo por tipo y revisar workspace correcto. | Real por código |
| UC-IN-02 | Rechazar Markdown ambiguo o inválido | Evitar que un archivo errado cree un proyecto falso. | `Importar` | `MarkdownModelParsingException`, `invalidos/*` | Probar frontmatter inválido, tipo desconocido y secciones incompatibles. | Real por código |
| UC-IN-03 | Preparar layout al importar conceptual | Ver diagrama Chen/pata de gallo con posiciones iniciales. | `Importar` | `ImportedProjectVisualPreparationUseCase`, `GenerateInitialChenLayoutUseCase`, `GenerateInitialCrowsFootLayoutUseCase` | Importar conceptual y verificar nodos visibles. | Real por código |
| UC-IN-04 | Importar documentos especializados sin tratarlos como conceptual | Preservar módulo, C4, BPMN, etc. | `Importar` | Parsers especializados: `ModuleMapMarkdownParser`, `BehaviorMarkdownParser`, `ArchitectureMarkdownParser`, etc. | Importar BPMN y verificar que abre `BehaviorDiagramEditorView`, no `DiagramCanvasView`. | Real por código |
| UC-IN-05 | Abrir ejemplo oficial importable | Cargar una referencia de UENS/colegio. | `Ejemplo` | `DefaultOfficialExampleCatalog`, `OfficialExampleSelectorDialog` | Seleccionar ejemplos importables y verificar apertura. | Real por código |
| UC-IN-06 | Diferenciar ejemplos importables y referenciales | Evitar promesas falsas al abrir ejemplos. | `Ejemplo` | `OfficialExampleDescriptor.importable` derivado de `DefaultDiagramTypeDefinitions` | Diccionario UENS es importable desde T01; cualquier ejemplo futuro no importable debe explicarlo. | Real por código |
| UC-IN-07 | Exportar recursos IA | Copiar gramáticas y plantillas para pedir Markdown a una IA. | `Recursos IA` | `ExportAiResourcesUseCase`, `ClasspathAiResourceCatalog` | Exportar carpeta y revisar IDs `diagram_type`. | Real por código |

### 3.3 Exportación y entrega

| ID | Caso de uso | Usuario quiere | Entrada UI | Anclaje técnico | Verificación mínima | Estado |
|---|---|---|---|---|---|---|
| UC-EXP-01 | Exportar PNG del visual activo | Obtener imagen del diagrama/matriz/maqueta activa. | `PNG` | `ExportCommandHandler`, `ActiveOutputResolver`, `exportVisualAsPng` por ViewModel | Abrir cada tipo visual y exportar PNG. | Real por código / requiere smoke visual |
| UC-EXP-02 | Exportar SVG conceptual | Obtener SVG del modelo conceptual. | `SVG` | `ExportSvgUseCase`, `SvgDiagramExporter` | SVG solo debe estar disponible para conceptual. | Real por código |
| UC-EXP-03 | Exportar Markdown actualizado | Obtener Markdown desde el modelo/documento editado. | `Markdown` | `ExportMarkdownUseCase`, exporters especializados | Editar algo, exportar y verificar que el cambio sale. | Real por código / requiere smoke por tipo |
| UC-EXP-04 | Exportar PDF documental | Generar documento técnico imprimible. | `PDF` | `ExportPdfUseCase`, `PdfDiagramExporter` | Diccionario y Levantamiento lógico exportan PDF; diagramas visuales no deben mostrar PDF. | Real por código |
| UC-EXP-05 | Exportar proyectos abiertos para cliente | Entregar lote con input/editable/output/manifiesto. | `Exportar abiertos` | `ExportOpenProjectsForClientUseCase`, `ClientBatchExportService`, `FileSystemClientBatchExporter` | Abrir varios tipos y revisar carpeta generada. | Real por código / requiere smoke |
| UC-EXP-06 | Bloquear formatos inexistentes | Evitar botones que no hacen nada. | Toolbar/menú exportar | `ExportableOutputDescriptor.supports`, `activeOutputFor` | En C4 no debe salir SVG; en conceptual no debe salir PDF. | Real por código |

### 3.4 Validación, ayuda y cierre funcional

| ID | Caso de uso | Usuario quiere | Entrada UI | Anclaje técnico | Verificación mínima | Estado |
|---|---|---|---|---|---|---|
| UC-VAL-01 | Validar modelo conceptual | Detectar errores de entidades, relaciones y cardinalidades. | `Validar` | `ValidateProjectUseCase`, `DiagramProjectValidator` | Crear relación inválida y revisar mensaje. | Real por código |
| UC-VAL-02 | Validar documentos especializados | Detectar elementos huérfanos o relaciones inválidas. | `Validar` por tipo | `Validate*UseCase` de cada familia | Eliminar destino de relación y validar. | Real por código / requiere smoke |
| UC-HELP-01 | Abrir manual integrado | Consultar teoría y criterios de cada tipo. | `Ayuda` | `ManualDialog`, `ManualContent`, `docs/teoria/*` | Abrir ficha de varios tipos y revisar lenguaje de producto. | Real por código |
| UC-HELP-02 | Mostrar figuras didácticas | Entender geometrías básicas. | Manual | `ManualFigureNodeFactory` | Ver figuras sin depender de imágenes externas. | Real por código |
| UC-REL-01 | Validar release manualmente | Corroborar que el producto cumple lo que promete. | Scripts/checklists | `docs/testeo/checklists/smoke_ui_mvp.md`, `scripts/*validar*` | Ejecutar checklist completo en Windows con Java/Maven. | Parcial en este entorno: Maven no disponible |

---

## 4. Matriz por tipo de diagrama visible

### 4.1 Modelo conceptual

Promesa visible: **representar entidades, atributos, relaciones y cardinalidades del dominio en Chen o pata de gallo**.

| Caso | Debe cumplir | Anclaje | Verificación anti-fachada | Estado |
|---|---|---|---|---|
| CON-01 Crear proyecto | Crear proyecto conceptual vacío. | `DiagramProject.blank`, `requestNewProject` | Aparece canvas conceptual, panel de estructura y propiedades. | Real por código |
| CON-02 Importar Markdown legacy/oficial | Leer entidades, atributos, relaciones y cardinalidades. | `MarkdownDiagramParser`, `DiagramMarkdownImportDispatcher` | Importar `conceptual_model_uens_gordito_importable.md`. | Real por código |
| CON-03 Cambiar notación | Alternar Chen/pata de gallo. | `SwitchNotationUseCase`, `requestSwitchNotation` | Cambiar vista y conservar semántica. | Real por código |
| CON-04 Editar manualmente | Agregar entidad, atributo, relación; duplicar y eliminar. | `AddEntityUseCase`, `AddAttributeUseCase`, `AddRelationshipUseCase`, `DuplicateEntityUseCase`, `RemoveDiagramElementUseCase` | Usar toolbar y confirmar árbol/inspector actualizados. | Real por código |
| CON-05 Mover elementos | Cambiar posición visual persistente. | `MoveElementUseCase`, `UpdateNodeLayoutUseCase`, `DiagramLayout` | Mover, guardar `.dms`, reabrir. | Real por código |
| CON-06 Editar líneas | Agregar/mover/eliminar puntos intermedios. | `AddBendPointUseCase`, `MoveBendPointUseCase`, `RemoveBendPointUseCase`, `ConnectorBendPointSelection` | Crear punto, eliminar solo el punto, no la relación. | Real por código |
| CON-07 Zoom/paneo/selección | Navegar diagramas grandes. | `DiagramCanvasView`, `DiagramCanvasViewModel`, `ViewCommandHandler` | Zoom, paneo con botón derecho, selección múltiple. | Real por código |
| CON-08 Exportar SVG/PNG/Markdown | Generar entregables. | `ExportSvgUseCase`, `CanvasPngExporter`, `ExportMarkdownUseCase` | Exportar los tres formatos desde el proyecto activo. | Real por código |
| CON-09 Validar | Detectar inconsistencias semánticas. | `ValidateProjectUseCase` | Validación con errores/advertencias visibles. | Real por código |

Criterio especial: este es el **canvas más maduro** y debe servir como referencia para extraer infraestructura común, no como clase a copiar completa.

---

### 4.2 Diccionario de datos

Promesa visible: **documentar entidades, campos, tipos, restricciones, reglas, responsables y observaciones**.

| Caso | Debe cumplir | Anclaje | Verificación anti-fachada | Estado |
|---|---|---|---|---|
| DIC-01 Crear diccionario | Crear documento estructurado vacío. | `CreateDataDictionaryUseCase`, `DataDictionaryDocument` | Nuevo proyecto abre vista documental, no canvas falso. | Real por código |
| DIC-02 Agregar entidad | Añadir entidad lógica/documental. | `AddDataDictionaryEntityUseCase` | Entidad aparece en lista y propiedades. | Real por código |
| DIC-03 Agregar campo | Añadir campo a entidad seleccionada. | `AddDataDictionaryFieldUseCase` | Campo conserva tipo, restricciones y observaciones. | Real por código |
| DIC-04 Editar entidad/campo | Cambiar nombre, tipo, regla, visibilidad, restricciones. | `UpdateDataDictionaryEntityUseCase`, `UpdateDataDictionaryFieldUseCase` | Exportar Markdown/PDF y ver cambios. | Real por código |
| DIC-05 Eliminar entidad/campo | Quitar elemento sin romper documento. | `RemoveDataDictionaryItemUseCase` | Eliminar y validar. | Real por código |
| DIC-06 Exportar Markdown | Generar documento técnico editable. | `ExportDataDictionaryMarkdownUseCase`, `DataDictionaryMarkdownExporter` | Markdown contiene campos actualizados. | Real por código |
| DIC-07 Exportar PDF | Generar PDF técnico. | `ExportPdfUseCase`, `DataDictionaryPdfExporter` | PDF disponible para documentos que lo declaran. | Real por código |
| DIC-08 Importar ejemplo oficial | Cargar diccionario desde selector. | `DefaultOfficialExampleCatalog` | Desde T01 UENS se declara importable como documento editable. | Funcional; requiere smoke manual |

Criterio especial: **no debe forzarse a canvas libre**. Si el usuario ve matriz/fichas/documento, eso sí corresponde a su promesa.

---

### 4.3 Mapa de módulos

Promesa visible: **representar módulos funcionales, submódulos, responsabilidades y dependencias de una aplicación administrativa**.

| Caso | Debe cumplir | Anclaje | Verificación anti-fachada | Estado |
|---|---|---|---|---|
| MOD-01 Crear mapa | Crear documento de módulos. | `CreateModuleMapUseCase`, `ModuleMapDocument` | Nuevo proyecto abre vista visual de módulos. | Real por código |
| MOD-02 Importar Markdown | Leer módulos y dependencias. | `ModuleMapMarkdownParser` | Importar `admin_module_map_uens_gordito.md`. | Real por código |
| MOD-03 Agregar módulo/submódulo | Añadir nodos funcionales. | `AddModuleMapModuleUseCase` | Toolbar agrega módulo visible y editable. | Real por código |
| MOD-04 Agregar dependencia | Conectar módulos. | `AddModuleMapDependencyUseCase` | Relación aparece en tabla y visual. | Real por código |
| MOD-05 Editar propiedades | Cambiar nombre, tipo, estado, responsabilidad. | `UpdateModuleMapModuleUseCase`, `UpdateModuleMapDependencyUseCase` | Cambios visibles en panel y exportación. | Real por código |
| MOD-06 Eliminar | Borrar módulo o dependencia seleccionada. | `RemoveModuleMapItemUseCase` | No quedan relaciones huérfanas. | Real por código |
| MOD-07 Validar | Revisar módulos/dependencias inconsistentes. | `ValidateModuleMapUseCase` | Validación advierte vacíos o huérfanos. | Real por código |
| MOD-08 Exportar PNG/Markdown | Obtener diagrama y documento actualizado. | `ModuleMapViewModel.exportVisualAsPng`, `ExportMarkdownUseCase` | Exportar desde pestaña activa. | Real por código / requiere smoke visual |
| MOD-09 Lienzo interactivo maduro | Mover nodos, persistir layout, zoom/paneo común. | Plan AV-I03 | Debe reutilizar canvas común futuro. | Parcial |

---

### 4.4 Roles y permisos

Promesa visible: **representar roles, permisos, acciones y límites operativos como matriz de autorización**.

| Caso | Debe cumplir | Anclaje | Verificación anti-fachada | Estado |
|---|---|---|---|---|
| ROL-01 Crear matriz | Crear documento de roles/permisos. | `CreateRolesPermissionsUseCase`, `RolesPermissionsDocument` | Abre matriz, no canvas artificial. | Real por código |
| ROL-02 Importar Markdown | Leer roles, permisos y asignaciones. | `RolesPermissionsMarkdownParser` | Importar `roles_permissions_uens_gordito.md`. | Real por código |
| ROL-03 Agregar rol | Añadir rol operativo. | `AddRoleUseCase` | Rol aparece como fila/matriz. | Real por código |
| ROL-04 Agregar permiso | Añadir permiso funcional. | `AddPermissionUseCase` | Permiso aparece como columna o lista. | Real por código |
| ROL-05 Asignar permiso | Marcar rol-permiso. | `AddPermissionAssignmentUseCase`, `UpdatePermissionAssignmentUseCase` | Check aparece en matriz y exportación. | Real por código |
| ROL-06 Editar rol/permiso/asignación | Cambiar estado, descripción, alcance o notas. | `UpdateRoleUseCase`, `UpdatePermissionUseCase` | Panel derecho modifica documento. | Real por código |
| ROL-07 Eliminar | Borrar rol, permiso o asignación. | `RemoveRolesPermissionsItemUseCase` | Matriz se actualiza sin restos. | Real por código |
| ROL-08 Validar | Detectar roles sin permisos o asignaciones inconsistentes. | `ValidateRolesPermissionsUseCase` | Validar documento incompleto. | Real por código |
| ROL-09 Exportar PNG/Markdown | Entregar matriz y documento. | `RolesPermissionsViewModel.exportVisualAsPng` | Exportar PNG de matriz. | Real por código / requiere smoke |

Criterio especial: es correcto que sea **editor estructurado tipo matriz**, no lienzo libre.

---

### 4.5 Flujo de pantallas

Promesa visible: **representar navegación entre pantallas de una aplicación administrativa**.

| Caso | Debe cumplir | Anclaje | Verificación anti-fachada | Estado |
|---|---|---|---|---|
| SCR-01 Crear flujo | Crear documento de pantallas. | `CreateScreenFlowUseCase`, `ScreenFlowDocument` | Abre vista visual de pantallas. | Real por código |
| SCR-02 Importar Markdown | Leer pantallas y transiciones. | `ScreenFlowMarkdownParser` | Importar `screen_flow_uens_gordito.md`. | Real por código |
| SCR-03 Agregar pantalla | Crear nodo de pantalla. | `AddScreenUseCase` | Pantalla aparece en estructura y vista. | Real por código |
| SCR-04 Agregar transición | Conectar pantallas. | `AddScreenTransitionUseCase` | Relación aparece visual y en tabla. | Real por código |
| SCR-05 Editar pantalla/transición | Cambiar módulo, ruta, propósito, trigger o condición. | `UpdateScreenUseCase`, `UpdateScreenTransitionUseCase` | Cambios se reflejan y exportan. | Real por código |
| SCR-06 Eliminar | Quitar pantalla o transición. | `RemoveScreenFlowItemUseCase` | Validar que no queden transiciones inválidas. | Real por código |
| SCR-07 Validar | Detectar pantallas sin relaciones o transiciones rotas. | `ValidateScreenFlowUseCase` | Probar varias pantallas sin transición. | Real por código |
| SCR-08 Exportar PNG/Markdown | Entregar flujo navegacional. | `ScreenFlowViewModel.exportVisualAsPng` | Exportar desde tab activo. | Real por código / requiere smoke |
| SCR-09 Interacción visual común | Mover pantallas, persistir layout y mejorar ruteo. | Plan AV-I04 | No debe depender solo de layout automático. | Parcial |

---

### 4.6 Wireframes administrativos

Promesa visible: **crear maquetas estructurales con geometrías simples: pantallas, secciones, formularios, tablas, campos, botones, alertas y estados**.

| Caso | Debe cumplir | Anclaje | Verificación anti-fachada | Estado |
|---|---|---|---|---|
| WIR-01 Crear wireframes | Crear documento de maquetas. | `CreateWireframeUseCase`, `WireframeDocument` | Abre vista con lista de pantallas, canvas/área visual y propiedades. | Real por código |
| WIR-02 Importar Markdown | Leer pantallas y componentes. | `WireframeMarkdownParser` | Importar `admin_wireframes_uens_gordito.md`. | Real por código |
| WIR-03 Agregar pantalla | Crear pantalla de maqueta. | `AddWireframeScreenUseCase` | Pantalla aparece a la izquierda y en vista. | Real por código |
| WIR-04 Agregar componentes | Insertar sección, formulario, tabla, campo, botón. | `AddWireframeComponentUseCase`, `WireframeComponentKind` | Cada componente se dibuja con geometría simple. | Real por código |
| WIR-05 Aplicar plantilla | Insertar estructura base de pantalla. | `ApplyWireframeTemplateUseCase` | Plantillas como Login/Listado CRUD/Dashboard deben insertar componentes. | Real por código |
| WIR-06 Editar pantalla/componente | Cambiar nombre, módulo, propósito, binding, comportamiento, notas. | `UpdateWireframeScreenUseCase`, `UpdateWireframeComponentUseCase` | Panel derecho actualiza visual y exportación. | Real por código |
| WIR-07 Eliminar | Borrar pantalla o componente. | `RemoveWireframeItemUseCase` | No quedan componentes en pantalla inexistente. | Real por código |
| WIR-08 Validar | Detectar pantallas vacías o componentes huérfanos. | `ValidateWireframeUseCase` | Crear pantalla sin componentes y validar. | Real por código |
| WIR-09 Exportar PNG/Markdown | Entregar maqueta y documento. | `WireframeViewModel.exportVisualAsPng` | PNG debe mostrar pantallas y componentes. | Real por código / requiere smoke |
| WIR-10 No prometer Figma | Mantenerlo como scaffolding administrativo. | `docs/teoria/wireframes_administrativos.md`, textos UI | No debe prometer diseño pixel-perfect. | Real por código / texto debe cuidarse |

---

### 4.7 UML Clases

Promesa visible: **representar clases, interfaces, enums, miembros y relaciones estructurales agrupadas por módulo/carpeta**.

| Caso | Debe cumplir | Anclaje | Verificación anti-fachada | Estado |
|---|---|---|---|---|
| UMLC-01 Crear diagrama | Crear documento UML Clases. | `CreateUmlClassDiagramUseCase`, `UmlClassDiagramDocument` | Abre editor UML Clases. | Real por código |
| UMLC-02 Importar Markdown | Leer módulos, clases, miembros y relaciones. | `UmlClassMarkdownParser` | Importar `uml_class_uens_gordito.md`. | Real por código |
| UMLC-03 Agregar módulo | Crear agrupador lógico. | `AddUmlModuleUseCase` | Módulo aparece como grupo. | Real por código |
| UMLC-04 Agregar clase/interfaz/enum | Crear nodo UML. | `AddUmlClassUseCase`, `UmlClassKind` | Tipo visual correcto según clase/interfaz/enum. | Real por código |
| UMLC-05 Agregar atributo/método | Añadir miembros. | `AddUmlMemberUseCase`, `UmlMemberKind` | Miembros aparecen en clase y exportación. | Real por código |
| UMLC-06 Agregar relación | Conectar clases. | `AddUmlRelationUseCase`, `UmlRelationKind` | Relación aparece visual y documental. | Real por código |
| UMLC-07 Editar/eliminar | Modificar elementos y miembros. | `UpdateUml*UseCase`, `RemoveUmlClassDiagramItemUseCase` | Cambios conservados en `.dms`. | Real por código |
| UMLC-08 Validar | Detectar relaciones inválidas o clases inconsistentes. | `ValidateUmlClassDiagramUseCase` | Crear relación con destino inexistente y validar. | Real por código |
| UMLC-09 Exportar PNG/Markdown | Entregar diagrama/documento. | `UmlClassDiagramViewModel.exportVisualAsPng` | Exportar desde pestaña activa. | Real por código / requiere smoke |
| UMLC-10 Layout agrupado serio | Mantener módulos visualmente separados en diagramas grandes. | Plan AV-I05 | No debe quedar como cajas cruzadas sin agrupación clara. | Parcial |

---

### 4.8 BPMN básico

Promesa visible: **representar procesos de negocio con eventos, actividades, decisiones, carriles y flujos**.

| Caso | Debe cumplir | Anclaje | Verificación anti-fachada | Estado |
|---|---|---|---|---|
| BPMN-01 Crear BPMN | Crear documento de comportamiento tipo BPMN. | `CreateBehaviorDiagramUseCase`, `BehaviorDiagramKind.BPMN_BASIC` | Abre `BehaviorDiagramEditorView` con toolbar BPMN. | Real por código |
| BPMN-02 Importar Markdown | Leer proceso y flujos. | `BehaviorMarkdownParser` | Importar `bpmn_basic_matricula_uens_gordito.md`. | Real por código |
| BPMN-03 Agregar inicio/actividad/decisión/fin/carril | Insertar elementos BPMN básicos. | `AddBehaviorNodeUseCase`, `BehaviorNodeKind` | Cada tipo tiene icono/figura diferenciable. | Real por código |
| BPMN-04 Conectar flujo | Crear relación de flujo. | `AddBehaviorEdgeUseCase`, `BehaviorEdgeKind.FLOW` | Relación aparece en tabla y visual. | Real por código |
| BPMN-05 Editar/eliminar | Modificar nodo o flujo. | `UpdateBehaviorNodeUseCase`, `UpdateBehaviorEdgeUseCase`, `RemoveBehaviorItemUseCase` | Cambios guardables. | Real por código |
| BPMN-06 Validar | Detectar proceso vacío o relaciones rotas. | `ValidateBehaviorDiagramUseCase` | Validar proceso incompleto. | Real por código |
| BPMN-07 Exportar PNG/Markdown | Entregar proceso. | `BehaviorDiagramViewModel.exportVisualAsPng` | Exportar desde tab activo. | Real por código / requiere smoke |
| BPMN-08 Mejorar layout/interacción | Reducir cruces y permitir ajustes visuales persistentes. | Plan AV-I06 | No basta con layout automático fijo. | Parcial |

---

### 4.9 Flujo operativo

Promesa visible: **explicar pasos operativos de negocio de forma menos formal que BPMN**.

| Caso | Debe cumplir | Anclaje | Verificación anti-fachada | Estado |
|---|---|---|---|---|
| OPE-01 Crear flujo operativo | Crear documento de comportamiento tipo operativo. | `CreateBehaviorDiagramUseCase`, `BehaviorDiagramKind.OPERATIONAL_FLOW` | Toolbar debe decir Paso/Responsable/Decisión/Documento/Conexión. | Real por código |
| OPE-02 Importar Markdown | Leer pasos y conexiones. | `BehaviorMarkdownParser` | Importar `operational_flow_secretaria_uens_gordito.md`. | Real por código |
| OPE-03 Agregar paso/responsable/decisión/documento | Insertar elementos operativos. | `AddBehaviorNodeUseCase` | Visual y estructura se actualizan. | Real por código |
| OPE-04 Conectar pasos | Crear flujo entre elementos. | `AddBehaviorEdgeUseCase` | Tabla de relaciones y visual coherentes. | Real por código |
| OPE-05 Validar/exportar | Revisar y entregar. | `ValidateBehaviorDiagramUseCase`, `BehaviorDiagramViewModel.exportVisualAsPng` | PNG + Markdown activos. | Real por código / requiere smoke |
| OPE-06 Interacción visual fina | Ajustar manualmente nodos y rutas. | Plan AV-I06 | Pendiente como lienzo común. | Parcial |

---

### 4.10 UML Casos de uso

Promesa visible: **representar actores y funcionalidades observables del sistema**.

| Caso | Debe cumplir | Anclaje | Verificación anti-fachada | Estado |
|---|---|---|---|---|
| USE-01 Crear casos de uso | Crear documento de comportamiento tipo use case. | `CreateBehaviorDiagramUseCase`, `BehaviorDiagramKind.UML_USE_CASE` | Abre toolbar de actores/casos/límite/sistema. | Real por código |
| USE-02 Importar Markdown | Leer actores, casos y relaciones. | `BehaviorMarkdownParser` | Importar `uml_use_case_uens_gordito.md`. | Real por código |
| USE-03 Agregar actor/caso/límite | Insertar nodos correctos. | `AddBehaviorNodeUseCase`, `BehaviorNodeKind.ACTOR`, `USE_CASE`, `SYSTEM_BOUNDARY` | Actor no debe verse igual que cualquier caja si se promete UML. | Parcial visual |
| USE-04 Agregar asociación/include/extend/generalización | Crear relaciones UML. | `AddBehaviorEdgeUseCase`, `BehaviorEdgeKind` | Tabla y visual diferencian tipo. | Real por código / visual a verificar |
| USE-05 Validar/exportar | Revisar y entregar. | `ValidateBehaviorDiagramUseCase`, `ExportMarkdownUseCase`, PNG | Exportar ejemplo UENS. | Real por código / requiere smoke |
| USE-06 Layout UML específico | Actores fuera, casos dentro del límite. | Plan AV-I05/AV-I06 | Si solo hay grid genérico, no cumple totalmente la promesa visual. | Parcial |

---

### 4.11 UML Actividad

Promesa visible: **representar flujo de acciones, decisiones e inicio/fin**.

| Caso | Debe cumplir | Anclaje | Verificación anti-fachada | Estado |
|---|---|---|---|---|
| ACT-01 Crear actividad | Crear documento UML Actividad. | `BehaviorDiagramKind.UML_ACTIVITY` | Toolbar de acción/decisión/inicio/fin/transición. | Real por código |
| ACT-02 Importar Markdown | Leer acciones y transiciones. | `BehaviorMarkdownParser` | Importar `uml_activity_registrar_matricula_uens_gordito.md`. | Real por código |
| ACT-03 Editar nodos y flujos | Agregar/editar/eliminar acciones. | `Add/Update/RemoveBehavior*UseCase` | Cambios persistidos. | Real por código |
| ACT-04 Exportar/validar | PNG + Markdown + validación. | `BehaviorDiagramViewModel`, `ValidateBehaviorDiagramUseCase` | Exportar desde pestaña activa. | Real por código / requiere smoke |
| ACT-05 Semántica visual UML | Figuras y flujo reconocibles como actividad. | Plan AV-I05/AV-I06 | Pendiente de pulido visual. | Parcial |

---

### 4.12 UML Secuencia

Promesa visible: **representar mensajes entre participantes en el tiempo**.

| Caso | Debe cumplir | Anclaje | Verificación anti-fachada | Estado |
|---|---|---|---|---|
| SEQ-01 Crear secuencia | Crear documento UML Secuencia. | `BehaviorDiagramKind.UML_SEQUENCE` | Toolbar de participante, activación, mensaje y retorno. | Real por código |
| SEQ-02 Importar Markdown | Leer participantes y mensajes. | `BehaviorMarkdownParser` | Importar `uml_sequence_registrar_calificacion_uens_gordito.md`. | Real por código |
| SEQ-03 Agregar participantes/mensajes | Insertar lifelines/mensajes. | `AddBehaviorNodeUseCase`, `AddBehaviorEdgeUseCase` | Datos existen y exportan. | Real por código |
| SEQ-04 Layout temporal especializado | Tiempo vertical, participantes arriba, lifelines. | Plan AV-I08 | Si se dibuja como cajas genéricas, es funcional pero no visualmente fiel. | Parcial fuerte |
| SEQ-05 Exportar/validar | PNG + Markdown + validación. | `BehaviorDiagramViewModel`, `ValidateBehaviorDiagramUseCase` | Exportar y revisar que no prometa más de lo que muestra. | Parcial |

Criterio especial: UML Secuencia **no debería migrarse igual que BPMN o C4**. Necesita editor/lienzo especializado temporal.

---

### 4.13 UML Estados

Promesa visible: **representar estados y transiciones de una entidad, proceso o componente**.

| Caso | Debe cumplir | Anclaje | Verificación anti-fachada | Estado |
|---|---|---|---|---|
| STA-01 Crear estados | Crear documento UML Estados. | `BehaviorDiagramKind.UML_STATE` | Toolbar de inicio/estado/final/transición. | Real por código |
| STA-02 Importar Markdown | Leer estados y transiciones. | `BehaviorMarkdownParser` | Importar `uml_state_matricula_uens_gordito.md`. | Real por código |
| STA-03 Agregar estados y transición | Insertar nodos/relaciones. | `AddBehaviorNodeUseCase`, `AddBehaviorEdgeUseCase` | Visual y tabla actualizados. | Real por código |
| STA-04 Validar/exportar | Revisar y entregar. | `ValidateBehaviorDiagramUseCase`, `BehaviorDiagramViewModel.exportVisualAsPng` | Exportar ejemplo UENS. | Real por código / requiere smoke |
| STA-05 Layout editable | Reordenar estados y reducir cruces. | Plan AV-I05/AV-I06 | Pendiente interacción visual común. | Parcial |

---

### 4.14 C4 Contexto

Promesa visible: **representar personas, sistema principal y sistemas externos relacionados**.

| Caso | Debe cumplir | Anclaje | Verificación anti-fachada | Estado |
|---|---|---|---|---|
| C4CTX-01 Crear contexto | Crear documento de arquitectura C4 Contexto. | `CreateArchitectureDiagramUseCase`, `ArchitectureDiagramKind.C4_CONTEXT` | Abre `ArchitectureDiagramEditorView` con toolbar C4 Contexto. | Real por código |
| C4CTX-02 Importar Markdown | Leer personas, sistemas, límites y relaciones. | `ArchitectureMarkdownParser` | Importar `c4_context_uens_gordito.md`. | Real por código |
| C4CTX-03 Agregar persona/sistema/externo/límite | Insertar elementos C4. | `AddArchitectureNodeUseCase`, `ArchitectureNodeKind` | Elementos aparecen diferenciados. | Real por código |
| C4CTX-04 Agregar usa/integra | Conectar elementos. | `AddArchitectureEdgeUseCase`, `ArchitectureEdgeKind` | Relación visible y exportable. | Real por código |
| C4CTX-05 Validar/exportar | PNG + Markdown + validación. | `ValidateArchitectureDiagramUseCase`, `ArchitectureDiagramViewModel.exportVisualAsPng` | Exportar ejemplo UENS. | Real por código / requiere smoke |
| C4CTX-06 Layout C4 legible | Evitar cruces y ordenar actores/sistemas. | Plan AV-I07 | Pendiente interacción visual común. | Parcial |

---

### 4.15 C4 Contenedores

Promesa visible: **representar aplicaciones, backend, base de datos y servicios externos**.

| Caso | Debe cumplir | Anclaje | Verificación anti-fachada | Estado |
|---|---|---|---|---|
| C4CON-01 Crear contenedores | Crear documento C4 Contenedores. | `ArchitectureDiagramKind.C4_CONTAINERS` | Toolbar de contenedor/aplicación/API/base/servicio. | Real por código |
| C4CON-02 Importar Markdown | Leer contenedores y relaciones. | `ArchitectureMarkdownParser` | Importar `c4_containers_uens_gordito.md`. | Real por código |
| C4CON-03 Agregar nodos | Añadir aplicación, API, BD, servicio externo. | `AddArchitectureNodeUseCase` | Visual refleja tipo. | Real por código |
| C4CON-04 Agregar llamadas/lecturas | Conectar contenedores. | `AddArchitectureEdgeUseCase` | Relación `Llama` o `Lee/escribe` visible. | Real por código |
| C4CON-05 Validar/exportar | PNG + Markdown + validación. | `ValidateArchitectureDiagramUseCase`, `ArchitectureDiagramViewModel` | Exportar desde tab activo. | Real por código / requiere smoke |
| C4CON-06 Interacción visual | Mover nodos y rutas persistentes. | Plan AV-I07 | Pendiente. | Parcial |

---

### 4.16 Despliegue técnico

Promesa visible: **representar ambientes, servidores, clientes, servicios, base de datos, red y artefactos desplegables**.

| Caso | Debe cumplir | Anclaje | Verificación anti-fachada | Estado |
|---|---|---|---|---|
| DEP-01 Crear despliegue | Crear documento de despliegue. | `ArchitectureDiagramKind.TECHNICAL_DEPLOYMENT` | Toolbar de ambiente, servidor, cliente, servicio, red, artefacto. | Real por código |
| DEP-02 Importar Markdown | Leer nodos técnicos y conexiones. | `ArchitectureMarkdownParser` | Importar `technical_deployment_uens_gordito.md`. | Real por código |
| DEP-03 Agregar elementos técnicos | Insertar ambiente, servidor, servicio, BD, red, artefacto. | `AddArchitectureNodeUseCase` | Nodos diferenciados visualmente. | Real por código |
| DEP-04 Agregar conexión/alojamiento/despliegue | Modelar dependencias técnicas. | `AddArchitectureEdgeUseCase`, `CONNECTS_TO`, `HOSTS`, `DEPLOYS_TO` | Relación correcta en tabla y visual. | Real por código |
| DEP-05 Validar/exportar | PNG + Markdown + validación. | `ValidateArchitectureDiagramUseCase`, `ArchitectureDiagramViewModel.exportVisualAsPng` | Exportar ejemplo UENS. | Real por código / requiere smoke |
| DEP-06 No confundir con infraestructura interna | Hablar de despliegue del sistema modelado, no del código de la app. | Textos UI/teoría | No usar jerga de implementación de Domain Model Studio. | Requiere vigilancia textual |

---

## 5. Matriz de compatibilidad por tipo

| Tipo visible | Crear | Importar MD | Editor real | Tipo de editor | Validar | PNG | SVG | PDF | Markdown | `.dms` | Estado anti-fachada |
|---|---:|---:|---:|---|---:|---:|---:|---:|---:|---:|---|
| Modelo conceptual | Sí | Sí | Sí | Canvas conceptual maduro | Sí | Sí | Sí | No | Sí | Sí | Real; base de referencia visual. |
| Diccionario de datos | Sí | Parcial | Sí | Documento/fichas | Sí | No | No | Sí | Sí | Sí | Real como editor documental; importación oficial UENS pendiente/honesta. |
| Mapa de módulos | Sí | Sí | Sí | Diagrama visual especializado | Sí | Sí | No | No | Sí | Sí | Real; interacción visual avanzada pendiente. |
| Roles y permisos | Sí | Sí | Sí | Matriz estructurada | Sí | Sí | No | No | Sí | Sí | Real; no debe ser canvas libre. |
| Flujo de pantallas | Sí | Sí | Sí | Diagrama visual especializado | Sí | Sí | No | No | Sí | Sí | Real; interacción visual avanzada pendiente. |
| Wireframes administrativos | Sí | Sí | Sí | Maqueta visual/scaffolding | Sí | Sí | No | No | Sí | Sí | Real; no prometer diseñador UI completo. |
| UML Clases | Sí | Sí | Sí | Diagrama visual especializado | Sí | Sí | No | No | Sí | Sí | Real; layout agrupado pendiente. |
| BPMN básico | Sí | Sí | Sí | Diagrama comportamiento genérico | Sí | Sí | No | No | Sí | Sí | Real básico; layout BPMN fino pendiente. |
| Flujo operativo | Sí | Sí | Sí | Diagrama comportamiento genérico | Sí | Sí | No | No | Sí | Sí | Real básico; layout fino pendiente. |
| UML Casos de uso | Sí | Sí | Sí | Diagrama comportamiento genérico | Sí | Sí | No | No | Sí | Sí | Parcial visual: falta layout UML específico fuerte. |
| UML Actividad | Sí | Sí | Sí | Diagrama comportamiento genérico | Sí | Sí | No | No | Sí | Sí | Real básico; pulido visual pendiente. |
| UML Secuencia | Sí | Sí | Sí | Diagrama comportamiento genérico | Sí | Sí | No | No | Sí | Sí | Parcial fuerte: necesita editor temporal especializado. |
| UML Estados | Sí | Sí | Sí | Diagrama comportamiento genérico | Sí | Sí | No | No | Sí | Sí | Real básico; pulido visual pendiente. |
| C4 Contexto | Sí | Sí | Sí | Diagrama arquitectura genérico | Sí | Sí | No | No | Sí | Sí | Real básico; layout C4 pendiente. |
| C4 Contenedores | Sí | Sí | Sí | Diagrama arquitectura genérico | Sí | Sí | No | No | Sí | Sí | Real básico; layout C4 pendiente. |
| Despliegue técnico | Sí | Sí | Sí | Diagrama arquitectura genérico | Sí | Sí | No | No | Sí | Sí | Real básico; layout técnico pendiente. |

---

## 6. Riesgos anti-fachada detectados

| Riesgo | Evidencia | Impacto | Acción recomendada |
|---|---|---|---|
| Varios diagramas son visuales pero no lienzos interactivos maduros | Capturas muestran cajas y flechas con layout automático y cruces; código usa vistas especializadas con `Pane`/`ScrollPane` y snapshot PNG. | El usuario puede sentir que el diagrama existe, pero no puede ajustarlo como editor visual serio. | Ejecutar AV-I01 a AV-I08: extraer lienzo común y migrar familias por prioridad. |
| UML Secuencia necesita semántica visual propia | Secuencia no se comporta igual que BPMN/C4; requiere participantes arriba y tiempo vertical. | Si se dibuja como cajas genéricas, parece fachada UML. | Tratarlo como caso especializado en AV-I08. |
| Diccionario UENS aparece como importable documental | `DefaultOfficialExampleCatalog` deriva `importable=true` desde la fuente única. | Correcto si el parser mantiene round-trip básico. | Probar smoke manual del selector y exportación Markdown/PDF. |
| `MainShellCommandHandler` sigue grande | Inventario técnico previo lo marca >1600 líneas. | Riesgo de mezclar coordinación, sesión, exportación, creación y edición. | Continuar extracción por familias: sesión, creación, batch export, especializados. |
| ViewModels/vistas especializadas pueden duplicar lógica visual | Múltiples `*EditorView` renderizan visual propio. | Riesgo de inconsistencias y mantenimiento caro. | Crear contratos de canvas común, adaptadores por diagrama y export surface. |
| Capacidades reales pueden adelantarse a la UI | Catálogo dice muchos tipos `AVAILABLE`. | Si una capacidad no tiene smoke, puede parecer más terminada de lo que está. | Usar esta matriz + smoke UI por tipo antes de cerrar release. |
| Exportación Markdown especializada debe verificarse por tipo | `ExportMarkdownUseCase` centraliza salida, pero cada documento necesita exporter correcto. | Riesgo de Markdown genérico que pierda información. | Smoke: editar campo/nodo/relación, exportar y revisar diff. |

---

## 7. Checklist obligatorio por cada tipo antes de declarar “cerrado”

Para cada tipo visible del catálogo:

- [ ] Crear proyecto nuevo desde `Nuevo`.
- [ ] Ver que abre el workspace correcto.
- [ ] Ver que la toolbar corresponde al tipo activo.
- [ ] Agregar un elemento principal.
- [ ] Agregar una relación/asignación/componente si aplica.
- [ ] Editar propiedades del elemento.
- [ ] Eliminar un elemento sin romper el documento.
- [ ] Validar y revisar mensaje.
- [ ] Guardar `.dms`.
- [ ] Cerrar y reabrir `.dms`.
- [ ] Importar ejemplo oficial o plantilla mínima.
- [ ] Exportar Markdown si el tipo lo soporta.
- [ ] Exportar PNG si el tipo es visual/matriz.
- [ ] Exportar PDF solo si el documento activo declara PDF.
- [ ] Exportar SVG solo si es modelo conceptual.
- [ ] Revisar que el estado inferior muestre tipo/conteo correcto.
- [ ] Revisar que la pantalla no use jerga interna como parser, handler, provider, viewmodel, backend de la app o infraestructura de Domain Model Studio.

---

## 8. Trazas internas técnica mínima por familia

| Familia | Dominio/documento | Casos de uso | Vista/ViewModel | Importación | Exportación | Persistencia/tests relevantes |
|---|---|---|---|---|---|---|
| Conceptual | `DiagramProject`, `DiagramModel`, `DiagramLayout`, `EntityElement`, `RelationshipElement` | `application/editing/*`, `SwitchNotationUseCase`, layout/validation/export | `DiagramCanvasView`, `DiagramCanvasViewModel`, `InspectorViewModel`, `ModelTreeViewModel` | `MarkdownDiagramParser` | `SvgDiagramExporter`, `MarkdownDiagramExporter`, `CanvasPngExporter` | `DmsProjectJson*`, tests de dominio/layout/svg/import/export |
| Diccionario | `DataDictionaryDocument`, `DataDictionaryEntity`, `DataDictionaryField` | `application/datadictionary/*` | `DataDictionaryEditorView`, `DataDictionaryViewModel` | Pendiente directo para UENS oficial; exporters existentes | `DataDictionaryMarkdownExporter`, `DataDictionaryPdfExporter` | `DmsProjectJsonDataDictionaryTest`, tests de diccionario/pdf |
| Módulos | `ModuleMapDocument`, `ModuleNode`, `ModuleDependency` | `application/modulemap/*` | `ModuleMapEditorView`, `ModuleMapViewModel` | `ModuleMapMarkdownParser` | Markdown central + PNG de vista | `DmsProjectJsonModuleMapTest`, parser/export smoke |
| Roles/permisos | `RolesPermissionsDocument`, `RoleNode`, `PermissionNode`, `PermissionAssignment` | `application/rolespermissions/*` | `RolesPermissionsEditorView`, `RolesPermissionsViewModel` | `RolesPermissionsMarkdownParser` | Markdown central + PNG de matriz | tests de persistencia especializada |
| Pantallas | `ScreenFlowDocument`, `ScreenNode`, `ScreenTransition` | `application/screenflow/*` | `ScreenFlowEditorView`, `ScreenFlowViewModel` | `ScreenFlowMarkdownParser` | `ScreenFlowMarkdownExporter`, PNG | `ScreenFlowMarkdownParserTest`, `ScreenFlowMarkdownExporterTest` |
| Wireframes | `WireframeDocument`, `WireframeScreen`, `WireframeComponent` | `application/wireframe/*` | `WireframeEditorView`, `WireframeViewModel` | `WireframeMarkdownParser` | `WireframeMarkdownExporter`, PNG | tests de wireframe/parser/export/template |
| UML Clases | `UmlClassDiagramDocument`, `UmlClassNode`, `UmlClassRelation`, `UmlModuleGroup` | `application/umlclass/*` | `UmlClassDiagramEditorView`, `UmlClassDiagramViewModel` | `UmlClassMarkdownParser` | `UmlClassMarkdownExporter`, PNG | tests parser/export/persistencia especializada |
| Comportamiento | `BehaviorDiagramDocument`, `BehaviorNode`, `BehaviorEdge` | `application/behavior/*` | `BehaviorDiagramEditorView`, `BehaviorDiagramViewModel` | `BehaviorMarkdownParser` | Markdown central + PNG | tests de importación oficial y persistencia especializada |
| Arquitectura | `ArchitectureDiagramDocument`, `ArchitectureNode`, `ArchitectureEdge` | `application/architecture/*` | `ArchitectureDiagramEditorView`, `ArchitectureDiagramViewModel` | `ArchitectureMarkdownParser` | `ArchitectureMarkdownParserExporterTest`, PNG | tests de arquitectura/import/export/persistencia |
| Producto/shell | `DiagramTypeDescriptor`, `DiagramCapabilitySet`, `WorkspaceRoute` | `application/catalog/*`, `application/workspace/*`, batch export | `MainShellView`, `MainShellState`, toolbar/status/dialogs | `ImportCommandHandler` | `ExportCommandHandler`, `ActiveOutputResolver` | tests de catálogo, workspace, toolbar, exportable, product minimum coherence |

---

## 9. Pruebas manuales prioritarias para la próxima tanda

Orden recomendado para no perder tiempo:

1. **Smoke de pestañas mixtas**: abrir conceptual, wireframe, UML estados, C4 contenedores, flujo operativo, roles/permisos. Cambiar entre tabs y revisar toolbar/status/exportación.
2. **Smoke de exportación activa**: en cada tab exportar PNG/Markdown y confirmar que no exporta el proyecto equivocado.
3. **Smoke de guardado/reapertura**: guardar un `.dms` por familia y reabrir.
4. **Smoke de ejemplos oficiales**: abrir todos los ejemplos importables UENS desde el selector.
5. **Smoke de honestidad visual**: confirmar que diccionario documental se abre como documento importable y no como canvas; confirmar que secuencia no se presenta como editor UML completo si aún usa layout genérico.
6. **Smoke de edición mínima**: en cada tipo agregar, editar, eliminar y validar.

---

## 10. Criterio de cierre

Una tanda no debe cerrarse solo porque compile o porque se vea una pantalla.

Cierre aceptable:

```text
Tipo visible
→ workspace correcto
→ acción de creación/importación real
→ edición mínima real
→ persistencia .dms real
→ exportación coherente
→ validación/feedback
→ smoke manual registrado
→ sin promesas visuales falsas
```

Cualquier elemento que no pase ese circuito debe quedar marcado como **Parcial** o **Planificado**, nunca como disponible completo.


---

## 11. Documentos derivados por categoría y porcentajes

Para profundizar esta matriz sin volverla inmanejable, se separaron casos de uso por categoría en:

```text
docs/productizacion/casos-uso/
```

| Documento | Uso |
|---|---|
| `00_indice_casos_uso.md` | Índice, criterio de porcentajes y resumen por familias. |
| `01_controles_generales_gestion_proyectos.md` | Shell, pestañas, proyecto, dirty state, undo/redo, zoom, ayuda y recursos IA. |
| `02_entrada_salida_exportacion.md` | Importación, ejemplos, exportación activa, SVG/PDF/Markdown/PNG y batch export. |
| `03_datos_y_administrativos.md` | Diccionario, roles/permisos, módulos, flujo de pantallas y wireframes. |
| `04_modelado_conceptual_y_uml_estructural.md` | Modelo conceptual y UML clases. |
| `05_comportamiento_bpmn_uml.md` | BPMN, flujo operativo y UML de comportamiento. |
| `06_arquitectura_c4_despliegue.md` | C4 contexto, C4 contenedores y despliegue técnico. |
| `07_resumen_porcentajes_y_brechas.md` | Porcentaje estimado por tipo, controles generales y brechas prioritarias. |

Estos documentos deben actualizarse al cerrar cada tanda AV-I. Un porcentaje solo debe subir si se agrega implementación verificable o smoke manual registrado.
