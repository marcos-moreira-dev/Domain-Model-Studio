# Plan de refactor integral — vigente desde Tanda 25

## Principio rector

El refactor no debe cambiar comportamiento visible. Primero se mide, luego se define una hipótesis, se protege con tests y recién entonces se extrae o simplifica código.

La regla central es: **si no es necesario, no se toca**.

## No refactorizar por tamaño solamente

Una clase grande no es automáticamente un problema. Se interviene cuando el tamaño viene acompañado de acoplamiento, responsabilidades mezcladas, cambios repetidos, dificultad de testeo o riesgo de regresión.

## Zonas de alto riesgo

Estas zonas requieren diagnóstico específico antes de cualquier extracción:

- `presentation/canvas/DiagramCanvasView.java`
- `presentation/canvas/DiagramCanvasViewModel.java`
- `presentation/inspector/*`
- `domain/er/*`
- renderers Chen/Crow's Foot
- importación de código fuente a UML Clases
- persistencia `.dms`
- parsers/exporters Markdown oficiales
- recursos IA oficiales
- scripts de instalador/release

El modelo conceptual ya no se trata como intocable por principio, pero sí como zona sensible: solo se interviene si hay beneficio claro y pruebas de no regresión.

## Fase R0 — Baseline de refactor

Estado: **abierta en Tanda 25**.

Objetivo:

- fijar métricas actuales;
- identificar hotspots;
- separar zonas de bajo, medio y alto riesgo;
- definir política documental;
- evitar que documentación histórica guíe refactors nuevos.

Documento rector:

- `docs/desarrollo/refactor/BASELINE_REFACTOR_TANDA_025.md`

## Fase R1 — Scripts operativos

Estado: **aplicada en Tanda 26**.

Objetivo: reducir scripts visibles a los realmente necesarios para desarrollo, pruebas y release.

Scripts públicos vigentes:

- `00-verificar-entorno.bat`
- `01-ejecutar-app.bat`
- `02-ejecutar-tests.bat`
- `06-medir-refactor.bat`
- `13-revalidacion-local-completa.bat`
- `14-app-image-completa.bat`
- `15-msi-completo.bat`
- `16-release-candidate.bat`
- `31-generar-javadoc.bat`

Los scripts históricos no permanecen como flujo recomendado ni como wrappers públicos de raíz.

## Fase R2 — Documentación viva

Estado: **aplicada en Tanda 27**.

Objetivo: eliminar Markdown histórico que ya no explica el producto actual.

Regla:

- conservar si explica una capacidad vigente, contrato vigente, frontera vigente o guía operativa real;
- eliminar si solo registra una tanda pasada, hipótesis abandonada o decisión sustituida;
- no archivar por defecto.

Documento rector:

- `docs/documentacion/POLITICA_DOCUMENTAL_REPOSITORIO.md`

## Fase R3 — `ApplicationServices`

Estado: **aplicada en Tanda 28**.

Objetivo: dividir la fachada por familias sin romper consumidores.

Resultado:

- `ApplicationServices` queda como fachada de compatibilidad durante la transición.
- Los servicios reales se agrupan por familias: proyecto, importación, exportación, catálogo/runtime, visual/layout, documentación IA, modelo conceptual, diccionario, mapa de módulos, UML clases, roles/permisos, flujo de pantallas, wireframes, comportamiento y arquitectura.
- Los accesos legacy siguen delegando a familias para no romper pantallas existentes.
- El código nuevo debe preferir las fachadas específicas antes que depender de la fachada global.

## Fase R4 — Shell y comandos

Estado: **iniciada en Tanda 29**.

Objetivo: reducir `MainShellCommandHandler` por coordinadores especializados sin cambiar UX visible.

Resultado aplicado:

- `ProjectCreationCoordinator` concentra la creación de proyectos nuevos y las decisiones de workspace inicial.
- `ProjectOpenCoordinator` concentra la apertura de `.dms`, FileChooser y validación de workspace disponible.
- `MainShellCommandHandler` queda como fachada pública de comandos y coordinador de activación de pestañas.
- El guardado, historial, validación, exportación por lote y comandos por familia se mantienen en coordinadores ya existentes.

Pendiente posible, solo si aporta valor medible:

- extraer cierre de pestañas/app;
- extraer navegación visual común;
- reducir más delegaciones públicas si se introduce un contrato de workspace activo.

## Fase R5 — Catálogos, capacidades y recursos

Estado: **aplicada en Tanda 30**.

Objetivo: separar definiciones por familia para que agregar o corregir un tipo de proyecto no requiera editar un archivo central gigante.

Resultado:

- `DefaultDiagramTypeDefinitions` queda como agregador por familias.
- `OfficialAiResourceDescriptors` queda como agregador por familias.
- Se preservan 19 tipos oficiales, 19 ejemplos importables, UENS como único Levantamiento lógico oficial y Óptica como recurso IA histórico/fixture.

## Fase R6 — Persistencia `.dms`

Estado: **aplicada en Tanda 31**.

Objetivo: modularizar readers/writers sin cambiar formato.

Resultado:

- `DmsProjectJsonReader` y `DmsProjectJsonWriter` quedan como coordinadores de formato.
- El modelo conceptual legacy pasa a readers/writers propios.
- Los documentos especializados opcionales se agrupan en un payload explícito.
- No se renombra ningún campo JSON ni se introduce migración.

## Fase R7 — Parsers/exporters Markdown

Estado: **aplicada en Tanda 32**.

Objetivo: extraer utilidades comunes sin cambiar gramáticas.

Resultado:

- Los parsers importables comparten `MarkdownImportDocument` para separar frontmatter y cuerpo.
- Se retiran copias locales de `readFrontMatter` y `stripFrontMatter` en parsers especializados y en el dispatcher.
- No se cambian gramáticas, ejemplos oficiales, frontmatter público ni comportamiento visible.
- El parser conceptual legacy también usa la utilidad común, pero conserva su gramática propia.

## Fase R8 — ViewModels visuales

Estado: **iniciada en Tanda 33**.

Objetivo: extraer patrones comunes solo donde haya duplicación real.

Resultado aplicado:

- Se agrega `ProjectChangeSupport` para centralizar listener de cambios, bloqueo durante carga y notificación del `DiagramProject` actualizado.
- Se migran los ViewModels visuales/documentales que repetían `loading` + `projectChangeListener`.
- No se introduce superclase ni abstracción de edición común forzada.
- El canvas conceptual legacy y el inspector quedan fuera de esta tanda.

Pendiente posible, solo con diagnóstico específico:

- selección/movimiento común por familias visuales;
- estado de dirty/refresh si hay duplicación activa;
- comandos de layout compartidos si no diluyen semántica propia.

## Fase R9 — UML Clases

Estado: **iniciada en Tanda 34**.

Objetivo: refactor focalizado del módulo avanzado de importación código→UML.

Resultado aplicado:

- `SourceCodeUmlClassViewBuilder` queda enfocado en construir vistas internas.
- `SourceCodeUmlSummarySelectionPolicy` concentra ranking, límites y selección de la vista Resumen segura para proyectos grandes.
- No se cambia parsing Java, parsing TypeScript, inferencia de relaciones, layout visual ni UX.

Pendiente posible, solo con diagnóstico específico:

- separar navegación/filtros de `UmlClassDiagramViewModel`;
- revisar `UmlClassCanvasAdapter` y `UmlClassStructurePanel` si aparece duplicación activa;
- mantener TypeScript como heurístico, no exhaustivo.

## Fase R10 — Modelo conceptual legacy

Objetivo: diagnóstico primero, extracción después si conviene.

Se debe preservar visualmente el dibujo actual y proteger Chen/Crow's Foot, exportaciones, inspector y `.dms`.

## Fase R11 — Artefactos compatibles legacy del Levantamiento lógico

Objetivo: decidir si la infraestructura interna se conserva, se renombra a borradores compatibles, se mueve a experimental o se elimina.

Mientras no sea visible como promesa de producto, no bloquea el refactor global.

## Fase R12 — Smoke integral y RC

Objetivo: demostrar que el refactor no cambió el producto.

Debe cubrir:

- ejemplos oficiales;
- importación Markdown/carpeta;
- `.dms`;
- exportaciones;
- SideDock;
- toolbar;
- Levantamiento lógico UENS;
- UML código fuente;
- modelo conceptual;
- instaladores.


## Tanda 35 — Canvas conceptual legacy

Se realiza solo una extracción de bajo riesgo: historial de edición y resolución geométrica de anclas. El canvas conceptual sigue siendo zona sensible; no se fuerza su migración al canvas transversal ni se toca el inspector legacy sin beneficio medible.

## Tanda 36 — Artefactos compatibles legacy

Se cierra la deuda visible de las antiguas derivaciones: no hay panel, CSS ni selección propia en el Levantamiento lógico. `LogicalBusinessDerivationService` se conserva como infraestructura interna de borradores compatibles mediante `compatibleDrafts` y `compatibleDraft`; los nombres legacy quedan deprecated solo por compatibilidad.


## Tanda 37 — CSS y recursos UI

Aplicada como limpieza de superficie: CSS vivo solamente, sin placeholders documentales, y cobertura de iconos internos declarados por toolbar. No cambia comportamiento visible.
