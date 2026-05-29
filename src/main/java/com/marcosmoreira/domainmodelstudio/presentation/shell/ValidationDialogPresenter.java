package com.marcosmoreira.domainmodelstudio.presentation.shell;

import java.util.List;
import javafx.scene.control.Alert;

/** Presenta resultados de validación de editores sin mezclar esa construcción visual en el handler principal. */
final class ValidationDialogPresenter {

    void show(String title, String summary, List<String> warnings) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(summary == null || summary.isBlank() ? title : summary);
        alert.setContentText(warnings == null || warnings.isEmpty() ? "" : String.join("\n", warnings));
        alert.showAndWait();
    }
}
