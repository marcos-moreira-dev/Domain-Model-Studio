package com.marcosmoreira.domainmodelstudio.presentation.screenflow;

import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.presentation.drawing.DiagramArrowKind;
import com.marcosmoreira.domainmodelstudio.presentation.drawing.DiagramConnectorStyle;
import com.marcosmoreira.domainmodelstudio.presentation.drawing.DiagramDrawingFacade;
import com.marcosmoreira.domainmodelstudio.presentation.drawing.DiagramShapeStyle;
import com.marcosmoreira.domainmodelstudio.presentation.drawing.admin.AdminShapeKit;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasBounds;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasConnectorGeometry;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasAdapter;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasConnector;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasNode;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasRenderKit;
import java.util.Optional;
import javafx.scene.Group;
import javafx.scene.Node;

/** Renderizador del flujo de pantallas usando primitivas transversales de diagrama. */
public final class ScreenFlowRenderKit implements InteractiveCanvasRenderKit {

    private final AdminShapeKit adminShapes = new AdminShapeKit();

    @Override
    public Node renderNode(InteractiveCanvasNode node, CanvasBounds bounds, boolean selected) {
        return renderNode(node, bounds, selected, DiagramDrawingFacade.defaults());
    }

    @Override
    public Node renderNode(
            InteractiveCanvasNode node,
            CanvasBounds bounds,
            boolean selected,
            DiagramDrawingFacade drawingFacade
    ) {
        DiagramDrawingFacade safeDrawingFacade = drawingFacade == null ? DiagramDrawingFacade.defaults() : drawingFacade;
        DiagramShapeStyle style = DiagramShapeStyle
                .node("screen-flow-diagram-node-" + node.kind(), selected)
                .withStyleClass("screen-flow-diagram-node");
        Group card = safeDrawingFacade.nodes().card(
                node.id(),
                bounds.x(),
                bounds.y(),
                bounds.width(),
                bounds.height(),
                node.title(),
                node.subtitle(),
                style,
                selected
        );
        Node symbol = adminShapes.screenSymbol();
        symbol.relocate(Math.max(12.0, bounds.width() - 46.0), 10.0);
        card.getChildren().add(symbol);
        card.getStyleClass().add("screen-flow-node-visual-group");
        card.getStyleClass().add("screen-flow-node-navigation-card");
        return card;
    }

    @Override
    public Node renderConnector(
            InteractiveCanvasConnector connector,
            InteractiveCanvasAdapter adapter,
            boolean selected
    ) {
        return renderConnector(connector, adapter, selected, DiagramDrawingFacade.defaults());
    }

    @Override
    public Node renderConnector(
            InteractiveCanvasConnector connector,
            InteractiveCanvasAdapter adapter,
            boolean selected,
            DiagramDrawingFacade drawingFacade
    ) {
        Optional<NodeLayout> sourceLayout = adapter.layoutForNode(connector.sourceNodeId());
        Optional<NodeLayout> targetLayout = adapter.layoutForNode(connector.targetNodeId());
        if (sourceLayout.isEmpty() || targetLayout.isEmpty()) {
            return new Group();
        }
        DiagramDrawingFacade safeDrawingFacade = drawingFacade == null ? DiagramDrawingFacade.defaults() : drawingFacade;
        var points = CanvasConnectorGeometry.edgeToEdgePoints(
                sourceLayout.get(),
                targetLayout.get(),
                adapter.layoutForConnector(connector.id()),
                safeDrawingFacade
        );
        DiagramConnectorStyle style = DiagramConnectorStyle
                .directed("screen-flow-diagram-connector-" + connector.kind(), selected)
                .withArrowKind(DiagramArrowKind.FILLED_TRIANGLE);
        Group group = safeDrawingFacade.connectors().polyline(points, "", style);
        group.getStyleClass().add("screen-flow-connector-visual-group");
        group.setUserData(connector.id());
        return group;
    }

}
