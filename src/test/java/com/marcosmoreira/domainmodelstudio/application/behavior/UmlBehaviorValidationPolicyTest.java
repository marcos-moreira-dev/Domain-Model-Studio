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

class UmlBehaviorValidationPolicyTest {

    private final ValidateBehaviorDiagramUseCase validate = new ValidateBehaviorDiagramUseCase();

    @Test
    void useCaseWarnsWhenIncludeDoesNotConnectTwoUseCases() {
        BehaviorNode actor = node("actor", BehaviorNodeKind.ACTOR, "Secretario");
        BehaviorNode useCase = node("caso", BehaviorNodeKind.USE_CASE, "Registrar matrícula");
        BehaviorDiagramDocument document = document(BehaviorDiagramKind.UML_USE_CASE, List.of(actor, useCase), List.of(
                edge("e1", actor.id(), useCase.id(), BehaviorEdgeKind.INCLUDE, "")
        ));

        BehaviorDiagramValidationResult result = validate.validate(document);

        assertTrue(result.warnings().stream().anyMatch(warning -> warning.contains("<<include>>") && warning.contains("casos de uso")));
    }

    @Test
    void activityWarnsDecisionsWithMultipleUnguardedOutputs() {
        BehaviorNode start = node("inicio", BehaviorNodeKind.INITIAL_STATE, "Inicio");
        BehaviorNode decision = node("decision", BehaviorNodeKind.DECISION, "Validar datos");
        BehaviorNode save = node("guardar", BehaviorNodeKind.ACTION, "Guardar");
        BehaviorNode reject = node("rechazar", BehaviorNodeKind.ACTION, "Rechazar");
        BehaviorDiagramDocument document = document(BehaviorDiagramKind.UML_ACTIVITY, List.of(start, decision, save, reject), List.of(
                edge("e1", start.id(), decision.id(), BehaviorEdgeKind.FLOW, ""),
                edge("e2", decision.id(), save.id(), BehaviorEdgeKind.FLOW, ""),
                edge("e3", decision.id(), reject.id(), BehaviorEdgeKind.FLOW, "[no]")
        ));

        BehaviorDiagramValidationResult result = validate.validate(document);

        assertTrue(result.warnings().stream().anyMatch(warning -> warning.contains("decisión") && warning.contains("guarda")));
    }

    @Test
    void stateWarnsFinalStateWithOutgoingTransitions() {
        BehaviorNode active = node("activo", BehaviorNodeKind.STATE, "Activo");
        BehaviorNode finalState = node("fin", BehaviorNodeKind.FINAL_STATE, "Cerrado");
        BehaviorDiagramDocument document = document(BehaviorDiagramKind.UML_STATE, List.of(active, finalState), List.of(
                edge("t1", active.id(), finalState.id(), BehaviorEdgeKind.TRANSITION, "cerrar"),
                edge("t2", finalState.id(), active.id(), BehaviorEdgeKind.TRANSITION, "reabrir")
        ));

        BehaviorDiagramValidationResult result = validate.validate(document);

        assertTrue(result.warnings().stream().anyMatch(warning -> warning.contains("estado final") && warning.contains("salientes")));
    }

    private static BehaviorDiagramDocument document(BehaviorDiagramKind kind, List<BehaviorNode> nodes, List<BehaviorEdge> edges) {
        return new BehaviorDiagramDocument("UML", "borrador", LocalDate.of(2026, 5, 1), kind, nodes, edges, "");
    }

    private static BehaviorNode node(String id, BehaviorNodeKind kind, String name) {
        return new BehaviorNode(id, kind, name, "", "", "", 0);
    }

    private static BehaviorEdge edge(String id, String source, String target, BehaviorEdgeKind kind, String condition) {
        return new BehaviorEdge(id, source, target, kind, "", condition, "");
    }
}
