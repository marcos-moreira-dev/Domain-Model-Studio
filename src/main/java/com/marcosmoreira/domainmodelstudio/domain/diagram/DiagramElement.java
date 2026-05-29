package com.marcosmoreira.domainmodelstudio.domain.diagram;

/**
 * Contrato mínimo para elementos semánticos del diagrama.
 *
 * <p>No representa una figura JavaFX. Solo describe identidad, nombre y tipo lógico.
 * La capa de presentación decide cómo se dibuja cada elemento según la notación activa.</p>
 */
public interface DiagramElement {

    DiagramElementId id();

    String name();

    DiagramElementType type();
}
