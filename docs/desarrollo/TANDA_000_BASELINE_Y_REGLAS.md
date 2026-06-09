# Tanda 0 — Línea base y reglas

## Objetivo
Dejar trazada la línea base de trabajo, la regla de protección del modelo conceptual y el orden general de ejecución por tandas.

## Reglas activas
- El **modelo conceptual** se considera **zona protegida** y no debe tocarse en estas tandas correctivas.
- Las correcciones inmediatas priorizan:
  1. Grafo libre.
  2. UML Clases.
  3. Auditoría de características prometidas.
  4. Refactorización masiva transversal.
  5. Limpieza de onboarding y documentación auxiliar.
  6. Javadocs con enfoque de estudio.

## Entorno observado
- Proyecto Maven Java.
- En este entorno no se cuenta con `mvn`, por lo que la validación completa debe correrse luego en Windows con los scripts del proyecto.
