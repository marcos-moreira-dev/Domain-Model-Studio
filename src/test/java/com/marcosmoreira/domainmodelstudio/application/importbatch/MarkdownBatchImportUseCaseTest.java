package com.marcosmoreira.domainmodelstudio.application.importbatch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.catalog.DefaultDiagramTypeRegistry;
import com.marcosmoreira.domainmodelstudio.application.importmodel.ImportMarkdownModelUseCase;
import com.marcosmoreira.domainmodelstudio.application.importmodel.MarkdownModelParser;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.validation.DiagramProjectValidator;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class MarkdownBatchImportUseCaseTest {

    @Test
    void importsValidProjectsAndIgnoresNonProjectFiles() throws Exception {
        MarkdownBatchImportUseCase useCase = new MarkdownBatchImportUseCase(
                (root, policy) -> List.of(
                        skipped(root.resolve("README.md"), MarkdownImportCandidateKind.SKIPPED_README),
                        project(root.resolve("01_levantamiento-logico.md"), "logical-business-intake"),
                        project(root.resolve("99_desconocido.md"), "unknown-kind")),
                new ImportMarkdownModelUseCase(new FakeParser(), new DiagramProjectValidator()),
                new DefaultDiagramTypeRegistry());

        MarkdownBatchImportResult result = useCase.importFolder(new MarkdownBatchImportRequest(
                Path.of("/tmp/caso"), MarkdownBatchImportPolicy.defaultPolicy()));

        assertEquals(3, result.scannedFiles());
        assertEquals(1, result.importedCount());
        assertEquals(1, result.skippedCount());
        assertEquals(1, result.rejectedCount());
        assertTrue(result.items().stream().anyMatch(item -> item.status() == MarkdownBatchImportItemStatus.SKIPPED_README));
        assertTrue(result.items().stream().anyMatch(item -> item.status() == MarkdownBatchImportItemStatus.REJECTED_UNKNOWN_DIAGRAM_TYPE));
    }

    @Test
    void suffixMismatchIsWarningInDefaultPolicy() throws Exception {
        MarkdownBatchImportUseCase useCase = new MarkdownBatchImportUseCase(
                (root, policy) -> List.of(new MarkdownImportCandidate(
                        root.resolve("04_uml-clases.md"),
                        "04_uml-clases.md",
                        MarkdownImportCandidateKind.PROJECT_CANDIDATE,
                        Optional.of("data-dictionary"),
                        Optional.of(true),
                        Optional.of("project"),
                        Optional.of(DiagramTypeId.UML_CLASS),
                        List.of())),
                new ImportMarkdownModelUseCase(new FakeParser(), new DiagramProjectValidator()),
                new DefaultDiagramTypeRegistry());

        MarkdownBatchImportResult result = useCase.importFolder(new MarkdownBatchImportRequest(
                Path.of("/tmp/caso"), MarkdownBatchImportPolicy.defaultPolicy()));

        assertEquals(1, result.importedCount());
        assertTrue(result.items().get(0).warnings().stream().anyMatch(warning -> warning.contains("sugiere uml-class")));
    }

    private static MarkdownImportCandidate project(Path path, String diagramType) {
        return new MarkdownImportCandidate(
                path,
                path.getFileName().toString(),
                MarkdownImportCandidateKind.PROJECT_CANDIDATE,
                Optional.of(diagramType),
                Optional.of(true),
                Optional.of("project"),
                Optional.empty(),
                List.of());
    }

    private static MarkdownImportCandidate skipped(Path path, MarkdownImportCandidateKind kind) {
        return new MarkdownImportCandidate(
                path,
                path.getFileName().toString(),
                kind,
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                List.of());
    }

    private static final class FakeParser implements MarkdownModelParser {
        @Override
        public DiagramProject parse(Path markdownFile) {
            return DiagramProject.blank("importado", "Importado", DiagramTypeId.CONCEPTUAL_MODEL)
                    .withMetadata(DiagramProject.blank("importado", "Importado", DiagramTypeId.CONCEPTUAL_MODEL)
                            .metadata().withSourceMarkdownPath(markdownFile.toString()));
        }

        @Override
        public DiagramProject parse(String markdownContent, String sourceName) {
            return DiagramProject.blank("importado", "Importado", DiagramTypeId.CONCEPTUAL_MODEL);
        }
    }
}
