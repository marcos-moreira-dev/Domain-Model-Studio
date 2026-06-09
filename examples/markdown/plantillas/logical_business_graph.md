---
dms_version: "1"
diagram_type: "logical-business-graph"
name: "<nombre del grafo lógico>"
sample_kind: "template"
domain: "<dominio del negocio>"
status: "template"
contract: "logical-business-graph-v1"
importable: true
intended_output: "diagrama visual"
---

# Instrucciones para completar con IA

1. Usa esta plantilla después de completar el levantamiento lógico del negocio.
2. Conserva siempre la leyenda visible de abreviaciones.
3. No conviertas preguntas pendientes en reglas validadas. Usa nodos `PEND` cuando falte información.
4. Define cada caso de uso una sola vez y conéctalo desde uno o varios microflujos mediante `usa` o `reutiliza`.
5. Prefiere relaciones semánticas precisas: `requiere`, `protege`, `garantiza`, `crea`, `modifica`, `consulta`, `bloquea`, `depende_de`.

# Leyenda

| Abreviación | Significado | Uso dentro del grafo |
|---|---|---|
| MF | Macroflujo | Agrupa una gran operación del negocio. |
| FL | Flujo o microflujo | Describe una variante operativa concreta dentro de un macroflujo. |
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
| MF-001 | MF | <macroflujo principal> | <gran operación del negocio> | borrador | |
| FL-001 | FL | <microflujo principal> | <flujo operativo dentro del macroflujo> | borrador | MF-001 |
| CU-001 | CU | <caso de uso principal> | <funcionalidad observable del sistema> | borrador | FL-001 |
| ACC-001 | ACC | <acción transformadora> | <acción que cambia el estado del negocio> | borrador | CU-001 |
| RN-001 | RN | <regla de negocio> | <restricción o política aplicable> | borrador | |
| PRE-001 | PRE | <precondición> | <condición inicial necesaria> | borrador | |
| INV-001 | INV | <invariante> | <verdad que debe mantenerse> | borrador | |
| POST-001 | POST | <postcondición> | <estado de cierre verificable> | borrador | |
| ENT-001 | ENT | <entidad candidata> | <concepto persistible identificado> | borrador | |
| PEND-001 | PEND | <pregunta pendiente> | <duda que debe resolverse> | borrador | |

# Relaciones

| ID | Origen | Relación | Destino | Descripción |
|---|---|---|---|---|
| rel-001 | MF-001 | contiene | FL-001 | El macroflujo contiene el microflujo. |
| rel-002 | FL-001 | usa | CU-001 | El microflujo usa el caso de uso. |
| rel-003 | CU-001 | ejecuta | ACC-001 | El caso de uso ejecuta la acción. |
| rel-004 | RN-001 | aplica | CU-001 | La regla aplica al caso de uso. |
| rel-005 | ACC-001 | requiere | PRE-001 | La acción requiere la precondición. |
| rel-006 | ACC-001 | protege | INV-001 | La acción protege la invariante. |
| rel-007 | ACC-001 | garantiza | POST-001 | La acción garantiza la postcondición. |
| rel-008 | ACC-001 | crea | ENT-001 | La acción crea o actualiza la entidad candidata. |
| rel-009 | PEND-001 | bloquea | RN-001 | La pregunta pendiente bloquea validar la regla. |

# Observaciones

Completar este grafo desde el levantamiento lógico del negocio. Mantener la leyenda visible cuando se usen abreviaciones.
