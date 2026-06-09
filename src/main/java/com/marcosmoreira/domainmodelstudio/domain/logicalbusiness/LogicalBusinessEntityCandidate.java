package com.marcosmoreira.domainmodelstudio.domain.logicalbusiness;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Entidad candidata identificada desde acciones, reglas, estados, evidencia o
 * reportes del levantamiento lógico.
 *
 * <p>La entidad conserva justificación, fuentes, reglas asociadas, invariantes y
 * casos de uso que la crean, modifican o consultan. Esto permite discutir una
 * posible estructura de información sin perder la razón de negocio que originó
 * cada candidato, sin tratarlo todavía como tabla final.</p>
 */
public final class LogicalBusinessEntityCandidate {

    private final String id;
    private final String name;
    private final LogicalBusinessItemStatus status;
    private final String logicalJustification;
    private final List<LogicalBusinessAttributeCandidate> attributes;
    private final List<LogicalBusinessRelationshipCandidate> relationships;
    private final List<String> sourceReferences;
    private final List<String> associatedRules;
    private final List<String> associatedInvariants;
    private final List<String> createdByUseCases;
    private final List<String> modifiedByUseCases;
    private final List<String> queriedByUseCases;
    private final String modelingRisk;

    /**
     * Construye una entidad candidata validada.
     *
     * <p>El ID debe iniciar con {@code ENT-}; atributos y relaciones propias
     * deben pertenecer a la entidad o tocar entidades existentes según el contrato
     * del levantamiento.</p>
     */
    public LogicalBusinessEntityCandidate(
            String id,
            String name,
            LogicalBusinessItemStatus status,
            String logicalJustification,
            List<LogicalBusinessAttributeCandidate> attributes,
            List<LogicalBusinessRelationshipCandidate> relationships,
            List<String> sourceReferences,
            List<String> associatedRules,
            List<String> associatedInvariants,
            List<String> createdByUseCases,
            List<String> modifiedByUseCases,
            List<String> queriedByUseCases,
            String modelingRisk
    ) {
        this.id = LogicalBusinessText.require(id, "id");
        this.name = LogicalBusinessText.require(name, "name");
        this.status = status == null ? LogicalBusinessItemStatus.DRAFT : status;
        this.logicalJustification = LogicalBusinessText.require(logicalJustification, "logicalJustification");
        this.attributes = List.copyOf(attributes == null ? List.of() : attributes);
        this.relationships = List.copyOf(relationships == null ? List.of() : relationships);
        this.sourceReferences = LogicalBusinessText.normalizedList(sourceReferences);
        this.associatedRules = LogicalBusinessText.normalizedList(associatedRules);
        this.associatedInvariants = LogicalBusinessText.normalizedList(associatedInvariants);
        this.createdByUseCases = LogicalBusinessText.normalizedList(createdByUseCases);
        this.modifiedByUseCases = LogicalBusinessText.normalizedList(modifiedByUseCases);
        this.queriedByUseCases = LogicalBusinessText.normalizedList(queriedByUseCases);
        this.modelingRisk = LogicalBusinessText.normalize(modelingRisk);
        validateIdentity();
        validateOwnedElements();
    }

