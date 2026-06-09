package com.marcosmoreira.domainmodelstudio.presentation.workspace;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.Objects;

/**
 * Resuelve la ruta visual activa del shell desde el tipo de diagrama de la pestaña activa.
 *
 * <p>Esta clase no conoce JavaFX ni view models. Su única responsabilidad es convertir el estado
 * de producto activo en una familia de workspace trazable.</p>
 */
public final class WorkspaceRouteResolver {

    private final WorkspaceTypeRoutingPolicy routingPolicy;

    public WorkspaceRouteResolver() {
        this(new WorkspaceTypeRoutingPolicy());
    }

    public WorkspaceRouteResolver(WorkspaceTypeRoutingPolicy routingPolicy) {
        this.routingPolicy = Objects.requireNonNull(routingPolicy, "routingPolicy");
    }

    public WorkspaceRoute resolve(DiagramTypeId activeDiagramType, boolean placeholderActive) {
        return resolve(activeDiagramType, placeholderActive, activeDiagramType == null);
    }

    public WorkspaceRoute resolve(DiagramTypeId activeDiagramType, boolean placeholderActive, boolean homeTabActive) {
        if (placeholderActive) {
            return new WorkspaceRoute(activeDiagramType, WorkspaceKind.PLACEHOLDER_GUIDE, true);
        }
        if (homeTabActive || activeDiagramType == null) {
            return new WorkspaceRoute(null, WorkspaceKind.WELCOME_HOME, false);
        }
        return new WorkspaceRoute(activeDiagramType, routingPolicy.kindOf(activeDiagramType), false);
    }
}
