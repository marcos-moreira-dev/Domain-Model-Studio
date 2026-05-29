package com.marcosmoreira.domainmodelstudio.presentation.behavior;

import com.marcosmoreira.domainmodelstudio.domain.behavior.SequenceCombinedFragmentSpec;
import com.marcosmoreira.domainmodelstudio.domain.behavior.SequenceFragmentOperand;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasBounds;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasAdapter;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasConnector;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasNode;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasRenderKit;
import java.util.Optional;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;

/** Renderizador temporal especializado para UML Secuencia sobre el canvas común. */
public final class SequenceRenderKit implements InteractiveCanvasRenderKit {

    private static final double ARROW_LENGTH = 12.0;
    private static final double ARROW_WIDTH = 7.0;

    @Override
    public Node renderNode(InteractiveCanvasNode node, CanvasBounds bounds, boolean selected) {
        if ("sequence-participant".equals(node.kind())) {
            return renderParticipant(node, bounds, selected);
        }
        if ("sequence-activation".equals(node.kind())) {
            return renderActivation(node, bounds, selected);
        }
        if ("sequence-fragment".equals(node.kind())) {
            return renderFragment(node, bounds, selected);
        }
        return renderNote(node, bounds, selected);
    }

    @Override
    public Node renderConnector(InteractiveCanvasConnector connector, InteractiveCanvasAdapter adapter, boolean selected) {
        Optional<NodeLayout> source = adapter.layoutForNode(connector.sourceNodeId());
        Optional<NodeLayout> target = adapter.layoutForNode(connector.targetNodeId());
        if (source.isEmpty() || target.isEmpty()) {
            return new Group();
        }
        double y = adapter instanceof SequenceCanvasAdapter sequenceAdapter
                ? sequenceAdapter.messageY(connector.id())
                : centerY(source.get());
        SequenceMessageGeometry.SequenceMessageRoute route = SequenceMessageGeometry.route(source.get(), target.get(), y);
        Polyline line = new Polyline();
        line.setStyle("-fx-fill: transparent;");
        for (SequenceMessageGeometry.Point point : route.points()) {
            line.getPoints().addAll(point.x(), point.y());
        }
        line.getStyleClass().add("sequence-message-line");
        line.getStyleClass().add("sequence-message-line-" + connector.kind());
        if (route.selfMessage()) {
            line.getStyleClass().add("sequence-message-self");
        }
        boolean returnMessage = SequenceMessageGeometry.returnMessage(connector.kind())
                || SequenceMessageGeometry.returnMessage(connector.label());
        boolean asyncMessage = SequenceMessageGeometry.asynchronousMessage(connector.kind())
                || SequenceMessageGeometry.asynchronousMessage(connector.label());
        if (returnMessage) {
            line.getStyleClass().add("sequence-message-return");
        }
        if (asyncMessage) {
            line.getStyleClass().add("sequence-message-async");
        }
        if (selected) {
            line.getStyleClass().add("sequence-message-line-selected");
        }
        Polygon arrow = arrowHead(route.arrowX(), route.arrowY(), route.arrowAngle());
        arrow.getStyleClass().add("sequence-message-arrow");
        if (returnMessage || asyncMessage) {
            arrow.getStyleClass().add("sequence-message-arrow-open");
        }
        if (selected) {
            arrow.getStyleClass().add("sequence-message-arrow-selected");
        }
        String visualLabel = adapter instanceof SequenceCanvasAdapter sequenceAdapter
                ? sequenceAdapter.messageLabel(connector.id(), connector.label())
                : connector.label();
        Label label = new Label(visualLabel);
        label.getStyleClass().add("sequence-message-label");
        label.setWrapText(true);
        label.setMinWidth(SequenceMessageLabelPlacement.widthFor(visualLabel));
        label.setPrefWidth(SequenceMessageLabelPlacement.widthFor(visualLabel));
        label.setMaxWidth(SequenceMessageLabelPlacement.widthFor(visualLabel));
        label.relocate(
                SequenceMessageLabelPlacement.xFor(route, visualLabel),
                SequenceMessageLabelPlacement.yFor(route));
        Group group = new Group(line, arrow, label);
        group.getStyleClass().add(route.selfMessage() ? "sequence-self-message-group" : "sequence-message-group");
        group.setUserData(connector.id());
        return group;
    }

