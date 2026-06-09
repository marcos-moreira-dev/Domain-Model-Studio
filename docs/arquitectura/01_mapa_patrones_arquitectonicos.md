# Tanda 1 — Mapa de patrones arquitectónicos

## Propósito

Este documento deja explícitas las familias de patrones que ya existen o que deben consolidarse en Domain Model Studio. La intención no es inflar el proyecto con patrones decorativos, sino hacer que cada pieza tenga un rol estudiable, mantenible y coherente con SOLID.

Regla de decisión:

```txt
Si se repite contrato        → interfaz / Port
Si se repite implementación → Support / Delegate
Si se repite creación        → Factory
Si se repite decisión        → Policy / Strategy
Si se repite coordinación   → Coordinator / Mediator
Si simplifica un subsistema  → Facade
Si registra capacidades      → Registry
```

## Decisión aplicada en esta tanda

`DiagramDrawingToolkit` fue renombrado a `DiagramDrawingFacade` porque su rol real es una fachada ligera sobre primitivas, fábricas y calculadores de dibujo. El nombre nuevo hace explícito el patrón estructural que cumple y ayuda a estudiar la arquitectura.

Responsabilidad de `DiagramDrawingFacade`:

- centralizar acceso a primitivas de dibujo;
- centralizar fábricas de nodos, conectores, flechas, textos y selección;
- mantener a los render kits libres de instanciar manualmente todas esas dependencias;
- no conocer UML, BPMN, C4, wireframes ni modelo conceptual como familias concretas.

No debe manejar:

- interacción de usuario;
- selección semántica;
- persistencia;
- exportación completa;
- reglas de dominio.

## Patrones creacionales

### Factory

Uso actual correcto:

- `ApplicationServicesFactory`
- `InfrastructureServicesFactory`
- `NewProjectFactory`
- `ConceptualDataDictionaryDraftFactory`
- `VisualLayoutSpecificationFactory`
- `DiagramNodeFactory`
- `DiagramConnectorFactory`
- `DiagramArrowFactory`
- `DiagramTextFactory`
- `SpecializedSvgModelFactory`
- `ManualFigureNodeFactory`

Criterio:

> Usar Factory cuando construir el objeto requiere composición, defaults, familias de objetos o reglas de creación no triviales.

Evitar factories vacías que solo llamen a `new` sin aportar intención.

## Patrones estructurales

### Facade

Uso actual o candidato:

- `DiagramDrawingFacade` — fachada real de dibujo para render kits.
- `ApplicationServices` — fachada grande de casos de uso; se mantiene por ahora, pero queda marcada como candidata a división en subfachadas.

Candidatos futuros para dividir `ApplicationServices`:

- `ConceptualUseCaseFacade`
- `VisualDiagramUseCaseFacade`
- `DocumentUseCaseFacade`
- `ExportUseCaseFacade`
- `CatalogUseCaseFacade`
- `WorkspaceUseCaseFacade`

Criterio:

> Usar Facade cuando una clase simplifica el acceso a un subsistema compuesto sin apropiarse de sus reglas internas.

### Adapter

Uso actual:

- `ArchitectureCanvasAdapter`
- `BehaviorCanvasAdapter`
- `SequenceCanvasAdapter`
- `ModuleMapCanvasAdapter`
- `ScreenFlowCanvasAdapter`
- `UmlClassCanvasAdapter`
- `WireframeCanvasAdapter`
- `ConceptualCanvasAdapter`
- `InMemoryInteractiveCanvasAdapter`

Responsabilidad correcta:

> Traducir el modelo específico de un diagrama hacia el contrato común del canvas y devolver cambios del canvas hacia el modelo/layout correspondiente.

Deuda detectada:

Muchos adapters repiten infraestructura de selección, IDs, `dirty state`, bounds y bendpoints. Esa duplicación no se resuelve con otra interfaz solamente; se resolverá en Tanda 3 con Supports reutilizables.

### Bridge

La arquitectura objetivo del canvas se interpreta como un puente entre:

```txt
Canvas común
├── Adapter de dominio
├── RenderKit
├── ShapeKit
└── InteractionProfile
```

Criterio:

> El canvas no debe saber de UML, BPMN o C4; debe operar sobre nodos, conectores, etiquetas, selección y viewport.

### Composite

Aplicación esperada:

- actor UML compuesto por cabeza, cuerpo, brazos, piernas y etiqueta;
- clase UML compuesta por compartimentos;
- gateway BPMN compuesto por rombo y marcador interno;
- participante de secuencia compuesto por cabecera, línea de vida y activaciones;
- componente de wireframe compuesto por figura y texto.

Regla:

> Muchas primitivas internas, una sola raíz interactiva para el usuario.

## Patrones de comportamiento

### Policy / Strategy

Uso actual:

- `ExportTargetPathPolicy`
- `ExportFolderNamePolicy`
- `ProjectExportFormatPolicy`
- `DiagramCapabilityPresentationPolicy`
- `WorkspaceTypeRoutingPolicy`
- `WorkbenchPanelPolicy`
- `ArchitectureLayoutPolicy`
- `UmlClassLayoutPolicy`
- `UmlBehaviorLayoutPolicy`
- `SequenceTimelineLayoutPolicy`
- `BehaviorProcessLayoutPolicy`
- `AdminApplicationsLayoutPolicy`
- `SequenceMessageOrderPolicy`
- políticas de validación por familia.

