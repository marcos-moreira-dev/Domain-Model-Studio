package com.marcosmoreira.domainmodelstudio.application.visual;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramPoint;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/** Generador sobrio de posiciones iniciales para cajas aún no ubicadas por el usuario. */
public final class DefaultVisualLayoutGenerator {

    private static final double START_X = 80.0;
    private static final double START_Y = 64.0;
    private static final double COLUMN_GAP = 250.0;
    private static final double ROW_GAP = 155.0;
    private static final int COLUMNS = 3;

    private static final int UML_CLASS_ORDER_BUCKET = 1000;
    private static final double UML_MODULE_COLUMN_GAP = 260.0;
    private static final double UML_MODULE_ROW_GAP = 240.0;
    private static final int UML_MODULE_COLUMNS = 2;
    private static final double UML_CLASS_START_X = 48.0;
    private static final double UML_CLASS_START_Y = 92.0;
    private static final double UML_CLASS_COLUMN_GAP = 72.0;
    private static final double UML_CLASS_ROW_GAP = 72.0;
    private static final int UML_CLASS_COLUMNS = 3;

    private static final int ADMIN_ORDER_BUCKET = 1000;
    private static final double MODULE_GROUP_GAP = 620.0;
    private static final double MODULE_CHILD_START_X = 42.0;
    private static final double MODULE_CHILD_START_Y = 104.0;
    private static final double MODULE_CHILD_COLUMN_GAP = 238.0;
    private static final double MODULE_CHILD_ROW_GAP = 124.0;
    private static final int MODULE_CHILD_COLUMNS = 2;

    private static final double SCREEN_GROUP_GAP_X = 820.0;
    private static final double SCREEN_GROUP_GAP_Y = 360.0;
    private static final double SCREEN_COLUMN_GAP = 246.0;
    private static final int SCREEN_COLUMNS = 3;
    private static final int SCREEN_GROUP_COLUMNS = 2;

    private static final double WIREFRAME_SCREEN_GAP = 540.0;
    private static final double WIREFRAME_SCREEN_ROW_GAP = 520.0;
    private static final int WIREFRAME_SCREEN_COLUMNS = 2;
    private static final double WIREFRAME_COMPONENT_START_X = 26.0;
    private static final double WIREFRAME_COMPONENT_START_Y = 70.0;
    private static final double WIREFRAME_COMPONENT_COLUMN_GAP = 188.0;
    private static final double WIREFRAME_COMPONENT_ROW_GAP = 92.0;
    private static final int WIREFRAME_COMPONENT_COLUMNS = 2;

    private static final double USE_CASE_LEFT_ACTOR_X = 80.0;
    private static final double USE_CASE_BOUNDARY_X = 300.0;
    private static final double USE_CASE_BOUNDARY_Y = 64.0;
    private static final double USE_CASE_USE_CASE_START_X = 390.0;
    private static final double USE_CASE_USE_CASE_START_Y = 150.0;
    private static final double USE_CASE_USE_CASE_COLUMN_GAP = 270.0;
    private static final double USE_CASE_USE_CASE_ROW_GAP = 128.0;
    private static final double USE_CASE_RIGHT_ACTOR_X = 1120.0;
    private static final int USE_CASE_COLUMNS = 2;

    private static final double PROCESS_LANE_X = 64.0;
    private static final double PROCESS_LANE_Y = 72.0;
    private static final double PROCESS_LANE_GAP_Y = 400.0;
    private static final double PROCESS_NODE_START_X = 168.0;
    private static final double PROCESS_NODE_START_Y = 122.0;
    private static final double PROCESS_NODE_GAP_X = 250.0;
    private static final double PROCESS_NODE_ROW_GAP = 132.0;

    private static final double UML_ACTIVITY_CENTER_X = 360.0;
    private static final double UML_ACTIVITY_START_Y = 80.0;
    private static final double UML_ACTIVITY_GAP_Y = 150.0;
    private static final double UML_ACTIVITY_BRANCH_GAP_X = 230.0;
    private static final int UML_ACTIVITY_DEPTHS_PER_COLUMN = 7;
    private static final double UML_ACTIVITY_COLUMN_GAP_X = 560.0;

    private static final double UML_STATE_START_X = 92.0;
    private static final double UML_STATE_BASE_Y = 150.0;
    private static final double UML_STATE_GAP_X = 292.0;
    private static final double UML_STATE_BRANCH_GAP_Y = 138.0;
    private static final int UML_STATE_DEPTHS_PER_ROW = 6;
    private static final double UML_STATE_ROW_GAP_Y = 300.0;

