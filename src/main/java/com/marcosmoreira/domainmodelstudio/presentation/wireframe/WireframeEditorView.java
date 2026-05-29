package com.marcosmoreira.domainmodelstudio.presentation.wireframe;

import com.marcosmoreira.domainmodelstudio.presentation.workbench.DiagramWorkbenchView;
import java.util.Objects;
import javafx.scene.Parent;

/** Vista de producto para wireframes administrativos editables. */
public final class WireframeEditorView {

    private final WireframeWorkbenchContributor contributor;
    private final DiagramWorkbenchView workbench;

    public WireframeEditorView(WireframeViewModel viewModel) {
        Objects.requireNonNull(viewModel, "viewModel");
        this.contributor = new WireframeWorkbenchContributor(viewModel);
        this.workbench = new DiagramWorkbenchView(contributor);
        viewModel.registerPngExportAction(contributor::exportWireframesAsPng);
        viewModel.registerDiagramFitAction(contributor::fitDiagram);
        viewModel.registerDiagramCenterAction(contributor::centerDiagram);
        viewModel.registerDiagramRefreshAction(contributor::refreshCanvas);
        viewModel.registerDeleteSelectedBendPointAction(contributor::deleteSelectedBendPoint);
    }

    public Parent getRoot() {
        return workbench.getRoot();
    }
}
