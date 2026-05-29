package com.marcosmoreira.domainmodelstudio.presentation.architecture;

import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.presentation.drawing.DiagramDrawingFacade;
import com.marcosmoreira.domainmodelstudio.presentation.drawing.c4.C4ShapeKit;
import com.marcosmoreira.domainmodelstudio.presentation.drawing.DiagramConnectorStyle;
import com.marcosmoreira.domainmodelstudio.presentation.drawing.DiagramArrowKind;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasBounds;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasConnectorGeometry;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasAdapter;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasConnector;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasNode;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasRenderKit;
import java.util.Optional;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

/** Renderizador sobrio para C4 Contexto, C4 Contenedores y Despliegue técnico. */
public final class ArchitectureRenderKit implements InteractiveCanvasRenderKit {

    private static final C4ShapeKit C4_SHAPES = new C4ShapeKit();

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
        if (ArchitectureCanvasSemantics.boundaryLike(node.kind())) {
            return renderZoneNode(node, bounds, selected);
        }
        VBox card = new VBox(5);
        card.setAlignment(Pos.TOP_LEFT);
        card.setPadding(new Insets(8, 10, 8, 10));
        card.getStyleClass().add("architecture-canvas-node");
        card.getStyleClass().add(ArchitectureCanvasSemantics.nodeFamilyClass(node.kind()));
        card.getStyleClass().add(ArchitectureCanvasSemantics.nodeRoleClass(node.kind()));
        card.getStyleClass().add("architecture-canvas-node-" + node.kind());
        if (selected) {
            card.getStyleClass().add("architecture-canvas-node-selected");
        }

        HBox heading = new HBox(7);
        heading.setAlignment(Pos.CENTER_LEFT);
        heading.getChildren().add(symbolFor(node.kind()));
        Label title = new Label(node.title().isBlank() ? node.id() : node.title());
        title.getStyleClass().add("architecture-canvas-node-title");
        title.setWrapText(true);
        title.setMinHeight(Region.USE_PREF_SIZE);
        title.setMaxWidth(Math.max(80.0, bounds.width() - 48.0));
        HBox.setHgrow(title, Priority.ALWAYS);
        heading.getChildren().add(title);
        card.getChildren().add(heading);

        if (!node.subtitle().isBlank()) {
            String[] lines = node.subtitle().split("\\R", 2);
            Label kind = new Label(lines[0]);
            kind.getStyleClass().add("architecture-canvas-node-kind");
            card.getChildren().add(kind);
            if (lines.length > 1 && !lines[1].isBlank()) {
                Label detail = new Label(lines[1]);
                detail.setWrapText(true);
                detail.setMinHeight(Region.USE_PREF_SIZE);
                detail.setMaxWidth(Math.max(80.0, bounds.width() - 22.0));
                detail.getStyleClass().add("architecture-canvas-node-detail");
                card.getChildren().add(detail);
            }
        }

