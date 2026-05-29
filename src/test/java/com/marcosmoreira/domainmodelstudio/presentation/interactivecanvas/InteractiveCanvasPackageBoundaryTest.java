package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class InteractiveCanvasPackageBoundaryTest {

    private static final Path PACKAGE = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas"
    );

    @Test
    void commonCanvasMustNotDependOnConcreteDiagramFamilies() throws IOException {
        List<String> forbiddenFragments = List.of(
                ".presentation.modulemap.",
                ".presentation.behavior.",
                ".presentation.architecture.",
                ".presentation.umlclass.",
                ".presentation.wireframe.",
                ".presentation.screenflow.",
                ".presentation.rolespermissions.",
                ".presentation.datadictionary."
        );
        List<String> violations = new ArrayList<>();
        try (Stream<Path> stream = Files.walk(PACKAGE)) {
            for (Path file : stream.filter(path -> path.toString().endsWith(".java")).toList()) {
                String source = Files.readString(file, StandardCharsets.UTF_8);
                for (String fragment : forbiddenFragments) {
                    if (source.contains(fragment)) {
                        violations.add(file + " importa o menciona " + fragment);
                    }
                }
            }
        }
        assertTrue(violations.isEmpty(), "Canvas común acoplado a familias concretas:\n" + String.join("\n", violations));
    }
}
