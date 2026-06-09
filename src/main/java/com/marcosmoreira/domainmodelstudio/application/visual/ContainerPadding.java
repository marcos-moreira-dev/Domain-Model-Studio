package com.marcosmoreira.domainmodelstudio.application.visual;

/** Márgenes mínimos que un contenedor visual debe mantener alrededor de sus hijos. */
public record ContainerPadding(double left, double top, double right, double bottom) {

    public ContainerPadding {
        left = positive(left, "left");
        top = positive(top, "top");
        right = positive(right, "right");
        bottom = positive(bottom, "bottom");
    }

    public static ContainerPadding of(double horizontal, double vertical) {
        return new ContainerPadding(horizontal, vertical, horizontal, vertical);
    }

    public static ContainerPadding of(double left, double top, double right, double bottom) {
        return new ContainerPadding(left, top, right, bottom);
    }

    private static double positive(double value, String label) {
        if (!Double.isFinite(value) || value < 0.0) {
            throw new IllegalArgumentException("El padding " + label + " debe ser positivo y finito.");
        }
        return value;
    }
}
