# Índice maestro de tandas de implementación

Estado: **plan maestro nuevo**  
Propósito: guiar una profesionalización gradual del proyecto sin parches gigantes ni archivos monstruosos.

## Orden obligatorio de ejecución

| Tanda | Documento | Objetivo central | Dependencia |
|---:|---|---|---|
| 00 | `02_tanda_00_baseline_y_limpieza_documental.md` | Crear baseline real, retirar contradicciones documentales y congelar reglas de validación. | Ninguna |
| 01 | `03_tanda_01_fuente_unica_tipos_capacidades.md` | Unificar tipos de proyecto, capacidades, toolbar, importación/exportación y ejemplos. | 00 |
| 02 | `04_tanda_02_workbench_comun_headers_sidebars.md` | Normalizar workbench, header, X de cierre, sidebars y panel acompañante. | 01 |
| 03 | `05_tanda_03_acciones_toolbar_botones_muertos.md` | Auditar acciones visibles; todo botón visible funciona, se deshabilita o desaparece. | 01, 02 |
| 04 | `06_tanda_04_canvas_interaccion_layout_persistente.md` | Unificar interacción: selección, drag, bend points, resize moderado y posiciones persistentes. | 02 |
| 05 | `07_tanda_05_drawing_core_y_shape_kits.md` | Crear dibujo común desacoplado: core pequeño + kits UML/BPMN/C4/Admin/Wireframe. | 04 |
| 06 | `08_tanda_06_exportacion_profesional_por_tipo.md` | Alinear PNG/SVG/Markdown/PDF con capacidades reales y calidad mínima. | 01, 05 |
| 07 | `09_tanda_07_diccionario_datos_profesional.md` | Convertir diccionario en documento profesional: tablas, portada, introducción, logo opcional. | 02, 06 |
| 08 | `10_tanda_08_modelado_datos_y_modelo_conceptual.md` | Preservar modelo conceptual como canon y crear puente conceptual → diccionario. | 04, 07 |
| 09 | `11_tanda_09_procesos_bpmn_flujo_operativo.md` | Dar identidad visual real a BPMN y flujo operativo. | 05 |
| 10 | `12_tanda_10_arquitectura_c4_despliegue.md` | Dar identidad C4/despliegue: persona, sistema, boundary, BD, ambiente, red. | 05 |
| 11 | `13_tanda_11_uml_clases_estructural.md` | UML Clases serio: cajas autoajustables, paquetes como contenedores, niveles de detalle. | 05 |
| 12 | `14_tanda_12_uml_comportamiento.md` | UML Casos de uso, Actividad y Estados con simbología y layout propios. | 05 |
| 13 | `15_tanda_13_uml_secuencia_temporal.md` | Secuencia como editor temporal: participantes, mensajes ordenados, activaciones, fragmentos. | 05 |
| 14 | `16_tanda_14_aplicaciones_administrativas.md` | Módulos, roles/permisos, pantallas y wireframes como herramientas administrativas reales. | 04, 05, 06 |
| 15 | `17_tanda_15_persistencia_dms_layout_assets.md` | Consolidar `.dms`: layout, assets, logo, recursos, compatibilidad y migración. | 07, 11, 14 |
| 16 | `18_tanda_16_refactor_srp_clases_grandes.md` | Reducir clases grandes sin cambiar comportamiento. | Paralela, pero después de 00 |
| 17 | `19_tanda_17_smoke_qa_cierre_release.md` | Smoke visual/exportable, validación de casos de uso y cierre de release. | Todas las anteriores ejecutadas |

## Reglas de avance

Una tanda solo puede cerrarse si cumple:

1. Compila.
2. No rompe apertura/guardado `.dms`.
3. No rompe importación/exportación de ejemplos oficiales.
4. No introduce botones muertos.
5. No agranda archivos por encima de los límites sin justificación.
6. Actualiza la documentación viva de la propia tanda.
7. Incluye smoke manual definido.

## Validaciones mínimas por cierre

En Windows, según disponibilidad:

```bat
scripts\02-ejecutar-tests.bat
scripts\08-validar-arquitectura.bat
scripts\10-validar-cierre-producto.bat
scripts\11-smoke-visual-uens.bat
```

Si una validación no existe o está desactualizada, la tanda debe documentarlo y no fingir cierre completo.

## Lectura contextual antes de ejecutar tandas

Antes de ejecutar una tanda grande, leer también:

