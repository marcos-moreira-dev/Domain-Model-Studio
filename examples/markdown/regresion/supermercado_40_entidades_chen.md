---
id: supermercado_40_entidades_v1
title: Regresión - Supermercado 40 entidades
notation: chen
version: 1.0.0
status: regression
author: Domain Model Studio
domain: supermercado
---

# Entidades

## Sucursal
id: sucursal
module: estructura
description: Entidad de regresión para el módulo estructura.
status: draft
confidence: medium

- pk id
- nombre
- estado

## Bodega
id: bodega
module: inventario
description: Entidad de regresión para el módulo inventario.
status: draft
confidence: medium

- pk id
- nombre
- estado

## Producto
id: producto
module: inventario
description: Entidad de regresión para el módulo inventario.
status: draft
confidence: medium

- pk id
- nombre
- estado
- codigo_referencia [unique]

## Categoria
id: categoria
module: inventario
description: Entidad de regresión para el módulo inventario.
status: draft
confidence: medium

- pk id
- nombre
- estado

## Marca
id: marca
module: inventario
description: Entidad de regresión para el módulo inventario.
status: draft
confidence: medium

- pk id
- nombre
- estado

## UnidadMedida
id: unidad_medida
module: inventario
description: Entidad de regresión para el módulo inventario.
status: draft
confidence: medium

- pk id
- nombre
- estado

## Lote
id: lote
module: inventario
description: Entidad de regresión para el módulo inventario.
status: draft
confidence: medium

- pk id
- nombre
- estado

## PrecioHistorico
id: precio_historico
module: ventas
description: Entidad de regresión para el módulo ventas.
status: draft
confidence: medium

- pk id
- nombre
- estado

## Promocion
id: promocion
module: ventas
description: Entidad de regresión para el módulo ventas.
status: draft
confidence: medium

- pk id
- nombre
- estado

## Proveedor
id: proveedor
module: compras
description: Entidad de regresión para el módulo compras.
status: draft
confidence: medium

- pk id
- nombre
- estado
- codigo_referencia [unique]

## CompraProveedor
id: compra_proveedor
module: compras
description: Entidad de regresión para el módulo compras.
status: draft
confidence: medium

- pk id
- nombre
- estado

## DetalleCompra
id: detalle_compra
module: compras
description: Entidad de regresión para el módulo compras.
status: draft
confidence: medium

- pk id
- nombre
- estado

## RecepcionMercaderia
id: recepcion_mercaderia
module: compras
description: Entidad de regresión para el módulo compras.
status: draft
confidence: medium

- pk id
- nombre
- estado

## AjusteInventario
id: ajuste_inventario
module: inventario
description: Entidad de regresión para el módulo inventario.
status: draft
confidence: medium

- pk id
- nombre
- estado

## MovimientoInventario
id: movimiento_inventario
module: inventario
description: Entidad de regresión para el módulo inventario.
status: draft
confidence: medium

- pk id
- nombre
- estado
- fecha_hora

## Cliente
id: cliente
module: ventas
description: Entidad de regresión para el módulo ventas.
status: draft
confidence: medium

- pk id
- nombre
- estado
- codigo_referencia [unique]

## Venta
id: venta
module: ventas
description: Entidad de regresión para el módulo ventas.
status: draft
confidence: medium

- pk id
- nombre
- estado
- fecha_hora

## DetalleVenta
id: detalle_venta
module: ventas
description: Entidad de regresión para el módulo ventas.
status: draft
confidence: medium

- pk id
- nombre
- estado

## Pago
id: pago
module: caja
description: Entidad de regresión para el módulo caja.
status: draft
confidence: medium

- pk id
- nombre
- estado
- fecha_hora

## Caja
id: caja
module: caja
description: Entidad de regresión para el módulo caja.
status: draft
confidence: medium

- pk id
- nombre
- estado

## AperturaCaja
id: apertura_caja
module: caja
description: Entidad de regresión para el módulo caja.
status: draft
confidence: medium

- pk id
- nombre
- estado

## CierreCaja
id: cierre_caja
module: caja
description: Entidad de regresión para el módulo caja.
status: draft
confidence: medium

- pk id
- nombre
- estado

## UsuarioSistema
id: usuario_sistema
module: seguridad
description: Entidad de regresión para el módulo seguridad.
status: draft
confidence: medium

- pk id
- nombre
- estado
- codigo_referencia [unique]

