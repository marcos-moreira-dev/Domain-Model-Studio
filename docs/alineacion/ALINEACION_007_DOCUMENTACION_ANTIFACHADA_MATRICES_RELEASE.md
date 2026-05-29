# Alineación 007 — Documentación anti-fachada, matrices y release

Estado: **alineación aplicada**  
Fecha: **2026-05-24**  
Alcance: **contrato documental de producto, matrices de capacidades/casos de uso/smoke y release candidate**  
Tipo de cambio: **documental y de guardarraíl fuente; no modifica lógica de ejecución**

## 1. Propósito

Esta alineación fija cómo deben actualizarse las matrices de producto y los documentos de release para que `logical-business-graph` no cierre como una fachada documental. La regla central es:

```txt
capacidad visible = implementación real + prueba focalizada + documentación viva + smoke verificable cuando aplique
```

La documentación no debe prometer que un tipo de proyecto está cerrado si el catálogo, toolbar, salida activa, persistencia, validación, ayuda académica, recursos IA y smoke no hablan el mismo idioma.

## 2. Relación con alineaciones anteriores

- La Alineación 001 definió el contrato final de producto.
- La Alineación 002 protegió pantalla de inicio, modelo conceptual y canvas conceptual.
- La Alineación 003 fijó la semántica del Grafo lógico del negocio.
- La Alineación 004 fijó el contrato de catálogo, capacidades, workspace, toolbar y exportación.
- La Alineación 005 separó guía académica, ayuda operativa y recursos IA.
- La Alineación 006 fijó la validación integral y los criterios de calidad.

Esta Alineación 007 fija cuándo y cómo las matrices y documentos de release deben reconocer el nuevo tipo visible.

## 3. Principio anti-fachada documental

La documentación viva tiene que ser posterior al contrato técnico, no un sustituto del contrato técnico. Por tanto:

```txt
No se debe marcar una capacidad como completa si todavía falta su implementación o su prueba.
No se debe registrar un smoke como aprobado si no fue ejecutado localmente.
No se debe declarar AVAILABLE si el workspace real no abre como PRODUCT_VIEW.
No se debe declarar EXPORT_PNG, EXPORT_SVG, SAVE_DMS o LOAD_DMS si el flujo real no existe.
No se debe declarar THEORY_HELP si no existe tema académico registrado.
No se debe declarar AI_RESOURCES si catálogo, descriptores y docs IA no están alineados.
```

La matriz puede registrar estados parciales, pero no debe convertir pendientes en éxitos silenciosos.

## 4. Documentos obligatorios a sincronizar

Cuando las tandas técnicas cierren, `logical-business-graph` debe quedar sincronizado como mínimo en:

```txt
docs/producto/MATRIZ_CAPACIDADES_REALES.md
docs/testeo/MATRIZ_CASOS_USO_Y_TESTS.md
docs/testeo/MATRIZ_CASOS_USO_CATEGORIZADA.md
docs/productizacion/casos-uso/09_matriz_cobertura_casos_uso_por_tipo.md
docs/productizacion/casos-uso/10_checklist_smoke_visual_por_tipo.md
docs/implementacion/tanda_17_smoke_qa_cierre_release/01_matriz_smoke_16_tipos.md
docs/ia/RECURSOS_IA.md
docs/release/RELEASE_CANDIDATE_0_0_1.md
docs/release/LIMITACIONES_CONOCIDAS_0_0_1.md
docs/release/RELEASE_NOTES.md
```

Si alguno de esos documentos sigue tratando el Grafo lógico como futuro, experimental no cerrado o ausente, el release candidate no debe considerarse completo.

## 5. Estado documental actual del Grafo lógico

El Grafo lógico del negocio ya existe parcialmente en código y recursos:

```txt
logical-business-graph
Grafo lógico del negocio
LogicalBusinessGraphDocument
LogicalBusinessGraphMarkdownParser
LogicalBusinessGraphMarkdownExporter
LogicalBusinessGraphEditorView
LogicalBusinessGraphViewModel
LogicalBusinessGraphWorkbenchContributor
logical_business_graph.md
logical_business_graph_minimo.md
logical_business_graph_uens_gordito.md
```

