package com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness;

import com.marcosmoreira.domainmodelstudio.presentation.sidedock.SideDockModule;
import com.marcosmoreira.domainmodelstudio.presentation.sidedock.SideDockModuleId;
import com.marcosmoreira.domainmodelstudio.presentation.sidedock.StaticSideDockModule;
import com.marcosmoreira.domainmodelstudio.presentation.workbench.StructuredWorkbenchDescriptor;
import com.marcosmoreira.domainmodelstudio.presentation.workbench.StructuredWorkbenchView;
import com.marcosmoreira.domainmodelstudio.presentation.workspace.WorkspaceKind;
import java.util.List;
import java.util.Objects;
import javafx.scene.Parent;

/** Vista documental MVP para el proyecto de Levantamiento lógico. */
public final class LogicalBusinessEditorView {

    private final StructuredWorkbenchView workbench;

    public LogicalBusinessEditorView(LogicalBusinessViewModel viewModel) {
        Objects.requireNonNull(viewModel, "viewModel");
        LogicalBusinessStructurePanel structurePanel = new LogicalBusinessStructurePanel(viewModel);
        LogicalBusinessElementsPanel elementsPanel = new LogicalBusinessElementsPanel(viewModel);
        LogicalBusinessEntitiesPanel entitiesPanel = new LogicalBusinessEntitiesPanel(viewModel);
        LogicalBusinessPropertiesPanel propertiesPanel = new LogicalBusinessPropertiesPanel(viewModel);
        LogicalBusinessValidationPanel validationPanel = new LogicalBusinessValidationPanel(viewModel);
        LogicalBusinessTraceabilityPanel traceabilityPanel = new LogicalBusinessTraceabilityPanel(viewModel);
        LogicalBusinessHelpPanel helpPanel = new LogicalBusinessHelpPanel(viewModel);
        LogicalBusinessDocumentView documentView = new LogicalBusinessDocumentView(viewModel);

        StructuredWorkbenchDescriptor descriptor = StructuredWorkbenchDescriptor.document(
                WorkspaceKind.LOGICAL_BUSINESS_DOCUMENT,
                "logical-business-workbench",
                "Levantamiento lógico",
                "Expediente documental estructurado para entrevistas, reglas, acciones, entidades, preguntas y trazas internas.",
                "Documento central con SideBar modular; no usa canvas ni paneles del modelo conceptual."
        );
        this.workbench = new StructuredWorkbenchView(
                descriptor,
                structurePanel.root(),
                documentView.root(),
                propertiesPanel.root(),
                List.of(
                        StaticSideDockModule.of(SideDockModuleId.PALETTE, "Elementos lógicos", elementsPanel.root()),
                        StaticSideDockModule.of(SideDockModuleId.ENTITIES, "Entidades y relaciones", entitiesPanel.root()),
                        StaticSideDockModule.of(SideDockModuleId.VALIDATION, "Validación", validationPanel.root()),
                        module(SideDockModuleId.TRACEABILITY, "Trazas internas", traceabilityPanel.root()),
                        StaticSideDockModule.of(SideDockModuleId.HELP, "Ayuda y glosario", helpPanel.root())
                ));
        viewModel.requestedSideDockModuleProperty().addListener((observable, previous, requested) -> {
            if (requested != null) {
                this.workbench.activateSideDockModule(requested);
            }
        });
    }

    public Parent getRoot() {
        return workbench.getRoot();
    }

    private SideDockModule module(SideDockModuleId id, String title, Parent root) {
        return StaticSideDockModule.of(id, title, root);
    }
}
