package com.marcosmoreira.domainmodelstudio.presentation.conceptual;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíles de Tanda 13D: textos legibles en figuras, toolbar sin elipsis y feedback de validación. */
class ConceptualToolbarAndValidationPolishSourceTest {

    private static final Path TOOLBAR_SIZING = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/toolbar/ToolbarButtonSizingPolicy.java");
    private static final Path CONTEXTUAL_TOOLBAR = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/toolbar/ContextualToolbarView.java");
    private static final Path GLOBAL_TOOLBAR = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/toolbar/GlobalToolbarView.java");
    private static final Path TEXT_FIT = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/canvas/ConceptualFigureTextFitPolicy.java");
    private static final Path CHEN_RENDERER = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/canvas/ChenDiagramRenderer.java");
    private static final Path CROWS_RENDERER = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/canvas/CrowsFootDiagramRenderer.java");
    private static final Path VALIDATION_PANEL = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/conceptual/sidedock/ConceptualValidationPanel.java");

    @Test
    void toolbarButtonsComputeEnoughWidthForTheirText() throws IOException {
        String policy = Files.readString(TOOLBAR_SIZING, StandardCharsets.UTF_8);
        String contextual = Files.readString(CONTEXTUAL_TOOLBAR, StandardCharsets.UTF_8);
        String global = Files.readString(GLOBAL_TOOLBAR, StandardCharsets.UTF_8);

        assertTrue(policy.contains("preferredWidth(String text"));
        assertTrue(policy.contains("minimumWidth(String text"));
        assertTrue(contextual.contains("ToolbarButtonSizingPolicy.minimumWidth"));
        assertTrue(contextual.contains("ToolbarButtonSizingPolicy.preferredWidth"));
        assertTrue(global.contains("ToolbarButtonSizingPolicy.minimumWidth"));
        assertTrue(global.contains("ToolbarButtonSizingPolicy.preferredWidth"));
    }

    @Test
    void conceptualRenderersApplyTextFitBeforeDrawingLabels() throws IOException {
        String policy = Files.readString(TEXT_FIT, StandardCharsets.UTF_8);
        String chen = Files.readString(CHEN_RENDERER, StandardCharsets.UTF_8);
        String crows = Files.readString(CROWS_RENDERER, StandardCharsets.UTF_8);

        assertTrue(policy.contains("fittedFontSize"));
        assertTrue(chen.contains("ConceptualFigureTextFitPolicy.fittedFontSize"));
        assertTrue(chen.contains("node.width() * 0.72"),
                "Los rombos Chen deben reservar una zona textual segura para nombres largos de relación.");
        assertTrue(crows.contains("ConceptualFigureTextFitPolicy.fittedFontSize"));
    }

    @Test
    void validationRefreshShowsCompletionFeedback() throws IOException {
        String validation = Files.readString(VALIDATION_PANEL, StandardCharsets.UTF_8);

        assertTrue(validation.contains("new Alert(type)"));
        assertTrue(validation.contains("Todo está bien"));
        assertTrue(validation.contains("Hay hallazgos que revisar"));
        assertTrue(validation.contains("refresh(true)"));
    }
}
