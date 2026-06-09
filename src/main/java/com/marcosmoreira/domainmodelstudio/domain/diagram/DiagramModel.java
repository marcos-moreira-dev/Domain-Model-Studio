package com.marcosmoreira.domainmodelstudio.domain.diagram;

import com.marcosmoreira.domainmodelstudio.domain.er.AttributeElement;
import com.marcosmoreira.domainmodelstudio.domain.er.EntityElement;
import com.marcosmoreira.domainmodelstudio.domain.er.RelationshipElement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Modelo semántico de un diagrama de dominio.
 *
 * <p>Conserva entidades y relaciones en orden estable para mantener trazabilidad con el
 * Markdown. También mantiene mapas internos por ID para búsquedas rápidas sin obligar
 * a la UI a recorrer listas manualmente.</p>
 */
public final class DiagramModel {

    private final List<EntityElement> entities;
    private final List<RelationshipElement> relationships;
    private final Map<DiagramElementId, EntityElement> entitiesById;
    private final Map<DiagramElementId, RelationshipElement> relationshipsById;

    public DiagramModel(List<EntityElement> entities, List<RelationshipElement> relationships) {
        this.entities = List.copyOf(entities == null ? List.of() : entities);
        this.relationships = List.copyOf(relationships == null ? List.of() : relationships);
        this.entitiesById = indexEntities(this.entities);
        this.relationshipsById = indexRelationships(this.relationships);
    }

    public static DiagramModel empty() {
        return new DiagramModel(List.of(), List.of());
    }

    public List<EntityElement> entities() {
        return entities;
    }

    public List<RelationshipElement> relationships() {
        return relationships;
    }

    public Optional<EntityElement> entityById(DiagramElementId id) {
        return Optional.ofNullable(entitiesById.get(id));
    }

    public Optional<RelationshipElement> relationshipById(DiagramElementId id) {
        return Optional.ofNullable(relationshipsById.get(id));
    }

    public Optional<AttributeElement> attributeById(DiagramElementId attributeId) {
        if (attributeId == null) {
            return Optional.empty();
        }
        for (EntityElement entity : entities) {
            Optional<AttributeElement> attribute = entity.attributeById(attributeId);
            if (attribute.isPresent()) {
                return attribute;
            }
        }
        return Optional.empty();
    }

    public Optional<EntityElement> entityOwningAttribute(DiagramElementId attributeId) {
        if (attributeId == null) {
            return Optional.empty();
        }
        return entities.stream()
                .filter(entity -> entity.attributeById(attributeId).isPresent())
                .findFirst();
    }

    public boolean containsEntity(DiagramElementId id) {
        return entitiesById.containsKey(id);
    }

    public boolean containsElement(DiagramElementId id) {
        return entityById(id).isPresent() || relationshipById(id).isPresent() || attributeById(id).isPresent();
    }

    public DiagramModel withEntity(EntityElement entity) {
        Objects.requireNonNull(entity, "La entidad no puede ser null");
        List<EntityElement> updated = new ArrayList<>(entities);
        updated.add(entity);
        return new DiagramModel(updated, relationships);
    }

    public DiagramModel withUpdatedEntity(EntityElement updatedEntity) {
        Objects.requireNonNull(updatedEntity, "La entidad actualizada no puede ser null");
        List<EntityElement> updated = new ArrayList<>();
        boolean replaced = false;
        for (EntityElement entity : entities) {
            if (entity.id().equals(updatedEntity.id())) {
                updated.add(updatedEntity);
                replaced = true;
            } else {
                updated.add(entity);
            }
        }
        if (!replaced) {
            throw new IllegalArgumentException("No existe entidad para actualizar: " + updatedEntity.id());
        }
        return new DiagramModel(updated, relationships);
    }

    public DiagramModel withAttribute(DiagramElementId entityId, AttributeElement attribute) {
        Objects.requireNonNull(entityId, "El ID de la entidad no puede ser null");
        Objects.requireNonNull(attribute, "El atributo no puede ser null");
        EntityElement entity = entityById(entityId)
                .orElseThrow(() -> new IllegalArgumentException("No existe entidad para agregar atributo: " + entityId));
        return withUpdatedEntity(entity.withAttribute(attribute));
    }

    public DiagramModel withoutAttribute(DiagramElementId attributeId) {
        Objects.requireNonNull(attributeId, "El ID del atributo no puede ser null");
        List<EntityElement> updated = new ArrayList<>();
        boolean removed = false;
        for (EntityElement entity : entities) {
            if (entity.attributeById(attributeId).isPresent()) {
                updated.add(entity.withoutAttribute(attributeId));
                removed = true;
            } else {
                updated.add(entity);
            }
        }
        if (!removed) {
            throw new IllegalArgumentException("No existe atributo para eliminar: " + attributeId);
        }
        return new DiagramModel(updated, relationships);
    }

    public DiagramModel withRelationship(RelationshipElement relationship) {
        Objects.requireNonNull(relationship, "La relación no puede ser null");
        ensureRelationshipEntitiesExist(relationship);
        List<RelationshipElement> updated = new ArrayList<>(relationships);
        updated.add(relationship);
        return new DiagramModel(entities, updated);
    }

    public DiagramModel withUpdatedRelationship(RelationshipElement updatedRelationship) {
        Objects.requireNonNull(updatedRelationship, "La relación actualizada no puede ser null");
        ensureRelationshipEntitiesExist(updatedRelationship);
        List<RelationshipElement> updated = new ArrayList<>();
        boolean replaced = false;
        for (RelationshipElement relationship : relationships) {
            if (relationship.id().equals(updatedRelationship.id())) {
                updated.add(updatedRelationship);
                replaced = true;
            } else {
                updated.add(relationship);
            }
        }
        if (!replaced) {
            throw new IllegalArgumentException("No existe relación para actualizar: " + updatedRelationship.id());
        }
        return new DiagramModel(entities, updated);
    }

