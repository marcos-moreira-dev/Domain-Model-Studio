package com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessDocument;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessIssueSeverity;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItemStatus;
import java.util.List;
import javafx.scene.control.TreeItem;

/** Marcadores semánticos del árbol del expediente lógico. */
final class LogicalBusinessTreeMarkers {

    static final String COMPLETE = "✓";
    static final String WARNING = "⚠";
    static final String BLOCKING = "⛔";
    static final String EMPTY = "○";
    static final String TRACE = "↔";
    static final String DERIVABLE = "◇";

    private LogicalBusinessTreeMarkers() {
    }

    static String markerForChildren(List<TreeItem<LogicalBusinessTreeNode>> children) {
        if (children.isEmpty()) {
            return EMPTY;
        }
        if (children.stream().map(TreeItem::getValue).anyMatch(node -> node.marker().equals(BLOCKING))) {
            return BLOCKING;
        }
        if (children.stream().map(TreeItem::getValue).anyMatch(node -> node.marker().equals(WARNING))) {
            return WARNING;
        }
        if (children.stream().map(TreeItem::getValue).allMatch(node -> node.marker().equals(EMPTY))) {
            return EMPTY;
        }
        if (children.stream().map(TreeItem::getValue).anyMatch(node -> node.marker().equals(TRACE))) {
            return TRACE;
        }
        return COMPLETE;
    }

    static String markerForTargets(List<String> targetIds) {
        return targetIds == null || targetIds.isEmpty() ? EMPTY : COMPLETE;
    }

    static String markerForDocument(LogicalBusinessDocument document, LogicalBusinessIssueIndex issueIndex) {
        if (document == null) {
            return EMPTY;
        }
        if (issueIndex.hasBlocking()) {
            return BLOCKING;
        }
        if (issueIndex.hasWarnings()) {
            return WARNING;
        }
        return document.items().isEmpty() && document.entityCandidates().isEmpty() ? WARNING : COMPLETE;
    }

    static String markerForMaturity(LogicalBusinessDocument document) {
        return switch (document.maturity().level()) {
            case SOURCE_READY, DERIVABLE, VALIDATED -> COMPLETE;
            case INITIAL, PARTIAL, CONSISTENT -> WARNING;
        };
    }

    static String markerForStatus(LogicalBusinessItemStatus status, LogicalBusinessIssueSeverity severity) {
        if (severity != null) {
            return markerForIssue(severity);
        }
        return switch (status) {
            case EMPTY -> EMPTY;
            case BLOCKING -> BLOCKING;
            case DRAFT, PARTIAL, WITH_DOUBTS -> WARNING;
            case COMPLETE, VALIDATED, DERIVABLE -> COMPLETE;
            case DISCARDED -> EMPTY;
        };
    }

    static String markerForIssue(LogicalBusinessIssueSeverity severity) {
        if (severity == null) {
            return COMPLETE;
        }
        return switch (severity) {
            case BLOCKING -> BLOCKING;
            case WARNING -> WARNING;
            case INFO -> TRACE;
        };
    }

    static String markerStyleClass(String marker) {
        return switch (marker) {
            case COMPLETE -> "logical-business-marker-complete";
            case WARNING -> "logical-business-marker-warning";
            case BLOCKING -> "logical-business-marker-blocking";
            case TRACE -> "logical-business-marker-trace";
            case DERIVABLE -> "logical-business-marker-derivable";
            default -> "logical-business-marker-empty";
        };
    }

    static String markerGlyph(String marker) {
        return switch (marker) {
            case COMPLETE -> "●";
            case WARNING -> "▲";
            case BLOCKING -> "■";
            case TRACE -> "↔";
            case DERIVABLE -> "◆";
            default -> "○";
        };
    }
}
