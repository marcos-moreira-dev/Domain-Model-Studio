# Auditoría JavaDoc JD-3 — Infraestructura de importación, persistencia y exportación

## Alcance

La tanda JD-3 documenta contratos existentes de infraestructura sin cambiar comportamiento funcional.
El objetivo es que la capa sea útil para estudiar cómo un diseño por capas traduce dominio y aplicación hacia formatos concretos.

## Paquetes revisados

```txt
infrastructure/markdown
infrastructure/json
infrastructure/svg
infrastructure/resources
infrastructure/pdf
```

## Contratos reforzados

```txt
- Markdown oficial: front matter, diagram_type, parsers especializados y roundtrip.
- .dms JSON: formato durable, versión, documentos especializados, layout, estilos y assets.
- SVG: salida vectorial reproducible, no captura accidental de pantalla.
- PDF: salida formal de lectura para diccionario de datos.
- Recursos IA: gramáticas, prompts, plantillas y ejemplos exportables con importabilidad explícita.
```

## Clases con JavaDoc pedagógico reforzado

```txt
DiagramMarkdownImportDispatcher
LogicalBusinessGraphMarkdownParser
LogicalBusinessGraphMarkdownExporter
DmsProjectJsonReader
DmsProjectJsonWriter
DmsProjectFileRepository
SpecializedVisualSvgDiagramExporter
ClasspathAiResourceCatalog
ClasspathAiResourceExporter
DataDictionaryPdfExporter
```

## Decisiones de documentación

```txt
- No se documentaron getters/setters triviales.
- No se cambió lógica de parsers, writers ni exporters.
- Se reforzó la distinción entre formato editable (.dms/Markdown) y salida de publicación (SVG/PDF).
- Se explicó que errores de versión, payload o estructura son fallas explícitas, no autocorrecciones silenciosas.
```

## Resultado esperado

Después de JD-3, un lector puede seguir el flujo:

```txt
Markdown IA/oficial → dispatcher → parser especializado → dominio
Dominio/proyecto → writer JSON → .dms → reader JSON → proyecto reabierto
Proyecto visual → layout → SVG especializado
Diccionario → PDF formal
Recursos IA → carpeta exportada + índice
```
