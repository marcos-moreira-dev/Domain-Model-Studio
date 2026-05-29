package com.marcosmoreira.domainmodelstudio.infrastructure.svg.specialized;

import com.marcosmoreira.domainmodelstudio.domain.behavior.SequenceCombinedFragmentSpec;
import com.marcosmoreira.domainmodelstudio.domain.behavior.SequenceFragmentOperand;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/** Escribe la geometría temporal propia de UML Secuencia en SVG especializado. */
final class SpecializedSvgSequenceWriter {

    private final SpecializedSvgText text;
    private final SpecializedSvgDocumentWriter documentWriter;
    private final SpecializedSvgNodeWriter nodeWriter;

    SpecializedSvgSequenceWriter(
            SpecializedSvgText text,
            SpecializedSvgDocumentWriter documentWriter,
            SpecializedSvgNodeWriter nodeWriter
    ) {
        this.text = text;
        this.documentWriter = documentWriter;
        this.nodeWriter = nodeWriter;
    }

    String writeSequence(SpecializedSvgModel model, DiagramLayout layout) {
        Map<DiagramElementId, SpecializedSvgNode> nodeIndex = model.nodes().stream()
                .collect(Collectors.toMap(SpecializedSvgNode::layoutId, Function.identity(), (a, b) -> a));
        List<NodeLayout> participants = layout.nodes().stream()
                .filter(NodeLayout::visible)
                .filter(node -> nodeIndex.containsKey(node.elementId()))
                .filter(node -> "Participante".equalsIgnoreCase(nodeIndex.get(node.elementId()).kindLabel()))
                .sorted((left, right) -> Double.compare(left.x(), right.x()))
                .toList();
        double maxParticipantX = participants.stream()
                .mapToDouble(node -> node.x() + Math.max(node.width(), SpecializedSvgConstants.SEQUENCE_PARTICIPANT_WIDTH))
                .max()
                .orElse(SpecializedSvgConstants.MIN_WIDTH - SpecializedSvgConstants.MARGIN * 2.0);
        double width = Math.max(SpecializedSvgConstants.MIN_WIDTH, maxParticipantX + SpecializedSvgConstants.MARGIN * 2.0);
        double messageBottom = SpecializedSvgConstants.SEQUENCE_MESSAGE_START_Y
                + Math.max(3, model.connectors().size()) * SpecializedSvgConstants.SEQUENCE_MESSAGE_ROW_GAP
                + 96.0;
        double nodeBottom = layout.nodes().stream()
                .mapToDouble(node -> node.y() + node.height() + SpecializedSvgConstants.HEADER_HEIGHT + SpecializedSvgConstants.MARGIN)
                .max()
                .orElse(0.0);
        double height = Math.max(SpecializedSvgConstants.MIN_HEIGHT, Math.max(messageBottom, nodeBottom) + SpecializedSvgConstants.MARGIN);
        double offsetX = SpecializedSvgConstants.MARGIN;
        StringBuilder svg = new StringBuilder(64_000);
        documentWriter.appendStart(svg, model, width, height);
        documentWriter.appendStyles(svg);
        documentWriter.appendHeader(svg, model, width);
        appendLifelines(svg, participants, nodeIndex, offsetX, height - SpecializedSvgConstants.MARGIN);
        appendMessages(svg, model, layout, offsetX);
        appendSequenceNotes(svg, layout, nodeIndex, offsetX);
        svg.append("</svg>\n");
        return svg.toString();
    }

    private void appendLifelines(
            StringBuilder svg,
            List<NodeLayout> participants,
            Map<DiagramElementId, SpecializedSvgNode> nodeIndex,
            double offsetX,
            double lifelineBottom
    ) {
        svg.append("  <g id=\"sequence-lifelines\">\n");
        for (NodeLayout participant : participants) {
            SpecializedSvgNode semantic = nodeIndex.get(participant.elementId());
            renderSequenceParticipant(svg, participant, semantic, offsetX, lifelineBottom);
        }
        svg.append("  </g>\n");
    }

    private void appendMessages(StringBuilder svg, SpecializedSvgModel model, DiagramLayout layout, double offsetX) {
        svg.append("  <g id=\"sequence-messages\">\n");
        for (int index = 0; index < model.connectors().size(); index++) {
            SpecializedSvgConnector connector = model.connectors().get(index);
            Optional<NodeLayout> source = layout.nodeFor(connector.sourceLayoutId());
            Optional<NodeLayout> target = layout.nodeFor(connector.targetLayoutId());
            if (source.isPresent() && target.isPresent()) {
                renderSequenceMessage(
                        svg,
                        connector,
                        source.get(),
                        target.get(),
                        offsetX,
                        SpecializedSvgConstants.SEQUENCE_MESSAGE_START_Y
                                + index * SpecializedSvgConstants.SEQUENCE_MESSAGE_ROW_GAP,
                        index + 1);
            }
        }
        svg.append("  </g>\n");
    }

