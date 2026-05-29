package com.marcosmoreira.domainmodelstudio.domain.umlclass;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class UmlClassDiagramViewTest {

    @Test
    void shouldFilterModulesClassesAndRelationsByInternalView() {
        UmlModuleGroup backend = new UmlModuleGroup("backend", "Backend", "backend", "", "");
        UmlModuleGroup frontend = new UmlModuleGroup("frontend", "Frontend", "frontend", "", "");
        UmlClassNode controller = node("controller", "backend", "PedidoController");
        UmlClassNode component = node("component", "frontend", "PedidoListComponent");
        UmlClassRelation relation = new UmlClassRelation("r1", "component", "controller", UmlRelationKind.DEPENDENCY, "api", "", "");
        UmlClassDiagramView view = new UmlClassDiagramView("backend-view", UmlClassDiagramViewKind.BACKEND,
                "Backend", "", List.of("backend-root"), List.of("backend"), List.of("controller"), List.of("r1"), "");
        UmlClassDiagramDocument document = new UmlClassDiagramDocument("Demo", "borrador", LocalDate.now(),
                List.of(backend, frontend), List.of(controller, component), List.of(relation), List.of(view), "");

        assertEquals(1, document.viewCount());
        assertEquals(List.of(backend), document.modulesForView("backend-view"));
        assertEquals(List.of(controller), document.classesForView("backend-view"));
        assertEquals(List.of(relation), document.relationsForView("backend-view"));
    }

    @Test
    void shouldRejectViewsPointingToMissingClasses() {
        UmlClassDiagramView view = new UmlClassDiagramView("invalid", UmlClassDiagramViewKind.CUSTOM,
                "Inválida", "", List.of(), List.of(), List.of("missing"), List.of(), "");

        assertThrows(IllegalArgumentException.class, () -> new UmlClassDiagramDocument("Demo", "borrador",
                LocalDate.now(), List.of(), List.of(), List.of(), List.of(view), ""));
    }

    @Test
    void shouldKeepOldConstructorCompatibleForMarkdownAndManualWorkflows() {
        UmlClassDiagramDocument document = new UmlClassDiagramDocument("Manual", "borrador", LocalDate.now(),
                List.of(), List.of(), List.of(), "Notas");

        assertTrue(document.views().isEmpty());
        assertEquals("Notas", document.notes());
    }

    private UmlClassNode node(String id, String moduleId, String displayName) {
        return new UmlClassNode(id, moduleId, displayName, "", UmlClassKind.CLASS, UmlVisibility.PUBLIC,
                "", "", List.of(), "");
    }
}
