package com.marcosmoreira.domainmodelstudio.application.visual;

import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramKind;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorEdge;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNode;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNodeKind;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Auto-layout semántico para UML Actividad y UML Estados.
 *
 * <p>Actividad se ordena como flujo vertical de acciones. Estados se ordena como ciclo de vida
 * horizontal. No usa fuerza genérica porque ambos diagramas tienen lectura temporal/lógica propia.</p>
 */
public final class UmlFlowAutoLayoutPolicy {

    public static final int ACTIVITY_BASE = 91_000;
    public static final int STATE_BASE = 110_000;
    public static final int ORDER_BUCKET = 100;

    public boolean supports(BehaviorDiagramDocument document) {
        if (document == null) return false;
        return document.diagramKind() == BehaviorDiagramKind.UML_ACTIVITY
                || document.diagramKind() == BehaviorDiagramKind.UML_STATE;
    }

    public List<VisualNodeReference> visualReferences(BehaviorDiagramDocument document) {
        Objects.requireNonNull(document, "document");
        List<BehaviorNode> ordered = orderedByFlow(document);
        Map<String, Integer> depthById = depthById(document, ordered);
        Map<Integer, Integer> slotsByDepth = new LinkedHashMap<>();
        List<VisualNodeReference> references = new ArrayList<>();
        int base = document.diagramKind() == BehaviorDiagramKind.UML_STATE ? STATE_BASE : ACTIVITY_BASE;
        for (BehaviorNode node : ordered) {
            int depth = depthById.getOrDefault(node.id(), Math.max(0, node.orderIndex()));
            int slot = slotsByDepth.getOrDefault(depth, 0);
            slotsByDepth.put(depth, slot + 1);
            references.add(reference(document.diagramKind(), node, base + depth * ORDER_BUCKET + slot));
        }
        return references;
    }

    private static VisualNodeReference reference(BehaviorDiagramKind diagramKind, BehaviorNode node, int orderIndex) {
        DiagramSize size = sizeFor(diagramKind, node.kind());
        return new VisualNodeReference(VisualElementLayoutIds.behaviorNode(node.id()),
                size.width(), size.height(), orderIndex);
    }

    private static DiagramSize sizeFor(BehaviorDiagramKind diagramKind, BehaviorNodeKind kind) {
        if (diagramKind == BehaviorDiagramKind.UML_ACTIVITY) {
            return switch (kind) {
                case INITIAL_STATE, FINAL_STATE -> new DiagramSize(82.0, 72.0);
                case DECISION -> new DiagramSize(132.0, 104.0);
                case FORK, JOIN -> new DiagramSize(190.0, 56.0);
                case ACTION -> new DiagramSize(210.0, 82.0);
                case NOTE -> new DiagramSize(180.0, 86.0);
                default -> new DiagramSize(180.0, 74.0);
            };
        }
        return switch (kind) {
            case INITIAL_STATE, FINAL_STATE -> new DiagramSize(82.0, 72.0);
            case STATE -> new DiagramSize(210.0, 86.0);
            case NOTE -> new DiagramSize(190.0, 90.0);
            default -> new DiagramSize(180.0, 76.0);
        };
    }

    private static List<BehaviorNode> orderedByFlow(BehaviorDiagramDocument document) {
        Map<String, BehaviorNode> nodes = new LinkedHashMap<>();
        for (BehaviorNode node : document.nodes()) nodes.put(node.id(), node);
        Map<String, List<String>> outgoing = outgoing(document, nodes);
        Map<String, Integer> indegree = indegree(document, nodes);
        ArrayDeque<String> queue = new ArrayDeque<>();
        roots(nodes, indegree).forEach(node -> queue.add(node.id()));
        List<BehaviorNode> ordered = new ArrayList<>();
        while (!queue.isEmpty()) {
            String id = queue.removeFirst();
            if (!nodes.containsKey(id) || contains(ordered, id)) continue;
            ordered.add(nodes.get(id));
            for (String target : outgoing.getOrDefault(id, List.of())) {
                int next = indegree.getOrDefault(target, 1) - 1;
                indegree.put(target, next);
                if (next <= 0) queue.addLast(target);
            }
        }
        for (BehaviorNode node : nodes.values()) {
            if (!contains(ordered, node.id())) ordered.add(node);
        }
        return ordered;
    }

