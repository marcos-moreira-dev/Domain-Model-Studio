package com.marcosmoreira.domainmodelstudio.presentation.architecture;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class ArchitectureShapeKitIntegrationSourceTest {

    @Test
    void renderKitMustUseSpecializedC4DeploymentSymbols() throws IOException {
        String source = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/architecture/ArchitectureRenderKit.java"),
                StandardCharsets.UTF_8);

        for (String call : new String[] {
                "personSymbol()",
                "externalSystemSymbol()",
                "apiSymbol()",
                "databaseSymbol()",
                "environmentSymbol()",
                "networkSymbol()",
                "artifactSymbol()"
        }) {
            assertTrue(source.contains(call), "ArchitectureRenderKit debe usar símbolo especializado: " + call);
        }
    }

    @Test
    void cssMustExposeArchitectureRoleClasses() throws IOException {
        String css = Files.readString(Path.of("src/main/resources/css/architecture-diagram.css"), StandardCharsets.UTF_8);

        assertTrue(css.contains("architecture-canvas-node-system-central"));
        assertTrue(css.contains("architecture-canvas-node-api"));
        assertTrue(css.contains("architecture-canvas-zone-environment"));
        assertTrue(css.contains("architecture-canvas-symbol-artifact"));
    }
}
