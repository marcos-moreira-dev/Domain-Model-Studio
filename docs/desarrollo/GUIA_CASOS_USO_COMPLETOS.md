# Guía de lectura por casos de uso completos — JD-8

## Objetivo

Esta guía permite estudiar Domain Model Studio siguiendo funcionalidades completas, no solo paquetes aislados. La idea es recorrer cada caso de uso desde la intención del usuario hasta las capas que la ejecutan: dominio, aplicación, infraestructura, presentación, pruebas y scripts.

La lectura recomendada para JD-8 no reemplaza el JavaDoc por capas; lo atraviesa. Primero se entiende el contrato del caso de uso y luego se baja a las clases concretas.

## Cómo usar esta guía

Para cada recorrido:

```txt
1. Lee la intención del usuario.
2. Ubica el contrato de dominio.
3. Revisa la política o caso de uso de aplicación.
4. Lee el adaptador de infraestructura, si entra o sale un archivo.
5. Revisa la presentación solo al final.
6. Ejecuta el test o script asociado.
7. Responde las preguntas de estudio.
```

## Recorrido 1 — Importar ejemplo UENS del Grafo lógico

### Intención del usuario

Abrir un Markdown oficial del Grafo lógico del negocio y verlo como proyecto visual editable con SideDock, leyenda, canvas y validación.

### Ruta de lectura

```txt
examples/markdown/diagramas/logical_business_graph_uens_gordito.md
src/main/resources/ai-resources/official-markdown/diagramas/logical_business_graph_uens_gordito.md
DiagramMarkdownImportDispatcher
LogicalBusinessGraphMarkdownParser
LogicalBusinessGraphDocument
LogicalBusinessGraphValidationPolicy
LogicalBusinessGraphLayoutPolicy
LogicalBusinessGraphViewModel
LogicalBusinessGraphCanvasAdapter
LogicalBusinessGraphWorkbenchContributor
LogicalBusinessGraphDiagramCenter
```

### Preguntas de estudio

```txt
- ¿Dónde se reconoce que el Markdown es logical-business-graph?
- ¿Qué parte valida que una relación apunte a nodos existentes?
- ¿Por qué la leyenda de abreviaciones vive en el Markdown y también en la UI?
- ¿Por qué el canvas no conoce MF, FL, CU ni ACC directamente?
- ¿Qué test protege que el ejemplo UENS siga siendo importable?
```

### Tests relacionados

```bat
mvn -Dtest=Tanda38LogicalBusinessGraphUensExampleTest,LogicalBusinessGraphMarkdownParserExporterTest test
```

## Recorrido 2 — Levantamiento lógico → Grafo lógico derivado

### Intención del usuario

Tomar un levantamiento lógico estructurado y generar un borrador Markdown de Grafo lógico para revisarlo, importarlo y editarlo visualmente.

### Ruta de lectura

```txt
LogicalBusinessDocument
LogicalBusinessItem
LogicalBusinessMaturityAssessor
LogicalBusinessValidationService
LogicalBusinessTraceabilityService
LogicalBusinessDerivationService
LogicalBusinessGraphDraftWriter
LogicalBusinessGraphMarkdownParser
LogicalBusinessGraphDocument
LogicalBusinessGraphRelationKind
```

### Preguntas de estudio

```txt
- ¿Por qué derivar no importa automáticamente el resultado?
- ¿Qué significa que el levantamiento lógico sea fuente lógica canónica?
- ¿Cómo se decide qué nodos MF, FL, CU, ACC, RN, PRE, INV y POST aparecen?
- ¿Por qué el writer solo debe generar relaciones aceptadas por LogicalBusinessGraphRelationKind?
- ¿Qué se pierde o se conserva al pasar de expediente lógico a grafo visual?
```

### Tests relacionados

```bat
mvn -Dtest=LogicalBusinessDerivationServiceTest,LogicalBusinessDerivationImportabilityTest test
```

## Recorrido 3 — Guardar y reabrir un proyecto `.dms`

### Intención del usuario

Guardar un proyecto especializado con documento semántico y layout visual, cerrar la aplicación y reabrirlo sin perder contenido ni posiciones.

### Ruta de lectura

```txt
DiagramProject
LogicalBusinessGraphDocument
DiagramLayout
NodeLayout
ConnectorLayout
DmsProjectJsonWriter
DmsProjectJsonReader
DmsProjectFileRepository
OpenProjectUseCaseSpecializedPersistenceTest
SpecializedProjectOpenUseCaseLayoutRoundTripTest
```

### Preguntas de estudio

```txt
- ¿Qué diferencia hay entre documento semántico y layout visual?
- ¿Por qué .dms es el formato durable del proyecto y no SVG?
- ¿Cómo se protege el roundtrip guardar → abrir?
- ¿Qué parte decide el workspace al reabrir?
- ¿Qué pasaría si un nuevo documento especializado no se agrega al JSON reader/writer?
```

### Tests relacionados

```bat
mvn -Dtest=DmsProjectJsonSpecializedRoundTripTest,DmsProjectJsonSpecializedLayoutRoundTripTest,OpenProjectUseCaseSpecializedPersistenceTest test
```

## Recorrido 4 — Exportar Markdown, SVG y recursos IA

### Intención del usuario

Producir salidas revisables o publicables desde un proyecto o desde el catálogo de recursos IA.

### Ruta de lectura

