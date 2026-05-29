package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import com.marcosmoreira.domainmodelstudio.application.visual.UmlClassModuleContainerDescriptor;
import com.marcosmoreira.domainmodelstudio.application.visual.UmlClassModuleContainerPolicy;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassNode;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassMember;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlModuleGroup;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlMemberKind;
import com.marcosmoreira.domainmodelstudio.presentation.drawing.DiagramDrawingFacade;
import com.marcosmoreira.domainmodelstudio.presentation.drawing.DiagramConnectorStyle;
import com.marcosmoreira.domainmodelstudio.presentation.drawing.DiagramArrowKind;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasBounds;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasNodeViewFactory;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasConnectorGeometry;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasAdapter;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasConnector;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasNode;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasRenderKit;
import java.util.List;
import java.util.Optional;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.control.OverrunStyle;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

/** Renderizador sobrio de UML Clases sobre el lienzo común. */
public final class UmlClassRenderKit implements InteractiveCanvasRenderKit {

    private static final CanvasNodeViewFactory NODE_VIEW_FACTORY = new CanvasNodeViewFactory();

    private final UmlClassDiagramViewModel viewModel;
    private final UmlClassModuleContainerPolicy moduleContainerPolicy = new UmlClassModuleContainerPolicy();
    private final UmlClassDisplayLabelPolicy labelPolicy = new UmlClassDisplayLabelPolicy();

