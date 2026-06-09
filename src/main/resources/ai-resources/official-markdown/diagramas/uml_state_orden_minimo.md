---
dms_version: "1"
diagram_type: "uml-state"
name: "UML Estados — orden mínimo"
sample_kind: "minimal"
domain: "ventas"
status: "importable"
importable: true
intended_output: "diagrama visual"
---
# Elemento observado

Nombre: Orden de venta

# Estados

- borrador
- pendiente_pago
- pagada
- anulada
- entregada

# Transiciones

- borrador -> pendiente_pago: confirmar
- pendiente_pago -> pagada: registrar pago
- pendiente_pago -> anulada: cancelar
- pagada -> entregada: entregar producto
