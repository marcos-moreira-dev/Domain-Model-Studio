package com.marcosmoreira.domainmodelstudio.application.umlclass;

import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeMemberKind;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeProject;
import com.marcosmoreira.domainmodelstudio.application.sourcecode.ParsedCodeType;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassDiagramDocument;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassMember;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassNode;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlMemberKind;
import java.util.Objects;

/**
 * Decide un perfil visual inicial para UML generado desde código fuente.
 *
 * <p>Esta política no renderiza ni modifica documentos. Solo concentra la regla de
 * producto: proyectos grandes se abren en modo ligero para evitar congelamientos.</p>
 */
public final class UmlSourceImportRenderProfilePolicy {

    private static final int LARGE_CLASS_COUNT = 180;
    private static final int HUGE_CLASS_COUNT = 400;
    private static final int LARGE_MEMBER_COUNT = 1200;
    private static final int HUGE_MEMBER_COUNT = 3500;
    private static final int LARGE_RELATION_COUNT = 500;

    public UmlSourceImportRenderProfileRecommendation recommend(ParsedCodeProject project) {
        Objects.requireNonNull(project, "project");
        int attributes = 0;
        int methods = 0;
        for (ParsedCodeType type : project.types()) {
            attributes += (int) type.members().stream()
                    .filter(member -> member.kind() == ParsedCodeMemberKind.FIELD || member.kind() == ParsedCodeMemberKind.PROPERTY)
                    .count();
            methods += (int) type.members().stream()
                    .filter(member -> member.kind() == ParsedCodeMemberKind.METHOD
                            || member.kind() == ParsedCodeMemberKind.CONSTRUCTOR)
                    .count();
        }
        return recommend(project.types().size(), attributes, methods, project.relations().size());
    }

    public UmlSourceImportRenderProfileRecommendation recommend(UmlClassDiagramDocument document) {
        Objects.requireNonNull(document, "document");
        int attributes = 0;
        int methods = 0;
        for (UmlClassNode node : document.classes()) {
            attributes += countMembers(node, UmlMemberKind.ATTRIBUTE);
            methods += countMembers(node, UmlMemberKind.METHOD) + countMembers(node, UmlMemberKind.CONSTRUCTOR);
        }
        return recommend(document.classes().size(), attributes, methods, document.relations().size());
    }

    public UmlSourceImportRenderProfileRecommendation recommend(int classCount, int attributeCount,
                                                               int methodCount, int relationCount) {
        int members = Math.max(0, attributeCount) + Math.max(0, methodCount);
        UmlSourceImportRenderProfile profile;
        String reason;
        if (classCount >= HUGE_CLASS_COUNT || members >= HUGE_MEMBER_COUNT) {
            profile = UmlSourceImportRenderProfile.LIGHT;
            reason = "Proyecto grande: se recomienda abrir en modo ligero.";
        } else if (classCount >= LARGE_CLASS_COUNT || members >= LARGE_MEMBER_COUNT || relationCount >= LARGE_RELATION_COUNT) {
            profile = UmlSourceImportRenderProfile.MEDIUM;
            reason = "Proyecto mediano/grande: se recomienda limitar detalle visible.";
        } else {
            profile = UmlSourceImportRenderProfile.DETAILED;
            reason = "Proyecto manejable: se puede abrir con detalle moderado.";
        }
        return new UmlSourceImportRenderProfileRecommendation(profile, reason, classCount, attributeCount, methodCount, relationCount);
    }

    private int countMembers(UmlClassNode node, UmlMemberKind kind) {
        int count = 0;
        for (UmlClassMember member : node.members()) {
            if (member.kind() == kind) {
                count++;
            }
        }
        return count;
    }
}
