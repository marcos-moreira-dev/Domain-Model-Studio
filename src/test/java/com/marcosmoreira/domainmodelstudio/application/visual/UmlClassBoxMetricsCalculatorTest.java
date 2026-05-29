package com.marcosmoreira.domainmodelstudio.application.visual;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.umlclass.UmlSourceImportRenderProfile;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassMember;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassNode;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlMemberKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlVisibility;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class UmlClassBoxMetricsCalculatorTest {

    private final UmlClassBoxMetricsCalculator calculator = new UmlClassBoxMetricsCalculator();

    @Test
    void classBoxGrowsWhenMembersGrow() {
        UmlClassNode small = node(List.of(attribute("id")));
        UmlClassNode large = node(List.of(
                attribute("id"), attribute("nombres"), attribute("apellidos"),
                method("registrarMatricula()"), method("validarEstado()"), method("generarResumenAcademico()")));

        UmlClassBoxMetrics smallMetrics = calculator.calculate(small);
        UmlClassBoxMetrics largeMetrics = calculator.calculate(large);

        assertTrue(largeMetrics.height() > smallMetrics.height());
        assertTrue(largeMetrics.width() >= smallMetrics.width());
    }


    @Test
    void classBoxHeightRespectsRenderProfile() {
        UmlClassNode node = node(manyMembers(40));

        UmlClassBoxMetrics light = calculator.calculate(node, UmlSourceImportRenderProfile.LIGHT);
        UmlClassBoxMetrics detailed = calculator.calculate(node, UmlSourceImportRenderProfile.DETAILED);
        UmlClassBoxMetrics full = calculator.calculate(node, UmlSourceImportRenderProfile.FULL);

        assertTrue(light.height() < detailed.height());
        assertTrue(detailed.height() < full.height());
        assertEquals(40, full.attributeCount());
        assertEquals(40, full.methodCount());
    }

    private static UmlClassNode node(List<UmlClassMember> members) {
        return new UmlClassNode("estudiante", "academico", "Estudiante", "uens.academico",
                UmlClassKind.CLASS, UmlVisibility.PUBLIC, "Persona matriculada", "", members, "");
    }


    private static List<UmlClassMember> manyMembers(int pairs) {
        ArrayList<UmlClassMember> members = new ArrayList<>();
        for (int index = 0; index < pairs; index++) {
            members.add(attribute("campo" + index));
            members.add(method("operacion" + index + "()"));
        }
        return members;
    }

    private static UmlClassMember attribute(String name) {
        return new UmlClassMember("attr-" + name, UmlMemberKind.ATTRIBUTE, name, "String", "",
                UmlVisibility.PRIVATE, false, "");
    }

    private static UmlClassMember method(String signature) {
        return new UmlClassMember("method-" + signature.replaceAll("\\W+", "-"), UmlMemberKind.METHOD, "", "", signature,
                UmlVisibility.PUBLIC, false, "");
    }
}
