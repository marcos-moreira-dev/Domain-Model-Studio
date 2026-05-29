package com.marcosmoreira.domainmodelstudio.presentation.modulemap;

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

/** Aporte del mapa de módulos al workbench común de diagramas visuales. */
final class ModuleMapWorkbenchContributor implements DiagramWorkbenchContributor {

    private final ModuleMapDiagramCenter diagramCenter;
    private final ModuleMapStructurePanel structurePanel;
    private final ModuleMapPropertiesPanel propertiesPanel;
    private final DiagramWorkbenchDescriptor descriptor;

    ModuleMapWorkbenchContributor(ModuleMapViewModel viewModel) {
        Objects.requireNonNull(viewModel, "viewModel");
        this.diagramCenter = new ModuleMapDiagramCenter(viewModel);
        this.structurePanel = new ModuleMapStructurePanel(viewModel, diagramCenter::refreshCanvasOnly);
        this.propertiesPanel = new ModuleMapPropertiesPanel(viewModel, diagramCenter::refreshCanvasOnly);
        this.descriptor = DiagramWorkbenchDescriptor.migratedVisualDiagram(
                WorkspaceKind.MODULE_MAP_DIAGRAM,
                "Mapa de módulos",
                "Diagrama visual de módulos, submódulos y dependencias funcionales de la aplicación.",
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
