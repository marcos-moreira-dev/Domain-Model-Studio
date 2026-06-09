# Alineación 004 — Catálogo, capacidades, toolbar, workspace y exportación

Estado: **alineación aplicada**  
Fecha: **2026-05-24**  
Alcance: **catálogo oficial, capacidades declaradas, workspace real, toolbar contextual, salida activa y exportaciones del Grafo lógico del negocio**  
Tipo de cambio: **documental y de guardarraíl fuente; no modifica lógica de ejecución**

## 1. Propósito

Esta alineación fija el contrato técnico que debe cumplir un tipo visible cuando el catálogo declara que puede crearse, abrirse, editarse, validarse, guardarse o exportarse.

La regla central es:

```txt
catálogo visible = workspace real + capacidades reales + acciones visibles coherentes + salida activa resoluble
```

Esta alineación no cambia todavía `DefaultDiagramTypeDefinitions`, toolbar, persistencia, exportadores ni validación global. Su función es dejar el mapa exacto de cableado que las tandas técnicas deben cerrar para que `logical-business-graph` deje de estar en estado híbrido.

## 2. Fuentes técnicas que deben quedar alineadas

Para cualquier tipo visible, estas piezas deben contar la misma historia:

| Área | Fuente técnica | Qué debe declarar o resolver |
|---|---|---|
| Identidad | `DiagramTypeId` | ID estable del tipo. |
| Catálogo oficial | `DefaultDiagramTypeDefinitions` | estado, categoría, capacidades, workspace, teoría, gramática, toolbar y ejemplos. |
| Capacidades | `DefaultDiagramCapabilityCatalog` / `DiagramCapabilitySet` | promesas reales de creación, importación, edición, exportación, persistencia, recursos IA y teoría. |
| Workspace | `DefaultCreateWorkspaceUseCase`, `WorkspaceTypeRoutingPolicy`, `DefaultWorkspaceDescriptorCatalog` | si abre producto real o placeholder. |
| Carga especializada | `SpecializedWorkspaceCoordinator` | ViewModel correcto para el tipo activo. |
| Toolbar | `DiagramToolbarContributorRegistry`, contributors y `DefaultDiagramToolbarActionProvider` | acciones contextuales coherentes con capacidades y perfil de interacción. |
| Validación global | `ProjectValidationCoordinator` | validación del tipo activo, sin caer al canvas conceptual. |
| Salida activa | `ActiveOutputResolver`, `ActiveOutputContributorRegistry`, contributors de salida | `ExportableOutput` correcto para el proyecto activo. |
| Formatos | `ProjectExportFormatPolicy` | formatos permitidos por capacidad y disponibilidad real del documento. |
| Exportadores | infraestructura Markdown/SVG/PNG/PDF según aplique | ejecución real de cada formato prometido. |
| Persistencia | repositorio `.dms` / JSON | roundtrip del documento especializado y su layout. |

## 3. Estado actual del Grafo lógico según la lectura

El tipo `logical-business-graph` ya tiene piezas reales:

```txt
DiagramTypeId.LOGICAL_BUSINESS_GRAPH
DiagramWorkspaceKind.LOGICAL_BUSINESS_GRAPH_DIAGRAM
WorkspaceKind.LOGICAL_BUSINESS_GRAPH_DIAGRAM
LogicalBusinessGraphEditorView
LogicalBusinessGraphViewModel
LogicalBusinessGraphDocument
LogicalBusinessGraphMarkdownParser
LogicalBusinessGraphMarkdownExporter
LogicalBusinessGraphCanvasAdapter
LogicalBusinessGraphRenderKit
LogicalBusinessGraphWorkbenchContributor
```

Sin embargo, el contrato todavía está incompleto:

```txt
Estado actual de catálogo: EXPERIMENTAL.
Workspace real: sí.
Capacidades declaradas actuales: CREATE_PROJECT, IMPORT_MARKDOWN, SHOW_VISUAL_OUTPUT, MANUAL_EDITING, EXPORT_MARKDOWN.
Capacidades faltantes para cierre productivo: EXPORT_PNG, EXPORT_SVG, SAVE_DMS, LOAD_DMS, AI_RESOURCES, THEORY_HELP.
Toolbar contextual propia: pendiente.
Validación global desde menú Diagrama: pendiente.
Active output específico: pendiente.
Persistencia .dms validada: pendiente.
Guía académica registrada: pendiente.
```

La contradicción principal es:

```txt
El código ya tiene workspace real,
pero el catálogo todavía no lo trata como producto disponible completo.
```

## 4. Decisión de estado

El estado final aceptado para `logical-business-graph` debe ser uno de estos dos, no un híbrido indefinido:

| Camino | Estado | Workspace | Uso correcto |
|---|---|---|---|
| Producto real | `AVAILABLE` | `LOGICAL_BUSINESS_GRAPH_DIAGRAM` | Se completan toolbar, exportaciones, persistencia, teoría, recursos IA, validación y matrices. |
| Preparación honesta | `IN_PREPARATION` | `PLACEHOLDER_GUIDE` | Se retira la promesa de workspace productivo hasta completar cierre. |

