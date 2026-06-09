package com.marcosmoreira.domainmodelstudio.infrastructure.svg;

import com.marcosmoreira.domainmodelstudio.application.export.SvgDiagramExporter;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.er.AttributeElement;
import com.marcosmoreira.domainmodelstudio.domain.er.EntityElement;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorMarker;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.domain.notation.NotationType;
import com.marcosmoreira.domainmodelstudio.domain.style.ElementStyle;
import com.marcosmoreira.domainmodelstudio.domain.style.DiagramStyleResolver;
import com.marcosmoreira.domainmodelstudio.domain.style.StrokeStyle;
import com.marcosmoreira.domainmodelstudio.infrastructure.svg.rolespermissions.RolesPermissionsMatrixSvgExporter;
import com.marcosmoreira.domainmodelstudio.infrastructure.svg.specialized.SpecializedVisualSvgDiagramExporter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Exportador SVG que respeta la notación activa del proyecto.
 *
 * <p>Chen conserva el exportador vectorial especializado existente. Crow's Foot usa una
 * salida compacta inicial, suficiente para que el selector de notación no quede limitado
 * al canvas JavaFX.</p>
 */
public final class MultiNotationSvgDiagramExporter implements SvgDiagramExporter {

    private final ChenSvgDiagramExporter chenExporter = new ChenSvgDiagramExporter();
    private final SpecializedVisualSvgDiagramExporter specializedExporter = new SpecializedVisualSvgDiagramExporter();
    private final RolesPermissionsMatrixSvgExporter rolesPermissionsMatrixExporter = new RolesPermissionsMatrixSvgExporter();

    @Override
    public String export(DiagramProject project) {
        if (DiagramTypeId.ROLES_PERMISSIONS_MAP.equals(project.metadata().diagramTypeId())) {
            return rolesPermissionsMatrixExporter.export(project);
        }
        if (!DiagramTypeId.CONCEPTUAL_MODEL.equals(project.metadata().diagramTypeId())) {
            return specializedExporter.export(project);
        }
        if (project.metadata().activeNotation() == NotationType.CROWS_FOOT) {
            return exportCrowsFoot(project);
        }
        return chenExporter.export(project);
    }

