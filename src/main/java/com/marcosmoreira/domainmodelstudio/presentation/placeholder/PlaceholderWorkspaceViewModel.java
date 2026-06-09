package com.marcosmoreira.domainmodelstudio.presentation.placeholder;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCapability;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Datos visibles para una guía de preparación. */
public record PlaceholderWorkspaceViewModel(
        String diagramTypeId,
        String title,
        String category,
        String statusLabel,
        String message,
        List<PlaceholderAction> allowedActions
) {

    public PlaceholderWorkspaceViewModel {
        diagramTypeId = requireText(diagramTypeId, "diagramTypeId");
        title = requireText(title, "title");
        category = requireText(category, "category");
        statusLabel = requireText(statusLabel, "statusLabel");
        message = requireText(message, "message");
        allowedActions = List.copyOf(Objects.requireNonNull(allowedActions, "allowedActions"));
    }

    public static PlaceholderWorkspaceViewModel from(DiagramTypeDescriptor descriptor, String categoryName) {
        Objects.requireNonNull(descriptor, "descriptor");
        return new PlaceholderWorkspaceViewModel(
                descriptor.id().value(),
                descriptor.displayName(),
                categoryName == null || categoryName.isBlank() ? descriptor.categoryId().value() : categoryName.strip(),
                "En preparación",
                "Este tipo de proyecto conserva guía, teoría y plantillas para trabajar el alcance "
                        + "y preparar materiales de referencia del tipo seleccionado.",
                actionsFor(descriptor));
    }

    private static List<PlaceholderAction> actionsFor(DiagramTypeDescriptor descriptor) {
        List<PlaceholderAction> actions = new ArrayList<>();
        if (descriptor.supports(DiagramCapability.THEORY_HELP)) {
            actions.add(PlaceholderAction.SHOW_THEORY);
        }
        if (descriptor.supports(DiagramCapability.AI_RESOURCES)) {
            actions.add(PlaceholderAction.EXPORT_AI_RESOURCES);
        }
        actions.add(PlaceholderAction.BACK_TO_NEW_PROJECT);
        return actions;
    }

    private static String requireText(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName);
        if (value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " no puede estar vacío.");
        }
        return value.strip();
    }
}
