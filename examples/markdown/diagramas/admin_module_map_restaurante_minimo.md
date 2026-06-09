---
dms_version: "1"
diagram_type: "admin-module-map"
name: "Mapa de módulos — restaurante mínimo"
sample_kind: "minimal"
domain: "restaurante"
status: "importable"
importable: true
intended_output: "diagrama visual"
---
# Módulos

## Ventas
id: ventas
responsabilidad: registrar pedidos, cuentas y comprobantes.

### Submódulos
- pedidos: toma y seguimiento de pedidos.
- caja: cobro y cierre diario.
- comprobantes: generación de documentos de venta.

## Inventario
id: inventario
responsabilidad: controlar insumos y existencias.

### Submódulos
- productos: catálogo de insumos y platos.
- movimientos: entradas, salidas y ajustes.

## Reportes
id: reportes
responsabilidad: entregar información para decisiones.

# Dependencias

- ventas -> inventario: descuenta insumos vendidos.
- ventas -> reportes: alimenta reportes diarios.
- inventario -> reportes: alimenta existencias y alertas.
