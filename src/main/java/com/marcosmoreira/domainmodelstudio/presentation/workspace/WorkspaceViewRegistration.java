package com.marcosmoreira.domainmodelstudio.presentation.workspace;

import java.util.Objects;
import javafx.scene.Parent;

/**
 * Une el contrato de producto de un workspace con su raíz JavaFX concreta.
 *
 * <p>Esta clase evita que el registro visual sea solo un mapa de nodos. Cada editor queda
 * registrado con la misma metadata que usa el shell para mensajes, paneles y pruebas.</p>
 */
public record WorkspaceViewRegistration(
        WorkspaceDescriptor descriptor,
        Parent root
) {

    public WorkspaceViewRegistration {
        Objects.requireNonNull(descriptor, "descriptor");
        Objects.requireNonNull(root, "root");
    }

    public WorkspaceKind workspaceKind() {
        return descriptor.workspaceKind();
    }
}
