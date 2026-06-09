package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import com.marcosmoreira.domainmodelstudio.application.umlclass.UmlSourceImportRenderProfile;
import com.marcosmoreira.domainmodelstudio.application.visual.UmlClassBoxMetricsCalculator;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassMember;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlMemberKind;
import java.util.List;

/** Decide cuántos miembros UML se muestran sin saturar visualmente una caja. */
public final class UmlClassMemberRenderPolicy {

    private final UmlSourceImportRenderProfile profile;

    public UmlClassMemberRenderPolicy() {
        this(UmlSourceImportRenderProfile.safeDefault());
    }

    public UmlClassMemberRenderPolicy(UmlSourceImportRenderProfile profile) {
        this.profile = profile == null ? UmlSourceImportRenderProfile.safeDefault() : profile;
    }

    public List<UmlClassMember> visibleMembers(List<UmlClassMember> members) {
        List<UmlClassMember> safe = members == null ? List.of() : members;
        int limit = maxVisibleMembersFor(safe);
        if (limit == Integer.MAX_VALUE) {
            return safe;
        }
        return safe.stream().limit(limit).toList();
    }

    public int hiddenCount(List<UmlClassMember> members) {
        List<UmlClassMember> safe = members == null ? List.of() : members;
        int limit = maxVisibleMembersFor(safe);
        if (limit == Integer.MAX_VALUE) {
            return 0;
        }
        return Math.max(0, safe.size() - limit);
    }

    public UmlSourceImportRenderProfile profile() {
        return profile;
    }

    private int maxVisibleMembersFor(List<UmlClassMember> members) {
        UmlMemberKind sectionKind = members.stream().findFirst().map(UmlClassMember::kind).orElse(UmlMemberKind.ATTRIBUTE);
        if (sectionKind == UmlMemberKind.METHOD || sectionKind == UmlMemberKind.CONSTRUCTOR) {
            return profile.showMethods() ? profile.maxVisibleMethods() : 0;
        }
        return profile.showAttributes() ? profile.maxVisibleAttributes() : 0;
    }

    /** Límite histórico usado por vistas UML no generadas desde código. */
    public static int legacyDefaultLimit() {
        return UmlClassBoxMetricsCalculator.MAX_VISIBLE_MEMBERS_PER_SECTION;
    }
}
