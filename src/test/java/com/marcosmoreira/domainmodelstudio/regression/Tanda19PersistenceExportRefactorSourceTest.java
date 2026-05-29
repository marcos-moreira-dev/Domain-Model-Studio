package com.marcosmoreira.domainmodelstudio.regression;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl fuente para la tanda 19 de persistencia y exportaciones. */
class Tanda19PersistenceExportRefactorSourceTest {

    private static final Path JSON_READER = Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/json/DmsProjectJsonReader.java");
    private static final Path JSON_WRITER = Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/json/DmsProjectJsonWriter.java");
    private static final Path SPECIALIZED_READER = Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/json/DmsProjectSpecializedJsonReader.java");
    private static final Path SPECIALIZED_WRITER = Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/json/DmsProjectSpecializedJsonWriter.java");
    private static final Path VISUAL_READER = Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/json/DmsProjectVisualStateJsonReader.java");
    private static final Path EXPORT_HANDLER = Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/commands/ExportCommandHandler.java");
    private static final Path EXPORT_CHOOSER = Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/commands/ExportFileChooserFactory.java");

    @Test
    void dmsReaderWriterDelegateSpecializedPersistenceSections() throws IOException {
        String reader = Files.readString(JSON_READER, StandardCharsets.UTF_8);
        String writer = Files.readString(JSON_WRITER, StandardCharsets.UTF_8);

        assertTrue(Files.readAllLines(JSON_READER, StandardCharsets.UTF_8).size() <= 450,
                "El lector JSON principal debe actuar como coordinador, no como archivo gigante.");
        assertTrue(Files.readAllLines(JSON_WRITER, StandardCharsets.UTF_8).size() <= 450,
                "El writer JSON principal debe actuar como coordinador, no como archivo gigante.");
        assertTrue(reader.contains("DmsProjectConceptualModelJsonReader"));
        assertTrue(reader.contains("DmsProjectSpecializedPayloadReader"));
        assertTrue(reader.contains("DmsProjectVisualStateJsonReader"));
        assertTrue(writer.contains("DmsProjectConceptualModelJsonWriter"));
        assertFalse(reader.contains("readUmlClassDiagramView("),
                "La lectura de UML Clases debe vivir en el lector especializado.");
        assertFalse(writer.contains("writeUmlClassDiagramView("),
                "La escritura de UML Clases debe vivir en el writer especializado.");
    }

    @Test
    void specializedPersistenceClassesOwnTheirSections() throws IOException {
        String specializedReader = Files.readString(SPECIALIZED_READER, StandardCharsets.UTF_8);
        String specializedWriter = Files.readString(SPECIALIZED_WRITER, StandardCharsets.UTF_8);
        String visualReader = Files.readString(VISUAL_READER, StandardCharsets.UTF_8);

        assertTrue(specializedReader.contains("DmsProjectCoreSpecializedJsonReader"));
        assertTrue(specializedReader.contains("DmsProjectAuxiliarySpecializedJsonReader"));
        assertTrue(specializedWriter.contains("DmsProjectCoreSpecializedJsonWriter"));
        assertTrue(specializedWriter.contains("DmsProjectAuxiliarySpecializedJsonWriter"));
        assertTrue(visualReader.contains("readLayouts"));
        assertTrue(visualReader.contains("readStyleSheet"));
        assertTrue(visualReader.contains("readViewState"));
    }

    @Test
    void exportCommandHandlerDelegatesFileChooserConstruction() throws IOException {
        String handler = Files.readString(EXPORT_HANDLER, StandardCharsets.UTF_8);
        String chooser = Files.readString(EXPORT_CHOOSER, StandardCharsets.UTF_8);

        assertTrue(handler.contains("ExportFileChooserFactory.showSaveDialog"));
        assertFalse(handler.contains("new FileChooser"));
        assertTrue(chooser.contains("case SVG"));
        assertTrue(chooser.contains("case PNG"));
        assertTrue(chooser.contains("case PDF"));
        assertTrue(chooser.contains("case MARKDOWN"));
    }
}
