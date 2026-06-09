# Tanda 030 — Refactor de catálogos, capacidades y recursos oficiales

Estado: aplicada.

## Objetivo

Reducir archivos centrales de catálogo y recursos IA sin cambiar el contrato visible del producto.

La tanda no cambia tipos disponibles, ejemplos oficiales, recursos IA, importabilidad, workspaces ni capacidades. Solo separa responsabilidades para que futuras correcciones no pasen por archivos monolíticos.

## Cambios aplicados

### Catálogo de tipos

`DefaultDiagramTypeDefinitions` queda como agregador pequeño y delega a familias:

- `BusinessAnalysisDiagramTypeDefinitions`
- `DataModelDiagramTypeDefinitions`
- `BusinessProcessDiagramTypeDefinitions`
- `ArchitectureDiagramTypeDefinitions`
- `UmlDiagramTypeDefinitions`
- `AdministrativeDiagramTypeDefinitions`
- `TechnicalDocumentationDiagramTypeDefinitions`

Los perfiles de capacidades se concentran en `DiagramCapabilityProfiles`.

### Recursos IA

`OfficialAiResourceDescriptors` queda como agregador pequeño y delega a:

- `CoreAiResourceDescriptors`
- `OfficialTemplateAiResourceDescriptors`
- `OfficialMinimalExampleAiResourceDescriptors`
- `OfficialUensExampleAiResourceDescriptors`
- `LogicalBusinessGraphAiResourceDescriptors`

## Contratos preservados

- 19 tipos oficiales.
- 19 ejemplos oficiales importables.
- Un solo Levantamiento lógico oficial automático: UENS.
- Óptica sigue únicamente como recurso IA/fixture histórico del Levantamiento lógico.
- Grafo lógico mantiene ejemplo UENS y recursos IA propios.
- UML Clases conserva capacidades de código fuente.
- Levantamiento lógico conserva salida documental Markdown sin PNG/SVG/PDF.

## Tests

Se agregaron/actualizaron guardarraíles fuente:

- `CatalogDefinitionsByFamilySourceTest`
- `OfficialAiResourceDescriptorFamiliesSourceTest`
- Rebaseline de tests que antes leían tokens directamente en los agregadores monolíticos.

## Validación local esperada

```bat
scripts\02-ejecutar-tests.bat
```
