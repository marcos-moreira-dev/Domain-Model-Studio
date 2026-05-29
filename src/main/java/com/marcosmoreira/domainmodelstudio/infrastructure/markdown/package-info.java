/**
 * Importación y exportación Markdown de los tipos de proyecto oficiales.
 *
 * <p>El Markdown es el puente humano/IA: debe ser legible, versionable e
 * importable cuando declara un {@code diagram_type} soportado. Los parsers de este
 * paquete convierten tablas y front matter en documentos de dominio; los exporters
 * generan una forma canónica que pueda reimportarse sin perder semántica esencial.</p>
 *
 * <p>{@code MarkdownImportDocument} centraliza la separación de frontmatter y cuerpo
 * sin cambiar las gramáticas de los tipos importables, sin cambiar ejemplos oficiales
 * y sin cambiar comportamiento visible. Cada parser conserva su contrato propio de
 * secciones, tablas e IDs.</p>
 *
 * <p>Tanda 38A — JavaDoc post-refactor: documentar aquí significa explicar la frontera
 * común de importación, no fusionar gramáticas ni convertir los parsers en una fachada.</p>
 */
package com.marcosmoreira.domainmodelstudio.infrastructure.markdown;
