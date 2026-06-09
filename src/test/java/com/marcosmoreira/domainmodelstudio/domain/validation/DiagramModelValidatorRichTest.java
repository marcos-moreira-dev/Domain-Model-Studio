package com.marcosmoreira.domainmodelstudio.domain.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramModel;
import com.marcosmoreira.domainmodelstudio.domain.er.AttributeElement;
import com.marcosmoreira.domainmodelstudio.domain.er.AttributeTag;
import com.marcosmoreira.domainmodelstudio.domain.er.EntityElement;
import com.marcosmoreira.domainmodelstudio.domain.er.EntityKind;
import com.marcosmoreira.domainmodelstudio.domain.er.ParticipationType;
import com.marcosmoreira.domainmodelstudio.domain.er.RelationshipElement;
import com.marcosmoreira.domainmodelstudio.domain.er.RelationshipKind;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

/**
 * Pruebas de validación semántica con casos más cercanos a Chen real.
 *
 * <p>Estas pruebas refuerzan que el validador distinga errores bloqueantes de
 * advertencias útiles para trazabilidad humana. No validan dibujo ni JavaFX.</p>
 */
class DiagramModelValidatorRichTest {

    private final DiagramModelValidator validator = new DiagramModelValidator();

    @Test
    void warnsWeakEntityWithoutIdentifyingRelationship() {
        EntityElement invoice = strongEntity("factura", "Factura");
        EntityElement invoiceLine = weakEntity("detalle_factura", "Detalle factura");
        RelationshipElement regular = RelationshipElement.between(
                "incluye",
                "Incluye",
                "factura",
                "detalle_factura",
                "1",
                "1..M"
        );

        ValidationResult result = validator.validate(new DiagramModel(List.of(invoice, invoiceLine), List.of(regular)));

        assertTrue(result.isValid());
        assertTrue(result.warnings().stream().anyMatch(
                issue -> issue.code() == ValidationCode.WEAK_ENTITY_WITHOUT_IDENTIFYING_RELATIONSHIP
        ));
    }

    @Test
    void acceptsWeakEntityWithIdentifyingRelationship() {
        EntityElement invoice = strongEntity("factura", "Factura");
        EntityElement invoiceLine = weakEntity("detalle_factura", "Detalle factura");
        RelationshipElement identifying = new RelationshipElement(
                DiagramElementId.of("identifica_detalle"),
                "Identifica detalle",
                DiagramElementId.of("factura"),
                DiagramElementId.of("detalle_factura"),
                com.marcosmoreira.domainmodelstudio.domain.er.Cardinality.of("1"),
                com.marcosmoreira.domainmodelstudio.domain.er.Cardinality.of("1..M"),
                RelationshipKind.IDENTIFYING,
                ParticipationType.TOTAL,
                ParticipationType.TOTAL,
                "Una factura identifica sus detalles."
        );

        ValidationResult result = validator.validate(new DiagramModel(List.of(invoice, invoiceLine), List.of(identifying)));

        assertTrue(result.isValid());
        assertFalse(result.warnings().stream().anyMatch(
                issue -> issue.code() == ValidationCode.WEAK_ENTITY_WITHOUT_IDENTIFYING_RELATIONSHIP
        ));
    }

    @Test
    void warnsDuplicatedAttributeNamesButDoesNotBlockModel() {
        EntityElement product = EntityElement.strong(
                "producto",
                "Producto",
                List.of(
                        AttributeElement.withTags("producto_id", "pk id", Set.of(AttributeTag.PRIMARY_KEY)),
                        AttributeElement.normal("nombre_comercial", "Nombre"),
                        AttributeElement.normal("nombre_factura", " nombre ")
                )
        );

        ValidationResult result = validator.validate(new DiagramModel(List.of(product), List.of()));

        assertTrue(result.isValid());
        assertEquals(0, result.errors().size());
        assertTrue(result.warnings().stream().anyMatch(
                issue -> issue.code() == ValidationCode.ENTITY_WITH_DUPLICATED_ATTRIBUTE_NAME
        ));
    }

    @Test
    void warnsSelfReferenceSoTheUserCanConfirmItIsIntentional() {
        EntityElement category = strongEntity("categoria", "Categoría");
        RelationshipElement parentRelation = RelationshipElement.between(
                "subcategoria_de",
                "Subcategoría de",
                "categoria",
                "categoria",
                "0..M",
                "0..1"
        );

        ValidationResult result = validator.validate(new DiagramModel(List.of(category), List.of(parentRelation)));

        assertTrue(result.isValid());
        assertTrue(result.warnings().stream().anyMatch(
                issue -> issue.code() == ValidationCode.RELATIONSHIP_SELF_REFERENCE
        ));
    }

    private static EntityElement strongEntity(String id, String name) {
        return EntityElement.strong(
                id,
                name,
                List.of(AttributeElement.withTags(id + "_id", "pk id", Set.of(AttributeTag.PRIMARY_KEY)))
        );
    }

    private static EntityElement weakEntity(String id, String name) {
        return new EntityElement(
                DiagramElementId.of(id),
                name,
                EntityKind.WEAK,
                "",
                "",
                List.of(AttributeElement.normal(id + "_secuencia", "secuencia"))
        );
    }
}