```txt
contexto-continuidad-dms/README.md
contexto-continuidad-dms/00_resumen_ejecutivo_para_retomar.md
contexto-continuidad-dms/01_principios_no_negociables.md
contexto-continuidad-dms/04_tandas_implementacion_contextualizadas.md
```

La carpeta `contexto-continuidad-dms/` no define pasos ejecutables; conserva el razonamiento estratégico para que una nueva ventana o IA no implemente las tandas como parches mecánicos.


## Tandas ejecutadas

La Tanda 00 dejó evidencia documental en:

```txt
docs/implementacion/baseline_t00/
```

Ese directorio contiene fuentes de verdad, contradicciones, clases grandes, validaciones pendientes y decisiones de limpieza documental.

La Tanda 01 dejó evidencia técnica/documental en:

```txt
docs/implementacion/tanda_01_fuente_unica/
```

A partir de esta tanda, los tipos, capacidades, workspaces, ejemplos mínimos y ejemplos UENS oficiales deben nacer de `DefaultDiagramTypeDefinitions`.

La Tanda 02 dejó evidencia técnica/documental en:

```txt
docs/implementacion/tanda_02_workbench_comun/
```

A partir de esta tanda, los workspaces documentales/matriciales deben usar `StructuredWorkbenchView` o justificar explícitamente por qué conservan una carcasa propia.


La Tanda 03 dejó evidencia técnica/documental en:

```txt
docs/implementacion/tanda_03_acciones_toolbar/
```

A partir de esta tanda, toda acción contextual visible debe tener despacho centralizado en `DiagramToolbarActionExecutor` o quedar bloqueada por guardarraíles.

La Tanda 04 dejó evidencia técnica/documental en:

```txt
docs/implementacion/tanda_04_canvas_interaccion/
```

A partir de esta tanda, los adapters del canvas común deben preservar la selección manual de puntos intermedios durante la interacción directa del usuario, sin dejar que la sincronización pasiva del panel de propiedades la pise.


## Evidencia agregada

```txt
docs/implementacion/tanda_05_drawing_shape_kits/README.md
```

La Tanda 06 dejó evidencia técnica/documental en:

```txt
docs/implementacion/tanda_06_exportacion_profesional/
```

A partir de esta tanda, los formatos ofrecidos por la salida activa deben nacer de las capacidades oficiales y pasar por `ProjectExportFormatPolicy`; las extensiones de archivo deben normalizarse con `ExportTargetPathPolicy`.

La Tanda 07 dejó evidencia técnica/documental en:

```txt
docs/implementacion/tanda_07_diccionario_profesional/
```

A partir de esta tanda, el diccionario de datos debe tratarse como documento profesional de entrega: portada con metadatos, introducción opcional, referencia de logo opcional, tablas reales en PDF y Markdown exportado/importable.
La Tanda 08 dejó evidencia técnica/documental en:

```txt
docs/implementacion/tanda_08_modelado_datos_modelo_conceptual/
```

A partir de esta tanda, el puente modelo conceptual → diccionario existe como caso de uso de aplicación conservador: genera un borrador editable desde entidades y atributos, deja relaciones como notas de revisión y evita decisiones físicas automáticas.


La Tanda 09 dejó evidencia técnica/documental en:

```txt
docs/implementacion/tanda_09_procesos_bpmn_flujo_operativo/
```

A partir de esta tanda, BPMN básico y flujo operativo ya no deben tratarse como cajas genéricas: BPMN conserva eventos, tareas, gateways y carriles; flujo operativo conserva pasos, responsables, documentos/evidencias y orden humano.


La Tanda 10 dejó evidencia técnica/documental en:

```txt
docs/implementacion/tanda_10_arquitectura_c4_despliegue/
```

A partir de esta tanda, C4 Contexto, C4 Contenedores y Despliegue técnico deben conservar niveles de abstracción separados: contexto no baja a APIs/BD/servidores, contenedores no se mezcla con despliegue físico y despliegue técnico debe mostrar ambientes/nodos/conexiones con identidad visual propia.


La Tanda 11 dejó evidencia técnica/documental en:

```txt
docs/implementacion/tanda_11_uml_clases_estructural/
```

A partir de esta tanda, UML Clases debe tratarse como mapa estructural: las cajas crecen según atributos/métodos, los módulos funcionan como contenedores visuales y el layout inicial agrupa clases por módulo sin convertir el ViewModel en una clase gigante.



