package com.marcosmoreira.domainmodelstudio.presentation.dialogs;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ManualContentDidacticFiguresTest {

    @Test
    void manualSectionsShouldExposeDidacticFiguresFromTheoryResources() {
        long sectionsWithFigures = ManualContent.sections().stream()
                .filter(section -> section.blocks().stream().anyMatch(ManualBlock::hasFigures))
                .count();

        assertTrue(sectionsWithFigures >= 15, "La guía académica debe incluir figuras didácticas por tipo visible.");
    }
}
