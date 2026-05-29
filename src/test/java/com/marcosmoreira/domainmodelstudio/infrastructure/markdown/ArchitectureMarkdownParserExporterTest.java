package com.marcosmoreira.domainmodelstudio.infrastructure.markdown;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureDiagramKind;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class ArchitectureMarkdownParserExporterTest {

    @Test
    void shouldImportOfficialC4ContextExample() throws Exception {
        DiagramProject project = new ArchitectureMarkdownParser()
                .parse(Path.of("examples", "markdown", "diagramas", "c4_context_sistema_administrativo_minimo.md"));

        assertEquals(DiagramTypeId.C4_CONTEXT, project.metadata().diagramTypeId());
        assertTrue(project.architectureDiagram().isPresent());
        assertEquals(ArchitectureDiagramKind.C4_CONTEXT, project.architectureDiagram().orElseThrow().diagramKind());
        assertEquals(6, project.architectureDiagram().orElseThrow().nodes().size());
        assertEquals(4, project.architectureDiagram().orElseThrow().edges().size());
    }

    @Test
    void shouldImportOfficialC4ContainersExample() throws Exception {
        DiagramProject project = new ArchitectureMarkdownParser()
                .parse(Path.of("examples", "markdown", "diagramas", "c4_containers_sistema_administrativo_minimo.md"));

        assertEquals(DiagramTypeId.C4_CONTAINERS, project.metadata().diagramTypeId());
        assertTrue(project.architectureDiagram().isPresent());
        assertEquals(ArchitectureDiagramKind.C4_CONTAINERS, project.architectureDiagram().orElseThrow().diagramKind());
        assertEquals(4, project.architectureDiagram().orElseThrow().nodes().size());
        assertEquals(4, project.architectureDiagram().orElseThrow().edges().size());
    }

    @Test
    void shouldImportOfficialTechnicalDeploymentExample() throws Exception {
        DiagramProject project = new ArchitectureMarkdownParser()
                .parse(Path.of("examples", "markdown", "diagramas", "technical_deployment_piloto_minimo.md"));

        assertEquals(DiagramTypeId.TECHNICAL_DEPLOYMENT, project.metadata().diagramTypeId());
        assertTrue(project.architectureDiagram().isPresent());
        assertEquals(ArchitectureDiagramKind.TECHNICAL_DEPLOYMENT, project.architectureDiagram().orElseThrow().diagramKind());
        assertEquals(7, project.architectureDiagram().orElseThrow().nodes().size());
        assertEquals(2, project.architectureDiagram().orElseThrow().edges().size());
    }

    @Test
    void shouldExportImportableArchitectureMarkdown() throws Exception {
        DiagramProject project = new ArchitectureMarkdownParser()
                .parse(Path.of("examples", "markdown", "diagramas", "c4_containers_sistema_administrativo_minimo.md"));

        String markdown = new ArchitectureMarkdownExporter().export(project);

        assertTrue(markdown.contains("diagram_type: \"c4-containers\""));
        assertTrue(markdown.contains("# Contenedores"));
        assertTrue(markdown.contains("# Relaciones"));
    }
}
