package com.marcosmoreira.domainmodelstudio.application.behavior;

import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramKind;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorEdge;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNode;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNodeKind;
import com.marcosmoreira.domainmodelstudio.domain.behavior.SequenceCombinedFragmentSpec;
import com.marcosmoreira.domainmodelstudio.domain.behavior.SequenceFragmentKind;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Validaciones específicas para UML Secuencia.
 *
 * <p>El objetivo es proteger la lectura temporal: participantes arriba,
 * mensajes ordenados verticalmente, activaciones asociadas a una línea de vida
 * y fragmentos combinados con operador, guardas, operandos y rangos temporales.</p>
 */
public final class SequenceTemporalValidationPolicy {

    private static final int MAX_NESTING_LEVEL = 3;
    private final SequenceMessageOrderPolicy orderPolicy = new SequenceMessageOrderPolicy();

    public List<String> validate(BehaviorDiagramDocument document) {
        if (document == null || document.diagramKind() != BehaviorDiagramKind.UML_SEQUENCE) {
            return List.of();
        }
        ArrayList<String> warnings = new ArrayList<>();
        Map<String, BehaviorNode> nodes = document.nodes().stream()
                .collect(Collectors.toMap(BehaviorNode::id, node -> node, (a, b) -> a));
        List<BehaviorNode> participants = document.nodes().stream()
                .filter(node -> node.kind() == BehaviorNodeKind.PARTICIPANT)
                .toList();
        List<BehaviorEdge> messages = orderPolicy.orderedMessages(document);

        if (participants.size() < 2) {
            warnings.add("UML Secuencia: agrega al menos dos participantes para que exista interacción temporal.");
        }
        if (messages.isEmpty()) {
            warnings.add("UML Secuencia: agrega mensajes; sin mensajes solo hay participantes, no una secuencia.");
        }
        for (BehaviorEdge message : messages) {
            BehaviorNode source = nodes.get(message.sourceNodeId());
            BehaviorNode target = nodes.get(message.targetNodeId());
            if (source == null || target == null) {
                warnings.add("UML Secuencia: hay un mensaje con origen o destino inexistente: " + message.label());
                continue;
            }
            if (source.kind() != BehaviorNodeKind.PARTICIPANT || target.kind() != BehaviorNodeKind.PARTICIPANT) {
                warnings.add("UML Secuencia: el mensaje '" + displayLabel(message) + "' debería conectar participantes/líneas de vida.");
            }
            if (message.label().isBlank()) {
                warnings.add("UML Secuencia: un mensaje no debería quedar sin nombre operativo.");
            }
        }
        document.nodes().stream()
                .filter(node -> node.kind() == BehaviorNodeKind.ACTIVATION)
                .filter(node -> !node.owner().isBlank())
                .filter(node -> !matchesParticipant(node.owner(), participants))
                .forEach(node -> warnings.add("UML Secuencia: la activación '" + node.displayName() + "' referencia un participante inexistente: " + node.owner()));
        validateFragments(document, messages.size(), nodes, warnings);
        return warnings;
    }

    private static void validateFragments(
            BehaviorDiagramDocument document,
            int messageCount,
            Map<String, BehaviorNode> nodes,
            List<String> warnings
    ) {
        List<BehaviorNode> fragments = document.nodes().stream()
                .filter(node -> node.kind() == BehaviorNodeKind.FRAGMENT)
                .toList();
        Map<String, SequenceCombinedFragmentSpec> specs = fragments.stream()
                .collect(Collectors.toMap(BehaviorNode::id, SequenceCombinedFragmentSpec::fromNode, (a, b) -> a));
        for (BehaviorNode fragment : fragments) {
            SequenceCombinedFragmentSpec spec = specs.get(fragment.id());
            if (!spec.kind().known()) {
                warnings.add("UML Secuencia: el fragmento '" + fragment.displayName()
                        + "' debería indicar " + SequenceFragmentKind.supportedKeywords() + ".");
                continue;
            }
            validateFragmentRange(fragment, spec, messageCount, warnings);
            validateFragmentKind(fragment, spec, warnings);
            validateFragmentParent(fragment, spec, nodes, specs, warnings);
            if (spec.nestingLevel() > MAX_NESTING_LEVEL) {
                warnings.add("UML Secuencia: el fragmento '" + fragment.displayName()
                        + "' supera la anidación recomendada de " + MAX_NESTING_LEVEL + " niveles.");
            }
        }
    }

