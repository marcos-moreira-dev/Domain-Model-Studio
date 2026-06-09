# Casos de uso — datos y diagramas administrativos

Estado: **matriz por tipo funcional administrativo**  
Alcance: diccionario de datos, roles/permisos, mapa de módulos, flujo de pantallas y wireframes administrativos.

---

## Resumen por tipo

| Tipo | % al ojo | Estado | Lectura rápida |
|---|---:|---|---|
| Diccionario de datos | 78% | Real como editor documental importable | Bueno como documento/fichas y PDF; desde T01 el ejemplo UENS se declara importable como documento editable. |
| Roles y permisos | 78% | Real como matriz estructurada | Correcto como matriz, no como canvas libre. |
| Mapa de módulos | 68% | Diagrama real básico | Nodos/dependencias existen; falta lienzo común con movimiento persistente. |
| Flujo de pantallas | 66% | Diagrama real básico | Pantallas/transiciones existen; falta navegación visual más fuerte. |
| Wireframes administrativos | 74% | Maqueta visual útil | Se ve útil y honesto como scaffolding; no vender como diseñador UI completo. |

---

# Diccionario de datos

Promesa correcta: **documentar entidades, campos, tipos, reglas, validaciones, responsables y observaciones**.

| Caso | Estado | % | Anclaje observado | Falta para cierre |
|---|---|---:|---|---|
| DD-01 Crear diccionario | Terminado funcional | 85% | `CreateDataDictionaryUseCase`, `DataDictionaryEditorView` | Smoke crear desde `Nuevo`. |
| DD-02 Agregar entidad | Terminado funcional | 80% | `AddDataDictionaryEntityUseCase` | Verificar estado sucio y selección. |
| DD-03 Agregar campo | Terminado funcional | 80% | `AddDataDictionaryFieldUseCase` | Probar con tipos/reglas diferentes. |
| DD-04 Editar entidad/campo | Terminado funcional | 75% | `UpdateDataDictionaryEntityUseCase`, `UpdateDataDictionaryFieldUseCase` | Revisar que todos los campos visibles persistan. |
| DD-05 Eliminar entidad/campo | Terminado funcional | 75% | `RemoveDataDictionaryItemUseCase` | Probar eliminación con selección vacía. |
| DD-06 Validar | Terminado básico | 70% | `ValidateDataDictionaryUseCase` | Mensajes más útiles si faltan campos críticos. |
| DD-07 Exportar Markdown | Terminado funcional | 75% | `DataDictionaryMarkdownExporter` | Diff después de edición. |
| DD-08 Exportar PDF | Terminado funcional | 70% | `DataDictionaryPdfExporter` | Probar textos largos y tablas amplias. |
| DD-09 Guardar/abrir `.dms` | Terminado funcional | 75% | `DmsProjectJsonDataDictionaryTest` | Smoke manual. |
| DD-10 Importar ejemplo UENS directo | Terminado funcional | 75% | `DefaultOfficialExampleCatalog` lo deriva de `DefaultDiagramTypeDefinitions` como importable | Smoke manual con selector de ejemplos UENS. |

## Casos faltantes para diccionario

| ID | Caso | Prioridad | Motivo |
|---|---|---:|---|
| DD-FALT-01 | Robustecer importación directa de Markdown de diccionario | Media | Ya existe importación; falta cubrir más variantes y mensajes de error. |
| DD-FALT-02 | Validación de campos duplicados por entidad | Alta | Evita diccionarios inconsistentes. |
| DD-FALT-03 | Exportación PDF con portada/resumen | Baja/media | Mejora entrega formal, no bloquea MVP. |
| DD-FALT-04 | Búsqueda/filtro de entidades y campos | Media | Necesario cuando el diccionario crezca. |

---

# Roles y permisos

Promesa correcta: **matriz de autorización para revisar qué permisos tiene cada rol operativo**.

