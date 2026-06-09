package com.marcosmoreira.domainmodelstudio.application.runtime;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.util.Optional;

/** Probe no-JavaFX para validaciones específicas del payload detectado. */
@FunctionalInterface
public interface PayloadConsistencyProbe {

    Optional<String> validate(DiagramProject project);
}
