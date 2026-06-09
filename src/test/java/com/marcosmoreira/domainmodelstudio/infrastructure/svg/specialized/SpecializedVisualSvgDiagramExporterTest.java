package com.marcosmoreira.domainmodelstudio.infrastructure.svg.specialized;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphDocument;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphEdge;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphKind;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphNode;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleDependency;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleMapDocument;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleNode;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramView;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramViewKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassNode;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlModuleGroup;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlVisibility;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Verifica que los diagramas especializados tengan SVG vectorial, no fachada. */
class SpecializedVisualSvgDiagramExporterTest {

    @Test
    void moduleMapShouldExportVectorNodesAndConnectors() {
        ModuleMapDocument document = new ModuleMapDocument(
                "Sistema escolar",
                "0.1.0",
                LocalDate.of(2026, 1, 1),
                List.of(ModuleNode.root("academico", "Académico"), ModuleNode.root("reportes", "Reportes")),
                List.of(ModuleDependency.of("dep-1", "academico", "reportes")),
                "");
        DiagramProject project = DiagramProject.blank("uens", "Mapa UENS", DiagramTypeId.ADMIN_MODULE_MAP)
                .withModuleMap(document);

        String svg = new SpecializedVisualSvgDiagramExporter().export(project);

        assertTrue(svg.contains("<svg"));
        assertTrue(svg.contains("<polyline"));
        assertTrue(svg.contains("Académico"));
        assertTrue(svg.contains("Reportes"));
        assertTrue(svg.contains("generated-by=Domain Model Studio"));
    }
    @Test
    void umlClassSvgShouldExposeInternalViewsMetadata() {
        UmlModuleGroup backend = new UmlModuleGroup("backend", "Backend", "backend/src/main/java", "Java", "");
        UmlClassNode controller = new UmlClassNode("producto_controller", backend.id(), "ProductoController",
                "com.acme.productos", UmlClassKind.CONTROLLER, UmlVisibility.PUBLIC, "API", "", List.of(), "");
        UmlClassDiagramView backendView = new UmlClassDiagramView("view_backend", UmlClassDiagramViewKind.BACKEND,
                "Backend Java", "Vista backend", List.of("backend"), List.of(backend.id()),
                List.of(controller.id()), List.of(), "");
        UmlClassDiagramDocument document = new UmlClassDiagramDocument("Cedro", "borrador", LocalDate.now(),
                List.of(backend), List.of(controller), List.of(), List.of(backendView), "");
        DiagramProject project = DiagramProject.blank("cedro", "Cedro", DiagramTypeId.UML_CLASS).withUmlClassDiagram(document);

        String svg = new SpecializedVisualSvgDiagramExporter().export(project);

        assertTrue(svg.contains("views=1"));
        assertTrue(svg.contains("Backend Java"));
        assertTrue(svg.contains("ProductoController"));
    }

    @Test
    void freeGraphShouldExportVectorNodesAndRelationships() {
        FreeGraphDocument document = new FreeGraphDocument(
                "Grafo de decisiones",
                "0.1.0",
                LocalDate.of(2026, 1, 1),
                FreeGraphKind.MIXED,
                List.of(
                        new FreeGraphNode("idea", "Idea", "Punto de partida", 0),
                        new FreeGraphNode("producto", "Producto", "Resultado esperado", 1)),
                List.of(FreeGraphEdge.directed("idea_producto", "idea", "producto", "evoluciona")),
                "");
        DiagramProject project = DiagramProject.blank("grafo", "Grafo libre", DiagramTypeId.FREE_GRAPH)
                .withFreeGraph(document);

        String svg = new SpecializedVisualSvgDiagramExporter().export(project);

        assertTrue(svg.contains("<svg"));
        assertTrue(svg.contains("Grafo libre"));
        assertTrue(svg.contains("Idea"));
        assertTrue(svg.contains("Producto"));
        assertTrue(svg.contains("evoluciona"));
        assertTrue(svg.contains("connector-free-graph"));
    }

}
