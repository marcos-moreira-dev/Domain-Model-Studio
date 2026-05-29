# Tanda 37B — Rebaseline final de tests post-refactor

Estado: aplicada.

## Objetivo

Cerrar los últimos fallos de la suite local después de la limpieza documental, la modularización de catálogos, el refactor de Markdown importable y la limpieza de recursos UI, sin cambiar código productivo ni comportamiento visible.

## Ajustes realizados

- Se refuerza la documentación de `MarkdownImportDocument` para declarar explícitamente que el refactor se hizo sin cambiar gramáticas, sin cambiar ejemplos oficiales y sin cambiar comportamiento visible.
- Se ajusta el contrato documental de artefactos compatibles legacy para indicar que dejan de formar parte de la presentación principal del Levantamiento lógico.
- Se corrige la evidencia JD-8 para mantener la frase `No modifica lógica funcional`.
- Se actualiza el plan vigente para conservar la referencia exacta a `ApplicationServices`.
- El README raíz vuelve a apuntar explícitamente a `docs/diagnostico/ESTADO_AUDITORIA_ACTUAL.md`.
- El test del ejemplo oficial UENS lee ahora la definición modular de análisis de negocio, porque la frase del Grafo lógico vive en la familia correspondiente y no en el factory central.

## Alcance

No cambia lógica funcional, UX visible, persistencia `.dms`, parsers/exporters Markdown, recursos IA, ejemplos oficiales ni scripts.
