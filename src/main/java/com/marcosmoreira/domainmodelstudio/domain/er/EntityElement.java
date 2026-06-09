package com.marcosmoreira.domainmodelstudio.domain.er;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElement;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Entidad conceptual del modelo ER.
 */
public final class EntityElement implements DiagramElement {

    private final DiagramElementId id;
    private final String name;
    private final EntityKind kind;
    private final String module;
    private final String description;
    private final List<AttributeElement> attributes;

    public EntityElement(
            DiagramElementId id,
            String name,
            EntityKind kind,
            String module,
            String description,
            List<AttributeElement> attributes
    ) {
        this.id = Objects.requireNonNull(id, "El ID de la entidad no puede ser null");
        this.name = requireText(name, "El nombre de la entidad no puede estar vacío");
        this.kind = kind == null ? EntityKind.STRONG : kind;
        this.module = normalizeOptionalText(module);
        this.description = normalizeOptionalText(description);
        this.attributes = List.copyOf(attributes == null ? List.of() : attributes);
    }

    public static EntityElement strong(String id, String name, List<AttributeElement> attributes) {
        return new EntityElement(DiagramElementId.of(id), name, EntityKind.STRONG, "", "", attributes);
    }

    @Override
    public DiagramElementId id() {
        return id;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public DiagramElementType type() {
        return DiagramElementType.ENTITY;
    }

    public EntityKind kind() {
        return kind;
    }

    public String module() {
        return module;
    }

    public String description() {
        return description;
    }

    public List<AttributeElement> attributes() {
        return attributes;
    }

    public EntityElement withAttribute(AttributeElement attribute) {
        Objects.requireNonNull(attribute, "El atributo no puede ser null");
        List<AttributeElement> updated = new ArrayList<>(attributes);
        updated.add(attribute);
        return new EntityElement(id, name, kind, module, description, updated);
    }

    public EntityElement withoutAttribute(DiagramElementId attributeId) {
        Objects.requireNonNull(attributeId, "El ID del atributo no puede ser null");
        List<AttributeElement> updated = attributes.stream()
                .filter(attribute -> !attribute.id().equals(attributeId))
                .toList();
        if (updated.size() == attributes.size()) {
            throw new IllegalArgumentException("No existe atributo en la entidad " + id + ": " + attributeId);
        }
        return new EntityElement(id, name, kind, module, description, updated);
    }

    public EntityElement withDescription(String updatedDescription) {
        return new EntityElement(id, name, kind, module, updatedDescription, attributes);
    }

    public EntityElement renamed(String updatedName) {
        return new EntityElement(id, updatedName, kind, module, description, attributes);
    }

    public EntityElement withUpdatedAttribute(AttributeElement updatedAttribute) {
        Objects.requireNonNull(updatedAttribute, "El atributo actualizado no puede ser null");
        List<AttributeElement> updated = new ArrayList<>();
        boolean replaced = false;
        for (AttributeElement attribute : attributes) {
            if (attribute.id().equals(updatedAttribute.id())) {
                updated.add(updatedAttribute);
                replaced = true;
            } else {
                updated.add(attribute);
            }
        }
        if (!replaced) {
            throw new IllegalArgumentException("No existe atributo en la entidad " + id + ": " + updatedAttribute.id());
        }
        return new EntityElement(id, name, kind, module, description, updated);
    }

    public Optional<AttributeElement> attributeById(DiagramElementId attributeId) {
        return attributes.stream()
                .filter(attribute -> attribute.id().equals(attributeId))
                .findFirst();
    }

    public boolean hasPrimaryKey() {
        return attributes.stream().anyMatch(AttributeElement::isPrimaryKey);
    }

    private static String requireText(String value, String message) {
        String normalized = Objects.requireNonNull(value, message).trim();
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
        return normalized;
    }

    private static String normalizeOptionalText(String value) {
        return value == null ? "" : value.trim();
    }
}
