# Matriz de casos de uso y pruebas de aceptación

Estado: **documentación viva de trazabilidad funcional**  
Tanda: **21**  
Alcance: casos de uso prometidos por la aplicación, componentes transversales, tipos visibles y estrategia de prueba correspondiente.

## Propósito

Esta matriz responde una pregunta concreta:

> Si la aplicación muestra una acción, un tipo de diagrama o una capacidad, ¿qué caso de uso promete y qué prueba debe cubrirlo?

No busca agregar funciones nuevas. Su objetivo es evitar **botones de mentira**, promesas ambiguas o diagramas que parezcan completos sin tener una ruta de validación humana y automática.

## Niveles de prueba usados

| Nivel | Uso principal | Ejemplo |
|---|---|---|
| Test unitario | Reglas puras de dominio/aplicación sin interfaz. | Validar layout, parser, normalización, capacidades. |
| Test integración | Flujo con varias piezas reales sin clics de usuario. | Importar Markdown, guardar `.dms`, reabrir, exportar archivo. |
| Test contrato/fuente | Guardarraíl de arquitectura, texto visible, capacidades o estructura esperada. | Verificar que no aparezca `Abrir código` fuera de UML Clases. |
| Test UI/E2E | Simulación de usuario: clic, selección, drag, toolbar, diálogo controlado. | Seleccionar nodo, arrastrar clase, exportar desde toolbar. |
| Smoke manual | Verificación humana visual del resultado. | Abrir SVG/PNG exportado y mirar si el diagrama se entiende. |

## Estados de cobertura

| Estado | Significado |
|---|---|
| `CUBIERTO` | Existe prueba automática suficiente para la promesa base. |
| `PARCIAL` | Hay pruebas de apoyo, pero falta cubrir una parte de interacción o runtime real. |
| `PENDIENTE_UI_E2E` | Requiere prueba con simulación de usuario o robot JavaFX/TestFX. |
| `MANUAL_REQUERIDO` | Además de tests, requiere revisión humana visual/documental. |
| `NO_APLICA` | La capacidad no corresponde a ese tipo o contexto. |

## Componentes transversales y casos de uso obligatorios

| Código | Componente | Caso de uso prometido | Prueba esperada | Estado base |
|---|---|---|---|---|
| `UC-PROJ-001` | Shell/proyectos | Crear un proyecto del tipo seleccionado. | Test integración + UI/E2E mínimo. | `PARCIAL` |
| `UC-PROJ-002` | Persistencia `.dms` | Guardar proyecto y reabrirlo sin perder datos especializados ni layout. | Test integración round-trip. | `CUBIERTO` |
| `UC-PROJ-003` | Pestañas/workbench | Mantener acciones ligadas a la pestaña activa. | Test contrato + UI/E2E mínimo. | `PARCIAL` |
| `UC-IMPORT-001` | Importación Markdown | Importar ejemplo o Markdown compatible del tipo activo. | Test integración por parser/dispatcher. | `CUBIERTO` |
| `UC-EXAMPLE-001` | Ejemplos oficiales | Abrir/importar ejemplo oficial UENS cuando aplique. | Test integración de catálogo/recursos. | `CUBIERTO` |
| `UC-CANVAS-001` | Canvas visual | Seleccionar elemento visual y reflejar selección semántica. | Test contrato + UI/E2E. | `PARCIAL` |
| `UC-CANVAS-002` | Canvas visual | Arrastrar elementos soportados y persistir posición. | Test contrato + UI/E2E. | `PARCIAL` |
| `UC-CANVAS-003` | Canvas visual | Zoom, paneo, centrar y ajustar a contenido. | Test contrato + smoke manual. | `PARCIAL` |
| `UC-CANVAS-004` | Canvas visual | Mostrar etiquetas de relaciones cuando el tipo las prometa. | Test contrato + smoke manual. | `PARCIAL` |
| `UC-CONCEPTUAL-001` | Modelo conceptual | Abrir y operar el modelo conceptual dentro del workspace común preservando el dibujo ER actual. | Test fuente + smoke manual conceptual. | `PARCIAL` |
| `UC-EXPORT-001` | Exportación SVG | Exportar archivo SVG no vacío cuando el tipo declare SVG. | Test integración + smoke manual. | `PARCIAL` |
| `UC-EXPORT-002` | Exportación PNG | Exportar archivo PNG no vacío cuando el tipo declare PNG. | Test integración + smoke manual. | `PARCIAL` |
| `UC-EXPORT-003` | Exportación Markdown | Exportar Markdown representativo cuando el tipo declare Markdown. | Test integración. | `CUBIERTO` |
| `UC-EXPORT-004` | Exportación PDF | Exportar PDF solo en tipos documentales que lo declaren. | Test integración + revisión manual. | `PARCIAL` |
| `UC-HELP-001` | Ayuda/teoría | Mostrar teoría, gramática o ayuda del tipo activo. | Test catálogo + smoke manual. | `PARCIAL` |
| `UC-AI-001` | Recursos IA | Exportar/mostrar recursos IA oficiales del tipo. | Test integración de recursos. | `CUBIERTO` |
| `UC-UMLSRC-001` | UML Clases | Importar directorio de código fuente Java/TypeScript. | Test aplicación/integración. | `CUBIERTO` |
| `UC-UMLSRC-002` | UML Clases | Abrir archivo fuente solo si la clase seleccionada tiene ruta real. | Test contrato + UI/E2E con fake launcher. | `PARCIAL` |

