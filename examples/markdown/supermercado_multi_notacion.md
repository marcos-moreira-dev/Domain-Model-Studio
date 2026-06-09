---
id: supermercado_v1
title: Modelo conceptual - Supermercado
notation: chen
default_notation: chen
available_notations:
  - chen
  - crows_foot
version: 1.0.0
status: draft
migration_ref: V1__initial_schema
purpose: Levantamiento inicial de dominio para un sistema administrativo de supermercado.
---

# Modelo conceptual - Supermercado

Este ejemplo está pensado para probar la gramática de entrada de Domain Model Studio con una complejidad más realista que un ejemplo mínimo.

El mismo modelo semántico debe poder representarse primero en notación Chen y, en una fase futura, en Crow's Foot / pata de gallo.

# Entidades

## Producto
id: producto
module: inventario
kind: strong_entity
description: Artículo vendido o administrado por el supermercado.

- producto_id [pk]
- nombre
- codigo_barras [unique]
- precio_actual
- stock_actual [derived]
- unidad_medida
- estado

## Categoria
id: categoria
module: inventario
kind: strong_entity
description: Clasificación comercial de productos.

- categoria_id [pk]
- nombre [unique]
- descripcion [optional]
- estado

## Proveedor
id: proveedor
module: compras
kind: strong_entity
description: Persona o empresa que abastece productos.

- proveedor_id [pk]
- razon_social
- ruc [unique]
- telefono [optional]
- correo [optional]
- estado

## Cliente
id: cliente
module: ventas
kind: strong_entity
description: Cliente registrado para facturación, historial o beneficios.

- cliente_id [pk]
- nombres
- apellidos
- identificacion [unique]
- telefono [optional]
- correo [optional]
- estado

## Venta
id: venta
module: ventas
kind: strong_entity
description: Transacción comercial registrada en caja.

- venta_id [pk]
- fecha_hora
- subtotal [derived]
- descuento_total [derived]
- impuesto_total [derived]
- total [derived]
- estado

## DetalleVenta
id: detalle_venta
module: ventas
kind: weak_entity
description: Línea de detalle de una venta. Depende de Venta y referencia Producto.

- detalle_venta_id [partial_key]
- cantidad
- precio_unitario
- subtotal_linea [derived]

## Caja
id: caja
module: caja
kind: strong_entity
description: Punto o sesión donde se registran cobros.

- caja_id [pk]
- codigo [unique]
- fecha_apertura
- fecha_cierre [optional]
- saldo_inicial
- saldo_final [derived]
- estado

## Usuario
id: usuario
module: seguridad
kind: strong_entity
description: Usuario del sistema administrativo.

- usuario_id [pk]
- nombre_login [unique]
- password_hash [sensitive]
- rol
- estado

# Relaciones

## Clasifica
id: clasifica
from: Categoria
to: Producto
from_cardinality: 1
to_cardinality: 0..M
participation_to: total
description: Cada producto debe pertenecer a una categoría; una categoría puede clasificar muchos productos.

## Abastece
id: abastece
from: Proveedor
to: Producto
from_cardinality: 0..M
to_cardinality: 0..M
relationship_entity_hint: proveedor_producto
description: Un proveedor puede abastecer muchos productos y un producto puede tener varios proveedores.

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
identifying: true
participation_to: total
description: Una venta contiene uno o más detalles; el detalle no existe sin la venta.

## Referencia
id: referencia_producto
from: DetalleVenta
to: Producto
from_cardinality: 0..M
to_cardinality: 1
participation_from: total
description: Cada detalle de venta referencia exactamente un producto.

## Registra
id: registra
from: Usuario
to: Venta
from_cardinality: 1
to_cardinality: 0..M
description: Un usuario cajero o administrador puede registrar muchas ventas.

## OcurreEn
id: ocurre_en_caja
from: Caja
to: Venta
from_cardinality: 1
to_cardinality: 0..M
description: Una venta ocurre en una caja abierta.

# Reglas de negocio detectadas

- Una venta debe tener al menos un detalle.
- El total de la venta es derivado de sus detalles, descuentos e impuestos.
- El stock actual es derivado de movimientos de inventario, aunque puede materializarse por rendimiento.
- Un producto puede existir sin proveedor asignado durante la carga inicial, pero para compras reales debe tener proveedor.
- DetalleVenta se considera entidad débil en el modelo conceptual porque depende de Venta.

# Preferencias de visualización

## Chen
show_attributes: true
show_relationship_diamonds: true
show_cardinality_labels: true

## CrowFoot
show_attributes_inside_entities: true
show_relationship_labels: optional
marker_orientation: auto
