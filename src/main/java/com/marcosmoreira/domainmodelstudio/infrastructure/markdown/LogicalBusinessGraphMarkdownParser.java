package com.marcosmoreira.domainmodelstudio.infrastructure.markdown;

import com.marcosmoreira.domainmodelstudio.application.importmodel.MarkdownModelParser;
import com.marcosmoreira.domainmodelstudio.application.importmodel.MarkdownModelParsingException;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphDocument;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphEdge;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphNode;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphNodeKind;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphNodeStatus;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphRelationKind;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * Importa Markdown oficial del Grafo lógico del negocio hacia su dominio especializado.
 *
 * <p>El contrato esperado es tabular: front matter, leyenda visible, nodos y relaciones.
 * El parser no crea un grafo libre; construye un {@code LogicalBusinessGraphDocument}
 * con nodos tipados y relaciones semánticas. Por eso las referencias inexistentes o los
 * códigos incompatibles deben convertirse en fallas de importación, no en arreglos
 * silenciosos.</p>
 
 *
 * <p><strong>Ejemplo pedagógico:</strong> una fila Markdown como
 * {@code | CU-001 | CU | Registrar venta | ... | validado | RN-001 |} se convierte
 * en un nodo tipado. La columna {@code Tipo} no es decoración: debe coincidir con el
 * prefijo del código para proteger la semántica del grafo.</p>
 */
