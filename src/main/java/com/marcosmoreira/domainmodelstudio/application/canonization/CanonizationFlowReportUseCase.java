package com.marcosmoreira.domainmodelstudio.application.canonization;

import com.marcosmoreira.domainmodelstudio.application.importbatch.MarkdownBatchImportItemResult;
import com.marcosmoreira.domainmodelstudio.application.importbatch.MarkdownBatchImportResult;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Construye un reporte enterprise después de abrir una carpeta raíz Markdown.
 *
 * <p>Este caso de uso no decide qué importar ni abre pestañas; lee el resultado del
 * batch import y lo interpreta en términos del flujo de canonización documental:
 * fuente lógica canónica única, artefactos compatibles, archivos omitidos y rechazos que requieren
 * revisión humana.</p>
 */
public final class CanonizationFlowReportUseCase {

    private final CanonizationArtifactClassifier artifactClassifier;

    public CanonizationFlowReportUseCase() {
        this(new CanonizationArtifactClassifier());
    }

    public CanonizationFlowReportUseCase(CanonizationArtifactClassifier artifactClassifier) {
        this.artifactClassifier = Objects.requireNonNull(artifactClassifier, "artifactClassifier");
    }

    public CanonizationFlowReport from(MarkdownBatchImportResult importResult) {
        Objects.requireNonNull(importResult, "importResult");
        List<CanonizationFlowArtifact> artifacts = importResult.importedItems().stream()
                .flatMap(item -> toArtifact(item).stream())
                .toList();
        int logicalBusinessIntakes = (int) artifacts.stream()
                .filter(artifact -> DiagramTypeId.LOGICAL_BUSINESS_INTAKE.equals(artifact.diagramTypeId()))
                .count();
        CanonizationFlowReadiness readiness = readinessFor(importResult, logicalBusinessIntakes);
        return new CanonizationFlowReport(
                importResult.sourceRoot(),
                readiness,
                artifacts,
                importResult.importedCount(),
                importResult.skippedCount(),
                importResult.rejectedCount(),
                logicalBusinessIntakes,
                recommendationsFor(importResult, readiness, artifacts));
    }

    private List<CanonizationFlowArtifact> toArtifact(MarkdownBatchImportItemResult item) {
        if (!item.imported() || item.declaredDiagramType().isEmpty()) {
            return List.of();
        }
        DiagramTypeId typeId = item.declaredDiagramType().orElseThrow();
        return List.of(new CanonizationFlowArtifact(
                item.sourceFile(),
                item.displayName(),
                typeId,
                artifactClassifier.roleFor(typeId)));
    }

    private CanonizationFlowReadiness readinessFor(MarkdownBatchImportResult result, int logicalBusinessIntakes) {
        if (result.importedCount() == 0) {
            return CanonizationFlowReadiness.EMPTY;
        }
        if (logicalBusinessIntakes == 0) {
            return CanonizationFlowReadiness.MISSING_LOGICAL_BUSINESS_INTAKE;
        }
        if (logicalBusinessIntakes > 1) {
            return CanonizationFlowReadiness.DUPLICATED_LOGICAL_BUSINESS_INTAKE;
        }
        if (result.rejectedCount() > 0) {
            return CanonizationFlowReadiness.READY_WITH_REJECTIONS;
        }
        return CanonizationFlowReadiness.READY;
    }

    private List<String> recommendationsFor(
            MarkdownBatchImportResult result,
            CanonizationFlowReadiness readiness,
            List<CanonizationFlowArtifact> artifacts
    ) {
        List<String> recommendations = new ArrayList<>();
        switch (readiness) {
            case EMPTY -> recommendations.add("No se abrió ningún proyecto; verifica que la carpeta tenga Markdown con diagram_type importable.");
            case MISSING_LOGICAL_BUSINESS_INTAKE -> recommendations.add("Agrega un Levantamiento lógico como fuente lógica canónica del caso de negocio.");
            case DUPLICATED_LOGICAL_BUSINESS_INTAKE -> recommendations.add("Unifica los levantamientos lógicos: el caso de negocio debe tener una sola fuente lógica canónica.");
            case READY_WITH_REJECTIONS -> recommendations.add("Revisa los archivos rechazados antes de cerrar la documentación del caso.");
            case READY -> recommendations.add("Flujo listo para revisión humana: empieza por el Levantamiento lógico y luego valida los artefactos compatibles.");
        }
        if (artifacts.stream().noneMatch(artifact -> artifact.role() == CanonizationArtifactRole.DATA_MODEL)) {
            recommendations.add("Considera construir al menos un artefacto de datos compatible: diccionario, modelo conceptual o UML clases.");
        }
        if (result.skippedCount() > 0) {
            recommendations.add("Los archivos omitidos se trataron como auxiliares y no bloquearon la apertura de la carpeta.");
        }
        return recommendations;
    }
}
