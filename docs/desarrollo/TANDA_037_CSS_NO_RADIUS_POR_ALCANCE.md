# Tanda 037 — CSS y recursos UI post-refactor

## Objetivo

Cerrar una tanda controlada de limpieza visual después del refactor global sin cambiar UX, layouts ni semántica de diagramas.

La regla aplicada es: **mantener CSS vivo, recursos UI verificables y sin bordes redondeados ornamentales en superficies nuevas o corregidas**.

## Alcance aplicado

Esta tanda conserva los contratos de no-radius por alcance ya vigentes y agrega limpieza de superficie CSS/recursos:

- eliminación de placeholders CSS documentales que ya no se importaban;
- actualización del README de CSS para listar solo hojas vivas;
- guardarraíl para que `app-light.css` no vuelva a depender de hojas obsoletas;
- guardarraíl para que los iconos declarados por la toolbar existan como PNG internos.

## CSS eliminado

Se eliminan como archivos físicos porque no contenían estilos vivos:

- `identity-polish.css`;
- `welcome-start.css`.

La identidad visual vigente vive en:

- `identity-foundation.css`;
- `identity-canvas.css`;
- `identity-inspector.css`;
- `identity-conceptual-details.css`;
- `identity-welcome-status.css`.

El inicio tipo IDE vive en:

- `welcome-start-layout.css`;
- `welcome-start-actions.css`;
- `welcome-start-cards.css`;
- `editor-tabs.css`.

## No-radius por alcance

Se mantiene la regla anterior: la UI nueva o corregida no debe introducir radius ornamental en botones, paneles, tabs, listas, tablas, labels, headers exportables ni contenedores rectangulares.

## Exclusiones deliberadas

No se tocan óvalos UML, eventos BPMN, estados iniciales/finales ni entidades Chen cuando la forma pertenece a la teoría del diagrama.

Tampoco se interviene la pantalla de inicio congelada ni el modelo conceptual legacy como limpieza global.

## Recursos UI

La toolbar usa iconos PNG internos declarados en `ToolbarIcon`. La Tanda 37 agrega un guardarraíl para detectar iconos declarados sin archivo físico en `src/main/resources/icons/`.

No se introduce dependencia de SVG runtime ni fuentes de glifos para la toolbar.

## Validación esperada

Ejecutar:

```bat
scripts\02-ejecutar-tests.bat
```

Pruebas agregadas o reforzadas:

- `CssResourceSurfaceCleanupSourceTest`;
- `ToolbarIconResourceCoverageSourceTest`;
- `CssNoRadiusScopedSourceTest`;
- `CssCascadeBudgetSourceTest`.

## Smoke visual sugerido

Abrir al menos:

1. Arquitectura/despliegue.
2. UML Clases.
3. Comportamiento/UML Secuencia.
4. Mapa de módulos.
5. Flujo de pantallas.
6. Wireframes.
7. Roles/permisos.
8. Grafo libre.
9. Levantamiento lógico.

Verificar que la app conserve estética técnica de escritorio, que las pestañas del SideDock sigan legibles y que las formas semánticas de las notaciones sigan siendo reconocibles.
