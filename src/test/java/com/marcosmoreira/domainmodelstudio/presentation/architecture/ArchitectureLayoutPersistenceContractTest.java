package com.marcosmoreira.domainmodelstudio.presentation.architecture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.architecture.AddArchitectureEdgeUseCase;
import com.marcosmoreira.domainmodelstudio.application.architecture.AddArchitectureNodeUseCase;
import com.marcosmoreira.domainmodelstudio.application.architecture.RemoveArchitectureItemUseCase;
import com.marcosmoreira.domainmodelstudio.application.architecture.UpdateArchitectureEdgeUseCase;
import com.marcosmoreira.domainmodelstudio.application.architecture.UpdateArchitectureNodeUseCase;
import com.marcosmoreira.domainmodelstudio.application.architecture.ValidateArchitectureDiagramUseCase;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualElementLayoutIds;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureDiagramKind;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureEdge;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureEdgeKind;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureNode;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureNodeKind;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class ArchitectureLayoutPersistenceContractTest {

    @Test
    void viewModelMovesArchitectureNodeThroughVisualLayoutInsteadOfDomainCoordinates() {
        List<DiagramProject> changes = new ArrayList<>();
        ArchitectureDiagramViewModel viewModel = newViewModel();
        viewModel.registerProjectChangeListener(changes::add);
        viewModel.loadProject(project());

        viewModel.moveNodeTo("sistema-uens", 460, 280);

        assertTrue(!changes.isEmpty());
        DiagramProject changed = changes.get(changes.size() - 1);
        var layout = changed.layouts().activeLayout().nodeFor(VisualElementLayoutIds.architectureNode("sistema-uens")).orElseThrow();
        assertEquals(460, layout.x());
        assertEquals(280, layout.y());
        assertEquals("sistema-uens", changed.architectureDiagram().orElseThrow().nodes().get(0).id());
    }

    @Test
    void viewModelPersistsBendPointForArchitectureEdge() {
        List<DiagramProject> changes = new ArrayList<>();
        ArchitectureDiagramViewModel viewModel = newViewModel();
        viewModel.registerProjectChangeListener(changes::add);
        viewModel.loadProject(project());

        viewModel.addConnectorBendPoint(VisualElementLayoutIds.architectureEdge("relacion-1"), 340, 210);

        DiagramProject changed = changes.get(changes.size() - 1);
        var layout = changed.layouts().activeLayout().connectorById(VisualElementLayoutIds.architectureEdge("relacion-1")).orElseThrow();
        assertEquals(1, layout.bendPoints().size());
        assertEquals(340, layout.bendPoints().get(0).x());
        assertEquals(210, layout.bendPoints().get(0).y());
    }

    private static ArchitectureDiagramViewModel newViewModel() {
        return new ArchitectureDiagramViewModel(
                new AddArchitectureNodeUseCase(),
                new AddArchitectureEdgeUseCase(),
                new UpdateArchitectureNodeUseCase(),
                new UpdateArchitectureEdgeUseCase(),
                new RemoveArchitectureItemUseCase(),
                new ValidateArchitectureDiagramUseCase(),
                ignored -> { }
        );
    }

    private static DiagramProject project() {
        ArchitectureNode system = new ArchitectureNode("sistema-uens", ArchitectureNodeKind.SOFTWARE_SYSTEM,
                "Sistema UENS", "JavaFX + Spring", "Secretaría", "Producción", "Sistema escolar", "", 0);
        ArchitectureNode database = new ArchitectureNode("postgres-uens", ArchitectureNodeKind.DATABASE,
                "PostgreSQL UENS", "PostgreSQL", "TI", "Producción", "Base académica", "", 1);
        ArchitectureEdge edge = new ArchitectureEdge("relacion-1", system.id(), database.id(), ArchitectureEdgeKind.READS_WRITES,
                "lee/escribe", "JDBC", "");
        ArchitectureDiagramDocument document = new ArchitectureDiagramDocument(
                "UENS",
                "borrador",
                LocalDate.of(2026, 5, 1),
                ArchitectureDiagramKind.C4_CONTAINERS,
                List.of(system, database),
                List.of(edge),
                "");
        return DiagramProject.blank("arquitectura-uens", "Arquitectura UENS", DiagramTypeId.C4_CONTAINERS)
                .withArchitectureDiagram(document);
    }
}
