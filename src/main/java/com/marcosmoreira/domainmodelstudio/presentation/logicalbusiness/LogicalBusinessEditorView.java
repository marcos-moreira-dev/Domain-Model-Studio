package com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness;

import com.marcosmoreira.domainmodelstudio.presentation.exportable.ProjectExportPanel;
import com.marcosmoreira.domainmodelstudio.presentation.sidedock.SideDockModule;
import com.marcosmoreira.domainmodelstudio.presentation.sidedock.SideDockModuleId;
import com.marcosmoreira.domainmodelstudio.presentation.sidedock.StaticSideDockModule;
import com.marcosmoreira.domainmodelstudio.presentation.workbench.StructuredWorkbenchDescriptor;
import com.marcosmoreira.domainmodelstudio.presentation.workbench.StructuredWorkbenchView;
import com.marcosmoreira.domainmodelstudio.presentation.workbench.WorkspaceHeaderState;
import com.marcosmoreira.domainmodelstudio.presentation.workspace.WorkspaceKind;
import java.util.List;
import java.util.Objects;
import javafx.scene.Parent;

/** Vista documental MVP para el proyecto de Levantamiento lógico. */
public final class LogicalBusinessEditorView {

    private final StructuredWorkbenchView workbench;

    public LogicalBusinessEditorView(LogicalBusinessViewModel viewModel) {
        this(viewModel, () -> { }, () -> { });
    }

    public LogicalBusinessEditorView(
            LogicalBusinessViewModel viewModel,
            Runnable exportPdfAction,
            Runnable exportMarkdownAction
    ) {
        Objects.requireNonNull(viewModel, "viewModel");
        Objects.requireNonNull(exportPdfAction, "exportPdfAction");
        Objects.requireNonNull(exportMarkdownAction, "exportMarkdownAction");
        LogicalBusinessStructurePanel structurePanel = new LogicalBusinessStructurePanel(viewModel);
        LogicalBusinessElementsPanel elementsPanel = new LogicalBusinessElementsPanel(viewModel);
        LogicalBusinessEntitiesPanel entitiesPanel = new LogicalBusinessEntitiesPanel(viewModel);
        LogicalBusinessPropertiesPanel propertiesPanel = new LogicalBusinessPropertiesPanel(viewModel);
        LogicalBusinessValidationPanel validationPanel = new LogicalBusinessValidationPanel(viewModel);
        LogicalBusinessTraceabilityPanel traceabilityPanel = new LogicalBusinessTraceabilityPanel(viewModel);
        ProjectExportPanel exportPanel = new ProjectExportPanel(
                "Levantamiento logico",
                exportPdfAction,
                exportMarkdownAction,
                viewModel.currentProjectProperty().isNull());
        LogicalBusinessHelpPanel helpPanel = new LogicalBusinessHelpPanel(viewModel);
        LogicalBusinessDocumentView documentView = new LogicalBusinessDocumentView(viewModel);

        StructuredWorkbenchDescriptor descriptor = new StructuredWorkbenchDescriptor(
                WorkspaceKind.LOGICAL_BUSINESS_DOCUMENT,
                "logical-business-workbench",
                WorkspaceHeaderState.visible(
                        "Levantamiento lógico",
                        "Expediente documental estructurado para entrevistas, reglas, acciones, entidades, preguntas e impacto interno.",
                        "Documento central con SideBar modular; no usa canvas ni paneles del modelo conceptual.",
                        true),
                "Estructura",
                "Ficha rápida"
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
                        module(SideDockModuleId.TRACEABILITY, "Impacto y dependencias", traceabilityPanel.root()),
                        StaticSideDockModule.of(SideDockModuleId.EXPORT, "Exportar", exportPanel.root()),
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
