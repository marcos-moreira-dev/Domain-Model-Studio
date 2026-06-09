package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

/**
 * Puerto opcional para que el canvas común ejecute Ctrl+C/Ctrl+V sobre selecciones visuales.
 *
 * <p>El canvas no conoce los tipos de dominio; cada adaptador decide cómo serializar y pegar
 * su selección, incluyendo relaciones y layout.</p>
 */
public interface CanvasSelectionClipboardPort {

    boolean copySelectionToClipboard();

    boolean pasteSelectionFromClipboard();
}