Pero las matrices de producto todavía deben actualizarse después de cerrar Tanda 40, Tanda 41 y Tanda 42. Hasta entonces, la documentación debe decir la verdad: tipo avanzado, pero pendiente de cierre final.

## 6. Fila esperada en matriz de capacidades

La matriz de capacidades reales debe incluir una fila explícita para:

```txt
ID oficial: logical-business-graph
Nombre: Grafo lógico del negocio
Familia: Levantamiento y análisis
Naturaleza: visual semántica derivada del levantamiento lógico
```

Capacidades objetivo después del cierre técnico:

| Capacidad | Estado esperado para cierre | Evidencia mínima |
|---|---|---|
| Crear proyecto | Sí | abre workspace `LOGICAL_BUSINESS_GRAPH_DIAGRAM` como `PRODUCT_VIEW` |
| Importar Markdown | Sí | parser + dispatcher + ejemplo UENS |
| Editar manualmente | Sí, mínimo propiedades/movimiento | ViewModel + SideDock + canvas transversal |
| Exportar Markdown | Sí | exporter + active output + toolbar |
| Exportar PNG | Sí | salida no vacía del canvas |
| Exportar SVG | Sí | salida vectorial del grafo |
| Guardar `.dms` | Sí | persistencia especializada del documento y layout |
| Abrir `.dms` | Sí | roundtrip de proyecto |
| Recursos IA | Sí | gramática, prompt, plantilla, mínimo y UENS |
| Guía académica | Sí | topic académico + figura didáctica |
| PDF | No, salvo decisión futura | no prometer PDF si no aplica |

Si alguna evidencia falta, la celda debe quedar como pendiente o parcial, no como completada.

## 7. Casos de uso mínimos a registrar

Las matrices de casos de uso deben contener o mapear casos equivalentes para:

```txt
UC-LBG-001 crear proyecto Grafo lógico del negocio.
UC-LBG-002 importar Markdown logical-business-graph.
UC-LBG-003 abrir ejemplo oficial UENS.
UC-LBG-004 seleccionar nodo y relación.
UC-LBG-005 mover nodo y conservar layout.
UC-LBG-006 editar propiedades de documento, nodo y relación.
UC-LBG-007 validar proyecto desde Diagrama > Validar proyecto.
UC-LBG-008 exportar Markdown y reimportar.
UC-LBG-009 exportar PNG.
UC-LBG-010 exportar SVG.
UC-LBG-011 guardar y reabrir .dms.
UC-LBG-012 abrir ayuda operativa del SideDock.
UC-LBG-013 abrir guía académica del menú Ayuda.
UC-LBG-014 exportar recursos IA del tipo.
UC-LBG-015 revisar trazabilidad MF → FL → CU → ACC → RN/PRE/INV/POST/ENT/EST/REP/RISK/PEND.
```

Estos casos no deben mezclarse con el Grafo libre. La matriz debe dejar claro que el Grafo lógico tiene dominio, semántica y validación propios.

## 8. Checklist smoke visual mínimo

El checklist de smoke visual debe incluir una sección específica para el Grafo lógico:

```txt
1. Abrir ejemplo oficial logical_business_graph_uens_gordito.md.
2. Confirmar leyenda visible de abreviaciones.
3. Confirmar nodos MF, FL, CU, ACC, RN, PRE, INV, POST, ENT, EST, REP, RISK y PEND.
4. Confirmar relaciones contiene, usa, ejecuta, aplica, requiere, protege, garantiza, crea, modifica, genera, alimenta, bloquea, habilita, depende_de y deriva_en.
5. Confirmar al menos un caso de relación consulta cuando el ejemplo o plantilla lo cubra.
6. Seleccionar nodo desde canvas y desde estructura.
7. Seleccionar relación desde canvas y desde estructura.
8. Mover nodo y verificar cambios sin guardar.
9. Guardar .dms, cerrar y reabrir.
10. Exportar Markdown y reimportar.
11. Exportar PNG.
12. Exportar SVG.
13. Abrir ayuda operativa del SideDock.
14. Abrir guía académica desde menú Ayuda.
15. Confirmar que no cae al canvas conceptual.
16. Confirmar que no usa FreeGraphDocument como dominio.
```

