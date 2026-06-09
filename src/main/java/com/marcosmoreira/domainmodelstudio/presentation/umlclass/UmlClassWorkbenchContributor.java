package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

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

/** Aporte de UML Clases al workbench común de diagramas visuales. */
final class UmlClassWorkbenchContributor implements DiagramWorkbenchContributor {

    private final UmlClassDiagramCenter diagramCenter;
    private final UmlClassStructurePanel structurePanel;
    private final UmlClassPropertiesPanel propertiesPanel;
    private final DiagramWorkbenchDescriptor descriptor;

    UmlClassWorkbenchContributor(UmlClassDiagramViewModel viewModel) {
        Objects.requireNonNull(viewModel, "viewModel");
        this.diagramCenter = new UmlClassDiagramCenter(viewModel);
        this.structurePanel = new UmlClassStructurePanel(viewModel, diagramCenter::requestCanvasRefresh);
        this.propertiesPanel = new UmlClassPropertiesPanel(viewModel, diagramCenter::requestCanvasRefresh);
        this.descriptor = DiagramWorkbenchDescriptor.migratedVisualDiagram(
                WorkspaceKind.UML_CLASS_DIAGRAM,
                "UML Clases",
                "Diagrama visual de clases, paquetes, atributos, métodos y relaciones estructurales.",
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

    void centerSelection() {
        diagramCenter.centerSelection();
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

    void exportVisualAsPng(Path targetFile) throws IOException {
        diagramCenter.exportVisualAsPng(targetFile);
    }
}
