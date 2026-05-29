package com.marcosmoreira.domainmodelstudio.domain.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Resultado acumulado de una validación.
 *
 * <p>Es inmutable desde el exterior y ofrece métodos pequeños para que application y
 * presentation puedan decidir si continuar o mostrar errores al usuario.</p>
 */
public final class ValidationResult {

    private final List<ValidationIssue> issues;

    public ValidationResult(List<ValidationIssue> issues) {
        this.issues = List.copyOf(issues == null ? List.of() : issues);
    }

    public static ValidationResult ok() {
        return new ValidationResult(List.of());
    }

    public static ValidationResult of(List<ValidationIssue> issues) {
        return new ValidationResult(issues);
    }

    public List<ValidationIssue> issues() {
        return issues;
    }

    public List<ValidationIssue> errors() {
        return issues.stream().filter(ValidationIssue::isError).toList();
    }

    public List<ValidationIssue> warnings() {
        return issues.stream().filter(ValidationIssue::isWarning).toList();
    }

    public boolean hasErrors() {
        return issues.stream().anyMatch(ValidationIssue::isError);
    }

    public boolean hasWarnings() {
        return issues.stream().anyMatch(ValidationIssue::isWarning);
    }

    public boolean isValid() {
        return !hasErrors();
    }

    public int issueCount() {
        return issues.size();
    }

    public ValidationResult plus(ValidationIssue issue) {
        Objects.requireNonNull(issue, "El hallazgo no puede ser null");
        List<ValidationIssue> updated = new ArrayList<>(issues);
        updated.add(issue);
        return new ValidationResult(updated);
    }

    public ValidationResult merge(ValidationResult other) {
        Objects.requireNonNull(other, "El resultado a fusionar no puede ser null");
        List<ValidationIssue> updated = new ArrayList<>(issues);
        updated.addAll(other.issues());
        return new ValidationResult(updated);
    }
}
