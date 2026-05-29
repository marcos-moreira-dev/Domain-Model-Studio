---
id: supermercado_conceptual_v1
title: Modelo conceptual - Supermercado
notation: chen
version: 0.1.0
status: draft
---

# Entidades

## Producto
id: producto
module: inventario
description: Producto vendido o administrado por el supermercado.

- pk id
- nombre
- codigo_barras [unique]
- precio_actual
- stock_actual [derived]
- estado

## Categoria
id: categoria
module: inventario
description: Clasificación de productos.

- pk id
- nombre
- descripcion [optional]
- estado

## Cliente
id: cliente
module: ventas
description: Persona o entidad que realiza compras.

- pk id
- nombres
- apellidos [optional]
- identificacion [optional]
- telefono [optional]
- estado

## Venta
id: venta
module: ventas
description: Transacción de venta registrada en caja.

- pk id
- fecha_hora
- total
- estado

## DetalleVenta
id: detalle_venta
module: ventas
description: Línea de producto dentro de una venta.

- pk id
- cantidad
- precio_unitario
- subtotal

## Caja
id: caja
module: caja
description: Punto o sesión de caja asociada a ventas y movimientos.

- pk id
- codigo
- estado

# Relaciones

## Pertenece
id: pertenece
from: Producto
to: Categoria
from_cardinality: 0..M
to_cardinality: 1
description: Cada producto pertenece a una categoría; una categoría puede agrupar muchos productos.

## Realiza
id: realiza
from: Cliente
to: Venta
from_cardinality: 0..1
to_cardinality: 0..M
description: Una venta puede estar asociada a un cliente registrado o ser consumidor final.

## Contiene
id: contiene
from: Venta
to: DetalleVenta
from_cardinality: 1
to_cardinality: 1..M
description: Una venta contiene uno o más detalles.

## DetallaProducto
id: detalla_producto
from: DetalleVenta
to: Producto
from_cardinality: 0..M
to_cardinality: 1
description: Cada detalle de venta corresponde a un producto.

## Registra
id: registra
from: Caja
to: Venta
from_cardinality: 1
to_cardinality: 0..M
description: Una caja registra muchas ventas.
