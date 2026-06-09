# Matriz categorizada de casos de uso y pruebas

Estado: **macro trazabilidad categorizada**  
Tanda: **22**  
Alcance: casos de uso prometidos por categoría transversal, familia de diagrama y tipo visible.

## Leyenda de cobertura

| Estado | Significado |
|---|---|
| `CUBIERTO` | Existe prueba automática razonable para la promesa base. |
| `PARCIAL` | Hay pruebas unitarias/integración/contrato, pero falta UI/E2E o smoke visual. |
| `PENDIENTE_UI_E2E` | Debe probarse con robot JavaFX/TestFX o equivalente. |
| `MANUAL_REQUERIDO` | Debe revisarse visualmente por humano aunque existan tests. |
| `NO_APLICA` | La acción no corresponde a ese tipo. |

## Categorías transversales

| Categoría | Código | Casos de uso | Nivel mínimo esperado | Diagnóstico actual |
|---|---|---|---|---|
| Proyecto, shell y workspace | `CAT-PROJ` | `UC-PROJ-001`, `UC-PROJ-002`, `UC-PROJ-003` | Integración + contrato + UI/E2E mínimo | `PARCIAL`: hay contratos y persistencia; falta flujo UI/E2E completo crear/cambiar pestaña. |
| Importación y ejemplos | `CAT-IMPORT` | `UC-IMPORT-001`, `UC-EXAMPLE-001` | Integración + smoke manual | `CUBIERTO` en base técnica; `MANUAL_REQUERIDO` para confirmar experiencia visual. |
| Canvas e interacción visual | `CAT-CANVAS` | `UC-CANVAS-001`, `UC-CANVAS-002`, `UC-CANVAS-003`, `UC-CANVAS-004` | Contrato/fuente + UI/E2E + smoke manual | `PARCIAL`: la lógica fue reforzada, pero los clics/drag reales deben automatizarse luego. |
| Persistencia `.dms` | `CAT-PERSIST` | `UC-PROJ-002` | Integración round-trip | `CUBIERTO`: existen round-trips por tipos especializados y persistencia. |
| Exportaciones | `CAT-EXPORT` | `UC-EXPORT-001`, `UC-EXPORT-002`, `UC-EXPORT-003`, `UC-EXPORT-004` | Integración + smoke manual | `PARCIAL`: Markdown fuerte; PNG/SVG/PDF necesitan smoke visual por tipo. |
| Ayuda, teoría y recursos IA | `CAT-HELP-AI` | `UC-HELP-001`, `UC-AI-001` | Catálogo + smoke manual | `PARCIAL`: recursos existen, pero conviene revisar contenido visible por tipo. |
| UML Clases e importación de código | `CAT-UML-SOURCE` | `UC-UMLSRC-001`, `UC-UMLSRC-002` | Aplicación/integración + contrato + UI/E2E con fakes | `PARCIAL`: importación y restricciones están cubiertas; falta UI/E2E con fake launcher. |
| Documentación y trazabilidad | `CAT-DOC` | `UC-DOC-001`, `UC-DOC-002`, `UC-DOC-003` | Test fuente documental | `CUBIERTO` para existencia de matrices; falta revisión humana final. |

## Casos documentales agregados en Tanda 22

| Código | Categoría | Caso de uso de documentación | Prueba esperada | Estado |
|---|---|---|---|---|
| `UC-DOC-001` | `CAT-DOC` | La documentación viva debe tener un mapa de lectura y separar histórico de fuente vigente. | `Tanda22DocumentationCleanupAndMacroDiagnosisSourceTest` | `CUBIERTO` |
| `UC-DOC-002` | `CAT-DOC` | Cada tipo visible debe aparecer en la matriz categorizada. | `Tanda22DocumentationCleanupAndMacroDiagnosisSourceTest` | `CUBIERTO` |
| `UC-DOC-003` | `CAT-DOC` | El macro diagnóstico debe listar pendientes, riesgos, no tocar y próximos pasos. | `Tanda22DocumentationCleanupAndMacroDiagnosisSourceTest` | `CUBIERTO` |

## Familias de diagramas y pruebas esperadas

