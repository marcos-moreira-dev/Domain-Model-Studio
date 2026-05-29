package com.marcosmoreira.domainmodelstudio.presentation.exportable;

/** Tipo de salida final que el proyecto activo presenta al usuario. */
public enum ExportableOutputKind {
    VISUAL_DIAGRAM("Diagrama visual"),
    MATRIX("Matriz"),
    DOCUMENT("Documento");

    private final String displayName;

    ExportableOutputKind(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }
}
