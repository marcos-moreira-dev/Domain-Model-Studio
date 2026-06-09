package com.marcosmoreira.domainmodelstudio.presentation.workspace;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

/**
 * Catálogo pequeño de workspaces disponibles en el shell.
 *
 * <p>Centraliza nombres visibles y reglas de paneles por familia de workspace. La vista
 * principal solo consulta este catálogo; no debe conocer textos ni excepciones de cada editor.</p>
 */
public final class DefaultWorkspaceDescriptorCatalog {

    private final Map<WorkspaceKind, WorkspaceDescriptor> descriptors = new EnumMap<>(WorkspaceKind.class);

    public DefaultWorkspaceDescriptorCatalog() {
        register(new WorkspaceDescriptor(
                WorkspaceKind.WELCOME_HOME,
                "Pantalla de inicio",
                false,
                "La pantalla de inicio no usa paneles laterales del modelo conceptual."));
        register(new WorkspaceDescriptor(
                WorkspaceKind.CONCEPTUAL_CANVAS,
                "Modelo conceptual",
                false,
                "El modelo conceptual se monta como workspace común con SideDock propio durante la migración."));
        register(new WorkspaceDescriptor(
                WorkspaceKind.DATA_DICTIONARY_DOCUMENT,
                "Diccionario de datos",
                false,
                "El diccionario de datos usa su propia vista documental."));
        register(new WorkspaceDescriptor(
                WorkspaceKind.LOGICAL_BUSINESS_DOCUMENT,
                "Levantamiento lógico",
                false,
                "El levantamiento lógico usa su propia vista documental con SideBar modular."));
        register(new WorkspaceDescriptor(
                WorkspaceKind.MODULE_MAP_DIAGRAM,
                "Mapa de módulos",
                false,
                "El mapa de módulos usa su propia vista visual."));
        register(new WorkspaceDescriptor(
                WorkspaceKind.UML_CLASS_DIAGRAM,
                "UML Clases",
                false,
                "UML Clases usa su propio diagrama visual."));
        register(new WorkspaceDescriptor(
                WorkspaceKind.ROLES_PERMISSIONS_MATRIX,
                "Roles y permisos",
                false,
                "Roles y permisos usa su propia matriz visual."));
        register(new WorkspaceDescriptor(
                WorkspaceKind.SCREEN_FLOW_DIAGRAM,
                "Flujo de pantallas",
                false,
                "Flujo de pantallas usa su propio diagrama visual."));
        register(new WorkspaceDescriptor(
                WorkspaceKind.WIREFRAME_DIAGRAM,
                "Wireframes administrativos",
                false,
                "Wireframes administrativos usa su propia vista visual."));
        register(new WorkspaceDescriptor(
                WorkspaceKind.ARCHITECTURE_DIAGRAM,
                "C4 y despliegue técnico",
                false,
                "C4 y despliegue técnico usan su propia vista visual."));
        register(new WorkspaceDescriptor(
                WorkspaceKind.BEHAVIOR_DIAGRAM,
                "Diagramas de comportamiento",
                false,
                "Los diagramas de comportamiento usan su propia vista visual."));
        register(new WorkspaceDescriptor(
                WorkspaceKind.FREE_GRAPH_DIAGRAM,
                "Grafo libre",
                false,
                "Grafo libre usa su propia vista visual con estructura y propiedades integradas."));
        register(new WorkspaceDescriptor(
                WorkspaceKind.LOGICAL_BUSINESS_GRAPH_DIAGRAM,
                "Grafo lógico del negocio",
                false,
                "Grafo lógico del negocio usa su propia vista visual sobre el canvas común."));
        register(new WorkspaceDescriptor(
                WorkspaceKind.PLACEHOLDER_GUIDE,
                "Guía de preparación",
                false,
                "La guía de preparación no usa paneles laterales del modelo conceptual."));
    }

    public WorkspaceDescriptor descriptorFor(WorkspaceKind kind) {
        Objects.requireNonNull(kind, "kind");
        WorkspaceDescriptor descriptor = descriptors.get(kind);
        if (descriptor == null) {
            throw new IllegalArgumentException("No existe descriptor para el workspace " + kind + ".");
        }
        return descriptor;
    }

    public WorkspaceDescriptor descriptorFor(WorkspaceRoute route) {
        Objects.requireNonNull(route, "route");
        return descriptorFor(route.workspaceKind());
    }

    public boolean usesGenericConceptualSidePanels(WorkspaceRoute route) {
        Objects.requireNonNull(route, "route");
        return !route.placeholderActive()
                && descriptorFor(route).usesGenericConceptualSidePanels();
    }

    public String panelUnavailableMessage(WorkspaceRoute route, String fallback) {
        Objects.requireNonNull(route, "route");
        WorkspaceDescriptor descriptor = descriptorFor(route);
        if (descriptor.panelUnavailableMessage().isBlank()) {
            return fallback;
        }
        return descriptor.panelUnavailableMessage();
    }

    public Collection<WorkspaceDescriptor> findAll() {
        return descriptors.values();
    }

    private void register(WorkspaceDescriptor descriptor) {
        descriptors.put(descriptor.workspaceKind(), descriptor);
    }
}
