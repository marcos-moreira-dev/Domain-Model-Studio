# Onboarding de código y JavaDoc

## Objetivo

Guiar la lectura del código fuente como material de estudio de ingeniería de software y desarrollo de software. Esta guía complementa el onboarding operativo: aquí el foco no es solo ejecutar la app, sino entender por qué el código está organizado así.

## Ruta de lectura recomendada

### 1. Dominio primero

Empieza en:

```txt
src/main/java/com/marcosmoreira/domainmodelstudio/domain
```

Lee los `package-info.java` antes de entrar a clases. Explican la frontera de cada paquete y qué dependencias están prohibidas.

Paquetes clave para iniciar:

```txt
domain/logicalbusinessgraph
domain/logicalbusiness
domain/diagram
domain/layout
domain/style
```

### 2. Grafo lógico del negocio

Ruta sugerida:

```txt
LogicalBusinessGraphDocument
LogicalBusinessGraphNode
LogicalBusinessGraphEdge
LogicalBusinessGraphNodeKind
LogicalBusinessGraphRelationKind
LogicalBusinessGraphIssueSeverity
```

Preguntas de estudio:

```txt
- ¿Por qué el documento es inmutable?
- ¿Qué invariantes protege el constructor?
- ¿Por qué el grafo no depende de JavaFX?
- ¿Cómo se evita que sea un grafo libre renombrado?
- ¿Qué relaciones son bloqueantes y cuáles son advertencias?
```

### 3. Levantamiento lógico

Ruta sugerida:

```txt
LogicalBusinessDocument
LogicalBusinessItem
LogicalBusinessEntityCandidate
LogicalBusinessMaturity
```

Preguntas de estudio:

```txt
- ¿Por qué el levantamiento lógico es fuente lógica canónica?
- ¿Qué significa que una entidad conserve fuentes?
- ¿Cómo se decide si un documento ya es derivable?
- ¿Qué tipo de problemas detecta structuralIssues()?
```

### 4. Aplicación: casos de uso y servicios

Después del dominio, entra en:

```txt
src/main/java/com/marcosmoreira/domainmodelstudio/application
```

Lee primero los paquetes:

```txt
application/catalog
application/logicalbusiness
application/logicalbusiness/derivation
application/export
application/project
application/visual
application/workspace
```

Preguntas de estudio:

```txt
- ¿Qué decide la aplicación que no debería decidir la UI?
- ¿Por qué derivar no significa importar automáticamente?
- ¿Cómo se evita que el catálogo prometa capacidades falsas?
- ¿Qué diferencia hay entre layout persistible y render JavaFX?
- ¿Qué precondiciones valida cada caso de uso antes de delegar a infraestructura?
```

Ruta sugerida:

```txt
DefaultDiagramTypeDefinitions
DefaultDiagramCapabilityCatalog
DefaultCreateWorkspaceUseCase
LogicalBusinessValidationService
LogicalBusinessTraceabilityService
LogicalBusinessDerivationService
LogicalBusinessGraphDraftWriter
VisualLayoutService
ExportMarkdownUseCase
ExportSvgUseCase
```


### 5. Infraestructura: formatos, roundtrip y recursos

Después de aplicación, entra en:

```txt
src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure
```

Lee primero los paquetes:

```txt
infrastructure/markdown
infrastructure/json
infrastructure/svg
infrastructure/resources
infrastructure/pdf
```

Preguntas de estudio:

```txt
- ¿Cómo se decide qué parser Markdown debe ejecutarse?
- ¿Por qué el .dms es el formato durable del proyecto?
- ¿Qué se considera salida editable y qué se considera publicación?
- ¿Cómo se protege el roundtrip guardar → abrir?
- ¿Por qué un prompt IA no es lo mismo que un ejemplo importable?
```

Ruta sugerida:

```txt
DiagramMarkdownImportDispatcher
LogicalBusinessGraphMarkdownParser
LogicalBusinessGraphMarkdownExporter
DmsProjectJsonReader
DmsProjectJsonWriter
DmsProjectFileRepository
SpecializedVisualSvgDiagramExporter
ClasspathAiResourceCatalog
ClasspathAiResourceExporter
DataDictionaryPdfExporter
```


### 6. Presentación: shell, workspaces y canvas

Después de infraestructura, entra en:

```txt
src/main/java/com/marcosmoreira/domainmodelstudio/presentation
```

Lee primero los paquetes:

```txt
presentation/shell
presentation/workspace
presentation/sidedock
presentation/diagramcanvas
presentation/interactivecanvas
presentation/logicalbusinessgraph
```

Preguntas de estudio:

```txt
- ¿Qué coordina el shell y qué no debe decidir?
- ¿Cómo se evita contaminar el modelo conceptual desde workspaces nuevos?
- ¿Qué diferencia hay entre el sidebar legacy conceptual y el SideDock transversal?
- ¿Cómo fluye una selección desde el canvas hasta el ViewModel?
- ¿Por qué la superficie zoomable no conoce UML, C4, BPMN ni Grafo lógico?
- ¿Cómo se insertan puntos intermedios sin deformar la ruta del conector?
```

Ruta sugerida:

```txt
MainShellView
WorkspaceRouteResolver
WorkspaceViewRegistry
SpecializedWorkspaceCoordinator
ProjectValidationCoordinator
WorkbenchSideDock
OperationalHelpCatalog
ZoomableDiagramSurface
InteractiveCanvasSurfaceView
InteractiveCanvasAdapter
CanvasBendPointController
LogicalBusinessGraphViewModel
LogicalBusinessGraphCanvasAdapter
LogicalBusinessGraphWorkbenchContributor
```

