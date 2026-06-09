package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl fuente para el ajuste discreto de tamaño en figuras visuales. */
class VisualNodeSizeToolbarSourceTest {

    @Test
    void toolbarShouldExposeDiscreteSizeActionsForVisualFigures() throws Exception {
        String actionIds = Files.readString(Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/toolbar/DiagramToolbarActionId.java"));
        String factory = Files.readString(Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/toolbar/DiagramToolbarActionFactory.java"));
        String executor = Files.readString(Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/toolbar/DiagramToolbarActionExecutor.java"));
        String shell = Files.readString(Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellCommandHandler.java"));

        assertTrue(actionIds.contains("GROW_SELECTED_FIGURE"));
        assertTrue(actionIds.contains("SHRINK_SELECTED_FIGURE"));
        assertTrue(factory.contains("Agrandar figura"));
        assertTrue(factory.contains("Achicar figura"));
        assertTrue(factory.contains("DiagramToolbarSection.ELEMENTS"));
        assertTrue(executor.contains("requestGrowSelectedVisualElement"));
        assertTrue(executor.contains("requestShrinkSelectedVisualElement"));
        assertTrue(shell.contains("visualNodeSizeCommands.growSelection"));
        assertTrue(shell.contains("visualNodeSizeCommands.shrinkSelection"));
    }

    @Test
    void resizeSupportShouldClampAndUseVisualLayoutOnly() throws Exception {
        String support = Files.readString(Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/VisualNodeSizeViewModelSupport.java"));
        String command = Files.readString(Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/application/visual/VisualNodeSizeCommand.java"));

        assertTrue(support.contains("resizeNodeTo"));
        assertTrue(support.contains("preferredNodeSize"));
        assertTrue(support.contains("MIN_WIDTH"));
        assertTrue(support.contains("MAX_WIDTH"));
        assertTrue(support.contains("texto mantiene el autoajuste visual"));
        assertTrue(command.contains("GROW(1.12"));
        assertTrue(command.contains("SHRINK(0.90"));
    }

    @Test
    void layoutReconciliationShouldPreserveReadableManualShrink() throws Exception {
        String layoutService = Files.readString(Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/application/visual/VisualLayoutService.java"));

        assertTrue(layoutService.contains("preferredNodeSize"));
        assertTrue(layoutService.contains("readableMinimumWidth"));
        assertTrue(layoutService.contains("reference.preferredWidth() * 0.68"));
        assertTrue(layoutService.contains("Math.max(existing.width(), readableMinimumWidth(reference))"));
    }

    @Test
    void sideDockTextShouldHaveExplicitReadableColor() throws Exception {
        String workbenchCss = Files.readString(Path.of("src/main/resources/css/workbench.css"));
        String behaviorCss = Files.readString(Path.of("src/main/resources/css/behavior-diagram.css"));

        assertTrue(workbenchCss.contains("diagram-workbench-panel-content .label"));
        assertTrue(workbenchCss.contains("-fx-text-fill: -dms-text"));
        assertTrue(behaviorCss.contains("behavior-panel-help"));
        assertTrue(behaviorCss.contains("-fx-text-fill: -dms-text-muted"));
    }
}
