package com.marcosmoreira.domainmodelstudio.regression;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/** Evita que el lienzo de diagrama vuelva a parecer formulario funcional embebido. */
class RenderKitNoInteractiveFormControlsTest {

    @Test
    void renderKitsShouldNotUseInteractiveFormControlsInsideDiagramCanvas() throws IOException {
        List<String> forbiddenControls = List.of(
                "javafx.scene.control.Button",
                "javafx.scene.control.TextField",
                "javafx.scene.control.TextArea",
                "javafx.scene.control.ComboBox",
                "javafx.scene.control.TableView",
                "javafx.scene.control.ListView",
                "javafx.scene.control.TreeView",
                "javafx.scene.control.CheckBox",
                "javafx.scene.control.RadioButton",
                "javafx.scene.control.Slider",
                "javafx.scene.control.SplitPane",
                "javafx.scene.control.ScrollPane"
        );
        List<String> violations = new ArrayList<>();
        try (Stream<Path> stream = Files.walk(Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation"))) {
            for (Path file : stream
                    .filter(path -> path.getFileName().toString().endsWith("RenderKit.java")
                            || path.getFileName().toString().equals("WireframeComponentFigureFactory.java"))
                    .toList()) {
                String source = Files.readString(file, StandardCharsets.UTF_8);
                for (String forbidden : forbiddenControls) {
                    if (source.contains(forbidden)) {
                        violations.add(file + " usa " + forbidden);
                    }
                }
            }
        }
        assertTrue(violations.isEmpty(), "El render de diagramas no debe usar controles interactivos:\n" + String.join("\n", violations));
    }
}
