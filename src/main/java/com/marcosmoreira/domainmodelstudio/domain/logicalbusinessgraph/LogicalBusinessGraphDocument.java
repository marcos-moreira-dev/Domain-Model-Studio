package com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Agregado raíz inmutable del grafo lógico del negocio.
 *
 * <p>El documento mantiene el encabezado del artefacto, la lista de nodos
 * semánticos, la lista de relaciones dirigidas y notas de lectura. Al
 * construirse indexa nodos y relaciones para rechazar duplicados y para impedir
 * relaciones hacia nodos inexistentes.</p>
 *
 * <p>Las operaciones {@code with...} no modifican la instancia actual: crean un
 * nuevo documento validado. Este contrato permite usar el dominio desde UI,
 * importadores, persistencia y pruebas sin efectos laterales ocultos.</p>
 
 *
 * <p><strong>Ejemplo pedagógico:</strong> importar el ejemplo UENS produce un documento
 * con nodos {@code MF}, {@code FL}, {@code CU}, {@code ACC} y relaciones semánticas.
 * Mover un nodo en el canvas no cambia estos nodos: solo actualiza el layout externo
 * asociado al proyecto.</p>
 */
public final class LogicalBusinessGraphDocument {

    private final String projectName;
    private final String version;
    private final LocalDate documentDate;
    private final List<LogicalBusinessGraphNode> nodes;
    private final List<LogicalBusinessGraphEdge> edges;
    private final String notes;
    private final Map<String, LogicalBusinessGraphNode> nodesByCode;
    private final Map<String, LogicalBusinessGraphEdge> edgesById;

    /**
     * Crea un documento validado y normalizado.
     *
     * @param projectName nombre legible del proyecto; se reemplaza por un valor
     *                    seguro si llega vacío
     * @param version versión documental visible para el usuario
     * @param documentDate fecha de referencia del documento; si es {@code null}
     *                     se usa la fecha actual
     * @param nodes nodos semánticos del grafo; no pueden repetir código
     * @param edges relaciones dirigidas; no pueden repetir ID ni apuntar a nodos
     *              inexistentes
     * @param notes observaciones generales del grafo
     */
    public LogicalBusinessGraphDocument(String projectName, String version, LocalDate documentDate,
                                        List<LogicalBusinessGraphNode> nodes,
                                        List<LogicalBusinessGraphEdge> edges,
                                        String notes) {
        this.projectName = defaulted(projectName, "Grafo lógico del negocio");
        this.version = defaulted(version, "v0.1");
        this.documentDate = documentDate == null ? LocalDate.now() : documentDate;
        this.nodes = List.copyOf(nodes == null ? List.of() : nodes);
        this.edges = List.copyOf(edges == null ? List.of() : edges);
        this.notes = LogicalBusinessGraphText.normalize(notes);
        this.nodesByCode = indexNodes(this.nodes);
        this.edgesById = indexEdges(this.edges);
        validateEdgeReferences();
    }

    /**
     * Crea un documento vacío para un proyecto nuevo.
     *
     * @param projectName nombre inicial mostrado en UI y exportaciones
     * @return grafo lógico sin nodos ni relaciones
     */
    public static LogicalBusinessGraphDocument blank(String projectName) {
        return new LogicalBusinessGraphDocument(projectName, "v0.1", LocalDate.now(), List.of(), List.of(), "");
    }

    public String projectName() {
        return projectName;
    }

    public String version() {
        return version;
    }

    public LocalDate documentDate() {
        return documentDate;
    }

    public List<LogicalBusinessGraphNode> nodes() {
        return nodes;
    }

    public List<LogicalBusinessGraphEdge> edges() {
        return edges;
    }

    public String notes() {
        return notes;
    }

    public boolean empty() {
        return nodes.isEmpty() && edges.isEmpty();
    }

    /**
     * Busca un nodo por su código lógico normalizado, por ejemplo {@code CU-001}.
     *
     * @param code código del nodo, aceptando minúsculas y espacios externos
     * @return nodo encontrado o vacío si el código no pertenece al documento
     */
    public Optional<LogicalBusinessGraphNode> nodeByCode(String code) {
        return Optional.ofNullable(nodesByCode.get(normalizeCode(code)));
    }

