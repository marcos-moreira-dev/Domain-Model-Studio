package com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItem;

/** Textos de presentación para elementos lógicos del workspace editable. */
final class LogicalBusinessItemPresentation {

    private LogicalBusinessItemPresentation() {
    }

    static String kindReading(LogicalBusinessItem item) {
        return switch (item.kind()) {
            case ACTION -> "Acción transformadora: describe un cambio de estado del negocio y sus cierres verificables.";
            case RULE -> "Regla de negocio: criterio que limita, permite o interpreta una operación.";
            case PRECONDITION -> "Precondición: condición necesaria antes de ejecutar una acción o flujo.";
            case INVARIANT -> "Invariante: verdad que no debe romperse durante el proceso.";
            case POSTCONDITION -> "Postcondición: evidencia o estado esperado al cerrar la operación.";
            case USE_CASE -> "Caso de uso: interacción observable entre un actor y el negocio.";
            case MACRO_FLOW, FLOW -> "Flujo operativo: secuencia de trabajo que agrupa acciones y decisiones.";
            case REPORT -> "Reporte o consulta: salida de información útil para decisión y control.";
            case CALCULATION -> "Cálculo: operación, fórmula o lectura cuantitativa que debe tener datos, reglas y riesgo claros.";
            case RISK -> "Riesgo: punto que requiere validación humana antes de cerrar o reutilizar el levantamiento.";
            case SUPPORTING_ASSUMPTION -> "Supuesto: hipótesis de trabajo que debe validarse antes de convertirla en regla o diseño.";
            case ACTOR -> "Actor: rol, persona o área que interviene en el negocio.";
            case STATE -> "Estado del negocio: situación observable antes o después de una acción.";
            case CONCEPT -> "Concepto del negocio: palabra o idea que debe conservar significado estable.";
            case EVIDENCE -> "Evidencia: fuente que justifica una afirmación del expediente.";
            case ENTITY -> "Entidad candidata: concepto del negocio detectado o declarado como candidato lógico.";
            case ATTRIBUTE -> "Atributo candidato: dato que debe recordarse, validarse, calcularse, auditarse o reportarse.";
            case RELATIONSHIP -> "Relación candidata: vínculo lógico entre entidades que debe justificarse.";
            case PENDING_QUESTION -> "Pregunta pendiente detectada dentro del texto importado.";
        };
    }

    static String fieldTitleForContent(LogicalBusinessItem item) {
        return switch (item.kind()) {
            case RULE, PRECONDITION, INVARIANT, POSTCONDITION -> "Forma lógica / contenido formal";
            case ACTION -> "Transformación descrita";
            case USE_CASE, MACRO_FLOW, FLOW -> "Secuencia o contenido operativo";
            case CALCULATION -> "Fórmula, datos y lectura";
            case SUPPORTING_ASSUMPTION -> "Hipótesis y validación pendiente";
            default -> "Contenido lógico";
        };
    }
}
