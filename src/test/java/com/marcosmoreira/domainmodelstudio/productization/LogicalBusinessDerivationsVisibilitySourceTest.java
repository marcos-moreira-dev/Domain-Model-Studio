package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl Tanda 17: artefactos compatibles deja de ser módulo principal del Levantamiento lógico. */
class LogicalBusinessDerivationsVisibilitySourceTest {

    private static final Path MAIN = Path.of("src/main/java");

    @Test
    void sideDockShouldExposeOnlyLogicalExpedientModules() throws IOException {
        String editor = read("com/marcosmoreira/domainmodelstudio/presentation/logicalbusiness/LogicalBusinessEditorView.java");
        String moduleIds = read("com/marcosmoreira/domainmodelstudio/presentation/sidedock/SideDockModuleId.java");

        assertTrue(editor.contains("SideDockModuleId.PALETTE"));
        assertTrue(editor.contains("SideDockModuleId.ENTITIES"));
        assertTrue(editor.contains("SideDockModuleId.VALIDATION"));
        assertTrue(editor.contains("SideDockModuleId.TRACEABILITY"));
        assertTrue(editor.contains("SideDockModuleId.HELP"));
        assertFalse(editor.contains("LogicalBusinessDerivationsPanel"));
        assertFalse(editor.contains("SideDockModuleId.DERIVATIONS"));
        assertFalse(moduleIds.contains("DERIVATIONS"));
    }

    @Test
    void toolbarAndCommandHandlerShouldNotOpenDerivations() throws IOException {
        String contributor = read("com/marcosmoreira/domainmodelstudio/presentation/toolbar/LogicalBusinessToolbarContributor.java");
        String handler = read("com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellCommandHandler.java");
        String executor = read("com/marcosmoreira/domainmodelstudio/presentation/toolbar/DiagramToolbarActionExecutor.java");

        assertFalse(contributor.contains("LOGICAL_BUSINESS_SHOW_DERIVATIONS"));
        assertFalse(contributor.contains("Artefactos"));
        assertFalse(handler.contains("requestShowLogicalBusinessDerivations"));
        assertTrue(executor.contains("case LOGICAL_BUSINESS_SHOW_DERIVATIONS -> false"));
    }

    @Test
    void treeAndDocumentViewShouldStayInsideLogicalExpedient() throws IOException {
        String factory = read("com/marcosmoreira/domainmodelstudio/presentation/logicalbusiness/LogicalBusinessTreeModelFactory.java");
        String structure = read("com/marcosmoreira/domainmodelstudio/presentation/logicalbusiness/LogicalBusinessStructurePanel.java");
        String document = read("com/marcosmoreira/domainmodelstudio/presentation/logicalbusiness/LogicalBusinessDocumentView.java");

        assertFalse(factory.contains("LogicalBusinessDerivationDraft"));
        assertFalse(factory.contains("derivationsGroup"));
        assertFalse(factory.contains("LogicalBusinessSelection.derivation"));
        assertFalse(structure.contains("derivationDrafts()"));
        assertFalse(document.contains("renderDerivation"));
        assertFalse(document.contains("selectedDerivationDraft"));
        assertTrue(factory.contains("Trazas internas"));
        assertTrue(factory.contains("Entidades candidatas"));
    }

    private static String read(String relativePath) throws IOException {
        return Files.readString(MAIN.resolve(relativePath), StandardCharsets.UTF_8);
    }
}
