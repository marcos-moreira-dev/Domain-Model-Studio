package com.marcosmoreira.domainmodelstudio.presentation.workbench;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramModel;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.diagram.ProjectMetadata;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayouts;
import com.marcosmoreira.domainmodelstudio.domain.style.DiagramStyleSheet;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

class ProjectChangeSupportTest {

    @Test
    void notifiesOnlyOutsideLoadingSections() {
        ProjectChangeSupport support = new ProjectChangeSupport();
        AtomicInteger notifications = new AtomicInteger();
        support.registerProjectChangeListener(project -> notifications.incrementAndGet());
        DiagramProject project = sampleProject();

        support.notifyChanged(project);
        support.runLoading(() -> support.notifyChanged(project));
        support.notifyChanged(null);

        assertEquals(1, notifications.get());
        assertFalse(support.loading());
    }

    @Test
    void exposesLoadingStateDuringGuardedWork() {
        ProjectChangeSupport support = new ProjectChangeSupport();
        AtomicInteger loadingReads = new AtomicInteger();

        support.runLoading(() -> {
            if (support.loading()) {
                loadingReads.incrementAndGet();
            }
        });

        assertEquals(1, loadingReads.get());
        assertFalse(support.loading());
    }

    @Test
    void acceptsNullListenerAsNoOp() {
        ProjectChangeSupport support = new ProjectChangeSupport();
        support.registerProjectChangeListener(null);

        support.notifyChanged(sampleProject());

        assertTrue(true, "El listener nulo se normaliza a no-op y no debe fallar.");
    }

    private DiagramProject sampleProject() {
        return new DiagramProject(
                ProjectMetadata.draft("project-1", "Proyecto de prueba"),
                DiagramModel.empty(),
                DiagramLayouts.empty(),
                DiagramStyleSheet.defaults());
    }
}
