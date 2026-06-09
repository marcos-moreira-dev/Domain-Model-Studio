package com.marcosmoreira.domainmodelstudio.presentation.toolbar;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl textual: acciones visibles pero contextuales no deben quedar clicables como promesa falsa. */
class ToolbarFalsePromiseGuardSourceTest {

    @Test
    void contextualToolbarShouldAskViewModelForNonExportActionAvailability() throws IOException {
        String source = Files.readString(
                Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/toolbar/ContextualToolbarView.java"),
                StandardCharsets.UTF_8);

        assertTrue(source.contains("viewModel.diagramActionUnavailable(action.id())"),
                "La toolbar debe delegar disponibilidad contextual al ViewModel, no solo al estado de proyecto abierto.");
    }

    @Test
    void openSourceActionShouldStayClickableInsideUmlClassWorkspace() throws IOException {
        String source = Files.readString(
                Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/toolbar/MainToolbarViewModel.java"),
                StandardCharsets.UTF_8);

        assertTrue(source.contains("actionId == DiagramToolbarActionId.OPEN_UML_SOURCE"));
        assertTrue(source.contains("!DiagramTypeId.UML_CLASS.equals(activeDiagramTypeProperty().get())"));
        assertTrue(!source.contains("umlClassDiagramViewModel.selectedSourcePath().isEmpty()"),
                "Abrir código debe quedar clicable en UML Clases; el handler informa si falta clase o ruta fuente.");
    }
}
