package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.style.ElementStyle;
import java.util.Objects;
import java.util.Optional;
import java.util.function.UnaryOperator;

/** Implementación base de estilos persistentes sobre la hoja de estilos del proyecto activo. */
public interface CanvasProjectStylePort extends CanvasStylePort {

    DiagramProject currentStyleProject();

    void patchStyleProject(UnaryOperator<DiagramProject> patch, String statusMessage);

    @Override
    default Optional<ElementStyle> explicitStyleForElement(String elementId) {
        DiagramProject project = currentStyleProject();
        if (project == null || clean(elementId).isBlank()) {
            return Optional.empty();
        }
        return project.styleSheet().explicitStyleFor(DiagramElementId.of(clean(elementId)));
    }

    @Override
    default ElementStyle resolvedStyleForElement(String elementId) {
        DiagramProject project = currentStyleProject();
        if (project == null || clean(elementId).isBlank()) {
            return ElementStyle.defaultElement();
        }
        return project.styleSheet().resolvedStyleFor(DiagramElementId.of(clean(elementId)));
    }

    @Override
    default void applyElementStyle(String elementId, ElementStyle style) {
        String normalized = clean(elementId);
        if (normalized.isBlank()) {
            return;
        }
        Objects.requireNonNull(style, "style");
        patchStyleProject(
                project -> project.withStyleSheet(project.styleSheet().withElementStyle(DiagramElementId.of(normalized), style)),
                "Apariencia aplicada al elemento seleccionado.");
    }

    @Override
    default void resetElementStyle(String elementId) {
        String normalized = clean(elementId);
        if (normalized.isBlank()) {
            return;
        }
        patchStyleProject(
                project -> project.withStyleSheet(project.styleSheet().withoutElementStyle(DiagramElementId.of(normalized))),
                "Apariencia restablecida para el elemento seleccionado.");
    }

    private static String clean(String value) {
        return value == null ? "" : value.strip();
    }
}
