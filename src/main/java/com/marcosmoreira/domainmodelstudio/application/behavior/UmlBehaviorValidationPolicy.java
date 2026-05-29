package com.marcosmoreira.domainmodelstudio.application.behavior;

import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramKind;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorEdge;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorEdgeKind;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNode;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNodeKind;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Reglas de validación específicas para UML de comportamiento.
 *
 * <p>La política mantiene separada la semántica UML del caso de uso general.
 * El objetivo no es volver al editor un CASE completo, sino detectar mezclas
 * de notación que vuelven confusos los diagramas: actores conectados por
 * include, casos de uso fuera de un límite conceptual, actividades sin inicio,
 * decisiones sin guardas o estados finales con salidas.</p>
 */
public final class UmlBehaviorValidationPolicy {

    public List<String> validate(BehaviorDiagramDocument document) {
        if (document == null) {
            return List.of();
        }
        return switch (document.diagramKind()) {
            case UML_USE_CASE -> validateUseCase(document);
            case UML_ACTIVITY -> validateActivity(document);
            case UML_STATE -> validateState(document);
            default -> List.of();
        };
    }

    private static List<String> validateUseCase(BehaviorDiagramDocument document) {
        ArrayList<String> warnings = new ArrayList<>();
        long actors = countNodes(document, BehaviorNodeKind.ACTOR);
        long useCases = countNodes(document, BehaviorNodeKind.USE_CASE);
        long boundaries = countNodes(document, BehaviorNodeKind.SYSTEM_BOUNDARY);

        if (actors == 0) warnings.add("UML Casos de uso: agrega al menos un actor externo al sistema.");
        if (useCases == 0) warnings.add("UML Casos de uso: agrega al menos un caso de uso como óvalo dentro del límite del sistema.");
        if (boundaries == 0) warnings.add("UML Casos de uso: conviene agregar el límite del sistema para separar actores externos y casos internos.");
        if (boundaries > 1) warnings.add("UML Casos de uso: revisa si realmente necesitas más de un límite de sistema en el mismo diagrama.");

        Map<String, BehaviorNode> byId = nodesById(document);
        for (BehaviorEdge edge : document.edges()) {
            BehaviorNode source = byId.get(edge.sourceNodeId());
            BehaviorNode target = byId.get(edge.targetNodeId());
            if (source == null || target == null) {
                warnings.add("UML Casos de uso: hay una relación con origen o destino inexistente: " + edge.label());
                continue;
            }
            if ((edge.kind() == BehaviorEdgeKind.INCLUDE || edge.kind() == BehaviorEdgeKind.EXTEND)
                    && (source.kind() != BehaviorNodeKind.USE_CASE || target.kind() != BehaviorNodeKind.USE_CASE)) {
                warnings.add("UML Casos de uso: <<include>> y <<extend>> deben conectar casos de uso, no actores ni límites.");
            }
            if (edge.kind() == BehaviorEdgeKind.ASSOCIATION && !connectsActorAndUseCase(source, target)) {
                warnings.add("UML Casos de uso: una asociación normal debería conectar actor con caso de uso.");
            }
            if (edge.kind() == BehaviorEdgeKind.GENERALIZATION && (source.kind() == BehaviorNodeKind.SYSTEM_BOUNDARY || target.kind() == BehaviorNodeKind.SYSTEM_BOUNDARY)) {
                warnings.add("UML Casos de uso: la generalización no debería apuntar al límite del sistema.");
            }
        }
        warnIsolated(document, warnings, BehaviorNodeKind.ACTOR, "UML Casos de uso: el actor '%s' no participa en ningún caso de uso.");
        warnIsolated(document, warnings, BehaviorNodeKind.USE_CASE, "UML Casos de uso: el caso '%s' está aislado.");
        return warnings;
    }

    private static List<String> validateActivity(BehaviorDiagramDocument document) {
        ArrayList<String> warnings = new ArrayList<>();
        long initials = countNodes(document, BehaviorNodeKind.INITIAL_STATE);
        long finals = countNodes(document, BehaviorNodeKind.FINAL_STATE);
        long actions = countNodes(document, BehaviorNodeKind.ACTION);
        long forks = countNodes(document, BehaviorNodeKind.FORK);
        long joins = countNodes(document, BehaviorNodeKind.JOIN);

        if (initials == 0) warnings.add("UML Actividad: conviene agregar un nodo inicial sólido.");
        if (initials > 1) warnings.add("UML Actividad: normalmente hay un único nodo inicial para el flujo principal.");
        if (finals == 0) warnings.add("UML Actividad: conviene agregar al menos un nodo final.");
        if (actions == 0) warnings.add("UML Actividad: agrega acciones; el diagrama no debería ser solo decisiones y conectores.");
        if (forks > 0 && joins == 0) warnings.add("UML Actividad: si usas bifurcación, considera una unión para cerrar el paralelismo.");

        document.nodes().stream()
                .filter(node -> node.kind() == BehaviorNodeKind.DECISION)
                .filter(node -> outgoing(document, node.id()).size() > 1)
                .filter(node -> outgoing(document, node.id()).stream().anyMatch(UmlBehaviorValidationPolicy::missingGuard))
                .forEach(node -> warnings.add("UML Actividad: la decisión '" + node.displayName() + "' tiene salidas sin guarda [condición]."));
        document.nodes().stream()
                .filter(node -> node.kind() == BehaviorNodeKind.INITIAL_STATE)
                .filter(node -> incoming(document, node.id()).size() > 0)
                .forEach(node -> warnings.add("UML Actividad: el nodo inicial no debería tener entradas."));
        document.nodes().stream()
                .filter(node -> node.kind() == BehaviorNodeKind.FINAL_STATE)
                .filter(node -> outgoing(document, node.id()).size() > 0)
                .forEach(node -> warnings.add("UML Actividad: un nodo final no debería tener salidas."));
        warnIsolated(document, warnings,
                node -> node.kind() == BehaviorNodeKind.ACTION || node.kind() == BehaviorNodeKind.DECISION,
                "UML Actividad: el elemento '%s' está aislado del flujo.");
        return warnings;
    }

