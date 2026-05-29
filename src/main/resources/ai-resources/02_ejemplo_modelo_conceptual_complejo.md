---
id: supermercado_integral_complejo_v1
title: Modelo conceptual complejo - Supermercado integral
notation: chen
project_type: CONCEPTUAL_MODEL
version: 1.0.0
status: draft
author: @programalobien
source: ejemplo_interno_para_ia
business_area: administracion_integral
---

# Entidades

## Sucursal
id: sucursal
kind: strong
module: organizacion
status: confirmed
description: Punto físico donde se venden productos, se gestiona inventario y se opera caja.

- pk id
- nombre
- codigo [unique]
- direccion [composite]
- telefono [optional]
- estado
- fecha_apertura

## Area de negocio
id: area_negocio
kind: strong
module: organizacion
status: confirmed
description: Área funcional del negocio, por ejemplo inventario, ventas, caja, compras o administración.

- pk id
- nombre [unique]
- descripcion [optional]
- activa

## Empleado
id: empleado
kind: strong
module: seguridad
status: confirmed
description: Persona que trabaja en una sucursal y puede operar cajas, inventario o procesos administrativos.

- pk id
- cedula [unique]
- nombres
- apellidos
- telefono [optional]
- correo [optional] [unique]
- fecha_ingreso
- estado

## Usuario de sistema
id: usuario_sistema
kind: strong
module: seguridad
status: confirmed
description: Credencial lógica de acceso al sistema. Se separa de empleado para permitir auditoría y seguridad.

- pk id
- username [unique]
- password_hash [sensitive]
- ultimo_acceso [optional]
- bloqueado
- estado

## Rol
id: rol
kind: strong
module: seguridad
status: confirmed
description: Perfil de permisos asignable a usuarios.

- pk id
- nombre [unique]
- descripcion [optional]
- nivel
- activo

## Permiso
id: permiso
kind: strong
module: seguridad
status: confirmed
description: Acción autorizable dentro del sistema.

- pk id
- codigo [unique]
- modulo
- accion
- descripcion [optional]

## Cliente
id: cliente
kind: strong
module: ventas
status: confirmed
description: Persona natural o jurídica que compra productos o acumula beneficios.

- pk id
- identificacion [unique]
- nombres
- apellidos [optional]
- razon_social [optional]
- telefono [optional]
- correo [optional]
- direccion [optional] [composite]
- puntos_acumulados [derived]
- estado

## Categoria
id: categoria
kind: strong
module: inventario
status: confirmed
description: Agrupación comercial de productos.

- pk id
- nombre [unique]
- descripcion [optional]
- activa

## Marca
id: marca
kind: strong
module: inventario
status: confirmed
description: Marca comercial asociada a productos.

- pk id
- nombre [unique]
- pais_origen [optional]
- activa

## Producto
id: producto
kind: strong
module: inventario
status: confirmed
description: Artículo administrado y vendido por el supermercado.

- pk id
- sku [unique]
- nombre
- descripcion [optional]
- codigo_barras [unique]
- precio_base
- precio_actual [derived]
- requiere_lote
- es_perecible
- estado

## Variante de producto
id: variante_producto
kind: weak
module: inventario
status: draft
description: Presentación específica de un producto, por ejemplo tamaño, sabor, color o empaque.

- codigo_variante [partial_key]
- nombre
- unidad_medida
- contenido_neto
- codigo_barras [unique]
- activa

## Lote
id: lote
kind: weak
module: inventario
status: confirmed
description: Grupo de unidades de producto con fecha de vencimiento o trazabilidad de compra.

- numero_lote [partial_key]
- fecha_fabricacion [optional]
- fecha_vencimiento [optional]
- costo_unitario
- estado

## Inventario sucursal
id: inventario_sucursal
kind: strong
module: inventario
status: confirmed
description: Existencia de un producto o variante en una sucursal concreta. Funciona como entidad asociativa entre sucursal y producto.