    private static final double C4_CONTEXT_BOUNDARY_X = 300.0;
    private static final double C4_CONTEXT_BOUNDARY_Y = 64.0;
    private static final double C4_CONTEXT_LEFT_X = 80.0;
    private static final double C4_CONTEXT_CENTER_X = 450.0;
    private static final double C4_CONTEXT_RIGHT_X = 1120.0;
    private static final double C4_CONTEXT_ROW_Y = 160.0;
    private static final double C4_CONTEXT_ROW_GAP = 170.0;
    private static final double C4_CONTEXT_BOUNDARY_GAP_Y = 640.0;

    private static final double C4_CONTAINER_BOUNDARY_X = 260.0;
    private static final double C4_CONTAINER_BOUNDARY_Y = 64.0;
    private static final double C4_CONTAINER_APP_X = 330.0;
    private static final double C4_CONTAINER_API_X = 610.0;
    private static final double C4_CONTAINER_DB_X = 900.0;
    private static final double C4_CONTAINER_EXTERNAL_X = 1260.0;
    private static final double C4_CONTAINER_ROW_Y = 170.0;
    private static final double C4_CONTAINER_ROW_GAP = 168.0;
    private static final double C4_CONTAINER_BOUNDARY_GAP_Y = 600.0;

    private static final double FREE_GRAPH_CENTER_X = 560.0;
    private static final double FREE_GRAPH_CENTER_Y = 360.0;
    private static final double FREE_GRAPH_BASE_RADIUS = 220.0;
    private static final double FREE_GRAPH_RING_GAP = 230.0;
    private static final int FREE_GRAPH_NODES_PER_RING = 12;


    /**
     * Calcula posiciones iniciales en conjunto cuando el layout necesita respirar entre nodos.
     *
     * <p>UML Clases usa tamaños variables; si cada nodo se posiciona de forma aislada con una
     * grilla fija, las cajas grandes pueden quedar demasiado cerca o solapadas. Este método
     * mantiene el comportamiento clásico para los demás diagramas y usa una grilla dinámica
     * por módulo para UML Clases.</p>
     */
    public Map<DiagramElementId, DiagramPoint> positionsFor(List<VisualNodeReference> nodes) {
        Map<DiagramElementId, DiagramPoint> positions = new LinkedHashMap<>();
        if (nodes == null || nodes.isEmpty()) {
            return positions;
        }
        ArrayList<VisualNodeReference> umlNodes = new ArrayList<>();
        ArrayList<VisualNodeReference> freeGraphNodes = new ArrayList<>();
        ArrayList<VisualNodeReference> logicalGraphNodes = new ArrayList<>();
        for (VisualNodeReference node : nodes) {
            if (isUmlClassReference(node)) {
                umlNodes.add(node);
            } else if (isFreeGraphNodeReference(node)) {
                freeGraphNodes.add(node);
            } else if (isLogicalBusinessGraphNodeReference(node)) {
                logicalGraphNodes.add(node);
            } else {
                positions.put(node.layoutId(), positionFor(node));
            }
        }
        positions.putAll(freeGraphPositionsFor(freeGraphNodes));
        positions.putAll(logicalBusinessGraphPositionsFor(logicalGraphNodes));
        positions.putAll(umlClassPositionsFor(umlNodes));
        return positions;
    }

    public DiagramPoint positionFor(VisualNodeReference node) {
        String layoutId = node == null ? "" : node.layoutId().value();
        if (layoutId.startsWith("uml-")) {
            return umlClassPositionFor(node);
        }
        if (layoutId.startsWith("free-graph-node:")) {
            return freeGraphPositionFor(node, Math.max(1, node == null ? 1 : node.orderIndex() + 1));
        }
        if (layoutId.startsWith("logical-business-graph-node:")) {
            return logicalBusinessGraphPositionFor(node, node == null ? 0 : node.orderIndex());
        }
        if (layoutId.startsWith("module:")) {
            return moduleMapPositionFor(node);
        }
        if (layoutId.startsWith("screen:")) {
            return screenFlowPositionFor(node);
        }
        if (layoutId.startsWith("wireframe-screen:") || layoutId.startsWith("wireframe-component:")) {
            return wireframePositionFor(node);
        }
        if (layoutId.startsWith("behavior-node:") && useCaseOrder(node)) {
            return useCasePositionFor(node);
        }
        if (layoutId.startsWith("behavior-node:") && businessProcessOrder(node)) {
            return businessProcessPositionFor(node);
        }
        if (layoutId.startsWith("behavior-node:") && umlFlowOrder(node)) {
            return umlFlowPositionFor(node);
        }
        if (layoutId.startsWith("architecture-node:") && c4ArchitectureOrder(node)) {
            return c4ArchitecturePositionFor(node);
        }
        int order = node == null ? 0 : Math.max(0, node.orderIndex());
        int column = order % COLUMNS;
        int row = order / COLUMNS;
        return DiagramPoint.of(START_X + column * COLUMN_GAP, START_Y + row * ROW_GAP);
    }

