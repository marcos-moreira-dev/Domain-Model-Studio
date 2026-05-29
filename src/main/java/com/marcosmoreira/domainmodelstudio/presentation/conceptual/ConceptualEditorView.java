package com.marcosmoreira.domainmodelstudio.presentation.conceptual;

import com.marcosmoreira.domainmodelstudio.domain.notation.NotationType;
import com.marcosmoreira.domainmodelstudio.presentation.canvas.DiagramCanvasViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.inspector.InspectorViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.sidebar.ModelTreeViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.workbench.DiagramWorkbenchView;
import java.util.Objects;
import java.util.function.Consumer;
import javafx.scene.Parent;

/** Vista conceptual integrada al workbench común sin sustituir todavía el canvas legacy. */
public final class ConceptualEditorView {

    private final ConceptualWorkbenchContributor contributor;
    private final DiagramWorkbenchView workbench;

    public ConceptualEditorView(
            ModelTreeViewModel modelTreeViewModel,
            DiagramCanvasViewModel canvasViewModel,
            InspectorViewModel inspectorViewModel,
            Consumer<NotationType> notationSwitchAction
    ) {
        this.contributor = new ConceptualWorkbenchContributor(
                Objects.requireNonNull(modelTreeViewModel, "modelTreeViewModel"),
                Objects.requireNonNull(canvasViewModel, "canvasViewModel"),
                Objects.requireNonNull(inspectorViewModel, "inspectorViewModel"),
                notationSwitchAction
        );
        this.workbench = new DiagramWorkbenchView(contributor);
    }

    public Parent getRoot() {
        return workbench.getRoot();
    }
}