    /**
     * Busca una relación por su identificador persistible.
     *
     * @param id identificador de relación
     * @return relación encontrada o vacío si no existe
     */
    public Optional<LogicalBusinessGraphEdge> edgeById(String id) {
        return Optional.ofNullable(edgesById.get(LogicalBusinessGraphText.normalize(id)));
    }

    /**
     * Filtra nodos por tipo semántico.
     *
     * @param kind tipo de nodo requerido
     * @return lista estable con los nodos de ese tipo
     */
    public List<LogicalBusinessGraphNode> nodesByKind(LogicalBusinessGraphNodeKind kind) {
        Objects.requireNonNull(kind, "kind");
        return nodes.stream().filter(node -> node.kind() == kind).toList();
    }

    /**
     * Devuelve relaciones que salen del nodo indicado.
     *
     * @param code código lógico del nodo origen
     * @return relaciones salientes en el orden del documento
     */
    public List<LogicalBusinessGraphEdge> outgoingEdgesOf(String code) {
        String normalized = normalizeCode(code);
        return edges.stream().filter(edge -> edge.sourceCode().equals(normalized)).toList();
    }

    /**
     * Devuelve relaciones que llegan al nodo indicado.
     *
     * @param code código lógico del nodo destino
     * @return relaciones entrantes en el orden del documento
     */
    public List<LogicalBusinessGraphEdge> incomingEdgesOf(String code) {
        String normalized = normalizeCode(code);
        return edges.stream().filter(edge -> edge.targetCode().equals(normalized)).toList();
    }

    /**
     * Agrega un nodo y devuelve un nuevo documento.
     *
     * @param node nodo semántico con código único
     * @return documento nuevo con el nodo agregado
     * @throws IllegalArgumentException si ya existe un nodo con el mismo código
     */
    public LogicalBusinessGraphDocument withNode(LogicalBusinessGraphNode node) {
        Objects.requireNonNull(node, "node");
        if (nodesByCode.containsKey(node.code())) {
            throw new IllegalArgumentException("Ya existe un nodo lógico con código: " + node.code());
        }
        List<LogicalBusinessGraphNode> updated = new ArrayList<>(nodes);
        updated.add(node);
        return copyWith(updated, edges);
    }

    /**
     * Reemplaza un nodo existente conservando su código.
     *
     * @param updatedNode nueva versión del nodo
     * @return documento nuevo con el nodo reemplazado
     * @throws IllegalArgumentException si el nodo no existe
     */
    public LogicalBusinessGraphDocument withUpdatedNode(LogicalBusinessGraphNode updatedNode) {
        Objects.requireNonNull(updatedNode, "updatedNode");
        return copyWith(replaceNode(updatedNode), edges);
    }

    /**
     * Elimina un nodo y también sus relaciones entrantes y salientes.
     *
     * @param code código del nodo a eliminar
     * @return documento nuevo sin el nodo ni sus relaciones asociadas
     */
    public LogicalBusinessGraphDocument withoutNode(String code) {
        String normalized = normalizeCode(code);
        if (!nodesByCode.containsKey(normalized)) {
            throw new IllegalArgumentException("No existe nodo lógico para eliminar: " + normalized);
        }
        List<LogicalBusinessGraphNode> updatedNodes = nodes.stream()
                .filter(node -> !node.code().equals(normalized))
                .toList();
        List<LogicalBusinessGraphEdge> updatedEdges = edges.stream()
                .filter(edge -> !edge.sourceCode().equals(normalized))
                .filter(edge -> !edge.targetCode().equals(normalized))
                .toList();
        return copyWith(updatedNodes, updatedEdges);
    }

    /**
     * Agrega una relación dirigida y valida que sus extremos existan.
     *
     * @param edge relación lógica con ID único
     * @return documento nuevo con la relación agregada
     */
    public LogicalBusinessGraphDocument withEdge(LogicalBusinessGraphEdge edge) {
        Objects.requireNonNull(edge, "edge");
        if (edgesById.containsKey(edge.id())) {
            throw new IllegalArgumentException("Ya existe una relación lógica con ID: " + edge.id());
        }
        List<LogicalBusinessGraphEdge> updated = new ArrayList<>(edges);
        updated.add(edge);
        return copyWith(nodes, updated);
    }

    /**
     * Reemplaza una relación existente conservando su ID.
     *
     * @param updatedEdge nueva versión de la relación
     * @return documento nuevo con la relación reemplazada
     */
    public LogicalBusinessGraphDocument withUpdatedEdge(LogicalBusinessGraphEdge updatedEdge) {
        Objects.requireNonNull(updatedEdge, "updatedEdge");
        return copyWith(nodes, replaceEdge(updatedEdge));
    }

