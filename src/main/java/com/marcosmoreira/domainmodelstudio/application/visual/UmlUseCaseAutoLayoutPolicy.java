package com.marcosmoreira.domainmodelstudio.application.visual;

import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramKind;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorEdge;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorEdgeKind;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNode;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNodeKind;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/** Layout semántico inicial para UML Casos de uso. */
public final class UmlUseCaseAutoLayoutPolicy {

    static final int LEFT_ACTOR_BASE = 10_000;
    static final int SYSTEM_BOUNDARY_BASE = 20_000;
    static final int USE_CASE_BASE = 30_000;
    static final int RIGHT_ACTOR_BASE = 40_000;
    static final int ORDER_BUCKET = 10_000;

    private final DependencyDrivenOrderingPolicy orderingPolicy = new DependencyDrivenOrderingPolicy();

    public boolean supports(BehaviorDiagramDocument document) {
        return document != null && document.diagramKind() == BehaviorDiagramKind.UML_USE_CASE;
    }

    public List<VisualNodeReference> visualReferences(BehaviorDiagramDocument document) {
        Objects.requireNonNull(document, "document");
        List<BehaviorNode> boundaries = nodesOfKind(document, BehaviorNodeKind.SYSTEM_BOUNDARY);
        List<BehaviorNode> useCases = orderedUseCases(document);
        List<BehaviorNode> leftActors = new ArrayList<>();
        List<BehaviorNode> rightActors = new ArrayList<>();
        for (BehaviorNode actor : nodesOfKind(document, BehaviorNodeKind.ACTOR)) {
            (rightSideActor(actor, document) ? rightActors : leftActors).add(actor);
        }
        List<VisualNodeReference> references = new ArrayList<>();
        append(references, leftActors, LEFT_ACTOR_BASE, 105.0, 112.0);
        append(references, boundaries, SYSTEM_BOUNDARY_BASE, 760.0, 560.0);
        append(references, useCases, USE_CASE_BASE, 205.0, 86.0);
        append(references, rightActors, RIGHT_ACTOR_BASE, 105.0, 112.0);
        return references;
    }

    private List<BehaviorNode> orderedUseCases(BehaviorDiagramDocument document) {
        List<BehaviorNode> useCases = nodesOfKind(document, BehaviorNodeKind.USE_CASE);
        List<String> ids = useCases.stream().map(BehaviorNode::id).toList();
        List<DependencyDrivenOrderingPolicy.DirectedDependency> dependencies = document.edges().stream()
                .filter(edge -> edge.kind() == BehaviorEdgeKind.INCLUDE || edge.kind() == BehaviorEdgeKind.EXTEND)
                .map(edge -> new DependencyDrivenOrderingPolicy.DirectedDependency(edge.sourceNodeId(), edge.targetNodeId()))
                .toList();
        List<String> orderedIds = orderingPolicy.order(ids, dependencies);
        return orderedIds.stream().map(id -> useCases.stream()
                .filter(node -> node.id().equals(id)).findFirst().orElseThrow()).toList();
    }

    private static boolean rightSideActor(BehaviorNode actor, BehaviorDiagramDocument document) {
        String text = (actor.displayName() + " " + actor.owner() + " " + actor.description() + " " + actor.notes())
                .toLowerCase(Locale.ROOT);
        if (text.contains("extern") || text.contains("secund") || text.contains("sistema")
                || text.contains("servicio") || text.contains("correo") || text.contains("pasarela")
                || text.contains("impres") || text.contains("soporte")) {
            return true;
        }
        long asTarget = document.edges().stream().filter(edge -> edge.targetNodeId().equals(actor.id())).count();
        long asSource = document.edges().stream().filter(edge -> edge.sourceNodeId().equals(actor.id())).count();
        return asTarget > asSource;
    }

    private static List<BehaviorNode> nodesOfKind(BehaviorDiagramDocument document, BehaviorNodeKind kind) {
        return document.nodes().stream().filter(node -> node.kind() == kind).toList();
    }

    private static void append(List<VisualNodeReference> references, List<BehaviorNode> nodes,
                               int base, double width, double height) {
        int index = 0;
        for (BehaviorNode node : nodes) {
            references.add(new VisualNodeReference(
                    VisualElementLayoutIds.behaviorNode(node.id()), width, height, base + index++));
        }
    }
}
