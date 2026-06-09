package com.marcosmoreira.domainmodelstudio.infrastructure.resources;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

/** Guardarraíl Tanda 37: los iconos declarados por la toolbar deben existir como PNG internos. */
class ToolbarIconResourceCoverageSourceTest {

    private static final Path TOOLBAR_ICON = Path.of(
            "src", "main", "java", "com", "marcosmoreira", "domainmodelstudio",
            "presentation", "toolbar", "ToolbarIcon.java");
    private static final Path ICONS_DIR = Path.of("src", "main", "resources", "icons");
    private static final Pattern ICON_DECLARATION = Pattern.compile("\\(\\\"([^\\\"]+\\.png)\\\"\\)");

    @Test
    void everyToolbarIconDeclarationShouldResolveToInternalPng() throws IOException {
        String source = Files.readString(TOOLBAR_ICON, StandardCharsets.UTF_8);
        Matcher matcher = ICON_DECLARATION.matcher(source);
        Set<String> missing = new TreeSet<>();
        Set<String> declared = new TreeSet<>();

        while (matcher.find()) {
            String fileName = matcher.group(1);
            declared.add(fileName);
            if (!Files.isRegularFile(ICONS_DIR.resolve(fileName))) {
                missing.add(fileName);
            }
        }

        assertTrue(declared.size() >= 60, "La toolbar debe seguir declarando su catálogo amplio de iconos internos.");
        assertTrue(missing.isEmpty(), "Iconos declarados por ToolbarIcon que no existen en src/main/resources/icons: " + missing);
    }

    @Test
    void iconsReadmeShouldExplainRuntimeContract() throws IOException {
        String readme = Files.readString(ICONS_DIR.resolve("README.md"), StandardCharsets.UTF_8);

        assertTrue(readme.contains("ImageView"));
        assertTrue(readme.contains(".png"));
        assertTrue(readme.contains("No hay dependencia de SVG ni fuentes de glifos"));
        assertFalse(readme.contains("histórico"), "El README de iconos debe describir contrato vivo, no bitácora histórica.");
    }
}
