package com.marcosmoreira.domainmodelstudio.application.visual;

import com.marcosmoreira.domainmodelstudio.application.behavior.SequenceMessageOrderPolicy;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramKind;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorEdge;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNode;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNodeKind;
import com.marcosmoreira.domainmodelstudio.domain.behavior.SequenceCombinedFragmentSpec;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramPoint;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramSize;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Optional;

/**
 * Política temporal para UML Secuencia.
 *
 * <p>La posición vertical de los mensajes se calcula por orden temporal, no por
 * bend points ni por layout de grafo. Los participantes conservan posición
 * horizontal manual, las líneas de vida crecen hasta cubrir todos los mensajes
 * y los fragmentos se renderizan como marcos temporales.</p>
 */
public final class SequenceTimelineLayoutPolicy {

    public static final double PARTICIPANT_TOP_Y = 72.0;
    public static final double PARTICIPANT_WIDTH = 210.0;
    public static final double PARTICIPANT_HEIGHT = 58.0;
    public static final double PARTICIPANT_GAP = 118.0;
    public static final double MESSAGE_START_Y = 180.0;
    public static final double MESSAGE_ROW_GAP = 84.0;
    public static final double NOTE_WIDTH = 220.0;
    public static final double NOTE_HEIGHT = 78.0;
    public static final double ACTIVATION_WIDTH = 20.0;
    public static final double ACTIVATION_HEIGHT = 96.0;
    public static final double FRAGMENT_WIDTH = 620.0;
    public static final double FRAGMENT_HEIGHT = 150.0;
    public static final double FRAGMENT_HEADER_HEIGHT = 34.0;
    public static final double FRAGMENT_MIN_OPERAND_HEIGHT = 54.0;
    public static final double FRAGMENT_NESTING_INDENT = 24.0;

    private final SequenceMessageOrderPolicy orderPolicy = new SequenceMessageOrderPolicy();

    public boolean supports(BehaviorDiagramDocument document) {
        return document != null && document.diagramKind() == BehaviorDiagramKind.UML_SEQUENCE;
    }

    public VisualNodeReference visualReference(BehaviorNode node, int fallbackIndex) {
        int order = node.orderIndex() > 0 ? node.orderIndex() : Math.max(0, fallbackIndex);
        double width = switch (node.kind()) {
            case PARTICIPANT -> PARTICIPANT_WIDTH;
            case ACTIVATION -> ACTIVATION_WIDTH;
            case FRAGMENT -> FRAGMENT_WIDTH;
            case NOTE -> NOTE_WIDTH;
            default -> NOTE_WIDTH;
        };
        double height = switch (node.kind()) {
            case PARTICIPANT -> Math.max(PARTICIPANT_HEIGHT, 360.0);
            case ACTIVATION -> ACTIVATION_HEIGHT;
            case FRAGMENT -> fragmentHeight(SequenceCombinedFragmentSpec.fromNode(node), order);
            case NOTE -> NOTE_HEIGHT;
            default -> NOTE_HEIGHT;
        };
        return new VisualNodeReference(VisualElementLayoutIds.behaviorNode(node.id()), width, height, order);
    }

    public NodeLayout layoutForNode(BehaviorDiagramDocument document, BehaviorNode node, NodeLayout base) {
        if (node.kind() == BehaviorNodeKind.PARTICIPANT) {
            return participantLayout(document, node, base);
        }
        if (node.kind() == BehaviorNodeKind.ACTIVATION) {
            return activationLayout(document, node, base);
        }
        if (node.kind() == BehaviorNodeKind.FRAGMENT) {
            return fragmentLayout(document, node, base);
        }
        return noteLayout(node, base);
    }

    public double messageY(BehaviorDiagramDocument document, String edgeId) {
        int index = orderPolicy.temporalIndex(document, rawId(edgeId));
        return MESSAGE_START_Y + Math.max(0, index) * MESSAGE_ROW_GAP;
    }

