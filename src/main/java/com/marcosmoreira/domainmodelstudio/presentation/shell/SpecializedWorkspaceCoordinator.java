package com.marcosmoreira.domainmodelstudio.presentation.shell;

import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureDiagramKind;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramKind;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualLayerOrderCommand;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualNodeSizeCommand;
import com.marcosmoreira.domainmodelstudio.presentation.architecture.ArchitectureDiagramViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.conceptual.ConceptualCanvasLegacyBridge;
import com.marcosmoreira.domainmodelstudio.presentation.behavior.BehaviorDiagramViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.datadictionary.DataDictionaryViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.freegraph.FreeGraphViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness.LogicalBusinessViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.logicalbusinessgraph.LogicalBusinessGraphViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.modulemap.ModuleMapViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.rolespermissions.RolesPermissionsViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.screenflow.ScreenFlowViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.umlclass.UmlClassDiagramViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.wireframe.WireframeViewModel;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Coordina los editores especializados sin obligar al shell a conocer sus detalles internos.
 *
 * <p>La intención es que {@link MainShellCommandHandler} conserve la orquestación de alto nivel,
 * mientras esta clase concentra el ruteo pequeño: limpiar editores, cargar el editor correcto y
 * consultar cuál editor especializado está activo. Así se reduce la tentación de agregar nuevas
 * cadenas de {@code if/else} en el shell cada vez que aparezca un tipo de diagrama.</p>
 */
final class SpecializedWorkspaceCoordinator {

    private final List<SpecializedWorkspaceBinding> bindings;