Decisión de alineación: **la ruta recomendada es llevarlo a `AVAILABLE`, pero solo después de completar los puntos de esta alineación y las tandas 40, 41 y 42**.

## 5. Contrato objetivo de catálogo para `logical-business-graph`

Cuando se cierre productivamente, la definición oficial deberá tender a:

```txt
id: logical-business-graph
nombre: Grafo lógico del negocio
categoría: BUSINESS_ANALYSIS
estado: AVAILABLE
workspace: LOGICAL_BUSINESS_GRAPH_DIAGRAM
theoryTopicId: logical-business-graph
grammarResourceId: logical-business-graph
toolbarPolicyId: logical-business-graph
minimalExampleResource: ai-resources/official-markdown/diagramas/logical_business_graph_minimo.md
officialExampleResource: ai-resources/official-markdown/diagramas/logical_business_graph_uens_gordito.md
officialExampleImportable: true
```

El ejemplo mínimo no debe apuntar al UENS completo. El mínimo debe servir para crear o probar rápidamente el tipo. El UENS debe quedar como ejemplo oficial completo y smoke principal.

## 6. Capacidades objetivo

El contrato final de capacidades debe ser:

```txt
CREATE_PROJECT
IMPORT_MARKDOWN
SHOW_VISUAL_OUTPUT
MANUAL_EDITING
EXPORT_MARKDOWN
EXPORT_PNG
EXPORT_SVG
SAVE_DMS
LOAD_DMS
AI_RESOURCES
THEORY_HELP
```

Cada capacidad exige una prueba de existencia:

| Capacidad | Evidencia obligatoria antes de declararla final |
|---|---|
| `CREATE_PROJECT` | `NewProjectFactory` crea proyecto y `DefaultCreateWorkspaceUseCase` devuelve `PRODUCT_VIEW`. |
| `IMPORT_MARKDOWN` | parser, dispatcher, plantilla, ejemplo mínimo, ejemplo UENS y roundtrip de importación. |
| `SHOW_VISUAL_OUTPUT` | workspace visual real con `LogicalBusinessGraphEditorView` y canvas transversal. |
| `MANUAL_EDITING` | edición de cabecera, propiedades, selección, movimiento y layout persistible. |
| `EXPORT_MARKDOWN` | exporter registrado, toolbar/menú conectados y salida activa resuelta. |
| `EXPORT_PNG` | exportador PNG desde canvas activo y smoke visual. |
| `EXPORT_SVG` | exportador SVG vectorial del documento visual o salida especializada equivalente. |
| `SAVE_DMS` | `.dms` guarda documento lógico del grafo y layout. |
| `LOAD_DMS` | `.dms` reabre workspace correcto con nodos, relaciones y layout. |
| `AI_RESOURCES` | descriptores, archivos, índice exportable y documentación `docs/ia`. |
| `THEORY_HELP` | tema académico, figura didáctica, micro-wikipedia y registro en catálogo teórico. |

## 7. Workspace y apertura

El Grafo lógico no debe abrir como `UNSUPPORTED` si se mantiene como tipo real.

Contrato esperado:

```txt
DefaultCreateWorkspaceUseCase
→ logical-business-graph con estado AVAILABLE
→ CreateWorkspaceResult.PRODUCT_VIEW
→ WorkspaceKind.LOGICAL_BUSINESS_GRAPH_DIAGRAM
→ LogicalBusinessGraphEditorView
```

Reglas:

- si el tipo tiene workspace real, no debe usar `PLACEHOLDER_GUIDE`;
- si el tipo está en preparación, no debe registrar un workspace real como promesa productiva;
- `SpecializedWorkspaceCoordinator` debe cargar `LogicalBusinessGraphViewModel`, no `FreeGraphViewModel` ni canvas conceptual;
- cerrar o cambiar pestaña debe limpiar el ViewModel especializado sin afectar el modelo conceptual.

## 8. Toolbar contextual esperada

El Grafo lógico necesita contributor propio o soporte explícito equivalente. No debe depender de `LogicalBusinessToolbarContributor`, porque ese contributor pertenece al Levantamiento lógico documental.

Nombre recomendado:

```txt
LogicalBusinessGraphToolbarContributor
```

Acciones mínimas recomendadas:

| Sección | Acción | Observación |
|---|---|---|
| Modelo | Validar | Debe abrir o ejecutar validación del Grafo lógico, no la del canvas conceptual. |
| Vista | Ajustar | Puede reutilizar `FIT_TO_CONTENT`. |
| Vista | Centrar | Puede reutilizar `CENTER_DIAGRAM`. |
| Exportar | Markdown | Solo si `EXPORT_MARKDOWN` está declarado y resuelto. |
| Exportar | SVG | Solo si `EXPORT_SVG` está declarado y resuelto. |
| Exportar | PNG | Solo si `EXPORT_PNG` está declarado y resuelto. |

