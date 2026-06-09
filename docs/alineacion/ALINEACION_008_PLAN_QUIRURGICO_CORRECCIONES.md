# Alineación 008 — Plan quirúrgico de correcciones

Estado: **alineación aplicada**  
Fecha: **2026-05-24**  
Alcance: **secuenciación quirúrgica de fixes técnicos, pruebas focalizadas, documentación y release candidate**  
Tipo de cambio: **documental y de guardarraíl fuente; no modifica lógica de ejecución**

## 1. Propósito

Esta alineación convierte las siete alineaciones previas en un plan de intervención ordenado. Su objetivo no es corregir todavía el código productivo, sino fijar el orden en que deben aplicarse las próximas tandas para evitar arreglos aislados, regresiones transversales o promesas falsas.

Regla central:

```txt
primero estabilizar contratos, luego implementar capacidades, después documentar matrices, finalmente validar release candidate
```

El `logical-business-graph` ya tiene dominio, Markdown, derivación, canvas, SideDock, ejemplo UENS y recursos IA. El riesgo actual es cerrarlo como producto mediante parches desordenados. Esta alineación evita eso.

## 2. Relación con alineaciones anteriores

- Alineación 001: definió el contrato final de tipo productivo.
- Alineación 002: fijó fronteras arquitectónicas y zonas prohibidas.
- Alineación 003: congeló la semántica del Grafo lógico del negocio.
- Alineación 004: fijó catálogo, capacidades, toolbar, workspace y exportación.
- Alineación 005: separó guía académica, ayuda operativa y recursos IA.
- Alineación 006: fijó validación integral y criterios de calidad.
- Alineación 007: fijó matrices, documentación anti-fachada y release.

Esta Alineación 008 transforma esos contratos en una ruta de corrección verificable.

## 3. Principio de intervención

Las correcciones deben cumplir estas reglas:

```txt
Una tanda debe tener objetivo pequeño, prueba focalizada y bajo riesgo.
No se debe tocar pantalla de inicio.
No se debe tocar modelo conceptual.
No se debe tocar canvas conceptual.
No se debe migrar código del modelo conceptual a otros proyectos.
No se debe usar FreeGraphDocument como dominio del Grafo lógico.
No se debe declarar AVAILABLE antes de cerrar catálogo, ayuda académica, persistencia, exportaciones, validación y documentación.
No se deben actualizar matrices como completadas antes de tener evidencia técnica.
```

Si una corrección requiere tocar una zona protegida, debe detenerse y justificarse como emergencia explícita.

## 4. Orden quirúrgico recomendado

El orden técnico recomendado es:

```txt
F1  CSS/tokens y regla visual mínima.
F2  Catálogo del Grafo lógico y estado AVAILABLE condicionado.
F3  Toolbar contextual y validación global.
F4  Active output y exportaciones Markdown/PNG/SVG.
F5  Persistencia .dms y roundtrip de layout.
F6  Guía académica del Grafo lógico.
F7  Recursos IA y docs IA.
F8  Validación integral del Grafo lógico.
F9  Matrices, smoke y release docs.
F10 Release candidate local Windows.
```

La secuencia evita actualizar documentación final antes de que el código cumpla.

## 5. F1 — CSS/tokens y regla visual mínima

Objetivo: resolver deuda visual de bajo riesgo y desbloquear una falla clara de token.

Cambios esperados:

```txt
- declarar -dms-border-strong en tokens.css;
- corregir comentario de tokens.css para eliminar “apenas redondeados”;
- limpiar radius ornamental en workbench.css, interactive-canvas.css, toolbar-contextual.css y componentes nuevos/corregidos;
- no tocar pantalla de inicio ni modelo conceptual;
- agregar guardarraíl limitado anti-radius para UI nueva/corregida.
```

Pruebas esperadas:

```txt
CssTokenCoverageTest
UiStraightEdgesProductizationTest
scripts\02-ejecutar-tests.bat
```

Criterio de salida:

```txt
No hay tokens -dms-* usados sin declaración.
No hay radius ornamental nuevo en SideDock, workbench ni canvas interactivo usado por el Grafo lógico.
```

