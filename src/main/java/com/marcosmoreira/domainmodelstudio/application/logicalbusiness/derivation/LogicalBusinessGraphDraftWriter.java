package com.marcosmoreira.domainmodelstudio.application.logicalbusiness.derivation;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessEntityCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItem;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItemStatus;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessPendingQuestion;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphNodeKind;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphNodeStatus;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphRelationKind;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphNodeKind.*;
import static com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphRelationKind.*;

/** Prepara un grafo lógico tipado compatible desde el levantamiento lógico del negocio. */
/**
 * Escribe el borrador Markdown del Grafo lógico del negocio.
 *
 * <p>Su responsabilidad es proyectar el levantamiento lógico hacia nodos y relaciones
 * semánticas, conservando un backbone mínimo MF → FL → CU → ACC cuando la fuente aún no trae
 * toda la estructura. El resultado debe ser revisable e importable, no una certificación final.</p>
 */
final class LogicalBusinessGraphDraftWriter implements LogicalBusinessDerivationWriter {

    @Override
    public LogicalBusinessDerivationTarget target() {
        return LogicalBusinessDerivationTarget.LOGICAL_BUSINESS_GRAPH;
    }

    @Override
    public LogicalBusinessDerivationDraft write(LogicalBusinessDerivationContext context) {
        GraphDraft draft = new GraphDraft(context);
        draft.collectNodes();
        draft.connectStructuralBackbone();
        draft.connectSemanticReferences();
        return new LogicalBusinessDerivationDraft(target(), target().displayName(), target().fileName(),
                draft.toMarkdown(), context.warnings());
    }

    private static final class GraphDraft {
        private final LogicalBusinessDerivationContext context;
        private final Map<String, NodeRow> nodes = new LinkedHashMap<>();
        private final List<EdgeRow> edges = new ArrayList<>();
        private final Set<String> edgeKeys = new LinkedHashSet<>();
        private int edgeCounter = 1;

        private GraphDraft(LogicalBusinessDerivationContext context) {
            this.context = context;
        }

        private void collectNodes() {
            addItems(context.macroFlows(), MACRO_FLOW);
            addItems(context.flows(), FLOW);
            addItems(context.useCases(), USE_CASE);
            addItems(context.actions(), ACTION);
            addItems(context.rules(), BUSINESS_RULE);
            addItems(context.preconditions(), PRECONDITION);
            addItems(context.invariants(), INVARIANT);
            addItems(context.postconditions(), POSTCONDITION);
            addEntityCandidates(context.entities());
            addItems(context.states(), STATE);
            addItems(context.reports(), REPORT);
            addItems(context.risks(), RISK);
            addItems(context.pendingQuestionItems(), PENDING_QUESTION);
            addPendingQuestions(context.pendingQuestions());
            ensureBackbone();
        }

        private void connectStructuralBackbone() {
            List<String> macros = codes(MACRO_FLOW);
            List<String> flows = codes(FLOW);
            List<String> useCases = codes(USE_CASE);
            List<String> actions = codes(ACTION);
            connectReferencedPairs(macros, flows, CONTAINS, "El macroflujo agrupa el flujo o microflujo.");
            for (String flow : flows) {
                if (incoming(flow, CONTAINS).isEmpty()) {
                    addEdge(macros.get(0), CONTAINS, flow, "Asignación estructural sugerida para revisar.");
                }
            }
            connectReferencedPairs(flows, useCases, USES, "El flujo usa el caso de uso.");
            for (String useCase : useCases) {
                if (incoming(useCase, USES).isEmpty()) {
                    addEdge(flows.get(0), USES, useCase, "Caso de uso asociado al flujo principal; revisar si pertenece a otro microflujo.");
                }
            }
            connectReferencedPairs(useCases, actions, EXECUTES, "El caso de uso ejecuta la acción transformadora.");
            if (useCases.size() == 1) {
                for (String action : actions) {
                    if (incoming(action, EXECUTES).isEmpty()) {
                        addEdge(useCases.get(0), EXECUTES, action, "Acción asociada al caso de uso principal; revisar granularidad.");
                    }
                }
            }
        }

        private void connectSemanticReferences() {
            connectRuleApplications();
            connectConditions();
            connectEntityReferences();
            connectReportsAndStates();
            connectBlockers();
        }