## Rol
id: rol
module: seguridad
description: Entidad de regresión para el módulo seguridad.
status: draft
confidence: medium

- pk id
- nombre
- estado

## Permiso
id: permiso
module: seguridad
description: Entidad de regresión para el módulo seguridad.
status: draft
confidence: medium

- pk id
- nombre
- estado

## Devolucion
id: devolucion
module: ventas
description: Entidad de regresión para el módulo ventas.
status: draft
confidence: medium

- pk id
- nombre
- estado

## DetalleDevolucion
id: detalle_devolucion
module: ventas
description: Entidad de regresión para el módulo ventas.
status: draft
confidence: medium

- pk id
- nombre
- estado

## Factura
id: factura
module: ventas
description: Entidad de regresión para el módulo ventas.
status: draft
confidence: medium

- pk id
- nombre
- estado

## NotaCredito
id: nota_credito
module: ventas
description: Entidad de regresión para el módulo ventas.
status: draft
confidence: medium

- pk id
- nombre
- estado

## Impuesto
id: impuesto
module: contabilidad
description: Entidad de regresión para el módulo contabilidad.
status: draft
confidence: medium

- pk id
- nombre
- estado

## Zona
id: zona
module: estructura
description: Entidad de regresión para el módulo estructura.
status: draft
confidence: medium

- pk id
- nombre
- estado

## Pasillo
id: pasillo
module: estructura
description: Entidad de regresión para el módulo estructura.
status: draft
confidence: medium

- pk id
- nombre
- estado

## Estante
id: estante
module: estructura
description: Entidad de regresión para el módulo estructura.
status: draft
confidence: medium

- pk id
- nombre
- estado

## UbicacionProducto
id: ubicacion_producto
module: inventario
description: Entidad de regresión para el módulo inventario.
status: draft
confidence: medium

- pk id
- nombre
- estado

## OrdenReposicion
id: orden_reposicion
module: inventario
description: Entidad de regresión para el módulo inventario.
status: draft
confidence: medium

- pk id
- nombre
- estado

## DetalleReposicion
id: detalle_reposicion
module: inventario
description: Entidad de regresión para el módulo inventario.
status: draft
confidence: medium

- pk id
- nombre
- estado

## AlertaStock
id: alerta_stock
module: inventario
description: Entidad de regresión para el módulo inventario.
status: draft
confidence: medium

- pk id
- nombre
- estado

## ConteoInventario
id: conteo_inventario
module: inventario
description: Entidad de regresión para el módulo inventario.
status: draft
confidence: medium

- pk id
- nombre
- estado

## DetalleConteo
id: detalle_conteo
module: inventario
description: Entidad de regresión para el módulo inventario.
status: draft
confidence: medium

- pk id
- nombre
- estado

## AuditoriaEvento
id: auditoria_evento
module: auditoria
description: Entidad de regresión para el módulo auditoria.
status: draft
confidence: medium

- pk id
- nombre
- estado
- fecha_hora

# Relaciones

## TieneBodega
id: tiene_bodega
from: Sucursal
to: Bodega
from_cardinality: 1
to_cardinality: 0..M
description: Relación de regresión entre Sucursal y Bodega.
status: draft
confidence: medium

## Clasifica
id: clasifica
from: Categoria
to: Producto
from_cardinality: 1
to_cardinality: 0..M
description: Relación de regresión entre Categoria y Producto.
status: draft
confidence: medium

## IdentificaMarca
id: identifica_marca
from: Marca
to: Producto
from_cardinality: 0..1
to_cardinality: 0..M
description: Relación de regresión entre Marca y Producto.
status: draft
confidence: medium

## Mide
id: mide
from: UnidadMedida
to: Producto
from_cardinality: 1
to_cardinality: 0..M
description: Relación de regresión entre UnidadMedida y Producto.
status: draft
confidence: medium

## AgrupaLote
id: agrupa_lote
from: Producto
to: Lote
from_cardinality: 1
to_cardinality: 0..M
description: Relación de regresión entre Producto y Lote.
status: draft
confidence: medium

## RegistraPrecio
id: registra_precio
from: Producto
to: PrecioHistorico
from_cardinality: 1
to_cardinality: 0..M
description: Relación de regresión entre Producto y PrecioHistorico.
status: draft
confidence: medium

## AplicaPromocion
id: aplica_promocion
from: Promocion
to: Producto
from_cardinality: 0..M
to_cardinality: 0..M
description: Relación de regresión entre Promocion y Producto.
status: draft
confidence: medium

