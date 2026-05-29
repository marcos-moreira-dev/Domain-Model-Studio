package com.marcosmoreira.domainmodelstudio.infrastructure.markdown;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.er.AttributeElement;
import com.marcosmoreira.domainmodelstudio.domain.er.AttributeTag;
import com.marcosmoreira.domainmodelstudio.domain.er.EntityElement;
import com.marcosmoreira.domainmodelstudio.domain.er.RelationshipElement;
import java.util.Locale;
import java.util.stream.Collectors;

/** Serializador Markdown simple para mantener sincronizado el archivo fuente importado. */
public final class MarkdownProjectWriter {

    private MarkdownProjectWriter() {
    }

    public static String write(DiagramProject project) {
        StringBuilder builder = new StringBuilder();
        builder.append("---\n");
        builder.append("id: ").append(project.metadata().id()).append('\n');
        builder.append("title: ").append(project.metadata().title()).append('\n');
        builder.append("project_type: ").append(project.metadata().projectType().name()).append('\n');
        builder.append("version: ").append(project.metadata().version()).append('\n');
        builder.append("status: ").append(project.metadata().status()).append('\n');
        builder.append("notation: ").append(project.metadata().activeNotation().name().toLowerCase(Locale.ROOT)).append('\n');
        if (!project.metadata().description().isBlank()) {
            builder.append("description: ").append(project.metadata().description()).append('\n');
        }
        builder.append("---\n\n");
        builder.append("# Entidades\n\n");
        for (EntityElement entity : project.model().entities()) {
            builder.append("## ").append(entity.name()).append("\n");
            builder.append("id: ").append(entity.id().value()).append("\n");
            builder.append("kind: ").append(entity.kind().name().toLowerCase(Locale.ROOT)).append("\n");
            if (!entity.module().isBlank()) {
                builder.append("module: ").append(entity.module()).append("\n");
            }
            if (!entity.description().isBlank()) {
                builder.append("description: ").append(entity.description()).append("\n");
            }
            for (AttributeElement attribute : entity.attributes()) {
                builder.append("- ").append(attribute.name());
                if (!attribute.tags().isEmpty()) {
                    builder.append(" [").append(attribute.tags().stream()
                            .map(MarkdownProjectWriter::tagName)
                            .sorted()
                            .collect(Collectors.joining(", "))).append("]");
                }
                builder.append("\n");
            }
            builder.append("\n");
        }
        builder.append("# Relaciones\n\n");
        for (RelationshipElement relationship : project.model().relationships()) {
            builder.append("## ").append(relationship.name()).append("\n");
            builder.append("id: ").append(relationship.id().value()).append("\n");
            builder.append("from: ").append(relationship.fromEntityId().value()).append("\n");
            builder.append("to: ").append(relationship.toEntityId().value()).append("\n");
            builder.append("from_cardinality: ").append(relationship.fromCardinality().displayText()).append("\n");
            builder.append("to_cardinality: ").append(relationship.toCardinality().displayText()).append("\n");
            builder.append("from_participation: ").append(relationship.fromParticipation().name().toLowerCase(Locale.ROOT)).append("\n");
            builder.append("to_participation: ").append(relationship.toParticipation().name().toLowerCase(Locale.ROOT)).append("\n");
            builder.append("kind: ").append(relationship.kind().name().toLowerCase(Locale.ROOT)).append("\n");
            if (!relationship.description().isBlank()) {
                builder.append("description: ").append(relationship.description()).append("\n");
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    private static String tagName(AttributeTag tag) {
        return tag.name().toLowerCase(Locale.ROOT);
    }
}
