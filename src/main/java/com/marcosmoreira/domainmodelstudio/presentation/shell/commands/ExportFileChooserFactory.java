package com.marcosmoreira.domainmodelstudio.presentation.shell.commands;

import com.marcosmoreira.domainmodelstudio.presentation.exportable.ExportFormat;
import java.io.File;
import javafx.stage.FileChooser;

/** Centraliza la creación de diálogos de guardado usados por las exportaciones directas. */
final class ExportFileChooserFactory {

    private ExportFileChooserFactory() {
    }

    static File showSaveDialog(String title, ExportFormat format, String initialFileName) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle(title);
        chooser.getExtensionFilters().add(extensionFilter(format));
        chooser.setInitialFileName(initialFileName);
        return chooser.showSaveDialog(null);
    }

    private static FileChooser.ExtensionFilter extensionFilter(ExportFormat format) {
        return switch (format) {
            case SVG -> new FileChooser.ExtensionFilter("Imagen SVG (*.svg)", "*.svg");
            case MARKDOWN -> new FileChooser.ExtensionFilter("Markdown (*.md)", "*.md");
            case PNG -> new FileChooser.ExtensionFilter("Imagen PNG (*.png)", "*.png");
            case PDF -> new FileChooser.ExtensionFilter("Documento PDF (*.pdf)", "*.pdf");
        };
    }
}