| Familia | Tipos | Casos obligatorios | Riesgo principal | Prueba pendiente más importante |
|---|---|---|---|---|
| Conceptual | `conceptual-model` | `CAT-PROJ`, `CAT-IMPORT`, `CAT-CANVAS`, `CAT-EXPORT` | Es el módulo protegido; no conviene tocarlo por refactors generales. | Smoke manual de exportaciones sin modificar comportamiento. |
| Documental/tabular | `data-dictionary`, `roles-permissions-map`, `logical-business-intake` | `CAT-PROJ`, `CAT-IMPORT`, `CAT-EXPORT` | Confundir documento/matriz con canvas libre. | Revisión visual de PDF/Markdown/SVG y PNG tabular según aplique. |
| Levantamiento visual | `logical-business-graph` | `CAT-PROJ`, `CAT-IMPORT`, `CAT-CANVAS`, `CAT-EXPORT`, `CAT-HELP-AI`, `CAT-DOC` | Confundir grafo semántico tipado con grafo libre o árbol rígido. | Smoke visual UENS, teoría académica, validación semántica y exportaciones. |
| Libre/general | `free-graph` | `CAT-PROJ`, `CAT-IMPORT`, `CAT-CANVAS`, `CAT-EXPORT` | Etiquetas y drag regresivos. | UI/E2E de crear/seleccionar/arrastrar/exportar. |
| UML estructural | `uml-class` | `CAT-PROJ`, `CAT-IMPORT`, `CAT-CANVAS`, `CAT-EXPORT`, `CAT-UML-SOURCE` | Selección, drag, jerarquías y abrir fuente. | UI/E2E con fake file chooser y fake editor launcher. |
| UML comportamiento | `uml-use-case`, `uml-activity`, `uml-sequence`, `uml-state` | `CAT-PROJ`, `CAT-IMPORT`, `CAT-CANVAS`, `CAT-EXPORT` | Venderlo como implementación total de UML. | Smoke visual por símbolos y exportaciones. |
| Procesos de negocio | `bpmn-basic`, `operational-flow` | `CAT-PROJ`, `CAT-IMPORT`, `CAT-CANVAS`, `CAT-EXPORT` | Vender BPMN como especificación completa. | Smoke visual de símbolos básicos. |
| Arquitectura | `c4-context`, `c4-containers`, `technical-deployment`, `admin-module-map` | `CAT-PROJ`, `CAT-IMPORT`, `CAT-CANVAS`, `CAT-EXPORT` | Relación entre capacidad declarada y output visual real. | Smoke visual de exportación PNG/SVG. |
| Pantallas | `screen-flow`, `admin-wireframes` | `CAT-PROJ`, `CAT-IMPORT`, `CAT-CANVAS`, `CAT-EXPORT` | Confundir wireframes simples con diseñador tipo Figma. | UI/E2E de selección/resize/exportación. |

## Matriz por tipo visible

