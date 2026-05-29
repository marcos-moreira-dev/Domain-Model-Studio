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
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleMapDocument;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleNode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class ModuleMapLayoutPersistenceContractTest {

    @Test
    void viewModelMovesModuleThroughVisualLayoutInsteadOfDomainCoordinates() {
        List<DiagramProject> changes = new ArrayList<>();
        ModuleMapViewModel viewModel = newViewModel();
        viewModel.registerProjectChangeListener(changes::add);
        viewModel.loadProject(project());

        viewModel.moveModuleTo("ventas", 410, 220);

        assertTrue(!changes.isEmpty());
        DiagramProject changed = changes.get(changes.size() - 1);
        var layout = changed.layouts().activeLayout().nodeFor(VisualElementLayoutIds.module("ventas")).orElseThrow();
        assertEquals(410, layout.x());
        assertEquals(220, layout.y());
        assertEquals("ventas", changed.moduleMap().orElseThrow().modules().get(0).id());
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
        ModuleMapDocument document = new ModuleMapDocument(
                "Sistema administrativo",
                "borrador",
                LocalDate.of(2026, 5, 1),
                List.of(ModuleNode.root("ventas", "Ventas")),
                List.of(),
                "");
        return DiagramProject.blank("mapa", "Mapa", DiagramTypeId.ADMIN_MODULE_MAP)
                .withModuleMap(document);
    }
}
