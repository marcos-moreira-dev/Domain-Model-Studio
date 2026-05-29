package com.marcosmoreira.domainmodelstudio.presentation.shell;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl fuente para que batch export use la salida activa real de cada pestaña. */
class ClientBatchExportCoordinatorActiveScopeSourceTest {

    private static final Path COORDINATOR = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/ClientBatchExportCoordinator.java");
    private static final Path ITEM = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/application/batchexport/OpenProjectExportItem.java");
    private static final Path FILE_SYSTEM_EXPORTER = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/batchexport/FileSystemClientBatchExporter.java");

    @Test
    void batchExportShouldActivateTabsAndUseVisualScopedOutputForDeliverables() throws Exception {
        String coordinator = Files.readString(COORDINATOR);
        String item = Files.readString(ITEM);
        String exporter = Files.readString(FILE_SYSTEM_EXPORTER);

        assertAll(
                () -> assertTrue(coordinator.contains("projectActivator.accept(session, \"Preparando exportación por lote\")"),
                        "Debe activar cada pestaña antes de resolver output activo."),
                () -> assertTrue(coordinator.contains("activeOutputResolver.activeOutput()"),
                        "Debe usar ActiveOutputResolver, no solo la sesión persistida."),
                () -> assertTrue(coordinator.contains("ExportableOutput::visualProject"),
                        "Debe respetar vistas filtradas como UML clases."),
                () -> assertTrue(coordinator.contains("descriptor().suggestedFileStem()"),
                        "Debe usar el nombre sugerido por el output activo."),
                () -> assertTrue(item.contains("DiagramProject outputProject"),
                        "OpenProjectExportItem debe distinguir editable completo y output visual."),
                () -> assertTrue(exporter.contains("item.outputProject()"),
                        "Los entregables deben generarse desde outputProject cuando aplique."),
                () -> assertTrue(exporter.contains("item.project()"),
                        "El .dms editable debe conservar el proyecto completo."));
    }
}
