---
dms_version: "1"
diagram_type: "bpmn-basic"
name: "BPMN básico — venta mínima"
sample_kind: "minimal"
domain: "ventas"
status: "importable"
importable: true
intended_output: "diagrama visual"
---
# Proceso

Nombre: Venta en local
Participantes: Cliente, Vendedor, Sistema

# Flujo

- inicio: Cliente solicita producto.
- actividad: Vendedor consulta disponibilidad.
- decisión: ¿Producto disponible?
- actividad: Registrar venta.
- actividad: Cobrar venta.
- actividad: Emitir comprobante.
- fin: Venta cerrada.

# Excepciones

- Si no hay disponibilidad, ofrecer alternativa o registrar pedido pendiente.