```txt
ProjectExportFormatPolicy
ActiveOutputResolver
LogicalBusinessGraphActiveOutputContributor
ExportMarkdownUseCase
ExportSvgUseCase
LogicalBusinessGraphMarkdownExporter
SpecializedVisualSvgDiagramExporter
LogicalBusinessGraphSvgExportSupport
OfficialAiResourceDescriptors
LogicalBusinessGraphAiResourceDescriptors
ClasspathAiResourceCatalog
ClasspathAiResourceExporter
```

### Preguntas de estudio

```txt
- ¿Qué salida es editable y qué salida es de publicación?
- ¿Por qué el prompt IA no es importable pero su salida esperada sí puede serlo?
- ¿Dónde se evita prometer una exportación que no existe?
- ¿Qué diferencia hay entre exporter de Markdown y exporter SVG?
- ¿Por qué los recursos IA se exportan aunque no haya un proyecto abierto?
```

### Tests relacionados

```bat
mvn -Dtest=ExportMarkdownUseCaseTest,ExportSvgUseCaseTest,ClasspathAiResourceExporterTest,OfficialAiResourceDescriptorsTest test
```

## Recorrido 5 — Interacción visual: seleccionar, mover y agregar puntos intermedios

### Intención del usuario

Seleccionar nodos o relaciones, mover nodos, agregar puntos intermedios y mantener una ruta visual coherente.

### Ruta de lectura

```txt
InteractiveCanvasSurfaceView
InteractiveCanvasAdapter
CanvasBendPointController
VisualLayoutService
ConnectorLayout
LogicalBusinessGraphCanvasAdapter
FreeGraphViewModelCore
BehaviorDiagramViewModel
ArchitectureDiagramViewModel
```

### Preguntas de estudio

```txt
- ¿Qué parte del canvas es transversal y qué parte depende del tipo de diagrama?
- ¿Por qué insertar un bendpoint debe respetar el segmento más cercano?
- ¿Por qué no todos los adapters soportan bendpoints?
- ¿Cómo se evita que el canvas común conozca reglas de negocio?
- ¿Qué test protege el bug de deformación de línea al agregar varios puntos?
```

### Tests relacionados

```bat
mvn -Dtest=VisualLayoutServiceTest,TransversalBendPointInsertionSourceTest,ConnectorBendPointSelectionTest test
```

## Recorrido 6 — Ayuda académica vs ayuda operativa

### Intención del usuario

Distinguir una explicación teórica del tipo de proyecto de una ayuda práctica de la herramienta activa.

### Ruta de lectura

```txt
DefaultTheoryCatalog
TheoryTopicMarkdownParser
help/topics/logical-business-graph.md
ManualContent
ManualDialog
OperationalHelpCatalog
StandardSideDockModules
WorkbenchSideDock
LogicalBusinessGraphWorkbenchContributor
```

### Preguntas de estudio

```txt
- ¿Por qué la guía académica no debe explicar botones?
- ¿Por qué el SideDock sí debe explicar acciones operativas?
- ¿Cómo se evita duplicar teoría en la ayuda operacional?
- ¿Qué test exige que cada tipo visible tenga teoría y figura didáctica?
- ¿Qué frontera de producto protege esta separación?
```

### Tests relacionados

```bat
mvn -Dtest=DefaultTheoryCatalogTest,DefaultTheoryCatalogFigureCoverageTest,DefaultTheoryCatalogMicroWikipediaTest test
```

## Recorrido 7 — Release candidate y evidencia

### Intención del usuario

Comprobar que una tanda queda cerrada con pruebas, documentación y scripts, no solo con cambios de código.

### Ruta de lectura

```txt
docs/diagnostico/ESTADO_AUDITORIA_ACTUAL.md
docs/raiz/PLAN_TANDAS_RESTANTES.md
docs/testeo/RELEASE_CANDIDATE_TANDA_31.md
docs/testeo/reportes/REPORTE_RELEASE_CANDIDATE_TANDA_31.md
scripts/02-ejecutar-tests.bat
scripts/29-validar-tanda31-release-candidate-local.bat
scripts/16-release-candidate.bat
```

### Preguntas de estudio

```txt
- ¿Qué evidencia permite declarar una tanda como cerrada?
- ¿Qué diferencia hay entre suite automatizada verde y instalable aprobado?
- ¿Por qué las matrices anti-fachada son parte del producto?
- ¿Qué documentos mandan si una bitácora histórica contradice el estado actual?
```

## Método de estudio recomendado

```txt
Día 1: Recorrido 1 y 2, porque explican el núcleo del Grafo lógico.
Día 2: Recorrido 3 y 4, porque explican persistencia y salidas.
Día 3: Recorrido 5, porque conecta canvas transversal y comportamiento visual.
Día 4: Recorrido 6 y 7, porque explican ayuda, documentación viva y release.
```

## Criterio de cierre JD-8

JD-8 queda completa cuando:

```txt
- existe esta guía de casos de uso completos;
- el onboarding principal apunta a ella;
- el onboarding JavaDoc la conecta con el sitio generado;
- los package-info de capas mencionan lectura transversal por casos de uso;
- existe un test fuente que protege el contenido mínimo;
- existe script focalizado para validar la tanda.
```

## Lectura complementaria: decisiones de diseño

Cada caso de uso completo muestra cómo fluye una funcionalidad. Para estudiar el **por qué** de las fronteras usadas en esos flujos, leer:

```txt
docs/desarrollo/ADR_DECISIONES_DISENO_PEDAGOGICAS.md
```

Especialmente útil después de los recorridos de Grafo lógico, persistencia `.dms`, canvas transversal, ayuda académica/operativa y Release Candidate.


## Ruta JD-8

Ruta JD-8: lectura pedagógica de casos de uso completos y relación con la documentación viva.
