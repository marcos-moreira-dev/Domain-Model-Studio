package com.marcosmoreira.domainmodelstudio.presentation.architecture;

import com.marcosmoreira.domainmodelstudio.application.visual.VisualLayerOrderCommand;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualNodeSizeCommand;
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

/** Aporte de C4/despliegue técnico al workbench común. */
final class ArchitectureWorkbenchContributor implements DiagramWorkbenchContributor {

    private final ArchitectureDiagramCenter diagramCenter;
    private final ArchitectureStructurePanel structurePanel;
    private final ArchitecturePropertiesPanel propertiesPanel;
    private final DiagramWorkbenchDescriptor descriptor;

    ArchitectureWorkbenchContributor(ArchitectureDiagramViewModel viewModel) {
        Objects.requireNonNull(viewModel, "viewModel");
        this.diagramCenter = new ArchitectureDiagramCenter(viewModel);
        this.structurePanel = new ArchitectureStructurePanel(viewModel, diagramCenter::refreshCanvasOnly);
        this.propertiesPanel = new ArchitecturePropertiesPanel(viewModel, diagramCenter::refreshCanvasOnly);
        this.descriptor = DiagramWorkbenchDescriptor.migratedVisualDiagram(
                WorkspaceKind.ARCHITECTURE_DIAGRAM,
                "Diagrama de arquitectura",
                "Diagrama visual C4 o de despliegue técnico con elementos, tecnologías y relaciones.",
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

    void activateVisualCommentTool() {
        diagramCenter.activateVisualCommentTool();
    }

    boolean reorderSelectedVisualComment(VisualLayerOrderCommand command) {
        return diagramCenter.reorderSelectedVisualComment(command);
    }

    boolean resizeSelectedVisualComment(VisualNodeSizeCommand command) {
        return diagramCenter.resizeSelectedVisualComment(command);
    }

    void exportDiagramAsPng(Path targetFile) throws IOException {
        diagramCenter.exportDiagramAsPng(targetFile);
    }
}
