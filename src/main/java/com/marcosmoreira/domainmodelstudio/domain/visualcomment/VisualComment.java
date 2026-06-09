package com.marcosmoreira.domainmodelstudio.domain.visualcomment;

import java.util.Objects;

/** Nota visual libre colocada sobre un canvas sin participar en el modelo semantico. */
public record VisualComment(
        String id,
        String title,
        String description
) {

    public static final String DEFAULT_TITLE = "Sin titulo";
    public static final String DEFAULT_DESCRIPTION = "Sin descripcion";

    public VisualComment {
        id = requireText(id, "id");
        title = normalize(title);
        description = normalize(description);
    }

    public static VisualComment blank(String id) {
        return new VisualComment(id, DEFAULT_TITLE, "");
    }

    public VisualComment withTitle(String updatedTitle) {
        return new VisualComment(id, updatedTitle, description);
    }

    public VisualComment withDescription(String updatedDescription) {
        return new VisualComment(id, title, updatedDescription);
    }

    public String visibleTitle() {
        return title.isBlank() ? DEFAULT_TITLE : title;
    }

    public String visibleDescription() {
        return description.isBlank() ? DEFAULT_DESCRIPTION : description;
    }

    private static String requireText(String value, String label) {
        String normalized = normalize(value);
        if (normalized.isBlank()) {
            throw new IllegalArgumentException("El campo " + label + " de la nota visual no puede estar vacio");
        }
        return normalized;
    }

    private static String normalize(String value) {
        return Objects.toString(value, "").strip();
    }
}