        private void connectRuleApplications() {
            for (String rule : codes(BUSINESS_RULE)) {
                for (String reference : referencesOf(rule)) {
                    addEdge(rule, APPLIES, reference, "Regla aplicada por referencia lógica del levantamiento.");
                }
            }
            for (String target : concatCodes(FLOW, USE_CASE, ACTION, ENTITY, REPORT)) {
                for (String reference : referencesOf(target)) {
                    if (kindOf(reference) == BUSINESS_RULE) {
                        addEdge(reference, APPLIES, target, "Regla aplicada al elemento que la referencia.");
                    }
                }
            }
        }

        private void connectConditions() {
            for (String source : concatCodes(FLOW, USE_CASE, ACTION)) {
                for (String reference : referencesOf(source)) {
                    addEdge(source, REQUIRES, reference, "Precondición requerida por referencia lógica.");
                    addEdge(source, PROTECTS, reference, "Invariante protegida por referencia lógica.");
                    addEdge(source, GUARANTEES, reference, "Postcondición garantizada por referencia lógica.");
                    addEdge(source, CONSULTS, reference, "Entidad o estado consultado por referencia lógica.");
                }
            }
            for (String condition : concatCodes(PRECONDITION, INVARIANT, POSTCONDITION)) {
                for (String reference : referencesOf(condition)) {
                    LogicalBusinessGraphNodeKind kind = kindOf(reference);
                    if (kind == FLOW || kind == USE_CASE || kind == ACTION) {
                        addEdge(reference, relationForCondition(condition), condition,
                                "Relación inferida desde la referencia inversa de la condición lógica.");
                    }
                }
            }
        }

        private LogicalBusinessGraphRelationKind relationForCondition(String conditionCode) {
            return switch (kindOf(conditionCode)) {
                case PRECONDITION -> REQUIRES;
                case INVARIANT -> PROTECTS;
                case POSTCONDITION -> GUARANTEES;
                default -> DEPENDS_ON;
            };
        }

        private void connectEntityReferences() {
            for (LogicalBusinessEntityCandidate entity : context.entities()) {
                String entityCode = normalizeCode(entity.id());
                entity.createdByUseCases().forEach(source -> addEdge(source, CREATES, entityCode,
                        "La entidad candidata declara que este caso de uso la crea."));
                entity.modifiedByUseCases().forEach(source -> addEdge(source, MODIFIES, entityCode,
                        "La entidad candidata declara que este caso de uso la modifica."));
                entity.queriedByUseCases().forEach(source -> addEdge(source, CONSULTS, entityCode,
                        "La entidad candidata declara que este caso de uso la consulta."));
                entity.associatedRules().forEach(rule -> addEdge(rule, APPLIES, entityCode,
                        "Entidad asociada a regla de negocio."));
                entity.associatedInvariants().forEach(invariant -> addEdge(invariant, DERIVES_IN, entityCode,
                        "Entidad sustentada por una invariante del negocio."));
                entity.sourceReferences().forEach(source -> addEdge(source, DERIVES_IN, entityCode,
                        "Entidad candidata sustentada porsde fuente lógica del levantamiento."));
            }
            for (String source : concatCodes(USE_CASE, ACTION, BUSINESS_RULE, INVARIANT, POSTCONDITION)) {
                for (String reference : referencesOf(source)) {
                    addEdge(source, DERIVES_IN, reference, "Artefacto lógico compatible por referencia del levantamiento.");
                }
            }
        }

        private void connectReportsAndStates() {
            for (String source : concatCodes(FLOW, USE_CASE, ACTION)) {
                for (String reference : referencesOf(source)) {
                    addEdge(source, GENERATES, reference, "Reporte o estado generado por referencia lógica.");
                }
            }
            for (String report : codes(REPORT)) {
                for (String reference : referencesOf(report)) {
                    addEdge(reference, FEEDS, report, "El elemento referenciado alimenta el reporte.");
                }
            }
        }

        private void connectBlockers() {
            for (String blocker : concatCodes(PENDING_QUESTION, RISK)) {
                for (String reference : referencesOf(blocker)) {
                    addEdge(blocker, BLOCKS, reference, "Duda o riesgo pendiente bloquea validación del elemento.");
                }
            }
        }

        private void addItems(List<LogicalBusinessItem> items, LogicalBusinessGraphNodeKind kind) {
            for (LogicalBusinessItem item : items) {
                addNode(item.id(), kind, item.title(), LogicalBusinessDraftText.itemSummary(item),
                        statusOf(item.status()), item.referenceIds());
            }
        }

