# Gramática Markdown — Mapa de módulos

Estado: importable por la app.  
Salida esperada: diagrama visual de módulos, submódulos y dependencias.  
Uso recomendado: generar rápidamente el mapa funcional de una aplicación administrativa a partir del levantamiento con el cliente.

## Front matter obligatorio

```yaml
---
dms_version: "1"
diagram_type: "admin-module-map"
name: "Mapa de módulos — <cliente o sistema>"
importable: true
intended_output: "diagrama visual"
---
```

## Estructura importable

```md
# Módulos

## Ventas
id: ventas
responsabilidad: registrar pedidos, cuentas y comprobantes.

### Submódulos
- pedidos: toma y seguimiento de pedidos.
- caja: cobro y cierre diario.

## Inventario
id: inventario
responsabilidad: controlar productos, insumos y existencias.

# Dependencias

- ventas -> inventario: descuenta productos vendidos.
```

## Reglas

- `diagram_type` debe ser `admin-module-map`.
- Cada módulo principal usa encabezado `##`.
- Cada módulo debe declarar `id`.
- La responsabilidad se declara con `responsabilidad:`.
- Los submódulos van bajo `### Submódulos` como lista `- id: descripción`.
- Las dependencias van en la sección `# Dependencias` con la forma `origen -> destino: motivo`.
- Las dependencias deben usar IDs existentes de módulos principales.
