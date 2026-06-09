# Tanda 27 — Limpieza documental viva

## Objetivo

Reducir deuda documental del repositorio y dejar como documentación principal solo aquello que explica capacidades, contratos, fronteras, pruebas o decisiones vigentes de Domain Model Studio.

## Criterio aplicado

- Se conserva Markdown si explica algo que existe hoy.
- Se elimina Markdown si solo registra una tanda pasada o una decisión sustituida.
- No se archiva por defecto; el histórico solo se conserva cuando aporta auditoría, compatibilidad o diagnóstico concreto.

## Cambios principales

1. `docs/estado/` queda reducido a `ESTADO_ACTUAL.md` como estado corto vigente.
2. Se eliminan carpetas históricas sin uso operativo directo:
   - `docs/historico/`
   - `docs/post_cierre/`
   - `docs/pendiente/`
3. Se eliminan tandas Markdown de desarrollo que ya no explicaban capacidades vigentes.
4. Se actualiza `docs/README.md` como entrada corta.
5. Se actualiza `docs/documentacion/MAPA_DOCUMENTACION_VIVA.md` con la lista de documentos que mandan.
6. Se actualiza `docs/documentacion/POLITICA_DOCUMENTAL_REPOSITORIO.md` con la regla de conservación/eliminación.
7. Se actualiza `docs/desarrollo/PLAN_TANDAS_ACTUAL.md` y `docs/raiz/PLAN_TANDAS_RESTANTES.md` para la línea de refactor vigente.

## Métrica resultante

La carpeta `docs/` queda 240 Markdown, frente a más de 900 antes de la limpieza. El repositorio completo queda 439 Markdown, incluyendo ejemplos, recursos IA y ayuda integrada.

## No incluido

No se modifica código productivo, scripts, recursos IA, ejemplos oficiales ni gramáticas Markdown. La limpieza se limita a documentación de repositorio y tests fuente asociados.
