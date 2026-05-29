package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl: el toolbar del Levantamiento lógico abre módulos del SideDock y no comprime el workspace. */
class LogicalBusinessToolbarContextualSourceTest {

    private static final Path MAIN = Path.of("src/main/java/com/marcosmoreira/domainmodelstudio");

    @Test
    void logicalBusinessContributorShouldUseToolbarForOperationalSideDockActions() throws IOException {
        String contributor = read("presentation/toolbar/LogicalBusinessToolbarContributor.java");

        assertTrue(contributor.contains("LOGICAL_BUSINESS_SHOW_VALIDATION"));
        assertTrue(contributor.contains("LOGICAL_BUSINESS_SHOW_TRACEABILITY"));
        assertFalse(contributor.contains("LOGICAL_BUSINESS_SHOW_DERIVATIONS"));
        assertTrue(contributor.contains("LOGICAL_BUSINESS_SHOW_STRUCTURE"));
        assertTrue(contributor.contains("LOGICAL_BUSINESS_SHOW_PROPERTIES"));
        assertTrue(contributor.contains("LOGICAL_BUSINESS_SHOW_HELP"));
        assertTrue(contributor.contains("EXPORT_MARKDOWN"));
        assertFalse(contributor.contains("ADD_LOGICAL_BUSINESS"),
                "La creación CRUD documental queda para una tanda posterior.");
    }

    @Test
    void logicalBusinessToolbarShouldRequestExistingSideDockModules() throws IOException {
        String handler = read("presentation/shell/MainShellCommandHandler.java");
        String viewModel = read("presentation/logicalbusiness/LogicalBusinessViewModel.java");
        String editorView = read("presentation/logicalbusiness/LogicalBusinessEditorView.java");
        String workbench = read("presentation/workbench/StructuredWorkbenchView.java");

        assertTrue(handler.contains("requestShowLogicalBusinessValidation"));
        assertTrue(handler.contains("SideDockModuleId.VALIDATION"));
        assertTrue(handler.contains("SideDockModuleId.TRACEABILITY"));
        assertFalse(handler.contains("SideDockModuleId.DERIVATIONS"));
        assertTrue(viewModel.contains("requestedSideDockModuleProperty"));
        assertTrue(viewModel.contains("requestSideDockModule"));
        assertTrue(editorView.contains("activateSideDockModule"));
        assertTrue(workbench.contains("activateSideDockModule(SideDockModuleId moduleId)"));
    }

    @Test
    void toolbarShouldNotIntroduceRightInspectorInsideWorkspace() throws IOException {
        String editorView = read("presentation/logicalbusiness/LogicalBusinessEditorView.java");
        String documentView = read("presentation/logicalbusiness/LogicalBusinessDocumentView.java");

        assertFalse(editorView.contains("new SplitPane"));
        assertFalse(documentView.contains("new SplitPane"));
        assertFalse(documentView.contains("setRight("));
    }

    private static String read(String relativePath) throws IOException {
        return Files.readString(MAIN.resolve(relativePath), StandardCharsets.UTF_8);
    }
}
