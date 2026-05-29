package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import com.marcosmoreira.domainmodelstudio.application.umlclass.UmlSourceImportRenderProfile;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassMember;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassNode;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassRelation;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlMemberKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlModuleGroup;
import java.util.List;

/** Calcula un costo aproximado antes de que el lienzo construya todos los nodos JavaFX visibles. */
final class UmlClassVisualCostEstimator {

    private static final int MODERATE_CLASS_COUNT = 120;
    private static final int HIGH_CLASS_COUNT = 250;
    private static final int CRITICAL_CLASS_COUNT = 600;
    private static final int MODERATE_RELATION_COUNT = 180;
    private static final int HIGH_RELATION_COUNT = 500;
    private static final int CRITICAL_RELATION_COUNT = 1200;
    private static final int MODERATE_VISIBLE_MEMBER_COUNT = 1000;
    private static final int HIGH_VISIBLE_MEMBER_COUNT = 2500;
    private static final int CRITICAL_VISIBLE_MEMBER_COUNT = 5000;
    private static final int MODERATE_CANVAS_ELEMENTS = 350;
    private static final int HIGH_CANVAS_ELEMENTS = 800;
    private static final int CRITICAL_CANVAS_ELEMENTS = 1800;

    UmlClassVisualCostEstimate estimate(
            List<UmlModuleGroup> modules,
            List<UmlClassNode> classes,
            List<UmlClassRelation> relations,
            UmlSourceImportRenderProfile renderProfile
    ) {
        List<UmlModuleGroup> safeModules = modules == null ? List.of() : modules;
        List<UmlClassNode> safeClasses = classes == null ? List.of() : classes;
        List<UmlClassRelation> safeRelations = relations == null ? List.of() : relations;
        UmlSourceImportRenderProfile profile = renderProfile == null
                ? UmlSourceImportRenderProfile.safeDefault()
                : renderProfile;

        int totalMembers = 0;
        int visibleMembers = 0;
        for (UmlClassNode node : safeClasses) {
            int attributes = countMembers(node, UmlMemberKind.ATTRIBUTE);
            int methods = countMembers(node, UmlMemberKind.METHOD);
            totalMembers += attributes + methods;
            visibleMembers += visibleCount(attributes, profile.maxVisibleAttributes(), profile.showAttributes());
            visibleMembers += visibleCount(methods, profile.maxVisibleMethods(), profile.showMethods());
        }
        int hiddenMembers = Math.max(0, totalMembers - visibleMembers);
        int canvasElements = safeModules.size() + safeClasses.size() + safeRelations.size() + visibleMembers;
        UmlClassVisualCostLevel level = classify(safeClasses.size(), safeRelations.size(), visibleMembers, canvasElements);
        return new UmlClassVisualCostEstimate(
                safeModules.size(),
                safeClasses.size(),
                safeRelations.size(),
                totalMembers,
                visibleMembers,
                hiddenMembers,
                canvasElements,
                profile,
                level
        );
    }

    private static UmlClassVisualCostLevel classify(int classCount, int relationCount, int visibleMembers, int canvasElements) {
        if (classCount >= CRITICAL_CLASS_COUNT
                || relationCount >= CRITICAL_RELATION_COUNT
                || visibleMembers >= CRITICAL_VISIBLE_MEMBER_COUNT
                || canvasElements >= CRITICAL_CANVAS_ELEMENTS) {
            return UmlClassVisualCostLevel.CRITICAL;
        }
        if (classCount >= HIGH_CLASS_COUNT
                || relationCount >= HIGH_RELATION_COUNT
                || visibleMembers >= HIGH_VISIBLE_MEMBER_COUNT
                || canvasElements >= HIGH_CANVAS_ELEMENTS) {
            return UmlClassVisualCostLevel.HIGH;
        }
        if (classCount >= MODERATE_CLASS_COUNT
                || relationCount >= MODERATE_RELATION_COUNT
                || visibleMembers >= MODERATE_VISIBLE_MEMBER_COUNT
                || canvasElements >= MODERATE_CANVAS_ELEMENTS) {
            return UmlClassVisualCostLevel.MODERATE;
        }
        return UmlClassVisualCostLevel.LOW;
    }

    private static int visibleCount(int total, int limit, boolean visible) {
        if (!visible || total <= 0) {
            return 0;
        }
        if (limit == Integer.MAX_VALUE) {
            return total;
        }
        return Math.min(total, Math.max(0, limit));
    }

    private static int countMembers(UmlClassNode node, UmlMemberKind kind) {
        if (node == null) {
            return 0;
        }
        int count = 0;
        for (UmlClassMember member : node.members()) {
            if (member.kind() == kind) {
                count++;
            }
        }
        return count;
    }
}
