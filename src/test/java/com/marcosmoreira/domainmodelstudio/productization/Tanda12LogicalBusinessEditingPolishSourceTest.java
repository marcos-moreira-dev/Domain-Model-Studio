package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl de Tanda 12: edición navegable, ayuda operativa y statusbar desplazable. */
class Tanda12LogicalBusinessEditingPolishSourceTest {

    private static final Path MAIN = Path.of("src/main/java");
    private static final Path RESOURCES = Path.of("src/main/resources");

    @Test
    void linkedElementsNavigateFromWorkspace() throws IOException {
        String linkedRows = readJava("com/marcosmoreira/domainmodelstudio/presentation/logicalbusiness/LogicalBusinessLinkedRows.java");
        String viewModel = readJava("com/marcosmoreira/domainmodelstudio/presentation/logicalbusiness/LogicalBusinessViewModel.java");

        assertTrue(linkedRows.contains("Hyperlink"));
        assertTrue(linkedRows.contains("selectReference"));
        assertTrue(viewModel.contains("selectReference"));
    }

    @Test
    void reusableClickDialogUsesProjectIconsAndComfortableWidth() throws IOException {
        String dialog = readJava("com/marcosmoreira/domainmodelstudio/presentation/dialogs/ClickMessageDialog.java");
        String css = readResource("css/click-message-dialog.css");

        assertTrue(dialog.contains("/icons/help-book.png"));
        assertTrue(dialog.contains("/icons/validate.png"));
        assertTrue(dialog.contains("/icons/close.png"));
        assertTrue(dialog.contains("setPrefWidth(760)"));
        assertTrue(css.contains("click-message-image-icon"));
    }

    @Test
    void operationalHelpIsRealForWorkbenchesExceptConceptualCanvas() throws IOException {
        String modules = readJava("com/marcosmoreira/domainmodelstudio/presentation/workbench/WorkbenchSideDockModules.java");
        String content = readJava("com/marcosmoreira/domainmodelstudio/presentation/sidedock/OperationalHelpContent.java");

        assertTrue(modules.contains("operationalHelp"));
        assertTrue(modules.contains("WorkspaceKind.CONCEPTUAL_CANVAS"));
        assertTrue(content.contains("Qué puedes hacer aquí"));
        assertTrue(content.contains("guía académica"));
        assertFalse(content.contains("placeholder"));
    }

    @Test
    void statusBarAllowsHorizontalScrollingForLongMessages() throws IOException {
        String status = readJava("com/marcosmoreira/domainmodelstudio/presentation/statusbar/StatusBarView.java");
        String css = readResource("css/statusbar.css");

        assertTrue(status.contains("ScrollPane"));
        assertTrue(status.contains("AS_NEEDED"));
        assertTrue(css.contains("status-bar-scroll"));
    }

    private static String readJava(String relativePath) throws IOException {
        return Files.readString(MAIN.resolve(relativePath), StandardCharsets.UTF_8);
    }

    private static String readResource(String relativePath) throws IOException {
        return Files.readString(RESOURCES.resolve(relativePath), StandardCharsets.UTF_8);
    }
}
