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
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * Auto-layout semántico para BPMN básico y flujo operativo.
 *
 * <p>No usa un grafo de fuerza genérico: mantiene carriles/responsables como
 * bandas y ordena el procedimiento de izquierda a derecha según el flujo.</p>
 */
public final class BusinessProcessAutoLayoutPolicy {

    public static final int BPMN_BASE = 50_000;
    public static final int OPERATIONAL_BASE = 70_000;
    public static final int ORDER_BUCKET = 1_000;
    public static final int NODE_OFFSET = 100;
    public static final int STEPS_PER_ROW = 5;

    private static final int FALLBACK_LANE = 0;
    private static final double LANE_ROW_HEIGHT = 132.0;
    private static final VisualTextFitPolicy TEXT_FIT = new VisualTextFitPolicy();


    public boolean supports(BehaviorDiagramDocument document) {
        if (document == null) return false;
        return document.diagramKind() == BehaviorDiagramKind.BPMN_BASIC
                || document.diagramKind() == BehaviorDiagramKind.OPERATIONAL_FLOW;
    }

    public List<VisualNodeReference> visualReferences(BehaviorDiagramDocument document) {
        Objects.requireNonNull(document, "document");
        int base = baseFor(document.diagramKind());
        List<BehaviorNode> lanes = lanes(document);
        Map<String, Integer> laneIndexes = laneIndexes(lanes);
        Map<Integer, Integer> laneStepCounts = countStepsByLane(document, laneIndexes);
        List<BehaviorNode> orderedFlow = orderedFlowNodes(document);
        Map<String, Integer> sequenceIndexes = sequenceIndexes(orderedFlow);
        List<VisualNodeReference> references = new ArrayList<>();
        for (int index = 0; index < lanes.size(); index++) {
            BehaviorNode lane = lanes.get(index);
            int steps = Math.max(1, laneStepCounts.getOrDefault(index, 0));
            references.add(reference(document.diagramKind(), lane, base + index * ORDER_BUCKET, steps));
        }
        for (BehaviorNode node : document.nodes()) {
            if (node.kind() == BehaviorNodeKind.LANE) continue;
            int laneIndex = laneIndexFor(node, laneIndexes, lanes.isEmpty());
            int sequence = sequenceIndexes.getOrDefault(node.id(), Math.max(0, node.orderIndex()));
            references.add(reference(document.diagramKind(), node, base + laneIndex * ORDER_BUCKET + NODE_OFFSET + sequence, 1));
        }
        return references;
    }

    private static VisualNodeReference reference(BehaviorDiagramKind diagramKind, BehaviorNode node,
                                                 int orderIndex, int relatedSteps) {
        DiagramSize base = sizeFor(diagramKind, node.kind(), relatedSteps);
        VisualTextFitPolicy.BoxSize fitted = node.kind() == BehaviorNodeKind.DECISION
                ? TEXT_FIT.fitDiamond(new VisualTextFitPolicy.BoxSize(base.width(), base.height()), node.displayName(), node.description())
                : TEXT_FIT.fitCard(new VisualTextFitPolicy.BoxSize(base.width(), base.height()), node.displayName(), node.description());
        return new VisualNodeReference(VisualElementLayoutIds.behaviorNode(node.id()),
                fitted.width(), fitted.height(), orderIndex);
    }

    private static DiagramSize sizeFor(BehaviorDiagramKind diagramKind, BehaviorNodeKind nodeKind, int relatedSteps) {
        if (nodeKind == BehaviorNodeKind.LANE) {
            int visibleSteps = Math.min(STEPS_PER_ROW, Math.max(1, relatedSteps));
            int rows = (int) Math.ceil(Math.max(1, relatedSteps) / (double) STEPS_PER_ROW);
            double width = Math.max(640.0, 260.0 + visibleSteps * 250.0);
            double baseHeight = diagramKind == BehaviorDiagramKind.BPMN_BASIC ? 188.0 : 176.0;
            return new DiagramSize(width, baseHeight + Math.max(0, rows - 1) * LANE_ROW_HEIGHT);
        }
        if (diagramKind == BehaviorDiagramKind.BPMN_BASIC) {
            return switch (nodeKind) {
                case START_EVENT, END_EVENT -> new DiagramSize(96.0, 76.0);
                case DECISION -> new DiagramSize(128.0, 104.0);
                case NOTE -> new DiagramSize(170.0, 86.0);
                default -> new DiagramSize(190.0, 82.0);
            };
        }
        return switch (nodeKind) {
            case START_EVENT, END_EVENT -> new DiagramSize(120.0, 72.0);
            case DECISION -> new DiagramSize(150.0, 104.0);
            case NOTE -> new DiagramSize(170.0, 86.0);
            default -> new DiagramSize(210.0, 86.0);
        };
    }

