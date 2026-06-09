# Prompt IA — Generar Grafo lógico del negocio importable

> Este archivo no se importa en Domain Model Studio. Cópialo en una IA junto con el levantamiento lógico del cliente para obtener un Markdown `logical-business-graph` importable.

## Prompt maestro

Actúa como analista de sistemas administrativos y convierte el levantamiento lógico del negocio en un Markdown importable para Domain Model Studio con `diagram_type: "logical-business-graph"`.

Objetivo: generar un **Grafo lógico del negocio** que muestre macroflujos, flujos o microflujos, casos de uso, acciones transformadoras, reglas, precondiciones, invariantes, postcondiciones, entidades candidatas, estados, reportes, riesgos y preguntas pendientes.

Reglas obligatorias:

1. Usa front matter válido con `diagram_type: "logical-business-graph"`, `contract: "logical-business-graph-v1"` e `importable: "true"`.
2. Incluye una sección `# Leyenda` visible antes de `# Nodos`.
3. No uses abreviaciones sin explicar su significado en la leyenda.
4. Usa códigos estables: `MF-001`, `FL-001`, `CU-001`, `ACC-001`, `RN-001`, `PRE-001`, `INV-001`, `POST-001`, `ENT-001`, `EST-001`, `REP-001`, `RISK-001`, `PEND-001`.
5. Cada nodo debe tener código, tipo, título, descripción, estado y referencias.
6. Cada relación debe tener ID, origen, relación, destino y descripción.
7. No inventes reglas definitivas si el levantamiento no las confirma; usa nodos `PEND` para dudas.
8. Reutiliza casos de uso repetidos en varios flujos mediante relaciones `usa` o `reutiliza`; no dupliques el mismo caso.
9. Mantén las relaciones semánticas: `MF contiene FL`, `FL usa CU`, `CU ejecuta ACC`, `ACC requiere PRE`, `ACC protege INV`, `ACC garantiza POST`, `RN aplica CU/ACC/ENT`, `ACC crea/modifica/consulta ENT`, `PEND bloquea RN/CU/FL/ENT`.
10. El resultado debe ser solo Markdown, sin explicación fuera del documento.

## Estructura obligatoria de salida

```md
---
dms_version: "1"
diagram_type: "logical-business-graph"
name: "<nombre del grafo>"
version: "v0.1"
sample_kind: "project"
domain: "<dominio>"
status: "borrador"
contract: "logical-business-graph-v1"
importable: "true"
intended_output: "grafo lógico editable"
---
# Leyenda

| Abreviación | Significado | Uso dentro del grafo |
|---|---|---|
| MF | Macroflujo | Gran operación del negocio. |
| FL | Flujo o microflujo | Variante operativa concreta dentro de un macroflujo. |
| CU | Caso de uso | Funcionalidad observable y reutilizable del sistema. |
| ACC | Acción transformadora | Acción que cambia datos, estados o evidencias. |
| RN | Regla de negocio | Restricción, política o criterio lógico del dominio. |
| PRE | Precondición | Verdad necesaria antes de ejecutar un flujo, caso o acción. |
| INV | Invariante | Verdad que debe mantenerse durante la operación. |
| POST | Postcondición | Verdad verificable al cerrar una acción, caso o flujo. |
| ENT | Entidad candidata | Concepto persistible identificado desde el levantamiento lógico. |
| EST | Estado | Situación válida del negocio o de una entidad. |
| REP | Reporte | Salida informativa construida desde datos y reglas. |
| RISK | Riesgo | Riesgo lógico u operativo detectado. |
| PEND | Pregunta pendiente | Duda que bloquea validación o diseño definitivo. |

# Nodos

| Código | Tipo | Título | Descripción | Estado | Referencias |
|---|---|---|---|---|---|

# Relaciones

| ID | Origen | Relación | Destino | Descripción |
|---|---|---|---|---|

# Observaciones

<Notas de interpretación, dudas o límites de alcance.>
```

## Checklist antes de entregar

- La leyenda explica todas las abreviaciones usadas.
- No hay códigos duplicados.
- Todo `Origen` y `Destino` existe en `# Nodos`.
- Cada macroflujo tiene al menos un flujo o microflujo.
- Cada flujo relevante usa al menos un caso de uso.
- Cada caso de uso importante ejecuta al menos una acción o queda justificado.
- Las reglas dudosas aparecen como `PEND`, no como `RN` validada.
- El Markdown no incluye comentarios fuera de las secciones esperadas.
