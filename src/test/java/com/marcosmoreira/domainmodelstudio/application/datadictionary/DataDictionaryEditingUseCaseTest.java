package com.marcosmoreira.domainmodelstudio.application.datadictionary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.datadictionary.FieldConstraint;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;

class DataDictionaryEditingUseCaseTest {

    @Test
    void shouldAddEntityAndFieldAndValidateWarnings() {
        var create = new DefaultCreateDataDictionaryUseCase(
                Clock.fixed(Instant.parse("2026-01-01T00:00:00Z"), ZoneOffset.UTC));
        var addEntity = new AddDataDictionaryEntityUseCase();
        var addField = new AddDataDictionaryFieldUseCase();
        var validate = new ValidateDataDictionaryUseCase();

        var document = create.createBlank("Restaurante");
        document = addEntity.add(document, "Cliente");
        document = addField.add(document, document.entities().get(0).id(), "correo electrónico");

        assertEquals(1, document.entityCount());
        assertEquals(1, document.fieldCount());
        assertTrue(document.entities().get(0).fields().get(0).hasConstraint(FieldConstraint.VISIBLE_IN_FORM));
        assertFalse(validate.validate(document).ok());
    }
}