- pk id
- stock_actual [derived]
- stock_minimo
- stock_maximo
- ubicacion_estante [optional]
- actualizado_en

## Movimiento de inventario
id: movimiento_inventario
kind: strong
module: inventario
status: confirmed
description: Entrada, salida, ajuste o transferencia que modifica existencias.

- pk id
- tipo_movimiento
- cantidad
- fecha_hora
- motivo [optional]
- referencia_externa [optional]

## Proveedor
id: proveedor
kind: strong
module: compras
status: confirmed
description: Empresa o persona que abastece productos al supermercado.

- pk id
- ruc [unique]
- razon_social
- telefono [optional]
- correo [optional]
- direccion [optional] [composite]
- estado

## Contrato de suministro
id: contrato_suministro
kind: strong
module: compras
status: review
description: Acuerdo marco con un proveedor para suministrar productos a una o varias sucursales.

- pk id
- codigo [unique]
- fecha_inicio
- fecha_fin [optional]
- condiciones_pago
- estado

## Suministro contratado
id: suministro_contratado
kind: strong
module: compras
status: review
description: Entidad asociativa que modela una relación compleja entre proveedor, producto, sucursal y contrato.

- pk id
- precio_pactado
- cantidad_minima
- tiempo_entrega_dias
- vigente

## Orden de compra
id: orden_compra
kind: strong
module: compras
status: confirmed
description: Solicitud formal de compra emitida a un proveedor.

- pk id
- numero [unique]
- fecha_emision
- fecha_esperada
- estado
- total_estimado [derived]

## Detalle orden de compra
id: detalle_orden_compra
kind: weak
module: compras
status: confirmed
description: Línea dependiente de una orden de compra.

- numero_linea [partial_key]
- cantidad_solicitada
- costo_estimado
- subtotal_estimado [derived]

## Caja
id: caja
kind: strong
module: caja
status: confirmed
description: Punto de cobro físico o lógico dentro de una sucursal.

- pk id
- codigo [unique]
- nombre
- estado

## Turno de caja
id: turno_caja
kind: strong
module: caja
status: confirmed
description: Periodo operativo en el que un empleado administra una caja.

- pk id
- fecha_apertura
- fecha_cierre [optional]
- monto_inicial
- monto_final [optional]
- diferencia [derived]
- estado

## Venta
id: venta
kind: strong
module: ventas
status: confirmed
description: Transacción comercial realizada a un cliente o consumidor final.

- pk id
- numero [unique]
- fecha_hora
- subtotal [derived]
- descuento_total [derived]
- impuesto_total [derived]
- total [derived]
- estado

## Detalle de venta
id: detalle_venta
kind: weak
module: ventas
status: confirmed
description: Línea de venta dependiente de una venta principal.

- numero_linea [partial_key]
- cantidad
- precio_unitario
- descuento_linea
- subtotal_linea [derived]

## Pago
id: pago
kind: weak
module: caja
status: confirmed
description: Forma de pago usada para cancelar total o parcialmente una venta.

- numero_pago [partial_key]
- metodo
- monto
- referencia [optional]
- confirmado

## Promocion
id: promocion
kind: strong
module: ventas
status: review
description: Regla comercial temporal que afecta precios o beneficios.

- pk id
- nombre
- fecha_inicio
- fecha_fin
- tipo_promocion
- porcentaje_descuento [optional]
- monto_descuento [optional]
- activa

## Aplicacion de promocion
id: aplicacion_promocion
kind: strong
module: ventas
status: review
description: Entidad asociativa para modelar la aplicación de una promoción a producto, sucursal y venta.

- pk id
- cantidad_afectada
- descuento_aplicado
- regla_evaluada
- fecha_hora

## Devolucion
id: devolucion
kind: strong
module: ventas
status: draft
description: Proceso de devolución total o parcial de una venta.

- pk id
- numero [unique]
- fecha_hora
- motivo
- estado
- total_devuelto [derived]

