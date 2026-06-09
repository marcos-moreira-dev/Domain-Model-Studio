package com.marcosmoreira.domainmodelstudio.presentation.behavior;

import com.marcosmoreira.domainmodelstudio.application.visual.VisualElementLayoutIds;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualLayoutService;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramKind;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNode;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNodeKind;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.util.List;
import java.util.function.Consumer;

/** Operaciones manuales de layout aplicables solo a fragmentos combinados de UML Secuencia. */
final class BehaviorSequenceFragmentManualLayout {

    private static final double MIN_FRAGMENT_WIDTH = 340.0;
    private static final double MIN_FRAGMENT_HEIGHT = 120.0;

    private BehaviorSequenceFragmentManualLayout() {
    }

    static boolean isFragmentNode(List<BehaviorNode> nodes, String nodeId) {
        return nodes.stream().anyMatch(candidate -> candidate.id().equals(nodeId)
                && candidate.kind() == BehaviorNodeKind.FRAGMENT);
    }

    static DiagramProject lockPosition(DiagramProject project, DiagramElementId layoutId) {
        if (project == null || layoutId == null) {
            return project;
        }
        return project.layouts().activeLayout().nodeFor(layoutId)
                .map(layout -> project.withLayouts(project.layouts().withLayout(
                        project.layouts().activeLayout().withNode(layout.withLocked(true)))))
                .orElse(project);
    }

    static DiagramProject resize(
            DiagramProject project,
            BehaviorDiagramKind kind,
            List<BehaviorNode> nodes,
            String nodeId,
            double width,
            double height,
            VisualLayoutService layoutService,
            Consumer<String> statusConsumer
    ) {
        if (kind != BehaviorDiagramKind.UML_SEQUENCE) {
            statusConsumer.accept("El redimensionado manual de fragmentos solo aplica a UML Secuencia.");
            return project;
        }
        if (!isFragmentNode(nodes, nodeId)) {
            statusConsumer.accept("Selecciona un fragmento alt, opt, loop, par, break, critical o ref para redimensionarlo.");
            return project;
        }
        DiagramElementId layoutId = VisualElementLayoutIds.behaviorNode(nodeId);
        DiagramProject resized = layoutService.resizeNodeTo(
                project,
                layoutId,
                Math.max(MIN_FRAGMENT_WIDTH, width),
                Math.max(MIN_FRAGMENT_HEIGHT, height));
        statusConsumer.accept("Tamaño de fragmento UML Secuencia actualizado.");
        return resized;
    }
}
