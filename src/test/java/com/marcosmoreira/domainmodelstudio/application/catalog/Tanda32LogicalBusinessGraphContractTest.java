package com.marcosmoreira.domainmodelstudio.application.catalog;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCapability;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCategoryId;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramSupportStatus;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeDescriptor;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramWorkspaceKind;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class Tanda32LogicalBusinessGraphContractTest {

    @Test
    void logicalBusinessGraphShouldBeRegisteredWithMarkdownAndInitialVisualCanvas() {
        DiagramTypeDescriptor descriptor = new DefaultDiagramTypeRegistry()
                .findById(DiagramTypeId.LOGICAL_BUSINESS_GRAPH)
                .orElseThrow();

        assertEquals("Grafo lógico del negocio", descriptor.displayName());
        assertEquals(DiagramCategoryId.BUSINESS_ANALYSIS, descriptor.categoryId());
        assertEquals(DiagramSupportStatus.AVAILABLE, descriptor.supportStatus());
        assertEquals(DiagramWorkspaceKind.LOGICAL_BUSINESS_GRAPH_DIAGRAM, descriptor.workspaceKind());
        assertFalse(descriptor.supports(DiagramCapability.PLANNING_VIEW));
        assertTrue(descriptor.supports(DiagramCapability.CREATE_PROJECT));
        assertTrue(descriptor.supports(DiagramCapability.IMPORT_MARKDOWN));
        assertTrue(descriptor.supports(DiagramCapability.EXPORT_MARKDOWN));
        assertTrue(descriptor.supports(DiagramCapability.EXPORT_PNG));
        assertTrue(descriptor.supports(DiagramCapability.EXPORT_SVG));
        assertTrue(descriptor.supports(DiagramCapability.SHOW_VISUAL_OUTPUT));
        assertTrue(descriptor.supports(DiagramCapability.SAVE_DMS));
        assertTrue(descriptor.supports(DiagramCapability.LOAD_DMS));
        assertTrue(descriptor.supports(DiagramCapability.AI_RESOURCES));
        assertTrue(descriptor.supports(DiagramCapability.THEORY_HELP));
        assertTrue(descriptor.isAvailable());
        assertTrue(descriptor.shortDescription().contains("levantamiento lógico"));
        assertTrue(descriptor.shortDescription().contains("Vista visual"));
    }

    @Test
    void graphPlanShouldKeepAbbreviationLegendAndSidebarHelpSeparated() throws Exception {
        String plan = Files.readString(Path.of("docs/arquitectura/19_plan_grafo_logico_negocio.md"));

        for (String token : new String[]{"MF", "FL", "CU", "ACC", "RN", "PRE", "INV", "POST", "ENT", "EST", "REP", "RISK", "PEND"}) {
            assertTrue(plan.contains(token), "Falta abreviación en leyenda: " + token);
        }
        assertTrue(plan.contains("Guía académica:"));
        assertTrue(plan.contains("Ayuda del sidebar:"));
        assertTrue(plan.contains("FreeGraphDocument"));
        assertTrue(plan.contains("Prohibido"));
    }

    @Test
    void currentContractShouldNotTouchConceptualModelOrFreeGraphDomain() throws Exception {
        String definitions = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/application/catalog/definitions/BusinessAnalysisDiagramTypeDefinitions.java"));
        String typeId = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/domain/catalog/DiagramTypeId.java"));

        assertTrue(typeId.contains("LOGICAL_BUSINESS_GRAPH"));
        assertTrue(definitions.contains("logicalBusinessGraph"));
        assertFalse(definitions.contains("FreeGraphDocument"));
        assertFalse(definitions.contains("DiagramWorkspaceKind.CONCEPTUAL_CANVAS"));
    }
}
