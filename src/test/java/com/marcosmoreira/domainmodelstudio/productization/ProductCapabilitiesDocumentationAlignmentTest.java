package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl documental: la matriz de capacidades no debe contradecir el catálogo Java. */
class ProductCapabilitiesDocumentationAlignmentTest {

    private static final Path CAPABILITIES_MATRIX = Path.of("docs", "producto", "MATRIZ_CAPACIDADES_REALES.md");
    private static final Path USE_CASE_MATRIX = Path.of("docs", "testeo", "MATRIZ_CASOS_USO_Y_TESTS.md");
    private static final Path CATEGORIZED_MATRIX = Path.of("docs", "testeo", "MATRIZ_CASOS_USO_CATEGORIZADA.md");

    @Test
    void capabilitiesMatrixShouldDeclareJavaCatalogAsPrimarySource() throws IOException {
        String matrix = read(CAPABILITIES_MATRIX);

        assertTrue(matrix.contains("DefaultDiagramTypeDefinitions"), "Debe nombrar el catálogo oficial de tipos.");
        assertTrue(matrix.contains("DefaultDiagramCapabilityCatalog"), "Debe nombrar el catálogo oficial de capacidades.");
        assertTrue(matrix.contains("documentación derivada"), "Debe aclarar que la matriz Markdown deriva del catálogo Java.");
    }

    @Test
    void rolesPermissionsShouldDocumentTabularPngSvgMarkdownExports() throws IOException {
        String capabilities = read(CAPABILITIES_MATRIX);
        String useCases = read(USE_CASE_MATRIX);
        String categorized = read(CATEGORIZED_MATRIX);

        assertTrue(
                capabilities.contains("| Roles y permisos | Sí | Sí | Sí | Sí | Sí | Sí | Sí | No | Sí |"),
                "Roles y permisos debe declarar PNG/SVG/Markdown como matriz tabular.");
        assertTrue(useCases.contains("PNG/SVG/Markdown"), "La matriz de casos debe mencionar PNG/SVG/Markdown.");
        assertTrue(categorized.contains("PNG/SVG/Markdown tabular"), "La matriz categorizada debe aclarar exportación tabular.");
        assertTrue(categorized.contains("no canvas libre"), "Debe seguir aclarando que no es canvas libre.");
    }

    private static String read(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
