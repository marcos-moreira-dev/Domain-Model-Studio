package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl Tanda 20: seleccionar desde el canvas no debe cortar el drag inicial. */
class Tanda20UmlClassDirectDragSourceTest {

    private static final Path STRUCTURE_PANEL = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassStructurePanel.java");
    private static final Path TANDA_DOC = Path.of(
            "docs/raiz/TANDA_20_DRAG_DIRECTO_UML_Y_LECTURA_CANVAS_CONCEPTUAL.md");

    @Test
    void sidePanelSelectionSynchronizationMustNotRefreshCanvasDuringNodePress() throws IOException {
        String source = read(STRUCTURE_PANEL);

        assertTrue(source.contains("moduleList.getSelectionModel().selectedItemProperty().addListener"));
        assertTrue(source.contains("classList.getSelectionModel().selectedItemProperty().addListener"));
        assertTrue(source.contains("memberList.getSelectionModel().selectedItemProperty().addListener"));
        assertTrue(source.contains("if (syncingControls) { return; }\n            if (current != null) {\n                viewModel.selectModuleById"),
                "La sincronización de módulo desde el canvas no debe disparar refresh que corte el drag.");
        assertTrue(source.contains("if (syncingControls) { return; }\n            if (current != null) {\n                viewModel.selectClassNodeById"),
                "La sincronización de clase desde el canvas no debe reconstruir el lienzo durante MOUSE_PRESSED.");
    }

    @Test
    void conceptualCanvasCapabilitiesAreDocumentedOnlyAsUxReference() throws IOException {
        String doc = read(TANDA_DOC);

        assertTrue(doc.contains("modelo conceptual sigue congelado"));
        assertTrue(doc.contains("Presionar y arrastrar nodo"));
        assertTrue(doc.contains("Doble clic sobre un tramo de relación"));
        assertTrue(doc.contains("Quitar punto"));
        assertTrue(doc.contains("No copiar lógica"));
    }

    private static String read(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
