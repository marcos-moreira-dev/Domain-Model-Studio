package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

class SpecializedLayoutMovementSourceTest {

    @Test
    void specializedMoveOperationsPrepareProjectLayoutBeforePersistingPosition() throws IOException {
        List<Path> viewModels = List.of(
                Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/modulemap/ModuleMapViewModel.java"),
                Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/screenflow/ScreenFlowViewModel.java"),
                Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/wireframe/WireframeViewModel.java"),
                Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassDiagramViewModel.java"),
                Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/behavior/BehaviorDiagramViewModel.java"),
                Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/architecture/ArchitectureDiagramViewModel.java")
        );

        for (Path viewModel : viewModels) {
            String source = Files.readString(viewModel, StandardCharsets.UTF_8);
            assertTrue(source.contains("ensureProjectForLayout"), viewModel + " debe tener guardarraíl de proyecto/layout activo.");
            boolean persistsMovement = source.contains("visualLayoutService.moveNodeTo")
                    || source.contains("movePreparedNodeTo")
                    || source.contains("containerLayoutSupport.moveNode");
            assertTrue(persistsMovement, viewModel + " debe persistir movimiento en DiagramLayouts.");
            assertTrue(source.contains("notifyProjectChanged()"), viewModel + " debe propagar el proyecto actualizado para guardar/reabrir posiciones.");
        }
    }
}
