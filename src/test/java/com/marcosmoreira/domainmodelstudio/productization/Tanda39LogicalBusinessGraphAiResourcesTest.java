package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl de Tanda 39: recursos IA del Grafo lógico del negocio. */
class Tanda39LogicalBusinessGraphAiResourcesTest {

    @Test
    void logicalBusinessGraphShouldHaveGrammarTemplatePromptAndExamples() throws IOException {
        assertTrue(Files.exists(Path.of("src/main/resources/ai-resources/18_grafo_logico_negocio_gramatica.md")));
        assertTrue(Files.exists(Path.of("src/main/resources/ai-resources/19_grafo_logico_negocio_prompt_ia.md")));
        assertTrue(Files.exists(Path.of("src/main/resources/ai-resources/official-markdown/plantillas/logical_business_graph.md")));
        assertTrue(Files.exists(Path.of("src/main/resources/ai-resources/official-markdown/diagramas/logical_business_graph_minimo.md")));
        assertTrue(Files.exists(Path.of("src/main/resources/ai-resources/official-markdown/diagramas/logical_business_graph_uens_gordito.md")));
    }

    @Test
    void resourcesShouldExplainAbbreviationsAndOperationalWorkflowForAi() throws IOException {
        String grammar = Files.readString(Path.of("src/main/resources/ai-resources/18_grafo_logico_negocio_gramatica.md"));
        String template = Files.readString(Path.of("src/main/resources/ai-resources/official-markdown/plantillas/logical_business_graph.md"));
        String prompt = Files.readString(Path.of("src/main/resources/ai-resources/19_grafo_logico_negocio_prompt_ia.md"));

        assertTrue(grammar.contains("## Leyenda obligatoria de abreviaciones"));
        assertTrue(grammar.contains("MF | Macroflujo"));
        assertTrue(grammar.contains("FL | Flujo o microflujo"));
        assertTrue(grammar.contains("No reemplaza al levantamiento lógico"));
        assertTrue(template.contains("# Instrucciones para completar con IA"));
        assertTrue(template.contains("# Leyenda"));
        assertTrue(prompt.contains("El resultado debe ser solo Markdown"));
        assertTrue(prompt.contains("No inventes reglas definitivas"));
        assertTrue(prompt.contains("PEND"));
    }

    @Test
    void descriptorsShouldExportPromptTemplateAndMinimalExample() throws IOException {
        String descriptors = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/resources/LogicalBusinessGraphAiResourceDescriptors.java"));
        String policy = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/resources/AiResourceProductizationPolicy.java"));

        assertTrue(descriptors.contains("plantilla-oficial-logical-business-graph"));
        assertTrue(descriptors.contains("ejemplo-oficial-logical-business-graph-minimo"));
        assertTrue(descriptors.contains("prompt-grafo-logico-negocio-desde-levantamiento"));
        assertTrue(descriptors.contains("importable: false") || descriptors.contains("false,"));
        assertTrue(policy.contains("PROMPT_GUIDE"));
    }
}
