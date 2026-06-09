package com.marcosmoreira.domainmodelstudio.infrastructure.markdown;

import com.marcosmoreira.domainmodelstudio.application.freegraph.GenerateInitialFreeGraphLayoutUseCase;
import com.marcosmoreira.domainmodelstudio.application.importmodel.MarkdownModelParser;
import com.marcosmoreira.domainmodelstudio.application.importmodel.MarkdownModelParsingException;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphDocument;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphEdge;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphEdgeDirection;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphKind;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphNode;
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

/** Importa Markdown oficial de Grafo libre hacia un proyecto semántico editable. */
public final class FreeGraphMarkdownParser implements MarkdownModelParser {

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
        String title = unquote(frontMatter.valueOrDefault("name", "Grafo libre importado"));
        FreeGraphKind graphKind = parseGraphKind(frontMatter.valueOrDefault("graph_kind", "mixed"));
        ParsedFreeGraph parsed = parseBody(importDocument.body(), graphKind);
        if (parsed.nodes().isEmpty()) {
            throw new MarkdownModelParsingException("El Grafo libre no contiene nodos reconocibles.");
        }
        FreeGraphDocument document;
        try {
            document = new FreeGraphDocument(
                    title,
                    unquote(frontMatter.valueOrDefault("version", "borrador")),
                    LocalDate.now(),
                    graphKind,
                    parsed.nodes(),
                    parsed.edges(),
                    parsed.notes());
        } catch (IllegalArgumentException exception) {
            throw new MarkdownModelParsingException("No se pudo construir el Grafo libre: " + exception.getMessage(), exception);
        }
        DiagramProject project = MarkdownTextUtils.withSourceMarkdownPath(
                DiagramProject.blank(stableProjectId(title), title, DiagramTypeId.FREE_GRAPH).withFreeGraph(document),
                sourceName);
        return new GenerateInitialFreeGraphLayoutUseCase().ensureLayout(project);
    }

    private static ParsedFreeGraph parseBody(String body, FreeGraphKind graphKind) throws MarkdownModelParsingException {
        Map<String, FreeGraphNode> nodes = new LinkedHashMap<>();
        List<FreeGraphEdge> edges = new ArrayList<>();
        StringBuilder notes = new StringBuilder();
        boolean inNodes = false;
        boolean inEdges = false;
        boolean inNotes = false;
        PendingNode pendingNode = null;
        int orderIndex = 0;
        int edgeCounter = 1;

        for (String rawLine : body.split("\\R")) {
            String line = rawLine.strip();
            if (line.isBlank()) {
                if (inNotes && notes.length() > 0) {
                    notes.append('\n');
                }
                continue;
            }
            if (line.startsWith(">")) {
                continue;
            }
            String lower = line.toLowerCase(Locale.ROOT);
            if (line.startsWith("# ")) {
                pendingNode = flushNode(nodes, pendingNode, orderIndex);
                if (pendingNode == null) {
                    orderIndex = nodes.size();
                }
                inNodes = lower.contains("nodo");
                inEdges = lower.contains("relacion") || lower.contains("relación") || lower.contains("arista") || lower.contains("conexion") || lower.contains("conexión");
                inNotes = lower.contains("observacion") || lower.contains("observación") || lower.contains("nota");
                continue;
            }
            if (line.startsWith("## ")) {
                pendingNode = flushNode(nodes, pendingNode, orderIndex);
                orderIndex = nodes.size();
                if (!inNodes) {
                    inNodes = true;
                    inEdges = false;
                    inNotes = false;
                }
                pendingNode = new PendingNode(line.substring(3).strip(), "", "");
                continue;
            }
            if (inNodes && pendingNode != null && MarkdownTextUtils.isPropertyLine(line)) {
                String key = MarkdownTextUtils.keyBeforeColon(line);
                String value = MarkdownTextUtils.valueAfterColon(line);
                if (key.equals("id")) {
                    pendingNode = pendingNode.withId(value);
                } else if (key.equals("titulo") || key.equals("título") || key.equals("title")) {
                    pendingNode = pendingNode.withTitle(value);
                } else if (key.equals("contenido") || key.equals("content") || key.equals("descripcion") || key.equals("descripción")) {
                    pendingNode = pendingNode.withContent(value);
                }
                continue;
            }
            if (inEdges && line.startsWith("- ")) {
                pendingNode = flushNode(nodes, pendingNode, orderIndex);
                orderIndex = nodes.size();
                edges.add(parseEdge(line.substring(2).strip(), nodes, graphKind, edgeCounter++));
                continue;
            }
            if (inNotes) {
                if (notes.length() > 0) {
                    notes.append('\n');
                }
                notes.append(line);
            }
        }
        flushNode(nodes, pendingNode, orderIndex);
        return new ParsedFreeGraph(new ArrayList<>(nodes.values()), edges, notes.toString().strip());
    }

    private static PendingNode flushNode(
            Map<String, FreeGraphNode> nodes,
            PendingNode pendingNode,
            int orderIndex
    ) throws MarkdownModelParsingException {
        if (pendingNode == null) {
            return null;
        }
        String title = normalize(pendingNode.title());
        if (title.isBlank()) {
            throw new MarkdownModelParsingException("Nodo sin título en Markdown de Grafo libre.");
        }
        String id = normalize(pendingNode.id()).isBlank()
                ? MarkdownTextUtils.toStableId(title)
                : MarkdownTextUtils.toStableId(pendingNode.id());
        if (nodes.containsKey(id)) {
            throw new MarkdownModelParsingException("Nodo duplicado en Markdown de Grafo libre: " + id);
        }
        nodes.put(id, new FreeGraphNode(id, title, pendingNode.content(), orderIndex));
        return null;
    }

    private static FreeGraphEdge parseEdge(
            String text,
            Map<String, FreeGraphNode> nodes,
            FreeGraphKind graphKind,
            int counter
    ) throws MarkdownModelParsingException {
        EdgeSyntax syntax = detectSyntax(text);
        String rawSource = text.substring(0, syntax.index()).strip();
        String right = text.substring(syntax.index() + syntax.symbol().length()).strip();
        int colon = right.indexOf(':');
        String rawTarget = colon >= 0 ? right.substring(0, colon).strip() : right;
        String label = colon >= 0 ? right.substring(colon + 1).strip() : "";
        String source = resolveNodeId(rawSource, nodes);
        String target = resolveNodeId(rawTarget, nodes);
        FreeGraphEdgeDirection direction = syntax.directed() ? FreeGraphEdgeDirection.DIRECTED : FreeGraphEdgeDirection.UNDIRECTED;
        if (graphKind == FreeGraphKind.DIRECTED) {
            direction = FreeGraphEdgeDirection.DIRECTED;
        } else if (graphKind == FreeGraphKind.UNDIRECTED) {
            direction = FreeGraphEdgeDirection.UNDIRECTED;
        }
        String id = "rel_" + counter + "_" + MarkdownTextUtils.toStableId(source + "_" + target + "_" + label);
        return new FreeGraphEdge(id, source, target, direction, label, "");
    }

    private static EdgeSyntax detectSyntax(String text) throws MarkdownModelParsingException {
        int directed = text.indexOf("->");
        int undirected = text.indexOf("--");
        if (directed < 0 && undirected < 0) {
            throw new MarkdownModelParsingException("Relación sin conector '->' o '--': " + text);
        }
        if (directed >= 0 && (undirected < 0 || directed < undirected)) {
            return new EdgeSyntax("->", directed, true);
        }
        return new EdgeSyntax("--", undirected, false);
    }

    private static String resolveNodeId(String raw, Map<String, FreeGraphNode> nodes) throws MarkdownModelParsingException {
        String stable = MarkdownTextUtils.toStableId(raw);
        if (nodes.containsKey(stable)) {
            return stable;
        }
        for (FreeGraphNode node : nodes.values()) {
            if (node.title().equalsIgnoreCase(raw.strip())) {
                return node.id();
            }
        }
        throw new MarkdownModelParsingException("La relación referencia un nodo inexistente: " + raw);
    }

    private static FreeGraphKind parseGraphKind(String rawValue) throws MarkdownModelParsingException {
        String normalized = unquote(rawValue)
                .strip()
                .toLowerCase(Locale.ROOT)
                .replace('_', '-')
                .replace(' ', '-');
        return switch (normalized) {
            case "directed", "dirigido", "directed-graph", "grafo-dirigido" -> FreeGraphKind.DIRECTED;
            case "undirected", "no-dirigido", "no-directed", "grafo-no-dirigido", "sin-direccion", "sin-dirección" -> FreeGraphKind.UNDIRECTED;
            case "mixed", "mixto", "grafo-mixto", "" -> FreeGraphKind.MIXED;
            default -> throw new MarkdownModelParsingException("Tipo de grafo libre no reconocido: " + rawValue);
        };
    }


    private static String stableProjectId(String title) {
        return "grafo_libre_" + MarkdownTextUtils.toStableId(title);
    }

    private static String unquote(String value) {
        String cleaned = value == null ? "" : value.strip();
        if (cleaned.length() >= 2 && cleaned.startsWith("\"") && cleaned.endsWith("\"")) {
            return cleaned.substring(1, cleaned.length() - 1);
        }
        return cleaned;
    }

    private static String normalize(String value) {
        return value == null ? "" : value.strip();
    }

    private record PendingNode(String title, String id, String content) {
        PendingNode withTitle(String updatedTitle) {
            return new PendingNode(updatedTitle, id, content);
        }

        PendingNode withId(String updatedId) {
            return new PendingNode(title, updatedId, content);
        }

        PendingNode withContent(String updatedContent) {
            return new PendingNode(title, id, updatedContent);
        }
    }

    private record EdgeSyntax(String symbol, int index, boolean directed) {
    }

    private record ParsedFreeGraph(List<FreeGraphNode> nodes, List<FreeGraphEdge> edges, String notes) {
    }
}