## Matriz por tipo visible

| Tipo visible | ID oficial | Promesa honesta | Casos obligatorios | Pruebas automáticas esperadas | Revisión humana |
|---|---|---|---|---|---|
| Modelo conceptual | `conceptual-model` | Editor visual conceptual con entidades, atributos, relaciones, cardinalidades y exportación visual/documental, montado ahora dentro del workspace común preservando el dibujo ER actual. | `UC-PROJ-001`, `UC-IMPORT-001`, `UC-CANVAS-001`, `UC-CANVAS-002`, `UC-CONCEPTUAL-001`, `UC-EXPORT-001`, `UC-EXPORT-002`, `UC-EXPORT-003` | Dominio, parser/exportador, canvas legacy encapsulado, workbench común y round-trip. | `MANUAL_REQUERIDO` para PNG/SVG y smoke conceptual migrado. |
| Diccionario de datos | `data-dictionary` | Documento tabular/formal de campos, reglas y observaciones; no promete canvas visual libre. | `UC-PROJ-001`, `UC-IMPORT-001`, `UC-EXPORT-003`, `UC-EXPORT-004` | Parser/exportador Markdown/PDF y persistencia. | `MANUAL_REQUERIDO` para PDF. |
| BPMN básico | `bpmn-basic` | Diagramación básica de procesos de negocio; no implementación completa de la especificación BPMN. | `UC-PROJ-001`, `UC-IMPORT-001`, `UC-CANVAS-001`, `UC-EXPORT-001`, `UC-EXPORT-002`, `UC-EXPORT-003` | Parser, layout, SVG, toolbar/capacidades. | `MANUAL_REQUERIDO` para símbolos. |
| Flujo operativo | `operational-flow` | Flujo operativo informal de negocio con pasos, decisiones y relaciones. | `UC-PROJ-001`, `UC-IMPORT-001`, `UC-CANVAS-001`, `UC-EXPORT-001`, `UC-EXPORT-002`, `UC-EXPORT-003` | Parser, layout, SVG, exportación. | `MANUAL_REQUERIDO`. |
| C4 Contexto | `c4-context` | Vista C4 básica de sistema, personas y sistemas externos. | `UC-PROJ-001`, `UC-IMPORT-001`, `UC-CANVAS-001`, `UC-EXPORT-001`, `UC-EXPORT-002`, `UC-EXPORT-003` | Parser, layout, SVG, exportación. | `MANUAL_REQUERIDO`. |
| C4 Contenedores | `c4-containers` | Vista C4 básica de aplicaciones, contenedores, bases y servicios. | `UC-PROJ-001`, `UC-IMPORT-001`, `UC-CANVAS-001`, `UC-EXPORT-001`, `UC-EXPORT-002`, `UC-EXPORT-003` | Parser, layout, SVG, exportación. | `MANUAL_REQUERIDO`. |
| Despliegue técnico | `technical-deployment` | Vista técnica básica de nodos, servicios, ambientes y dependencias. | `UC-PROJ-001`, `UC-IMPORT-001`, `UC-CANVAS-001`, `UC-EXPORT-001`, `UC-EXPORT-002`, `UC-EXPORT-003` | Parser, layout, SVG, exportación. | `MANUAL_REQUERIDO`. |
| UML Clases | `uml-class` | Diagrama estructural de clases, relaciones y módulos; además importación de código fuente. | `UC-PROJ-001`, `UC-IMPORT-001`, `UC-CANVAS-001`, `UC-CANVAS-002`, `UC-EXPORT-001`, `UC-EXPORT-002`, `UC-EXPORT-003`, `UC-UMLSRC-001`, `UC-UMLSRC-002` | Parser Markdown, importación código, selección, drag, SVG, toolbar y capacidades. | `MANUAL_REQUERIDO` para jerarquías visuales. |
| UML Casos de uso | `uml-use-case` | Diagramación básica de actores y casos observables; no suite UML profesional completa. | `UC-PROJ-001`, `UC-IMPORT-001`, `UC-CANVAS-001`, `UC-EXPORT-001`, `UC-EXPORT-002`, `UC-EXPORT-003` | Parser, layout, SVG, exportación. | `MANUAL_REQUERIDO`. |
| UML Actividad | `uml-activity` | Diagrama básico de acciones, decisiones y flujos. | `UC-PROJ-001`, `UC-IMPORT-001`, `UC-CANVAS-001`, `UC-EXPORT-001`, `UC-EXPORT-002`, `UC-EXPORT-003` | Parser, layout, SVG, exportación. | `MANUAL_REQUERIDO`. |
| UML Secuencia | `uml-sequence` | Vista temporal básica de participantes y mensajes. | `UC-PROJ-001`, `UC-IMPORT-001`, `UC-CANVAS-001`, `UC-EXPORT-001`, `UC-EXPORT-002`, `UC-EXPORT-003` | Parser, política temporal, SVG, exportación. | `MANUAL_REQUERIDO`. |
| UML Estados | `uml-state` | Estados y transiciones básicas de una entidad/proceso. | `UC-PROJ-001`, `UC-IMPORT-001`, `UC-CANVAS-001`, `UC-EXPORT-001`, `UC-EXPORT-002`, `UC-EXPORT-003` | Parser, layout, SVG, exportación. | `MANUAL_REQUERIDO`. |
| Mapa de módulos | `admin-module-map` | Mapa funcional de módulos, submódulos y dependencias administrativas. | `UC-PROJ-001`, `UC-IMPORT-001`, `UC-CANVAS-001`, `UC-EXPORT-001`, `UC-EXPORT-002`, `UC-EXPORT-003` | Parser, layout, persistencia, exportación. | `MANUAL_REQUERIDO`. |
| Roles y permisos | `roles-permissions-map` | Matriz administrativa de roles, permisos y acciones. | `UC-PROJ-001`, `UC-IMPORT-001`, `UC-EXPORT-001`, `UC-EXPORT-002`, `UC-EXPORT-003` | Parser, editor/matriz, PNG/SVG/Markdown. | `MANUAL_REQUERIDO`. |
| Flujo de pantallas | `screen-flow` | Navegación entre pantallas administrativas. | `UC-PROJ-001`, `UC-IMPORT-001`, `UC-CANVAS-001`, `UC-EXPORT-001`, `UC-EXPORT-002`, `UC-EXPORT-003` | Parser, layout, SVG, exportación. | `MANUAL_REQUERIDO`. |
| Wireframes administrativos | `admin-wireframes` | Maquetas administrativas simples; no diseñador visual completo tipo Figma. | `UC-PROJ-001`, `UC-IMPORT-001`, `UC-CANVAS-001`, `UC-CANVAS-002`, `UC-EXPORT-001`, `UC-EXPORT-002`, `UC-EXPORT-003` | Parser, layout, tamaño manual, SVG/PNG/Markdown. | `MANUAL_REQUERIDO`. |
| Levantamiento lógico | `logical-business-intake` | Proyecto documental estructurado para importar/exportar Markdown, fijar la lógica estable del negocio, validar coherencia interna, revisar trazas internas y servir como fuente para IA/usuario al preparar otros artefactos compatibles; sin salida PNG/SVG/PDF ni generación automática de proyectos derivados. | `UC-PROJ-001`, `UC-IMPORT-001`, `UC-EXPORT-003`, `UC-HELP-001`, `UC-AI-001` | Parser/exporter Markdown, persistencia `.dms`, tests de catálogo, ayuda y recursos IA. | `MANUAL_REQUERIDO` para revisar UX documental. |
| Grafo lógico del negocio | `logical-business-graph` | Vista visual semántica derivada del levantamiento lógico con nodos tipados, relaciones lógicas, leyenda, validación y exportación visual/documental. | `UC-PROJ-001`, `UC-PROJ-002`, `UC-IMPORT-001`, `UC-EXAMPLE-001`, `UC-CANVAS-001`, `UC-CANVAS-002`, `UC-CANVAS-004`, `UC-EXPORT-001`, `UC-EXPORT-002`, `UC-EXPORT-003`, `UC-HELP-001`, `UC-AI-001` | Dominio, parser/exporter Markdown, SideDock, canvas transversal, persistencia `.dms`, SVG/PNG/Markdown, teoría y recursos IA. | `MANUAL_REQUERIDO` para revisar legibilidad, leyenda y trazabilidad MF→FL→CU→ACC. |
| Grafo libre | `free-graph` | Nodos y relaciones libres con etiquetas visibles y exportación visual/documental. | `UC-PROJ-001`, `UC-IMPORT-001`, `UC-CANVAS-001`, `UC-CANVAS-002`, `UC-CANVAS-004`, `UC-EXPORT-001`, `UC-EXPORT-002`, `UC-EXPORT-003` | Dominio, adapter, etiquetas, selección, drag, exportación. | `MANUAL_REQUERIDO`. |

