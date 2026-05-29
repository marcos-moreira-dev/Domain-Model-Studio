# Tanda 035 — Refactor focalizado del canvas conceptual legacy

## Alcance

Esta tanda interviene el modelo conceptual clásico con criterio conservador. No migra el canvas a `interactivecanvas`, no cambia UX visible, no altera persistencia `.dms`, no modifica Markdown ni toca los renderers Chen/Crow's Foot.

El objetivo es reducir responsabilidad interna en `DiagramCanvasViewModel` sin forzar una abstracción transversal prematura.

## Cambios aplicados

1. Se agrega `ConceptualCanvasEditHistory` para encapsular las pilas de deshacer/rehacer del canvas conceptual.
2. Se agrega `ConceptualAnchorResolver` para extraer la política geométrica de selección de anclas de conectores.
3. `DiagramCanvasViewModel` deja de almacenar directamente `undoStack`/`redoStack` y delega en `ConceptualCanvasEditHistory`.
4. `DiagramCanvasViewModel` deja de tener la matemática privada `nearestAnchor`/`squaredDistance` y delega en `ConceptualAnchorResolver`.

## Fronteras preservadas

- No se toca `DiagramCanvasView`.
- No se toca `InspectorViewModel`.
- No se toca `InspectorView`.
- No se toca `ChenDiagramRenderer` ni `CrowsFootDiagramRenderer`.
- No se toca dominio ER.
- No se cambia el contrato de selección, movimiento, undo/redo ni edición de conectores.

## Métrica focalizada

- `DiagramCanvasViewModel.java`: aproximadamente 916 líneas antes del refactor integral; queda en unas 866 líneas.
- Nuevas clases pequeñas: `ConceptualCanvasEditHistory` y `ConceptualAnchorResolver`, ambas de responsabilidad única.

## Pruebas agregadas

- `ConceptualCanvasEditHistoryTest`
- `ConceptualAnchorResolverTest`
- `ConceptualLegacyCanvasRefactorSourceTest`

## Validación local requerida

Ejecutar:

```bat
scripts\02-ejecutar-tests.bat
```

Y revisar manualmente:

1. crear modelo conceptual;
2. agregar entidad;
3. agregar relación;
4. mover elementos;
5. mover bendpoints y etiquetas;
6. ajustar extremos de relación;
7. deshacer/rehacer;
8. guardar/abrir `.dms`;
9. exportar PNG/SVG/Markdown.

## Decisión

El modelo conceptual legacy queda parcialmente refactorizado, pero sigue siendo zona sensible. Cualquier migración mayor hacia canvas transversal requiere diagnóstico propio y smoke visual específico.