    SpecializedWorkspaceCoordinator(
            ConceptualCanvasLegacyBridge conceptualCanvasBridge,
            DataDictionaryViewModel dataDictionaryViewModel,
            ModuleMapViewModel moduleMapViewModel,
            UmlClassDiagramViewModel umlClassDiagramViewModel,
            RolesPermissionsViewModel rolesPermissionsViewModel,
            ScreenFlowViewModel screenFlowViewModel,
            WireframeViewModel wireframeViewModel,
            BehaviorDiagramViewModel behaviorDiagramViewModel,
            ArchitectureDiagramViewModel architectureDiagramViewModel,
            FreeGraphViewModel freeGraphViewModel,
            LogicalBusinessViewModel logicalBusinessViewModel,
            LogicalBusinessGraphViewModel logicalBusinessGraphViewModel
    ) {
        Objects.requireNonNull(conceptualCanvasBridge, "conceptualCanvasBridge");
        bindings = List.of(
                SpecializedWorkspaceBinding.of(
                        DiagramTypeId.CONCEPTUAL_MODEL::equals,
                        conceptualCanvasBridge::loadProject,
                        conceptualCanvasBridge::clear,
                        conceptualCanvasBridge::active,
                        conceptualCanvasBridge::currentProject),
                SpecializedWorkspaceBinding.of(
                        DiagramTypeId.DATA_DICTIONARY::equals,
                        dataDictionaryViewModel::loadProject,
                        dataDictionaryViewModel::clear,
                        dataDictionaryViewModel::active,
                        dataDictionaryViewModel::currentProject),
                SpecializedWorkspaceBinding.of(
                        DiagramTypeId.ADMIN_MODULE_MAP::equals,
                        moduleMapViewModel::loadProject,
                        moduleMapViewModel::clear,
                        moduleMapViewModel::active,
                        moduleMapViewModel::currentProject,
                        moduleMapViewModel::fitDiagramView,
                        moduleMapViewModel::centerDiagramView,
                        moduleMapViewModel::deleteSelectedBendPoint,
                        moduleMapViewModel::reorderSelectedElement,
                        moduleMapViewModel::resizeSelectedElement,
                        moduleMapViewModel::activateVisualCommentTool),
                SpecializedWorkspaceBinding.of(
                        DiagramTypeId.UML_CLASS::equals,
                        umlClassDiagramViewModel::loadProject,
                        umlClassDiagramViewModel::clear,
                        umlClassDiagramViewModel::active,
                        umlClassDiagramViewModel::currentProject,
                        umlClassDiagramViewModel::fitDiagramView,
                        umlClassDiagramViewModel::centerDiagramView,
                        umlClassDiagramViewModel::deleteSelectedBendPoint,
                        umlClassDiagramViewModel::reorderSelectedElement,
                        umlClassDiagramViewModel::resizeSelectedElement,
                        umlClassDiagramViewModel::activateVisualCommentTool),
                SpecializedWorkspaceBinding.of(
                        DiagramTypeId.ROLES_PERMISSIONS_MAP::equals,
                        rolesPermissionsViewModel::loadProject,
                        rolesPermissionsViewModel::clear,
                        rolesPermissionsViewModel::active,
                        rolesPermissionsViewModel::currentProject),
                SpecializedWorkspaceBinding.of(
                        DiagramTypeId.SCREEN_FLOW::equals,
                        screenFlowViewModel::loadProject,
                        screenFlowViewModel::clear,
                        screenFlowViewModel::active,
                        screenFlowViewModel::currentProject,
                        screenFlowViewModel::fitDiagramView,
                        screenFlowViewModel::centerDiagramView,
                        screenFlowViewModel::deleteSelectedBendPoint,
                        screenFlowViewModel::reorderSelectedElement,
                        screenFlowViewModel::resizeSelectedElement,
                        screenFlowViewModel::activateVisualCommentTool),
                SpecializedWorkspaceBinding.of(
                        DiagramTypeId.ADMIN_WIREFRAMES::equals,
                        wireframeViewModel::loadProject,
                        wireframeViewModel::clear,
                        wireframeViewModel::active,
                        wireframeViewModel::currentProject,
                        wireframeViewModel::fitDiagramView,
                        wireframeViewModel::centerDiagramView,
                        wireframeViewModel::deleteSelectedBendPoint,
                        wireframeViewModel::reorderSelectedElement,
                        wireframeViewModel::resizeSelectedElement,
                        wireframeViewModel::activateVisualCommentTool),
                SpecializedWorkspaceBinding.of(
                        SpecializedWorkspaceCoordinator::isBehaviorDiagramType,
                        behaviorDiagramViewModel::loadProject,
                        behaviorDiagramViewModel::clear,
                        behaviorDiagramViewModel::active,
                        behaviorDiagramViewModel::currentProject,
                        behaviorDiagramViewModel::fitDiagramView,
                        behaviorDiagramViewModel::centerDiagramView,
                        behaviorDiagramViewModel::deleteSelectedBendPoint,
                        behaviorDiagramViewModel::reorderSelectedElement,
                        behaviorDiagramViewModel::resizeSelectedElement,
                        behaviorDiagramViewModel::activateVisualCommentTool),
                SpecializedWorkspaceBinding.of(
                        ArchitectureDiagramKind::supports,
                        architectureDiagramViewModel::loadProject,
                        architectureDiagramViewModel::clear,
                        architectureDiagramViewModel::active,
                        architectureDiagramViewModel::currentProject,
                        architectureDiagramViewModel::fitDiagramView,
                        architectureDiagramViewModel::centerDiagramView,
                        architectureDiagramViewModel::deleteSelectedBendPoint,
                        architectureDiagramViewModel::reorderSelectedElement,
                        architectureDiagramViewModel::resizeSelectedElement,
                        architectureDiagramViewModel::activateVisualCommentTool),
                SpecializedWorkspaceBinding.of(
                        DiagramTypeId.FREE_GRAPH::equals,
                        freeGraphViewModel::loadProject,
                        freeGraphViewModel::clear,
                        freeGraphViewModel::active,
                        freeGraphViewModel::currentProject,
                        freeGraphViewModel::fitDiagramView,
                        freeGraphViewModel::centerDiagramView,
                        freeGraphViewModel::deleteSelectedBendPoint,
                        freeGraphViewModel::reorderSelectedElement,
                        freeGraphViewModel::resizeSelectedElement,
                        freeGraphViewModel::activateVisualCommentTool),
                SpecializedWorkspaceBinding.of(
                        DiagramTypeId.LOGICAL_BUSINESS_INTAKE::equals,
                        logicalBusinessViewModel::loadProject,
                        logicalBusinessViewModel::clear,
                        logicalBusinessViewModel::active,
                        logicalBusinessViewModel::currentProject),
                SpecializedWorkspaceBinding.of(
                        DiagramTypeId.LOGICAL_BUSINESS_GRAPH::equals,
                        logicalBusinessGraphViewModel::loadProject,
                        logicalBusinessGraphViewModel::clear,
                        logicalBusinessGraphViewModel::active,
                        logicalBusinessGraphViewModel::currentProject,
                        logicalBusinessGraphViewModel::fitDiagramView,
                        logicalBusinessGraphViewModel::centerDiagramView,
                        logicalBusinessGraphViewModel::deleteSelectedBendPoint,
                        logicalBusinessGraphViewModel::reorderSelectedElement,
                        logicalBusinessGraphViewModel::resizeSelectedElement,
                        logicalBusinessGraphViewModel::activateVisualCommentTool)
        );
    }

    boolean loadIfSpecialized(DiagramProject project) {
        Objects.requireNonNull(project, "project");
        DiagramTypeId diagramTypeId = project.metadata().diagramTypeId();
        for (SpecializedWorkspaceBinding binding : bindings) {
            if (binding.supports(diagramTypeId)) {
                binding.load(project);
                return true;
            }
        }
        return false;
    }

