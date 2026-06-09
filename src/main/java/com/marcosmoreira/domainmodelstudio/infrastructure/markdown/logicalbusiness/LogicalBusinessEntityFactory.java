package com.marcosmoreira.domainmodelstudio.infrastructure.markdown.logicalbusiness;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessAttributeCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessEntityCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItemKind;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessRelationshipCandidate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

final class LogicalBusinessEntityFactory {

    List<LogicalBusinessEntityCandidate> entities(List<ParsedLogicalBusinessItem> parsedItems) {
        Map<String, ParsedLogicalBusinessItem> entityItems = byKind(parsedItems, LogicalBusinessItemKind.ENTITY);
        Map<String, LogicalBusinessAttributeCandidate> attributes = attributes(parsedItems, entityItems);
        Map<String, LogicalBusinessRelationshipCandidate> relationships = relationships(parsedItems, entityItems);
        List<LogicalBusinessEntityCandidate> entities = new ArrayList<>();
        for (ParsedLogicalBusinessItem entityItem : entityItems.values()) {
            entities.add(toEntity(entityItem, attributesFor(entityItem.id(), attributes),
                    relationshipsFor(entityItem.id(), relationships)));
        }
        return List.copyOf(entities);
    }

    private LogicalBusinessEntityCandidate toEntity(
            ParsedLogicalBusinessItem item,
            List<LogicalBusinessAttributeCandidate> attributes,
            List<LogicalBusinessRelationshipCandidate> relationships
    ) {
        return new LogicalBusinessEntityCandidate(
                item.id(),
                item.title(),
                LogicalBusinessMarkdownStatusMapper.itemStatus(LogicalBusinessMarkdownFields.statusFor(item)),
                logicalJustification(item),
                attributes,
                relationships,
                sourceReferences(item),
                idsFromField(item, "reglas asociadas"),
                idsFromField(item, "invariantes asociadas"),
                idsFromField(item, "acciones que la crean"),
                idsFromField(item, "acciones que la modifican"),
                idsFromField(item, "acciones que la consultan"),
                LogicalBusinessMarkdownFields.field(item.body(), "riesgo si se modela mal").orElse(""));
    }

    private Map<String, LogicalBusinessAttributeCandidate> attributes(
            List<ParsedLogicalBusinessItem> parsedItems,
            Map<String, ParsedLogicalBusinessItem> entityItems
    ) {
        Map<String, LogicalBusinessAttributeCandidate> attributes = new LinkedHashMap<>();
        for (ParsedLogicalBusinessItem item : byKind(parsedItems, LogicalBusinessItemKind.ATTRIBUTE).values()) {
            entityId(item, entityItems).ifPresent(entityId -> attributes.put(item.id(), toAttribute(item, entityId)));
        }
        return attributes;
    }

    private LogicalBusinessAttributeCandidate toAttribute(ParsedLogicalBusinessItem item, String entityId) {
        boolean calculated = LogicalBusinessMarkdownStatusMapper.stable(
                LogicalBusinessMarkdownFields.field(item.body(), "¿es calculado?", "es calculado").orElse("no"))
                .contains("si");
        return new LogicalBusinessAttributeCandidate(
                item.id(),
                entityId,
                attributeName(item.title()),
                reason(item),
                LogicalBusinessMarkdownFields.field(item.body(), "tipo tentativo").orElse(""),
                calculated,
                LogicalBusinessMarkdownFields.field(item.body(), "formula o lectura de calculo",
                        "fórmula o lectura de cálculo", "formula", "fórmula").orElse(calculated ? reason(item) : ""),
                LogicalBusinessMarkdownFields.field(item.body(), "riesgo si se modela mal").orElse(""),
                sourceReferences(item),
                idsFromField(item, "reglas asociadas"),
                idsFromField(item, "invariantes asociadas"));
    }

    private Map<String, LogicalBusinessRelationshipCandidate> relationships(
            List<ParsedLogicalBusinessItem> parsedItems,
            Map<String, ParsedLogicalBusinessItem> entityItems
    ) {
        Map<String, LogicalBusinessRelationshipCandidate> relationships = new LinkedHashMap<>();
        for (ParsedLogicalBusinessItem item : byKind(parsedItems, LogicalBusinessItemKind.RELATIONSHIP).values()) {
            Optional<String> source = firstEntityId(item, "entidad origen");
            Optional<String> target = firstEntityId(item, "entidad destino");
            if (source.isPresent() && target.isPresent()
                    && entityItems.containsKey(source.get()) && entityItems.containsKey(target.get())) {
                relationships.put(item.id(), toRelationship(item, source.get(), target.get()));
            }
        }
        return relationships;
    }