El smoke debe ser reproducible por el usuario en Windows y debe permitir retroalimentación concreta.

## 9. Release candidate

Antes de marcar release candidate, los documentos de release deben decir explícitamente qué estado tiene `logical-business-graph`:

```txt
Incluido como tipo disponible.
Incluido como tipo experimental cerrado parcialmente.
O excluido del RC si no alcanza el contrato.
```

La opción preferida es incluirlo como `AVAILABLE` solo cuando Tanda 40, Tanda 41, Tanda 42 y validación local hayan pasado. Si no, el release debe declararlo como limitación conocida.

## 10. Limitaciones conocidas

Si alguna capacidad queda fuera del cierre, `LIMITACIONES_CONOCIDAS_0_0_1.md` debe declararla de forma honesta. Ejemplos:

```txt
El Grafo lógico permite edición de propiedades y movimiento, pero no CRUD completo de nodos/relaciones.
Las etiquetas de relaciones no tienen posicionamiento manual persistente.
La validación semántica detecta advertencias principales, pero no reemplaza revisión humana.
La relación consulta queda documentada, pero su cobertura visual depende de ejemplos futuros.
```

No registrar una limitación conocida cuando se sabe que existe es una forma de fachada.

## 11. Recursos IA y documentación IA

`docs/ia/RECURSOS_IA.md` debe incluir `logical-business-graph` con:

```txt
18_grafo_logico_negocio_gramatica.md
19_grafo_logico_negocio_prompt_ia.md
official-markdown/plantillas/logical_business_graph.md
official-markdown/diagramas/logical_business_graph_minimo.md
official-markdown/diagramas/logical_business_graph_uens_gordito.md
```

La documentación debe aclarar:

```txt
El prompt maestro no se importa como proyecto.
El prompt maestro guía a la IA para producir Markdown importable.
La plantilla, el mínimo y el UENS sí son Markdown importable.
```

## 12. Orden obligatorio de actualización documental

La documentación de release debe actualizarse en este orden:

```txt
1. Cerrar contrato técnico de catálogo/capacidades.
2. Cerrar guía académica del Grafo lógico.
3. Cerrar persistencia .dms y exportaciones.
4. Cerrar validación integral.
5. Actualizar matriz de capacidades.
6. Actualizar matrices de casos de uso.
7. Actualizar checklist smoke visual.
8. Actualizar docs IA.
9. Actualizar release notes y limitaciones conocidas.
10. Ejecutar validación local Windows.
```

Actualizar matrices antes de los puntos 1 a 4 solo debe hacerse si se registra estado parcial de forma explícita.

## 13. Guardarraíles esperados

La línea final debe mantener o agregar guardarraíles que detecten:

```txt
- tipo visible ausente en matriz de capacidades;
- tipo visible ausente en matriz de casos de uso;
- tipo visible ausente en matriz categorizada;
- tipo visible ausente en checklist smoke;
- capacidad declarada sin toolbar o salida activa;
- recurso IA declarado pero no documentado;
- guía académica declarada pero sin topic;
- release candidate que omite una limitación conocida relevante;
- tests acoplados a ubicaciones viejas cuando el catálogo fue delegado correctamente.
```

El objetivo no es hacer tests frágiles, sino impedir que una promesa visible quede sin evidencia.

## 14. Criterio de cierre de esta alineación

Esta alineación se considera aplicada cuando existe este documento, existe test focalizado, existe script de validación y los documentos vivos apuntan al contrato.

No se considera actualizada todavía la matriz final de capacidades ni el release candidate. Eso queda para después de cerrar las tandas técnicas.

## 15. Tests focalizados esperados

```bat
scripts-validar-alineacion-07.bat
```

También puede ejecutarse directamente:

```bat
mvn -Dtest=Alineacion7DocumentationReleaseContractSourceTest test
```
