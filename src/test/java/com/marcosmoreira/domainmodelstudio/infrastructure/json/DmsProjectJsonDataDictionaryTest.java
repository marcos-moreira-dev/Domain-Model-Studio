package com.marcosmoreira.domainmodelstudio.infrastructure.json;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.datadictionary.AddDataDictionaryEntityUseCase;
import com.marcosmoreira.domainmodelstudio.application.datadictionary.AddDataDictionaryFieldUseCase;
import com.marcosmoreira.domainmodelstudio.application.datadictionary.DefaultCreateDataDictionaryUseCase;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;

class DmsProjectJsonDataDictionaryTest {

    @Test
    void shouldPersistDataDictionaryInsideDmsProject() {
        var create = new DefaultCreateDataDictionaryUseCase(
                Clock.fixed(Instant.parse("2026-01-01T00:00:00Z"), ZoneOffset.UTC));
        var document = create.createBlank("Restaurante");
        document = new AddDataDictionaryEntityUseCase().add(document, "Cliente");
        document = new AddDataDictionaryFieldUseCase().add(document, document.entities().get(0).id(), "correo electrónico");
        var project = DiagramProject.blank("diccionario_restaurante", "Diccionario Restaurante", DiagramTypeId.DATA_DICTIONARY)
                .withDataDictionary(document);

        String json = new DmsProjectJsonWriter().write(project);
        DiagramProject reopened = new DmsProjectJsonReader().read(json);

        assertEquals(DiagramTypeId.DATA_DICTIONARY, reopened.metadata().diagramTypeId());
        assertTrue(reopened.dataDictionary().isPresent());
        assertEquals(1, reopened.dataDictionary().get().entityCount());
        assertEquals(1, reopened.dataDictionary().get().fieldCount());
    }
}
