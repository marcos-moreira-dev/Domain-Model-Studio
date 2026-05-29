package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl Tanda 15/18: arrastrar un módulo UML debe mover también sus clases internas visibles. */
class Tanda15UmlClassModuleDragSourceTest {

    @Test
    void movingModuleMustMoveVisibleClassesInsideIt() throws IOException {
        String coordinator = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassDiagramLayoutCoordinator.java");
        String adapter = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassCanvasAdapter.java");

        assertTrue(coordinator.contains("void moveModuleTo(String moduleId, double x, double y)"));
        assertTrue(coordinator.contains("classesInModule(moduleId)"));
        assertTrue(coordinator.contains("movePreparedNodeByIfPresent(VisualElementLayoutIds.umlClass"));
        assertTrue(adapter.contains("viewModel.moveModuleTo"),
                "El adapter debe delegar el commit del módulo al coordinator UML, que mueve módulo + clases internas.");
    }

    @Test
    void moduleDragPreviewAndCommitMustStayAligned() throws IOException {
        String adapter = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassCanvasAdapter.java");
        String coordinator = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassDiagramLayoutCoordinator.java");

        assertTrue(adapter.contains("addClassIdsForModule(result, moduleId)"),
                "La previsualización debe incluir clases internas del módulo.");
        assertTrue(coordinator.contains("movePreparedNodeByIfPresent(VisualElementLayoutIds.umlClass"),
                "El commit debe persistir el desplazamiento de clases internas desde el coordinator.");
        assertFalse(adapter.contains("moveVisibleClassesInsideModuleBy"),
                "El adapter no debe duplicar el delta que ya aplica el coordinator.");
    }

    private static String read(String path) throws IOException {
        return Files.readString(Path.of(path), StandardCharsets.UTF_8);
    }
}