    private static Map<DiagramElementId, DiagramPoint> logicalBusinessGraphPositionsFor(List<VisualNodeReference> references) {
        Map<DiagramElementId, DiagramPoint> positions = new LinkedHashMap<>();
        if (references == null || references.isEmpty()) {
            return positions;
        }
        Map<Integer, List<VisualNodeReference>> byColumn = new TreeMap<>();
        for (VisualNodeReference reference : references.stream()
                .sorted(java.util.Comparator.comparingInt(VisualNodeReference::orderIndex))
                .toList()) {
            byColumn.computeIfAbsent(logicalBusinessGraphColumn(reference.layoutId().value()), ignored -> new ArrayList<>())
                    .add(reference);
        }
        for (Map.Entry<Integer, List<VisualNodeReference>> entry : byColumn.entrySet()) {
            List<VisualNodeReference> columnNodes = entry.getValue();
            for (int row = 0; row < columnNodes.size(); row++) {
                VisualNodeReference node = columnNodes.get(row);
                positions.put(node.layoutId(), logicalBusinessGraphPositionFor(node, entry.getKey(), row));
            }
        }
        return positions;
    }

    private static DiagramPoint logicalBusinessGraphPositionFor(VisualNodeReference node, int orderIndex) {
        return logicalBusinessGraphPositionFor(node, logicalBusinessGraphColumn(node == null ? "" : node.layoutId().value()), orderIndex);
    }

    private static DiagramPoint logicalBusinessGraphPositionFor(VisualNodeReference node, int column, int row) {
        double width = node == null ? 226.0 : node.preferredWidth();
        double x = 80.0 + Math.max(0, column) * 300.0;
        double y = 86.0 + Math.max(0, row) * 160.0;
        return DiagramPoint.of(x - Math.min(0.0, width - 226.0) / 2.0, y);
    }

    private static int logicalBusinessGraphColumn(String layoutId) {
        String id = layoutId == null ? "" : layoutId.toUpperCase();
        if (id.contains(":MF-")) {
            return 0;
        }
        if (id.contains(":FL-")) {
            return 1;
        }
        if (id.contains(":CU-")) {
            return 2;
        }
        if (id.contains(":ACC-")) {
            return 3;
        }
        return 4;
    }

    private static Map<DiagramElementId, DiagramPoint> freeGraphPositionsFor(List<VisualNodeReference> references) {
        Map<DiagramElementId, DiagramPoint> positions = new LinkedHashMap<>();
        if (references == null || references.isEmpty()) {
            return positions;
        }
        List<VisualNodeReference> ordered = references.stream()
                .sorted(java.util.Comparator.comparingInt(VisualNodeReference::orderIndex))
                .toList();
        for (int i = 0; i < ordered.size(); i++) {
            positions.put(ordered.get(i).layoutId(), freeGraphPositionFor(ordered.get(i), ordered.size(), i));
        }
        return positions;
    }

    private static DiagramPoint freeGraphPositionFor(VisualNodeReference node, int totalNodes) {
        int index = Math.max(0, node == null ? 0 : node.orderIndex());
        return freeGraphPositionFor(node, totalNodes, index);
    }

    private static DiagramPoint freeGraphPositionFor(VisualNodeReference node, int totalNodes, int index) {
        double width = node == null ? 142.0 : node.preferredWidth();
        double height = node == null ? 142.0 : node.preferredHeight();
        int total = Math.max(1, totalNodes);
        if (total == 1) {
            return DiagramPoint.of(FREE_GRAPH_CENTER_X - width / 2.0, FREE_GRAPH_CENTER_Y - height / 2.0);
        }
        int ring = Math.max(0, index / FREE_GRAPH_NODES_PER_RING);
        int ringStart = ring * FREE_GRAPH_NODES_PER_RING;
        int remaining = Math.max(1, total - ringStart);
        int slotsInRing = Math.min(FREE_GRAPH_NODES_PER_RING, remaining);
        int slot = Math.max(0, index - ringStart);
        double radius = FREE_GRAPH_BASE_RADIUS + ring * FREE_GRAPH_RING_GAP;
        double angle = -Math.PI / 2.0 + (2.0 * Math.PI * slot / Math.max(1, slotsInRing));
        double x = FREE_GRAPH_CENTER_X + Math.cos(angle) * radius - width / 2.0;
        double y = FREE_GRAPH_CENTER_Y + Math.sin(angle) * radius - height / 2.0;
        return DiagramPoint.of(Math.max(40.0, x), Math.max(40.0, y));
    }