    /**
     * Ejecuta la validación semántica integral del grafo.
     *
     * @return hallazgos de calidad clasificados como bloqueo, advertencia o
     *         información
     */
    public List<LogicalBusinessGraphIssue> semanticIssues() {
        return new LogicalBusinessGraphValidationPolicy().validate(this);
    }

    /**
     * Actualiza metadatos editoriales sin alterar nodos ni relaciones.
     *
     * @return documento nuevo con encabezado y notas actualizados
     */
    public LogicalBusinessGraphDocument withHeader(String updatedProjectName, String updatedVersion,
                                                   LocalDate updatedDocumentDate, String updatedNotes) {
        return new LogicalBusinessGraphDocument(updatedProjectName, updatedVersion, updatedDocumentDate,
                nodes, edges, updatedNotes);
    }

    private LogicalBusinessGraphDocument copyWith(List<LogicalBusinessGraphNode> updatedNodes,
                                                  List<LogicalBusinessGraphEdge> updatedEdges) {
        return new LogicalBusinessGraphDocument(projectName, version, documentDate, updatedNodes, updatedEdges, notes);
    }

    private List<LogicalBusinessGraphNode> replaceNode(LogicalBusinessGraphNode updatedNode) {
        List<LogicalBusinessGraphNode> updated = new ArrayList<>();
        boolean replaced = false;
        for (LogicalBusinessGraphNode node : nodes) {
            if (node.code().equals(updatedNode.code())) {
                updated.add(updatedNode);
                replaced = true;
            } else {
                updated.add(node);
            }
        }
        if (!replaced) {
            throw new IllegalArgumentException("No existe nodo lógico para actualizar: " + updatedNode.code());
        }
        return updated;
    }

    private List<LogicalBusinessGraphEdge> replaceEdge(LogicalBusinessGraphEdge updatedEdge) {
        List<LogicalBusinessGraphEdge> updated = new ArrayList<>();
        boolean replaced = false;
        for (LogicalBusinessGraphEdge edge : edges) {
            if (edge.id().equals(updatedEdge.id())) {
                updated.add(updatedEdge);
                replaced = true;
            } else {
                updated.add(edge);
            }
        }
        if (!replaced) {
            throw new IllegalArgumentException("No existe relación lógica para actualizar: " + updatedEdge.id());
        }
        return updated;
    }

    private void validateEdgeReferences() {
        for (LogicalBusinessGraphEdge edge : edges) {
            if (!nodesByCode.containsKey(edge.sourceCode())) {
                throw new IllegalArgumentException("La relación " + edge.id() + " referencia origen inexistente: " + edge.sourceCode());
            }
            if (!nodesByCode.containsKey(edge.targetCode())) {
                throw new IllegalArgumentException("La relación " + edge.id() + " referencia destino inexistente: " + edge.targetCode());
            }
        }
    }

    private static Map<String, LogicalBusinessGraphNode> indexNodes(List<LogicalBusinessGraphNode> nodes) {
        Map<String, LogicalBusinessGraphNode> indexed = new LinkedHashMap<>();
        for (LogicalBusinessGraphNode node : nodes) {
            if (indexed.putIfAbsent(node.code(), node) != null) {
                throw new IllegalArgumentException("Código de nodo lógico duplicado: " + node.code());
            }
        }
        return Map.copyOf(indexed);
    }

    private static Map<String, LogicalBusinessGraphEdge> indexEdges(List<LogicalBusinessGraphEdge> edges) {
        Map<String, LogicalBusinessGraphEdge> indexed = new LinkedHashMap<>();
        for (LogicalBusinessGraphEdge edge : edges) {
            if (indexed.putIfAbsent(edge.id(), edge) != null) {
                throw new IllegalArgumentException("ID de relación lógica duplicado: " + edge.id());
            }
        }
        return Map.copyOf(indexed);
    }

    private static String normalizeCode(String code) {
        return LogicalBusinessGraphText.require(code, "code").toUpperCase();
    }

    private static String defaulted(String value, String fallback) {
        String normalized = LogicalBusinessGraphText.normalize(value);
        return normalized.isBlank() ? fallback : normalized;
    }
}
