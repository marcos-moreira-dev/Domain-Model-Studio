package com.marcosmoreira.domainmodelstudio.presentation.sidedock;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Guardarraíl visual: SideDock nuevo vive en workbench.css y no en tabs legacy conceptuales. */
class Tanda10SideDockCssContractSourceTest {

    private static final Path APP_LIGHT = Path.of("src/main/resources/css/app-light.css");
    private static final Path WORKBENCH_CSS = Path.of("src/main/resources/css/workbench.css");
    private static final List<Path> SPECIALIZED_WORKBENCH_SOURCES = List.of(
            Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/workbench/DiagramWorkbenchView.java"),
            Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/workbench/StructuredWorkbenchView.java"),
            Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/sidedock/WorkbenchSideDock.java"),
            Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/sidedock/SideDockRailFactory.java")
    );

    @Test
    void appLightMustLoadLegacyConceptualPanelsBeforeWorkbenchSideDock() throws IOException {
        String source = Files.readString(APP_LIGHT, StandardCharsets.UTF_8);
        int panels = source.indexOf("panels-sidebar.css");
        int workbench = source.indexOf("workbench.css");

        assertTrue(panels >= 0, "app-light.css debe seguir cargando los paneles conceptuales legacy.");
        assertTrue(workbench >= 0, "app-light.css debe seguir cargando workbench.css.");
        assertTrue(panels < workbench, "workbench.css debe cargarse después del CSS legacy para controlar el SideDock nuevo.");
    }

    @Test
    void workbenchCssMustContainCanonicalSideDockClasses() throws IOException {
        String source = Files.readString(WORKBENCH_CSS, StandardCharsets.UTF_8);

        for (String cssClass : List.of(
                ".workbench-side-dock",
                ".side-dock-rail",
                ".side-dock-rail-scroll",
                ".side-dock-rail-tab",
                ".side-dock-rail-button",
                ".side-dock-rail-button-active",
                ".side-dock-content-host",
                ".side-dock-module-frame",
                ".side-dock-module-header",
                ".side-dock-module-scroll")) {
            assertTrue(source.contains(cssClass), cssClass + " debe existir en workbench.css.");
        }
    }

    @Test
    void specializedSideDockSourcesMustNotUseLegacyRestoreTabs() throws IOException {
        for (Path file : SPECIALIZED_WORKBENCH_SOURCES) {
            String source = Files.readString(file, StandardCharsets.UTF_8);
            assertFalse(source.contains("panel-restore-tab"), file + " no debe usar tabs legacy del modelo conceptual.");
            assertFalse(source.contains("panel-restore-tab-button"), file + " no debe usar tabs legacy del modelo conceptual.");
        }
    }
}
