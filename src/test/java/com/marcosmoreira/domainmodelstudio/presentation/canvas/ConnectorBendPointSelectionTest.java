package com.marcosmoreira.domainmodelstudio.presentation.canvas;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import org.junit.jupiter.api.Test;

class ConnectorBendPointSelectionTest {

    @Test
    void matchesConnectorAndIndex() {
        DiagramElementId connectorId = DiagramElementId.of("conn_cliente_matricula");
        ConnectorBendPointSelection selection = new ConnectorBendPointSelection(connectorId, 2);

        assertTrue(selection.matches(connectorId, 2));
        assertFalse(selection.matches(connectorId, 1));
        assertFalse(selection.matches(DiagramElementId.of("otro_conector"), 2));
    }

    @Test
    void rejectsNegativeIndex() {
        assertThrows(IllegalArgumentException.class,
                () -> new ConnectorBendPointSelection(DiagramElementId.of("conn"), -1));
    }
}
