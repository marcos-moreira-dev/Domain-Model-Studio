package com.marcosmoreira.domainmodelstudio.application.catalog.definitions;

import com.marcosmoreira.domainmodelstudio.application.catalog.DiagramTypeOfficialDefinition;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCategoryId;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramWorkspaceKind;
import java.util.List;

/** Tipos oficiales de procesos y flujos de negocio. */
public final class BusinessProcessDiagramTypeDefinitions {

    private BusinessProcessDiagramTypeDefinitions() {
    }

    public static List<DiagramTypeOfficialDefinition> all() {
        return List.of(
                DiagramTypeDefinitionFactory.available(DiagramTypeId.BPMN_BASIC, "BPMN básico", DiagramCategoryId.BUSINESS_PROCESS,
                        DiagramWorkspaceKind.BEHAVIOR_DIAGRAM, DiagramCapabilityProfiles.visual(),
                        "Representa procesos de negocio con eventos, actividades, decisiones y flujos.",
                        "bpmn-basico", "bpmn-basico-gramatica", "bpmn-basic",
                        "bpmn_basic_venta_minimo.md", "uens-bpmn-matricula",
                        "UENS — BPMN básico de asignación estudiante-sección", "bpmn_basic_matricula_uens_gordito.md",
                        "Proceso operativo de registro/actualización del estudiante y asignación vigente a sección; matrícula se trata como trámite, no como tabla.", true),
                DiagramTypeDefinitionFactory.available(DiagramTypeId.OPERATIONAL_FLOW, "Flujo operativo", DiagramCategoryId.BUSINESS_PROCESS,
                        DiagramWorkspaceKind.BEHAVIOR_DIAGRAM, DiagramCapabilityProfiles.visual(),
                        "Explica pasos operativos de un negocio de forma menos formal que BPMN.",
                        "flujo-operativo", "flujo-operativo-gramatica", "operational-flow",
                        "operational_flow_soporte_minimo.md", "uens-flujo-operativo-secretaria",
                        "UENS — flujo operativo de secretaría", "operational_flow_secretaria_uens_gordito.md",
                        "Atención administrativa de solicitudes desde recepción hasta cierre trazable.", true)
        );
    }
}
