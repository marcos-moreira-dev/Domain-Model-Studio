# Tanda 25 — Baseline técnico de refactor integral

## Objetivo

Preparar el refactor de toda la aplicación sin tocar comportamiento visible. Esta tanda fija criterios, métricas, zonas de riesgo y política documental antes de modificar shell, servicios, persistencia, parsers, catálogos o canvas.

## Cambios aplicados

- Se agrega `docs/desarrollo/refactor/BASELINE_REFACTOR_TANDA_025.md`.
- Se agrega `docs/documentacion/POLITICA_DOCUMENTAL_REPOSITORIO.md`.
- Se actualiza `docs/desarrollo/refactor/PLAN_REFACTOR_SOLID.md` para convertirlo en plan vigente post-Levantamiento lógico.
- Se actualiza `docs/desarrollo/refactor/MAPA_SEGURO_REFACTOR.md` con la regla de refactor cuidadoso.
- Se actualiza `docs/documentacion/MAPA_DOCUMENTACION_VIVA.md` con la política de no conservar historial obsoleto por defecto.
- Se agregan tests fuente de guardarraíl.

## Decisiones

1. No se refactoriza por tamaño; se refactoriza por beneficio verificable.
2. El modelo conceptual no queda congelado por dogma, pero requiere diagnóstico específico antes de tocarlo.
3. La persistencia `.dms`, parsers Markdown, recursos IA e importación de código fuente son zonas de alto riesgo.
4. Los Markdown históricos no se archivan por defecto: si no explican una capacidad vigente o una decisión activa, se eliminan en la limpieza documental.
5. La limpieza de scripts y documentación se ejecuta antes de refactors profundos de código.

## No se toca

- UX visible.
- SideDock.
- Toolbar.
- Parser/exporter Markdown.
- `.dms`.
- Recursos IA.
- Scripts físicos.
- Modelo conceptual.

## Tests

- `RefactorBaselineSourceTest`
- `RepositoryDocumentationPolicySourceTest`

## Próxima tanda

Tanda 26 — Limpieza controlada de scripts.
