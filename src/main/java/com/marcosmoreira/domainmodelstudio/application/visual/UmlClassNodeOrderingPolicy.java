package com.marcosmoreira.domainmodelstudio.application.visual;

import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassNode;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassRelation;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlRelationKind;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/** Ordena clases dentro de un módulo priorizando abstracciones arriba y especializaciones abajo. */
final class UmlClassNodeOrderingPolicy {

    List<UmlClassNode> orderClasses(List<UmlClassNode> classes) {
        return orderClasses(classes, List.of());
    }

    List<UmlClassNode> orderClasses(List<UmlClassNode> classes, List<UmlClassRelation> relations) {
        List<UmlClassNode> safeClasses = classes == null ? List.of() : classes;
        List<UmlClassRelation> safeRelations = relations == null ? List.of() : relations;
        Map<String, Integer> inheritanceDepths = inheritanceDepths(safeClasses, safeRelations);
        Set<String> hierarchyParticipants = hierarchyParticipants(safeClasses, safeRelations);
        return safeClasses.stream()
                .sorted(Comparator
                        .comparingInt((UmlClassNode node) -> hierarchyRank(node, inheritanceDepths, hierarchyParticipants))
                        .thenComparingInt(this::roleRank)
                        .thenComparing(this::packageKey)
                        .thenComparing(node -> normalize(node.displayName()).toLowerCase(Locale.ROOT)))
                .toList();
    }

    private int hierarchyRank(UmlClassNode node, Map<String, Integer> inheritanceDepths, Set<String> participants) {
        if (!participants.contains(node.id()) && !isAbstraction(node)) {
            return 10_000;
        }
        int depth = inheritanceDepths.getOrDefault(node.id(), 0);
        return depth * 100 + abstractionRank(node);
    }

    private int abstractionRank(UmlClassNode node) {
        UmlClassKind kind = kindOf(node);
        if (kind == UmlClassKind.INTERFACE) {
            return 0;
        }
        if (kind == UmlClassKind.ABSTRACT_CLASS) {
            return 10;
        }
        if (kind == UmlClassKind.ENUM) {
            return 80;
        }
        return 50;
    }

    private Map<String, Integer> inheritanceDepths(List<UmlClassNode> classes, List<UmlClassRelation> relations) {
        Set<String> ids = new HashSet<>();
        for (UmlClassNode node : classes) {
            ids.add(node.id());
        }
        Map<String, List<String>> parentsByChild = new HashMap<>();
        for (UmlClassRelation relation : relations) {
            if (!isHierarchyRelation(relation) || !ids.contains(relation.sourceClassId()) || !ids.contains(relation.targetClassId())) {
                continue;
            }
            parentsByChild.computeIfAbsent(relation.sourceClassId(), key -> new ArrayList<>()).add(relation.targetClassId());
        }
        Map<String, Integer> memo = new HashMap<>();
        for (UmlClassNode node : classes) {
            depthOf(node.id(), parentsByChild, memo, new HashSet<>());
        }
        return memo;
    }

    private int depthOf(String classId, Map<String, List<String>> parentsByChild,
                        Map<String, Integer> memo, Set<String> visiting) {
        if (memo.containsKey(classId)) {
            return memo.get(classId);
        }
        if (!visiting.add(classId)) {
            return 0;
        }
        int depth = 0;
        for (String parentId : parentsByChild.getOrDefault(classId, List.of())) {
            depth = Math.max(depth, depthOf(parentId, parentsByChild, memo, visiting) + 1);
        }
        visiting.remove(classId);
        memo.put(classId, depth);
        return depth;
    }

    private Set<String> hierarchyParticipants(List<UmlClassNode> classes, List<UmlClassRelation> relations) {
        Set<String> ids = new HashSet<>();
        for (UmlClassNode node : classes) {
            ids.add(node.id());
        }
        Set<String> participants = new HashSet<>();
        for (UmlClassRelation relation : relations) {
            if (!isHierarchyRelation(relation)) {
                continue;
            }
            if (ids.contains(relation.sourceClassId())) {
                participants.add(relation.sourceClassId());
            }
            if (ids.contains(relation.targetClassId())) {
                participants.add(relation.targetClassId());
            }
        }
        return participants;
    }

    private boolean isHierarchyRelation(UmlClassRelation relation) {
        UmlRelationKind kind = relation == null ? null : relation.kind();
        return kind == UmlRelationKind.INHERITANCE || kind == UmlRelationKind.IMPLEMENTATION;
    }

    private boolean isAbstraction(UmlClassNode node) {
        UmlClassKind kind = kindOf(node);
        return kind == UmlClassKind.INTERFACE || kind == UmlClassKind.ABSTRACT_CLASS;
    }

    private int roleRank(UmlClassNode node) {
        UmlClassKind kind = kindOf(node);
        if (kind == UmlClassKind.CONTROLLER) return 10;
        if (kind == UmlClassKind.COMPONENT) return 15;
        if (kind == UmlClassKind.SERVICE) return 20;
        if (kind == UmlClassKind.REPOSITORY) return 30;
        if (kind == UmlClassKind.DTO) return 40;
        String text = (node.packageName() + " " + node.displayName() + " " + node.responsibility())
                .toLowerCase(Locale.ROOT);
        if (text.contains("entity") || text.contains("modelo") || text.contains("model") || text.contains("domain")) {
            return 50;
        }
        if (kind == UmlClassKind.INTERFACE) return 60;
        if (kind == UmlClassKind.ABSTRACT_CLASS) return 62;
        if (kind == UmlClassKind.ENUM) return 70;
        return 55;
    }

    private UmlClassKind kindOf(UmlClassNode node) {
        return node.kind() == null ? UmlClassKind.CLASS : node.kind();
    }

    private String packageKey(UmlClassNode node) {
        return normalize(node.packageName()).toLowerCase(Locale.ROOT);
    }

    private static String normalize(String value) {
        return value == null ? "" : value.strip();
    }
}
