package com.marcosmoreira.domainmodelstudio.presentation.datadictionary;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class DataDictionaryDocumentEditorSourceTest {

    private static final Path EDITOR = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/datadictionary/DataDictionaryEditorView.java");
    private static final Path PREVIEW = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/datadictionary/DataDictionaryDocumentPreview.java");
    private static final Path LABELS = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/datadictionary/DataDictionaryLabels.java");

    @Test
    void dataDictionaryKeepsDocumentPreviewOutsideMainEditor() throws IOException {
        String editor = Files.readString(EDITOR, StandardCharsets.UTF_8);
        String preview = Files.readString(PREVIEW, StandardCharsets.UTF_8);
        String labels = Files.readString(LABELS, StandardCharsets.UTF_8);

        assertTrue(editor.contains("DataDictionaryDocumentPreview"),
                "El editor debe delegar la vista documental, no renderizar todo el documento en la clase principal.");
        assertFalse(editor.contains("private void refreshDocumentPreview"),
                "El render documental no debe volver a crecer dentro del editor principal.");
        assertTrue(preview.contains("Diccionario no se fuerza a canvas") || preview.contains("no se fuerza a canvas"),
                "La vista documental debe dejar claro que el diccionario no es un canvas visual.");
        assertTrue(labels.contains("constraintSummary") && labels.contains("visibilitySummary"),
                "Las etiquetas compartidas deben centralizar restricciones y visibilidad.");
    }
}
