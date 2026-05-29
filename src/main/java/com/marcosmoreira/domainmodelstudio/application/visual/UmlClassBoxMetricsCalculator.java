package com.marcosmoreira.domainmodelstudio.application.visual;

import com.marcosmoreira.domainmodelstudio.application.umlclass.UmlSourceImportRenderProfile;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassMember;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassNode;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlMemberKind;
import java.util.Objects;

/**
 * Calcula el tamaño mínimo profesional de una caja UML según su contenido.
 *
 * <p>No usa JavaFX ni mide fuentes reales. Aplica una métrica conservadora y
 * determinista para que el layout inicial, la exportación y la UI compartan la
 * misma idea: una clase con más atributos/métodos necesita más espacio.</p>
 */
public final class UmlClassBoxMetricsCalculator {

    public static final int MAX_VISIBLE_MEMBERS_PER_SECTION = 25;

    private static final double MIN_WIDTH = 220.0;
    private static final double MAX_WIDTH = 560.0;
    private static final double HEADER_HEIGHT = 52.0;
    private static final double SECTION_HEADER_HEIGHT = 20.0;
    private static final double MEMBER_LINE_HEIGHT = 16.0;
    private static final double VERTICAL_PADDING = 18.0;
    private static final double HORIZONTAL_PADDING = 44.0;
    private static final double APPROX_CHAR_WIDTH = 6.1;

    public UmlClassBoxMetrics calculate(UmlClassNode node) {
        return calculate(node, UmlSourceImportRenderProfile.safeDefault());
    }

    public UmlClassBoxMetrics calculate(UmlClassNode node, UmlSourceImportRenderProfile profile) {
        Objects.requireNonNull(node, "node");
        UmlSourceImportRenderProfile safeProfile = profile == null ? UmlSourceImportRenderProfile.safeDefault() : profile;
        int attributeCount = memberCount(node, UmlMemberKind.ATTRIBUTE);
        int methodCount = memberCount(node, UmlMemberKind.METHOD);
        int visibleAttributes = visibleCount(attributeCount, safeProfile.maxVisibleAttributes(), safeProfile.showAttributes());
        int visibleMethods = visibleCount(methodCount, safeProfile.maxVisibleMethods(), safeProfile.showMethods());
        double width = Math.max(MIN_WIDTH, longestDisplayLine(node, safeProfile) * APPROX_CHAR_WIDTH + HORIZONTAL_PADDING);
        double height = HEADER_HEIGHT
                + sectionHeight(attributeCount, visibleAttributes)
                + sectionHeight(methodCount, visibleMethods)
                + VERTICAL_PADDING;
        return new UmlClassBoxMetrics(Math.min(MAX_WIDTH, width), height, attributeCount, methodCount);
    }

    private static double sectionHeight(int totalCount, int visibleCount) {
        if (totalCount <= 0) {
            return SECTION_HEADER_HEIGHT + MEMBER_LINE_HEIGHT;
        }
        boolean overflow = totalCount > visibleCount;
        return SECTION_HEADER_HEIGHT + visibleCount * MEMBER_LINE_HEIGHT + (overflow ? MEMBER_LINE_HEIGHT : 0.0);
    }

    private static int visibleCount(int totalCount, int limit, boolean visible) {
        if (!visible || totalCount <= 0) {
            return 0;
        }
        if (limit == Integer.MAX_VALUE) {
            return totalCount;
        }
        return Math.min(Math.max(0, totalCount), Math.max(0, limit));
    }

    private static int memberCount(UmlClassNode node, UmlMemberKind kind) {
        return (int) node.members().stream().filter(member -> member.kind() == kind).count();
    }

    private static int longestDisplayLine(UmlClassNode node, UmlSourceImportRenderProfile profile) {
        int longest = Math.max(node.displayName().length(), node.kind().displayName().length() + 2);
        if (!node.packageName().isBlank()) {
            longest = Math.max(longest, node.packageName().length());
        }
        if (!node.responsibility().isBlank()) {
            longest = Math.max(longest, Math.min(node.responsibility().length(), 52));
        }
        longest = Math.max(longest, longestVisibleMemberLine(node, UmlMemberKind.ATTRIBUTE, profile.maxVisibleAttributes(), profile.showAttributes()));
        longest = Math.max(longest, longestVisibleMemberLine(node, UmlMemberKind.METHOD, profile.maxVisibleMethods(), profile.showMethods()));
        return longest;
    }

    private static int longestVisibleMemberLine(UmlClassNode node, UmlMemberKind kind, int limit, boolean visible) {
        if (!visible || limit == 0) {
            return 0;
        }
        int consumed = 0;
        int longest = 0;
        for (UmlClassMember member : node.members()) {
            if (member.kind() != kind) {
                continue;
            }
            if (limit != Integer.MAX_VALUE && consumed >= limit) {
                break;
            }
            longest = Math.max(longest, safeDisplayText(member).length());
            consumed++;
        }
        return longest;
    }

    private static String safeDisplayText(UmlClassMember member) {
        String text = member == null ? "" : member.displayText();
        return text == null ? "" : text.strip();
    }
}
