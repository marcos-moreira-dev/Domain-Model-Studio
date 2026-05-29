# Mapa de documentación viva

Estado: **vigente después de Tanda 38A**  
Propósito: indicar qué documentos deben leerse para entender, usar, validar y continuar Domain Model Studio sin volver a planes históricos.

## Regla de lectura

La documentación ya no se lee como una bitácora lineal de tandas. La verdad vigente se obtiene así:

1. Código y tests actuales.
2. README raíz y scripts vigentes.
3. Contratos técnicos y recursos IA actuales.
4. Matrices de capacidades, casos de uso y smoke.
5. Plan de refactor vigente.
6. Documentos de auditoría solo cuando expliquen una decisión todavía activa.

## Documentos principales

| Documento | Uso |
|---|---|
| `README.md` | Descripción pública del producto, flujo de trabajo y ejecución. |
| `scripts/README.md` | Lista corta de scripts públicos vigentes. |
| `docs/README.md` | Entrada a esta carpeta y regla documental vigente. |
| `docs/estado/ESTADO_ACTUAL.md` | Estado corto actual, sin bitácora acumulada. |
| `docs/documentacion/POLITICA_DOCUMENTAL_REPOSITORIO.md` | Criterio para conservar o eliminar Markdown. |
| `docs/desarrollo/PLAN_TANDAS_ACTUAL.md` | Plan de refactor vigente. |
| `docs/raiz/PLAN_TANDAS_RESTANTES.md` | Continuidad por tandas restantes. |
| `docs/desarrollo/refactor/PLAN_REFACTOR_SOLID.md` | Estrategia del refactor integral. |
| `docs/desarrollo/refactor/MAPA_SEGURO_REFACTOR.md` | Zonas seguras, zonas sensibles y orden recomendado. |
| `docs/desarrollo/refactor/BASELINE_REFACTOR_TANDA_025.md` | Métricas base antes del refactor de aplicación. |
| `docs/producto/MATRIZ_CAPACIDADES_REALES.md` | Capacidades reales del producto. |
| `docs/testeo/MATRIZ_CASOS_USO_Y_TESTS.md` | Trazas internas de casos de uso y pruebas. |
| `docs/testeo/MATRIZ_CASOS_USO_CATEGORIZADA.md` | Casos de uso agrupados por familia. |
| `docs/tecnico/CONTRATO_IMPORTACION_MARKDOWN.md` | Contrato general de importación Markdown. |
| `docs/tecnico/CONTRATO_MARKDOWN_LEVANTAMIENTO_LOGICO.md` | Contrato canónico del Levantamiento lógico. |
| `docs/ia/RECURSOS_IA.md` | Recursos y ejemplos oficiales para trabajo con IA. |
| `docs/user-guide/README.md` | Guía de usuario. |

## Documentos de soporte técnico que siguen vigentes

| Familia | Documentos |
|---|---|
| Arquitectura | `docs/arquitectura/MAPA_ARQUITECTURA.md`, canvas común, SideDock, exportaciones, importación de código fuente UML. |
| Calidad/JavaDoc | `docs/calidad/PLAN_TANDAS_JAVADOC.md`, `docs/desarrollo/JAVADOC_SITIO_GUIA.md`, `docs/desarrollo/TANDA_038A_JAVADOC_POST_REFACTOR.md`, `docs/calidad/AUDITORIA_JAVADOC_POST_REFACTOR.md` y auditorías JD si se está revisando documentación de código. |
| Release | `docs/release/RELEASE_NOTES.md`, `docs/release/RELEASE_CANDIDATE_0_0_1.md`, `docs/release/LIMITACIONES_CONOCIDAS_0_0_1.md`, `docs/desarrollo/TANDA_DOC_001_DOCUMENTACION_RELEASE.md`. |
| Guía académica | `src/main/resources/help/topics/` como fuente visible dentro de la app. |
| Ejemplos | `examples/markdown/` y `src/main/resources/ai-resources/official-markdown/`. |

## Qué se eliminó en Tanda 27

- Bitácoras masivas de `docs/estado/` reemplazadas por un estado corto actual.
- Carpetas históricas sin valor operativo directo: `docs/historico/`, `docs/post_cierre/`, `docs/pendiente/`.
- Tandas Markdown de `docs/desarrollo/` que solo registraban pasos pasados y no explicaban una capacidad vigente.
- Diagnósticos intermedios sustituidos por el baseline y el plan de refactor vigente.

## Reglas de conservación

- Conservar si explica una capacidad actual, frontera técnica vigente, contrato, guía operativa, matriz de pruebas, limitación conocida o decisión activa.
- Eliminar si solo registra una tanda pasada, un intento sustituido, una hipótesis abandonada o un plan ya absorbido por documentación viva.
- No crear archivo histórico por defecto. El histórico solo se justifica si aporta auditoría, compatibilidad o diagnóstico concreto.

