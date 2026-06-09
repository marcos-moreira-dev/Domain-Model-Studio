package com.marcosmoreira.domainmodelstudio.application.behavior;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramKind;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorEdge;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorEdgeKind;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNode;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNodeKind;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class MoveSequenceMessageUseCaseTest {

    private final MoveSequenceMessageUseCase useCase = new MoveSequenceMessageUseCase();

    @Test
    void movesSelectedMessageWithoutChangingItsId() {
        BehaviorDiagramDocument document = baseDocument();

        BehaviorDiagramDocument moved = useCase.move(document, "m2", MoveSequenceMessageUseCase.Direction.UP);

        assertEquals("m2", moved.edges().get(0).id());
        assertEquals("m1", moved.edges().get(1).id());
    }

    private static BehaviorDiagramDocument baseDocument() {
        return new BehaviorDiagramDocument("Secuencia", "borrador", LocalDate.now(), BehaviorDiagramKind.UML_SEQUENCE,
                List.of(participant("usuario"), participant("servicio")),
                List.of(
                        new BehaviorEdge("m1", "usuario", "servicio", BehaviorEdgeKind.MESSAGE, "solicita", "", ""),
                        new BehaviorEdge("m2", "servicio", "usuario", BehaviorEdgeKind.RETURN_MESSAGE, "responde", "", "")
                ), "");
    }

    private static BehaviorNode participant(String id) {
        return new BehaviorNode(id, BehaviorNodeKind.PARTICIPANT, id, "", "", "", 0);
    }
}
