package com.marcosmoreira.domainmodelstudio.presentation.dialogs;

import com.marcosmoreira.domainmodelstudio.application.theory.TheorySection;
import com.marcosmoreira.domainmodelstudio.application.theory.TheoryTopic;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Convierte temas académicos cargados desde recursos Markdown en secciones visibles del centro de ayuda. */
final class ManualTheorySectionFactory {

    private ManualTheorySectionFactory() {
    }

    static ManualSection fromTheoryTopic(String title, String summary, TheoryTopic topic) {
        Objects.requireNonNull(title, "title");
        Objects.requireNonNull(summary, "summary");
        Objects.requireNonNull(topic, "topic");

        List<ManualBlock> blocks = new ArrayList<>();
        for (TheorySection section : topic.sections()) {
            blocks.add(new ManualBlock(section.title(), section.lines(), List.of(), section.figures()));
        }
        return new ManualSection(title, summary, blocks);
    }
}
