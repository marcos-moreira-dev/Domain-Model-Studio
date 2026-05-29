package com.marcosmoreira.domainmodelstudio.infrastructure.markdown;

import java.util.Arrays;
import java.util.Objects;

/**
 * Resultado común de separar un Markdown importable en frontmatter y cuerpo.
 *
 * <p>Varios parsers oficiales usaban copias locales de la misma rutina para leer
 * frontmatter YAML limitado y retirar ese encabezado antes de procesar secciones.
 * Esta pieza mantiene ese contrato en un solo lugar sin cambiar las gramáticas de
 * cada tipo de proyecto, sin cambiar ejemplos oficiales y sin cambiar comportamiento
 * visible.</p>
 *
 * <p>El resultado es deliberadamente pequeño: frontmatter ya parseado y cuerpo limpio.
 * Cada parser conserva su responsabilidad de interpretar secciones, tablas, IDs y
 * validaciones propias del tipo de proyecto.</p>
 */

final class MarkdownImportDocument {

    private final MarkdownFrontMatter frontMatter;
    private final String body;

    private MarkdownImportDocument(MarkdownFrontMatter frontMatter, String body) {
        this.frontMatter = Objects.requireNonNull(frontMatter, "frontMatter");
        this.body = Objects.requireNonNull(body, "body");
    }

    static MarkdownImportDocument parse(String markdownContent) {
        Objects.requireNonNull(markdownContent, "markdownContent");
        String[] lines = markdownContent.split("\\R", -1);
        if (lines.length == 0 || !"---".equals(lines[0].strip())) {
            return new MarkdownImportDocument(new MarkdownFrontMatter(), markdownContent);
        }
        MarkdownFrontMatter frontMatter = new MarkdownFrontMatter();
        for (int index = 1; index < lines.length; index++) {
            String line = lines[index].strip();
            if ("---".equals(line)) {
                return new MarkdownImportDocument(frontMatter, bodyAfter(lines, index));
            }
            if (MarkdownTextUtils.isPropertyLine(line)) {
                frontMatter.put(MarkdownTextUtils.keyBeforeColon(line), MarkdownTextUtils.valueAfterColon(line));
            }
        }
        return new MarkdownImportDocument(new MarkdownFrontMatter(), markdownContent);
    }

    MarkdownFrontMatter frontMatter() {
        return frontMatter;
    }

    String body() {
        return body;
    }

    private static String bodyAfter(String[] lines, int delimiterIndex) {
        if (delimiterIndex + 1 >= lines.length) {
            return "";
        }
        return String.join("\n", Arrays.copyOfRange(lines, delimiterIndex + 1, lines.length));
    }
}
