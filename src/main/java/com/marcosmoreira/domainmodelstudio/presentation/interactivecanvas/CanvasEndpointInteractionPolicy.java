package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.profile.DiagramInteractionProfile;
import java.util.Objects;

/**
 * Política transversal para exponer edición de extremos de conectores solo cuando
 * el perfil y el adaptador lo soportan explícitamente.
 *
 * <p>El canvas conceptual mantiene su lógica propia de anclas. El canvas común no debe
 * prometer reconexión de extremos por defecto: cada adapter debe declarar el soporte
 * mediante {@link CanvasEndpointPort} cuando la operación sea semánticamente segura.</p>
 */
public final class CanvasEndpointInteractionPolicy {

    private CanvasEndpointInteractionPolicy() {
        // Utilidad estática.
    }

    public static boolean isEndpointDraggingEnabled(
            DiagramInteractionProfile profile,
            InteractiveCanvasAdapter adapter
    ) {
        Objects.requireNonNull(profile, "El perfil de interacción no puede ser null");
        Objects.requireNonNull(adapter, "El adaptador no puede ser null");
        return profile.supportsEndpointDragging()
                && adapter instanceof CanvasEndpointPort endpointPort
                && endpointPort.supportsEndpointDragging();
    }
}