## 6. F2 — Catálogo del Grafo lógico

Objetivo: alinear `DefaultDiagramTypeDefinitions` con el estado real del tipo.

Cambios esperados:

```txt
- decidir y aplicar DiagramSupportStatus.AVAILABLE solo cuando el cierre técnico lo justifique;
- declarar capacidades objetivo: CREATE_PROJECT, IMPORT_MARKDOWN, SHOW_VISUAL_OUTPUT, MANUAL_EDITING, EXPORT_MARKDOWN, EXPORT_PNG, EXPORT_SVG, SAVE_DMS, LOAD_DMS, AI_RESOURCES, THEORY_HELP;
- corregir minimalExampleResource para que apunte a logical_business_graph_minimo.md;
- mantener officialExampleResource apuntando a logical_business_graph_uens_gordito.md;
- asignar theoryTopicId del Grafo lógico cuando exista la guía académica;
- revisar tests que asumían que todo no-AVAILABLE debe ser PLACEHOLDER_GUIDE.
```

Pruebas esperadas:

```txt
DefaultDiagramTypeDefinitionsTest
DefaultDiagramTypeRegistryTest
DefaultDiagramCapabilityCatalogTest
DefaultCreateWorkspaceUseCaseTest
ProductMinimumCoherenceTest
```

Criterio de salida:

```txt
El Grafo lógico no queda en estado híbrido: si tiene workspace real, abre como PRODUCT_VIEW y sus capacidades coinciden con lo implementado.
```

## 7. F3 — Toolbar contextual y validación global

Objetivo: que las acciones visibles del Grafo lógico coincidan con capacidades reales.

Cambios esperados:

```txt
- crear LogicalBusinessGraphToolbarContributor;
- exponer solo acciones reales;
- ocultar Quitar punto si supportsBendPoints() es false;
- conectar Diagrama > Validar proyecto con ProjectValidationCoordinator;
- mostrar issues semánticos del Grafo lógico sin caer al canvas conceptual;
- evitar que el fallback conceptual responda por un workspace especializado.
```

Pruebas esperadas:

```txt
LogicalBusinessGraphToolbarContributorTest
ProjectValidationCoordinatorLogicalBusinessGraphTest
DiagramCapabilityPresentationPolicyTest
DefaultDiagramToolbarActionProviderTest
```

Criterio de salida:

```txt
La UI no promete acciones inexistentes y Validar proyecto opera sobre LogicalBusinessGraphDocument.
```

## 8. F4 — Active output y exportaciones Markdown/PNG/SVG

Objetivo: completar salida activa para que el menú Exportar y la toolbar funcionen con el Grafo lógico.

Cambios esperados:

```txt
- crear LogicalBusinessGraphActiveOutputContributor;
- registrar contributor en ActiveOutputContributorRegistry;
- conectar exportación Markdown existente;
- conectar PNG desde InteractiveCanvasPngExporter;
- implementar o conectar SVG vectorial del Grafo lógico;
- evitar que exportar grafo vacío genere Markdown inválido con FL-001 inexistente;
- agregar prueba de exportar y reimportar Markdown.
```

Pruebas esperadas:

```txt
LogicalBusinessGraphActiveOutputContributorTest
LogicalBusinessGraphMarkdownParserExporterTest
LogicalBusinessGraphSvgExporterTest
DiagramMarkdownImportDispatcherTest
ExportMarkdownUseCaseTest
ExportSvgUseCaseTest
```

Criterio de salida:

```txt
Exportar Markdown, PNG y SVG funciona desde salida activa real y no por promesa de catálogo.
```

## 9. F5 — Persistencia .dms y roundtrip de layout

Objetivo: que `SAVE_DMS` y `LOAD_DMS` sean capacidades reales del Grafo lógico.

Cambios esperados:

```txt
- serializar LogicalBusinessGraphDocument en .dms;
- preservar nodos, relaciones, notas, versión y fecha;
- preservar layout especializado;
- preservar estilos si el proyecto los usa;
- abrir .dms y reconstruir workspace LOGICAL_BUSINESS_GRAPH_DIAGRAM;
- verificar que mover nodo marca cambios sin guardar y queda persistido.
```

