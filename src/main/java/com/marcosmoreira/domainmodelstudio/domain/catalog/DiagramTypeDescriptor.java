package com.marcosmoreira.domainmodelstudio.domain.catalog;

import java.util.Objects;

/** Descripción trazable de un tipo de diagrama dentro de la plataforma. */
public record DiagramTypeDescriptor(
        DiagramTypeId id,
        String displayName,
        DiagramCategoryId categoryId,
        DiagramSupportStatus supportStatus,
        DiagramCapabilitySet capabilities,
        DiagramWorkspaceKind workspaceKind,
        String shortDescription,
        String theoryTopicId,
        String grammarResourceId,
        String toolbarPolicyId,
        String minimalExampleResource,
        String officialExampleResource
) {

    public DiagramTypeDescriptor {
        Objects.requireNonNull(id, "id");
        displayName = requireText(displayName, "displayName");
        Objects.requireNonNull(categoryId, "categoryId");
        Objects.requireNonNull(supportStatus, "supportStatus");
        Objects.requireNonNull(capabilities, "capabilities");
        Objects.requireNonNull(workspaceKind, "workspaceKind");
        shortDescription = requireText(shortDescription, "shortDescription");
        theoryTopicId = normalizeOptional(theoryTopicId);
        grammarResourceId = normalizeOptional(grammarResourceId);
        toolbarPolicyId = normalizeOptional(toolbarPolicyId);
        minimalExampleResource = normalizeOptional(minimalExampleResource);
        officialExampleResource = normalizeOptional(officialExampleResource);
    }

    public DiagramTypeDescriptor(
            DiagramTypeId id,
            String displayName,
            DiagramCategoryId categoryId,
            DiagramSupportStatus supportStatus,
            DiagramCapabilitySet capabilities,
            String shortDescription,
            String theoryTopicId,
            String grammarResourceId
    ) {
        this(
                id,
                displayName,
                categoryId,
                supportStatus,
                capabilities,
                DiagramWorkspaceKind.PLACEHOLDER_GUIDE,
                shortDescription,
                theoryTopicId,
                grammarResourceId,
                "",
                "",
                "");
    }

    public boolean isAvailable() {
        return supportStatus == DiagramSupportStatus.AVAILABLE;
    }

    /**
     * Alias temporal para código antiguo. En nuevas reglas de producto usar {@link #isAvailable()}.
     */
    @Deprecated(forRemoval = false)
    public boolean isImplemented() {
        return isAvailable();
    }

    public boolean supports(DiagramCapability capability) {
        return capabilities.has(capability);
    }

    private static String requireText(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName);
        if (value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " no puede estar vacío.");
        }
        return value;
    }

    private static String normalizeOptional(String value) {
        return value == null ? "" : value.strip();
    }
}
