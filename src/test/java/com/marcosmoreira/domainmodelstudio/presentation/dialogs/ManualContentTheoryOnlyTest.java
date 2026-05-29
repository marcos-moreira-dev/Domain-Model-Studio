package com.marcosmoreira.domainmodelstudio.presentation.dialogs;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ManualContentTheoryOnlyTest {

    @Test
    void manualShouldBehaveAsTheoryCenterNotFeatureChecklist() {
        String allText = ManualContent.sections().stream()
                .flatMap(section -> section.blocks().stream())
                .map(block -> block.title() + "\n" + String.join("\n", block.lines()))
                .reduce("", (left, right) -> left + "\n" + right);

        assertTrue(allText.contains("qué es") || allText.contains("Qué es"));
        assertTrue(allText.contains("Errores comunes"));
        assertFalse(allText.contains("Capacidades visibles"));
        assertFalse(allText.contains("Estado y materiales"));
        assertFalse(allText.contains("Plantilla IA"));
    }
}