Pruebas esperadas:

```txt
DmsProjectJsonLogicalBusinessGraphTest
DmsProjectJsonSpecializedRoundTripTest
SpecializedProjectOpenUseCasePersistenceTest
SpecializedProjectOpenUseCaseLayoutRoundTripTest
OpenProjectUseCaseSpecializedPersistenceTest
```

Criterio de salida:

```txt
Guardar y abrir .dms conserva semántica y layout visual del Grafo lógico.
```

## 10. F6 — Guía académica del Grafo lógico

Objetivo: cerrar `THEORY_HELP` y separar definitivamente teoría de ayuda operativa.

Cambios esperados:

```txt
- crear TheoryTopicId.LOGICAL_BUSINESS_GRAPH;
- crear help/topics/logical-business-graph.md;
- registrar el tema en DefaultTheoryCatalog;
- agregar figura didáctica renderizable;
- cumplir micro-wikipedia: Qué es, Para qué sirve, Elementos principales, Relaciones y lectura, Casos especiales, Cuándo usarlo, Cuándo no usarlo, Errores comunes;
- renombrar UI de “Guía operativa” a “Guía académica” en menú/ventana/status; **cerrado en Tanda 32**;
- no mover ayuda operativa del SideDock.
```

Pruebas esperadas:

```txt
DefaultTheoryCatalogTest
DefaultTheoryCatalogFigureCoverageTest
DefaultTheoryCatalogMicroWikipediaTest
TheoryTopicMarkdownParserFigureTest
LogicalBusinessGraphAcademicTheoryTest
```

Criterio de salida:

```txt
El menú Ayuda enseña teoría; el SideDock Ayuda enseña operación de herramienta.
```

## 11. F7 — Recursos IA y docs IA

Objetivo: integrar Tanda 39 al contrato final de producto.

Cambios esperados:

```txt
- declarar AI_RESOURCES en capacidades del Grafo lógico;
- actualizar docs/ia/RECURSOS_IA.md con logical-business-graph;
- aclarar que el prompt maestro no es importable como proyecto;
- aclarar que el prompt produce Markdown importable;
- ajustar guardarraíl literal que confunde el output esperado del prompt con importabilidad del recurso;
- actualizar test acoplado a OfficialAiResourceDescriptors si el recurso está delegado a LogicalBusinessGraphAiResourceDescriptors;
- limpiar “experimental” en gramática si el tipo pasa a AVAILABLE.
```

Pruebas esperadas:

```txt
Tanda39LogicalBusinessGraphAiResourcesTest
OfficialAiResourceDescriptorsTest
ClasspathAiResourceCatalogTest
ClasspathAiResourceExporterTest
Tanda32AiResourcesAntiFacadeTest
OfficialAiResourceUensCoverageTest
```

Criterio de salida:

```txt
Recursos IA exportables y Markdown importables están diferenciados sin contradicción documental ni de tests.
```

## 12. F8 — Validación integral del Grafo lógico

Objetivo: ampliar `semanticIssues()` o crear servicio de aplicación para validar el grafo como artefacto lógico, no solo como dibujo.

Cambios esperados:

```txt
- validar backbone MF → FL → CU → ACC;
- diferenciar BLOCKING, WARNING e INFO;
- detectar MF sin FL, FL sin CU, CU sin ACC;
- detectar ACC crítica sin PRE/POST cuando aplique;
- detectar INV sin protege y POST sin garantiza;
- detectar RN aislada, ENT aislada, REP sin fuente, RISK sin nodo afectado, PEND sin elemento bloqueado;
- revisar compatibilidad de consulta, habilita y contiene según contrato semántico;
- validar ejemplo UENS con cero bloqueos;
- presentar resultados en SideDock y Diagrama > Validar proyecto.
```

Pruebas esperadas:

```txt
LogicalBusinessGraphValidationServiceTest
LogicalBusinessGraphDocumentTest
LogicalBusinessGraphRelationKindTest
LogicalBusinessGraphOfficialExampleValidationTest
LogicalBusinessGraphProjectValidationCoordinatorTest
Tanda38LogicalBusinessGraphUensExampleTest
```

