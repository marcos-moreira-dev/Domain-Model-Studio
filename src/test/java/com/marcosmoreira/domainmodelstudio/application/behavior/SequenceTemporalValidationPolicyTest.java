package com.marcosmoreira.domainmodelstudio.application.behavior;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramKind;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorEdge;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorEdgeKind;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNode;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNodeKind;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class SequenceTemporalValidationPolicyTest {

    private final SequenceTemporalValidationPolicy policy = new SequenceTemporalValidationPolicy();

    @Test
    void warnsWhenSequenceHasParticipantsButNoMessages() {
        BehaviorDiagramDocument document = new BehaviorDiagramDocument(
                "Secuencia", "borrador", LocalDate.now(), BehaviorDiagramKind.UML_SEQUENCE,
                List.of(participant("usuario"), participant("servicio")),
                List.of(), "");

        assertTrue(policy.validate(document).stream().anyMatch(warning -> warning.contains("sin mensajes")));
    }

    @Test
    void acceptsKnownAltKeywordAndValidatesItsOperands() {
        BehaviorDiagramDocument document = new BehaviorDiagramDocument(
                "Secuencia", "borrador", LocalDate.now(), BehaviorDiagramKind.UML_SEQUENCE,
                List.of(participant("usuario"), participant("servicio"),
                        new BehaviorNode("frag", BehaviorNodeKind.FRAGMENT,
                                "alt | titulo: credenciales | rango: 1..1 | operandos: [válidas] 1..1; [inválidas] 1..1", "", "", "", 1)),
                List.of(new BehaviorEdge("m1", "usuario", "servicio", BehaviorEdgeKind.MESSAGE, "validar", "", "")), "");

        assertTrue(policy.validate(document).stream().noneMatch(warning -> warning.contains("debería indicar alt")));
    }

    @Test
    void warnsWhenLoopDoesNotDeclareGuard() {
        BehaviorDiagramDocument document = new BehaviorDiagramDocument(
                "Secuencia", "borrador", LocalDate.now(), BehaviorDiagramKind.UML_SEQUENCE,
                List.of(participant("usuario"), participant("servicio"),
                        new BehaviorNode("loop", BehaviorNodeKind.FRAGMENT, "loop | titulo: repetir | rango: 1..1", "", "", "", 1)),
                List.of(new BehaviorEdge("m1", "usuario", "servicio", BehaviorEdgeKind.MESSAGE, "validar", "", "")), "");

        assertTrue(policy.validate(document).stream().anyMatch(warning -> warning.contains("condición de repetición")));
    }

    private static BehaviorNode participant(String id) {
        return new BehaviorNode(id, BehaviorNodeKind.PARTICIPANT, id, "", "", "", 0);
    }
}