    private static Map<String, Integer> depthById(BehaviorDiagramDocument document, List<BehaviorNode> ordered) {
        Map<String, Integer> depth = new LinkedHashMap<>();
        for (BehaviorNode node : ordered) depth.putIfAbsent(node.id(), fallbackDepth(node));
        for (BehaviorNode node : ordered) {
            int sourceDepth = depth.getOrDefault(node.id(), 0);
            for (BehaviorEdge edge : document.edges()) {
                if (!edge.sourceNodeId().equals(node.id())) continue;
                depth.put(edge.targetNodeId(), Math.max(depth.getOrDefault(edge.targetNodeId(), 0), sourceDepth + 1));
            }
        }
        return depth;
    }

    private static Map<String, List<String>> outgoing(BehaviorDiagramDocument document, Map<String, BehaviorNode> nodes) {
        Map<String, List<String>> outgoing = new LinkedHashMap<>();
        for (BehaviorEdge edge : document.edges()) {
            if (!nodes.containsKey(edge.sourceNodeId()) || !nodes.containsKey(edge.targetNodeId())) continue;
            outgoing.computeIfAbsent(edge.sourceNodeId(), ignored -> new ArrayList<>()).add(edge.targetNodeId());
        }
        return outgoing;
    }

    private static Map<String, Integer> indegree(BehaviorDiagramDocument document, Map<String, BehaviorNode> nodes) {
        Map<String, Integer> indegree = new LinkedHashMap<>();
        nodes.keySet().forEach(id -> indegree.put(id, 0));
        for (BehaviorEdge edge : document.edges()) {
            if (nodes.containsKey(edge.sourceNodeId()) && nodes.containsKey(edge.targetNodeId())) {
                indegree.put(edge.targetNodeId(), indegree.getOrDefault(edge.targetNodeId(), 0) + 1);
            }
        }
        return indegree;
    }

    private static List<BehaviorNode> roots(Map<String, BehaviorNode> nodes, Map<String, Integer> indegree) {
        List<BehaviorNode> roots = nodes.values().stream()
                .filter(node -> node.kind() == BehaviorNodeKind.INITIAL_STATE || indegree.getOrDefault(node.id(), 0) == 0)
                .sorted(Comparator.comparingInt(UmlFlowAutoLayoutPolicy::kindPriority)
                        .thenComparingInt(BehaviorNode::orderIndex)
                        .thenComparing(BehaviorNode::displayName))
                .toList();
        return roots.isEmpty() ? nodes.values().stream().limit(1).toList() : roots;
    }

    private static int kindPriority(BehaviorNode node) {
        return switch (node.kind()) {
            case INITIAL_STATE -> 0;
            case START_EVENT -> 1;
            case ACTION, STATE -> 2;
            case DECISION, FORK, JOIN -> 3;
            case FINAL_STATE, END_EVENT -> 9;
            case NOTE -> 10;
            default -> 5;
        };
    }

    private static int fallbackDepth(BehaviorNode node) {
        if (node.kind() == BehaviorNodeKind.INITIAL_STATE) return 0;
        if (node.kind() == BehaviorNodeKind.FINAL_STATE) return Math.max(2, node.orderIndex());
        return Math.max(1, node.orderIndex());
    }

    private static boolean contains(List<BehaviorNode> nodes, String id) {
        return nodes.stream().anyMatch(node -> node.id().equals(id));
    }

    private record DiagramSize(double width, double height) { }
}
