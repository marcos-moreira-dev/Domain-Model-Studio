package com.marcosmoreira.domainmodelstudio.presentation.shell;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;

/**
 * Historial de ediciones asociado a una pestaña de proyecto.
 *
 * <p>No pertenece al dominio: conserva snapshots inmutables de {@link DiagramProject}
 * para que cada pestaña abierta tenga su propia cola de deshacer/rehacer.</p>
 */
final class ProjectEditHistory {

    private static final int MAX_ENTRIES = 100;
    private static final long COALESCE_WINDOW_NANOS = 450_000_000L;

    private final Deque<DiagramProject> undoStack = new ArrayDeque<>();
    private final Deque<DiagramProject> redoStack = new ArrayDeque<>();
    private long lastRememberedAtNanos;

    void rememberBeforeEdit(DiagramProject previousProject, DiagramProject updatedProject) {
        if (previousProject == null || updatedProject == null || previousProject.equals(updatedProject)) {
            return;
        }
        long now = System.nanoTime();
        if (!undoStack.isEmpty() && now - lastRememberedAtNanos < COALESCE_WINDOW_NANOS) {
            redoStack.clear();
            lastRememberedAtNanos = now;
            return;
        }
        undoStack.push(previousProject);
        lastRememberedAtNanos = now;
        trim(undoStack);
        redoStack.clear();
    }

    Optional<DiagramProject> undo(DiagramProject currentProject) {
        if (currentProject == null || undoStack.isEmpty()) {
            return Optional.empty();
        }
        redoStack.push(currentProject);
        trim(redoStack);
        return Optional.of(undoStack.pop());
    }

    Optional<DiagramProject> redo(DiagramProject currentProject) {
        if (currentProject == null || redoStack.isEmpty()) {
            return Optional.empty();
        }
        undoStack.push(currentProject);
        trim(undoStack);
        return Optional.of(redoStack.pop());
    }

    boolean canUndo() {
        return !undoStack.isEmpty();
    }

    boolean canRedo() {
        return !redoStack.isEmpty();
    }

    void clear() {
        undoStack.clear();
        redoStack.clear();
        lastRememberedAtNanos = 0L;
    }

    private static void trim(Deque<DiagramProject> stack) {
        while (stack.size() > MAX_ENTRIES) {
            stack.removeLast();
        }
    }
}
