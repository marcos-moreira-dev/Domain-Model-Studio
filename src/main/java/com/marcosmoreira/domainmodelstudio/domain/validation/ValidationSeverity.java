package com.marcosmoreira.domainmodelstudio.domain.validation;

/**
 * Severidad de un hallazgo de validación.
 *
 * <p>Un ERROR impide continuar con render, persistencia o exportación confiable. Una
 * WARNING señala una decisión sospechosa o incompleta, pero no necesariamente bloquea
 * el flujo de trabajo.</p>
 */
public enum ValidationSeverity {
    ERROR,
    WARNING
}
