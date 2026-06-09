package com.marcosmoreira.domainmodelstudio.presentation.shell;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassKind;
import com.marcosmoreira.domainmodelstudio.presentation.umlclass.UmlClassDiagramViewModel;
import java.util.Objects;

/** Comandos de UML Clases y navegación hacia código fuente. */
final class UmlClassShellCommands {

    private final MainShellState shellState;
    private final UmlClassDiagramViewModel viewModel;
    private final ProjectValidationCoordinator validationCoordinator;
    private final SpecializedProjectSynchronizer projectSynchronizer;

    UmlClassShellCommands(
            MainShellState shellState,
            UmlClassDiagramViewModel viewModel,
            ProjectValidationCoordinator validationCoordinator,
            SpecializedProjectSynchronizer projectSynchronizer
    ) {
        this.shellState = Objects.requireNonNull(shellState, "shellState");
        this.viewModel = Objects.requireNonNull(viewModel, "viewModel");
        this.validationCoordinator = Objects.requireNonNull(validationCoordinator, "validationCoordinator");
        this.projectSynchronizer = Objects.requireNonNull(projectSynchronizer, "projectSynchronizer");
    }

    void synchronizeEdit(DiagramProject updatedProject) {
        projectSynchronizer.synchronize(updatedProject, "UML Clases actualizado");
    }

    void requestAddModule() {
        if (!ensureActive("agregar módulos")) {
            return;
        }
        viewModel.addModule();
    }

    void requestAddClass() {
        requestAddClass(UmlClassKind.CLASS, "agregar clases");
    }

    void requestAddInterface() {
        requestAddClass(UmlClassKind.INTERFACE, "agregar interfaces");
    }

    void requestAddEnum() {
        requestAddClass(UmlClassKind.ENUM, "agregar enums");
    }

    void requestAddAttribute() {
        if (!ensureActive("agregar atributos")) {
            return;
        }
        viewModel.addAttribute();
    }

    void requestAddMethod() {
        if (!ensureActive("agregar métodos")) {
            return;
        }
        viewModel.addMethod();
    }

    void requestAddRelation() {
        if (!ensureActive("agregar relaciones")) {
            return;
        }
        viewModel.addRelation();
    }

    void requestRemoveItem() {
        if (!ensureActive("eliminar elementos")) {
            return;
        }
        viewModel.removeSelected();
    }

    void requestOpenSelectedSourceFile() {
        if (!ensureActive("abrir código fuente")) {
            return;
        }
        viewModel.openSelectedSourceWithSystemDefault();
    }

    void requestValidate() {
        validationCoordinator.validateUmlClassDiagram();
    }

    void requestRegenerateLayout() {
        viewModel.reorganizeLayout();
    }

    private void requestAddClass(UmlClassKind kind, String action) {
        if (!ensureActive(action)) {
            return;
        }
        viewModel.addClass(kind);
    }

    private boolean ensureActive(String action) {
        if (viewModel.active()) {
            return true;
        }
        shellState.updateStatus("Abre un diagrama UML Clases para " + action + ".");
        return false;
    }
}
