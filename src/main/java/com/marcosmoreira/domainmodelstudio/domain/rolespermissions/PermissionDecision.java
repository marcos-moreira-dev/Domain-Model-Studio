package com.marcosmoreira.domainmodelstudio.domain.rolespermissions;

import java.util.Locale;

/** Decisión visible de una celda en la matriz rol × permiso. */
public enum PermissionDecision {
    ALLOWED("Permitido", "✓"),
    CONDITIONAL("Condicionado", "△"),
    DENIED("Denegado", "×"),
    NOT_ASSIGNED("Sin asignar", "—"),
    NOT_APPLICABLE("No aplica", "N/A");

    private final String displayName;
    private final String matrixSymbol;

    PermissionDecision(String displayName, String matrixSymbol) {
        this.displayName = displayName;
        this.matrixSymbol = matrixSymbol;
    }

    public String displayName() {
        return displayName;
    }

    public String matrixSymbol() {
        return matrixSymbol;
    }

    public boolean allowedValue() {
        return this == ALLOWED || this == CONDITIONAL;
    }

    public static PermissionDecision fromAssignment(PermissionAssignment assignment) {
        if (assignment == null) {
            return NOT_ASSIGNED;
        }
        if (!assignment.allowed()) {
            return DENIED;
        }
        return assignment.condition().isBlank() ? ALLOWED : CONDITIONAL;
    }

    public static PermissionDecision fromText(String value) {
        String normalized = normalize(value);
        if (normalized.isBlank()) {
            return ALLOWED;
        }
        return switch (normalized) {
            case "permitido", "allowed", "si", "sí", "yes", "✓" -> ALLOWED;
            case "condicionado", "conditional", "parcial", "partial", "△" -> CONDITIONAL;
            case "denegado", "denied", "bloqueado", "blocked", "no", "×" -> DENIED;
            case "no aplica", "n/a", "na", "not applicable" -> NOT_APPLICABLE;
            default -> normalized.contains("condicion") || normalized.contains("parcial")
                    ? CONDITIONAL
                    : normalized.contains("deneg") || normalized.contains("bloque")
                    ? DENIED
                    : ALLOWED;
        };
    }

    private static String normalize(String value) {
        return value == null ? "" : value.strip().toLowerCase(Locale.ROOT);
    }
}
