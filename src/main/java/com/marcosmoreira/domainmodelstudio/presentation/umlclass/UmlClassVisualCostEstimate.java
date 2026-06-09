package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import com.marcosmoreira.domainmodelstudio.application.umlclass.UmlSourceImportRenderProfile;

/** Estimación liviana del costo de pintar la vista UML Clases activa. */
public record UmlClassVisualCostEstimate(
        int moduleCount,
        int classCount,
        int relationCount,
        int totalMemberCount,
        int visibleMemberCount,
        int hiddenMemberCount,
        int estimatedCanvasElements,
        UmlSourceImportRenderProfile renderProfile,
        UmlClassVisualCostLevel level
) {
    public UmlClassVisualCostEstimate {
        moduleCount = Math.max(0, moduleCount);
        classCount = Math.max(0, classCount);
        relationCount = Math.max(0, relationCount);
        totalMemberCount = Math.max(0, totalMemberCount);
        visibleMemberCount = Math.max(0, visibleMemberCount);
        hiddenMemberCount = Math.max(0, hiddenMemberCount);
        estimatedCanvasElements = Math.max(0, estimatedCanvasElements);
        renderProfile = renderProfile == null ? UmlSourceImportRenderProfile.safeDefault() : renderProfile;
        level = level == null ? UmlClassVisualCostLevel.LOW : level;
    }

    public static UmlClassVisualCostEstimate empty(UmlSourceImportRenderProfile renderProfile) {
        return new UmlClassVisualCostEstimate(0, 0, 0, 0, 0, 0, 0,
                renderProfile == null ? UmlSourceImportRenderProfile.safeDefault() : renderProfile,
                UmlClassVisualCostLevel.LOW);
    }

    public boolean warns() {
        return level.warns();
    }

    public String shortSummary() {
        return level.displayName() + " · " + classCount + " clases, " + relationCount
                + " relaciones, " + visibleMemberCount + " miembros visibles"
                + (hiddenMemberCount > 0 ? " (" + hiddenMemberCount + " ocultos)" : "")
                + " · perfil " + renderProfile.displayName();
    }

    public String recommendation() {
        return switch (level) {
            case LOW -> "Vista manejable para render normal.";
            case MODERATE -> "Vista moderada: usa filtros si el equipo se siente lento.";
            case HIGH -> "Vista pesada: conviene trabajar por vistas internas o filtros antes de exportar.";
            case CRITICAL -> "Vista crítica: evita Mega vista completa; usa Resumen, filtros o vistas por módulo.";
        };
    }
}
