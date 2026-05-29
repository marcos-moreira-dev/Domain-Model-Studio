package com.marcosmoreira.domainmodelstudio.presentation.workbench;

import com.marcosmoreira.domainmodelstudio.presentation.sidedock.WorkbenchSideDock;
import javafx.scene.Parent;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;

/** Monta de forma uniforme el SideDock y el contenido central dentro de un SplitPane. */
final class WorkbenchSideDockLayout {

    private WorkbenchSideDockLayout() {
    }

    static void mount(
            BorderPane root,
            SplitPane splitPane,
            WorkbenchSideDock sideDock,
            Parent centerContent,
            double dividerPosition
    ) {
        splitPane.getItems().clear();
        splitPane.getItems().add(sideDock.getRoot());
        splitPane.getItems().add(centerContent);
        SplitPane.setResizableWithParent(sideDock.getRoot(), false);
        SplitPane.setResizableWithParent(centerContent, true);
        root.setCenter(splitPane);
        splitPane.setDividerPositions(dividerPosition);
    }
}
