# Tanda 3 — Modelo UML con miembros completos, pero render limitado

## Objetivo

Conservar todos los miembros detectados en el modelo, pero renderizar solo una cantidad limitada en el canvas.

## Regla

Una clase puede tener 100 atributos y 100 métodos, pero el canvas no debe pintar todo por defecto.

Ejemplo:

```text
ProductoService

Atributos
- repository: ProductoRepository
- mapper: ProductoMapper
...
+ 97 más

Métodos
+ crearProducto(...)
+ actualizarProducto(...)
...
+ 88 más
```

## Resultado esperado

El detalle completo permanece en el modelo y se muestra bajo demanda en el panel derecho o mediante acción contextual.