public final class LogicalBusinessGraphMarkdownParser implements MarkdownModelParser {

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
        ParsedLogicalBusinessGraph parsed = parseBody(importDocument.body());
        if (parsed.nodes().isEmpty()) {
            throw new MarkdownModelParsingException("El Grafo lógico del negocio no contiene nodos reconocibles.");
        }
        String title = frontMatter.valueOrDefault("name", "Grafo lógico del negocio importado");
        LogicalBusinessGraphDocument document;
        try {
            document = new LogicalBusinessGraphDocument(
                    title,
                    frontMatter.valueOrDefault("version", "v0.1"),
                    parseDate(frontMatter.valueOrDefault("document_date", "")),
                    parsed.nodes(),
                    parsed.edges(),
                    parsed.notes());
        } catch (IllegalArgumentException exception) {
            throw new MarkdownModelParsingException("No se pudo construir el Grafo lógico del negocio: "
                    + exception.getMessage(), exception);
        }
        return MarkdownTextUtils.withSourceMarkdownPath(
                DiagramProject.blank(stableProjectId(title), title, DiagramTypeId.LOGICAL_BUSINESS_GRAPH)
                        .withLogicalBusinessGraphDocument(document),
                sourceName);
    }

    private static ParsedLogicalBusinessGraph parseBody(String body) throws MarkdownModelParsingException {
        List<LogicalBusinessGraphNode> nodes = new ArrayList<>();
        List<LogicalBusinessGraphEdge> edges = new ArrayList<>();
        StringBuilder notes = new StringBuilder();
        Section section = Section.NONE;
        List<String> nodeHeader = List.of();
        List<String> edgeHeader = List.of();
        int edgeCounter = 1;

        for (String rawLine : body.split("\\R")) {
            String line = rawLine.strip();
            if (line.isBlank()) {
                if (section == Section.NOTES && notes.length() > 0) {
                    notes.append('\n');
                }
                continue;
            }
            if (line.startsWith(">")) {
                continue;
            }
            if (line.startsWith("# ")) {
                section = detectSection(line);
                continue;
            }
            if (line.startsWith("|")) {
                if (isSeparatorRow(line)) {
                    continue;
                }
                List<String> cells = splitTableRow(line);
                if (section == Section.NODES) {
                    if (nodeHeader.isEmpty()) {
                        nodeHeader = normalizeHeader(cells);
                    } else {
                        nodes.add(parseNode(cells, nodeHeader));
                    }
                } else if (section == Section.EDGES) {
                    if (edgeHeader.isEmpty()) {
                        edgeHeader = normalizeHeader(cells);
                    } else {
                        edges.add(parseEdge(cells, edgeHeader, edgeCounter++));
                    }
                }
                continue;
            }
            if (section == Section.NOTES) {
                if (notes.length() > 0) {
                    notes.append('\n');
                }
                notes.append(line);
            }
        }
        return new ParsedLogicalBusinessGraph(List.copyOf(nodes), List.copyOf(edges), notes.toString().strip());
    }

    private static LogicalBusinessGraphNode parseNode(List<String> cells, List<String> header)
            throws MarkdownModelParsingException {
        String code = value(cells, header, "codigo", "code").toUpperCase(Locale.ROOT);
        if (code.isBlank()) {
            throw new MarkdownModelParsingException("Fila de nodo lógico sin código.");
        }
        LogicalBusinessGraphNodeKind kind = parseNodeKind(value(cells, header, "tipo", "kind", "type"), code);
        String title = value(cells, header, "titulo", "title", "nombre", "name");
        if (title.isBlank()) {
            throw new MarkdownModelParsingException("El nodo " + code + " no tiene título.");
        }
        String description = value(cells, header, "descripcion", "description", "detalle", "detail");
        LogicalBusinessGraphNodeStatus status = parseStatus(value(cells, header, "estado", "status"));
        List<String> references = splitList(value(cells, header, "referencias", "references", "fuentes", "source"));
        try {
            return new LogicalBusinessGraphNode(code, kind, title, description, status, references);
        } catch (IllegalArgumentException exception) {
            throw new MarkdownModelParsingException("Nodo lógico inválido " + code + ": " + exception.getMessage(), exception);
        }
    }

    private static LogicalBusinessGraphEdge parseEdge(List<String> cells, List<String> header, int counter)
            throws MarkdownModelParsingException {
        String source = value(cells, header, "origen", "source").toUpperCase(Locale.ROOT);
        String target = value(cells, header, "destino", "target").toUpperCase(Locale.ROOT);
        String relationText = value(cells, header, "relacion", "relation", "tipo", "kind");
        if (source.isBlank() || target.isBlank() || relationText.isBlank()) {
            throw new MarkdownModelParsingException("Fila de relación lógica incompleta: origen, relación y destino son obligatorios.");
        }
        LogicalBusinessGraphRelationKind relationKind = LogicalBusinessGraphRelationKind.fromCode(relationText)
                .orElseThrow(() -> new MarkdownModelParsingException("Relación lógica no reconocida: " + relationText));
        String id = value(cells, header, "id", "codigo", "code");
        if (id.isBlank()) {
            id = "rel-" + String.format(Locale.ROOT, "%03d", counter) + "-"
                    + MarkdownTextUtils.toStableId(source + "-" + relationKind.code() + "-" + target);
        }
        String description = value(cells, header, "descripcion", "description", "detalle", "detail");
        return new LogicalBusinessGraphEdge(id, source, relationKind, target, description);
    }

    private static LogicalBusinessGraphNodeKind parseNodeKind(String rawKind, String code)
            throws MarkdownModelParsingException {
        if (rawKind != null && !rawKind.isBlank()) {
            String normalized = normalizeKey(rawKind);
            for (LogicalBusinessGraphNodeKind kind : LogicalBusinessGraphNodeKind.values()) {
                if (normalizeKey(kind.prefix()).equals(normalized)
                        || normalizeKey(kind.displayName()).equals(normalized)) {
                    return kind;
                }
            }
        }
        return LogicalBusinessGraphNodeKind.fromCode(code)
                .orElseThrow(() -> new MarkdownModelParsingException("No se pudo inferir el tipo lógico desde el código: " + code));
    }

    private static LogicalBusinessGraphNodeStatus parseStatus(String rawStatus) {
        String normalized = normalizeKey(rawStatus);
        return switch (normalized) {
            case "en revision", "en revisión", "review", "in review" -> LogicalBusinessGraphNodeStatus.IN_REVIEW;
            case "validado parcialmente", "parcial", "partially validated" -> LogicalBusinessGraphNodeStatus.PARTIALLY_VALIDATED;
            case "validado", "validated" -> LogicalBusinessGraphNodeStatus.VALIDATED;
            case "bloqueado", "blocked" -> LogicalBusinessGraphNodeStatus.BLOCKED;
            case "descartado", "discarded" -> LogicalBusinessGraphNodeStatus.DISCARDED;
            default -> LogicalBusinessGraphNodeStatus.DRAFT;
        };
    }

    private static Section detectSection(String heading) {
        String normalized = normalizeKey(heading.replaceFirst("^#+\\s+", ""));
        if (normalized.contains("nodo")) {
            return Section.NODES;
        }
        if (normalized.contains("relacion") || normalized.contains("relación") || normalized.contains("arista")) {
            return Section.EDGES;
        }
        if (normalized.contains("observacion") || normalized.contains("observación") || normalized.contains("nota")) {
            return Section.NOTES;
        }
        return Section.NONE;
    }


    private static LocalDate parseDate(String rawDate) {
        if (rawDate == null || rawDate.isBlank()) {
            return LocalDate.now();
        }
        try {
            return LocalDate.parse(rawDate.strip());
        } catch (DateTimeParseException exception) {
            return LocalDate.now();
        }
    }

    private static List<String> splitTableRow(String row) {
        String trimmed = row.strip();
        if (trimmed.startsWith("|")) {
            trimmed = trimmed.substring(1);
        }
        if (trimmed.endsWith("|")) {
            trimmed = trimmed.substring(0, trimmed.length() - 1);
        }
        List<String> cells = new ArrayList<>();
        for (String cell : trimmed.split("\\|", -1)) {
            cells.add(cell.replace("\\|", "|").strip());
        }
        return cells;
    }

    private static List<String> normalizeHeader(List<String> cells) {
        return cells.stream().map(LogicalBusinessGraphMarkdownParser::normalizeKey).toList();
    }

    private static String value(List<String> cells, List<String> header, String... keys) {
        for (String key : keys) {
            int index = header.indexOf(normalizeKey(key));
            if (index >= 0 && index < cells.size()) {
                return cells.get(index).strip();
            }
        }
        return "";
    }

    private static boolean isSeparatorRow(String line) {
        String normalized = line.replace("|", "").replace(":", "").replace("-", "").strip();
        return normalized.isBlank();
    }

    private static List<String> splitList(String raw) {
        if (raw == null || raw.isBlank()) {
            return List.of();
        }
        return java.util.Arrays.stream(raw.split("[,;]"))
                .map(String::strip)
                .filter(value -> !value.isBlank())
                .toList();
    }

    private static String normalizeKey(String value) {
        String text = value == null ? "" : value.strip().toLowerCase(Locale.ROOT);
        return java.text.Normalizer.normalize(text, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replace('_', ' ')
                .replace('-', ' ')
                .replaceAll("\\s+", " ")
                .strip();
    }

    private static String stableProjectId(String title) {
        return "logical_business_graph_" + MarkdownTextUtils.toStableId(title);
    }

    private enum Section {
        NONE,
        NODES,
        EDGES,
        NOTES
    }

    private record ParsedLogicalBusinessGraph(
            List<LogicalBusinessGraphNode> nodes,
            List<LogicalBusinessGraphEdge> edges,
            String notes
    ) {
    }
}
