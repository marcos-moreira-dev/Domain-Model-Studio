package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl Tanda 18: el SideDock agrupa elementos lógicos por familias, no como lista plana. */
class LogicalBusinessElementFamiliesSourceTest {

    private static final Path MAIN = Path.of("src/main/java");
    private static final Path RESOURCES = Path.of("src/main/resources");

    @Test
    void elementFamilyCatalogShouldCoverCanonicalPrefixes() throws IOException {
        String families = readJava("com/marcosmoreira/domainmodelstudio/presentation/logicalbusiness/LogicalBusinessElementFamily.java");

        assertTrue(families.contains("Flujo operativo"));
        assertTrue(families.contains("Reglas y condiciones"));
        assertTrue(families.contains("Lenguaje, actores y evidencia"));
        assertTrue(families.contains("Estados, reportes y cálculos"));
        assertTrue(families.contains("Candidatos estructurales"));
        assertTrue(families.contains("Riesgos y preguntas pendientes"));
        assertTrue(families.contains("LogicalBusinessItemKind.MACRO_FLOW"));
        assertTrue(families.contains("LogicalBusinessItemKind.FLOW"));
        assertTrue(families.contains("LogicalBusinessItemKind.USE_CASE"));
        assertTrue(families.contains("LogicalBusinessItemKind.ACTION"));
        assertTrue(families.contains("LogicalBusinessItemKind.RULE"));
        assertTrue(families.contains("LogicalBusinessItemKind.PRECONDITION"));
        assertTrue(families.contains("LogicalBusinessItemKind.INVARIANT"));
        assertTrue(families.contains("LogicalBusinessItemKind.POSTCONDITION"));
        assertTrue(families.contains("LogicalBusinessItemKind.ACTOR"));
        assertTrue(families.contains("LogicalBusinessItemKind.CONCEPT"));
        assertTrue(families.contains("LogicalBusinessItemKind.EVIDENCE"));
        assertTrue(families.contains("LogicalBusinessItemKind.SUPPORTING_ASSUMPTION"));
        assertTrue(families.contains("LogicalBusinessItemKind.ENTITY"));
        assertTrue(families.contains("LogicalBusinessItemKind.ATTRIBUTE"));
        assertTrue(families.contains("LogicalBusinessItemKind.RELATIONSHIP"));
        assertTrue(families.contains("LogicalBusinessItemKind.PENDING_QUESTION"));
    }

    @Test
    void elementsPanelShouldRenderFamiliesAndPendingQuestions() throws IOException {
        String panel = readJava("com/marcosmoreira/domainmodelstudio/presentation/logicalbusiness/LogicalBusinessElementsPanel.java");

        assertTrue(panel.contains("Elementos lógicos por familia"));
        assertTrue(panel.contains("LogicalBusinessElementFamily.groupItems"));
        assertTrue(panel.contains("familyCard"));
        assertTrue(panel.contains("Prefijos:"));
        assertTrue(panel.contains("document.pendingQuestions()"));
        assertTrue(panel.contains("selectPendingQuestion"));
        assertFalse(panel.contains("No hay RN, PRE, INV, POST, ACC, CU u otros elementos cargados."));
    }

    @Test
    void cssShouldExposeStraightFamilyCards() throws IOException {
        String baseCss = Files.readString(RESOURCES.resolve("css/logical-business-side-panels.css"), StandardCharsets.UTF_8);
        String extraCss = Files.readString(RESOURCES.resolve("css/logical-business-side-panels-extra.css"), StandardCharsets.UTF_8);
        String css = baseCss + "\n" + extraCss;

        assertTrue(css.contains("logical-business-element-family"));
        assertTrue(css.contains("logical-business-family-flow"));
        assertTrue(css.contains("logical-business-family-rules"));
        assertTrue(css.contains("logical-business-family-language"));
        assertTrue(css.contains("logical-business-family-states"));
        assertTrue(css.contains("logical-business-family-candidates"));
        assertTrue(css.contains("logical-business-family-risks"));
        assertTrue(css.contains("logical-business-question-button"));
        assertFalse(css.contains("-fx-border-radius"));
        assertFalse(css.contains("-fx-background-radius"));
    }

    private static String readJava(String relativePath) throws IOException {
        return Files.readString(MAIN.resolve(relativePath), StandardCharsets.UTF_8);
    }
}
