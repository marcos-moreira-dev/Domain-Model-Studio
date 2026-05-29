package com.marcosmoreira.domainmodelstudio.infrastructure.svg;

import com.marcosmoreira.domainmodelstudio.application.export.SvgDiagramExporter;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramModel;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.er.AttributeElement;
import com.marcosmoreira.domainmodelstudio.domain.er.AttributeTag;
import com.marcosmoreira.domainmodelstudio.domain.er.EntityElement;
import com.marcosmoreira.domainmodelstudio.domain.er.EntityKind;
import com.marcosmoreira.domainmodelstudio.domain.er.RelationshipElement;
import com.marcosmoreira.domainmodelstudio.domain.er.RelationshipKind;
import com.marcosmoreira.domainmodelstudio.domain.layout.AnchorSide;
import com.marcosmoreira.domainmodelstudio.domain.layout.BendPoint;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.domain.notation.NotationType;
import com.marcosmoreira.domainmodelstudio.domain.style.ElementStyle;
import com.marcosmoreira.domainmodelstudio.domain.style.DiagramStyleResolver;
import com.marcosmoreira.domainmodelstudio.domain.style.StrokeStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Exportador SVG real para el perfil Chen del MVP. */
public final class ChenSvgDiagramExporter implements SvgDiagramExporter {

    private static final double MARGIN = 48.0;