## Detalle devolucion
id: detalle_devolucion
kind: weak
module: ventas
status: draft
description: Línea dependiente de una devolución.

- numero_linea [partial_key]
- cantidad_devuelta
- monto_devuelto
- observacion [optional]

## Auditoria
id: auditoria
kind: strong
module: seguridad
status: confirmed
description: Registro de eventos importantes ejecutados por usuarios del sistema.

- pk id
- fecha_hora
- accion
- modulo
- entidad_afectada
- identificador_afectado [optional]
- ip_origen [optional]
- detalle [optional]

# Relaciones

## Sucursal pertenece a area
id: sucursal_pertenece_area
from: Sucursal
to: Area de negocio
from_cardinality: 1..M
to_cardinality: 1
kind: regular
from_participation: total
to_participation: partial
description: Cada sucursal opera bajo al menos un área responsable; un área puede gestionar varias sucursales.

## Empleado trabaja en sucursal
id: empleado_trabaja_sucursal
from: Sucursal
to: Empleado
from_cardinality: 1
to_cardinality: 0..M
kind: regular
from_participation: partial
to_participation: total
description: Un empleado activo se asigna a una sucursal principal.

## Empleado posee usuario
id: empleado_posee_usuario
from: Empleado
to: Usuario de sistema
from_cardinality: 1
to_cardinality: 0..1
kind: regular
from_participation: partial
to_participation: total
description: Un usuario de sistema corresponde a un empleado; no todo empleado necesita credencial.

## Usuario tiene rol
id: usuario_tiene_rol
from: Usuario de sistema
to: Rol
from_cardinality: 0..M
to_cardinality: 1..M
kind: associative
from_participation: total
to_participation: partial
description: Un usuario puede tener varios roles y un rol puede asignarse a muchos usuarios.

## Rol concede permiso
id: rol_concede_permiso
from: Rol
to: Permiso
from_cardinality: 0..M
to_cardinality: 0..M
kind: associative
from_participation: partial
to_participation: partial
description: Un rol agrupa permisos; un permiso puede pertenecer a varios roles.

## Categoria agrupa producto
id: categoria_agrupa_producto
from: Categoria
to: Producto
from_cardinality: 0..M
to_cardinality: 1
kind: regular
from_participation: partial
to_participation: total
description: Cada producto tiene una categoría principal.

## Marca identifica producto
id: marca_identifica_producto
from: Marca
to: Producto
from_cardinality: 0..M
to_cardinality: 0..1
kind: regular
from_participation: partial
to_participation: partial
description: Un producto puede tener marca; algunas marcas agrupan muchos productos.

## Producto identifica variante
id: producto_identifica_variante
from: Producto
to: Variante de producto
from_cardinality: 1
to_cardinality: 0..M
kind: identifying
from_participation: partial
to_participation: total
description: La variante no existe de forma independiente al producto base.

## Producto identifica lote
id: producto_identifica_lote
from: Producto
to: Lote
from_cardinality: 1
to_cardinality: 0..M
kind: identifying
from_participation: partial
to_participation: total
description: Los lotes se asocian a productos trazables o perecibles.

## Sucursal mantiene inventario
id: sucursal_mantiene_inventario
from: Sucursal
to: Inventario sucursal
from_cardinality: 1
to_cardinality: 0..M
kind: associative
from_participation: partial
to_participation: total
description: Cada registro de inventario pertenece a una sucursal.

## Producto aparece en inventario
id: producto_aparece_inventario
from: Producto
to: Inventario sucursal
from_cardinality: 1
to_cardinality: 0..M
kind: associative
from_participation: partial
to_participation: total
description: Un producto puede existir en inventarios de varias sucursales.

## Inventario registra movimiento
id: inventario_registra_movimiento
from: Inventario sucursal
to: Movimiento de inventario
from_cardinality: 1
to_cardinality: 0..M
kind: regular
from_participation: partial
to_participation: total
description: Todo movimiento afecta un registro de inventario.

