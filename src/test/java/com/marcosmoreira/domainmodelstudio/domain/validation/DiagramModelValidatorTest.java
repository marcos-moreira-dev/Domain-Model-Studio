package com.marcosmoreira.domainmodelstudio.domain.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramModel;
import com.marcosmoreira.domainmodelstudio.domain.er.AttributeElement;
import com.marcosmoreira.domainmodelstudio.domain.er.AttributeTag;
import com.marcosmoreira.domainmodelstudio.domain.er.EntityElement;
import com.marcosmoreira.domainmodelstudio.domain.er.RelationshipElement;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DiagramModelValidatorTest {

    private final DiagramModelValidator validator = new DiagramModelValidator();

    @Test
    void acceptsBasicValidModel() {
        EntityElement product = EntityElement.strong(
                "producto",
                "Producto",
                List.of(AttributeElement.withTags("producto_id", "pk id", Set.of(AttributeTag.PRIMARY_KEY)))
        );
        EntityElement category = EntityElement.strong(
                "categoria",
                "Categoría",
                List.of(AttributeElement.withTags("categoria_id", "pk id", Set.of(AttributeTag.PRIMARY_KEY)))
        );
        RelationshipElement belongsTo = RelationshipElement.between(
                "pertenece",
                "Pertenece",
                "producto",
                "categoria",
                "0..M",
                "1"
        );

        ValidationResult result = validator.validate(new DiagramModel(List.of(product, category), List.of(belongsTo)));

        assertTrue(result.isValid());
        assertEquals(0, result.errors().size());
    }

    @Test
    void detectsRelationshipWithUnknownEntity() {
        EntityElement product = EntityElement.strong(
                "producto",
                "Producto",
                List.of(AttributeElement.withTags("producto_id", "pk id", Set.of(AttributeTag.PRIMARY_KEY)))
        );
        RelationshipElement belongsTo = RelationshipElement.between(
                "pertenece",
                "Pertenece",
                "producto",
                "categoria_inexistente",
                "0..M",
                "1"
        );

        ValidationResult result = validator.validate(new DiagramModel(List.of(product), List.of(belongsTo)));

        assertFalse(result.isValid());
        assertTrue(result.errors().stream().anyMatch(issue -> issue.code() == ValidationCode.RELATIONSHIP_REFERENCES_UNKNOWN_ENTITY));
    }

    @Test
    void warnsAboutStrongEntityWithoutPrimaryKey() {
        EntityElement product = EntityElement.strong("producto", "Producto", List.of(AttributeElement.normal("nombre", "nombre")));

        ValidationResult result = validator.validate(new DiagramModel(List.of(product), List.of()));

        assertTrue(result.isValid());
        assertTrue(result.warnings().stream().anyMatch(issue -> issue.code() == ValidationCode.ENTITY_WITHOUT_PRIMARY_KEY));
    }

    @Test
    void detectsDuplicatedAttributeIdsInsideEntity() {
        EntityElement product = EntityElement.strong(
                "producto",
                "Producto",
                List.of(
                        AttributeElement.normal("nombre", "nombre"),
                        AttributeElement.normal("nombre", "nombre comercial")
                )
        );

        ValidationResult result = validator.validate(new DiagramModel(List.of(product), List.of()));

        assertFalse(result.isValid());
        assertTrue(result.errors().stream().anyMatch(issue -> issue.code() == ValidationCode.ENTITY_WITH_DUPLICATED_ATTRIBUTE_ID));
        assertEquals(DiagramElementId.of("producto"), result.errors().getFirst().elementId().orElseThrow());
    }
}
