# Métricas pre-refactor — Tanda 15

Generado: `2026-05-29T18:49:33`

## Alcance

- Fuente analizada: `src/main/java`.
- Métricas estáticas aproximadas: líneas físicas, líneas de código sin comentarios simples y métodos detectados por patrón.
- No sustituye revisión humana; sirve para priorizar refactor seguro.

## Resumen

- Archivos Java: **1254**
- Líneas físicas totales: **114431**
- Líneas de código aproximadas: **97548**
- Métodos aproximados detectados: **14965**

### Distribución por tamaño

| Nivel | Criterio | Archivos |
|---|---:|---:|
| Crítico | >= 700 líneas | 4 |
| Muy alto | >= 450 líneas | 12 |
| Alto | >= 300 líneas | 60 |
| Observación | >= 180 líneas | 106 |
| Normal | < 180 líneas | 1072 |

## Top 40 archivos por tamaño

| # | Nivel | Líneas | Código aprox. | Métodos aprox. | Archivo |
|---:|---|---:|---:|---:|---|
| 1 | Crítico | 1075 | 970 | 171 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/canvas/DiagramCanvasView.java` |
| 2 | Crítico | 879 | 774 | 136 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/canvas/DiagramCanvasViewModel.java` |
| 3 | Crítico | 847 | 834 | 234 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellCommandHandler.java` |
| 4 | Crítico | 806 | 742 | 138 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/inspector/InspectorViewModel.java` |
| 5 | Muy alto | 650 | 584 | 80 | `src/main/java/com/marcosmoreira/domainmodelstudio/application/visual/DefaultVisualLayoutGenerator.java` |
| 6 | Muy alto | 616 | 555 | 97 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/logicalbusinessgraph/LogicalBusinessGraphViewModel.java` |
| 7 | Muy alto | 598 | 586 | 132 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassDiagramViewModel.java` |
| 8 | Muy alto | 567 | 512 | 98 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/modulemap/ModuleMapViewModel.java` |
| 9 | Muy alto | 556 | 421 | 116 | `src/main/java/com/marcosmoreira/domainmodelstudio/application/ApplicationServices.java` |
| 10 | Muy alto | 520 | 485 | 78 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/freegraph/FreeGraphViewModel.java` |
| 11 | Muy alto | 519 | 475 | 39 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/datadictionary/DataDictionaryEditorView.java` |
| 12 | Muy alto | 479 | 426 | 41 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/inspector/InspectorView.java` |
| 13 | Muy alto | 471 | 411 | 77 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/freegraph/FreeGraphCanvasAdapter.java` |
| 14 | Muy alto | 470 | 448 | 83 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassCanvasAdapter.java` |
| 15 | Muy alto | 450 | 377 | 57 | `src/main/java/com/marcosmoreira/domainmodelstudio/application/visual/VisualLayoutService.java` |
| 16 | Muy alto | 450 | 398 | 79 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/freegraph/FreeGraphViewModelCore.java` |
| 17 | Alto | 449 | 445 | 90 | `src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/sourcecode/typescript/TypeScriptModelExtractor.java` |
| 18 | Alto | 447 | 440 | 49 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/InteractiveCanvasSurfaceView.java` |
| 19 | Alto | 447 | 439 | 88 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/wireframe/WireframeViewModel.java` |
| 20 | Alto | 443 | 406 | 44 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassStructurePanel.java` |
| 21 | Alto | 439 | 343 | 57 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/diagramcanvas/ZoomableDiagramSurface.java` |
| 22 | Alto | 438 | 402 | 75 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/screenflow/ScreenFlowViewModel.java` |
| 23 | Alto | 437 | 394 | 79 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/architecture/ArchitectureDiagramViewModel.java` |
| 24 | Alto | 435 | 395 | 37 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/behavior/BehaviorRenderKit.java` |
| 25 | Alto | 434 | 371 | 71 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/behavior/SequenceCanvasAdapter.java` |
| 26 | Alto | 434 | 377 | 43 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellView.java` |
| 27 | Alto | 428 | 426 | 87 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/behavior/BehaviorDiagramViewModel.java` |
| 28 | Alto | 426 | 376 | 67 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/modulemap/ModuleMapCanvasAdapter.java` |
| 29 | Alto | 425 | 377 | 85 | `src/main/java/com/marcosmoreira/domainmodelstudio/application/logicalbusiness/derivation/LogicalBusinessGraphDraftWriter.java` |
| 30 | Alto | 420 | 365 | 69 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/behavior/BehaviorCanvasAdapter.java` |
| 31 | Alto | 417 | 356 | 63 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/logicalbusiness/LogicalBusinessViewModel.java` |
| 32 | Alto | 413 | 388 | 62 | `src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/markdown/UmlClassMarkdownParser.java` |
| 33 | Alto | 412 | 385 | 73 | `src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/markdown/WireframeMarkdownParser.java` |
| 34 | Alto | 411 | 369 | 50 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/commands/ImportCommandHandler.java` |
| 35 | Alto | 409 | 363 | 49 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/canvas/ChenDiagramRenderer.java` |
| 36 | Alto | 405 | 351 | 47 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/InteractiveCanvasPngExporter.java` |
| 37 | Alto | 403 | 360 | 87 | `src/main/java/com/marcosmoreira/domainmodelstudio/domain/behavior/SequenceCombinedFragmentSpec.java` |
| 38 | Alto | 402 | 364 | 53 | `src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/pdf/SimplePdfDocument.java` |
| 39 | Alto | 399 | 368 | 66 | `src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/markdown/RolesPermissionsMarkdownParser.java` |
| 40 | Alto | 398 | 355 | 54 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassDiagramLayoutCoordinator.java` |

## Paquetes más pesados

| # | Líneas | Código aprox. | Archivos | Package |
|---:|---:|---:|---:|---|
| 1 | 6683 | 5388 | 87 | `com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas` |
| 2 | 5929 | 5322 | 37 | `com.marcosmoreira.domainmodelstudio.infrastructure.markdown` |
| 3 | 5276 | 4706 | 34 | `com.marcosmoreira.domainmodelstudio.presentation.umlclass` |
| 4 | 4968 | 4296 | 36 | `com.marcosmoreira.domainmodelstudio.presentation.shell` |
| 5 | 4887 | 4273 | 33 | `com.marcosmoreira.domainmodelstudio.presentation.dialogs` |
| 6 | 4721 | 4029 | 38 | `com.marcosmoreira.domainmodelstudio.application.visual` |
| 7 | 4296 | 3767 | 33 | `com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness` |
| 8 | 3616 | 3137 | 14 | `com.marcosmoreira.domainmodelstudio.presentation.canvas` |
| 9 | 3449 | 3020 | 23 | `com.marcosmoreira.domainmodelstudio.infrastructure.json` |
| 10 | 3171 | 2845 | 15 | `com.marcosmoreira.domainmodelstudio.presentation.behavior` |
| 11 | 2778 | 2417 | 27 | `com.marcosmoreira.domainmodelstudio.presentation.toolbar` |
| 12 | 2562 | 2262 | 13 | `com.marcosmoreira.domainmodelstudio.presentation.freegraph` |
| 13 | 2094 | 1863 | 12 | `com.marcosmoreira.domainmodelstudio.presentation.wireframe` |
| 14 | 2067 | 1753 | 39 | `com.marcosmoreira.domainmodelstudio.application.sourcecode` |
| 15 | 2010 | 1754 | 10 | `com.marcosmoreira.domainmodelstudio.presentation.logicalbusinessgraph` |
| 16 | 1910 | 1691 | 9 | `com.marcosmoreira.domainmodelstudio.presentation.modulemap` |
| 17 | 1849 | 1632 | 10 | `com.marcosmoreira.domainmodelstudio.presentation.architecture` |
| 18 | 1812 | 1605 | 15 | `com.marcosmoreira.domainmodelstudio.presentation.shell.commands` |
| 19 | 1690 | 1482 | 21 | `com.marcosmoreira.domainmodelstudio.infrastructure.markdown.logicalbusiness` |
| 20 | 1549 | 1368 | 9 | `com.marcosmoreira.domainmodelstudio.presentation.screenflow` |
| 21 | 1480 | 1159 | 18 | `com.marcosmoreira.domainmodelstudio.presentation.diagramcanvas` |
| 22 | 1425 | 1295 | 6 | `com.marcosmoreira.domainmodelstudio.presentation.datadictionary` |
| 23 | 1394 | 1172 | 27 | `com.marcosmoreira.domainmodelstudio.presentation.exportable` |
| 24 | 1322 | 1131 | 22 | `com.marcosmoreira.domainmodelstudio.application.umlclass` |
| 25 | 1315 | 1113 | 16 | `com.marcosmoreira.domainmodelstudio.application.logicalbusiness.derivation` |
| 26 | 1287 | 1169 | 3 | `com.marcosmoreira.domainmodelstudio.presentation.inspector` |
| 27 | 1283 | 1148 | 16 | `com.marcosmoreira.domainmodelstudio.infrastructure.svg.specialized` |
| 28 | 1122 | 862 | 18 | `com.marcosmoreira.domainmodelstudio.domain.logicalbusiness` |
| 29 | 1103 | 893 | 9 | `com.marcosmoreira.domainmodelstudio.domain.diagram` |
| 30 | 1096 | 863 | 19 | `com.marcosmoreira.domainmodelstudio.presentation.workbench` |

## Lectura rápida

- Priorizar primero clases `Crítico` y `Muy alto`, pero solo si hay guardarraíles de comportamiento.
- No partir clases por tamaño solamente: partir por responsabilidad observable y contratos existentes.
- Mantener el modelo conceptual protegido durante las primeras tandas de refactor.
