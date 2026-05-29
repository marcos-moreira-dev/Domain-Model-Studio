package com.marcosmoreira.domainmodelstudio.domain.projectpayload;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.Objects;

/** Descriptor de dominio liviano para payloads de proyecto. */
public record ProjectPayloadDescriptor(
        DiagramTypeId diagramTypeId,
        String payloadKind,
        String displayName
) {

    public ProjectPayloadDescriptor {
        Objects.requireNonNull(diagramTypeId, "diagramTypeId");
        payloadKind = requireText(payloadKind, "payloadKind");
        displayName = requireText(displayName, "displayName");
    }

    private static String requireText(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName);
        if (value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " no puede estar vacío.");
        }
        return value.strip();
    }
}
