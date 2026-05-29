package com.marcosmoreira.domainmodelstudio.application.freegraph;

import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphDocument;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphEdgeDirection;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphKind;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FreeGraphUseCasesTest {

    @Test
    void createBlankUsesRequestedKind() {
        FreeGraphDocument document = new CreateFreeGraphUseCase()
                .createBlank("Mapa mental", FreeGraphKind.DIRECTED);

        assertEquals("Mapa mental", document.projectName());
        assertEquals(FreeGraphKind.DIRECTED, document.graphKind());
        assertTrue(document.isEmpty());
    }

    @Test
    void addNodeGeneratesUniqueReadableIdsAndOrder() {
        FreeGraphDocument document = FreeGraphDocument.blank("Grafo");
        AddFreeGraphNodeUseCase addNode = new AddFreeGraphNodeUseCase();

        document = addNode.addNode(document, "Nodo Raíz", "Contenido A");
        document = addNode.addNode(document, "Nodo Raíz", "Contenido B");

        assertEquals(2, document.nodeCount());
        assertTrue(document.nodeById("nodo_raiz").isPresent());
        assertTrue(document.nodeById("nodo_raiz_2").isPresent());
        assertEquals(0, document.nodeById("nodo_raiz").orElseThrow().orderIndex());
        assertEquals(1, document.nodeById("nodo_raiz_2").orElseThrow().orderIndex());
    }

    @Test
    void addEdgeRequiresExistingNodes() {
        FreeGraphDocument document = new AddFreeGraphNodeUseCase()
                .addNode(FreeGraphDocument.blank("Grafo"), "A");

        AddFreeGraphEdgeUseCase addEdge = new AddFreeGraphEdgeUseCase();

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> addEdge.addEdge(document, "a", "b", FreeGraphEdgeDirection.DIRECTED, "depende"));
        assertTrue(error.getMessage().contains("No existe nodo destino"));
    }

    @Test
    void addAndUpdateEdgeKeepsApplicationContract() {
        FreeGraphDocument document = FreeGraphDocument.blank("Grafo");
        AddFreeGraphNodeUseCase addNode = new AddFreeGraphNodeUseCase();
        AddFreeGraphEdgeUseCase addEdge = new AddFreeGraphEdgeUseCase();
        UpdateFreeGraphEdgeUseCase updateEdge = new UpdateFreeGraphEdgeUseCase();

        document = addNode.addNode(document, "A");
        document = addNode.addNode(document, "B");
        document = addEdge.addEdge(document, "a", "b", FreeGraphEdgeDirection.DIRECTED, "conecta");

        String edgeId = document.edges().getFirst().id();
        document = updateEdge.updateEdge(document, edgeId, "b", "a", FreeGraphEdgeDirection.UNDIRECTED,
                "relacionado", "nota");

        assertEquals(1, document.edgeCount());
        assertEquals("b", document.edges().getFirst().sourceNodeId());
        assertEquals("a", document.edges().getFirst().targetNodeId());
        assertEquals("relacionado", document.edges().getFirst().label());
    }

    @Test
    void removeNodeAlsoRemovesIncidentEdgesThroughDomainContract() {
        FreeGraphDocument document = FreeGraphDocument.blank("Grafo");
        AddFreeGraphNodeUseCase addNode = new AddFreeGraphNodeUseCase();
        AddFreeGraphEdgeUseCase addEdge = new AddFreeGraphEdgeUseCase();
        RemoveFreeGraphItemUseCase removeItem = new RemoveFreeGraphItemUseCase();

        document = addNode.addNode(document, "A");
        document = addNode.addNode(document, "B");
        document = addEdge.addEdge(document, "a", "b", FreeGraphEdgeDirection.DIRECTED, "conecta");
        document = removeItem.removeNode(document, "a");

        assertEquals(1, document.nodeCount());
        assertEquals(0, document.edgeCount());
        assertFalse(document.nodeById("a").isPresent());
    }

    @Test
    void validationReportsUsefulWarnings() {
        FreeGraphDocument document = new AddFreeGraphNodeUseCase()
                .addNode(FreeGraphDocument.blank("Grafo"), "Nodo aislado");

        FreeGraphValidationResult result = new ValidateFreeGraphUseCase().validate(document);

        assertFalse(result.ok());
        assertTrue(result.warnings().stream().anyMatch(warning -> warning.contains("todavía no tiene contenido")));
        assertTrue(result.warnings().stream().anyMatch(warning -> warning.contains("todavía no tiene relaciones")));
    }

    @Test
    void shouldCreateFreeGraphProjectPayloadForPersistence() {
        var project = new CreateFreeGraphProjectUseCase()
                .createBlankProject("grafo_libre", "Grafo libre");

        assertEquals(DiagramTypeId.FREE_GRAPH, project.metadata().diagramTypeId());
        assertTrue(project.freeGraph().isPresent());
        assertEquals("Grafo libre", project.freeGraph().get().projectName());
    }
}
