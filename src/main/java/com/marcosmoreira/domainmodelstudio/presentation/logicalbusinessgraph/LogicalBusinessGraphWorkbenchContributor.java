package com.marcosmoreira.domainmodelstudio.presentation.logicalbusinessgraph;

import com.marcosmoreira.domainmodelstudio.presentation.sidedock.SideDockModule;
import com.marcosmoreira.domainmodelstudio.presentation.sidedock.SideDockModuleId;
import com.marcosmoreira.domainmodelstudio.presentation.sidedock.StaticSideDockModule;
import com.marcosmoreira.domainmodelstudio.presentation.sidedock.StandardSideDockModules;
import com.marcosmoreira.domainmodelstudio.presentation.workbench.DiagramWorkbenchContributor;
import com.marcosmoreira.domainmodelstudio.presentation.workbench.DiagramWorkbenchDescriptor;
import com.marcosmoreira.domainmodelstudio.presentation.workspace.WorkspaceKind;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import javafx.scene.Parent;

/**
 * Aporte del Grafo lógico del negocio al workbench visual común.
 *
 * <p>Declara qué ve el usuario en el centro del workspace y qué módulos aparecen en el SideDock:
 * estructura, propiedades, leyenda, apariencia y ayuda operativa. Esta clase permite estudiar
 * cómo un tipo propio se enchufa en la carcasa transversal sin tocar el modelo conceptual.</p>
 */
final class LogicalBusinessGraphWorkbenchContributor implements DiagramWorkbenchContributor {

    private final LogicalBusinessGraphDiagramCenter diagramCenter;
    private final LogicalBusinessGraphStructurePanel structurePanel;
    private final LogicalBusinessGraphPropertiesPanel propertiesPanel;
    private final LogicalBusinessGraphLegendPanel legendPanel = new LogicalBusinessGraphLegendPanel();
    private final DiagramWorkbenchDescriptor descriptor;

    LogicalBusinessGraphWorkbenchContributor(LogicalBusinessGraphViewModel viewModel) {
        Objects.requireNonNull(viewModel, "viewModel");
        this.diagramCenter = new LogicalBusinessGraphDiagramCenter(viewModel);
        this.structurePanel = new LogicalBusinessGraphStructurePanel(viewModel, diagramCenter::refreshCanvasOnly);
        this.propertiesPanel = new LogicalBusinessGraphPropertiesPanel(viewModel, diagramCenter::refreshCanvasOnly);
        this.descriptor = DiagramWorkbenchDescriptor.migratedVisualDiagram(
                WorkspaceKind.LOGICAL_BUSINESS_GRAPH_DIAGRAM,
                "Grafo lógico del negocio",
                "Vista visual derivada del levantamiento lógico: macroflujos, microflujos, casos de uso y trazabilidad lógica.",
                "Canvas visual semántico con nodos tipados, relaciones lógicas y movimiento transversal."
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
    public java.util.Optional<Parent> structurePanel() {
        return java.util.Optional.of(structurePanel.root());
    }

    @Override
    public java.util.Optional<Parent> propertiesPanel() {
        return java.util.Optional.of(propertiesPanel.root());
    }

    @Override
    public List<SideDockModule> additionalSideDockModules() {
        return List.of(
                StaticSideDockModule.of(SideDockModuleId.COMPONENTS, "Leyenda", legendPanel.root()),
                StandardSideDockModules.appearance(diagramCenter.appearancePanel())
        );
    }

    @Override
    public void onActivated() {
        diagramCenter.refitOnActivation();
    }

    void refreshAndFitIfNeeded() {
        diagramCenter.refreshAndFitIfNeeded();
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
