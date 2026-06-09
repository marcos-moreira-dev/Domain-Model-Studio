package com.marcosmoreira.domainmodelstudio.infrastructure.resources.definitions;

import com.marcosmoreira.domainmodelstudio.application.resources.AiResourceDescriptor;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.List;

/** Ejemplos UENS oficiales que acompañan el selector y los recursos IA. */
public final class OfficialUensExampleAiResourceDescriptors {

    private OfficialUensExampleAiResourceDescriptors() {
    }

    public static List<AiResourceDescriptor> all() {
        return List.of(
                AiResourceDescriptorFactory.uensExample("ejemplo-uens-conceptual-escolar", "conceptual_model_uens_gordito_importable.md", DiagramTypeId.CONCEPTUAL_MODEL,
                        "Ejemplo UENS oficial importable para modelo conceptual escolar."),
                AiResourceDescriptorFactory.uensExample("ejemplo-uens-diccionario-datos", "data_dictionary_uens_gordito.md", DiagramTypeId.DATA_DICTIONARY,
                        "Ejemplo UENS oficial importable para diccionario de datos escolar."),
                AiResourceDescriptorFactory.uensExample("ejemplo-uens-mapa-modulos", "admin_module_map_uens_gordito.md", DiagramTypeId.ADMIN_MODULE_MAP,
                        "Ejemplo UENS oficial importable para mapa de módulos escolar."),
                AiResourceDescriptorFactory.uensExample("ejemplo-uens-wireframes-administrativos", "admin_wireframes_uens_gordito.md", DiagramTypeId.ADMIN_WIREFRAMES,
                        "Ejemplo UENS oficial importable para wireframes administrativos escolares."),
                AiResourceDescriptorFactory.uensExample("ejemplo-uens-bpmn-matricula", "bpmn_basic_matricula_uens_gordito.md", DiagramTypeId.BPMN_BASIC,
                        "Ejemplo UENS oficial importable para BPMN básico de asignación estudiante-sección; matrícula se conserva solo como nombre operativo."),
                AiResourceDescriptorFactory.uensExample("ejemplo-uens-c4-contexto", "c4_context_uens_gordito.md", DiagramTypeId.C4_CONTEXT,
                        "Ejemplo UENS oficial importable para C4 contexto escolar."),
                AiResourceDescriptorFactory.uensExample("ejemplo-uens-c4-contenedores", "c4_containers_uens_gordito.md", DiagramTypeId.C4_CONTAINERS,
                        "Ejemplo UENS oficial importable para C4 contenedores escolares."),
                AiResourceDescriptorFactory.uensExample("ejemplo-uens-flujo-operativo-secretaria", "operational_flow_secretaria_uens_gordito.md", DiagramTypeId.OPERATIONAL_FLOW,
                        "Ejemplo UENS oficial importable para flujo operativo de secretaría."),
                AiResourceDescriptorFactory.uensExample("ejemplo-uens-roles-permisos", "roles_permissions_uens_gordito.md", DiagramTypeId.ROLES_PERMISSIONS_MAP,
                        "Ejemplo UENS oficial importable para roles y permisos escolares."),
                AiResourceDescriptorFactory.uensExample("ejemplo-uens-flujo-pantallas", "screen_flow_uens_gordito.md", DiagramTypeId.SCREEN_FLOW,
                        "Ejemplo UENS oficial importable para flujo de pantallas administrativo."),
                AiResourceDescriptorFactory.uensExample("ejemplo-uens-despliegue-tecnico", "technical_deployment_uens_gordito.md", DiagramTypeId.TECHNICAL_DEPLOYMENT,
                        "Ejemplo UENS oficial importable para despliegue técnico escolar."),
                AiResourceDescriptorFactory.uensExample("ejemplo-uens-uml-actividad-matricula", "uml_activity_registrar_matricula_uens_gordito.md", DiagramTypeId.UML_ACTIVITY,
                        "Ejemplo UENS oficial importable para UML actividad de asignación vigente estudiante-sección."),
                AiResourceDescriptorFactory.uensExample("ejemplo-uens-uml-clases", "uml_class_uens_gordito.md", DiagramTypeId.UML_CLASS,
                        "Ejemplo UENS oficial importable para UML clases de dominio escolar."),
                AiResourceDescriptorFactory.uensExample("ejemplo-uens-uml-secuencia-calificacion", "uml_sequence_registrar_calificacion_uens_gordito.md", DiagramTypeId.UML_SEQUENCE,
                        "Ejemplo UENS oficial importable para UML secuencia de registro de calificación."),
                AiResourceDescriptorFactory.uensExample("ejemplo-uens-uml-estados-matricula", "uml_state_matricula_uens_gordito.md", DiagramTypeId.UML_STATE,
                        "Ejemplo UENS oficial importable para UML estados de reporte_solicitud_queue."),
                AiResourceDescriptorFactory.uensExample("ejemplo-uens-uml-casos-uso", "uml_use_case_uens_gordito.md", DiagramTypeId.UML_USE_CASE,
                        "Ejemplo UENS oficial importable para UML casos de uso del sistema escolar."),
                AiResourceDescriptorFactory.uensExample("ejemplo-uens-grafo-libre", "free_graph_uens_gordito.md", DiagramTypeId.FREE_GRAPH,
                        "Ejemplo UENS oficial importable para Grafo libre de relaciones escolares.")
        );
    }
}
