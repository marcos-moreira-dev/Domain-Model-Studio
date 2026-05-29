# Gramática Markdown — Grafo lógico del negocio

> Estado: importable. Esta gramática documenta una vista visual compatible del levantamiento lógico del negocio.

## Propósito

El Grafo lógico del negocio sirve para navegar la cadena:

```text
Macroflujo → Flujo o microflujo → Caso de uso → Acción transformadora → reglas, precondiciones, invariantes, postcondiciones y entidades
```

No reemplaza al levantamiento lógico. El levantamiento lógico es la fuente lógica canónica textual; el grafo es una vista visual compatible, editable y trazable de esa lógica.

## Front matter obligatorio

```yaml
dms_version: "1"
diagram_type: "logical-business-graph"
name: "Nombre del grafo lógico"
version: "v0.1"
document_date: "2026-01-31"
sample_kind: "template | minimal-example | full-example | project"
domain: "dominio del negocio"
status: "borrador | importable | validado parcialmente | validado"
contract: "logical-business-graph-v1"
importable: true
intended_output: "grafo lógico editable"
```

## Leyenda obligatoria de abreviaciones

Si el documento usa abreviaciones, debe incluir una sección `# Leyenda` o `# Leyenda de abreviaciones` antes de los nodos.

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

## Sección `# Nodos`

La sección de nodos es obligatoria. Cada código debe coincidir con su tipo: `MF-001`, `FL-001`, `CU-001`, etc.

```md
| Código | Tipo | Título | Descripción | Estado | Referencias |
|---|---|---|---|---|---|
| MF-001 | MF | Gestión principal | Agrupa operaciones del negocio. | borrador | entrevista |
| FL-001 | FL | Flujo principal | Variante concreta del macroflujo. | borrador | MF-001 |
| CU-001 | CU | Caso de uso principal | Funcionalidad observable del sistema. | borrador | FL-001 |
```

Estados permitidos:

```text
borrador
en revisión
validado parcialmente
validado
bloqueado
descartado
```

## Sección `# Relaciones`

La sección de relaciones es obligatoria. Las relaciones deben conectar códigos existentes.

```md
| ID | Origen | Relación | Destino | Descripción |
|---|---|---|---|---|
| rel-001 | MF-001 | contiene | FL-001 | El macroflujo contiene el microflujo. |
| rel-002 | FL-001 | usa | CU-001 | El microflujo usa el caso de uso. |
| rel-003 | CU-001 | ejecuta | ACC-001 | El caso de uso ejecuta una acción. |
```

Relaciones soportadas:

```text
contiene
usa
reutiliza
ejecuta
aplica
requiere
protege
garantiza
crea
modifica
consulta
genera
alimenta
bloquea
habilita
depende_de
deriva_en
```

## Reglas semánticas recomendadas

| Origen | Relación | Destino esperado | Lectura |
|---|---|---|---|
| MF | contiene | FL | El macroflujo agrupa el microflujo. |
| FL | usa | CU | El microflujo usa el caso de uso. |
| FL | reutiliza | CU | El microflujo reutiliza un caso definido una sola vez. |
| CU | ejecuta | ACC | El caso de uso ejecuta una acción transformadora. |
| RN | aplica | CU / ACC / ENT | La regla aplica sobre un caso, acción o entidad. |
| ACC / CU | requiere | PRE | La acción o caso necesita una precondición. |
| ACC / CU | protege | INV | La acción o caso protege una invariante. |
| ACC / CU | garantiza | POST | La acción o caso garantiza un cierre verificable. |
| ACC / CU | crea / modifica / consulta | ENT | La acción o caso afecta una entidad candidata. |
| REP | depende_de | ENT | El reporte depende de datos de una entidad. |
| PEND | bloquea | RN / CU / FL / ENT | La pregunta pendiente bloquea validación o diseño. |

El importador puede aceptar relaciones no ideales, pero el validador debe advertir cuando una conexión no sea semánticamente esperada.

## Flujo recomendado con IA

1. Completar primero el levantamiento lógico del negocio.
2. Pedir a la IA que identifique nodos `MF`, `FL`, `CU`, `ACC`, `RN`, `PRE`, `INV`, `POST`, `ENT`, `EST`, `REP`, `RISK` y `PEND` solo cuando exista información suficiente.
3. Mantener una leyenda visible de abreviaciones.
4. Evitar inventar reglas dudosas: usar `PEND` cuando falte validación del cliente.
5. Importar el Markdown y revisar advertencias del grafo dentro de Domain Model Studio.

## Errores comunes

- Usar abreviaciones sin leyenda visible.
- Convertir el grafo en BPMN: el grafo lógico muestra trazabilidad, no secuencia ejecutable detallada.
- Duplicar un caso de uso repetido en varios microflujos; debe definirse una vez y conectarse con `usa` o `reutiliza`.
- Conectar todo con `contiene`; las reglas, acciones, invariantes y entidades requieren relaciones semánticas más precisas.
- Asumir preguntas pendientes como reglas definitivas.

## Ejemplo mínimo

```md
# Nodos

| Código | Tipo | Título | Descripción | Estado | Referencias |
|---|---|---|---|---|---|
| MF-001 | MF | Gestión de ventas | Agrupa cotización, venta y cobro. | borrador | entrevista |
| FL-001 | FL | Registrar venta | Microflujo para confirmar productos y generar comprobante. | borrador | MF-001 |
| CU-001 | CU | Crear venta | Caso de uso para registrar cabecera y detalle de venta. | borrador | FL-001 |
| ACC-001 | ACC | Guardar venta | Acción que persiste la venta y actualiza stock. | borrador | CU-001 |
| INV-001 | INV | Stock no negativo | Ningún producto puede quedar con stock negativo. | borrador | RN-001 |

# Relaciones

| ID | Origen | Relación | Destino | Descripción |
|---|---|---|---|---|
| rel-001 | MF-001 | contiene | FL-001 | Ventas contiene el flujo de registrar venta. |
| rel-002 | FL-001 | usa | CU-001 | El flujo usa el caso de uso de venta. |
| rel-003 | CU-001 | ejecuta | ACC-001 | Crear venta ejecuta guardar venta. |
| rel-004 | ACC-001 | protege | INV-001 | Guardar venta protege stock no negativo. |
```
