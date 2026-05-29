package com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness;

import java.util.Objects;

/**
 * Nodo visible del árbol del expediente lógico.
 *
 * <p>El árbol no representa archivos ni permisos: representa la estructura
 * humana del levantamiento. Cada nodo conserva una selección unificada para
 * que el SideDock pueda navegar secciones, grupos, elementos, entidades,
 * atributos, relaciones, preguntas, artefactos compatibles y madurez sin crear estados
 * paralelos.</p>
 */
record LogicalBusinessTreeNode(
        String label,
        String marker,
        String detail,
        LogicalBusinessSelection selection,
        int count
) {

    LogicalBusinessTreeNode {
        label = clean(label, "Nodo");
        marker = clean(marker, "");
        detail = clean(detail, "");
        selection = Objects.requireNonNullElseGet(selection, LogicalBusinessSelection::none);
    }

    static LogicalBusinessTreeNode of(String label, String marker, LogicalBusinessSelection selection) {
        return new LogicalBusinessTreeNode(label, marker, "", selection, -1);
    }

    static LogicalBusinessTreeNode of(String label, String marker, String detail,
            LogicalBusinessSelection selection, int count) {
        return new LogicalBusinessTreeNode(label, marker, detail, selection, count);
    }

    String displayText() {
        StringBuilder text = new StringBuilder(label);
        if (count >= 0) {
            text.append("  ·  ").append(count);
        }
        return text.toString();
    }

    boolean selectionEquals(LogicalBusinessSelection other) {
        return selection.equals(other);
    }

    private static String clean(String value, String fallback) {
        if (value == null || value.isBlank()) {
            return fallback;
        }
        return value.strip();
    }
}
