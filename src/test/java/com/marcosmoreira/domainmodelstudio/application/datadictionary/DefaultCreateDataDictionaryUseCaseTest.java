package com.marcosmoreira.domainmodelstudio.application.datadictionary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;

class DefaultCreateDataDictionaryUseCaseTest {

    @Test
    void createsBlankDictionaryWithDeterministicDate() {
        Clock clock = Clock.fixed(Instant.parse("2026-05-11T00:00:00Z"), ZoneOffset.UTC);
        CreateDataDictionaryUseCase useCase = new DefaultCreateDataDictionaryUseCase(clock);

        var document = useCase.createBlank("Sistema administrativo");

        assertEquals("Sistema administrativo", document.projectName());
        assertEquals(LocalDate.of(2026, 5, 11), document.documentDate());
        assertTrue(document.entities().isEmpty());
    }
}
