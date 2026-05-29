package com.marcosmoreira.domainmodelstudio.presentation.shell.tabs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.marcosmoreira.domainmodelstudio.presentation.shell.EditorTabViewState;
import java.util.List;
import org.junit.jupiter.api.Test;

class ProjectTabOrderPolicyTest {

    @Test
    void shouldMoveAProjectTabAfterTheDropTargetWithoutChangingItsIdentity() {
        List<EditorTabViewState> tabs = List.of(
                home(),
                project("A"),
                project("B"),
                project("C"),
                project("D"));

        List<EditorTabViewState> reordered = ProjectTabOrderPolicy.moveAfter(tabs, "B", "D");

        assertEquals(List.of("__home__", "A", "C", "D", "B"), ids(reordered));
        assertEquals(tabs.get(2), reordered.get(4), "La misma pestaña debe moverse sin recrear su estado.");
    }

    @Test
    void shouldKeepHomeTabFixed() {
        List<EditorTabViewState> tabs = List.of(home(), project("A"), project("B"));

        assertEquals(ids(tabs), ids(ProjectTabOrderPolicy.moveAfter(tabs, "__home__", "B")));
        assertEquals(ids(tabs), ids(ProjectTabOrderPolicy.moveAfter(tabs, "B", "__home__")));
    }

    @Test
    void shouldKeepOrderWhenDragAndDropIdsAreInvalidOrSame() {
        List<EditorTabViewState> tabs = List.of(home(), project("A"), project("B"));

        assertEquals(ids(tabs), ids(ProjectTabOrderPolicy.moveAfter(tabs, "A", "A")));
        assertEquals(ids(tabs), ids(ProjectTabOrderPolicy.moveAfter(tabs, "X", "A")));
        assertEquals(ids(tabs), ids(ProjectTabOrderPolicy.moveAfter(tabs, "A", "X")));
    }

    private static EditorTabViewState home() {
        return new EditorTabViewState("__home__", "Pantalla de inicio", false, true, false);
    }

    private static EditorTabViewState project(String id) {
        return new EditorTabViewState(id, "Proyecto " + id, true, false, false);
    }

    private static List<String> ids(List<EditorTabViewState> tabs) {
        return tabs.stream().map(EditorTabViewState::id).toList();
    }
}
