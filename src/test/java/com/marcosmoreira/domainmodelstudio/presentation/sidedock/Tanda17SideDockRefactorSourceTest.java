package com.marcosmoreira.domainmodelstudio.presentation.sidedock;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class Tanda17SideDockRefactorSourceTest {

    private static final Path WORKBENCH_SIDE_DOCK = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/sidedock/WorkbenchSideDock.java");
    private static final Path STANDARD_MODULES = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/sidedock/StandardSideDockModules.java");

    @Test
    void workbenchSideDockDelegatesVisualConstructionToSmallFactories() throws IOException {
        String source = Files.readString(WORKBENCH_SIDE_DOCK, StandardCharsets.UTF_8);

        assertTrue(source.contains("SideDockRailFactory"));
        assertTrue(source.contains("SideDockModuleFrameFactory"));
        assertTrue(source.contains("SideDockSizingPolicy"));
        assertTrue(source.contains("tabFor(module"));
        assertTrue(source.contains("frameFor(module"));
        assertFalse(source.contains("new Button("), "WorkbenchSideDock no debe construir botones directamente.");
        assertFalse(source.contains("new Label("), "WorkbenchSideDock no debe construir headers directamente.");
    }

    @Test
    void appearanceModuleIsCentralizedForVisualContributors() throws IOException {
        String source = Files.readString(STANDARD_MODULES, StandardCharsets.UTF_8);

        assertTrue(source.contains("appearance(Parent content)"));
        assertTrue(source.contains("SideDockModuleId.APPEARANCE"));
        assertTrue(source.contains("StaticSideDockModule.of"));
    }
}
