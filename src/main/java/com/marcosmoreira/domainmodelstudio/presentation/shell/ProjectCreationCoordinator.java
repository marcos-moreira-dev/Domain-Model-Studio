package com.marcosmoreira.domainmodelstudio.presentation.shell;

import com.marcosmoreira.domainmodelstudio.application.ApplicationServices;
import com.marcosmoreira.domainmodelstudio.application.workspace.CreateWorkspaceRequest;
import com.marcosmoreira.domainmodelstudio.application.workspace.CreateWorkspaceResult;
import com.marcosmoreira.domainmodelstudio.application.workspace.WorkspaceSupportDecision;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeDescriptor;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.presentation.dialogs.NewProjectDialog;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import javafx.scene.control.Alert;

/**
 * Coordina la creación de proyectos desde el shell.
 *
 * <p>El coordinador conserva en presentation las decisiones de escritorio como el
 * diálogo de nuevo proyecto y los mensajes visibles. La creación real se delega a
 * application y a {@link NewProjectFactory}; la apertura en pestañas se devuelve al
 * shell mediante un puerto pequeño para no mezclar creación, sesiones y UI en una
 * sola clase.</p>
 */
final class ProjectCreationCoordinator {

    interface ProjectOpenTarget {
        default void openProject(DiagramProject project, String statusLabel, boolean dirty) {
            openProject(project, statusLabel, dirty, null);
        }

        void openProject(DiagramProject project, String statusLabel, boolean dirty, Path projectFile);

        void openPlaceholder(
                DiagramProject project,
                DiagramTypeDescriptor descriptor,
                boolean dirty,
                String statusLabel,
                Path projectFile
        );
    }

    private final MainShellState shellState;
    private final ApplicationServices applicationServices;
    private final ProjectSessionCoordinator projectSessionCoordinator;
    private final NewProjectFactory newProjectFactory;
    private final ProjectOpenTarget target;

    ProjectCreationCoordinator(
            MainShellState shellState,
            ApplicationServices applicationServices,
            ProjectSessionCoordinator projectSessionCoordinator,
            NewProjectFactory newProjectFactory,
            ProjectOpenTarget target
    ) {
        this.shellState = Objects.requireNonNull(shellState, "shellState");
        this.applicationServices = Objects.requireNonNull(applicationServices, "applicationServices");
        this.projectSessionCoordinator = Objects.requireNonNull(projectSessionCoordinator, "projectSessionCoordinator");
        this.newProjectFactory = Objects.requireNonNull(newProjectFactory, "newProjectFactory");
        this.target = Objects.requireNonNull(target, "target");
    }

    void requestNewProject() {
        Optional<DiagramTypeDescriptor> selectedType = NewProjectDialog.showAndWait(
                applicationServices.listDiagramCategoriesUseCase().execute(),
                applicationServices.listDiagramTypesUseCase().execute()
        );
        if (selectedType.isEmpty()) {
            shellState.updateStatus("Nuevo proyecto cancelado.");
            return;
        }
        DiagramTypeDescriptor descriptor = selectedType.get();
        CreateWorkspaceResult workspaceResult = applicationServices.createWorkspaceUseCase().execute(
                new CreateWorkspaceRequest(descriptor.id(), descriptor.displayName() + " nuevo"));
        if (workspaceResult.decision() == WorkspaceSupportDecision.PLANNING_VIEW) {
            openPlaceholder(descriptor);
            shellState.updateStatus(workspaceResult.userMessage());
            return;
        }
        if (workspaceResult.decision() == WorkspaceSupportDecision.UNSUPPORTED) {
            showUnsupportedDiagramType(workspaceResult);
            shellState.updateStatus(workspaceResult.userMessage());
            return;
        }
        createSupportedProject(descriptor);
    }