La Tanda 12 dejó evidencia técnica/documental en:

```txt
docs/implementacion/tanda_12_uml_comportamiento/
```

A partir de esta tanda, UML Casos de uso, UML Actividad y UML Estados deben conservar simbología UML propia y no volver a renderizarse como grafo genérico de cajas.


La Tanda 13 dejó evidencia técnica/documental en:

```txt
docs/implementacion/tanda_13_uml_secuencia_temporal/
```

A partir de esta tanda, UML Secuencia debe tratarse como línea temporal ordenada: participantes arriba, mensajes numerados, activaciones y fragmentos básicos, no como canvas libre con bend points.


La Tanda 14 dejó evidencia técnica/documental en:

```txt
docs/implementacion/tanda_14_aplicaciones_administrativas/
```

A partir de esta tanda, las aplicaciones administrativas deben conservar su lenguaje propio: mapa de módulos como agrupadores funcionales, roles/permisos como matriz, flujo de pantallas como navegación por módulos y wireframes como scaffolding primitivo, no diseñador visual complejo.


La Tanda 15 dejó evidencia técnica/documental en:

```txt
docs/implementacion/tanda_15_persistencia_dms_layout_assets/
```

A partir de esta tanda, el formato `.dms` vigente es `formatVersion = 3` y puede persistir un catálogo de assets del proyecto mediante rutas relativas controladas. Los logos/documentos opcionales no deben depender de rutas absolutas del equipo original.


La Tanda 16 dejó evidencia técnica/documental en:

```txt
docs/implementacion/tanda_16_refactor_srp_clases_grandes/
```

A partir de esta tanda, `MainShellCommandHandler` ya no debe absorber exportación por lote ni validación especializada. Esas responsabilidades viven en `ClientBatchExportCoordinator` y `ProjectValidationCoordinator`; el shell queda como fachada de comandos de alto nivel.

| 9 cierre | `TANDA_009_DEUDA_SRP_FOCALIZADA_CIERRE.md` | Cierre SRP focalizado sin refactor funcional; deuda auditada sin bloqueo real. |


## JavaDoc y onboarding de código

- `docs/implementacion/TANDA_JD_001_JAVADOC_DOMINIO_ONBOARDING.md`
- `docs/desarrollo/ONBOARDING_CODIGO_JAVADOC.md`
- `docs/calidad/AUDITORIA_JAVADOC_JD1.md`
- `TANDA_JD_002_JAVADOC_APLICACION_SERVICIOS.md`: JavaDoc de aplicación, servicios y onboarding de lectura.

- `TANDA_JD_003_JAVADOC_INFRAESTRUCTURA_FORMATOS.md`: JavaDoc de infraestructura, formatos y roundtrip.

## Línea JavaDoc pedagógica

- JD-1 — `docs/implementacion/TANDA_JD_001_JAVADOC_DOMINIO_ONBOARDING.md`.
- JD-2 — `docs/implementacion/TANDA_JD_002_JAVADOC_APLICACION_SERVICIOS.md`.
- JD-3 — `docs/implementacion/TANDA_JD_003_JAVADOC_INFRAESTRUCTURA_FORMATOS.md`.
- JD-4 — `docs/implementacion/TANDA_JD_004_JAVADOC_PRESENTATION_WORKBENCH_CANVAS.md`.

- `TANDA_JD_005_JAVADOC_SITIO_COBERTURA.md`: sitio JavaDoc, cobertura gradual y validación de `target/site/apidocs`.


- `TANDA_JD_006_JAVADOC_EJEMPLOS_PEDAGOGICOS.md`: JavaDoc con ejemplos pedagógicos en contratos de dominio, infraestructura, canvas y SideDock.


- `TANDA_JD_007_ONBOARDING_VIVO_ARQUITECTURA.md`: onboarding vivo de arquitectura, rutas de estudio por capas y conexión con JavaDoc/scripts.

## JavaDoc JD-8 — Guía de lectura por casos de uso completos

- `TANDA_JD_008_GUIA_CASOS_USO_COMPLETOS.md`: cierre de JD-8 con recorridos de lectura de punta a punta, guardarraíl fuente y script focalizado.

## JavaDoc JD-9 — ADRs pedagógicos y decisiones de diseño

- `TANDA_JD_009_ADRS_PEDAGOGICOS_DECISIONES_DISENO.md`: cierre de JD-9 con registros de decisión arquitectónica para estudiar alternativas, decisiones y consecuencias.
