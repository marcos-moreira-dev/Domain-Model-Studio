package com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class LogicalBusinessRebaselineSourceTest {

    private static final Path REBASELINE_DOC = Path.of(
            "docs/desarrollo/TANDA_014_REBASELINE_LEVANTAMIENTO_LOGICO.md");
    private static final Path DATA_DICTIONARY_UX_TEST = Path.of(
            "src/test/java/com/marcosmoreira/domainmodelstudio/presentation/datadictionary/DataDictionaryStructuredUxSourceTest.java");

    @Test
    void rebaselineDefinesLogicalBusinessAsStableSourceNotAutomaticGenerator() throws IOException {
        String doc = Files.readString(REBASELINE_DOC, StandardCharsets.UTF_8);

        assertTrue(doc.contains("fuente lógica canónica"));
        assertTrue(doc.contains("No genera automáticamente todos los demás proyectos"));
        assertTrue(doc.contains("La alineación semántica entre proyectos independientes queda a cargo del usuario y de la IA"));
        assertTrue(doc.contains("`Derivaciones` no debe permanecer como módulo principal")
                || Files.readString(Path.of("docs/desarrollo/TANDA_036_ARTEFACTOS_COMPATIBLES_LEGACY_LEVANTAMIENTO_LOGICO.md"), StandardCharsets.UTF_8)
                        .contains("dejan de formar parte de la presentación"));
        assertTrue(doc.contains("Ayuda y glosario"));
    }

    @Test
    void dataDictionaryGuardrailAcceptsExternalSideDockScrollPolicy() throws IOException {
        String sourceTest = Files.readString(DATA_DICTIONARY_UX_TEST, StandardCharsets.UTF_8);

        assertTrue(sourceTest.contains("DataEntityKind.values()"));
        assertTrue(sourceTest.contains("data-dictionary-entity-kind-disclosure"));
        assertTrue(sourceTest.contains("configureTableView(fieldTable)"));
        assertFalse(sourceTest.contains("configureListView(entityList)"));
    }
}
