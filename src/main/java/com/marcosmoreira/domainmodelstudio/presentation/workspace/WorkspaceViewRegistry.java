package com.marcosmoreira.domainmodelstudio.presentation.workspace;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javafx.scene.Parent;

/**
 * Registro de nodos JavaFX disponibles para cada familia de workspace.
 *
 * <p>El shell monta workspaces desde este registro en vez de encadenar decisiones visuales
 * por cada tipo de diagrama. Cada raíz se registra con su descriptor de producto, para que
 * vista, mensajes y política de paneles hablen desde el mismo contrato.</p>
 */
public final class WorkspaceViewRegistry {

    private final Map<WorkspaceKind, WorkspaceViewRegistration> registrations = new EnumMap<>(WorkspaceKind.class);

    public WorkspaceViewRegistry register(WorkspaceDescriptor descriptor, Parent root) {
        Objects.requireNonNull(descriptor, "descriptor");
        if (root != null) {
            registrations.put(descriptor.workspaceKind(), new WorkspaceViewRegistration(descriptor, root));
        }
        return this;
    }

    public Optional<WorkspaceViewRegistration> registrationFor(WorkspaceRoute route) {
        Objects.requireNonNull(route, "route");
        return Optional.ofNullable(registrations.get(route.workspaceKind()));
    }

    public Optional<Parent> rootFor(WorkspaceRoute route) {
        return registrationFor(route).map(WorkspaceViewRegistration::root);
    }

    public Parent rootForOrFallback(WorkspaceRoute route, Parent fallback) {
        return rootFor(route).orElse(fallback);
    }

    public Collection<WorkspaceViewRegistration> registrations() {
        return registrations.values();
    }
}
