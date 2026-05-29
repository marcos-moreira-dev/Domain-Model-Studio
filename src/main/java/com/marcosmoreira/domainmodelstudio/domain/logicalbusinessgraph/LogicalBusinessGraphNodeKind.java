package com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph;

import java.util.Arrays;
import java.util.Optional;

/**
 * Tipos semánticos permitidos dentro del grafo lógico del negocio.
 *
 * <p>El enum es parte del contrato Markdown y de la leyenda visible: cambiar un
 * prefijo requiere migración documental, parser, exportador y guía académica.</p>
 */
public enum LogicalBusinessGraphNodeKind {
    MACRO_FLOW("MF", "Macroflujo", "Agrupa una gran operación o área lógica del negocio."),
    FLOW("FL", "Flujo o microflujo", "Describe una variante operativa concreta dentro de un macroflujo."),
    USE_CASE("CU", "Caso de uso", "Representa una interacción funcional reutilizable entre actor y sistema."),
    ACTION("ACC", "Acción transformadora", "Transforma el estado del negocio y protege reglas lógicas."),
    BUSINESS_RULE("RN", "Regla de negocio", "Declara una condición, restricción o política del dominio."),
    PRECONDITION("PRE", "Precondición", "Debe cumplirse antes de ejecutar una acción, flujo o caso de uso."),
    INVARIANT("INV", "Invariante", "Debe mantenerse verdadera durante la operación del negocio."),
    POSTCONDITION("POST", "Postcondición", "Debe quedar verdadera al cerrar una acción, flujo o caso de uso."),
    ENTITY("ENT", "Entidad candidata", "Concepto persistible derivado de reglas, acciones o evidencia."),
    STATE("EST", "Estado", "Situación válida o relevante del negocio durante su ciclo de vida."),
    REPORT("REP", "Reporte", "Vista o salida informativa derivada de datos y reglas."),
    RISK("RISK", "Riesgo", "Amenaza lógica, operativa o de calidad detectada en el levantamiento."),
    PENDING_QUESTION("PEND", "Pregunta pendiente", "Duda que debe resolverse antes de validar o derivar diseño.");

    private final String prefix;
    private final String displayName;
    private final String description;

    LogicalBusinessGraphNodeKind(String prefix, String displayName, String description) {
        this.prefix = prefix;
        this.displayName = displayName;
        this.description = description;
    }

    public String prefix() {
        return prefix;
    }

    public String displayName() {
        return displayName;
    }

    public String description() {
        return description;
    }

    /**
     * Verifica si un código respeta el prefijo obligatorio de este tipo.
     */
    public boolean matchesCode(String code) {
        return LogicalBusinessGraphText.normalize(code).toUpperCase().startsWith(prefix + "-");
    }

    /**
     * Devuelve una línea breve para la leyenda visible del grafo.
     */
    public String legendEntry() {
        return prefix + " — " + displayName;
    }

    /**
     * Resuelve el tipo desde un código completo como {@code ACC-001}.
     */
    public static Optional<LogicalBusinessGraphNodeKind> fromCode(String code) {
        String normalized = LogicalBusinessGraphText.normalize(code).toUpperCase();
        return Arrays.stream(values())
                .filter(kind -> normalized.startsWith(kind.prefix + "-"))
                .findFirst();
    }

    /**
     * Resuelve el tipo desde su abreviación, por ejemplo {@code RN}.
     */
    public static Optional<LogicalBusinessGraphNodeKind> fromPrefix(String prefix) {
        String normalized = LogicalBusinessGraphText.normalize(prefix).toUpperCase();
        return Arrays.stream(values())
                .filter(kind -> kind.prefix.equals(normalized))
                .findFirst();
    }
}
