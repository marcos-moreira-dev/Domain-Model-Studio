package com.marcosmoreira.domainmodelstudio.domain.logicalbusiness;

import java.util.List;

/**
 * Resumen de madurez documental del levantamiento lógico.
 *
 * <p>La madurez no sustituye la revisión humana: resume fortalezas, bloqueos y
 * próximos pasos para decidir si el expediente es consistente como fuente lógica
 * revisable. No aprueba el negocio real, no genera artefactos y no sincroniza otros
 * proyectos.</p>
 */
public record LogicalBusinessMaturity(
        LogicalBusinessMaturityLevel level,
        List<String> strengths,
        List<String> blockers,
        List<String> nextSteps
) {
    public LogicalBusinessMaturity {
        level = level == null ? LogicalBusinessMaturityLevel.INITIAL : level;
        strengths = LogicalBusinessText.normalizedList(strengths);
        blockers = LogicalBusinessText.normalizedList(blockers);
        nextSteps = LogicalBusinessText.normalizedList(nextSteps);
    }

    /**
     * Indica si el expediente tiene madurez suficiente para usarse como fuente revisable.
     */
    public boolean usableAsSource() {
        return level == LogicalBusinessMaturityLevel.SOURCE_READY
                || level == LogicalBusinessMaturityLevel.DERIVABLE
                || level == LogicalBusinessMaturityLevel.VALIDATED;
    }

    /**
     * Alias histórico para compatibilidad con código y Markdown antiguo.
     *
     * @deprecated usar {@link #usableAsSource()} para evitar lenguaje de derivación.
     */
    @Deprecated(forRemoval = false)
    public boolean derivable() {
        return usableAsSource();
    }

    /**
     * Estado inicial antes de encontrar reglas, acciones, invariantes y entidades.
     */
    public static LogicalBusinessMaturity initial() {
        return new LogicalBusinessMaturity(LogicalBusinessMaturityLevel.INITIAL, List.of(), List.of(), List.of());
    }
}
