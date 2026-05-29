package com.marcosmoreira.domainmodelstudio.presentation.shell.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class SelectedMarkdownFilesImportSourceTest {

    @Test
    void markdownImportActionShouldAllowOneOrManyFilesAndReportOutcome() throws Exception {
        String handler = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/commands/ImportCommandHandler.java"));
        String coordinator = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/commands/SelectedMarkdownFilesImportCoordinator.java"));
        String progress = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/commands/MarkdownFolderImportProgressDialog.java"));
        String formatter = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/commands/MarkdownFolderImportReportFormatter.java"));

        assertTrue(handler.contains("SelectedMarkdownFilesImportCoordinator"));
        assertTrue(handler.contains("selectedMarkdownFilesImportCoordinator.requestImportMarkdownFiles()"));
        assertTrue(coordinator.contains("showOpenMultipleDialog"));
        assertTrue(coordinator.contains("Task<MarkdownBatchImportResult>"));
        assertTrue(coordinator.contains("dms-markdown-file-import"));
        assertTrue(coordinator.contains("MarkdownFolderImportResultDialog"));
        assertTrue(coordinator.contains("MarkdownBatchImportItemStatus.REJECTED_PARSE_ERROR"));
        assertTrue(coordinator.contains("MarkdownBatchImportItemStatus.REJECTED_IO_ERROR"));
        assertTrue(progress.contains("forFiles"));
        assertTrue(progress.contains("Importación de archivos seleccionados en progreso"));
        assertTrue(formatter.contains("buildSelectedFilesSummary"));
        assertTrue(formatter.contains("Selección Markdown revisada"));
        assertFalse(coordinator.contains("DirectoryChooser"));
    }
}
