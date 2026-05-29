package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.profile;

/**
 * Perfil de interacción que declara qué operaciones permite la herramienta visual activa.
 *
 * <p>El canvas común consulta este contrato para habilitar o bloquear comportamientos
 * transversales sin depender de familias concretas como UML, C4, BPMN, documentos o
 * matrices. La semántica específica sigue viviendo en adapters, render kits y modelos
 * del área de trabajo correspondiente.</p>
 */
public interface DiagramInteractionProfile {

    /** Nombre estable para documentación, pruebas y trazabilidad técnica. */
    String id();

    /** Nombre visible en documentación técnica y paneles contextuales. */
    String displayName();

    /** Descripción corta de la naturaleza de interacción del perfil. */
    String description();

    /** Permite arrastrar nodos o elementos visuales principales. */
    boolean supportsNodeDragging();

    /** Permite seleccionar varios elementos dibujando un rectángulo sobre el canvas. */
    boolean supportsAreaSelection();

    /** Permite seleccionar conectores o relaciones visuales. */
    boolean supportsConnectorSelection();

    /** Permite agregar, mover o eliminar puntos intermedios en conectores. */
    boolean supportsBendPoints();

    /** Permite mover etiquetas asociadas a conectores o mensajes. */
    boolean supportsConnectorLabels();

    /**
     * Indica si las etiquetas de conectores deben dibujarse en la capa común del canvas.
     *
     * <p>UML Secuencia, por ejemplo, permite etiquetas de mensaje, pero las dibuja como parte
     * de su eje temporal especializado; no debe recibir una segunda etiqueta overlay.</p>
     */
    default boolean supportsCommonConnectorLabelOverlay() {
        return supportsConnectorLabels() && !supportsTemporalOrdering();
    }

    /** Permite reconectar extremos de un conector cuando la notación lo permite. */
    boolean supportsEndpointDragging();

    /** Permite redimensionar nodos o componentes visuales. */
    boolean supportsNodeResize();

    /** Permite previsualización viva durante operaciones de edición. */
    boolean supportsLivePreview();

    /** Indica que el tipo activo tiene orden temporal propio, como UML Secuencia. */
    boolean supportsTemporalOrdering();

    /** Indica que la herramienta activa se edita como matriz, no como canvas libre. */
    boolean supportsMatrixEditing();

    /** Indica que la herramienta activa se edita o consulta como documento. */
    boolean supportsDocumentEditing();
}
