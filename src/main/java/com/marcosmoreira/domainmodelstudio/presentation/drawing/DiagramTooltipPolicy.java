package com.marcosmoreira.domainmodelstudio.presentation.drawing;

import javafx.scene.control.Control;
import javafx.scene.control.Tooltip;

/** Política centralizada para tooltips de texto completo en el canvas. */
public final class DiagramTooltipPolicy {

    public void attachIfUseful(Control control, String fullText, boolean force) {
        if (control == null) {
            return;
        }
        String clean = fullText == null ? "" : fullText.strip();
        if (clean.isBlank()) {
            control.setTooltip(null);
            return;
        }
        if (force) {
            control.setTooltip(new Tooltip(clean));
        }
    }
}
