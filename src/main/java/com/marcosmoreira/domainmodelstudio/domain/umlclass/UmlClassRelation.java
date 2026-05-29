package com.marcosmoreira.domainmodelstudio.domain.umlclass;

/** Relación estructural entre dos clases UML. */
public record UmlClassRelation(
        String id,
        String sourceClassId,
        String targetClassId,
        UmlRelationKind kind,
        String label,
        String description,
        String notes
) {
    public UmlClassRelation {
        id = required(id, "id");
        sourceClassId = required(sourceClassId, "clase origen");
        targetClassId = required(targetClassId, "clase destino");
        kind = kind == null ? UmlRelationKind.DEPENDENCY : kind;
        label = normalize(label);
        description = normalize(description);
        notes = normalize(notes);
    }

    public UmlClassRelation withDetails(
            String sourceClassId,
            String targetClassId,
            UmlRelationKind kind,
            String label,
            String description,
            String notes
    ) {
        return new UmlClassRelation(id, sourceClassId, targetClassId, kind, label, description, notes);
    }

    private static String required(String value, String label) {
        String normalized = normalize(value);
        if (normalized.isBlank()) {
            throw new IllegalArgumentException("El campo " + label + " de la relación UML no puede estar vacío.");
        }
        return normalized;
    }

    private static String normalize(String value) { return value == null ? "" : value.strip(); }
}