    public int messageNumber(BehaviorDiagramDocument document, String edgeId) {
        return orderPolicy.displayNumber(document, rawId(edgeId));
    }

    public List<BehaviorEdge> orderedMessages(BehaviorDiagramDocument document) {
        return orderPolicy.orderedMessages(document);
    }

    public List<BehaviorNode> participants(BehaviorDiagramDocument document) {
        return orderedParticipants(document);
    }

    public double sequenceBottomY(BehaviorDiagramDocument document) {
        int messageRows = Math.max(3, orderedMessages(document).size());
        double messageBottom = MESSAGE_START_Y + messageRows * MESSAGE_ROW_GAP + 80.0;
        double fragmentBottom = document == null ? 0.0 : document.nodes().stream()
                .filter(node -> node.kind() == BehaviorNodeKind.FRAGMENT)
                .mapToDouble(node -> fragmentY(SequenceCombinedFragmentSpec.fromNode(node), node, null, -34.0)
                        + fragmentHeight(SequenceCombinedFragmentSpec.fromNode(node), Math.max(1, node.orderIndex())))
                .max()
                .orElse(0.0);
        int nodeRows = document == null ? 0 : document.nodes().stream()
                .mapToInt(BehaviorNode::orderIndex)
                .max()
                .orElse(0);
        double nodeBottom = MESSAGE_START_Y + Math.max(0, nodeRows) * MESSAGE_ROW_GAP + FRAGMENT_HEIGHT;
        return Math.max(Math.max(messageBottom, nodeBottom), fragmentBottom + 80.0);
    }

    public String temporalLabel(BehaviorDiagramDocument document, String edgeId, String rawLabel) {
        int number = messageNumber(document, edgeId);
        String label = rawLabel == null ? "" : rawLabel.strip();
        if (number <= 0) {
            return label;
        }
        return number + ". " + label;
    }

    private NodeLayout participantLayout(BehaviorDiagramDocument document, BehaviorNode node, NodeLayout base) {
        double x = Math.abs(base.y() - PARTICIPANT_TOP_Y) < 0.01 ? base.x() : defaultParticipantX(document, node.id());
        return new NodeLayout(
                VisualElementLayoutIds.behaviorNode(node.id()),
                DiagramPoint.of(Math.max(32.0, x), PARTICIPANT_TOP_Y),
                DiagramSize.of(PARTICIPANT_WIDTH, lifelineHeight(document)),
                true,
                false);
    }

    private NodeLayout activationLayout(BehaviorDiagramDocument document, BehaviorNode node, NodeLayout base) {
        double x = base.x();
        Optional<BehaviorNode> owner = participantByOwner(document, node.owner());
        if (owner.isPresent()) {
            NodeLayout ownerLayout = participantLayout(document, owner.get(), base);
            x = ownerLayout.x() + ownerLayout.width() / 2.0 - ACTIVATION_WIDTH / 2.0;
        }
        double y = temporalNodeY(node, base, -12.0);
        return new NodeLayout(
                VisualElementLayoutIds.behaviorNode(node.id()),
                DiagramPoint.of(x, y),
                DiagramSize.of(ACTIVATION_WIDTH, ACTIVATION_HEIGHT),
                true,
                false);
    }

    private NodeLayout fragmentLayout(BehaviorDiagramDocument document, BehaviorNode node, NodeLayout base) {
        SequenceCombinedFragmentSpec spec = SequenceCombinedFragmentSpec.fromNode(node);
        int nestingLevel = effectiveFragmentNestingLevel(document, node, spec);
        double nestingOffset = nestingLevel * FRAGMENT_NESTING_INDENT;
        double automaticX = firstParticipantX(document).orElse(base == null ? 44.0 : Math.max(44.0, base.x()))
                - 32.0 + nestingOffset;
        double automaticY = fragmentY(spec, node, base, -34.0);
        double x = base != null && base.locked() ? base.x() : automaticX;
        double y = base != null && base.locked() ? base.y() : automaticY;
        double automaticWidth = Math.max(340.0, fragmentWidth(document) - nestingOffset * 2.0);
        double automaticHeight = fragmentHeight(spec, Math.max(1, node.orderIndex()));
        double width = Math.max(automaticWidth, base == null ? 0.0 : base.width());
        double height = Math.max(automaticHeight, base == null ? 0.0 : base.height());
        return new NodeLayout(
                VisualElementLayoutIds.behaviorNode(node.id()),
                DiagramPoint.of(Math.max(28.0, x), Math.max(PARTICIPANT_TOP_Y + PARTICIPANT_HEIGHT + 16.0, y)),
                DiagramSize.of(width, height),
                true,
                base != null && base.locked());
    }

