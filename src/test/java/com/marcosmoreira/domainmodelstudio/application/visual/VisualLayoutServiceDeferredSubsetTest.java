package com.marcosmoreira.domainmodelstudio.application.visual;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassNode;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlModuleGroup;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlVisibility;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class VisualLayoutServiceDeferredSubsetTest {

    @Test
    void ensureAdditionalVisualLayoutPreservesLayoutsOutsideCurrentSubset() {
        VisualLayoutService service = new VisualLayoutService();
        DiagramProject project = DiagramProject.blank("uml", "UML", DiagramTypeId.UML_CLASS)
                .withUmlClassDiagram(document("m1", "c1"));

        DiagramProject firstPrepared = service.ensureVisualLayout(project);
        assertTrue(firstPrepared.layouts().activeLayout().nodeFor(VisualElementLayoutIds.umlClass("c1")).isPresent());

        DiagramProject subsetProject = firstPrepared.withUmlClassDiagram(document("m2", "c2"));
        DiagramProject secondPrepared = service.ensureAdditionalVisualLayout(subsetProject);

        assertTrue(secondPrepared.layouts().activeLayout().nodeFor(VisualElementLayoutIds.umlClass("c1")).isPresent(),
                "El layout diferido no debe podar nodos ajenos a la vista parcial actual.");
        assertTrue(secondPrepared.layouts().activeLayout().nodeFor(VisualElementLayoutIds.umlClass("c2")).isPresent(),
                "Debe agregar los nodos pedidos por la nueva vista parcial.");
    }

    private static UmlClassDiagramDocument document(String moduleId, String classId) {
        UmlModuleGroup module = new UmlModuleGroup(moduleId, "Módulo " + moduleId, moduleId, "", "");
        UmlClassNode node = new UmlClassNode(classId, moduleId, "Clase " + classId, moduleId,
                UmlClassKind.CLASS, UmlVisibility.PUBLIC, "", "", List.of(), "");
        return new UmlClassDiagramDocument("UML", "borrador", LocalDate.of(2026, 5, 17),
                List.of(module), List.of(node), List.of(), List.of(), "");
    }
}
