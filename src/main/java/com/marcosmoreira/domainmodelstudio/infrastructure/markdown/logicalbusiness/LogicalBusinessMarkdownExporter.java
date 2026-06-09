package com.marcosmoreira.domainmodelstudio.infrastructure.markdown.logicalbusiness;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessDocument;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/** Exporta el Levantamiento lógico a Markdown canónico y reimportable. */
public final class LogicalBusinessMarkdownExporter {

    private final LogicalBusinessMarkdownDocumentWriter documentWriter;

    public LogicalBusinessMarkdownExporter() {
        this(new LogicalBusinessMarkdownDocumentWriter(
                new LogicalBusinessMarkdownItemWriter(),
                new LogicalBusinessMarkdownEntityWriter()));
    }

    LogicalBusinessMarkdownExporter(LogicalBusinessMarkdownDocumentWriter documentWriter) {
        this.documentWriter = Objects.requireNonNull(documentWriter, "documentWriter");
    }

    public String export(LogicalBusinessDocument document) {
        Objects.requireNonNull(document, "document");
        return documentWriter.write(document);
    }

    public void export(LogicalBusinessDocument document, Path targetFile) throws IOException {
        Objects.requireNonNull(targetFile, "targetFile");
        Files.writeString(targetFile, export(document), StandardCharsets.UTF_8);
    }
}
