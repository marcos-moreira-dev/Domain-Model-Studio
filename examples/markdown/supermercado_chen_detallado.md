---
id: supermercado_chen_detallado_v1
title: Modelo conceptual - Supermercado administrativo
notation: chen
version: 1.0.0
status: draft
author: @programalobien
source: ejemplo_prepracticas
domain: supermercado
---

# Entidades

## Producto
id: producto
module: inventario
description: Artículo comercial vendido o administrado por el supermercado.
status: draft
confidence: high

- pk id
- nombre
- codigo_barras [unique]
- descripcion [optional]
- precio_actual
- unidad_medida
- estado

## Categoria
id: categoria
module: inventario
description: Agrupación comercial o administrativa de productos.
status: draft
confidence: high

- pk id
- nombre
- descripcion [optional]
- estado

## Proveedor
id: proveedor
module: compras
description: Persona o empresa que abastece productos al supermercado.
status: draft
confidence: high

- pk id
- razon_social
- ruc [optional] [unique]
- telefono [optional]
- correo_electronico [optional]
- estado

## Cliente
id: cliente
module: ventas
description: Persona o entidad que realiza compras. Puede ser consumidor final o cliente registrado.
status: draft
confidence: medium

- pk id
- nombres [optional]
- apellidos [optional]
- identificacion [optional] [unique]
- telefono [optional]
- correo_electronico [optional]
- estado

## Venta
id: venta
module: ventas
description: Operación comercial registrada en caja.
status: draft
confidence: high

- pk id
- fecha_hora
- subtotal
- descuento_total
- impuesto_total
- total
- estado

## DetalleVenta
id: detalle_venta
module: ventas
description: Línea de detalle de una venta, con producto, cantidad y precio congelado.
status: draft
confidence: high

- pk id
- cantidad
- precio_unitario_aplicado
- subtotal_linea

## Caja
id: caja
module: caja
description: Punto o sesión donde se registran cobros y movimientos de dinero.
status: draft
confidence: medium

- pk id
- nombre
- estado

## Pago
id: pago
module: caja
description: Registro de pago asociado a una venta.
status: draft
confidence: medium

- pk id
- metodo_pago
- monto
- referencia [optional]
- fecha_hora

## MovimientoInventario
id: movimiento_inventario
module: inventario
description: Entrada, salida o ajuste de stock relacionado con productos.
status: draft
confidence: high

- pk id
- tipo_movimiento
- cantidad
- fecha_hora
- motivo [optional]

## CompraProveedor
id: compra_proveedor
module: compras
description: Registro de compra o abastecimiento realizado a un proveedor.
status: draft
confidence: medium

- pk id
- fecha
- total_estimado [optional]
- estado

## DetalleCompraProveedor
id: detalle_compra_proveedor
module: compras
description: Línea de producto dentro de una compra a proveedor.
status: draft
confidence: medium

- pk id
- cantidad
- costo_unitario
- subtotal_linea

## UsuarioSistema
id: usuario_sistema
module: seguridad
description: Usuario que opera el sistema administrativo.
status: draft
confidence: high

- pk id
- nombre_login [unique]
- credencial [sensitive]
- rol
- estado

# Relaciones

## Pertenece
id: pertenece
from: Producto
to: Categoria
from_cardinality: 0..M
to_cardinality: 1
description: Una categoría puede agrupar muchos productos; cada producto pertenece a una categoría principal.
status: draft
confidence: medium

## Abastece
id: abastece
from: Proveedor
to: Producto
from_cardinality: 0..M
to_cardinality: 0..M
description: Un proveedor puede abastecer varios productos y un producto podría tener varios proveedores.
status: draft
confidence: medium

## Realiza
id: realiza
from: Cliente
to: Venta
from_cardinality: 0..1
to_cardinality: 0..M
description: Un cliente registrado puede realizar muchas ventas; una venta puede ser consumidor final sin cliente registrado.
status: draft
confidence: medium

## Contiene
id: contiene
from: Venta
to: DetalleVenta
from_cardinality: 1
to_cardinality: 1..M
description: Una venta debe contener al menos un detalle; cada detalle pertenece a una venta.
status: draft
confidence: high

## DetallaProducto
id: detalla_producto
from: Producto
to: DetalleVenta
from_cardinality: 1
to_cardinality: 0..M
description: Un producto puede aparecer en muchos detalles de venta; cada detalle referencia un producto.
status: draft
confidence: high

## Cobra
id: cobra
from: Caja
to: Venta
from_cardinality: 1
to_cardinality: 0..M
description: Una caja puede registrar muchas ventas; cada venta se registra desde una caja.
status: draft
confidence: medium

## RegistraPago
id: registra_pago
from: Venta
to: Pago
from_cardinality: 1
to_cardinality: 1..M
description: Una venta puede tener uno o varios pagos; cada pago corresponde a una venta.
status: draft
confidence: medium

## GeneraMovimiento
id: genera_movimiento
from: Producto
to: MovimientoInventario
from_cardinality: 1
to_cardinality: 0..M
description: Un producto puede tener muchos movimientos de inventario; cada movimiento corresponde a un producto.
status: draft
confidence: high

## SolicitaCompra
id: solicita_compra
from: Proveedor
to: CompraProveedor
from_cardinality: 1
to_cardinality: 0..M
description: Un proveedor puede estar asociado a muchas compras; cada compra se realiza a un proveedor.
status: draft
confidence: medium

## CompraContiene
id: compra_contiene
from: CompraProveedor
to: DetalleCompraProveedor
from_cardinality: 1
to_cardinality: 1..M
description: Una compra contiene líneas de detalle; cada detalle pertenece a una compra.
status: draft
confidence: medium

## CompraDetalleProducto
id: compra_detalle_producto
from: Producto
to: DetalleCompraProveedor
from_cardinality: 1
to_cardinality: 0..M
description: Un producto puede aparecer en muchos detalles de compra; cada detalle referencia un producto.
status: draft
confidence: medium

## OperaVenta
id: opera_venta
from: UsuarioSistema
to: Venta
from_cardinality: 1
to_cardinality: 0..M
description: Un usuario del sistema puede registrar muchas ventas; cada venta debe tener un usuario responsable.
status: draft
confidence: medium

# Reglas de negocio

- El precio aplicado en DetalleVenta debe quedar congelado para preservar historial.
- La venta final debe registrarse en una transacción persistente.
- La consulta rápida por código de barras puede apoyarse en caché o memoria, pero la fuente oficial sigue siendo la base de datos.
- El stock actual puede derivarse de movimientos o mantenerse como dato operativo sincronizado, según decisión futura.
- Una venta no debe cerrarse sin detalles.

# Dudas pendientes

- Confirmar si el stock se manejará globalmente o por sucursal/bodega.
- Confirmar si el sistema requiere facturación electrónica o solo comprobante interno.
- Confirmar si una venta puede pagarse con métodos mixtos desde el MVP.
- Confirmar si proveedor-producto será relación N:M real o si basta un proveedor principal por producto.
- Confirmar si habrá apertura y cierre de caja formal desde la primera versión.
