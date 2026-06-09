package com.marcosmoreira.domainmodelstudio.application.visual;

/** Acción discreta para ampliar o reducir una figura seleccionada del lienzo. */
public enum VisualNodeSizeCommand {
    GROW(1.12, "agrandada"),
    SHRINK(0.90, "reducida");

    private final double factor;
    private final String visibleResult;

    VisualNodeSizeCommand(double factor, String visibleResult) {
        this.factor = factor;
        this.visibleResult = visibleResult;
    }

    public double factor() {
        return factor;
    }

    public String visibleResult() {
        return visibleResult;
    }
}