    private NodeLayout noteLayout(BehaviorNode node, NodeLayout base) {
        double y = Math.max(MESSAGE_START_Y, base.y());
        return new NodeLayout(
                VisualElementLayoutIds.behaviorNode(node.id()),
                DiagramPoint.of(base.x(), y),
                DiagramSize.of(NOTE_WIDTH, NOTE_HEIGHT),
                true,
                false);
    }

    private double temporalNodeY(BehaviorNode node, NodeLayout base, double offset) {
        int order = Math.max(0, node.orderIndex());
        if (order > 0) {
            return MESSAGE_START_Y + order * MESSAGE_ROW_GAP + offset;
        }
        return Math.max(MESSAGE_START_Y + offset, base.y());
    }

    private static double fragmentY(SequenceCombinedFragmentSpec spec, BehaviorNode node, NodeLayout base, double offset) {
        int start = spec.effectiveStartIndex(Math.max(1, node.orderIndex()));
        if (start > 0) {
            return MESSAGE_START_Y + Math.max(0, start - 1) * MESSAGE_ROW_GAP + offset;
        }
        if (base == null) {
            return MESSAGE_START_Y + offset;
        }
        return Math.max(MESSAGE_START_Y + offset, base.y());
    }

    private static double fragmentHeight(SequenceCombinedFragmentSpec spec, int fallbackIndex) {
        int start = spec.effectiveStartIndex(fallbackIndex);
        int end = spec.effectiveEndIndex(fallbackIndex);
        int rows = Math.max(1, end - start + 1);
        int operandCount = Math.max(1, spec.operands().size());
        double byMessages = rows * MESSAGE_ROW_GAP + FRAGMENT_HEADER_HEIGHT + 40.0;
        double byOperands = FRAGMENT_HEADER_HEIGHT + operandCount * FRAGMENT_MIN_OPERAND_HEIGHT + 20.0;
        return Math.max(FRAGMENT_HEIGHT, Math.max(byMessages, byOperands));
    }

    private Optional<BehaviorNode> participantByOwner(BehaviorDiagramDocument document, String owner) {
        String normalized = normalize(owner);
        if (document == null || normalized.isBlank()) {
            return Optional.empty();
        }
        return document.nodes().stream()
                .filter(node -> node.kind() == BehaviorNodeKind.PARTICIPANT)
                .filter(node -> node.id().equalsIgnoreCase(normalized)
                        || normalize(node.displayName()).equals(normalized))
                .findFirst();
    }

    private Optional<Double> firstParticipantX(BehaviorDiagramDocument document) {
        if (document == null) {
            return Optional.empty();
        }
        return orderedParticipants(document).stream()
                .map(node -> defaultParticipantX(document, node.id()))
                .min(Double::compareTo);
    }

    private double defaultParticipantX(BehaviorDiagramDocument document, String nodeId) {
        if (document == null) {
            return 80.0;
        }
        List<BehaviorNode> participants = orderedParticipants(document);
        for (int index = 0; index < participants.size(); index++) {
            if (participants.get(index).id().equals(nodeId)) {
                return 80.0 + index * participantStep(document);
            }
        }
        return 80.0;
    }


