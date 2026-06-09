package com.marcosmoreira.domainmodelstudio.presentation.modulemap;

import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
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
import javafx.scene.text.Text;

/** Renderizador del mapa de módulos usando primitivas transversales de diagrama. */
public final class ModuleMapRenderKit implements InteractiveCanvasRenderKit {

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
                .node("module-map-diagram-node-" + node.kind(), selected)
                .withStyleClass("module-map-diagram-node");
        Group card = isRootModule(node)
                ? renderRootModule(node, bounds, selected, safeDrawingFacade, style)
                : renderChildModule(node, bounds, selected, safeDrawingFacade, style);
        card.getStyleClass().add("module-map-node-visual-group");
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
        if (isContainmentConnector(connector)) {
            Group invisibleContainment = new Group();
            invisibleContainment.setMouseTransparent(true);
            invisibleContainment.setUserData(connector.id());
            return invisibleContainment;
        }
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
        DiagramConnectorStyle style = connectorStyle(connector, selected);
        Group group = safeDrawingFacade.connectors().polyline(points, "", style);
        group.getStyleClass().add("module-map-connector-visual-group");
        group.setUserData(connector.id());
        return group;
    }

    private Group renderRootModule(
            InteractiveCanvasNode node,
            CanvasBounds bounds,
            boolean selected,
            DiagramDrawingFacade drawingFacade,
            DiagramShapeStyle style
    ) {
        Group group = drawingFacade.nodes().packageBox(
                node.id(),
                bounds.x(),
                bounds.y(),
                bounds.width(),
                bounds.height(),
                node.title(),
                style,
                selected
        );
        addSubtitle(group, node.subtitle(), 16.0, 36.0, bounds.width());
        Node symbol = adminShapes.moduleSymbol();
        symbol.relocate(Math.max(16.0, bounds.width() - 48.0), 12.0);
        group.getChildren().add(symbol);
        group.getStyleClass().add("module-map-root-container-group");
        return group;
    }

    private Group renderChildModule(
            InteractiveCanvasNode node,
            CanvasBounds bounds,
            boolean selected,
            DiagramDrawingFacade drawingFacade,
            DiagramShapeStyle style
    ) {
        Group card = drawingFacade.nodes().card(
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
        Node symbol = adminShapes.moduleSymbol();
        symbol.relocate(Math.max(12.0, bounds.width() - 44.0), 10.0);
        card.getChildren().add(symbol);
        return card;
    }

    private static void addSubtitle(Group group, String subtitle, double x, double y, double width) {
        if (subtitle == null || subtitle.isBlank()) {
            return;
        }
        Text text = new Text(clamp(firstLine(subtitle), 64));
        text.getStyleClass().add("diagram-node-subtitle");
        text.setWrappingWidth(Math.max(20.0, width - x - 18.0));
        text.relocate(x, y);
        group.getChildren().add(text);
    }

    private static boolean isRootModule(InteractiveCanvasNode node) {
        return "module-root".equals(node.kind());
    }

    private static String firstLine(String value) {
        int lineBreak = value.indexOf('\n');
        return lineBreak < 0 ? value.strip() : value.substring(0, lineBreak).strip();
    }

    private static String clamp(String value, int maxLength) {
        if (value == null || value.length() <= maxLength) {
            return value == null ? "" : value;
        }
        return value.substring(0, Math.max(0, maxLength - 1)) + "…";
    }

    private static boolean isContainmentConnector(InteractiveCanvasConnector connector) {
        return "module-containment".equals(connector.kind());
    }

    private static DiagramConnectorStyle connectorStyle(InteractiveCanvasConnector connector, boolean selected) {
        boolean containment = isContainmentConnector(connector);
        DiagramConnectorStyle style = DiagramConnectorStyle
                .directed("module-map-diagram-connector-" + connector.kind(), selected)
                .withArrowKind(containment ? DiagramArrowKind.HOLLOW_TRIANGLE : DiagramArrowKind.FILLED_TRIANGLE)
                .withDashed(containment);
        return containment
                ? style.withLineStyleClass("module-map-diagram-connector-containment")
                : style.withLineStyleClass("module-map-diagram-connector-dependency");
    }

}
