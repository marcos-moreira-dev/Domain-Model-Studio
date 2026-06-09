# Casos de uso — modelo conceptual y UML estructural

Estado: **matriz de control de modelado estructural**  
Alcance: modelo conceptual ER y UML clases.

---

## Resumen por tipo

| Tipo | % al ojo | Estado | Lectura rápida |
|---|---:|---|---|
| Modelo conceptual | 88% | Más maduro | Es la base de interacción visual: zoom, paneo, selección, arrastre, conectores, puntos intermedios, SVG/PNG/Markdown. |
| UML Clases | 65% | Funcional básico | Tiene dominio/parser/editor/exportación, pero falta layout por módulos/agrupadores e interacción visual comparable. |

---

# Modelo conceptual

Promesa correcta: **representar entidades, atributos, relaciones y cardinalidades del dominio en notación Chen o pata de gallo**.

| Caso | Estado | % | Anclaje observado | Falta para cierre |
|---|---|---:|---|---|
| CON-01 Crear modelo conceptual | Terminado funcional | 95% | `DiagramProject`, `DiagramModel`, `CreateWorkspaceUseCase` | Smoke nuevo proyecto. |
| CON-02 Importar Markdown legacy/oficial | Terminado funcional | 90% | `MarkdownDiagramParser`, `DiagramMarkdownImportDispatcher` | Probar ejemplos grandes. |
| CON-03 Agregar entidad | Terminado funcional | 92% | `AddEntityUseCase`, toolbar conceptual | Revisar estado sucio. |
| CON-04 Agregar atributo | Terminado funcional | 90% | `AddAttributeUseCase` | Probar tags/PK/FK si aplica. |
| CON-05 Agregar relación | Terminado funcional | 90% | `AddRelationshipUseCase` | Probar cardinalidades y participación. |
| CON-06 Editar nombre/descripción/cardinalidad | Terminado funcional | 88% | `RenameElementUseCase`, `Update*UseCase`, inspector | Smoke de panel derecho. |
| CON-07 Mover nodos | Terminado funcional | 90% | `MoveElementUseCase`, `UpdateNodeLayoutUseCase` | Confirmar persistencia al reabrir. |
| CON-08 Editar puntos intermedios | Terminado funcional reciente | 80% | `Add/Move/RemoveBendPointUseCase`, tests de selección | Smoke con selección del punto + toolbar/Suprimir. |
| CON-09 Cambiar Chen/pata de gallo | Terminado funcional | 88% | `SwitchNotationUseCase`, renderers Chen/CrowsFoot | Confirmar exportación en ambas notaciones. |
| CON-10 Exportar SVG | Terminado funcional | 85% | `SvgDiagramExporter`, tests SVG | Revisar salida visual compleja. |
| CON-11 Exportar PNG | Terminado funcional | 85% | `CanvasPngExporter`, `exportVisibleCanvasAsPng` | Revisar viewport/fit. |
| CON-12 Exportar Markdown actualizado | Terminado funcional | 82% | `MarkdownDiagramExporter` | Diff después de edición. |
| CON-13 Guardar/abrir `.dms` | Terminado funcional | 85% | `DmsProjectJsonReader/Writer`, tests de persistencia | Smoke manual. |
| CON-14 Mantener clases pequeñas/SRP | Parcial | 45% | `DiagramCanvasView`, `DiagramCanvasViewModel` muy grandes | Extraer controladores de canvas. |

## Casos faltantes para modelo conceptual

| ID | Caso | Prioridad | Motivo |
|---|---|---:|---|
| CON-FALT-01 | Refactor de canvas en controladores de interacción | Alta | Evita que el canvas se vuelva monolítico. |
| CON-FALT-02 | Contrato común reutilizable para otros diagramas | Alta | Es la base de AV-I01/AV-I02. |
| CON-FALT-03 | Smoke de exportación con diagrama grande | Media | Asegura que SVG/PNG no se corten. |
| CON-FALT-04 | Prueba manual de cambio de notación + guardado + reapertura | Alta | Punto sensible del producto. |

---

# UML Clases

Promesa correcta: **representar clases, atributos, métodos y relaciones estructurales agrupadas por módulo/carpeta/paquete**.

| Caso | Estado | % | Anclaje observado | Falta para cierre |
|---|---|---:|---|---|
| UMLC-01 Crear diagrama | Terminado funcional | 82% | `CreateUmlClassDiagramUseCase`, `UmlClassDiagramEditorView` | Smoke desde `Nuevo`. |
| UMLC-02 Importar Markdown | Terminado funcional | 80% | `UmlClassMarkdownParser` | Probar UENS y restaurante mínimo. |
| UMLC-03 Agregar módulo/grupo | Terminado funcional | 70% | `UmlModuleGroup`, toolbar `Módulo` | Mejorar agrupación visual real. |
| UMLC-04 Agregar clase/interfaz/enum | Terminado funcional | 78% | `AddUmlClassNodeUseCase`, `UmlClassKind` | Verificar figura diferenciada. |
| UMLC-05 Agregar atributo/método | Terminado funcional | 75% | `UmlClassMember`, use cases de edición | Smoke de edición en panel. |
| UMLC-06 Agregar relación UML | Terminado funcional | 72% | `UmlClassRelation`, `UmlRelationKind` | Visual debe diferenciar asociación/herencia/etc. |
| UMLC-07 Editar/eliminar | Terminado funcional | 70% | `Update/RemoveUmlClass*UseCase` | Revisar persistencia. |
| UMLC-08 Validar/exportar | Terminado básico | 72% | `ValidateUmlClassDiagramUseCase`, Markdown/PNG | Smoke tab activa. |
| UMLC-09 Layout agrupado por módulo | Parcial fuerte | 35% | Plan AV-I05 | Necesario para cumplir promesa de módulos/carpeta/paquete. |
| UMLC-10 Interacción visual persistente | Parcial | 40% | Vista especializada actual | Migrar a canvas común/adaptador. |

## Casos faltantes para UML clases

| ID | Caso | Prioridad | Motivo |
|---|---|---:|---|
| UMLC-FALT-01 | Contenedores/zones por módulo | Alta | Decisión de producto ya definida: clases de una carpeta/módulo agrupadas. |
| UMLC-FALT-02 | Diferenciar visualmente relaciones UML | Alta | Si todas las líneas se ven igual, la promesa UML queda débil. |
| UMLC-FALT-03 | Arrastrar clases dentro de grupos y persistir | Alta | Necesario para diagramas grandes. |
| UMLC-FALT-04 | Colapsar/expandir miembros | Media | Ayuda a manejar clases grandes sin saturar. |
