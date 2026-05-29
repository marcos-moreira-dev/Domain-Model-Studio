package com.marcosmoreira.domainmodelstudio.presentation.conceptual;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíles de Tanda 7: bridges híbridos del canvas conceptual. */
class ConceptualHybridCanvasBridgeSourceTest {

    private static final Path CONCEPTUAL = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/conceptual");
    private static final Path BRIDGE_PACKAGE = CONCEPTUAL.resolve("bridge");
    private static final Path LEGACY_BRIDGE = CONCEPTUAL.resolve("ConceptualCanvasLegacyBridge.java");

    @Test
    void conceptualLegacyBridgeExposesHybridCanvasBridges() throws IOException {
        String legacy = Files.readString(LEGACY_BRIDGE, StandardCharsets.UTF_8);

        assertTrue(legacy.contains("ConceptualHybridCanvasBridge"));
        assertTrue(legacy.contains("hybridCanvas()"));
        assertTrue(legacy.contains("selection()"));
        assertTrue(legacy.contains("commands()"));
        assertTrue(legacy.contains("layout()"));
        assertTrue(legacy.contains("validation()"));
        assertFalse(legacy.contains("InteractiveCanvasSurfaceView"),
                "Tanda 7 agrega bridges híbridos, no reemplaza todavía el canvas conceptual legacy.");
    }

    @Test
    void hybridBridgeAggregatesSelectionCommandsLayoutAndValidation() throws IOException {
        String hybrid = Files.readString(BRIDGE_PACKAGE.resolve("ConceptualHybridCanvasBridge.java"), StandardCharsets.UTF_8);

        assertTrue(hybrid.contains("ConceptualSelectionBridge"));
        assertTrue(hybrid.contains("ConceptualCanvasCommandBridge"));
        assertTrue(hybrid.contains("ConceptualLayoutBridge"));
        assertTrue(hybrid.contains("ConceptualValidationBridge"));
        assertTrue(hybrid.contains("new ConceptualSelectionBridge(canvasViewModel)"));
        assertTrue(hybrid.contains("new ConceptualCanvasCommandBridge(canvasViewModel, selection)"));
        assertTrue(hybrid.contains("new ConceptualLayoutBridge(canvasViewModel, selection)"));
        assertFalse(hybrid.contains("ChenDiagramRenderer"));
        assertFalse(hybrid.contains("CrowsFootDiagramRenderer"));
        assertFalse(hybrid.contains("InteractiveCanvasSurfaceView"));
    }

    @Test
    void selectionBridgeExposesNeutralSelectionSnapshot() throws IOException {
        String selection = Files.readString(BRIDGE_PACKAGE.resolve("ConceptualSelectionBridge.java"), StandardCharsets.UTF_8);
        String snapshot = Files.readString(BRIDGE_PACKAGE.resolve("ConceptualSelectionSnapshot.java"), StandardCharsets.UTF_8);
        String kind = Files.readString(BRIDGE_PACKAGE.resolve("ConceptualSelectedElementKind.java"), StandardCharsets.UTF_8);

        assertTrue(selection.contains("ConceptualSelectionSnapshot"));
        assertTrue(selection.contains("entityById(selected)"));
        assertTrue(selection.contains("attributeById(selected)"));
        assertTrue(selection.contains("relationshipById(selected)"));
        assertTrue(snapshot.contains("primaryElementId"));
        assertTrue(snapshot.contains("selectedElementIds"));
        assertTrue(snapshot.contains("bendPointSelected"));
        assertTrue(kind.contains("ENTITY"));
        assertTrue(kind.contains("ATTRIBUTE"));
        assertTrue(kind.contains("RELATIONSHIP"));
        assertTrue(kind.contains("CONNECTOR_BEND_POINT"));
    }

    @Test
    void commandBridgeDelegatesToExistingCanvasViewModel() throws IOException {
        String commands = Files.readString(BRIDGE_PACKAGE.resolve("ConceptualCanvasCommandBridge.java"), StandardCharsets.UTF_8);

        assertTrue(commands.contains("beginAddEntityTool()"));
        assertTrue(commands.contains("beginAddRelationshipTool()"));
        assertTrue(commands.contains("addAttributeToSelectedEntity()"));
        assertTrue(commands.contains("duplicateSelectedEntity()"));
        assertTrue(commands.contains("removeSelectedElement()"));
        assertTrue(commands.contains("removeSelectedBendPoint()"));
        assertTrue(commands.contains("selectAllVisibleElements()"));
        assertFalse(commands.contains("new AddEntityUseCase"));
        assertFalse(commands.contains("new AddRelationshipUseCase"));
    }

    @Test
    void layoutAndValidationBridgeDoNotMigrateToInteractiveCanvasYet() throws IOException {
        String layout = Files.readString(BRIDGE_PACKAGE.resolve("ConceptualLayoutBridge.java"), StandardCharsets.UTF_8);
        String validation = Files.readString(BRIDGE_PACKAGE.resolve("ConceptualValidationBridge.java"), StandardCharsets.UTF_8);
        String validationPanel = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/conceptual/sidedock/ConceptualValidationPanel.java"),
                StandardCharsets.UTF_8);

        assertTrue(layout.contains("activeLayout()"));
        assertTrue(layout.contains("selectedNodeLayout()"));
        assertTrue(layout.contains("previewMoveSelectedElementBy"));
        assertTrue(layout.contains("moveSelectedElementBy"));
        assertTrue(validation.contains("DiagramModelValidator"));
        assertTrue(validation.contains("validateActiveProject()"));
        assertTrue(validationPanel.contains("bridge.validation().validateActiveProject()"));
        assertFalse(layout.contains("VisualLayoutService"));
        assertFalse(layout.contains("InteractiveCanvasSurfaceView"));
        assertFalse(validation.contains("withModel("));
        assertFalse(validation.contains("withMetadata("));
    }
}
