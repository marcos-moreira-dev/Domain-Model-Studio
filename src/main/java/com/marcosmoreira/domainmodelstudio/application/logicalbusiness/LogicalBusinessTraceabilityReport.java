package com.marcosmoreira.domainmodelstudio.application.logicalbusiness;

import java.util.List;

/** Vista filtrada de trazas internas para un elemento o para el documento completo. */
public record LogicalBusinessTraceabilityReport(
        String focusId,
        List<LogicalBusinessTraceLink> outgoing,
        List<LogicalBusinessTraceLink> incoming,
        List<String> unresolvedReferences
) {
    public LogicalBusinessTraceabilityReport {
        focusId = focusId == null ? "" : focusId.strip();
        outgoing = List.copyOf(outgoing == null ? List.of() : outgoing);
        incoming = List.copyOf(incoming == null ? List.of() : incoming);
        unresolvedReferences = List.copyOf(unresolvedReferences == null ? List.of() : unresolvedReferences);
    }

    public boolean empty() {
        return outgoing.isEmpty() && incoming.isEmpty() && unresolvedReferences.isEmpty();
    }
}
