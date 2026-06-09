# IMP-02 — Auditoría técnica CSS, tokens y estilo sobrio

Estado: **aplicada**

## Motivación

El proyecto necesitaba alinear la interfaz visual con la promesa de herramienta técnica de modelado, evitando:

```text
- fondos café/beige accidentales;
- mini-paletas por editor;
- archivos CSS grandes difíciles de mantener;
- tokens usados sin declaración central;
- estilos de dashboard web demasiado llamativos.
```

## Decisión técnica

`app-light.css` se mantiene como ensamblador. Los estilos se distribuyen por responsabilidad:

```text
tokens.css                 tokens base y aliases mínimos
diagram-palette.css        paleta semántica común para diagramas
identity-foundation.css    shell, menú, toolbar y paneles base
identity-canvas.css        canvas, grilla, árbol y selección
identity-inspector.css     inspector y campos
identity-conceptual-details.css  detalles Chen / pata de gallo
identity-welcome-status.css      statusbar, bienvenida clásica y tooltips
welcome-start-layout.css   pantalla de inicio: layout y títulos
welcome-start-actions.css  pantalla de inicio: acciones
welcome-start-cards.css    pantalla de inicio: tarjetas
editor-tabs.css            pestañas escrolleables de proyectos
```

## Regla de mantenimiento CSS

```text
Una hoja CSS nueva no debe crecer como archivo monolítico.
Si supera el tamaño razonable, dividir por responsabilidad visual.
```

Excepciones heredadas aceptadas por ahora:

```text
diagram-conceptual.css
manual.css
```

No se dividen en IMP-02 para no tocar la parte más madura del canvas conceptual ni el manual integrado sin una necesidad funcional directa.

## Tokens/aliases consolidados

```text
-dms-surface
-dms-panel
-dms-panel-bg
-dms-canvas
-app-workspace-bg
-dms-accent-border
-dms-help
-dms-node-fill
-dms-node-fill-soft
-dms-node-fill-accent
-dms-node-border
-dms-node-border-soft
-dms-node-selected-border
-dms-connector
-dms-label-bg
-dms-chen-entity
-dms-chen-attribute
-dms-chen-relationship
```

## Familias visuales tocadas

```text
module-map.css
behavior-diagram.css
architecture-diagram.css
uml-class.css
data-dictionary.css
wireframe.css
screen-flow.css
roles-permissions.css
```

El objetivo no fue rediseñar la app, sino reducir variación visual y preparar futuras migraciones al canvas común.

## Prueba de contrato

Se añadió `CssTokenCoverageTest` para impedir que nuevas tandas introduzcan tokens visuales fantasma o imports rotos.
