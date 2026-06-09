package com.marcosmoreira.domainmodelstudio.application.visual;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureNode;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureNodeKind;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayouts;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.domain.notation.NotationType;
import java.util.List;
import org.junit.jupiter.api.Test;

class ArchitectureContainerLayoutSupportTest {

    private final ArchitectureContainerLayoutSupport support = new ArchitectureContainerLayoutSupport();

    @Test
    void networkIsARealVisualContainerAndMovesContainedServices() {
        ArchitectureNode network = node("red-admin", ArchitectureNodeKind.NETWORK);
        ArchitectureNode service = node("api-uens", ArchitectureNodeKind.SERVICE);
        DiagramProject project = projectWithLayouts(
                NodeLayout.at(VisualElementLayoutIds.architectureNode(network.id()).value(), 100, 100, 300, 190),
                NodeLayout.at(VisualElementLayoutIds.architectureNode(service.id()).value(), 150, 160, 100, 60));

        DiagramProject moved = support.moveNode(project, network.id(), 200.0, 180.0, List.of(network, service));

        NodeLayout serviceLayout = layout(moved, service.id());
        NodeLayout networkLayout = layout(moved, network.id());
        assertTrue(ArchitectureContainerLayoutSupport.isVisualContainer(ArchitectureNodeKind.NETWORK));
        assertEquals(250.0, serviceLayout.x(), 0.001);
        assertEquals(240.0, serviceLayout.y(), 0.001);
        assertTrue(centerInside(serviceLayout, networkLayout), "La red debe seguir conteniendo visualmente al servicio movido.");
    }

    @Test
    void environmentMovesNestedNetworkAndServiceAsOneVisualGroup() {
        ArchitectureNode environment = node("produccion", ArchitectureNodeKind.ENVIRONMENT);
        ArchitectureNode network = node("red-admin", ArchitectureNodeKind.NETWORK);
        ArchitectureNode service = node("api-uens", ArchitectureNodeKind.SERVICE);
        DiagramProject project = projectWithLayouts(
                NodeLayout.at(VisualElementLayoutIds.architectureNode(environment.id()).value(), 0, 0, 800, 500),
                NodeLayout.at(VisualElementLayoutIds.architectureNode(network.id()).value(), 100, 100, 300, 190),
                NodeLayout.at(VisualElementLayoutIds.architectureNode(service.id()).value(), 150, 160, 100, 60));

        DiagramProject moved = support.moveNode(project, environment.id(), 50.0, 60.0, List.of(environment, network, service));

        NodeLayout serviceLayout = layout(moved, service.id());
        NodeLayout networkLayout = layout(moved, network.id());
        NodeLayout environmentLayout = layout(moved, environment.id());
        assertEquals(200.0, serviceLayout.x(), 0.001);
        assertEquals(220.0, serviceLayout.y(), 0.001);
        assertTrue(centerInside(serviceLayout, networkLayout), "La red anidada debe reajustarse alrededor del servicio.");
        assertTrue(centerInside(networkLayout, environmentLayout), "El ambiente debe reajustarse alrededor de la red anidada.");
    }

    @Test
    void movingChildInsideNetworkExpandsTheNetworkAroundTheChild() {
        ArchitectureNode network = node("red-admin", ArchitectureNodeKind.NETWORK);
        ArchitectureNode service = node("api-uens", ArchitectureNodeKind.SERVICE);
        DiagramProject project = projectWithLayouts(
                NodeLayout.at(VisualElementLayoutIds.architectureNode(network.id()).value(), 100, 100, 300, 190),
                NodeLayout.at(VisualElementLayoutIds.architectureNode(service.id()).value(), 520, 260, 100, 60));

        DiagramProject moved = support.moveNode(project, service.id(), 140.0, 175.0, List.of(network, service));

        NodeLayout serviceLayout = layout(moved, service.id());
        NodeLayout networkLayout = layout(moved, network.id());
        assertTrue(centerInside(serviceLayout, networkLayout),
                "Al soltar un hijo dentro de una red, la zona debe autoajustarse como contenedor visual.");
        assertTrue(networkLayout.width() >= 300.0, "La red conserva ancho mínimo legible.");
        assertTrue(networkLayout.height() >= 190.0, "La red conserva alto mínimo legible.");
    }

    private static DiagramProject projectWithLayouts(NodeLayout... nodes) {
        DiagramLayout layout = new DiagramLayout(NotationType.CHEN, List.of(nodes), List.of());
        return DiagramProject.blank("arch", "Arquitectura", DiagramTypeId.TECHNICAL_DEPLOYMENT)
                .withLayouts(DiagramLayouts.empty().withLayout(layout));
    }

    private static ArchitectureNode node(String id, ArchitectureNodeKind kind) {
        return new ArchitectureNode(id, kind, id, "", "", "", "", "", 0);
    }

    private static NodeLayout layout(DiagramProject project, String nodeId) {
        DiagramElementId layoutId = VisualElementLayoutIds.architectureNode(nodeId);
        return project.layouts().activeLayout().nodeFor(layoutId).orElseThrow();
    }

    private static boolean centerInside(NodeLayout child, NodeLayout container) {
        double centerX = child.x() + child.width() / 2.0;
        double centerY = child.y() + child.height() / 2.0;
        return centerX >= container.x()
                && centerX <= container.x() + container.width()
                && centerY >= container.y()
                && centerY <= container.y() + container.height();
    }
}