| Caso | Estado | % | Anclaje observado | Falta para cierre |
|---|---|---:|---|---|
| RP-01 Crear mapa de roles/permisos | Terminado funcional | 85% | `RolesPermissionsDocument`, `RolesPermissionsEditorView` | Smoke desde `Nuevo`. |
| RP-02 Importar Markdown | Terminado funcional | 80% | `RolesPermissionsMarkdownParser` | Probar ejemplo UENS y mínimo. |
| RP-03 Agregar rol | Terminado funcional | 80% | `AddRoleUseCase` / ViewModel | Probar edición de descripción/responsabilidad. |
| RP-04 Agregar permiso | Terminado funcional | 80% | `AddPermissionUseCase` | Probar scopes/acciones. |
| RP-05 Asignar permiso a rol | Terminado funcional | 78% | `AddPermissionAssignmentUseCase`, matriz visual | Probar toggles/asignaciones repetidas. |
| RP-06 Editar rol/permiso/asignación | Terminado funcional | 72% | `Update*UseCase` | Confirmar que la matriz se refresca. |
| RP-07 Eliminar | Terminado funcional | 75% | `RemoveRolesPermissionsItemUseCase` | Verificar limpieza de asignaciones colgantes. |
| RP-08 Validar | Terminado básico | 70% | `ValidateRolesPermissionsUseCase` | Más mensajes de roles sin permisos o permisos huérfanos. |
| RP-09 Exportar PNG/Markdown | Terminado funcional | 75% | `RolesPermissionsViewModel.exportVisualAsPng`, exporter Markdown | Smoke tab activa. |
| RP-10 Guardar/abrir `.dms` | Terminado funcional | 75% | Persistencia especializada | Round-trip manual. |

## Casos faltantes para roles/permisos

| ID | Caso | Prioridad | Motivo |
|---|---|---:|---|
| RP-FALT-01 | Filtro/búsqueda por rol o permiso | Media | La matriz crece rápido. |
| RP-FALT-02 | Vista compacta para exportar matriz grande | Media | PNG puede quedar ancho e incómodo. |
| RP-FALT-03 | Detección de permisos críticos sin rol responsable | Alta | Aporta utilidad real de auditoría. |

---

# Mapa de módulos

Promesa correcta: **representar módulos, submódulos, responsabilidades y dependencias de una aplicación administrativa**.

| Caso | Estado | % | Anclaje observado | Falta para cierre |
|---|---|---:|---|---|
| MOD-01 Crear mapa | Terminado funcional | 85% | `CreateModuleMapUseCase`, `ModuleMapEditorView` | Smoke desde `Nuevo`. |
| MOD-02 Importar Markdown | Terminado funcional | 80% | `ModuleMapMarkdownParser` | Probar UENS y restaurante mínimo. |
| MOD-03 Agregar módulo/submódulo | Terminado funcional | 78% | `AddModuleNodeUseCase` | Verificar posición inicial. |
| MOD-04 Agregar dependencia | Terminado funcional | 75% | `AddModuleDependencyUseCase` | Revisar tabla y línea visual. |
| MOD-05 Editar propiedades | Terminado funcional | 70% | `UpdateModuleNodeUseCase`, `UpdateModuleDependencyUseCase` | Probar que panel derecho sincronice selección. |
| MOD-06 Eliminar | Terminado funcional | 75% | `RemoveModuleMapItemUseCase` | Confirmar limpieza de dependencias. |
| MOD-07 Validar | Terminado básico | 70% | `ValidateModuleMapUseCase` | Detectar dependencia rota. |
| MOD-08 Exportar PNG/Markdown | Terminado funcional | 74% | `ModuleMapViewModel.exportVisualAsPng`, exporter | Smoke tab activa. |
| MOD-09 Movimiento visual persistente | Parcial | 40% | Plan AV-I03 | Falta lienzo común/interacción real. |
| MOD-10 Reducir cruces/layout legible | Parcial | 45% | Layout automático actual | Mejorar con rutas persistentes. |

## Casos faltantes para mapa de módulos

| ID | Caso | Prioridad | Motivo |
|---|---|---:|---|
| MOD-FALT-01 | Arrastrar módulos y guardar posiciones | Alta | Sin esto parece diagrama estático. |
| MOD-FALT-02 | Editar ruta de dependencia o puntos intermedios | Media | Necesario para diagramas grandes. |
| MOD-FALT-03 | Agrupar módulos por dominio/capa | Media | Muy útil para aplicaciones empresariales. |
| MOD-FALT-04 | Auto-layout bajo demanda sin destruir ajustes manuales | Alta | Evita perder trabajo humano. |

