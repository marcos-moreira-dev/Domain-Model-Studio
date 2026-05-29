package com.marcosmoreira.domainmodelstudio.presentation.freegraph;

import com.marcosmoreira.domainmodelstudio.presentation.sidedock.SideDockModule;
import com.marcosmoreira.domainmodelstudio.presentation.sidedock.StandardSideDockModules;
import com.marcosmoreira.domainmodelstudio.presentation.workbench.DiagramWorkbenchContributor;
import com.marcosmoreira.domainmodelstudio.presentation.workbench.DiagramWorkbenchDescriptor;
import com.marcosmoreira.domainmodelstudio.presentation.workspace.WorkspaceKind;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javafx.scene.Parent;

/** Aporte de Grafo libre al workbench visual común con SideDock contextual. */
final class FreeGraphWorkbenchContributor implements DiagramWorkbenchContributor {

    private final FreeGraphDiagramCenter diagramCenter;
    private final FreeGraphStructurePanel structurePanel;
    private final FreeGraphPropertiesPanel propertiesPanel;
    private final DiagramWorkbenchDescriptor descriptor;

    FreeGraphWorkbenchContributor(FreeGraphViewModel viewModel) {
        Objects.requireNonNull(viewModel, "viewModel");
        this.diagramCenter = new FreeGraphDiagramCenter(viewModel);
        this.structurePanel = new FreeGraphStructurePanel(viewModel, diagramCenter::refreshCanvasOnly);
        this.propertiesPanel = new FreeGraphPropertiesPanel(viewModel, diagramCenter::refreshCanvasOnly);
        this.descriptor = DiagramWorkbenchDescriptor.migratedVisualDiagram(
                WorkspaceKind.FREE_GRAPH_DIAGRAM,
                "Grafo libre",
                "Diagrama visual de nodos y relaciones para modelar dependencias, asociaciones y mapas de conocimiento.",
                "Área de trabajo visual con estructura, lienzo, inspector y apariencia contextual."
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

    void refreshAndFitIfNeeded() {
        diagramCenter.refreshAndFitIfNeeded();
    }

    void refitOnActivation() {
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
