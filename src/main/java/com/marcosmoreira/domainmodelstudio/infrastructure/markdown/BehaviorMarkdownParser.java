package com.marcosmoreira.domainmodelstudio.infrastructure.markdown;

import com.marcosmoreira.domainmodelstudio.application.importmodel.MarkdownModelParser;
import com.marcosmoreira.domainmodelstudio.application.importmodel.MarkdownModelParsingException;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramKind;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorEdge;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorEdgeKind;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNode;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNodeKind;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.diagram.ProjectType;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/** Importa Markdown oficial para BPMN básico, flujo operativo y UML de comportamiento. */
public final class BehaviorMarkdownParser implements MarkdownModelParser {

    @Override
    public DiagramProject parse(Path markdownFile) throws IOException, MarkdownModelParsingException {
        Objects.requireNonNull(markdownFile, "markdownFile");
        return parse(Files.readString(markdownFile, StandardCharsets.UTF_8), markdownFile.toString());
    }

    @Override
    public DiagramProject parse(String markdownContent, String sourceName) throws MarkdownModelParsingException {
        Objects.requireNonNull(markdownContent, "markdownContent");
        MarkdownImportDocument importDocument = MarkdownImportDocument.parse(markdownContent);
        MarkdownFrontMatter frontMatter = importDocument.frontMatter();
        BehaviorDiagramKind kind = kindFromFrontMatter(frontMatter);
        String title = frontMatter.valueOrDefault("name", kind.displayName() + " importado");
        ParsedBehavior parsed = parseBody(importDocument.body(), kind);
        if (parsed.nodes.isEmpty()) {
            throw new MarkdownModelParsingException(kind.displayName() + " no contiene elementos reconocibles.");
        }
        BehaviorDiagramDocument document;
        try {
            document = new BehaviorDiagramDocument(
                    title,
                    frontMatter.valueOrDefault("version", "borrador"),
                    LocalDate.now(),
                    kind,
                    new ArrayList<>(parsed.nodes.values()),
                    parsed.edges,
                    String.join("\n", parsed.notes));
        } catch (IllegalArgumentException exception) {
            throw new MarkdownModelParsingException("No se pudo construir el diagrama de comportamiento: " + exception.getMessage(), exception);
        }
        return MarkdownTextUtils.withSourceMarkdownPath(
                DiagramProject.blank(stableProjectId(title, kind), title, ProjectType.CONCEPTUAL_MODEL, kind.diagramTypeId())
                        .withBehaviorDiagram(document),
                sourceName);
    }

    private static ParsedBehavior parseBody(String body, BehaviorDiagramKind kind) throws MarkdownModelParsingException {
        ParsedBehavior parsed = new ParsedBehavior();
        String section = "";
        int order = 0;
        int edgeCounter = 1;
        for (String rawLine : body.split("\\R")) {
            String line = rawLine.strip();
            if (line.isBlank()) {
                continue;
            }
            if (line.startsWith(">")) {
                parsed.notes.add(line.substring(1).strip());
                continue;
            }
            if (line.startsWith("#")) {
                section = sectionName(line);
                continue;
            }
            if (isProperty(line, "Nombre")) {
                parsed.notes.add(line);
                continue;
            }
            if (!line.startsWith("- ") && !looksLikeNumberedLine(line)) {
                continue;
            }
            String item = stripListPrefix(line);
            if (isNotesSection(section)) {
                parsed.notes.add(item);
                continue;
            }
            if (isEdgeSection(section, kind) || item.contains("->")) {
                parsed.edges.add(parseEdge(item, parsed.nodes, kind, edgeCounter++));
                continue;
            }
            BehaviorNode node = parseNode(item, nodeKindFor(section, item, kind), order++);
            putNode(parsed.nodes, node);
        }
        addImplicitEdgesIfNeeded(parsed, kind, edgeCounter);
        return parsed;
    }

