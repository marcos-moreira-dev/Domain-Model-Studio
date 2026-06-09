# IMP-06 — Exportación SVG vectorial especializada

## Problema técnico

Antes de esta tanda, solo el modelo conceptual declaraba SVG. Los demás diagramas visuales exportaban PNG por snapshot y Markdown, pero no SVG.

Eso impedía cumplir el criterio:

```text
todo diagrama visual debe poder entregar una imagen editable/vectorial cuando la app lo promete
```

## Solución aplicada

Se agregó un exportador SVG genérico para documentos visuales especializados.

La solución no usa JavaFX ni snapshots. Construye texto SVG directamente desde:

```text
modelo semántico especializado + DiagramLayouts
```

## Capas

```text
SpecializedSvgModelFactory
```

Traduce documentos de dominio a nodos/conectores normalizados.

```text
SpecializedVisualSvgWriter
```

Convierte nodos/conectores + layout en SVG con primitivas vectoriales.

```text
SpecializedVisualSvgDiagramExporter
```

Orquesta `VisualLayoutService`, factory y writer.

```text
MultiNotationSvgDiagramExporter
```

Decide si usar exportador conceptual o exportador especializado.

## Regla de identidad visual

Los nodos/conectores usan los mismos IDs de layout creados en IMP-05:

```text
module:<id>
dependency:<id>
screen:<id>
transition:<id>
wireframe-screen:<id>
wireframe-component:<id>
uml-module:<id>
uml-class:<id>
uml-relation:<id>
behavior-node:<id>
behavior-edge:<id>
architecture-node:<id>
architecture-edge:<id>
role:<id>
permission:<id>
assignment:<id>
```

Esto mantiene trazabilidad entre:

```text
Markdown / modelo semántico / layout .dms / SVG exportado
```

## Decisiones anti-fachada

```text
- SVG especializado no incrusta PNG.
- PDF no se habilita para visuales porque no hay renderer PDF de diagramas todavía.
- DATA_DICTIONARY no usa exportador SVG; su salida real es Markdown/PDF.
- El SVG especializado es genérico por familia; los símbolos teóricos refinados quedan para tandas posteriores.
```

## Riesgos pendientes

```text
- UML secuencia necesita renderer especializado temporal más adelante.
- Wireframes aún se exportan como nodos vectoriales generales, no como reproducción exacta pixel-perfect de la pantalla.
- Los renderers JavaFX especializados todavía usan snapshots para PNG hasta migrarse al canvas común.
```