    private static List<BehaviorNode> lanes(BehaviorDiagramDocument document) {
        return document.nodes().stream()
                .filter(node -> node.kind() == BehaviorNodeKind.LANE)
                .sorted(Comparator.comparingInt(BehaviorNode::orderIndex).thenComparing(BehaviorNode::displayName))
                .toList();
    }

    private static Map<String, Integer> laneIndexes(List<BehaviorNode> lanes) {
        Map<String, Integer> indexes = new LinkedHashMap<>();
        for (int index = 0; index < lanes.size(); index++) {
            BehaviorNode lane = lanes.get(index);
            indexes.put(normalize(lane.id()), index);
            indexes.put(normalize(lane.displayName()), index);
            indexes.put(normalize(lane.owner()), index);
        }
        return indexes;
    }

    private static Map<Integer, Integer> countStepsByLane(BehaviorDiagramDocument document, Map<String, Integer> laneIndexes) {
        Map<Integer, Integer> counts = new LinkedHashMap<>();
        for (BehaviorNode node : document.nodes()) {
            if (node.kind() == BehaviorNodeKind.LANE) continue;
            int laneIndex = laneIndexFor(node, laneIndexes, false);
            counts.put(laneIndex, counts.getOrDefault(laneIndex, 0) + 1);
        }
        return counts;
    }

    private static int laneIndexFor(BehaviorNode node, Map<String, Integer> laneIndexes, boolean noLanes) {
        if (noLanes || laneIndexes.isEmpty()) return FALLBACK_LANE;
        Integer byOwner = laneIndexes.get(normalize(node.owner()));
        if (byOwner != null) return byOwner;
        Integer byDescription = laneIndexes.get(normalize(node.description()));
        if (byDescription != null) return byDescription;
        return FALLBACK_LANE;
    }

    private static List<BehaviorNode> orderedFlowNodes(BehaviorDiagramDocument document) {
        Map<String, BehaviorNode> nodes = new LinkedHashMap<>();
        for (BehaviorNode node : document.nodes()) {
            if (node.kind() != BehaviorNodeKind.LANE) nodes.put(node.id(), node);
        }
        Map<String, List<String>> outgoing = new LinkedHashMap<>();
        Map<String, Integer> indegree = new LinkedHashMap<>();
        nodes.keySet().forEach(id -> indegree.put(id, 0));
        for (BehaviorEdge edge : document.edges()) {
            if (!nodes.containsKey(edge.sourceNodeId()) || !nodes.containsKey(edge.targetNodeId())) continue;
            outgoing.computeIfAbsent(edge.sourceNodeId(), ignored -> new ArrayList<>()).add(edge.targetNodeId());
            indegree.put(edge.targetNodeId(), indegree.getOrDefault(edge.targetNodeId(), 0) + 1);
        }
        ArrayDeque<String> queue = new ArrayDeque<>();
        nodes.values().stream()
                .filter(node -> node.kind() == BehaviorNodeKind.START_EVENT || indegree.getOrDefault(node.id(), 0) == 0)
                .sorted(Comparator.comparingInt(BehaviorNode::orderIndex).thenComparing(BehaviorNode::displayName))
                .forEach(node -> queue.add(node.id()));
        List<BehaviorNode> ordered = new ArrayList<>();
        while (!queue.isEmpty()) {
            String id = queue.removeFirst();
            if (!nodes.containsKey(id) || ordered.stream().anyMatch(node -> node.id().equals(id))) continue;
            ordered.add(nodes.get(id));
            for (String target : outgoing.getOrDefault(id, List.of())) {
                int next = indegree.getOrDefault(target, 1) - 1;
                indegree.put(target, next);
                if (next <= 0) queue.addLast(target);
            }
        }
        for (BehaviorNode node : nodes.values()) {
            if (ordered.stream().noneMatch(existing -> existing.id().equals(node.id()))) ordered.add(node);
        }
        return ordered;
    }

    private static Map<String, Integer> sequenceIndexes(List<BehaviorNode> nodes) {
        Map<String, Integer> indexes = new LinkedHashMap<>();
        for (int index = 0; index < nodes.size(); index++) indexes.put(nodes.get(index).id(), index);
        return indexes;
    }

    private static int baseFor(BehaviorDiagramKind diagramKind) {
        return diagramKind == BehaviorDiagramKind.OPERATIONAL_FLOW ? OPERATIONAL_BASE : BPMN_BASE;
    }

    private static String normalize(String value) {
        return value == null ? "" : value.strip().toLowerCase(Locale.ROOT);
    }

    private record DiagramSize(double width, double height) { }
}