    private static DiagramPoint moduleMapPositionFor(VisualNodeReference node) {
        int order = Math.max(0, node.orderIndex());
        int groupIndex = order / ADMIN_ORDER_BUCKET;
        int localIndex = order % ADMIN_ORDER_BUCKET;
        double groupX = START_X + groupIndex * MODULE_GROUP_GAP;
        if (localIndex == 0) {
            return DiagramPoint.of(groupX, START_Y);
        }
        int childIndex = Math.max(0, localIndex - 1);
        int column = childIndex % MODULE_CHILD_COLUMNS;
        int row = childIndex / MODULE_CHILD_COLUMNS;
        return DiagramPoint.of(
                groupX + MODULE_CHILD_START_X + column * MODULE_CHILD_COLUMN_GAP,
                START_Y + MODULE_CHILD_START_Y + row * MODULE_CHILD_ROW_GAP);
    }

    private static DiagramPoint screenFlowPositionFor(VisualNodeReference node) {
        int order = Math.max(0, node.orderIndex());
        int groupIndex = order / ADMIN_ORDER_BUCKET;
        int localIndex = order % ADMIN_ORDER_BUCKET;
        int groupColumn = groupIndex % SCREEN_GROUP_COLUMNS;
        int groupRow = groupIndex / SCREEN_GROUP_COLUMNS;
        int column = localIndex % SCREEN_COLUMNS;
        int row = localIndex / SCREEN_COLUMNS;
        return DiagramPoint.of(
                START_X + groupColumn * SCREEN_GROUP_GAP_X + column * SCREEN_COLUMN_GAP,
                START_Y + groupRow * SCREEN_GROUP_GAP_Y + row * ROW_GAP);
    }

    private static DiagramPoint wireframePositionFor(VisualNodeReference node) {
        int order = Math.max(0, node.orderIndex());
        int groupIndex = order / ADMIN_ORDER_BUCKET;
        int localIndex = order % ADMIN_ORDER_BUCKET;
        int groupColumn = groupIndex % WIREFRAME_SCREEN_COLUMNS;
        int groupRow = groupIndex / WIREFRAME_SCREEN_COLUMNS;
        double screenX = START_X + groupColumn * WIREFRAME_SCREEN_GAP;
        double screenY = START_Y + groupRow * WIREFRAME_SCREEN_ROW_GAP;
        if (localIndex == 0 && node.layoutId().value().startsWith("wireframe-screen:")) {
            return DiagramPoint.of(screenX, screenY);
        }
        int componentIndex = Math.max(0, localIndex - 1);
        int column = componentIndex % WIREFRAME_COMPONENT_COLUMNS;
        int row = componentIndex / WIREFRAME_COMPONENT_COLUMNS;
        return DiagramPoint.of(
                screenX + WIREFRAME_COMPONENT_START_X + column * WIREFRAME_COMPONENT_COLUMN_GAP,
                screenY + WIREFRAME_COMPONENT_START_Y + row * WIREFRAME_COMPONENT_ROW_GAP);
    }


    private static boolean c4ArchitectureOrder(VisualNodeReference node) {
        int order = node == null ? 0 : Math.max(0, node.orderIndex());
        int contextEnd = C4ArchitectureAutoLayoutPolicy.CONTEXT_BASE
                + C4ArchitectureAutoLayoutPolicy.ORDER_BUCKET * 5;
        int containersEnd = C4ArchitectureAutoLayoutPolicy.CONTAINERS_BASE
                + C4ArchitectureAutoLayoutPolicy.ORDER_BUCKET * 5;
        return (order >= C4ArchitectureAutoLayoutPolicy.CONTEXT_BASE && order < contextEnd)
                || (order >= C4ArchitectureAutoLayoutPolicy.CONTAINERS_BASE && order < containersEnd);
    }

    private static DiagramPoint c4ArchitecturePositionFor(VisualNodeReference node) {
        int order = Math.max(0, node.orderIndex());
        boolean containers = order >= C4ArchitectureAutoLayoutPolicy.CONTAINERS_BASE;
        int base = containers ? C4ArchitectureAutoLayoutPolicy.CONTAINERS_BASE : C4ArchitectureAutoLayoutPolicy.CONTEXT_BASE;
        int encoded = order - base;
        int slot = encoded / C4ArchitectureAutoLayoutPolicy.ORDER_BUCKET;
        int index = encoded % C4ArchitectureAutoLayoutPolicy.ORDER_BUCKET;
        return containers ? c4ContainerPosition(slot, index) : c4ContextPosition(slot, index);
    }

