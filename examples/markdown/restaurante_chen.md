---
id: restaurante_conceptual_v1
title: Modelo conceptual - Restaurante
notation: chen
version: 0.1.0
status: draft
---

# Entidades

## ProductoMenu
id: producto_menu
module: menu
description: Producto ofrecido en el menú del restaurante.

- pk id
- nombre
- precio_actual
- estado

## Pedido
id: pedido
module: pedidos
description: Orden registrada por un cliente o mesa.

- pk id
- fecha_hora
- estado
- total

## DetallePedido
id: detalle_pedido
module: pedidos
description: Línea de producto dentro de un pedido.

- pk id
- cantidad
- precio_unitario
- subtotal
- observacion [optional]

## Mesa
id: mesa
module: salon
description: Mesa física o lógica del restaurante.

- pk id
- codigo
- capacidad
- estado

## Cliente
id: cliente
module: clientes
description: Cliente asociado opcionalmente a un pedido.

- pk id
- nombre
- telefono [optional]
- estado

# Relaciones

## Ocupa
id: ocupa
from: Mesa
to: Pedido
from_cardinality: 0..1
to_cardinality: 0..M
description: Una mesa puede tener pedidos asociados durante la operación.

## Solicita
id: solicita
from: Cliente
to: Pedido
from_cardinality: 0..1
to_cardinality: 0..M
description: Un pedido puede estar asociado a cliente registrado o consumidor no identificado.

## Contiene
id: contiene
from: Pedido
to: DetallePedido
from_cardinality: 1
to_cardinality: 1..M
description: Un pedido contiene uno o más detalles.

## DetallaProducto
id: detalla_producto
from: DetallePedido
to: ProductoMenu
from_cardinality: 0..M
to_cardinality: 1
description: Cada detalle corresponde a un producto del menú.
