package com.marcosmoreira.domainmodelstudio.application.canonization;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

/** Reporte de lectura enterprise sobre una carpeta Markdown ya importada. */
public record CanonizationFlowReport(
        Path sourceRoot,
        CanonizationFlowReadiness readiness,
        List<CanonizationFlowArtifact> artifacts,
        int importedCount,
        int skippedCount,
        int rejectedCount,
        int logicalBusinessIntakeCount,
        List<String> recommendations
) {

    public CanonizationFlowReport {
        Objects.requireNonNull(sourceRoot, "sourceRoot");
        Objects.requireNonNull(readiness, "readiness");
        artifacts = List.copyOf(artifacts == null ? List.of() : artifacts);
        if (importedCount < 0 || skippedCount < 0 || rejectedCount < 0 || logicalBusinessIntakeCount < 0) {
            throw new IllegalArgumentException("Los contadores del flujo de canonización no pueden ser negativos.");
        }
        recommendations = List.copyOf(recommendations == null ? List.of() : recommendations);
    }

    public boolean hasSingleLogicalBusinessIntake() {
        return logicalBusinessIntakeCount == 1;
    }

    public boolean readyForHumanReview() {
        return readiness == CanonizationFlowReadiness.READY
                || readiness == CanonizationFlowReadiness.READY_WITH_REJECTIONS;
    }

    public long countByRole(CanonizationArtifactRole role) {
        Objects.requireNonNull(role, "role");
        return artifacts.stream().filter(artifact -> artifact.role() == role).count();
    }
}
