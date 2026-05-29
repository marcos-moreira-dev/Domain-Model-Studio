package com.marcosmoreira.domainmodelstudio.presentation.dialogs;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class LargeTextReportDialogSourceTest {

    @Test
    void shouldBeReusableForLargeCopyableReports() throws Exception {
        String source = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/dialogs/LargeTextReportDialog.java"));

        assertTrue(source.contains("Diálogo transversal para reportes largos"));
        assertTrue(source.contains("record Request"));
        assertTrue(source.contains("TextArea detail"));
        assertTrue(source.contains("ScrollPane summaryScroll"));
        assertTrue(source.contains("ButtonBar.ButtonData.OK_DONE"));
        assertTrue(source.contains("Clipboard.getSystemClipboard"));
        assertTrue(source.contains("setMinWidth(170)"));
        assertTrue(source.contains("Copiado"));
        assertTrue(source.contains("Screen.getScreensForRectangle"));
        assertTrue(source.contains("showLater"));
        assertTrue(source.contains("dialog.show()"));
        assertFalse(source.contains("MarkdownBatchImportResult"));
    }
}
