package com.marcosmoreira.domainmodelstudio.domain.diagram;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import org.junit.jupiter.api.Test;

class DiagramElementIdTest {

    @Test
    void trimsValidIdentifier() {
        DiagramElementId id = DiagramElementId.of(" producto ");

        assertEquals("producto", id.value());
    }

    @Test
    void rejectsBlankIdentifier() {
        assertThrows(IllegalArgumentException.class, () -> DiagramElementId.of("   "));
    }

    @Test
    void rejectsIdentifierWithWhitespace() {
        assertThrows(IllegalArgumentException.class, () -> DiagramElementId.of("producto principal"));
    }
}