    /**
     * Crea una entidad mínima con justificación lógica obligatoria.
     */
    public static LogicalBusinessEntityCandidate of(String id, String name, String logicalJustification) {
        return new LogicalBusinessEntityCandidate(id, name, LogicalBusinessItemStatus.DRAFT, logicalJustification,
                List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), "");
    }

    public String id() {
        return id;
    }

    public String name() {
        return name;
    }

    public LogicalBusinessItemStatus status() {
        return status;
    }

    public String logicalJustification() {
        return logicalJustification;
    }

    public List<LogicalBusinessAttributeCandidate> attributes() {
        return attributes;
    }

    public List<LogicalBusinessRelationshipCandidate> relationships() {
        return relationships;
    }

    public List<String> sourceReferences() {
        return sourceReferences;
    }

    public List<String> associatedRules() {
        return associatedRules;
    }

    public List<String> associatedInvariants() {
        return associatedInvariants;
    }

    public List<String> createdByUseCases() {
        return createdByUseCases;
    }

    public List<String> modifiedByUseCases() {
        return modifiedByUseCases;
    }

    public List<String> queriedByUseCases() {
        return queriedByUseCases;
    }

    public String modelingRisk() {
        return modelingRisk;
    }

    /**
     * Busca un atributo propio por ID normalizado.
     */
    public Optional<LogicalBusinessAttributeCandidate> attributeById(String attributeId) {
        String normalizedId = LogicalBusinessText.normalize(attributeId);
        return attributes.stream().filter(attribute -> attribute.id().equals(normalizedId)).findFirst();
    }

    /**
     * Agrega un atributo candidato y rechaza duplicados dentro de la entidad.
     */
    public LogicalBusinessEntityCandidate withAttribute(LogicalBusinessAttributeCandidate attribute) {
        Objects.requireNonNull(attribute, "attribute");
        if (attributeById(attribute.id()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un atributo candidato con ID: " + attribute.id());
        }
        List<LogicalBusinessAttributeCandidate> updated = new ArrayList<>(attributes);
        updated.add(attribute);
        return copyWith(updated, relationships);
    }

    /**
     * Agrega una relación candidata asociada a esta entidad.
     */
    public LogicalBusinessEntityCandidate withRelationship(LogicalBusinessRelationshipCandidate relationship) {
        Objects.requireNonNull(relationship, "relationship");
        if (relationships.stream().anyMatch(existing -> existing.id().equals(relationship.id()))) {
            throw new IllegalArgumentException("Ya existe una relación candidata con ID: " + relationship.id());
        }
        List<LogicalBusinessRelationshipCandidate> updated = new ArrayList<>(relationships);
        updated.add(relationship);
        return copyWith(attributes, updated);
    }

    /**
     * Actualiza trazabilidad y metadatos de modelado sin perder atributos ni relaciones.
     */
    public LogicalBusinessEntityCandidate withEditableDetails(
            String updatedName,
            LogicalBusinessItemStatus updatedStatus,
            String updatedLogicalJustification,
            List<String> updatedSourceReferences,
            List<String> updatedAssociatedRules,
            List<String> updatedAssociatedInvariants,
            List<String> updatedCreatedByUseCases,
            List<String> updatedModifiedByUseCases,
            List<String> updatedQueriedByUseCases,
            String updatedModelingRisk
    ) {
        return new LogicalBusinessEntityCandidate(id, updatedName, updatedStatus, updatedLogicalJustification,
                attributes, relationships, updatedSourceReferences, updatedAssociatedRules,
                updatedAssociatedInvariants, updatedCreatedByUseCases, updatedModifiedByUseCases,
                updatedQueriedByUseCases, updatedModelingRisk);
    }

    public LogicalBusinessEntityCandidate withUpdatedAttribute(LogicalBusinessAttributeCandidate updatedAttribute) {
        Objects.requireNonNull(updatedAttribute, "updatedAttribute");
        return copyWith(replaceAttribute(updatedAttribute), relationships);
    }

    public LogicalBusinessEntityCandidate withUpdatedRelationship(LogicalBusinessRelationshipCandidate updatedRelationship) {
        Objects.requireNonNull(updatedRelationship, "updatedRelationship");
        return copyWith(attributes, replaceRelationship(updatedRelationship));
    }

    private List<LogicalBusinessAttributeCandidate> replaceAttribute(LogicalBusinessAttributeCandidate replacement) {
        List<LogicalBusinessAttributeCandidate> updated = new ArrayList<>();
        boolean replaced = false;
        for (LogicalBusinessAttributeCandidate attribute : attributes) {
            if (attribute.id().equals(replacement.id())) {
                updated.add(replacement);
                replaced = true;
            } else {
                updated.add(attribute);
            }
        }
        if (!replaced) {
            throw new IllegalArgumentException("No existe atributo candidato para actualizar: " + replacement.id());
        }
        return updated;
    }

    private List<LogicalBusinessRelationshipCandidate> replaceRelationship(LogicalBusinessRelationshipCandidate replacement) {
        List<LogicalBusinessRelationshipCandidate> updated = new ArrayList<>();
        boolean replaced = false;
        for (LogicalBusinessRelationshipCandidate relationship : relationships) {
            if (relationship.id().equals(replacement.id())) {
                updated.add(replacement);
                replaced = true;
            } else {
                updated.add(relationship);
            }
        }
        if (!replaced) {
            throw new IllegalArgumentException("No existe relación candidata para actualizar: " + replacement.id());
        }
        return updated;
    }

    private LogicalBusinessEntityCandidate copyWith(
            List<LogicalBusinessAttributeCandidate> updatedAttributes,
            List<LogicalBusinessRelationshipCandidate> updatedRelationships
    ) {
        return new LogicalBusinessEntityCandidate(id, name, status, logicalJustification, updatedAttributes,
                updatedRelationships, sourceReferences, associatedRules, associatedInvariants, createdByUseCases,
                modifiedByUseCases, queriedByUseCases, modelingRisk);
    }

    private void validateIdentity() {
        if (!LogicalBusinessItemKind.ENTITY.matchesId(id)) {
            throw new IllegalArgumentException("La entidad candidata debe usar ID ENT-XXX: " + id);
        }
    }

    private void validateOwnedElements() {
        attributes.forEach(attribute -> {
            if (!attribute.entityId().equals(id)) {
                throw new IllegalArgumentException("El atributo " + attribute.id() + " pertenece a otra entidad.");
            }
        });
        relationships.forEach(relationship -> {
            if (!relationship.sourceEntityId().equals(id) && !relationship.targetEntityId().equals(id)) {
                throw new IllegalArgumentException("La relación " + relationship.id() + " no toca la entidad " + id + ".");
            }
        });
    }
}
