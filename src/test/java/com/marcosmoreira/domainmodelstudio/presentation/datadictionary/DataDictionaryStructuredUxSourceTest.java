package com.marcosmoreira.domainmodelstudio.presentation.datadictionary;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class DataDictionaryStructuredUxSourceTest {

    private static final Path EDITOR = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/datadictionary/DataDictionaryEditorView.java");
    private static final Path PREVIEW = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/datadictionary/DataDictionaryDocumentPreview.java");

    @Test
    void dataDictionaryKeepsStructuredDocumentUxAndMinimumRows() throws IOException {
        String editor = Files.readString(EDITOR, StandardCharsets.UTF_8);
        String preview = Files.readString(PREVIEW, StandardCharsets.UTF_8);

        assertTrue(editor.contains("Índice documental"),
                "El panel izquierdo debe presentarse como índice documental, no como árbol/canvas genérico.");
        assertTrue(editor.contains("PDF formal") && editor.contains("Markdown canónico"),
                "La vista central debe explicar las salidas reales del diccionario.");
        assertTrue(editor.contains("SideDockCollectionSizingPolicy.configureListViewForExternalSideDockScroll(entityList)")
                        && editor.contains("SideDockCollectionSizingPolicy.configureTableView(fieldTable)"),
                "El diccionario debe respetar el mínimo visual transversal de ocho filas sin crear registros falsos.");
        assertTrue(preview.contains("Documento técnico de entidades, campos, tipos, restricciones y reglas de datos"),
                "La vista previa debe leerse como documento técnico de datos.");
        assertTrue(preview.contains("data-dictionary-summary-metrics"),
                "El resumen ejecutivo debe usar métricas documentales legibles.");
        assertFalse(editor.contains("InteractiveCanvas") || editor.contains("DiagramCanvas"),
                "El diccionario no debe convertirse en canvas visual.");
    }
}