    private static DiagramPoint c4ContextPosition(int slot, int index) {
        return switch (slot) {
            case C4ArchitectureAutoLayoutPolicy.BOUNDARY_SLOT -> DiagramPoint.of(
                    C4_CONTEXT_BOUNDARY_X, C4_CONTEXT_BOUNDARY_Y + index * C4_CONTEXT_BOUNDARY_GAP_Y);
            case C4ArchitectureAutoLayoutPolicy.LEFT_SLOT -> DiagramPoint.of(
                    C4_CONTEXT_LEFT_X, C4_CONTEXT_ROW_Y + index * C4_CONTEXT_ROW_GAP);
            case C4ArchitectureAutoLayoutPolicy.RIGHT_SLOT -> DiagramPoint.of(
                    C4_CONTEXT_RIGHT_X, C4_CONTEXT_ROW_Y + index * C4_CONTEXT_ROW_GAP);
            default -> DiagramPoint.of(
                    C4_CONTEXT_CENTER_X, C4_CONTEXT_ROW_Y + 60.0 + index * C4_CONTEXT_ROW_GAP);
        };
    }

    private static DiagramPoint c4ContainerPosition(int slot, int index) {
        return switch (slot) {
            case C4ArchitectureAutoLayoutPolicy.BOUNDARY_SLOT -> DiagramPoint.of(
                    C4_CONTAINER_BOUNDARY_X, C4_CONTAINER_BOUNDARY_Y + index * C4_CONTAINER_BOUNDARY_GAP_Y);
            case C4ArchitectureAutoLayoutPolicy.LEFT_SLOT -> DiagramPoint.of(
                    C4_CONTAINER_APP_X, C4_CONTAINER_ROW_Y + index * C4_CONTAINER_ROW_GAP);
            case C4ArchitectureAutoLayoutPolicy.CENTER_SLOT -> DiagramPoint.of(
                    C4_CONTAINER_API_X, C4_CONTAINER_ROW_Y + index * C4_CONTAINER_ROW_GAP);
            case C4ArchitectureAutoLayoutPolicy.RIGHT_SLOT -> DiagramPoint.of(
                    C4_CONTAINER_DB_X, C4_CONTAINER_ROW_Y + index * C4_CONTAINER_ROW_GAP);
            default -> DiagramPoint.of(
                    C4_CONTAINER_EXTERNAL_X, C4_CONTAINER_ROW_Y + index * C4_CONTAINER_ROW_GAP);
        };
    }

    private static boolean umlFlowOrder(VisualNodeReference node) {
        int order = node == null ? 0 : Math.max(0, node.orderIndex());
        int activityEnd = UmlFlowAutoLayoutPolicy.ACTIVITY_BASE + UmlFlowAutoLayoutPolicy.ORDER_BUCKET * 80;
        int stateEnd = UmlFlowAutoLayoutPolicy.STATE_BASE + UmlFlowAutoLayoutPolicy.ORDER_BUCKET * 80;
        return (order >= UmlFlowAutoLayoutPolicy.ACTIVITY_BASE && order < activityEnd)
                || (order >= UmlFlowAutoLayoutPolicy.STATE_BASE && order < stateEnd);
    }

    private static DiagramPoint umlFlowPositionFor(VisualNodeReference node) {
        int order = Math.max(0, node.orderIndex());
        boolean state = order >= UmlFlowAutoLayoutPolicy.STATE_BASE;
        int base = state ? UmlFlowAutoLayoutPolicy.STATE_BASE : UmlFlowAutoLayoutPolicy.ACTIVITY_BASE;
        int encoded = order - base;
        int depth = encoded / UmlFlowAutoLayoutPolicy.ORDER_BUCKET;
        int slot = encoded % UmlFlowAutoLayoutPolicy.ORDER_BUCKET;
        if (state) {
            int rowBand = depth / UML_STATE_DEPTHS_PER_ROW;
            int localDepth = depth % UML_STATE_DEPTHS_PER_ROW;
            return DiagramPoint.of(UML_STATE_START_X + localDepth * UML_STATE_GAP_X,
                    UML_STATE_BASE_Y + rowBand * UML_STATE_ROW_GAP_Y
                            + symmetricSlotOffset(slot) * UML_STATE_BRANCH_GAP_Y);
        }
        int columnBand = depth / UML_ACTIVITY_DEPTHS_PER_COLUMN;
        int localDepth = depth % UML_ACTIVITY_DEPTHS_PER_COLUMN;
        return DiagramPoint.of(UML_ACTIVITY_CENTER_X + columnBand * UML_ACTIVITY_COLUMN_GAP_X
                        + symmetricSlotOffset(slot) * UML_ACTIVITY_BRANCH_GAP_X,
                UML_ACTIVITY_START_Y + localDepth * UML_ACTIVITY_GAP_Y);
    }

    private static int symmetricSlotOffset(int slot) {
        if (slot <= 0) return 0;
        int magnitude = (slot + 1) / 2;
        return slot % 2 == 1 ? -magnitude : magnitude;
    }

