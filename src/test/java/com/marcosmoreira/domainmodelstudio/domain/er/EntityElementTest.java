package com.marcosmoreira.domainmodelstudio.domain.er;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.er.AttributeElement;
import com.marcosmoreira.domainmodelstudio.domain.er.AttributeTag;
import com.marcosmoreira.domainmodelstudio.domain.er.EntityElement;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class EntityElementTest {

    @Test
    void createsEntityWithAttributesInStableOrder() {
        AttributeElement id = AttributeElement.withTags("producto_id", "pk id", Set.of(AttributeTag.PRIMARY_KEY));
        AttributeElement name = AttributeElement.normal("nombre", "nombre");

        EntityElement product = EntityElement.strong("producto", "Producto", List.of(id, name));

        assertEquals(2, product.attributes().size());
        assertEquals("producto_id", product.attributes().getFirst().id().value());
        assertTrue(product.hasPrimaryKey());
    }

    @Test
    void canFindAttributeById() {
        EntityElement product = EntityElement.strong(
                "producto",
                "Producto",
                List.of(AttributeElement.normal("nombre", "nombre"))
        );

        assertTrue(product.attributeById(DiagramElementId.of("nombre")).isPresent());
    }
}
