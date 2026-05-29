package com.marcosmoreira.domainmodelstudio.application.freegraph;

import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphDocument;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphEdge;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphEdgeDirection;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphKind;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphNode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

/** Revisa consistencia funcional y advertencias de uso del Grafo libre. */
public final class ValidateFreeGraphUseCase {

    public FreeGraphValidationResult validate(FreeGraphDocument document) {
        Objects.requireNonNull(document, "document");
        ArrayList<String> warnings = new ArrayList<>();
        if (document.nodes().isEmpty()) {
            warnings.add("El grafo todavía no tiene nodos.");
        }
        Set<String> titles = new HashSet<>();
        for (FreeGraphNode node : document.nodes()) {
            if (!titles.add(node.title().toLowerCase(Locale.ROOT))) {
                warnings.add("Título de nodo repetido: " + node.title());
            }
            if (node.content().isBlank()) {
                warnings.add("El nodo '" + node.title() + "' todavía no tiene contenido.");
            }
            if (document.incidentEdgesOf(node.id()).isEmpty()) {
                warnings.add("El nodo '" + node.title() + "' todavía no tiene relaciones.");
            }
        }
        for (FreeGraphEdge edge : document.edges()) {
            if (edge.label().isBlank()) {
                warnings.add("Relación sin etiqueta: " + edge.sourceNodeId() + " → " + edge.targetNodeId());
            }
            if (edge.loop()) {
                warnings.add("Relación circular sobre el mismo nodo: " + edge.sourceNodeId());
            }
            if (document.graphKind() == FreeGraphKind.DIRECTED && edge.direction() != FreeGraphEdgeDirection.DIRECTED) {
                warnings.add("Relación no dirigida dentro de grafo dirigido: " + edge.id());
            }
            if (document.graphKind() == FreeGraphKind.UNDIRECTED && edge.direction() != FreeGraphEdgeDirection.UNDIRECTED) {
                warnings.add("Relación dirigida dentro de grafo no dirigido: " + edge.id());
            }
        }
        return new FreeGraphValidationResult(warnings);
    }
}
