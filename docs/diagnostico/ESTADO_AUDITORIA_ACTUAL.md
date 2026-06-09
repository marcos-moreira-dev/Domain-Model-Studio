# Estado de auditoría actual

Estado: **documentación viva corta**  
Actualizado en: **Tanda 9 — Deuda SRP focalizada aplicada como auditoría de cierre**

## Regla de verdad

La evidencia actual manda sobre bitácoras históricas.

Jerarquía recomendada:

```txt
1. log más reciente de tests/smoke local;
2. código actual;
3. catálogo Java de tipos y capacidades;
4. documentación viva;
5. bitácoras históricas.
```

## Último estado de tests confirmado

Criterio de cierre para liberar esta auditoría: la corrida global debe terminar en `BUILD SUCCESS`.

La evidencia local Windows más reciente compartida por el usuario confirma:

```txt
scripts\27-validar-cierre-tests-post-grafo-logico-productivo.bat
Tests run: 34, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS

scripts\02-ejecutar-tests.bat
Tests run: 1218, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

La Tanda 42 agregó validación integral del Grafo lógico. La Tanda 31 consolida esa evidencia como base automatizada verde para Release Candidate local. La Tanda 9 auditó la deuda SRP y queda aplicada sin refactor funcional porque no apareció bloqueo real. Aun así, app-image, MSI y smoke manual deben completarse si se quiere declarar un RC instalable aprobado.

## Estado aplicado en esta línea actual

```txt
28 — Corrección de log post Tanda 27 y plan UML Secuencia.
29 — UML Secuencia: fragmentos combinados.
30 — Guía académica UML Secuencia premium.
5B — Saneamiento teórico de ejemplos oficiales.
7B — Especificación del futuro grafo lógico.
8B — Limpieza GitHub / RC documental mínima.
29C — Corrección UML Secuencia y ejemplo oficial limpio.
29D — Redimensionado manual de fragmentos UML Secuencia.
29E — Movimiento manual de fragmentos UML Secuencia y selección azul.
32 — Contrato del proyecto Grafo lógico del negocio.
33 — Dominio del Grafo lógico.
34 — Parser y exportador Markdown del Grafo lógico.
35 — Derivación del Grafo lógico desde Levantamiento lógico.
36 — Canvas visual inicial del Grafo lógico.
37 — SideDock, propiedades, leyenda y ayuda operativa del Grafo lógico.
38 — Ejemplo oficial UENS del Grafo lógico.
39 — Recursos IA y plantilla oficial del Grafo lógico.
Alineación 1 — Contrato final de producto aplicado como documentación viva y guardarraíl fuente.
Alineación 2 — Fronteras arquitectónicas y zonas prohibidas aplicada como contrato de no contaminación y guardarraíl fuente.
Alineación 3 — Contrato semántico del Grafo lógico aplicado como contrato de identidad, nodos, relaciones, backbone y validación esperada.
Alineación 4 — Catálogo, capacidades, toolbar, workspace y exportación aplicada como contrato de cableado técnico antes de declarar el Grafo lógico como AVAILABLE.
Alineación 5 — Ayuda académica, ayuda operativa y recursos IA aplicada como contrato de frontera entre guía académica teórica, ayuda operativa del SideDock y recursos IA del Grafo lógico.
Alineación 6 — Validación integral y criterios de calidad aplicada como contrato de severidades, backbone, trazabilidad, criterios de calidad y alcance de Tanda 42.
Alineación 7 — Documentación anti-fachada, matrices y release aplicada como contrato documental para capacidades, casos de uso, smoke, recursos IA y release notes.
Alineación 8 — Plan quirúrgico de correcciones aplicada como ruta F1-F10 para ordenar fixes técnicos, pruebas focalizadas, matrices y release candidate.
Fix transversal — Puntos intermedios de conectores: aplicado ajuste para insertar nuevos puntos en el segmento más cercano de la relación y seleccionar el handle realmente creado, evitando deformaciones visuales al agregar puntos sucesivos.

