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

class OpenProjectUseCaseSpecializedPersistenceTest {

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

        VisualLayoutService layoutService = new VisualLayoutService();
        DiagramProject moved = layoutService.moveNodeTo(project, VisualElementLayoutIds.module("ventas"), 512, 300);
        DiagramProject withBendPoint = layoutService.addBendPoint(
                moved,
                VisualElementLayoutIds.dependency("dep-ventas-inventario"),
                260,
                240);
        Path target = Files.createTempFile("domain-model-studio-specialized-open-", ".dms");
        DmsProjectFileRepository repository = new DmsProjectFileRepository();
        repository.save(withBendPoint, target);

        DiagramProject reopened = new OpenProjectUseCase(repository, new DiagramProjectValidator()).open(target);

        assertEquals(DiagramTypeId.ADMIN_MODULE_MAP, reopened.metadata().diagramTypeId());
        assertTrue(reopened.moduleMap().isPresent());
        var node = reopened.layouts().activeLayout().nodeFor(VisualElementLayoutIds.module("ventas")).orElseThrow();
        assertEquals(512, node.x());
        assertEquals(300, node.y());
        var connector = reopened.layouts().activeLayout().connectorById(
                VisualElementLayoutIds.dependency("dep-ventas-inventario")).orElseThrow();
        assertEquals(1, connector.bendPoints().size());
    }
}
