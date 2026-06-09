package com.marcosmoreira.domainmodelstudio.presentation.architecture;

import com.marcosmoreira.domainmodelstudio.presentation.workbench.DiagramWorkbenchView;
import java.util.Objects;
import javafx.scene.Parent;

/** Vista de producto para C4 y despliegue técnico sobre workbench canónico. */
public final class ArchitectureDiagramEditorView {

    private final ArchitectureWorkbenchContributor contributor;
    private final DiagramWorkbenchView workbench;

    public ArchitectureDiagramEditorView(ArchitectureDiagramViewModel viewModel) {
        Objects.requireNonNull(viewModel, "viewModel");
        this.contributor = new ArchitectureWorkbenchContributor(viewModel);
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
