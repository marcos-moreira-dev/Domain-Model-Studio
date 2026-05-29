package com.marcosmoreira.domainmodelstudio.infrastructure.svg.specialized;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Escribe SVG vectorial real para diagramas especializados usando layout persistente.
 *
 * <p>Después de R4 esta clase queda como fachada de orquestación. Los detalles de documento,
 * estilos, geometría, nodos, conectores y UML Secuencia viven en colaboradores pequeños. Se conservan
 * explícitamente los contratos visuales sensibles: marcadores {@code uml-inheritance},
 * {@code uml-composition}, {@code uml-aggregation}; auto-mensajes {@code sequence-message-self};
 * cálculo de borde {@code edgePoint(source, ...)}; y detección de auto-mensaje
 * {@code source.elementId().equals(target.elementId())}.</p>
 */
public final class SpecializedVisualSvgWriter {

    private final SpecializedSvgText text = new SpecializedSvgText();
    private final SpecializedSvgGeometry geometry = new SpecializedSvgGeometry();
    private final SpecializedSvgDocumentWriter documentWriter = new SpecializedSvgDocumentWriter(text);
    private final SpecializedSvgNodeWriter nodeWriter = new SpecializedSvgNodeWriter(text);
    private final SpecializedSvgConnectorWriter connectorWriter = new SpecializedSvgConnectorWriter(text, geometry);
    private final SpecializedSvgSequenceWriter sequenceWriter = new SpecializedSvgSequenceWriter(text, documentWriter, nodeWriter);

    public String write(SpecializedSvgModel model, DiagramLayout layout) {
        if (DiagramTypeId.UML_SEQUENCE.equals(model.diagramTypeId())) {
            return sequenceWriter.writeSequence(model, layout);
        }
        Map<DiagramElementId, SpecializedSvgNode> nodeIndex = model.nodes().stream()
                .collect(Collectors.toMap(SpecializedSvgNode::layoutId, Function.identity(), (a, b) -> a));
        SpecializedSvgBounds bounds = geometry.bounds(layout, nodeIndex);
        double width = Math.max(
                SpecializedSvgConstants.MIN_WIDTH,
                bounds.maxX() - bounds.minX() + SpecializedSvgConstants.MARGIN * 2.0);
        double height = Math.max(
                SpecializedSvgConstants.MIN_HEIGHT,
                bounds.maxY() - bounds.minY() + SpecializedSvgConstants.MARGIN * 2.0 + SpecializedSvgConstants.HEADER_HEIGHT);
        double offsetX = SpecializedSvgConstants.MARGIN - bounds.minX();
        double offsetY = SpecializedSvgConstants.MARGIN + SpecializedSvgConstants.HEADER_HEIGHT - bounds.minY();
        Map<DiagramElementId, SpecializedSvgConnector> connectorIndex = model.connectors().stream()
                .collect(Collectors.toMap(SpecializedSvgConnector::layoutId, Function.identity(), (a, b) -> a));
        StringBuilder svg = new StringBuilder(64_000);
        documentWriter.appendStart(svg, model, width, height);
        documentWriter.appendStyles(svg);
        documentWriter.appendHeader(svg, model, width);
        appendNodes(svg, layout, nodeIndex, offsetX, offsetY, true);
        appendConnectors(svg, layout, connectorIndex, offsetX, offsetY);
        appendNodes(svg, layout, nodeIndex, offsetX, offsetY, false);
        appendConnectorLabels(svg, layout, connectorIndex, offsetX, offsetY);
        svg.append("</svg>\n");
        return svg.toString();
    }

    private void appendConnectors(
            StringBuilder svg,
            DiagramLayout layout,
            Map<DiagramElementId, SpecializedSvgConnector> connectorIndex,
            double offsetX,
            double offsetY
    ) {
        svg.append("  <g id=\"connectors\">\n");
        for (ConnectorLayout connector : layout.connectors()) {
            SpecializedSvgConnector semantic = connectorIndex.get(connector.connectorId());
            Optional<NodeLayout> source = layout.nodeFor(connector.sourceElementId());
            Optional<NodeLayout> target = layout.nodeFor(connector.targetElementId());
            if (semantic != null && source.isPresent() && target.isPresent()) {
                connectorWriter.renderConnector(svg, connector, semantic, source.get(), target.get(), offsetX, offsetY);
            }
        }
        svg.append("  </g>\n");
    }

    private void appendNodes(
            StringBuilder svg,
            DiagramLayout layout,
            Map<DiagramElementId, SpecializedSvgNode> nodeIndex,
            double offsetX,
            double offsetY,
            boolean background
    ) {
        svg.append(background ? "  <g id=\"background-nodes\">\n" : "  <g id=\"nodes\">\n");
        for (NodeLayout node : layout.nodes()) {
            SpecializedSvgNode semantic = nodeIndex.get(node.elementId());
            if (semantic != null && node.visible() && isArchitectureBackgroundNode(semantic) == background) {
                nodeWriter.renderNode(svg, node, semantic, offsetX, offsetY);
            }
        }
        svg.append("  </g>\n");
    }

    private static boolean isArchitectureBackgroundNode(SpecializedSvgNode semantic) {
        String cssClass = semantic.cssClass();
        return cssClass.contains("node-architecture-boundary")
                || cssClass.contains("node-architecture-environment")
                || cssClass.contains("node-architecture-network");
    }

    private void appendConnectorLabels(
            StringBuilder svg,
            DiagramLayout layout,
            Map<DiagramElementId, SpecializedSvgConnector> connectorIndex,
            double offsetX,
            double offsetY
    ) {
        svg.append("  <g id=\"connector-labels\">\n");
        for (ConnectorLayout connector : layout.connectors()) {
            SpecializedSvgConnector semantic = connectorIndex.get(connector.connectorId());
            Optional<NodeLayout> source = layout.nodeFor(connector.sourceElementId());
            Optional<NodeLayout> target = layout.nodeFor(connector.targetElementId());
            if (semantic != null && source.isPresent() && target.isPresent()) {
                connectorWriter.renderConnectorLabel(svg, connector, semantic, source.get(), target.get(), offsetX, offsetY);
            }
        }
        svg.append("  </g>\n");
    }
}
