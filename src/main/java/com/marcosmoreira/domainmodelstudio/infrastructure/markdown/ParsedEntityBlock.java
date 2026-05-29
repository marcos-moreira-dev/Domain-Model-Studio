package com.marcosmoreira.domainmodelstudio.infrastructure.markdown;

import com.marcosmoreira.domainmodelstudio.domain.er.AttributeElement;
import java.util.ArrayList;
import java.util.List;

/** Bloque temporal de entidad mientras se parsea el Markdown. */
final class ParsedEntityBlock {

    private final String headerName;
    private String id;
    private String module = "";
    private String description = "";
    private String kind = "strong";
    private final List<AttributeElement> attributes = new ArrayList<>();

    ParsedEntityBlock(String headerName) {
        this.headerName = headerName == null ? "" : headerName.trim();
        this.id = MarkdownTextUtils.toStableId(headerName);
    }

    String headerName() {
        return headerName;
    }

    String id() {
        return id;
    }

    String module() {
        return module;
    }

    String description() {
        return description;
    }

    String kind() {
        return kind;
    }

    List<AttributeElement> attributes() {
        return List.copyOf(attributes);
    }

    void applyProperty(String key, String value) {
        switch (key) {
            case "id" -> id = value.isBlank() ? id : value.trim();
            case "module" -> module = value.trim();
            case "description" -> description = value.trim();
            case "kind" -> kind = value.trim();
            default -> {
                // La gramática permite metadatos extra; se conservan para futuras versiones.
            }
        }
    }

    void addAttribute(AttributeElement attribute) {
        attributes.add(attribute);
    }
}
