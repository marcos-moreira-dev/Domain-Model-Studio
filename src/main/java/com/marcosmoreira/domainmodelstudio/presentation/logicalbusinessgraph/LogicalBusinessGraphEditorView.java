package com.marcosmoreira.domainmodelstudio.presentation.logicalbusinessgraph;

import com.marcosmoreira.domainmodelstudio.presentation.workbench.DiagramWorkbenchView;
import java.util.Objects;
import javafx.scene.Parent;

/** Vista de producto para Grafo lógico del negocio sobre el workbench visual canónico. */
public final class LogicalBusinessGraphEditorView {

    private final LogicalBusinessGraphWorkbenchContributor contributor;
    private final DiagramWorkbenchView workbench;

    public LogicalBusinessGraphEditorView(LogicalBusinessGraphViewModel viewModel) {
        Objects.requireNonNull(viewModel, "viewModel");
        this.contributor = new LogicalBusinessGraphWorkbenchContributor(viewModel);
        this.workbench = new DiagramWorkbenchView(contributor);
        viewModel.registerPngExportAction(contributor::exportDiagramAsPng);
        viewModel.registerDiagramFitAction(contributor::fitDiagram);
        viewModel.registerDiagramCenterAction(contributor::centerDiagram);
        viewModel.registerDiagramRefreshAction(contributor::refreshCanvas);
        viewModel.registerDeleteSelectedBendPointAction(contributor::deleteSelectedBendPoint);
    }

    public Parent getRoot() {
        return workbench.getRoot();
    }

    public void refresh() {
        contributor.refreshAndFitIfNeeded();
    }

    public void refitOnActivation() {
        workbench.activate();
    }
}