    private static List<String> validateState(BehaviorDiagramDocument document) {
        ArrayList<String> warnings = new ArrayList<>();
        long initials = countNodes(document, BehaviorNodeKind.INITIAL_STATE);
        long finals = countNodes(document, BehaviorNodeKind.FINAL_STATE);
        long states = countNodes(document, BehaviorNodeKind.STATE);

        if (initials == 0) warnings.add("UML Estados: conviene agregar un estado inicial sólido.");
        if (initials > 1) warnings.add("UML Estados: normalmente hay un único estado inicial por máquina de estados.");
        if (states == 0) warnings.add("UML Estados: agrega al menos un estado significativo del objeto o proceso.");
        if (finals == 0) warnings.add("UML Estados: agrega un estado final si el ciclo de vida puede cerrarse.");

        document.nodes().stream()
                .filter(node -> node.kind() == BehaviorNodeKind.INITIAL_STATE)
                .filter(node -> incoming(document, node.id()).size() > 0)
                .forEach(node -> warnings.add("UML Estados: el estado inicial no debería recibir transiciones."));
        document.nodes().stream()
                .filter(node -> node.kind() == BehaviorNodeKind.FINAL_STATE)
                .filter(node -> outgoing(document, node.id()).size() > 0)
                .forEach(node -> warnings.add("UML Estados: el estado final no debería tener transiciones salientes."));
        document.edges().stream()
                .filter(edge -> edge.kind() == BehaviorEdgeKind.TRANSITION)
                .filter(edge -> edge.label().isBlank() && edge.condition().isBlank())
                .forEach(edge -> warnings.add("UML Estados: una transición no indica evento, guarda ni acción."));
        warnIsolated(document, warnings, BehaviorNodeKind.STATE, "UML Estados: el estado '%s' está aislado.");
        return warnings;
    }

    private static Map<String, BehaviorNode> nodesById(BehaviorDiagramDocument document) {
        return document.nodes().stream().collect(Collectors.toMap(BehaviorNode::id, node -> node, (a, b) -> a));
    }

    private static boolean connectsActorAndUseCase(BehaviorNode source, BehaviorNode target) {
        return (source.kind() == BehaviorNodeKind.ACTOR && target.kind() == BehaviorNodeKind.USE_CASE)
                || (source.kind() == BehaviorNodeKind.USE_CASE && target.kind() == BehaviorNodeKind.ACTOR);
    }

    private static boolean missingGuard(BehaviorEdge edge) {
        return edge.label().isBlank() && edge.condition().isBlank();
    }

    private static void warnIsolated(BehaviorDiagramDocument document, ArrayList<String> warnings,
                                     BehaviorNodeKind kind, String messageTemplate) {
        warnIsolated(document, warnings, node -> node.kind() == kind, messageTemplate);
    }

    private static void warnIsolated(BehaviorDiagramDocument document, ArrayList<String> warnings,
                                     Predicate<BehaviorNode> predicate, String messageTemplate) {
        Set<String> connected = document.edges().stream()
                .flatMap(edge -> java.util.stream.Stream.of(edge.sourceNodeId(), edge.targetNodeId()))
                .collect(Collectors.toSet());
        document.nodes().stream()
                .filter(predicate)
                .filter(node -> !connected.contains(node.id()))
                .forEach(node -> warnings.add(String.format(messageTemplate, node.displayName())));
    }

    private static List<BehaviorEdge> outgoing(BehaviorDiagramDocument document, String nodeId) {
        return document.edges().stream().filter(edge -> edge.sourceNodeId().equals(nodeId)).toList();
    }

    private static List<BehaviorEdge> incoming(BehaviorDiagramDocument document, String nodeId) {
        return document.edges().stream().filter(edge -> edge.targetNodeId().equals(nodeId)).toList();
    }

    private static long countNodes(BehaviorDiagramDocument document, BehaviorNodeKind kind) {
        return document.nodes().stream().filter(node -> node.kind() == kind).count();
    }
}
