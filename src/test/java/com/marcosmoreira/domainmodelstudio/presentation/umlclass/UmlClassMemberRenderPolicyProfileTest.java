package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.marcosmoreira.domainmodelstudio.application.umlclass.UmlSourceImportRenderProfile;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassMember;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlMemberKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlVisibility;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class UmlClassMemberRenderPolicyProfileTest {

    @Test
    void shouldLimitVisibleMembersWithLightProfile() {
        var policy = new UmlClassMemberRenderPolicy(UmlSourceImportRenderProfile.LIGHT);
        var attributes = attributes(12);

        assertEquals(5, policy.visibleMembers(attributes).size());
        assertEquals(7, policy.hiddenCount(attributes));
    }

    @Test
    void shouldLimitVisibleMembersWithDetailedProfile() {
        var policy = new UmlClassMemberRenderPolicy(UmlSourceImportRenderProfile.DETAILED);
        var methods = methods(40);

        assertEquals(25, policy.visibleMembers(methods).size());
        assertEquals(15, policy.hiddenCount(methods));
    }

    private List<UmlClassMember> attributes(int count) {
        ArrayList<UmlClassMember> members = new ArrayList<>();
        for (int index = 0; index < count; index++) {
            members.add(new UmlClassMember("attr-" + index, UmlMemberKind.ATTRIBUTE, "campo" + index,
                    "String", "", UmlVisibility.PRIVATE, false, ""));
        }
        return members;
    }

    private List<UmlClassMember> methods(int count) {
        ArrayList<UmlClassMember> members = new ArrayList<>();
        for (int index = 0; index < count; index++) {
            members.add(new UmlClassMember("met-" + index, UmlMemberKind.METHOD, "metodo" + index,
                    "void", "metodo" + index + "()", UmlVisibility.PUBLIC, false, ""));
        }
        return members;
    }
}
