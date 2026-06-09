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

class BusinessProcessValidationPolicyTest {

    private final ValidateBehaviorDiagramUseCase validate = new ValidateBehaviorDiagramUseCase();

    @Test
    void bpmnWarnsGatewaysWithUnlabeledAlternativeOutputs() {
        BehaviorNode start = node("inicio", BehaviorNodeKind.START_EVENT, "Inicio", "");
        BehaviorNode decision = node("validar", BehaviorNodeKind.DECISION, "Validar pago", "Caja");
        BehaviorNode taskA = node("aprobar", BehaviorNodeKind.ACTIVITY, "Aprobar", "Caja");
        BehaviorNode taskB = node("rechazar", BehaviorNodeKind.ACTIVITY, "Rechazar", "Caja");
        BehaviorNode end = node("fin", BehaviorNodeKind.END_EVENT, "Fin", "");
        BehaviorDiagramDocument document = document(BehaviorDiagramKind.BPMN_BASIC, List.of(start, decision, taskA, taskB, end), List.of(
                edge("f1", start.id(), decision.id(), ""),
                edge("f2", decision.id(), taskA.id(), ""),
                edge("f3", decision.id(), taskB.id(), "no"),
                edge("f4", taskA.id(), end.id(), ""),
                edge("f5", taskB.id(), end.id(), "")
        ));

        BehaviorDiagramValidationResult result = validate.validate(document);

        assertTrue(result.warnings().stream().anyMatch(warning -> warning.contains("compuerta") && warning.contains("sin condición")));
    }

    @Test
    void operationalFlowWarnsStepsWithoutResponsible() {
        BehaviorNode start = node("paso", BehaviorNodeKind.ACTIVITY, "Revisar solicitud", "");
        BehaviorNode end = node("fin", BehaviorNodeKind.END_EVENT, "Cierre", "Secretaría");
        BehaviorDiagramDocument document = document(BehaviorDiagramKind.OPERATIONAL_FLOW, List.of(start, end), List.of(
                edge("e1", start.id(), end.id(), "continúa")
        ));

        BehaviorDiagramValidationResult result = validate.validate(document);

        assertTrue(result.warnings().stream().anyMatch(warning -> warning.contains("no tiene responsable")));
    }

    private static BehaviorDiagramDocument document(BehaviorDiagramKind kind, List<BehaviorNode> nodes, List<BehaviorEdge> edges) {
        return new BehaviorDiagramDocument("Proceso", "borrador", LocalDate.of(2026, 5, 1), kind, nodes, edges, "");
    }

    private static BehaviorNode node(String id, BehaviorNodeKind kind, String name, String owner) {
        return new BehaviorNode(id, kind, name, owner, "", "", 0);
    }

    private static BehaviorEdge edge(String id, String source, String target, String label) {
        return new BehaviorEdge(id, source, target, BehaviorEdgeKind.FLOW, label, "", "");
    }
}
