package com.marcosmoreira.domainmodelstudio.presentation.toolbar;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl de la tanda 13: cada acción visible debe tener policy, routing y despacho real. */
class Tanda13ToolbarRoutingSourceTest {

    private static final Path EXECUTOR = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/toolbar/DiagramToolbarActionExecutor.java");
    private static final Path MAIN_SHELL = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellCommandHandler.java");
    private static final Path CONTEXTUAL_TOOLBAR = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/toolbar/ContextualToolbarView.java");
    private static final Path VIEW_MODEL = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/toolbar/MainToolbarViewModel.java");

    @Test
    void executorMustRouteCommonToolbarActionsToShellCommands() throws IOException {
        String source = read(EXECUTOR);

        assertTrue(source.contains("case REORGANIZE_DIAGRAM -> commandHandler.requestRegenerateLayout();"));
        assertTrue(source.contains("case FIT_TO_CONTENT -> commandHandler.requestFitToContent();"));
        assertTrue(source.contains("case CENTER_DIAGRAM -> commandHandler.requestCenterDiagram();"));
        assertTrue(source.contains("case EXPORT_SVG -> commandHandler.requestExportSvg();"));
        assertTrue(source.contains("case EXPORT_PDF -> commandHandler.requestExportPdf();"));
        assertTrue(source.contains("case EXPORT_MARKDOWN -> commandHandler.requestExportMarkdown();"));
        assertTrue(source.contains("case EXPORT_PNG -> commandHandler.requestExportPng();"));
        assertTrue(source.contains("case VALIDATE_FREE_GRAPH -> commandHandler.requestValidateFreeGraph();"));
        assertTrue(source.contains("case LOGICAL_BUSINESS_SHOW_VALIDATION -> commandHandler.requestShowLogicalBusinessValidation();"));
    }

    @Test
    void contextualToolbarMustDisableExportButtonsFromActiveOutputFormats() throws IOException {
        String toolbar = read(CONTEXTUAL_TOOLBAR);
        String viewModel = read(VIEW_MODEL);

        assertTrue(toolbar.contains("exportFormatFor(action.id()).ifPresentOrElse"));
        assertTrue(toolbar.contains("control.disableProperty().bind(viewModel.exportUnavailable(format))"));
        assertTrue(toolbar.contains("case EXPORT_SVG -> Optional.of(ExportFormat.SVG);"));
        assertTrue(toolbar.contains("case EXPORT_PNG -> Optional.of(ExportFormat.PNG);"));
        assertTrue(toolbar.contains("case EXPORT_MARKDOWN -> Optional.of(ExportFormat.MARKDOWN);"));
        assertTrue(toolbar.contains("case EXPORT_PDF -> Optional.of(ExportFormat.PDF);"));
        assertTrue(toolbar.contains("case EXPORT_DICTIONARY_PDF -> Optional.of(ExportFormat.PDF);"));
        assertTrue(viewModel.contains("activeExportFormatsProperty()"));
        assertTrue(viewModel.contains("activeOutputSupports(ExportFormat format)"));
        assertTrue(viewModel.contains("exportUnavailable(ExportFormat format)"));
    }

    @Test
    void autoLayoutMustNotFallBackToConceptualCommandForUnsupportedSpecializedWorkspaces() throws IOException {
        String source = read(MAIN_SHELL);

        assertTrue(source.contains("DiagramTypeId activeType = activeProjectForOutput()"));
        assertTrue(source.contains("moduleMapCommands.requestRegenerateLayout();"));
        assertTrue(source.contains("umlClassCommands.requestRegenerateLayout();"));
        assertTrue(source.contains("behaviorDiagramCommands.requestRegenerateLayout();"));
        assertTrue(source.contains("architectureDiagramCommands.requestRegenerateLayout();"));
        assertTrue(source.contains("freeGraphCommands.requestRegenerateLayout();"));
        assertTrue(source.contains("if (activeType != null && !DiagramTypeId.CONCEPTUAL_MODEL.equals(activeType))"));
        assertTrue(source.contains("Autoorganización no disponible para el artefacto activo."));
        assertTrue(source.contains("diagramCommandHandler.requestRegenerateLayout();"));
    }

    private static String read(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
