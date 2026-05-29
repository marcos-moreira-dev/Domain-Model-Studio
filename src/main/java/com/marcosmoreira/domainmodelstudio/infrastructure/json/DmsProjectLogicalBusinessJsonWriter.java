package com.marcosmoreira.domainmodelstudio.infrastructure.json;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessAttributeCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessDocument;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessEntityCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItem;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessPendingQuestion;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessRelationshipCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessSection;
import java.util.List;

/** Escribe el payload documental de levantamiento lógico dentro del bloque model. */
final class DmsProjectLogicalBusinessJsonWriter {

    void write(LogicalBusinessDocument document, StringBuilder json, int level, boolean comma) {
        indent(json, level).append("\"logicalBusinessDocument\": {\n");
        field(json, level + 1, "projectName", document.projectName(), true);
        field(json, level + 1, "version", document.version(), true);
        field(json, level + 1, "documentDate", document.documentDate().toString(), true);
        field(json, level + 1, "documentStatus", document.documentStatus().name(), true);
        field(json, level + 1, "mainSource", document.mainSource(), true);
        field(json, level + 1, "notes", document.notes(), true);
        writeSections(document.sections(), json, level + 1, true);
        writeItems(document.items(), json, level + 1, true);
        writeEntities(document.entityCandidates(), json, level + 1, true);
        writePendingQuestions(document.pendingQuestions(), json, level + 1, true);
        writeMaturity(document.maturity(), json, level + 1, false);
        indent(json, level).append("}").append(comma ? "," : "").append("\n");
    }

    private void writeSections(List<LogicalBusinessSection> sections, StringBuilder json, int level, boolean comma) {
        indent(json, level).append("\"sections\": [\n");
        for (int i = 0; i < sections.size(); i++) {
            LogicalBusinessSection section = sections.get(i);
            indent(json, level + 1).append("{\n");
            field(json, level + 2, "id", section.id(), true);
            field(json, level + 2, "title", section.title(), true);
            field(json, level + 2, "purpose", section.purpose(), true);
            field(json, level + 2, "status", section.status().name(), true);
            writeStringArray("itemIds", section.itemIds(), json, level + 2, true);
            field(json, level + 2, "notes", section.notes(), false);
            indent(json, level + 1).append("}").append(i + 1 < sections.size() ? "," : "").append("\n");
        }
        indent(json, level).append("]").append(comma ? "," : "").append("\n");
    }

    private void writeItems(List<LogicalBusinessItem> items, StringBuilder json, int level, boolean comma) {
        indent(json, level).append("\"items\": [\n");
        for (int i = 0; i < items.size(); i++) {
            LogicalBusinessItem item = items.get(i);
            indent(json, level + 1).append("{\n");
            field(json, level + 2, "id", item.id(), true);
            field(json, level + 2, "kind", item.kind().name(), true);
            field(json, level + 2, "title", item.title(), true);
            field(json, level + 2, "status", item.status().name(), true);
            field(json, level + 2, "source", item.source(), true);
            field(json, level + 2, "description", item.description(), true);
            field(json, level + 2, "humanReading", item.humanReading(), true);
            field(json, level + 2, "content", item.content(), true);
            writeStringArray("referenceIds", item.referenceIds(), json, level + 2, false);
            indent(json, level + 1).append("}").append(i + 1 < items.size() ? "," : "").append("\n");
        }
        indent(json, level).append("]").append(comma ? "," : "").append("\n");
    }

    private void writeEntities(List<LogicalBusinessEntityCandidate> entities, StringBuilder json, int level, boolean comma) {
        indent(json, level).append("\"entityCandidates\": [\n");
        for (int i = 0; i < entities.size(); i++) {
            writeEntity(entities.get(i), json, level + 1, i + 1 < entities.size());
        }
        indent(json, level).append("]").append(comma ? "," : "").append("\n");
    }

    private void writeEntity(LogicalBusinessEntityCandidate entity, StringBuilder json, int level, boolean comma) {
        indent(json, level).append("{\n");
        field(json, level + 1, "id", entity.id(), true);
        field(json, level + 1, "name", entity.name(), true);
        field(json, level + 1, "status", entity.status().name(), true);
        field(json, level + 1, "logicalJustification", entity.logicalJustification(), true);
        writeStringArray("sourceReferences", entity.sourceReferences(), json, level + 1, true);
        writeStringArray("associatedRules", entity.associatedRules(), json, level + 1, true);
        writeStringArray("associatedInvariants", entity.associatedInvariants(), json, level + 1, true);
        writeStringArray("createdByUseCases", entity.createdByUseCases(), json, level + 1, true);
        writeStringArray("modifiedByUseCases", entity.modifiedByUseCases(), json, level + 1, true);
        writeStringArray("queriedByUseCases", entity.queriedByUseCases(), json, level + 1, true);
        field(json, level + 1, "modelingRisk", entity.modelingRisk(), true);
        writeAttributes(entity.attributes(), json, level + 1, true);
        writeRelationships(entity.relationships(), json, level + 1, false);
        indent(json, level).append("}").append(comma ? "," : "").append("\n");
    }

