package com.marcosmoreira.domainmodelstudio.application.canonization;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import org.junit.jupiter.api.Test;

class CanonizationArtifactClassifierTest {

    private final CanonizationArtifactClassifier classifier = new CanonizationArtifactClassifier();

    @Test
    void shouldClassifyLogicalBusinessIntakeAsSourceMother() {
        assertEquals(
                CanonizationArtifactRole.SOURCE_MOTHER,
                classifier.roleFor(DiagramTypeId.LOGICAL_BUSINESS_INTAKE));
    }

    @Test
    void shouldClassifyDerivedArtifactsByEnterpriseFamily() {
        assertEquals(CanonizationArtifactRole.LOGICAL_VIEW, classifier.roleFor(DiagramTypeId.LOGICAL_BUSINESS_GRAPH));
        assertEquals(CanonizationArtifactRole.DATA_MODEL, classifier.roleFor(DiagramTypeId.DATA_DICTIONARY));
        assertEquals(CanonizationArtifactRole.DATA_MODEL, classifier.roleFor(DiagramTypeId.CONCEPTUAL_MODEL));
        assertEquals(CanonizationArtifactRole.ARCHITECTURE_VIEW, classifier.roleFor(DiagramTypeId.C4_CONTAINERS));
        assertEquals(CanonizationArtifactRole.BEHAVIOR_VIEW, classifier.roleFor(DiagramTypeId.UML_ACTIVITY));
        assertEquals(CanonizationArtifactRole.ADMINISTRATIVE_VIEW, classifier.roleFor(DiagramTypeId.SCREEN_FLOW));
        assertEquals(CanonizationArtifactRole.SUPPORTING_GRAPH, classifier.roleFor(DiagramTypeId.FREE_GRAPH));
    }
}
