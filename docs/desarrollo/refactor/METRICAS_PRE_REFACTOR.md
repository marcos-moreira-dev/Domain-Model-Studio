# Métricas pre-refactor — Tanda 15

Generado: `2026-05-19T18:00:16`

## Alcance

- Fuente analizada: `src/main/java`.
- Métricas estáticas aproximadas: líneas físicas, líneas de código sin comentarios simples y métodos detectados por patrón.
- No sustituye revisión humana; sirve para priorizar refactor seguro.

## Resumen

- Archivos Java: **893**
- Líneas físicas totales: **82900**
- Líneas de código aproximadas: **71644**
- Métodos aproximados detectados: **10969**

### Distribución por tamaño

| Nivel | Criterio | Archivos |
|---|---:|---:|
| Crítico | >= 700 líneas | 8 |
| Muy alto | >= 450 líneas | 9 |
| Alto | >= 300 líneas | 50 |
| Observación | >= 180 líneas | 65 |
| Normal | < 180 líneas | 761 |

## Top 40 archivos por tamaño

| # | Nivel | Líneas | Código aprox. | Métodos aprox. | Archivo |
|---:|---|---:|---:|---:|---|
| 1 | Crítico | 1191 | 1184 | 295 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellCommandHandler.java` |
| 2 | Crítico | 1063 | 959 | 168 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/canvas/DiagramCanvasView.java` |
| 3 | Crítico | 1048 | 1001 | 176 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassDiagramViewModel.java` |
| 4 | Crítico | 957 | 880 | 132 | `src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/json/DmsProjectJsonReader.java` |
| 5 | Crítico | 916 | 810 | 141 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/canvas/DiagramCanvasViewModel.java` |
| 6 | Crítico | 815 | 754 | 70 | `src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/json/DmsProjectJsonWriter.java` |
| 7 | Crítico | 806 | 742 | 138 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/inspector/InspectorViewModel.java` |
| 8 | Crítico | 772 | 696 | 98 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/InteractiveCanvasSurfaceView.java` |
| 9 | Muy alto | 690 | 616 | 100 | `src/main/java/com/marcosmoreira/domainmodelstudio/application/ApplicationServices.java` |
| 10 | Muy alto | 624 | 550 | 70 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellView.java` |
| 11 | Muy alto | 567 | 506 | 67 | `src/main/java/com/marcosmoreira/domainmodelstudio/application/visual/DefaultVisualLayoutGenerator.java` |
| 12 | Muy alto | 557 | 499 | 94 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/modulemap/ModuleMapViewModel.java` |
| 13 | Muy alto | 534 | 505 | 67 | `src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/svg/specialized/SpecializedVisualSvgWriter.java` |
| 14 | Muy alto | 487 | 457 | 40 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/toolbar/DefaultDiagramToolbarActionProvider.java` |
| 15 | Muy alto | 467 | 415 | 38 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/inspector/InspectorView.java` |
| 16 | Muy alto | 464 | 424 | 35 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/datadictionary/DataDictionaryEditorView.java` |
| 17 | Muy alto | 450 | 403 | 81 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/behavior/BehaviorDiagramViewModel.java` |
| 18 | Alto | 449 | 423 | 71 | `src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/markdown/UmlClassMarkdownParser.java` |
| 19 | Alto | 449 | 445 | 90 | `src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/sourcecode/typescript/TypeScriptModelExtractor.java` |
| 20 | Alto | 448 | 408 | 63 | `src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/pdf/SimplePdfDocument.java` |
| 21 | Alto | 448 | 393 | 76 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/freegraph/FreeGraphViewModelCore.java` |
| 22 | Alto | 448 | 403 | 72 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/screenflow/ScreenFlowViewModel.java` |
| 23 | Alto | 448 | 443 | 85 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/wireframe/WireframeViewModel.java` |
| 24 | Alto | 441 | 397 | 77 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassCanvasAdapter.java` |
| 25 | Alto | 429 | 383 | 75 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/architecture/ArchitectureDiagramViewModel.java` |
| 26 | Alto | 426 | 370 | 71 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/freegraph/FreeGraphCanvasAdapter.java` |
| 27 | Alto | 416 | 328 | 56 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/diagramcanvas/ZoomableDiagramSurface.java` |
| 28 | Alto | 413 | 372 | 50 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/commands/ImportCommandHandler.java` |
| 29 | Alto | 411 | 400 | 7 | `src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/resources/OfficialAiResourceDescriptors.java` |
| 30 | Alto | 401 | 373 | 55 | `src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/markdown/DataDictionaryMarkdownParser.java` |
| 31 | Alto | 400 | 365 | 31 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/behavior/BehaviorRenderKit.java` |
| 32 | Alto | 400 | 355 | 48 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/canvas/ChenDiagramRenderer.java` |
| 33 | Alto | 395 | 341 | 50 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/dialogs/ManualDialog.java` |
| 34 | Alto | 383 | 341 | 36 | `src/main/java/com/marcosmoreira/domainmodelstudio/domain/layout/ConnectorLayout.java` |
| 35 | Alto | 375 | 339 | 43 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/canvas/CrowsFootDiagramRenderer.java` |
| 36 | Alto | 371 | 314 | 63 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/behavior/SequenceCanvasAdapter.java` |
| 37 | Alto | 370 | 332 | 51 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/CanvasAppearancePanel.java` |
| 38 | Alto | 370 | 338 | 34 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassStructurePanel.java` |
| 39 | Alto | 367 | 319 | 64 | `src/main/java/com/marcosmoreira/domainmodelstudio/presentation/behavior/BehaviorCanvasAdapter.java` |
| 40 | Alto | 365 | 335 | 56 | `src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/markdown/BehaviorMarkdownParser.java` |

## Paquetes más pesados

| # | Líneas | Código aprox. | Archivos | Package |
|---:|---:|---:|---:|---|
| 1 | 5330 | 4867 | 31 | `com.marcosmoreira.domainmodelstudio.infrastructure.markdown` |
| 2 | 4718 | 3820 | 65 | `com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas` |
| 3 | 4629 | 4120 | 30 | `com.marcosmoreira.domainmodelstudio.presentation.umlclass` |
| 4 | 4035 | 3449 | 34 | `com.marcosmoreira.domainmodelstudio.application.visual` |
| 5 | 3805 | 3355 | 27 | `com.marcosmoreira.domainmodelstudio.presentation.dialogs` |
| 6 | 3496 | 3125 | 18 | `com.marcosmoreira.domainmodelstudio.presentation.shell` |
| 7 | 3432 | 3011 | 11 | `com.marcosmoreira.domainmodelstudio.presentation.canvas` |
| 8 | 2797 | 2469 | 14 | `com.marcosmoreira.domainmodelstudio.presentation.behavior` |
| 9 | 2253 | 2054 | 9 | `com.marcosmoreira.domainmodelstudio.infrastructure.json` |
| 10 | 2198 | 1957 | 15 | `com.marcosmoreira.domainmodelstudio.presentation.toolbar` |
| 11 | 2178 | 1910 | 12 | `com.marcosmoreira.domainmodelstudio.presentation.freegraph` |
| 12 | 2048 | 1829 | 12 | `com.marcosmoreira.domainmodelstudio.presentation.wireframe` |
| 13 | 1955 | 1655 | 38 | `com.marcosmoreira.domainmodelstudio.application.sourcecode` |
| 14 | 1686 | 1486 | 9 | `com.marcosmoreira.domainmodelstudio.presentation.modulemap` |
| 15 | 1611 | 1418 | 10 | `com.marcosmoreira.domainmodelstudio.presentation.architecture` |
| 16 | 1480 | 1303 | 9 | `com.marcosmoreira.domainmodelstudio.presentation.screenflow` |
| 17 | 1440 | 1132 | 18 | `com.marcosmoreira.domainmodelstudio.presentation.diagramcanvas` |
| 18 | 1337 | 1213 | 6 | `com.marcosmoreira.domainmodelstudio.presentation.datadictionary` |
| 19 | 1275 | 1158 | 3 | `com.marcosmoreira.domainmodelstudio.presentation.inspector` |
| 20 | 1267 | 1102 | 21 | `com.marcosmoreira.domainmodelstudio.application.umlclass` |
| 21 | 1020 | 827 | 9 | `com.marcosmoreira.domainmodelstudio.domain.diagram` |
| 22 | 999 | 856 | 11 | `com.marcosmoreira.domainmodelstudio.application.layout` |
| 23 | 980 | 833 | 21 | `com.marcosmoreira.domainmodelstudio.application.editing` |
| 24 | 946 | 875 | 6 | `com.marcosmoreira.domainmodelstudio.infrastructure.svg.specialized` |
| 25 | 943 | 757 | 19 | `com.marcosmoreira.domainmodelstudio.presentation.drawing` |
| 26 | 917 | 815 | 5 | `com.marcosmoreira.domainmodelstudio.presentation.rolespermissions` |
| 27 | 898 | 781 | 11 | `com.marcosmoreira.domainmodelstudio.presentation.exportable` |
| 28 | 887 | 707 | 15 | `com.marcosmoreira.domainmodelstudio.presentation.workbench` |
| 29 | 834 | 743 | 7 | `com.marcosmoreira.domainmodelstudio.presentation.shell.commands` |
| 30 | 829 | 661 | 12 | `com.marcosmoreira.domainmodelstudio.domain.layout` |

## Lectura rápida

- Priorizar primero clases `Crítico` y `Muy alto`, pero solo si hay guardarraíles de comportamiento.
- No partir clases por tamaño solamente: partir por responsabilidad observable y contratos existentes.
- Mantener el modelo conceptual protegido durante las primeras tandas de refactor.
