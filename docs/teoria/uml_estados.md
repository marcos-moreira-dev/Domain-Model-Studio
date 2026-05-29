# UML Estados

## Estado en la app

Ver estado funcional actualizado en `docs/estado/ESTADO_ACTUAL.md`.

## Propósito

Representar los estados posibles de una entidad o proceso y las transiciones válidas entre ellos.

## Cuándo usar

```txt
- cuando una entidad tiene ciclo de vida
- para órdenes, tickets, citas, garantías, pagos o reportes
- para evitar transiciones inválidas
- para diseñar reglas de negocio basadas en estado
```

## Cuándo no usar

```txt
- para entidades sin ciclo de vida relevante
- para modelar estructura de campos
- para dibujar pantallas
```

## Elementos permitidos

```txt
Estado inicial
Estado
Transición
Evento
Condición/guardia
Acción, si aplica
Estado final
```

## Relaciones permitidas

```txt
Estado inicial -> Estado
Estado -> Estado: evento [condición] / acción
Estado -> Estado final
```

## Reglas visuales

```txt
Estado: rectángulo redondeado
Transición: flecha
Estado inicial/final: círculo convencional o simplificado
```

## Reglas semánticas

```txt
- Las transiciones deben tener motivo o evento.
- No todo campo estado merece diagrama si no aporta valor.
- Las reglas deben evitar estados imposibles.
```

## Errores comunes

```txt
- confundir estado con rol
- crear estados decorativos sin reglas
- permitir saltos que el negocio no acepta
```

## Ejemplo mínimo

```txt
Pendiente -> En proceso -> Completado
Pendiente -> Cancelado
En proceso -> Cancelado, si todavía no fue cerrado
```

## Relación con aplicaciones administrativas

Clave para tickets, órdenes de trabajo, garantías, citas, reportes generados y solicitudes aprobables.
