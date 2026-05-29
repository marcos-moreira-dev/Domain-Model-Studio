package com.marcosmoreira.domainmodelstudio.application.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.visual.VisualElementLayoutIds;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualLayoutService;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.DependencyKind;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleDependency;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleMapDocument;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleNode;
import com.marcosmoreira.domainmodelstudio.domain.validation.DiagramProjectValidator;
import com.marcosmoreira.domainmodelstudio.infrastructure.json.DmsProjectFileRepository;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class SpecializedProjectOpenUseCaseLayoutRoundTripTest {

    @Test
    void opensSpecializedProjectWithVisualLayoutThroughUseCase() throws Exception {
        ModuleNode sales = ModuleNode.root("ventas", "Ventas");
        ModuleNode inventory = ModuleNode.root("inventario", "Inventario");
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
        DiagramProject project = DiagramProject.blank("mapa", "Mapa", DiagramTypeId.ADMIN_MODULE_MAP)
                .withModuleMap(document);

        VisualLayoutService visualLayout = new VisualLayoutService();
        DiagramProject moved = visualLayout.moveNodeTo(project, VisualElementLayoutIds.module("ventas"), 512, 300);
        DiagramProject withBendPoint = visualLayout.addBendPoint(
                moved,
                VisualElementLayoutIds.dependency("dep-ventas-inventario"),
                260,
                240);

        Path target = Files.createTempFile("domain-model-studio-specialized-layout-", ".dms");
        DmsProjectFileRepository repository = new DmsProjectFileRepository();
        repository.save(withBendPoint, target);

        DiagramProject opened = new OpenProjectUseCase(repository, new DiagramProjectValidator()).open(target);

        assertEquals(DiagramTypeId.ADMIN_MODULE_MAP, opened.metadata().diagramTypeId());
        assertTrue(opened.moduleMap().isPresent());
        var node = opened.layouts().activeLayout().nodeFor(VisualElementLayoutIds.module("ventas")).orElseThrow();
        assertEquals(512, node.x());
        assertEquals(300, node.y());
        var connector = opened.layouts().activeLayout()
                .connectorById(VisualElementLayoutIds.dependency("dep-ventas-inventario"));
        assertTrue(connector.isPresent());
        assertEquals(1, connector.orElseThrow().bendPoints().size());
    }
}
