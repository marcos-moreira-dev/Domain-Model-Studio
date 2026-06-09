package com.marcosmoreira.domainmodelstudio.infrastructure.resources;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/**
 * Verifica que la modularización CSS no deje tokens visuales sueltos.
 */
class CssTokenCoverageTest {

    private static final Path CSS_DIR = Path.of("src/main/resources/css");
    private static final Pattern DMS_TOKEN = Pattern.compile("(?<![\\w-])-(?:dms|app)-[\\w-]+");
    private static final Pattern TOKEN_DECLARATION = Pattern.compile("(?m)^\\s*(-(?:dms|app)-[\\w-]+)\\s*:");
    private static final Pattern IMPORT_DECLARATION = Pattern.compile("@import url\\(\\\"([^\\\"]+)\\\"\\)");

    @Test
    void everyDmsTokenUsedByCssShouldBeDeclared() throws IOException {
        Set<String> declared = new HashSet<>();
        Set<String> used = new HashSet<>();

        for (Path file : cssFiles()) {
            String source = Files.readString(file, StandardCharsets.UTF_8);
            Matcher declarations = TOKEN_DECLARATION.matcher(source);
            while (declarations.find()) {
                declared.add(declarations.group(1));
            }
            Matcher usages = DMS_TOKEN.matcher(source);
            while (usages.find()) {
                used.add(usages.group());
            }
        }

        used.removeAll(declared);
        assertTrue(used.isEmpty(), "Tokens CSS usados sin declaración explícita: " + used);
    }

    @Test
    void appLightShouldImportOnlyExistingCssFiles() throws IOException {
        Path appLight = CSS_DIR.resolve("app-light.css");
        String source = Files.readString(appLight, StandardCharsets.UTF_8);
        List<String> missing = new ArrayList<>();
        Matcher imports = IMPORT_DECLARATION.matcher(source);
        while (imports.find()) {
            Path imported = CSS_DIR.resolve(imports.group(1));
            if (!Files.isRegularFile(imported)) {
                missing.add(imports.group(1));
            }
        }
        assertTrue(missing.isEmpty(), "Imports CSS inexistentes desde app-light.css: " + missing);
    }

    @Test
    void cssFilesShouldStaySmallEnoughToRemainTraceable() throws IOException {
        List<String> oversized = new ArrayList<>();
        for (Path file : cssFiles()) {
            String name = file.getFileName().toString();
            if (List.of("diagram-conceptual.css", "manual.css",
                    "architecture-diagram.css", "behavior-diagram.css", "data-dictionary.css",
                    "module-map.css", "screen-flow.css", "sequence-diagram.css",
                    "uml-class.css", "wireframe.css", "workbench.css").contains(name)) {
                continue;
            }
            long lines = Files.lines(file, StandardCharsets.UTF_8).count();
            if (lines > 200) {
                oversized.add(name + " tiene " + lines + " líneas");
            }
        }
        assertTrue(oversized.isEmpty(), "CSS demasiado grandes para mantenimiento fino: " + oversized);
    }

    private static List<Path> cssFiles() throws IOException {
        try (Stream<Path> files = Files.walk(CSS_DIR)) {
            return files.filter(path -> path.toString().endsWith(".css")).toList();
        }
    }
}
