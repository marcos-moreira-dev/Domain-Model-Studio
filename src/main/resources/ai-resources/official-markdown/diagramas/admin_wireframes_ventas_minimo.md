---
dms_version: "1"
diagram_type: "admin-wireframes"
name: "Wireframes administrativos — ventas mínimo"
sample_kind: "minimal"
domain: "ventas"
status: "importable"
importable: true
intended_output: "wireframe visual"
---
# Pantallas

## Lista de ventas
id: lista_ventas
tipo: escritorio

### Secciones
- encabezado: título "Ventas" y botón Nueva venta.
- filtros: fecha, cliente, estado y número de comprobante.
- tabla_resultados: columnas fecha, cliente, total, estado y acciones.
- pie: total del día y cantidad de ventas.

### Controles
- botón_nueva_venta: abre formulario de venta.
- botón_exportar: exporta listado filtrado.
- acción_ver: abre detalle de una venta.

## Nueva venta
id: nueva_venta
tipo: escritorio

### Secciones
- datos_cliente: búsqueda o registro rápido de cliente.
- productos: tabla de ítems, cantidad, precio y subtotal.
- resumen: subtotal, descuento, impuestos y total.
- acciones: guardar, cancelar, emitir comprobante.
