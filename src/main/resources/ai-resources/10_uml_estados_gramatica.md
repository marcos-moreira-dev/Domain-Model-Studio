# Gramática IA — UML Estados

Estado: recurso de apoyo para IA y ejemplos oficiales.  
Importable por la app: sí.  
Editor visual implementado: sí, mediante el editor común de comportamiento.  
Uso recomendado: describir ciclos de vida de entidades, órdenes, tickets, pagos o procesos.

## Advertencia

Domain Model Studio importa y renderiza una versión básica de estados. Usar para planificación de reglas de transición.


## Estructura sugerida

```md
# UML Estados: <entidad o proceso>

## Objeto modelado
- Nombre: <entidad/proceso>
- Propósito: <por qué tiene estados>

## Estados
### Estado: <nombre>
- Descripción: <significado>
- Es inicial: sí | no
- Es final: sí | no

## Transiciones
- <Estado A> -> <Estado B>
  - Evento: <qué provoca el cambio>
  - Condición: <regla si aplica>
  - Acción: <efecto si aplica>
```

## Reglas

- Cada estado debe representar una condición estable y reconocible.
- Cada transición debe tener evento o condición clara.
- No usar estados para describir pantallas.
