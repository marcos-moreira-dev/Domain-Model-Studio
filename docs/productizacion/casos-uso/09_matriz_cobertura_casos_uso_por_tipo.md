# Matriz de cobertura de casos de uso por tipo visible

Estado: **registro vivo de cobertura funcional**  
Alcance: **menú, barra general, toolbar contextual, casos por subcategoría, ejemplos UENS y recursos IA**  
Fuente canónica de tipos: `DefaultDiagramTypeRegistry`

## Regla

Esta matriz no describe componentes GUI internos. Su función es comprobar que cada tipo visible tenga una ruta funcional humana:

```txt
Menú y barra general
→ crear/abrir/importar/guardar/exportar
→ toolbar contextual del tipo activo
→ caso de uso propio de la subcategoría
→ ejemplo UENS o referencia oficial
→ plantilla y recursos IA exportables
→ smoke visual/documental según corresponda
```

Si se agrega, renombra o elimina un tipo visible, esta matriz debe actualizarse junto con el catálogo, ejemplos, recursos IA y smoke.

## Casos transversales obligatorios

| Bloque | Documento vivo | Qué cubre |
|---|---|---|
| Menú y barra general | `docs/productizacion/casos-uso/01_controles_generales_gestion_proyectos.md` | Nuevo, abrir, guardar, cerrar, pestañas, estado, Toolbar contextual y ayuda. |
| Entrada, salida y recursos | `docs/productizacion/casos-uso/02_entrada_salida_exportacion.md` | Importación Markdown, ejemplos oficiales, recursos IA, PNG/SVG/Markdown/PDF y exportación por pestaña activa. |
| Canvas/interacción | `docs/productizacion/casos-uso/08_canvas_exportacion_interaccion_por_diagrama.md` | Paneo, zoom, selección, drag, puntos intermedios, layout persistente y exportación visual. |
| Smoke visual | `docs/productizacion/casos-uso/10_checklist_smoke_visual_por_tipo.md` | Validación manual por tipo con dominio UENS cuando aplique. |

## Matriz por tipo visible