    private void createSupportedProject(DiagramTypeDescriptor descriptor) {
        if (descriptor.id().equals(DiagramTypeId.DATA_DICTIONARY)) {
            openProject(newProjectFactory.createDataDictionary(descriptor, nextTabNumber()),
                    "Diccionario de datos nuevo",
                    "Diccionario de datos creado. Agrega entidades y campos desde la barra contextual.");
            return;
        }
        if (descriptor.id().equals(DiagramTypeId.ADMIN_MODULE_MAP)) {
            openProject(newProjectFactory.createModuleMap(descriptor, nextTabNumber()),
                    "Mapa de módulos nuevo",
                    "Mapa de módulos creado. Agrega módulos, submódulos y dependencias desde la barra contextual.");
            return;
        }
        if (descriptor.id().equals(DiagramTypeId.UML_CLASS)) {
            openProject(newProjectFactory.createUmlClassDiagram(descriptor, nextTabNumber()),
                    "UML Clases nuevo",
                    "UML Clases creado. Agrega módulos, clases y relaciones desde la barra contextual.");
            return;
        }
        if (descriptor.id().equals(DiagramTypeId.ROLES_PERMISSIONS_MAP)) {
            openProject(newProjectFactory.createRolesPermissions(descriptor, nextTabNumber()),
                    "Roles y permisos nuevo",
                    "Roles y permisos creado. Agrega roles, permisos y asignaciones desde la barra contextual.");
            return;
        }
        if (descriptor.id().equals(DiagramTypeId.SCREEN_FLOW)) {
            openProject(newProjectFactory.createScreenFlow(descriptor, nextTabNumber()),
                    "Flujo de pantallas nuevo",
                    "Flujo de pantallas creado. Agrega pantallas y transiciones desde la barra contextual.");
            return;
        }
        if (descriptor.id().equals(DiagramTypeId.ADMIN_WIREFRAMES)) {
            openProject(newProjectFactory.createWireframe(descriptor, nextTabNumber()),
                    "Wireframes administrativos nuevo",
                    "Wireframes creado. Agrega pantallas y componentes desde la barra contextual.");
            return;
        }
        if (descriptor.id().equals(DiagramTypeId.FREE_GRAPH)) {
            openProject(newProjectFactory.createFreeGraph(descriptor, nextTabNumber()),
                    "Grafo libre nuevo",
                    "Grafo libre creado. Usa Agregar nodo o Agregar relación para dibujar sobre el lienzo.");
            return;
        }
        if (descriptor.id().equals(DiagramTypeId.LOGICAL_BUSINESS_INTAKE)) {
            openProject(newProjectFactory.createLogicalBusinessIntake(descriptor, nextTabNumber()),
                    "Levantamiento lógico nuevo",
                    "Levantamiento lógico creado. Usa el documento central y el SideDock modular para revisar estructura, entidades y validación.");
            return;
        }
        if (descriptor.id().equals(DiagramTypeId.LOGICAL_BUSINESS_GRAPH)) {
            openProject(newProjectFactory.createLogicalBusinessGraph(descriptor, nextTabNumber()),
                    "Grafo lógico del negocio nuevo",
                    "Grafo lógico creado. Visualiza macroflujos, flujos, casos de uso y reglas con IDs compartidos.");
            return;
        }
        if (newProjectFactory.supportsBehaviorDiagram(descriptor.id())) {
            openProject(newProjectFactory.createBehaviorDiagram(descriptor, nextTabNumber()),
                    descriptor.displayName() + " nuevo",
                    descriptor.displayName() + " creado. Agrega elementos y relaciones desde la barra contextual.");
            return;
        }
        if (newProjectFactory.supportsArchitectureDiagram(descriptor.id())) {
            openProject(newProjectFactory.createArchitectureDiagram(descriptor, nextTabNumber()),
                    descriptor.displayName() + " nuevo",
                    descriptor.displayName() + " creado. Agrega elementos y relaciones desde la barra contextual.");
            return;
        }
        DiagramProject project = newProjectFactory.createConceptualFallback(descriptor, nextTabNumber());
        target.openProject(project, "Proyecto nuevo", true);
        shellState.updateStatus(descriptor.displayName()
                + " creado en una pestaña nueva. Puedes importar un modelo Markdown o guardarlo.");
    }

    private void openPlaceholder(DiagramTypeDescriptor descriptor) {
        DiagramProject project = newProjectFactory.createPlaceholderProject(descriptor, nextTabNumber());
        target.openPlaceholder(project, descriptor, false, "Guía de preparación", null);
    }

    private void openProject(DiagramProject project, String statusLabel, String statusMessage) {
        target.openProject(project, statusLabel, true);
        shellState.updateStatus(statusMessage);
    }

    private int nextTabNumber() {
        return projectSessionCoordinator.nextProjectTabNumber();
    }

    private void showUnsupportedDiagramType(CreateWorkspaceResult result) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Tipo de proyecto no disponible");
        alert.setHeaderText(result.diagramType().displayName() + " no está disponible en esta versión.");
        alert.setContentText(result.userMessage());
        alert.showAndWait();
    }
}
