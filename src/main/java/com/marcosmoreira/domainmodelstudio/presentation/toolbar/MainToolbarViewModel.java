package com.marcosmoreira.domainmodelstudio.presentation.toolbar;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.notation.NotationType;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeScreenTemplateKind;
import com.marcosmoreira.domainmodelstudio.presentation.exportable.ExportFormat;
import com.marcosmoreira.domainmodelstudio.presentation.shell.MainShellCommandHandler;
import com.marcosmoreira.domainmodelstudio.presentation.umlclass.UmlClassDiagramViewModel;
import java.util.Objects;
import java.util.Set;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * ViewModel de la toolbar principal.
 *
 * <p>La toolbar no modifica directamente propiedades del shell. Solo solicita comandos al
 * manejador global, que delega en el manejador global de comandos.</p>
 */
public final class MainToolbarViewModel {

    private final MainShellCommandHandler commandHandler;
    private final UmlClassDiagramViewModel umlClassDiagramViewModel;
    private final ObjectProperty<WireframeScreenTemplateKind> selectedWireframeTemplate =
            new SimpleObjectProperty<>(WireframeScreenTemplateKind.CRUD_LIST);
    private final DiagramToolbarActionExecutor diagramActionExecutor;

    public MainToolbarViewModel(
            MainShellCommandHandler commandHandler,
            UmlClassDiagramViewModel umlClassDiagramViewModel
    ) {
        this.commandHandler = Objects.requireNonNull(commandHandler, "commandHandler");
        this.umlClassDiagramViewModel = Objects.requireNonNull(umlClassDiagramViewModel, "umlClassDiagramViewModel");
        this.diagramActionExecutor = new DiagramToolbarActionExecutor(commandHandler, selectedWireframeTemplate::get);
    }

    public void newProject() {
        commandHandler.requestNewProject();
    }

    public void openProject() {
        commandHandler.requestOpenProject();
    }

    public void saveProject() {
        commandHandler.requestSaveProject();
    }

    public void saveProjectAs() {
        commandHandler.requestSaveProjectAs();
    }

    public void closeProject() {
        commandHandler.requestCloseProject();
    }


    public void undo() {
        commandHandler.requestUndo();
    }

    public void redo() {
        commandHandler.requestRedo();
    }

    public void importMarkdown() {
        commandHandler.requestImportMarkdown();
    }

    public void importMarkdownFolder() {
        commandHandler.requestImportMarkdownFolder();
    }

    public void openExampleProject() {
        commandHandler.requestOpenExampleProject();
    }

    public void exportAiResources() {
        commandHandler.requestExportAiResources();
    }

    public void addEntity() {
        commandHandler.requestAddEntityTool();
    }

    public void addAttribute() {
        commandHandler.requestAddAttributeToSelectedEntity();
    }

    public void addRelationship() {
        commandHandler.requestAddRelationshipTool();
    }

    public void duplicateElement() {
        commandHandler.requestDuplicateSelectedEntity();
    }

    public void deleteElement() {
        commandHandler.requestRemoveSelectedElement();
    }


    public void addDataDictionaryEntity() {
        commandHandler.requestAddDataDictionaryEntity();
    }

    public void addDataDictionaryField() {
        commandHandler.requestAddDataDictionaryField();
    }

    public void removeDataDictionaryItem() {
        commandHandler.requestRemoveDataDictionaryItem();
    }

    public void validateDataDictionary() {
        commandHandler.requestValidateDataDictionary();
    }

    public void exportDataDictionaryPdf() {
        commandHandler.requestExportDataDictionaryPdf();
    }

    public void addModule() {
        commandHandler.requestAddModuleMapModule();
    }

    public void addSubmodule() {
        commandHandler.requestAddModuleMapSubmodule();
    }

    public void addModuleDependency() {
        commandHandler.requestAddModuleMapDependency();
    }

    public void removeModuleMapItem() {
        commandHandler.requestRemoveModuleMapItem();
    }

    public void validateModuleMap() {
        commandHandler.requestValidateModuleMap();
    }

    public void addUmlModule() {
        commandHandler.requestAddUmlModule();
    }

    public void addUmlClass() {
        commandHandler.requestAddUmlClass();
    }

    public void addUmlInterface() {
        commandHandler.requestAddUmlInterface();
    }

    public void addUmlEnum() {
        commandHandler.requestAddUmlEnum();
    }

    public void addUmlAttribute() {
        commandHandler.requestAddUmlAttribute();
    }

    public void addUmlMethod() {
        commandHandler.requestAddUmlMethod();
    }

    public void addUmlRelation() {
        commandHandler.requestAddUmlRelation();
    }

    public void removeUmlItem() {
        commandHandler.requestRemoveUmlItem();
    }

