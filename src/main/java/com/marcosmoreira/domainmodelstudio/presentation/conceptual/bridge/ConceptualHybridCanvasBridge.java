package com.marcosmoreira.domainmodelstudio.presentation.conceptual.bridge;

import com.marcosmoreira.domainmodelstudio.presentation.canvas.DiagramCanvasViewModel;
import java.util.Objects;

/**
 * Fachada de migración híbrida del canvas conceptual.
 *
 * <p>Agrupa bridges de selección, comandos, layout y validación para que la carcasa común pueda
 * evolucionar sin reemplazar todavía el canvas ni los renderizadores conceptuales actuales.</p>
 */
public final class ConceptualHybridCanvasBridge {

    private final ConceptualSelectionBridge selection;
    private final ConceptualCanvasCommandBridge commands;
    private final ConceptualLayoutBridge layout;
    private final ConceptualValidationBridge validation;

    public ConceptualHybridCanvasBridge(DiagramCanvasViewModel canvasViewModel) {
        Objects.requireNonNull(canvasViewModel, "canvasViewModel");
        this.selection = new ConceptualSelectionBridge(canvasViewModel);
        this.commands = new ConceptualCanvasCommandBridge(canvasViewModel, selection);
        this.layout = new ConceptualLayoutBridge(canvasViewModel, selection);
        this.validation = new ConceptualValidationBridge(canvasViewModel);
    }

    public ConceptualSelectionBridge selection() {
        return selection;
    }

    public ConceptualCanvasCommandBridge commands() {
        return commands;
    }

    public ConceptualLayoutBridge layout() {
        return layout;
    }

    public ConceptualValidationBridge validation() {
        return validation;
    }
}
