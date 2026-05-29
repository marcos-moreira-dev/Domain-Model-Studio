package com.marcosmoreira.domainmodelstudio.presentation.workbench;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Guardarraíles de Tanda 33 para no volver a duplicar loading/listener en ViewModels. */
class VisualViewModelsProjectChangeSupportSourceTest {

    private static final Path PRESENTATION = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation");

    private static final List<Path> MIGRATED_VIEW_MODELS = List.of(
            PRESENTATION.resolve("architecture/ArchitectureDiagramViewModel.java"),
            PRESENTATION.resolve("behavior/BehaviorDiagramViewModel.java"),
            PRESENTATION.resolve("datadictionary/DataDictionaryViewModel.java"),
            PRESENTATION.resolve("freegraph/FreeGraphViewModelCore.java"),
            PRESENTATION.resolve("logicalbusinessgraph/LogicalBusinessGraphViewModel.java"),
            PRESENTATION.resolve("modulemap/ModuleMapViewModel.java"),
            PRESENTATION.resolve("rolespermissions/RolesPermissionsViewModel.java"),
            PRESENTATION.resolve("screenflow/ScreenFlowViewModel.java"),
            PRESENTATION.resolve("umlclass/UmlClassDiagramViewModel.java"),
            PRESENTATION.resolve("wireframe/WireframeViewModel.java"));

    @Test
    void supportClassCentralizesLoadingAndNotificationContract() throws IOException {
        String support = read(PRESENTATION.resolve("workbench/ProjectChangeSupport.java"));

        assertTrue(support.contains("private Consumer<DiagramProject> projectChangeListener = project -> { };"));
        assertTrue(support.contains("public void runLoading(Runnable action)"));
        assertTrue(support.contains("public void notifyChanged(DiagramProject project)"));
        assertTrue(support.contains("if (!loading && project != null)"));
    }

    @Test
    void migratedViewModelsUseSharedProjectChangeSupport() throws IOException {
        for (Path viewModel : MIGRATED_VIEW_MODELS) {
            String source = read(viewModel);
            assertTrue(source.contains("ProjectChangeSupport"), viewModel + " debe usar ProjectChangeSupport");
            assertTrue(source.contains("projectChangeSupport.runLoading"), viewModel + " debe encapsular carga/limpieza");
            assertTrue(source.contains("projectChangeSupport.notifyChanged(currentProject)"), viewModel + " debe notificar por soporte común");
            assertFalse(source.contains("private boolean loading"), viewModel + " no debe reintroducir bandera loading local");
            assertFalse(source.contains("projectChangeListener = project ->"), viewModel + " no debe reintroducir listener local");
        }
    }

    @Test
    void conceptualLegacyCanvasIsNotTouchedByThisBatch() throws IOException {
        String canvasViewModel = read(PRESENTATION.resolve("canvas/DiagramCanvasViewModel.java"));
        String inspectorViewModel = read(PRESENTATION.resolve("inspector/InspectorViewModel.java"));

        assertFalse(canvasViewModel.contains("ProjectChangeSupport"));
        assertFalse(inspectorViewModel.contains("ProjectChangeSupport"));
    }

    private String read(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