Criterio:

> Usar Policy/Strategy cuando se encapsula una decisión variable: ruta, layout, validación, capacidades, formato o comportamiento por tipo.

En código del producto, `Policy` se mantiene como sufijo preferido cuando la clase representa una regla de decisión estable.

### Command / Handler

Uso actual:

- handlers del shell;
- acciones de toolbar;
- casos de uso en aplicación.

Deuda detectada:

`MainShellCommandHandler` todavía concentra demasiada coordinación. En Tanda 14 debe seguir reduciéndose mediante coordinadores más pequeños.

### Coordinator / Mediator

Uso actual:

- `SpecializedWorkspaceCoordinator`
- `ProjectValidationCoordinator`
- `ClientBatchExportCoordinator`
- `CanvasLayoutCommandCoordinator`

Candidatos futuros:

- `ProjectSessionCoordinator`
- `EditorTabCoordinator`
- `ExportCoordinator`
- `WorkspaceActivationCoordinator`
- `SideDockCoordinator`

Criterio:

> Usar Coordinator cuando una pieza conecta vistas, estado, casos de uso y eventos sin apropiarse de toda la lógica de negocio.

### State

Zonas donde aplica:

- tab/pestaña activa;
- workspace activo;
- selección actual;
- modo de interacción del canvas;
- módulo activo del futuro SideDock;
- dirty state.

Regla:

> El estado debe pertenecer al contexto activo, especialmente al tab activo, no a un global fijo que contamine otros workspaces.

## Registry

Uso actual:

- `DiagramTypeRegistry`
- `DefaultDiagramTypeRegistry`
- `WorkspaceViewRegistry`

Uso futuro deseable:

- registry de perfiles de interacción;
- registry de módulos del SideDock;
- registry de exportadores por tipo;
- registry de ayuda contextual por tipo.

Criterio:

> Usar Registry cuando el sistema necesita descubrir capacidades por tipo sin llenar el shell de condicionales.

## Port

Uso actual en canvas:

- `CanvasReadPort`
- `CanvasSelectionPort`
- `CanvasLayoutCommandPort`
- `CanvasBendPointPort`
- `CanvasDirtyPort`
- `DiagramSurfaceCommandPort`

Ports faltantes candidatos:

- `CanvasConnectorLabelPort`
- `CanvasEndpointPort`
- `CanvasResizePort`
- `CanvasLivePreviewPort`

Regla:

> Un Port expresa una frontera de capacidad; no hace falta una interfaz padre `Port` si no aporta comportamiento real.

## Support / Delegate

No se deben confundir con interfaces. Los Supports se justifican cuando hay implementación repetida.

Candidatos futuros para Tanda 3:

- `CanvasElementIdCodec`
- `CanvasSelectionSupport`
- `CanvasBendPointEditingSupport`
- `CanvasConnectorLabelEditingSupport`
- `CanvasContentBoundsCalculator`
- `CanvasDirtyState`

Regla:

> Si se repite código, extraer implementación común; si solo se repite contrato, usar interfaz.

## Builder

Uso actual:

- `DataDictionaryPdfReportBuilder`

Criterio:

> Usar Builder cuando se arma progresivamente una salida compleja como PDF, documento o reporte.

## Fronteras de capas

Regla objetivo:

```txt
presentation → application → domain
infrastructure → application ports/domain según corresponda
```

Deuda a vigilar:

- cualquier dependencia directa `presentation → infrastructure` debe revisarse y, si corresponde, pasar por caso de uso o puerto de aplicación.

## Convenciones de nombres

Usar sufijo explícito cuando haga más legible el rol:

- `Facade`
- `Factory`
- `Adapter`
- `Policy`
- `Coordinator`
- `Registry`
- `Builder`
- `Port`
- `Support`
- `Resolver`
- `Handler`

Evitar nombres inflados que encadenen demasiados patrones.

Ejemplo malo:

```txt
UmlClassRenderStrategyFactoryBuilderAdapter
```

Ejemplo correcto:

```txt
UmlClassRenderKit
UmlClassCanvasAdapter
UmlClassLayoutPolicy
UmlShapeKit
```

## Resultado de Tanda 1

- Se hizo explícito el rol de fachada en `DiagramDrawingFacade`.
- Se documentaron familias de patrones actuales.
- Se identificaron ports faltantes y supports futuros sin implementarlos prematuramente.
- Se mantiene `ApplicationServices` como fachada grande candidata a división posterior.
- Se evita refactor profundo de canvas/adapters hasta las tandas correspondientes.

## Actualización Tanda 14

La limpieza SOLID agregó piezas pequeñas con rol explícito:

- `EditorActivationGuard`: guardarraíl de comandos contextuales de editor.
- `SpecializedProjectSynchronizer`: coordinador de sincronización entre workspaces especializados y sesión activa.
- `UnsavedChangesDialog`: componente de presentación para confirmar acciones con cambios sin guardar.
- `SourceMarkdownSynchronizer`: puerto de application para sincronización del Markdown fuente.
- `FileSystemSourceMarkdownSynchronizer`: adaptador de infraestructura para escritura en archivo.

También se corrigió una frontera de capas: `InspectorViewModel` ya no depende directamente de `MarkdownProjectWriter`.
