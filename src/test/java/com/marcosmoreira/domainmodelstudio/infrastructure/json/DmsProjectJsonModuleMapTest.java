package com.marcosmoreira.domainmodelstudio.infrastructure.json;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.DependencyKind;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleDependency;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleKind;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleMapDocument;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleNode;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleStatus;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class DmsProjectJsonModuleMapTest {

    @Test
    void shouldPersistModuleMapInsideDmsProject() {
        ModuleNode sales = new ModuleNode(
                "ventas",
                "Ventas",
                "",
                ModuleKind.MAIN,
                ModuleStatus.PLANNED,
                "Gestionar ventas y pedidos.",
                "Módulo principal de ventas.",
                List.of("administrativo", "core"),
                "Prioridad alta");
        ModuleNode inventory = new ModuleNode(
                "inventario",
                "Inventario",
                "",
                ModuleKind.MAIN,
                ModuleStatus.PLANNED,
                "Gestionar productos y stock.",
                "Módulo principal de inventario.",
                List.of("administrativo"),
                "Prioridad media");
        ModuleDependency dependency = new ModuleDependency(
                "ventas-inventario",
                sales.id(),
                inventory.id(),
                DependencyKind.USES,
                "Ventas consulta stock disponible.",
                "Dependencia funcional");
        ModuleMapDocument document = new ModuleMapDocument(
                "Restaurante",
                "borrador",
                LocalDate.of(2026, 1, 1),
                List.of(sales, inventory),
                List.of(dependency),
                "Mapa inicial");
        DiagramProject project = DiagramProject.blank("mapa_restaurante", "Mapa Restaurante", DiagramTypeId.ADMIN_MODULE_MAP)
                .withModuleMap(document);

        String json = new DmsProjectJsonWriter().write(project);
        DiagramProject reopened = new DmsProjectJsonReader().read(json);

        assertEquals(DiagramTypeId.ADMIN_MODULE_MAP, reopened.metadata().diagramTypeId());
        assertTrue(reopened.moduleMap().isPresent());
        assertEquals(2, reopened.moduleMap().get().moduleCount());
        assertEquals(1, reopened.moduleMap().get().dependencyCount());
        assertEquals("Ventas", reopened.moduleMap().get().moduleById("ventas").orElseThrow().displayName());
    }
}