Acciones no recomendadas para el cierre inicial:

```txt
Agregar nodo desde canvas.
Agregar relación por arrastre.
Eliminar nodo.
Eliminar relación.
Quitar punto.
```

Motivo: el contrato semántico de Alineación 3 define que `MANUAL_EDITING` inicial no exige CRUD estructural completo. Además, el adapter del Grafo lógico no soporta bendpoints; por tanto, la acción `DELETE_SELECTED_BEND_POINT` no debe aparecer para este tipo mientras `supportsBendPoints()` sea falso.

## 9. Validación global

El menú `Diagrama > Validar proyecto` debe reconocer el tipo activo `logical-business-graph`.

Contrato esperado:

```txt
ProjectValidationCoordinator
→ si activeType == LOGICAL_BUSINESS_GRAPH
→ usar LogicalBusinessGraphViewModel / LogicalBusinessGraphDocument.semanticIssues()
→ mostrar issues del Grafo lógico
→ no caer al DiagramCommandHandler conceptual
```

Esta validación global es independiente de la validación profunda de Tanda 42. La primera obligación es enrutar correctamente. La segunda obligación será ampliar reglas semánticas.

## 10. Salida activa y exportaciones

`ProjectExportFormatPolicy` ya tiene una ruta para `formatsForLogicalBusinessGraph(...)`, pero eso no basta. El menú Exportar necesita una salida activa concreta.

Contrato esperado:

```txt
ActiveOutputResolver
→ ActiveOutputContributorRegistry
→ LogicalBusinessGraphActiveOutputContributor
→ ExportableOutput del proyecto logical-business-graph
→ formatos Markdown, PNG, SVG según capacidades reales
```

Nombre recomendado:

```txt
LogicalBusinessGraphActiveOutputContributor
```

Reglas:

- `ProjectExportFormatPolicy` decide formatos por capacidades y disponibilidad de documento;
- el contributor decide si el ViewModel activo puede exportar realmente;
- la toolbar y el menú Exportar no deben prometer formatos sin contributor;
- exportar un grafo vacío no debe producir Markdown inválido con relación a `FL-001` inexistente;
- PNG y SVG deben tener smoke manual porque el valor final es visual.

## 11. Persistencia `.dms`

La persistencia no se corrige en esta alineación, pero la capacidad `SAVE_DMS` / `LOAD_DMS` no puede declararse final hasta comprobar:

```txt
- guardar documento LogicalBusinessGraphDocument;
- guardar nodos y relaciones;
- guardar layout especializado;
- reabrir proyecto con DiagramTypeId.LOGICAL_BUSINESS_GRAPH;
- enrutar a LOGICAL_BUSINESS_GRAPH_DIAGRAM;
- reconstruir selección/estado visual sin caer al grafo libre;
- conservar cambios de movimiento de nodos.
```

Esto queda para Tanda 41.

## 12. Orden técnico recomendado después de esta alineación

El orden más seguro es:

```txt
1. Ajuste de catálogo: estado, capacidades, ejemplo mínimo, theoryTopicId.
2. Toolbar contextual propia del Grafo lógico.
3. Validación global en ProjectValidationCoordinator.
4. Active output contributor.
5. Export Markdown/PNG/SVG conectados desde menú y toolbar.
6. Persistencia .dms roundtrip.
7. Tests focalizados por capacidad.
8. Matrices y documentación anti-fachada.
```

No se deben actualizar matrices como si todo estuviera listo antes de cerrar las capacidades reales.

## 13. Criterios de aceptación de Alineación 4

Esta alineación se considera aplicada si existe este contrato y si los documentos vivos la referencian como continuidad vigente.

No se considera cierre técnico del Grafo lógico. El cierre técnico ocurrirá cuando pasen las tandas de implementación asociadas.

## 14. Tests de comprobación agregados

Esta alineación agrega el guardarraíl documental:

```bat
mvn -Dtest=Alineacion4CatalogCapabilitiesWorkspaceExportContractSourceTest test
```

El test verifica que este contrato exista, que enumere fuentes técnicas, capacidades objetivo, workspace, toolbar, validación global, salida activa, persistencia `.dms` y que los documentos vivos apunten a la Alineación 4.

## 15. Tandas posteriores que dependen de esta alineación

Después de esta alineación, quedan:

1. Alineación 5 — Ayuda académica, ayuda operativa y recursos IA.
2. Alineación 6 — Validación integral y criterios de calidad.
3. Alineación 7 — Documentación anti-fachada, matrices y release.
4. Alineación 8 — Plan quirúrgico de correcciones.
5. Tanda 40 — Guía académica del Grafo lógico.
6. Tanda 41 — Persistencia `.dms` y exportaciones.
7. Tanda 42 — Validación integral del nuevo proyecto.
8. Tanda 31 — Validación local Windows / Release Candidate.
9. Tanda 9 — Deuda SRP focalizada, solo si aparece bloqueo real.
