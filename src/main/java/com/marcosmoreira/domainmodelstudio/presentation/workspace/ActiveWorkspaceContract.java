package com.marcosmoreira.domainmodelstudio.presentation.workspace;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;

/**
 * Contrato mínimo que deben cumplir los adaptadores de workspace.
 *
 * <p>Define un punto de extensión pequeño para que el montaje de workspaces
 * no siga creciendo como condicionales grandes dentro de MainShellView.</p>
 */
public interface ActiveWorkspaceContract {

    DiagramTypeId diagramTypeId();

    WorkspaceKind workspaceKind();

    default boolean supportsGenericSidePanels() {
        return workspaceKind() == WorkspaceKind.CONCEPTUAL_CANVAS;
    }
}
