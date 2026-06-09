package com.marcosmoreira.domainmodelstudio.presentation.dialogs;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ManualContentTest {

    @Test
    void shouldExposeCategorizedManualContent() {
        var categories = ManualContent.categories();

        assertFalse(categories.isEmpty());
        assertTrue(categories.stream().anyMatch(category -> category.title().equals("Modelado de datos")));
        assertTrue(categories.stream()
                .flatMap(category -> category.sections().stream())
                .anyMatch(section -> section.title().equals("Modelo conceptual")));
        assertTrue(categories.stream()
                .flatMap(category -> category.sections().stream())
                .anyMatch(section -> section.title().equals("Diccionario de datos")));
        assertTrue(categories.stream()
                .flatMap(category -> category.sections().stream())
                .anyMatch(section -> section.title().equals("Levantamiento lógico")));
    }

    @Test
    void shouldKeepFlattenedSectionsForLegacyUse() {
        assertFalse(ManualContent.sections().isEmpty());
    }
}
