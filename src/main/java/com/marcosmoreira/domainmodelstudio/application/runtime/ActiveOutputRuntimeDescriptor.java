package com.marcosmoreira.domainmodelstudio.application.runtime;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCapability;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

/** Contrato runtime declarativo para la salida activa/exportable de un tipo. */
public final class ActiveOutputRuntimeDescriptor {

    private final DiagramTypeId diagramTypeId;
    private final boolean visualOutput;
    private final boolean documentOutput;
    private final EnumSet<DiagramCapability> exportCapabilities;

    public ActiveOutputRuntimeDescriptor(
            DiagramTypeId diagramTypeId,
            boolean visualOutput,
            boolean documentOutput,
            Set<DiagramCapability> exportCapabilities
    ) {
        this.diagramTypeId = Objects.requireNonNull(diagramTypeId, "diagramTypeId");
        this.visualOutput = visualOutput;
        this.documentOutput = documentOutput;
        Objects.requireNonNull(exportCapabilities, "exportCapabilities");
        this.exportCapabilities = exportCapabilities.isEmpty()
                ? EnumSet.noneOf(DiagramCapability.class)
                : EnumSet.copyOf(exportCapabilities);
    }

    public DiagramTypeId diagramTypeId() {
        return diagramTypeId;
    }

    public boolean visualOutput() {
        return visualOutput;
    }

    public boolean documentOutput() {
        return documentOutput;
    }

    public Set<DiagramCapability> exportCapabilities() {
        return Collections.unmodifiableSet(exportCapabilities);
    }

    public boolean exports(DiagramCapability capability) {
        return exportCapabilities.contains(Objects.requireNonNull(capability, "capability"));
    }
}
