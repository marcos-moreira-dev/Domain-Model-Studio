package com.marcosmoreira.domainmodelstudio.domain.modulemap;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Módulo o submódulo funcional de una aplicación administrativa. */
public final class ModuleNode {

    private final String id;
    private final String displayName;
    private final String parentId;
    private final ModuleKind kind;
    private final ModuleStatus status;
    private final String responsibility;
    private final String description;
    private final List<String> tags;
    private final String notes;

    public ModuleNode(
            String id,
            String displayName,
            String parentId,
            ModuleKind kind,
            ModuleStatus status,
            String responsibility,
            String description,
            List<String> tags,
            String notes
    ) {
        this.id = required(id, "id");
        this.displayName = required(displayName, "displayName");
        this.parentId = normalize(parentId);
        this.kind = kind == null ? ModuleKind.MAIN : kind;
        this.status = status == null ? ModuleStatus.PLANNED : status;
        this.responsibility = normalize(responsibility);
        this.description = normalize(description);
        this.tags = List.copyOf(tags == null ? List.of() : tags.stream()
                .map(ModuleNode::normalize)
                .filter(value -> !value.isBlank())
                .toList());
        this.notes = normalize(notes);
    }

    public static ModuleNode root(String id, String displayName) {
        return new ModuleNode(id, displayName, "", ModuleKind.MAIN, ModuleStatus.PLANNED,
                "", "", List.of(), "");
    }

    public static ModuleNode child(String id, String displayName, String parentId) {
        return new ModuleNode(id, displayName, parentId, ModuleKind.SUPPORT, ModuleStatus.PLANNED,
                "", "", List.of(), "");
    }

    public String id() {
        return id;
    }

    public String displayName() {
        return displayName;
    }

    public String parentId() {
        return parentId;
    }

    public boolean rootModule() {
        return parentId.isBlank();
    }

    public ModuleKind kind() {
        return kind;
    }

    public ModuleStatus status() {
        return status;
    }

    public String responsibility() {
        return responsibility;
    }

    public String description() {
        return description;
    }

    public List<String> tags() {
        return tags;
    }

    public String notes() {
        return notes;
    }

    public ModuleNode renamed(String updatedDisplayName) {
        return new ModuleNode(id, updatedDisplayName, parentId, kind, status, responsibility, description, tags, notes);
    }

    public ModuleNode withDetails(
            String updatedDisplayName,
            String updatedParentId,
            ModuleKind updatedKind,
            ModuleStatus updatedStatus,
            String updatedResponsibility,
            String updatedDescription,
            List<String> updatedTags,
            String updatedNotes
    ) {
        return new ModuleNode(id, updatedDisplayName, updatedParentId, updatedKind, updatedStatus,
                updatedResponsibility, updatedDescription, updatedTags, updatedNotes);
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
        return other instanceof ModuleNode module && module.id.equals(id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static List<String> splitTags(String rawTags) {
        if (rawTags == null || rawTags.isBlank()) {
            return List.of();
        }
        List<String> result = new ArrayList<>();
        for (String part : rawTags.split(",")) {
            String normalized = normalize(part);
            if (!normalized.isBlank()) {
                result.add(normalized);
            }
        }
        return result;
    }
}