    private LogicalBusinessRelationshipCandidate toRelationship(ParsedLogicalBusinessItem item, String source, String target) {
        return new LogicalBusinessRelationshipCandidate(
                item.id(),
                source,
                target,
                item.title(),
                LogicalBusinessMarkdownFields.field(item.body(), "cardinalidad tentativa").orElse(""),
                logicalJustification(item),
                sourceReferences(item));
    }

    private Map<String, ParsedLogicalBusinessItem> byKind(
            List<ParsedLogicalBusinessItem> items,
            LogicalBusinessItemKind kind
    ) {
        Map<String, ParsedLogicalBusinessItem> values = new LinkedHashMap<>();
        for (ParsedLogicalBusinessItem item : items) {
            if (kind.matchesId(item.id())) {
                values.putIfAbsent(item.id(), item);
            }
        }
        return values;
    }

    private List<LogicalBusinessAttributeCandidate> attributesFor(
            String entityId,
            Map<String, LogicalBusinessAttributeCandidate> attributes
    ) {
        return attributes.values().stream()
                .filter(attribute -> attribute.entityId().equals(entityId))
                .toList();
    }

    private List<LogicalBusinessRelationshipCandidate> relationshipsFor(
            String entityId,
            Map<String, LogicalBusinessRelationshipCandidate> relationships
    ) {
        return relationships.values().stream()
                .filter(relationship -> relationship.sourceEntityId().equals(entityId))
                .toList();
    }

    private Optional<String> entityId(
            ParsedLogicalBusinessItem item,
            Map<String, ParsedLogicalBusinessItem> entityItems
    ) {
        Optional<String> explicit = firstEntityId(item, "pertenece a");
        if (explicit.isPresent()) {
            return explicit;
        }
        String prefix = item.title().contains(".") ? item.title().substring(0, item.title().indexOf('.')).strip() : "";
        return entityItems.values().stream()
                .filter(entity -> entity.title().equalsIgnoreCase(prefix))
                .map(ParsedLogicalBusinessItem::id)
                .findFirst();
    }

    private Optional<String> firstEntityId(ParsedLogicalBusinessItem item, String fieldName) {
        return LogicalBusinessMarkdownFields.field(item.body(), fieldName)
                .flatMap(LogicalBusinessMarkdownIds::firstId);
    }

    private List<String> idsFromField(ParsedLogicalBusinessItem item, String fieldName) {
        return LogicalBusinessMarkdownFields.field(item.body(), fieldName)
                .map(LogicalBusinessMarkdownIds::idsIn)
                .orElse(List.of());
    }

    private List<String> sourceReferences(ParsedLogicalBusinessItem item) {
        List<String> fieldIds = idsFromAnyField(item,
                "fuente logica", "fuente lógica", "sustento logico", "sustento lógico",
                "justificada por", "justificado por", "derivada de", "derivado de",
                "fuente de derivacion", "fuente de derivación");
        return fieldIds.isEmpty() ? LogicalBusinessMarkdownFields.referencesFor(item) : fieldIds;
    }

    private List<String> idsFromAnyField(ParsedLogicalBusinessItem item, String... fieldNames) {
        for (String fieldName : fieldNames) {
            List<String> ids = idsFromField(item, fieldName);
            if (!ids.isEmpty()) {
                return ids;
            }
        }
        return List.of();
    }

    private String logicalJustification(ParsedLogicalBusinessItem item) {
        String description = LogicalBusinessMarkdownFields.descriptionFor(item);
        return description.isBlank() ? "Justificado por el levantamiento lógico." : description;
    }

    private String reason(ParsedLogicalBusinessItem item) {
        return LogicalBusinessMarkdownFields.field(item.body(), "razon operativa", "razón operativa")
                .orElse(logicalJustification(item));
    }

    private String attributeName(String title) {
        return title.contains(".") ? title.substring(title.indexOf('.') + 1).strip() : title.strip();
    }
}