## Caso agregado en Tanda 003

| Código | Caso de uso | Cobertura esperada | Estado |
|---|---|---|---|
| `UC-IMPORT-002` | Abrir carpeta Markdown con `DirectoryChooser` para cargar varios proyectos compatibles desde una carpeta raíz. | `MarkdownFrontMatterProbeTest`, `MarkdownImportCandidateClassifierTest`, `MarkdownFileTypeSuffixResolverTest`, `MarkdownBatchImportUseCaseTest`, `MarkdownFolderImportSourceTest`, `SMOKE_ABRIR_CARPETA_MARKDOWN.md`. | `MANUAL_REQUERIDO` para verificar selector nativo y apertura real de pestañas. |
| `UC-LB-001` | Mantener el Levantamiento lógico como fuente lógica canónica, sin prometer generación automática ni sincronización cruzada entre proyectos. | `LogicalBusinessRebaselineSourceTest` y futuros tests de guía, gramática, glosario, trazas y validación interna. | `PARCIAL` hasta completar el rediseño específico del módulo lógico. |


## Regla de trazabilidad

Cada fila anterior debe poder vincularse a una de estas categorías:

1. un test unitario;
2. un test de integración;
3. un test contrato/fuente;
4. un test UI/E2E;
5. o una justificación `NO_APLICA`.

