package com.marcosmoreira.domainmodelstudio.domain.er;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElement;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementType;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

/**
 * Atributo conceptual de una entidad.
 *
 * <p>En Chen suele representarse como óvalo, pero esta clase no conoce figuras. Solo
 * conserva la semántica necesaria para que una notación visual lo represente después.</p>
 */
public final class AttributeElement implements DiagramElement {

    private final DiagramElementId id;
    private final String name;
    private final Set<AttributeTag> tags;
    private final String description;

    public AttributeElement(DiagramElementId id, String name, Set<AttributeTag> tags, String description) {
        this.id = Objects.requireNonNull(id, "El ID del atributo no puede ser null");
        this.name = requireText(name, "El nombre del atributo no puede estar vacío");
        this.tags = copyTags(tags);
        this.description = normalizeOptionalText(description);
    }

    public static AttributeElement normal(String id, String name) {
        return new AttributeElement(DiagramElementId.of(id), name, Set.of(), "");
    }

    public static AttributeElement withTags(String id, String name, Set<AttributeTag> tags) {
        return new AttributeElement(DiagramElementId.of(id), name, tags, "");
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
        return DiagramElementType.ATTRIBUTE;
    }

    public Set<AttributeTag> tags() {
        return tags;
    }

    public String description() {
        return description;
    }

    public boolean hasTag(AttributeTag tag) {
        return tags.contains(tag);
    }

    public boolean isPrimaryKey() {
        return hasTag(AttributeTag.PRIMARY_KEY);
    }

    public boolean isPartialKey() {
        return hasTag(AttributeTag.PARTIAL_KEY);
    }

    public boolean isKeyLike() {
        return isPrimaryKey() || isPartialKey();
    }

    public boolean isDerived() {
        return hasTag(AttributeTag.DERIVED);
    }

    public boolean isOptional() {
        return hasTag(AttributeTag.OPTIONAL);
    }

    public boolean isMultivalued() {
        return hasTag(AttributeTag.MULTIVALUED);
    }


    public AttributeElement withDescription(String updatedDescription) {
        return new AttributeElement(id, name, tags, updatedDescription);
    }

    public AttributeElement renamed(String updatedName) {
        return new AttributeElement(id, updatedName, tags, description);
    }

    private static Set<AttributeTag> copyTags(Set<AttributeTag> tags) {
        if (tags == null || tags.isEmpty()) {
            return Set.of();
        }
        EnumSet<AttributeTag> copy = EnumSet.copyOf(tags);
        return Collections.unmodifiableSet(copy);
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
