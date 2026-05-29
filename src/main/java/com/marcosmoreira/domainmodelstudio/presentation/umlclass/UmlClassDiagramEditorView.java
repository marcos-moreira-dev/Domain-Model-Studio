package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import com.marcosmoreira.domainmodelstudio.presentation.workbench.DiagramWorkbenchView;
import java.util.Objects;
import javafx.scene.Parent;

/** Vista de producto para UML Clases montada sobre el workbench canónico. */
public final class UmlClassDiagramEditorView {

    private final UmlClassWorkbenchContributor contributor;
    private final DiagramWorkbenchView workbench;

    public UmlClassDiagramEditorView(UmlClassDiagramViewModel viewModel) {
        Objects.requireNonNull(viewModel, "viewModel");
        this.contributor = new UmlClassWorkbenchContributor(viewModel);
        this.workbench = new DiagramWorkbenchView(contributor);
        viewModel.registerPngExportAction(contributor::exportVisualAsPng);
        viewModel.registerDiagramFitAction(contributor::fitDiagram);
        viewModel.registerDiagramCenterAction(contributor::centerDiagram);
        viewModel.registerDiagramRefreshAction(contributor::refreshCanvas);
        viewModel.registerDeleteSelectedBendPointAction(contributor::deleteSelectedBendPoint);
        viewModel.registerSelectionCenterAction(contributor::centerSelection);
    }

    public Parent getRoot() {
        return workbench.getRoot();
    }
}
