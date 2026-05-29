package com.marcosmoreira.domainmodelstudio.application.importbatch;

/** Política de seguridad para importar una carpeta raíz de proyectos Markdown. */
public record MarkdownBatchImportPolicy(
        boolean recursive,
        boolean respectImportableFlag,
        boolean skipTemplatesByDefault,
        boolean strictSuffixMatch,
        int maxFiles
) {

    public MarkdownBatchImportPolicy {
        if (maxFiles <= 0) {
            throw new IllegalArgumentException("El límite de archivos Markdown debe ser mayor que cero.");
        }
    }

    public static MarkdownBatchImportPolicy defaultPolicy() {
        return recursiveProductionPolicy();
    }

    public static MarkdownBatchImportPolicy recursiveProductionPolicy() {
        return new MarkdownBatchImportPolicy(true, true, true, false, 500);
    }

    public static MarkdownBatchImportPolicy flatLegacyPolicy() {
        return new MarkdownBatchImportPolicy(false, true, true, false, 50);
    }
}
