package com.marcosmoreira.domainmodelstudio.presentation.drawing;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/** Guardarraíles para evitar que los shape kits se vuelvan un catálogo acoplado. */
class ShapeKitsBoundaryTest {

    private static final Path PACKAGE = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/drawing"
    );

    @Test
    void shapeKitsMustExistAsFamilyPackages() {
        for (String path : new String[] {
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/drawing/uml/UmlShapeKit.java",
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/drawing/bpmn/BpmnShapeKit.java",
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/drawing/c4/C4ShapeKit.java",
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/drawing/admin/AdminShapeKit.java",
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/drawing/wireframe/WireframeShapeKit.java"
        }) {
            assertTrue(Files.exists(Path.of(path)), "Debe existir shape kit: " + path);
        }
    }

    @Test
    void shapeKitsMustNotImportConcretePresentationModules() throws IOException {
        List<String> forbiddenFragments = List.of(
                ".presentation.modulemap.",
                ".presentation.behavior.",
                ".presentation.architecture.",
                ".presentation.umlclass.",
                ".presentation.wireframe.",
                ".presentation.screenflow.",
                ".presentation.rolespermissions.",
                ".presentation.datadictionary.",
                ".presentation.canvas.",
                ".presentation.shell."
        );
        List<String> violations = new ArrayList<>();
        try (Stream<Path> stream = Files.walk(PACKAGE)) {
            for (Path file : stream.filter(path -> path.toString().endsWith("ShapeKit.java")).toList()) {
                String source = Files.readString(file, StandardCharsets.UTF_8);
                for (String fragment : forbiddenFragments) {
                    if (source.contains(fragment)) {
                        violations.add(file + " importa o menciona " + fragment);
                    }
                }
            }
        }
        assertTrue(violations.isEmpty(), "Shape kits acoplados a módulos concretos:\n" + String.join("\n", violations));
    }
}
