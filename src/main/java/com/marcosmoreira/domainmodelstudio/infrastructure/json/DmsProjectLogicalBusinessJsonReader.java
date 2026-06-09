package com.marcosmoreira.domainmodelstudio.infrastructure.json;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessAttributeCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessDocument;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessDocumentStatus;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessEntityCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItem;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItemKind;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItemStatus;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessMaturity;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessMaturityLevel;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessPendingQuestion;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessQuestionPriority;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessRelationshipCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessSection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Lee el payload documental de levantamiento lógico desde un archivo .dms. */
final class DmsProjectLogicalBusinessJsonReader {

    LogicalBusinessDocument read(Map<String, Object> object) {
        List<LogicalBusinessSection> sections = readSections(array(object, "sections"));
        List<LogicalBusinessItem> items = readItems(array(object, "items"));
        List<LogicalBusinessEntityCandidate> entities = readEntities(array(object, "entityCandidates"));
        List<LogicalBusinessPendingQuestion> questions = readPendingQuestions(array(object, "pendingQuestions"));
        return new LogicalBusinessDocument(
                stringOrDefault(object, "projectName", "Levantamiento lógico"),
                stringOrDefault(object, "version", "v0.1"),
                readDate(stringOrDefault(object, "documentDate", LocalDate.now().toString())),
                enumValue(LogicalBusinessDocumentStatus.class,
                        stringOrDefault(object, "documentStatus", "DRAFT"),
                        LogicalBusinessDocumentStatus.DRAFT),
                stringOrDefault(object, "mainSource", ""),
                sections,
                items,
                entities,
                questions,
                readMaturity(asOptionalObject(object.get("maturity"))),
                stringOrDefault(object, "notes", "")
        );
    }

    private List<LogicalBusinessSection> readSections(List<Object> values) {
        List<LogicalBusinessSection> sections = new ArrayList<>();
        for (Object value : values) {
            Map<String, Object> object = asObject(value, "logicalBusinessSection");
            sections.add(new LogicalBusinessSection(
                    string(object, "id"),
                    stringOrDefault(object, "title", string(object, "id")),
                    stringOrDefault(object, "purpose", ""),
                    enumValue(LogicalBusinessItemStatus.class,
                            stringOrDefault(object, "status", "DRAFT"),
                            LogicalBusinessItemStatus.DRAFT),
                    stringList(array(object, "itemIds")),
                    stringOrDefault(object, "notes", "")
            ));
        }
        return sections;
    }

    private List<LogicalBusinessItem> readItems(List<Object> values) {
        List<LogicalBusinessItem> items = new ArrayList<>();
        for (Object value : values) {
            Map<String, Object> object = asObject(value, "logicalBusinessItem");
            items.add(new LogicalBusinessItem(
                    string(object, "id"),
                    enumValue(LogicalBusinessItemKind.class,
                            stringOrDefault(object, "kind", "CONCEPT"),
                            LogicalBusinessItemKind.CONCEPT),
                    stringOrDefault(object, "title", string(object, "id")),
                    enumValue(LogicalBusinessItemStatus.class,
                            stringOrDefault(object, "status", "DRAFT"),
                            LogicalBusinessItemStatus.DRAFT),
                    stringOrDefault(object, "source", ""),
                    stringOrDefault(object, "description", ""),
                    stringOrDefault(object, "humanReading", ""),
                    stringOrDefault(object, "content", ""),
                    stringList(array(object, "referenceIds"))
            ));
        }
        return items;
    }

    private List<LogicalBusinessEntityCandidate> readEntities(List<Object> values) {
        List<LogicalBusinessEntityCandidate> entities = new ArrayList<>();
        for (Object value : values) {
            Map<String, Object> object = asObject(value, "logicalBusinessEntityCandidate");
            entities.add(new LogicalBusinessEntityCandidate(
                    string(object, "id"),
                    stringOrDefault(object, "name", string(object, "id")),
                    enumValue(LogicalBusinessItemStatus.class,
                            stringOrDefault(object, "status", "DRAFT"),
                            LogicalBusinessItemStatus.DRAFT),
                    stringOrDefault(object, "logicalJustification", "Entidad derivada del levantamiento."),
                    readAttributes(array(object, "attributes")),
                    readRelationships(array(object, "relationships")),
                    stringList(array(object, "sourceReferences")),
                    stringList(array(object, "associatedRules")),
                    stringList(array(object, "associatedInvariants")),
                    stringList(array(object, "createdByUseCases")),
                    stringList(array(object, "modifiedByUseCases")),
                    stringList(array(object, "queriedByUseCases")),
                    stringOrDefault(object, "modelingRisk", "")
            ));
        }
        return entities;
    }

