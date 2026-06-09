package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import com.marcosmoreira.domainmodelstudio.application.visual.VisualElementLayoutIds;
import com.marcosmoreira.domainmodelstudio.presentation.drawing.DiagramDrawingFacade;
import java.util.Objects;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/** Decorador de render kit que dibuja notas visuales antes de delegar al render especializado. */
public final class VisualCommentRenderKit implements InteractiveCanvasRenderKit {

    public static final String NODE_KIND = "visual-comment";
    public static final String TITLE_ROLE = "visual-comment-title";
    public static final String DESCRIPTION_ROLE = "visual-comment-description";

    private static final CanvasNodeViewFactory NODE_VIEW_FACTORY = new CanvasNodeViewFactory();

    private final InteractiveCanvasRenderKit delegate;

    public VisualCommentRenderKit(InteractiveCanvasRenderKit delegate) {
        this.delegate = Objects.requireNonNull(delegate, "delegate");
    }

    @Override
    public Node renderNode(InteractiveCanvasNode node, CanvasBounds bounds, boolean selected) {
        if (isVisualComment(node)) {
            return renderComment(node, bounds, selected);
        }
        return delegate.renderNode(node, bounds, selected);
    }

    @Override
    public Node renderConnector(InteractiveCanvasConnector connector, InteractiveCanvasAdapter adapter, boolean selected) {
        return delegate.renderConnector(connector, adapter, selected);
    }

    @Override
    public Node renderNode(
            InteractiveCanvasNode node,
            CanvasBounds bounds,
            boolean selected,
            DiagramDrawingFacade drawingFacade
    ) {
        if (isVisualComment(node)) {
            return renderComment(node, bounds, selected);
        }
        return delegate.renderNode(node, bounds, selected, drawingFacade);
    }

    @Override
    public Node renderConnector(
            InteractiveCanvasConnector connector,
            InteractiveCanvasAdapter adapter,
            boolean selected,
            DiagramDrawingFacade drawingFacade
    ) {
        return delegate.renderConnector(connector, adapter, selected, drawingFacade);
    }

    private Node renderComment(InteractiveCanvasNode node, CanvasBounds bounds, boolean selected) {
        Label title = new Label(node.title());
        title.getStyleClass().add("canvas-visual-comment-title");
        title.getProperties().put("canvasInteractionRole", TITLE_ROLE);
        title.setWrapText(true);
        title.setMaxWidth(Double.MAX_VALUE);

        Label description = new Label(node.subtitle());
        description.getStyleClass().add("canvas-visual-comment-description");
        description.getProperties().put("canvasInteractionRole", DESCRIPTION_ROLE);
        description.setWrapText(true);
        description.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(description, Priority.ALWAYS);

        VBox content = new VBox(6.0, title, description);
        content.setPadding(new Insets(8.0, 10.0, 8.0, 10.0));
        content.getStyleClass().add("canvas-visual-comment-content");
        content.setPrefSize(bounds.width(), bounds.height());
        content.setMinSize(bounds.width(), bounds.height());
        content.setMaxSize(bounds.width(), bounds.height());

        return NODE_VIEW_FACTORY.wrap(
                node.id(),
                bounds,
                content,
                selected,
                "canvas-visual-comment",
                selected ? "canvas-visual-comment-selected" : ""
        );
    }

    private boolean isVisualComment(InteractiveCanvasNode node) {
        return node != null
                && (NODE_KIND.equals(node.kind()) || VisualElementLayoutIds.isVisualComment(node.id()));
    }
}
