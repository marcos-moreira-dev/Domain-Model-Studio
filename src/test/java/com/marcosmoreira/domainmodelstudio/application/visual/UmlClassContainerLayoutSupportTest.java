package com.marcosmoreira.domainmodelstudio.application.visual;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayouts;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassNode;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlVisibility;
import java.util.List;
import org.junit.jupiter.api.Test;

class UmlClassContainerLayoutSupportTest {

    @Test
    void fitAllModulesWrapsClassesInsideTheirVisualContainer() {
        UmlClassNode first = node("venta-controller", "backend-ventas");
        UmlClassNode second = node("venta-service", "backend-ventas");
        DiagramProject project = projectWithLayouts(
                NodeLayout.at("uml-module:backend-ventas", 0, 0, 100, 80),
                NodeLayout.at("uml-class:venta-controller", 300, 220, 180, 110),
                NodeLayout.at("uml-class:venta-service", 560, 360, 180, 110));

        DiagramProject fitted = new UmlClassContainerLayoutSupport().fitAllModules(project, List.of(first, second));
        NodeLayout module = fitted.layouts().activeLayout().nodeFor(DiagramElementId.of("uml-module:backend-ventas")).orElseThrow();

        assertTrue(module.x() <= 252.0);
        assertTrue(module.y() <= 124.0);
        assertTrue(module.x() + module.width() >= 792.0);
        assertTrue(module.y() + module.height() >= 522.0);
    }

    private static DiagramProject projectWithLayouts(NodeLayout... nodes) {
        DiagramLayout layout = DiagramLayout.empty(com.marcosmoreira.domainmodelstudio.domain.notation.NotationType.CHEN);
        for (NodeLayout node : nodes) {
            layout = layout.withNode(node);
        }
        return DiagramProject.blank("p", "Proyecto", DiagramTypeId.UML_CLASS)
                .withLayouts(DiagramLayouts.empty().withLayout(layout));
    }

    private static UmlClassNode node(String id, String moduleId) {
        return new UmlClassNode(id, moduleId, id, "", UmlClassKind.CLASS,
                UmlVisibility.PUBLIC, "", "", List.of(), "");
    }
}
