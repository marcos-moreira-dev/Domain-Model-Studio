package com.marcosmoreira.domainmodelstudio.presentation.shell;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.Locale;

/**
 * Genera identificadores estables para proyectos creados desde el shell.
 */
final class ProjectIdPolicy {

    String newProjectId(DiagramTypeId diagramTypeId, int sequence) {
        String raw = diagramTypeId == null ? "diagram" : diagramTypeId.value();
        String normalized = raw.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", "_");
        normalized = normalized.replaceAll("^_+|_+$", "");
        if (normalized.isBlank()) {
            normalized = "diagram";
        }
        return normalized + "_nuevo_" + sequence;
    }
}
