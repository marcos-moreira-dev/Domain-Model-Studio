package com.marcosmoreira.domainmodelstudio.application.architecture;

import com.marcosmoreira.domainmodelstudio.domain.architecture.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Validaciones básicas de consistencia para diagramas de arquitectura. */
public final class ValidateArchitectureDiagramUseCase {
    private final ArchitectureDiagramValidationPolicy diagramPolicy = new ArchitectureDiagramValidationPolicy();

    public ArchitectureDiagramValidationResult validate(ArchitectureDiagramDocument document) {
        if (document == null) return new ArchitectureDiagramValidationResult(List.of("No hay diagrama abierto."));
        ArrayList<String> warnings = new ArrayList<>();
        if (document.nodes().isEmpty()) warnings.add("Agrega al menos un elemento de arquitectura.");
        Set<String> ids = new HashSet<>();
        for (ArchitectureNode node : document.nodes()) {
            if (!ids.add(node.id())) warnings.add("Elemento duplicado: " + node.id());
            if (node.displayName().isBlank()) warnings.add("Hay un elemento sin nombre visible.");
        }
        if (document.nodes().size() > 1 && document.edges().isEmpty()) warnings.add("Conecta los elementos principales para mostrar dependencias o comunicación.");
        warnings.addAll(diagramPolicy.validateByDiagramKind(document));
        for (ArchitectureEdge edge : document.edges()) {
            if (!ids.contains(edge.sourceNodeId())) warnings.add("Relación con origen inexistente: " + edge.sourceNodeId());
            if (!ids.contains(edge.targetNodeId())) warnings.add("Relación con destino inexistente: " + edge.targetNodeId());
            if (edge.sourceNodeId().equals(edge.targetNodeId())) warnings.add("Relación apunta al mismo elemento: " + edge.id());
        }
        return new ArchitectureDiagramValidationResult(warnings);
    }
}
