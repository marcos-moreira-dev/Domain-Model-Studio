package com.marcosmoreira.domainmodelstudio.application.logicalbusiness;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessIssueSeverity;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItem;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItemKind;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessValidationIssue;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Reglas semánticas para elementos RN/PRE/INV/POST/ACC/CU del levantamiento. */
final class LogicalBusinessItemValidationPolicy {

    List<LogicalBusinessValidationIssue> validate(LogicalBusinessItem item, LogicalBusinessReferenceIndex index) {
        List<LogicalBusinessValidationIssue> issues = new ArrayList<>();
        warnWhenDescriptionIsMissing(item, issues);
        warnWhenHumanReadingIsMissing(item, issues);
        warnWhenActionHasWeakClosure(item, issues);
        warnWhenUseCaseHasNoActor(item, issues);
        warnWhenInvariantHasWeakOperationalTrace(item, issues);
        warnWhenReferencesAreUnknown(item, index, issues);
        return List.copyOf(issues);
    }

    private void warnWhenDescriptionIsMissing(LogicalBusinessItem item, List<LogicalBusinessValidationIssue> issues) {
        if (requiresDescription(item.kind()) && item.description().isBlank()) {
            issues.add(warning(item.id(), "El elemento necesita descripción humana para ser revisable."));
        }
    }

    private void warnWhenHumanReadingIsMissing(LogicalBusinessItem item, List<LogicalBusinessValidationIssue> issues) {
        if (requiresHumanReading(item.kind()) && item.humanReading().isBlank()) {
            issues.add(warning(item.id(), "Debe conservar lectura humana; una fórmula o regla sin lectura es débil."));
        }
    }

    private void warnWhenActionHasWeakClosure(LogicalBusinessItem item, List<LogicalBusinessValidationIssue> issues) {
        if (item.kind() != LogicalBusinessItemKind.ACTION) {
            return;
        }
        if (item.referenceIds().stream().noneMatch(reference -> reference.startsWith("PRE-"))) {
            issues.add(warning(item.id(), "La acción transformadora no referencia precondiciones."));
        }
        if (item.referenceIds().stream().noneMatch(reference -> reference.startsWith("POST-"))) {
            issues.add(blocking(item.id(), "La acción transformadora no referencia postcondiciones de cierre."));
        }
        if (!containsAny(item.content(), "evidencia", "comprobante", "auditor", "registro")) {
            issues.add(warning(item.id(), "La acción debería indicar evidencia o registro verificable."));
        }
    }

    private void warnWhenUseCaseHasNoActor(LogicalBusinessItem item, List<LogicalBusinessValidationIssue> issues) {
        if (item.kind() == LogicalBusinessItemKind.USE_CASE && !containsAny(item.content(), "actor", "responsable")) {
            issues.add(warning(item.id(), "El caso de uso debería indicar actor o responsable."));
        }
    }

    private void warnWhenInvariantHasWeakOperationalTrace(LogicalBusinessItem item, List<LogicalBusinessValidationIssue> issues) {
        if (item.kind() != LogicalBusinessItemKind.INVARIANT) {
            return;
        }
        if (!containsAny(item.content(), "riesgo")) {
            issues.add(warning(item.id(), "La invariante debería indicar riesgo si se rompe."));
        }
        if (!containsAny(item.content(), "validación", "validacion", "constraint", "prueba", "auditor")) {
            issues.add(warning(item.id(), "La invariante debería indicar cómo se verificará o controlará."));
        }
    }

    private void warnWhenReferencesAreUnknown(
            LogicalBusinessItem item,
            LogicalBusinessReferenceIndex index,
            List<LogicalBusinessValidationIssue> issues
    ) {
        item.referenceIds().stream()
                .filter(reference -> !index.known(reference))
                .forEach(reference -> issues.add(warning(item.id(), "Referencia no resuelta: " + reference)));
    }

    private boolean requiresDescription(LogicalBusinessItemKind kind) {
        return switch (kind) {
            case RULE, PRECONDITION, INVARIANT, POSTCONDITION, ACTION, USE_CASE, REPORT, RISK,
                    SUPPORTING_ASSUMPTION, CALCULATION -> true;
            default -> false;
        };
    }

    private boolean requiresHumanReading(LogicalBusinessItemKind kind) {
        return switch (kind) {
            case RULE, PRECONDITION, INVARIANT, POSTCONDITION, CALCULATION -> true;
            default -> false;
        };
    }

    private boolean containsAny(String content, String... needles) {
        String normalized = content == null ? "" : content.toLowerCase(Locale.ROOT);
        for (String needle : needles) {
            if (normalized.contains(needle.toLowerCase(Locale.ROOT))) {
                return true;
            }
        }
        return false;
    }

    private LogicalBusinessValidationIssue warning(String targetId, String message) {
        return new LogicalBusinessValidationIssue(LogicalBusinessIssueSeverity.WARNING, targetId, message);
    }

    private LogicalBusinessValidationIssue blocking(String targetId, String message) {
        return LogicalBusinessValidationIssue.blocking(targetId, message);
    }
}
