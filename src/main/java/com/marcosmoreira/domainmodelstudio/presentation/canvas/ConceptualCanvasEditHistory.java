package com.marcosmoreira.domainmodelstudio.presentation.canvas;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Historial de edición del canvas conceptual legacy.
 *
 * <p>Encapsula las pilas de deshacer/rehacer para que {@link DiagramCanvasViewModel}
 * conserve la orquestación de comandos visuales sin almacenar directamente la mecánica
 * mutable del historial. No cambia el formato {@code .dms}, la selección, el render Chen/Crow's Foot
 * ni el contrato de edición del modelo conceptual.</p>
 */
final class ConceptualCanvasEditHistory {

    private final Deque<DiagramProject> undoStack = new ArrayDeque<>();
    private final Deque<DiagramProject> redoStack = new ArrayDeque<>();

    void reset() {
        undoStack.clear();
        redoStack.clear();
    }

    void prepareEdit(DiagramProject currentProject) {
        if (currentProject != null) {
            undoStack.push(currentProject);
        }
        redoStack.clear();
    }

    boolean canUndo() {
        return !undoStack.isEmpty();
    }

    boolean canRedo() {
        return !redoStack.isEmpty();
    }

    DiagramProject undo(DiagramProject currentProject) {
        if (currentProject == null || undoStack.isEmpty()) {
            return currentProject;
        }
        redoStack.push(currentProject);
        return undoStack.pop();
    }

    DiagramProject redo(DiagramProject currentProject) {
        if (currentProject == null || redoStack.isEmpty()) {
            return currentProject;
        }
        undoStack.push(currentProject);
        return redoStack.pop();
    }
}
