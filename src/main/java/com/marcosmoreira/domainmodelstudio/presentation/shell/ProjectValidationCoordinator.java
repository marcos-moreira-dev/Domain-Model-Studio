package com.marcosmoreira.domainmodelstudio.presentation.shell;

import com.marcosmoreira.domainmodelstudio.presentation.architecture.ArchitectureDiagramViewModel;
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
import java.util.Objects;

/**
 * Centraliza la validación visible de los workspaces especializados.
 *
 * <p>La validación es una operación transversal de shell: cada editor produce sus
 * advertencias, pero la presentación de diálogos y el mensaje de estado deben ser
 * consistentes. Esta clase evita que el shell principal concentre cadenas largas de
 * {@code if/else} y bloques repetidos de diálogo por tipo de proyecto.</p>
 */
final class ProjectValidationCoordinator {

    private final MainShellState shellState;
    private final DataDictionaryViewModel dataDictionaryViewModel;
    private final ModuleMapViewModel moduleMapViewModel;
    private final UmlClassDiagramViewModel umlClassDiagramViewModel;
    private final RolesPermissionsViewModel rolesPermissionsViewModel;
    private final ScreenFlowViewModel screenFlowViewModel;
    private final WireframeViewModel wireframeViewModel;
    private final BehaviorDiagramViewModel behaviorDiagramViewModel;
    private final ArchitectureDiagramViewModel architectureDiagramViewModel;
    private final FreeGraphViewModel freeGraphViewModel;
    private final LogicalBusinessViewModel logicalBusinessViewModel;
    private final LogicalBusinessGraphViewModel logicalBusinessGraphViewModel;
    private final ValidationDialogPresenter validationDialogPresenter;

    ProjectValidationCoordinator(
            MainShellState shellState,
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
            LogicalBusinessGraphViewModel logicalBusinessGraphViewModel,
            ValidationDialogPresenter validationDialogPresenter
    ) {
        this.shellState = Objects.requireNonNull(shellState, "shellState");
        this.dataDictionaryViewModel = Objects.requireNonNull(dataDictionaryViewModel, "dataDictionaryViewModel");
        this.moduleMapViewModel = Objects.requireNonNull(moduleMapViewModel, "moduleMapViewModel");
        this.umlClassDiagramViewModel = Objects.requireNonNull(umlClassDiagramViewModel, "umlClassDiagramViewModel");
        this.rolesPermissionsViewModel = Objects.requireNonNull(rolesPermissionsViewModel, "rolesPermissionsViewModel");
        this.screenFlowViewModel = Objects.requireNonNull(screenFlowViewModel, "screenFlowViewModel");
        this.wireframeViewModel = Objects.requireNonNull(wireframeViewModel, "wireframeViewModel");
        this.behaviorDiagramViewModel = Objects.requireNonNull(behaviorDiagramViewModel, "behaviorDiagramViewModel");
        this.architectureDiagramViewModel = Objects.requireNonNull(architectureDiagramViewModel, "architectureDiagramViewModel");
        this.freeGraphViewModel = Objects.requireNonNull(freeGraphViewModel, "freeGraphViewModel");
        this.logicalBusinessViewModel = Objects.requireNonNull(logicalBusinessViewModel, "logicalBusinessViewModel");
        this.logicalBusinessGraphViewModel = Objects.requireNonNull(logicalBusinessGraphViewModel, "logicalBusinessGraphViewModel");
        this.validationDialogPresenter = Objects.requireNonNull(validationDialogPresenter, "validationDialogPresenter");
    }

    void validateActiveProject(Runnable conceptualFallback) {
        if (dataDictionaryViewModel.active()) {
            validateDataDictionary();
            return;
        }
        if (moduleMapViewModel.active()) {
            validateModuleMap();
            return;
        }
        if (umlClassDiagramViewModel.active()) {
            validateUmlClassDiagram();
            return;
        }
        if (rolesPermissionsViewModel.active()) {
            validateRolesPermissions();
            return;
        }
        if (screenFlowViewModel.active()) {
            validateScreenFlow();
            return;
        }
        if (wireframeViewModel.active()) {
            validateWireframe();
            return;
        }
        if (behaviorDiagramViewModel.active()) {
            validateBehaviorDiagram();
            return;
        }
        if (architectureDiagramViewModel.active()) {
            validateArchitectureDiagram();
            return;
        }
        if (freeGraphViewModel.active()) {
            validateFreeGraph();
            return;
        }
        if (logicalBusinessViewModel.active()) {
            validateLogicalBusiness();
            return;
        }
        if (logicalBusinessGraphViewModel.active()) {
            validateLogicalBusinessGraph();
            return;
        }
        conceptualFallback.run();
    }

