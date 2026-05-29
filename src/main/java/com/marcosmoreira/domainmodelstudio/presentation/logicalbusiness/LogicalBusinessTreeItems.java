package com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessDocument;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessEntityCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItem;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessPendingQuestion;
import java.util.Comparator;
import java.util.List;
import javafx.scene.control.TreeItem;

import static com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness.LogicalBusinessTreeMarkers.markerForTargets;

/** Utilidades pequeñas para construir nodos del árbol sin inflar la factoría principal. */
final class LogicalBusinessTreeItems {

    private LogicalBusinessTreeItems() {
    }

    static TreeItem<LogicalBusinessTreeNode> group(
            String id,
            String label,
            String detail,
            List<String> targetIds,
            int count
    ) {
        return group(id, label, detail, targetIds, count, markerForTargets(targetIds));
    }

    static TreeItem<LogicalBusinessTreeNode> group(
            String id,
            String label,
            String detail,
            List<String> targetIds,
            int count,
            String marker
    ) {
        return expanded(LogicalBusinessTreeNode.of(label, marker, detail, LogicalBusinessSelection.group(id), count));
    }

    static TreeItem<LogicalBusinessTreeNode> expanded(LogicalBusinessTreeNode node) {
        TreeItem<LogicalBusinessTreeNode> item = new TreeItem<>(node);
        item.setExpanded(true);
        return item;
    }

    static TreeItem<LogicalBusinessTreeNode> collapsed(LogicalBusinessTreeNode node) {
        TreeItem<LogicalBusinessTreeNode> item = new TreeItem<>(node);
        item.setExpanded(false);
        return item;
    }

    static int totalNodes(LogicalBusinessDocument document) {
        if (document == null) {
            return 0;
        }
        int entityChildren = document.entityCandidates().stream()
                .mapToInt(entity -> entity.attributes().size() + entity.relationships().size())
                .sum();
        return document.items().size()
                + document.entityCandidates().size()
                + entityChildren
                + document.pendingQuestions().size();
    }

    static List<LogicalBusinessItem> sorted(List<LogicalBusinessItem> items) {
        return items.stream().sorted(Comparator.comparing(LogicalBusinessItem::id)).toList();
    }

    static List<LogicalBusinessEntityCandidate> sortedEntities(List<LogicalBusinessEntityCandidate> entities) {
        return entities.stream().sorted(Comparator.comparing(LogicalBusinessEntityCandidate::id)).toList();
    }

    static List<String> itemIds(List<LogicalBusinessItem> items) {
        return items.stream().map(LogicalBusinessItem::id).toList();
    }

    static List<String> entityIds(List<LogicalBusinessEntityCandidate> entities) {
        return entities.stream().map(LogicalBusinessEntityCandidate::id).toList();
    }

    static List<String> pendingQuestionIds(List<LogicalBusinessPendingQuestion> questions) {
        return questions.stream().map(LogicalBusinessPendingQuestion::id).toList();
    }

    static String shortText(String text) {
        if (text == null || text.isBlank()) {
            return "Pregunta pendiente";
        }
        String clean = text.strip();
        return clean.length() > 52 ? clean.substring(0, 49) + "..." : clean;
    }
}
