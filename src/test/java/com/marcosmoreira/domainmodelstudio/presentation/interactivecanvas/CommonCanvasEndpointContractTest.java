package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.profile.StandardDiagramInteractionProfile;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class CommonCanvasEndpointContractTest {

    @Test
    void graphProfileDoesNotPromiseEndpointDraggingByDefault() {
        assertFalse(StandardDiagramInteractionProfile.GRAPH.supportsEndpointDragging());
    }

    @Test
    void endpointPortIsOptInInsteadOfDefaultPromise() {
        CanvasEndpointPort port = (connectorId, endpoint, targetNodeId) -> { };

        assertFalse(port.supportsEndpointDragging());
    }

    @Test
    void endpointDraggingRequiresProfileAndAdapterOptIn() {
        assertFalse(CanvasEndpointInteractionPolicy.isEndpointDraggingEnabled(
                StandardDiagramInteractionProfile.GRAPH,
                new MinimalAdapter()
        ));
        assertFalse(CanvasEndpointInteractionPolicy.isEndpointDraggingEnabled(
                StandardDiagramInteractionProfile.GRAPH,
                new EndpointAdapter(false)
        ));
        assertTrue(CanvasEndpointInteractionPolicy.isEndpointDraggingEnabled(
                EndpointProfile.INSTANCE,
                new EndpointAdapter(true)
        ));
    }

    private static class MinimalAdapter implements InteractiveCanvasAdapter {
        @Override public DiagramTypeId diagramTypeId() { return DiagramTypeId.FREE_GRAPH; }
        @Override public List<InteractiveCanvasNode> nodes() { return List.of(); }
        @Override public List<InteractiveCanvasConnector> connectors() { return List.of(); }
        @Override public Optional<NodeLayout> layoutForNode(String elementId) { return Optional.empty(); }
        @Override public Optional<ConnectorLayout> layoutForConnector(String connectorId) { return Optional.empty(); }
        @Override public InteractiveCanvasSelection selection() { return InteractiveCanvasSelection.empty(); }
        @Override public void selectNode(String elementId, boolean additive) { }
        @Override public void selectConnector(String connectorId, boolean additive) { }
        @Override public void selectNodesInside(CanvasBounds selectionBounds, boolean additive) { }
        @Override public void clearSelection() { }
        @Override public void moveNode(String elementId, double x, double y) { }
        @Override public void moveSelectedNodesBy(double deltaX, double deltaY) { }
        @Override public void addBendPoint(String connectorId, double x, double y) { }
        @Override public void moveBendPoint(String connectorId, int index, double x, double y) { }
        @Override public void selectBendPoint(String connectorId, int index) { }
        @Override public void removeSelectedBendPoint() { }
        @Override public void markDirty() { }
    }

    private static final class EndpointAdapter extends MinimalAdapter implements CanvasEndpointPort {
        private final boolean enabled;
        private EndpointAdapter(boolean enabled) { this.enabled = enabled; }
        @Override public void reconnectEndpoint(String connectorId, CanvasConnectorEndpoint endpoint, String targetNodeId) { }
        @Override public boolean supportsEndpointDragging() { return enabled; }
    }

    private enum EndpointProfile implements com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.profile.DiagramInteractionProfile {
        INSTANCE;
        @Override public String id() { return "endpoint-test"; }
        @Override public String displayName() { return "Endpoint test"; }
        @Override public String description() { return "Perfil de prueba con extremos editables."; }
        @Override public boolean supportsNodeDragging() { return true; }
        @Override public boolean supportsAreaSelection() { return true; }
        @Override public boolean supportsConnectorSelection() { return true; }
        @Override public boolean supportsBendPoints() { return true; }
        @Override public boolean supportsConnectorLabels() { return true; }
        @Override public boolean supportsEndpointDragging() { return true; }
        @Override public boolean supportsNodeResize() { return false; }
        @Override public boolean supportsLivePreview() { return true; }
        @Override public boolean supportsTemporalOrdering() { return false; }
        @Override public boolean supportsMatrixEditing() { return false; }
        @Override public boolean supportsDocumentEditing() { return false; }
    }
}
