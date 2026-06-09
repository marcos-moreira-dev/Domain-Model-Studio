package com.marcosmoreira.domainmodelstudio.presentation.screenflow;

import com.marcosmoreira.domainmodelstudio.presentation.workbench.DiagramWorkbenchView;
import java.util.Objects;
import javafx.scene.Parent;

/** Vista de producto para el flujo visual de pantallas administrativas. */
public final class ScreenFlowEditorView {

    private final ScreenFlowWorkbenchContributor contributor;
    private final DiagramWorkbenchView workbench;

    public ScreenFlowEditorView(ScreenFlowViewModel viewModel) {
        Objects.requireNonNull(viewModel, "viewModel");
        this.contributor = new ScreenFlowWorkbenchContributor(viewModel);
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
}
