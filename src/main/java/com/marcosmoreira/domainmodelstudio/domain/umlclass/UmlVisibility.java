package com.marcosmoreira.domainmodelstudio.domain.umlclass;

/** Visibilidad UML de clases, miembros y operaciones. */
public enum UmlVisibility {
    PUBLIC("public", "+"),
    PROTECTED("protected", "#"),
    PACKAGE("package", "~"),
    PRIVATE("private", "-"),
    UNSPECIFIED("sin especificar", "");

    private final String displayName;
    private final String symbol;

    UmlVisibility(String displayName, String symbol) {
        this.displayName = displayName;
        this.symbol = symbol;
    }

    public String displayName() { return displayName; }
    public String symbol() { return symbol; }
}
