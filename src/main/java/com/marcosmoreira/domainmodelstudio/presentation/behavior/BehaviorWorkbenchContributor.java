package com.marcosmoreira.domainmodelstudio.presentation.behavior;

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

/** Aporte de diagramas de comportamiento al workbench común. */
final class BehaviorWorkbenchContributor implements DiagramWorkbenchContributor {

    private final BehaviorDiagramCenter diagramCenter;
    private final BehaviorStructurePanel structurePanel;
    private final BehaviorPropertiesPanel propertiesPanel;
    private final DiagramWorkbenchDescriptor descriptor;

    BehaviorWorkbenchContributor(BehaviorDiagramViewModel viewModel) {
        Objects.requireNonNull(viewModel, "viewModel");
        this.diagramCenter = new BehaviorDiagramCenter(viewModel);
        this.structurePanel = new BehaviorStructurePanel(viewModel, diagramCenter::refreshCanvasOnly);
        this.propertiesPanel = new BehaviorPropertiesPanel(viewModel, diagramCenter::refreshCanvasOnly);
        this.descriptor = DiagramWorkbenchDescriptor.migratedVisualDiagram(
                WorkspaceKind.BEHAVIOR_DIAGRAM,
                "Diagrama de comportamiento",
                "Diagrama visual de procesos, actividades, estados o secuencias según el tipo abierto.",
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
