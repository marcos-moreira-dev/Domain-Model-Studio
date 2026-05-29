package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import com.marcosmoreira.domainmodelstudio.application.umlclass.UmlSourceImportRenderProfile;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassMember;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassNode;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlMemberKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlModuleGroup;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlVisibility;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UmlClassVisualCostEstimatorTest {

    private final UmlClassVisualCostEstimator estimator = new UmlClassVisualCostEstimator();

    @Test
    void respectsRenderProfileWhenCountingVisibleMembers() {
        UmlClassNode largeClass = classWithMembers("pedido-service", 80, 80);

        UmlClassVisualCostEstimate light = estimator.estimate(
                List.of(module()), List.of(largeClass), List.of(), UmlSourceImportRenderProfile.LIGHT);
        UmlClassVisualCostEstimate full = estimator.estimate(
                List.of(module()), List.of(largeClass), List.of(), UmlSourceImportRenderProfile.FULL);

        assertEquals(160, light.totalMemberCount());
        assertEquals(10, light.visibleMemberCount());
        assertEquals(150, light.hiddenMemberCount());
        assertEquals(160, full.visibleMemberCount());
        assertEquals(0, full.hiddenMemberCount());
    }

    @Test
    void classifiesLargeVisibleViewsAsHeavyBeforeCanvasRender() {
        List<UmlClassNode> classes = new ArrayList<>();
        for (int index = 0; index < 260; index++) {
            classes.add(classWithMembers("clase-" + index, 0, 0));
        }

        UmlClassVisualCostEstimate estimate = estimator.estimate(
                List.of(module()), classes, List.of(), UmlSourceImportRenderProfile.LIGHT);

        assertEquals(UmlClassVisualCostLevel.HIGH, estimate.level());
        assertTrue(estimate.warns());
        assertTrue(estimate.recommendation().contains("vistas internas"));
    }

    private UmlModuleGroup module() {
        return new UmlModuleGroup("backend", "Backend", "src/main/java", "", "");
    }

    private UmlClassNode classWithMembers(String id, int attributes, int methods) {
        List<UmlClassMember> members = new ArrayList<>();
        for (int index = 0; index < attributes; index++) {
            members.add(new UmlClassMember("attr-" + index, UmlMemberKind.ATTRIBUTE, "campo" + index,
                    "String", "", UmlVisibility.PRIVATE, false, ""));
        }
        for (int index = 0; index < methods; index++) {
            members.add(new UmlClassMember("method-" + index, UmlMemberKind.METHOD, "metodo" + index,
                    "void", "metodo" + index + "()", UmlVisibility.PUBLIC, false, ""));
        }
        return new UmlClassNode(id, "backend", id, "com.acme", UmlClassKind.SERVICE,
                UmlVisibility.PUBLIC, "", "", members, "");
    }
}
