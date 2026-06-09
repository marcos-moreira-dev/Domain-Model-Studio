package com.marcosmoreira.domainmodelstudio.application.umlclass;

/** Resultado de recomendar un perfil de render para una importación UML desde código. */
public record UmlSourceImportRenderProfileRecommendation(
        UmlSourceImportRenderProfile profile,
        String reason,
        int classCount,
        int attributeCount,
        int methodCount,
        int relationCount
) {
    public UmlSourceImportRenderProfileRecommendation {
        profile = profile == null ? UmlSourceImportRenderProfile.safeDefault() : profile;
        reason = reason == null ? "" : reason.strip();
        classCount = Math.max(0, classCount);
        attributeCount = Math.max(0, attributeCount);
        methodCount = Math.max(0, methodCount);
        relationCount = Math.max(0, relationCount);
    }
}
