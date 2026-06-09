package com.marcosmoreira.domainmodelstudio.application.runtime;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.projectpayload.ProjectPayloadDescriptor;
import java.util.Objects;
import java.util.Optional;

/**
 * Descriptor runtime de payload y persistencia .dms para un tipo de proyecto.
 *
 * <p>En esta tanda es un contrato de transición: describe y valida el payload
 * efectivo sin cambiar todavía el formato .dms v3 ni reemplazar los campos
 * especializados existentes en {@code DiagramProject}.</p>
 */
public final class PayloadRuntimeDescriptor {

    private final ProjectPayloadDescriptor descriptor;
    private final String jsonSectionName;
    private final boolean specializedPayload;
    private final PayloadPresenceProbe presenceProbe;
    private final PayloadConsistencyProbe consistencyProbe;

    public PayloadRuntimeDescriptor(
            DiagramTypeId diagramTypeId,
            String payloadKind,
            String displayName,
            String jsonSectionName,
            boolean specializedPayload,
            PayloadPresenceProbe presenceProbe,
            PayloadConsistencyProbe consistencyProbe
    ) {
        this.descriptor = new ProjectPayloadDescriptor(diagramTypeId, payloadKind, displayName);
        this.jsonSectionName = requireText(jsonSectionName, "jsonSectionName");
        this.specializedPayload = specializedPayload;
        this.presenceProbe = Objects.requireNonNull(presenceProbe, "presenceProbe");
        this.consistencyProbe = Objects.requireNonNull(consistencyProbe, "consistencyProbe");
    }

    public DiagramTypeId diagramTypeId() {
        return descriptor.diagramTypeId();
    }

    public String payloadKind() {
        return descriptor.payloadKind();
    }

    public String displayName() {
        return descriptor.displayName();
    }

    public String jsonSectionName() {
        return jsonSectionName;
    }

    public boolean specializedPayload() {
        return specializedPayload;
    }

    public ProjectPayloadDescriptor descriptor() {
        return descriptor;
    }

    public boolean presentIn(DiagramProject project) {
        return presenceProbe.presentIn(Objects.requireNonNull(project, "project"));
    }

    public Optional<String> consistencyProblem(DiagramProject project) {
        return consistencyProbe.validate(Objects.requireNonNull(project, "project"));
    }

    private static String requireText(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName);
        if (value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " no puede estar vacío.");
        }
        return value.strip();
    }
}
