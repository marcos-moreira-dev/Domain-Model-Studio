package com.marcosmoreira.domainmodelstudio.presentation.sidedock;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl de Tanda 29: ayuda operativa concreta por tipo/familia real. */
class Tanda29OperationalHelpByTypeSourceTest {

    private static final Path CATALOG = Path.of(
            "src", "main", "java", "com", "marcosmoreira", "domainmodelstudio",
            "presentation", "sidedock", "OperationalHelpCatalog.java");
    private static final Path CONTENT = Path.of(
            "src", "main", "java", "com", "marcosmoreira", "domainmodelstudio",
            "presentation", "sidedock", "OperationalHelpContent.java");

    @Test
    void operationalHelpShouldMentionRealBehaviorAndArchitectureTypes() throws IOException {
        String source = Files.readString(CATALOG, StandardCharsets.UTF_8);
        assertAll(
                () -> assertTrue(source.contains("BPMN básico")),
                () -> assertTrue(source.contains("flujo operativo")),
                () -> assertTrue(source.contains("UML actividad")),
                () -> assertTrue(source.contains("UML secuencia")),
                () -> assertTrue(source.contains("UML estados")),
                () -> assertTrue(source.contains("UML casos de uso")),
                () -> assertTrue(source.contains("C4 contexto")),
                () -> assertTrue(source.contains("C4 contenedores")),
                () -> assertTrue(source.contains("Despliegue técnico"))
        );
    }

    @Test
    void operationalHelpShouldSeparateSoftwareUseFromAcademicGuideAndAiResources() throws IOException {
        String source = Files.readString(CONTENT, StandardCharsets.UTF_8);
        assertAll(
                () -> assertTrue(source.contains("Ayuda de herramienta")),
                () -> assertTrue(source.contains("Guía académica")),
                () -> assertTrue(source.contains("Recursos IA")),
                () -> assertFalse(source.contains("reemplaza la referencia académica"))
        );
    }

    @Test
    void operationalHelpShouldKeepWireframesConservative() throws IOException {
        String source = Files.readString(CATALOG, StandardCharsets.UTF_8);
        assertAll(
                () -> assertTrue(source.contains("maquetas estructurales")),
                () -> assertTrue(source.contains("No es Figma")),
                () -> assertTrue(source.contains("prototipado interactivo")),
                () -> assertTrue(source.contains("enfoque conservador") || source.contains("Alcance conservador"))
        );
    }
}
