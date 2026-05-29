package com.marcosmoreira.domainmodelstudio.application.catalog;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCapabilitySet;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCategoryId;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramSupportStatus;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramWorkspaceKind;
import java.util.Objects;

/**
 * Fuente de verdad interna para cada tipo oficial de proyecto.
 *
 * <p>Esta definición concentra metadatos que antes podían quedar repartidos entre
 * catálogo de tipos, catálogo de capacidades, ejemplos oficiales, workspaces y
 * documentación. La UI puede seguir teniendo adaptadores especializados, pero no
 * debe inventar desde cero qué promete cada tipo visible.</p>
 */
/**
 * Registro completo de un tipo oficial visible en la aplicación.
 *
 * <p>La definición une datos de producto y de implementación: identificador, categoría,
 * estado, capacidades, workspace, teoría, gramática y ejemplos oficiales. Es deliberadamente
 * explícita para que las pruebas anti-fachada puedan detectar incoherencias.</p>
 */
public record DiagramTypeOfficialDefinition(
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
        String officialExampleId,
        String officialExampleTitle,
        String officialExampleResource,
        String officialExampleSummary,
        boolean officialExampleImportable
) {

    public DiagramTypeOfficialDefinition {
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
        officialExampleId = normalizeOptional(officialExampleId);
        officialExampleTitle = normalizeOptional(officialExampleTitle);
        officialExampleResource = normalizeOptional(officialExampleResource);
        officialExampleSummary = normalizeOptional(officialExampleSummary);
    }

    public boolean hasOfficialExample() {
        return !officialExampleId.isBlank() && !officialExampleResource.isBlank();
    }

    private static String requireText(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName);
        if (value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " no puede estar vacío.");
        }
        return value.strip();
    }

    private static String normalizeOptional(String value) {
        return value == null ? "" : value.strip();
    }
}
