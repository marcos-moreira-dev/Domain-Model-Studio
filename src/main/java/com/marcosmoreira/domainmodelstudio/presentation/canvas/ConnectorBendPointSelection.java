package com.marcosmoreira.domainmodelstudio.presentation.canvas;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import java.util.Objects;

/**
 * Selección puntual de un nodo intermedio de una línea conceptual.
 *
 * <p>El conector sigue siendo el elemento semántico seleccionado, pero esta referencia
 * permite que acciones como Suprimir/Borrar actúen sobre el punto intermedio y no sobre
 * toda la relación. Mantenerlo como valor pequeño evita cargar al modelo de selección
 * general con detalles exclusivos del canvas.</p>
 */
public record ConnectorBendPointSelection(DiagramElementId connectorId, int bendPointIndex) {

    public ConnectorBendPointSelection {
        Objects.requireNonNull(connectorId, "El ID del conector no puede ser null");
        if (bendPointIndex < 0) {
            throw new IllegalArgumentException("El índice del punto intermedio no puede ser negativo");
        }
    }

    public boolean matches(DiagramElementId candidateConnectorId, int candidateBendPointIndex) {
        return connectorId.equals(candidateConnectorId) && bendPointIndex == candidateBendPointIndex;
    }
}