    public void validateUmlClassDiagram() {
        commandHandler.requestValidateUmlClassDiagram();
    }


    public void addRole() { commandHandler.requestAddRole(); }
    public void addPermission() { commandHandler.requestAddPermission(); }
    public void addPermissionAssignment() { commandHandler.requestAddPermissionAssignment(); }
    public void removeRolesPermissionsItem() { commandHandler.requestRemoveRolesPermissionsItem(); }
    public void validateRolesPermissions() { commandHandler.requestValidateRolesPermissions(); }
    public void addScreen() { commandHandler.requestAddScreen(); }
    public void addScreenTransition() { commandHandler.requestAddScreenTransition(); }
    public void removeScreenFlowItem() { commandHandler.requestRemoveScreenFlowItem(); }
    public void validateScreenFlow() { commandHandler.requestValidateScreenFlow(); }
    public void addWireframeScreen() { commandHandler.requestAddWireframeScreen(); }
    public void addWireframeSection() { commandHandler.requestAddWireframeSection(); }
    public void addWireframeForm() { commandHandler.requestAddWireframeForm(); }
    public void addWireframeTable() { commandHandler.requestAddWireframeTable(); }
    public void addWireframeField() { commandHandler.requestAddWireframeField(); }
    public void addWireframeButton() { commandHandler.requestAddWireframeButton(); }
    public void removeWireframeItem() { commandHandler.requestRemoveWireframeItem(); }
    public void validateWireframe() { commandHandler.requestValidateWireframe(); }
    public void applySelectedWireframeTemplate() { commandHandler.requestApplyWireframeTemplate(selectedWireframeTemplate.get()); }
    public ObjectProperty<WireframeScreenTemplateKind> selectedWireframeTemplateProperty() { return selectedWireframeTemplate; }
    public void addBpmnStart() { commandHandler.requestAddBpmnStart(); }
    public void addBpmnActivity() { commandHandler.requestAddBpmnActivity(); }
    public void addBpmnDecision() { commandHandler.requestAddBpmnDecision(); }
    public void addBpmnEnd() { commandHandler.requestAddBpmnEnd(); }
    public void addBpmnLane() { commandHandler.requestAddBpmnLane(); }
    public void addBehaviorFlow() { commandHandler.requestAddBehaviorFlow(); }
    public void addBehaviorNote() { commandHandler.requestAddBehaviorNote(); }
    public void addUseCaseActor() { commandHandler.requestAddUseCaseActor(); }
    public void addUseCase() { commandHandler.requestAddUseCase(); }
    public void addUseCaseSystem() { commandHandler.requestAddUseCaseSystem(); }
    public void addUseCaseAssociation() { commandHandler.requestAddUseCaseAssociation(); }
    public void addUseCaseInclude() { commandHandler.requestAddUseCaseInclude(); }
    public void addUseCaseExtend() { commandHandler.requestAddUseCaseExtend(); }
    public void addUseCaseGeneralization() { commandHandler.requestAddUseCaseGeneralization(); }
    public void addUmlAction() { commandHandler.requestAddUmlAction(); }
    public void addUmlDecision() { commandHandler.requestAddUmlDecision(); }
    public void addUmlInitialState() { commandHandler.requestAddUmlInitialState(); }
    public void addUmlFinalState() { commandHandler.requestAddUmlFinalState(); }
    public void addSequenceParticipant() { commandHandler.requestAddSequenceParticipant(); }
    public void addSequenceActivation() { commandHandler.requestAddSequenceActivation(); }
    public void addSequenceFragment() { commandHandler.requestAddSequenceFragment(); }
    public void addSequenceMessage() { commandHandler.requestAddSequenceMessage(); }
    public void addSequenceReturnMessage() { commandHandler.requestAddSequenceReturnMessage(); }
    public void addState() { commandHandler.requestAddState(); }
    public void addStateTransition() { commandHandler.requestAddStateTransition(); }
    public void removeBehaviorItem() { commandHandler.requestRemoveBehaviorItem(); }
    public void validateBehaviorDiagram() { commandHandler.requestValidateBehaviorDiagram(); }
    public void addC4Person() { commandHandler.requestAddC4Person(); }
    public void addC4System() { commandHandler.requestAddC4System(); }
    public void addC4ExternalSystem() { commandHandler.requestAddC4ExternalSystem(); }
    public void addC4Boundary() { commandHandler.requestAddC4Boundary(); }
    public void addC4Container() { commandHandler.requestAddC4Container(); }
    public void addC4Application() { commandHandler.requestAddC4Application(); }
    public void addC4Api() { commandHandler.requestAddC4Api(); }
    public void addArchitectureDatabase() { commandHandler.requestAddArchitectureDatabase(); }
    public void addArchitectureExternalService() { commandHandler.requestAddArchitectureExternalService(); }
    public void addDeploymentEnvironment() { commandHandler.requestAddDeploymentEnvironment(); }
    public void addDeploymentServer() { commandHandler.requestAddDeploymentServer(); }
    public void addDeploymentClient() { commandHandler.requestAddDeploymentClient(); }
    public void addDeploymentService() { commandHandler.requestAddDeploymentService(); }
    public void addDeploymentNetwork() { commandHandler.requestAddDeploymentNetwork(); }
    public void addDeploymentArtifact() { commandHandler.requestAddDeploymentArtifact(); }
    public void addArchitectureUses() { commandHandler.requestAddArchitectureUses(); }
    public void addArchitectureDependency() { commandHandler.requestAddArchitectureDependency(); }
    public void addArchitectureIntegration() { commandHandler.requestAddArchitectureIntegration(); }
    public void addArchitectureCall() { commandHandler.requestAddArchitectureCall(); }
    public void addArchitectureReadsWrites() { commandHandler.requestAddArchitectureReadsWrites(); }
    public void addDeploymentConnection() { commandHandler.requestAddDeploymentConnection(); }
    public void addDeploymentHosting() { commandHandler.requestAddDeploymentHosting(); }
    public void addDeploymentTarget() { commandHandler.requestAddDeploymentTarget(); }
    public void removeArchitectureItem() { commandHandler.requestRemoveArchitectureItem(); }
    public void validateArchitectureDiagram() { commandHandler.requestValidateArchitectureDiagram(); }

