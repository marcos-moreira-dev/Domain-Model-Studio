package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class UmlClassVisualExportProtectionSourceTest {

    @Test
    void viewModelGuardsPngAndCreatesScopedSvgProject() throws Exception {
        String source = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassDiagramViewModel.java"));

        assertTrue(source.contains("ensurePngExportAllowed(activeVisualCostEstimate(), activeRuntimeMemorySnapshot())"));
        assertTrue(source.contains("currentVisualExportProject()"));
        assertTrue(source.contains("scopedDocumentForVisualExport"));
    }

    @Test
    void shellExportsSvgFromVisualProjectAndAppliesUmlSafetyPolicy() throws Exception {
        String source = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/commands/ExportCommandHandler.java"));

        assertTrue(source.contains("output.visualProject().orElse(project)"));
        assertTrue(source.contains("ensureSvgExportAllowed(visualProject)"));
        assertTrue(source.contains("exportSvgUseCase().export(visualProject"));
    }

    @Test
    void svgWriterBoundsUseOnlyNodesPresentInTheScopedModel() throws Exception {
        String writer = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/svg/specialized/SpecializedVisualSvgWriter.java"));
        String geometry = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/svg/specialized/SpecializedSvgGeometry.java"));

        assertTrue(writer.contains("bounds(layout, nodeIndex)"));
        assertTrue(geometry.contains("!nodeIndex.containsKey(node.elementId())"));
    }
}
