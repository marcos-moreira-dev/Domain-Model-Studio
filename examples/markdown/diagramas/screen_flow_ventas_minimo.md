---
dms_version: "1"
diagram_type: "screen-flow"
name: "Flujo de pantallas — ventas mínimo"
sample_kind: "minimal"
domain: "ventas"
status: "importable"
importable: true
intended_output: "diagrama visual"
---
# Pantallas

## Inicio
id: inicio
propósito: acceso a módulos principales.

## Lista de ventas
id: lista_ventas
propósito: buscar y revisar ventas registradas.

## Nueva venta
id: nueva_venta
propósito: registrar cliente, productos y cobro.

## Detalle de venta
id: detalle_venta
propósito: revisar comprobante y estado.

# Navegación

- inicio -> lista_ventas: abrir módulo ventas.
- lista_ventas -> nueva_venta: crear venta.
- lista_ventas -> detalle_venta: abrir venta existente.
- nueva_venta -> detalle_venta: guardar venta.
- detalle_venta -> lista_ventas: volver al listado.
