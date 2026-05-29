# Tanda 8 — Apertura segura del editor UML

## Objetivo

Evitar congelamiento al abrir la pestaña UML después de generar el modelo.

## Estrategia

Mostrar primero un estado temporal:

```text
Diagrama generado. Preparando vista light...
```

Luego abrir una vista pequeña o resumen, no la Mega vista.

## Resultado esperado

La UI no debe pasar de progreso a `No responde` al cerrar el diálogo de carga.
