package com.marcosmoreira.domainmodelstudio.infrastructure.markdown.logicalbusiness;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessDocumentStatus;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItemStatus;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessMaturityLevel;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessQuestionPriority;
import java.text.Normalizer;
import java.util.Locale;

final class LogicalBusinessMarkdownStatusMapper {

    private LogicalBusinessMarkdownStatusMapper() {
    }

    static LogicalBusinessDocumentStatus documentStatus(String value) {
        String normalized = stable(value);
        if (normalized.contains("borrador") || normalized.contains("draft")) {
            return LogicalBusinessDocumentStatus.DRAFT;
        }
        if (normalized.contains("validado_parcial")) {
            return LogicalBusinessDocumentStatus.PARTIALLY_VALIDATED;
        }
        if (normalized.equals("validado") || normalized.contains("validada")) {
            return LogicalBusinessDocumentStatus.VALIDATED;
        }
        if (normalized.contains("archivado")) {
            return LogicalBusinessDocumentStatus.ARCHIVED;
        }
        return LogicalBusinessDocumentStatus.DRAFT;
    }

    static LogicalBusinessItemStatus itemStatus(String value) {
        String normalized = stable(value);
        if (normalized.isBlank()) {
            return LogicalBusinessItemStatus.DRAFT;
        }
        if (normalized.contains("validad")) {
            return LogicalBusinessItemStatus.VALIDATED;
        }
        if (normalized.contains("bloque")) {
            return LogicalBusinessItemStatus.BLOCKING;
        }
        if (normalized.contains("duda")) {
            return LogicalBusinessItemStatus.WITH_DOUBTS;
        }
        if (normalized.contains("derivable")) {
            return LogicalBusinessItemStatus.DERIVABLE;
        }
        if (normalized.contains("descart")) {
            return LogicalBusinessItemStatus.DISCARDED;
        }
        if (normalized.contains("complet")) {
            return LogicalBusinessItemStatus.COMPLETE;
        }
        if (normalized.contains("parcial") || normalized.contains("inicial")) {
            return LogicalBusinessItemStatus.PARTIAL;
        }
        if (normalized.contains("vacio")) {
            return LogicalBusinessItemStatus.EMPTY;
        }
        return LogicalBusinessItemStatus.DRAFT;
    }

    static LogicalBusinessQuestionPriority priority(String value) {
        String normalized = stable(value);
        if (normalized.contains("critic") || normalized.contains("bloque")) {
            return LogicalBusinessQuestionPriority.CRITICAL;
        }
        if (normalized.contains("alta")) {
            return LogicalBusinessQuestionPriority.HIGH;
        }
        if (normalized.contains("baja")) {
            return LogicalBusinessQuestionPriority.LOW;
        }
        return LogicalBusinessQuestionPriority.MEDIUM;
    }

    static LogicalBusinessMaturityLevel maturityLevel(String value) {
        String normalized = stable(value);
        if (normalized.contains("valid")) {
            return LogicalBusinessMaturityLevel.VALIDATED;
        }
        if (normalized.contains("fuente") || normalized.contains("usable") || normalized.contains("lista")) {
            return LogicalBusinessMaturityLevel.SOURCE_READY;
        }
        if (normalized.contains("derivable")) {
            return LogicalBusinessMaturityLevel.DERIVABLE;
        }
        if (normalized.contains("consistent") || normalized.contains("complet")) {
            return LogicalBusinessMaturityLevel.CONSISTENT;
        }
        if (normalized.contains("parcial")) {
            return LogicalBusinessMaturityLevel.PARTIAL;
        }
        return LogicalBusinessMaturityLevel.INITIAL;
    }

    static String stable(String value) {
        return Normalizer.normalize(value == null ? "" : value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "_")
                .replaceAll("^_+|_+$", "");
    }
}
