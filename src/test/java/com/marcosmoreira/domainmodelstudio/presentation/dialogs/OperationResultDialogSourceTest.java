package com.marcosmoreira.domainmodelstudio.presentation.dialogs;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class OperationResultDialogSourceTest {

    @Test
    void shouldRenderReusableVisualResultWithoutExternalImages() throws Exception {
        String source = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/dialogs/OperationResultDialog.java"));
        String markdownDialog = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/commands/MarkdownFolderImportResultDialog.java"));

        assertTrue(source.contains("Diálogo transversal para comunicar el resultado de una operación masiva"));
        assertTrue(source.contains("enum Status"));
        assertTrue(source.contains("SUCCESS"));
        assertTrue(source.contains("PROBLEM"));
        assertTrue(source.contains("StatusIconFactory"));
        assertTrue(source.contains("new Circle"));
        assertTrue(source.contains("new Polyline"));
        assertTrue(source.contains("new Line"));
        assertTrue(source.contains("RadialGradient"));
        assertTrue(source.contains("DropShadow"));
        assertTrue(source.contains("Clipboard.getSystemClipboard"));
        assertTrue(source.contains("showLater"));
        assertFalse(source.contains("ImageView"));
        assertFalse(source.contains("new Image"));
        assertFalse(source.contains("http://"));
        assertFalse(source.contains("https://"));
        assertTrue(markdownDialog.contains("OperationResultDialog.Request"));
        assertTrue(markdownDialog.contains("Todo está en orden"));
        assertTrue(markdownDialog.contains("Copiar problemas"));
        assertTrue(markdownDialog.contains("Copiar estado"));
    }
}
