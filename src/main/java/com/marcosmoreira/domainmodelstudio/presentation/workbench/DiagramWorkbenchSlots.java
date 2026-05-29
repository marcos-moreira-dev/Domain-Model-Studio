package com.marcosmoreira.domainmodelstudio.presentation.workbench;

import java.util.Objects;
import java.util.Optional;
import javafx.scene.Parent;

/** Piezas ya resueltas que un workbench puede montar o exponer al shell. */
public record DiagramWorkbenchSlots(
        DiagramWorkbenchDescriptor descriptor,
        Optional<Parent> header,
        Optional<Parent> structurePanel,
        Parent center,
        Optional<Parent> propertiesPanel
) {

    public DiagramWorkbenchSlots {
        Objects.requireNonNull(descriptor, "descriptor");
        header = header == null ? Optional.empty() : header;
        structurePanel = structurePanel == null ? Optional.empty() : structurePanel;
        Objects.requireNonNull(center, "center");
        propertiesPanel = propertiesPanel == null ? Optional.empty() : propertiesPanel;
    }

    public boolean hasSidePanels() {
        return structurePanel.isPresent() || propertiesPanel.isPresent();
    }
}
