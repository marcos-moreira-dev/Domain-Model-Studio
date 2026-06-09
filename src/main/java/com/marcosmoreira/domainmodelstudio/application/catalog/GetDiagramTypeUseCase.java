package com.marcosmoreira.domainmodelstudio.application.catalog;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeDescriptor;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.Objects;
import java.util.Optional;

/** Obtiene un tipo de diagrama por su identificador estable. */
public final class GetDiagramTypeUseCase {

    private final DiagramTypeRegistry registry;

    public GetDiagramTypeUseCase(DiagramTypeRegistry registry) {
        this.registry = Objects.requireNonNull(registry, "registry");
    }

    public Optional<DiagramTypeDescriptor> execute(DiagramTypeId id) {
        return registry.findById(Objects.requireNonNull(id, "id"));
    }
}
