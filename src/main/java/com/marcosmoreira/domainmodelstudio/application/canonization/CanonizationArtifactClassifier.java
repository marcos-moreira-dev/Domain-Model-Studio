package com.marcosmoreira.domainmodelstudio.application.canonization;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.Map;
import java.util.Objects;

/** Clasifica tipos oficiales según su papel en el flujo de canonización documental. */
public final class CanonizationArtifactClassifier {

    private static final Map<DiagramTypeId, CanonizationArtifactRole> ROLES = Map.ofEntries(
            Map.entry(DiagramTypeId.LOGICAL_BUSINESS_INTAKE, CanonizationArtifactRole.SOURCE_MOTHER),
            Map.entry(DiagramTypeId.LOGICAL_BUSINESS_GRAPH, CanonizationArtifactRole.LOGICAL_VIEW),
            Map.entry(DiagramTypeId.CONCEPTUAL_MODEL, CanonizationArtifactRole.DATA_MODEL),
            Map.entry(DiagramTypeId.DATA_DICTIONARY, CanonizationArtifactRole.DATA_MODEL),
            Map.entry(DiagramTypeId.UML_CLASS, CanonizationArtifactRole.DATA_MODEL),
            Map.entry(DiagramTypeId.C4_CONTEXT, CanonizationArtifactRole.ARCHITECTURE_VIEW),
            Map.entry(DiagramTypeId.C4_CONTAINERS, CanonizationArtifactRole.ARCHITECTURE_VIEW),
            Map.entry(DiagramTypeId.TECHNICAL_DEPLOYMENT, CanonizationArtifactRole.ARCHITECTURE_VIEW),
            Map.entry(DiagramTypeId.BPMN_BASIC, CanonizationArtifactRole.BEHAVIOR_VIEW),
            Map.entry(DiagramTypeId.OPERATIONAL_FLOW, CanonizationArtifactRole.BEHAVIOR_VIEW),
            Map.entry(DiagramTypeId.UML_USE_CASE, CanonizationArtifactRole.BEHAVIOR_VIEW),
            Map.entry(DiagramTypeId.UML_ACTIVITY, CanonizationArtifactRole.BEHAVIOR_VIEW),
            Map.entry(DiagramTypeId.UML_SEQUENCE, CanonizationArtifactRole.BEHAVIOR_VIEW),
            Map.entry(DiagramTypeId.UML_STATE, CanonizationArtifactRole.BEHAVIOR_VIEW),
            Map.entry(DiagramTypeId.ADMIN_MODULE_MAP, CanonizationArtifactRole.ADMINISTRATIVE_VIEW),
            Map.entry(DiagramTypeId.ROLES_PERMISSIONS_MAP, CanonizationArtifactRole.ADMINISTRATIVE_VIEW),
            Map.entry(DiagramTypeId.SCREEN_FLOW, CanonizationArtifactRole.ADMINISTRATIVE_VIEW),
            Map.entry(DiagramTypeId.ADMIN_WIREFRAMES, CanonizationArtifactRole.ADMINISTRATIVE_VIEW),
            Map.entry(DiagramTypeId.FREE_GRAPH, CanonizationArtifactRole.SUPPORTING_GRAPH)
    );

    public CanonizationArtifactRole roleFor(DiagramTypeId diagramTypeId) {
        Objects.requireNonNull(diagramTypeId, "diagramTypeId");
        return ROLES.getOrDefault(diagramTypeId, CanonizationArtifactRole.UNKNOWN);
    }
}
