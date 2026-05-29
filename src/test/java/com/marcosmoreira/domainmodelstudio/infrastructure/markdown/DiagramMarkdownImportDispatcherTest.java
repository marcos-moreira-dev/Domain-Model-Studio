package com.marcosmoreira.domainmodelstudio.infrastructure.markdown;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.catalog.DefaultDiagramTypeRegistry;
import com.marcosmoreira.domainmodelstudio.application.importmodel.MarkdownModelParsingException;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import org.junit.jupiter.api.Test;

class DiagramMarkdownImportDispatcherTest {

    private final DiagramMarkdownImportDispatcher dispatcher = new DiagramMarkdownImportDispatcher(
            new DefaultDiagramTypeRegistry()
    );

    @Test
    void importsConceptualMarkdownDeclaredWithDiagramType() throws Exception {
        DiagramProject project = dispatcher.parse(conceptualMarkdown(), "conceptual.md");

        assertEquals("colegio_v1", project.metadata().id());
        assertEquals(2, project.model().entityCount());
        assertEquals(1, project.model().relationshipCount());
    }

    @Test
    void keepsLegacyConceptualMarkdownCompatibleWhenItHasEntitiesSection() throws Exception {
        DiagramProject project = dispatcher.parse(conceptualMarkdown().replace("diagram_type: \"conceptual-model\"\n", ""), "legacy.md");

        assertEquals(2, project.model().entityCount());
    }

    @Test
    void importsRecognizedModuleMapMarkdown() throws Exception {
        DiagramProject project = dispatcher.parse(moduleMapMarkdown(), "module-map.md");

        assertEquals("admin-module-map", project.metadata().diagramTypeId().value());
        assertEquals(2, project.moduleMap().orElseThrow().moduleCount());
        assertEquals(1, project.moduleMap().orElseThrow().dependencyCount());
    }

    @Test
    void importsRecognizedRolesPermissionsMarkdown() throws Exception {
        DiagramProject project = dispatcher.parse(rolesPermissionsMarkdown(), "roles-permissions.md");

        assertEquals("roles-permissions-map", project.metadata().diagramTypeId().value());
        assertEquals(2, project.rolesPermissions().orElseThrow().roles().size());
        assertEquals(2, project.rolesPermissions().orElseThrow().permissions().size());
        assertEquals(2, project.rolesPermissions().orElseThrow().assignments().size());
    }


    @Test
    void importsRecognizedUmlClassMarkdown() throws Exception {
        DiagramProject project = dispatcher.parse(umlClassMarkdown(), "uml-class.md");

        assertEquals("uml-class", project.metadata().diagramTypeId().value());
        assertEquals(1, project.umlClassDiagram().orElseThrow().moduleCount());
        assertEquals(2, project.umlClassDiagram().orElseThrow().classCount());
        assertEquals(1, project.umlClassDiagram().orElseThrow().relationCount());
    }

    @Test
    void rejectsUnknownDiagramType() {
        MarkdownModelParsingException exception = assertThrows(
                MarkdownModelParsingException.class,
                () -> dispatcher.parse(conceptualMarkdown().replace("conceptual-model", "diagrama-inventado"), "unknown.md"));

        assertTrue(exception.getMessage().contains("no registrado"));
    }

    @Test
    void rejectsMarkdownWithoutRecognizableDiagramTypeOrLegacyConceptualShape() {
        MarkdownModelParsingException exception = assertThrows(
                MarkdownModelParsingException.class,
                () -> dispatcher.parse("# Roles\n\n## Administrador\n", "roles.md"));

        assertTrue(exception.getMessage().contains("No se pudo reconocer el tipo de diagrama"));
    }

    private static String conceptualMarkdown() {
        return """
                ---
                id: colegio_v1
                title: Modelo conceptual - Colegio
                diagram_type: "conceptual-model"
                notation: chen
                ---

                # Entidades

                ## Estudiante
                id: estudiante
                - pk id
                - nombres

                ## Seccion
                id: seccion
                - pk id
                - grado

                # Relaciones

                ## Agrupa
                id: agrupa
                from: Seccion
                to: Estudiante
                from_cardinality: 1
                to_cardinality: 0..M
                """;
    }

    private static String rolesPermissionsMarkdown() {
        return """
                ---
                dms_version: "1"
                diagram_type: "roles-permissions-map"
                name: "Roles y permisos"
                importable: true
                ---

                # Roles

                ## Administrador
                id: administrador
                propósito: administra el sistema.

                ## Vendedor
                id: vendedor
                propósito: registra ventas.

                # Permisos

                - ventas_crear: registrar ventas.
                - reportes_ver: revisar reportes.

                # Asignaciones

                | Rol | Permiso | Alcance | Observación |
                |---|---|---|---|
                | administrador | reportes_ver | global | Revisión general. |
                | vendedor | ventas_crear | ventas | Venta diaria. |
                """;
    }

    private static String umlClassMarkdown() {
        return """
                ---
                dms_version: "1"
                diagram_type: "uml-class"
                name: "UML Clases"
                importable: true
                ---

                # Paquetes
                ## dominio
                propósito: reglas del negocio.

                # Clases
                ## Pedido
                paquete: dominio
                responsabilidad: agrupa ítems.
                atributos:
                - id: String
                métodos:
                - cerrar(): void

                ## ItemPedido
                paquete: dominio
                responsabilidad: representa un ítem.
                atributos:
                - cantidad: int

                # Relaciones
                - Pedido *-- ItemPedido: contiene
                """;
    }

    private static String moduleMapMarkdown() {
        return """
                ---
                dms_version: "1"
                diagram_type: "admin-module-map"
                name: "Mapa de módulos"
                importable: true
                ---

                # Módulos
                ## Ventas
                id: ventas
                responsabilidad: registrar ventas.

                ## Reportes
                id: reportes
                responsabilidad: resumir operación.

                # Dependencias
                - ventas -> reportes: alimenta reportes diarios.
                """;
    }
}
