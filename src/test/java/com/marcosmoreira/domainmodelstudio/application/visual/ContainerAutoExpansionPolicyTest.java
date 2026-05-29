package com.marcosmoreira.domainmodelstudio.application.visual;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramModel;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.diagram.ProjectMetadata;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayouts;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.domain.notation.NotationType;
import com.marcosmoreira.domainmodelstudio.domain.style.DiagramStyleSheet;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ContainerAutoExpansionPolicyTest {

    private final ContainerAutoExpansionPolicy policy = new ContainerAutoExpansionPolicy();

    @Test
    void expandsContainerWithoutMovingChildren() {
        DiagramElementId containerId = DiagramElementId.of("container:main");
        DiagramElementId childId = DiagramElementId.of("child:one");
        DiagramProject project = projectWithLayouts(
                NodeLayout.at(containerId.value(), 100, 100, 120, 100),
                NodeLayout.at(childId.value(), 250, 230, 80, 60));

        DiagramProject expanded = policy.expandContainerToFit(
                project,
                containerId,
                List.of(childId),
                ContainerPadding.of(20, 30, 20, 30));

        NodeLayout container = expanded.layouts().activeLayout().nodeFor(containerId).orElseThrow();
        NodeLayout child = expanded.layouts().activeLayout().nodeFor(childId).orElseThrow();

        assertEquals(100.0, container.x());
        assertEquals(100.0, container.y());
        assertEquals(250.0, container.width());
        assertEquals(220.0, container.height());
        assertEquals(250.0, child.x());
        assertEquals(230.0, child.y());
    }

    @Test
    void movesChildrenWithContainerDelta() {
        DiagramElementId childId = DiagramElementId.of("child:one");
        DiagramProject project = projectWithLayouts(NodeLayout.at(childId.value(), 30, 40, 80, 60));

        DiagramProject moved = policy.moveNodesBy(project, List.of(childId), 15, -10);

        NodeLayout child = moved.layouts().activeLayout().nodeFor(childId).orElseThrow();
        assertEquals(45.0, child.x());
        assertEquals(30.0, child.y());
    }

    private static DiagramProject projectWithLayouts(NodeLayout... nodes) {
        DiagramLayout layout = new DiagramLayout(NotationType.CHEN, List.of(nodes), List.of());
        return new DiagramProject(
                ProjectMetadata.draft("test", "Test"),
                DiagramModel.empty(),
                new DiagramLayouts(NotationType.CHEN, Map.of(NotationType.CHEN, layout)),
                DiagramStyleSheet.defaults());
    }
}
