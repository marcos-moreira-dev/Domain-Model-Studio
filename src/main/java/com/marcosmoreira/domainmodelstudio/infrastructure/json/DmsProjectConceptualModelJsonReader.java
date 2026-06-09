package com.marcosmoreira.domainmodelstudio.infrastructure.json;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramModel;
import com.marcosmoreira.domainmodelstudio.domain.er.AttributeElement;
import com.marcosmoreira.domainmodelstudio.domain.er.AttributeTag;
import com.marcosmoreira.domainmodelstudio.domain.er.Cardinality;
import com.marcosmoreira.domainmodelstudio.domain.er.EntityElement;
import com.marcosmoreira.domainmodelstudio.domain.er.EntityKind;
import com.marcosmoreira.domainmodelstudio.domain.er.ParticipationType;
import com.marcosmoreira.domainmodelstudio.domain.er.RelationshipElement;
import com.marcosmoreira.domainmodelstudio.domain.er.RelationshipKind;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

/**
 * Lee el modelo conceptual básico almacenado en el bloque {@code model} de un archivo {@code .dms}.
 *
 * <p>Esta clase mantiene separada la persistencia conceptual legacy de los documentos especializados.
 * El lector principal del proyecto coordina el roundtrip, pero no debe conocer el detalle de entidades,
 * atributos y relaciones Chen/Crow's Foot.</p>
 */
final class DmsProjectConceptualModelJsonReader {

    DiagramModel readModel(Map<String, Object> object) {
        List<EntityElement> entities = new ArrayList<>();
        for (Object entityValue : array(object, "entities")) {
            entities.add(readEntity(asObject(entityValue, "entity")));
        }
        List<RelationshipElement> relationships = new ArrayList<>();
        for (Object relationshipValue : array(object, "relationships")) {
            relationships.add(readRelationship(asObject(relationshipValue, "relationship")));
        }
        return new DiagramModel(entities, relationships);
    }

    private EntityElement readEntity(Map<String, Object> object) {
        List<AttributeElement> attributes = new ArrayList<>();
        for (Object attributeValue : array(object, "attributes")) {
            attributes.add(readAttribute(asObject(attributeValue, "attribute")));
        }
        return new EntityElement(
                DiagramElementId.of(string(object, "id")),
                string(object, "name"),
                readEntityKind(stringOrDefault(object, "kind", "STRONG")),
                stringOrDefault(object, "module", ""),
                stringOrDefault(object, "description", ""),
                attributes
        );
    }

    private AttributeElement readAttribute(Map<String, Object> object) {
        EnumSet<AttributeTag> tags = readAttributeTags(object);
        return new AttributeElement(
                DiagramElementId.of(string(object, "id")),
                string(object, "name"),
                tags,
                stringOrDefault(object, "description", "")
        );
    }

    private RelationshipElement readRelationship(Map<String, Object> object) {
        return new RelationshipElement(
                DiagramElementId.of(string(object, "id")),
                string(object, "name"),
                DiagramElementId.of(stringOrDefault(object, "fromEntityId", stringOrDefault(object, "from", ""))),
                DiagramElementId.of(stringOrDefault(object, "toEntityId", stringOrDefault(object, "to", ""))),
                Cardinality.of(string(object, "fromCardinality")),
                Cardinality.of(string(object, "toCardinality")),
                readRelationshipKind(object),
                enumValue(ParticipationType.class, stringOrDefault(object, "fromParticipation", "UNSPECIFIED"), ParticipationType.UNSPECIFIED),
                enumValue(ParticipationType.class, stringOrDefault(object, "toParticipation", "UNSPECIFIED"), ParticipationType.UNSPECIFIED),
                stringOrDefault(object, "description", "")
        );
    }

    private EntityKind readEntityKind(String rawKind) {
        String normalized = rawKind == null ? "" : rawKind.trim().toUpperCase(java.util.Locale.ROOT);
        return switch (normalized) {
            case "WEAK", "WEAK_ENTITY", "ENTIDAD_DEBIL" -> EntityKind.WEAK;
            default -> EntityKind.STRONG;
        };
    }

    private EnumSet<AttributeTag> readAttributeTags(Map<String, Object> object) {
        EnumSet<AttributeTag> tags = EnumSet.noneOf(AttributeTag.class);
        for (Object tagValue : array(object, "tags")) {
            tags.add(enumValue(AttributeTag.class, String.valueOf(tagValue), AttributeTag.OPTIONAL));
        }
        if (tags.isEmpty()) {
            String kind = stringOrDefault(object, "kind", "").trim().toUpperCase(java.util.Locale.ROOT);
            switch (kind) {
                case "KEY", "PK", "PRIMARY_KEY" -> tags.add(AttributeTag.PRIMARY_KEY);
                case "PARTIAL_KEY" -> tags.add(AttributeTag.PARTIAL_KEY);
                case "UNIQUE" -> tags.add(AttributeTag.UNIQUE);
                case "DERIVED" -> tags.add(AttributeTag.DERIVED);
                case "MULTIVALUED" -> tags.add(AttributeTag.MULTIVALUED);
                case "COMPOSITE" -> tags.add(AttributeTag.COMPOSITE);
                case "SENSITIVE" -> tags.add(AttributeTag.SENSITIVE);
                default -> { }
            }
        }
        return tags;
    }

    private RelationshipKind readRelationshipKind(Map<String, Object> object) {
        if (boolOrDefault(object, "identifying", false)) {
            return RelationshipKind.IDENTIFYING;
        }
        String rawKind = stringOrDefault(object, "kind", "REGULAR").trim().toUpperCase(java.util.Locale.ROOT);
        return switch (rawKind) {
            case "IDENTIFYING", "IDENTIFICADORA" -> RelationshipKind.IDENTIFYING;
            case "ASSOCIATIVE", "ASOCIATIVA" -> RelationshipKind.ASSOCIATIVE;
            default -> RelationshipKind.REGULAR;
        };
    }

    private List<Object> array(Map<String, Object> parent, String key) {
        Object value = parent.get(key);
        if (value == null) {
            return List.of();
        }
        if (value instanceof List<?> list) {
            return new ArrayList<>(list);
        }
        throw new IllegalArgumentException("Se esperaba arreglo JSON en: " + key);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> asObject(Object value, String context) {
        if (value instanceof Map<?, ?> map) {
            return (Map<String, Object>) map;
        }
        throw new IllegalArgumentException("Se esperaba objeto JSON en: " + context);
    }

    private String string(Map<String, Object> object, String key) {
        if (!object.containsKey(key)) {
            throw new IllegalArgumentException("Falta string requerido: " + key);
        }
        return String.valueOf(object.get(key));
    }

    private String stringOrDefault(Map<String, Object> object, String key, String defaultValue) {
        Object value = object.get(key);
        return value == null ? defaultValue : String.valueOf(value);
    }

    private boolean boolOrDefault(Map<String, Object> object, String key, boolean defaultValue) {
        Object value = object.get(key);
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Boolean bool) {
            return bool;
        }
        return Boolean.parseBoolean(String.valueOf(value));
    }

    private <E extends Enum<E>> E enumValue(Class<E> enumType, String rawValue, E fallback) {
        if (rawValue == null || rawValue.isBlank()) {
            return fallback;
        }
        try {
            return Enum.valueOf(enumType, rawValue.trim().toUpperCase(java.util.Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            return fallback;
        }
    }
}
