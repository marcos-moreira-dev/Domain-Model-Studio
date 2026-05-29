package com.marcosmoreira.domainmodelstudio.infrastructure.resources;

import com.marcosmoreira.domainmodelstudio.application.resources.AiResourceDescriptor;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.List;

/** Recursos IA oficiales específicos del Grafo lógico del negocio. */
final class LogicalBusinessGraphAiResourceDescriptors {

    private static final String BASE = "ai-resources/";

    private LogicalBusinessGraphAiResourceDescriptors() {
    }

    static List<AiResourceDescriptor> all() {
        return List.of(
                importable(
                        "logical-business-graph",
                        "18_grafo_logico_negocio_gramatica.md",
                        "Gramática Markdown importable para Grafo lógico del negocio."),
                importable(
                        "plantilla-oficial-logical-business-graph",
                        "official-markdown/plantillas/logical_business_graph.md",
                        "Plantilla oficial importable para construir un grafo lógico desde levantamiento de negocio."),
                importable(
                        "ejemplo-oficial-logical-business-graph-minimo",
                        "official-markdown/diagramas/logical_business_graph_minimo.md",
                        "Ejemplo mínimo importable para validar macroflujo, microflujo, caso de uso y reglas."),
                importable(
                        "ejemplo-uens-grafo-logico-negocio",
                        "official-markdown/diagramas/logical_business_graph_uens_gordito.md",
                        "Ejemplo UENS oficial importable para Grafo lógico del negocio escolar."),
                prompt(
                        "prompt-grafo-logico-negocio-desde-levantamiento",
                        "19_grafo_logico_negocio_prompt_ia.md",
                        "Prompt guía no importable para pedir a una IA un grafo lógico compatible con Domain Model Studio.")
        );
    }

    private static AiResourceDescriptor importable(String id, String fileName, String description) {
        return new AiResourceDescriptor(
                id,
                fileName,
                DiagramTypeId.LOGICAL_BUSINESS_GRAPH,
                BASE + fileName,
                true,
                true,
                description);
    }

    private static AiResourceDescriptor prompt(String id, String fileName, String description) {
        return new AiResourceDescriptor(
                id,
                fileName,
                DiagramTypeId.LOGICAL_BUSINESS_GRAPH,
                BASE + fileName,
                true,
                false,
                description);
    }
}
