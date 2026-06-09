package com.marcosmoreira.domainmodelstudio.application.catalog.definitions;

import com.marcosmoreira.domainmodelstudio.application.catalog.DiagramTypeOfficialDefinition;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCategoryId;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramWorkspaceKind;
import java.util.List;

/** Tipos oficiales de arquitectura de software y despliegue. */
public final class ArchitectureDiagramTypeDefinitions {

    private ArchitectureDiagramTypeDefinitions() {
    }

    public static List<DiagramTypeOfficialDefinition> all() {
        return List.of(
                DiagramTypeDefinitionFactory.available(DiagramTypeId.C4_CONTEXT, "C4 Contexto", DiagramCategoryId.SOFTWARE_ARCHITECTURE,
                        DiagramWorkspaceKind.ARCHITECTURE_DIAGRAM, DiagramCapabilityProfiles.visual(),
                        "Representa el sistema, sus usuarios y sistemas externos relacionados.",
                        "c4-contexto-contenedores", "c4-contexto-contenedores-gramatica", "c4-context",
                        "c4_context_sistema_administrativo_minimo.md", "uens-c4-contexto",
                        "UENS — C4 contexto escolar", "c4_context_uens_gordito.md",
                        "Personas, sistema administrativo y sistemas externos de apoyo institucional.", true),
                DiagramTypeDefinitionFactory.available(DiagramTypeId.C4_CONTAINERS, "C4 Contenedores", DiagramCategoryId.SOFTWARE_ARCHITECTURE,
                        DiagramWorkspaceKind.ARCHITECTURE_DIAGRAM, DiagramCapabilityProfiles.visual(),
                        "Representa aplicaciones, backend, base de datos y servicios externos.",
                        "c4-contexto-contenedores", "c4-contenedores-gramatica-alias", "c4-containers",
                        "c4_containers_sistema_administrativo_minimo.md", "uens-c4-contenedores",
                        "UENS — C4 contenedores escolares", "c4_containers_uens_gordito.md",
                        "Aplicación desktop, API, PostgreSQL, cola/worker de reportes, archivos generados y seguridad JWT.", true),
                DiagramTypeDefinitionFactory.available(DiagramTypeId.TECHNICAL_DEPLOYMENT, "Despliegue técnico", DiagramCategoryId.SOFTWARE_ARCHITECTURE,
                        DiagramWorkspaceKind.ARCHITECTURE_DIAGRAM, DiagramCapabilityProfiles.visual(),
                        "Representa máquinas, entornos, servicios locales/remotos y dependencias de despliegue.",
                        "despliegue-tecnico", "despliegue-tecnico-gramatica", "technical-deployment",
                        "technical_deployment_piloto_minimo.md", "uens-despliegue-tecnico",
                        "UENS — despliegue técnico escolar", "technical_deployment_uens_gordito.md",
                        "PCs de operación, servidor de aplicación, PostgreSQL, almacenamiento, red local y ambientes.", true)
        );
    }
}
