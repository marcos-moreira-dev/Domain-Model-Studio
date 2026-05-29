package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/** Instala atajos de portapapeles visual sin engordar la superficie común del canvas. */
final class CanvasClipboardKeyboardInstaller {

    private CanvasClipboardKeyboardInstaller() {
    }

    static void install(
            Parent root,
            InteractiveCanvasAdapter adapter,
            Runnable refreshPreservingViewport
    ) {
        root.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (!event.isShortcutDown() || !(adapter instanceof CanvasSelectionClipboardPort clipboardPort)) {
                return;
            }
            if (event.getCode() == KeyCode.C && clipboardPort.copySelectionToClipboard()) {
                event.consume();
                return;
            }
            if (event.getCode() == KeyCode.V && clipboardPort.pasteSelectionFromClipboard()) {
                refreshPreservingViewport.run();
                event.consume();
            }
        });
    }
}
