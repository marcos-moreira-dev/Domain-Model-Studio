# Onboarding vivo de arquitectura y ruta de estudio

## Propósito

Este documento convierte el proyecto en una ruta de estudio viva. No reemplaza el JavaDoc ni las pruebas: los conecta para que un lector pueda entender cómo se organiza Domain Model Studio desde decisiones de arquitectura, contratos de código y flujos reales de uso.

La lectura recomendada es progresiva:

```txt
Dominio → aplicación → infraestructura → presentación → scripts/tests → release
```

La regla central es no empezar por JavaFX. Primero se entiende el dominio y después se observa cómo la UI adapta ese dominio.

## Mapa de capas

| Capa | Qué se estudia | Qué no debe hacer |
|---|---|---|
| Dominio | Agregados, invariantes, reglas semánticas, layouts como datos | No abrir ventanas, no parsear archivos, no depender de JavaFX |
| Aplicación | Casos de uso, catálogos, validación, derivación, export policies | No dibujar, no leer archivos concretos, no conocer controles JavaFX |
| Infraestructura | Markdown, `.dms`, SVG, PDF, recursos IA, archivos | No inventar reglas de negocio |
| Presentación | Shell, workspaces, SideDock, canvas, ViewModels | No mutar dominio sin pasar por contratos claros |
| Scripts/tests | Validación reproducible, smoke, release, guardarraíles | No sustituir revisión manual cuando el smoke lo exige |

## Ruta base para estudiar una funcionalidad

Para cada funcionalidad nueva o existente, leer en este orden:

```txt
1. Dominio: ¿qué concepto existe y qué invariantes protege?
2. Aplicación: ¿qué caso de uso o servicio coordina la intención?
3. Infraestructura: ¿qué formato entra o sale?
4. Presentación: ¿qué ViewModel/adaptador lo expone al usuario?
5. Tests: ¿qué guardarraíl prueba que no sea fachada?
6. Docs: ¿qué documento explica el contrato al lector futuro?
```

## Ruta recomendada 1: Grafo lógico del negocio

```txt
LogicalBusinessGraphDocument
LogicalBusinessGraphValidationPolicy
LogicalBusinessGraphRelationKind
LogicalBusinessGraphMarkdownParser
LogicalBusinessGraphMarkdownExporter
DmsProjectJsonReader / DmsProjectJsonWriter
LogicalBusinessGraphViewModel
LogicalBusinessGraphCanvasAdapter
LogicalBusinessGraphWorkbenchContributor
```

Preguntas de estudio:

```txt
- ¿Dónde vive la semántica del grafo?
- ¿Por qué el grafo no es FreeGraphDocument?
- ¿Qué significa que MF → FL → CU → ACC sea backbone y no árbol rígido?
- ¿Qué validaciones son bloqueantes y cuáles son advertencias?
- ¿Dónde se separa layout visual de documento semántico?
```

## Ruta recomendada 2: Levantamiento lógico a derivación

```txt
LogicalBusinessDocument
LogicalBusinessValidationService
LogicalBusinessTraceabilityService
LogicalBusinessDerivationService
LogicalBusinessGraphDraftWriter
DiagramMarkdownImportDispatcher
```

Preguntas de estudio:

```txt
- ¿Por qué derivar no significa importar automáticamente?
- ¿Qué información se conserva como referencia de trazabilidad?
- ¿Qué diferencia hay entre expediente lógico y vista visual?
```

## Ruta recomendada 3: Canvas transversal

```txt
ZoomableDiagramSurface
InteractiveCanvasSurfaceView
InteractiveCanvasAdapter
CanvasBendPointController
VisualLayoutService
```

Preguntas de estudio:

```txt
- ¿Por qué la superficie no conoce los tipos de diagrama?
- ¿Qué responsabilidad tiene el adaptador?
- ¿Cómo se insertan puntos intermedios sin deformar la línea?
- ¿Dónde se persiste el movimiento y dónde solo se renderiza?
```

## Ruta recomendada 4: SideDock y ayuda operativa

```txt
WorkbenchSideDock
SideDockModule
StandardSideDockModules
OperationalHelpCatalog
LogicalBusinessGraphWorkbenchContributor
```

Preguntas de estudio:

```txt
- ¿Qué módulos son transversales y cuáles son específicos?
- ¿Por qué la ayuda operativa no debe mezclarse con la guía académica?
- ¿Cómo se evita usar el sidebar legacy del modelo conceptual?
```

## Scripts que acompañan la lectura

| Necesidad | Script |
|---|---|
| Generar JavaDoc | `scripts\31-generar-javadoc.bat` |
| Validar onboarding JD-7 | `scripts\02-ejecutar-tests.bat` |
| Ejecutar tests globales | `scripts\02-ejecutar-tests.bat` |
| Validar release candidate | `scripts\29-validar-tanda31-release-candidate-local.bat` |

## Cómo usar el JavaDoc con esta guía

1. Generar el sitio con `scripts\31-generar-javadoc.bat`.
2. Abrir `target\site\apidocs\index.html`.
3. Buscar primero el paquete, no la clase suelta.
4. Leer `package-info.java` de la capa.
5. Seguir una de las rutas recomendadas de este documento.
6. Confirmar el contrato con el test o script asociado.

## Señales de buena lectura

Un lector entiende la arquitectura cuando puede explicar:

```txt
- por qué el dominio no depende de infraestructura;
- por qué el catálogo no debe prometer capacidades no implementadas;
- por qué el .dms es formato durable y SVG/PDF son salidas;
- por qué SideDock y canvas transversal son infraestructura de presentación;
- por qué el modelo conceptual queda protegido;
- qué test impide que una promesa se vuelva fachada.
```

## Relación con próximas tandas

JD-8 convirtió esta ruta viva en recorridos por casos de uso completos. JD-9 agrega decisiones de arquitectura tipo ADR para explicar alternativas descartadas y consecuencias.

## JD-8 — lectura por casos de uso completos

Cuando ya entiendas la ruta por capas, pasa a la guía transversal:

```txt
docs/desarrollo/GUIA_CASOS_USO_COMPLETOS.md
```

Esa guía permite estudiar casos reales de punta a punta: importar ejemplo UENS, derivar desde levantamiento lógico, guardar/reabrir `.dms`, exportar, interactuar con canvas y revisar evidencia de release. La diferencia frente a esta guía de arquitectura es que JD-8 no empieza por paquetes, sino por intenciones completas de usuario.

## JD-9 — decisiones de diseño

La ruta de arquitectura se completa con los ADRs pedagógicos:

```txt
docs/desarrollo/ADR_DECISIONES_DISENO_PEDAGOGICAS.md
```

Leerlos al final permite responder por qué se protegió el modelo conceptual, por qué el Grafo lógico tiene dominio propio, por qué el SideDock es transversal, por qué la ayuda académica se separa de la ayuda operativa y por qué el canvas común usa adaptadores.
