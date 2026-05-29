package com.marcosmoreira.domainmodelstudio.presentation.visualtransfer;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.presentation.visualtransfer.ProjectVisualSelectionTransferService.PasteResult;
import java.util.Optional;

/** Enruta el pegado visual al soporte especializado de cada familia de diagrama. */
final class ProjectVisualSelectionPasteRouter {

    private ProjectVisualSelectionPasteRouter() {
    }

    static Optional<PasteResult> paste(DiagramProject targetProject, ProjectVisualSelectionTransferPayload payload) {
        if (targetProject == null || payload == null || payload.empty()) {
            return Optional.empty();
        }
        DiagramTypeId targetType = targetProject.metadata().diagramTypeId();
        if (!payload.diagramTypeId().equals(targetType)) {
            return Optional.empty();
        }
        PasteResult result;
        if (DiagramTypeId.CONCEPTUAL_MODEL.equals(targetType)) {
            result = ProjectVisualSelectionPasteCore.pasteConceptual(targetProject, payload);
        } else if (DiagramTypeId.ADMIN_MODULE_MAP.equals(targetType)) {
            result = ProjectVisualSelectionPasteStructured.pasteModuleMap(targetProject, payload);
        } else if (DiagramTypeId.SCREEN_FLOW.equals(targetType)) {
            result = ProjectVisualSelectionPasteStructured.pasteScreenFlow(targetProject, payload);
        } else if (DiagramTypeId.ADMIN_WIREFRAMES.equals(targetType)) {
            result = ProjectVisualSelectionPasteStructured.pasteWireframe(targetProject, payload);
        } else if (DiagramTypeId.UML_CLASS.equals(targetType)) {
            result = ProjectVisualSelectionPasteStructured.pasteUmlClass(targetProject, payload);
        } else if (targetProject.architectureDiagram().isPresent()) {
            result = ProjectVisualSelectionPasteCore.pasteArchitecture(targetProject, payload);
        } else if (targetProject.behaviorDiagram().isPresent()) {
            result = ProjectVisualSelectionPasteCore.pasteBehavior(targetProject, payload);
        } else {
            return Optional.empty();
        }
        return result.nodeCount() + result.connectorCount() == 0 ? Optional.empty() : Optional.of(result);
    }
}
