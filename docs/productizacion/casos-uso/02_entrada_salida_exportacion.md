# Casos de uso — entrada, ejemplos, exportación y entrega

Estado: **matriz de control de I/O**  
Alcance: importación Markdown, selector de ejemplos, recursos IA, exportación activa, formatos y entrega por lote.

---

## Porcentaje general estimado

| Bloque | % al ojo | Estado | Motivo |
|---|---:|---|---|
| Importación Markdown por tipo | 80% | Funcional | Dispatcher por `diagram_type` y parsers especializados para casi todos los tipos importables. |
| Ejemplos oficiales | 86% | Sólido | Familia UENS/colegio existe; diccionario queda declarado como ejemplo importable documental. |
| Recursos IA | 88% | Sólido | Gramáticas y plantillas empaquetadas. |
| Exportación PNG activa | 76% | Funcional con smoke pendiente | Cada ViewModel visual ofrece snapshot PNG; hay que probar tab activa. |
| Exportación Markdown | 74% | Funcional con riesgo | Hay exporters por familias; debe revisarse pérdida de información por tipo. |
| Exportación SVG | 55% | Parcial honesto | Real en modelo conceptual; no debe ofrecerse como universal. |
| Exportación PDF | 75% | Documental por tipo | Real para diccionario y Levantamiento lógico; no debe aparecer para diagramas visuales. |
| Exportación por lote | 60% | Útil pero delicada | Hay caso de uso y ensamblaje, pero requiere smoke manual en Windows. |

---

## CU-IO-01 Importar Markdown oficial por `diagram_type`

| Campo | Detalle |
|---|---|
| Usuario quiere | Convertir Markdown estructurado en proyecto editable. |
| Entrada | Botón `Importar`. |
| Anclaje observado | `DiagramMarkdownImportDispatcher`, `ImportMarkdownModelUseCase`, parsers por tipo. |
| Estado | **Terminado funcional — 80%** |
| Falta | Smoke con todos los ejemplos importables y frontmatter inválido. |

### Regla anti-fachada

Si un tipo no tiene parser conectado, no debe importarse como conceptual por accidente.

---

## CU-IO-02 Rechazar Markdown ambiguo o incompatible

| Campo | Detalle |
|---|---|
| Usuario quiere | Que la app no cree proyectos falsos desde archivos mal declarados. |
| Entrada | `Importar`. |
| Anclaje observado | `MarkdownModelParsingException`, validación de `diagram_type`, rechazo de secciones incompatibles. |
| Estado | **Terminado funcional — 82%** |
| Falta | Casos negativos por cada familia: tipo inexistente, sección mezclada, archivo vacío. |

---

## CU-IO-03 Abrir ejemplo oficial importable

| Campo | Detalle |
|---|---|
| Usuario quiere | Cargar un ejemplo completo como proyecto. |
| Entrada | Botón `Ejemplo`. |
| Anclaje observado | `DefaultOfficialExampleCatalog`, `OfficialExampleSelectorDialog`, recursos `ai-resources/official-markdown/diagramas/*`. |
| Estado | **Terminado funcional — 86%** |
| Falta | Opción ergonómica de “abrir todos los ejemplos” o familia UENS completa. |

---

## CU-IO-04 Marcar ejemplo documental no importable

| Campo | Detalle |
|---|---|
| Usuario quiere | Ver una referencia sin que la app prometa abrirla como editor si aún no puede. |
| Entrada | Selector de ejemplos. |
| Anclaje observado | `OfficialExampleDescriptor.importable=true` para diccionario UENS, derivado de `DefaultDiagramTypeDefinitions`. |
| Estado | **Terminado honesto — 90%** |
| Falta | Implementar importación directa del diccionario solo si se decide que el tipo debe abrirse desde ejemplo. |

---

## CU-IO-05 Exportar PNG del workspace activo

| Campo | Detalle |
|---|---|
| Usuario quiere | Obtener una imagen del diagrama/matriz/maqueta activa. |
| Entrada | `Exportar > PNG` o toolbar contextual. |
| Anclaje observado | `ActiveOutputResolver`, `ExportCommandHandler`, `exportVisualAsPng` en ViewModels visuales. |
| Estado | **Terminado funcional — 76%** |
| Falta | Smoke de pestañas mixtas: exportar cada tab y confirmar que no sale el proyecto equivocado. |

---

## CU-IO-06 Exportar SVG

| Campo | Detalle |
|---|---|
| Usuario quiere | Obtener diagrama vectorial editable/limpio. |
| Entrada | `Exportar > SVG`. |
| Anclaje observado | `SvgDiagramExporter`, `ExportSvgUseCase`, salida conceptual. |
| Estado | **Parcial honesto — 55%** |
| Falta | Mantener SVG solo en conceptual o crear exportadores SVG reales por familia en AV-I10. |

