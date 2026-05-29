package com.marcosmoreira.domainmodelstudio.presentation;

import com.marcosmoreira.domainmodelstudio.application.ApplicationServices;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.presentation.canvas.DiagramCanvasViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.canvas.DiagramCanvasViewModel.WelcomeActions;
import com.marcosmoreira.domainmodelstudio.presentation.inspector.InspectorViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.datadictionary.DataDictionaryViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.modulemap.ModuleMapViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.umlclass.UmlClassDiagramViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.rolespermissions.RolesPermissionsViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.screenflow.ScreenFlowViewModel;
import com.marcosmoreira.domainmodelstudio.application.wireframe.ApplyWireframeTemplateUseCase;
import com.marcosmoreira.domainmodelstudio.application.freegraph.AddFreeGraphEdgeUseCase;
import com.marcosmoreira.domainmodelstudio.application.freegraph.AddFreeGraphNodeUseCase;
import com.marcosmoreira.domainmodelstudio.application.freegraph.RemoveFreeGraphItemUseCase;
import com.marcosmoreira.domainmodelstudio.application.freegraph.UpdateFreeGraphEdgeUseCase;
import com.marcosmoreira.domainmodelstudio.application.freegraph.UpdateFreeGraphNodeUseCase;
import com.marcosmoreira.domainmodelstudio.application.freegraph.ValidateFreeGraphUseCase;
import com.marcosmoreira.domainmodelstudio.presentation.wireframe.WireframeViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.behavior.BehaviorDiagramViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.architecture.ArchitectureDiagramViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.freegraph.FreeGraphViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness.LogicalBusinessViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.logicalbusinessgraph.LogicalBusinessGraphViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.selection.DiagramSelectionModel;
import com.marcosmoreira.domainmodelstudio.presentation.shell.MainShellCommandHandler;
import com.marcosmoreira.domainmodelstudio.presentation.shell.MainShellModule;
import com.marcosmoreira.domainmodelstudio.presentation.shell.MainShellState;
import com.marcosmoreira.domainmodelstudio.presentation.shell.MainShellView;
import com.marcosmoreira.domainmodelstudio.presentation.shell.MainShellViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.sidebar.ModelTreeViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.statusbar.StatusBarViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.toolbar.MainToolbarViewModel;
import java.util.Objects;

/** Composition root específico de presentación. */
public final class PresentationCompositionRoot {

    private final ApplicationServices applicationServices;

    public PresentationCompositionRoot(ApplicationServices applicationServices) {
        this.applicationServices = Objects.requireNonNull(applicationServices, "applicationServices");
    }

