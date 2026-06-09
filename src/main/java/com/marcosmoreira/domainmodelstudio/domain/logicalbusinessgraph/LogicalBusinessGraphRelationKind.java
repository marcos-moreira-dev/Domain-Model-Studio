package com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph;

import java.util.Arrays;
import java.util.Optional;

import static com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphNodeKind.*;

/**
 * Relaciones semánticas dirigidas entre nodos del grafo lógico del negocio.
 *
 * <p>Cada valor define un código persistible usado en Markdown, canvas, SVG y
 * validación. La matriz de compatibilidad evita que el grafo se convierta en
 * una red libre sin significado lógico.</p>
 
 *
 * <p><strong>Ejemplo pedagógico:</strong> {@code ACC-001 garantiza POST-001} es una
 * relación válida porque una acción puede asegurar una postcondición. En cambio,
 * {@code REP-001 garantiza POST-001} no expresa una transformación del negocio y debe
 * ser rechazado por la validación semántica.</p>
 */
public enum LogicalBusinessGraphRelationKind {
    CONTAINS("contiene", "Agrupa jerárquicamente un elemento lógico."),
    USES("usa", "Utiliza un caso, acción o elemento lógico sin redefinirlo."),
    REUSES("reutiliza", "Reaprovecha un caso de uso o acción en otro flujo."),
    EXECUTES("ejecuta", "Dispara o materializa una acción transformadora."),
    APPLIES("aplica", "Aplica una regla a un flujo, caso, acción o entidad."),
    REQUIRES("requiere", "Exige una precondición o requisito lógico previo."),
    PROTECTS("protege", "Mantiene una invariante durante la operación."),
    GUARANTEES("garantiza", "Asegura una postcondición o cierre verificable."),
    CREATES("crea", "Crea una entidad, estado, reporte o evidencia lógica."),
    MODIFIES("modifica", "Actualiza una entidad o estado del negocio."),
    CONSULTS("consulta", "Lee una entidad, estado o reporte sin transformarlo."),
    GENERATES("genera", "Produce un reporte, estado o evidencia."),
    FEEDS("alimenta", "Aporta datos o evidencia a otro elemento."),
    BLOCKS("bloquea", "Impide validar o ejecutar otro elemento mientras siga abierto."),
    ENABLES("habilita", "Permite avanzar cuando se satisface una condición lógica."),
    DEPENDS_ON("depende_de", "Declara dependencia lógica o de información."),
    DERIVES_IN("deriva_en", "Justifica la existencia de un artefacto derivado.");

    private final String code;
    private final String description;

    LogicalBusinessGraphRelationKind(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String code() {
        return code;
    }

    public String description() {
        return description;
    }

    /**
     * Evalúa si la relación es semánticamente esperada entre dos tipos de nodo.
     *
     * @param source tipo del nodo origen
     * @param target tipo del nodo destino
     * @return {@code true} si el par respeta la gramática del grafo lógico
     */
    public boolean canConnect(LogicalBusinessGraphNodeKind source, LogicalBusinessGraphNodeKind target) {
        if (source == null || target == null) {
            return false;
        }
        return switch (this) {
            case CONTAINS -> source == MACRO_FLOW && target == FLOW;
            case USES -> source == FLOW && target == USE_CASE || source == USE_CASE && target == ACTION;
            case REUSES -> source == FLOW && target == USE_CASE || source == USE_CASE && target == USE_CASE;
            case EXECUTES -> source == USE_CASE && target == ACTION || source == FLOW && target == ACTION;
            case APPLIES -> source == BUSINESS_RULE && oneOf(target, MACRO_FLOW, FLOW, USE_CASE, ACTION, ENTITY, REPORT);
            case REQUIRES -> oneOf(source, FLOW, USE_CASE, ACTION) && target == PRECONDITION;
            case PROTECTS -> oneOf(source, FLOW, USE_CASE, ACTION) && target == INVARIANT;
            case GUARANTEES -> oneOf(source, FLOW, USE_CASE, ACTION) && target == POSTCONDITION;
            case CREATES, MODIFIES -> oneOf(source, USE_CASE, ACTION) && oneOf(target, ENTITY, STATE);
            case CONSULTS -> oneOf(source, USE_CASE, ACTION) && oneOf(target, ENTITY, STATE, REPORT);
            case GENERATES -> oneOf(source, FLOW, USE_CASE, ACTION) && target == REPORT;
            case FEEDS -> oneOf(source, ENTITY, STATE, REPORT) && oneOf(target, REPORT, ACTION, USE_CASE);
            case BLOCKS -> oneOf(source, PENDING_QUESTION, RISK) && target != PENDING_QUESTION;
            case ENABLES -> oneOf(source, PRECONDITION, POSTCONDITION, BUSINESS_RULE, STATE) && oneOf(target, FLOW, USE_CASE, ACTION);
            case DEPENDS_ON -> source != target;
            case DERIVES_IN -> oneOf(source, BUSINESS_RULE, ACTION, USE_CASE, FLOW, INVARIANT, POSTCONDITION)
                    && oneOf(target, ENTITY, REPORT, STATE, USE_CASE, ACTION);
        };
    }

    /**
     * Resuelve una relación desde su código Markdown, por ejemplo {@code protege}.
     */
    public static Optional<LogicalBusinessGraphRelationKind> fromCode(String code) {
        String normalized = LogicalBusinessGraphText.normalize(code).toLowerCase();
        return Arrays.stream(values())
                .filter(kind -> kind.code.equals(normalized))
                .findFirst();
    }

    private static boolean oneOf(LogicalBusinessGraphNodeKind value, LogicalBusinessGraphNodeKind... options) {
        return Arrays.stream(options).anyMatch(option -> option == value);
    }
}
