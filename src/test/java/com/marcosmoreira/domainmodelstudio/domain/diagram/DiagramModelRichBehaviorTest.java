package com.marcosmoreira.domainmodelstudio.domain.diagram;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.er.AttributeElement;
import com.marcosmoreira.domainmodelstudio.domain.er.AttributeTag;
import com.marcosmoreira.domainmodelstudio.domain.er.Cardinality;
import com.marcosmoreira.domainmodelstudio.domain.er.EntityElement;
import com.marcosmoreira.domainmodelstudio.domain.er.EntityKind;
import com.marcosmoreira.domainmodelstudio.domain.er.ParticipationType;
import com.marcosmoreira.domainmodelstudio.domain.er.RelationshipElement;
import com.marcosmoreira.domainmodelstudio.domain.er.RelationshipKind;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DiagramModelRichBehaviorTest {

    @Test
    void preservesRichChenSemanticsWithoutJavaFx() {
        EntityElement invoice = new EntityElement(
                DiagramElementId.of("factura"),
                "Factura",
                EntityKind.STRONG,
                "ventas",
                "Documento de venta.",
                List.of(
                        AttributeElement.withTags("factura_id", "pk id", Set.of(AttributeTag.PRIMARY_KEY)),
                        AttributeElement.withTags("factura_total", "total", Set.of(AttributeTag.DERIVED)),
                        AttributeElement.withTags("factura_observacion", "observación", Set.of(AttributeTag.OPTIONAL))
                )
        );
        EntityElement invoiceLine = new EntityElement(
                DiagramElementId.of("detalle_factura"),
                "Detalle factura",
                EntityKind.WEAK,
                "ventas",
                "Entidad débil dependiente de Factura.",
                List.of(
                        AttributeElement.withTags("detalle_factura_numero_linea", "número línea", Set.of(AttributeTag.PRIMARY_KEY)),
                        AttributeElement.withTags("detalle_factura_notas", "notas", Set.of(AttributeTag.MULTIVALUED, AttributeTag.OPTIONAL))
                )
        );
        RelationshipElement contains = new RelationshipElement(
                DiagramElementId.of("contiene"),
                "Contiene",
                invoice.id(),
                invoiceLine.id(),
                Cardinality.of("1"),
                Cardinality.of("1..M"),
                RelationshipKind.IDENTIFYING,
                ParticipationType.PARTIAL,
                ParticipationType.TOTAL,
                "Una factura contiene uno o más detalles."
        );

        DiagramModel model = new DiagramModel(List.of(invoice, invoiceLine), List.of(contains));

        assertEquals(2, model.entityCount());
        assertEquals(1, model.relationshipCount());
        assertEquals(EntityKind.WEAK, model.entityById(DiagramElementId.of("detalle_factura")).orElseThrow().kind());
        assertEquals(RelationshipKind.IDENTIFYING, model.relationshipById(DiagramElementId.of("contiene")).orElseThrow().kind());
        assertEquals(ParticipationType.TOTAL, model.relationshipById(DiagramElementId.of("contiene")).orElseThrow().toParticipation());
        assertTrue(invoice.attributes().get(1).isDerived());
        assertTrue(invoiceLine.attributes().get(1).isMultivalued());
    }

    @Test
    void renamesEntityRelationshipAndAttributeByStableId() {
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
        RelationshipElement belongsTo = RelationshipElement.between("pertenece", "Pertenece", "producto", "categoria", "0..M", "1");
        DiagramModel model = new DiagramModel(List.of(product, category), List.of(belongsTo));

        DiagramModel renamedEntity = model.withRenamedElement(DiagramElementId.of("producto"), "Producto comercial");
        DiagramModel renamedAttribute = renamedEntity.withRenamedElement(DiagramElementId.of("producto_id"), "identificador");
        DiagramModel renamedRelationship = renamedAttribute.withRenamedElement(DiagramElementId.of("pertenece"), "Clasifica");

        assertEquals("Producto comercial", renamedRelationship.entityById(DiagramElementId.of("producto")).orElseThrow().name());
        assertEquals("identificador", renamedRelationship.entityById(DiagramElementId.of("producto")).orElseThrow().attributeById(DiagramElementId.of("producto_id")).orElseThrow().name());
        assertEquals("Clasifica", renamedRelationship.relationshipById(DiagramElementId.of("pertenece")).orElseThrow().name());
        assertTrue(renamedRelationship.containsEntity(DiagramElementId.of("producto")));
        assertFalse(renamedRelationship.containsEntity(DiagramElementId.of("producto_comercial")));
    }
}
