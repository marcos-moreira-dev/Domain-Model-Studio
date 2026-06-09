# 31 - Vistas y cambio de notación

## 1. Objetivo

Domain Model Studio debe permitir que un mismo modelo pueda tener distintas vistas.

Ejemplos:

```txt
Vista conceptual completa Chen
Vista compacta Chen sin atributos
Vista Crow's Foot lógica
Vista por módulo: inventario
Vista por módulo: ventas/caja
Vista por migración: V3
```

## 2. Vista no es modelo

Una vista no crea un dominio nuevo.

Una vista es una forma de mirar el mismo modelo.

```txt
Modelo:
  Producto, Categoría, Venta, DetalleVenta

Vista A:
  todo el modelo con atributos

Vista B:
  solo inventario

Vista C:
  todo en Crow's Foot
```

## 3. Estructura sugerida en `.dms`

```json
"views": [
  {
    "id": "chen_full",
    "name": "Chen completo",
    "notation": "CHEN",
    "visibleModules": ["inventario", "ventas", "seguridad"],
    "showAttributes": true,
    "showCardinalities": true,
    "layoutRef": "CHEN"
  },
  {
    "id": "crows_foot_logical",
    "name": "Crow's Foot lógico",
    "notation": "CROWS_FOOT",
    "showAttributes": true,
    "layoutRef": "CROWS_FOOT"
  }
]
```

## 4. Cambio de notación en UI

Selector recomendado:

```txt
Notación: [ Chen ▼ ]
Vista:    [ Completa ▼ ]
```

El flujo esperado:

```txt
Usuario selecciona Crow's Foot
↓
ViewModel actualiza activeNotation
↓
Application pide layout de CROWS_FOOT
↓
Si existe, se usa
↓
Si no existe, se genera layout inicial
↓
Presentation redibuja canvas
```

## 5. No perder edición manual

Si el usuario mueve figuras en Chen y luego cambia a Crow's Foot, la app no debe borrar el layout Chen.

Correcto:

```txt
Chen conserva su layout.
Crow's Foot usa otro layout.
Al volver a Chen, las figuras siguen donde estaban.
```

## 6. Atributos en diferentes notaciones

### En Chen

Los atributos pueden ser figuras independientes.

```txt
Producto -- óvalo nombre
Producto -- óvalo código_barras
```

### En Crow's Foot

Los atributos suelen estar dentro de la caja de entidad.

```txt
Producto
---------
PK producto_id
nombre
codigo_barras
precio_actual
```

Por eso el mismo atributo semántico puede tener representación visual distinta.

## 7. Relaciones en diferentes notaciones

### En Chen

La relación es un nodo visual.

```txt
Producto -> rombo Pertenece -> Categoría
```

### En Crow's Foot

La relación puede convertirse en una línea con etiqueta opcional.

```txt
Categoría 1 ───< Producto
```

## 8. Cardinalidades

La cardinalidad semántica debe guardarse de manera neutral:

```json
{
  "sourceCardinality": "0..M",
  "targetCardinality": "1"
}
```

Luego cada notación decide:

```txt
Chen       -> mostrar texto 0..M / 1
Crow's Foot -> dibujar marcadores de cero, uno, muchos
```

## 9. MVP recomendado

Para no inflar el proyecto:

```txt
1. Mantener un solo modelo semántico.
2. Guardar campo activeNotation.
3. Mantener layouts separados por notación.
4. Evitar duplicar entidades o relaciones por vista.
```

## 10. Riesgo

Intentar implementar Chen y Crow's Foot al mismo tiempo puede retrasar el producto.

La decisión correcta es:

```txt
arquitectura preparada para varias notaciones;
implementación inicial enfocada en Chen.
```
