package com.marcosmoreira.domainmodelstudio.application.architecture;

import com.marcosmoreira.domainmodelstudio.domain.architecture.*;
import java.util.Objects;

/** Agrega un elemento de arquitectura al documento activo. */
public final class AddArchitectureNodeUseCase {
    public ArchitectureDiagramDocument add(ArchitectureDiagramDocument document, ArchitectureNodeKind kind, String displayName) {
        Objects.requireNonNull(document, "document");
        ArchitectureNodeKind resolvedKind = kind == null ? document.diagramKind().defaultNodeKind() : kind;
        ArchitectureNode node = new ArchitectureNode(
                ArchitectureDiagramIds.next(resolvedKind.name()),
                resolvedKind,
                displayName == null || displayName.isBlank() ? resolvedKind.displayName() : displayName,
                "", "", "", "", "", document.nodes().size());
        return document.withNode(node);
    }
}