    private void appendSequenceNotes(
            StringBuilder svg,
            DiagramLayout layout,
            Map<DiagramElementId, SpecializedSvgNode> nodeIndex,
            double offsetX
    ) {
        svg.append("  <g id=\"sequence-notes\">\n");
        for (NodeLayout node : layout.nodes()) {
            SpecializedSvgNode semantic = nodeIndex.get(node.elementId());
            if (semantic != null && node.visible() && !"Participante".equalsIgnoreCase(semantic.kindLabel())) {
                if ("Fragmento".equalsIgnoreCase(semantic.kindLabel())) {
                    renderSequenceFragment(svg, node, semantic, offsetX,
                            SpecializedSvgConstants.HEADER_HEIGHT + SpecializedSvgConstants.MARGIN);
                } else {
                    nodeWriter.renderNode(svg, node, semantic, offsetX,
                            SpecializedSvgConstants.HEADER_HEIGHT + SpecializedSvgConstants.MARGIN);
                }
            }
        }
        svg.append("  </g>\n");
    }

    private void renderSequenceParticipant(StringBuilder svg, NodeLayout node, SpecializedSvgNode semantic, double offsetX, double lifelineBottom) {
        double x = node.x() + offsetX;
        double y = SpecializedSvgConstants.SEQUENCE_TOP_Y;
        double width = Math.max(SpecializedSvgConstants.SEQUENCE_PARTICIPANT_WIDTH, node.width());
        double centerX = x + width / 2.0;
        svg.append("    <g class=\"sequence-participant\">\n");
        svg.append("      <rect x=\"").append(text.format(x)).append("\" y=\"").append(text.format(y))
                .append("\" width=\"").append(text.format(width)).append("\" height=\"")
                .append(text.format(SpecializedSvgConstants.SEQUENCE_PARTICIPANT_HEIGHT)).append("\"/>\n");
        svg.append("      <text text-anchor=\"middle\" x=\"").append(text.format(centerX)).append("\" y=\"")
                .append(text.format(y + 34.0)).append("\">").append(text.escape(text.shorten(semantic.title(), 28))).append("</text>\n");
        svg.append("      <line class=\"sequence-lifeline\" x1=\"").append(text.format(centerX)).append("\" y1=\"")
                .append(text.format(y + SpecializedSvgConstants.SEQUENCE_PARTICIPANT_HEIGHT)).append("\" x2=\"")
                .append(text.format(centerX)).append("\" y2=\"").append(text.format(lifelineBottom)).append("\"/>\n");
        svg.append("    </g>\n");
    }

    private void renderSequenceMessage(
            StringBuilder svg,
            SpecializedSvgConnector connector,
            NodeLayout source,
            NodeLayout target,
            double offsetX,
            double y,
            int number
    ) {
        double sourceWidth = Math.max(SpecializedSvgConstants.SEQUENCE_PARTICIPANT_WIDTH, source.width());
        double targetWidth = Math.max(SpecializedSvgConstants.SEQUENCE_PARTICIPANT_WIDTH, target.width());
        double startX = source.x() + offsetX + sourceWidth / 2.0;
        double endX = target.x() + offsetX + targetWidth / 2.0;
        boolean response = connector.kindLabel().toLowerCase(Locale.ROOT).contains("respuesta")
                || connector.visibleLabel().toLowerCase(Locale.ROOT).contains("respuesta")
                || connector.visibleLabel().toLowerCase(Locale.ROOT).contains("retorno");
        svg.append("    <g>\n");
        if (source.elementId().equals(target.elementId())) {
            double loopStartX = startX + 8.0;
            double loopEndX = startX + 82.0;
            double loopBottomY = y + 38.0;
            svg.append("      <polyline class=\"sequence-message-self")
                    .append(response ? " sequence-message-return" : "")
                    .append("\" points=\"")
                    .append(text.format(loopStartX)).append(',').append(text.format(y)).append(' ')
                    .append(text.format(loopEndX)).append(',').append(text.format(y)).append(' ')
                    .append(text.format(loopEndX)).append(',').append(text.format(loopBottomY)).append(' ')
                    .append(text.format(loopStartX)).append(',').append(text.format(loopBottomY))
                    .append("\"/>\n");
            appendSequenceLabel(svg, number + ". " + connector.visibleLabel(), (loopStartX + loopEndX) / 2.0, y - 18.0);
        } else {
            svg.append("      <line class=\"sequence-message")
                    .append(response ? " sequence-message-return" : "")
                    .append("\" x1=\"").append(text.format(startX)).append("\" y1=\"").append(text.format(y))
                    .append("\" x2=\"").append(text.format(endX)).append("\" y2=\"").append(text.format(y)).append("\"/>\n");
            appendSequenceLabel(svg, number + ". " + connector.visibleLabel(), (startX + endX) / 2.0, y - 18.0);
        }
        svg.append("    </g>\n");
    }

