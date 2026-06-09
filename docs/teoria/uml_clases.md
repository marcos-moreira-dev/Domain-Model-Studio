# UML Clases

## Estado en la app

Ver estado funcional actualizado en `docs/estado/ESTADO_ACTUAL.md`.

## Propósito

Representar estructura estática de clases, atributos, operaciones y relaciones de diseño técnico.

## Cuándo usar

```txt
- cuando ya se está diseñando software internamente
- para explicar componentes de dominio o servicios importantes
- para documentar partes técnicas estables
- para estudiar arquitectura y responsabilidades
```

## Cuándo no usar

```txt
- como primer documento para cliente no técnico
- para reemplazar modelo conceptual
- para dibujar tablas físicas directamente
- para documentar todo el código de forma excesiva
```

## Elementos permitidos

```txt
Clase
Atributo
Operación
Interfaz
Asociación
Agregación
Composición
Herencia
Dependencia
Multiplicidad
```

## Relaciones permitidas

```txt
Clase -- Clase
Clase -> Interfaz
Clase <|-- Clase
Clase *-- Clase
Clase o-- Clase
Clase ..> Clase
```

## Reglas visuales

```txt
Clase: rectángulo dividido en nombre, atributos y operaciones
Interfaz: clase marcada como interfaz o símbolo equivalente
Relación: línea/flecha según tipo
Multiplicidad: cerca de extremos
```

## Reglas semánticas

```txt
- UML clases modela diseño técnico, no necesariamente negocio puro.
- No debe forzar clases donde basta con modelo conceptual.
- Debe respetar responsabilidad única cuando se use para diseño de código.
```

## Errores comunes

```txt
- copiar tablas como clases sin pensar responsabilidades
- convertir todo en herencia
- documentar getters/setters triviales sin valor
- mezclar clase de UI, entidad y tabla sin diferenciar
```

## Ejemplo mínimo

```txt
OrdenService depende de OrdenRepository.
Orden contiene DetalleOrden.
DetalleOrden referencia Producto.
```

## Relación con aplicaciones administrativas

Útil en etapas técnicas para backend, servicios de dominio, validadores, reportes y separación de responsabilidades.