    private static void validateFragmentRange(BehaviorNode fragment, SequenceCombinedFragmentSpec spec, int messageCount, List<String> warnings) {
        if (!spec.hasRange()) {
            warnings.add("UML Secuencia: el fragmento '" + fragment.displayName()
                    + "' debería indicar un rango temporal de mensajes, por ejemplo rango: 2..5.");
            return;
        }
        if (spec.startMessageIndex() > messageCount || spec.endMessageIndex() > messageCount) {
            warnings.add("UML Secuencia: el fragmento '" + fragment.displayName()
                    + "' referencia mensajes fuera del rango real de la secuencia.");
        }
    }

    private static void validateFragmentKind(BehaviorNode fragment, SequenceCombinedFragmentSpec spec, List<String> warnings) {
        switch (spec.kind()) {
            case ALT -> {
                if (spec.operands().size() < 2) {
                    warnings.add("UML Secuencia: el fragmento alt '" + fragment.displayName()
                            + "' debería tener al menos dos operandos con guardas alternativas.");
                }
            }
            case OPT -> {
                if (!hasGuard(spec)) {
                    warnings.add("UML Secuencia: el fragmento opt '" + fragment.displayName()
                            + "' debería declarar una guarda [condición].");
                }
            }
            case LOOP -> {
                if (!hasGuard(spec)) {
                    warnings.add("UML Secuencia: el fragmento loop '" + fragment.displayName()
                            + "' debería declarar condición de repetición.");
                }
            }
            case PAR -> {
                if (spec.operands().size() < 2) {
                    warnings.add("UML Secuencia: el fragmento par '" + fragment.displayName()
                            + "' debería tener al menos dos operandos paralelos.");
                }
            }
            case BREAK -> {
                if (!hasGuard(spec)) {
                    warnings.add("UML Secuencia: el fragmento break '" + fragment.displayName()
                            + "' debería indicar la condición que interrumpe la interacción.");
                }
            }
            case REF -> {
                if (spec.reference().isBlank() && spec.title().isBlank()) {
                    warnings.add("UML Secuencia: el fragmento ref '" + fragment.displayName()
                            + "' debería indicar la interacción referenciada.");
                }
            }
            case CRITICAL, UNKNOWN -> {
                // CRITICAL no exige guarda; protege una región temporal completa. UNKNOWN se valida antes.
            }
        }
    }

    private static void validateFragmentParent(
            BehaviorNode fragment,
            SequenceCombinedFragmentSpec spec,
            Map<String, BehaviorNode> nodes,
            Map<String, SequenceCombinedFragmentSpec> specs,
            List<String> warnings
    ) {
        if (spec.parentId().isBlank()) {
            return;
        }
        BehaviorNode parent = nodes.get(spec.parentId());
        SequenceCombinedFragmentSpec parentSpec = specs.get(spec.parentId());
        if (parent == null || parentSpec == null) {
            warnings.add("UML Secuencia: el fragmento '" + fragment.displayName()
                    + "' referencia un padre inexistente: " + spec.parentId());
            return;
        }
        if (spec.hasRange() && parentSpec.hasRange()
                && (spec.startMessageIndex() < parentSpec.startMessageIndex()
                || spec.endMessageIndex() > parentSpec.endMessageIndex())) {
            warnings.add("UML Secuencia: el fragmento '" + fragment.displayName()
                    + "' queda fuera del rango temporal de su padre '" + parent.displayName() + "'.");
        }
    }

    private static boolean hasGuard(SequenceCombinedFragmentSpec spec) {
        return !spec.guard().isBlank() || spec.operands().stream().anyMatch(operand -> !operand.guard().isBlank());
    }

    private static boolean matchesParticipant(String owner, List<BehaviorNode> participants) {
        String normalized = normalize(owner);
        return participants.stream().anyMatch(participant -> participant.id().equalsIgnoreCase(normalized)
                || normalize(participant.displayName()).equals(normalized));
    }


    private static String displayLabel(BehaviorEdge message) {
        return message.label().isBlank() ? message.id() : message.label();
    }

    private static String normalize(String value) {
        return value == null ? "" : value.strip().toLowerCase(java.util.Locale.ROOT);
    }
}