    private static BehaviorNode parseNode(String text, BehaviorNodeKind kind, int order) {
        String name = text;
        String description = "";
        int colon = text.indexOf(':');
        if (colon >= 0) {
            String prefix = text.substring(0, colon).strip();
            String right = text.substring(colon + 1).strip();
            if (isNodeKindAlias(prefix)) {
                name = right;
            } else {
                name = prefix;
                description = right;
            }
        }
        if (name.isBlank()) {
            name = kind.displayName();
        }
        String explicitId = explicitValue(text, "id");
        String nodeId = explicitId.isBlank() ? stableNodeId(name, order) : MarkdownTextUtils.toStableId(explicitId);
        return new BehaviorNode(
                nodeId,
                kind,
                displayName(name),
                ownerFromText(text),
                description,
                "",
                order);
    }

    private static BehaviorEdge parseEdge(
            String text,
            Map<String, BehaviorNode> nodes,
            BehaviorDiagramKind kind,
            int counter
    ) throws MarkdownModelParsingException {
        int arrow = text.indexOf("->");
        if (arrow < 0) {
            throw new MarkdownModelParsingException("Relación sin flecha '->': " + text);
        }
        String sourceText = stripLeadingNumber(text.substring(0, arrow).strip());
        String right = text.substring(arrow + 2).strip();
        int colon = right.indexOf(':');
        String targetText = colon >= 0 ? right.substring(0, colon).strip() : right;
        String label = colon >= 0 ? right.substring(colon + 1).strip() : kind.defaultEdgeKind().displayName();
        String sourceId = resolveNodeId(sourceText, nodes);
        String targetId = resolveNodeId(targetText, nodes);
        BehaviorEdgeKind edgeKind = edgeKindFromText(label, kind);
        return new BehaviorEdge("relacion_" + counter, sourceId, targetId, edgeKind, label, "", "");
    }

    private static void addImplicitEdgesIfNeeded(ParsedBehavior parsed, BehaviorDiagramKind kind, int initialCounter) {
        if (!parsed.edges.isEmpty()) {
            return;
        }
        if (kind == BehaviorDiagramKind.UML_USE_CASE) {
            return;
        }
        List<BehaviorNode> nodes = parsed.nodes.values().stream()
                .filter(node -> node.kind() != BehaviorNodeKind.LANE)
                .toList();
        for (int index = 0; index < nodes.size() - 1; index++) {
            BehaviorNode source = nodes.get(index);
            BehaviorNode target = nodes.get(index + 1);
            parsed.edges.add(new BehaviorEdge("relacion_" + (initialCounter + index), source.id(), target.id(), kind.defaultEdgeKind(), kind.defaultEdgeKind().displayName(), "", ""));
        }
    }

    private static BehaviorNodeKind nodeKindFor(String section, String item, BehaviorDiagramKind kind) {
        String normalized = normalize(section + " " + item);
        if (kind == BehaviorDiagramKind.UML_USE_CASE) {
            if (normalized.contains("actor")) return BehaviorNodeKind.ACTOR;
            if (normalized.contains("sistema") || normalized.contains("limite") || normalized.contains("límite")) return BehaviorNodeKind.SYSTEM_BOUNDARY;
            return BehaviorNodeKind.USE_CASE;
        }
        if (kind == BehaviorDiagramKind.UML_SEQUENCE) {
            return BehaviorSequenceMarkdownClassifier.nodeKindFor(section, item);
        }
        if (kind == BehaviorDiagramKind.UML_STATE) {
            if (normalized.contains("inicial") || normalized.startsWith("inicio") || normalized.contains(" inicio")) return BehaviorNodeKind.INITIAL_STATE;
            if (normalized.contains("final") || normalized.startsWith("fin") || normalized.contains(" fin")) return BehaviorNodeKind.FINAL_STATE;
            if (normalized.contains("nota")) return BehaviorNodeKind.NOTE;
            return BehaviorNodeKind.STATE;
        }
        if (kind == BehaviorDiagramKind.UML_ACTIVITY) {
            if (normalized.contains("carril") || normalized.contains("swimlane")
                    || normalized.contains("responsable") || normalized.contains("area") || normalized.contains("área")) return BehaviorNodeKind.LANE;
            if (normalized.contains("inicio") || normalized.contains("inicial")) return BehaviorNodeKind.INITIAL_STATE;
            if (normalized.contains("fin") || normalized.contains("final")) return BehaviorNodeKind.FINAL_STATE;
            if (normalized.contains("decision") || normalized.contains("decisión") || normalized.contains("?")) return BehaviorNodeKind.DECISION;
            if (normalized.contains("bifurcacion") || normalized.contains("bifurcación")) return BehaviorNodeKind.FORK;
            if (normalized.contains("union") || normalized.contains("unión")) return BehaviorNodeKind.JOIN;
            return BehaviorNodeKind.ACTION;
        }
        if (normalized.contains("inicio")) return BehaviorNodeKind.START_EVENT;
        if (normalized.contains("fin")) return BehaviorNodeKind.END_EVENT;
        if (normalized.contains("decision") || normalized.contains("decisión") || normalized.contains("?")) return BehaviorNodeKind.DECISION;
        if (normalized.contains("carril") || normalized.contains("responsable") || normalized.contains("area") || normalized.contains("área")) return BehaviorNodeKind.LANE;
        if (normalized.contains("documento") || normalized.contains("evidencia") || normalized.contains("nota")) return BehaviorNodeKind.NOTE;
        return BehaviorNodeKind.ACTIVITY;
    }


