# Tanda 28 — Refactor de `ApplicationServices` por familias

## Objetivo

Reducir el rol monolítico de `ApplicationServices` sin cambiar comportamiento visible ni romper consumidores existentes.

La tanda no modifica UX, parser/exporter Markdown, persistencia `.dms`, SideDock, toolbar ni recursos IA. Solo reordena la composición de casos de uso de aplicación.

## Decisión aplicada

`ApplicationServices` queda como fachada de compatibilidad. ApplicationServices queda como fachada de compatibilidad para consumidores existentes. Los accesos legacy siguen existiendo, pero delegan en fachadas funcionales. El código nuevo debe preferir las familias específicas.

Familias vigentes:

- `ProjectApplicationServices`
- `ImportApplicationServices`
- `ExportApplicationServices`
- `CatalogApplicationServices`
- `VisualApplicationServices`
- `DocumentationApplicationServices`
- `ConceptualModelApplicationServices`
- `DataDictionaryApplicationServices`
- `ModuleMapApplicationServices`
- `UmlClassApplicationServices`
- `RolesPermissionsApplicationServices`
- `ScreenFlowApplicationServices`
- `WireframeApplicationServices`
- `BehaviorApplicationServices`
- `ArchitectureApplicationServices`

## Fronteras importantes

- La familia conceptual agrupa edición semántica, no layout ni persistencia.
- La familia visual agrupa layout, bendpoints, estilo y notación.
- La familia de diccionario no absorbe exportaciones PDF/Markdown; esas siguen en exportación transversal.
- UML Clases conserva la edición manual separada de la importación desde código fuente.
- Las familias de aplicación no importan JavaFX, `presentation` ni adaptadores concretos de infraestructura.

## Comportamiento preservado

- Los getters históricos de `ApplicationServices` siguen devolviendo los mismos tipos.
- `ApplicationServicesFactory` construye la fachada mediante familias explícitas, no mediante un constructor gigante de casos de uso individuales.
- Shell, workbench, SideDock, toolbar y exportaciones no cambian.
- No se modifica el contrato de ejemplos oficiales ni los recursos IA.

## Tests agregados/actualizados

- `ApplicationServicesFamilyFacadeSourceTest`
- `ApplicationServicesTanda28RefactorSourceTest`

Los tests validan que las familias existan, que se mantengan en application puro, que la fachada global ya no almacene casos de uso individuales y que los accesos legacy deleguen a familias.

## Próxima tanda sugerida

Tanda 29 — Refactor de shell/comandos por coordinadores especializados.
