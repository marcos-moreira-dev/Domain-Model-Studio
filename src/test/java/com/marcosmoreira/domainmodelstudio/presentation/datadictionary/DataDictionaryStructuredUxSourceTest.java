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
    private static final Path DISCLOSURE = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/datadictionary/DataDictionaryDisclosure.java");
    private static final Path INDEX = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/datadictionary/DataDictionaryEntityIndexPanel.java");

    @Test
    void dataDictionaryKeepsStructuredDocumentUxAndMinimumRows() throws IOException {
        String editor = Files.readString(EDITOR, StandardCharsets.UTF_8);
        String preview = Files.readString(PREVIEW, StandardCharsets.UTF_8);
        String disclosure = Files.readString(DISCLOSURE, StandardCharsets.UTF_8);
        String index = Files.readString(INDEX, StandardCharsets.UTF_8);

        assertTrue(index.contains("Índice documental"),
                "El panel izquierdo debe presentarse como índice documental, no como árbol/canvas genérico.");
        assertTrue(editor.contains("PDF formal") && editor.contains("Markdown canónico"),
                "La vista central debe explicar las salidas reales del diccionario.");
        assertTrue(index.contains("searchField.setPromptText(\"Buscar entidad, técnico, módulo, tipo o campo...\")"),
                "El índice debe incluir búsqueda operacional por entidad, metadatos y campos.");
        assertTrue(index.contains("DataEntityKind.values()") && index.contains("data-dictionary-entity-kind-disclosure"),
                "El índice debe agrupar entidades por tipo en secciones colapsables.");
        assertTrue(editor.contains("Agregar campo") && editor.contains("Eliminar campo")
                        && editor.contains("data-dictionary-field-toolbar"),
                "La tabla debe tener acciones contextuales para campos.");
        assertTrue(editor.contains("\"Documento\"") && editor.contains("\"Entidad seleccionada\"")
                        && editor.contains("\"Campo seleccionado\""),
                "La ficha lateral debe separar documento, entidad y campo en secciones claras.");
        assertTrue(editor.contains("SideDockCollectionSizingPolicy.configureTableView(fieldTable)"),
                "La tabla de campos debe conservar el mínimo visual transversal de ocho filas.");
        assertTrue(disclosure.contains("TitledPane") && disclosure.contains("data-dictionary-disclosure"),
                "El diccionario debe usar acordeones propios sin depender del levantamiento lógico.");
        assertTrue(preview.contains("Documento técnico de entidades, campos, tipos, restricciones y reglas de datos"),
                "La vista previa debe leerse como documento técnico de datos.");
        assertTrue(preview.contains("data-dictionary-summary-metrics"),
                "El resumen ejecutivo debe usar métricas documentales legibles.");
        assertFalse(editor.contains("InteractiveCanvas") || editor.contains("DiagramCanvas"),
                "El diccionario no debe convertirse en canvas visual.");
    }
}
