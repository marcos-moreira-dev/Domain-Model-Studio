package com.marcosmoreira.domainmodelstudio.application.freegraph;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.catalog.DefaultDiagramCapabilityCatalog;
import com.marcosmoreira.domainmodelstudio.application.catalog.DefaultDiagramTypeRegistry;
import com.marcosmoreira.domainmodelstudio.application.examples.DefaultOfficialExampleCatalog;
import com.marcosmoreira.domainmodelstudio.application.resources.AiResourceDescriptor;
import com.marcosmoreira.domainmodelstudio.application.theory.DefaultTheoryCatalog;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCapability;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramSupportStatus;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramWorkspaceKind;
import com.marcosmoreira.domainmodelstudio.infrastructure.resources.OfficialAiResourceDescriptors;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Regresiones anti-fachada para que Grafo libre no vuelva a quedar como botón sin respaldo. */
class FreeGraphG12AntiFacadeContractTest {

    @Test
    void catalogShouldAdvertiseOnlyBackedFreeGraphCapabilities() {
        var descriptor = new DefaultDiagramTypeRegistry().findById(DiagramTypeId.FREE_GRAPH).orElseThrow();
        var capabilities = new DefaultDiagramCapabilityCatalog().capabilitiesOf(DiagramTypeId.FREE_GRAPH);

        assertTrue(descriptor.supportStatus().equals(DiagramSupportStatus.AVAILABLE));
        assertTrue(descriptor.workspaceKind().equals(DiagramWorkspaceKind.FREE_GRAPH_DIAGRAM));
        assertTrue(capabilities.has(DiagramCapability.CREATE_PROJECT));
        assertTrue(capabilities.has(DiagramCapability.IMPORT_MARKDOWN));
        assertTrue(capabilities.has(DiagramCapability.SHOW_VISUAL_OUTPUT));
        assertTrue(capabilities.has(DiagramCapability.MANUAL_EDITING));
        assertTrue(capabilities.has(DiagramCapability.EXPORT_PNG));
        assertTrue(capabilities.has(DiagramCapability.EXPORT_SVG));
        assertTrue(capabilities.has(DiagramCapability.EXPORT_MARKDOWN));
        assertTrue(capabilities.has(DiagramCapability.SAVE_DMS));
        assertTrue(capabilities.has(DiagramCapability.LOAD_DMS));
        assertTrue(capabilities.has(DiagramCapability.AI_RESOURCES));
        assertTrue(capabilities.has(DiagramCapability.THEORY_HELP));
        assertFalse(capabilities.has(DiagramCapability.PLANNING_VIEW));
        assertFalse(capabilities.has(DiagramCapability.EXPORT_PDF));
        assertFalse(capabilities.has(DiagramCapability.BATCH_EXPORT));
    }

    @Test
    void freeGraphShouldHaveOfficialAiResourcesTheoryAndExampleFiles() {
        List<AiResourceDescriptor> resources = OfficialAiResourceDescriptors.all().stream()
                .filter(resource -> resource.diagramTypeId().equals(DiagramTypeId.FREE_GRAPH))
                .toList();

        assertTrue(resources.stream().anyMatch(resource -> resource.id().equals("grafo-libre-gramatica")));
        assertTrue(resources.stream().anyMatch(resource -> resource.id().equals("plantilla-oficial-free-graph")));
        assertTrue(resources.stream().anyMatch(resource -> resource.id().equals("ejemplo-oficial-free-graph-minimo")));
        assertTrue(resources.stream().anyMatch(resource -> resource.id().equals("ejemplo-uens-grafo-libre")));
        assertTrue(resources.stream().allMatch(AiResourceDescriptor::importableByApplication));

        assertTrue(new DefaultTheoryCatalog().findByDiagramType(DiagramTypeId.FREE_GRAPH).isPresent());
        assertFalse(new DefaultOfficialExampleCatalog().findByDiagramType(DiagramTypeId.FREE_GRAPH).isEmpty());
    }

    @Test
    void freeGraphVisibleWiringShouldPointToRealSourceFiles() throws Exception {
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellView.java",
                "FreeGraphEditorView",
                "WorkspaceKind.FREE_GRAPH_DIAGRAM");
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/SpecializedWorkspaceCoordinator.java",
                "DiagramTypeId.FREE_GRAPH::equals",
                "freeGraphViewModel::loadProject");
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/toolbar/FreeGraphToolbarContributor.java",
                "FREE_GRAPH_ADD_NODE_TOOL",
                "FREE_GRAPH_ADD_EDGE_TOOL",
                "EXPORT_MARKDOWN");
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/exportable/ActiveOutputContributorRegistry.java",
                "FreeGraphActiveOutputContributor");
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/exportable/FreeGraphActiveOutputContributor.java",
                "DiagramTypeId.FREE_GRAPH",
                "viewModel::exportVisualAsPng");
        assertContains("src/main/java/com/marcosmoreira/domainmodelstudio/infrastructure/svg/specialized/SpecializedSvgModelFactory.java",
                "addFreeGraph",
                "VisualElementLayoutIds.freeGraphNode",
                "VisualElementLayoutIds.freeGraphEdge");
    }

    private static void assertContains(String file, String... fragments) throws Exception {
        String source = Files.readString(Path.of(file));
        for (String fragment : fragments) {
            assertTrue(source.contains(fragment), file + " debe contener: " + fragment);
        }
    }
}
