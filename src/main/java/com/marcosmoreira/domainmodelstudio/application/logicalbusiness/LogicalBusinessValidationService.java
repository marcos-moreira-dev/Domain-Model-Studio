package com.marcosmoreira.domainmodelstudio.application.logicalbusiness;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessDocument;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessIssueSeverity;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessValidationIssue;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Orquesta la validación de coherencia interna del levantamiento lógico.
 *
 * <p>Combina problemas estructurales del dominio con políticas de items y entidades. No
 * corrige el documento, no aprueba el negocio real, no sincroniza proyectos y no genera
 * diagramas: produce una lista ordenada de hallazgos para que la UI, los tests o una
 * revisión asistida decidan si el expediente es consistente como fuente lógica.</p>
 */
public final class LogicalBusinessValidationService {

    private final LogicalBusinessItemValidationPolicy itemPolicy = new LogicalBusinessItemValidationPolicy();
    private final LogicalBusinessEntityValidationPolicy entityPolicy = new LogicalBusinessEntityValidationPolicy();

    /**
     * Evalúa el documento completo y devuelve hallazgos deduplicados y ordenados por severidad.
     *
     * @param document levantamiento lógico a revisar; no se modifica.
     * @return advertencias y bloqueos que describen deudas de coherencia interna.
     */
    public List<LogicalBusinessValidationIssue> validate(LogicalBusinessDocument document) {
        Objects.requireNonNull(document, "document");
        LogicalBusinessReferenceIndex index = new LogicalBusinessReferenceIndex(document);
        List<LogicalBusinessValidationIssue> issues = new ArrayList<>(document.structuralIssues());
        document.items().forEach(item -> issues.addAll(itemPolicy.validate(item, index)));
        document.entityCandidates().forEach(entity -> issues.addAll(entityPolicy.validate(entity, index)));
        warnWhenDocumentIsTooEmpty(document, issues);
        return issues.stream()
                .distinct()
                .sorted(Comparator.comparing(LogicalBusinessValidationIssue::severity).reversed()
                        .thenComparing(LogicalBusinessValidationIssue::targetId)
                        .thenComparing(LogicalBusinessValidationIssue::message))
                .toList();
    }

    public long count(List<LogicalBusinessValidationIssue> issues, LogicalBusinessIssueSeverity severity) {
        return issues.stream().filter(issue -> issue.severity() == severity).count();
    }

    private void warnWhenDocumentIsTooEmpty(
            LogicalBusinessDocument document,
            List<LogicalBusinessValidationIssue> issues
    ) {
        if (document.items().isEmpty() && document.entityCandidates().isEmpty()) {
            issues.add(new LogicalBusinessValidationIssue(LogicalBusinessIssueSeverity.WARNING, "DOCUMENTO",
                    "El levantamiento todavía no tiene elementos lógicos detectados."));
        }
    }
}
