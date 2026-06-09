package com.marcosmoreira.domainmodelstudio.presentation.freegraph;

import com.marcosmoreira.domainmodelstudio.presentation.workbench.DiagramWorkbenchView;
import java.util.Objects;
import javafx.scene.Parent;

/** Vista de producto para Grafo libre sobre el workbench visual canónico. */
public final class FreeGraphEditorView {

    private final FreeGraphWorkbenchContributor contributor;
    private final DiagramWorkbenchView workbench;

    public FreeGraphEditorView(FreeGraphViewModel viewModel) {
        Objects.requireNonNull(viewModel, "viewModel");
        this.contributor = new FreeGraphWorkbenchContributor(viewModel);
        this.workbench = new DiagramWorkbenchView(contributor);
        viewModel.registerPngExportAction(contributor::exportDiagramAsPng);
        viewModel.registerDiagramFitAction(contributor::fitDiagram);
        viewModel.registerDiagramCenterAction(contributor::centerDiagram);
        viewModel.registerDiagramRefreshAction(contributor::refreshCanvas);
        viewModel.registerDeleteSelectedBendPointAction(contributor::deleteSelectedBendPoint);
        viewModel.registerVisualCommentToolAction(contributor::activateVisualCommentTool);
        viewModel.registerVisualCommentLayerOrderAction(contributor::reorderSelectedVisualComment);
        viewModel.registerVisualCommentSizeAction(contributor::resizeSelectedVisualComment);
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
