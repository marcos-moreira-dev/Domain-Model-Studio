package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl de Tanda 6 para mantener la UI documental del levantamiento lógico sin canvas ni fachadas. */
class LogicalBusinessDocumentalUiSourceTest {

    private static final Path MAIN = Path.of("src/main/java");

    @Test
    void logicalBusinessUsesStructuredWorkbenchAndModularSideDock() throws IOException {
        String editor = read("com/marcosmoreira/domainmodelstudio/presentation/logicalbusiness/LogicalBusinessEditorView.java");
        String workbench = read("com/marcosmoreira/domainmodelstudio/presentation/workbench/StructuredWorkbenchView.java");
        String modules = read("com/marcosmoreira/domainmodelstudio/presentation/workbench/WorkbenchSideDockModules.java");
        String moduleIds = read("com/marcosmoreira/domainmodelstudio/presentation/sidedock/SideDockModuleId.java");

        assertTrue(editor.contains("StructuredWorkbenchView"));
        assertTrue(editor.contains("new StructuredWorkbenchDescriptor"));
        assertTrue(editor.contains("Ficha rápida"));
        assertTrue(editor.contains("WorkspaceKind.LOGICAL_BUSINESS_DOCUMENT"));
        assertTrue(editor.contains("SideDockModuleId.PALETTE"));
        assertTrue(editor.contains("SideDockModuleId.ENTITIES"));
        assertTrue(editor.contains("SideDockModuleId.VALIDATION"));
        assertTrue(editor.contains("SideDockModuleId.TRACEABILITY"));
        assertTrue(editor.contains("SideDockModuleId.EXPORT"));
        assertFalse(editor.contains("SideDockModuleId.DERIVATIONS"));
        assertTrue(editor.contains("SideDockModuleId.HELP"));
        assertTrue(workbench.contains("List<SideDockModule> additionalModules"));
        assertTrue(modules.contains("additionalModules.forEach(registry::register)"));
        assertTrue(moduleIds.contains("TRACEABILITY"));
        assertFalse(moduleIds.contains("DERIVATIONS"));
    }

    @Test
    void logicalBusinessDoesNotReuseConceptualCanvasOrDiagramWorkbench() throws IOException {
        String editor = read("com/marcosmoreira/domainmodelstudio/presentation/logicalbusiness/LogicalBusinessEditorView.java");
        String allLogicalBusiness = readPackage("com/marcosmoreira/domainmodelstudio/presentation/logicalbusiness");

        assertFalse(editor.contains("DiagramWorkbenchView"));
        assertFalse(allLogicalBusiness.contains("DiagramCanvasView"));
        assertFalse(allLogicalBusiness.contains("CONCEPTUAL_CANVAS"));
        assertFalse(allLogicalBusiness.contains("presentation.canvas"));
    }

    @Test
    void catalogExposesDocumentMvpWithMarkdownButWithoutVisualFalsePromises() throws IOException {
        String definitions = read("com/marcosmoreira/domainmodelstudio/application/catalog/definitions/DiagramTypeDefinitionFactory.java");
        String profiles = read("com/marcosmoreira/domainmodelstudio/application/catalog/definitions/DiagramCapabilityProfiles.java");
        String routing = read("com/marcosmoreira/domainmodelstudio/presentation/workspace/WorkspaceTypeRoutingPolicy.java");
        String profile = read("com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/profile/DiagramInteractionProfileResolver.java");

        assertTrue(definitions.contains("DiagramWorkspaceKind.LOGICAL_BUSINESS_DOCUMENT"));
        assertTrue(definitions.contains("DiagramCapabilityProfiles.logicalBusinessDocument()"));
        String logicalBusinessDocumentProfile = slice(profiles, "public static DiagramCapabilitySet logicalBusinessDocument", "public static DiagramCapabilitySet logicalBusinessGraphVisual");
        assertTrue(logicalBusinessDocumentProfile.contains("DiagramCapability.CREATE_PROJECT"));
        assertTrue(logicalBusinessDocumentProfile.contains("DiagramCapability.SHOW_DOCUMENT_OUTPUT"));
        assertTrue(logicalBusinessDocumentProfile.contains("DiagramCapability.MANUAL_EDITING"));
        assertTrue(logicalBusinessDocumentProfile.contains("DiagramCapability.SAVE_DMS"));
        assertTrue(logicalBusinessDocumentProfile.contains("DiagramCapability.LOAD_DMS"));
        assertFalse(logicalBusinessDocumentProfile.contains("EXPORT_PNG"));
        assertFalse(logicalBusinessDocumentProfile.contains("EXPORT_SVG"));
        assertTrue(logicalBusinessDocumentProfile.contains("EXPORT_PDF"));
        assertTrue(logicalBusinessDocumentProfile.contains("IMPORT_MARKDOWN"));
        assertTrue(logicalBusinessDocumentProfile.contains("EXPORT_MARKDOWN"));
        assertTrue(routing.contains("LOGICAL_BUSINESS_DOCUMENT"));
        assertTrue(routing.contains("WorkspaceKind.LOGICAL_BUSINESS_DOCUMENT"));
        assertTrue(profile.contains("DiagramTypeId.LOGICAL_BUSINESS_INTAKE"));
        assertTrue(profile.contains("LOGICAL_BUSINESS_DOCUMENT"));
    }

    @Test
    void shellCanCreateAndOpenLogicalBusinessWorkspace() throws IOException {
        String factory = read("com/marcosmoreira/domainmodelstudio/presentation/shell/NewProjectFactory.java");
        String coordinator = read("com/marcosmoreira/domainmodelstudio/presentation/shell/SpecializedWorkspaceCoordinator.java");
        String shell = read("com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellView.java");
        String composition = read("com/marcosmoreira/domainmodelstudio/presentation/PresentationCompositionRoot.java");

        assertTrue(factory.contains("createLogicalBusinessIntake"));
        assertTrue(factory.contains("LogicalBusinessDocument.blank"));
        assertTrue(factory.contains("withLogicalBusinessDocument"));
        assertTrue(coordinator.contains("LogicalBusinessViewModel"));
        assertTrue(coordinator.contains("DiagramTypeId.LOGICAL_BUSINESS_INTAKE::equals"));
        assertTrue(shell.contains("LogicalBusinessEditorView"));
        assertTrue(shell.contains("WorkspaceKind.LOGICAL_BUSINESS_DOCUMENT"));
        assertTrue(composition.contains("new LogicalBusinessViewModel"));
    }

    private static String read(String relativePath) throws IOException {
        return Files.readString(MAIN.resolve(relativePath), StandardCharsets.UTF_8);
    }

    private static String readPackage(String relativePackage) throws IOException {
        Path directory = MAIN.resolve(relativePackage);
        StringBuilder result = new StringBuilder();
        try (var files = Files.walk(directory)) {
            for (Path file : files.filter(path -> path.toString().endsWith(".java")).toList()) {
                result.append(Files.readString(file, StandardCharsets.UTF_8)).append('\n');
            }
        }
        return result.toString();
    }

    private static String slice(String text, String startToken, String endToken) {
        int start = text.indexOf(startToken);
        int end = text.indexOf(endToken, start + startToken.length());
        if (start < 0 || end < 0) {
            return text;
        }
        return text.substring(start, end);
    }
}
