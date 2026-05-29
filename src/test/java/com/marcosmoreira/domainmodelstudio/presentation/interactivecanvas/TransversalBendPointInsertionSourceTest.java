package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Guardarraíl para que agregar puntos intermedios no deforme rutas ya editadas. */
class TransversalBendPointInsertionSourceTest {

    @Test
    void visualLayoutServiceShouldInsertBendPointInNearestSegment() throws IOException {
        String source = read("src/main/java/com/marcosmoreira/domainmodelstudio/application/visual/VisualLayoutService.java");

        assertTrue(source.contains("insertBendPointInNearestSegment"),
                "El servicio visual debe insertar el punto en el segmento más cercano, no solo al final.");
        assertTrue(source.contains("nearestSegmentIndex"),
                "La inserción debe calcular el segmento más cercano de la ruta existente.");
        assertTrue(source.contains("bendPointIndexAt"),
                "El servicio debe poder devolver el índice real del punto insertado para seleccionar el handle correcto.");
    }

    @Test
    void realViewModelsShouldSelectTheInsertedBendPointIndex() throws IOException {
        List<String> files = List.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/architecture/ArchitectureDiagramViewModel.java",
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/behavior/BehaviorDiagramViewModel.java",
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/freegraph/FreeGraphViewModelCore.java",
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/modulemap/ModuleMapViewModel.java",
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/screenflow/ScreenFlowViewModel.java",
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassDiagramLayoutCoordinator.java");

        for (String file : files) {
            String source = read(file);
            assertTrue(source.contains("bendPointIndexAt"),
                    file + " debe seleccionar el índice real del punto insertado.");
            assertFalse(source.contains("bendPoints().size() - 1"),
                    file + " no debe asumir que el punto nuevo siempre queda al final de la ruta.");
        }
    }

    private static String read(String path) throws IOException {
        return Files.readString(Path.of(path), StandardCharsets.UTF_8);
    }
}
