package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramView;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassNode;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassRelation;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlModuleGroup;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/** Aplica búsqueda, vista interna y filtros de tipo sin modificar el documento UML original. */
final class UmlClassDiagramFilterEngine {

    UmlClassDiagramFilterResult apply(UmlClassDiagramDocument document, UmlClassDiagramFilterState state) {
        return apply(document, state, UmlClassSearchIndex.from(document));
    }

    UmlClassDiagramFilterResult apply(
            UmlClassDiagramDocument document,
            UmlClassDiagramFilterState state,
            UmlClassSearchIndex searchIndex
    ) {
        if (document == null) {
            return UmlClassDiagramFilterResult.empty();
        }
        UmlClassDiagramFilterState effectiveState = state == null ? UmlClassDiagramFilterState.all() : state;
        UmlClassSearchIndex effectiveIndex = searchIndex == null ? UmlClassSearchIndex.from(document) : searchIndex;
        List<UmlModuleGroup> baseModules = baseModules(document, effectiveState);
        List<UmlClassNode> baseClasses = baseClasses(document, effectiveState);
        List<UmlClassRelation> baseRelations = baseRelations(document, effectiveState);

        List<UmlClassNode> filteredClasses = baseClasses.stream()
                .filter(node -> matchesClassKind(node, effectiveState))
                .filter(node -> matchesSearch(effectiveIndex, node, effectiveState))
                .toList();
        Set<String> classIds = filteredClasses.stream().map(UmlClassNode::id)
                .collect(java.util.stream.Collectors.toCollection(LinkedHashSet::new));
        List<UmlClassRelation> candidateRelations = baseRelations.stream()
                .filter(relation -> matchesRelationKind(relation, effectiveState))
                .filter(relation -> matchesSearch(effectiveIndex, relation, effectiveState) || !effectiveState.hasSearchQuery())
                .toList();
        if (effectiveState.hasSearchQuery()) {
            classIds.addAll(relationClassIds(candidateRelations));
            filteredClasses = baseClasses.stream()
                    .filter(node -> classIds.contains(node.id()))
                    .filter(node -> matchesClassKind(node, effectiveState))
                    .toList();
        } else if (effectiveState.hasRelationKindFilter()) {
            Set<String> relationEndpoints = relationClassIds(candidateRelations);
            filteredClasses = filteredClasses.stream()
                    .filter(node -> relationEndpoints.contains(node.id()))
                    .toList();
        }
        Set<String> visibleClassIds = filteredClasses.stream().map(UmlClassNode::id)
                .collect(java.util.stream.Collectors.toCollection(LinkedHashSet::new));
        List<UmlClassRelation> filteredRelations = candidateRelations.stream()
                .filter(relation -> visibleClassIds.contains(relation.sourceClassId()))
                .filter(relation -> visibleClassIds.contains(relation.targetClassId()))
                .toList();
        Set<String> visibleModuleIds = filteredClasses.stream().map(UmlClassNode::moduleId)
                .collect(java.util.stream.Collectors.toCollection(LinkedHashSet::new));
        List<UmlModuleGroup> filteredModules = baseModules.stream()
                .filter(module -> visibleModuleIds.contains(module.id()) || matchesSearch(effectiveIndex, module, effectiveState))
                .toList();
        return new UmlClassDiagramFilterResult(filteredModules, filteredClasses, filteredRelations);
    }

    private List<UmlModuleGroup> baseModules(UmlClassDiagramDocument document, UmlClassDiagramFilterState state) {
        return activeView(document, state)
                .map(view -> {
                    if (view.isGlobal() || !view.moduleIds().isEmpty()) {
                        return document.modules().stream().filter(view::includesModule).toList();
                    }
                    if (!view.classIds().isEmpty() || !view.relationIds().isEmpty()) {
                        return document.modules();
                    }
                    return List.<UmlModuleGroup>of();
                })
                .orElse(document.modules());
    }

    private List<UmlClassNode> baseClasses(UmlClassDiagramDocument document, UmlClassDiagramFilterState state) {
        return activeView(document, state).map(view -> document.classes().stream().filter(view::includesClass).toList())
                .orElse(document.classes());
    }

    private List<UmlClassRelation> baseRelations(UmlClassDiagramDocument document, UmlClassDiagramFilterState state) {
        return activeView(document, state).map(view -> document.relations().stream().filter(view::includesRelation).toList())
                .orElse(document.relations());
    }

    private java.util.Optional<UmlClassDiagramView> activeView(UmlClassDiagramDocument document, UmlClassDiagramFilterState state) {
        return state.hasViewFilter() ? document.viewById(state.viewId()) : java.util.Optional.empty();
    }

    private boolean matchesClassKind(UmlClassNode node, UmlClassDiagramFilterState state) {
        return !state.hasClassKindFilter() || node.kind() == state.classKind();
    }

    private boolean matchesRelationKind(UmlClassRelation relation, UmlClassDiagramFilterState state) {
        return !state.hasRelationKindFilter() || relation.kind() == state.relationKind();
    }

    private boolean matchesSearch(UmlClassSearchIndex searchIndex, UmlClassNode node, UmlClassDiagramFilterState state) {
        return !state.hasSearchQuery() || searchIndex.matchesClass(state.searchQuery(), node);
    }

    private boolean matchesSearch(UmlClassSearchIndex searchIndex, UmlClassRelation relation, UmlClassDiagramFilterState state) {
        return !state.hasSearchQuery() || searchIndex.matchesRelation(state.searchQuery(), relation);
    }

    private boolean matchesSearch(UmlClassSearchIndex searchIndex, UmlModuleGroup module, UmlClassDiagramFilterState state) {
        return state.hasSearchQuery() && searchIndex.matchesModule(state.searchQuery(), module);
    }

    private Set<String> relationClassIds(List<UmlClassRelation> relations) {
        LinkedHashSet<String> result = new LinkedHashSet<>();
        for (UmlClassRelation relation : relations) {
            result.add(relation.sourceClassId());
            result.add(relation.targetClassId());
        }
        return result;
    }
}
