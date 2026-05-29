package com.marcosmoreira.domainmodelstudio.infrastructure.svg.specialized;

import com.marcosmoreira.domainmodelstudio.application.export.DiagramExportHeaderMetadata;
import com.marcosmoreira.domainmodelstudio.application.export.DiagramExportHeaderPolicy;

/** Escribe el esqueleto común de documentos SVG especializados. */
final class SpecializedSvgDocumentWriter {

    private final SpecializedSvgText text;

    SpecializedSvgDocumentWriter(SpecializedSvgText text) {
        this.text = text;
    }

    void appendStart(StringBuilder svg, SpecializedSvgModel model, double width, double height) {
        svg.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        svg.append("<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"").append(text.format(width))
                .append("\" height=\"").append(text.format(height)).append("\" viewBox=\"0 0 ")
                .append(text.format(width)).append(' ').append(text.format(height)).append("\">\n");
        svg.append("  <metadata>").append(text.escape(model.title()))
                .append(" | type=").append(text.escape(model.diagramTypeId().value()))
                .append(" | views=").append(model.viewLabels().size())
                .append(" | generated-by=Domain Model Studio | renderer=specialized-vector-svg</metadata>\n");
        svg.append("  <rect x=\"0\" y=\"0\" width=\"100%\" height=\"100%\" fill=\"#F6F7F9\"/>\n");
    }

    void appendStyles(StringBuilder svg) {
        svg.append("  <style><![CDATA[\n")
                .append("    text { font-family: 'Segoe UI', Arial, sans-serif; }\n")
                .append("    .diagram-header-panel { fill: #FFFFFF; stroke: #D0D5DD; stroke-width: 1; }\n")
                .append("    .diagram-title { font-size: 18px; font-weight: 700; fill: #1F2933; }\n")
                .append("    .diagram-subtitle { font-size: 12px; fill: #667085; }\n")
                .append("    .node rect.body { fill: #FFFFFF; stroke: #C7CDD6; stroke-width: 1.25; rx: 10; ry: 10; }\n")
                .append("    .node rect.header { fill: #EEF2F7; stroke: #C7CDD6; stroke-width: 1; rx: 10; ry: 10; }\n")
                .append("    .node-title { font-size: 12px; font-weight: 700; fill: #1F2933; }\n")
                .append("    .node-kind { font-size: 10px; font-weight: 600; fill: #475467; }\n")
                .append("    .node-detail { font-size: 10.5px; fill: #344054; }\n")
                .append("    .node-primary rect.header { fill: #E7EEF8; }\n")
                .append("    .node-support rect.header { fill: #EFF4F2; }\n")
                .append("    .node-screen rect.header, .node-wireframe-screen rect.header { fill: #E9F1FB; }\n")
                .append("    .node-wireframe-component rect.header { fill: #F4F6F8; }\n")
                .append("    .node-uml-class rect.header { fill: #EEF0FA; }\n")
                .append("    .node-behavior rect.header { fill: #F1F5EA; }\n")
                .append("    .node-architecture rect.header { fill: #EDF3F6; }\n")
                .append("    .node-free-graph rect.body { rx: 14; ry: 14; }\n")
                .append("    .node-free-graph rect.header { fill: #EEF6F3; rx: 14; ry: 14; }\n")
                .append("    .connector-free-graph-undirected .connector-line { marker-end: none; }\n")
                .append("    .node-architecture-boundary rect.body, .node-architecture-environment rect.body, .node-architecture-network rect.body { fill: none; fill-opacity: 0; stroke-dasharray: 9 6; }\n")
                .append("    .node-architecture-boundary rect.header, .node-architecture-environment rect.header, .node-architecture-network rect.header { fill: none; fill-opacity: 0; stroke-dasharray: 9 6; }\n")
                .append("    .node-wireframe-component-button rect.header { fill: #E7EEF8; }\n")
                .append("    .node-wireframe-component-table rect.header, .node-wireframe-component-report rect.header { fill: #EEF2F7; }\n")
                .append("    .node-role rect.header { fill: #F3F0FA; }\n")
                .append("    .node-permission rect.header { fill: #EEF6F3; }\n")
                .append("    .connector-line { fill: none; stroke: #4E5968; stroke-width: 1.7; marker-end: url(#arrow); }\n")
                .append("    .connector-uml .connector-line { stroke: #111827; stroke-width: 2.05; marker-end: none; }\n")
                .append("    .connector-uml-inheritance .connector-line { marker-end: url(#uml-inheritance); }\n")
                .append("    .connector-uml-implementation .connector-line { stroke-dasharray: 8 6; marker-end: url(#uml-inheritance); }\n")
                .append("    .connector-uml-composition .connector-line { marker-end: url(#uml-composition); }\n")
                .append("    .connector-uml-aggregation .connector-line { marker-end: url(#uml-aggregation); }\n")
                .append("    .connector-uml-dependency .connector-line { stroke-dasharray: 7 5; marker-end: url(#uml-dependency); }\n")
                .append("    .connector-uml-association .connector-line { marker-end: url(#arrow); }\n")
                .append("    .connector-denied .connector-line { stroke-dasharray: 4 4; }\n")
                .append("    .connector-label-box { fill: #FFFFFF; fill-opacity: .96; stroke: #D0D5DD; stroke-width: 1; rx: 6; ry: 6; }\n")
                .append("    .connector-label { font-size: 10px; fill: #344054; }\n")
                .append("    .sequence-participant rect { fill: #FFFFFF; stroke: #C7CDD6; stroke-width: 1.25; rx: 10; ry: 10; }\n")
                .append("    .sequence-participant text { font-size: 12px; font-weight: 700; fill: #1F2933; }\n")
                .append("    .sequence-lifeline { stroke: #98A2B3; stroke-width: 1.2; stroke-dasharray: 8 8; }\n")
                .append("    .sequence-message { stroke: #4E5968; stroke-width: 1.7; marker-end: url(#arrow); }\n")
                .append("    .sequence-message-return { stroke-dasharray: 7 5; }\n")
                .append("    .sequence-message-self { fill: none; stroke: #4E5968; stroke-width: 1.7; marker-end: url(#arrow); }\n")
                .append("    .sequence-label-box { fill: #FFFFFF; fill-opacity: .96; stroke: #D0D5DD; stroke-width: 1; rx: 6; ry: 6; }\n")
                .append("    .sequence-label { font-size: 10.5px; fill: #344054; }\n")
                .append("    .sequence-fragment-box { fill: #FFFFFF; fill-opacity: .18; stroke: #98A2B3; stroke-width: 1.2; stroke-dasharray: 10 6; rx: 6; ry: 6; }\n")
                .append("    .sequence-fragment-tag { fill: #EEF2F7; stroke: #C7CDD6; stroke-width: 1; rx: 4; ry: 4; }\n")
                .append("    .sequence-fragment-operand-separator { stroke: #667085; stroke-width: 1; stroke-dasharray: 6 4; }\n")
                .append("  ]]></style>\n")
                .append("  <defs>\n")
                .append("    <marker id=\"arrow\" markerWidth=\"10\" markerHeight=\"8\" refX=\"9\" refY=\"4\" orient=\"auto\" markerUnits=\"strokeWidth\">\n")
                .append("      <path d=\"M 0 0 L 10 4 L 0 8 z\" fill=\"#4E5968\"/>\n")
                .append("    </marker>\n")
                .append("    <marker id=\"uml-inheritance\" markerWidth=\"16\" markerHeight=\"14\" refX=\"15\" refY=\"7\" orient=\"auto\" markerUnits=\"strokeWidth\">\n")
                .append("      <path d=\"M 1 1 L 15 7 L 1 13 z\" fill=\"#FFFFFF\" stroke=\"#111827\" stroke-width=\"1.3\"/>\n")
                .append("    </marker>\n")
                .append("    <marker id=\"uml-composition\" markerWidth=\"18\" markerHeight=\"14\" refX=\"17\" refY=\"7\" orient=\"auto\" markerUnits=\"strokeWidth\">\n")
                .append("      <path d=\"M 1 7 L 9 1 L 17 7 L 9 13 z\" fill=\"#111827\" stroke=\"#111827\" stroke-width=\"1.1\"/>\n")
                .append("    </marker>\n")
                .append("    <marker id=\"uml-aggregation\" markerWidth=\"18\" markerHeight=\"14\" refX=\"17\" refY=\"7\" orient=\"auto\" markerUnits=\"strokeWidth\">\n")
                .append("      <path d=\"M 1 7 L 9 1 L 17 7 L 9 13 z\" fill=\"#FFFFFF\" stroke=\"#111827\" stroke-width=\"1.3\"/>\n")
                .append("    </marker>\n")
                .append("    <marker id=\"uml-dependency\" markerWidth=\"12\" markerHeight=\"10\" refX=\"11\" refY=\"5\" orient=\"auto\" markerUnits=\"strokeWidth\">\n")
                .append("      <path d=\"M 1 1 L 11 5 L 1 9\" fill=\"none\" stroke=\"#111827\" stroke-width=\"1.6\"/>\n")
                .append("    </marker>\n")
                .append("  </defs>\n");
    }

    void appendHeader(StringBuilder svg, SpecializedSvgModel model, double width) {
        DiagramExportHeaderMetadata metadata = DiagramExportHeaderPolicy.forSpecializedSvg(
                model.title(),
                model.typeLabel(),
                model.viewSummary());
        svg.append("  <g id=\"export-header\">\n");
        svg.append("    <rect class=\"diagram-header-panel\" x=\"24\" y=\"18\" width=\"").append(text.format(width - 48.0))
                .append("\" height=\"").append(text.format(DiagramExportHeaderPolicy.SVG_HEADER_BLOCK_HEIGHT))
                .append("\" fill=\"#FFFFFF\" stroke=\"#D0D5DD\"/>\n");
        svg.append("    <text class=\"diagram-title\" x=\"44\" y=\"43\">")
                .append(text.escape(metadata.title())).append("</text>\n");
        svg.append("    <text class=\"diagram-subtitle\" x=\"44\" y=\"58\">")
                .append(text.escape(metadata.compactSubtitle())).append("</text>\n");
        svg.append("  </g>\n");
    }
}
