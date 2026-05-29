package com.marcosmoreira.domainmodelstudio.domain.screenflow;

/** Transición de navegación entre dos pantallas. */
public record ScreenTransition(
        String id,
        String sourceScreenId,
        String targetScreenId,
        ScreenTransitionKind kind,
        String trigger,
        String condition,
        String notes
) {
    public ScreenTransition {
        id = required(id, "id");
        sourceScreenId = required(sourceScreenId, "pantalla origen");
        targetScreenId = required(targetScreenId, "pantalla destino");
        kind = kind == null ? ScreenTransitionKind.NAVIGATES : kind;
        trigger = normalize(trigger);
        condition = normalize(condition);
        notes = normalize(notes);
        if (sourceScreenId.equals(targetScreenId)) {
            throw new IllegalArgumentException("La transición no puede apuntar a la misma pantalla.");
        }
    }

    public ScreenTransition withDetails(
            String sourceScreenId,
            String targetScreenId,
            ScreenTransitionKind kind,
            String trigger,
            String condition,
            String notes
    ) {
        return new ScreenTransition(id, sourceScreenId, targetScreenId, kind, trigger, condition, notes);
    }

    private static String required(String value, String label) {
        String normalized = normalize(value);
        if (normalized.isBlank()) {
            throw new IllegalArgumentException("El campo " + label + " de la transición no puede estar vacío.");
        }
        return normalized;
    }

    private static String normalize(String value) {
        return value == null ? "" : value.strip();
    }
}
