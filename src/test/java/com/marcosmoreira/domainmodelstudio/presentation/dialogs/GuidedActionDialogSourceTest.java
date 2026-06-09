package com.marcosmoreira.domainmodelstudio.presentation.dialogs;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class GuidedActionDialogSourceTest {

    @Test
    void shouldProvideReusableGuidedConfirmationForLargeActions() throws Exception {
        String source = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/dialogs/GuidedActionDialog.java"));
        String markdownConfirmation = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/commands/MarkdownFolderImportConfirmationDialog.java"));

        assertTrue(source.contains("Diálogo transversal para confirmar acciones masivas"));
        assertTrue(source.contains("record Request"));
        assertTrue(source.contains("ButtonBar.ButtonData.OK_DONE"));
        assertTrue(source.contains("ButtonBar.ButtonData.CANCEL_CLOSE"));
        assertTrue(source.contains("TextArea detail"));
        assertTrue(source.contains("ScrollPane summaryScroll"));
        assertTrue(markdownConfirmation.contains("GuidedActionDialog.Request"));
        assertTrue(markdownConfirmation.contains("Candidatos Markdown que se revisarán"));
        assertTrue(markdownConfirmation.contains("PREVIEW_LIMIT"));
    }
}
