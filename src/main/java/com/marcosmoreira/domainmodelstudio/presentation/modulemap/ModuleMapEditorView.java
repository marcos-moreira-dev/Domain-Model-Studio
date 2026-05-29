package com.marcosmoreira.domainmodelstudio.presentation.modulemap;

import com.marcosmoreira.domainmodelstudio.presentation.workbench.DiagramWorkbenchView;
import java.util.Objects;
import javafx.scene.Parent;

/** Vista de producto para el mapa de módulos administrativos. */
public final class ModuleMapEditorView {

    private final ModuleMapWorkbenchContributor contributor;
    private final DiagramWorkbenchView workbench;

    public ModuleMapEditorView(ModuleMapViewModel viewModel) {
        Objects.requireNonNull(viewModel, "viewModel");
        this.contributor = new ModuleMapWorkbenchContributor(viewModel);
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
