package com.marcosmoreira.domainmodelstudio.presentation.canvas;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import org.junit.jupiter.api.Test;

class ConceptualCanvasEditHistoryTest {

    @Test
    void undoAndRedoPreserveTheSameProjectSnapshots() {
        ConceptualCanvasEditHistory history = new ConceptualCanvasEditHistory();
        DiagramProject first = DiagramProject.blank("primer", "Primer modelo");
        DiagramProject second = DiagramProject.blank("segundo", "Segundo modelo");

        history.prepareEdit(first);

        assertTrue(history.canUndo());
        assertFalse(history.canRedo());
        assertSame(first, history.undo(second));
        assertTrue(history.canRedo());
        assertSame(second, history.redo(first));
    }

    @Test
    void resetClearsBothStacks() {
        ConceptualCanvasEditHistory history = new ConceptualCanvasEditHistory();
        history.prepareEdit(DiagramProject.blank("modelo", "Modelo"));

        history.reset();

        assertFalse(history.canUndo());
        assertFalse(history.canRedo());
    }

    @Test
    void nullCurrentProjectIsIgnoredWhenPreparingEdit() {
        ConceptualCanvasEditHistory history = new ConceptualCanvasEditHistory();

        history.prepareEdit(null);

        assertFalse(history.canUndo());
        assertEquals(null, history.undo(null));
    }
}
