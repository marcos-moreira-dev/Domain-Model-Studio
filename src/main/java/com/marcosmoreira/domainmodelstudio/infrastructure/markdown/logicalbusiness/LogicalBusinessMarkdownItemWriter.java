package com.marcosmoreira.domainmodelstudio.infrastructure.markdown.logicalbusiness;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItem;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessPendingQuestion;
import java.util.List;

final class LogicalBusinessMarkdownItemWriter {

    void writeItem(StringBuilder markdown, LogicalBusinessItem item) {
        markdown.append("### ").append(item.id()).append(" — ").append(item.title())
                .append(System.lineSeparator());
        writeScalarField(markdown, "Estado", item.status().name().toLowerCase());
        writeScalarField(markdown, "Fuente", item.source());
        writeScalarField(markdown, "Descripción humana", item.description());
        writeScalarField(markdown, "Lectura", item.humanReading());
        writeListField(markdown, "Relacionada con", item.referenceIds());
        writeOriginalContent(markdown, item.content());
        markdown.append(System.lineSeparator());
    }

    void writePendingQuestion(StringBuilder markdown, LogicalBusinessPendingQuestion question) {
        markdown.append("### ").append(question.id()).append(" — ").append(question.question())
                .append(System.lineSeparator());
        writeScalarField(markdown, "Afecta a", question.affects());
        writeScalarField(markdown, "Prioridad", question.priority().name().toLowerCase());
        writeScalarField(markdown, "Estado", question.status().name().toLowerCase());
        markdown.append(System.lineSeparator());
    }

    void writeScalarField(StringBuilder markdown, String label, String value) {
        String safeValue = clean(value);
        if (!safeValue.isBlank()) {
            markdown.append("- ").append(label).append(": ").append(safeValue).append(System.lineSeparator());
        }
    }

    void writeListField(StringBuilder markdown, String label, List<String> values) {
        List<String> cleanValues = values == null ? List.of() : values.stream()
                .map(this::clean)
                .filter(value -> !value.isBlank())
                .toList();
        if (cleanValues.isEmpty()) {
            return;
        }
        markdown.append("- ").append(label).append(":").append(System.lineSeparator());
        for (String value : cleanValues) {
            markdown.append("  - ").append(value).append(System.lineSeparator());
        }
    }

    private void writeOriginalContent(StringBuilder markdown, String content) {
        String safeContent = clean(content);
        if (safeContent.isBlank()) {
            return;
        }
        markdown.append(System.lineSeparator());
        markdown.append("#### Contenido original").append(System.lineSeparator()).append(System.lineSeparator());
        markdown.append(safeContent).append(System.lineSeparator());
    }

    private String clean(String value) {
        return value == null ? "" : value.strip();
    }
}
