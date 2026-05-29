package com.marcosmoreira.domainmodelstudio.infrastructure.markdown;

import com.marcosmoreira.domainmodelstudio.application.export.MarkdownDiagramExporter;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphDocument;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphEdge;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphEdgeDirection;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphNode;
import java.util.Comparator;
import java.util.Objects;

/** Exporta Grafo libre como Markdown oficial e importable por la propia aplicación. */
public final class FreeGraphMarkdownExporter implements MarkdownDiagramExporter {

    @Override
    public String export(DiagramProject project) {
        Objects.requireNonNull(project, "project");
        if (!DiagramTypeId.FREE_GRAPH.equals(project.metadata().diagramTypeId())) {
            throw new IllegalArgumentException("La salida activa no es un Grafo libre.");
        }
        FreeGraphDocument document = project.freeGraph()
                .orElseThrow(() -> new IllegalArgumentException("El proyecto no contiene Grafo libre."));
        return export(document);
    }

    public String export(FreeGraphDocument document) {
        Objects.requireNonNull(document, "document");
        StringBuilder markdown = new StringBuilder(4096);
        markdown.append("---\n")
                .append("dms_version: \"1\"\n")
                .append("diagram_type: \"").append(DiagramTypeId.FREE_GRAPH.value()).append("\"\n")
                .append("name: \"").append(escape(document.projectName())).append("\"\n")
                .append("version: \"").append(escape(document.version())).append("\"\n")
                .append("graph_kind: \"").append(graphKindValue(document)).append("\"\n")
                .append("sample_kind: \"project\"\n")
                .append("status: \"importable\"\n")
                .append("importable: true\n")
                .append("intended_output: \"diagrama visual\"\n")
                .append("---\n\n")
                .append("# Nodos\n\n");
        if (document.nodes().isEmpty()) {
            markdown.append("> Sin nodos registrados todavía.\n\n");
        } else {
            document.nodes().stream()
                    .sorted(Comparator.comparingInt(FreeGraphNode::orderIndex).thenComparing(FreeGraphNode::title))
                    .forEach(node -> writeNode(markdown, node));
        }
        markdown.append("# Relaciones\n\n");
        if (document.edges().isEmpty()) {
            markdown.append("> Sin relaciones registradas todavía.\n");
        } else {
            document.edges().forEach(edge -> writeEdge(markdown, edge));
        }
        if (!document.notes().isBlank()) {
            markdown.append("\n# Observaciones\n\n").append(document.notes()).append('\n');
        }
        return markdown.toString();
    }

    private static void writeNode(StringBuilder markdown, FreeGraphNode node) {
        markdown.append("## ").append(node.title()).append("\n")
                .append("id: ").append(node.id()).append("\n");
        if (!node.content().isBlank()) {
            markdown.append("contenido: ").append(node.content()).append("\n");
        }
        markdown.append('\n');
    }

    private static void writeEdge(StringBuilder markdown, FreeGraphEdge edge) {
        String connector = edge.direction() == FreeGraphEdgeDirection.UNDIRECTED ? " -- " : " -> ";
        markdown.append("- ").append(edge.sourceNodeId()).append(connector).append(edge.targetNodeId());
        if (!edge.label().isBlank()) {
            markdown.append(": ").append(edge.label());
        }
        markdown.append("\n");
    }

    private static String graphKindValue(FreeGraphDocument document) {
        return switch (document.graphKind()) {
            case DIRECTED -> "directed";
            case UNDIRECTED -> "undirected";
            case MIXED -> "mixed";
        };
    }

    private static String escape(String value) {
        return value == null ? "" : value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
