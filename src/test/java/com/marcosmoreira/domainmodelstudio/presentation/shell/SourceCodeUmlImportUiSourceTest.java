package com.marcosmoreira.domainmodelstudio.presentation.shell;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl fuente de la entrada UI para generar UML Clases desde código. */
class SourceCodeUmlImportUiSourceTest {

    private static final Path IMPORT_HANDLER = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/commands/ImportCommandHandler.java");
    private static final Path MAIN_SHELL = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellCommandHandler.java");
    private static final Path MAIN_SHELL_VIEW = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellView.java");
    private static final Path MAIN_SHELL_VIEW_MODEL = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellViewModel.java");
    private static final Path APP_FACTORY = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/bootstrap/ApplicationServicesFactory.java");

    @Test
    void importHandlerShouldKeepMarkdownAndSourceCodeEntriesSeparated() throws IOException {
        String source = Files.readString(IMPORT_HANDLER);

        assertTrue(source.contains("requestImportMarkdown()"));
        assertTrue(source.contains("requestImportUmlClassFromSourceCode()"));
        assertTrue(source.contains("DirectoryChooser chooser = new DirectoryChooser()"));
        assertTrue(source.contains("SourceCodeImportRequest.flexible(sourceDirectory)"));
        assertTrue(source.contains("generateUmlClassDiagramFromSourceCodeUseCase()"));
        assertTrue(source.contains("DiagramTypeId.UML_CLASS"));
    }

    @Test
    void mainShellShouldOnlyDelegateSourceCodeImport() throws IOException {
        String shell = Files.readString(MAIN_SHELL);

        assertTrue(shell.contains("requestImportUmlClassFromSourceCode()"));
        assertTrue(shell.contains("importCommandHandler.requestImportUmlClassFromSourceCode()"));
    }

    @Test
    void menuShouldExposeSourceCodeImportWithoutReplacingMarkdown() throws IOException {
        String view = Files.readString(MAIN_SHELL_VIEW);
        String viewModel = Files.readString(MAIN_SHELL_VIEW_MODEL);

        assertTrue(view.contains("Importar modelo Markdown..."));
        assertTrue(view.contains("Importar UML desde código fuente..."));
        assertTrue(viewModel.contains("importUmlClassFromSourceCode()"));
    }

    @Test
    void applicationFactoryShouldWireJavaAndTypescriptParsers() throws IOException {
        String factory = Files.readString(APP_FACTORY);

        assertTrue(factory.contains("new JavaSourceCodeParserAdapter()"));
        assertTrue(factory.contains("new TypeScriptSourceCodeParserAdapter()"));
        assertTrue(factory.contains("new FileSystemSourceDirectoryScanner()"));
        assertTrue(factory.contains("new GenerateUmlClassDiagramFromSourceCodeUseCase"));
    }
}
