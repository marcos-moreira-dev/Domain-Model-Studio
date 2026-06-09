package com.marcosmoreira.domainmodelstudio.presentation.statusbar;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Contrato estático de textos visibles de la barra inferior. */
class StatusBarViewContractTest {

    @Test
    void statusBarShowsViewLabelInsteadOfNotationForAllDiagramTypes() throws IOException {
        Path source = Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/statusbar/StatusBarView.java");
        String content = Files.readString(source);

        assertTrue(content.contains("prefixed(\"Vista\""));
    }
}