---

# Flujo de pantallas

Promesa correcta: **representar navegación entre pantallas de una aplicación administrativa**.

| Caso | Estado | % | Anclaje observado | Falta para cierre |
|---|---|---:|---|---|
| SF-01 Crear flujo | Terminado funcional | 82% | `CreateScreenFlowUseCase`, `ScreenFlowEditorView` | Smoke desde `Nuevo`. |
| SF-02 Importar Markdown | Terminado funcional | 80% | `ScreenFlowMarkdownParser` | Probar UENS y ventas mínimo. |
| SF-03 Agregar pantalla | Terminado funcional | 78% | `AddScreenUseCase` | Verificar tipo de pantalla. |
| SF-04 Agregar transición | Terminado funcional | 75% | `AddScreenTransitionUseCase` | Tabla + línea visual. |
| SF-05 Editar/eliminar | Terminado funcional | 70% | `Update/RemoveScreenFlow*` | Persistencia de cambios. |
| SF-06 Validar/exportar | Terminado básico | 72% | `ValidateScreenFlowUseCase`, PNG/Markdown | Smoke tab activa. |
| SF-07 Layout navegacional persistente | Parcial | 40% | Plan AV-I04 | Falta movimiento y rutas manuales. |

## Casos faltantes para flujo de pantallas

| ID | Caso | Prioridad | Motivo |
|---|---|---:|---|
| SF-FALT-01 | Marcar pantalla inicial/final de forma visible | Alta | Fundamental para lectura de navegación. |
| SF-FALT-02 | Agrupar pantallas por módulo | Media | Muy necesario en sistemas administrativos grandes. |
| SF-FALT-03 | Arrastrar pantallas y persistir layout | Alta | Evita sensación de visualización estática. |

---

# Wireframes administrativos

Promesa correcta: **maquetas estructurales simples de pantallas administrativas: paneles, secciones, formularios, tablas, campos, botones y estados**.

| Caso | Estado | % | Anclaje observado | Falta para cierre |
|---|---|---:|---|---|
| WF-01 Crear wireframe | Terminado funcional | 85% | `CreateWireframeUseCase`, `WireframeEditorView` | Smoke desde `Nuevo`. |
| WF-02 Importar Markdown | Terminado funcional | 82% | `WireframeMarkdownParser` | Probar ejemplo UENS. |
| WF-03 Insertar pantalla | Terminado funcional | 80% | `AddWireframeScreenUseCase` | Verificar panel izquierdo. |
| WF-04 Insertar componentes | Terminado funcional | 78% | `AddWireframeComponentUseCase`, tipos scaffolding | Probar sección/formulario/tabla/campo/botón. |
| WF-05 Aplicar plantillas | Terminado funcional | 75% | `ApplyWireframeTemplateUseCase` | Ampliar catálogo si hace falta. |
| WF-06 Editar propiedades | Terminado funcional | 75% | `WireframeViewModel`, panel derecho | Confirmar guardado. |
| WF-07 Exportar PNG/Markdown | Terminado funcional | 76% | `WireframeMarkdownExporter`, snapshot PNG | Smoke tab activa. |
| WF-08 Arrastrar/redimensionar componentes | Parcial | 45% | Plan AV-I04 | Definir hasta qué punto será editor visual. |
| WF-09 No prometer Figma | Terminado conceptual | 90% | Textos de “maquetas/scaffolding” | Mantener lenguaje honesto. |

## Casos faltantes para wireframes

| ID | Caso | Prioridad | Motivo |
|---|---|---:|---|
| WF-FALT-01 | Mover componentes dentro de pantalla | Alta | Sin esto la maqueta queda muy rígida. |
| WF-FALT-02 | Redimensionar pantalla/componente | Media | Útil, pero no convertir en diseñador complejo. |
| WF-FALT-03 | Plantilla dashboard/listado/formulario/reporte más controlada | Media | Da productividad sin sobreingeniería. |
| WF-FALT-04 | Exportar varias pantallas con separación clara | Media | PNG de una sola vista puede no servir para documentos largos. |
