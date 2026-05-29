package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl R1: la toolbar contextual se mantiene por contributors y no como clase central gigante. */
class ToolbarContributorsRefactorSourceTest {

    private static final Path TOOLBAR_PACKAGE = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/toolbar");

    @Test
    void defaultProviderShouldDelegateToContributorRegistry() throws IOException {
        String provider = read("DefaultDiagramToolbarActionProvider.java");

        assertTrue(provider.contains("DiagramToolbarContributorRegistry"));
        assertTrue(provider.contains("contributorRegistry.actionsFor(diagramTypeId)"));
        assertTrue(provider.contains("capabilityPolicy.shouldExposeToolbarAction"));
        assertTrue(provider.contains("interactionPolicy.shouldExpose"));
        assertFalse(provider.contains("private List<DiagramToolbarAction> umlClassActions()"));
        assertFalse(provider.contains("private List<DiagramToolbarAction> freeGraphActions()"));
    }

    @Test
    void registryShouldRegisterContributorFamiliesExplicitly() throws IOException {
        String registry = read("DiagramToolbarContributorRegistry.java");

        assertTrue(registry.contains("new ConceptualToolbarContributor()"));
        assertTrue(registry.contains("new ConceptualDocumentToolbarContributor()"));
        assertTrue(registry.contains("new AdministrativeToolbarContributor()"));
        assertTrue(registry.contains("new UmlClassToolbarContributor()"));
        assertTrue(registry.contains("new BehaviorToolbarContributor()"));
        assertTrue(registry.contains("new ArchitectureToolbarContributor()"));
        assertTrue(registry.contains("new FreeGraphToolbarContributor()"));
        assertTrue(registry.contains("new LogicalBusinessToolbarContributor()"));
    }

    @Test
    void sourceFilesShouldKeepActionsNearTheirDiagramFamily() throws IOException {
        assertTrue(read("ConceptualToolbarContributor.java").contains("ADD_ENTITY"));
        assertTrue(read("ConceptualToolbarContributor.java").contains("SWITCH_TO_CROWS_FOOT"));
        assertTrue(read("UmlClassToolbarContributor.java").contains("IMPORT_UML_FROM_SOURCE"));
        assertTrue(read("UmlClassToolbarContributor.java").contains("OPEN_UML_SOURCE"));
        assertTrue(read("FreeGraphToolbarContributor.java").contains("FREE_GRAPH_ADD_NODE_TOOL"));
        assertTrue(read("FreeGraphToolbarContributor.java").contains("FREE_GRAPH_ADD_EDGE_TOOL"));
        assertTrue(read("AdministrativeToolbarContributor.java").contains("APPLY_WIREFRAME_TEMPLATE"));
        assertTrue(read("BehaviorToolbarContributor.java").contains("ADD_SEQUENCE_MESSAGE"));
        assertTrue(read("ArchitectureToolbarContributor.java").contains("ADD_DEPLOYMENT_SERVER"));
        assertTrue(read("LogicalBusinessToolbarContributor.java").contains("LOGICAL_BUSINESS_SHOW_VALIDATION"));
        assertTrue(read("LogicalBusinessToolbarContributor.java").contains("LOGICAL_BUSINESS_SHOW_TRACEABILITY"));
    }

    @Test
    void providerShouldStaySmallAfterRefactor() throws IOException {
        long providerLines = Files.lines(TOOLBAR_PACKAGE.resolve("DefaultDiagramToolbarActionProvider.java")).count();

        assertTrue(providerLines < 120, "El proveedor central no debe volver a contener todas las acciones por tipo.");
    }

    private static String read(String fileName) throws IOException {
        return Files.readString(TOOLBAR_PACKAGE.resolve(fileName), StandardCharsets.UTF_8);
    }
}
