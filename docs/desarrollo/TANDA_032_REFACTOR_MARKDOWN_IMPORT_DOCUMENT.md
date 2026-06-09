# Tanda 32 — Refactor de parsers Markdown importables

Estado: aplicada.

## Objetivo

Centralizar la separación de frontmatter y cuerpo en una utilidad común para reducir duplicación activa entre parsers Markdown oficiales, sin cambiar gramáticas, ejemplos oficiales ni comportamiento visible.

Este refactor se aplicó sin cambiar ejemplos oficiales y sin cambiar comportamiento visible.

## Cambios realizados

- Se agrega `MarkdownImportDocument` en `infrastructure.markdown`.
- `MarkdownImportDocument` devuelve:
  - `MarkdownFrontMatter` leído desde el encabezado YAML limitado;
  - cuerpo Markdown sin frontmatter.
- Los parsers importables usan esta utilidad común:
  - conceptual legacy;
  - diccionario de datos;
  - mapa de módulos;
  - roles/permisos;
  - flujo de pantallas;
  - wireframes;
  - UML clases;
  - arquitectura;
  - comportamiento;
  - grafo libre;
  - grafo lógico del negocio;
  - dispatcher de importación.
- Se retiran copias locales de `readFrontMatter` y `stripFrontMatter`.

## Límites

No se cambiaron:

- gramáticas Markdown;
- frontmatter público;
- ejemplos oficiales;
- recursos IA;
- exporters canónicos;
- dispatcher funcional;
- tipos soportados;
- comportamiento visible.

## Pruebas y guardarraíles

Se agregan:

- `MarkdownImportDocumentTest`
- `MarkdownImportDocumentRefactorSourceTest`

Los tests validan que:

- el frontmatter inicial se separa correctamente;
- el cuerpo queda disponible para parsers especializados;
- documentos sin frontmatter mantienen el cuerpo original;
- frontmatter no cerrado no se interpreta como encabezado válido;
- los parsers importables no vuelven a duplicar `readFrontMatter`/`stripFrontMatter`.