## Empleado autoriza movimiento
id: empleado_autoriza_movimiento
from: Empleado
to: Movimiento de inventario
from_cardinality: 0..M
to_cardinality: 1
kind: regular
from_participation: partial
to_participation: total
description: Cada movimiento debe quedar asociado al empleado responsable.

## Proveedor firma contrato
id: proveedor_firma_contrato
from: Proveedor
to: Contrato de suministro
from_cardinality: 1
to_cardinality: 0..M
kind: regular
from_participation: partial
to_participation: total
description: Un contrato de suministro pertenece a un proveedor.

## Contrato define suministro
id: contrato_define_suministro
from: Contrato de suministro
to: Suministro contratado
from_cardinality: 1
to_cardinality: 1..M
kind: identifying
from_participation: total
to_participation: total
description: Cada suministro contratado depende de un contrato marco.

## Proveedor participa en suministro
id: proveedor_participa_suministro
from: Proveedor
to: Suministro contratado
from_cardinality: 1
to_cardinality: 0..M
kind: associative
from_participation: partial
to_participation: total
description: Simula un componente de la relación compleja proveedor-producto-sucursal-contrato.

## Producto participa en suministro
id: producto_participa_suministro
from: Producto
to: Suministro contratado
from_cardinality: 1
to_cardinality: 0..M
kind: associative
from_participation: partial
to_participation: total
description: Simula un componente de la relación compleja proveedor-producto-sucursal-contrato.

## Sucursal participa en suministro
id: sucursal_participa_suministro
from: Sucursal
to: Suministro contratado
from_cardinality: 1
to_cardinality: 0..M
kind: associative
from_participation: partial
to_participation: total
description: Simula un componente de la relación compleja proveedor-producto-sucursal-contrato.

## Proveedor recibe orden
id: proveedor_recibe_orden
from: Proveedor
to: Orden de compra
from_cardinality: 1
to_cardinality: 0..M
kind: regular
from_participation: partial
to_participation: total
description: Una orden de compra se emite a un proveedor.

## Sucursal emite orden
id: sucursal_emite_orden
from: Sucursal
to: Orden de compra
from_cardinality: 1
to_cardinality: 0..M
kind: regular
from_participation: partial
to_participation: total
description: Una sucursal puede emitir órdenes de compra.

## Orden identifica detalle
id: orden_identifica_detalle
from: Orden de compra
to: Detalle orden de compra
from_cardinality: 1
to_cardinality: 1..M
kind: identifying
from_participation: total
to_participation: total
description: Un detalle de orden no existe sin su orden.

## Producto solicitado en detalle compra
id: producto_solicitado_detalle_compra
from: Producto
to: Detalle orden de compra
from_cardinality: 1
to_cardinality: 0..M
kind: regular
from_participation: partial
to_participation: total
description: Cada detalle de orden solicita un producto.

## Sucursal posee caja
id: sucursal_posee_caja
from: Sucursal
to: Caja
from_cardinality: 1
to_cardinality: 1..M
kind: regular
from_participation: total
to_participation: total
description: Cada caja pertenece a una sucursal.

## Caja abre turno
id: caja_abre_turno
from: Caja
to: Turno de caja
from_cardinality: 1
to_cardinality: 0..M
kind: regular
from_participation: partial
to_participation: total
description: Una caja puede tener muchos turnos a lo largo del tiempo.

## Empleado opera turno
id: empleado_opera_turno
from: Empleado
to: Turno de caja
from_cardinality: 0..M
to_cardinality: 1
kind: regular
from_participation: partial
to_participation: total
description: Cada turno de caja tiene un responsable.

## Turno registra venta
id: turno_registra_venta
from: Turno de caja
to: Venta
from_cardinality: 1
to_cardinality: 0..M
kind: regular
from_participation: partial
to_participation: total
description: Toda venta se registra dentro de un turno de caja.

