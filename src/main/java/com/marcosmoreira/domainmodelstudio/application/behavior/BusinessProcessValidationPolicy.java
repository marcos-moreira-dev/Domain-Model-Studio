package com.marcosmoreira.domainmodelstudio.application.behavior;

import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramKind;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorEdge;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNode;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNodeKind;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Reglas de validación específicas para diagramas de procesos de negocio.
 *
 * <p>Se separan del caso de uso general para que BPMN básico y flujo operativo
 * tengan advertencias útiles sin inflar {@link ValidateBehaviorDiagramUseCase}.</p>
 */
public final class BusinessProcessValidationPolicy {

    public List<String> validate(BehaviorDiagramDocument document) {
        if (document == null) {
            return List.of();
        }
        if (document.diagramKind() == BehaviorDiagramKind.BPMN_BASIC) {
            return validateBpmn(document);
        }
        if (document.diagramKind() == BehaviorDiagramKind.OPERATIONAL_FLOW) {
            return validateOperationalFlow(document);
        }
        return List.of();
    }

    private static List<String> validateBpmn(BehaviorDiagramDocument document) {
        ArrayList<String> warnings = new ArrayList<>();
        long starts = countNodes(document, BehaviorNodeKind.START_EVENT);
        long ends = countNodes(document, BehaviorNodeKind.END_EVENT);
        long activities = countNodes(document, BehaviorNodeKind.ACTIVITY);

        if (starts == 0) warnings.add("BPMN: conviene agregar un evento de inicio.");
        if (starts > 1) warnings.add("BPMN: revisa si realmente necesitas más de un evento de inicio.");
        if (ends == 0) warnings.add("BPMN: conviene agregar al menos un evento de fin.");
        if (activities == 0) warnings.add("BPMN: agrega al menos una actividad o tarea del proceso.");

        warnIsolatedWorkNodes(document, warnings, "BPMN");
        document.nodes().stream()
                .filter(node -> node.kind() == BehaviorNodeKind.DECISION)
                .filter(node -> outgoing(document, node.id()).size() > 1)
                .filter(node -> outgoing(document, node.id()).stream().anyMatch(edge -> edge.condition().isBlank() && edge.label().isBlank()))
                .forEach(node -> warnings.add("BPMN: la compuerta '" + node.displayName() + "' tiene salidas sin condición o etiqueta."));
        return warnings;
    }

    private static List<String> validateOperationalFlow(BehaviorDiagramDocument document) {
        ArrayList<String> warnings = new ArrayList<>();
        long steps = document.nodes().stream()
                .filter(node -> node.kind() == BehaviorNodeKind.ACTIVITY)
                .count();
        long responsibleBands = document.nodes().stream()
                .filter(node -> node.kind() == BehaviorNodeKind.LANE)
                .count();

        if (steps == 0) warnings.add("Flujo operativo: agrega al menos un paso operativo.");
        if (responsibleBands == 0 && document.nodes().stream().anyMatch(node -> node.owner().isBlank())) {
            warnings.add("Flujo operativo: registra responsables como banda o como propietario de cada paso.");
        }
        warnIsolatedWorkNodes(document, warnings, "Flujo operativo");
        document.nodes().stream()
                .filter(node -> node.kind() == BehaviorNodeKind.ACTIVITY)
                .filter(node -> node.owner().isBlank())
                .forEach(node -> warnings.add("Flujo operativo: el paso '" + node.displayName() + "' no tiene responsable."));
        document.edges().stream()
                .filter(edge -> edge.label().isBlank() && edge.condition().isBlank())
                .forEach(edge -> warnings.add("Flujo operativo: una conexión no indica acción, verbo o condición."));
        return warnings;
    }

    private static void warnIsolatedWorkNodes(BehaviorDiagramDocument document, ArrayList<String> warnings, String label) {
        Set<String> connected = document.edges().stream()
                .flatMap(edge -> java.util.stream.Stream.of(edge.sourceNodeId(), edge.targetNodeId()))
                .collect(Collectors.toSet());
        document.nodes().stream()
                .filter(node -> node.kind() == BehaviorNodeKind.ACTIVITY || node.kind() == BehaviorNodeKind.DECISION)
                .filter(node -> !connected.contains(node.id()))
                .forEach(node -> warnings.add(label + ": el elemento '" + node.displayName() + "' está aislado."));
    }

    private static long countNodes(BehaviorDiagramDocument document, BehaviorNodeKind kind) {
        return document.nodes().stream().filter(node -> node.kind() == kind).count();
    }

    private static List<BehaviorEdge> outgoing(BehaviorDiagramDocument document, String nodeId) {
        return document.edges().stream().filter(edge -> edge.sourceNodeId().equals(nodeId)).toList();
    }
}
