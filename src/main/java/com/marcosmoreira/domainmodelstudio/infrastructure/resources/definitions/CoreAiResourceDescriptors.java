package com.marcosmoreira.domainmodelstudio.infrastructure.resources.definitions;

import com.marcosmoreira.domainmodelstudio.application.resources.AiResourceDescriptor;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.List;

/** Gramáticas raíz y recursos canónicos del Levantamiento lógico. */
public final class CoreAiResourceDescriptors {

    private CoreAiResourceDescriptors() {
    }

    public static List<AiResourceDescriptor> all() {
        return List.of(
                AiResourceDescriptorFactory.importable("modelo-conceptual-gramatica", "01_gramatica_markdown_modelo_conceptual.md", DiagramTypeId.CONCEPTUAL_MODEL,
                        "Gramática Markdown actualmente importable para modelo conceptual."),
                AiResourceDescriptorFactory.importable("modelo-conceptual-ejemplo-complejo", "02_ejemplo_modelo_conceptual_complejo.md", DiagramTypeId.CONCEPTUAL_MODEL,
                        "Ejemplo complejo compatible con la gramática activa."),
                AiResourceDescriptorFactory.importable("diccionario-datos-gramatica", "03_diccionario_datos_gramatica.md", DiagramTypeId.DATA_DICTIONARY,
                        "Gramática Markdown actualmente importable para diccionario de datos; parser documental activo y salida exportable."),
                AiResourceDescriptorFactory.importable("c4-contexto-contenedores-gramatica", "04_c4_contexto_contenedores_gramatica.md", DiagramTypeId.C4_CONTEXT,
                        "Gramática Markdown actualmente importable para C4 contexto y C4 contenedores."),
                AiResourceDescriptorFactory.alias("c4-contenedores-gramatica-alias", "04_c4_contexto_contenedores_gramatica.md", DiagramTypeId.C4_CONTAINERS,
                        "Alias del recurso C4 combinado para contenedores; no exporta copia duplicada."),
                AiResourceDescriptorFactory.importable("despliegue-tecnico-gramatica", "15_despliegue_tecnico_gramatica.md", DiagramTypeId.TECHNICAL_DEPLOYMENT,
                        "Gramática Markdown actualmente importable para despliegue técnico."),
                AiResourceDescriptorFactory.importable("bpmn-basico-gramatica", "05_bpmn_basico_gramatica.md", DiagramTypeId.BPMN_BASIC,
                        "Gramática Markdown actualmente importable para BPMN básico."),
                AiResourceDescriptorFactory.importable("flujo-operativo-gramatica", "17_flujo_operativo_gramatica.md", DiagramTypeId.OPERATIONAL_FLOW,
                        "Gramática Markdown actualmente importable para flujo operativo."),
                AiResourceDescriptorFactory.importable("uml-casos-uso-gramatica", "06_uml_casos_uso_gramatica.md", DiagramTypeId.UML_USE_CASE,
                        "Gramática Markdown actualmente importable para UML casos de uso."),
                AiResourceDescriptorFactory.importable("uml-clases-gramatica", "07_uml_clases_gramatica.md", DiagramTypeId.UML_CLASS,
                        "Gramática Markdown actualmente importable para UML Clases."),
                AiResourceDescriptorFactory.importable("uml-actividad-gramatica", "08_uml_actividad_gramatica.md", DiagramTypeId.UML_ACTIVITY,
                        "Gramática Markdown actualmente importable para UML actividad."),
                AiResourceDescriptorFactory.importable("uml-secuencia-gramatica", "09_uml_secuencia_gramatica.md", DiagramTypeId.UML_SEQUENCE,
                        "Gramática Markdown actualmente importable para UML secuencia."),
                AiResourceDescriptorFactory.importable("uml-estados-gramatica", "10_uml_estados_gramatica.md", DiagramTypeId.UML_STATE,
                        "Gramática Markdown actualmente importable para UML estados."),
                AiResourceDescriptorFactory.importable("wireframes-administrativos-gramatica", "11_wireframes_administrativos_gramatica.md", DiagramTypeId.ADMIN_WIREFRAMES,
                        "Gramática Markdown actualmente importable para wireframes administrativos."),
                AiResourceDescriptorFactory.importable("roles-permisos-gramatica", "12_roles_permisos_gramatica.md", DiagramTypeId.ROLES_PERMISSIONS_MAP,
                        "Gramática Markdown actualmente importable para roles y permisos."),
                AiResourceDescriptorFactory.importable("mapa-modulos-gramatica", "13_mapa_modulos_gramatica.md", DiagramTypeId.ADMIN_MODULE_MAP,
                        "Gramática Markdown actualmente importable para mapa de módulos."),
                AiResourceDescriptorFactory.importable("flujo-pantallas-gramatica", "14_flujo_pantallas_gramatica.md", DiagramTypeId.SCREEN_FLOW,
                        "Gramática Markdown actualmente importable para flujo de pantallas."),
                AiResourceDescriptorFactory.importable("grafo-libre-gramatica", "16_grafo_libre_gramatica.md", DiagramTypeId.FREE_GRAPH,
                        "Gramática Markdown actualmente importable para Grafo libre."),
                AiResourceDescriptorFactory.logicalBusiness("plantilla-canonica-levantamiento-logico", "logical_business_intake_template.md",
                        "Plantilla canónica oficial para levantamiento lógico de negocio; recurso IA exportable e importable como proyecto documental."),
                AiResourceDescriptorFactory.logicalBusiness("ejemplo-levantamiento-logico-optica-minimo", "logical_business_intake_optica_minimo.md",
                        "Ejemplo ficticio mínimo de levantamiento lógico para óptica; ejemplo importable como proyecto documental."),
                AiResourceDescriptorFactory.logicalBusiness("ejemplo-levantamiento-logico-optica-gordito", "logical_business_intake_optica_gordito.md",
                        "Ejemplo ficticio completo de levantamiento lógico para óptica; ejemplo importable y fuente histórica de validación."),
                AiResourceDescriptorFactory.logicalBusiness("ejemplo-levantamiento-logico-uens-gordito", "logical_business_intake_uens_gordito.md",
                        "Ejemplo oficial completo UENS; expediente lógico canónico para reglas, acciones, entidades candidatas, reportes, riesgos y uso posterior como fuente revisable.")
        );
    }
}