    public UmlClassRenderKit(UmlClassDiagramViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public Node renderNode(InteractiveCanvasNode node, CanvasBounds bounds, boolean selected) {
        if (node.kind().equals("uml-module")) {
            return renderModule(node, bounds, selected);
        }
        return renderClass(node, bounds, selected);
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
        DiagramConnectorStyle style = relationStyle(connector, selected);
        Group group = safeDrawingFacade.connectors().polyline(points, "", style);
        group.getStyleClass().add("uml-class-connector-visual-group");
        group.setUserData(connector.id());
        return group;
    }

    private Node renderModule(InteractiveCanvasNode node, CanvasBounds bounds, boolean selected) {
        VBox card = new VBox(6);
        card.setAlignment(Pos.TOP_LEFT);
        card.setPadding(new Insets(10, 12, 10, 12));
        card.getStyleClass().add("uml-class-canvas-module");
        card.getStyleClass().add("interactive-canvas-style-root");
        UmlModuleGroup semantic = semanticModule(node.id());
        UmlClassModuleContainerDescriptor descriptor = moduleDescriptor(semantic, node);
        card.getStyleClass().add(descriptor.roleStyleClass());
        if (selected) {
            card.getStyleClass().add("uml-class-canvas-node-selected");
        }
        String moduleTitle = semantic == null
                ? UmlClassDisplayLabelPolicy.truncate(descriptor.title(), 30)
                : labelPolicy.moduleTitle(semantic);
        Label title = fixedLabel(moduleTitle, bounds.width() - 24.0);
        title.setTooltip(new Tooltip(semantic == null ? descriptor.title() : labelPolicy.moduleTooltip(semantic)));
        title.getStyleClass().add("uml-class-canvas-module-title");
        Label count = fixedLabel(descriptor.classCountLabel(), bounds.width() - 24.0);
        count.getStyleClass().add("uml-class-canvas-module-count");
        card.getChildren().addAll(title, count);
        if (!descriptor.subtitle().isBlank()) {
            Label subtitle = fixedLabel(UmlClassDisplayLabelPolicy.truncate(descriptor.subtitle(), 44), bounds.width() - 24.0);
            subtitle.setTooltip(new Tooltip(descriptor.subtitle()));
            subtitle.setWrapText(true);
            subtitle.getStyleClass().add("uml-class-canvas-module-path");
            card.getChildren().add(subtitle);
        }
        return wrapUmlNode(node.id(), bounds, card, selected, "uml-class-canvas-module-wrapper");
    }

    private Node renderClass(InteractiveCanvasNode node, CanvasBounds bounds, boolean selected) {
        VBox card = new VBox(4);
        card.setAlignment(Pos.TOP_LEFT);
        card.setPadding(new Insets(8, 10, 8, 10));
        card.getStyleClass().add("uml-class-canvas-node");
        card.getStyleClass().add("interactive-canvas-style-root");
        card.getStyleClass().add("uml-class-canvas-node-" + node.kind());
        if (selected) {
            card.getStyleClass().add("uml-class-canvas-node-selected");
        }
        UmlClassNode semantic = semanticClass(node.id());
        Label stereotype = fixedLabel("«" + (semantic == null ? node.subtitle().split("\\R", 2)[0] : semantic.kind().displayName()) + "»", bounds.width() - 20.0);
        stereotype.getStyleClass().add("uml-class-canvas-stereotype");
        String visibleTitle = semantic == null ? node.title() : labelPolicy.classTitle(semantic);
        Label title = fixedLabel((semantic == null ? "" : semantic.visibility().symbol() + " ")
                + (visibleTitle.isBlank() ? node.id() : visibleTitle), bounds.width() - 20.0);
        if (semantic != null) {
            title.setTooltip(new Tooltip(labelPolicy.classTooltip(semantic)));
            card.setAccessibleText(labelPolicy.classTooltip(semantic));
        }
        title.getStyleClass().add("uml-class-canvas-node-title");
        card.getChildren().addAll(stereotype, title);
        if (semantic != null) {
            addMembers(card, "Atributos", semantic, UmlMemberKind.ATTRIBUTE, bounds.width() - 20.0);
            addMembers(card, "Métodos", semantic, UmlMemberKind.METHOD, bounds.width() - 20.0);
        } else if (!node.subtitle().isBlank()) {
            Label subtitle = fixedLabel(UmlClassDisplayLabelPolicy.truncate(node.subtitle(), 44), bounds.width() - 20.0);
            subtitle.setTooltip(new Tooltip(node.subtitle()));
            subtitle.setWrapText(true);
            subtitle.getStyleClass().add("uml-class-canvas-node-detail");
            card.getChildren().add(subtitle);
        }
        return wrapUmlNode(node.id(), bounds, card, selected, "uml-class-canvas-class-wrapper");
    }

    private void addMembers(VBox card, String title, UmlClassNode node, UmlMemberKind kind, double maxWidth) {
        Label header = fixedLabel(title, maxWidth);
        header.getStyleClass().add("uml-class-canvas-member-header");
        card.getChildren().add(header);
        UmlClassMemberRenderPolicy memberRenderPolicy = new UmlClassMemberRenderPolicy(viewModel.activeRenderProfile());
        List<UmlClassMember> members = node.members().stream().filter(member -> member.kind() == kind).toList();
        List<UmlClassMember> visibleMembers = memberRenderPolicy.visibleMembers(members);
        if (visibleMembers.isEmpty()) {
            Label empty = fixedLabel("—", maxWidth);
            empty.getStyleClass().add("uml-class-canvas-member-line");
            card.getChildren().add(empty);
            return;
        }
        for (var member : visibleMembers) {
            Label item = fixedLabel(labelPolicy.memberLine(member), maxWidth);
            item.setTooltip(new Tooltip(labelPolicy.memberTooltip(member)));
            item.getStyleClass().add("uml-class-canvas-member-line");
            card.getChildren().add(item);
        }
        int hiddenCount = memberRenderPolicy.hiddenCount(members);
        if (hiddenCount > 0) {
            Label overflow = fixedLabel("… " + hiddenCount + " más", maxWidth);
            overflow.getStyleClass().add("uml-class-canvas-member-line-muted");
            card.getChildren().add(overflow);
        }
    }

    private UmlModuleGroup semanticModule(String layoutId) {
        String id = layoutId == null || !layoutId.startsWith("uml-module:") ? "" : layoutId.substring("uml-module:".length());
        return viewModel.modules().stream().filter(module -> module.id().equals(id)).findFirst().orElse(null);
    }

    private UmlClassModuleContainerDescriptor moduleDescriptor(UmlModuleGroup module, InteractiveCanvasNode node) {
        if (module == null) {
            return new UmlClassModuleContainerDescriptor(node.id(), node.title(), "", node.subtitle(), "Agrupador UML", "");
        }
        return moduleContainerPolicy.describe(module, viewModel.classes());
    }

    private UmlClassNode semanticClass(String layoutId) {
        String id = layoutId == null || !layoutId.startsWith("uml-class:") ? "" : layoutId.substring("uml-class:".length());
        return viewModel.classes().stream().filter(node -> node.id().equals(id)).findFirst().orElse(null);
    }

    private Node wrapUmlNode(String id, CanvasBounds bounds, VBox card, boolean selected, String styleClass) {
        card.setPrefSize(bounds.width(), bounds.height());
        card.setMinSize(bounds.width(), bounds.height());
        card.setMaxSize(bounds.width(), bounds.height());
        card.setClip(new Rectangle(bounds.width(), bounds.height()));
        card.setUserData(id);
        return NODE_VIEW_FACTORY.wrap(id, bounds, card, selected, styleClass);
    }

    private static Label fixedLabel(String text, double maxWidth) {
        Label label = new Label(text == null ? "" : text);
        double width = Math.max(24.0, maxWidth);
        label.setMinWidth(0.0);
        label.setPrefWidth(width);
        label.setMaxWidth(width);
        label.setTextOverrun(OverrunStyle.ELLIPSIS);
        label.setWrapText(false);
        return label;
    }

    private static DiagramConnectorStyle relationStyle(InteractiveCanvasConnector connector, boolean selected) {
        String kind = connector.kind() == null ? "" : connector.kind();
        boolean dashed = kind.equals("uml-relation-dependency") || kind.equals("uml-relation-implementation");
        DiagramArrowKind arrowKind = switch (kind) {
            case "uml-relation-inheritance", "uml-relation-implementation" -> DiagramArrowKind.HOLLOW_TRIANGLE;
            case "uml-relation-composition" -> DiagramArrowKind.FILLED_DIAMOND;
            case "uml-relation-aggregation" -> DiagramArrowKind.HOLLOW_DIAMOND;
            case "uml-relation-dependency" -> DiagramArrowKind.OPEN;
            default -> DiagramArrowKind.NONE;
        };
        DiagramConnectorStyle style = DiagramConnectorStyle
                .directed("uml-class-canvas-connector-" + kind, selected)
                .withLineStyleClass("uml-class-canvas-connector")
                .withArrowStyleClass("uml-class-canvas-arrow-head")
                .withLabelStyleClass("uml-class-canvas-connector-label")
                .withArrowKind(arrowKind)
                .withDashed(dashed);
        if (selected) {
            style = style
                    .withLineStyleClass("uml-class-canvas-connector-selected")
                    .withArrowStyleClass("uml-class-canvas-arrow-head-selected");
        }
        return style;
    }
}
