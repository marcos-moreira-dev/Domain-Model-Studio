package com.marcosmoreira.domainmodelstudio.domain.validation;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramModel;
import com.marcosmoreira.domainmodelstudio.domain.er.AttributeElement;
import com.marcosmoreira.domainmodelstudio.domain.er.EntityElement;
import com.marcosmoreira.domainmodelstudio.domain.er.EntityKind;
import com.marcosmoreira.domainmodelstudio.domain.er.RelationshipElement;
import com.marcosmoreira.domainmodelstudio.domain.er.RelationshipKind;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Validador semántico del modelo ER.
 *
 * <p>Su responsabilidad es revisar la coherencia del modelo conceptual antes de renderizar,
 * guardar o exportar. No conoce layout, estilos, JavaFX ni archivos.</p>
 */
public final class DiagramModelValidator {

    public ValidationResult validate(DiagramModel model) {
        Objects.requireNonNull(model, "El modelo no puede ser null");
        ValidationResult result = ValidationResult.ok();

        for (EntityElement entity : model.entities()) {
            result = result.merge(validateEntity(entity));
        }

        for (RelationshipElement relationship : model.relationships()) {
            result = result.merge(validateRelationshipReferences(model, relationship));
        }

        result = result.merge(validateWeakEntities(model));
        return result;
    }

    private ValidationResult validateEntity(EntityElement entity) {
        ValidationResult result = ValidationResult.ok();

        if (!entity.hasPrimaryKey() && entity.kind() == EntityKind.STRONG) {
            result = result.plus(ValidationIssue.warning(
                    ValidationCode.ENTITY_WITHOUT_PRIMARY_KEY,
                    entity.id(),
                    "La entidad fuerte '" + entity.name() + "' no tiene atributo marcado como [pk]."
            ));
        }

        result = result.merge(validateDuplicatedAttributeIds(entity));
        result = result.merge(validateDuplicatedAttributeNames(entity));
        return result;
    }

    private ValidationResult validateDuplicatedAttributeIds(EntityElement entity) {
        ValidationResult result = ValidationResult.ok();
        Set<DiagramElementId> seen = new HashSet<>();

        for (AttributeElement attribute : entity.attributes()) {
            if (!seen.add(attribute.id())) {
                result = result.plus(ValidationIssue.error(
                        ValidationCode.ENTITY_WITH_DUPLICATED_ATTRIBUTE_ID,
                        entity.id(),
                        "La entidad '" + entity.name() + "' tiene el atributo duplicado con ID '" + attribute.id() + "'."
                ));
            }
        }

        return result;
    }

    private ValidationResult validateDuplicatedAttributeNames(EntityElement entity) {
        ValidationResult result = ValidationResult.ok();
        Set<String> seen = new HashSet<>();

        for (AttributeElement attribute : entity.attributes()) {
            String normalizedName = attribute.name().trim().toLowerCase();
            if (!seen.add(normalizedName)) {
                result = result.plus(ValidationIssue.warning(
                        ValidationCode.ENTITY_WITH_DUPLICATED_ATTRIBUTE_NAME,
                        entity.id(),
                        "La entidad '" + entity.name() + "' tiene más de un atributo visible llamado '" + attribute.name() + "'."
                ));
            }
        }

        return result;
    }

    private ValidationResult validateRelationshipReferences(DiagramModel model, RelationshipElement relationship) {
        ValidationResult result = ValidationResult.ok();

        if (!model.containsEntity(relationship.fromEntityId())) {
            result = result.plus(ValidationIssue.error(
                    ValidationCode.RELATIONSHIP_REFERENCES_UNKNOWN_ENTITY,
                    relationship.id(),
                    "La relación '" + relationship.name() + "' referencia una entidad origen inexistente: "
                            + relationship.fromEntityId()
            ));
        }

        if (!model.containsEntity(relationship.toEntityId())) {
            result = result.plus(ValidationIssue.error(
                    ValidationCode.RELATIONSHIP_REFERENCES_UNKNOWN_ENTITY,
                    relationship.id(),
                    "La relación '" + relationship.name() + "' referencia una entidad destino inexistente: "
                            + relationship.toEntityId()
            ));
        }

        if (relationship.fromEntityId().equals(relationship.toEntityId())) {
            result = result.plus(ValidationIssue.warning(
                    ValidationCode.RELATIONSHIP_SELF_REFERENCE,
                    relationship.id(),
                    "La relación '" + relationship.name() + "' conecta la entidad consigo misma; verificar si es intencional."
            ));
        }

        return result;
    }

    private ValidationResult validateWeakEntities(DiagramModel model) {
        ValidationResult result = ValidationResult.ok();

        for (EntityElement entity : model.entities()) {
            if (entity.kind() == EntityKind.WEAK && !hasIdentifyingRelationship(model, entity.id())) {
                result = result.plus(ValidationIssue.warning(
                        ValidationCode.WEAK_ENTITY_WITHOUT_IDENTIFYING_RELATIONSHIP,
                        entity.id(),
                        "La entidad débil '" + entity.name() + "' no aparece conectada a una relación identificadora."
                ));
            }
        }

        return result;
    }

    private boolean hasIdentifyingRelationship(DiagramModel model, DiagramElementId entityId) {
        for (RelationshipElement relationship : model.relationships()) {
            boolean touchesEntity = relationship.fromEntityId().equals(entityId) || relationship.toEntityId().equals(entityId);
            if (touchesEntity && relationship.kind() == RelationshipKind.IDENTIFYING) {
                return true;
            }
        }
        return false;
    }
}