## Cliente realiza venta
id: cliente_realiza_venta
from: Cliente
to: Venta
from_cardinality: 0..1
to_cardinality: 0..M
kind: regular
from_participation: partial
to_participation: partial
description: Una venta puede ser anónima o estar asociada a un cliente.

## Venta identifica detalle
id: venta_identifica_detalle
from: Venta
to: Detalle de venta
from_cardinality: 1
to_cardinality: 1..M
kind: identifying
from_participation: total
to_participation: total
description: El detalle de venta no existe sin su venta.

## Producto vendido en detalle
id: producto_vendido_detalle
from: Producto
to: Detalle de venta
from_cardinality: 1
to_cardinality: 0..M
kind: regular
from_participation: partial
to_participation: total
description: Cada línea de venta referencia un producto.

## Lote vendido en detalle
id: lote_vendido_detalle
from: Lote
to: Detalle de venta
from_cardinality: 0..1
to_cardinality: 0..M
kind: regular
from_participation: partial
to_participation: partial
description: Si el producto requiere lote, la línea de venta puede asociarse a un lote.

## Venta identifica pago
id: venta_identifica_pago
from: Venta
to: Pago
from_cardinality: 1
to_cardinality: 1..M
kind: identifying
from_participation: total
to_participation: total
description: Una venta puede pagarse con uno o varios pagos.

## Promocion aplicada en venta
id: promocion_aplicada_venta
from: Promocion
to: Aplicacion de promocion
from_cardinality: 1
to_cardinality: 0..M
kind: associative
from_participation: partial
to_participation: total
description: La aplicación de promoción registra qué regla promocional fue usada.

## Producto afectado por promocion
id: producto_afectado_promocion
from: Producto
to: Aplicacion de promocion
from_cardinality: 1
to_cardinality: 0..M
kind: associative
from_participation: partial
to_participation: total
description: La aplicación promocional puede afectar un producto concreto.

## Venta recibe promocion
id: venta_recibe_promocion
from: Venta
to: Aplicacion de promocion
from_cardinality: 1
to_cardinality: 0..M
kind: associative
from_participation: partial
to_participation: total
description: La promoción aplicada queda trazada dentro de una venta.

## Sucursal limita promocion
id: sucursal_limita_promocion
from: Sucursal
to: Aplicacion de promocion
from_cardinality: 1
to_cardinality: 0..M
kind: associative
from_participation: partial
to_participation: total
description: Permite representar promociones por sucursal como relación compleja.

## Venta origina devolucion
id: venta_origina_devolucion
from: Venta
to: Devolucion
from_cardinality: 1
to_cardinality: 0..M
kind: regular
from_participation: partial
to_participation: total
description: Una devolución siempre se origina desde una venta.

## Devolucion identifica detalle
id: devolucion_identifica_detalle
from: Devolucion
to: Detalle devolucion
from_cardinality: 1
to_cardinality: 1..M
kind: identifying
from_participation: total
to_participation: total
description: Cada devolución debe tener una o más líneas de detalle.

## Detalle venta devuelto
id: detalle_venta_devuelto
from: Detalle de venta
to: Detalle devolucion
from_cardinality: 1
to_cardinality: 0..M
kind: regular
from_participation: partial
to_participation: total
description: Una línea de devolución referencia la línea vendida originalmente.

## Usuario genera auditoria
id: usuario_genera_auditoria
from: Usuario de sistema
to: Auditoria
from_cardinality: 1
to_cardinality: 0..M
kind: regular
from_participation: partial
to_participation: total
description: Todo evento auditado debe quedar asociado al usuario que ejecutó la acción.

## Sucursal contextualiza auditoria
id: sucursal_contextualiza_auditoria
from: Sucursal
to: Auditoria
from_cardinality: 0..1
to_cardinality: 0..M
kind: regular
from_participation: partial
to_participation: partial
description: Algunos eventos se vinculan a una sucursal específica.
