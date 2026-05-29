package com.marcosmoreira.domainmodelstudio.infrastructure.markdown;

import com.marcosmoreira.domainmodelstudio.application.importmodel.MarkdownModelParser;
import com.marcosmoreira.domainmodelstudio.application.importmodel.MarkdownModelParsingException;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureDiagramKind;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureEdge;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureEdgeKind;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureNode;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureNodeKind;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
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

/** Importa Markdown oficial para C4 Contexto, C4 Contenedores y Despliegue técnico. */
public final class ArchitectureMarkdownParser implements MarkdownModelParser {

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
        ArchitectureDiagramKind kind = kindFromFrontMatter(frontMatter);
        String title = frontMatter.valueOrDefault("name", kind.displayName() + " importado");
        ParsedArchitecture parsed = parseBody(importDocument.body(), kind);
        if (parsed.nodes.isEmpty()) {
            throw new MarkdownModelParsingException(kind.displayName() + " no contiene elementos reconocibles.");
        }
        ArchitectureDiagramDocument document;
        try {
            document = new ArchitectureDiagramDocument(
                    title,
                    frontMatter.valueOrDefault("version", "borrador"),
                    LocalDate.now(),
                    kind,
                    new ArrayList<>(parsed.nodes.values()),
                    parsed.edges,
                    String.join("\n", parsed.notes));
        } catch (IllegalArgumentException exception) {
            throw new MarkdownModelParsingException("No se pudo construir el diagrama de arquitectura: " + exception.getMessage(), exception);
        }
        return MarkdownTextUtils.withSourceMarkdownPath(
                DiagramProject.blank(stableProjectId(title, kind), title, kind.diagramTypeId()).withArchitectureDiagram(document),
                sourceName);
    }

    private static ParsedArchitecture parseBody(String body, ArchitectureDiagramKind kind) throws MarkdownModelParsingException {
        ParsedArchitecture parsed = new ParsedArchitecture();
        String section = "";
        int edgeCounter = 1;
        int order = 0;
        for (String rawLine : body.split("\\R")) {
            String line = rawLine.strip();
            if (line.isBlank()) {
                continue;
            }
            if (line.startsWith(">")) {
                parsed.notes.add(line.substring(1).strip());
                continue;
            }
            String lower = normalize(line);
            if (line.startsWith("#")) {
                section = sectionName(line);
                continue;
            }
            if (line.startsWith("Sistema:") || line.startsWith("Sistema principal:")) {
                String name = MarkdownTextUtils.valueAfterColon(line);
                if (!name.isBlank()) {
                    addNode(parsed, name, ArchitectureNodeKind.SOFTWARE_SYSTEM, "", "", "", "Sistema central del contexto.", order++);
                }
                continue;
            }
            if (line.startsWith("Propósito:") || line.startsWith("Proposito:")) {
                parsed.notes.add(line);
                continue;
            }
            if (line.startsWith("- ")) {
                String item = line.substring(2).strip();
                if (section.contains("relacion") || section.contains("relación") || section.contains("conexion") || section.contains("conexión")) {
                    parsed.edges.add(parseEdge(item, parsed.nodes, kind, edgeCounter++));
                } else if (section.contains("persona")) {
                    ArchitectureNode node = parseNode(item, ArchitectureNodeKind.PERSON, order++);
                    putNode(parsed.nodes, node);
                } else if (section.contains("sistema externo")) {
                    ArchitectureNode node = parseNode(item, ArchitectureNodeKind.EXTERNAL_SYSTEM, order++);
                    putNode(parsed.nodes, node);
                } else if (section.contains("contenedor")) {
                    ArchitectureNode node = parseNode(item, nodeKindForContainer(item), order++);
                    putNode(parsed.nodes, node);
                } else if (section.contains("ambiente")) {
                    ArchitectureNode node = parseNode(item, ArchitectureNodeKind.ENVIRONMENT, order++);
                    putNode(parsed.nodes, node);
                } else if (section.contains("nodo")) {
                    ArchitectureNode node = parseNode(item, nodeKindForDeployment(item), order++);
                    putNode(parsed.nodes, node);
                } else if (section.contains("contexto") && lower.startsWith("sistema")) {
                    ArchitectureNode node = parseNode(item, ArchitectureNodeKind.SOFTWARE_SYSTEM, order++);
                    putNode(parsed.nodes, node);
                } else if (kind == ArchitectureDiagramKind.C4_CONTEXT) {
                    ArchitectureNode node = parseNode(item, ArchitectureNodeKind.SOFTWARE_SYSTEM, order++);
                    putNode(parsed.nodes, node);
                } else if (kind == ArchitectureDiagramKind.C4_CONTAINERS) {
                    ArchitectureNode node = parseNode(item, nodeKindForContainer(item), order++);
                    putNode(parsed.nodes, node);
                } else {
                    ArchitectureNode node = parseNode(item, nodeKindForDeployment(item), order++);
                    putNode(parsed.nodes, node);
                }
            }
        }
        return parsed;
    }

    private static ArchitectureNode parseNode(String text, ArchitectureNodeKind kind, int order) {
        int colon = text.indexOf(':');
        String name = colon >= 0 ? text.substring(0, colon).strip() : text.strip();
        String description = colon >= 0 ? text.substring(colon + 1).strip() : "";
        String technology = technologyFromText(name + " " + description, kind);
        return new ArchitectureNode(
                MarkdownTextUtils.toStableId(name),
                kind,
                displayName(name),
                technology,
                "",
                "",
                description,
                "",
                order);
    }

    private static void addNode(ParsedArchitecture parsed, String name, ArchitectureNodeKind kind, String technology,
                                String owner, String environment, String description, int order) throws MarkdownModelParsingException {
        putNode(parsed.nodes, new ArchitectureNode(MarkdownTextUtils.toStableId(name), kind, displayName(name),
                technology, owner, environment, description, "", order));
    }

    private static ArchitectureEdge parseEdge(
            String text,
            Map<String, ArchitectureNode> nodes,
            ArchitectureDiagramKind diagramKind,
            int counter
    ) throws MarkdownModelParsingException {
        int arrow = text.indexOf("->");
        if (arrow < 0) {
            throw new MarkdownModelParsingException("Relación sin flecha '->': " + text);
        }
        String sourceText = text.substring(0, arrow).strip();
        String right = text.substring(arrow + 2).strip();
        int colon = right.indexOf(':');
        String targetText = colon >= 0 ? right.substring(0, colon).strip() : right;
        String label = colon >= 0 ? right.substring(colon + 1).strip() : diagramKind.defaultEdgeKind().displayName();
        String sourceId = resolveNodeId(sourceText, nodes);
        String targetId = resolveNodeId(targetText, nodes);
        ArchitectureEdgeKind kind = edgeKindFromText(label, diagramKind);
        return new ArchitectureEdge("relacion_" + counter, sourceId, targetId, kind, label, protocolFromText(label), "");
    }

    private static ArchitectureNodeKind nodeKindForContainer(String text) {
        String normalized = normalize(text);
        if (normalized.contains("base de datos") || normalized.contains("postgres") || normalized.contains("mysql")) {
            return ArchitectureNodeKind.DATABASE;
        }
        if (normalized.contains("api") || normalized.contains("backend")) {
            return ArchitectureNodeKind.API;
        }
        if (normalized.contains("servicio externo") || normalized.contains("correo") || normalized.contains("pasarela")) {
            return ArchitectureNodeKind.EXTERNAL_SERVICE;
        }
        if (normalized.contains("aplicacion") || normalized.contains("aplicación") || normalized.contains("desktop") || normalized.contains("web")) {
            return ArchitectureNodeKind.APPLICATION;
        }
        return ArchitectureNodeKind.CONTAINER;
    }

    private static ArchitectureNodeKind nodeKindForDeployment(String text) {
        String normalized = normalize(text);
        if (normalized.contains("ambiente") || normalized.contains("produccion") || normalized.contains("producción") || normalized.contains("piloto") || normalized.contains("desarrollo")) {
            return ArchitectureNodeKind.ENVIRONMENT;
        }
        if (normalized.contains("servidor")) {
            return ArchitectureNodeKind.SERVER;
        }
        if (normalized.contains("laptop") || normalized.contains("cliente") || normalized.contains("usuario") || normalized.contains("equipo")) {
            return ArchitectureNodeKind.CLIENT;
        }
        if (normalized.contains("base de datos") || normalized.contains("postgres") || normalized.contains("mysql")) {
            return ArchitectureNodeKind.DATABASE;
        }
        if (normalized.contains("red") || normalized.contains("network")) {
            return ArchitectureNodeKind.NETWORK;
        }
        if (normalized.contains("artefacto") || normalized.contains("jar") || normalized.contains("imagen")) {
            return ArchitectureNodeKind.ARTIFACT;
        }
        return ArchitectureNodeKind.SERVICE;
    }

    private static ArchitectureEdgeKind edgeKindFromText(String text, ArchitectureDiagramKind diagramKind) {
        String normalized = normalize(text);
        if (diagramKind == ArchitectureDiagramKind.TECHNICAL_DEPLOYMENT) {
            if (normalized.contains("aloja")) return ArchitectureEdgeKind.HOSTS;
            if (normalized.contains("despliega")) return ArchitectureEdgeKind.DEPLOYS_TO;
            return ArchitectureEdgeKind.CONNECTS_TO;
        }
        if (diagramKind == ArchitectureDiagramKind.C4_CONTAINERS) {
            if (normalized.contains("lee") || normalized.contains("escribe") || normalized.contains("persist")) return ArchitectureEdgeKind.READS_WRITES;
            if (normalized.contains("publica")) return ArchitectureEdgeKind.PUBLISHES;
            if (normalized.contains("consume")) return ArchitectureEdgeKind.SUBSCRIBES;
            return ArchitectureEdgeKind.CALLS;
        }
        if (normalized.contains("integra")) return ArchitectureEdgeKind.INTEGRATES_WITH;
        if (normalized.contains("depende")) return ArchitectureEdgeKind.DEPENDS_ON;
        return ArchitectureEdgeKind.USES;
    }

    private static String resolveNodeId(String rawName, Map<String, ArchitectureNode> nodes) throws MarkdownModelParsingException {
        String stable = MarkdownTextUtils.toStableId(rawName);
        if (nodes.containsKey(stable)) {
            return stable;
        }
        for (ArchitectureNode node : nodes.values()) {
            if (node.displayName().equalsIgnoreCase(rawName.strip())) {
                return node.id();
            }
        }
        throw new MarkdownModelParsingException("La relación referencia un elemento inexistente: " + rawName);
    }

    private static void putNode(Map<String, ArchitectureNode> nodes, ArchitectureNode node) throws MarkdownModelParsingException {
        if (nodes.containsKey(node.id())) {
            throw new MarkdownModelParsingException("Elemento duplicado en Markdown: " + node.id());
        }
        nodes.put(node.id(), node);
    }

    private static ArchitectureDiagramKind kindFromFrontMatter(MarkdownFrontMatter frontMatter) throws MarkdownModelParsingException {
        String type = frontMatter.valueOrDefault("diagram_type", DiagramTypeId.C4_CONTEXT.value())
                .toLowerCase(Locale.ROOT)
                .replace('_', '-')
                .strip();
        if (DiagramTypeId.C4_CONTEXT.value().equals(type)) return ArchitectureDiagramKind.C4_CONTEXT;
        if (DiagramTypeId.C4_CONTAINERS.value().equals(type)) return ArchitectureDiagramKind.C4_CONTAINERS;
        if (DiagramTypeId.TECHNICAL_DEPLOYMENT.value().equals(type)) return ArchitectureDiagramKind.TECHNICAL_DEPLOYMENT;
        throw new MarkdownModelParsingException("El importador de arquitectura no reconoce diagram_type: " + type);
    }


    private static String sectionName(String heading) {
        return normalize(heading.replaceFirst("^#+", "").strip());
    }

    private static String displayName(String value) {
        String text = value == null ? "" : value.strip();
        return text.isBlank() ? "Elemento" : text;
    }

    private static String technologyFromText(String text, ArchitectureNodeKind kind) {
        String normalized = normalize(text);
        if (normalized.contains("postgres")) return "PostgreSQL";
        if (normalized.contains("javafx")) return "JavaFX";
        if (normalized.contains("spring")) return "Spring Boot";
        if (normalized.contains("http") || normalized.contains("https")) return "HTTP/HTTPS";
        if (kind == ArchitectureNodeKind.DATABASE) return "Base de datos";
        return "";
    }

    private static String protocolFromText(String text) {
        String normalized = normalize(text);
        if (normalized.contains("https")) return "HTTPS";
        if (normalized.contains("http")) return "HTTP";
        if (normalized.contains("jdbc")) return "JDBC";
        if (normalized.contains("red local")) return "red local";
        return "";
    }

    private static String normalize(String value) {
        return value == null ? "" : value.strip().toLowerCase(Locale.ROOT);
    }

    private static String stableProjectId(String title, ArchitectureDiagramKind kind) {
        return MarkdownTextUtils.toStableId(kind.diagramTypeId().value() + "_" + title);
    }

    private static final class ParsedArchitecture {
        private final Map<String, ArchitectureNode> nodes = new LinkedHashMap<>();
        private final List<ArchitectureEdge> edges = new ArrayList<>();
        private final List<String> notes = new ArrayList<>();
    }
}
