# Productización — Índice de planificación

> Nota T00: las carpetas antiguas `planificacion-alineacion-visual/` y `planificacion-implementacion-visual/` ya no están activas en la raíz. Fueron movidas a `docs/historico/planificacion_legacy/`. Las referencias de este archivo a esas rutas se conservan como memoria del proceso, no como plan vivo. El plan actual está en `docs/implementacion/`.


Estado: **planificación completa PF00-PF09**  
Tipo: **alineación funcional / producto / arquitectura de escritorio**  
Alcance: **no implementa código; ordena el trabajo posterior**

> Nota de vigencia según Planificación 9: esta carpeta conserva la planificación de productización PF00-PF09 y la matriz anti-fachada. Para decisiones nuevas manda `docs/implementacion/`. Para casos de uso manda `docs/productizacion/casos-uso/`.

## Propósito

Esta carpeta contiene las tandas de planificación para convertir Domain Model Studio en un producto funcionalmente alineado: cada tipo visible debe abrir su salida visual o documental real, usar herramientas coherentes, guardar/cargar, exportar lo que promete y estar explicado por una ayuda integrada seria.

## Tandas de planificación

| Tanda | Documento | Estado | Propósito |
|---|---|---|---|
| PF00 | `PF00_contrato_alineacion_funcional.md` | Ejecutada | Congelar contrato de producto, principios SRP y brechas críticas antes de implementar. |
| PF01 | `PF01_inventario_brechas_por_tipo.md` | Ejecutada | Inventario exhaustivo por tipo visible: cumple/parcial/roto/no aplica. |
| PF02 | `PF02_shell_multiproyecto_multitipo.md` | Ejecutada | Diseño del shell multiproyecto/multitipo por pestaña activa. |
| PF03 | `PF03_contrato_workspace_activo.md` | Ejecutada | Contrato de workspace/editor activo y fachadas de acciones. |
| PF04 | `PF04_menus_toolbars_capacidades.md` | Ejecutada | Alineación de menús, toolbars, comandos globales y capacidades reales. |
| PF05 | `PF05_importacion_layout_persistencia_por_tipo.md` | Ejecutada | Importación Markdown, layout inicial y persistencia por tipo. |
| PF06 | `PF06_estado_paneles_contextuales.md` | Ejecutada | Barra de estado, estructura y propiedades contextuales por tipo. |
| PF07 | `PF07_centro_ayuda_micro_wikipedia.md` | Ejecutada | Centro de ayuda como micro-Wikipedia teórica, matriz de contenido y recursos gráficos didácticos. |
| PF08 | `PF08_ejemplos_gorditos_uens_y_selector.md` | Ejecutada | Ejemplos gorditos UENS/colegio y selector de ejemplos oficiales. |
| PF09 | `PF09_plan_pruebas_cierre_funcional.md` | Ejecutada | Plan de pruebas de cierre funcional y smoke test visual real. |

## Documentos complementarios

```text
PF07_matriz_contenidos_teoricos.md
PF07_recursos_graficos_didacticos.md
PF08_matriz_ejemplos_oficiales_uens.md
PF09_matriz_smoke_visual_por_tipo.md
MATRIZ_CASOS_USO_TRAZABILIDAD_NO_FACHADA.md
docs/productizacion/casos-uso/08_canvas_exportacion_interaccion_por_diagrama.md
```

## Regla de esta fase

No se debe implementar en estas tandas. La salida esperada son documentos que permitan implementar después sin parches grandes, sin mezclar responsabilidades y sin volver a declarar capacidades falsas.

## Fase de implementación I13

La planificación PF00-PF09 ya se convirtió en una secuencia de implementación numerada para avanzar por tandas pequeñas, trazables y con responsabilidad única.

```text
Tanda 1  = I13-A  Guardarraíles y estructura base
Tanda 2  = I13-B  Shell multiproyecto/multitipo
Tanda 3  = I13-C  Conectar diagramas de comportamiento
Tanda 4  = I13-D  Migrar workspaces existentes al contrato común
Tanda 5  = I13-E  Menús, toolbars y capacidades reales
Tanda 6  = I13-F  Importación Markdown, layout y persistencia por tipo
Tanda 7  = I13-G  Estado inferior, estructura y propiedades contextuales
Tanda 8  = I13-H  Centro de ayuda micro-Wikipedia teórica
Tanda 9  = I13-I  Figuras didácticas para la ayuda
Tanda 10 = I13-J  Selector de ejemplos oficiales
Tanda 11 = I13-K  Ejemplos gorditos UENS/colegio
Tanda 12 = I13-L  Wireframes como maquetas/scaffolding
Tanda 13 = I13-M  Edición de líneas y puntos intermedios
Tanda 14 = I13-N  Limpieza técnica y trazabilidad humana
Tanda 15 = I13-O  Cierre funcional real
```