Regla de lectura: empieza por la carcasa, luego por el ruteo, después por el SideDock, luego por el canvas común y finalmente por un workspace concreto. No empieces por un renderer si todavía no entiendes el contrato del adaptador.

### 7. Proyecto, layout y estilo

Ruta sugerida:

```txt
DiagramProject
DiagramLayout
ConnectorLayout
NodeLayout
DiagramStyleSheet
RgbaColor
```

Preguntas de estudio:

```txt
- ¿Por qué DiagramProject contiene documentos especializados explícitos?
- ¿Qué se guarda como layout y qué se deja al renderizador?
- ¿Por qué estilo y layout son dominio puro?
```

### 8. Sitio JavaDoc

Después de leer las rutas por capas, genera el sitio JavaDoc:

```bat
scripts\31-generar-javadoc.bat
```

Abrir:

```txt
target\site\apidocs\index.html
```

Guía específica del sitio:

```txt
docs/desarrollo/JAVADOC_SITIO_GUIA.md
```

Preguntas de estudio:

```txt
- ¿Qué paquetes tienen una frontera clara?
- ¿Qué clases conviene buscar con Search?
- ¿Qué contratos se entienden desde JavaDoc y cuáles requieren documentos de arquitectura?
- ¿Dónde se corta la responsabilidad entre dominio, aplicación, infraestructura y presentación?
```

Regla de lectura: el sitio JavaDoc no reemplaza las matrices ni las decisiones de arquitectura. Úsalo para navegar contratos públicos y luego vuelve a los documentos vivos cuando necesites contexto de producto o release.

## Cómo generar JavaDoc

Desde la raíz del proyecto:

```bat
scripts\31-generar-javadoc.bat
```

Abrir:

```txt
target\site\apidocs\index.html
```

## Cómo validar esta tanda

La raíz pública de `scripts/` ya no conserva wrappers históricos por cada JD. La validación vigente se hace con:

```bat
scripts\02-ejecutar-tests.bat
```

Para generar y revisar el sitio JavaDoc, usar:

```bat
scripts\31-generar-javadoc.bat
```

## Regla de lectura

No leas primero la UI. La UI muestra comportamientos, pero el dominio explica las reglas. Para estudiar correctamente, sigue esta secuencia:

```txt
dominio → aplicación → infraestructura → presentación → scripts → smoke manual
```

## Qué no debe hacerse

```txt
- No meter JavaFX en dominio.
- No documentar obviedades en cada getter.
- No usar JavaDoc para justificar deuda técnica no resuelta.
- No cambiar comportamiento solo para mejorar documentación.
```


## Ejemplos pedagógicos en JavaDoc

Después de JD-6, varias clases críticas incluyen párrafos marcados como **Ejemplo pedagógico**. Úsalos para estudiar contratos pequeños sin tener que leer toda la implementación de una vez.

Guía específica:

```txt
docs/desarrollo/JAVADOC_EJEMPLOS_PEDAGOGICOS.md
```

Ruta recomendada dentro del sitio JavaDoc:

```txt
LogicalBusinessGraphDocument
LogicalBusinessGraphRelationKind
LogicalBusinessGraphMarkdownParser
DmsProjectJsonWriter
InteractiveCanvasAdapter
CanvasBendPointController
WorkbenchSideDock
```

Pregunta guía: ¿qué frontera arquitectónica explica cada ejemplo y qué error evita?

## 9. Onboarding vivo de arquitectura

Después de JD-7, la lectura de código tiene una guía de arquitectura viva:

```txt
docs/desarrollo/ONBOARDING_ARQUITECTURA_RUTA_ESTUDIO.md
```

Úsala cuando quieras estudiar el proyecto por flujos y no solo por paquetes. La ruta recomendada es:

```txt
Dominio → aplicación → infraestructura → presentación → scripts/tests → release
```

Preguntas de estudio:

```txt
- ¿Qué clase representa el concepto de dominio?
- ¿Qué servicio o caso de uso coordina la intención?
- ¿Qué formato de infraestructura entra o sale?
- ¿Qué ViewModel o adaptador lo muestra en la UI?
- ¿Qué test impide que la capacidad sea fachada?
```

La guía JD-7 también funciona como índice para usar el sitio JavaDoc: primero abrir `target\site\apidocs\index.html`, leer el `package-info.java` de la capa y luego seguir una ruta de funcionalidad.


## 10. Guía de lectura por casos de uso completos

Después de JD-8, complementa la lectura por capas con recorridos de punta a punta:

```txt
docs/desarrollo/GUIA_CASOS_USO_COMPLETOS.md
```

Esta guía conecta intención de usuario, dominio, aplicación, infraestructura, presentación, tests y scripts. Es la forma recomendada de estudiar ingeniería de software aplicada dentro del proyecto, porque obliga a ver cómo una funcionalidad cruza capas sin romper sus fronteras.

Recorridos sugeridos:

```txt
- Importar ejemplo UENS del Grafo lógico.
- Levantamiento lógico → Grafo lógico derivado.
- Guardar y reabrir `.dms`.
- Exportar Markdown, SVG y recursos IA.
- Interactuar con canvas y puntos intermedios.
- Separar ayuda académica de ayuda operativa.
- Leer evidencia de release candidate.
```

## ADRs pedagógicos y decisiones de diseño — JD-9

Después de revisar el JavaDoc por capas y la guía de casos de uso completos, leer:

```txt
docs/desarrollo/ADR_DECISIONES_DISENO_PEDAGOGICAS.md
```

Esta guía conecta código, tests y decisiones. Sirve para estudiar alternativas descartadas, consecuencias de arquitectura y criterios de diseño que no aparecen en un método aislado.
