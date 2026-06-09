package com.marcosmoreira.domainmodelstudio.domain.umlclass;

/** Atributo, método o constructor de una clase UML. */
public record UmlClassMember(
        String id,
        UmlMemberKind kind,
        String name,
        String type,
        String signature,
        UmlVisibility visibility,
        boolean staticMember,
        String description
) {
    public UmlClassMember {
        id = required(id, "id");
        kind = kind == null ? UmlMemberKind.ATTRIBUTE : kind;
        name = normalizeOrDefault(name, kind == UmlMemberKind.METHOD ? "nuevoMetodo" : "nuevoAtributo");
        type = normalize(type);
        signature = normalize(signature);
        visibility = visibility == null ? UmlVisibility.PUBLIC : visibility;
        description = normalize(description);
    }

    public UmlClassMember withDetails(
            UmlMemberKind kind,
            String name,
            String type,
            String signature,
            UmlVisibility visibility,
            boolean staticMember,
            String description
    ) {
        return new UmlClassMember(id, kind, name, type, signature, visibility, staticMember, description);
    }

    public String displayText() {
        String prefix = visibility.symbol();
        String body = kind == UmlMemberKind.METHOD || kind == UmlMemberKind.CONSTRUCTOR
                ? (signature.isBlank() ? name + "()" : signature)
                : name + (type.isBlank() ? "" : ": " + type);
        return (prefix.isBlank() ? "" : prefix + " ") + body;
    }

    private static String required(String value, String label) {
        String normalized = normalize(value);
        if (normalized.isBlank()) {
            throw new IllegalArgumentException("El " + label + " del miembro UML no puede estar vacío.");
        }
        return normalized;
    }

    private static String normalizeOrDefault(String value, String fallback) {
        String normalized = normalize(value);
        return normalized.isBlank() ? fallback : normalized;
    }

    private static String normalize(String value) { return value == null ? "" : value.strip(); }
}
