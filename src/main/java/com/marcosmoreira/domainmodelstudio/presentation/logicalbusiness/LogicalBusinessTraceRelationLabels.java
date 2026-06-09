package com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness;

/** Traduce códigos internos de relación a lenguaje visible de trazas internas. */
final class LogicalBusinessTraceRelationLabels {

    private LogicalBusinessTraceRelationLabels() {
    }

    static String labelFor(String relation) {
        return switch (relation == null ? "" : relation.strip()) {
            case "deriva_de" -> "sustentado por";
            case "regla_asociada" -> "regla asociada";
            case "invariante_asociada" -> "invariante asociada";
            case "tiene_atributo" -> "tiene atributo candidato";
            case "relaciona" -> "relaciona";
            case "origen" -> "origen candidato";
            case "destino" -> "destino candidato";
            case "crea" -> "crea";
            case "modifica" -> "modifica";
            case "consulta" -> "consulta";
            case "bloquea" -> "bloquea decisión";
            case "referencia" -> "referencia";
            default -> relation == null || relation.isBlank() ? "referencia" : relation.strip();
        };
    }
}
