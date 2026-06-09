package com.marcosmoreira.domainmodelstudio.application.visual;

import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureDiagramKind;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureNode;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureNodeKind;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Auto-layout semántico para C4 Contexto y C4 Contenedores.
 *
 * <p>C4 no se trata como grafo libre: el contexto coloca el sistema bajo estudio
 * al centro, personas y sistemas externos fuera; contenedores organiza aplicaciones,
 * APIs, bases de datos y servicios externos por capas técnicas.</p>
 */
public final class C4ArchitectureAutoLayoutPolicy {

    public static final int CONTEXT_BASE = 90_000;
    public static final int CONTAINERS_BASE = 130_000;
    public static final int ORDER_BUCKET = 10_000;
    public static final int BOUNDARY_SLOT = 0;
    public static final int LEFT_SLOT = 1;
    public static final int CENTER_SLOT = 2;
    public static final int RIGHT_SLOT = 3;
    public static final int EXTERNAL_SLOT = 4;
    private static final VisualTextFitPolicy TEXT_FIT = new VisualTextFitPolicy();

    public boolean supports(ArchitectureDiagramDocument document) {
        if (document == null) return false;
        return document.diagramKind() == ArchitectureDiagramKind.C4_CONTEXT
                || document.diagramKind() == ArchitectureDiagramKind.C4_CONTAINERS;
    }

    public List<VisualNodeReference> visualReferences(ArchitectureDiagramDocument document) {
        Objects.requireNonNull(document, "document");
        return document.diagramKind() == ArchitectureDiagramKind.C4_CONTEXT
                ? contextReferences(document)
                : containerReferences(document);
    }

    private List<VisualNodeReference> contextReferences(ArchitectureDiagramDocument document) {
        List<VisualNodeReference> references = new ArrayList<>();
        append(references, nodes(document, ArchitectureNodeKind.BOUNDARY), CONTEXT_BASE, BOUNDARY_SLOT, 760.0, 560.0);
        append(references, nodes(document, ArchitectureNodeKind.PERSON), CONTEXT_BASE, LEFT_SLOT, 178.0, 104.0);
        append(references, nodes(document, ArchitectureNodeKind.SOFTWARE_SYSTEM), CONTEXT_BASE, CENTER_SLOT, 245.0, 112.0);
        append(references, nodes(document, ArchitectureNodeKind.EXTERNAL_SYSTEM), CONTEXT_BASE, RIGHT_SLOT, 220.0, 104.0);
        return references;
    }

    private List<VisualNodeReference> containerReferences(ArchitectureDiagramDocument document) {
        List<VisualNodeReference> references = new ArrayList<>();
        append(references, nodes(document, ArchitectureNodeKind.BOUNDARY), CONTAINERS_BASE, BOUNDARY_SLOT, 930.0, 520.0);
        append(references, nodes(document, ArchitectureNodeKind.APPLICATION), CONTAINERS_BASE, LEFT_SLOT, 220.0, 102.0);
        List<ArchitectureNode> middle = new ArrayList<>();
        middle.addAll(nodes(document, ArchitectureNodeKind.SOFTWARE_SYSTEM));
        middle.addAll(nodes(document, ArchitectureNodeKind.API));
        middle.addAll(nodes(document, ArchitectureNodeKind.CONTAINER));
        append(references, middle, CONTAINERS_BASE, CENTER_SLOT, 222.0, 102.0);
        append(references, nodes(document, ArchitectureNodeKind.DATABASE), CONTAINERS_BASE, RIGHT_SLOT, 210.0, 105.0);
        append(references, nodes(document, ArchitectureNodeKind.EXTERNAL_SERVICE), CONTAINERS_BASE, EXTERNAL_SLOT, 220.0, 98.0);
        return references;
    }

    private static void append(List<VisualNodeReference> references, List<ArchitectureNode> nodes,
                               int base, int slot, double width, double height) {
        for (int index = 0; index < nodes.size(); index++) {
            ArchitectureNode node = nodes.get(index);
            VisualTextFitPolicy.BoxSize size = TEXT_FIT.fitCard(
                    new VisualTextFitPolicy.BoxSize(width, height),
                    node.displayName(),
                    node.description());
            references.add(new VisualNodeReference(
                    VisualElementLayoutIds.architectureNode(node.id()),
                    size.width(),
                    size.height(),
                    base + slot * ORDER_BUCKET + index));
        }
    }

    private static List<ArchitectureNode> nodes(ArchitectureDiagramDocument document, ArchitectureNodeKind kind) {
        return document.nodes().stream()
                .filter(node -> node.kind() == kind)
                .sorted(Comparator.comparingInt(ArchitectureNode::orderIndex).thenComparing(ArchitectureNode::displayName))
                .toList();
    }
}