Criterio de salida:

```txt
La validación detecta problemas reales del levantamiento lógico visual y no reemplaza la revisión humana.
```

## 13. F9 — Matrices, smoke y release docs

Objetivo: actualizar documentación viva solo después de que el código cumpla.

Cambios esperados:

```txt
- actualizar MATRIZ_CAPACIDADES_REALES.md;
- actualizar MATRIZ_CASOS_USO_Y_TESTS.md;
- actualizar MATRIZ_CASOS_USO_CATEGORIZADA.md;
- actualizar 09_matriz_cobertura_casos_uso_por_tipo.md;
- actualizar 10_checklist_smoke_visual_por_tipo.md;
- actualizar 01_matriz_smoke_16_tipos.md;
- actualizar RELEASE_CANDIDATE_0_0_1.md;
- actualizar LIMITACIONES_CONOCIDAS_0_0_1.md;
- actualizar RELEASE_NOTES.md;
- registrar limitaciones honestas si CRUD completo, etiquetas movibles o validación avanzada quedan fuera.
```

Pruebas esperadas:

```txt
ProductMinimumCoherenceTest
AcceptanceMatrixCoverageTest
CategorizedUseCaseMatrixCoverageTest
UseCaseCoverageByTypeTest
SmokeMatrixCoverageTest
Alineacion7DocumentationReleaseContractSourceTest
```

Criterio de salida:

```txt
Las matrices dicen exactamente lo que el producto puede demostrar.
```

## 14. F10 — Release candidate local Windows

Objetivo: validar el producto como ejecutable, no solo como código que compila.

Comandos esperados:

```bat
scripts\02-ejecutar-tests.bat
scripts\13-revalidacion-local-completa.bat
scripts\14-app-image-completa.bat
scripts\15-msi-completo.bat
scripts\16-release-candidate.bat
```

Smoke manual mínimo:

```txt
Abrir ejemplo UENS del Grafo lógico.
Ver leyenda.
Seleccionar nodos y relaciones.
Mover nodo.
Guardar .dms.
Cerrar y reabrir.
Exportar Markdown.
Reimportar Markdown.
Exportar PNG.
Exportar SVG.
Abrir ayuda operativa del SideDock.
Abrir guía académica del menú Ayuda.
Confirmar que no cae al canvas conceptual.
Confirmar que no usa FreeGraphDocument.
```

Criterio de salida:

```txt
BUILD SUCCESS, tests verdes, app-image funcional, MSI generado y smoke manual documentado.
```

## 15. Tandas técnicas resultantes

Después de esta alineación, la continuidad recomendada queda así:

```txt
Tanda 40 — Guía académica del Grafo lógico.
Tanda 41 — Persistencia .dms y exportaciones.
Tanda 42 — Validación integral del nuevo proyecto.
Tanda 31 — Validación local Windows / Release Candidate.
Tanda 9 — Deuda SRP focalizada, solo si aparece bloqueo real.
```

El plan quirúrgico permite subdividir esas tandas si el log local muestra fallas independientes. Por ejemplo, Tanda 41 puede dividirse en 41A persistencia, 41B active output, 41C SVG/PNG.

## 16. Criterio para no abrir deuda SRP todavía

La deuda SRP no debe ejecutarse por estética. Debe activarse solo si:

```txt
una clase impide probar una capacidad;
una clase mezcla dominio con JavaFX, Markdown o JSON;
una clase impide aislar toolbar, exportación, persistencia o validación;
un test demuestra acoplamiento dañino;
una corrección simple obligaría a tocar demasiadas responsabilidades juntas.
```

Si no hay bloqueo real, la Tanda 9 queda aplazada.

## 17. Resultado esperado de esta alineación

Con esta alineación, las correcciones dejan de ser una lista abierta y pasan a ser una ruta verificable:

```txt
contrato → implementación focalizada → prueba → documentación → smoke → release
```

Esto protege el cierre del Grafo lógico del negocio y mantiene intactas las zonas sagradas del sistema.
