---
id: restaurante_chen_detallado_v1
title: Modelo conceptual - Restaurante administrativo
notation: chen
version: 1.0.0
status: draft
author: @programalobien
source: referencia_cedro_damasco
domain: restaurante
---

# Entidades

## ProductoVenta
id: producto_venta
module: ventas
description: Ítem vendible en carta o sistema de pedidos.
status: draft
confidence: high

- pk id
- nombre
- descripcion [optional]
- precio_actual
- estado

## CategoriaCarta
id: categoria_carta
module: ventas
description: Agrupación de productos de venta en la carta.
status: draft
confidence: high

- pk id
- nombre
- estado

## Pedido
id: pedido
module: pedidos
description: Solicitud de productos realizada por mesa, cliente o canal externo.
status: draft
confidence: high

- pk id
- fecha_hora
- tipo_pedido
- estado
- total

## DetallePedido
id: detalle_pedido
module: pedidos
description: Línea de pedido con producto, cantidad y precio aplicado.
status: draft
confidence: high

- pk id
- cantidad
- precio_unitario_aplicado
- observacion [optional]
- subtotal_linea

## Mesa
id: mesa
module: salon
description: Mesa física o espacio de atención dentro del restaurante.
status: draft
confidence: medium

- pk id
- codigo
- capacidad
- estado

## Cliente
id: cliente
module: clientes
description: Cliente registrado o asociado a pedidos específicos.
status: draft
confidence: medium

- pk id
- nombres [optional]
- telefono [optional]
- correo_electronico [optional]
- estado

## Caja
id: caja
module: caja
description: Punto de cobro o sesión de caja.
status: draft
confidence: medium

- pk id
- nombre
- estado

## Pago
id: pago
module: caja
description: Pago asociado a un pedido.
status: draft
confidence: medium

- pk id
- metodo_pago
- monto
- fecha_hora
- referencia [optional]

## Insumo
id: insumo
module: inventario
description: Material o ingrediente usado para preparar productos.
status: draft
confidence: medium

- pk id
- nombre
- unidad_medida
- stock_actual [derived]
- estado

## Receta
id: receta
module: cocina
description: Definición de insumos requeridos para preparar un producto de venta.
status: draft
confidence: medium

- pk id
- nombre
- version
- estado

## DetalleReceta
id: detalle_receta
module: cocina
description: Insumo y cantidad requerida dentro de una receta.
status: draft
confidence: medium

- pk id
- cantidad_requerida

## UsuarioSistema
id: usuario_sistema
module: seguridad
description: Usuario que opera el sistema de restaurante.
status: draft
confidence: high

- pk id
- nombre_login [unique]
- credencial [sensitive]
- rol
- estado

# Relaciones

## Clasifica
id: clasifica
from: ProductoVenta
to: CategoriaCarta
from_cardinality: 0..M
to_cardinality: 1
description: Una categoría agrupa productos de venta; cada producto pertenece a una categoría principal.
status: draft
confidence: high

## ContieneDetalle
id: contiene_detalle
from: Pedido
to: DetallePedido
from_cardinality: 1
to_cardinality: 1..M
description: Un pedido contiene uno o más detalles; cada detalle pertenece a un pedido.
status: draft
confidence: high

## DetallaProducto
id: detalla_producto
from: ProductoVenta
to: DetallePedido
from_cardinality: 1
to_cardinality: 0..M
description: Un producto puede aparecer en muchos detalles de pedido; cada detalle referencia un producto.
status: draft
confidence: high

## OcupaMesa
id: ocupa_mesa
from: Mesa
to: Pedido
from_cardinality: 0..1
to_cardinality: 0..M
description: Una mesa puede estar asociada a pedidos; algunos pedidos pueden no usar mesa si son para llevar o delivery.
status: draft
confidence: medium

## ClienteRealizaPedido
id: cliente_realiza_pedido
from: Cliente
to: Pedido
from_cardinality: 0..1
to_cardinality: 0..M
description: Un cliente registrado puede realizar pedidos; un pedido puede ser anónimo o consumidor final.
status: draft
confidence: medium

## PedidoRegistraPago
id: pedido_registra_pago
from: Pedido
to: Pago
from_cardinality: 1
to_cardinality: 0..M
description: Un pedido puede tener pagos asociados; cada pago corresponde a un pedido.
status: draft
confidence: medium

## CajaCobraPedido
id: caja_cobra_pedido
from: Caja
to: Pedido
from_cardinality: 1
to_cardinality: 0..M
description: Una caja registra cobros de pedidos; cada pedido cobrado se asocia a una caja.
status: draft
confidence: medium

## ProductoTieneReceta
id: producto_tiene_receta
from: ProductoVenta
to: Receta
from_cardinality: 1
to_cardinality: 0..M
description: Un producto puede tener una o varias versiones de receta; cada receta corresponde a un producto.
status: draft
confidence: medium

## RecetaContieneInsumo
id: receta_contiene_insumo
from: Receta
to: DetalleReceta
from_cardinality: 1
to_cardinality: 1..M
description: Una receta contiene detalles de insumos; cada detalle pertenece a una receta.
status: draft
confidence: medium

## DetalleUsaInsumo
id: detalle_usa_insumo
from: Insumo
to: DetalleReceta
from_cardinality: 1
to_cardinality: 0..M
description: Un insumo puede aparecer en muchos detalles de receta; cada detalle referencia un insumo.
status: draft
confidence: medium

## UsuarioRegistraPedido
id: usuario_registra_pedido
from: UsuarioSistema
to: Pedido
from_cardinality: 1
to_cardinality: 0..M
description: Un usuario puede registrar pedidos; cada pedido debe tener un usuario responsable.
status: draft
confidence: medium

# Reglas de negocio

- El precio aplicado en DetallePedido debe conservarse aunque cambie el precio del producto.
- El pedido debe tener estado controlado durante su ciclo de vida.
- El consumo de insumos puede calcularse al cerrar o preparar pedidos, según decisión operativa.
- Los pedidos para llevar o delivery pueden no tener mesa.

# Dudas pendientes

- Confirmar si se manejará cocina como cola de preparación desde el MVP.
- Confirmar si habrá delivery como módulo independiente.
- Confirmar si el inventario de insumos será obligatorio o fase posterior.
- Confirmar si una receta tendrá versiones históricas activas/inactivas.
