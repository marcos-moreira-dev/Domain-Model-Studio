package com.marcosmoreira.domainmodelstudio.infrastructure.markdown.logicalbusiness;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessDocument;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItem;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItemKind;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessPendingQuestion;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessSection;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

final class LogicalBusinessMarkdownDocumentWriter {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;

    private final LogicalBusinessMarkdownItemWriter itemWriter;
    private final LogicalBusinessMarkdownEntityWriter entityWriter;

    LogicalBusinessMarkdownDocumentWriter(
            LogicalBusinessMarkdownItemWriter itemWriter,
            LogicalBusinessMarkdownEntityWriter entityWriter
    ) {
        this.itemWriter = Objects.requireNonNull(itemWriter, "itemWriter");
        this.entityWriter = Objects.requireNonNull(entityWriter, "entityWriter");
    }

    String write(LogicalBusinessDocument document) {
        StringBuilder markdown = new StringBuilder();
        writeHeader(markdown, document);
        Set<String> writtenItemIds = new HashSet<>();
        writeCanonicalSections(markdown, document, writtenItemIds);
        writeCustomSections(markdown, document, writtenItemIds);
        writeOrphanItems(markdown, document, writtenItemIds);
        return markdown.toString().stripTrailing() + System.lineSeparator();
    }

    private void writeHeader(StringBuilder markdown, LogicalBusinessDocument document) {
        markdown.append("---").append(System.lineSeparator());
        markdown.append("diagram_type: \"logical-business-intake\"").append(System.lineSeparator());
        markdown.append("importable: true").append(System.lineSeparator());
        markdown.append("canonical_contract: \"").append(LogicalBusinessCanonicalMarkdownContract.CONTRACT_ID)
                .append("\"").append(System.lineSeparator());
        markdown.append("---").append(System.lineSeparator()).append(System.lineSeparator());
        markdown.append("# Levantamiento lógico de negocio").append(System.lineSeparator()).append(System.lineSeparator());
        markdown.append("> Documento exportado desde Domain Model Studio. El Markdown canónico es la fuente de verdad.")
                .append(System.lineSeparator()).append(System.lineSeparator());
        markdown.append("## 0. Portada lógica del levantamiento")
                .append(System.lineSeparator()).append(System.lineSeparator());
        markdown.append("- **Proyecto / sistema:** `").append(escapeInline(document.projectName())).append('`')
                .append(System.lineSeparator());
        markdown.append("- **Versión del levantamiento:** `").append(escapeInline(document.version())).append('`')
                .append(System.lineSeparator());
        markdown.append("- **Fecha:** `").append(DATE_FORMAT.format(document.documentDate())).append('`')
                .append(System.lineSeparator());
        markdown.append("- **Estado del documento:** `").append(document.documentStatus().displayName()).append('`')
                .append(System.lineSeparator());
        markdown.append("- **Fuente principal:** `").append(escapeInline(document.mainSource())).append('`')
                .append(System.lineSeparator()).append(System.lineSeparator());
    }

    private void writeCanonicalSections(
            StringBuilder markdown,
            LogicalBusinessDocument document,
            Set<String> writtenItemIds
    ) {
        for (LogicalBusinessCanonicalMarkdownContract.CanonicalSection canonical
                : LogicalBusinessCanonicalMarkdownContract.sections()) {
            if (canonical.number() == 0) {
                continue;
            }
            writeCanonicalSection(markdown, document, writtenItemIds, canonical);
        }
    }

    private void writeCanonicalSection(
            StringBuilder markdown,
            LogicalBusinessDocument document,
            Set<String> writtenItemIds,
            LogicalBusinessCanonicalMarkdownContract.CanonicalSection canonical
    ) {
        markdown.append("## ").append(canonical.heading()).append(System.lineSeparator()).append(System.lineSeparator());
        findSection(document, canonical).ifPresent(section -> writeSectionIntro(markdown, section));
        if (canonical.number() == 14) {
            entityWriter.writeEntityCatalogBody(markdown, document, writtenItemIds);
        } else if (canonical.number() == 20) {
            writePendingQuestions(markdown, document, writtenItemIds);
        } else if (canonical.number() == 21) {
            writeMaturity(markdown, document);
        }
        writeSectionItems(markdown, document, writtenItemIds, findSection(document, canonical).orElse(null));
        markdown.append(System.lineSeparator());
    }

