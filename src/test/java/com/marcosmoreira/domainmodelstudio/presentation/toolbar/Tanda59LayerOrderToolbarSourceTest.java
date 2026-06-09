package com.marcosmoreira.domainmodelstudio.presentation.toolbar;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class Tanda59LayerOrderToolbarSourceTest {

    private static final Path MAIN = Path.of("src/main/java");

    @Test
    void toolbarShouldDeclareAndDispatchLayerOrderActions() throws Exception {
        String ids = Files.readString(MAIN.resolve("com/marcosmoreira/domainmodelstudio/presentation/toolbar/DiagramToolbarActionId.java"));
        String executor = Files.readString(MAIN.resolve("com/marcosmoreira/domainmodelstudio/presentation/toolbar/DiagramToolbarActionExecutor.java"));
        String provider = Files.readString(MAIN.resolve("com/marcosmoreira/domainmodelstudio/presentation/toolbar/DefaultDiagramToolbarActionProvider.java"));

        assertTrue(ids.contains("BRING_SELECTION_TO_FRONT") && ids.contains("SEND_SELECTION_TO_BACK")
                && ids.contains("RAISE_SELECTION_LAYER") && ids.contains("LOWER_SELECTION_LAYER"));
        assertTrue(executor.contains("requestBringSelectionToFront") && executor.contains("requestSendSelectionToBack")
                && executor.contains("requestRaiseSelectionLayer") && executor.contains("requestLowerSelectionLayer"));
        assertTrue(provider.contains("layerOrderActions()") && provider.contains("!DiagramTypeId.CONCEPTUAL_MODEL"),
                "Las acciones de capas deben inyectarse solo en diagramas visuales especializados.");
    }

    @Test
    void layerOrderShouldUsePersistentLayoutInsteadOfJavaFxOnlyStacking() throws Exception {
        String visualService = Files.readString(MAIN.resolve("com/marcosmoreira/domainmodelstudio/application/visual/VisualLayoutService.java"));
        String layout = Files.readString(MAIN.resolve("com/marcosmoreira/domainmodelstudio/domain/layout/DiagramLayout.java"));
        String nodeLayout = Files.readString(MAIN.resolve("com/marcosmoreira/domainmodelstudio/domain/layout/NodeLayout.java"));
        String layering = Files.readString(MAIN.resolve("com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/CanvasLayeringPolicy.java"));

        assertTrue(visualService.contains("reorderNodes") && visualService.contains("VisualLayerOrderCommand"));
        assertTrue(layout.contains("bringNodesToFront") && layout.contains("sendNodesToBack")
                && layout.contains("raiseNodes") && layout.contains("lowerNodes"));
        assertTrue(nodeLayout.contains("zOrder()"));
        assertTrue(layering.contains("NodeLayout::zOrder"));
    }
}
