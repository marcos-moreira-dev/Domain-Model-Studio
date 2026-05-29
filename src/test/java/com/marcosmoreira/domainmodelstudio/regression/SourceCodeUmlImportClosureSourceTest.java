package com.marcosmoreira.domainmodelstudio.regression;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class SourceCodeUmlImportClosureSourceTest {

    @Test
    void applicationCompositionRegistersBothSourceCodeParsers() throws IOException {
        String source = Files.readString(Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/bootstrap/ApplicationServicesFactory.java"));

        assertTrue(source.contains("new JavaSourceCodeParserAdapter()"));
        assertTrue(source.contains("new TypeScriptSourceCodeParserAdapter()"));
        assertTrue(source.contains("new SourceCodeParserRegistry"));
        assertTrue(source.contains("new GenerateUmlClassDiagramFromSourceCodeUseCase"));
        assertTrue(source.contains("new PreviewSourceCodeImportUseCase"));
    }

    @Test
    void sourceCodeImportIsASeparateUiCommandFromMarkdownImport() throws IOException {
        String actionIds = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/toolbar/DiagramToolbarActionId.java"));
        String executor = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/toolbar/DiagramToolbarActionExecutor.java"));
        String importHandler = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/commands/ImportCommandHandler.java"));
        String shellView = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellView.java"));

        assertTrue(actionIds.contains("IMPORT_UML_FROM_SOURCE"));
        assertTrue(executor.contains("requestImportUmlClassFromSourceCode"));
        assertTrue(importHandler.contains("requestImportUmlClassFromSourceCode"));
        assertTrue(importHandler.contains("PreviewSourceCodeImportUseCase"));
        assertTrue(shellView.contains("Importar UML desde código fuente"));
        assertTrue(shellView.contains("Importar Markdown") || shellView.contains("Importar modelo"),
                "El flujo Markdown debe seguir existiendo en paralelo al flujo desde código.");
    }

    @Test
    void umlClassMarkdownParserRemainsIndependentFromSourceCodeImport() throws IOException {
        String markdownParser = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/markdown/UmlClassMarkdownParser.java"));
        String sourceCodeMapper = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/application/umlclass/SourceCodeToUmlClassDiagramMapper.java"));

        assertTrue(markdownParser.contains("implements MarkdownModelParser"));
        assertTrue(markdownParser.contains("UmlClassMarkdownViewSectionParser") || markdownParser.contains("Section.VIEWS"));
        assertTrue(sourceCodeMapper.contains("ParsedCodeProject"));
        assertTrue(sourceCodeMapper.contains("SourceCodeUmlClassViewBuilder"));
    }
}