    private List<BehaviorNode> orderedParticipants(BehaviorDiagramDocument document) {
        if (document == null) {
            return List.of();
        }
        Map<String, Integer> firstMessageIndex = firstMessageIndexByParticipant(document);
        return document.nodes().stream()
                .filter(node -> node.kind() == BehaviorNodeKind.PARTICIPANT)
                .sorted(Comparator
                        .comparingInt((BehaviorNode node) -> firstMessageIndex.getOrDefault(node.id(), Integer.MAX_VALUE))
                        .thenComparingInt(BehaviorNode::orderIndex)
                        .thenComparing(BehaviorNode::displayName))
                .toList();
    }

    private Map<String, Integer> firstMessageIndexByParticipant(BehaviorDiagramDocument document) {
        Map<String, Integer> result = new HashMap<>();
        int appearanceIndex = 0;
        for (BehaviorEdge edge : orderedMessages(document)) {
            if (!result.containsKey(edge.sourceNodeId())) {
                result.put(edge.sourceNodeId(), appearanceIndex++);
            }
            if (!result.containsKey(edge.targetNodeId())) {
                result.put(edge.targetNodeId(), appearanceIndex++);
            }
        }
        return result;
    }

    private double participantStep(BehaviorDiagramDocument document) {
        int maxNameLength = orderedParticipants(document).stream()
                .mapToInt(node -> node.displayName().length())
                .max()
                .orElse(0);
        double extra = Math.min(92.0, Math.max(0.0, (maxNameLength - 18) * 4.5));
        return PARTICIPANT_WIDTH + PARTICIPANT_GAP + extra;
    }

    private int effectiveFragmentNestingLevel(BehaviorDiagramDocument document, BehaviorNode node, SequenceCombinedFragmentSpec spec) {
        if (spec.nestingLevel() > 0) {
            return Math.min(3, spec.nestingLevel());
        }
        if (spec.parentId().isBlank()) {
            return 0;
        }
        return Math.min(3, parentDepth(document, node, spec.parentId(), new LinkedHashSet<>()));
    }

    private int parentDepth(BehaviorDiagramDocument document, BehaviorNode current, String parentId, Set<String> visited) {
        if (document == null || parentId == null || parentId.isBlank()) {
            return 0;
        }
        String parentKey = stableKey(parentId);
        if (parentKey.isBlank() || !visited.add(parentKey)) {
            return 0;
        }
        Optional<BehaviorNode> parent = document.nodes().stream()
                .filter(candidate -> candidate.kind() == BehaviorNodeKind.FRAGMENT)
                .filter(candidate -> !candidate.id().equals(current.id()))
                .filter(candidate -> stableKey(candidate.id()).equals(parentKey))
                .findFirst();
        if (parent.isEmpty()) {
            return 1;
        }
        SequenceCombinedFragmentSpec parentSpec = SequenceCombinedFragmentSpec.fromNode(parent.get());
        return 1 + parentDepth(document, parent.get(), parentSpec.parentId(), visited);
    }

    private double fragmentWidth(BehaviorDiagramDocument document) {
        List<BehaviorNode> ordered = orderedParticipants(document);
        if (ordered.isEmpty()) {
            return FRAGMENT_WIDTH;
        }
        double firstX = defaultParticipantX(document, ordered.get(0).id());
        double lastX = defaultParticipantX(document, ordered.get(ordered.size() - 1).id());
        return Math.max(FRAGMENT_WIDTH, lastX - firstX + PARTICIPANT_WIDTH + 64.0);
    }

    private double lifelineHeight(BehaviorDiagramDocument document) {
        return Math.max(360.0, sequenceBottomY(document) - PARTICIPANT_TOP_Y);
    }

    private static String rawId(String value) {
        String normalized = value == null ? "" : value.strip();
        String prefix = "behavior-edge:";
        return normalized.startsWith(prefix) ? normalized.substring(prefix.length()) : normalized;
    }

    private static String stableKey(String value) {
        return java.text.Normalizer.normalize(value == null ? "" : value, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(java.util.Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "_")
                .replaceAll("^_+|_+$", "");
    }

    private static String normalize(String value) {
        return value == null ? "" : value.strip().toLowerCase(java.util.Locale.ROOT);
    }
}
