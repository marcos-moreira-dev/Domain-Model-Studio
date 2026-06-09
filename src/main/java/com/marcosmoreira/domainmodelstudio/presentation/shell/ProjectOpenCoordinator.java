package com.marcosmoreira.domainmodelstudio.presentation.shell;

import com.marcosmoreira.domainmodelstudio.application.ApplicationServices;
import com.marcosmoreira.domainmodelstudio.application.workspace.CreateWorkspaceRequest;
import com.marcosmoreira.domainmodelstudio.application.workspace.CreateWorkspaceResult;
import com.marcosmoreira.domainmodelstudio.application.workspace.WorkspaceSupportDecision;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeDescriptor;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import javafx.stage.FileChooser;

/**
 * Coordina la apertura de proyectos .dms desde el shell.
 *
 * <p>La clase encapsula FileChooser, validación del workspace disponible y mensajes de
 * apertura. La activación de pestañas queda en el shell mediante el mismo puerto usado
 * por la creación de proyectos.</p>
 */
final class ProjectOpenCoordinator {

    private final MainShellState shellState;
    private final ApplicationServices applicationServices;
    private final ProjectCreationCoordinator.ProjectOpenTarget target;

    ProjectOpenCoordinator(
            MainShellState shellState,
            ApplicationServices applicationServices,
            ProjectCreationCoordinator.ProjectOpenTarget target
    ) {
        this.shellState = Objects.requireNonNull(shellState, "shellState");
        this.applicationServices = Objects.requireNonNull(applicationServices, "applicationServices");
        this.target = Objects.requireNonNull(target, "target");
    }

    void requestOpenProject() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Abrir proyecto Domain Model Studio");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                "Proyecto Domain Model Studio (*.dms)", "*.dms"));
        java.io.File file = chooser.showOpenDialog(null);
        if (file == null) {
            shellState.updateStatus("Apertura cancelada.");
            return;
        }
        try {
            DiagramProject project = applicationServices.openProjectUseCase().open(file.toPath());
            openLoadedProject(project, file.toPath());
            shellState.updateStatus("Proyecto abierto en una pestaña nueva desde: "
                    + file.toPath().toAbsolutePath());
        } catch (IOException | IllegalArgumentException exception) {
            shellState.updateStatus("No se pudo abrir el proyecto: " + exception.getMessage());
        }
    }

    private void openLoadedProject(DiagramProject project, Path projectFile) {
        DiagramTypeDescriptor descriptor = findDiagramTypeDescriptor(project.metadata().diagramTypeId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Tipo de proyecto no registrado en el catálogo: "
                                + project.metadata().diagramTypeId().value()));
        CreateWorkspaceResult workspaceResult = applicationServices.createWorkspaceUseCase().execute(
                new CreateWorkspaceRequest(descriptor.id(), project.metadata().title()));
        if (workspaceResult.decision() == WorkspaceSupportDecision.PRODUCT_VIEW) {
            target.openProject(project, "Proyecto abierto", false, projectFile);
            return;
        }
        if (workspaceResult.decision() == WorkspaceSupportDecision.PLANNING_VIEW) {
            target.openPlaceholder(project, descriptor, false, "Proyecto abierto", projectFile);
            return;
        }
        throw new IllegalArgumentException(workspaceResult.userMessage());
    }

    private Optional<DiagramTypeDescriptor> findDiagramTypeDescriptor(DiagramTypeId diagramTypeId) {
        return applicationServices.listDiagramTypesUseCase().execute().stream()
                .filter(type -> type.id().equals(diagramTypeId))
                .findFirst();
    }
}
