# Auditoría JavaDoc JD-6 — Ejemplos pedagógicos

## Estado

Tanda: **JD-6 aplicada**  
Tipo: documentación de código + onboarding  
Lógica funcional: **sin cambios**

## Alcance

Se agregaron ejemplos JavaDoc pedagógicos en puntos de alto valor de estudio:

```txt
LogicalBusinessGraphValidationPolicy
LogicalBusinessGraphDocument
LogicalBusinessGraphRelationKind
LogicalBusinessGraphMarkdownParser
LogicalBusinessGraphMarkdownExporter
DmsProjectJsonReader
DmsProjectJsonWriter
InteractiveCanvasAdapter
CanvasBendPointController
WorkbenchSideDock
```

## Criterio usado

```txt
Ejemplo pedagógico = comentario que muestra un caso pequeño y explica una frontera.
No cuenta como ejemplo pedagógico un comentario que solo repite nombres de clases o métodos.
```

## Resultado esperado

Después de generar el sitio JavaDoc, un estudiante puede buscar las clases anteriores y encontrar ejemplos que expliquen:

```txt
- dominio inmutable vs layout externo;
- relaciones semánticas válidas o inválidas;
- Markdown como contrato importable;
- .dms como formato durable;
- canvas común mediante adaptadores;
- SideDock como carcasa transversal.
```

## Riesgo controlado

No se activó umbral global de JavaDoc para métodos públicos. La prioridad fue agregar ejemplos en contratos complejos, no rellenar miembros triviales.
