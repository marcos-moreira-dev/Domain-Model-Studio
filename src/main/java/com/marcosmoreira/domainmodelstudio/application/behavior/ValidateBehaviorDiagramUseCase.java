package com.marcosmoreira.domainmodelstudio.application.behavior;

import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorDiagramKind;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/** Validación básica para BPMN básico y UML de comportamiento. */
public final class ValidateBehaviorDiagramUseCase {

    private final BusinessProcessValidationPolicy processValidationPolicy = new BusinessProcessValidationPolicy();
    private final UmlBehaviorValidationPolicy umlBehaviorValidationPolicy = new UmlBehaviorValidationPolicy();
    private final SequenceTemporalValidationPolicy sequenceTemporalValidationPolicy = new SequenceTemporalValidationPolicy();

    public BehaviorDiagramValidationResult validate(BehaviorDiagramDocument document) {
        Objects.requireNonNull(document, "document");
        ArrayList<String> warnings = new ArrayList<>();
        if (document.nodes().isEmpty()) warnings.add("El diagrama no tiene elementos.");
        Set<String> names = new HashSet<>();
        document.nodes().forEach(node -> {
            if (node.displayName().isBlank()) warnings.add("Hay un elemento sin nombre.");
            String key = node.kind().name() + ":" + node.displayName().toLowerCase(java.util.Locale.ROOT);
            if (!names.add(key)) warnings.add("Elemento repetido: " + node.displayName());
        });
        document.edges().forEach(edge -> {
            if (edge.sourceNodeId().equals(edge.targetNodeId())) warnings.add("Relación apunta al mismo elemento: " + edge.label());
        });
        if (document.diagramKind() == BehaviorDiagramKind.BPMN_BASIC || document.diagramKind() == BehaviorDiagramKind.OPERATIONAL_FLOW) {
            warnings.addAll(processValidationPolicy.validate(document));
        }
        if (document.diagramKind() == BehaviorDiagramKind.UML_USE_CASE
                || document.diagramKind() == BehaviorDiagramKind.UML_ACTIVITY
                || document.diagramKind() == BehaviorDiagramKind.UML_STATE) {
            warnings.addAll(umlBehaviorValidationPolicy.validate(document));
        }
        if (document.diagramKind() == BehaviorDiagramKind.UML_SEQUENCE) {
            warnings.addAll(sequenceTemporalValidationPolicy.validate(document));
        }
        return new BehaviorDiagramValidationResult(warnings);
    }
}
