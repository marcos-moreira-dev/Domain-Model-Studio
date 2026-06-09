package com.marcosmoreira.domainmodelstudio.application.visual;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureDiagramKind;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureNode;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureNodeKind;
import org.junit.jupiter.api.Test;

class ArchitectureLayoutPolicyTest {

    private final ArchitectureLayoutPolicy policy = new ArchitectureLayoutPolicy();

    @Test
    void boundariesAreBornAsLargeZones() {
        VisualNodeReference reference = policy.visualReference(
                ArchitectureDiagramKind.C4_CONTAINERS,
                node("boundary", ArchitectureNodeKind.BOUNDARY),
                0);

        assertTrue(reference.preferredWidth() >= 520.0, "boundary debe nacer como contenedor visual ancho");
        assertTrue(reference.preferredHeight() >= 300.0, "boundary debe nacer como zona, no como cajita");
    }

    @Test
    void deploymentEnvironmentIsLargerThanServer() {
        VisualNodeReference environment = policy.visualReference(
                ArchitectureDiagramKind.TECHNICAL_DEPLOYMENT,
                node("prod", ArchitectureNodeKind.ENVIRONMENT),
                0);
        VisualNodeReference server = policy.visualReference(
                ArchitectureDiagramKind.TECHNICAL_DEPLOYMENT,
                node("server", ArchitectureNodeKind.SERVER),
                1);

        assertTrue(environment.preferredWidth() > server.preferredWidth());
        assertTrue(environment.preferredHeight() > server.preferredHeight());
    }

    private static ArchitectureNode node(String id, ArchitectureNodeKind kind) {
        return new ArchitectureNode(id, kind, kind.displayName(), "", "", "", "", "", 0);
    }
}
