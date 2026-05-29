package com.marcosmoreira.domainmodelstudio.application.workspace;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeDescriptor;
import java.util.Objects;

/** Decisión de apertura para que presentación elija la vista correcta. */
public record CreateWorkspaceResult(
        DiagramTypeDescriptor diagramType,
        WorkspaceSupportDecision decision,
        String userMessage
) {

    public CreateWorkspaceResult {
        Objects.requireNonNull(diagramType, "diagramType");
        Objects.requireNonNull(decision, "decision");
        userMessage = userMessage == null ? "" : userMessage.strip();
    }
}
