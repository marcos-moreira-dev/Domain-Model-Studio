package com.marcosmoreira.domainmodelstudio.domain.er;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.er.Cardinality;
import org.junit.jupiter.api.Test;

class CardinalityTest {

    @Test
    void acceptsSupportedMvpExpressions() {
        Cardinality cardinality = Cardinality.of("0..M");

        assertEquals("0..M", cardinality.displayText());
        assertTrue(cardinality.isOptional());
        assertTrue(cardinality.isMany());
    }

    @Test
    void detectsSingleMandatoryCardinality() {
        Cardinality cardinality = Cardinality.of("1");

        assertFalse(cardinality.isOptional());
        assertFalse(cardinality.isMany());
    }

    @Test
    void acceptsBoundedNumericRange() {
        Cardinality cardinality = Cardinality.of("0..35");

        assertEquals("0..35", cardinality.displayText());
        assertTrue(cardinality.isOptional());
        assertTrue(cardinality.isMany());
    }

    @Test
    void rejectsUnsupportedExpression() {
        assertThrows(IllegalArgumentException.class, () -> Cardinality.of("0..X"));
    }
}
