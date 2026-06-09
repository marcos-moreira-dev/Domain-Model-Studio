---
dms_version: "1"
diagram_type: "uml-class"
name: "UML Clases — restaurante mínimo"
sample_kind: "minimal"
domain: "restaurante"
status: "importable"
importable: true
intended_output: "diagrama visual"
---
# Paquetes

## dominio.ventas
propósito: reglas de pedidos y ventas.

## dominio.inventario
propósito: reglas de productos e insumos.

# Clases

## Pedido
paquete: dominio.ventas
responsabilidad: agrupa ítems solicitados por el cliente.
atributos:
- id: String
- estado: EstadoPedido
- total: Decimal
métodos:
- agregarItem(producto, cantidad): void
- cerrar(): void

## ItemPedido
paquete: dominio.ventas
responsabilidad: representa un producto pedido.
atributos:
- cantidad: int
- precioUnitario: Decimal

## Producto
paquete: dominio.inventario
responsabilidad: describe producto vendible o insumo.
atributos:
- id: String
- nombre: String
- precio: Decimal

# Relaciones

- Pedido *-- ItemPedido: contiene
- ItemPedido --> Producto: referencia
