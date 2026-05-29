package com.marcosmoreira.domainmodelstudio.presentation.modulemap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.modulemap.AddModuleMapDependencyUseCase;
import com.marcosmoreira.domainmodelstudio.application.modulemap.AddModuleMapModuleUseCase;
import com.marcosmoreira.domainmodelstudio.application.modulemap.RemoveModuleMapItemUseCase;
import com.marcosmoreira.domainmodelstudio.application.modulemap.UpdateModuleMapDependencyUseCase;
import com.marcosmoreira.domainmodelstudio.application.modulemap.UpdateModuleMapModuleUseCase;
import com.marcosmoreira.domainmodelstudio.application.modulemap.ValidateModuleMapUseCase;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualElementLayoutIds;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.DependencyKind;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleDependency;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleMapDocument;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleNode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class ModuleMapCanvasAdapterTest {

    @Test
    void exposesModulesAndRelationshipsAsInteractiveCanvasModel() {
        ModuleMapViewModel viewModel = newViewModel();
        viewModel.loadProject(project());
        ModuleMapCanvasAdapter adapter = new ModuleMapCanvasAdapter(viewModel);

        assertEquals(2, adapter.nodes().size());
        assertEquals(2, adapter.connectors().size());
        assertTrue(adapter.connectors().stream().anyMatch(connector -> connector.kind().equals("module-containment")));
        assertTrue(adapter.connectors().stream().anyMatch(connector -> connector.kind().equals("module-dependency")));
    }

    @Test
    void movingNodeThroughCanvasUpdatesProjectLayout() {
        List<DiagramProject> changes = new ArrayList<>();
        ModuleMapViewModel viewModel = newViewModel();
        viewModel.registerProjectChangeListener(changes::add);
        viewModel.loadProject(project());
        ModuleMapCanvasAdapter adapter = new ModuleMapCanvasAdapter(viewModel);

        adapter.selectNode(ModuleMapCanvasAdapter.moduleLayoutId("ventas"), false);
        adapter.moveSelectedNodesBy(120, 90);

        assertTrue(!changes.isEmpty());
        var moved = viewModel.currentProject().layouts().activeLayout()
                .nodeFor(VisualElementLayoutIds.module("ventas"))
                .orElseThrow();
        assertTrue(moved.x() > 80);
        assertTrue(moved.y() > 64);
    }

    @Test
    void bendPointCommandsAreDelegatedToPersistentLayout() {
        ModuleMapViewModel viewModel = newViewModel();
        viewModel.loadProject(project());
        ModuleMapCanvasAdapter adapter = new ModuleMapCanvasAdapter(viewModel);
        String dependencyLayoutId = ModuleMapCanvasAdapter.dependencyLayoutId("dep-ventas-inventario");

        adapter.addBendPoint(dependencyLayoutId, 260, 180);

        var layout = viewModel.currentProject().layouts().activeLayout()
                .connectorById(VisualElementLayoutIds.dependency("dep-ventas-inventario"))
                .orElseThrow();
        assertEquals(1, layout.bendPoints().size());
        assertEquals(260, layout.bendPoints().get(0).x());
    }

    private static ModuleMapViewModel newViewModel() {
        return new ModuleMapViewModel(
                new AddModuleMapModuleUseCase(),
                new AddModuleMapDependencyUseCase(),
                new UpdateModuleMapModuleUseCase(),
                new UpdateModuleMapDependencyUseCase(),
                new RemoveModuleMapItemUseCase(),
                new ValidateModuleMapUseCase(),
                ignored -> { }
        );
    }

    private static DiagramProject project() {
        ModuleNode sales = ModuleNode.root("ventas", "Ventas");
        ModuleNode inventory = ModuleNode.child("inventario", "Inventario", "ventas");
        ModuleDependency dependency = new ModuleDependency(
                "dep-ventas-inventario",
                sales.id(),
                inventory.id(),
                DependencyKind.USES,
                "Ventas consulta stock.",
                "");
        ModuleMapDocument document = new ModuleMapDocument(
                "Sistema administrativo",
                "borrador",
                LocalDate.of(2026, 5, 1),
                List.of(sales, inventory),
                List.of(dependency),
                "");
        return DiagramProject.blank("mapa", "Mapa", DiagramTypeId.ADMIN_MODULE_MAP)
                .withModuleMap(document);
    }
}
