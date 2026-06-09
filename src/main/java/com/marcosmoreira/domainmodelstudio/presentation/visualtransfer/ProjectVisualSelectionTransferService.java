package com.marcosmoreira.domainmodelstudio.presentation.visualtransfer;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasConnector;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasSelection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/** Fachada transversal para copiar/pegar selecciones de diagramas visuales DMS. */
public final class ProjectVisualSelectionTransferService {

    private ProjectVisualSelectionTransferService() {
    }

    public static boolean supports(DiagramTypeId type) {
        return DiagramTypeId.CONCEPTUAL_MODEL.equals(type)
                || DiagramTypeId.ADMIN_MODULE_MAP.equals(type)
                || DiagramTypeId.SCREEN_FLOW.equals(type)
                || DiagramTypeId.ADMIN_WIREFRAMES.equals(type)
                || DiagramTypeId.UML_CLASS.equals(type)
                || DiagramTypeId.UML_USE_CASE.equals(type)
                || DiagramTypeId.UML_ACTIVITY.equals(type)
                || DiagramTypeId.UML_SEQUENCE.equals(type)
                || DiagramTypeId.UML_STATE.equals(type)
                || DiagramTypeId.BPMN_BASIC.equals(type)
                || DiagramTypeId.OPERATIONAL_FLOW.equals(type)
                || DiagramTypeId.C4_CONTEXT.equals(type)
                || DiagramTypeId.C4_CONTAINERS.equals(type)
                || DiagramTypeId.TECHNICAL_DEPLOYMENT.equals(type);
    }

    public static boolean copySelectionToClipboard(
            DiagramProject project,
            DiagramTypeId type,
            InteractiveCanvasSelection selection,
            List<InteractiveCanvasConnector> connectors,
            Function<String, Optional<NodeLayout>> nodeLayoutResolver,
            Function<String, Optional<ConnectorLayout>> connectorLayoutResolver
    ) {
        Optional<ProjectVisualSelectionTransferPayload> payload = selectionPayload(
                project,
                type,
                selection,
                connectors,
                nodeLayoutResolver,
                connectorLayoutResolver);
        payload.ifPresent(VisualSelectionClipboard::copy);
        return payload.isPresent();
    }

    public static Optional<ProjectVisualSelectionTransferPayload> selectionPayload(
            DiagramProject project,
            DiagramTypeId type,
            InteractiveCanvasSelection selection,
            List<InteractiveCanvasConnector> connectors,
            Function<String, Optional<NodeLayout>> nodeLayoutResolver,
            Function<String, Optional<ConnectorLayout>> connectorLayoutResolver
    ) {
        if (project == null || type == null || selection == null || !supports(type)) {
            return Optional.empty();
        }
        Set<String> selectedNodes = new LinkedHashSet<>(selection.selectedNodeIds());
        Set<String> selectedConnectors = new LinkedHashSet<>(selection.selectedConnectorIds());
        List<InteractiveCanvasConnector> availableConnectors = connectors == null ? List.of() : connectors;
        for (InteractiveCanvasConnector connector : availableConnectors) {
            if (selectedConnectors.contains(connector.id())) {
                selectedNodes.add(connector.sourceNodeId());
                selectedNodes.add(connector.targetNodeId());
            }
        }
        for (InteractiveCanvasConnector connector : availableConnectors) {
            if (selectedNodes.contains(connector.sourceNodeId()) && selectedNodes.contains(connector.targetNodeId())) {
                selectedConnectors.add(connector.id());
            }
        }
        if (selectedNodes.isEmpty() && selectedConnectors.isEmpty()) {
            return Optional.empty();
        }
        List<NodeLayout> nodeLayouts = selectedNodes.stream()
                .map(id -> nodeLayoutResolver.apply(id))
                .flatMap(Optional::stream)
                .toList();
        List<ConnectorLayout> connectorLayouts = selectedConnectors.stream()
                .map(id -> connectorLayoutResolver.apply(id))
                .flatMap(Optional::stream)
                .toList();
        return Optional.of(new ProjectVisualSelectionTransferPayload(
                type,
                project.metadata().title(),
                project,
                selectedNodes,
                selectedConnectors,
                nodeLayouts,
                connectorLayouts));
    }

    public static Optional<PasteResult> pasteSelectionFromClipboard(DiagramProject targetProject) {
        Optional<VisualSelectionTransferPayload> current = VisualSelectionClipboard.current();
        if (current.isEmpty() || !(current.orElseThrow() instanceof ProjectVisualSelectionTransferPayload payload)) {
            return Optional.empty();
        }
        return paste(targetProject, payload);
    }

    public static Optional<PasteResult> paste(DiagramProject targetProject, ProjectVisualSelectionTransferPayload payload) {
        return ProjectVisualSelectionPasteRouter.paste(targetProject, payload);
    }

    public record PasteResult(DiagramProject project, int nodeCount, int connectorCount, String message) {
        static PasteResult empty(DiagramProject project) {
            return new PasteResult(project, 0, 0, "No se pegó selección visual.");
        }
    }
}