## Inicio de implementación I13

La implementación va por **I13-N — Limpieza técnica y trazabilidad humana**.

Archivos de estado:

```txt
docs/estado/I13A_estado_despues_guardarrailes_workspace.md
docs/estado/I13B_estado_despues_shell_multiproyecto.md
docs/estado/I13C_estado_despues_comportamiento_conectado.md
docs/estado/I13D_estado_despues_contrato_comun_workspaces.md
docs/estado/I13E_estado_despues_menus_toolbars_capacidades.md
docs/estado/I13F_estado_despues_importacion_layout_persistencia.md
docs/estado/I13G_estado_despues_estado_paneles_contextuales.md
docs/estado/I13H_estado_despues_centro_ayuda_micro_wikipedia.md
docs/estado/I13I_estado_despues_figuras_didacticas.md
docs/estado/I13J_estado_despues_selector_ejemplos.md
docs/estado/I13K_estado_despues_ejemplos_gorditos_uens.md
docs/estado/I13L_estado_despues_wireframes_scaffolding.md
docs/estado/I13M_estado_despues_edicion_lineas_puntos.md
docs/estado/I13N_estado_despues_limpieza_trazabilidad.md
```

I13-A agregó contratos y pruebas para el ruteo `tipo de diagrama -> familia de workspace`. I13-B empezó a usar ese ruteo para montar el workspace de la pestaña activa. I13-C conectó visualmente los diagramas de comportamiento al shell. I13-D migró los workspaces existentes al contrato común de `WorkspaceDescriptor`. I13-E alineó menús, toolbars y exportaciones con el catálogo oficial de capacidades. I13-F corrigió la preparación visual de importaciones: Chen/pata de gallo solo para modelo conceptual y documentos especializados preservados por tipo. I13-G agregó resumen contextual por tipo en la barra inferior y limitó explícitamente los paneles conceptuales al workspace conceptual. I13-H convirtió la ayuda en centro teórico tipo micro-Wikipedia, cargado desde recursos Markdown. I13-I agregó referencias de figuras didácticas en las fichas teóricas y una fábrica visual JavaFX basada en geometrías primitivas. I13-J integró un selector de ejemplos oficiales por tipo y eliminó el acceso rígido al ejemplo conceptual fijo. I13-K reemplazó la selección oficial fragmentada por una familia coherente de ejemplos UENS/colegio para todos los tipos visibles. I13-L formalizó wireframes como maquetas/scaffolding administrativo con nuevos bloques de geometría primitiva y plantillas de pantalla. I13-M incorporó selección puntual de puntos intermedios de líneas conceptuales y eliminación segura sin borrar toda la relación. I13-N redujo el acoplamiento del shell mediante `SpecializedWorkspaceCoordinator`, agregó guardarraíles de trazabilidad y limpió textos obsoletos en recursos teóricos/IA.


## Avance de implementación I13