    private void appendSequenceLabel(StringBuilder svg, String label, double x, double y) {
        String normalized = label == null ? "" : label.strip();
        if (normalized.isBlank()) {
            return;
        }
        double boxWidth = Math.min(220.0, Math.max(80.0, normalized.length() * 6.0 + 22.0));
        svg.append("      <rect class=\"sequence-label-box\" x=\"").append(text.format(x - boxWidth / 2.0))
                .append("\" y=\"").append(text.format(y - 13.0)).append("\" width=\"").append(text.format(boxWidth))
                .append("\" height=\"22\"/>\n");
        svg.append("      <text class=\"sequence-label\" text-anchor=\"middle\" x=\"").append(text.format(x))
                .append("\" y=\"").append(text.format(y + 2.0)).append("\">")
                .append(text.escape(text.shorten(normalized, 34))).append("</text>\n");
    }

    private void renderSequenceFragment(StringBuilder svg, NodeLayout node, SpecializedSvgNode semantic, double offsetX, double offsetY) {
        double x = node.x() + offsetX;
        double y = node.y() + offsetY;
        double width = Math.max(420.0, node.width());
        double height = Math.max(120.0, node.height());
        SequenceCombinedFragmentSpec spec = SequenceCombinedFragmentSpec.fromText(
                semantic.title(),
                String.join(" | ", semantic.details()),
                "");
        svg.append("    <g class=\"sequence-fragment\">\n");
        svg.append("      <rect class=\"sequence-fragment-box\" x=\"").append(text.format(x)).append("\" y=\"").append(text.format(y))
                .append("\" width=\"").append(text.format(width)).append("\" height=\"").append(text.format(height)).append("\"/>\n");
        svg.append("      <rect class=\"sequence-fragment-tag\" x=\"").append(text.format(x)).append("\" y=\"").append(text.format(y))
                .append("\" width=\"70\" height=\"24\"/>\n");
        svg.append("      <text class=\"node-title\" x=\"").append(text.format(x + 10.0)).append("\" y=\"").append(text.format(y + 16.0))
                .append("\">").append(text.escape(spec.kind().keyword())).append("</text>\n");
        svg.append("      <text class=\"node-detail\" x=\"").append(text.format(x + 84.0)).append("\" y=\"").append(text.format(y + 17.0))
                .append("\">").append(text.escape(text.shorten(spec.displayTitle(), 46))).append("</text>\n");
        appendFragmentGuard(svg, spec, x, y, width);
        appendFragmentOperands(svg, spec, x, y, width, height);
        svg.append("    </g>\n");
    }

    private void appendFragmentGuard(StringBuilder svg, SequenceCombinedFragmentSpec spec, double x, double y, double width) {
        String guard = spec.guardWithBrackets();
        if (guard.isBlank() && !spec.reference().isBlank()) {
            guard = "ref: " + spec.reference();
        }
        if (guard.isBlank() && spec.hasRange()) {
            guard = "rango: " + spec.rangeLabel();
        }
        if (guard.isBlank()) {
            return;
        }
        svg.append("      <text class=\"node-detail\" x=\"").append(text.format(x + 12.0)).append("\" y=\"").append(text.format(y + 46.0))
                .append("\">").append(text.escape(text.shorten(guard, Math.max(24, (int) width / 9)))).append("</text>\n");
    }

    private void appendFragmentOperands(StringBuilder svg, SequenceCombinedFragmentSpec spec, double x, double y, double width, double height) {
        int operandCount = Math.max(1, spec.operands().size());
        double headerHeight = 62.0;
        double availableHeight = Math.max(48.0, height - headerHeight);
        for (int index = 1; index < operandCount; index++) {
            double separatorY = y + headerHeight + availableHeight * index / operandCount;
            svg.append("      <line class=\"sequence-fragment-operand-separator\" x1=\"").append(text.format(x))
                    .append("\" y1=\"").append(text.format(separatorY))
                    .append("\" x2=\"").append(text.format(x + width))
                    .append("\" y2=\"").append(text.format(separatorY)).append("\"/>\n");
        }
        for (int index = 0; index < spec.operands().size(); index++) {
            SequenceFragmentOperand operand = spec.operands().get(index);
            String label = operand.canonicalLabel();
            if (label.isBlank()) {
                continue;
            }
            double labelY = y + headerHeight + availableHeight * index / operandCount + 20.0;
            svg.append("      <text class=\"node-detail\" x=\"").append(text.format(x + 12.0)).append("\" y=\"").append(text.format(labelY))
                    .append("\">").append(text.escape(text.shorten(label, 52))).append("</text>\n");
        }
    }
}
