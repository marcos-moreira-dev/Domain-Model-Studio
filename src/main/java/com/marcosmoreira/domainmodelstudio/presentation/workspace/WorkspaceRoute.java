package com.marcosmoreira.domainmodelstudio.presentation.workspace;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;

/**
 * Resultado pequeño e inmutable de resolver el workspace que corresponde al estado activo del shell.
 *
 * <p>Evita que la vista principal tenga que repetir listas de tipos para decidir si muestra lienzo,
 * paneles genéricos o una vista especializada. La ruta se calcula desde la pestaña/proyecto activo,
 * no desde una bandera global suelta.</p>
 */
public record WorkspaceRoute(
        DiagramTypeId diagramTypeId,
        WorkspaceKind workspaceKind,
        boolean placeholderActive
) {

    public boolean usesGenericConceptualSidePanels() {
        return false;
    }

    public boolean usesSpecializedWorkspace() {
        return !placeholderActive
                && workspaceKind != WorkspaceKind.WELCOME_HOME
                && workspaceKind != WorkspaceKind.PLACEHOLDER_GUIDE;
    }

    public boolean usesPlaceholderWorkspace() {
        return placeholderActive || workspaceKind == WorkspaceKind.PLACEHOLDER_GUIDE;
    }
}
