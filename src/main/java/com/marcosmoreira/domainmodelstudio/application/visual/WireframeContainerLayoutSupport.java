package com.marcosmoreira.domainmodelstudio.application.visual;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeComponent;
import java.util.List;
import java.util.Objects;

/** Ajustes de contención para pantallas y componentes de wireframes administrativos. */
public final class WireframeContainerLayoutSupport {

    private static final ContainerPadding SCREEN_PADDING = ContainerPadding.of(44.0, 86.0, 44.0, 52.0);

    private final ContainerAutoExpansionPolicy containerPolicy = new ContainerAutoExpansionPolicy();
    private final VisualLayoutService visualLayoutService = new VisualLayoutService();

    /**
     * Mueve una pantalla y desplaza sus componentes conservando sus posiciones relativas.
     *
     * <p>Esta operación no redimensiona automáticamente la pantalla: en wireframes la plantilla
     * propone un tamaño inicial, pero el tamaño manual definido por el usuario manda.</p>
     */
    public DiagramProject moveScreenWithComponents(
            DiagramProject project,
            String screenId,
            double x,
            double y,
            List<WireframeComponent> components
    ) {
        Objects.requireNonNull(project, "project");
        DiagramElementId screenLayoutId = VisualElementLayoutIds.wireframeScreen(screenId);
        NodeLayout current = visualLayoutService.nodeLayout(project, screenLayoutId)
                .orElseThrow(() -> new IllegalArgumentException("No existe layout para la pantalla: " + screenId));
        double deltaX = x - current.x();
        double deltaY = y - current.y();
        DiagramProject moved = visualLayoutService.moveNodeTo(project, screenLayoutId, x, y);
        return containerPolicy.moveNodesBy(moved, componentIds(screenId, components), deltaX, deltaY);
    }

    /**
     * Ajusta la pantalla al contenido únicamente cuando una acción explícita lo solicite.
     */
    public DiagramProject expandScreen(DiagramProject project, String screenId, List<WireframeComponent> components) {
        return containerPolicy.fitContainerToChildren(
                project,
                VisualElementLayoutIds.wireframeScreen(screenId),
                componentIds(screenId, components),
                SCREEN_PADDING,
                320.0,
                220.0);
    }

    private static List<DiagramElementId> componentIds(String screenId, List<WireframeComponent> components) {
        String normalizedScreenId = normalize(screenId);
        return components == null ? List.of() : components.stream()
                .filter(component -> component.screenId().equals(normalizedScreenId))
                .map(component -> VisualElementLayoutIds.wireframeComponent(component.id()))
                .toList();
    }

    private static String normalize(String value) {
        return value == null ? "" : value.strip();
    }
}