Si una acción visible no aparece en esta matriz, se considera **promesa no trazada**. Si aparece en la matriz pero no tiene prueba ni justificación, se considera **deuda de aceptación**.

## Primeros UI/E2E mínimos recomendados

Estos tests se dejan como backlog técnico, no como obligación de esta tanda:

| Código | Nombre sugerido | Objetivo |
|---|---|---|
| `E2E-001` | `ApplicationLaunchUiTest` | Abrir la aplicación y verificar shell, toolbar y bienvenida. |
| `E2E-002` | `CreateProjectFromCatalogUiTest` | Crear proyecto desde catálogo y confirmar workspace activo. |
| `E2E-003` | `ImportOfficialExampleUiTest` | Importar ejemplo oficial sin usar diálogos nativos reales. |
| `E2E-004` | `VisualSelectionAndDragUiTest` | Seleccionar y arrastrar un nodo/clase en canvas. |
| `E2E-005` | `ExportActiveDiagramUiTest` | Exportar SVG/PNG/Markdown usando servicios de diálogo falsos. |
| `E2E-006` | `UmlClassOpenSourceUiTest` | Abrir fuente mediante launcher falso cuando exista ruta resuelta. |

## Relación con docs existentes

Esta matriz complementa, no reemplaza:

- `docs/productizacion/casos-uso/09_matriz_cobertura_casos_uso_por_tipo.md`
- `docs/producto/MATRIZ_CAPACIDADES_REALES.md`
- `docs/testeo/checklists/smoke_ui_mvp.md`
- `docs/testeo/checklists/15_smoke_qa_post_refactor.md`


## UC-CONCEPTUAL-002 — Toolbar contextual conceptual común

- **Objetivo:** operar el modelo conceptual desde la toolbar contextual común sin reescribir su canvas actual.
- **Cobertura:** `ConceptualToolbarCommonContractSourceTest`.
- **Estado:** CUBIERTO_POR_TEST_FUENTE + MANUAL_REQUERIDO.
- **Smoke:** crear modelo conceptual, usar acciones de entidad/atributo/relación, cambiar notación, validar y exportar Markdown/SVG/PNG.


| UC-CONCEPTUAL-003 | Bridges híbridos del canvas conceptual | El modelo conceptual expone selección, comandos, layout y validación por contratos de migración sin reemplazar el render Chen/Crow’s Foot. | `ConceptualHybridCanvasBridgeSourceTest` | CUBIERTO |

## UC-CONCEPTUAL-004 — Retiro legacy conceptual visible

