package com.marcosmoreira.domainmodelstudio.application.runtime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíles de Tanda 11 para el registry runtime por tipo de proyecto. */
class RuntimeRegistryArchitectureSourceTest {

    private static final Path RUNTIME_DIR = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/application/runtime");
    private static final Path CATALOG_SERVICES = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/application/services/CatalogApplicationServices.java");

    @Test
    void runtimeRegistryShouldRemainApplicationOnlyAndJavaFxFree() throws Exception {
        for (Path file : Files.walk(RUNTIME_DIR).filter(path -> path.toString().endsWith(".java")).toList()) {
            String source = Files.readString(file);
            assertFalse(source.contains("javafx."), file + " no debe depender de JavaFX.");
            assertFalse(source.contains("presentation."), file + " no debe depender de presentation.");
            assertFalse(source.contains("infrastructure."), file + " no debe depender de infrastructure.");
        }
    }

    @Test
    void catalogServicesShouldExposeRuntimeRegistryAsCatalogFamilyConcern() throws Exception {
        String source = Files.readString(CATALOG_SERVICES);

        assertTrue(source.contains("private final DiagramTypeRuntimeRegistry diagramTypeRuntimeRegistry"));
        assertTrue(source.contains("new DefaultDiagramTypeRuntimeRegistry(DefaultDiagramTypeDefinitions.all(), payloadRuntimeRegistry)"));
        assertTrue(source.contains("public DiagramTypeRuntimeRegistry diagramTypeRuntimeRegistry()"));
        assertTrue(source.contains("public PayloadRuntimeRegistry payloadRuntimeRegistry()"));
    }
}
