package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl editorial para mensajes visibles y de diagnóstico del centro de ayuda. */
class HelpCenterEditorialSpanishSourceTest {

    private static final Path APPLICATION_THEORY = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/application/theory");

    private static final Path PRESENTATION_DIALOGS = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/dialogs");

    private static final Path THEORY_TESTS = Path.of(
            "src/test/java/com/marcosmoreira/domainmodelstudio/application/theory");

    @Test
    void helpCenterTheoryMessagesShouldUseMasculineArticleForTema() throws IOException {
        String source = readDirectory(APPLICATION_THEORY) + readDirectory(PRESENTATION_DIALOGS) + readDirectory(THEORY_TESTS);

        assertFalse(source.contains("La " + "tema"), "Debe usar artículo masculino para la palabra tema.");
        assertFalse(source.contains("la " + "tema"), "Debe usar artículo masculino para la palabra tema.");
        assertFalse(source.contains("Una " + "tema"), "Debe usar artículo masculino para la palabra tema.");
        assertFalse(source.contains("una " + "tema"), "Debe usar artículo masculino para la palabra tema.");
        assertFalse(source.contains("tema académico " + "cargada"), "Debe usar concordancia masculina en tema académico.");

        assertTrue(source.contains("El tema académico"));
        assertTrue(source.contains("un tema académico editable"));
        assertTrue(source.contains("tema académico cargado"));
    }

    private static String readDirectory(Path directory) throws IOException {
        StringBuilder builder = new StringBuilder();
        try (var paths = Files.walk(directory)) {
            for (Path path : paths.filter(path -> path.toString().endsWith(".java")).toList()) {
                builder.append(Files.readString(path, StandardCharsets.UTF_8)).append('\n');
            }
        }
        return builder.toString();
    }
}
