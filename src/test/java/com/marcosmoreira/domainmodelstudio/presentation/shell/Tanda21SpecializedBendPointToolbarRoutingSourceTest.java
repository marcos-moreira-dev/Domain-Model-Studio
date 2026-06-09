package com.marcosmoreira.domainmodelstudio.presentation.shell;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class Tanda21SpecializedBendPointToolbarRoutingSourceTest {

    @Test
    void shellRoutesDeleteBendPointToSpecializedWorkspaceBeforeConceptualFallback() throws IOException {
        String source = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellCommandHandler.java");
        assertTrue(source.contains("specializedWorkspaces.deleteSelectedBendPoint(activeProject.get())"),
                "El botón Quitar punto debe funcionar también en workspaces visuales especializados.");
        assertTrue(source.contains("conceptualModelCommands.requestDeleteSelectedBendPoint()"),
                "El modelo conceptual conserva su flujo legado de Quitar punto.");
    }

    @Test
    void specializedCoordinatorCarriesDeleteBendPointActionForVisualBindings() throws IOException {
        String source = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/SpecializedWorkspaceCoordinator.java");
        assertTrue(source.contains("BooleanSupplier deleteSelectedBendPointAction"),
                "El binding especializado debe cargar una acción de Quitar punto.");
        assertTrue(source.contains("boolean deleteSelectedBendPoint(DiagramProject activeProject)"),
                "El coordinator debe proteger el ruteo por proyecto activo.");
        assertTrue(source.contains("umlClassDiagramViewModel::deleteSelectedBendPoint"),
                "UML Clases debe participar del contrato transversal de puntos intermedios.");
        assertTrue(source.contains("moduleMapViewModel::deleteSelectedBendPoint"),
                "Mapa de módulos debe participar del contrato transversal de puntos intermedios.");
        assertTrue(source.contains("behaviorDiagramViewModel::deleteSelectedBendPoint"),
                "Diagramas de comportamiento deben participar donde su perfil lo permita.");
        assertTrue(source.contains("freeGraphViewModel::deleteSelectedBendPoint"),
                "Grafo libre debe participar del contrato transversal de puntos intermedios.");
    }

    @Test
    void visualViewActionsExposeRegisteredDeleteBendPointOperation() throws IOException {
        String source = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/VisualDiagramViewActions.java");
        assertTrue(source.contains("registerDeleteSelectedBendPointAction"),
                "Los ViewModels visuales deben registrar la acción real del centro del diagrama.");
        assertTrue(source.contains("public boolean deleteSelectedBendPoint()"),
                "La acción debe devolver si realmente quitó un punto.");
        assertTrue(source.contains("Selecciona un punto intermedio de una relación para quitarlo."),
                "Debe haber feedback claro cuando no hay punto seleccionado.");
    }

    private static String read(String path) throws IOException {
        return Files.readString(Path.of(path), StandardCharsets.UTF_8);
    }
}
