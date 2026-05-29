package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl de Tanda 11: formulario editable y diálogo de ayuda por clic. */
class LogicalBusinessEditableWorkspaceSourceTest {

    private static final Path MAIN = Path.of("src/main/java");
    private static final Path RESOURCES = Path.of("src/main/resources");

    @Test
    void structureUsesReusableClickMessageDialogInsteadOfSmallAlert() throws IOException {
        String panel = readJava("com/marcosmoreira/domainmodelstudio/presentation/logicalbusiness/LogicalBusinessStructurePanel.java");
        String dialog = readJava("com/marcosmoreira/domainmodelstudio/presentation/dialogs/ClickMessageDialog.java");

        assertTrue(panel.contains("ClickMessageDialog.showInfo"));
        assertFalse(panel.contains("new Alert"));
        assertTrue(dialog.contains("enum MessageType"));
        assertTrue(dialog.contains("setPrefWidth(760)"));
        assertTrue(dialog.contains("/icons/help-book.png"));
        assertTrue(dialog.contains("setWrapText(true)"));
    }

    @Test
    void workspaceUsesMegaFormAndApplyChangesForEditableFields() throws IOException {
        String view = readJava("com/marcosmoreira/domainmodelstudio/presentation/logicalbusiness/LogicalBusinessDocumentView.java");
        String viewModel = readJava("com/marcosmoreira/domainmodelstudio/presentation/logicalbusiness/LogicalBusinessViewModel.java");

        assertTrue(view.contains("TextArea"));
        assertTrue(view.contains("TextField"));
        assertTrue(view.contains("DatePicker"));
        assertTrue(view.contains("ComboBox"));
        assertTrue(view.contains("Actualizar documento"));
        assertTrue(viewModel.contains("applyItemEdit"));
        assertTrue(viewModel.contains("withLogicalBusinessDocument"));
    }

    @Test
    void cssKeepsStraightDesktopFormControlsAndDarkerHeader() throws IOException {
        String baseCss = readResource("css/logical-business-base.css");
        String docCss = readResource("css/logical-business-document.css");
        String clickCss = readResource("css/click-message-dialog.css");

        assertTrue(baseCss.contains("#092a45"));
        assertTrue(docCss.contains("logical-business-form-row"));
        assertTrue(docCss.contains("logical-business-apply-button"));
        assertTrue(clickCss.contains("click-message-content"));
        assertFalse(docCss.contains("-fx-border-radius"));
        assertFalse(clickCss.contains("-fx-background-radius"));
    }

    private static String readJava(String relativePath) throws IOException {
        return Files.readString(MAIN.resolve(relativePath), StandardCharsets.UTF_8);
    }

    private static String readResource(String relativePath) throws IOException {
        return Files.readString(RESOURCES.resolve(relativePath), StandardCharsets.UTF_8);
    }
}
