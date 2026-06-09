package com.marcosmoreira.domainmodelstudio.presentation.freegraph;

/** Herramienta activa para la edición manual del Grafo libre desde el lienzo. */
public enum FreeGraphCanvasTool {
    SELECT("Seleccionar", "Selecciona, mueve y edita elementos existentes."),
    ADD_NODE("Agregar nodo", "Haz clic en un espacio vacío del lienzo para crear un nodo; vuelve a pulsar para regresar a selección."),
    ADD_EDGE("Agregar relación", "Haz clic en nodo origen y luego en nodo destino; puedes repetir el mismo nodo para una autorrelación.");

    private final String displayName;
    private final String helpText;

    FreeGraphCanvasTool(String displayName, String helpText) {
        this.displayName = displayName;
        this.helpText = helpText;
    }

    public String displayName() {
        return displayName;
    }

    public String helpText() {
        return helpText;
    }
}
