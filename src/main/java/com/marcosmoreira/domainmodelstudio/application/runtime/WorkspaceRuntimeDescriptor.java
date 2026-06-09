package com.marcosmoreira.domainmodelstudio.application.runtime;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramWorkspaceKind;
import java.util.Objects;

/** Descriptor runtime no-JavaFX del workspace esperado para un tipo de proyecto. */
public record WorkspaceRuntimeDescriptor(
        DiagramTypeId diagramTypeId,
        DiagramWorkspaceKind workspaceKind,
        String toolbarPolicyId
) {

    public WorkspaceRuntimeDescriptor {
        Objects.requireNonNull(diagramTypeId, "diagramTypeId");
        Objects.requireNonNull(workspaceKind, "workspaceKind");
        toolbarPolicyId = toolbarPolicyId == null ? "" : toolbarPolicyId.strip();
    }
}