| Tanda | Estado | Resultado |
|---|---|---|
| I13-A | Implementada | Guardarraíles de ruteo `tipo de diagrama -> familia de workspace`. |
| I13-B | Implementada | Shell usando `WorkspaceRouteResolver` y `WorkspaceViewRegistry` para montar el workspace de la pestaña activa. |
| I13-C | Implementada | `BehaviorDiagramEditorView` conectado como workspace real para BPMN, flujo operativo y UML de comportamiento. |
| I13-D | Implementada | Workspaces existentes registrados con `WorkspaceDescriptor`; mensajes y política de paneles centralizados. |
| I13-E | Implementada | Menús, toolbars y exportaciones alineados con capacidades reales del catálogo. |
| I13-F | Implementada | Importación Markdown prepara layout conceptual por notación y conserva documentos especializados. |
| I13-G | Implementada | Barra inferior contextual por tipo y paneles conceptuales limitados al modelo conceptual. |
| I13-H | Implementada | Centro de ayuda micro-Wikipedia teórica con fichas Markdown editables. |
| I13-I | Implementada | Figuras didácticas por tipo visible, renderizadas con geometrías primitivas. |
| I13-J | Implementada | Selector de ejemplos oficiales por tipo de diagrama. |
| I13-K | Implementada | Ejemplos oficiales gorditos UENS/colegio para todos los tipos visibles. |
| I13-L | Implementada | Wireframes formalizados como maquetas/scaffolding con plantillas de pantalla y bloques administrativos ampliados. |
| I13-M | Implementada | Selección y eliminación segura de puntos intermedios de líneas conceptuales; IMP-01 retira menú contextual/doble clic y deja toolbar + Suprimir/Backspace. |
| I13-N | Implementada | Limpieza técnica: coordinador de workspaces especializados, guardarraíles de SRP y alineación de recursos/documentación obsoleta. |
| IMP-01 | Implementada | Shell con pestañas escrolleables, plantillas de wireframe en toolbar contextual y clic derecho reservado para paneo. |

---

## Nueva subfase: alineación visual por tipo de diagrama

Después de I13-N se detectó una brecha adicional: algunos workspaces especializados dibujan cajas, tarjetas o conexiones, pero no se comportan todavía como lienzos interactivos comparables al modelo conceptual. La nueva planificación queda en una carpeta de raíz:

```text
planificacion-alineacion-visual/
```

| Tanda | Documento raíz | Estado | Propósito |
|---|---|---|---|
| PF10 | `planificacion-alineacion-visual/PF10_inventario_visual_por_diagrama.md` | Planificada | Inventario visual tipo por tipo: lienzo, matriz, documento o híbrido. |
| PF11 | `planificacion-alineacion-visual/PF11_reutilizacion_gui_modelo_conceptual.md` | Planificada | Reutilización prudente de infraestructura GUI del modelo conceptual. |

También se registró como pendiente la pantalla de inicio de referencia:

```text
planificacion-alineacion-visual/PENDIENTE_pantalla_inicio_hub_producto.md
planificacion-alineacion-visual/referencias/pantalla_inicio_referencia.png
```

La regla de esta subfase es: los diagramas de nodos, cajas, líneas o geometrías primitivas deben intentar reutilizar zoom, paneo, selección, arrastre, conectores, exportación PNG/SVG y propiedades visuales del modelo conceptual; los editores tipo matriz o documento no deben forzarse a canvas libre.

### Avance adicional de planificación visual

Se agregaron dos documentos más en la carpeta raíz de alineación visual:

```text
planificacion-alineacion-visual/PF12_contrato_lienzo_interactivo_comun.md
planificacion-alineacion-visual/PF13_revision_lienzos_por_diagrama.md
planificacion-alineacion-visual/PF12_PF13_manifest_cambios.md
```

PF12 define el contrato mínimo para que los diagramas tipo lienzo reutilicen infraestructura GUI del modelo conceptual: zoom, paneo, selección, arrastre, conectores, persistencia de posiciones y exportación PNG/SVG. PF13 revisa los diagramas visuales uno por uno: modelo conceptual, mapa de módulos, flujo de pantallas, wireframes, UML, BPMN, C4 y despliegue técnico.


### Avance adicional de planificación visual — PF14/PF15

Se agregaron dos documentos más en la carpeta raíz de alineación visual:

```text
planificacion-alineacion-visual/PF14_revision_editores_estructurados.md
planificacion-alineacion-visual/PF15_svg_y_propiedades_visuales.md
planificacion-alineacion-visual/PF14_PF15_manifest_cambios.md
```

PF14 separa los editores estructurados que no deben forzarse a canvas libre: roles/permisos como matriz de autorización y diccionario de datos como tabla/documento técnico. PF15 define la política de exportación SVG real y de propiedades visuales por familia, reutilizando con cuidado el patrón visual del modelo conceptual solo donde el tipo de diagrama lo justifique.


### Cierre de planificación visual — PF16/PF17

Se agregaron las últimas dos tandas de la subfase de alineación visual:

```text
planificacion-alineacion-visual/PF16_ergonomia_producto_inicio_tabs_ejemplos.md
planificacion-alineacion-visual/PF17_plan_implementacion_y_pruebas_por_diagrama.md
planificacion-alineacion-visual/PF16_PF17_manifest_cambios.md
```

PF16 planifica la ergonomía general: pantalla de inicio como hub de producto, pestañas manejables/escrolleables, apertura de todos los ejemplos o de la familia UENS, y mensajes visibles más claros.

PF17 convierte PF10-PF16 en una secuencia de implementación por familias de diagrama y pruebas visuales por tipo, priorizando la reutilización prudente de infraestructura GUI del modelo conceptual para diagramas de lienzo.

Con esto, la planificación visual PF10-PF17 queda completa.

## Planificación fina de implementación visual

Se agregó la carpeta raíz:

```text
planificacion-implementacion-visual/
```

Contiene el desglose detallado de implementación AV-I01 a AV-I12 para ejecutar la alineación visual por tipo de diagrama, reutilizando infraestructura GUI del modelo conceptual cuando tenga sentido y evitando convertir editores estructurados en canvas libre.

---

## Subcarpeta de casos de uso y porcentajes anti-fachada

Se agregó una subcarpeta específica para separar los casos de uso por categoría, controles generales, gestión de proyectos y tipo de diagrama:

```text
docs/productizacion/casos-uso/
```

Documentos principales:

```text
docs/productizacion/casos-uso/00_indice_casos_uso.md
docs/productizacion/casos-uso/01_controles_generales_gestion_proyectos.md
docs/productizacion/casos-uso/02_entrada_salida_exportacion.md
docs/productizacion/casos-uso/03_datos_y_administrativos.md
docs/productizacion/casos-uso/04_modelado_conceptual_y_uml_estructural.md
docs/productizacion/casos-uso/05_comportamiento_bpmn_uml.md
docs/productizacion/casos-uso/06_arquitectura_c4_despliegue.md
docs/productizacion/casos-uso/07_resumen_porcentajes_y_brechas.md
```

Regla de lectura: estos porcentajes son estimaciones humanas por inspección estática y capturas, no cobertura automática. Sirven para decidir qué está terminado, qué está parcial y qué podría ser una utilidad visible sin respaldo funcional suficiente.


## Guardarraíl posterior de canvas/exportación

Se agregó el documento:

```text
docs/productizacion/casos-uso/08_canvas_exportacion_interaccion_por_diagrama.md
planificacion-implementacion-visual/AV-I00_guardarrailes_canvas_exportacion_interaccion.md
```

Este guardarraíl corrige la lectura de I13-M: la eliminación de puntos intermedios existe, pero el clic derecho debe reservarse para paneo. La eliminación debe quedar en toolbar/atajo de teclado para no mezclar gestos. También fija que los diagramas visuales deben migrar progresivamente a un canvas común con PNG y SVG reales.

## UX/UI y scaffolding de herramienta

- `ui-ux/00_indice_ui_ux.md`: índice de criterios UX/UI.
- `ui-ux/01_criterios_toolbar_paneles_canvas.md`: separación toolbar / paneles / canvas.
- `ui-ux/02_auditoria_inconsistencias_ui_por_vista.md`: inconsistencias detectadas por vista.
- `ui-ux/03_estilo_visual_tokens_paleta_sobria.md`: paleta, tokens y fondo sobrio.
- `ui-ux/04_plan_alineacion_ui_canvas_exportacion.md`: orden recomendado de alineación.

## Planificación de aterrizaje integral

La planificación profunda para convertir los criterios de interfaz, canvas, exportación y trazabilidad en tandas ejecutables está en:

```text
planificacion-implementacion-visual/aterrizaje-planificacion/00_indice_aterrizaje_planificacion.md
```


## Tandas de implementación fina visual

Se agregó una planificación de implementación fina, basada en lectura estática del código actual:

```text
planificacion-implementacion-visual/tandas-implementacion-fina/00_indice_implementacion_fina.md
```

Esta carpeta diferencia patrón Facade útil en código de “fachada” como utilidad visible sin respaldo real, y aterriza la ruta para que los diagramas sean editables, persistibles y exportables con PNG/SVG reales cuando el tipo aplique.
- `casos-uso/10_checklist_smoke_visual_por_tipo.md`: smoke visual por tipo usando UENS como unidad educativa.
