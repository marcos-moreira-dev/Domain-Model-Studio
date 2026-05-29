package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.catalog.DefaultDiagramTypeRegistry;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeDescriptor;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl documental para que los tipos visibles no queden fuera del registro de casos de uso. */
class UseCaseDocumentationCoverageTest {

    private static final Path COVERAGE_MATRIX = Path.of(
            "docs", "productizacion", "casos-uso", "09_matriz_cobertura_casos_uso_por_tipo.md");

    @Test
    void everyVisibleDiagramTypeShouldBeRegisteredInUseCaseCoverageMatrix() throws IOException {
        String matrix = Files.readString(COVERAGE_MATRIX, StandardCharsets.UTF_8);

        for (DiagramTypeDescriptor type : new DefaultDiagramTypeRegistry().findAll()) {
            assertTrue(
                    matrix.contains("`" + type.id().value() + "`"),
                    "Falta registrar tipo en matriz de casos de uso: " + type.id().value());
            assertTrue(
                    matrix.contains(type.displayName()),
                    "Falta nombre visible en matriz de casos de uso: " + type.displayName());
        }
    }

    @Test
    void coverageMatrixShouldPointToGeneralMenusExportExamplesAndAiResources() throws IOException {
        String matrix = Files.readString(COVERAGE_MATRIX, StandardCharsets.UTF_8);

        assertTrue(matrix.contains("Menú y barra general"));
        assertTrue(matrix.contains("Toolbar contextual"));
        assertTrue(matrix.contains("Ejemplo UENS"));
        assertTrue(matrix.contains("Recursos IA"));
        assertTrue(matrix.contains("docs/productizacion/casos-uso/01_controles_generales_gestion_proyectos.md"));
        assertTrue(matrix.contains("docs/productizacion/casos-uso/02_entrada_salida_exportacion.md"));
    }
}
