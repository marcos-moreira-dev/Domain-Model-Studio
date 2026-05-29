package com.marcosmoreira.domainmodelstudio.regression;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class UmlClassLongNameMetadataSourceTest {

    @Test
    void umlClassCanvasUsesShortLabelsWithFullMetadataTooltips() throws Exception {
        String renderKit = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassRenderKit.java"));
        String properties = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassPropertiesPanel.java"));

        assertTrue(renderKit.contains("UmlClassDisplayLabelPolicy"));
        assertTrue(renderKit.contains("new Tooltip(labelPolicy.classTooltip"));
        assertTrue(renderKit.contains("labelPolicy.memberLine"));
        assertTrue(properties.contains("classOriginArea"));
        assertTrue(properties.contains("labelPolicy.classMetadataPanel"));
    }
}
