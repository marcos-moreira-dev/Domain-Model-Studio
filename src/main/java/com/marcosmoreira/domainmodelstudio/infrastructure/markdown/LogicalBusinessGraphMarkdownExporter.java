package com.marcosmoreira.domainmodelstudio.infrastructure.markdown;

import com.marcosmoreira.domainmodelstudio.application.export.MarkdownDiagramExporter;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphDocument;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphEdge;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphNode;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphNodeKind;
import java.util.Comparator;
import java.util.Objects;

/**
 * Exporta el Grafo lógico del negocio como Markdown oficial e importable.
 *
 * <p>La salida conserva la leyenda de abreviaciones, el conjunto de nodos tipados y las
 * relaciones semánticas. También protege el caso de proyecto vacío generando una
 * plantilla mínima reimportable, porque el Markdown oficial debe servir tanto para
 * documentación como para ida y vuelta con IA.</p>
 
 *
 * <p><strong>Ejemplo pedagógico:</strong> un grafo vacío se exporta con un macroflujo
 * y un flujo mínimo para que el archivo resultante pueda reimportarse. Eso convierte
 * el Markdown en una plantilla útil, no en una salida rota.</p>
 */
public final class LogicalBusinessGraphMarkdownExporter implements MarkdownDiagramExporter {

    @Override
    public String export(DiagramProject project) {
        Objects.requireNonNull(project, "project");
        if (!DiagramTypeId.LOGICAL_BUSINESS_GRAPH.equals(project.metadata().diagramTypeId())) {
            throw new IllegalArgumentException("La salida activa no es un Grafo lógico del negocio.");
        }
        LogicalBusinessGraphDocument document = project.logicalBusinessGraphDocument()
                .orElseThrow(() -> new IllegalArgumentException("El proyecto no contiene Grafo lógico del negocio."));
        return export(document);
    }

    public String export(LogicalBusinessGraphDocument document) {
        Objects.requireNonNull(document, "document");
        StringBuilder markdown = new StringBuilder(8192);
        markdown.append("---\n")
                .append("dms_version: \"1\"\n")
                .append("diagram_type: \"").append(DiagramTypeId.LOGICAL_BUSINESS_GRAPH.value()).append("\"\n")
                .append("name: \"").append(escape(document.projectName())).append("\"\n")
                .append("version: \"").append(escape(document.version())).append("\"\n")
                .append("document_date: \"").append(document.documentDate()).append("\"\n")
                .append("sample_kind: \"project\"\n")
                .append("status: \"importable\"\n")
                .append("importable: true\n")
                .append("intended_output: \"grafo lógico editable\"\n")
                .append("---\n\n");
        writeLegend(markdown);
        writeNodes(markdown, document);
        writeEdges(markdown, document);
        if (!document.notes().isBlank()) {
            markdown.append("\n# Observaciones\n\n").append(document.notes()).append('\n');
        }
        return markdown.toString();
    }

    private static void writeLegend(StringBuilder markdown) {
        markdown.append("# Leyenda de abreviaciones\n\n")
                .append("| Código | Significado | Descripción |\n")
                .append("|---|---|---|\n");
        for (LogicalBusinessGraphNodeKind kind : LogicalBusinessGraphNodeKind.values()) {
            markdown.append("| ").append(kind.prefix())
                    .append(" | ").append(kind.displayName())
                    .append(" | ").append(clean(kind.description()))
                    .append(" |\n");
        }
        markdown.append('\n');
    }

    private static void writeNodes(StringBuilder markdown, LogicalBusinessGraphDocument document) {
        markdown.append("# Nodos\n\n")
                .append("| Código | Tipo | Título | Descripción | Estado | Referencias |\n")
                .append("|---|---|---|---|---|---|\n");
        if (document.nodes().isEmpty()) {
            markdown.append("| MF-001 | MF | Macroflujo pendiente | Completar macroflujo principal. | borrador | |\n");
            markdown.append("| FL-001 | FL | Flujo pendiente | Completar flujo inicial del macroflujo. | borrador | |\n");
        } else {
            document.nodes().stream()
                    .sorted(Comparator.comparing(LogicalBusinessGraphNode::code))
                    .forEach(node -> writeNode(markdown, node));
        }
        markdown.append('\n');
    }

    private static void writeNode(StringBuilder markdown, LogicalBusinessGraphNode node) {
        markdown.append("| ").append(node.code())
                .append(" | ").append(node.kind().prefix())
                .append(" | ").append(clean(node.title()))
                .append(" | ").append(clean(node.description()))
                .append(" | ").append(node.status().displayName())
                .append(" | ").append(clean(String.join(", ", node.sourceReferenceIds())))
                .append(" |\n");
    }

    private static void writeEdges(StringBuilder markdown, LogicalBusinessGraphDocument document) {
        markdown.append("# Relaciones\n\n")
                .append("| ID | Origen | Relación | Destino | Descripción |\n")
                .append("|---|---|---|---|---|\n");
        if (document.edges().isEmpty()) {
            markdown.append("| rel-001 | MF-001 | contiene | FL-001 | Completar relación lógica. |\n");
        } else {
            document.edges().forEach(edge -> writeEdge(markdown, edge));
        }
    }

    private static void writeEdge(StringBuilder markdown, LogicalBusinessGraphEdge edge) {
        markdown.append("| ").append(clean(edge.id()))
                .append(" | ").append(edge.sourceCode())
                .append(" | ").append(edge.relationKind().code())
                .append(" | ").append(edge.targetCode())
                .append(" | ").append(clean(edge.description()))
                .append(" |\n");
    }

    private static String clean(String value) {
        return value == null ? "" : value.replace('|', '/').replace("\n", " ").strip();
    }

    private static String escape(String value) {
        return value == null ? "" : value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
