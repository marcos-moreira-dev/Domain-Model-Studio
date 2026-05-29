package com.marcosmoreira.domainmodelstudio.presentation.workbench;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class WorkbenchPackageBoundaryTest {

    private static final Path PACKAGE = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/workbench"
    );

    @Test
    void workbenchMustNotDependOnConcreteDiagramFamilies() throws IOException {
        List<String> forbiddenFragments = List.of(
                ".presentation.modulemap.",
                ".presentation.behavior.",
                ".presentation.architecture.",
                ".presentation.umlclass.",
                ".presentation.wireframe.",
                ".presentation.screenflow.",
                ".presentation.rolespermissions.",
                ".presentation.datadictionary.",
                ".presentation.canvas."
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
        assertTrue(violations.isEmpty(), "Workbench común contaminado:\n" + String.join("\n", violations));
    }

    @Test
    void appLightMustImportWorkbenchCssAfterInteractiveCanvas() throws IOException {
        String source = Files.readString(Path.of("src/main/resources/css/app-light.css"), StandardCharsets.UTF_8);

        int interactiveCanvas = source.indexOf("interactive-canvas.css");
        int workbench = source.indexOf("workbench.css");

        assertTrue(workbench > interactiveCanvas, "workbench.css debe cargarse después del canvas común.");
        assertFalse(source.contains("modulemap.ModuleMap"), "El CSS ensamblador no debe declarar familias concretas en workbench.");
    }
}