    private List<LogicalBusinessAttributeCandidate> readAttributes(List<Object> values) {
        List<LogicalBusinessAttributeCandidate> attributes = new ArrayList<>();
        for (Object value : values) {
            Map<String, Object> object = asObject(value, "logicalBusinessAttributeCandidate");
            attributes.add(new LogicalBusinessAttributeCandidate(
                    string(object, "id"),
                    string(object, "entityId"),
                    stringOrDefault(object, "name", string(object, "id")),
                    stringOrDefault(object, "reason", "Atributo derivado del levantamiento."),
                    stringOrDefault(object, "tentativeType", ""),
                    boolOrDefault(object, "calculated", false),
                    stringOrDefault(object, "formula", ""),
                    stringOrDefault(object, "riskIfWrong", ""),
                    stringList(array(object, "sourceReferences")),
                    stringList(array(object, "relatedRules")),
                    stringList(array(object, "relatedInvariants"))
            ));
        }
        return attributes;
    }

    private List<LogicalBusinessRelationshipCandidate> readRelationships(List<Object> values) {
        List<LogicalBusinessRelationshipCandidate> relationships = new ArrayList<>();
        for (Object value : values) {
            Map<String, Object> object = asObject(value, "logicalBusinessRelationshipCandidate");
            relationships.add(new LogicalBusinessRelationshipCandidate(
                    string(object, "id"),
                    string(object, "sourceEntityId"),
                    string(object, "targetEntityId"),
                    stringOrDefault(object, "name", string(object, "id")),
                    stringOrDefault(object, "cardinalityHint", ""),
                    stringOrDefault(object, "justification", "Relación derivada del levantamiento."),
                    stringList(array(object, "sourceReferences"))
            ));
        }
        return relationships;
    }

    private List<LogicalBusinessPendingQuestion> readPendingQuestions(List<Object> values) {
        List<LogicalBusinessPendingQuestion> questions = new ArrayList<>();
        for (Object value : values) {
            Map<String, Object> object = asObject(value, "logicalBusinessPendingQuestion");
            questions.add(new LogicalBusinessPendingQuestion(
                    string(object, "id"),
                    stringOrDefault(object, "question", string(object, "id")),
                    stringOrDefault(object, "affects", ""),
                    enumValue(LogicalBusinessQuestionPriority.class,
                            stringOrDefault(object, "priority", "MEDIUM"),
                            LogicalBusinessQuestionPriority.MEDIUM),
                    enumValue(LogicalBusinessItemStatus.class,
                            stringOrDefault(object, "status", "DRAFT"),
                            LogicalBusinessItemStatus.DRAFT)
            ));
        }
        return questions;
    }

    private LogicalBusinessMaturity readMaturity(Map<String, Object> object) {
        return new LogicalBusinessMaturity(
                enumValue(LogicalBusinessMaturityLevel.class,
                        stringOrDefault(object, "level", "INITIAL"),
                        LogicalBusinessMaturityLevel.INITIAL),
                stringList(array(object, "strengths")),
                stringList(array(object, "blockers")),
                stringList(array(object, "nextSteps"))
        );
    }

    private Map<String, Object> asOptionalObject(Object value) {
        if (value == null) {
            return Map.of();
        }
        return asObject(value, "logicalBusinessMaturity");
    }

    private LocalDate readDate(String value) {
        try {
            return LocalDate.parse(value);
        } catch (RuntimeException exception) {
            return LocalDate.now();
        }
    }

    private List<String> stringList(List<Object> values) {
        List<String> result = new ArrayList<>();
        for (Object value : values) {
            String normalized = String.valueOf(value).strip();
            if (!normalized.isBlank()) result.add(normalized);
        }
        return result;
    }

    private List<Object> array(Map<String, Object> parent, String key) {
        Object value = parent.get(key);
        if (value == null) return List.of();
        if (value instanceof List<?> list) return new ArrayList<>(list);
        throw new IllegalArgumentException("Se esperaba arreglo en " + key);
    }

    private Map<String, Object> asObject(Object value, String context) {
        if (value instanceof Map<?, ?> map) {
            LinkedHashMap<String, Object> result = new LinkedHashMap<>();
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                result.put(String.valueOf(entry.getKey()), entry.getValue());
            }
            return result;
        }
        throw new IllegalArgumentException("Se esperaba objeto JSON en " + context);
    }

    private String string(Map<String, Object> object, String key) {
        Object value = object.get(key);
        if (value == null) throw new IllegalArgumentException("Campo obligatorio ausente: " + key);
        return String.valueOf(value);
    }

    private String stringOrDefault(Map<String, Object> object, String key, String defaultValue) {
        Object value = object.get(key);
        return value == null ? defaultValue : String.valueOf(value);
    }

    private boolean boolOrDefault(Map<String, Object> object, String key, boolean defaultValue) {
        Object value = object.get(key);
        return value == null ? defaultValue : Boolean.parseBoolean(String.valueOf(value));
    }

    private <E extends Enum<E>> E enumValue(Class<E> enumType, String rawValue, E fallback) {
        if (rawValue == null || rawValue.isBlank()) return fallback;
        try {
            return Enum.valueOf(enumType, rawValue.trim().toUpperCase(java.util.Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            return fallback;
        }
    }
}
