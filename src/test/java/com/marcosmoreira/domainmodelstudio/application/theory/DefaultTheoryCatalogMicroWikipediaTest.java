package com.marcosmoreira.domainmodelstudio.application.theory;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.catalog.DefaultDiagramTypeRegistry;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeDescriptor;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class DefaultTheoryCatalogMicroWikipediaTest {

    private static final Set<String> REQUIRED_SECTIONS = Set.of(
            "Qué es",
            "Para qué sirve",
            "Elementos principales",
            "Relaciones y lectura",
            "Casos especiales",
            "Cuándo usarlo",
            "Cuándo no usarlo",
            "Errores comunes");

    @Test
    void eachRegisteredDiagramTypeShouldHaveMicroWikipediaDepth() {
        DefaultTheoryCatalog catalog = new DefaultTheoryCatalog();

        for (DiagramTypeDescriptor type : new DefaultDiagramTypeRegistry().findAll()) {
            TheoryTopic topic = catalog.findByDiagramType(type.id()).orElseThrow();
            Set<String> sectionTitles = topic.sections().stream()
                    .map(TheorySection::title)
                    .collect(Collectors.toSet());

            assertTrue(sectionTitles.containsAll(REQUIRED_SECTIONS),
                    "El tema académico de " + type.displayName() + " no cumple estructura de guía académica.");
            assertTrue(topic.sections().stream().mapToInt(section -> section.lines().size()).sum() >= 20,
                    "El tema académico de " + type.displayName() + " necesita mayor desarrollo académico.");
        }
    }
}