| Tipo visible | ID oficial | Subcategoría funcional | Documento de casos de uso | Ejemplo UENS | Plantilla IA | Recursos IA |
|---|---|---|---|---|---|---|
| Modelo conceptual | `conceptual-model` | Modelo de datos / conceptual | `04_modelado_conceptual_y_uml_estructural.md` | `conceptual_model_uens_gordito_importable.md` | `conceptual_model.md` | Gramática, plantilla, ejemplo mínimo y ejemplo UENS exportable. |
| Diccionario de datos | `data-dictionary` | Documento técnico estructurado | `03_datos_y_administrativos.md` | `data_dictionary_uens_gordito.md` | `data_dictionary.md` | Gramática, plantilla, ejemplo mínimo y ejemplo UENS exportable. |
| BPMN básico | `bpmn-basic` | Proceso de negocio | `05_comportamiento_bpmn_uml.md` | `bpmn_basic_matricula_uens_gordito.md` | `bpmn_basic.md` | Gramática, plantilla, ejemplo mínimo y ejemplo UENS exportable. |
| Flujo operativo | `operational-flow` | Flujo operativo de negocio | `05_comportamiento_bpmn_uml.md` | `operational_flow_secretaria_uens_gordito.md` | `operational_flow.md` | Plantilla, ejemplo mínimo y ejemplo UENS exportable. |
| C4 Contexto | `c4-context` | Arquitectura de software | `06_arquitectura_c4_despliegue.md` | `c4_context_uens_gordito.md` | `c4_context.md` | Gramática C4, plantilla, ejemplo mínimo y ejemplo UENS exportable. |
| C4 Contenedores | `c4-containers` | Arquitectura de software | `06_arquitectura_c4_despliegue.md` | `c4_containers_uens_gordito.md` | `c4_containers.md` | Gramática C4, plantilla, ejemplo mínimo y ejemplo UENS exportable. |
| Despliegue técnico | `technical-deployment` | Arquitectura / infraestructura técnica | `06_arquitectura_c4_despliegue.md` | `technical_deployment_uens_gordito.md` | `technical_deployment.md` | Gramática, plantilla, ejemplo mínimo y ejemplo UENS exportable. |
| UML Casos de uso | `uml-use-case` | UML comportamiento observable | `05_comportamiento_bpmn_uml.md` | `uml_use_case_uens_gordito.md` | `uml_use_case.md` | Gramática, plantilla, ejemplo mínimo y ejemplo UENS exportable. |
| UML Clases | `uml-class` | UML estructural | `04_modelado_conceptual_y_uml_estructural.md` | `uml_class_uens_gordito.md` | `uml_class.md` | Gramática, plantilla, ejemplo mínimo y ejemplo UENS exportable. |
| UML Actividad | `uml-activity` | UML comportamiento | `05_comportamiento_bpmn_uml.md` | `uml_activity_registrar_matricula_uens_gordito.md` | `uml_activity.md` | Gramática, plantilla, ejemplo mínimo y ejemplo UENS exportable. |
| UML Secuencia | `uml-sequence` | UML interacción temporal | `05_comportamiento_bpmn_uml.md` | `uml_sequence_registrar_calificacion_uens_gordito.md` | `uml_sequence.md` | Gramática, plantilla, ejemplo mínimo y ejemplo UENS exportable. |
| UML Estados | `uml-state` | UML comportamiento por estados | `05_comportamiento_bpmn_uml.md` | `uml_state_matricula_uens_gordito.md` | `uml_state.md` | Gramática, plantilla, ejemplo mínimo y ejemplo UENS exportable. |
| Mapa de módulos | `admin-module-map` | Aplicación administrativa | `03_datos_y_administrativos.md` | `admin_module_map_uens_gordito.md` | `admin_module_map.md` | Gramática, plantilla, ejemplo mínimo y ejemplo UENS exportable. |
| Roles y permisos | `roles-permissions-map` | Matriz administrativa de seguridad | `03_datos_y_administrativos.md` | `roles_permissions_uens_gordito.md` | `roles_permissions_map.md` | Gramática, plantilla, ejemplo mínimo y ejemplo UENS exportable. |
| Flujo de pantallas | `screen-flow` | Navegación administrativa | `03_datos_y_administrativos.md` | `screen_flow_uens_gordito.md` | `screen_flow.md` | Gramática, plantilla, ejemplo mínimo y ejemplo UENS exportable. |
| Wireframes administrativos | `admin-wireframes` | Maqueta administrativa simple | `03_datos_y_administrativos.md` | `admin_wireframes_uens_gordito.md` | `admin_wireframes.md` | Gramática, plantilla, ejemplo mínimo y ejemplo UENS exportable. |
| Levantamiento lógico | `logical-business-intake` | Levantamiento y análisis documental | `07_levantamiento_logico_negocio.md` | `logical_business_intake_uens_gordito.md` | `logical_business_intake.md` | Plantilla canónica, ejemplo oficial UENS y recursos IA exportables/importables; Óptica queda solo como fixture/recurso IA histórico. |
| Grafo lógico del negocio | `logical-business-graph` | Levantamiento y análisis visual | `08_canvas_exportacion_interaccion_por_diagrama.md` / `07_levantamiento_logico_negocio.md` | `logical_business_graph_uens_gordito.md` | `logical_business_graph.md` | Gramática, prompt maestro, plantilla, ejemplo mínimo y ejemplo UENS exportable/importable. |
| Grafo libre | `free-graph` | Grafo matemático/informal | `08_canvas_exportacion_interaccion_por_diagrama.md` | `free_graph_uens_gordito.md` | `free_graph.md` | Gramática, plantilla, ejemplo mínimo y ejemplo UENS exportable. |

## Criterio de revisión recurrente

Los casos de uso son base escrita en piedra como promesa funcional, pero su estado debe revisarse cada vez que cambie alguno de estos elementos:

```txt
catálogo de tipos;
capacidades visibles;
parser Markdown;
workspace activo;
exportación;
recursos IA;
ejemplos oficiales;
smoke visual o documental.
```

La actualización debe evitar dos errores:

1. **Falsa promesa:** un formato, ejemplo o acción aparece visible pero no pasa por una ruta real.
2. **Falsa deuda:** un documento viejo dice que algo no existe aunque la implementación actual ya lo soporte.

## Relación con implementación posterior

La Implementación 12 consolida esta matriz como guardarraíl documental y sincroniza los ejemplos UENS con el paquete exportable de recursos IA. La Implementación 14 debe usarla como checklist para el smoke visual/exportable final.