## Abastece
id: abastece
from: Proveedor
to: CompraProveedor
from_cardinality: 1
to_cardinality: 0..M
description: Relación de regresión entre Proveedor y CompraProveedor.
status: draft
confidence: medium

## CompraDetalle
id: compra_detalle
from: CompraProveedor
to: DetalleCompra
from_cardinality: 1
to_cardinality: 1..M
description: Relación de regresión entre CompraProveedor y DetalleCompra.
status: draft
confidence: medium

## DetalleCompraProducto
id: detalle_compra_producto
from: Producto
to: DetalleCompra
from_cardinality: 1
to_cardinality: 0..M
description: Relación de regresión entre Producto y DetalleCompra.
status: draft
confidence: medium

## RecibeCompra
id: recibe_compra
from: CompraProveedor
to: RecepcionMercaderia
from_cardinality: 1
to_cardinality: 0..M
description: Relación de regresión entre CompraProveedor y RecepcionMercaderia.
status: draft
confidence: medium

## MueveProducto
id: mueve_producto
from: Producto
to: MovimientoInventario
from_cardinality: 1
to_cardinality: 0..M
description: Relación de regresión entre Producto y MovimientoInventario.
status: draft
confidence: medium

## AjusteGeneraMovimiento
id: ajuste_genera_movimiento
from: AjusteInventario
to: MovimientoInventario
from_cardinality: 0..1
to_cardinality: 0..M
description: Relación de regresión entre AjusteInventario y MovimientoInventario.
status: draft
confidence: medium

## ClienteRealiza
id: cliente_realiza
from: Cliente
to: Venta
from_cardinality: 0..1
to_cardinality: 0..M
description: Relación de regresión entre Cliente y Venta.
status: draft
confidence: medium

## VentaContiene
id: venta_contiene
from: Venta
to: DetalleVenta
from_cardinality: 1
to_cardinality: 1..M
description: Relación de regresión entre Venta y DetalleVenta.
status: draft
confidence: medium

## DetalleVentaProducto
id: detalle_venta_producto
from: Producto
to: DetalleVenta
from_cardinality: 1
to_cardinality: 0..M
description: Relación de regresión entre Producto y DetalleVenta.
status: draft
confidence: medium

## VentaRegistraPago
id: venta_registra_pago
from: Venta
to: Pago
from_cardinality: 1
to_cardinality: 1..M
description: Relación de regresión entre Venta y Pago.
status: draft
confidence: medium

## CajaRegistraVenta
id: caja_registra_venta
from: Caja
to: Venta
from_cardinality: 1
to_cardinality: 0..M
description: Relación de regresión entre Caja y Venta.
status: draft
confidence: medium

## CajaApertura
id: caja_apertura
from: Caja
to: AperturaCaja
from_cardinality: 1
to_cardinality: 0..M
description: Relación de regresión entre Caja y AperturaCaja.
status: draft
confidence: medium

## CajaCierre
id: caja_cierre
from: Caja
to: CierreCaja
from_cardinality: 1
to_cardinality: 0..M
description: Relación de regresión entre Caja y CierreCaja.
status: draft
confidence: medium

## UsuarioOperaVenta
id: usuario_opera_venta
from: UsuarioSistema
to: Venta
from_cardinality: 1
to_cardinality: 0..M
description: Relación de regresión entre UsuarioSistema y Venta.
status: draft
confidence: medium

## RolUsuario
id: rol_usuario
from: Rol
to: UsuarioSistema
from_cardinality: 1
to_cardinality: 0..M
description: Relación de regresión entre Rol y UsuarioSistema.
status: draft
confidence: medium

## RolPermiso
id: rol_permiso
from: Rol
to: Permiso
from_cardinality: 0..M
to_cardinality: 0..M
description: Relación de regresión entre Rol y Permiso.
status: draft
confidence: medium

## VentaDevolucion
id: venta_devolucion
from: Venta
to: Devolucion
from_cardinality: 1
to_cardinality: 0..M
description: Relación de regresión entre Venta y Devolucion.
status: draft
confidence: medium

## DevolucionDetalle
id: devolucion_detalle
from: Devolucion
to: DetalleDevolucion
from_cardinality: 1
to_cardinality: 1..M
description: Relación de regresión entre Devolucion y DetalleDevolucion.
status: draft
confidence: medium