    public DiagramModel withoutRelationship(DiagramElementId relationshipId) {
        Objects.requireNonNull(relationshipId, "El ID de la relación no puede ser null");
        List<RelationshipElement> updated = relationships.stream()
                .filter(relationship -> !relationship.id().equals(relationshipId))
                .toList();
        if (updated.size() == relationships.size()) {
            throw new IllegalArgumentException("No existe relación para eliminar: " + relationshipId);
        }
        return new DiagramModel(entities, updated);
    }

    public DiagramModel withoutEntity(DiagramElementId entityId) {
        Objects.requireNonNull(entityId, "El ID de la entidad no puede ser null");
        List<EntityElement> updatedEntities = entities.stream()
                .filter(entity -> !entity.id().equals(entityId))
                .toList();
        if (updatedEntities.size() == entities.size()) {
            throw new IllegalArgumentException("No existe entidad para eliminar: " + entityId);
        }
        List<RelationshipElement> updatedRelationships = relationships.stream()
                .filter(relationship -> !relationship.fromEntityId().equals(entityId))
                .filter(relationship -> !relationship.toEntityId().equals(entityId))
                .toList();
        return new DiagramModel(updatedEntities, updatedRelationships);
    }

    public DiagramModel withRenamedElement(DiagramElementId elementId, String updatedName) {
        Objects.requireNonNull(elementId, "El ID del elemento no puede ser null");

        List<EntityElement> updatedEntities = new ArrayList<>();
        boolean changed = false;
        for (EntityElement entity : entities) {
            if (entity.id().equals(elementId)) {
                updatedEntities.add(entity.renamed(updatedName));
                changed = true;
                continue;
            }
            EntityElement updatedEntity = entity;
            for (AttributeElement attribute : entity.attributes()) {
                if (attribute.id().equals(elementId)) {
                    updatedEntity = entity.withUpdatedAttribute(attribute.renamed(updatedName));
                    changed = true;
                    break;
                }
            }
            updatedEntities.add(updatedEntity);
        }

        List<RelationshipElement> updatedRelationships = new ArrayList<>();
        for (RelationshipElement relationship : relationships) {
            if (relationship.id().equals(elementId)) {
                updatedRelationships.add(relationship.renamed(updatedName));
                changed = true;
            } else {
                updatedRelationships.add(relationship);
            }
        }

        if (!changed) {
            throw new IllegalArgumentException("No existe elemento para renombrar: " + elementId);
        }
        return new DiagramModel(updatedEntities, updatedRelationships);
    }

    public DiagramModel withUpdatedDescription(DiagramElementId elementId, String updatedDescription) {
        Objects.requireNonNull(elementId, "El ID del elemento no puede ser null");

        List<EntityElement> updatedEntities = new ArrayList<>();
        boolean changed = false;
        for (EntityElement entity : entities) {
            if (entity.id().equals(elementId)) {
                updatedEntities.add(entity.withDescription(updatedDescription));
                changed = true;
                continue;
            }
            EntityElement updatedEntity = entity;
            for (AttributeElement attribute : entity.attributes()) {
                if (attribute.id().equals(elementId)) {
                    updatedEntity = entity.withUpdatedAttribute(attribute.withDescription(updatedDescription));
                    changed = true;
                    break;
                }
            }
            updatedEntities.add(updatedEntity);
        }

        List<RelationshipElement> updatedRelationships = new ArrayList<>();
        for (RelationshipElement relationship : relationships) {
            if (relationship.id().equals(elementId)) {
                updatedRelationships.add(relationship.withDescription(updatedDescription));
                changed = true;
            } else {
                updatedRelationships.add(relationship);
            }
        }

        if (!changed) {
            throw new IllegalArgumentException("No existe elemento para actualizar descripción: " + elementId);
        }
        return new DiagramModel(updatedEntities, updatedRelationships);
    }

    public int entityCount() {
        return entities.size();
    }

    public int relationshipCount() {
        return relationships.size();
    }

    public boolean isEmpty() {
        return entities.isEmpty() && relationships.isEmpty();
    }

    private void ensureRelationshipEntitiesExist(RelationshipElement relationship) {
        if (!containsEntity(relationship.fromEntityId())) {
            throw new IllegalArgumentException("La entidad origen no existe: " + relationship.fromEntityId());
        }
        if (!containsEntity(relationship.toEntityId())) {
            throw new IllegalArgumentException("La entidad destino no existe: " + relationship.toEntityId());
        }
    }

    private static Map<DiagramElementId, EntityElement> indexEntities(List<EntityElement> entities) {
        Map<DiagramElementId, EntityElement> index = new LinkedHashMap<>();
        for (EntityElement entity : entities) {
            EntityElement previous = index.putIfAbsent(entity.id(), entity);
            if (previous != null) {
                throw new IllegalArgumentException("Entidad duplicada: " + entity.id());
            }
        }
        return Map.copyOf(index);
    }

    private static Map<DiagramElementId, RelationshipElement> indexRelationships(List<RelationshipElement> relationships) {
        Map<DiagramElementId, RelationshipElement> index = new LinkedHashMap<>();
        for (RelationshipElement relationship : relationships) {
            RelationshipElement previous = index.putIfAbsent(relationship.id(), relationship);
            if (previous != null) {
                throw new IllegalArgumentException("Relación duplicada: " + relationship.id());
            }
        }
        return Map.copyOf(index);
    }
}
