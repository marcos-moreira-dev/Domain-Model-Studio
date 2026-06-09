package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl: los borradores compatibles quedan como infraestructura interna, no UI principal. */
class LogicalBusinessDerivationsFormalSourceTest {

    private static final Path MAIN = Path.of("src/main/java");
    private static final Path RESOURCES = Path.of("src/main/resources");

    @Test
    void compatibleDraftsShouldNotHaveSideDockPanelOrCssModule() throws IOException {
        assertFalse(MAIN.resolve("com/marcosmoreira/domainmodelstudio/presentation/logicalbusiness/LogicalBusinessDerivationsPanel.java")
                .toFile().exists());
        assertFalse(RESOURCES.resolve("css/logical-business-derivations.css").toFile().exists());

        String assembler = Files.readString(RESOURCES.resolve("css/logical-business.css"), StandardCharsets.UTF_8);
        assertFalse(assembler.contains("logical-business-derivations.css"));
    }

    @Test
    void compatibleDraftServiceShouldBeExplicitlyInternalAndReviewable() throws IOException {
        String service = readJava("com/marcosmoreira/domainmodelstudio/application/logicalbusiness/derivation/LogicalBusinessDerivationService.java");
        String packageInfo = readJava("com/marcosmoreira/domainmodelstudio/application/logicalbusiness/derivation/package-info.java");

        assertTrue(service.contains("compatibleDrafts"));
        assertTrue(service.contains("compatibleDraft("));
        assertTrue(service.contains("@Deprecated"));
        assertTrue(packageInfo.contains("Infraestructura interna"));
        assertTrue(packageInfo.contains("no respalda un módulo visible"));
        assertTrue(packageInfo.contains("no hay importación automática"));
    }

    private static String readJava(String relativePath) throws IOException {
        return Files.readString(MAIN.resolve(relativePath), StandardCharsets.UTF_8);
    }
}
