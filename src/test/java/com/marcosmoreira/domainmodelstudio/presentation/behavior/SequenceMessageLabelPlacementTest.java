package com.marcosmoreira.domainmodelstudio.presentation.behavior;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class SequenceMessageLabelPlacementTest {

    @Test
    void longMessagesReceiveAReadableExportWidth() {
        double width = SequenceMessageLabelPlacement.widthFor(
                "guarda calificaciones por estudiante y registra auditoria academica");

        assertTrue(width >= 300.0, "los mensajes largos de secuencia no deben quedar truncados por un ancho mínimo demasiado bajo");
    }
}
