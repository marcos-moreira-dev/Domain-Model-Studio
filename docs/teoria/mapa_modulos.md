# Mapa de módulos

## Estado en la app

Ver estado funcional actualizado en `docs/estado/ESTADO_ACTUAL.md`.

## Propósito

Representar los módulos funcionales de una aplicación administrativa y sus relaciones principales.

## Cuándo usar

```txt
- al planificar alcance del sistema
- para separar entregas por fases
- para explicar al cliente qué tendrá la aplicación
- para conectar módulos con roles, datos y reportes
```

## Cuándo no usar

```txt
- para modelar clases de código
- para sustituir modelo conceptual
- para representar flujo detallado de procesos
```

## Elementos permitidos

```txt
Módulo
Submódulo
Función principal
Dependencia funcional
Reporte relacionado
Rol relacionado
Entidad relacionada
```

## Relaciones permitidas

```txt
Módulo contiene submódulo.
Módulo depende de módulo.
Módulo usa entidad.
Módulo genera reporte.
Rol accede a módulo.
```

## Reglas visuales

```txt
Usar cajas simples agrupadas por área funcional.
Evitar mezclar módulos con clases o tablas.
Mostrar dependencias solo cuando aporten decisión de diseño.
```

## Reglas semánticas

```txt
- Un módulo representa capacidad funcional del sistema.
- Un submódulo debe pertenecer a una operación reconocible.
- Debe evitarse inflar módulos por cada pantalla menor.
```

## Errores comunes

```txt
- crear un módulo por cada tabla
- confundir módulo con carpeta de código
- mezclar menú visual con arquitectura interna sin criterio
```

## Ejemplo mínimo

```txt
Ventas
  - Registrar venta
  - Consultar ventas
  - Anular venta
  - Reporte de ventas

Inventario
  - Productos
  - Stock
  - Movimientos
```

## Relación con aplicaciones administrativas

Sirve para planificar MVP, fases de entrega, menús, roles, pantallas y prioridades.