        private void addEntityCandidates(List<LogicalBusinessEntityCandidate> entities) {
            for (LogicalBusinessEntityCandidate entity : entities) {
                List<String> references = new ArrayList<>();
                references.addAll(entity.sourceReferences());
                references.addAll(entity.associatedRules());
                references.addAll(entity.associatedInvariants());
                references.addAll(entity.createdByUseCases());
                references.addAll(entity.modifiedByUseCases());
                references.addAll(entity.queriedByUseCases());
                addNode(entity.id(), ENTITY, entity.name(), entity.logicalJustification(),
                        statusOf(entity.status()), references);
            }
        }

        private void addPendingQuestions(List<LogicalBusinessPendingQuestion> questions) {
            for (LogicalBusinessPendingQuestion question : questions) {
                addNode(question.id(), PENDING_QUESTION, question.question(), question.affects(),
                        statusOf(question.status()), extractCodes(question.affects()));
            }
        }

        private void ensureBackbone() {
            if (codes(MACRO_FLOW).isEmpty()) {
                addNode("MF-001", MACRO_FLOW, "Macroflujo principal por revisar",
                        "Agrupa los flujos detectados en el levantamiento lógico.",
                        LogicalBusinessGraphNodeStatus.DRAFT, List.of());
            }
            if (codes(FLOW).isEmpty()) {
                addNode("FL-001", FLOW, "Microflujo principal por revisar",
                        "Flujo operativo inicial sugerido para organizar casos de uso.",
                        LogicalBusinessGraphNodeStatus.DRAFT, List.of());
            }
            if (codes(USE_CASE).isEmpty()) {
                addNode("CU-001", USE_CASE, "Caso de uso principal por revisar",
                        "Completar o validar casos de uso desde acciones y entrevistas.",
                        LogicalBusinessGraphNodeStatus.DRAFT, List.of());
            }
        }

        private void addNode(String code, LogicalBusinessGraphNodeKind kind, String title, String description,
                             LogicalBusinessGraphNodeStatus status, List<String> references) {
            String normalizedCode = normalizeCode(code);
            if (normalizedCode.isBlank() || !kind.matchesCode(normalizedCode)) {
                return;
            }
            nodes.putIfAbsent(normalizedCode, new NodeRow(normalizedCode, kind,
                    safe(title, kind.displayName()), safe(description, kind.description()), status,
                    normalizedReferences(references)));
        }

        private void connectReferencedPairs(List<String> sources, List<String> targets,
                                            LogicalBusinessGraphRelationKind relation, String description) {
            for (String source : sources) {
                for (String reference : referencesOf(source)) {
                    if (targets.contains(reference)) {
                        addEdge(source, relation, reference, description);
                    }
                }
            }
            for (String target : targets) {
                for (String reference : referencesOf(target)) {
                    if (sources.contains(reference)) {
                        addEdge(reference, relation, target, description);
                    }
                }
            }
        }

        private void addEdge(String sourceCode, LogicalBusinessGraphRelationKind relation, String targetCode,
                             String description) {
            String source = normalizeCode(sourceCode);
            String target = normalizeCode(targetCode);
            NodeRow sourceNode = nodes.get(source);
            NodeRow targetNode = nodes.get(target);
            if (sourceNode == null || targetNode == null || source.equals(target)) {
                return;
            }
            if (!relation.canConnect(sourceNode.kind(), targetNode.kind())) {
                return;
            }
            String key = source + "|" + relation.code() + "|" + target;
            if (!edgeKeys.add(key)) {
                return;
            }
            String id = "rel-" + String.format(Locale.ROOT, "%03d", edgeCounter++);
            edges.add(new EdgeRow(id, source, relation, target, description));
        }

        private List<String> referencesOf(String code) {
            NodeRow row = nodes.get(normalizeCode(code));
            return row == null ? List.of() : row.references();
        }

        private List<String> codes(LogicalBusinessGraphNodeKind kind) {
            return nodes.values().stream().filter(row -> row.kind() == kind).map(NodeRow::code).toList();
        }

        private List<String> incoming(String target, LogicalBusinessGraphRelationKind relation) {
            return edges.stream()
                    .filter(edge -> edge.target().equals(normalizeCode(target)))
                    .filter(edge -> edge.relation() == relation)
                    .map(EdgeRow::source)
                    .toList();
        }

        private LogicalBusinessGraphNodeKind kindOf(String code) {
            NodeRow row = nodes.get(normalizeCode(code));
            return row == null ? null : row.kind();
        }

        private List<String> concatCodes(LogicalBusinessGraphNodeKind... kinds) {
            List<String> result = new ArrayList<>();
            for (LogicalBusinessGraphNodeKind kind : kinds) {
                result.addAll(codes(kind));
            }
            return result;
        }