    private Node renderParticipant(InteractiveCanvasNode node, CanvasBounds bounds, boolean selected) {
        Group group = new Group();
        StackPane header = new StackPane();
        header.getStyleClass().add("sequence-participant-header");
        if (selected) {
            header.getStyleClass().add("sequence-participant-selected");
        }
        Label title = new Label(node.title().isBlank() ? node.id() : node.title());
        title.getStyleClass().add("sequence-participant-title");
        title.setWrapText(true);
        title.setAlignment(Pos.CENTER);
        header.getChildren().add(title);
        header.setLayoutX(bounds.x());
        header.setLayoutY(bounds.y());
        header.setPrefSize(bounds.width(), SequenceCanvasAdapter.PARTICIPANT_HEIGHT);
        header.setMinSize(bounds.width(), SequenceCanvasAdapter.PARTICIPANT_HEIGHT);
        header.setMaxSize(bounds.width(), SequenceCanvasAdapter.PARTICIPANT_HEIGHT);

        Line lifeline = new Line(
                bounds.x() + bounds.width() / 2.0,
                bounds.y() + SequenceCanvasAdapter.PARTICIPANT_HEIGHT,
                bounds.x() + bounds.width() / 2.0,
                bounds.y() + bounds.height());
        lifeline.getStyleClass().add("sequence-lifeline");
        if (selected) {
            lifeline.getStyleClass().add("sequence-lifeline-selected");
        }
        group.getChildren().addAll(lifeline, header);
        group.setUserData(node.id());
        return group;
    }

    private Node renderActivation(InteractiveCanvasNode node, CanvasBounds bounds, boolean selected) {
        Rectangle activation = new Rectangle(bounds.x(), bounds.y(), bounds.width(), bounds.height());
        activation.getStyleClass().add("sequence-activation-box");
        if (selected) {
            activation.getStyleClass().add("sequence-activation-selected");
        }
        activation.setUserData(node.id());
        return activation;
    }

    private Node renderFragment(InteractiveCanvasNode node, CanvasBounds bounds, boolean selected) {
        SequenceCombinedFragmentSpec spec = SequenceCombinedFragmentSpec.fromText(node.title(), node.subtitle(), "");
        Pane fragment = new Pane();
        fragment.getStyleClass().add("sequence-fragment-box");
        if (selected) {
            fragment.getStyleClass().add("sequence-fragment-selected");
        }
        fragment.setLayoutX(bounds.x());
        fragment.setLayoutY(bounds.y());
        fragment.setPrefSize(bounds.width(), bounds.height());
        fragment.setMinSize(bounds.width(), bounds.height());
        fragment.setMaxSize(bounds.width(), bounds.height());

        Label tag = new Label(spec.kind().keyword());
        tag.getStyleClass().add("sequence-fragment-tag");
        tag.relocate(0, 0);

        Label title = new Label(fragmentTitle(node, spec));
        title.getStyleClass().add("sequence-fragment-title");
        title.setWrapText(true);
        title.setMaxWidth(Math.max(120.0, bounds.width() - 96.0));
        title.relocate(72.0, 6.0);

        Label guard = new Label(fragmentGuard(spec));
        guard.getStyleClass().add("sequence-fragment-detail");
        guard.setWrapText(true);
        guard.setMaxWidth(Math.max(120.0, bounds.width() - 24.0));
        guard.relocate(12.0, 34.0);

        fragment.getChildren().addAll(tag, title);
        if (!guard.getText().isBlank()) {
            fragment.getChildren().add(guard);
        }
        appendOperandSeparators(fragment, bounds, spec);
        fragment.setUserData(node.id());
        return fragment;
    }