    private static boolean businessProcessOrder(VisualNodeReference node) {
        int order = node == null ? 0 : Math.max(0, node.orderIndex());
        int bpmnEnd = BusinessProcessAutoLayoutPolicy.BPMN_BASE
                + BusinessProcessAutoLayoutPolicy.ORDER_BUCKET * 20;
        int operationalEnd = BusinessProcessAutoLayoutPolicy.OPERATIONAL_BASE
                + BusinessProcessAutoLayoutPolicy.ORDER_BUCKET * 20;
        return (order >= BusinessProcessAutoLayoutPolicy.BPMN_BASE && order < bpmnEnd)
                || (order >= BusinessProcessAutoLayoutPolicy.OPERATIONAL_BASE && order < operationalEnd);
    }

    private static DiagramPoint businessProcessPositionFor(VisualNodeReference node) {
        int order = Math.max(0, node.orderIndex());
        int base = order >= BusinessProcessAutoLayoutPolicy.OPERATIONAL_BASE
                ? BusinessProcessAutoLayoutPolicy.OPERATIONAL_BASE
                : BusinessProcessAutoLayoutPolicy.BPMN_BASE;
        int encoded = order - base;
        int laneIndex = encoded / BusinessProcessAutoLayoutPolicy.ORDER_BUCKET;
        int local = encoded % BusinessProcessAutoLayoutPolicy.ORDER_BUCKET;
        double laneY = PROCESS_LANE_Y + laneIndex * PROCESS_LANE_GAP_Y;
        if (local < BusinessProcessAutoLayoutPolicy.NODE_OFFSET) {
            return DiagramPoint.of(PROCESS_LANE_X, laneY);
        }
        int sequenceIndex = local - BusinessProcessAutoLayoutPolicy.NODE_OFFSET;
        int column = sequenceIndex % BusinessProcessAutoLayoutPolicy.STEPS_PER_ROW;
        int row = sequenceIndex / BusinessProcessAutoLayoutPolicy.STEPS_PER_ROW;
        return DiagramPoint.of(PROCESS_NODE_START_X + column * PROCESS_NODE_GAP_X,
                PROCESS_NODE_START_Y + laneIndex * PROCESS_LANE_GAP_Y + row * PROCESS_NODE_ROW_GAP);
    }

    private static boolean useCaseOrder(VisualNodeReference node) {
        int order = node == null ? 0 : Math.max(0, node.orderIndex());
        return order >= UmlUseCaseAutoLayoutPolicy.LEFT_ACTOR_BASE
                && order < UmlUseCaseAutoLayoutPolicy.RIGHT_ACTOR_BASE + UmlUseCaseAutoLayoutPolicy.ORDER_BUCKET;
    }

    private static DiagramPoint useCasePositionFor(VisualNodeReference node) {
        int order = Math.max(0, node.orderIndex());
        if (order >= UmlUseCaseAutoLayoutPolicy.RIGHT_ACTOR_BASE) {
            int index = order - UmlUseCaseAutoLayoutPolicy.RIGHT_ACTOR_BASE;
            return DiagramPoint.of(USE_CASE_RIGHT_ACTOR_X, USE_CASE_USE_CASE_START_Y + index * USE_CASE_USE_CASE_ROW_GAP);
        }
        if (order >= UmlUseCaseAutoLayoutPolicy.USE_CASE_BASE) {
            int index = order - UmlUseCaseAutoLayoutPolicy.USE_CASE_BASE;
            int column = index % USE_CASE_COLUMNS;
            int row = index / USE_CASE_COLUMNS;
            return DiagramPoint.of(
                    USE_CASE_USE_CASE_START_X + column * USE_CASE_USE_CASE_COLUMN_GAP,
                    USE_CASE_USE_CASE_START_Y + row * USE_CASE_USE_CASE_ROW_GAP);
        }
        if (order >= UmlUseCaseAutoLayoutPolicy.SYSTEM_BOUNDARY_BASE) {
            int index = order - UmlUseCaseAutoLayoutPolicy.SYSTEM_BOUNDARY_BASE;
            return DiagramPoint.of(USE_CASE_BOUNDARY_X, USE_CASE_BOUNDARY_Y + index * 620.0);
        }
        int index = Math.max(0, order - UmlUseCaseAutoLayoutPolicy.LEFT_ACTOR_BASE);
        return DiagramPoint.of(USE_CASE_LEFT_ACTOR_X, USE_CASE_USE_CASE_START_Y + index * USE_CASE_USE_CASE_ROW_GAP);
    }

