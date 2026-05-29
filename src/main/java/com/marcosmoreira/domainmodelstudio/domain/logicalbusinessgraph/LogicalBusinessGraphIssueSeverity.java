package com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph;

/**
 * Severidad de los hallazgos producidos por la validación del grafo lógico.
 *
 * <p>{@code BLOCKING} indica inconsistencia que debe corregirse antes de dar
 * por válido el artefacto; {@code WARNING} exige revisión; {@code INFO}
 * comunica oportunidades de mejora.</p>
 */
public enum LogicalBusinessGraphIssueSeverity {
    INFO,
    WARNING,
    BLOCKING
}
