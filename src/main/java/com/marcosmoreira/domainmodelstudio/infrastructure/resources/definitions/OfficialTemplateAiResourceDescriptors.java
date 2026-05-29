package com.marcosmoreira.domainmodelstudio.infrastructure.resources.definitions;

import com.marcosmoreira.domainmodelstudio.application.resources.AiResourceDescriptor;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.List;

/** Plantillas oficiales empaquetadas para uso con IA. */
public final class OfficialTemplateAiResourceDescriptors {

    private OfficialTemplateAiResourceDescriptors() {
    }

    public static List<AiResourceDescriptor> all() {
        return List.of(
                AiResourceDescriptorFactory.importable("plantilla-oficial-admin-module-map", "official-markdown/plantillas/admin_module_map.md", DiagramTypeId.ADMIN_MODULE_MAP,
                        "Plantilla oficial importable para mapa de módulos."),
                AiResourceDescriptorFactory.importable("plantilla-oficial-admin-wireframes", "official-markdown/plantillas/admin_wireframes.md", DiagramTypeId.ADMIN_WIREFRAMES,
                        "Plantilla oficial importable para wireframes administrativos."),
                AiResourceDescriptorFactory.importable("plantilla-oficial-bpmn-basic", "official-markdown/plantillas/bpmn_basic.md", DiagramTypeId.BPMN_BASIC,
                        "Plantilla oficial importable para generar Markdown equivalente del cliente."),
                AiResourceDescriptorFactory.importable("plantilla-oficial-c4-containers", "official-markdown/plantillas/c4_containers.md", DiagramTypeId.C4_CONTAINERS,
                        "Plantilla oficial importable para C4 Contenedores."),
                AiResourceDescriptorFactory.importable("plantilla-oficial-c4-context", "official-markdown/plantillas/c4_context.md", DiagramTypeId.C4_CONTEXT,
                        "Plantilla oficial importable para C4 Contexto."),
                AiResourceDescriptorFactory.importable("plantilla-oficial-conceptual-model", "official-markdown/plantillas/conceptual_model.md", DiagramTypeId.CONCEPTUAL_MODEL,
                        "Plantilla oficial importable para generar Markdown equivalente del cliente en modelo conceptual."),
                AiResourceDescriptorFactory.importable("plantilla-oficial-data-dictionary", "official-markdown/plantillas/data_dictionary.md", DiagramTypeId.DATA_DICTIONARY,
                        "Plantilla oficial para documentar campos; importación directa disponible."),
                AiResourceDescriptorFactory.importable("plantilla-oficial-operational-flow", "official-markdown/plantillas/operational_flow.md", DiagramTypeId.OPERATIONAL_FLOW,
                        "Plantilla oficial importable para generar Markdown equivalente del cliente."),
                AiResourceDescriptorFactory.importable("plantilla-oficial-roles-permissions-map", "official-markdown/plantillas/roles_permissions_map.md", DiagramTypeId.ROLES_PERMISSIONS_MAP,
                        "Plantilla oficial importable para roles y permisos."),
                AiResourceDescriptorFactory.importable("plantilla-oficial-screen-flow", "official-markdown/plantillas/screen_flow.md", DiagramTypeId.SCREEN_FLOW,
                        "Plantilla oficial importable para flujo de pantallas."),
                AiResourceDescriptorFactory.importable("plantilla-oficial-technical-deployment", "official-markdown/plantillas/technical_deployment.md", DiagramTypeId.TECHNICAL_DEPLOYMENT,
                        "Plantilla oficial importable para despliegue técnico."),
                AiResourceDescriptorFactory.importable("plantilla-oficial-uml-activity", "official-markdown/plantillas/uml_activity.md", DiagramTypeId.UML_ACTIVITY,
                        "Plantilla oficial importable para generar Markdown equivalente del cliente."),
                AiResourceDescriptorFactory.importable("plantilla-oficial-uml-class", "official-markdown/plantillas/uml_class.md", DiagramTypeId.UML_CLASS,
                        "Plantilla oficial importable para UML Clases."),
                AiResourceDescriptorFactory.importable("plantilla-oficial-uml-sequence", "official-markdown/plantillas/uml_sequence.md", DiagramTypeId.UML_SEQUENCE,
                        "Plantilla oficial importable para generar Markdown equivalente del cliente."),
                AiResourceDescriptorFactory.importable("plantilla-oficial-uml-state", "official-markdown/plantillas/uml_state.md", DiagramTypeId.UML_STATE,
                        "Plantilla oficial importable para generar Markdown equivalente del cliente."),
                AiResourceDescriptorFactory.importable("plantilla-oficial-uml-use-case", "official-markdown/plantillas/uml_use_case.md", DiagramTypeId.UML_USE_CASE,
                        "Plantilla oficial importable para generar Markdown equivalente del cliente."),
                AiResourceDescriptorFactory.importable("plantilla-oficial-free-graph", "official-markdown/plantillas/free_graph.md", DiagramTypeId.FREE_GRAPH,
                        "Plantilla oficial importable para Grafo libre.")
        );
    }
}
