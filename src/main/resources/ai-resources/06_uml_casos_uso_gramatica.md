# Gramática IA — UML Casos de uso

Estado: recurso de apoyo para IA y ejemplos oficiales.  
Importable por la app: sí.  
Editor visual implementado: sí, mediante el editor común de comportamiento.  
Uso recomendado: identificar actores y funcionalidades observables del sistema.

## Advertencia

Domain Model Studio importa y renderiza una versión básica para levantamiento de requerimientos. Este formato debe respetar la teoría UML básica y no reemplaza una herramienta UML avanzada.


## Estructura sugerida

```md
# UML Casos de uso: <nombre del sistema>

## Límite del sistema
- Sistema: <nombre>
- Alcance: <qué incluye>
- Fuera de alcance: <qué no incluye>

## Actores
### Actor: <nombre>
- Tipo: primario | secundario | sistema externo
- Descripción: <rol frente al sistema>

## Casos de uso
### Caso de uso: <nombre con verbo>
- Actor principal: <actor>
- Objetivo: <resultado observable>
- Precondiciones: <condiciones>
- Resultado esperado: <resultado>

## Relaciones
- <Actor> -> <Caso de uso>
- <Caso A> -> <Caso B>: include
- <Caso A> -> <Caso B>: extend
```

## Reglas

- Un caso de uso describe una meta del actor, no una pantalla.
- Usar `include` solo cuando un comportamiento siempre ocurre y escribirlo como `<Caso A> -> <Caso B>: include`.
- Usar `extend` solo para comportamiento opcional o condicional y escribirlo como `<Caso A> -> <Caso B>: extend`.
- No mezclar casos de uso con flujo detallado de pasos internos.
