package com.marcosmoreira.domainmodelstudio.presentation.wireframe;

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

/** Aporte de wireframes administrativos al workbench común de diagramas visuales. */
final class WireframeWorkbenchContributor implements DiagramWorkbenchContributor {

    private final WireframeDiagramCenter diagramCenter;
    private final WireframeStructurePanel structurePanel;
    private final WireframePropertiesPanel propertiesPanel;
    private final DiagramWorkbenchDescriptor descriptor;

    WireframeWorkbenchContributor(WireframeViewModel viewModel) {
        Objects.requireNonNull(viewModel, "viewModel");
        this.diagramCenter = new WireframeDiagramCenter(viewModel);
        this.structurePanel = new WireframeStructurePanel(viewModel, diagramCenter::refreshCanvasOnly);
        this.propertiesPanel = new WireframePropertiesPanel(viewModel, diagramCenter::refreshCanvasOnly);
        this.descriptor = DiagramWorkbenchDescriptor.migratedVisualDiagram(
                WorkspaceKind.WIREFRAME_DIAGRAM,
                "Wireframes administrativos",
                "Maquetas estructurales de pantallas, componentes, tablas, formularios, acciones y estados.",
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

    void exportWireframesAsPng(Path targetFile) throws IOException {
        diagramCenter.exportWireframesAsPng(targetFile);
    }
}
