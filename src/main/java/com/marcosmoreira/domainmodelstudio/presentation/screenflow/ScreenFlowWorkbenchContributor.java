package com.marcosmoreira.domainmodelstudio.presentation.screenflow;

import com.marcosmoreira.domainmodelstudio.presentation.workbench.DiagramWorkbenchContributor;
import com.marcosmoreira.domainmodelstudio.presentation.workbench.DiagramWorkbenchDescriptor;
import com.marcosmoreira.domainmodelstudio.presentation.sidedock.SideDockModule;
import com.marcosmoreira.domainmodelstudio.presentation.sidedock.StandardSideDockModules;
import com.marcosmoreira.domainmodelstudio.presentation.workspace.WorkspaceKind;
import java.io.IOException;
import java.util.List;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import javafx.scene.Parent;

/** Aporte del flujo de pantallas al workbench común de diagramas visuales. */
final class ScreenFlowWorkbenchContributor implements DiagramWorkbenchContributor {

    private final ScreenFlowDiagramCenter diagramCenter;
    private final ScreenFlowStructurePanel structurePanel;
    private final ScreenFlowPropertiesPanel propertiesPanel;
    private final DiagramWorkbenchDescriptor descriptor;

    ScreenFlowWorkbenchContributor(ScreenFlowViewModel viewModel) {
        Objects.requireNonNull(viewModel, "viewModel");
        this.diagramCenter = new ScreenFlowDiagramCenter(viewModel);
        this.structurePanel = new ScreenFlowStructurePanel(viewModel, diagramCenter::refreshCanvasOnly);
        this.propertiesPanel = new ScreenFlowPropertiesPanel(viewModel, diagramCenter::refreshCanvasOnly);
        this.descriptor = DiagramWorkbenchDescriptor.migratedVisualDiagram(
                WorkspaceKind.SCREEN_FLOW_DIAGRAM,
                "Flujo de pantallas",
                "Diagrama visual de pantallas conectadas por acciones, rutas y condiciones de navegación.",
                "Área de trabajo visual con estructura, lienzo e inspector."
        );
    }

    @Override
    public DiagramWorkbenchDescriptor descriptor() {
        return descriptor;
    }

    @Override
    public Parent centerContent() {
        return diagramCenter.root();
    }

    @Override
    public Optional<Parent> structurePanel() {
        return Optional.of(structurePanel.root());
    }

    @Override
    public Optional<Parent> propertiesPanel() {
        return Optional.of(propertiesPanel.root());
    }


    @Override
    public List<SideDockModule> additionalSideDockModules() {
        return List.of(StandardSideDockModules.appearance(diagramCenter.appearancePanel()));
    }

    @Override
    public void onActivated() {
        diagramCenter.refitOnActivation();
    }

    void fitDiagram() {
        diagramCenter.fitDiagram();
    }

    void centerDiagram() {
        diagramCenter.centerDiagram();
    }

    void refreshCanvas() {
        diagramCenter.refreshCanvasOnly();
    }

    boolean deleteSelectedBendPoint() {
        return diagramCenter.deleteSelectedBendPoint();
    }

    void exportDiagramAsPng(Path targetFile) throws IOException {
        diagramCenter.exportDiagramAsPng(targetFile);
    }
}