    private Optional<LogicalBusinessSection> findSection(
            LogicalBusinessDocument document,
            LogicalBusinessCanonicalMarkdownContract.CanonicalSection canonical
    ) {
        return document.sections().stream().filter(section -> section.id().equals(canonical.id())).findFirst();
    }

    private void writeSectionIntro(StringBuilder markdown, LogicalBusinessSection section) {
        if (!section.purpose().isBlank()) {
            markdown.append(section.purpose()).append(System.lineSeparator()).append(System.lineSeparator());
        }
        if (!section.notes().isBlank()) {
            markdown.append(section.notes()).append(System.lineSeparator()).append(System.lineSeparator());
        }
    }

    private void writeSectionItems(
            StringBuilder markdown,
            LogicalBusinessDocument document,
            Set<String> writtenItemIds,
            LogicalBusinessSection section
    ) {
        if (section == null) {
            return;
        }
        for (LogicalBusinessItem item : itemsFor(section, document)) {
            itemWriter.writeItem(markdown, item);
            writtenItemIds.add(item.id());
        }
    }

    private List<LogicalBusinessItem> itemsFor(LogicalBusinessSection section, LogicalBusinessDocument document) {
        return section.itemIds().stream()
                .map(document::itemById)
                .flatMap(Optional::stream)
                .filter(item -> !entityWriter.isEntityCatalogKind(item.kind()))
                .filter(item -> item.kind() != LogicalBusinessItemKind.PENDING_QUESTION)
                .toList();
    }

    private void writeCustomSections(
            StringBuilder markdown,
            LogicalBusinessDocument document,
            Set<String> writtenItemIds
    ) {
        for (LogicalBusinessSection section : document.sections()) {
            if (LogicalBusinessCanonicalMarkdownContract.sections().stream()
                    .anyMatch(canonical -> canonical.id().equals(section.id()))) {
                continue;
            }
            markdown.append("## ").append(section.title()).append(System.lineSeparator()).append(System.lineSeparator());
            writeSectionIntro(markdown, section);
            writeSectionItems(markdown, document, writtenItemIds, section);
        }
    }

    private void writeOrphanItems(
            StringBuilder markdown,
            LogicalBusinessDocument document,
            Set<String> writtenItemIds
    ) {
        List<LogicalBusinessItem> orphanItems = document.items().stream()
                .filter(item -> !writtenItemIds.contains(item.id()))
                .filter(item -> !entityWriter.isEntityCatalogKind(item.kind()))
                .filter(item -> item.kind() != LogicalBusinessItemKind.PENDING_QUESTION)
                .toList();
        if (orphanItems.isEmpty()) {
            return;
        }
        markdown.append("## Elementos lógicos no agrupados").append(System.lineSeparator()).append(System.lineSeparator());
        for (LogicalBusinessItem item : orphanItems) {
            itemWriter.writeItem(markdown, item);
            writtenItemIds.add(item.id());
        }
    }

    private void writePendingQuestions(
            StringBuilder markdown,
            LogicalBusinessDocument document,
            Set<String> writtenItemIds
    ) {
        for (LogicalBusinessPendingQuestion question : document.pendingQuestions()) {
            itemWriter.writePendingQuestion(markdown, question);
            writtenItemIds.add(question.id());
        }
        for (LogicalBusinessItem item : document.itemsByKind(LogicalBusinessItemKind.PENDING_QUESTION)) {
            if (!writtenItemIds.contains(item.id())) {
                itemWriter.writeItem(markdown, item);
                writtenItemIds.add(item.id());
            }
        }
    }

    private void writeMaturity(StringBuilder markdown, LogicalBusinessDocument document) {
        markdown.append("- **Nivel:** `").append(document.maturity().level().name().toLowerCase()).append('`')
                .append(System.lineSeparator());
        itemWriter.writeListField(markdown, "Fortalezas", document.maturity().strengths());
        itemWriter.writeListField(markdown, "Bloqueadores", document.maturity().blockers());
        itemWriter.writeListField(markdown, "Siguientes pasos", document.maturity().nextSteps());
    }

    private static String escapeInline(String value) {
        return value == null ? "" : value.replace("`", "'").strip();
    }
}
