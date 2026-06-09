package com.marcosmoreira.domainmodelstudio.domain.er;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.er.AttributeElement;
import com.marcosmoreira.domainmodelstudio.domain.er.AttributeTag;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AttributeElementTest {

    @Test
    void exposesChenRelatedAttributeTags() {
        AttributeElement attribute = AttributeElement.withTags(
                "edad",
                "edad",
                Set.of(AttributeTag.DERIVED, AttributeTag.OPTIONAL)
        );

        assertTrue(attribute.isDerived());
        assertTrue(attribute.isOptional());
        assertFalse(attribute.isPrimaryKey());
    }

    @Test
    void rejectsBlankAttributeName() {
        assertThrows(IllegalArgumentException.class, () -> AttributeElement.normal("nombre", " "));
    }
}
