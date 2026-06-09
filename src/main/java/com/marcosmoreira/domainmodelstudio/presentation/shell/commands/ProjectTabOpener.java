package com.marcosmoreira.domainmodelstudio.presentation.shell.commands;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;

/** Abre un proyecto en una nueva pestaña del shell. */
@FunctionalInterface
public interface ProjectTabOpener {

    void openProjectInNewTab(DiagramProject project, String statusLabel, boolean dirty);
}
