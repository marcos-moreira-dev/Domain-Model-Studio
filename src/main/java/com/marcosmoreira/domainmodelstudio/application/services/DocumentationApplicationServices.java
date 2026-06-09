package com.marcosmoreira.domainmodelstudio.application.services;

import com.marcosmoreira.domainmodelstudio.application.canonization.CanonizationFlowReportUseCase;
import com.marcosmoreira.domainmodelstudio.application.resources.ExportAiResourcesUseCase;
import java.util.Objects;

/**
 * Fachada de recursos documentales y productización IA.
 *
 * <p>La operación de elegir destino sigue siendo presentación; esta familia solo
 * expone el caso de uso de exportación de recursos.</p>
 */
public final class DocumentationApplicationServices {

    private final ExportAiResourcesUseCase exportAiResourcesUseCase;
    private final CanonizationFlowReportUseCase canonizationFlowReportUseCase;

    public DocumentationApplicationServices(ExportAiResourcesUseCase exportAiResourcesUseCase) {
        this(exportAiResourcesUseCase, new CanonizationFlowReportUseCase());
    }

    public DocumentationApplicationServices(
            ExportAiResourcesUseCase exportAiResourcesUseCase,
            CanonizationFlowReportUseCase canonizationFlowReportUseCase
    ) {
        this.exportAiResourcesUseCase = Objects.requireNonNull(exportAiResourcesUseCase, "exportAiResourcesUseCase");
        this.canonizationFlowReportUseCase = Objects.requireNonNull(
                canonizationFlowReportUseCase, "canonizationFlowReportUseCase");
    }

    public ExportAiResourcesUseCase exportAiResourcesUseCase() {
        return exportAiResourcesUseCase;
    }

    public CanonizationFlowReportUseCase canonizationFlowReportUseCase() {
        return canonizationFlowReportUseCase;
    }
}
