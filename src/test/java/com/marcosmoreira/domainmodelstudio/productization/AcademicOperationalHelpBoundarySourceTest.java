package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl de Tanda 32: guía académica, ayuda operativa y recursos IA no se mezclan. */
class AcademicOperationalHelpBoundarySourceTest {

    private static final Path MAIN = Path.of("src/main/java");

    @Test
    void menuHelpShouldBeAcademicAndSideDockHelpShouldRemainOperational() throws IOException {
        String manualDialog = readJava("com/marcosmoreira/domainmodelstudio/presentation/dialogs/ManualDialog.java");
        String manualContent = readJava("com/marcosmoreira/domainmodelstudio/presentation/dialogs/ManualContent.java");
        String operationalHelp = readJava("com/marcosmoreira/domainmodelstudio/presentation/sidedock/OperationalHelpContent.java");
        String standardModules = readJava("com/marcosmoreira/domainmodelstudio/presentation/sidedock/StandardSideDockModules.java");

        assertTrue(manualDialog.contains("Guía académica"));
        assertTrue(manualContent.contains("referencia académica integrada"));
        assertTrue(manualContent.contains("no es repetir botones de la interfaz"));
        assertFalse(manualDialog.contains("Guía operativa de Domain Model Studio"));

        assertTrue(operationalHelp.contains("Ayuda operativa breve del módulo activo"));
        assertTrue(operationalHelp.contains("Ayuda de herramienta: operación del módulo activo"));
        assertTrue(operationalHelp.contains("SideDock: uso de la herramienta"));
        assertTrue(operationalHelp.contains("Guía académica: teoría, notación"));
        assertTrue(standardModules.contains("SideDockModuleId.HELP"));
    }

    private static String readJava(String relativePath) throws IOException {
        return Files.readString(MAIN.resolve(relativePath), StandardCharsets.UTF_8);
    }
}
