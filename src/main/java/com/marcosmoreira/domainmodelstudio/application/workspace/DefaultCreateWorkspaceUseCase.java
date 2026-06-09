package com.marcosmoreira.domainmodelstudio.application.workspace;

import com.marcosmoreira.domainmodelstudio.application.catalog.DiagramTypeRegistry;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCapability;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCapabilitySet;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCategoryId;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramSupportStatus;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeDescriptor;
import java.util.Objects;

/**
 * Decide si un tipo de proyecto abre una vista de trabajo, una vista de preparación o ninguna vista.
 *
 * <p>La interfaz no debe deducir el estado de un tipo de diagrama con reglas dispersas. Esta clase
 * usa el catálogo oficial para evitar prometer diagramas o exportaciones que todavía no existen.</p>
 */
public final class DefaultCreateWorkspaceUseCase implements CreateWorkspaceUseCase {

    private final DiagramTypeRegistry diagramTypeRegistry;

    public DefaultCreateWorkspaceUseCase(DiagramTypeRegistry diagramTypeRegistry) {
        this.diagramTypeRegistry = Objects.requireNonNull(diagramTypeRegistry, "diagramTypeRegistry");
    }

    /**
     * Evalúa el tipo solicitado usando el catálogo oficial.
     *
     * @param request intención de crear o abrir un workspace para un tipo de proyecto.
     * @return decisión de producto y mensaje operativo para la presentación.
     */
    @Override
    public CreateWorkspaceResult execute(CreateWorkspaceRequest request) {
        Objects.requireNonNull(request, "request");
        return diagramTypeRegistry.findById(request.diagramTypeId())
                .map(descriptor -> {
                    WorkspaceSupportDecision decision = decisionFor(descriptor);
                    return new CreateWorkspaceResult(descriptor, decision, messageFor(descriptor, decision));
                })
                .orElseGet(() -> unsupportedResult(request));
    }

    private static WorkspaceSupportDecision decisionFor(DiagramTypeDescriptor descriptor) {
        if (descriptor.isAvailable()
                && (descriptor.supports(DiagramCapability.SHOW_VISUAL_OUTPUT)
                || descriptor.supports(DiagramCapability.SHOW_DOCUMENT_OUTPUT))) {
            return WorkspaceSupportDecision.PRODUCT_VIEW;
        }
        if (descriptor.supports(DiagramCapability.PLANNING_VIEW)) {
            return WorkspaceSupportDecision.PLANNING_VIEW;
        }
        return WorkspaceSupportDecision.UNSUPPORTED;
    }

    private static String messageFor(DiagramTypeDescriptor descriptor, WorkspaceSupportDecision decision) {
        return switch (decision) {
            case PRODUCT_VIEW -> descriptor.displayName() + " abre una vista lista para revisar, editar y exportar según su tipo.";
            case PLANNING_VIEW -> descriptor.displayName()
                    + " está en preparación: conserva guía, teoría y recursos para la siguiente etapa de diagramación.";
            case UNSUPPORTED -> descriptor.displayName()
                    + " no forma parte de los tipos de proyecto de esta versión.";
        };
    }

    private static CreateWorkspaceResult unsupportedResult(CreateWorkspaceRequest request) {
        DiagramTypeDescriptor descriptor = new DiagramTypeDescriptor(
                request.diagramTypeId(),
                request.projectName(),
                DiagramCategoryId.TECHNICAL_DOCUMENTATION,
                DiagramSupportStatus.DOCUMENTATION_AVAILABLE,
                DiagramCapabilitySet.empty(),
                "Tipo de diagrama fuera del catálogo de esta versión.",
                "",
                "");
        return new CreateWorkspaceResult(descriptor, WorkspaceSupportDecision.UNSUPPORTED, messageFor(descriptor, WorkspaceSupportDecision.UNSUPPORTED));
    }
}
