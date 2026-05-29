package com.marcosmoreira.domainmodelstudio.application.umlclass;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassMember;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassNode;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlMemberKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlVisibility;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class UmlSourceImportRenderProfilePolicyTest {

    private final UmlSourceImportRenderProfilePolicy policy = new UmlSourceImportRenderProfilePolicy();

    @Test
    void shouldRecommendDetailedForSmallProjects() {
        var recommendation = policy.recommend(12, 40, 60, 20);

        assertEquals(UmlSourceImportRenderProfile.DETAILED, recommendation.profile());
        assertTrue(recommendation.reason().contains("manejable"));
    }

    @Test
    void shouldRecommendMediumForLargeMemberCounts() {
        var recommendation = policy.recommend(90, 700, 650, 120);

        assertEquals(UmlSourceImportRenderProfile.MEDIUM, recommendation.profile());
        assertEquals(10, recommendation.profile().maxVisibleAttributes());
        assertEquals(10, recommendation.profile().maxVisibleMethods());
    }


    @Test
    void shouldRecommendLightFromCompleteUmlDocument() {
        ArrayList<UmlClassNode> classes = new ArrayList<>();
        for (int classIndex = 0; classIndex < 420; classIndex++) {
            classes.add(new UmlClassNode("clase_" + classIndex, "", "Clase" + classIndex, "com.acme",
                    UmlClassKind.CLASS, UmlVisibility.PUBLIC, "", "", members(10), ""));
        }
        UmlClassDiagramDocument document = new UmlClassDiagramDocument(
                "Grande", "borrador", LocalDate.of(2026, 5, 17),
                List.of(), classes, List.of(), "");

        var recommendation = policy.recommend(document);

        assertEquals(UmlSourceImportRenderProfile.LIGHT, recommendation.profile());
        assertEquals(420, recommendation.classCount());
        assertEquals(4200, recommendation.attributeCount());
        assertEquals(4200, recommendation.methodCount());
    }

    @Test
    void shouldRecommendLightForHugeProjects() {
        var recommendation = policy.recommend(450, 2200, 1900, 900);

        assertEquals(UmlSourceImportRenderProfile.LIGHT, recommendation.profile());
        assertEquals(5, recommendation.profile().maxVisibleAttributes());
        assertEquals(5, recommendation.profile().maxVisibleMethods());
    }
    private static List<UmlClassMember> members(int pairs) {
        ArrayList<UmlClassMember> members = new ArrayList<>();
        for (int index = 0; index < pairs; index++) {
            members.add(new UmlClassMember("attr_" + index, UmlMemberKind.ATTRIBUTE, "campo" + index,
                    "String", "", UmlVisibility.PRIVATE, false, ""));
            members.add(new UmlClassMember("met_" + index, UmlMemberKind.METHOD, "operacion" + index,
                    "void", "operacion" + index + "()", UmlVisibility.PUBLIC, false, ""));
        }
        return members;
    }
}
