package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.profile;

/**
 * Perfiles estándar de interacción para las familias principales de Domain Model Studio.
 */
public enum StandardDiagramInteractionProfile implements DiagramInteractionProfile {

    /** Diagramas de nodos y conectores con edición visual general. */
    GRAPH(
            "graph",
            "Diagrama visual",
            "Nodos y conectores editables con selección, puntos intermedios, etiquetas y vista exportable.",
            true, true, true, true, true, false, false, true, false, false, false
    ),

    /** Diagramas UML de secuencia: edición temporal controlada, no grafo libre. */
    SEQUENCE(
            "sequence",
            "UML Secuencia",
            "Interacción temporal controlada con participantes, mensajes y orden vertical; no es un grafo libre.",
            true, true, true, false, true, false, true, true, true, false, false
    ),

    /** Maquetas administrativas: componentes movibles y redimensionables, sin conectores centrales. */
    WIREFRAME(
            "wireframe",
            "Maqueta administrativa",
            "Componentes de pantalla movibles y redimensionables; no usa relaciones UML como operación central.",
            true, true, false, false, false, false, true, true, false, false, false
    ),

    /** Matrices administrativas, como roles y permisos. */
    MATRIX(
            "matrix",
            "Matriz administrativa",
            "Edición tabular de filas, columnas y celdas; no tiene nodos, conectores ni puntos intermedios.",
            false, false, false, false, false, false, false, false, false, true, false
    ),

    /** Documentos técnicos, como diccionario de datos. */
    DOCUMENT(
            "document",
            "Documento técnico",
            "Edición documental o tabular con secciones, metadatos y exportación formal.",
            false, false, false, false, false, false, false, false, false, false, true
    ),

    /** Referencias de lectura, como la ayuda académica. */
    READ_ONLY_REFERENCE(
            "read-only-reference",
            "Referencia de lectura",
            "Consulta documental con navegación y búsqueda; no modifica modelos ni diagramas.",
            false, false, false, false, false, false, false, false, false, false, true
    );

    private final String id;
    private final String displayName;
    private final String description;
    private final boolean nodeDragging;
    private final boolean areaSelection;
    private final boolean connectorSelection;
    private final boolean bendPoints;
    private final boolean connectorLabels;
    private final boolean endpointDragging;
    private final boolean nodeResize;
    private final boolean livePreview;
    private final boolean temporalOrdering;
    private final boolean matrixEditing;
    private final boolean documentEditing;

    StandardDiagramInteractionProfile(
            String id,
            String displayName,
            String description,
            boolean nodeDragging,
            boolean areaSelection,
            boolean connectorSelection,
            boolean bendPoints,
            boolean connectorLabels,
            boolean endpointDragging,
            boolean nodeResize,
            boolean livePreview,
            boolean temporalOrdering,
            boolean matrixEditing,
            boolean documentEditing
    ) {
        this.id = id;
        this.displayName = displayName;
        this.description = description;
        this.nodeDragging = nodeDragging;
        this.areaSelection = areaSelection;
        this.connectorSelection = connectorSelection;
        this.bendPoints = bendPoints;
        this.connectorLabels = connectorLabels;
        this.endpointDragging = endpointDragging;
        this.nodeResize = nodeResize;
        this.livePreview = livePreview;
        this.temporalOrdering = temporalOrdering;
        this.matrixEditing = matrixEditing;
        this.documentEditing = documentEditing;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String displayName() {
        return displayName;
    }

    @Override
    public String description() {
        return description;
    }

    @Override
    public boolean supportsNodeDragging() {
        return nodeDragging;
    }

    @Override
    public boolean supportsAreaSelection() {
        return areaSelection;
    }

    @Override
    public boolean supportsConnectorSelection() {
        return connectorSelection;
    }

    @Override
    public boolean supportsBendPoints() {
        return bendPoints;
    }

    @Override
    public boolean supportsConnectorLabels() {
        return connectorLabels;
    }

    @Override
    public boolean supportsEndpointDragging() {
        return endpointDragging;
    }

    @Override
    public boolean supportsNodeResize() {
        return nodeResize;
    }

    @Override
    public boolean supportsLivePreview() {
        return livePreview;
    }

    @Override
    public boolean supportsTemporalOrdering() {
        return temporalOrdering;
    }

    @Override
    public boolean supportsMatrixEditing() {
        return matrixEditing;
    }

    @Override
    public boolean supportsDocumentEditing() {
        return documentEditing;
    }
}