    private Node renderNote(InteractiveCanvasNode node, CanvasBounds bounds, boolean selected) {
        VBox note = new VBox(4);
        note.getStyleClass().add("sequence-note-box");
        if (selected) {
            note.getStyleClass().add("sequence-note-selected");
        }
        note.setPadding(new Insets(7, 8, 7, 8));
        Label title = new Label(node.title().isBlank() ? node.id() : node.title());
        title.getStyleClass().add("sequence-note-title");
        title.setWrapText(true);
        note.getChildren().add(title);
        if (!node.subtitle().isBlank()) {
            Label detail = new Label(node.subtitle());
            detail.getStyleClass().add("sequence-note-detail");
            detail.setWrapText(true);
            note.getChildren().add(detail);
        }
        note.setLayoutX(bounds.x());
        note.setLayoutY(bounds.y());
        note.setPrefSize(bounds.width(), bounds.height());
        note.setMinSize(bounds.width(), bounds.height());
        note.setMaxSize(bounds.width(), bounds.height());
        note.setUserData(node.id());
        return note;
    }

    private static void appendOperandSeparators(Pane fragment, CanvasBounds bounds, SequenceCombinedFragmentSpec spec) {
        int operandCount = Math.max(1, spec.operands().size());
        double headerHeight = 62.0;
        double availableHeight = Math.max(48.0, bounds.height() - headerHeight);
        for (int index = 1; index < operandCount; index++) {
            double y = headerHeight + availableHeight * index / operandCount;
            Line separator = new Line(0.0, y, bounds.width(), y);
            separator.getStyleClass().add("sequence-fragment-operand-separator");
            fragment.getChildren().add(separator);
        }
        for (int index = 0; index < spec.operands().size(); index++) {
            SequenceFragmentOperand operand = spec.operands().get(index);
            String label = operand.canonicalLabel();
            if (label.isBlank()) {
                continue;
            }
            double y = headerHeight + availableHeight * index / operandCount + 8.0;
            Label operandLabel = new Label(label);
            operandLabel.getStyleClass().add("sequence-fragment-operand");
            operandLabel.setMaxWidth(Math.max(120.0, bounds.width() - 24.0));
            operandLabel.relocate(12.0, y);
            fragment.getChildren().add(operandLabel);
        }
    }

    private static String fragmentTitle(InteractiveCanvasNode node, SequenceCombinedFragmentSpec spec) {
        String title = spec.displayTitle();
        if (title.equals(spec.kind().displayName()) && !node.title().isBlank()) {
            return node.title();
        }
        return title;
    }

    private static String fragmentGuard(SequenceCombinedFragmentSpec spec) {
        String guard = spec.guardWithBrackets();
        if (!guard.isBlank()) {
            return guard;
        }
        if (!spec.reference().isBlank()) {
            return "ref: " + spec.reference();
        }
        if (spec.hasRange()) {
            return "rango: " + spec.rangeLabel();
        }
        return "";
    }

    private static Polygon arrowHead(double x, double y, double angle) {
        double backX = x - ARROW_LENGTH * Math.cos(angle);
        double backY = y - ARROW_LENGTH * Math.sin(angle);
        double normalX = Math.cos(angle + Math.PI / 2.0) * ARROW_WIDTH / 2.0;
        double normalY = Math.sin(angle + Math.PI / 2.0) * ARROW_WIDTH / 2.0;
        return new Polygon(
                x, y,
                backX + normalX, backY + normalY,
                backX - normalX, backY - normalY);
    }

    private static double centerX(NodeLayout layout) {
        return layout.x() + layout.width() / 2.0;
    }

    private static double centerY(NodeLayout layout) {
        return layout.y() + layout.height() / 2.0;
    }
}
