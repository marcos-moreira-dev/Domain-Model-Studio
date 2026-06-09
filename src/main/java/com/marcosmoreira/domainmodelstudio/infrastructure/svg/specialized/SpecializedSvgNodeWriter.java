package com.marcosmoreira.domainmodelstudio.infrastructure.svg.specialized;

import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;

/** Escribe nodos SVG genéricos para diagramas especializados no temporales. */
final class SpecializedSvgNodeWriter {

    private final SpecializedSvgText text;

    SpecializedSvgNodeWriter(SpecializedSvgText text) {
        this.text = text;
    }

    void renderNode(StringBuilder svg, NodeLayout node, SpecializedSvgNode semantic, double offsetX, double offsetY) {
        double x = node.x() + offsetX;
        double y = node.y() + offsetY;
        double width = node.width();
        double height = node.height();
        svg.append("    <g class=\"node ").append(text.escapeAttr(semantic.cssClass())).append("\">\n");
        svg.append("      <rect class=\"body\" x=\"").append(text.format(x)).append("\" y=\"").append(text.format(y))
                .append("\" width=\"").append(text.format(width)).append("\" height=\"").append(text.format(height)).append("\"/>\n");
        svg.append("      <rect class=\"header\" x=\"").append(text.format(x)).append("\" y=\"").append(text.format(y))
                .append("\" width=\"").append(text.format(width)).append("\" height=\"30\"/>\n");
        svg.append("      <text class=\"node-title\" x=\"").append(text.format(x + 10)).append("\" y=\"")
                .append(text.format(y + 20)).append("\">").append(text.escape(text.shorten(semantic.title(), 34))).append("</text>\n");
        svg.append("      <text class=\"node-kind\" x=\"").append(text.format(x + 10)).append("\" y=\"")
                .append(text.format(y + 45)).append("\">").append(text.escape(text.shorten(semantic.kindLabel(), 40))).append("</text>\n");
        double rowY = y + 64.0;
        for (String detail : semantic.details()) {
            if (rowY > y + height - 8.0) {
                break;
            }
            svg.append("      <text class=\"node-detail\" x=\"").append(text.format(x + 10)).append("\" y=\"")
                    .append(text.format(rowY)).append("\">").append(text.escape(text.shorten(detail, 46))).append("</text>\n");
            rowY += 16.0;
        }
        svg.append("    </g>\n");
    }
}
