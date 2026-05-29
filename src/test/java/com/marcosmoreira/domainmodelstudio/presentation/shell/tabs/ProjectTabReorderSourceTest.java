package com.marcosmoreira.domainmodelstudio.presentation.shell.tabs;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl fuente para el reordenamiento visual de pestañas de proyectos abiertos. */
class ProjectTabReorderSourceTest {

    private static final Path TAB_BAR = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/tabs/ScrollableEditorTabBarView.java");
    private static final Path TAB_CELL = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/tabs/EditorTabCellView.java");
    private static final Path SHELL_STATE = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellState.java");
    private static final Path SHELL_HANDLER = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellCommandHandler.java");
    private static final Path BATCH_EXPORT = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/ClientBatchExportCoordinator.java");

    @Test
    void tabReorderShouldStayInPresentationAndPreserveProjectState() throws Exception {
        String tabBar = Files.readString(TAB_BAR);
        String tabCell = Files.readString(TAB_CELL);
        String shellState = Files.readString(SHELL_STATE);
        String shellHandler = Files.readString(SHELL_HANDLER);
        String batchExport = Files.readString(BATCH_EXPORT);

        assertAll(
                () -> assertTrue(tabBar.contains("BiConsumer<String, String> reorderTabAfter"),
                        "La barra de pestañas debe recibir una acción de reordenamiento desacoplada."),
                () -> assertTrue(tabCell.contains("startDragAndDrop(TransferMode.MOVE)"),
                        "La celda debe permitir drag & drop visual de pestañas."),
                () -> assertTrue(shellState.contains("moveProjectTabAfter"),
                        "El orden visual debe vivir en MainShellState/editorTabs, no en DiagramProject."),
                () -> assertTrue(shellHandler.contains("reorderEditorTabAfter"),
                        "El shell debe exponer una delegación mínima para la UI."),
                () -> assertTrue(batchExport.contains("for (EditorTabViewState tab : shellState.editorTabs())"),
                        "La exportación por lote debe seguir el orden visual de las pestañas."));
    }

    @Test
    void tabReorderMustNotTouchDomainOrPersistence() throws Exception {
        String tabBar = Files.readString(TAB_BAR);
        String tabCell = Files.readString(TAB_CELL);
        String policy = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/tabs/ProjectTabOrderPolicy.java"));

        String combined = tabBar + tabCell + policy;
        assertAll(
                () -> assertTrue(!combined.contains("DmsProjectJsonWriter"),
                        "Reordenar pestañas no debe tocar persistencia .dms."),
                () -> assertTrue(!combined.contains("DiagramProject"),
                        "Reordenar pestañas no debe modificar modelos de proyecto."),
                () -> assertTrue(!combined.contains("sourceMarkdownPath"),
                        "Reordenar pestañas no debe modificar trazabilidad Markdown."));
    }
}
