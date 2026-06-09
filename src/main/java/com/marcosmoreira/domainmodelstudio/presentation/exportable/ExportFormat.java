package com.marcosmoreira.domainmodelstudio.presentation.exportable;

/** Formatos de salida que una vista final puede ofrecer al usuario. */
public enum ExportFormat {
    SVG("SVG", "svg"),
    PNG("PNG", "png"),
    PDF("PDF", "pdf"),
    MARKDOWN("Markdown", "md");

    private final String displayName;
    private final String defaultExtension;

    ExportFormat(String displayName, String defaultExtension) {
        this.displayName = displayName;
        this.defaultExtension = defaultExtension;
    }

    public String displayName() {
        return displayName;
    }

    public String defaultExtension() {
        return defaultExtension;
    }
}
