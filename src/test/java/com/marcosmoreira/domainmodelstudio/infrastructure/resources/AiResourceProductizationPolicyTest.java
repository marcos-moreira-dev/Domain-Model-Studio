package com.marcosmoreira.domainmodelstudio.infrastructure.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.resources.AiResourceDescriptor;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import org.junit.jupiter.api.Test;

class AiResourceProductizationPolicyTest {

    @Test
    void shouldClassifyCommonResourceKinds() {
        assertEquals(AiResourceKind.GRAMMAR, AiResourceProductizationPolicy.kindOf(resource(
                "bpmn-basico-gramatica", "05_bpmn_basico_gramatica.md", true)));
        assertEquals(AiResourceKind.AI_TEMPLATE, AiResourceProductizationPolicy.kindOf(resource(
                "plantilla-oficial-bpmn-basic", "official-markdown/plantillas/bpmn_basic.md", true)));
        assertEquals(AiResourceKind.MINIMAL_EXAMPLE, AiResourceProductizationPolicy.kindOf(resource(
                "ejemplo-oficial-bpmn-basic-venta-minimo", "official-markdown/diagramas/bpmn_basic_venta_minimo.md", true)));
        assertEquals(AiResourceKind.FULL_EXAMPLE, AiResourceProductizationPolicy.kindOf(resource(
                "ejemplo-uens-bpmn-matricula", "official-markdown/diagramas/bpmn_basic_matricula_uens_gordito.md", true)));
        assertEquals(AiResourceKind.LOGICAL_MASTER_TEMPLATE, AiResourceProductizationPolicy.kindOf(resource(
                "plantilla-canonica-levantamiento-logico", "official-markdown/levantamiento-logico/logical_business_intake_template.md", true)));
        assertEquals(AiResourceKind.PROMPT_GUIDE, AiResourceProductizationPolicy.kindOf(resource(
                "prompt-grafo-logico-negocio-desde-levantamiento", "19_grafo_logico_negocio_prompt_ia.md", false)));
    }

    @Test
    void shouldExplainImportContractAndReviewableLogicalDerivations() {
        AiResourceDescriptor logicalTemplate = resource(
                "plantilla-canonica-levantamiento-logico",
                "official-markdown/levantamiento-logico/logical_business_intake_template.md",
                true);

        assertTrue(AiResourceProductizationPolicy.importContract(logicalTemplate).contains("expediente lógico"));
        assertTrue(AiResourceProductizationPolicy.recommendedUse(logicalTemplate).contains("ENT/ATR/REL"));
        assertTrue(AiResourceProductizationPolicy.importContract(resource(
                "prompt-grafo-logico-negocio-desde-levantamiento", "19_grafo_logico_negocio_prompt_ia.md", false))
                .contains("prompt no importable"));
        assertTrue(AiResourceProductizationPolicy.importContract(resource(
                "referencia", "referencia.md", false)).contains("referencia no importable"));
    }

    private static AiResourceDescriptor resource(String id, String fileName, boolean importable) {
        return new AiResourceDescriptor(
                id,
                fileName,
                DiagramTypeId.BPMN_BASIC,
                "ai-resources/05_bpmn_basico_gramatica.md",
                true,
                importable,
                "Recurso de prueba.");
    }
}