- **Objetivo:** retirar los paneles conceptuales genéricos del shell y cargar el modelo conceptual solo mediante la ruta de workspace común, preservando el dibujo ER actual como implementación interna encapsulada.
- **Cobertura:** `ConceptualLegacyRetirementSourceTest`.
- **Estado:** CUBIERTO_POR_TEST_FUENTE + MANUAL_REQUERIDO.
- **Smoke:** crear/importar modelo conceptual, confirmar que no hay menús ni tabs laterales legacy de estructura/propiedades, editar desde SideDock, cambiar notación, guardar/reabrir `.dms` y exportar Markdown/PNG/SVG.

## UC-SHELL-001 — Refactor de shell y comandos

- Estado: CUBIERTO por source tests.
- Tests: `Tanda9ShellCommandRefactorSourceTest`.
- Alcance: sesiones, pestaña activa, guard de activación y undo/redo delegados a coordinadores dedicados.

### UC-ARCH-010 — Separación inicial de ApplicationServices

| Código | Componente | Caso de uso prometido | Prueba esperada | Estado base |
|---|---|---|---|---|
| `UC-ARCH-010` | Arquitectura application | Separar la fachada monolítica `ApplicationServices` en familias funcionales de proyecto, importación, exportación, catálogo, visuales y documentación, manteniendo compatibilidad transitoria. | `ApplicationServicesFamilyFacadeSourceTest` + smoke de import/export. | `PARCIAL` |

### UC-ARCH-011 — Registry runtime por tipo de proyecto

- Estado: CUBIERTO POR TESTS FUENTE Y UNITARIOS.
- Tests: `DefaultDiagramTypeRuntimeRegistryTest`, `RuntimeRegistryArchitectureSourceTest`.
- Cobertura: todo tipo oficial disponible tiene descriptor runtime; las capacidades visibles de importación, exportación, workspace, ayuda y ejemplo principal quedan representadas sin depender de JavaFX ni de infrastructure concreta.

### UC-PERSIST-012 — Payload común y consistencia `.dms`

- Estado: CUBIERTO POR TESTS DE CONTRATO / REQUIERE VALIDACIÓN LOCAL.
- Tests principales:
  - `DefaultPayloadRuntimeRegistryTest`
  - `PayloadRuntimeArchitectureSourceTest`
  - `DmsProjectPayloadConsistencyValidatorTest`
- Criterio: cada tipo oficial tiene descriptor de payload, `logical-business-graph` cuenta con validación explícita y `.dms` v3 no cambia de formato.

### UC-ENTERPRISE-013 — Canonización documental desde carpeta raíz

- Estado: CUBIERTO POR TESTS DE APLICACIÓN / REQUIERE SMOKE MANUAL.
- Tests principales:
  - `CanonizationArtifactClassifierTest`
  - `CanonizationFlowReportUseCaseTest`
  - `CanonizationFlowArchitectureSourceTest`
- Alcance: interpretar el resultado de Abrir carpeta Markdown como sesión enterprise con fuente lógica canónica lógica, artefactos derivados, omitidos y rechazados.
- Smoke: `docs/testeo/SMOKE_CANONIZACION_DOCUMENTAL_ENTERPRISE.md`.

## UC-CONCEPTUAL-005 — Limpieza visual del workspace conceptual

- **Objetivo:** eliminar la cabecera informativa interna redundante del canvas conceptual cuando el modelo se ejecuta dentro del workbench común, manteniendo el dibujo ER actual.
- **Cobertura:** `ConceptualVisualCleanupSourceTest`.
- **Estado:** CUBIERTO_POR_TEST_FUENTE + MANUAL_REQUERIDO.
- **Smoke:** abrir modelo conceptual, confirmar que el panel interno redundante no aparece, revisar SideDock/canvas y probar deshacer/rehacer con iconos de flecha.


### UC-EXAMPLES-002 — Abrir todos con Levantamiento lógico único

- Estado: CUBIERTO
- Alcance: `Ejemplo → Abrir todos` debe abrir todos los tipos oficiales sin duplicar `logical-business-intake`.
- Pruebas: `DefaultOfficialExampleCatalogTest`, `OfficialExampleUensFamilyTest`, `LogicalBusinessImportExportUiSourceTest`.
- Smoke: Abrir todos y confirmar que solo aparece el levantamiento lógico UENS completo.


- Tanda 015: contrato de alcance y guía académica del Levantamiento lógico — `TANDA_015_CONTRATO_ALCANCE_GUIA_LEVANTAMIENTO_LOGICO.md`.
