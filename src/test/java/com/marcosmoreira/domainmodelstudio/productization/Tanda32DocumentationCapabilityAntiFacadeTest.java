package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.catalog.DefaultDiagramTypeRegistry;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeDescriptor;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Guardarraíl anti-fachada: documentación viva debe declarar límites y cubrir tipos visibles. */
class Tanda32DocumentationCapabilityAntiFacadeTest {

    private static final Path CAPABILITY_MATRIX = Path.of("docs", "producto", "MATRIZ_CAPACIDADES_REALES.md");
    private static final Path CURRENT_AUDIT = Path.of("docs", "diagnostico", "ESTADO_AUDITORIA_ACTUAL.md");
    private static final Path ROOT_PLAN = Path.of("docs", "raiz", "PLAN_TANDAS_RESTANTES.md");
    private static final Path TANDA_DOC = Path.of("docs", "raiz", "TANDA_32_TESTS_ANTI-FACHADA.md");

    @Test
    void capabilityMatrixShouldMentionEveryVisibleDiagramTypeByDisplayName() throws IOException {
        String matrix = read(CAPABILITY_MATRIX);
        for (DiagramTypeDescriptor type : new DefaultDiagramTypeRegistry().findAll()) {
            assertTrue(
                    matrix.contains(type.displayName()),
                    () -> "Matriz de capacidades no menciona tipo visible: " + type.displayName());
        }
    }

    @Test
    void liveDocumentationShouldKeepAntiFacadeTruthHierarchyAndSvgLimits() throws IOException {
        String matrix = read(CAPABILITY_MATRIX);
        String audit = read(CURRENT_AUDIT);
        String joined = matrix + "\n" + audit;
        for (String token : List.of(
                "DefaultDiagramTypeDefinitions",
                "catálogo Java",
                "toolbar/menú coherente",
                "SVG vectorial documental",
                "no WYSIWYG universal",
                "smoke manual")) {
            assertTrue(joined.contains(token), "Falta criterio anti-fachada vivo: " + token);
        }
    }

    @Test
    void rootTandaPlanShouldPreserveContinuationInstructions() throws IOException {
        String plan = read(ROOT_PLAN);
        String tanda = read(TANDA_DOC);
        for (String token : List.of(
                "Tanda 32",
                "Tests anti-fachada",
                "capacidad visible = implementación + prueba + documentación",
                "No avanzar")) {
            assertTrue((plan + "\n" + tanda).contains(token), "Falta continuidad anti-fachada en raíz: " + token);
        }
    }

    private static String read(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
