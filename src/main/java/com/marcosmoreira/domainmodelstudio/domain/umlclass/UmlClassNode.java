package com.marcosmoreira.domainmodelstudio.domain.umlclass;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/** Clase, interfaz o enum dentro de un grupo/módulo UML. */
public record UmlClassNode(
        String id,
        String moduleId,
        String displayName,
        String packageName,
        UmlClassKind kind,
        UmlVisibility visibility,
        String responsibility,
        String description,
        List<UmlClassMember> members,
        String notes
) {
    public UmlClassNode {
        id = required(id, "id");
        moduleId = normalize(moduleId);
        displayName = normalizeOrDefault(displayName, id);
        packageName = normalize(packageName);
        kind = kind == null ? UmlClassKind.CLASS : kind;
        visibility = visibility == null ? UmlVisibility.PUBLIC : visibility;
        responsibility = normalize(responsibility);
        description = normalize(description);
        members = List.copyOf(members == null ? List.of() : members);
        notes = normalize(notes);
    }

    public UmlClassNode withDetails(
            String moduleId,
            String displayName,
            String packageName,
            UmlClassKind kind,
            UmlVisibility visibility,
            String responsibility,
            String description,
            String notes
    ) {
        return new UmlClassNode(id, moduleId, displayName, packageName, kind, visibility,
                responsibility, description, members, notes);
    }

    public UmlClassNode withMember(UmlClassMember member) {
        Objects.requireNonNull(member, "member");
        ArrayList<UmlClassMember> updated = new ArrayList<>(members);
        updated.add(member);
        return new UmlClassNode(id, moduleId, displayName, packageName, kind, visibility,
                responsibility, description, updated, notes);
    }

    public UmlClassNode withUpdatedMember(UmlClassMember member) {
        Objects.requireNonNull(member, "member");
        ArrayList<UmlClassMember> updated = new ArrayList<>();
        boolean replaced = false;
        for (UmlClassMember current : members) {
            if (current.id().equals(member.id())) {
                updated.add(member);
                replaced = true;
            } else {
                updated.add(current);
            }
        }
        if (!replaced) {
            throw new IllegalArgumentException("No existe miembro UML para actualizar: " + member.id());
        }
        return new UmlClassNode(id, moduleId, displayName, packageName, kind, visibility,
                responsibility, description, updated, notes);
    }

    public UmlClassNode withoutMember(String memberId) {
        String normalized = normalize(memberId);
        List<UmlClassMember> updated = members.stream()
                .filter(member -> !member.id().equals(normalized))
                .toList();
        if (updated.size() == members.size()) {
            throw new IllegalArgumentException("No existe miembro UML para eliminar: " + normalized);
        }
        return new UmlClassNode(id, moduleId, displayName, packageName, kind, visibility,
                responsibility, description, updated, notes);
    }

    public Optional<UmlClassMember> memberById(String memberId) {
        String normalized = normalize(memberId);
        return members.stream().filter(member -> member.id().equals(normalized)).findFirst();
    }

    private static String required(String value, String label) {
        String normalized = normalize(value);
        if (normalized.isBlank()) {
            throw new IllegalArgumentException("El " + label + " de la clase UML no puede estar vacío.");
        }
        return normalized;
    }

    private static String normalizeOrDefault(String value, String fallback) {
        String normalized = normalize(value);
        return normalized.isBlank() ? fallback : normalized;
    }

    private static String normalize(String value) { return value == null ? "" : value.strip(); }
}
