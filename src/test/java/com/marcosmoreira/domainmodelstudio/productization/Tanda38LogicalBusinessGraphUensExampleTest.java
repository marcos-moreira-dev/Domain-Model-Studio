package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphDocument;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphNodeKind;
import com.marcosmoreira.domainmodelstudio.infrastructure.markdown.LogicalBusinessGraphMarkdownParser;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

/** Guardarraíl de Tanda 38 para mantener el ejemplo oficial UENS del Grafo lógico. */
class Tanda38LogicalBusinessGraphUensExampleTest {

    private static final Path EXAMPLE = Path.of(
            "src/main/resources/ai-resources/official-markdown/diagramas/logical_business_graph_uens_gordito.md");
    private static final Path MIRROR = Path.of(
            "examples/markdown/diagramas/logical_business_graph_uens_gordito.md");
    private static final Path DEFINITIONS = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/application/catalog/definitions/BusinessAnalysisDiagramTypeDefinitions.java");
    private static final Path RESOURCES = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/resources/LogicalBusinessGraphAiResourceDescriptors.java");

    @Test
    void uensLogicalBusinessGraphExampleShouldBeImportableAndMirrored() throws Exception {
        String markdown = read(EXAMPLE);
        String mirror = read(MIRROR);

        assertEquals(markdown, mirror);
        assertTrue(markdown.contains("diagram_type: \"logical-business-graph\""));
        assertTrue(markdown.contains("sample_kind: \"uens-family\""));
        assertTrue(markdown.contains("# Leyenda"));
        assertTrue(markdown.contains("MF | Macroflujo"));
        assertTrue(markdown.contains("FL | Flujo o microflujo"));
        assertTrue(markdown.contains("CU | Caso de uso"));
        assertTrue(markdown.contains("ACC | Acción transformadora"));
        assertTrue(markdown.contains("PEND | Pregunta pendiente"));
    }

    @Test
    void uensLogicalBusinessGraphShouldCoverCoreNodeKinds() throws Exception {
        LogicalBusinessGraphDocument document = new LogicalBusinessGraphMarkdownParser()
                .parse(EXAMPLE)
                .logicalBusinessGraphDocument()
                .orElseThrow();

        Set<LogicalBusinessGraphNodeKind> kinds = document.nodes().stream()
                .map(node -> node.kind())
                .collect(Collectors.toSet());

        assertTrue(kinds.contains(LogicalBusinessGraphNodeKind.MACRO_FLOW));
        assertTrue(kinds.contains(LogicalBusinessGraphNodeKind.FLOW));
        assertTrue(kinds.contains(LogicalBusinessGraphNodeKind.USE_CASE));
        assertTrue(kinds.contains(LogicalBusinessGraphNodeKind.ACTION));
        assertTrue(kinds.contains(LogicalBusinessGraphNodeKind.BUSINESS_RULE));
        assertTrue(kinds.contains(LogicalBusinessGraphNodeKind.PRECONDITION));
        assertTrue(kinds.contains(LogicalBusinessGraphNodeKind.INVARIANT));
        assertTrue(kinds.contains(LogicalBusinessGraphNodeKind.POSTCONDITION));
        assertTrue(kinds.contains(LogicalBusinessGraphNodeKind.ENTITY));
        assertTrue(kinds.contains(LogicalBusinessGraphNodeKind.PENDING_QUESTION));
        assertTrue(document.nodes().size() >= 35);
        assertTrue(document.edges().size() >= 45);
        assertTrue(document.semanticIssues().isEmpty(), () -> "El ejemplo UENS no debería traer advertencias semánticas: " + document.semanticIssues());
    }

    @Test
    void uensLogicalBusinessGraphShouldBeRegisteredAsOfficialExampleAndAiResource() throws Exception {
        String definitions = read(DEFINITIONS);
        String resources = read(RESOURCES);

        assertTrue(definitions.contains("uens-grafo-logico-negocio"));
        assertTrue(definitions.contains("logical_business_graph_uens_gordito.md"));
        assertTrue(definitions.contains("DiagramTypeId.LOGICAL_BUSINESS_GRAPH"));
        assertTrue(resources.contains("ejemplo-uens-grafo-logico-negocio"));
        assertTrue(resources.contains("logical_business_graph_uens_gordito.md"));
        assertTrue(resources.contains("DiagramTypeId.LOGICAL_BUSINESS_GRAPH"));
    }

    private static String read(Path path) throws Exception {
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
