package com.marcosmoreira.domainmodelstudio.application.visual;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/** Ordena identificadores conectados preservando una lectura izquierda-derecha de dependencias. */
final class DependencyDrivenOrderingPolicy {

    List<String> order(List<String> candidates, Collection<DirectedDependency> dependencies) {
        Objects.requireNonNull(candidates, "candidates");
        Objects.requireNonNull(dependencies, "dependencies");
        Map<String, Integer> originalIndex = originalIndex(candidates);
        Map<String, Set<String>> outgoing = new LinkedHashMap<>();
        Map<String, Integer> incoming = new LinkedHashMap<>();
        for (String candidate : candidates) {
            outgoing.put(candidate, new LinkedHashSet<>());
            incoming.put(candidate, 0);
        }
        for (DirectedDependency dependency : dependencies) {
            if (!outgoing.containsKey(dependency.source()) || !outgoing.containsKey(dependency.target())) {
                continue;
            }
            if (dependency.source().equals(dependency.target())) {
                continue;
            }
            if (outgoing.get(dependency.source()).add(dependency.target())) {
                incoming.put(dependency.target(), incoming.get(dependency.target()) + 1);
            }
        }
        ArrayDeque<String> ready = new ArrayDeque<>(candidates.stream()
                .filter(candidate -> incoming.get(candidate) == 0)
                .sorted((left, right) -> compareByConnectivity(left, right, outgoing, incoming, originalIndex))
                .toList());
        List<String> ordered = new ArrayList<>();
        while (!ready.isEmpty()) {
            String current = ready.removeFirst();
            ordered.add(current);
            List<String> targets = outgoing.get(current).stream()
                    .sorted((left, right) -> compareByConnectivity(left, right, outgoing, incoming, originalIndex))
                    .toList();
            for (String target : targets) {
                int updatedIncoming = incoming.get(target) - 1;
                incoming.put(target, updatedIncoming);
                if (updatedIncoming == 0) {
                    insertReady(ready, target, outgoing, incoming, originalIndex);
                }
            }
        }
        if (ordered.size() == candidates.size()) {
            return ordered;
        }
        candidates.stream()
                .filter(candidate -> !ordered.contains(candidate))
                .sorted((left, right) -> compareByConnectivity(left, right, outgoing, incoming, originalIndex))
                .forEach(ordered::add);
        return ordered;
    }

    private static void insertReady(ArrayDeque<String> ready, String value, Map<String, Set<String>> outgoing,
                                    Map<String, Integer> incoming, Map<String, Integer> originalIndex) {
        List<String> values = new ArrayList<>(ready);
        values.add(value);
        values.sort((left, right) -> compareByConnectivity(left, right, outgoing, incoming, originalIndex));
        ready.clear();
        ready.addAll(values);
    }

    private static int compareByConnectivity(String left, String right, Map<String, Set<String>> outgoing,
                                             Map<String, Integer> incoming, Map<String, Integer> originalIndex) {
        int leftDegree = outgoing.getOrDefault(left, Set.of()).size() + incoming.getOrDefault(left, 0);
        int rightDegree = outgoing.getOrDefault(right, Set.of()).size() + incoming.getOrDefault(right, 0);
        int degreeComparison = Integer.compare(rightDegree, leftDegree);
        if (degreeComparison != 0) {
            return degreeComparison;
        }
        return Integer.compare(originalIndex.getOrDefault(left, Integer.MAX_VALUE),
                originalIndex.getOrDefault(right, Integer.MAX_VALUE));
    }

    private static Map<String, Integer> originalIndex(List<String> candidates) {
        Map<String, Integer> indexes = new HashMap<>();
        int index = 0;
        for (String candidate : candidates) {
            indexes.put(candidate, index++);
        }
        return indexes;
    }

    record DirectedDependency(String source, String target) {
        DirectedDependency {
            source = normalize(source);
            target = normalize(target);
        }

        private static String normalize(String value) {
            return value == null ? "" : value.strip();
        }
    }
}