    private static DiagramPoint umlClassPositionFor(VisualNodeReference node) {
        int order = Math.max(0, node.orderIndex());
        int groupIndex = order / UML_CLASS_ORDER_BUCKET;
        int localIndex = order % UML_CLASS_ORDER_BUCKET;
        int moduleColumn = groupIndex % UML_MODULE_COLUMNS;
        int moduleRow = groupIndex / UML_MODULE_COLUMNS;
        double groupX = START_X + moduleColumn * (720.0 + UML_MODULE_COLUMN_GAP);
        double groupY = START_Y + moduleRow * (640.0 + UML_MODULE_ROW_GAP);
        if (node.layoutId().value().startsWith("uml-module:")) {
            return DiagramPoint.of(groupX, groupY);
        }
        int classIndex = Math.max(0, localIndex - 1);
        int column = classIndex % UML_CLASS_COLUMNS;
        int row = classIndex / UML_CLASS_COLUMNS;
        return DiagramPoint.of(
                groupX + UML_CLASS_START_X + column * (280.0 + UML_CLASS_COLUMN_GAP),
                groupY + UML_CLASS_START_Y + row * (180.0 + UML_CLASS_ROW_GAP));
    }

    private static Map<DiagramElementId, DiagramPoint> umlClassPositionsFor(List<VisualNodeReference> references) {
        Map<DiagramElementId, DiagramPoint> positions = new LinkedHashMap<>();
        if (references == null || references.isEmpty()) {
            return positions;
        }
        TreeMap<Integer, VisualNodeReference> modulesByGroup = new TreeMap<>();
        TreeMap<Integer, List<VisualNodeReference>> classesByGroup = new TreeMap<>();
        for (VisualNodeReference reference : references) {
            int groupIndex = groupIndex(reference);
            int localIndex = localIndex(reference);
            if (localIndex == 0 && reference.layoutId().value().startsWith("uml-module:")) {
                modulesByGroup.put(groupIndex, reference);
            } else {
                classesByGroup.computeIfAbsent(groupIndex, key -> new ArrayList<>()).add(reference);
            }
        }
        TreeMap<Integer, Boolean> groups = new TreeMap<>();
        modulesByGroup.keySet().forEach(group -> groups.put(group, Boolean.TRUE));
        classesByGroup.keySet().forEach(group -> groups.put(group, Boolean.TRUE));
        List<Integer> orderedGroups = new ArrayList<>(groups.keySet());
        double[] columnWidths = moduleColumnWidths(orderedGroups, modulesByGroup, classesByGroup);
        double[] columnX = new double[UML_MODULE_COLUMNS];
        columnX[0] = START_X;
        for (int column = 1; column < UML_MODULE_COLUMNS; column++) {
            columnX[column] = columnX[column - 1] + columnWidths[column - 1] + UML_MODULE_COLUMN_GAP;
        }
        double rowY = START_Y;
        for (int rowStart = 0; rowStart < orderedGroups.size(); rowStart += UML_MODULE_COLUMNS) {
            double rowHeight = 0.0;
            for (int column = 0; column < UML_MODULE_COLUMNS && rowStart + column < orderedGroups.size(); column++) {
                int group = orderedGroups.get(rowStart + column);
                rowHeight = Math.max(rowHeight, moduleHeight(group, modulesByGroup, classesByGroup));
            }
            for (int column = 0; column < UML_MODULE_COLUMNS && rowStart + column < orderedGroups.size(); column++) {
                int group = orderedGroups.get(rowStart + column);
                double groupX = columnX[column];
                VisualNodeReference module = modulesByGroup.get(group);
                if (module != null) {
                    positions.put(module.layoutId(), DiagramPoint.of(groupX, rowY));
                }
                addClassPositions(positions, groupX, rowY, classesByGroup.getOrDefault(group, List.of()));
            }
            rowY += rowHeight + UML_MODULE_ROW_GAP;
        }
        return positions;
    }

    private static double[] moduleColumnWidths(List<Integer> orderedGroups,
                                               Map<Integer, VisualNodeReference> modulesByGroup,
                                               Map<Integer, List<VisualNodeReference>> classesByGroup) {
        double[] widths = new double[UML_MODULE_COLUMNS];
        for (int index = 0; index < orderedGroups.size(); index++) {
            int column = index % UML_MODULE_COLUMNS;
            int group = orderedGroups.get(index);
            widths[column] = Math.max(widths[column], moduleWidth(group, modulesByGroup, classesByGroup));
        }
        for (int column = 0; column < widths.length; column++) {
            widths[column] = Math.max(widths[column], 720.0);
        }
        return widths;
    }

    private static double moduleWidth(int group, Map<Integer, VisualNodeReference> modulesByGroup,
                                      Map<Integer, List<VisualNodeReference>> classesByGroup) {
        VisualNodeReference module = modulesByGroup.get(group);
        double childrenWidth = classGridSize(classesByGroup.getOrDefault(group, List.of())).width() + UML_CLASS_START_X * 2.0;
        return module == null ? childrenWidth : Math.max(module.preferredWidth(), childrenWidth);
    }