    private static BehaviorEdgeKind edgeKindFromText(String label, BehaviorDiagramKind kind) {
        String normalized = normalize(label);
        if (normalized.contains("include")) return BehaviorEdgeKind.INCLUDE;
        if (normalized.contains("extend")) return BehaviorEdgeKind.EXTEND;
        if (normalized.contains("general")) return BehaviorEdgeKind.GENERALIZATION;
        if (normalized.contains("respuesta") || normalized.contains("retorno")) return BehaviorEdgeKind.RETURN_MESSAGE;
        if (normalized.contains("asincrono") || normalized.contains("async")) return BehaviorEdgeKind.ASYNC_MESSAGE;
        if (normalized.contains("mensaje") || kind == BehaviorDiagramKind.UML_SEQUENCE) return BehaviorEdgeKind.MESSAGE;
        if (normalized.contains("transicion") || normalized.contains("transición") || kind == BehaviorDiagramKind.UML_STATE) return BehaviorEdgeKind.TRANSITION;
        if (normalized.contains("asociacion") || normalized.contains("asociación") || kind == BehaviorDiagramKind.UML_USE_CASE) return BehaviorEdgeKind.ASSOCIATION;
        return kind.defaultEdgeKind();
    }

    private static String resolveNodeId(String rawName, Map<String, BehaviorNode> nodes) throws MarkdownModelParsingException {
        String normalized = MarkdownTextUtils.toStableId(stripLeadingNumber(rawName));
        for (BehaviorNode node : nodes.values()) {
            if (node.id().equals(normalized) || MarkdownTextUtils.toStableId(node.displayName()).equals(normalized)) {
                return node.id();
            }
        }
        throw new MarkdownModelParsingException("Relación apunta a un elemento inexistente: " + rawName);
    }

    private static void putNode(Map<String, BehaviorNode> nodes, BehaviorNode node) {
        String id = node.id();
        if (!nodes.containsKey(id)) {
            nodes.put(id, node);
            return;
        }
        int suffix = 2;
        while (nodes.containsKey(id + "_" + suffix)) {
            suffix++;
        }
        nodes.put(id + "_" + suffix, new BehaviorNode(id + "_" + suffix, node.kind(), node.displayName(), node.owner(), node.description(), node.notes(), node.orderIndex()));
    }

    private static BehaviorDiagramKind kindFromFrontMatter(MarkdownFrontMatter frontMatter) throws MarkdownModelParsingException {
        String diagramType = frontMatter.valueOrDefault("diagram_type", DiagramTypeId.BPMN_BASIC.value());
        DiagramTypeId id = DiagramTypeId.of(diagramType.strip().toLowerCase(Locale.ROOT).replace('_', '-'));
        for (BehaviorDiagramKind kind : BehaviorDiagramKind.values()) {
            if (kind.diagramTypeId().equals(id)) {
                return kind;
            }
        }
        throw new MarkdownModelParsingException("El Markdown no corresponde a un diagrama de comportamiento: " + diagramType);
    }


