# Modelo conceptual

## Estado en la app

Ver estado funcional actualizado en `docs/estado/ESTADO_ACTUAL.md`.

El modelo conceptual ya puede importarse desde Markdown con gramática y visualizarse como Chen o pata de gallo.

## Propósito

Representar los conceptos principales del dominio de negocio: entidades, atributos, relaciones y cardinalidades.

## Cuándo usar

```txt
- al iniciar el levantamiento de información
- antes de diseñar base de datos física
- antes de crear módulos de backend
- para conversar con el cliente sobre conceptos del negocio
- para descubrir reglas estructurales importantes
```

## Cuándo no usar

```txt
- para detallar sintaxis PostgreSQL
- para representar pantallas
- para modelar pasos temporales de un proceso
- para documentar clases internas de código
```

## Elementos permitidos

```txt
Entidad
Atributo
Relación
Cardinalidad
Participación
Identificador lógico
Atributo compuesto
Atributo derivado
Atributo multivaluado, si la notación lo permite
```

## Relaciones permitidas

```txt
Entidad — Relación — Entidad
Entidad — Atributo
Relación — Atributo, si el atributo pertenece a la relación
```

## Reglas visuales

Para Chen:

```txt
Entidad: rectángulo
Atributo: óvalo
Relación: rombo
Línea: asociación
```

Para pata de gallo:

```txt
Entidad: caja con nombre y campos
Relación: línea con cardinalidad en extremos
Cardinalidad: símbolo visual junto a cada extremo
```

## Reglas semánticas

```txt
- Chen y pata de gallo representan el mismo modelo base.
- No deben tratarse como proyectos separados.
- La cardinalidad debe conservar significado entre notaciones.
- Una entidad representa un concepto de negocio, no necesariamente una tabla física final.
```

## Errores comunes

```txt
- convertir cada tabla técnica en entidad conceptual
- confundir atributo con entidad
- crear relaciones sin verbo o intención de negocio
- asumir que una migración pequeña cambia siempre el modelo conceptual
- mezclar detalles físicos de PostgreSQL con modelo conceptual
```

## Ejemplo mínimo

```txt
Cliente realiza Pedido.
Pedido contiene DetallePedido.
DetallePedido referencia Producto.
```

## Relación con aplicaciones administrativas

Es el punto de partida más importante para sistemas con clientes, productos, ventas, inventario, citas, reportes, órdenes, tickets o procesos internos.
