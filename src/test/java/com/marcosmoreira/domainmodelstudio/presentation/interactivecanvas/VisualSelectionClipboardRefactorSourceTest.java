package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl para que el portapapeles visual multi-diagrama no vuelva a ser una clase monolítica. */
class VisualSelectionClipboardRefactorSourceTest {

    @Test
    void projectClipboardShouldRemainSplitByResponsibility() throws IOException {
        assertTrue(exists("ProjectVisualSelectionTransferService.java"));
        assertTrue(exists("ProjectVisualSelectionPasteRouter.java"));
        assertTrue(exists("ProjectVisualSelectionPasteCore.java"));
        assertTrue(exists("ProjectVisualSelectionPasteStructured.java"));
        assertTrue(exists("ProjectVisualSelectionTransferSupport.java"));
        assertTrue(lineCount("ProjectVisualSelectionTransferService.java") < 180);
        assertTrue(lineCount("ProjectVisualSelectionPasteCore.java") < 260);
        assertTrue(lineCount("ProjectVisualSelectionPasteStructured.java") < 280);
    }

    private static boolean exists(String filename) {
        return path(filename).toFile().isFile();
    }

    private static int lineCount(String filename) throws IOException {
        return Files.readAllLines(path(filename), StandardCharsets.UTF_8).size();
    }

    private static Path path(String filename) {
        return Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/visualtransfer").resolve(filename);
    }
}
