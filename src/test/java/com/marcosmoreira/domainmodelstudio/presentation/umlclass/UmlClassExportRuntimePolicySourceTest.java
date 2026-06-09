package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Regresión fuente: UML Clases debe seguir ofreciendo exportación visual aunque la vista sea grande. */
class UmlClassExportRuntimePolicySourceTest {

    @Test
    void exportSafetyPolicyDoesNotPreemptivelyBlockVisualExportsByCostOnly() throws IOException {
        String source = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassExportSafetyPolicy.java");
        assertTrue(source.contains("Tanda 8: la política ya no bloquea de entrada"));
        assertFalse(source.contains("Exportación SVG detenida: la vista UML Clases seleccionada es crítica"),
                "SVG no debe bloquearse por costo visual antes de intentar la exportación real.");
    }

    private static String read(String path) throws IOException {
        return Files.readString(Path.of(path), StandardCharsets.UTF_8);
    }
}