### Anti-fachada

No activar SVG en BPMN, C4, wireframes o UML hasta que exista exportador real, no solo snapshot disfrazado.

---

## CU-IO-07 Exportar PDF

| Campo | Detalle |
|---|---|
| Usuario quiere | Generar documento formal. |
| Entrada | `Exportar documento PDF`. |
| Anclaje observado | `ExportPdfUseCase`, `PdfDiagramExporter`, `DataDictionaryPdfExporter`, `LogicalBusinessPdfExporter`. |
| Estado | **Terminado documental por tipo — 75%** |
| Falta | Probar PDF con diccionario y levantamiento grandes. No ofrecer PDF general si no existe. |

---

## CU-IO-08 Exportar Markdown actualizado

| Campo | Detalle |
|---|---|
| Usuario quiere | Sacar un Markdown que refleje lo editado. |
| Entrada | `Exportar Markdown`. |
| Anclaje observado | `ExportMarkdownUseCase`, exporters especializados: conceptual, diccionario, screen flow, wireframe, UML clase, arquitectura, comportamiento, etc. |
| Estado | **Terminado funcional — 74%** |
| Falta | Diff por tipo: editar un campo/nodo/relación, exportar y verificar que el cambio aparece. |

---

## CU-IO-09 Exportar proyectos abiertos para cliente

| Campo | Detalle |
|---|---|
| Usuario quiere | Preparar paquete de entrega con varios proyectos abiertos. |
| Entrada | `Exportar proyectos abiertos...`. |
| Anclaje observado | `ExportOpenProjectsForClientUseCase`, `ClientBatchExportService`, lógica de snapshots PNG pendientes en shell. |
| Estado | **Parcial útil — 60%** |
| Falta | Definir regla visible de estados sin guardar, probar rutas de salida y revisar resumen generado. |

---

## CU-IO-10 Exportar recursos IA

| Campo | Detalle |
|---|---|
| Usuario quiere | Obtener plantillas y gramáticas para generar Markdown compatible. |
| Entrada | `Recursos IA`. |
| Anclaje observado | `ExportAiResourcesUseCase`, `ClasspathAiResourceExporter`, recursos `ai-resources/*`. |
| Estado | **Terminado funcional — 88%** |
| Falta | Smoke: exportar carpeta y abrir recursos principales. |

---

## Casos faltantes detectados en entrada/salida

| ID faltante | Caso | Prioridad | Motivo |
|---|---|---:|---|
| IO-FALT-01 | Abrir todos los ejemplos oficiales importables | Alta | Útil para smoke y para mostrar la familia UENS completa. |
| IO-FALT-02 | Reporte automático de capacidades por tipo | Media | Evita que catálogo, toolbar y exportación se desalineen. |
| IO-FALT-03 | Round-trip Markdown por tipo | Alta | Importar → editar → exportar → reimportar para detectar pérdida. |
| IO-FALT-04 | Política formal de SVG por familia | Alta | Ahora SVG real es conceptual; AV-I10 debe decidir expansión. |
| IO-FALT-05 | Smoke de batch export con varios tipos abiertos | Media | Es una función potente pero puede ocultar errores de tab activa. |
---

## Nota de vigencia frente a planificación viva 6

Este documento conserva valor como matriz histórica de control funcional y casos de uso. Sin embargo, la planificación viva de exportación queda ahora centralizada en:

```txt
docs/implementacion/08_tanda_06_exportacion_profesional_por_tipo.md
```

Si aquí aparecen porcentajes o frases anteriores sobre SVG/PNG/Markdown/PDF que contradicen el estado objetivo actual, no se deben borrar sin auditoría; se deben tratar como diagnóstico anterior hasta la limpieza documental de la Planificación 9.

Regla vigente:

```txt
La promesa visible de exportación debe obedecer al workspace activo y pasar smoke real por tipo.
Si un formato no pasa smoke, debe ocultarse/deshabilitarse o corregirse antes de considerarse cerrado.
```

## Nota de vigencia — Implementación 7

La Implementación 7 agrega una política explícita de formatos reales (`ProjectExportFormatPolicy`).
Los porcentajes históricos de esta matriz siguen sirviendo como diagnóstico, pero la disponibilidad visible actual debe obedecer a:

```txt
proyecto activo + documento interno presente + formato soportado por la salida activa
```

Por tanto, SVG/Markdown/PDF no deben entenderse como simples promesas por tipo nominal, sino como formatos condicionados por la salida activa real y por smoke pendiente en Windows.
