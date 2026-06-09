package com.marcosmoreira.domainmodelstudio.presentation.shell.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.importbatch.MarkdownBatchImportItemResult;
import com.marcosmoreira.domainmodelstudio.application.importbatch.MarkdownBatchImportItemStatus;
import com.marcosmoreira.domainmodelstudio.application.importbatch.MarkdownBatchImportResult;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class MarkdownFolderImportReportFormatterTest {

    @Test
    void detailedReportShouldExposeNonCatalogedMarkdownForIaCorrection() {
        Path root = Path.of("/tmp/ferreteria");
        MarkdownBatchImportResult result = new MarkdownBatchImportResult(
                root,
                3,
                2,
                1,
                1,
                1,
                List.of(
                        item(root.resolve("ventas/facturacion.md"), MarkdownBatchImportItemStatus.IMPORTED,
                                Optional.of(DiagramTypeId.of("screen-flow")), Optional.empty()),
                        item(root.resolve("stock/notas.md"), MarkdownBatchImportItemStatus.SKIPPED_MISSING_FRONTMATTER,
                                Optional.empty(), Optional.empty()),
                        item(root.resolve("uml/relaciones.md"), MarkdownBatchImportItemStatus.REJECTED_PARSE_ERROR,
                                Optional.of(DiagramTypeId.of("uml-class")), Optional.of("Relación sin clase origen")),
                        item(root.resolve("imagen.png"), MarkdownBatchImportItemStatus.SKIPPED_NOT_MARKDOWN,
                                Optional.empty(), Optional.empty())),
                List.of());

        MarkdownFolderImportReportFormatter formatter = new MarkdownFolderImportReportFormatter();
        String report = formatter.buildDetailedReport(result);
        String correctionReport = formatter.buildCorrectionReport(result);

        assertTrue(report.contains("Archivos Markdown no catalogados"));
        assertTrue(correctionReport.contains("Markdown problemáticos para corregir"));
        assertTrue(correctionReport.contains("stock/notas.md"));
        assertTrue(correctionReport.contains("uml/relaciones.md"));
        assertTrue(correctionReport.contains("Relación sin clase origen"));
        assertFalse(correctionReport.contains("facturacion.md"));
        assertFalse(correctionReport.contains("imagen.png"));
        assertTrue(report.contains("Archivos no Markdown omitidos"));
        assertFalse(report.contains("fuente madre"));
    }

    private static MarkdownBatchImportItemResult item(
            Path file,
            MarkdownBatchImportItemStatus status,
            Optional<DiagramTypeId> declaredType,
            Optional<String> error
    ) {
        return new MarkdownBatchImportItemResult(
                file,
                file.getFileName().toString(),
                declaredType,
                Optional.empty(),
                status,
                Optional.empty(),
                List.of(),
                error);
    }
}
