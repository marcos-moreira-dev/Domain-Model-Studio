package com.marcosmoreira.domainmodelstudio.presentation.statusbar;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/** Estado informativo inferior de la aplicación. */
public final class StatusBarViewModel {

    private final StringProperty message = new SimpleStringProperty(
            "Listo. Crea un proyecto, abre un .dms o importa un modelo Markdown."
    );
    private final StringProperty projectState = new SimpleStringProperty("Sin proyecto");
    private final StringProperty notation = new SimpleStringProperty("—");
    private final StringProperty zoom = new SimpleStringProperty("100%");
    private final StringProperty elementCount = new SimpleStringProperty("Sin proyecto abierto");
    private final StringProperty saveState = new SimpleStringProperty("Sin proyecto");

    public StringProperty messageProperty() {
        return message;
    }

    public StringProperty projectStateProperty() {
        return projectState;
    }

    public StringProperty notationProperty() {
        return notation;
    }

    public StringProperty zoomProperty() {
        return zoom;
    }

    public StringProperty elementCountProperty() {
        return elementCount;
    }

    public StringProperty saveStateProperty() {
        return saveState;
    }

    public void setMessage(String message) {
        this.message.set(message == null || message.isBlank() ? "Listo." : message.trim());
    }

    public void setProjectState(String projectState) {
        this.projectState.set(projectState == null || projectState.isBlank() ? "Sin proyecto" : projectState.trim());
    }

    public void setSaveState(String saveState) {
        this.saveState.set(saveState == null || saveState.isBlank() ? "Sin proyecto" : saveState.trim());
    }

    public void setNotation(String notation) {
        this.notation.set(notation == null || notation.isBlank() ? "—" : notation.trim());
    }

    public void setZoom(String zoom) {
        this.zoom.set(zoom == null || zoom.isBlank() ? "100%" : zoom);
    }

    public void setElementCount(String elementCount) {
        this.elementCount.set(elementCount == null || elementCount.isBlank() ? "Sin proyecto abierto" : elementCount.trim());
    }
}
