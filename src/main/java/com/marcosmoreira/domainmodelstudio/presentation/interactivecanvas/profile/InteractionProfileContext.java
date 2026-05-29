package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.profile;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramWorkspaceKind;
import java.util.Objects;

/**
 * Contexto liviano para que tabs, toolbars, sidebars y exportaciones consulten
 * una misma decisión de interacción sin depender de un estado global fijo.
 */
public record InteractionProfileContext(
        DiagramTypeId diagramTypeId,
        DiagramWorkspaceKind workspaceKind,
        DiagramInteractionProfile profile
) {

    public InteractionProfileContext {
        Objects.requireNonNull(profile, "El perfil de interacción no puede ser null");
    }

    public static InteractionProfileContext from(DiagramTypeId diagramTypeId, DiagramWorkspaceKind workspaceKind) {
        return new InteractionProfileContext(
                diagramTypeId,
                workspaceKind,
                DiagramInteractionProfileResolver.resolve(diagramTypeId, workspaceKind)
        );
    }

    public boolean isFreeCanvas() {
        return !profile.supportsDocumentEditing() && !profile.supportsMatrixEditing();
    }

    public boolean isStructuredWorkspace() {
        return profile.supportsDocumentEditing() || profile.supportsMatrixEditing();
    }
}