        private String toMarkdown() {
            StringBuilder markdown = new StringBuilder(8192);
            markdown.append(LogicalBusinessDraftText.yamlHeader(LogicalBusinessDerivationTarget.LOGICAL_BUSINESS_GRAPH,
                    "Grafo lógico compatible — " + context.projectName(), context.domainName(),
                    "grafo lógico editable"));
            writeLegend(markdown);
            writeNodes(markdown);
            writeEdges(markdown);
            markdown.append("\n# Observaciones\n\n")
                    .append("Borrador compatible preparado desde el levantamiento lógico. ")
                    .append("Revisar macroflujos, microflujos, casos de uso y relaciones antes de usarlo como documentación validada.\n");
            return markdown.toString();
        }

        private void writeLegend(StringBuilder markdown) {
            markdown.append("# Leyenda de abreviaciones\n\n")
                    .append("| Código | Significado | Descripción |\n")
                    .append("|---|---|---|\n");
            for (LogicalBusinessGraphNodeKind kind : LogicalBusinessGraphNodeKind.values()) {
                markdown.append("| ").append(kind.prefix()).append(" | ")
                        .append(kind.displayName()).append(" | ")
                        .append(clean(kind.description())).append(" |\n");
            }
            markdown.append('\n');
        }

        private void writeNodes(StringBuilder markdown) {
            markdown.append("# Nodos\n\n")
                    .append("| Código | Tipo | Título | Descripción | Estado | Referencias |\n")
                    .append("|---|---|---|---|---|---|\n");
            nodes.values().forEach(node -> markdown.append("| ").append(node.code()).append(" | ")
                    .append(node.kind().prefix()).append(" | ").append(clean(node.title())).append(" | ")
                    .append(clean(node.description())).append(" | ").append(node.status().displayName()).append(" | ")
                    .append(clean(String.join(", ", node.references()))).append(" |\n"));
            markdown.append('\n');
        }

        private void writeEdges(StringBuilder markdown) {
            markdown.append("# Relaciones\n\n")
                    .append("| ID | Origen | Relación | Destino | Descripción |\n")
                    .append("|---|---|---|---|---|\n");
            edges.forEach(edge -> markdown.append("| ").append(edge.id()).append(" | ")
                    .append(edge.source()).append(" | ").append(edge.relation().code()).append(" | ")
                    .append(edge.target()).append(" | ").append(clean(edge.description())).append(" |\n"));
        }
    }

    private record NodeRow(String code, LogicalBusinessGraphNodeKind kind, String title, String description,
                           LogicalBusinessGraphNodeStatus status, List<String> references) {
    }

    private record EdgeRow(String id, String source, LogicalBusinessGraphRelationKind relation, String target,
                           String description) {
    }

    private static LogicalBusinessGraphNodeStatus statusOf(LogicalBusinessItemStatus status) {
        if (status == null) {
            return LogicalBusinessGraphNodeStatus.DRAFT;
        }
        return switch (status) {
            case VALIDATED -> LogicalBusinessGraphNodeStatus.VALIDATED;
            case COMPLETE, DERIVABLE -> LogicalBusinessGraphNodeStatus.PARTIALLY_VALIDATED;
            case PARTIAL, WITH_DOUBTS -> LogicalBusinessGraphNodeStatus.IN_REVIEW;
            case BLOCKING -> LogicalBusinessGraphNodeStatus.BLOCKED;
            case DISCARDED -> LogicalBusinessGraphNodeStatus.DISCARDED;
            case EMPTY, DRAFT -> LogicalBusinessGraphNodeStatus.DRAFT;
        };
    }

    private static List<String> normalizedReferences(List<String> references) {
        return references == null ? List.of() : references.stream()
                .flatMap(reference -> extractCodes(reference).stream())
                .distinct()
                .toList();
    }

    private static List<String> extractCodes(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }
        List<String> result = new ArrayList<>();
        java.util.regex.Matcher matcher = java.util.regex.Pattern
                .compile("\\b(MF|FL|CU|ACC|RN|PRE|INV|POST|ENT|EST|REP|RISK|PEND)-\\d+\\b",
                        java.util.regex.Pattern.CASE_INSENSITIVE)
                .matcher(text);
        while (matcher.find()) {
            result.add(matcher.group().toUpperCase(Locale.ROOT));
        }
        return result.stream().distinct().toList();
    }

    private static String normalizeCode(String code) {
        return code == null ? "" : code.strip().toUpperCase(Locale.ROOT);
    }

    private static String safe(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value.strip();
    }

    private static String clean(String value) {
        return value == null ? "" : value.replace('|', '/').replace("\n", " ").strip();
    }
}
