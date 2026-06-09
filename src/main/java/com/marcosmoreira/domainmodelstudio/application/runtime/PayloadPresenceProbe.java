package com.marcosmoreira.domainmodelstudio.application.runtime;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;

/** Probe no-JavaFX que indica si un proyecto contiene un payload esperado. */
@FunctionalInterface
public interface PayloadPresenceProbe {

    boolean presentIn(DiagramProject project);
}
