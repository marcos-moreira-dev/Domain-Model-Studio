package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class UmlClassFullDetailPanelSourceTest {

    @Test
    void propertiesPanelKeepsCanvasLightButShowsCompleteDetailOnSelection() throws IOException {
        String panel = Files.readString(Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassPropertiesPanel.java"), StandardCharsets.UTF_8);
        String formatter = Files.readString(Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassFullDetailFormatter.java"), StandardCharsets.UTF_8);

        assertTrue(panel.contains("UmlClassFullDetailFormatter"));
        assertTrue(panel.contains("classFullDetailArea"));
        assertTrue(panel.contains("Detalle completo"));
        assertTrue(formatter.contains("node.members()"));
        assertTrue(formatter.contains("No debe truncar nombres ni firmas"));
    }
}
