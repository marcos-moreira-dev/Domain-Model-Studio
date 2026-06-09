package com.marcosmoreira.domainmodelstudio.domain.logicalbusiness;

import java.util.Arrays;
import java.util.Optional;

/** Tipo semántico de los elementos detectables dentro del levantamiento lógico. */
public enum LogicalBusinessItemKind {
    RULE("RN"),
    PRECONDITION("PRE"),
    INVARIANT("INV"),
    POSTCONDITION("POST"),
    ACTION("ACC"),
    USE_CASE("CU"),
    MACRO_FLOW("MF"),
    FLOW("FL"),
    ENTITY("ENT"),
    ATTRIBUTE("ATR"),
    RELATIONSHIP("REL"),
    REPORT("REP"),
    CALCULATION("CALC"),
    RISK("RISK"),
    SUPPORTING_ASSUMPTION("SUP"),
    PENDING_QUESTION("PEND"),
    ACTOR("ACT"),
    STATE("EST"),
    CONCEPT("CON"),
    EVIDENCE("EVID");

    private final String prefix;

    LogicalBusinessItemKind(String prefix) {
        this.prefix = prefix;
    }

    public String prefix() {
        return prefix;
    }

    public boolean matchesId(String id) {
        String normalized = LogicalBusinessText.normalize(id).toUpperCase();
        return normalized.startsWith(prefix + "-");
    }

    public static Optional<LogicalBusinessItemKind> fromId(String id) {
        String normalized = LogicalBusinessText.normalize(id).toUpperCase();
        return Arrays.stream(values())
                .filter(kind -> normalized.startsWith(kind.prefix + "-"))
                .findFirst();
    }
}
