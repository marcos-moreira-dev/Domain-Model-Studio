package com.marcosmoreira.domainmodelstudio.application.layout;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramModel;
import com.marcosmoreira.domainmodelstudio.domain.er.EntityElement;
import com.marcosmoreira.domainmodelstudio.domain.er.RelationshipElement;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;

/** Ordena entidades conectadas cerca para mejorar el primer render. */
final class ConceptualEntityOrdering {

    List<EntityElement> order(DiagramModel model) {
        Objects.requireNonNull(model, "model");
        Map<DiagramElementId, EntityElement> entitiesById = new LinkedHashMap<>();
        for (EntityElement entity : model.entities()) {
            entitiesById.put(entity.id(), entity);
        }
        Map<DiagramElementId, Set<DiagramElementId>> adjacency = adjacency(model);
        List<EntityElement> ordered = new ArrayList<>();
        Set<DiagramElementId> visited = new HashSet<>();

        while (ordered.size() < entitiesById.size()) {
            Optional<EntityElement> seed = entitiesById.values().stream()
                    .filter(entity -> !visited.contains(entity.id()))
                    .max(Comparator
                            .comparingInt((EntityElement entity) -> adjacency.getOrDefault(entity.id(), Set.of()).size())
                            .thenComparing(EntityElement::name));
            if (seed.isEmpty()) {
                break;
            }
            traverse(seed.get(), entitiesById, adjacency, visited, ordered);
        }
        return ordered;
    }

    private Map<DiagramElementId, Set<DiagramElementId>> adjacency(DiagramModel model) {
        Map<DiagramElementId, Set<DiagramElementId>> adjacency = new LinkedHashMap<>();
        for (EntityElement entity : model.entities()) {
            adjacency.put(entity.id(), new LinkedHashSet<>());
        }
        for (RelationshipElement relationship : model.relationships()) {
            adjacency.computeIfAbsent(relationship.fromEntityId(), ignored -> new LinkedHashSet<>()).add(relationship.toEntityId());
            adjacency.computeIfAbsent(relationship.toEntityId(), ignored -> new LinkedHashSet<>()).add(relationship.fromEntityId());
        }
        return adjacency;
    }

    private void traverse(
            EntityElement seed,
            Map<DiagramElementId, EntityElement> entitiesById,
            Map<DiagramElementId, Set<DiagramElementId>> adjacency,
            Set<DiagramElementId> visited,
            List<EntityElement> ordered
    ) {
        Queue<EntityElement> queue = new ArrayDeque<>();
        queue.add(seed);
        visited.add(seed.id());
        while (!queue.isEmpty()) {
            EntityElement current = queue.poll();
            ordered.add(current);
            adjacency.getOrDefault(current.id(), Set.of()).stream()
                    .map(entitiesById::get)
                    .filter(Objects::nonNull)
                    .filter(entity -> !visited.contains(entity.id()))
                    .sorted(Comparator.comparing(EntityElement::name))
                    .forEach(entity -> {
                        visited.add(entity.id());
                        queue.add(entity);
                    });
        }
    }
}
