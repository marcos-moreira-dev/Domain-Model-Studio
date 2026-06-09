---
dms_version: "1"
diagram_type: "logical-business-graph"
name: "Grafo lógico mínimo — venta administrativa"
sample_kind: "minimal-example"
domain: "ventas administrativas"
status: "importable"
contract: "logical-business-graph-v1"
importable: true
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
| MF-001 | MF | Gestión de ventas | Agrupa venta, cobro, comprobante y actualización de stock. | borrador | entrevista inicial |
| FL-001 | FL | Registrar venta al contado | Microflujo para seleccionar productos, validar stock, cobrar y cerrar comprobante. | borrador | MF-001 |
| CU-001 | CU | Crear venta | Caso de uso para registrar cliente opcional, productos, cantidades y total. | borrador | FL-001 |
| ACC-001 | ACC | Guardar venta | Acción que persiste la venta y descuenta stock disponible. | borrador | CU-001 |
| RN-001 | RN | No vender sin stock suficiente | La venta no debe confirmarse si algún producto no tiene stock suficiente. | borrador | cliente |
| PRE-001 | PRE | Productos seleccionados | Antes de guardar la venta debe existir al menos un producto con cantidad válida. | borrador | CU-001 |
| INV-001 | INV | Stock no negativo | Ningún producto debe quedar con stock menor que cero. | borrador | RN-001 |
| POST-001 | POST | Venta confirmada con comprobante | Al cerrar la venta debe existir comprobante y stock actualizado. | borrador | ACC-001 |
| ENT-001 | ENT | Venta | Entidad candidata que registra cabecera, total, estado y fecha. | borrador | modelo futuro |
| ENT-002 | ENT | Producto | Entidad candidata que registra nombre, precio y stock disponible. | borrador | inventario |
| PEND-001 | PEND | Confirmar política de anulación | Falta definir si una venta anulada repone stock automáticamente. | bloqueado | cliente |

# Relaciones

| ID | Origen | Relación | Destino | Descripción |
|---|---|---|---|---|
| rel-001 | MF-001 | contiene | FL-001 | El macroflujo de ventas contiene el microflujo de venta al contado. |
| rel-002 | FL-001 | usa | CU-001 | El microflujo usa el caso de uso de crear venta. |
| rel-003 | CU-001 | ejecuta | ACC-001 | Crear venta ejecuta la acción de guardado. |
| rel-004 | RN-001 | aplica | ACC-001 | La regla de stock aplica al guardar la venta. |
| rel-005 | ACC-001 | requiere | PRE-001 | Guardar venta requiere productos seleccionados. |
| rel-006 | ACC-001 | protege | INV-001 | Guardar venta protege el stock no negativo. |
| rel-007 | ACC-001 | garantiza | POST-001 | Guardar venta garantiza venta confirmada y comprobante. |
| rel-008 | ACC-001 | crea | ENT-001 | Guardar venta crea la entidad venta. |
| rel-009 | ACC-001 | modifica | ENT-002 | Guardar venta modifica el stock del producto. |
| rel-010 | PEND-001 | bloquea | RN-001 | La política de anulación puede afectar la regla de stock. |

# Observaciones

Ejemplo mínimo para validar la importación del Grafo lógico del negocio y su leyenda de abreviaciones.
