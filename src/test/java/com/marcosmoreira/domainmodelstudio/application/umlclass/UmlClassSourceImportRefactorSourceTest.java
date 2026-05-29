package com.marcosmoreira.domainmodelstudio.application.umlclass;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl de Tanda 34: el constructor de vistas UML no debe volver a absorber ranking/resumen. */
class UmlClassSourceImportRefactorSourceTest {

    private static final Path UML_APP = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/application/umlclass");
    private static final Path VIEW_BUILDER = UML_APP.resolve("SourceCodeUmlClassViewBuilder.java");
    private static final Path SUMMARY_POLICY = UML_APP.resolve("SourceCodeUmlSummarySelectionPolicy.java");
    private static final Path TANDA = Path.of("docs/desarrollo/TANDA_034_REFACTOR_UML_CLASES_SOURCE_IMPORT.md");

    @Test
    void viewBuilderShouldDelegateSummarySelectionToPolicy() throws Exception {
        String builder = Files.readString(VIEW_BUILDER, StandardCharsets.UTF_8);
        String policy = Files.readString(SUMMARY_POLICY, StandardCharsets.UTF_8);

        assertTrue(builder.contains("SourceCodeUmlSummarySelectionPolicy"));
        assertTrue(builder.contains("summarySelectionPolicy.select(project, typeIdMap, relationIdMap)"));
        assertFalse(builder.contains("private int rolePriority"));
        assertFalse(builder.contains("private int relationPriority"));
        assertTrue(policy.contains("private int rolePriority"));
        assertTrue(policy.contains("case API_CALL -> 100"));
        assertTrue(policy.contains("SUMMARY_MAX_CLASSES = 120"));
    }

    @Test
    void refactorShouldKeepSourceImportInApplicationLayer() throws Exception {
        String policy = Files.readString(SUMMARY_POLICY, StandardCharsets.UTF_8);

        assertFalse(policy.contains("javafx"));
        assertFalse(policy.contains("presentation"));
        assertTrue(policy.contains("ParsedCodeProject"));
        assertTrue(policy.contains("Selection select"));
    }

    @Test
    void documentationShouldExplainWhyThisBatchIsLimited() throws Exception {
        String tanda = Files.readString(TANDA, StandardCharsets.UTF_8);

        assertTrue(tanda.contains("No cambia parsing Java"));
        assertTrue(tanda.contains("No cambia parsing TypeScript"));
        assertTrue(tanda.contains("vista Resumen segura"));
        assertTrue(tanda.contains("sin cambiar UX visible") || tanda.contains("No cambia UX visible"));
    }
}