| Tipo visible | ID oficial | Categorías requeridas | Estado macro | Comentario operativo |
|---|---|---|---|---|
| Modelo conceptual | `conceptual-model` | `CAT-PROJ`, `CAT-IMPORT`, `CAT-CANVAS`, `CAT-EXPORT`, `CAT-HELP-AI` | `PARCIAL` | Proteger; validar visualmente sin refactor. |
| Diccionario de datos | `data-dictionary` | `CAT-PROJ`, `CAT-IMPORT`, `CAT-EXPORT`, `CAT-HELP-AI` | `PARCIAL` | PDF/Markdown requieren revisión humana. |
| BPMN básico | `bpmn-basic` | `CAT-PROJ`, `CAT-IMPORT`, `CAT-CANVAS`, `CAT-EXPORT`, `CAT-HELP-AI` | `PARCIAL` | Promesa básica, no especificación completa. |
| Flujo operativo | `operational-flow` | `CAT-PROJ`, `CAT-IMPORT`, `CAT-CANVAS`, `CAT-EXPORT`, `CAT-HELP-AI` | `PARCIAL` | Validar símbolos y labels. |
| C4 Contexto | `c4-context` | `CAT-PROJ`, `CAT-IMPORT`, `CAT-CANVAS`, `CAT-EXPORT`, `CAT-HELP-AI` | `PARCIAL` | Validar export visual. |
| C4 Contenedores | `c4-containers` | `CAT-PROJ`, `CAT-IMPORT`, `CAT-CANVAS`, `CAT-EXPORT`, `CAT-HELP-AI` | `PARCIAL` | Validar export visual. |
| Despliegue técnico | `technical-deployment` | `CAT-PROJ`, `CAT-IMPORT`, `CAT-CANVAS`, `CAT-EXPORT`, `CAT-HELP-AI` | `PARCIAL` | Validar nodos y dependencias. |
| UML Clases | `uml-class` | `CAT-PROJ`, `CAT-IMPORT`, `CAT-CANVAS`, `CAT-EXPORT`, `CAT-UML-SOURCE`, `CAT-HELP-AI` | `PARCIAL` | Debe tener UI/E2E de selección, drag y abrir código con fakes. |
| UML Casos de uso | `uml-use-case` | `CAT-PROJ`, `CAT-IMPORT`, `CAT-CANVAS`, `CAT-EXPORT`, `CAT-HELP-AI` | `PARCIAL` | Validar actores/casos visualmente. |
| UML Actividad | `uml-activity` | `CAT-PROJ`, `CAT-IMPORT`, `CAT-CANVAS`, `CAT-EXPORT`, `CAT-HELP-AI` | `PARCIAL` | Validar decisiones y flujo. |
| UML Secuencia | `uml-sequence` | `CAT-PROJ`, `CAT-IMPORT`, `CAT-CANVAS`, `CAT-EXPORT`, `CAT-HELP-AI` | `PARCIAL` | Validar lectura temporal. |
| UML Estados | `uml-state` | `CAT-PROJ`, `CAT-IMPORT`, `CAT-CANVAS`, `CAT-EXPORT`, `CAT-HELP-AI` | `PARCIAL` | Validar estados/transiciones. |
| Mapa de módulos | `admin-module-map` | `CAT-PROJ`, `CAT-IMPORT`, `CAT-CANVAS`, `CAT-EXPORT`, `CAT-HELP-AI` | `PARCIAL` | Validar contenedores y SideDock. |
| Roles y permisos | `roles-permissions-map` | `CAT-PROJ`, `CAT-IMPORT`, `CAT-EXPORT`, `CAT-HELP-AI` | `PARCIAL` | Matriz/documento con PNG/SVG/Markdown tabular; no canvas libre. |
| Flujo de pantallas | `screen-flow` | `CAT-PROJ`, `CAT-IMPORT`, `CAT-CANVAS`, `CAT-EXPORT`, `CAT-HELP-AI` | `PARCIAL` | Validar navegación y flechas. |
| Wireframes administrativos | `admin-wireframes` | `CAT-PROJ`, `CAT-IMPORT`, `CAT-CANVAS`, `CAT-EXPORT`, `CAT-HELP-AI` | `PARCIAL` | Validar tamaño manual/resize/export. |
| Levantamiento lógico | `logical-business-intake` | `CAT-PROJ`, `CAT-IMPORT`, `CAT-EXPORT`, `CAT-HELP-AI`, `CAT-DOC` | `PARCIAL` | Artefacto documental estructurado; importa/exporta PDF/Markdown y deriva borradores revisables sin prometer canvas visual. |
| Grafo lógico del negocio | `logical-business-graph` | `CAT-PROJ`, `CAT-IMPORT`, `CAT-CANVAS`, `CAT-PERSIST`, `CAT-EXPORT`, `CAT-HELP-AI`, `CAT-DOC` | `PARCIAL` | Vista visual semántica derivada; validar leyenda, trazabilidad, relaciones y exportaciones sin tratarlo como grafo libre. |
| Grafo libre | `free-graph` | `CAT-PROJ`, `CAT-IMPORT`, `CAT-CANVAS`, `CAT-EXPORT`, `CAT-HELP-AI` | `PARCIAL` | Validar etiquetas, selección, drag y exportaciones. |

## Criterio de cierre por categoría

Para declarar cerrada una categoría se requiere:

1. matriz documentada;
2. test unitario/integración/contrato cuando sea posible;
3. UI/E2E cuando haya clic/drag/toolbar crítico;
4. smoke manual cuando el resultado sea visual;
5. registro explícito si algo es `NO_APLICA`.

Esta matriz no reemplaza `docs/testeo/MATRIZ_CASOS_USO_Y_TESTS.md`; la reorganiza por categoría para que una siguiente IA no pierda el mapa funcional.


## Smoke UI mínimo agregado en Tanda 28

La aceptación UI mínima antes de refactorizar zonas rojas se centraliza en:

```txt
docs/testeo/UI_SMOKE_MINIMO_EJECUTABLE.md
docs/testeo/reportes/REPORTE_SMOKE_UI_MINIMO.md
scripts/09-smoke-ui-minimo.bat
```

Bloques obligatorios principales: `UI-SMOKE-001` a `UI-SMOKE-011`.

Antes de tocar `MainShellCommandHandler`, `MainShellView`, `InteractiveCanvasSurfaceView`, `UmlClassDiagramViewModel`, `ModuleMapViewModel`, `WireframeViewModel`, `SpecializedVisualSvgWriter` o `ClientBatchExportCoordinator`, deben ejecutarse o registrarse hallazgos para `UI-SMOKE-002`, `UI-SMOKE-003`, `UI-SMOKE-004`, `UI-SMOKE-005`, `UI-SMOKE-007` y `UI-SMOKE-008`.
