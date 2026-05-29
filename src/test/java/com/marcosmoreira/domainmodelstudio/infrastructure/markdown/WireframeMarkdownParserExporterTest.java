package com.marcosmoreira.domainmodelstudio.infrastructure.markdown;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeComponentKind;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class WireframeMarkdownParserExporterTest {

    @Test
    void shouldImportOfficialWireframeExample() throws Exception {
        WireframeMarkdownParser parser = new WireframeMarkdownParser();

        DiagramProject project = parser.parse(Path.of("examples", "markdown", "diagramas", "admin_wireframes_ventas_minimo.md"));

        assertEquals(DiagramTypeId.ADMIN_WIREFRAMES, project.metadata().diagramTypeId());
        assertTrue(project.wireframe().isPresent());
        assertEquals(2, project.wireframe().orElseThrow().screens().size());
        assertTrue(project.wireframe().orElseThrow().components().size() >= 6);
    }

    @Test
    void shouldExportWireframeAsImportableMarkdown() throws Exception {
        WireframeMarkdownParser parser = new WireframeMarkdownParser();
        DiagramProject project = parser.parse(Path.of("examples", "markdown", "diagramas", "admin_wireframes_ventas_minimo.md"));

        String markdown = new WireframeMarkdownExporter().export(project);

        assertTrue(markdown.contains("diagram_type: \"admin-wireframes\""));
        assertTrue(markdown.contains("importable: true"));
        assertTrue(markdown.contains("# Pantallas"));
        assertTrue(markdown.contains("### Secciones"));
        assertTrue(markdown.contains("### Controles"));
    }
    @Test
    void exportedMarkdownRoundTripsWithoutNavigationPlaceholdersAndPreservesComponentDetails() throws Exception {
        WireframeMarkdownParser parser = new WireframeMarkdownParser();
        DiagramProject project = parser.parse(Path.of("examples", "markdown", "diagramas", "admin_wireframes_ventas_minimo.md"));

        String markdown = new WireframeMarkdownExporter().export(project);
        DiagramProject reparsed = parser.parse(markdown, "wireframes-exportados.md");

        var original = project.wireframe().orElseThrow();
        var roundTripped = reparsed.wireframe().orElseThrow();
        assertEquals(original.screens().size(), roundTripped.screens().size());
        assertEquals(original.components().size(), roundTripped.components().size());
        assertTrue(roundTripped.components().stream()
                .noneMatch(component -> component.displayName().equalsIgnoreCase("Pendiente")));
        assertTrue(roundTripped.components().stream()
                .anyMatch(component -> component.kind() == WireframeComponentKind.BUTTON
                        && component.behavior().contains("formulario")));
        assertTrue(roundTripped.components().stream()
                .anyMatch(component -> component.kind() == WireframeComponentKind.TABLE));
    }

}
