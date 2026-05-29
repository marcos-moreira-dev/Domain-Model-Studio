package com.marcosmoreira.domainmodelstudio.domain.projectpayload;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.Objects;

/** Referencia estable al payload efectivo detectado en un proyecto. */
public record ProjectPayloadReference(
        DiagramTypeId diagramTypeId,
        String payloadKind,
        String displayName
) implements ProjectPayload {

    public ProjectPayloadReference {
        Objects.requireNonNull(diagramTypeId, "diagramTypeId");
        payloadKind = requireText(payloadKind, "payloadKind");
        displayName = requireText(displayName, "displayName");
    }

    public static ProjectPayloadReference from(ProjectPayloadDescriptor descriptor) {
        Objects.requireNonNull(descriptor, "descriptor");
        return new ProjectPayloadReference(
                descriptor.diagramTypeId(),
                descriptor.payloadKind(),
                descriptor.displayName());
    }

    private static String requireText(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName);
        if (value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " no puede estar vacío.");
        }
        return value.strip();
    }
}
