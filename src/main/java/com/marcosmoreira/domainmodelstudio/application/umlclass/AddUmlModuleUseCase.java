package com.marcosmoreira.domainmodelstudio.application.umlclass;

import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlModuleGroup;
import java.util.Objects;

/** Agrega agrupadores por módulo/carpeta/paquete al diagrama UML Clases. */
public final class AddUmlModuleUseCase {
    public UmlClassDiagramDocument add(UmlClassDiagramDocument document, String displayName) {
        Objects.requireNonNull(document, "document");
        String name = normalized(displayName, "Módulo");
        String id = uniqueId(document, UmlClassDiagramIds.slug(name, "modulo"));
        return document.withModule(new UmlModuleGroup(id, name, "", "", ""));
    }

    private String uniqueId(UmlClassDiagramDocument document, String base) {
        String candidate = base;
        int counter = 2;
        while (document.moduleById(candidate).isPresent()) {
            candidate = base + "_" + counter++;
        }
        return candidate;
    }

    private String normalized(String value, String fallback) {
        String normalized = value == null ? "" : value.strip();
        return normalized.isBlank() ? fallback : normalized;
    }
}
