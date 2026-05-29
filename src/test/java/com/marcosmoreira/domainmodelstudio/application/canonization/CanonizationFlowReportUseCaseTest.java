package com.marcosmoreira.domainmodelstudio.application.canonization;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

class CanonizationFlowReportUseCaseTest {

    private final CanonizationFlowReportUseCase useCase = new CanonizationFlowReportUseCase();

    @Test
    void shouldReportReadyFlowWhenFolderHasSingleSourceMotherAndDerivedArtifacts() {
        MarkdownBatchImportResult importResult = result(
                imported("01_levantamiento-logico.md", DiagramTypeId.LOGICAL_BUSINESS_INTAKE),
                imported("02_grafo-logico.md", DiagramTypeId.LOGICAL_BUSINESS_GRAPH),
                imported("03_diccionario.md", DiagramTypeId.DATA_DICTIONARY),
                skipped("README.md"));

        CanonizationFlowReport report = useCase.from(importResult);

        assertEquals(CanonizationFlowReadiness.READY, report.readiness());
        assertTrue(report.readyForHumanReview());
        assertTrue(report.hasSingleLogicalBusinessIntake());
        assertEquals(1, report.logicalBusinessIntakeCount());
        assertEquals(1, report.countByRole(CanonizationArtifactRole.SOURCE_MOTHER));
        assertEquals(1, report.countByRole(CanonizationArtifactRole.LOGICAL_VIEW));
        assertEquals(1, report.countByRole(CanonizationArtifactRole.DATA_MODEL));
        assertTrue(report.recommendations().stream().anyMatch(text -> text.contains("omitidos")));
    }

    @Test
    void shouldWarnWhenSourceMotherIsMissing() {
        CanonizationFlowReport report = useCase.from(result(
                imported("03_diccionario.md", DiagramTypeId.DATA_DICTIONARY),
                imported("04_uml.md", DiagramTypeId.UML_CLASS)));

        assertEquals(CanonizationFlowReadiness.MISSING_LOGICAL_BUSINESS_INTAKE, report.readiness());
        assertFalse(report.readyForHumanReview());
        assertEquals(0, report.logicalBusinessIntakeCount());
    }

    @Test
    void shouldRejectDuplicatedLogicalBusinessIntakesAsEnterpriseFlowRisk() {
        CanonizationFlowReport report = useCase.from(result(
                imported("01_levantamiento-logico.md", DiagramTypeId.LOGICAL_BUSINESS_INTAKE),
                imported("01b_levantamiento-logico-minimo.md", DiagramTypeId.LOGICAL_BUSINESS_INTAKE)));

        assertEquals(CanonizationFlowReadiness.DUPLICATED_LOGICAL_BUSINESS_INTAKE, report.readiness());
        assertFalse(report.readyForHumanReview());
        assertEquals(2, report.logicalBusinessIntakeCount());
        assertTrue(report.recommendations().stream().anyMatch(text -> text.contains("Unifica")));
    }

    @Test
    void shouldRemainReviewableWhenThereAreRejectedAuxiliaryProjectFiles() {
        CanonizationFlowReport report = useCase.from(result(
                imported("01_levantamiento-logico.md", DiagramTypeId.LOGICAL_BUSINESS_INTAKE),
                rejected("04_uml-clases.md", DiagramTypeId.UML_CLASS)));

        assertEquals(CanonizationFlowReadiness.READY_WITH_REJECTIONS, report.readiness());
        assertTrue(report.readyForHumanReview());
        assertEquals(1, report.rejectedCount());
    }

    private static MarkdownBatchImportResult result(MarkdownBatchImportItemResult... items) {
        int imported = (int) java.util.Arrays.stream(items).filter(MarkdownBatchImportItemResult::imported).count();
        int skipped = (int) java.util.Arrays.stream(items).filter(MarkdownBatchImportItemResult::skipped).count();
        int rejected = (int) java.util.Arrays.stream(items).filter(MarkdownBatchImportItemResult::rejected).count();
        return new MarkdownBatchImportResult(
                Path.of("/tmp/caso"),
                items.length,
                imported + rejected,
                imported,
                skipped,
                rejected,
                List.of(items),
                List.of());
    }

    private static MarkdownBatchImportItemResult imported(String fileName, DiagramTypeId typeId) {
        return new MarkdownBatchImportItemResult(
                Path.of("/tmp/caso").resolve(fileName),
                fileName,
                Optional.of(typeId),
                Optional.empty(),
                MarkdownBatchImportItemStatus.IMPORTED,
                Optional.empty(),
                List.of(),
                Optional.empty());
    }

    private static MarkdownBatchImportItemResult skipped(String fileName) {
        return new MarkdownBatchImportItemResult(
                Path.of("/tmp/caso").resolve(fileName),
                fileName,
                Optional.empty(),
                Optional.empty(),
                MarkdownBatchImportItemStatus.SKIPPED_README,
                Optional.empty(),
                List.of(),
                Optional.empty());
    }

    private static MarkdownBatchImportItemResult rejected(String fileName, DiagramTypeId typeId) {
        return new MarkdownBatchImportItemResult(
                Path.of("/tmp/caso").resolve(fileName),
                fileName,
                Optional.of(typeId),
                Optional.empty(),
                MarkdownBatchImportItemStatus.REJECTED_PARSE_ERROR,
                Optional.empty(),
                List.of(),
                Optional.of("Contenido incompleto."));
    }
}
