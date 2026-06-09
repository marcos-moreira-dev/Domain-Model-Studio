package com.marcosmoreira.domainmodelstudio.domain.validation;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import java.util.Objects;
import java.util.Optional;

/**
 * Hallazgo individual de validación.
 *
 * <p>Guarda severidad, código estable, elemento asociado y mensaje humano. La UI puede
 * mostrar el mensaje, mientras que pruebas y automatizaciones pueden usar el código.</p>
 */
public final class ValidationIssue {

    private final ValidationSeverity severity;
    private final ValidationCode code;
    private final DiagramElementId elementId;
    private final String message;

    private ValidationIssue(
            ValidationSeverity severity,
            ValidationCode code,
            DiagramElementId elementId,
            String message
    ) {
        this.severity = Objects.requireNonNull(severity, "La severidad no puede ser null");
        this.code = Objects.requireNonNull(code, "El código de validación no puede ser null");
        this.elementId = elementId;
        this.message = requireText(message);
    }

    public static ValidationIssue error(ValidationCode code, DiagramElementId elementId, String message) {
        return new ValidationIssue(ValidationSeverity.ERROR, code, elementId, message);
    }

    public static ValidationIssue warning(ValidationCode code, DiagramElementId elementId, String message) {
        return new ValidationIssue(ValidationSeverity.WARNING, code, elementId, message);
    }

    public ValidationSeverity severity() {
        return severity;
    }

    public ValidationCode code() {
        return code;
    }

    public Optional<DiagramElementId> elementId() {
        return Optional.ofNullable(elementId);
    }

    public String message() {
        return message;
    }

    public boolean isError() {
        return severity == ValidationSeverity.ERROR;
    }

    public boolean isWarning() {
        return severity == ValidationSeverity.WARNING;
    }

    private static String requireText(String value) {
        String normalized = Objects.requireNonNull(value, "El mensaje no puede ser null").trim();
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("El mensaje no puede estar vacío");
        }
        return normalized;
    }
}
