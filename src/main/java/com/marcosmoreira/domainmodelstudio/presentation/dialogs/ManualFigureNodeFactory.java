package com.marcosmoreira.domainmodelstudio.presentation.dialogs;

import com.marcosmoreira.domainmodelstudio.application.theory.TheoryFigureReference;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/** Crea figuras didácticas simples para la referencia académica teórica. */
final class ManualFigureNodeFactory {

    private static final double WIDTH = 470;
    private static final double HEIGHT = 170;

    private ManualFigureNodeFactory() {
    }

    static Node create(TheoryFigureReference reference) {
        Pane canvas = new Pane();
        canvas.getStyleClass().add("manual-figure-canvas");
        canvas.setPrefSize(WIDTH, HEIGHT);
        canvas.setMinSize(WIDTH, HEIGHT);
        canvas.setMaxSize(WIDTH, HEIGHT);

        draw(reference.figureId(), canvas);

        Label caption = new Label(reference.caption());
        caption.getStyleClass().add("manual-figure-caption");
        caption.setWrapText(true);

        VBox wrapper = new VBox(6, canvas, caption);
        wrapper.getStyleClass().add("manual-figure");
        wrapper.setAlignment(Pos.CENTER_LEFT);
        return wrapper;
    }

    private static void draw(String figureId, Pane canvas) {
        if (ManualFigureConceptualDataFigures.draw(figureId, canvas)) {
            return;
        }
        if (ManualFigureAdminAccessScreenFigures.draw(figureId, canvas)) {
            return;
        }
        if (ManualFigureAdminWireframeFigures.draw(figureId, canvas)) {
            return;
        }
        if (ManualFigureProcessFigures.draw(figureId, canvas)) {
            return;
        }
        if (ManualFigureUmlUseCaseFigures.draw(figureId, canvas)) {
            return;
        }
        if (ManualFigureUmlClassFigures.draw(figureId, canvas)) {
            return;
        }
        if (ManualFigureUmlActivityFigures.draw(figureId, canvas)) {
            return;
        }
        if (ManualFigureUmlSequenceFigures.draw(figureId, canvas)) {
            return;
        }
        if (ManualFigureUmlStateFigures.draw(figureId, canvas)) {
            return;
        }
        if (ManualFigureArchitectureUmlFigures.draw(figureId, canvas)) {
            return;
        }
        if (ManualFigureFreeLogicalBusinessFigures.draw(figureId, canvas)) {
            return;
        }
        drawUnknown(canvas, figureId);
    }

    private static void drawUnknown(Pane canvas, String figureId) {
        ManualFigureDrawingSupport.rectangle(canvas, 120, 60, 230, 48, "Figura no registrada");
        ManualFigureDrawingSupport.text(canvas, figureId, 135, 132);
    }
}
