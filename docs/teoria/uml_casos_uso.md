# UML Casos de uso

## Estado en la app

Ver estado funcional actualizado en `docs/estado/ESTADO_ACTUAL.md`.

## Propósito

Representar qué actores interactúan con el sistema y qué objetivos funcionales pueden cumplir.

## Cuándo usar

```txt
- para delimitar alcance funcional
- para discutir roles con el cliente
- para identificar módulos y permisos
- para ordenar requisitos funcionales
```

## Cuándo no usar

```txt
- para describir flujo detallado paso a paso
- para dibujar pantallas
- para modelar estructura de datos
- para listar botones internos de una interfaz
```

## Elementos permitidos

```txt
Actor
Caso de uso
Límite del sistema
Asociación
Include
Extend
Generalización, si realmente aplica
```

## Relaciones permitidas

```txt
Actor -- Caso de uso
Caso de uso ..> Caso de uso: include
Caso de uso ..> Caso de uso: extend
Actor -> Actor: generalización
Caso de uso -> Caso de uso: generalización
```

## Reglas visuales

```txt
Actor: figura o etiqueta externa
Caso de uso: óvalo
Límite del sistema: rectángulo contenedor
Asociación: línea simple
Include/extend: flecha punteada etiquetada
```

## Reglas semánticas

```txt
- Un caso de uso expresa un objetivo observable para un actor.
- Un actor no necesariamente es una persona; puede ser otro sistema.
- Include representa comportamiento obligatorio reutilizado.
- Extend representa comportamiento opcional o condicionado.
```

## Errores comunes

```txt
- poner CRUD completo como casos separados sin criterio
- confundir actor con rol interno de base de datos
- convertir cada botón en caso de uso
- usar include/extend por decoración sin necesidad real
```

## Ejemplo mínimo

```txt
Administrador -- Gestionar productos
Vendedor -- Registrar venta
Sistema externo -- Consultar stock
```

## Relación con aplicaciones administrativas

Ayuda a delimitar qué puede hacer cada rol antes de diseñar pantallas, permisos y endpoints.
