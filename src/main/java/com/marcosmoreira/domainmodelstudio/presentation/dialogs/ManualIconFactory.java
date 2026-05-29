package com.marcosmoreira.domainmodelstudio.presentation.dialogs;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

/** Iconos vectoriales pequeños para la ayuda con estética de libro morado. */
final class ManualIconFactory {

    private ManualIconFactory() {
    }

    static Node closedBook() {
        return book(false);
    }

    static Node openBook() {
        return book(true);
    }

    static Node page() {
        Pane pane = new Pane();
        pane.getStyleClass().add("manual-page-icon");
        pane.setPrefSize(16, 16);
        pane.setMinSize(16, 16);
        pane.setMaxSize(16, 16);

        Rectangle sheet = new Rectangle(3, 2, 10, 12);
        sheet.getStyleClass().add("manual-page-icon-sheet");
        Rectangle fold = new Rectangle(9, 2, 4, 4);
        fold.getStyleClass().add("manual-page-icon-fold");
        pane.getChildren().addAll(sheet, fold);
        return pane;
    }

    private static Node book(boolean open) {
        Pane pane = new Pane();
        pane.getStyleClass().add(open ? "manual-book-icon-open" : "manual-book-icon");
        pane.setPrefSize(17, 16);
        pane.setMinSize(17, 16);
        pane.setMaxSize(17, 16);

        Rectangle cover = new Rectangle(2, 2, 12, 12);
        cover.getStyleClass().add("manual-book-cover");
        Rectangle spine = new Rectangle(3, 3, 2, 10);
        spine.getStyleClass().add("manual-book-spine");
        Rectangle page = new Rectangle(6, 4, 7, 8);
        page.getStyleClass().add(open ? "manual-book-open-page" : "manual-book-page");
        pane.getChildren().addAll(cover, page, spine);
        return pane;
    }
}
