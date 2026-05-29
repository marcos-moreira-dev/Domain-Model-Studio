package com.marcosmoreira.domainmodelstudio.presentation.behavior;

import com.marcosmoreira.domainmodelstudio.presentation.workbench.DiagramWorkbenchView;
import java.util.Objects;
import javafx.scene.Parent;

/** Vista de producto para comportamiento, procesos y UML Secuencia sobre workbench canónico. */
public final class BehaviorDiagramEditorView {

    private final BehaviorWorkbenchContributor contributor;
    private final DiagramWorkbenchView workbench;

    public BehaviorDiagramEditorView(BehaviorDiagramViewModel viewModel) {
        Objects.requireNonNull(viewModel, "viewModel");
        this.contributor = new BehaviorWorkbenchContributor(viewModel);
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
}