    private static double moduleHeight(int group, Map<Integer, VisualNodeReference> modulesByGroup,
                                       Map<Integer, List<VisualNodeReference>> classesByGroup) {
        VisualNodeReference module = modulesByGroup.get(group);
        double childrenHeight = classGridSize(classesByGroup.getOrDefault(group, List.of())).height()
                + UML_CLASS_START_Y + UML_CLASS_START_X;
        return module == null ? childrenHeight : Math.max(module.preferredHeight(), childrenHeight);
    }

    private static void addClassPositions(Map<DiagramElementId, DiagramPoint> positions, double groupX, double groupY,
                                          List<VisualNodeReference> classes) {
        if (classes == null || classes.isEmpty()) {
            return;
        }
        List<VisualNodeReference> ordered = classes.stream()
                .sorted(java.util.Comparator.comparingInt(DefaultVisualLayoutGenerator::localIndex))
                .toList();
        int columns = umlClassColumns(ordered.size());
        double[] columnWidths = columnWidths(ordered, columns);
        double[] rowHeights = rowHeights(ordered, columns);
        for (int index = 0; index < ordered.size(); index++) {
            int column = index % columns;
            int row = index / columns;
            double x = groupX + UML_CLASS_START_X + sum(columnWidths, 0, column) + column * UML_CLASS_COLUMN_GAP;
            double y = groupY + UML_CLASS_START_Y + sum(rowHeights, 0, row) + row * UML_CLASS_ROW_GAP;
            positions.put(ordered.get(index).layoutId(), DiagramPoint.of(x, y));
        }
    }

    private static GridSize classGridSize(List<VisualNodeReference> classes) {
        if (classes == null || classes.isEmpty()) {
            return new GridSize(0.0, 0.0);
        }
        List<VisualNodeReference> ordered = classes.stream()
                .sorted(java.util.Comparator.comparingInt(DefaultVisualLayoutGenerator::localIndex))
                .toList();
        int columns = umlClassColumns(ordered.size());
        double[] columnWidths = columnWidths(ordered, columns);
        double[] rowHeights = rowHeights(ordered, columns);
        return new GridSize(
                sum(columnWidths, 0, columnWidths.length) + Math.max(0, columnWidths.length - 1) * UML_CLASS_COLUMN_GAP,
                sum(rowHeights, 0, rowHeights.length) + Math.max(0, rowHeights.length - 1) * UML_CLASS_ROW_GAP);
    }

    private static int umlClassColumns(int classCount) {
        if (classCount <= 4) {
            return 1;
        }
        if (classCount <= 14) {
            return 2;
        }
        return UML_CLASS_COLUMNS;
    }

    private static double[] columnWidths(List<VisualNodeReference> nodes, int columns) {
        double[] widths = new double[Math.max(1, columns)];
        for (int index = 0; index < nodes.size(); index++) {
            int column = index % widths.length;
            widths[column] = Math.max(widths[column], nodes.get(index).preferredWidth());
        }
        return widths;
    }

    private static double[] rowHeights(List<VisualNodeReference> nodes, int columns) {
        int rowCount = Math.max(1, (int) Math.ceil(nodes.size() / (double) Math.max(1, columns)));
        double[] heights = new double[rowCount];
        for (int index = 0; index < nodes.size(); index++) {
            int row = index / Math.max(1, columns);
            heights[row] = Math.max(heights[row], nodes.get(index).preferredHeight());
        }
        return heights;
    }

    private static double sum(double[] values, int startInclusive, int endExclusive) {
        double total = 0.0;
        for (int index = Math.max(0, startInclusive); index < Math.min(values.length, endExclusive); index++) {
            total += values[index];
        }
        return total;
    }

    private static boolean isUmlClassReference(VisualNodeReference reference) {
        return reference != null && reference.layoutId().value().startsWith("uml-");
    }

    private static boolean isLogicalBusinessGraphNodeReference(VisualNodeReference reference) {
        return reference != null && reference.layoutId().value().startsWith("logical-business-graph-node:");
    }

    private static boolean isFreeGraphNodeReference(VisualNodeReference reference) {
        return reference != null && reference.layoutId().value().startsWith("free-graph-node:");
    }

    private static int groupIndex(VisualNodeReference reference) {
        return Math.max(0, reference.orderIndex()) / UML_CLASS_ORDER_BUCKET;
    }

    private static int localIndex(VisualNodeReference reference) {
        return Math.max(0, reference.orderIndex()) % UML_CLASS_ORDER_BUCKET;
    }

    private record GridSize(double width, double height) {
    }
}

