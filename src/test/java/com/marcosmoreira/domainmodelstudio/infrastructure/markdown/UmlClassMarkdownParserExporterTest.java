package com.marcosmoreira.domainmodelstudio.infrastructure.markdown;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramView;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramViewKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassNode;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlModuleGroup;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlVisibility;
import java.time.LocalDate;
import java.util.List;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class UmlClassMarkdownParserExporterTest {

    @Test
    void shouldImportOfficialUmlClassExample() throws Exception {
        UmlClassMarkdownParser parser = new UmlClassMarkdownParser();

        DiagramProject project = parser.parse(Path.of("examples", "markdown", "diagramas", "uml_class_restaurante_minimo.md"));

        assertEquals(DiagramTypeId.UML_CLASS, project.metadata().diagramTypeId());
        assertTrue(project.umlClassDiagram().isPresent());
        assertEquals(2, project.umlClassDiagram().orElseThrow().moduleCount());
        assertEquals(3, project.umlClassDiagram().orElseThrow().classCount());
        assertEquals(2, project.umlClassDiagram().orElseThrow().relationCount());
    }

    @Test
    void shouldExportImportableUmlClassMarkdown() throws Exception {
        DiagramProject project = new UmlClassMarkdownParser()
                .parse(Path.of("examples", "markdown", "diagramas", "uml_class_restaurante_minimo.md"));

        String markdown = new UmlClassMarkdownExporter().export(project);

        assertTrue(markdown.contains("diagram_type: \"uml-class\""));
        assertTrue(markdown.contains("# Clases"));
        assertTrue(markdown.contains("# Relaciones"));
    }
    @Test
    void shouldRoundTripInternalViewsInMarkdownExport() throws Exception {
        UmlModuleGroup backend = new UmlModuleGroup("backend", "Backend", "backend/src/main/java", "Java", "");
        UmlModuleGroup frontend = new UmlModuleGroup("frontend", "Frontend", "frontend/src/app", "TypeScript", "");
        UmlClassNode controller = new UmlClassNode("producto_controller", backend.id(), "ProductoController",
                "com.acme.productos", UmlClassKind.CONTROLLER, UmlVisibility.PUBLIC, "API", "", List.of(), "");
        UmlClassNode api = new UmlClassNode("producto_api_service", frontend.id(), "ProductoApiService",
                "src.app.productos", UmlClassKind.SERVICE, UmlVisibility.PUBLIC, "Cliente HTTP", "", List.of(), "");
        UmlClassDiagramView backendView = new UmlClassDiagramView("view_backend", UmlClassDiagramViewKind.BACKEND,
                "Backend Java", "Vista backend", List.of("backend"), List.of(backend.id()),
                List.of(controller.id()), List.of(), "");
        UmlClassDiagramDocument document = new UmlClassDiagramDocument("Cedro", "borrador", LocalDate.now(),
                List.of(backend, frontend), List.of(controller, api), List.of(), List.of(backendView), "");
        DiagramProject project = DiagramProject.blank("cedro", "Cedro", DiagramTypeId.UML_CLASS).withUmlClassDiagram(document);

        String markdown = new UmlClassMarkdownExporter().export(project);
        DiagramProject imported = new UmlClassMarkdownParser().parse(markdown, "memoria.md");

        assertTrue(markdown.contains("# Vistas internas"));
        assertEquals(1, imported.umlClassDiagram().orElseThrow().viewCount());
        assertEquals(UmlClassDiagramViewKind.BACKEND,
                imported.umlClassDiagram().orElseThrow().viewById("view_backend").orElseThrow().kind());
    }

}
