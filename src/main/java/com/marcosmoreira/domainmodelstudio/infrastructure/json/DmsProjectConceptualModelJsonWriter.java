package com.marcosmoreira.domainmodelstudio.infrastructure.json;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.er.AttributeElement;
import com.marcosmoreira.domainmodelstudio.domain.er.EntityElement;
import com.marcosmoreira.domainmodelstudio.domain.er.RelationshipElement;

/**
 * Escribe el bloque {@code model} del archivo {@code .dms} sin mezclar documentos especializados.
 *
 * <p>El bloque mantiene primero los documentos especializados opcionales y luego el modelo conceptual
 * legacy de entidades y relaciones. Separarlo del writer principal evita que el coordinador del formato
 * durable conozca detalles de cada elemento conceptual.</p>
 */
final class DmsProjectConceptualModelJsonWriter {

    void writeModel(DiagramProject project, StringBuilder json, int level) {
        indent(json, level).append("\"model\": {\n");
        field(json, level + 1, "modelKind", project.metadata().diagramTypeId().value(), true);
        new DmsProjectSpecializedJsonWriter().writeSpecializedDocuments(project, json, level + 1);
        writeEntities(project, json, level + 1);
        json.append(",\n");
        writeRelationships(project, json, level + 1);
        indent(json, level).append("}");
    }

    private void writeEntities(DiagramProject project, StringBuilder json, int level) {
        indent(json, level).append("\"entities\": [\n");
        for (int i = 0; i < project.model().entities().size(); i++) {
            EntityElement entity = project.model().entities().get(i);
            indent(json, level + 1).append("{\n");
            field(json, level + 2, "id", entity.id().value(), true);
            field(json, level + 2, "name", entity.name(), true);
            field(json, level + 2, "kind", entity.kind().name(), true);
            field(json, level + 2, "module", entity.module(), true);
            field(json, level + 2, "description", entity.description(), true);
            writeAttributes(entity, json, level + 2);
            indent(json, level + 1).append("}").append(i + 1 < project.model().entities().size() ? "," : "").append("\n");
        }
        indent(json, level).append("]");
    }

    private void writeAttributes(EntityElement entity, StringBuilder json, int level) {
        indent(json, level).append("\"attributes\": [\n");
        for (int i = 0; i < entity.attributes().size(); i++) {
            AttributeElement attribute = entity.attributes().get(i);
            indent(json, level + 1).append("{\n");
            field(json, level + 2, "id", attribute.id().value(), true);
            field(json, level + 2, "name", attribute.name(), true);
            indent(json, level + 2).append("\"tags\": [");
            int index = 0;
            for (var tag : attribute.tags()) {
                if (index++ > 0) {
                    json.append(", ");
                }
                json.append(quote(tag.name()));
            }
            json.append("],\n");
            field(json, level + 2, "description", attribute.description(), false);
            indent(json, level + 1).append("}").append(i + 1 < entity.attributes().size() ? "," : "").append("\n");
        }
        indent(json, level).append("]\n");
    }

    private void writeRelationships(DiagramProject project, StringBuilder json, int level) {
        indent(json, level).append("\"relationships\": [\n");
        for (int i = 0; i < project.model().relationships().size(); i++) {
            RelationshipElement relationship = project.model().relationships().get(i);
            indent(json, level + 1).append("{\n");
            field(json, level + 2, "id", relationship.id().value(), true);
            field(json, level + 2, "name", relationship.name(), true);
            field(json, level + 2, "fromEntityId", relationship.fromEntityId().value(), true);
            field(json, level + 2, "toEntityId", relationship.toEntityId().value(), true);
            field(json, level + 2, "fromCardinality", relationship.fromCardinality().displayText(), true);
            field(json, level + 2, "toCardinality", relationship.toCardinality().displayText(), true);
            field(json, level + 2, "kind", relationship.kind().name(), true);
            field(json, level + 2, "fromParticipation", relationship.fromParticipation().name(), true);
            field(json, level + 2, "toParticipation", relationship.toParticipation().name(), true);
            field(json, level + 2, "description", relationship.description(), false);
            indent(json, level + 1).append("}").append(i + 1 < project.model().relationships().size() ? "," : "").append("\n");
        }
        indent(json, level).append("]\n");
    }

    private void field(StringBuilder json, int level, String name, String value, boolean comma) {
        indent(json, level).append(quote(name)).append(": ").append(quote(value)).append(comma ? "," : "").append("\n");
    }

    private StringBuilder indent(StringBuilder json, int level) {
        return json.append("  ".repeat(Math.max(0, level)));
    }

    private String quote(String value) {
        return JsonStringEscaper.quote(value == null ? "" : value);
    }
}