    private String exportCrowsFoot(DiagramProject project) {
        DiagramLayout layout = project.layouts()
                .layoutFor(NotationType.CROWS_FOOT)
                .orElseGet(() -> DiagramLayout.empty(NotationType.CROWS_FOOT));
        StringBuilder svg = new StringBuilder(48_000);
        Bounds bounds = bounds(layout);
        double width = Math.max(900.0, bounds.maxX - bounds.minX + 96.0);
        double height = Math.max(650.0, bounds.maxY - bounds.minY + 96.0);
        double ox = 48.0 - bounds.minX;
        double oy = 48.0 - bounds.minY;
        svg.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        svg.append("<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"").append(format(width))
                .append("\" height=\"").append(format(height)).append("\" viewBox=\"0 0 ")
                .append(format(width)).append(' ').append(format(height)).append("\">\n");
        svg.append("  <metadata>").append(escape(project.metadata().title()))
                .append(" | generated-by=Domain Model Studio | notation=CROWS_FOOT</metadata>\n");
        svg.append("  <rect x=\"0\" y=\"0\" width=\"100%\" height=\"100%\" fill=\"")
                .append(project.styleSheet().appearance().diagramBackground().toHex())
                .append("\"/>\n");
        svg.append("  <g id=\"connectors\">\n");
        for (ConnectorLayout connector : layout.connectors()) {
            Optional<NodeLayout> source = layout.nodeFor(connector.sourceElementId());
            Optional<NodeLayout> target = layout.nodeFor(connector.targetElementId());
            if (source.isPresent() && target.isPresent()) {
                double x1 = centerX(source.get()) + ox;
                double y1 = centerY(source.get()) + oy;
                double x2 = centerX(target.get()) + ox;
                double y2 = centerY(target.get()) + oy;
                String connectorStyle = connectorSvgStyle(project, connector);
                List<Point> route = routeFor(connector, x1, y1, x2, y2, ox, oy);
                for (int routeIndex = 0; routeIndex < route.size() - 1; routeIndex++) {
                    Point a = route.get(routeIndex);
                    Point b = route.get(routeIndex + 1);
                    appendLine(svg, a.x(), a.y(), b.x(), b.y(), connectorStyle);
                }
                appendMarker(svg, connector.sourceMarker(), route.get(1).x(), route.get(1).y(), route.get(0).x(), route.get(0).y(), connectorStyle);
                appendMarker(svg, connector.targetMarker(), route.get(route.size() - 2).x(), route.get(route.size() - 2).y(),
                        route.get(route.size() - 1).x(), route.get(route.size() - 1).y(), connectorStyle);
                appendRelationshipLabel(svg, project, connector, route);
            }
        }
        svg.append("  </g>\n");
        svg.append("  <g id=\"entities\">\n");
        for (EntityElement entity : project.model().entities()) {
            layout.nodeFor(entity.id()).ifPresent(node -> renderEntity(svg, project, entity, node, ox, oy));
        }
        svg.append("  </g>\n");
        svg.append("</svg>\n");
        return svg.toString();
    }

    private void renderEntity(StringBuilder svg, DiagramProject project, EntityElement entity, NodeLayout node, double ox, double oy) {
        double x = node.x() + ox;
        double y = node.y() + oy;
        ElementStyle style = DiagramStyleResolver.resolvedStyleFor(project, entity.id());
        svg.append("    <rect x=\"").append(format(x)).append("\" y=\"").append(format(y))
                .append("\" width=\"").append(format(node.width())).append("\" height=\"")
                .append(format(node.height())).append("\" fill=\"").append(style.fill().color().toHex())
                .append("\" stroke=\"").append(style.stroke().color().toHex())
                .append("\" stroke-width=\"").append(format(Math.max(1.0, style.stroke().width()))).append("\"/>\n");
        svg.append("    <rect x=\"").append(format(x)).append("\" y=\"").append(format(y))
                .append("\" width=\"").append(format(node.width())).append("\" height=\"34\" fill=\"#E9EDF2\" stroke=\"")
                .append(style.stroke().color().toHex()).append("\" stroke-width=\"1\"/>\n");
        svg.append("    <text x=\"").append(format(x + 8)).append("\" y=\"").append(format(y + 22))
                .append("\" font-family=\"Segoe UI\" font-size=\"12\" font-weight=\"bold\" fill=\"#222222\">")
                .append(escape(entity.name())).append("</text>\n");
        double rowY = y + 56.0;
        for (AttributeElement attribute : entity.attributes()) {
            svg.append("    <text x=\"").append(format(x + 10)).append("\" y=\"").append(format(rowY))
                    .append("\" font-family=\"Consolas, Segoe UI\" font-size=\"11\" fill=\"#2a2a2a\">")
                    .append(escape(attributeLabel(attribute)))
                    .append("</text>\n");
            rowY += 22.0;
        }
    }

    private String attributeLabel(AttributeElement attribute) {
        if (attribute.isPrimaryKey()) {
            return "PK  " + attribute.name();
        }
        if (attribute.isPartialKey()) {
            return "PPK " + attribute.name();
        }
        return "    " + attribute.name();
    }

    private void appendMarker(StringBuilder svg, ConnectorMarker marker, double previousX, double previousY, double endX, double endY, String connectorStyle) {
        if (marker == ConnectorMarker.NONE) return;
        double angle = Math.atan2(endY - previousY, endX - previousX);
        double ux = Math.cos(angle);
        double uy = Math.sin(angle);
        double px = -uy;
        double py = ux;
        double back = 16.0;
        double spread = 10.0;
        if (marker == ConnectorMarker.OPTIONAL_ONE || marker == ConnectorMarker.OPTIONAL_MANY) {
            svg.append("    <circle cx=\"").append(format(endX - ux * 10.0)).append("\" cy=\"")
                    .append(format(endY - uy * 10.0)).append("\" r=\"5\" fill=\"#FFFFFF\" ")
                    .append(connectorStyle).append("/>\n");
        }
        if (marker == ConnectorMarker.ONE || marker == ConnectorMarker.OPTIONAL_ONE) {
            appendLine(svg, endX - ux * back + px * spread, endY - uy * back + py * spread,
                    endX - ux * back - px * spread, endY - uy * back - py * spread, connectorStyle);
        } else {
            double bx = endX - ux * back;
            double by = endY - uy * back;
            appendLine(svg, endX, endY, bx + px * spread, by + py * spread, connectorStyle);
            appendLine(svg, endX, endY, bx, by, connectorStyle);
            appendLine(svg, endX, endY, bx - px * spread, by - py * spread, connectorStyle);
        }
    }

    private List<Point> routeFor(ConnectorLayout connector, double x1, double y1, double x2, double y2, double ox, double oy) {
        List<Point> route = new ArrayList<>();
        route.add(new Point(x1, y1));
        connector.bendPoints().forEach(bendPoint -> route.add(new Point(bendPoint.x() + ox, bendPoint.y() + oy)));
        route.add(new Point(x2, y2));
        return route;
    }

    private void appendRelationshipLabel(StringBuilder svg, DiagramProject project, ConnectorLayout connector, List<Point> route) {
        String relationshipName = relationshipNameFor(project, connector).orElse(null);
        if (relationshipName == null || relationshipName.isBlank()) {
            return;
        }
        Point anchor = labelAnchorPoint(route);
        double x = anchor.x() + connector.labelOffsetX();
        double y = anchor.y() + connector.labelOffsetY();
        svg.append("    <rect x=\"").append(format(x - 60)).append("\" y=\"").append(format(y - 13))
                .append("\" width=\"120\" height=\"22\" fill=\"#FFFFFF\" fill-opacity=\"0.94\" ")
                .append("stroke=\"#C8C8C2\" stroke-width=\"1\"/>\n");
        svg.append("    <text x=\"").append(format(x)).append("\" y=\"").append(format(y + 4))
                .append("\" text-anchor=\"middle\" font-family=\"Segoe UI\" font-size=\"11\" fill=\"#303030\">")
                .append(escape(relationshipName)).append("</text>\n");
    }

    private Optional<String> relationshipNameFor(DiagramProject project, ConnectorLayout connector) {
        String connectorValue = connector.connectorId().value();
        return project.model().relationships().stream()
                .filter(relationship -> relationship.id().equals(connector.connectorId())
                        || ("conn_crowsfoot_" + relationship.id().value()).equals(connectorValue)
                        || (relationship.fromEntityId().equals(connector.sourceElementId())
                                && relationship.toEntityId().equals(connector.targetElementId())))
                .map(relationship -> relationship.name())
                .findFirst();
    }

    private Point labelAnchorPoint(List<Point> route) {
        if (route == null || route.isEmpty()) {
            return new Point(0.0, 0.0);
        }
        if (route.size() == 1) {
            return route.get(0);
        }
        double totalLength = 0.0;
        for (int index = 0; index < route.size() - 1; index++) {
            totalLength += distance(route.get(index), route.get(index + 1));
        }
        if (totalLength <= 0.01) {
            return route.get(route.size() / 2);
        }
        double halfLength = totalLength / 2.0;
        double traversed = 0.0;
        for (int index = 0; index < route.size() - 1; index++) {
            Point a = route.get(index);
            Point b = route.get(index + 1);
            double segmentLength = distance(a, b);
            if (traversed + segmentLength >= halfLength) {
                double ratio = segmentLength <= 0.01 ? 0.0 : (halfLength - traversed) / segmentLength;
                return new Point(a.x() + (b.x() - a.x()) * ratio, a.y() + (b.y() - a.y()) * ratio);
            }
            traversed += segmentLength;
        }
        return route.get(route.size() - 1);
    }

    private double distance(Point a, Point b) {
        return Math.hypot(a.x() - b.x(), a.y() - b.y());
    }

    private void appendLine(StringBuilder svg, double x1, double y1, double x2, double y2, String connectorStyle) {
        svg.append("    <line x1=\"").append(format(x1)).append("\" y1=\"").append(format(y1))
                .append("\" x2=\"").append(format(x2)).append("\" y2=\"").append(format(y2))
                .append("\" ").append(connectorStyle).append("/>\n");
    }

    private String connectorSvgStyle(DiagramProject project, ConnectorLayout connector) {
        StrokeStyle stroke = DiagramStyleResolver.resolvedStyleFor(project, connector.connectorId()).stroke();
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

    private Bounds bounds(DiagramLayout layout) {
        if (layout.nodes().isEmpty()) return new Bounds(0, 0, 900, 650);
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
        return new Bounds(minX, minY, maxX, maxY);
    }

    private double centerX(NodeLayout node) { return node.x() + node.width() / 2.0; }
    private double centerY(NodeLayout node) { return node.y() + node.height() / 2.0; }
    private String format(double value) { return String.format(java.util.Locale.ROOT, "%.2f", value); }
    private String escape(String value) {
        return value == null ? "" : value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
    private record Bounds(double minX, double minY, double maxX, double maxY) {}
    private record Point(double x, double y) {}
}
