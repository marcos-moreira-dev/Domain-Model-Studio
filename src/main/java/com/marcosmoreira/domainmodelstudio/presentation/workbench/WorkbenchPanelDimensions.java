package com.marcosmoreira.domainmodelstudio.presentation.workbench;

import javafx.scene.layout.Region;

/** Rangos de ancho para paneles laterales del área de trabajo. */
public record WorkbenchPanelDimensions(
        double minimumWidth,
        double preferredWidth,
        double maximumWidth
) {

    private static final WorkbenchPanelDimensions STRUCTURE = new WorkbenchPanelDimensions(230.0, 300.0, 460.0);
    private static final WorkbenchPanelDimensions PROPERTIES = new WorkbenchPanelDimensions(320.0, 390.0, 560.0);

    public WorkbenchPanelDimensions {
        if (minimumWidth <= 0 || preferredWidth <= 0 || maximumWidth <= 0) {
            throw new IllegalArgumentException("Los anchos del panel deben ser positivos.");
        }
        if (minimumWidth > preferredWidth || preferredWidth > maximumWidth) {
            throw new IllegalArgumentException("Los anchos del panel deben cumplir mínimo <= preferido <= máximo.");
        }
    }

    public static WorkbenchPanelDimensions forSlot(WorkbenchPanelSlot slot) {
        return slot == WorkbenchPanelSlot.STRUCTURE ? STRUCTURE : PROPERTIES;
    }

    public void applyTo(Region region) {
        region.setMinWidth(minimumWidth);
        region.setPrefWidth(preferredWidth);
        region.setMaxWidth(maximumWidth);
    }

    public double initialDividerPosition() {
        return preferredWidth / maximumWidth;
    }
}
