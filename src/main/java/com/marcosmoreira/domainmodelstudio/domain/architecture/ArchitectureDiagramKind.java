package com.marcosmoreira.domainmodelstudio.domain.architecture;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.List;
import java.util.Objects;

/** Familia de diagramas de arquitectura implementados con el editor funcional común. */
public enum ArchitectureDiagramKind {
    C4_CONTEXT("C4 Contexto", DiagramTypeId.C4_CONTEXT,
            List.of(ArchitectureNodeKind.PERSON, ArchitectureNodeKind.SOFTWARE_SYSTEM, ArchitectureNodeKind.EXTERNAL_SYSTEM, ArchitectureNodeKind.BOUNDARY),
            List.of(ArchitectureEdgeKind.USES, ArchitectureEdgeKind.DEPENDS_ON, ArchitectureEdgeKind.INTEGRATES_WITH)),
    C4_CONTAINERS("C4 Contenedores", DiagramTypeId.C4_CONTAINERS,
            List.of(ArchitectureNodeKind.CONTAINER, ArchitectureNodeKind.APPLICATION, ArchitectureNodeKind.API, ArchitectureNodeKind.DATABASE, ArchitectureNodeKind.EXTERNAL_SERVICE, ArchitectureNodeKind.BOUNDARY),
            List.of(ArchitectureEdgeKind.CALLS, ArchitectureEdgeKind.READS_WRITES, ArchitectureEdgeKind.PUBLISHES, ArchitectureEdgeKind.SUBSCRIBES, ArchitectureEdgeKind.DEPENDS_ON)),
    TECHNICAL_DEPLOYMENT("Despliegue técnico", DiagramTypeId.TECHNICAL_DEPLOYMENT,
            List.of(ArchitectureNodeKind.ENVIRONMENT, ArchitectureNodeKind.SERVER, ArchitectureNodeKind.CLIENT, ArchitectureNodeKind.SERVICE, ArchitectureNodeKind.DATABASE, ArchitectureNodeKind.NETWORK, ArchitectureNodeKind.ARTIFACT),
            List.of(ArchitectureEdgeKind.DEPLOYS_TO, ArchitectureEdgeKind.CONNECTS_TO, ArchitectureEdgeKind.HOSTS, ArchitectureEdgeKind.DEPENDS_ON));

    private final String displayName;
    private final DiagramTypeId diagramTypeId;
    private final List<ArchitectureNodeKind> nodeKinds;
    private final List<ArchitectureEdgeKind> edgeKinds;

    ArchitectureDiagramKind(String displayName, DiagramTypeId diagramTypeId, List<ArchitectureNodeKind> nodeKinds, List<ArchitectureEdgeKind> edgeKinds) {
        this.displayName = Objects.requireNonNull(displayName, "displayName");
        this.diagramTypeId = Objects.requireNonNull(diagramTypeId, "diagramTypeId");
        this.nodeKinds = List.copyOf(nodeKinds);
        this.edgeKinds = List.copyOf(edgeKinds);
    }

    public String displayName() { return displayName; }
    public DiagramTypeId diagramTypeId() { return diagramTypeId; }
    public List<ArchitectureNodeKind> nodeKinds() { return nodeKinds; }
    public List<ArchitectureEdgeKind> edgeKinds() { return edgeKinds; }
    public ArchitectureNodeKind defaultNodeKind() { return nodeKinds.isEmpty() ? ArchitectureNodeKind.SOFTWARE_SYSTEM : nodeKinds.get(0); }
    public ArchitectureEdgeKind defaultEdgeKind() { return edgeKinds.isEmpty() ? ArchitectureEdgeKind.DEPENDS_ON : edgeKinds.get(0); }

    public static boolean supports(DiagramTypeId diagramTypeId) {
        for (ArchitectureDiagramKind kind : values()) {
            if (kind.diagramTypeId.equals(diagramTypeId)) return true;
        }
        return false;
    }

    public static ArchitectureDiagramKind fromDiagramTypeId(DiagramTypeId diagramTypeId) {
        for (ArchitectureDiagramKind kind : values()) {
            if (kind.diagramTypeId.equals(diagramTypeId)) return kind;
        }
        return C4_CONTEXT;
    }
}
