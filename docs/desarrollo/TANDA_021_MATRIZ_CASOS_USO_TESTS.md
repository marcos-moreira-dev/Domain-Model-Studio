# Tanda 21 — Matriz de casos de uso y pruebas de aceptación

## Objetivo

Formalizar qué promete la aplicación por cada componente transversal y por cada tipo visible, y dejar una ruta de prueba asociada a cada promesa.

Esta tanda no agrega funcionalidades de producto. Agrega trazabilidad para saber qué debe probarse antes de afirmar que un tipo o acción está cerrado.

## Archivos agregados

- `docs/testeo/MATRIZ_CASOS_USO_Y_TESTS.md`
- `docs/testeo/PLAN_PRUEBAS_UI_E2E.md`
- `src/test/java/com/marcosmoreira/domainmodelstudio/productization/Tanda21AcceptanceTraceabilitySourceTest.java`

## Decisión tomada

La limpieza documental se desplaza. La nueva Tanda 21 queda dedicada a trazabilidad de pruebas, porque después de los hotfixes de Tanda 20 era más importante fijar qué casos de uso reales deben existir y cómo se validan.

## Criterio de éxito

- Todos los tipos visibles del catálogo aparecen en la matriz de aceptación.
- La matriz distingue test unitario, integración, contrato/fuente, UI/E2E y smoke manual.
- Las capacidades especiales de UML Clases quedan explícitas.
- Los formatos de exportación se tratan como promesas verificables.

## Validación local sugerida

```bat
scripts\02-ejecutar-tests.bat
scripts\06-medir-refactor.bat
```

