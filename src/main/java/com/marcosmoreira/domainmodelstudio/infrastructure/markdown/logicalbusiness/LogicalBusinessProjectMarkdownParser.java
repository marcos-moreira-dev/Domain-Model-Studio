package com.marcosmoreira.domainmodelstudio.infrastructure.markdown.logicalbusiness;

import com.marcosmoreira.domainmodelstudio.application.importmodel.MarkdownModelParser;
import com.marcosmoreira.domainmodelstudio.application.importmodel.MarkdownModelParsingException;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessDocument;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Objects;

/** Adapta el parser puro del levantamiento lógico al contrato general de importación Markdown. */
public final class LogicalBusinessProjectMarkdownParser implements MarkdownModelParser {

    private final LogicalBusinessMarkdownParser documentParser;

    public LogicalBusinessProjectMarkdownParser() {
        this(new LogicalBusinessMarkdownParser());
    }

    LogicalBusinessProjectMarkdownParser(LogicalBusinessMarkdownParser documentParser) {
        this.documentParser = Objects.requireNonNull(documentParser, "documentParser");
    }

    @Override
    public DiagramProject parse(Path markdownFile) throws IOException, MarkdownModelParsingException {
        Objects.requireNonNull(markdownFile, "markdownFile");
        String content = Files.readString(markdownFile, StandardCharsets.UTF_8);
        return parse(content, markdownFile.toString());
    }

    @Override
    public DiagramProject parse(String markdownContent, String sourceName) throws MarkdownModelParsingException {
        try {
            LogicalBusinessDocument document = documentParser.parse(markdownContent);
            DiagramProject project = DiagramProject.blank(projectId(document, sourceName), document.projectName(), DiagramTypeId.LOGICAL_BUSINESS_INTAKE)
                    .withLogicalBusinessDocument(document);
            if (sourceName != null && !sourceName.isBlank()) {
                return project.withMetadata(project.metadata().withSourceMarkdownPath(sourceName));
            }
            return project;
        } catch (RuntimeException exception) {
            throw new MarkdownModelParsingException("No se pudo importar el levantamiento lógico: " + exception.getMessage(), exception);
        }
    }

    private String projectId(LogicalBusinessDocument document, String sourceName) {
        String base = document.projectName().isBlank() ? sourceName : document.projectName();
        String normalized = (base == null || base.isBlank() ? "levantamiento-logico" : base)
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9áéíóúñ]+", "-")
                .replaceAll("^-+|-+$", "");
        if (normalized.isBlank()) {
            return "levantamiento-logico";
        }
        return normalized.length() <= 72 ? normalized : normalized.substring(0, 72).replaceAll("-+$", "");
    }
}
