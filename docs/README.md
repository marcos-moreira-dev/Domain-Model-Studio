# Documentación de Domain Model Studio

Esta carpeta contiene solo documentación que explica capacidades, fronteras, contratos y validaciones vigentes del producto. La documentación histórica de tandas pasadas no se conserva por defecto: si no explica algo que existe hoy o una decisión todavía útil, se elimina.

## Lectura recomendada

1. `README.md` en la raíz: descripción pública del producto, flujo de trabajo y ejecución básica.
2. `scripts/README.md`: scripts vigentes para validar, ejecutar y empaquetar.
3. `docs/documentacion/MAPA_DOCUMENTACION_VIVA.md`: mapa corto de documentos que mandan.
4. `docs/documentacion/POLITICA_DOCUMENTAL_REPOSITORIO.md`: regla de conservación/eliminación documental.
5. `docs/desarrollo/refactor/PLAN_REFACTOR_SOLID.md`: plan de refactor integral con criterio de no tocar si no aporta valor.
6. `docs/producto/MATRIZ_CAPACIDADES_REALES.md`: capacidades reales del producto.
7. `docs/testeo/MATRIZ_CASOS_USO_Y_TESTS.md`: trazabilidad de casos de uso y pruebas.
8. `docs/tecnico/CONTRATO_IMPORTACION_MARKDOWN.md`: contrato general de importación Markdown.
9. `docs/tecnico/CONTRATO_MARKDOWN_LEVANTAMIENTO_LOGICO.md`: contrato canónico del Levantamiento lógico.
10. `docs/ia/RECURSOS_IA.md`: recursos exportables para trabajar con IA.

## Regla vigente

- Se conserva Markdown si explica una capacidad actual, una frontera arquitectónica, una guía operativa, un contrato técnico, una matriz de pruebas o una decisión todavía vigente.
- Se elimina Markdown si solo registra una tanda pasada, una hipótesis abandonada, un smoke sustituido o una bitácora que ya fue absorbida por documentación viva.
- No se archiva por defecto. El histórico solo se conserva si tiene valor concreto de auditoría, compatibilidad o diagnóstico.

## Estado post Tanda 27

La limpieza documental dejó el repositorio orientado a producto y refactor: menos bitácora, menos scripts, menos planes viejos y más foco en documentación que ayuda a ejecutar, validar, usar o extender la aplicación.
