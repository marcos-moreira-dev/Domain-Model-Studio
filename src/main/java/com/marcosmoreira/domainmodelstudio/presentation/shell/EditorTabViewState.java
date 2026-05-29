package com.marcosmoreira.domainmodelstudio.presentation.shell;

/** Estado mínimo de una pestaña visible en el área de edición. */
public record EditorTabViewState(
        String id,
        String title,
        boolean closeable,
        boolean home,
        boolean dirty
) {
    public EditorTabViewState {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El ID de pestaña no puede estar vacío");
        }
        title = title == null || title.isBlank() ? "Sin título" : title.trim();
    }
}