    public void validateProject() {
        commandHandler.requestValidateProject();
    }

    public void regenerateLayout() {
        commandHandler.requestRegenerateLayout();
    }

    public void exportSvg() {
        commandHandler.requestExportSvg();
    }

    public void exportMarkdown() {
        commandHandler.requestExportMarkdown();
    }

    public void exportPng() {
        commandHandler.requestExportPng();
    }

    public void exportClientBatch() {
        commandHandler.requestExportClientBatch();
    }

    public void switchToChen() {
        commandHandler.requestSwitchNotation(NotationType.CHEN);
    }

    public void switchToCrowsFoot() {
        commandHandler.requestSwitchNotation(NotationType.CROWS_FOOT);
    }

    public void zoomIn() {
        commandHandler.requestZoomIn();
    }

    public void zoomOut() {
        commandHandler.requestZoomOut();
    }

    public void resetZoom() {
        commandHandler.requestResetZoom();
    }

    public void fitToContent() {
        commandHandler.requestFitToContent();
    }

    public void centerSelection() {
        commandHandler.requestCenterSelection();
    }

    public BooleanExpression projectClosed() {
        return commandHandler.projectOpenProperty().not();
    }

    public BooleanExpression saveableProjectClosed() {
        return commandHandler.saveableProjectOpenProperty().not();
    }

    public BooleanExpression conceptualCanvasCommandUnavailable() {
        return projectClosed().or(Bindings.createBooleanBinding(
                () -> !DiagramTypeId.CONCEPTUAL_MODEL.equals(activeDiagramTypeProperty().get()),
                activeDiagramTypeProperty()));
    }

    public ObjectProperty<NotationType> activeNotationProperty() {
        return commandHandler.activeNotationProperty();
    }

    public ObjectProperty<DiagramTypeId> activeDiagramTypeProperty() {
        return commandHandler.activeDiagramTypeProperty();
    }

    public ObjectProperty<Set<ExportFormat>> activeExportFormatsProperty() {
        return commandHandler.activeExportFormatsProperty();
    }

    public boolean activeOutputSupports(ExportFormat format) {
        Set<ExportFormat> formats = activeExportFormatsProperty().get();
        return format != null && formats != null && formats.contains(format);
    }

    public BooleanExpression exportUnavailable(ExportFormat format) {
        return projectClosed().or(Bindings.createBooleanBinding(
                () -> !activeOutputSupports(format),
                activeExportFormatsProperty()));
    }

    public BooleanExpression diagramActionUnavailable(DiagramToolbarActionId actionId) {
        if (actionId == DiagramToolbarActionId.OPEN_UML_SOURCE) {
            return projectClosed().or(Bindings.createBooleanBinding(
                    () -> !DiagramTypeId.UML_CLASS.equals(activeDiagramTypeProperty().get()),
                    activeDiagramTypeProperty()));
        }
        return projectClosed();
    }

    public boolean canExecuteDiagramAction(DiagramToolbarActionId actionId) {
        return diagramActionExecutor.canExecute(actionId);
    }

    public void executeDiagramAction(DiagramToolbarActionId actionId) {
        diagramActionExecutor.execute(actionId);
    }

}

