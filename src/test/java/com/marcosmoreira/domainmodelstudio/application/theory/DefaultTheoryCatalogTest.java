package com.marcosmoreira.domainmodelstudio.application.theory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.catalog.DefaultDiagramTypeRegistry;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeDescriptor;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.List;
import org.junit.jupiter.api.Test;

class DefaultTheoryCatalogTest {

    @Test
    void shouldExposeTheoryForRegisteredDiagramTypes() {
        DefaultTheoryCatalog catalog = new DefaultTheoryCatalog();

        for (DiagramTypeDescriptor type : new DefaultDiagramTypeRegistry().findAll()) {
            assertTrue(
                    catalog.findByDiagramType(type.id()).isPresent(),
                    "Falta teoría para " + type.id().value());
        }
    }

    @Test
    void c4ContextAndContainersShouldShareTheoryTopic() {
        DefaultTheoryCatalog catalog = new DefaultTheoryCatalog();

        TheoryTopic context = catalog.findByDiagramType(DiagramTypeId.C4_CONTEXT).orElseThrow();
        TheoryTopic containers = catalog.findByDiagramType(DiagramTypeId.C4_CONTAINERS).orElseThrow();

        assertEquals(context.id(), containers.id());
        assertEquals(TheoryTopicId.C4_CONTEXT_CONTAINERS, context.id());
    }


    @Test
    void shouldExposeLogicalBusinessIntakeAcademicTheory() {
        DefaultTheoryCatalog catalog = new DefaultTheoryCatalog();

        TheoryTopic topic = catalog.findById(TheoryTopicId.LOGICAL_BUSINESS_INTAKE).orElseThrow();

        assertEquals(DiagramTypeId.LOGICAL_BUSINESS_INTAKE, topic.diagramTypeId());
        assertTrue(topic.title().contains("Levantamiento lógico"));
        assertTrue(topic.sections().stream().anyMatch(section -> section.title().equals("Fundamento algorítmico")));
    }

    @Test
    void shouldRejectDuplicateTheoryTopicIds() {
        TheoryTopic topic = new TheoryTopic(
                TheoryTopicId.CONCEPTUAL_MODEL,
                DiagramTypeId.CONCEPTUAL_MODEL,
                "Modelo conceptual",
                List.of(new TheorySection("Propósito", List.of("Línea de prueba."))));

        assertThrows(IllegalArgumentException.class, () -> new DefaultTheoryCatalog(List.of(topic, topic)));
    }

    @Test
    void unknownTheoryShouldReturnEmptyOptional() {
        DefaultTheoryCatalog catalog = new DefaultTheoryCatalog();

        assertFalse(catalog.findById(TheoryTopicId.of("desconocido")).isPresent());
        assertFalse(catalog.findByDiagramType(DiagramTypeId.of("desconocido")).isPresent());
    }
}
