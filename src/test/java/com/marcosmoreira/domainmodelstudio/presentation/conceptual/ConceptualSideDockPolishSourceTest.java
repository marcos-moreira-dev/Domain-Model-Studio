package com.marcosmoreira.domainmodelstudio.presentation.conceptual;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíles de Tanda 13C: legibilidad y scroll único del SideDock conceptual. */
class ConceptualSideDockPolishSourceTest {

    private static final Path MODEL_TREE_VIEW = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/sidebar/ModelTreeView.java");
    private static final Path INSPECTOR_VIEW = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/inspector/InspectorView.java");
    private static final Path CONTRIBUTOR = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/conceptual/ConceptualWorkbenchContributor.java");
    private static final Path BRIDGE = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/conceptual/ConceptualCanvasLegacyBridge.java");
    private static final Path VALIDATION = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/conceptual/sidedock/ConceptualValidationPanel.java");
    private static final Path APPEARANCE = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/conceptual/sidedock/ConceptualAppearancePanel.java");
    private static final Path WORKBENCH_CSS = Path.of("src/main/resources/css/workbench.css");

    @Test
    void conceptualStructureAndPropertiesUseEmbeddedSideDockMode() throws IOException {
        String modelTree = Files.readString(MODEL_TREE_VIEW, StandardCharsets.UTF_8);
        String inspector = Files.readString(INSPECTOR_VIEW, StandardCharsets.UTF_8);
        String contributor = Files.readString(CONTRIBUTOR, StandardCharsets.UTF_8);

        assertTrue(modelTree.contains("boolean showHeader"));
        assertTrue(modelTree.contains("if (showHeader) {\n            root.setTop(header);"));
        assertTrue(inspector.contains("boolean showHeader"));
        assertTrue(inspector.contains("if (showHeader) {\n            root.getChildren().add(header);"));
        assertTrue(contributor.contains("new ModelTreeView(modelTreeViewModel, () -> { }, notationSwitchAction, false)"));
        assertTrue(contributor.contains("new InspectorView(inspectorViewModel, () -> { }, false)"));
    }

    @Test
    void conceptualLegacyPanelsOwnTheirScrollInsideSideDock() throws IOException {
        String modelTree = Files.readString(MODEL_TREE_VIEW, StandardCharsets.UTF_8);
        String inspector = Files.readString(INSPECTOR_VIEW, StandardCharsets.UTF_8);

        assertTrue(modelTree.contains("side-dock-content-owns-scroll"));
        assertTrue(inspector.contains("side-dock-content-owns-scroll"));
    }

    @Test
    void conceptualSideDockTextUsesExplicitReadableColors() throws IOException {
        String css = Files.readString(WORKBENCH_CSS, StandardCharsets.UTF_8);

        assertTrue(css.contains(".conceptual-side-dock-panel .label"));
        assertTrue(css.contains(".conceptual-side-dock-lead"));
        assertTrue(css.contains(".conceptual-side-dock-message"));
        assertTrue(css.contains("-fx-text-fill: #425568"));
        assertFalse(css.contains(".conceptual-side-dock-message {\n    -fx-text-fill: #FFFFFF"));
    }

    @Test
    void conceptualValidationAndAppearanceRefreshWhenProjectChanges() throws IOException {
        String bridge = Files.readString(BRIDGE, StandardCharsets.UTF_8);
        String validation = Files.readString(VALIDATION, StandardCharsets.UTF_8);
        String appearance = Files.readString(APPEARANCE, StandardCharsets.UTF_8);

        assertTrue(bridge.contains("currentProjectObservable()"));
        assertTrue(validation.contains("bridge.currentProjectObservable().addListener"));
        assertTrue(appearance.contains("bridge.currentProjectObservable().addListener"));
    }
}
