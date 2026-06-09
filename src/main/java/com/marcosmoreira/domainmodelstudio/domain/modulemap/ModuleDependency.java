package com.marcosmoreira.domainmodelstudio.domain.modulemap;

import java.util.Objects;

/** Dependencia funcional entre dos módulos del sistema. */
public final class ModuleDependency {

    private final String id;
    private final String sourceModuleId;
    private final String targetModuleId;
    private final DependencyKind kind;
    private final String description;
    private final String notes;

    public ModuleDependency(
            String id,
            String sourceModuleId,
            String targetModuleId,
            DependencyKind kind,
            String description,
            String notes
    ) {
        this.id = required(id, "id");
        this.sourceModuleId = required(sourceModuleId, "sourceModuleId");
        this.targetModuleId = required(targetModuleId, "targetModuleId");
        this.kind = kind == null ? DependencyKind.USES : kind;
        this.description = normalize(description);
        this.notes = normalize(notes);
        if (this.sourceModuleId.equals(this.targetModuleId)) {
            throw new IllegalArgumentException("Una dependencia no puede apuntar al mismo módulo.");
        }
    }

    public static ModuleDependency of(String id, String sourceModuleId, String targetModuleId) {
        return new ModuleDependency(id, sourceModuleId, targetModuleId, DependencyKind.USES, "", "");
    }

    public String id() {
        return id;
    }

    public String sourceModuleId() {
        return sourceModuleId;
    }

    public String targetModuleId() {
        return targetModuleId;
    }

    public DependencyKind kind() {
        return kind;
    }

    public String description() {
        return description;
    }

    public String notes() {
        return notes;
    }

    public ModuleDependency withDetails(
            String updatedSourceModuleId,
            String updatedTargetModuleId,
            DependencyKind updatedKind,
            String updatedDescription,
            String updatedNotes
    ) {
        return new ModuleDependency(id, updatedSourceModuleId, updatedTargetModuleId, updatedKind,
                updatedDescription, updatedNotes);
    }

    private static String required(String value, String fieldName) {
        String normalized = normalize(value);
        if (normalized.isBlank()) {
            throw new IllegalArgumentException("El campo " + fieldName + " no puede estar vacío.");
        }
        return normalized;
    }

    private static String normalize(String value) {
        return value == null ? "" : value.strip();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof ModuleDependency dependency && dependency.id.equals(id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
