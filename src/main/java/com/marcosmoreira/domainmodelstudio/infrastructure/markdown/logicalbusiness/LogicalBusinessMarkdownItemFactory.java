package com.marcosmoreira.domainmodelstudio.infrastructure.markdown.logicalbusiness;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItem;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItemKind;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItemStatus;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessPendingQuestion;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

final class LogicalBusinessMarkdownItemFactory {

    List<LogicalBusinessItem> items(List<ParsedLogicalBusinessItem> parsedItems) {
        Map<String, LogicalBusinessItem> items = new LinkedHashMap<>();
        for (ParsedLogicalBusinessItem parsedItem : parsedItems) {
            LogicalBusinessMarkdownIds.kindFor(parsedItem.id()).ifPresent(kind -> {
                LogicalBusinessItem item = toItem(parsedItem, kind);
                items.putIfAbsent(item.id(), item);
            });
        }
        return List.copyOf(items.values());
    }

    List<LogicalBusinessPendingQuestion> pendingQuestions(List<ParsedLogicalBusinessItem> parsedItems) {
        Map<String, LogicalBusinessPendingQuestion> questions = new LinkedHashMap<>();
        for (ParsedLogicalBusinessItem parsedItem : parsedItems) {
            if (!LogicalBusinessItemKind.PENDING_QUESTION.matchesId(parsedItem.id())) {
                continue;
            }
            LogicalBusinessPendingQuestion question = toPendingQuestion(parsedItem);
            questions.putIfAbsent(question.id(), question);
        }
        return List.copyOf(questions.values());
    }

    private LogicalBusinessItem toItem(ParsedLogicalBusinessItem parsedItem, LogicalBusinessItemKind kind) {
        LogicalBusinessItemStatus status = LogicalBusinessMarkdownStatusMapper.itemStatus(
                LogicalBusinessMarkdownFields.statusFor(parsedItem));
        return new LogicalBusinessItem(
                parsedItem.id(),
                kind,
                parsedItem.title(),
                status,
                LogicalBusinessMarkdownFields.sourceFor(parsedItem),
                LogicalBusinessMarkdownFields.descriptionFor(parsedItem),
                LogicalBusinessMarkdownFields.humanReadingFor(parsedItem),
                parsedItem.body(),
                LogicalBusinessMarkdownFields.referencesFor(parsedItem));
    }

    private LogicalBusinessPendingQuestion toPendingQuestion(ParsedLogicalBusinessItem parsedItem) {
        if (parsedItem.fromTable()) {
            return new LogicalBusinessPendingQuestion(
                    parsedItem.id(),
                    LogicalBusinessMarkdownFields.cell(parsedItem, 1).orElse(parsedItem.title()),
                    LogicalBusinessMarkdownFields.cell(parsedItem, 2).orElse(""),
                    LogicalBusinessMarkdownStatusMapper.priority(
                            LogicalBusinessMarkdownFields.cell(parsedItem, 3).orElse("media")),
                    LogicalBusinessMarkdownStatusMapper.itemStatus(
                            LogicalBusinessMarkdownFields.cell(parsedItem, 4).orElse("pendiente")));
        }
        return new LogicalBusinessPendingQuestion(
                parsedItem.id(),
                parsedItem.title(),
                LogicalBusinessMarkdownFields.field(parsedItem.body(), "afecta a", "bloquea").orElse(""),
                LogicalBusinessMarkdownStatusMapper.priority(
                        LogicalBusinessMarkdownFields.field(parsedItem.body(), "prioridad").orElse("media")),
                LogicalBusinessMarkdownStatusMapper.itemStatus(
                        LogicalBusinessMarkdownFields.field(parsedItem.body(), "estado").orElse("pendiente")));
    }
}
