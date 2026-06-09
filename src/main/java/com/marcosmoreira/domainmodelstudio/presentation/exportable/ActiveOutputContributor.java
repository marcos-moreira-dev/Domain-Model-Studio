package com.marcosmoreira.domainmodelstudio.presentation.exportable;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.util.Optional;

/** Contributor especializado que resuelve la salida exportable de una familia de workspace. */
interface ActiveOutputContributor {

    boolean supports(DiagramTypeId diagramTypeId);

    Optional<ExportableOutput> resolve(
            DiagramProject activeProject,
            ProjectExportFormatPolicy exportFormatPolicy
    );
}
