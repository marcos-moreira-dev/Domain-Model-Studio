package com.marcosmoreira.domainmodelstudio.infrastructure.markdown;

import com.marcosmoreira.domainmodelstudio.application.behavior.SequenceMessageOrderPolicy;
import com.marcosmoreira.domainmodelstudio.application.export.MarkdownDiagramExporter;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramKind;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorEdge;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNode;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNodeKind;
import com.marcosmoreira.domainmodelstudio.domain.behavior.SequenceCombinedFragmentSpec;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import java.util.List;
import java.util.Objects;

/** Exporta Markdown oficial para BPMN básico, flujo operativo y UML de comportamiento. */
public final class BehaviorMarkdownExporter implements MarkdownDiagramExporter {

    private final SequenceMessageOrderPolicy sequenceMessageOrderPolicy = new SequenceMessageOrderPolicy();

    @Override
    public String export(DiagramProject project) {
        Objects.requireNonNull(project, "project");
        BehaviorDiagramDocument document = project.behaviorDiagram()
                .orElseThrow(() -> new IllegalArgumentException("El proyecto no contiene diagrama de comportamiento."));
        StringBuilder builder = new StringBuilder();
        builder.append("---\n");
        builder.append("dms_version: \"1\"\n");
        builder.append("diagram_type: \"").append(document.diagramKind().diagramTypeId().value()).append("\"\n");
        builder.append("name: \"").append(escape(document.projectName())).append("\"\n");
        builder.append("sample_kind: \"exported\"\n");
        builder.append("domain: \"general\"\n");
        builder.append("status: \"importable\"\n");
        builder.append("importable: true\n");
        builder.append("intended_output: \"diagrama visual\"\n");
        builder.append("---\n\n");
        writeBody(builder, document);
        return builder.toString();
    }

    private void writeBody(StringBuilder builder, BehaviorDiagramDocument document) {
        if (document.diagramKind() == BehaviorDiagramKind.UML_SEQUENCE) {
            writeSequenceBody(builder, document);
            return;
        }
        BehaviorDiagramKind kind = document.diagramKind();
        builder.append("# ").append(kind.displayName()).append("\n\n");
        builder.append("Nombre: ").append(document.projectName()).append("\n\n");
        if (!document.notes().isBlank()) {
            builder.append("# Notas\n\n").append(document.notes()).append("\n\n");
        }
        if (kind == BehaviorDiagramKind.UML_STATE) {
            builder.append("# Estados\n\n");
        } else if (kind == BehaviorDiagramKind.UML_USE_CASE) {
            builder.append("# Elementos\n\n");
        } else {
            builder.append("# Flujo\n\n");
        }
        for (BehaviorNode node : document.nodes()) {
            builder.append("- ")
                    .append(node.kind().displayName().toLowerCase(java.util.Locale.ROOT))
                    .append(": ")
                    .append(node.displayName());
            if (!node.description().isBlank()) {
                builder.append(" — ").append(node.description());
            }
            builder.append("\n");
        }
        if (!document.edges().isEmpty()) {
            builder.append("\n# Relaciones\n\n");
            for (BehaviorEdge edge : document.edges()) {
                writeEdge(builder, document, edge, false, 0);
            }
        }
    }

    private void writeSequenceBody(StringBuilder builder, BehaviorDiagramDocument document) {
        builder.append("# UML Secuencia\n\n");
        builder.append("Nombre: ").append(document.projectName()).append("\n\n");
        if (!document.notes().isBlank()) {
            builder.append("# Notas generales\n\n").append(document.notes()).append("\n\n");
        }
        writeSequenceNodes(builder, "Participantes", document, BehaviorNodeKind.PARTICIPANT);
        writeSequenceNodes(builder, "Activaciones", document, BehaviorNodeKind.ACTIVATION);
        writeSequenceFragments(builder, document);
        writeSequenceNodes(builder, "Notas", document, BehaviorNodeKind.NOTE);
        List<BehaviorEdge> messages = sequenceMessageOrderPolicy.orderedMessages(document);
        if (!messages.isEmpty()) {
            builder.append("# Mensajes\n\n");
            int index = 1;
            for (BehaviorEdge edge : messages) {
                writeEdge(builder, document, edge, true, index++);
            }
            builder.append("\n");
        }
    }

    private void writeSequenceNodes(StringBuilder builder, String title, BehaviorDiagramDocument document, BehaviorNodeKind kind) {
        List<BehaviorNode> nodes = document.nodes().stream().filter(node -> node.kind() == kind).toList();
        if (nodes.isEmpty()) {
            return;
        }
        builder.append("# ").append(title).append("\n\n");
        for (BehaviorNode node : nodes) {
            builder.append("- ");
            if (kind != BehaviorNodeKind.PARTICIPANT) {
                builder.append(kind.displayName().toLowerCase(java.util.Locale.ROOT)).append(": ");
            }
            builder.append(node.displayName());
            if (!node.owner().isBlank()) {
                builder.append(" @").append(node.owner());
            }
            if (!node.description().isBlank()) {
                builder.append(": ").append(node.description());
            }
            builder.append("\n");
        }
        builder.append("\n");
    }

    private void writeSequenceFragments(StringBuilder builder, BehaviorDiagramDocument document) {
        List<BehaviorNode> fragments = document.nodes().stream()
                .filter(node -> node.kind() == BehaviorNodeKind.FRAGMENT)
                .toList();
        if (fragments.isEmpty()) {
            return;
        }
        builder.append("# Fragmentos combinados\n\n");
        for (BehaviorNode fragment : fragments) {
            SequenceCombinedFragmentSpec spec = SequenceCombinedFragmentSpec.fromNode(fragment);
            builder.append(spec.canonicalMarkdownLine(fragment.id())).append("\n");
        }
        builder.append("\n");
    }

    private void writeEdge(StringBuilder builder, BehaviorDiagramDocument document, BehaviorEdge edge, boolean numbered, int number) {
        String source = document.nodeById(edge.sourceNodeId()).map(BehaviorNode::displayName).orElse(edge.sourceNodeId());
        String target = document.nodeById(edge.targetNodeId()).map(BehaviorNode::displayName).orElse(edge.targetNodeId());
        builder.append(numbered ? number + ". " : "- ").append(source).append(" -> ").append(target);
        String label = edge.label().isBlank() ? edge.kind().displayName() : edge.label();
        if (!label.isBlank()) {
            builder.append(": ").append(label);
        }
        builder.append("\n");
    }

    private static String escape(String text) {
        return text == null ? "" : text.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
