package com.marcosmoreira.domainmodelstudio.application.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.visual.VisualElementLayoutIds;
import com.marcosmoreira.domainmodelstudio.application.visual.VisualLayoutService;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayouts;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.DependencyKind;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleDependency;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleMapDocument;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleNode;
import com.marcosmoreira.domainmodelstudio.domain.notation.NotationType;
import com.marcosmoreira.domainmodelstudio.domain.validation.DiagramProjectValidator;
import com.marcosmoreira.domainmodelstudio.infrastructure.json.DmsProjectFileRepository;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Regresión de Tanda 19: abrir .dms debe validar layouts especializados contra su documento,
 * no solo contra el modelo conceptual ER vacío.
 */
class SpecializedProjectOpenUseCasePersistenceTest {

    @TempDir
    Path tempDir;

    @Test
    void opensSpecializedModuleMapWithManualLayoutThroughUseCase() throws Exception {
        ModuleNode secretaria = ModuleNode.root("secretaria", "Secretaría académica");
        ModuleNode calificaciones = ModuleNode.root("calificaciones", "Calificaciones");
        ModuleDependency dependency = new ModuleDependency(
                "dep-secretaria-calificaciones",
                secretaria.id(),
                calificaciones.id(),
                DependencyKind.USES,
                "Secretaría consulta notas.",
                "");
        ModuleMapDocument document = new ModuleMapDocument(
                "Sistema escolar",
                "borrador",
                LocalDate.of(2026, 5, 21),
                List.of(secretaria, calificaciones),
                List.of(dependency),
                "");
        DiagramProject project = DiagramProject.blank("mapa-uens", "Mapa UENS", DiagramTypeId.ADMIN_MODULE_MAP)
                .withModuleMap(document);

        VisualLayoutService layoutService = new VisualLayoutService();
        DiagramProject withLayout = layoutService.ensureVisualLayout(project);
        withLayout = layoutService.moveNodeTo(withLayout, VisualElementLayoutIds.module("secretaria"), 512, 300);
        withLayout = layoutService.addBendPoint(
                withLayout,
                VisualElementLayoutIds.dependency("dep-secretaria-calificaciones"),
                420,
                260);

        DmsProjectFileRepository repository = new DmsProjectFileRepository();
        Path file = tempDir.resolve("mapa-uens.dms");
        repository.save(withLayout, file);

        DiagramProject reopened = new OpenProjectUseCase(repository, new DiagramProjectValidator()).open(file);

        assertEquals(DiagramTypeId.ADMIN_MODULE_MAP, reopened.metadata().diagramTypeId());
        assertTrue(reopened.moduleMap().isPresent());
        assertEquals(2, reopened.moduleMap().orElseThrow().modules().size());
        var node = reopened.layouts().activeLayout().nodeFor(VisualElementLayoutIds.module("secretaria")).orElseThrow();
        assertEquals(512, node.x());
        assertEquals(300, node.y());
        var connector = reopened.layouts().activeLayout()
                .connectorById(VisualElementLayoutIds.dependency("dep-secretaria-calificaciones"))
                .orElseThrow();
        assertEquals(1, connector.bendPoints().size());
    }

    @Test
    void stillRejectsUnknownSpecializedLayoutNodesOnOpen() throws Exception {
        ModuleNode secretaria = ModuleNode.root("secretaria", "Secretaría académica");
        ModuleMapDocument document = new ModuleMapDocument(
                "Sistema escolar",
                "borrador",
                LocalDate.of(2026, 5, 21),
                List.of(secretaria),
                List.of(),
                "");
        DiagramLayout invalidLayout = new DiagramLayout(
                NotationType.CHEN,
                List.of(NodeLayout.at("module:secretaria", 100, 100, 180, 80),
                        NodeLayout.at("module:fantasma", 420, 100, 180, 80)),
                List.of());
        DiagramProject project = DiagramProject.blank("mapa-invalido", "Mapa inválido", DiagramTypeId.ADMIN_MODULE_MAP)
                .withModuleMap(document)
                .withLayouts(DiagramLayouts.empty().withLayout(invalidLayout));

        DmsProjectFileRepository repository = new DmsProjectFileRepository();
        Path file = tempDir.resolve("mapa-invalido.dms");
        repository.save(project, file);

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> new OpenProjectUseCase(repository, new DiagramProjectValidator()).open(file));
        assertTrue(error.getMessage().contains(DiagramElementId.of("module:fantasma").value()));
    }
}
