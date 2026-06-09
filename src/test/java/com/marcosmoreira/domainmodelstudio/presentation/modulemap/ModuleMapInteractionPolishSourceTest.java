package com.marcosmoreira.domainmodelstudio.presentation.modulemap;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

final class ModuleMapInteractionPolishSourceTest {

    @Test
    void moduleMapSkipsSelectedDescendantsAndHidesRedundantContainmentArrows() throws Exception {
        String adapter = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/modulemap/ModuleMapCanvasAdapter.java"));
        String renderKit = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/modulemap/ModuleMapRenderKit.java"));

        assertTrue(adapter.contains("hasSelectedAncestor(moduleId, selectedModuleIds)"));
        assertTrue(renderKit.contains("isContainmentConnector(connector)"));
        assertTrue(renderKit.contains("invisibleContainment.setMouseTransparent(true)"));
    }

    @Test
    void moduleMapDependenciesUseTransversalConnectorGeometryInsteadOfFakeElbows() throws Exception {
        String renderKit = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/modulemap/ModuleMapRenderKit.java"));

        assertTrue(renderKit.contains("CanvasConnectorGeometry.edgeToEdgePoints"));
        assertTrue(renderKit.contains("adapter.layoutForConnector(connector.id())"));
        assertFalse(renderKit.contains("dependencyConnectorPoints"));
        assertFalse(renderKit.contains("firstElbow"));
        assertFalse(renderKit.contains("secondElbow"));
    }

    @Test
    void moduleMapAdapterPersistsManualBendPointCommands() throws Exception {
        String adapter = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/modulemap/ModuleMapCanvasAdapter.java"));

        assertTrue(adapter.contains("addConnectorBendPoint"));
        assertTrue(adapter.contains("moveConnectorBendPointTo"));
        assertTrue(adapter.contains("removeConnectorBendPoint"));
    }
}
