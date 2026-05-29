package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import com.marcosmoreira.domainmodelstudio.application.umlclass.UmlSourceImportRenderProfile;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramDocument;
import java.io.IOException;
import java.util.Objects;

/**
 * Reglas de seguridad para exportar UML Clases sin intentar materializar vistas enormes.
 *
 * <p>La exportación visual debe trabajar por vista activa. La Mega vista o las vistas
 * completas siguen disponibles como concepto, pero se bloquean si el costo estimado
 * o la presión de memoria llegan a nivel crítico.</p>
 */
public final class UmlClassExportSafetyPolicy {

    private final UmlClassVisualCostEstimator estimator = new UmlClassVisualCostEstimator();

    public void ensurePngExportAllowed(
            UmlClassVisualCostEstimate estimate,
            UmlClassRuntimeMemorySnapshot memorySnapshot
    ) throws IOException {
        UmlClassVisualCostEstimate safeEstimate = estimate == null
                ? UmlClassVisualCostEstimate.empty(UmlSourceImportRenderProfile.safeDefault())
                : estimate;
        UmlClassRuntimeMemorySnapshot safeMemory = memorySnapshot == null
                ? UmlClassRuntimeMemorySnapshot.empty()
                : memorySnapshot;
        if (safeEstimate.level() == UmlClassVisualCostLevel.CRITICAL) {
            // Tanda 8: la política ya no bloquea de entrada; el exportador PNG común conserva
            // sus protecciones de memoria y la UI informa errores reales si el snapshot falla.
        }
        if (safeMemory.level() == UmlClassRuntimeMemoryLevel.CRITICAL) {
            throw new IOException("Exportación PNG detenida: memoria JVM crítica ("
                    + safeMemory.detailSummary()
                    + "). Cierra vistas pesadas o usa una vista más pequeña antes de exportar.");
        }
    }

    public void ensureSvgExportAllowed(DiagramProject project) throws IOException {
        if (project == null || !DiagramTypeId.UML_CLASS.equals(project.metadata().diagramTypeId())) {
            return;
        }
        UmlClassDiagramDocument document = project.umlClassDiagram().orElse(null);
        if (document == null) {
            return;
        }
        UmlClassVisualCostEstimate estimate = estimator.estimate(
                document.modules(),
                document.classes(),
                document.relations(),
                UmlSourceImportRenderProfile.LIGHT);
        if (estimate.level() == UmlClassVisualCostLevel.CRITICAL) {
            // Tanda 8: SVG es salida obligatoria para diagramas visuales; no se bloquea aquí.
            // Si una vista es enorme, la exportación especializada o el sistema reportarán el fallo concreto.
        }
    }

    public UmlClassVisualCostEstimate estimateForSvg(DiagramProject project) {
        Objects.requireNonNull(project, "project");
        UmlClassDiagramDocument document = project.umlClassDiagram().orElse(null);
        if (document == null) {
            return UmlClassVisualCostEstimate.empty(UmlSourceImportRenderProfile.LIGHT);
        }
        return estimator.estimate(document.modules(), document.classes(), document.relations(), UmlSourceImportRenderProfile.LIGHT);
    }
}
