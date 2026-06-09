package com.marcosmoreira.domainmodelstudio.presentation.shell;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.presentation.placeholder.PlaceholderWorkspaceViewModel;
import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Coordina las sesiones de proyectos abiertas por el shell.
 *
 * <p>La clase separa el estado de pestañas/sesiones de {@link MainShellCommandHandler}
 * para que el shell conserve una fachada pública delgada y delegue el ciclo de vida
 * de sesiones. No conoce JavaFX ni persistencia; solo conserva el orden de apertura,
 * la sesión activa, el contador de pestañas y el estado de activación usado para evitar
 * sincronizaciones espurias durante cargas de workspace.</p>
 */
final class ProjectSessionCoordinator {

    private final Map<String, ProjectSession> sessionsByTabId = new LinkedHashMap<>();
    private String activeProjectTabId;
    private int nextProjectTabNumber = 1;
    private boolean activatingProjectSession;

    Map<String, ProjectSession> sessionsByTabId() {
        return sessionsByTabId;
    }

    Collection<ProjectSession> sessions() {
        return sessionsByTabId.values();
    }

    ProjectSession get(String tabId) {
        return tabId == null ? null : sessionsByTabId.get(tabId);
    }

    ProjectSession activeSession() {
        return activeProjectTabId == null ? null : sessionsByTabId.get(activeProjectTabId);
    }

    String activeProjectTabId() {
        return activeProjectTabId;
    }

    boolean hasActiveProjectTab() {
        return activeProjectTabId != null;
    }

    boolean isActive(ProjectSession session) {
        return session != null && Objects.equals(session.tabId, activeProjectTabId);
    }

    int nextProjectTabNumber() {
        return nextProjectTabNumber;
    }

    boolean activatingProjectSession() {
        return activatingProjectSession;
    }

    ProjectSession createProjectSession(DiagramProject project, boolean dirty, Path projectFile) {
        String tabId = nextTabId();
        ProjectSession session = ProjectSession.forProject(tabId, project, dirty, projectFile);
        sessionsByTabId.put(tabId, session);
        return session;
    }

    ProjectSession createPlaceholderSession(
            DiagramProject project,
            PlaceholderWorkspaceViewModel placeholder,
            boolean dirty,
            Path projectFile
    ) {
        String tabId = nextTabId();
        ProjectSession session = ProjectSession.forPlaceholder(tabId, project, placeholder, dirty);
        session.projectFile = projectFile;
        sessionsByTabId.put(tabId, session);
        return session;
    }

    ProjectSession remove(String tabId) {
        ProjectSession removed = tabId == null ? null : sessionsByTabId.remove(tabId);
        if (Objects.equals(tabId, activeProjectTabId)) {
            activeProjectTabId = null;
        }
        return removed;
    }

    void activate(ProjectSession session) {
        activeProjectTabId = Objects.requireNonNull(session, "session").tabId;
    }

    void activateHome() {
        activeProjectTabId = null;
    }

    void runActivating(Runnable action) {
        Objects.requireNonNull(action, "action");
        activatingProjectSession = true;
        try {
            action.run();
        } finally {
            activatingProjectSession = false;
        }
    }

    private String nextTabId() {
        return "project-" + nextProjectTabNumber++;
    }
}