    private void writeAttributes(List<LogicalBusinessAttributeCandidate> attributes, StringBuilder json, int level, boolean comma) {
        indent(json, level).append("\"attributes\": [\n");
        for (int i = 0; i < attributes.size(); i++) {
            LogicalBusinessAttributeCandidate attribute = attributes.get(i);
            indent(json, level + 1).append("{\n");
            field(json, level + 2, "id", attribute.id(), true);
            field(json, level + 2, "entityId", attribute.entityId(), true);
            field(json, level + 2, "name", attribute.name(), true);
            field(json, level + 2, "reason", attribute.reason(), true);
            field(json, level + 2, "tentativeType", attribute.tentativeType(), true);
            booleanField(json, level + 2, "calculated", attribute.calculated(), true);
            field(json, level + 2, "formula", attribute.formula(), true);
            field(json, level + 2, "riskIfWrong", attribute.riskIfWrong(), true);
            writeStringArray("sourceReferences", attribute.sourceReferences(), json, level + 2, true);
            writeStringArray("relatedRules", attribute.relatedRules(), json, level + 2, true);
            writeStringArray("relatedInvariants", attribute.relatedInvariants(), json, level + 2, false);
            indent(json, level + 1).append("}").append(i + 1 < attributes.size() ? "," : "").append("\n");
        }
        indent(json, level).append("]").append(comma ? "," : "").append("\n");
    }

    private void writeRelationships(List<LogicalBusinessRelationshipCandidate> relationships, StringBuilder json, int level, boolean comma) {
        indent(json, level).append("\"relationships\": [\n");
        for (int i = 0; i < relationships.size(); i++) {
            LogicalBusinessRelationshipCandidate relationship = relationships.get(i);
            indent(json, level + 1).append("{\n");
            field(json, level + 2, "id", relationship.id(), true);
            field(json, level + 2, "sourceEntityId", relationship.sourceEntityId(), true);
            field(json, level + 2, "targetEntityId", relationship.targetEntityId(), true);
            field(json, level + 2, "name", relationship.name(), true);
            field(json, level + 2, "cardinalityHint", relationship.cardinalityHint(), true);
            field(json, level + 2, "justification", relationship.justification(), true);
            writeStringArray("sourceReferences", relationship.sourceReferences(), json, level + 2, false);
            indent(json, level + 1).append("}").append(i + 1 < relationships.size() ? "," : "").append("\n");
        }
        indent(json, level).append("]").append(comma ? "," : "").append("\n");
    }

    private void writePendingQuestions(List<LogicalBusinessPendingQuestion> questions, StringBuilder json, int level, boolean comma) {
        indent(json, level).append("\"pendingQuestions\": [\n");
        for (int i = 0; i < questions.size(); i++) {
            LogicalBusinessPendingQuestion question = questions.get(i);
            indent(json, level + 1).append("{\n");
            field(json, level + 2, "id", question.id(), true);
            field(json, level + 2, "question", question.question(), true);
            field(json, level + 2, "affects", question.affects(), true);
            field(json, level + 2, "priority", question.priority().name(), true);
            field(json, level + 2, "status", question.status().name(), false);
            indent(json, level + 1).append("}").append(i + 1 < questions.size() ? "," : "").append("\n");
        }
        indent(json, level).append("]").append(comma ? "," : "").append("\n");
    }

    private void writeMaturity(com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessMaturity maturity, StringBuilder json, int level, boolean comma) {
        indent(json, level).append("\"maturity\": {\n");
        field(json, level + 1, "level", maturity.level().name(), true);
        writeStringArray("strengths", maturity.strengths(), json, level + 1, true);
        writeStringArray("blockers", maturity.blockers(), json, level + 1, true);
        writeStringArray("nextSteps", maturity.nextSteps(), json, level + 1, false);
        indent(json, level).append("}").append(comma ? "," : "").append("\n");
    }

    private void writeStringArray(String name, List<String> values, StringBuilder json, int level, boolean comma) {
        indent(json, level).append(quote(name)).append(": [");
        for (int i = 0; i < values.size(); i++) {
            if (i > 0) json.append(", ");
            json.append(quote(values.get(i)));
        }
        json.append("]").append(comma ? "," : "").append("\n");
    }

    private void field(StringBuilder json, int level, String name, String value, boolean comma) {
        indent(json, level).append(quote(name)).append(": ").append(quote(value)).append(comma ? "," : "").append("\n");
    }

    private void booleanField(StringBuilder json, int level, String name, boolean value, boolean comma) {
        indent(json, level).append(quote(name)).append(": ").append(value).append(comma ? "," : "").append("\n");
    }

    private StringBuilder indent(StringBuilder json, int level) {
        return json.append("  ".repeat(Math.max(0, level)));
    }

    private String quote(String value) {
        return JsonStringEscaper.quote(value);
    }
}