    public MainShellModule createMainShell() {
        StatusBarViewModel statusBarViewModel = new StatusBarViewModel();
        MainShellState shellState = new MainShellState(statusBarViewModel);
        DiagramSelectionModel selectionModel = new DiagramSelectionModel();
        ModelTreeViewModel modelTreeViewModel = new ModelTreeViewModel(selectionModel);
        DiagramCanvasViewModel canvasViewModel = new DiagramCanvasViewModel(
                selectionModel,
                applicationServices.addEntityUseCase(),
                applicationServices.addAttributeUseCase(),
                applicationServices.addRelationshipUseCase(),
                applicationServices.duplicateEntityUseCase(),
                applicationServices.removeDiagramElementUseCase(),
                applicationServices.moveElementUseCase(),
                applicationServices.addBendPointUseCase(),
                applicationServices.moveBendPointUseCase(),
                applicationServices.moveConnectorLabelUseCase(),
                applicationServices.removeBendPointUseCase(),
                applicationServices.changeConnectorAnchorsUseCase(),
                movedElement -> {
                    shellState.markDirty();
                    shellState.updateStatus("Diagrama actualizado.");
                }
        );
        statusBarViewModel.zoomProperty().bind(canvasViewModel.zoomTextProperty());
        selectionModel.selectedElementIdsProperty().addListener((observable, previous, current) -> {
            if (canvasViewModel.currentProject() == null) {
                return;
            }
            int count = current == null ? 0 : current.size();
            if (count == 0) {
                shellState.updateStatus("Sin selección.");
            } else if (count == 1) {
                shellState.updateStatus("1 elemento seleccionado.");
            } else {
                shellState.updateStatus(count + " elementos seleccionados.");
            }
        });

        java.util.function.Consumer<DiagramProject> projectConsumer = project -> {
            modelTreeViewModel.loadProject(project);
            canvasViewModel.applyEditedProject(project);
            shellState.showProjectState(project, "Proyecto en edición");
        };

        InspectorViewModel inspectorViewModel = new InspectorViewModel(
                selectionModel,
                canvasViewModel::currentProject,
                projectConsumer,
                applicationServices.renameElementUseCase(),
                applicationServices.updateNodeLayoutUseCase(),
                applicationServices.updateElementDescriptionUseCase(),
                applicationServices.updateRelationshipCardinalityUseCase(),
                applicationServices.changeElementStyleUseCase(),
                applicationServices.changeDiagramAppearanceUseCase(),
                applicationServices.changeConnectorAnchorsUseCase(),
                applicationServices.changeConnectorMarkerOrientationUseCase(),
                applicationServices.sourceMarkdownSynchronizer(),
                shellState::updateStatus,
                shellState::markDirty
        );

        DataDictionaryViewModel dataDictionaryViewModel = new DataDictionaryViewModel(
                applicationServices.addDataDictionaryEntityUseCase(),
                applicationServices.addDataDictionaryFieldUseCase(),
                applicationServices.updateDataDictionaryEntityUseCase(),
                applicationServices.updateDataDictionaryFieldUseCase(),
                applicationServices.removeDataDictionaryItemUseCase(),
                applicationServices.validateDataDictionaryUseCase(),
                shellState::updateStatus
        );

        ModuleMapViewModel moduleMapViewModel = new ModuleMapViewModel(
                applicationServices.addModuleMapModuleUseCase(),
                applicationServices.addModuleMapDependencyUseCase(),
                applicationServices.updateModuleMapModuleUseCase(),
                applicationServices.updateModuleMapDependencyUseCase(),
                applicationServices.removeModuleMapItemUseCase(),
                applicationServices.validateModuleMapUseCase(),
                shellState::updateStatus
        );

        UmlClassDiagramViewModel umlClassDiagramViewModel = new UmlClassDiagramViewModel(
                applicationServices.addUmlModuleUseCase(),
                applicationServices.addUmlClassUseCase(),
                applicationServices.addUmlMemberUseCase(),
                applicationServices.addUmlRelationUseCase(),
                applicationServices.updateUmlModuleUseCase(),
                applicationServices.updateUmlClassUseCase(),
                applicationServices.updateUmlMemberUseCase(),
                applicationServices.updateUmlRelationUseCase(),
                applicationServices.removeUmlClassDiagramItemUseCase(),
                applicationServices.validateUmlClassDiagramUseCase(),
                shellState::updateStatus
        );

        RolesPermissionsViewModel rolesPermissionsViewModel = new RolesPermissionsViewModel(
                applicationServices.addRoleUseCase(),
                applicationServices.addPermissionUseCase(),
                applicationServices.addPermissionAssignmentUseCase(),
                applicationServices.updateRoleUseCase(),
                applicationServices.updatePermissionUseCase(),
                applicationServices.updatePermissionAssignmentUseCase(),
                applicationServices.removeRolesPermissionsItemUseCase(),
                applicationServices.validateRolesPermissionsUseCase(),
                shellState::updateStatus
        );

        ScreenFlowViewModel screenFlowViewModel = new ScreenFlowViewModel(
                applicationServices.addScreenUseCase(),
                applicationServices.addScreenTransitionUseCase(),
                applicationServices.updateScreenUseCase(),
                applicationServices.updateScreenTransitionUseCase(),
                applicationServices.removeScreenFlowItemUseCase(),
                applicationServices.validateScreenFlowUseCase(),
                shellState::updateStatus
        );

        WireframeViewModel wireframeViewModel = new WireframeViewModel(
                applicationServices.addWireframeScreenUseCase(),
                applicationServices.addWireframeComponentUseCase(),
                applicationServices.updateWireframeScreenUseCase(),
                applicationServices.updateWireframeComponentUseCase(),
                applicationServices.removeWireframeItemUseCase(),
                applicationServices.validateWireframeUseCase(),
                new ApplyWireframeTemplateUseCase(),
                shellState::updateStatus
        );

        BehaviorDiagramViewModel behaviorDiagramViewModel = new BehaviorDiagramViewModel(
                applicationServices.addBehaviorNodeUseCase(),
                applicationServices.addBehaviorEdgeUseCase(),
                applicationServices.updateBehaviorNodeUseCase(),
                applicationServices.updateBehaviorEdgeUseCase(),
                applicationServices.removeBehaviorItemUseCase(),
                applicationServices.validateBehaviorDiagramUseCase(),
                shellState::updateStatus
        );

        ArchitectureDiagramViewModel architectureDiagramViewModel = new ArchitectureDiagramViewModel(
                applicationServices.addArchitectureNodeUseCase(),
                applicationServices.addArchitectureEdgeUseCase(),
                applicationServices.updateArchitectureNodeUseCase(),
                applicationServices.updateArchitectureEdgeUseCase(),
                applicationServices.removeArchitectureItemUseCase(),
                applicationServices.validateArchitectureDiagramUseCase(),
                shellState::updateStatus
        );

        FreeGraphViewModel freeGraphViewModel = new FreeGraphViewModel(
                new AddFreeGraphNodeUseCase(),
                new AddFreeGraphEdgeUseCase(),
                new UpdateFreeGraphNodeUseCase(),
                new UpdateFreeGraphEdgeUseCase(),
                new RemoveFreeGraphItemUseCase(),
                new ValidateFreeGraphUseCase(),
                shellState::updateStatus
        );

        LogicalBusinessViewModel logicalBusinessViewModel = new LogicalBusinessViewModel(shellState::updateStatus);
        LogicalBusinessGraphViewModel logicalBusinessGraphViewModel = new LogicalBusinessGraphViewModel(shellState::updateStatus);

        MainShellCommandHandler commandHandler = new MainShellCommandHandler(
                shellState,
                applicationServices,
                modelTreeViewModel,
                canvasViewModel,
                inspectorViewModel,
                dataDictionaryViewModel,
                moduleMapViewModel,
                umlClassDiagramViewModel,
                rolesPermissionsViewModel,
                screenFlowViewModel,
                wireframeViewModel,
                behaviorDiagramViewModel,
                architectureDiagramViewModel,
                freeGraphViewModel,
                logicalBusinessViewModel,
                logicalBusinessGraphViewModel
        );
        dataDictionaryViewModel.registerProjectChangeListener(commandHandler::synchronizeDataDictionaryEdit);
        moduleMapViewModel.registerProjectChangeListener(commandHandler::synchronizeModuleMapEdit);
        umlClassDiagramViewModel.registerProjectChangeListener(commandHandler::synchronizeUmlClassDiagramEdit);
        rolesPermissionsViewModel.registerProjectChangeListener(commandHandler::synchronizeRolesPermissionsEdit);
        screenFlowViewModel.registerProjectChangeListener(commandHandler::synchronizeScreenFlowEdit);
        wireframeViewModel.registerProjectChangeListener(commandHandler::synchronizeWireframeEdit);
        behaviorDiagramViewModel.registerProjectChangeListener(commandHandler::synchronizeBehaviorDiagramEdit);
        architectureDiagramViewModel.registerProjectChangeListener(commandHandler::synchronizeArchitectureDiagramEdit);
        freeGraphViewModel.registerProjectChangeListener(commandHandler::synchronizeFreeGraphEdit);
        logicalBusinessViewModel.registerProjectChangeListener(commandHandler::synchronizeLogicalBusinessEdit);
        logicalBusinessGraphViewModel.registerProjectChangeListener(commandHandler::synchronizeLogicalBusinessGraphEdit);
        canvasViewModel.registerStructuralEditListener(commandHandler::synchronizeStructuralEditFromCanvas);
        canvasViewModel.registerWelcomeActions(new WelcomeActions(
                commandHandler::requestNewProject,
                commandHandler::requestOpenProject,
                commandHandler::requestImportMarkdown,
                commandHandler::requestOpenExampleProject,
                commandHandler::requestOpenManual
        ));

        MainShellViewModel viewModel = new MainShellViewModel(
                shellState,
                commandHandler,
                new MainToolbarViewModel(commandHandler, umlClassDiagramViewModel),
                modelTreeViewModel,
                canvasViewModel,
                inspectorViewModel,
                dataDictionaryViewModel,
                moduleMapViewModel,
                umlClassDiagramViewModel,
                rolesPermissionsViewModel,
                screenFlowViewModel,
                wireframeViewModel,
                behaviorDiagramViewModel,
                architectureDiagramViewModel,
                freeGraphViewModel,
                logicalBusinessViewModel,
                logicalBusinessGraphViewModel,
                statusBarViewModel
        );
        MainShellView view = new MainShellView(viewModel);
        return new MainShellModule(view, viewModel);
    }
}
