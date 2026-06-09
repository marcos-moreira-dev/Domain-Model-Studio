package com.marcosmoreira.domainmodelstudio.application.catalog.definitions;

import com.marcosmoreira.domainmodelstudio.application.catalog.DiagramTypeOfficialDefinition;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCategoryId;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramWorkspaceKind;
import java.util.List;

/** Tipos oficiales de aplicaciones administrativas. */
public final class AdministrativeDiagramTypeDefinitions {

    private AdministrativeDiagramTypeDefinitions() {
    }

    public static List<DiagramTypeOfficialDefinition> all() {
        return List.of(
                DiagramTypeDefinitionFactory.available(DiagramTypeId.ADMIN_MODULE_MAP, "Mapa de módulos", DiagramCategoryId.ADMIN_APPLICATIONS,
                        DiagramWorkspaceKind.MODULE_MAP_DIAGRAM, DiagramCapabilityProfiles.visual(),
                        "Representa módulos funcionales, submódulos, responsabilidades y dependencias de una aplicación administrativa.",
                        "mapa-modulos", "mapa-modulos-gramatica", "admin-module-map",
                        "admin_module_map_restaurante_minimo.md", "uens-mapa-modulos",
                        "UENS — mapa de módulos escolar", "admin_module_map_uens_gordito.md",
                        "Módulos de secretaría, gestión académica, calificaciones, reportes, seguridad y auditoría.", true),
                DiagramTypeDefinitionFactory.available(DiagramTypeId.ROLES_PERMISSIONS_MAP, "Roles y permisos", DiagramCategoryId.ADMIN_APPLICATIONS,
                        DiagramWorkspaceKind.ROLES_PERMISSIONS_MATRIX, DiagramCapabilityProfiles.matrix(),
                        "Matriz administrativa de roles, permisos, acciones y límites operativos.",
                        "roles-permisos", "roles-permisos-gramatica", "roles-permissions-map",
                        "roles_permissions_optica_minimo.md", "uens-roles-permisos",
                        "UENS — roles y permisos escolares", "roles_permissions_uens_gordito.md",
                        "Roles de administración, secretaría, docente, dirección y soporte con permisos de operación.", true),
                DiagramTypeDefinitionFactory.available(DiagramTypeId.SCREEN_FLOW, "Flujo de pantallas", DiagramCategoryId.ADMIN_APPLICATIONS,
                        DiagramWorkspaceKind.SCREEN_FLOW_DIAGRAM, DiagramCapabilityProfiles.visual(),
                        "Representa navegación entre pantallas de una aplicación administrativa.",
                        "flujo-pantallas", "flujo-pantallas-gramatica", "screen-flow",
                        "screen_flow_ventas_minimo.md", "uens-flujo-pantallas",
                        "UENS — flujo de pantallas administrativo", "screen_flow_uens_gordito.md",
                        "Navegación entre vistas reales del desktop: login, dashboard, estudiantes, representantes, docentes, secciones, asignaturas, clases, calificaciones, reportes y auditoría.", true),
                DiagramTypeDefinitionFactory.available(DiagramTypeId.ADMIN_WIREFRAMES, "Wireframes administrativos", DiagramCategoryId.ADMIN_APPLICATIONS,
                        DiagramWorkspaceKind.WIREFRAME_DIAGRAM, DiagramCapabilityProfiles.visual(),
                        "Representa pantallas administrativas con cajas simples, tablas, formularios, botones y filtros.",
                        "wireframes-administrativos", "wireframes-administrativos-gramatica", "admin-wireframes",
                        "admin_wireframes_ventas_minimo.md", "uens-wireframes-administrativos",
                        "UENS — wireframes administrativos escolares", "admin_wireframes_uens_gordito.md",
                        "Maquetas de vistas reales del desktop: login, dashboard, estudiantes, representantes, clases, calificaciones, reportes y auditoría.", true)
        );
    }
}
