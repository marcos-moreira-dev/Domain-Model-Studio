package com.marcosmoreira.domainmodelstudio.domain.behavior;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.List;
import java.util.Objects;

/** Familia de diagramas de comportamiento implementados con el editor funcional común. */
public enum BehaviorDiagramKind {
    BPMN_BASIC("BPMN básico", DiagramTypeId.BPMN_BASIC,
            List.of(BehaviorNodeKind.START_EVENT, BehaviorNodeKind.ACTIVITY, BehaviorNodeKind.DECISION, BehaviorNodeKind.END_EVENT, BehaviorNodeKind.LANE),
            List.of(BehaviorEdgeKind.FLOW)),
    OPERATIONAL_FLOW("Flujo operativo", DiagramTypeId.OPERATIONAL_FLOW,
            List.of(BehaviorNodeKind.ACTIVITY, BehaviorNodeKind.LANE, BehaviorNodeKind.DECISION, BehaviorNodeKind.NOTE, BehaviorNodeKind.START_EVENT, BehaviorNodeKind.END_EVENT),
            List.of(BehaviorEdgeKind.FLOW)),
    UML_USE_CASE("UML Casos de uso", DiagramTypeId.UML_USE_CASE,
            List.of(BehaviorNodeKind.ACTOR, BehaviorNodeKind.USE_CASE, BehaviorNodeKind.SYSTEM_BOUNDARY),
            List.of(BehaviorEdgeKind.ASSOCIATION, BehaviorEdgeKind.INCLUDE, BehaviorEdgeKind.EXTEND, BehaviorEdgeKind.GENERALIZATION)),
    UML_ACTIVITY("UML Actividad", DiagramTypeId.UML_ACTIVITY,
            List.of(BehaviorNodeKind.INITIAL_STATE, BehaviorNodeKind.ACTION, BehaviorNodeKind.DECISION, BehaviorNodeKind.FORK, BehaviorNodeKind.JOIN, BehaviorNodeKind.FINAL_STATE, BehaviorNodeKind.LANE),
            List.of(BehaviorEdgeKind.FLOW)),
    UML_SEQUENCE("UML Secuencia", DiagramTypeId.UML_SEQUENCE,
            List.of(BehaviorNodeKind.PARTICIPANT, BehaviorNodeKind.ACTIVATION, BehaviorNodeKind.FRAGMENT, BehaviorNodeKind.NOTE),
            List.of(BehaviorEdgeKind.MESSAGE, BehaviorEdgeKind.ASYNC_MESSAGE, BehaviorEdgeKind.RETURN_MESSAGE)),
    UML_STATE("UML Estados", DiagramTypeId.UML_STATE,
            List.of(BehaviorNodeKind.INITIAL_STATE, BehaviorNodeKind.STATE, BehaviorNodeKind.FINAL_STATE, BehaviorNodeKind.NOTE),
            List.of(BehaviorEdgeKind.TRANSITION));

    private final String displayName;
    private final DiagramTypeId diagramTypeId;
    private final List<BehaviorNodeKind> nodeKinds;
    private final List<BehaviorEdgeKind> edgeKinds;

    BehaviorDiagramKind(String displayName, DiagramTypeId diagramTypeId, List<BehaviorNodeKind> nodeKinds, List<BehaviorEdgeKind> edgeKinds) {
        this.displayName = Objects.requireNonNull(displayName, "displayName");
        this.diagramTypeId = Objects.requireNonNull(diagramTypeId, "diagramTypeId");
        this.nodeKinds = List.copyOf(nodeKinds);
        this.edgeKinds = List.copyOf(edgeKinds);
    }

    public String displayName() { return displayName; }
    public DiagramTypeId diagramTypeId() { return diagramTypeId; }
    public List<BehaviorNodeKind> nodeKinds() { return nodeKinds; }
    public List<BehaviorEdgeKind> edgeKinds() { return edgeKinds; }

    public BehaviorNodeKind defaultNodeKind() { return nodeKinds.isEmpty() ? BehaviorNodeKind.ACTIVITY : nodeKinds.get(0); }
    public BehaviorEdgeKind defaultEdgeKind() { return edgeKinds.isEmpty() ? BehaviorEdgeKind.FLOW : edgeKinds.get(0); }

    public static BehaviorDiagramKind fromDiagramTypeId(DiagramTypeId diagramTypeId) {
        if (diagramTypeId == null) return BPMN_BASIC;
        for (BehaviorDiagramKind kind : values()) {
            if (kind.diagramTypeId.equals(diagramTypeId)) return kind;
        }
        return BPMN_BASIC;
    }
}
