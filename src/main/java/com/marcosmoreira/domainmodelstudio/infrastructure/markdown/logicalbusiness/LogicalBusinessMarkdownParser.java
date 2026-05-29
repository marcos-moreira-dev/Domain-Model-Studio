package com.marcosmoreira.domainmodelstudio.infrastructure.markdown.logicalbusiness;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessDocument;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItem;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessMaturity;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessPendingQuestion;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessSection;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

/** Importa la plantilla canónica de Levantamiento lógico hacia dominio puro. */
public final class LogicalBusinessMarkdownParser {

    private final LogicalBusinessMarkdownScanner scanner;
    private final LogicalBusinessMarkdownItemFactory itemFactory;
    private final LogicalBusinessEntityFactory entityFactory;

    public LogicalBusinessMarkdownParser() {
        this(new LogicalBusinessMarkdownScanner(), new LogicalBusinessMarkdownItemFactory(),
                new LogicalBusinessEntityFactory());
    }

    LogicalBusinessMarkdownParser(
            LogicalBusinessMarkdownScanner scanner,
            LogicalBusinessMarkdownItemFactory itemFactory,
            LogicalBusinessEntityFactory entityFactory
    ) {
        this.scanner = Objects.requireNonNull(scanner, "scanner");
        this.itemFactory = Objects.requireNonNull(itemFactory, "itemFactory");
        this.entityFactory = Objects.requireNonNull(entityFactory, "entityFactory");
    }

    public LogicalBusinessDocument parse(Path markdownFile) throws IOException {
        Objects.requireNonNull(markdownFile, "markdownFile");
        return parse(Files.readString(markdownFile, StandardCharsets.UTF_8));
    }

    public LogicalBusinessDocument parse(String markdownContent) {
        Objects.requireNonNull(markdownContent, "markdownContent");
        ParsedLogicalBusinessMarkdown parsed = scanner.scan(markdownContent);
        List<LogicalBusinessItem> items = itemFactory.items(parsed.items());
        List<LogicalBusinessPendingQuestion> questions = itemFactory.pendingQuestions(parsed.items());
        List<LogicalBusinessSection> sections = sections(parsed.sections());
        return new LogicalBusinessDocument(
                parsed.metadata().effectiveProjectName(),
                parsed.metadata().version(),
                parsed.metadata().documentDate(),
                parsed.metadata().status(),
                parsed.metadata().mainSource(),
                sections,
                items,
                entityFactory.entities(parsed.items()),
                questions,
                LogicalBusinessMaturity.initial(),
                parsed.notes());
    }

    private List<LogicalBusinessSection> sections(List<ParsedLogicalBusinessSection> parsedSections) {
        return parsedSections.stream()
                .map(section -> new LogicalBusinessSection(section.id(), section.title(), section.purpose(),
                        null, section.itemIds(), ""))
                .toList();
    }
}
