package com.marcosmoreira.domainmodelstudio.infrastructure.resources;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl Tanda 37: la superficie CSS no debe conservar placeholders heredados. */
class CssResourceSurfaceCleanupSourceTest {

    private static final Path CSS_DIR = Path.of("src", "main", "resources", "css");

    @Test
    void obsoleteCompatibilityCssFilesShouldNotRemainAsDocumentalPlaceholders() {
        assertFalse(Files.exists(CSS_DIR.resolve("identity-polish.css")),
                "identity-polish.css era un placeholder documental; la documentación viva debe apuntar a identity-*.css reales.");
        assertFalse(Files.exists(CSS_DIR.resolve("welcome-start.css")),
                "welcome-start.css era un placeholder documental; la bienvenida está dividida en layout/actions/cards/editor-tabs.");
    }

    @Test
    void appLightShouldOnlyReferenceActiveCssFiles() throws IOException {
        String appLight = Files.readString(CSS_DIR.resolve("app-light.css"), StandardCharsets.UTF_8);

        assertFalse(appLight.contains("identity-polish.css"));
        assertFalse(appLight.contains("welcome-start.css"));
        assertTrue(appLight.contains("identity-foundation.css"));
        assertTrue(appLight.contains("welcome-start-layout.css"));
        assertTrue(appLight.contains("welcome-start-actions.css"));
        assertTrue(appLight.contains("welcome-start-cards.css"));
    }

    @Test
    void cssReadmeShouldDocumentOnlyLiveCssSurface() throws IOException {
        String readme = Files.readString(CSS_DIR.resolve("README.md"), StandardCharsets.UTF_8);

        assertTrue(readme.contains("No conservar archivos CSS vacíos o meramente documentales"));
        assertTrue(readme.contains("identity-foundation.css"));
        assertTrue(readme.contains("welcome-start-layout.css"));
        assertFalse(readme.contains("identity-polish.css"));
        assertFalse(readme.contains("welcome-start.css"));
    }
}