## DetalleDevolucionProducto
id: detalle_devolucion_producto
from: Producto
to: DetalleDevolucion
from_cardinality: 1
to_cardinality: 0..M
description: Relación de regresión entre Producto y DetalleDevolucion.
status: draft
confidence: medium

## VentaFactura
id: venta_factura
from: Venta
to: Factura
from_cardinality: 1
to_cardinality: 0..1
description: Relación de regresión entre Venta y Factura.
status: draft
confidence: medium

## FacturaNotaCredito
id: factura_nota_credito
from: Factura
to: NotaCredito
from_cardinality: 1
to_cardinality: 0..M
description: Relación de regresión entre Factura y NotaCredito.
status: draft
confidence: medium

## VentaImpuesto
id: venta_impuesto
from: Impuesto
to: DetalleVenta
from_cardinality: 1
to_cardinality: 0..M
description: Relación de regresión entre Impuesto y DetalleVenta.
status: draft
confidence: medium

## SucursalZona
id: sucursal_zona
from: Sucursal
to: Zona
from_cardinality: 1
to_cardinality: 0..M
description: Relación de regresión entre Sucursal y Zona.
status: draft
confidence: medium

## ZonaPasillo
id: zona_pasillo
from: Zona
to: Pasillo
from_cardinality: 1
to_cardinality: 0..M
description: Relación de regresión entre Zona y Pasillo.
status: draft
confidence: medium

## PasilloEstante
id: pasillo_estante
from: Pasillo
to: Estante
from_cardinality: 1
to_cardinality: 0..M
description: Relación de regresión entre Pasillo y Estante.
status: draft
confidence: medium

## UbicaProducto
id: ubica_producto
from: UbicacionProducto
to: Producto
from_cardinality: 0..M
to_cardinality: 1
description: Relación de regresión entre UbicacionProducto y Producto.
status: draft
confidence: medium

## UbicacionEstante
id: ubicacion_estante
from: Estante
to: UbicacionProducto
from_cardinality: 1
to_cardinality: 0..M
description: Relación de regresión entre Estante y UbicacionProducto.
status: draft
confidence: medium

## ReposicionSucursal
id: reposicion_sucursal
from: Sucursal
to: OrdenReposicion
from_cardinality: 1
to_cardinality: 0..M
description: Relación de regresión entre Sucursal y OrdenReposicion.
status: draft
confidence: medium

## ReposicionDetalle
id: reposicion_detalle
from: OrdenReposicion
to: DetalleReposicion
from_cardinality: 1
to_cardinality: 1..M
description: Relación de regresión entre OrdenReposicion y DetalleReposicion.
status: draft
confidence: medium

## DetalleReposicionProducto
id: detalle_reposicion_producto
from: Producto
to: DetalleReposicion
from_cardinality: 1
to_cardinality: 0..M
description: Relación de regresión entre Producto y DetalleReposicion.
status: draft
confidence: medium

## ProductoAlerta
id: producto_alerta
from: Producto
to: AlertaStock
from_cardinality: 1
to_cardinality: 0..M
description: Relación de regresión entre Producto y AlertaStock.
status: draft
confidence: medium

## ConteoSucursal
id: conteo_sucursal
from: Sucursal
to: ConteoInventario
from_cardinality: 1
to_cardinality: 0..M
description: Relación de regresión entre Sucursal y ConteoInventario.
status: draft
confidence: medium

## ConteoDetalle
id: conteo_detalle
from: ConteoInventario
to: DetalleConteo
from_cardinality: 1
to_cardinality: 1..M
description: Relación de regresión entre ConteoInventario y DetalleConteo.
status: draft
confidence: medium

## DetalleConteoProducto
id: detalle_conteo_producto
from: Producto
to: DetalleConteo
from_cardinality: 1
to_cardinality: 0..M
description: Relación de regresión entre Producto y DetalleConteo.
status: draft
confidence: medium

## AuditaUsuario
id: audita_usuario
from: UsuarioSistema
to: AuditoriaEvento
from_cardinality: 1
to_cardinality: 0..M
description: Relación de regresión entre UsuarioSistema y AuditoriaEvento.
status: draft
confidence: medium

# Reglas de negocio

- Este archivo existe para probar que el parser, validadores, layouts y exportadores soporten modelos grandes.
- El auto-layout puede requerir ajuste manual; la regresión busca robustez, no belleza perfecta.