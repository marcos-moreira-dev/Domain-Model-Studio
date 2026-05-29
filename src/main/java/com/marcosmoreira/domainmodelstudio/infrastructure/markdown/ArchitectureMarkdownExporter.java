package com.marcosmoreira.domainmodelstudio.infrastructure.markdown;

import com.marcosmoreira.domainmodelstudio.application.export.MarkdownDiagramExporter;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureDiagramKind;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureEdge;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureNode;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureNodeKind;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/** Exporta C4 Contexto, C4 Contenedores y Despliegue técnico como Markdown oficial. */
public final class ArchitectureMarkdownExporter implements MarkdownDiagramExporter {

    @Override
    public String export(DiagramProject project) {
        Objects.requireNonNull(project, "project");
        ArchitectureDiagramDocument document = project.architectureDiagram()
                .orElseThrow(() -> new IllegalArgumentException("El proyecto no contiene diagrama de arquitectura."));
        StringBuilder markdown = new StringBuilder();
        writeFrontMatter(markdown, document);
        markdown.append("# ").append(document.diagramKind().displayName()).append("\n\n");
        switch (document.diagramKind()) {
            case C4_CONTEXT -> writeContext(markdown, document);
            case C4_CONTAINERS -> writeContainers(markdown, document);
            case TECHNICAL_DEPLOYMENT -> writeDeployment(markdown, document);
        }
        if (!document.notes().isBlank()) {
            markdown.append("\n# Observaciones\n\n").append(document.notes()).append("\n");
        }
        return markdown.toString();
    }

    private static void writeFrontMatter(StringBuilder markdown, ArchitectureDiagramDocument document) {
        markdown.append("---\n");
        markdown.append("dms_version: \"1\"\n");
        markdown.append("diagram_type: \"").append(document.diagramKind().diagramTypeId().value()).append("\"\n");
        markdown.append("name: \"").append(escape(document.projectName())).append("\"\n");
        markdown.append("sample_kind: \"project\"\n");
        markdown.append("domain: \"arquitectura de software\"\n");
        markdown.append("status: \"importable\"\n");
        markdown.append("importable: true\n");
        markdown.append("intended_output: \"diagrama visual\"\n");
        markdown.append("---\n");
    }

    private static void writeContext(StringBuilder markdown, ArchitectureDiagramDocument document) {
        List<ArchitectureNode> systems = nodesOf(document, ArchitectureNodeKind.SOFTWARE_SYSTEM, ArchitectureNodeKind.BOUNDARY);
        List<ArchitectureNode> people = nodesOf(document, ArchitectureNodeKind.PERSON);
        List<ArchitectureNode> externalSystems = nodesOf(document, ArchitectureNodeKind.EXTERNAL_SYSTEM);
        if (!systems.isEmpty()) {
            ArchitectureNode main = systems.get(0);
            markdown.append("# Contexto\n\n");
            markdown.append("Sistema: ").append(main.displayName()).append("\n");
            if (!main.description().isBlank()) markdown.append("Propósito: ").append(main.description()).append("\n");
            markdown.append("\n");
        }
        writeNodeList(markdown, "# Personas", people);
        writeNodeList(markdown, "# Sistemas externos", externalSystems);
        writeRelations(markdown, "# Relaciones", document);
    }

    private static void writeContainers(StringBuilder markdown, ArchitectureDiagramDocument document) {
        writeNodeList(markdown, "# Contenedores", ordered(document.nodes()));
        writeRelations(markdown, "# Relaciones", document);
    }

    private static void writeDeployment(StringBuilder markdown, ArchitectureDiagramDocument document) {
        writeNodeList(markdown, "# Ambientes", nodesOf(document, ArchitectureNodeKind.ENVIRONMENT));
        List<ArchitectureNode> nodes = document.nodes().stream()
                .filter(node -> node.kind() != ArchitectureNodeKind.ENVIRONMENT)
                .sorted(Comparator.comparingInt(ArchitectureNode::orderIndex).thenComparing(ArchitectureNode::displayName))
                .toList();
        writeNodeList(markdown, "# Nodos", nodes);
        writeRelations(markdown, "# Conexiones", document);
    }

    private static void writeNodeList(StringBuilder markdown, String title, List<ArchitectureNode> nodes) {
        markdown.append(title).append("\n\n");
        if (nodes.isEmpty()) {
            markdown.append("- Elemento pendiente: describe aquí el elemento.\n\n");
            return;
        }
        for (ArchitectureNode node : nodes) {
            markdown.append("- ").append(node.displayName());
            String description = descriptionFor(node);
            if (!description.isBlank()) markdown.append(": ").append(description);
            markdown.append("\n");
        }
        markdown.append("\n");
    }

    private static void writeRelations(StringBuilder markdown, String title, ArchitectureDiagramDocument document) {
        markdown.append(title).append("\n\n");
        if (document.edges().isEmpty()) {
            markdown.append("- Elemento A -> Elemento B: describe la relación.\n");
            return;
        }
        for (ArchitectureEdge edge : document.edges()) {
            String source = document.nodeById(edge.sourceNodeId()).map(ArchitectureNode::displayName).orElse(edge.sourceNodeId());
            String target = document.nodeById(edge.targetNodeId()).map(ArchitectureNode::displayName).orElse(edge.targetNodeId());
            markdown.append("- ").append(source).append(" -> ").append(target).append(": ");
            markdown.append(edge.label().isBlank() ? edge.kind().displayName() : edge.label());
            if (!edge.protocol().isBlank()) markdown.append(" (").append(edge.protocol()).append(")");
            markdown.append("\n");
        }
    }

    private static String descriptionFor(ArchitectureNode node) {
        StringBuilder text = new StringBuilder();
        if (!node.description().isBlank()) text.append(node.description());
        if (!node.technology().isBlank()) {
            if (!text.isEmpty()) text.append(" · ");
            text.append(node.technology());
        }
        return text.toString();
    }

    private static List<ArchitectureNode> nodesOf(ArchitectureDiagramDocument document, ArchitectureNodeKind... kinds) {
        List<ArchitectureNodeKind> accepted = List.of(kinds);
        return document.nodes().stream()
                .filter(node -> accepted.contains(node.kind()))
                .sorted(Comparator.comparingInt(ArchitectureNode::orderIndex).thenComparing(ArchitectureNode::displayName))
                .toList();
    }

    private static List<ArchitectureNode> ordered(List<ArchitectureNode> nodes) {
        return nodes.stream()
                .sorted(Comparator.comparingInt(ArchitectureNode::orderIndex).thenComparing(ArchitectureNode::displayName))
                .toList();
    }

    private static String escape(String value) {
        return value == null ? "" : value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