    void validateDataDictionary() {
        if (!dataDictionaryViewModel.active()) {
            shellState.updateStatus("Abre un diccionario de datos para validar.");
            return;
        }
        var result = dataDictionaryViewModel.validateDocument();
        if (!result.ok()) {
            validationDialogPresenter.show("Validación del diccionario", result.summary(), result.warnings());
        }
    }

    void validateModuleMap() {
        if (!moduleMapViewModel.active()) {
            shellState.updateStatus("Abre un mapa de módulos para validar.");
            return;
        }
        var result = moduleMapViewModel.validateDocument();
        if (!result.ok()) {
            validationDialogPresenter.show("Validación del mapa de módulos", result.summary(), result.warnings());
        }
    }

    void validateUmlClassDiagram() {
        if (!umlClassDiagramViewModel.active()) {
            shellState.updateStatus("Abre un diagrama UML Clases para validar.");
            return;
        }
        var result = umlClassDiagramViewModel.validateDocument();
        if (!result.ok()) {
            validationDialogPresenter.show("Validación UML Clases", result.summary(), result.warnings());
        }
    }

    void validateRolesPermissions() {
        if (!rolesPermissionsViewModel.active()) {
            shellState.updateStatus("Abre Roles y permisos para validar.");
            return;
        }
        var result = rolesPermissionsViewModel.validateDocument();
        if (!result.ok()) {
            validationDialogPresenter.show("Validación de roles y permisos", result.summary(), result.warnings());
        }
    }

    void validateScreenFlow() {
        if (!screenFlowViewModel.active()) {
            shellState.updateStatus("Abre Flujo de pantallas para validar.");
            return;
        }
        var result = screenFlowViewModel.validateDocument();
        if (!result.ok()) {
            validationDialogPresenter.show("Validación de flujo de pantallas", result.summary(), result.warnings());
        }
    }

    void validateWireframe() {
        if (!wireframeViewModel.active()) {
            shellState.updateStatus("Abre Wireframes para validar.");
            return;
        }
        var result = wireframeViewModel.validateDocument();
        if (!result.ok()) {
            validationDialogPresenter.show("Validación de wireframes", result.summary(), result.warnings());
        }
    }

    void validateBehaviorDiagram() {
        if (!behaviorDiagramViewModel.active()) {
            shellState.updateStatus("Abre un diagrama de comportamiento para validar.");
            return;
        }
        var result = behaviorDiagramViewModel.validateDocument();
        if (!result.ok()) {
            validationDialogPresenter.show("Validación del diagrama", result.summary(), result.warnings());
        }
    }

    void validateArchitectureDiagram() {
        if (!architectureDiagramViewModel.active()) {
            shellState.updateStatus("Abre un diagrama de arquitectura para validar.");
            return;
        }
        var result = architectureDiagramViewModel.validateDocument();
        if (!result.ok()) {
            validationDialogPresenter.show("Validación del diagrama de arquitectura", result.summary(), result.warnings());
        }
    }

    void validateFreeGraph() {
        if (!freeGraphViewModel.active()) {
            shellState.updateStatus("Abre un Grafo libre para validar.");
            return;
        }
        var result = freeGraphViewModel.validateDocument();
        if (!result.ok()) {
            validationDialogPresenter.show("Validación del grafo libre", result.summary(), result.warnings());
        }
    }

    void validateLogicalBusiness() {
        if (!logicalBusinessViewModel.active()) {
            shellState.updateStatus("Abre un Levantamiento lógico para validar.");
            return;
        }
        var issues = logicalBusinessViewModel.validationIssues();
        String summary = issues.isEmpty()
                ? "Levantamiento lógico sin observaciones."
                : (issues.size() == 1
                ? "1 observación en levantamiento lógico."
                : issues.size() + " observaciones en levantamiento lógico.");
        shellState.updateStatus(summary);
        if (!issues.isEmpty()) {
            validationDialogPresenter.show(
                    "Validación del levantamiento lógico",
                    summary,
                    issues.stream()
                            .map(issue -> issue.severity() + " · " + issue.targetId() + " · " + issue.message())
                            .toList());
        }
    }

    void validateLogicalBusinessGraph() {
        if (!logicalBusinessGraphViewModel.active()) {
            shellState.updateStatus("Abre un Grafo lógico del negocio para validar.");
            return;
        }
        var issues = logicalBusinessGraphViewModel.semanticIssues();
        String summary = issues.isEmpty()
                ? "Grafo lógico sin observaciones semánticas."
                : (issues.size() == 1
                ? "1 observación en grafo lógico."
                : issues.size() + " observaciones en grafo lógico.");
        shellState.updateStatus(summary);
        if (!issues.isEmpty()) {
            validationDialogPresenter.show(
                    "Validación del grafo lógico del negocio",
                    summary,
                    issues.stream()
                            .map(issue -> issue.severity() + " · " + issue.elementId() + " · " + issue.message())
                            .toList());
        }
    }
}
