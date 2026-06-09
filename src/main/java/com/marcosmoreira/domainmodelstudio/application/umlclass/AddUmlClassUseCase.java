package com.marcosmoreira.domainmodelstudio.application.umlclass;

import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassNode;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlVisibility;
import java.util.List;
import java.util.Objects;

/** Agrega clases, interfaces o enums dentro de un módulo UML. */
public final class AddUmlClassUseCase {
    public UmlClassDiagramDocument add(UmlClassDiagramDocument document, String moduleId, String displayName, UmlClassKind kind) {
        Objects.requireNonNull(document, "document");
        String normalizedModuleId = moduleId == null ? "" : moduleId.strip();
        if (!normalizedModuleId.isBlank()) {
            document.moduleById(normalizedModuleId)
                    .orElseThrow(() -> new IllegalArgumentException("No existe el módulo UML: " + normalizedModuleId));
        }
        String name = normalized(displayName, defaultName(kind));
        String id = uniqueId(document, UmlClassDiagramIds.slug(name, "clase"));
        return document.withClass(new UmlClassNode(id, normalizedModuleId, name, "", kind == null ? UmlClassKind.CLASS : kind,
                UmlVisibility.PUBLIC, "", "", List.of(), ""));
    }

    private String uniqueId(UmlClassDiagramDocument document, String base) {
        String candidate = base;
        int counter = 2;
        while (document.classById(candidate).isPresent()) {
            candidate = base + "_" + counter++;
        }
        return candidate;
    }

    private String defaultName(UmlClassKind kind) {
        return switch (kind == null ? UmlClassKind.CLASS : kind) {
            case INTERFACE -> "Interfaz";
            case ENUM -> "Enum";
            default -> "Clase";
        };
    }

    private String normalized(String value, String fallback) {
        String normalized = value == null ? "" : value.strip();
        return normalized.isBlank() ? fallback : normalized;
    }
}
