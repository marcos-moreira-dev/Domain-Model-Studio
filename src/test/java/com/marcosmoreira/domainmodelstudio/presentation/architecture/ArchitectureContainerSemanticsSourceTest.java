package com.marcosmoreira.domainmodelstudio.presentation.architecture;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class ArchitectureContainerSemanticsSourceTest {

    @Test
    void architectureViewModelMustDelegateNodeMovesToContainerSupport() throws IOException {
        String viewModel = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/architecture/ArchitectureDiagramViewModel.java");

        assertTrue(viewModel.contains("containerLayoutSupport.moveNode(currentProject, nodeId, x, y, nodes())"),
                "Mover nodos de arquitectura debe pasar por la política de contenedores visuales.");
        assertFalse(viewModel.contains("visualLayoutService.moveNodeTo(currentProject, layoutId, x, y);\n            currentProject = containerLayoutSupport.expandContainers"),
                "No se debe mover directamente y expandir después, porque los contenedores no arrastran hijos consistentemente.");
    }

    @Test
    void networkMustBeAlignedAcrossArchitectureContainerSemantics() throws IOException {
        String support = read("src/main/java/com/marcosmoreira/domainmodelstudio/application/visual/ArchitectureContainerLayoutSupport.java");
        String semantics = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/architecture/ArchitectureCanvasSemantics.java");

        assertTrue(support.contains("ArchitectureNodeKind.NETWORK"),
                "NETWORK debe participar en la misma semántica de contenedor que BOUNDARY y ENVIRONMENT.");
        assertTrue(semantics.contains("normalized.equals(\"network\")"),
                "La semántica visual del canvas ya trata network como zona; la política de layout debe acompañarla.");
    }

    private static String read(String path) throws IOException {
        return Files.readString(Path.of(path), StandardCharsets.UTF_8);
    }
}
