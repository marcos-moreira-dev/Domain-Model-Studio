package com.marcosmoreira.domainmodelstudio.infrastructure.svg.specialized;

import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import java.util.List;

/** Escribe conectores, autorrelaciones y etiquetas comunes para SVG especializado. */
final class SpecializedSvgConnectorWriter {

    private final SpecializedSvgText text;
    private final SpecializedSvgGeometry geometry;

    SpecializedSvgConnectorWriter(SpecializedSvgText text, SpecializedSvgGeometry geometry) {
        this.text = text;
        this.geometry = geometry;
    }

    void renderConnector(
            StringBuilder svg,
            ConnectorLayout connector,
            SpecializedSvgConnector semantic,
            NodeLayout source,
            NodeLayout target,
            double offsetX,
            double offsetY
    ) {
        if (!connector.visible()) {
            return;
        }
        List<SpecializedSvgPoint> route = geometry.route(connector, source, target, offsetX, offsetY);
        if (route.size() < 2) {
            return;
        }
        svg.append("    <g class=\"").append(text.escapeAttr(semantic.cssClass())).append("\">\n");
        if (source.elementId().equals(target.elementId()) && connector.bendPoints().isEmpty()) {
            renderSelfLoopConnector(svg, source, offsetX, offsetY);
        } else {
            svg.append("      <polyline class=\"connector-line\" points=\"");
            for (SpecializedSvgPoint point : route) {
                svg.append(text.format(point.x())).append(',').append(text.format(point.y())).append(' ');
            }
            svg.append("\"/>\n");
        }
        svg.append("    </g>\n");
    }

    void renderConnectorLabel(
            StringBuilder svg,
            ConnectorLayout connector,
            SpecializedSvgConnector semantic,
            NodeLayout source,
            NodeLayout target,
            double offsetX,
            double offsetY
    ) {
        if (!connector.visible()) {
            return;
        }
        List<SpecializedSvgPoint> route = geometry.route(connector, source, target, offsetX, offsetY);
        if (route.size() < 2) {
            return;
        }
        appendConnectorLabel(svg, semantic.visibleLabel(), route, connector.labelOffsetX(), connector.labelOffsetY());
    }

    private void renderSelfLoopConnector(StringBuilder svg, NodeLayout node, double offsetX, double offsetY) {
        SpecializedSvgSelfLoop loop = geometry.selfLoop(node, offsetX, offsetY);
        svg.append("      <path class=\"connector-line\" d=\"M ")
                .append(text.format(loop.start().x())).append(' ').append(text.format(loop.start().y()))
                .append(" C ").append(text.format(loop.control1().x())).append(' ').append(text.format(loop.control1().y()))
                .append(", ").append(text.format(loop.control2().x())).append(' ').append(text.format(loop.control2().y()))
                .append(", ").append(text.format(loop.end().x())).append(' ').append(text.format(loop.end().y()))
                .append("\"/>\n");
    }

    private void appendConnectorLabel(
            StringBuilder svg,
            String label,
            List<SpecializedSvgPoint> route,
            double offsetX,
            double offsetY
    ) {
        String normalized = label == null ? "" : label.strip();
        if (normalized.isBlank()) {
            return;
        }
        SpecializedSvgPoint anchor = geometry.midPoint(route);
        double x = anchor.x() + offsetX;
        double y = anchor.y() + offsetY;
        double boxWidth = Math.min(180.0, Math.max(72.0, normalized.length() * 6.0 + 18.0));
        svg.append("      <rect class=\"connector-label-box\" x=\"").append(text.format(x - boxWidth / 2.0))
                .append("\" y=\"").append(text.format(y - 14.0)).append("\" width=\"").append(text.format(boxWidth))
                .append("\" height=\"22\"/>\n");
        svg.append("      <text class=\"connector-label\" text-anchor=\"middle\" x=\"").append(text.format(x))
                .append("\" y=\"").append(text.format(y + 1.0)).append("\">").append(text.escape(text.shorten(normalized, 30))).append("</text>\n");
    }
}
