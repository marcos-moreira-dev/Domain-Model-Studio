package com.marcosmoreira.domainmodelstudio.presentation.toolbar;

import java.util.Objects;

/** Acción declarada para la barra contextual de un tipo de diagrama. */
public record DiagramToolbarAction(
        DiagramToolbarActionId id,
        String text,
        String tooltip,
        ToolbarIcon icon,
        DiagramToolbarSection section,
        Width width
) {

    public DiagramToolbarAction {
        Objects.requireNonNull(id, "id");
        text = text == null ? "" : text.strip();
        tooltip = requireText(tooltip, "tooltip");
        Objects.requireNonNull(icon, "icon");
        Objects.requireNonNull(section, "section");
        width = width == null ? Width.NORMAL : width;
    }

    private static String requireText(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName);
        if (value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " no puede estar vacío.");
        }
        return value.strip();
    }

    public enum Width {
        TINY(40, 40, "toolbar-button-tiny"),
        SMALL(78, 86, "toolbar-button-small"),
        NORMAL(112, 128, "toolbar-button-normal"),
        WIDE(160, 178, "toolbar-button-wide");

        private final double minWidth;
        private final double prefWidth;
        private final String styleClass;

        Width(double minWidth, double prefWidth, String styleClass) {
            this.minWidth = minWidth;
            this.prefWidth = prefWidth;
            this.styleClass = styleClass;
        }

        public double minWidth() {
            return minWidth;
        }

        public double prefWidth() {
            return prefWidth;
        }

        public String styleClass() {
            return styleClass;
        }
    }
}