        card.setLayoutX(bounds.x());
        card.setLayoutY(bounds.y());
        card.setPrefSize(bounds.width(), bounds.height());
        card.setMinSize(bounds.width(), bounds.height());
        card.setMaxSize(bounds.width(), bounds.height());
        card.setUserData(node.id());
        return card;
    }

    private Node renderZoneNode(InteractiveCanvasNode node, CanvasBounds bounds, boolean selected) {
        Group group = new Group();
        group.getStyleClass().add("architecture-canvas-zone-group");
        group.getStyleClass().add("interactive-canvas-handle-only-node");
        group.setPickOnBounds(false);

        Rectangle zone = new Rectangle(bounds.x(), bounds.y(), bounds.width(), bounds.height());
        zone.getStyleClass().add("architecture-canvas-zone");
        zone.getStyleClass().add("architecture-canvas-zone-" + node.kind());
        zone.setMouseTransparent(true);
        if (selected) {
            zone.getStyleClass().add("architecture-canvas-zone-selected");
        }

        String titleText = node.title().isBlank() ? node.id() : node.title();
        String detailText = firstLine(node.subtitle());
        double handleWidth = titleHandleWidth(bounds, titleText, detailText);
        double handleTextWidth = Math.max(90.0, handleWidth - 40.0);
        double handleHeight = titleHandleHeight(titleText, detailText, handleTextWidth);
        double handleX = bounds.x() + 10.0;
        double handleY = bounds.y() + 8.0;
        Rectangle titleHandle = new Rectangle(handleX, handleY, handleWidth, handleHeight);
        titleHandle.getStyleClass().add("architecture-canvas-zone-title-handle");
        titleHandle.getStyleClass().add("architecture-canvas-zone-title-handle-" + node.kind());
        titleHandle.setCursor(Cursor.MOVE);
        if (selected) {
            titleHandle.getStyleClass().add("architecture-canvas-zone-title-handle-selected");
        }

        Label title = new Label(titleText);
        title.getStyleClass().add("architecture-canvas-zone-title");
        title.setWrapText(true);
        title.setMinHeight(Region.USE_PREF_SIZE);
        title.setPrefWidth(handleTextWidth);
        title.setMaxWidth(handleTextWidth);
        title.setCursor(Cursor.MOVE);
        title.relocate(handleX + 8.0, handleY + 6.0);
        Label detail = new Label(detailText);
        detail.getStyleClass().add("architecture-canvas-zone-detail");
        detail.setWrapText(true);
        detail.setMinHeight(Region.USE_PREF_SIZE);
        detail.setPrefWidth(handleTextWidth);
        detail.setMaxWidth(handleTextWidth);
        detail.setCursor(Cursor.MOVE);
        detail.relocate(handleX + 8.0, handleY + 25.0 + extraTitleLineOffset(titleText, handleTextWidth));

        Label moveGlyph = new Label("✥");
        moveGlyph.getStyleClass().add("architecture-canvas-zone-move-glyph");
        moveGlyph.setCursor(Cursor.MOVE);
        moveGlyph.relocate(handleX + handleWidth - 21.0, handleY + handleHeight - 22.0);
        group.getChildren().addAll(zone, titleHandle, title, detail, moveGlyph);
        group.setUserData(node.id());
        return group;
    }

    private static double titleHandleWidth(CanvasBounds bounds, String title, String detail) {
        double estimatedTitle = Math.max(0, title == null ? 0 : title.length()) * 8.2;
        double estimatedDetail = Math.max(0, detail == null ? 0 : detail.length()) * 6.2;
        double preferred = Math.max(190.0, Math.max(estimatedTitle, estimatedDetail) + 58.0);
        double available = Math.max(120.0, bounds.width() - 20.0);
        return Math.max(120.0, Math.min(available, preferred));
    }

    private static double titleHandleHeight(String title, String detail, double textWidth) {
        int titleLines = estimatedLines(title, textWidth, 8.2);
        int detailLines = estimatedLines(detail, textWidth, 6.2);
        return Math.max(44.0, 16.0 + titleLines * 15.0 + detailLines * 12.0);
    }

    private static double extraTitleLineOffset(String title, double textWidth) {
        return Math.max(0, estimatedLines(title, textWidth, 8.2) - 1) * 15.0;
    }

    private static int estimatedLines(String value, double textWidth, double charWidth) {
        String normalized = value == null ? "" : value.strip();
        if (normalized.isBlank() || textWidth <= 0.0) {
            return 1;
        }
        return Math.max(1, (int) Math.ceil((normalized.length() * charWidth) / Math.max(80.0, textWidth)));
    }

    private static String firstLine(String value) {
        if (value == null || value.isBlank()) {
            return "Zona / límite";
        }
        return value.split("\\R", 2)[0];
    }

    @Override
    public Node renderConnector(InteractiveCanvasConnector connector, InteractiveCanvasAdapter adapter, boolean selected) {
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
                .directed("architecture-canvas-connector-" + connector.kind(), selected)
                .withLineStyleClass("architecture-canvas-connector")
                .withLineStyleClass(ArchitectureCanvasSemantics.connectorFamilyClass(connector.kind()))
                .withArrowStyleClass("architecture-canvas-arrow-head")
                .withLabelStyleClass("architecture-canvas-connector-label")
                .withArrowKind(DiagramArrowKind.FILLED_TRIANGLE);
        if (selected) {
            style = style
                    .withLineStyleClass("architecture-canvas-connector-selected")
                    .withArrowStyleClass("architecture-canvas-arrow-head-selected");
        }
        Group group = safeDrawingFacade.connectors().polyline(points, "", style);
        group.getStyleClass().add("architecture-connector-visual-group");
        group.setUserData(connector.id());
        return group;
    }

    private static Node symbolFor(String kind) {
        String normalized = kind == null ? "" : kind;
        return switch (normalized) {
            case "person" -> C4_SHAPES.personSymbol();
            case "client" -> C4_SHAPES.clientSymbol();
            case "software-system" -> C4_SHAPES.systemSymbol();
            case "external-system" -> C4_SHAPES.externalSystemSymbol();
            case "application" -> C4_SHAPES.applicationSymbol();
            case "api" -> C4_SHAPES.apiSymbol();
            case "database" -> C4_SHAPES.databaseSymbol();
            case "boundary" -> C4_SHAPES.boundarySymbol();
            case "environment" -> C4_SHAPES.environmentSymbol();
            case "network" -> C4_SHAPES.networkSymbol();
            case "server", "node" -> C4_SHAPES.serverSymbol();
            case "service", "external-service" -> C4_SHAPES.serviceSymbol();
            case "artifact" -> C4_SHAPES.artifactSymbol();
            case "container" -> C4_SHAPES.containerSymbol();
            default -> C4_SHAPES.systemSymbol();
        };
    }

}