    @Override
    public String export(DiagramProject project) {
        DiagramLayout layout = project.layouts()
                .layoutFor(NotationType.CHEN)
                .orElseGet(() -> DiagramLayout.empty(NotationType.CHEN));
        Bounds bounds = calculateBounds(layout);
        double width = Math.max(900.0, bounds.maxX() - bounds.minX() + MARGIN * 2.0);
        double height = Math.max(650.0, bounds.maxY() - bounds.minY() + MARGIN * 2.0);
        double offsetX = MARGIN - bounds.minX();
        double offsetY = MARGIN - bounds.minY();

        StringBuilder svg = new StringBuilder(64_000);
        svg.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        svg.append("<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"").append(format(width))
                .append("\" height=\"").append(format(height)).append("\" viewBox=\"0 0 ")
                .append(format(width)).append(' ').append(format(height)).append("\">\n");
        svg.append("  <metadata>").append(escape(project.metadata().title()))
                .append(" | generated-by=Domain Model Studio | notation=CHEN</metadata>\n");
        svg.append("  <rect x=\"0\" y=\"0\" width=\"100%\" height=\"100%\" fill=\"")
                .append(project.styleSheet().appearance().diagramBackground().toHex())
                .append("\"/>\n");
        svg.append("  <g id=\"connectors\">\n");
        for (ConnectorLayout connector : layout.connectors()) {
            renderConnector(svg, project, layout, connector, offsetX, offsetY);
        }
        svg.append("  </g>\n");
        svg.append("  <g id=\"nodes\">\n");
        for (NodeLayout node : layout.nodes()) {
            renderNode(svg, project, node, offsetX, offsetY);
        }
        svg.append("  </g>\n");
        svg.append("  <g id=\"cardinalities\">\n");
        renderCardinalities(svg, project.model(), layout, offsetX, offsetY);
        svg.append("  </g>\n");
        svg.append("</svg>\n");
        return svg.toString();
    }

    private void renderConnector(StringBuilder svg, DiagramProject project, DiagramLayout layout, ConnectorLayout connector, double offsetX, double offsetY) {
        if (!connector.visible()) {
            return;
        }
        Optional<NodeLayout> source = layout.nodeFor(connector.sourceElementId());
        Optional<NodeLayout> target = layout.nodeFor(connector.targetElementId());
        if (source.isEmpty() || target.isEmpty()) {
            return;
        }
        Point start = anchorPoint(source.get(), connector.sourceAnchor(), target.get());
        Point end = anchorPoint(target.get(), connector.targetAnchor(), source.get());
        List<Point> route = new ArrayList<>();
        route.add(start);
        for (BendPoint bendPoint : connector.bendPoints()) {
            route.add(new Point(bendPoint.x(), bendPoint.y()));
        }
        route.add(end);
        svg.append("    <polyline id=\"").append(escape(connector.connectorId().value())).append("\" points=\"");
        for (Point point : route) {
            svg.append(format(point.x() + offsetX)).append(',').append(format(point.y() + offsetY)).append(' ');
        }
        svg.append("\" fill=\"none\" ").append(connectorSvgStyle(project, connector.connectorId())).append("/>\n");
    }

    private String connectorSvgStyle(DiagramProject project, DiagramElementId connectorId) {
        StrokeStyle stroke = DiagramStyleResolver.resolvedStyleFor(project, connectorId).stroke();
        StringBuilder style = new StringBuilder();
        style.append("stroke=\"").append(stroke.color().toHex()).append("\" ")
                .append("stroke-width=\"").append(format(stroke.width())).append("\"");
        switch (stroke.pattern()) {
            case DASHED -> style.append(" stroke-dasharray=\"10 6\"");
            case DOTTED -> style.append(" stroke-dasharray=\"2 5\"");
            case SOLID -> { }
        }
        return style.toString();
    }

    private void renderNode(StringBuilder svg, DiagramProject project, NodeLayout node, double offsetX, double offsetY) {
        if (!node.visible()) {
            return;
        }
        DiagramModel model = project.model();
        Optional<EntityElement> entity = model.entityById(node.elementId());
        if (entity.isPresent()) {
            renderEntity(svg, project, entity.get(), node, offsetX, offsetY);
            return;
        }
        Optional<RelationshipElement> relationship = model.relationshipById(node.elementId());
        if (relationship.isPresent()) {
            renderRelationship(svg, project, relationship.get(), node, offsetX, offsetY);
            return;
        }
        Optional<AttributeElement> attribute = attributeById(model, node.elementId());
        attribute.ifPresent(value -> renderAttribute(svg, project, value, node, offsetX, offsetY));
    }

    private void renderEntity(StringBuilder svg, DiagramProject project, EntityElement entity, NodeLayout node, double offsetX, double offsetY) {
        ElementStyle style = DiagramStyleResolver.resolvedStyleFor(project, entity.id());
        double x = node.x() + offsetX;
        double y = node.y() + offsetY;
        if (entity.kind() == EntityKind.WEAK) {
            svg.append("    <rect x=\"").append(format(x - 4)).append("\" y=\"").append(format(y - 4))
                    .append("\" width=\"").append(format(node.width() + 8)).append("\" height=\"")
                    .append(format(node.height() + 8)).append("\" fill=\"none\" stroke=\"#606060\" stroke-width=\"1\"/>\n");
        }
        svg.append("    <rect id=\"").append(escape(entity.id().value())).append("\" x=\"").append(format(x))
                .append("\" y=\"").append(format(y)).append("\" width=\"").append(format(node.width()))
                .append("\" height=\"").append(format(node.height())).append("\" ")
                .append(shapeStyle(style)).append("/>\n");
        renderCenteredText(svg, entity.name(), entity.id(), style, centerX(node) + offsetX, centerY(node) + offsetY + 4, false);
    }

    private void renderAttribute(StringBuilder svg, DiagramProject project, AttributeElement attribute, NodeLayout node, double offsetX, double offsetY) {
        ElementStyle style = DiagramStyleResolver.resolvedStyleFor(project, attribute.id());
        double cx = centerX(node) + offsetX;
        double cy = centerY(node) + offsetY;
        if (attribute.hasTag(AttributeTag.MULTIVALUED)) {
            svg.append("    <ellipse cx=\"").append(format(cx)).append("\" cy=\"").append(format(cy))
                    .append("\" rx=\"").append(format(node.width() / 2.0 + 4)).append("\" ry=\"")
                    .append(format(node.height() / 2.0 + 4)).append("\" fill=\"none\" stroke=\"#606060\" stroke-width=\"1\"/>\n");
        }
        svg.append("    <ellipse id=\"").append(escape(attribute.id().value())).append("\" cx=\"").append(format(cx))
                .append("\" cy=\"").append(format(cy)).append("\" rx=\"").append(format(node.width() / 2.0))
                .append("\" ry=\"").append(format(node.height() / 2.0)).append("\" ")
                .append(shapeStyle(style));
        if (attribute.hasTag(AttributeTag.DERIVED)) {
            svg.append(" stroke-dasharray=\"5 4\"");
        }
        svg.append("/>\n");
        renderCenteredText(svg, attribute.name(), attribute.id(), style, cx, cy + 4, attribute.isKeyLike());
    }

    private void renderRelationship(StringBuilder svg, DiagramProject project, RelationshipElement relationship, NodeLayout node, double offsetX, double offsetY) {
        ElementStyle style = DiagramStyleResolver.resolvedStyleFor(project, relationship.id());
        double cx = centerX(node) + offsetX;
        double cy = centerY(node) + offsetY;
        if (relationship.kind() == RelationshipKind.IDENTIFYING) {
            appendDiamond(svg, relationship.id().value() + "_outer", cx, cy, node.width() / 2.0 + 5, node.height() / 2.0 + 5,
                    "fill=\"none\" stroke=\"#606060\" stroke-width=\"1\"");
        }
        appendDiamond(svg, relationship.id().value(), cx, cy, node.width() / 2.0, node.height() / 2.0, shapeStyle(style));
        renderCenteredText(svg, relationship.name(), relationship.id(), style, cx, cy + 4, false);
    }

    private void appendDiamond(StringBuilder svg, String id, double cx, double cy, double halfWidth, double halfHeight, String style) {
        svg.append("    <polygon id=\"").append(escape(id)).append("\" points=\"")
                .append(format(cx)).append(',').append(format(cy - halfHeight)).append(' ')
                .append(format(cx + halfWidth)).append(',').append(format(cy)).append(' ')
                .append(format(cx)).append(',').append(format(cy + halfHeight)).append(' ')
                .append(format(cx - halfWidth)).append(',').append(format(cy)).append("\" ")
                .append(style).append("/>\n");
    }

    private void renderCardinalities(StringBuilder svg, DiagramModel model, DiagramLayout layout, double offsetX, double offsetY) {
        for (RelationshipElement relationship : model.relationships()) {
            Optional<NodeLayout> relationshipNode = layout.nodeFor(relationship.id());
            Optional<NodeLayout> fromNode = layout.nodeFor(relationship.fromEntityId());
            Optional<NodeLayout> toNode = layout.nodeFor(relationship.toEntityId());
            if (relationshipNode.isEmpty()) {
                continue;
            }
            if (fromNode.isPresent()) {
                renderSmallText(svg, relationship.fromCardinality().displayText(),
                        midpointX(fromNode.get(), relationshipNode.get()) + offsetX,
                        midpointY(fromNode.get(), relationshipNode.get()) + offsetY - 18);
            }
            if (toNode.isPresent()) {
                renderSmallText(svg, relationship.toCardinality().displayText(),
                        midpointX(toNode.get(), relationshipNode.get()) + offsetX,
                        midpointY(toNode.get(), relationshipNode.get()) + offsetY - 18);
            }
        }
    }

    private void renderCenteredText(StringBuilder svg, String text, DiagramElementId id, ElementStyle style, double x, double y, boolean underline) {
        svg.append("    <text id=\"").append(escape(id.value())).append("_label\" x=\"").append(format(x))
                .append("\" y=\"").append(format(y)).append("\" text-anchor=\"middle\" font-family=\"")
                .append(escape(style.text().fontFamily())).append("\" font-size=\"").append(format(style.text().fontSize()))
                .append("\" fill=\"").append(style.text().color().toHex()).append("\"");
        if (underline) {
            svg.append(" text-decoration=\"underline\"");
        }
        svg.append(">").append(escape(text)).append("</text>\n");
    }

    private void renderSmallText(StringBuilder svg, String text, double x, double y) {
        svg.append("    <text x=\"").append(format(x)).append("\" y=\"").append(format(y))
                .append("\" text-anchor=\"middle\" font-family=\"Segoe UI\" font-size=\"12\" fill=\"#303030\">")
                .append(escape(text)).append("</text>\n");
    }

    private Optional<AttributeElement> attributeById(DiagramModel model, DiagramElementId attributeId) {
        for (EntityElement entity : model.entities()) {
            Optional<AttributeElement> attribute = entity.attributeById(attributeId);
            if (attribute.isPresent()) {
                return attribute;
            }
        }
        return Optional.empty();
    }

    private String shapeStyle(ElementStyle style) {
        return "fill=\"" + style.fill().color().toHex() + "\" stroke=\"" + style.stroke().color().toHex()
                + "\" stroke-width=\"" + format(style.stroke().width()) + "\"";
    }

    private Bounds calculateBounds(DiagramLayout layout) {
        if (layout.nodes().isEmpty()) {
            return new Bounds(0, 0, 900, 650);
        }
        double minX = Double.POSITIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;
        for (NodeLayout node : layout.nodes()) {
            minX = Math.min(minX, node.x());
            minY = Math.min(minY, node.y());
            maxX = Math.max(maxX, node.x() + node.width());
            maxY = Math.max(maxY, node.y() + node.height());
        }
        for (ConnectorLayout connector : layout.connectors()) {
            for (BendPoint bendPoint : connector.bendPoints()) {
                minX = Math.min(minX, bendPoint.x());
                minY = Math.min(minY, bendPoint.y());
                maxX = Math.max(maxX, bendPoint.x());
                maxY = Math.max(maxY, bendPoint.y());
            }
        }
        return new Bounds(minX, minY, maxX, maxY);
    }

    private Point anchorPoint(NodeLayout node, AnchorSide requestedSide, NodeLayout oppositeNode) {
        AnchorSide side = requestedSide == null || requestedSide == AnchorSide.AUTO ? inferSide(node, oppositeNode) : requestedSide;
        return switch (side) {
            case LEFT -> new Point(node.x(), centerY(node));
            case RIGHT -> new Point(node.x() + node.width(), centerY(node));
            case TOP -> new Point(centerX(node), node.y());
            case BOTTOM -> new Point(centerX(node), node.y() + node.height());
            case CENTER, AUTO -> new Point(centerX(node), centerY(node));
        };
    }

    private AnchorSide inferSide(NodeLayout node, NodeLayout oppositeNode) {
        double dx = centerX(oppositeNode) - centerX(node);
        double dy = centerY(oppositeNode) - centerY(node);
        if (Math.abs(dx) >= Math.abs(dy)) {
            return dx >= 0 ? AnchorSide.RIGHT : AnchorSide.LEFT;
        }
        return dy >= 0 ? AnchorSide.BOTTOM : AnchorSide.TOP;
    }

    private double centerX(NodeLayout node) {
        return node.x() + node.width() / 2.0;
    }

    private double centerY(NodeLayout node) {
        return node.y() + node.height() / 2.0;
    }

    private double midpointX(NodeLayout a, NodeLayout b) {
        return (centerX(a) + centerX(b)) / 2.0;
    }

    private double midpointY(NodeLayout a, NodeLayout b) {
        return (centerY(a) + centerY(b)) / 2.0;
    }

    private String format(double value) {
        if (Math.rint(value) == value) {
            return Long.toString(Math.round(value));
        }
        return String.format(java.util.Locale.ROOT, "%.2f", value);
    }

    private String escape(String raw) {
        return raw == null ? "" : raw
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }

    private record Point(double x, double y) { }
    private record Bounds(double minX, double minY, double maxX, double maxY) { }
}