    void clearAll() {
        bindings.forEach(SpecializedWorkspaceBinding::clear);
    }

    boolean replaceActiveProjectIfCompatible(DiagramProject project) {
        Objects.requireNonNull(project, "project");
        DiagramTypeId diagramTypeId = project.metadata().diagramTypeId();
        for (SpecializedWorkspaceBinding binding : bindings) {
            if (binding.supports(diagramTypeId) && binding.isActive()) {
                binding.load(project);
                return true;
            }
        }
        return false;
    }

    Optional<DiagramProject> firstActiveProject() {
        return bindings.stream()
                .filter(SpecializedWorkspaceBinding::isActive)
                .map(SpecializedWorkspaceBinding::currentProject)
                .filter(Objects::nonNull)
                .findFirst();
    }

    boolean fitActiveDiagram(DiagramProject activeProject) {
        SpecializedWorkspaceBinding binding = activeViewportBinding(activeProject).orElse(null);
        if (binding == null) {
            return false;
        }
        binding.fitDiagram();
        return true;
    }

    boolean centerActiveDiagram(DiagramProject activeProject) {
        SpecializedWorkspaceBinding binding = activeViewportBinding(activeProject).orElse(null);
        if (binding == null) {
            return false;
        }
        binding.centerDiagram();
        return true;
    }

    boolean deleteSelectedBendPoint(DiagramProject activeProject) {
        SpecializedWorkspaceBinding binding = activeViewportBinding(activeProject).orElse(null);
        return binding != null && binding.deleteSelectedBendPoint();
    }


    boolean reorderSelectedElement(DiagramProject activeProject, VisualLayerOrderCommand command) {
        Objects.requireNonNull(command, "command");
        SpecializedWorkspaceBinding binding = activeViewportBinding(activeProject).orElse(null);
        return binding != null && binding.reorderSelectedElement(command);
    }

    boolean resizeSelectedElement(DiagramProject activeProject, VisualNodeSizeCommand command) {
        Objects.requireNonNull(command, "command");
        SpecializedWorkspaceBinding binding = activeViewportBinding(activeProject).orElse(null);
        return binding != null && binding.resizeSelectedElement(command);
    }

    boolean activateVisualCommentTool(DiagramProject activeProject) {
        SpecializedWorkspaceBinding binding = activeViewportBinding(activeProject).orElse(null);
        if (binding == null) {
            return false;
        }
        binding.activateVisualCommentTool();
        return true;
    }

    private Optional<SpecializedWorkspaceBinding> activeViewportBinding(DiagramProject activeProject) {
        if (activeProject == null) {
            return Optional.empty();
        }
        DiagramTypeId diagramTypeId = activeProject.metadata().diagramTypeId();
        return bindings.stream()
                .filter(binding -> binding.supports(diagramTypeId))
                .filter(SpecializedWorkspaceBinding::isActive)
                .filter(SpecializedWorkspaceBinding::canNavigateViewport)
                .filter(binding -> sameWorkspaceProject(binding.currentProject(), activeProject))
                .findFirst();
    }

    private static boolean sameWorkspaceProject(DiagramProject candidate, DiagramProject activeProject) {
        if (candidate == null || activeProject == null) {
            return false;
        }
        return Objects.equals(candidate.metadata().id(), activeProject.metadata().id())
                && Objects.equals(candidate.metadata().diagramTypeId(), activeProject.metadata().diagramTypeId());
    }

    static boolean isBehaviorDiagramType(DiagramTypeId diagramTypeId) {
        for (BehaviorDiagramKind kind : BehaviorDiagramKind.values()) {
            if (kind.diagramTypeId().equals(diagramTypeId)) {
                return true;
            }
        }
        return false;
    }

