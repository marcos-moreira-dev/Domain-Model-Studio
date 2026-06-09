package com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessIssueSeverity;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessValidationIssue;
import java.util.List;
import javafx.scene.layout.VBox;

/** Utilidades de los módulos inspectores del SideDock de Levantamiento lógico. */
final class LogicalBusinessInspectorSupport {

    private LogicalBusinessInspectorSupport() {
    }

    static String focusId(LogicalBusinessSelection selection) {
        return selection == null ? "" : selection.traceabilityFocusId();
    }

    static String focusTitle(LogicalBusinessSelection selection) {
        if (selection == null || selection.empty()) {
            return "Resumen global";
        }
        return switch (selection.kind()) {
            case DOCUMENT -> "Expediente completo";
            case GROUP -> "Grupo: " + selection.id();
            case SECTION -> "Sección: " + selection.id();
            case ITEM -> "Elemento: " + selection.id();
            case ENTITY -> "Entidad: " + selection.id();
            case ATTRIBUTE -> "Atributo: " + selection.id();
            case RELATIONSHIP -> "Relación: " + selection.id();
            case PENDING_QUESTION -> "Pregunta pendiente: " + selection.id();
            case MATURITY -> "Madurez del levantamiento";
            case NONE -> "Resumen global";
        };
    }

    static List<LogicalBusinessValidationIssue> issuesForFocus(
            List<LogicalBusinessValidationIssue> issues,
            LogicalBusinessSelection selection
    ) {
        String focus = focusId(selection);
        if (focus.isBlank()) {
            return List.of();
        }
        return issues.stream()
                .filter(issue -> issue.targetId().equals(focus) || issue.message().contains(focus))
                .toList();
    }

    static long count(List<LogicalBusinessValidationIssue> issues, LogicalBusinessIssueSeverity severity) {
        return issues.stream().filter(issue -> issue.severity() == severity).count();
    }

    static VBox issueCard(LogicalBusinessValidationIssue issue) {
        VBox card = LogicalBusinessUiNodes.inspectorCard(
                severityIcon(issue.severity()) + " " + LogicalBusinessStatusFormatter.severity(issue.severity()),
                issue.targetId() + " — " + issue.message(),
                severityStyle(issue.severity())
        );
        return card;
    }

    static String severityStyle(LogicalBusinessIssueSeverity severity) {
        return switch (severity) {
            case BLOCKING -> "logical-business-inspector-blocking";
            case WARNING -> "logical-business-inspector-warning";
            case INFO -> "logical-business-inspector-info";
        };
    }

    static String severityIcon(LogicalBusinessIssueSeverity severity) {
        return switch (severity) {
            case BLOCKING -> "⛔";
            case WARNING -> "⚠";
            case INFO -> "ℹ";
        };
    }

    static String emptyFocusMessage(LogicalBusinessSelection selection) {
        if (selection == null || selection.traceabilityFocusId().isBlank()) {
            return "El nodo seleccionado funciona como contenedor. Selecciona una regla, acción, entidad, atributo, relación o pregunta para inspección puntual.";
        }
        return "Este foco no tiene hallazgos registrados.";
    }
}