Tanda 42 — Validación integral del Grafo lógico: aplicada con política semántica dedicada, `LogicalBusinessGraphIntegralValidationTest`, `LogicalBusinessGraphRelationKindTest`, refuerzo del exportador Markdown vacío y script `scripts\28-validar-tanda42-validacion-integral-grafo-logico.bat`.
Tanda 9 — Deuda SRP focalizada: aplicada como auditoría de cierre; sin bloqueo real, no activada como refactor funcional, con guardarraíl `Tanda9FocusedSrpDebtClosureSourceTest` y script `scripts\30-validar-tanda09-deuda-srp-focalizada.bat`.
```

## Capacidades relevantes actuales

```txt
UML Secuencia: participantes, mensajes, activaciones, autorelaciones y fragmentos combinados alt/opt/loop/par/break/critical/ref; corregida la clasificación accidental de participantes como fragmentos `par`; los fragmentos combinados pueden redimensionarse y moverse manualmente; la selección visual se refuerza en azul.
Guía académica: teoría premium de fragmentos combinados, guardas, operandos, anidación y relación con pruebas.
Ejemplos oficiales: roles/permisos, diccionario UENS, UML Actividad, UML Casos de uso y UML Secuencia saneados.
Levantamiento lógico: expediente documental y fuente lógica canónica, no canvas.
Grafo lógico de negocio: especificado como tipo propio logical-business-graph; ya tiene dominio puro propio, contrato semántico explícito de nodos/relaciones/backbone, catálogo/capacidades/workspace/toolbar/exportación, guía académica, ayuda operativa, recursos IA, persistencia .dms, SVG/PNG/Markdown, contrato documental anti-fachada, plan quirúrgico F1-F10 y validación integral aplicada con severidades, backbone, trazabilidad, riesgos y pendientes. Validación local Windows / Release Candidate aplicada a nivel de base automatizada verde; Tanda 9 auditada y cerrada sin refactor funcional por ausencia de bloqueo real; aún falta app-image/MSI/smoke manual si se requiere RC instalable aprobado.
README raíz: restaurado para GitHub con rutas de imágenes corregidas.
LICENSE: aviso conservador de todos los derechos reservados, no licencia open-source.
pom.xml: descripción actualizada al alcance multiproyecto administrativo.
SVG: vectorial documental, no WYSIWYG universal.
PNG: salida visual rápida para diagramas.
PDF: diccionario de datos y levantamiento lógico.
Markdown: intercambio principal con IA y reimportación según tipo.
```

## Decisión vigente sobre el futuro grafo lógico

```txt
Debe nacer como tipo propio.
Puede reutilizar infraestructura visual del grafo libre/canvas común.
No debe reutilizar FreeGraphDocument como dominio final.
No debe contaminar pantalla de inicio, modelo conceptual ni canvas conceptual.
Debe cubrir MF/FL/CU/ACC/RN/PRE/INV/POST/ENT/EST/REP/RISK/PEND.
Debe cubrir relaciones contiene, usa, reutiliza, ejecuta, aplica, requiere, protege, garantiza, crea, modifica, consulta, genera, alimenta, bloquea, habilita, depende_de y deriva_en.
Desde Tanda 33, esas relaciones existen como `LogicalBusinessGraphRelationKind` con validación semántica básica.
```

Documento vivo:

```txt
docs\arquitectura\19_plan_grafo_logico_negocio.md
docs\alineacion\ALINEACION_001_CONTRATO_FINAL_PRODUCTO.md
docs\alineacion\ALINEACION_002_FRONTERAS_ARQUITECTONICAS_ZONAS_PROHIBIDAS.md
docs\alineacion\ALINEACION_003_CONTRATO_SEMANTICO_GRAFO_LOGICO.md
docs\alineacion\ALINEACION_004_CATALOGO_CAPACIDADES_TOOLBAR_WORKSPACE_EXPORTACION.md
docs\alineacion\ALINEACION_005_AYUDA_ACADEMICA_OPERATIVA_RECURSOS_IA.md
docs\alineacion\ALINEACION_006_VALIDACION_INTEGRAL_CRITERIOS_CALIDAD.md
docs\alineacion\ALINEACION_007_DOCUMENTACION_ANTIFACHADA_MATRICES_RELEASE.md
docs\alineacion\ALINEACION_008_PLAN_QUIRURGICO_CORRECCIONES.md
docs\implementacion\fixes\FIX_PUNTOS_INTERMEDIOS_TRANSVERSALES.md
docs\implementacion\TANDA_042_VALIDACION_INTEGRAL_GRAFO_LOGICO.md
docs\implementacion\TANDA_009_DEUDA_SRP_FOCALIZADA_CIERRE.md
```

## Zonas protegidas

```txt
pantalla de inicio;
modelo conceptual;
canvas conceptual;
DiagramCanvasView;
DiagramCanvasViewModel;
ChenDiagramRenderer;
CrowsFootDiagramRenderer;
presentation/sidebar como sidebar legacy conceptual;
InteractiveCanvasSurfaceView solo puede tocarse por bug transversal comprobable, no por semántica del Grafo lógico.
```

Alineación 8 aplicada: plan quirúrgico F1-F10 queda como ruta de cierre; las correcciones deben hacerse con prueba focalizada, sin tocar zonas protegidas y sin actualizar matrices como completadas antes de tener evidencia.

## Pendientes antes de release instalable

```txt
App-image, MSI y smoke manual completo si se quiere declarar RC instalable aprobado.
Tanda 9 — Deuda SRP focalizada — aplicada; sin bloqueo real y no activada como refactor funcional.
```

## Comandos de cierre recomendados

```bat
scripts\02-ejecutar-tests.bat
scripts\13-revalidacion-local-completa.bat
scripts\29-validar-tanda31-release-candidate-local.bat
scripts\16-release-candidate.bat
```

## Documentos históricos que no deben mandar solos

```txt
docs/estado/ESTADO_ACTUAL.md
docs/historico/
docs/implementacion/ antiguos si contradicen esta auditoría
docs/post_cierre/
docs/release/ si contradice el log local actual
docs/desarrollo/PLAN_TANDAS_ACTUAL.md si contradice este estado vivo
```

Se conservan por trazabilidad, pero no son fuente de verdad actual si contradicen código, tests o este estado de auditoría.

## Tanda 43 — Instalable Windows y auditoría JavaDoc

Aplicada como preparación de cierre instalable y auditoría de documentación de código.

```txt
Documento: docs\implementacion\TANDA_043_INSTALABLE_JAVADOC_AUDITORIA.md
Guía instalable: docs\testeo\INSTALABLE_WINDOWS_RC_GUIA.md
Reporte instalable: docs\testeo\reportes\REPORTE_INSTALABLE_WINDOWS_RC.md
Auditoría JavaDoc: docs\calidad\AUDITORIA_JAVADOC_2026_05_24.md
Plan JavaDoc: docs\calidad\PLAN_TANDAS_JAVADOC.md
Script JavaDoc: scripts\31-generar-javadoc.bat
Script focalizado: scripts\32-validar-instalable-y-javadoc.bat
```

Lectura vigente: el proyecto tiene cobertura alta de JavaDoc en tipos públicos, pero deuda clara en métodos públicos. El instalable no queda bloqueado por JavaDoc; las tandas JavaDoc quedan planificadas para estudio pedagógico de ingeniería de software.


## JavaDoc y onboarding de código

- `docs/implementacion/TANDA_JD_001_JAVADOC_DOMINIO_ONBOARDING.md`
- `docs/desarrollo/ONBOARDING_CODIGO_JAVADOC.md`
- `docs/calidad/AUDITORIA_JAVADOC_JD1.md`
- JD-2 aplicada: contratos de aplicación documentados y onboarding ampliado.

- JD-3 aplicada: infraestructura de Markdown, .dms, SVG/PDF y recursos IA documentada sin tocar lógica funcional.


## Continuidad post-refactor actual

La línea vigente posterior al refactor conserva como contratos aplicados:

`ALINEACION_003_CONTRATO_SEMANTICO_GRAFO_LOGICO.md`, `ALINEACION_004_CATALOGO_CAPACIDADES_TOOLBAR_WORKSPACE_EXPORTACION.md`, `ALINEACION_005_AYUDA_ACADEMICA_OPERATIVA_RECURSOS_IA.md`, `ALINEACION_006_VALIDACION_INTEGRAL_CRITERIOS_CALIDAD.md`, `ALINEACION_007_DOCUMENTACION_ANTIFACHADA_MATRICES_RELEASE.md`, `ALINEACION_008_PLAN_QUIRURGICO_CORRECCIONES.md`.

Tanda 8B — Limpieza GitHub / RC documental mínima — aplicada. Tanda 9 — Deuda SRP focalizada — aplicada, sin bloqueo real, no activada. Tanda 31 — Validación local Windows / Release Candidate queda como antecedente; la continuidad actual se concentra en Tanda 38 y Tanda 39 post-refactor.
