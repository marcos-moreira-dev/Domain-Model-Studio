package com.marcosmoreira.domainmodelstudio.domain.diagram;

import java.util.Objects;

/**
 * Identificador estable de un elemento del diagrama.
 *
 * <p>El ID no es el texto visible. El texto visible puede cambiar por edición humana,
 * pero el ID permite mantener trazabilidad entre Markdown, modelo interno, layout,
 * estilos, archivo {@code .dms} y exportaciones.</p>
 */
public record DiagramElementId(String value) {

    public DiagramElementId {
        value = requireValidValue(value);
    }

    public static DiagramElementId of(String rawValue) {
        return new DiagramElementId(rawValue);
    }

    private static String requireValidValue(String rawValue) {
        String normalized = Objects.requireNonNull(rawValue, "El ID no puede ser null").trim();
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("El ID no puede estar vacío");
        }
        if (normalized.chars().anyMatch(Character::isWhitespace)) {
            throw new IllegalArgumentException("El ID no debe contener espacios: " + normalized);
        }
        return normalized;
    }

    @Override
    public String toString() {
        return value;
    }
}