## Guardarraíl

La documentación viva debe ayudar a ejecutar, probar, usar o extender el producto. Si un Markdown obliga a leer historia para entender el presente, debe consolidarse o eliminarse.


## Refactor Markdown

La Tanda 32 queda documentada en `docs/desarrollo/TANDA_032_REFACTOR_MARKDOWN_IMPORT_DOCUMENT.md`: centraliza frontmatter/cuerpo para importadores Markdown sin cambiar gramáticas ni comportamiento visible.


## Refactor vigente reciente

- `docs/desarrollo/TANDA_033_VIEWMODELS_VISUALES_PROJECT_CHANGE_SUPPORT.md`: soporte común de listener/loading/notificación para ViewModels visuales/documentales, sin cambiar UX visible.


## Refactor UML Clases

- `docs/desarrollo/TANDA_034_REFACTOR_UML_CLASES_SOURCE_IMPORT.md`: extracción de la política de selección segura de Resumen para importación código→UML, sin cambiar parsing ni UX.


- `docs/desarrollo/TANDA_035_REFACTOR_CANVAS_CONCEPTUAL_LEGACY.md`: refactor focalizado del canvas conceptual legacy, sin migración forzada al canvas transversal.


## Artefactos compatibles legacy

- `docs/desarrollo/TANDA_036_ARTEFACTOS_COMPATIBLES_LEGACY_LEVANTAMIENTO_LOGICO.md`: cierre de la deuda de derivaciones visibles del Levantamiento lógico y contrato interno de borradores compatibles.


## UI/CSS vigente

- `src/main/resources/css/README.md` — superficie CSS viva.
- `docs/desarrollo/TANDA_037_CSS_NO_RADIUS_POR_ALCANCE.md` — contrato post-refactor de CSS/no-radius y recursos UI.


## Referencias contractuales conservadas tras limpieza

La limpieza documental no elimina contratos vigentes. Siguen siendo referencias vivas cuando se necesita auditar decisiones aplicadas:

- `docs/alineacion/ALINEACION_003_CONTRATO_SEMANTICO_GRAFO_LOGICO.md`
- `docs/alineacion/ALINEACION_004_CATALOGO_CAPACIDADES_TOOLBAR_WORKSPACE_EXPORTACION.md`
- `docs/alineacion/ALINEACION_005_AYUDA_ACADEMICA_OPERATIVA_RECURSOS_IA.md`
- `docs/alineacion/ALINEACION_006_VALIDACION_INTEGRAL_CRITERIOS_CALIDAD.md`
- `docs/alineacion/ALINEACION_007_DOCUMENTACION_ANTIFACHADA_MATRICES_RELEASE.md`
- `docs/alineacion/ALINEACION_008_PLAN_QUIRURGICO_CORRECCIONES.md`
- `docs/desarrollo/ONBOARDING_ARQUITECTURA_RUTA_ESTUDIO.md` — Onboarding vivo de arquitectura ? JD-7
- `docs/desarrollo/GUIA_CASOS_USO_COMPLETOS.md` — Ruta JD-8
- `docs/desarrollo/ADR_DECISIONES_DISENO_PEDAGOGICAS.md` — JD-9

Rebaseline documental — Tanda 25: conservar Markdown si explica una capacidad vigente; eliminar Markdown si solo registra una etapa pasada. La regla queda detallada en `POLITICA_DOCUMENTAL_REPOSITORIO.md`.

- `docs/diagnostico/MACRO_DIAGNOSTICO_TANDA_022.md` — diagnóstico macro conservado por valor vigente.
- `docs/testeo/PLAN_PRUEBAS_UI_E2E.md` — plan de pruebas UI/E2E vigente.

- Onboarding vivo de arquitectura — JD-7: `ONBOARDING_ARQUITECTURA_RUTA_ESTUDIO.md`.

- Guía de lectura por casos de uso completos — JD-8: `GUIA_CASOS_USO_COMPLETOS.md`.

- ADRs pedagógicos y decisiones de diseño — JD-9: `ADR_DECISIONES_DISENO_PEDAGOGICAS.md`.

- `docs/implementacion/TANDA_009_DEUDA_SRP_FOCALIZADA_CIERRE.md` — auditoría focalizada SRP; `ArchitectureBoundaryTest` y `ArchitectureStrongAuditTest` siguen como guardarraíles.


## JavaDoc post-refactor

- `docs/desarrollo/TANDA_038A_JAVADOC_POST_REFACTOR.md`: revisión de JavaDoc y guías técnicas después del refactor integral, sin cambiar comportamiento visible.
- `docs/calidad/AUDITORIA_JAVADOC_POST_REFACTOR.md`: auditoría corta de zonas revisadas y scripts vigentes de JavaDoc.