    private record SpecializedWorkspaceBinding(
            Predicate<DiagramTypeId> supportedType,
            Consumer<DiagramProject> loader,
            Runnable clearer,
            BooleanSupplier active,
            Supplier<DiagramProject> projectSupplier,
            Runnable fitDiagramAction,
            Runnable centerDiagramAction,
            BooleanSupplier deleteSelectedBendPointAction,
            Function<VisualLayerOrderCommand, Boolean> layerOrderAction,
            Function<VisualNodeSizeCommand, Boolean> nodeSizeAction,
            Runnable visualCommentAction,
            boolean canNavigateViewport
    ) {
        private SpecializedWorkspaceBinding {
            Objects.requireNonNull(supportedType, "supportedType");
            Objects.requireNonNull(loader, "loader");
            Objects.requireNonNull(clearer, "clearer");
            Objects.requireNonNull(active, "active");
            Objects.requireNonNull(projectSupplier, "projectSupplier");
            Objects.requireNonNull(fitDiagramAction, "fitDiagramAction");
            Objects.requireNonNull(centerDiagramAction, "centerDiagramAction");
            Objects.requireNonNull(deleteSelectedBendPointAction, "deleteSelectedBendPointAction");
            Objects.requireNonNull(layerOrderAction, "layerOrderAction");
            Objects.requireNonNull(nodeSizeAction, "nodeSizeAction");
            Objects.requireNonNull(visualCommentAction, "visualCommentAction");
        }

        static SpecializedWorkspaceBinding of(
                Predicate<DiagramTypeId> supportedType,
                Consumer<DiagramProject> loader,
                Runnable clearer,
                BooleanSupplier active,
                Supplier<DiagramProject> projectSupplier
        ) {
            return new SpecializedWorkspaceBinding(supportedType, loader, clearer, active, projectSupplier,
                    () -> { }, () -> { }, () -> false, command -> false, command -> false, () -> { }, false);
        }

        static SpecializedWorkspaceBinding of(
                Predicate<DiagramTypeId> supportedType,
                Consumer<DiagramProject> loader,
                Runnable clearer,
                BooleanSupplier active,
                Supplier<DiagramProject> projectSupplier,
                Runnable fitDiagramAction,
                Runnable centerDiagramAction,
                BooleanSupplier deleteSelectedBendPointAction
        ) {
            return new SpecializedWorkspaceBinding(supportedType, loader, clearer, active, projectSupplier,
                    fitDiagramAction, centerDiagramAction, deleteSelectedBendPointAction,
                    command -> false, command -> false, () -> { }, true);
        }

        static SpecializedWorkspaceBinding of(
                Predicate<DiagramTypeId> supportedType,
                Consumer<DiagramProject> loader,
                Runnable clearer,
                BooleanSupplier active,
                Supplier<DiagramProject> projectSupplier,
                Runnable fitDiagramAction,
                Runnable centerDiagramAction,
                BooleanSupplier deleteSelectedBendPointAction,
                Function<VisualLayerOrderCommand, Boolean> layerOrderAction
        ) {
            return new SpecializedWorkspaceBinding(supportedType, loader, clearer, active, projectSupplier,
                    fitDiagramAction, centerDiagramAction, deleteSelectedBendPointAction,
                    layerOrderAction, command -> false, () -> { }, true);
        }

        static SpecializedWorkspaceBinding of(
                Predicate<DiagramTypeId> supportedType,
                Consumer<DiagramProject> loader,
                Runnable clearer,
                BooleanSupplier active,
                Supplier<DiagramProject> projectSupplier,
                Runnable fitDiagramAction,
                Runnable centerDiagramAction,
                BooleanSupplier deleteSelectedBendPointAction,
                Function<VisualLayerOrderCommand, Boolean> layerOrderAction,
                Function<VisualNodeSizeCommand, Boolean> nodeSizeAction
        ) {
            return new SpecializedWorkspaceBinding(supportedType, loader, clearer, active, projectSupplier,
                    fitDiagramAction, centerDiagramAction, deleteSelectedBendPointAction,
                    layerOrderAction, nodeSizeAction, () -> { }, true);
        }

        static SpecializedWorkspaceBinding of(
                Predicate<DiagramTypeId> supportedType,
                Consumer<DiagramProject> loader,
                Runnable clearer,
                BooleanSupplier active,
                Supplier<DiagramProject> projectSupplier,
                Runnable fitDiagramAction,
                Runnable centerDiagramAction,
                BooleanSupplier deleteSelectedBendPointAction,
                Function<VisualLayerOrderCommand, Boolean> layerOrderAction,
                Function<VisualNodeSizeCommand, Boolean> nodeSizeAction,
                Runnable visualCommentAction
        ) {
            return new SpecializedWorkspaceBinding(supportedType, loader, clearer, active, projectSupplier,
                    fitDiagramAction, centerDiagramAction, deleteSelectedBendPointAction,
                    layerOrderAction, nodeSizeAction, visualCommentAction, true);
        }

        boolean supports(DiagramTypeId diagramTypeId) {
            return supportedType.test(diagramTypeId);
        }

        void load(DiagramProject project) {
            loader.accept(project);
        }

        void clear() {
            clearer.run();
        }

        boolean isActive() {
            return active.getAsBoolean();
        }

        DiagramProject currentProject() {
            return projectSupplier.get();
        }

        void fitDiagram() {
            fitDiagramAction.run();
        }

        void centerDiagram() {
            centerDiagramAction.run();
        }

        boolean deleteSelectedBendPoint() {
            return deleteSelectedBendPointAction.getAsBoolean();
        }

        boolean reorderSelectedElement(VisualLayerOrderCommand command) {
            return layerOrderAction.apply(command);
        }

        boolean resizeSelectedElement(VisualNodeSizeCommand command) {
            return nodeSizeAction.apply(command);
        }

        void activateVisualCommentTool() {
            visualCommentAction.run();
        }
    }
}
