package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl de Tanda 68: selección legible sin aplicar stroke/fill de Shape a Text. */
final class Tanda68SelectionTargetsSkipTextSourceTest {

    @Test
    void selectionTargetStyleShouldNotBeAppliedToTextNodes() throws IOException {
        String registry = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/CanvasNodeVisualRegistry.java"));

        assertTrue(registry.contains("import javafx.scene.text.Text;"));
        assertTrue(registry.contains("visual instanceof Shape && !(visual instanceof Text)"),
                "Text también es Shape en JavaFX; no debe recibir stroke/fill de selección.");
    }

    @Test
    void moduleContainersShouldUseChildTextFootprint() throws IOException {
        String policy = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/application/visual/AdminApplicationsLayoutPolicy.java"));

        assertTrue(policy.contains("moduleGroupFootprints(document)"));
        assertTrue(policy.contains("fittedModuleChildSize(module)"));
        assertTrue(policy.contains("moduleGroupWidth(children, footprint.maxChildWidth())"));
    }
}
