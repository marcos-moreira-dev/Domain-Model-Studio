package com.marcosmoreira.domainmodelstudio.presentation.exportable;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.util.Objects;

/** Utilidades compartidas por contributors de salida exportable. */
final class ActiveOutputContributorSupport {

    private ActiveOutputContributorSupport() {
    }

    static boolean sameActiveProject(DiagramProject candidate, DiagramProject activeProject) {
        if (candidate == null || activeProject == null) {
            return false;
        }
        return Objects.equals(candidate.metadata().id(), activeProject.metadata().id())
                && Objects.equals(candidate.metadata().diagramTypeId(), activeProject.metadata().diagramTypeId());
    }
}
