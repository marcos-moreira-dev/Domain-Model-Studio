package com.marcosmoreira.domainmodelstudio.infrastructure.resources.definitions;

import com.marcosmoreira.domainmodelstudio.application.resources.AiResourceDescriptor;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.List;

/** Ejemplos mínimos oficiales usados como recursos IA y fixtures importables. */
public final class OfficialMinimalExampleAiResourceDescriptors {

    private OfficialMinimalExampleAiResourceDescriptors() {
    }

    public static List<AiResourceDescriptor> all() {
        return List.of(
                AiResourceDescriptorFactory.importable("ejemplo-oficial-admin-module-map-restaurante-minimo", "official-markdown/diagramas/admin_module_map_restaurante_minimo.md", DiagramTypeId.ADMIN_MODULE_MAP,
                        "Ejemplo oficial importable para generar Markdown equivalente del cliente."),
                AiResourceDescriptorFactory.importable("ejemplo-oficial-admin-wireframes-ventas-minimo", "official-markdown/diagramas/admin_wireframes_ventas_minimo.md", DiagramTypeId.ADMIN_WIREFRAMES,
                        "Ejemplo oficial importable para generar Markdown equivalente del cliente."),
                AiResourceDescriptorFactory.importable("ejemplo-oficial-bpmn-basic-venta-minimo", "official-markdown/diagramas/bpmn_basic_venta_minimo.md", DiagramTypeId.BPMN_BASIC,
                        "Ejemplo oficial importable para generar Markdown equivalente del cliente."),
                AiResourceDescriptorFactory.importable("ejemplo-oficial-c4-containers-sistema-administrativo-minimo", "official-markdown/diagramas/c4_containers_sistema_administrativo_minimo.md", DiagramTypeId.C4_CONTAINERS,
                        "Ejemplo oficial importable para generar Markdown equivalente del cliente."),
                AiResourceDescriptorFactory.importable("ejemplo-oficial-c4-context-sistema-administrativo-minimo", "official-markdown/diagramas/c4_context_sistema_administrativo_minimo.md", DiagramTypeId.C4_CONTEXT,
                        "Ejemplo oficial importable para generar Markdown equivalente del cliente."),
                AiResourceDescriptorFactory.importable("ejemplo-oficial-conceptual-model-colegio-minimo-importable", "official-markdown/diagramas/conceptual_model_colegio_minimo_importable.md", DiagramTypeId.CONCEPTUAL_MODEL,
                        "Ejemplo oficial importable para generar Markdown equivalente del cliente."),
                AiResourceDescriptorFactory.importable("ejemplo-oficial-data-dictionary-colegio-minimo", "official-markdown/diagramas/data_dictionary_colegio_minimo.md", DiagramTypeId.DATA_DICTIONARY,
                        "Ejemplo oficial documental importable como diccionario de datos."),
                AiResourceDescriptorFactory.importable("ejemplo-oficial-operational-flow-soporte-minimo", "official-markdown/diagramas/operational_flow_soporte_minimo.md", DiagramTypeId.OPERATIONAL_FLOW,
                        "Ejemplo oficial importable para generar Markdown equivalente del cliente."),
                AiResourceDescriptorFactory.importable("ejemplo-oficial-roles-permissions-optica-minimo", "official-markdown/diagramas/roles_permissions_optica_minimo.md", DiagramTypeId.ROLES_PERMISSIONS_MAP,
                        "Ejemplo oficial importable para generar Markdown equivalente del cliente."),
                AiResourceDescriptorFactory.importable("ejemplo-oficial-screen-flow-ventas-minimo", "official-markdown/diagramas/screen_flow_ventas_minimo.md", DiagramTypeId.SCREEN_FLOW,
                        "Ejemplo oficial importable para generar Markdown equivalente del cliente."),
                AiResourceDescriptorFactory.importable("ejemplo-oficial-technical-deployment-piloto-minimo", "official-markdown/diagramas/technical_deployment_piloto_minimo.md", DiagramTypeId.TECHNICAL_DEPLOYMENT,
                        "Ejemplo oficial importable para generar Markdown equivalente del cliente."),
                AiResourceDescriptorFactory.importable("ejemplo-oficial-uml-activity-cierre-caja-minimo", "official-markdown/diagramas/uml_activity_cierre_caja_minimo.md", DiagramTypeId.UML_ACTIVITY,
                        "Ejemplo oficial importable para generar Markdown equivalente del cliente."),
                AiResourceDescriptorFactory.importable("ejemplo-oficial-uml-class-restaurante-minimo", "official-markdown/diagramas/uml_class_restaurante_minimo.md", DiagramTypeId.UML_CLASS,
                        "Ejemplo oficial importable para generar Markdown equivalente del cliente."),
                AiResourceDescriptorFactory.importable("ejemplo-oficial-uml-sequence-login-minimo", "official-markdown/diagramas/uml_sequence_login_minimo.md", DiagramTypeId.UML_SEQUENCE,
                        "Ejemplo oficial importable para generar Markdown equivalente del cliente."),
                AiResourceDescriptorFactory.importable("ejemplo-oficial-uml-state-orden-minimo", "official-markdown/diagramas/uml_state_orden_minimo.md", DiagramTypeId.UML_STATE,
                        "Ejemplo oficial importable para generar Markdown equivalente del cliente."),
                AiResourceDescriptorFactory.importable("ejemplo-oficial-uml-use-case-restaurante-minimo", "official-markdown/diagramas/uml_use_case_restaurante_minimo.md", DiagramTypeId.UML_USE_CASE,
                        "Ejemplo oficial importable para generar Markdown equivalente del cliente."),
                AiResourceDescriptorFactory.importable("ejemplo-oficial-free-graph-minimo", "official-markdown/diagramas/free_graph_minimo.md", DiagramTypeId.FREE_GRAPH,
                        "Ejemplo oficial importable para Grafo libre.")
        );
    }
}
