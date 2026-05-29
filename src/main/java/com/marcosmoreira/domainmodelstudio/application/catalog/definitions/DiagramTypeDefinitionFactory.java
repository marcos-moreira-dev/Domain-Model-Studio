package com.marcosmoreira.domainmodelstudio.application.catalog.definitions;

import com.marcosmoreira.domainmodelstudio.application.catalog.DiagramTypeOfficialDefinition;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCapabilitySet;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCategoryId;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramSupportStatus;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramWorkspaceKind;

/**
 * Fábrica pequeña para evitar constructores repetidos dentro de los catálogos por familia.
 *
 * <p>La Tanda 30 mueve las definiciones oficiales a familias y conserva un agregador público.
 * Esta factory mantiene uniforme el armado de recursos, ejemplos oficiales y capacidades sin
 * volver a un catálogo monolítico.</p>
 */

final class DiagramTypeDefinitionFactory {

    private static final String DIAGRAMS_BASE = "ai-resources/official-markdown/diagramas/";
    private static final String LOGICAL_BUSINESS_BASE = "ai-resources/official-markdown/levantamiento-logico/";

    private DiagramTypeDefinitionFactory() {
    }

    static DiagramTypeOfficialDefinition available(
            DiagramTypeId id,
            String displayName,
            DiagramCategoryId categoryId,
            DiagramWorkspaceKind workspaceKind,
            DiagramCapabilitySet capabilities,
            String shortDescription,
            String theoryTopicId,
            String grammarResourceId,
            String toolbarPolicyId,
            String minimalExample,
            String officialExampleId,
            String officialExampleTitle,
            String officialExample,
            String officialExampleSummary,
            boolean officialExampleImportable
    ) {
        return new DiagramTypeOfficialDefinition(
                id,
                displayName,
                categoryId,
                DiagramSupportStatus.AVAILABLE,
                capabilities,
                workspaceKind,
                shortDescription,
                theoryTopicId,
                grammarResourceId,
                toolbarPolicyId,
                diagramResource(minimalExample),
                officialExampleId,
                officialExampleTitle,
                diagramResource(officialExample),
                officialExampleSummary,
                officialExampleImportable);
    }

    static DiagramTypeOfficialDefinition logicalBusinessIntake() {
        return new DiagramTypeOfficialDefinition(
                DiagramTypeId.LOGICAL_BUSINESS_INTAKE,
                "Levantamiento lógico",
                DiagramCategoryId.BUSINESS_ANALYSIS,
                DiagramSupportStatus.AVAILABLE,
                DiagramCapabilityProfiles.logicalBusinessDocument(),
                DiagramWorkspaceKind.LOGICAL_BUSINESS_DOCUMENT,
                "Documento estructurado para convertir entrevistas y observaciones en reglas, estados, acciones, entidades candidatas, atributos, relaciones, reportes, riesgos y preguntas pendientes.",
                "logical-business-intake",
                "plantilla-canonica-levantamiento-logico",
                "logical-business-intake",
                logicalBusinessResource("logical_business_intake_template.md"),
                "ejemplo-levantamiento-logico-uens-gordito",
                "UENS — levantamiento lógico escolar completo",
                logicalBusinessResource("logical_business_intake_uens_gordito.md"),
                "Ejemplo completo de la unidad educativa UENS con macroflujos, reglas, acciones, entidades candidatas, atributos, estados, reportes, riesgos, preguntas pendientes y uso como fuente lógica revisable.",
                true);
    }

    static String diagramResource(String fileName) {
        return fileName == null || fileName.isBlank() ? "" : DIAGRAMS_BASE + fileName;
    }

    private static String logicalBusinessResource(String fileName) {
        return LOGICAL_BUSINESS_BASE + fileName;
    }
}