    private static boolean isNotesSection(String section) {
        String normalized = normalize(section);
        return normalized.contains("excepcion") || normalized.contains("excepción")
                || normalized.contains("riesgo") || normalized.contains("punto de atencion") || normalized.contains("puntos de atencion")
                || normalized.contains("punto de atención") || normalized.contains("nota");
    }

    private static boolean isEdgeSection(String section, BehaviorDiagramKind kind) {
        String normalized = normalize(section);
        return normalized.contains("relacion") || normalized.contains("relación") || normalized.contains("mensaje") || normalized.contains("transicion") || normalized.contains("transición");
    }

    private static boolean looksLikeNumberedLine(String line) {
        return line.matches("\\d+\\..+");
    }

    private static String stripListPrefix(String line) {
        String stripped = line.strip();
        if (stripped.startsWith("- ")) {
            return stripped.substring(2).strip();
        }
        return stripLeadingNumber(stripped);
    }

    private static String stripLeadingNumber(String text) {
        return text == null ? "" : text.replaceFirst("^\\d+\\.\\s*", "").strip();
    }

    private static boolean isProperty(String line, String key) {
        return line.toLowerCase(Locale.ROOT).startsWith(key.toLowerCase(Locale.ROOT) + ":");
    }

    private static String explicitValue(String text, String key) {
        String normalizedKey = key == null ? "" : key.strip().toLowerCase(Locale.ROOT);
        for (String segment : (text == null ? "" : text).split("[|\n]")) {
            int colon = segment.indexOf(':');
            int equals = segment.indexOf('=');
            int separator = colon < 0 ? equals : (equals < 0 ? colon : Math.min(colon, equals));
            if (separator < 0) {
                continue;
            }
            String left = segment.substring(0, separator).strip().toLowerCase(Locale.ROOT);
            if (left.equals(normalizedKey)) {
                return segment.substring(separator + 1).strip();
            }
        }
        return "";
    }

    private static boolean isNodeKindAlias(String text) {
        String normalized = normalize(text);
        return normalized.equals("inicio") || normalized.equals("fin") || normalized.equals("actividad")
                || normalized.equals("accion") || normalized.equals("acción") || normalized.equals("decision")
                || normalized.equals("decisión") || normalized.equals("carril") || normalized.equals("swimlane")
                || normalized.equals("responsable") || normalized.equals("estado") || normalized.equals("actor")
                || normalized.equals("caso") || normalized.equals("caso de uso") || normalized.equals("participante")
                || normalized.equals("activacion") || normalized.equals("activación") || normalized.equals("fragmento")
                || normalized.equals("alt") || normalized.equals("opt") || normalized.equals("loop")
                || normalized.equals("par") || normalized.equals("break") || normalized.equals("critical")
                || normalized.equals("critico") || normalized.equals("crítico") || normalized.equals("ref");
    }

    private static String ownerFromText(String text) {
        int at = text.indexOf('@');
        if (at < 0) return "";
        return text.substring(at + 1).strip();
    }

    private static String sectionName(String line) {
        return normalize(line.replaceFirst("^#+", "").strip());
    }

    private static String stableNodeId(String name, int order) {
        String id = MarkdownTextUtils.toStableId(name);
        return id.isBlank() || id.equals("elemento") ? "elemento_" + (order + 1) : id;
    }

    private static String stableProjectId(String title, BehaviorDiagramKind kind) {
        return MarkdownTextUtils.toStableId(kind.diagramTypeId().value() + "_" + title);
    }

    private static String displayName(String text) {
        String cleaned = stripLeadingNumber(text == null ? "" : text.strip());
        if (cleaned.isBlank()) return "Elemento";
        return cleaned;
    }

    private static String normalize(String text) {
        return java.text.Normalizer.normalize(text == null ? "" : text, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .strip();
    }

    private static final class ParsedBehavior {
        private final Map<String, BehaviorNode> nodes = new LinkedHashMap<>();
        private final List<BehaviorEdge> edges = new ArrayList<>();
        private final List<String> notes = new ArrayList<>();
    }
}
