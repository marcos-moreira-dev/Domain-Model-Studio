package com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItem;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItemKind;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** Agrupación operativa de elementos lógicos para el SideDock del Levantamiento lógico. */
enum LogicalBusinessElementFamily {
    FLOW(
            "Flujo operativo",
            "Macroflujos, flujos, casos de uso y acciones que transforman estados del negocio.",
            List.of(LogicalBusinessItemKind.MACRO_FLOW, LogicalBusinessItemKind.FLOW,
                    LogicalBusinessItemKind.USE_CASE, LogicalBusinessItemKind.ACTION),
            "logical-business-family-flow"
    ),
    RULES(
            "Reglas y condiciones",
            "Reglas, precondiciones, invariantes y postcondiciones que gobiernan el expediente.",
            List.of(LogicalBusinessItemKind.RULE, LogicalBusinessItemKind.PRECONDITION,
                    LogicalBusinessItemKind.INVARIANT, LogicalBusinessItemKind.POSTCONDITION),
            "logical-business-family-rules"
    ),
    LANGUAGE(
            "Lenguaje, actores y evidencia",
            "Actores, conceptos, evidencias y supuestos que fijan el vocabulario lógico compartido.",
            List.of(LogicalBusinessItemKind.ACTOR, LogicalBusinessItemKind.CONCEPT,
                    LogicalBusinessItemKind.EVIDENCE, LogicalBusinessItemKind.SUPPORTING_ASSUMPTION),
            "logical-business-family-language"
    ),
    STATES_OUTPUTS(
            "Estados, reportes y cálculos",
            "Estados observables, reportes y cálculos internos que apoyan decisiones del negocio.",
            List.of(LogicalBusinessItemKind.STATE, LogicalBusinessItemKind.REPORT,
                    LogicalBusinessItemKind.CALCULATION),
            "logical-business-family-states"
    ),
    CANDIDATES(
            "Candidatos estructurales",
            "Entidades, atributos y relaciones candidatos; su detalle vive en Entidades y relaciones.",
            List.of(LogicalBusinessItemKind.ENTITY, LogicalBusinessItemKind.ATTRIBUTE,
                    LogicalBusinessItemKind.RELATIONSHIP),
            "logical-business-family-candidates"
    ),
    RISKS_PENDING(
            "Riesgos y preguntas pendientes",
            "Riesgos y preguntas que condicionan el cierre lógico o una decisión pendiente.",
            List.of(LogicalBusinessItemKind.RISK, LogicalBusinessItemKind.PENDING_QUESTION),
            "logical-business-family-risks"
    );

    private final String title;
    private final String description;
    private final List<LogicalBusinessItemKind> kinds;
    private final String styleClass;

    LogicalBusinessElementFamily(
            String title,
            String description,
            List<LogicalBusinessItemKind> kinds,
            String styleClass
    ) {
        this.title = title;
        this.description = description;
        this.kinds = List.copyOf(kinds);
        this.styleClass = styleClass;
    }

    String title() {
        return title;
    }

    String description() {
        return description;
    }

    String styleClass() {
        return styleClass;
    }

    String prefixesLabel() {
        return kinds.stream()
                .map(LogicalBusinessItemKind::prefix)
                .distinct()
                .reduce((left, right) -> left + ", " + right)
                .orElse("—");
    }

    boolean includes(LogicalBusinessItemKind kind) {
        return kinds.contains(kind);
    }

    boolean acceptsPendingQuestions() {
        return includes(LogicalBusinessItemKind.PENDING_QUESTION);
    }

    static List<LogicalBusinessElementFamily> ordered() {
        return Arrays.asList(values());
    }

    static Optional<LogicalBusinessElementFamily> forKind(LogicalBusinessItemKind kind) {
        return ordered().stream().filter(family -> family.includes(kind)).findFirst();
    }

    static Map<LogicalBusinessElementFamily, List<LogicalBusinessItem>> groupItems(List<LogicalBusinessItem> items) {
        EnumMap<LogicalBusinessElementFamily, List<LogicalBusinessItem>> grouped = new EnumMap<>(LogicalBusinessElementFamily.class);
        for (LogicalBusinessElementFamily family : ordered()) {
            grouped.put(family, new ArrayList<>());
        }
        for (LogicalBusinessItem item : items == null ? List.<LogicalBusinessItem>of() : items) {
            if (item.kind() == LogicalBusinessItemKind.PENDING_QUESTION) {
                continue;
            }
            forKind(item.kind()).ifPresent(family -> grouped.get(family).add(item));
        }
        EnumMap<LogicalBusinessElementFamily, List<LogicalBusinessItem>> snapshot = new EnumMap<>(LogicalBusinessElementFamily.class);
        grouped.forEach((family, familyItems) -> snapshot.put(family, List.copyOf(familyItems)));
        return Map.copyOf(snapshot);
    }
}
