package com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessDocumentStatus;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessIssueSeverity;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItemKind;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItemStatus;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessMaturityLevel;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessQuestionPriority;

/** Traduce enums del dominio a lenguaje visible de producto. */
final class LogicalBusinessStatusFormatter {

    private LogicalBusinessStatusFormatter() {
    }

    static String documentStatus(LogicalBusinessDocumentStatus status) {
        return switch (status) {
            case DRAFT -> "En revisión";
            case PARTIALLY_VALIDATED -> "Validado parcialmente";
            case VALIDATED -> "Validado";
            case ARCHIVED -> "Archivado";
        };
    }

    static String itemStatus(LogicalBusinessItemStatus status) {
        return switch (status) {
            case EMPTY -> "Vacío";
            case DRAFT -> "En revisión";
            case PARTIAL -> "Validación parcial";
            case COMPLETE -> "Completo";
            case VALIDATED -> "Validado";
            case WITH_DOUBTS -> "Con dudas";
            case BLOCKING -> "Bloqueante";
            case DERIVABLE -> "Usable como fuente";
            case DISCARDED -> "Descartado";
        };
    }

    static String itemKind(LogicalBusinessItemKind kind) {
        return switch (kind) {
            case RULE -> "Regla";
            case PRECONDITION -> "Precondición";
            case INVARIANT -> "Invariante";
            case POSTCONDITION -> "Postcondición";
            case ACTION -> "Acción";
            case USE_CASE -> "Caso de uso";
            case MACRO_FLOW -> "Macroflujo";
            case FLOW -> "Flujo";
            case ENTITY -> "Entidad";
            case ATTRIBUTE -> "Atributo";
            case RELATIONSHIP -> "Relación";
            case REPORT -> "Reporte";
            case CALCULATION -> "Cálculo";
            case RISK -> "Riesgo";
            case SUPPORTING_ASSUMPTION -> "Supuesto";
            case PENDING_QUESTION -> "Pregunta pendiente";
            case ACTOR -> "Actor";
            case STATE -> "Estado";
            case CONCEPT -> "Concepto";
            case EVIDENCE -> "Evidencia";
        };
    }

    static String maturity(LogicalBusinessMaturityLevel level) {
        return switch (level) {
            case INITIAL -> "Inicial";
            case PARTIAL -> "Validación parcial";
            case CONSISTENT -> "Consistente";
            case SOURCE_READY, DERIVABLE -> "Usable como fuente";
            case VALIDATED -> "Validado";
        };
    }

    static String priority(LogicalBusinessQuestionPriority priority) {
        return switch (priority) {
            case LOW -> "Baja";
            case MEDIUM -> "Media";
            case HIGH -> "Alta";
            case CRITICAL -> "Crítica";
        };
    }

    static String severity(LogicalBusinessIssueSeverity severity) {
        return switch (severity) {
            case INFO -> "Información";
            case WARNING -> "Advertencia";
            case BLOCKING -> "Bloqueante";
        };
    }
}
