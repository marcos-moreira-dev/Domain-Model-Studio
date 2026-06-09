package com.marcosmoreira.domainmodelstudio.application.freegraph;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.visual.VisualElementLayoutIds;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphDocument;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphEdge;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphEdgeDirection;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphKind;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphNode;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayout;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class FreeGraphLayoutUseCaseTest {

    @Test
    void shouldGenerateVisualLayoutForNodesAndEdges() {
        DiagramProject prepared = new GenerateInitialFreeGraphLayoutUseCase().ensureLayout(projectWithGraph());

        DiagramLayout layout = prepared.layouts().activeLayout();

        assertEquals(3, layout.nodeCount());
        assertEquals(2, layout.connectorCount());
        assertTrue(layout.nodeFor(VisualElementLayoutIds.freeGraphNode("sistema")).isPresent());
        assertTrue(layout.nodeFor(VisualElementLayoutIds.freeGraphNode("cliente")).isPresent());
        assertTrue(layout.connectorById(VisualElementLayoutIds.freeGraphEdge("rel_cliente_sistema")).isPresent());
    }

    @Test
    void shouldPreserveManualPositionsWhenEnsuringLayoutAgain() {
        GenerateInitialFreeGraphLayoutUseCase useCase = new GenerateInitialFreeGraphLayoutUseCase();
        DiagramProject prepared = useCase.ensureLayout(projectWithGraph());
        DiagramLayout movedLayout = prepared.layouts().activeLayout()
                .moveNode(VisualElementLayoutIds.freeGraphNode("sistema"), 900.0, 700.0);
        DiagramProject movedProject = prepared.withLayouts(prepared.layouts().withLayout(movedLayout));

        DiagramProject ensuredAgain = useCase.ensureLayout(movedProject);

        assertEquals(900.0, ensuredAgain.layouts().activeLayout()
                .nodeFor(VisualElementLayoutIds.freeGraphNode("sistema")).orElseThrow().x());
        assertEquals(700.0, ensuredAgain.layouts().activeLayout()
                .nodeFor(VisualElementLayoutIds.freeGraphNode("sistema")).orElseThrow().y());
    }

    @Test
    void shouldRegenerateLayoutWhenAutoorganizingExplicitly() {
        GenerateInitialFreeGraphLayoutUseCase useCase = new GenerateInitialFreeGraphLayoutUseCase();
        DiagramProject prepared = useCase.ensureLayout(projectWithGraph());
        DiagramLayout movedLayout = prepared.layouts().activeLayout()
                .moveNode(VisualElementLayoutIds.freeGraphNode("sistema"), 900.0, 700.0);
        DiagramProject movedProject = prepared.withLayouts(prepared.layouts().withLayout(movedLayout));

        DiagramProject regenerated = useCase.regenerateLayout(movedProject);

        assertNotEquals(900.0, regenerated.layouts().activeLayout()
                .nodeFor(VisualElementLayoutIds.freeGraphNode("sistema")).orElseThrow().x());
        assertNotEquals(700.0, regenerated.layouts().activeLayout()
                .nodeFor(VisualElementLayoutIds.freeGraphNode("sistema")).orElseThrow().y());
    }

    private static DiagramProject projectWithGraph() {
        FreeGraphDocument document = new FreeGraphDocument(
                "Grafo libre",
                "borrador",
                LocalDate.of(2026, 1, 1),
                FreeGraphKind.MIXED,
                List.of(
                        new FreeGraphNode("sistema", "Sistema", "Núcleo", 0),
                        new FreeGraphNode("cliente", "Cliente", "Usuario", 1),
                        new FreeGraphNode("reporte", "Reporte", "Salida", 2)),
                List.of(
                        new FreeGraphEdge("rel_cliente_sistema", "cliente", "sistema",
                                FreeGraphEdgeDirection.DIRECTED, "usa", ""),
                        new FreeGraphEdge("rel_sistema_reporte", "sistema", "reporte",
                                FreeGraphEdgeDirection.UNDIRECTED, "produce", "")),
                "");
        return DiagramProject.blank("grafo", "Grafo libre", DiagramTypeId.FREE_GRAPH).withFreeGraph(document);
    }
}
