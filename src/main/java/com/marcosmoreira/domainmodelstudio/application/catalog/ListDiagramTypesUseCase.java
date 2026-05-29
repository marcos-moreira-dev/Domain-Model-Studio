package com.marcosmoreira.domainmodelstudio.application.catalog;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCategoryId;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeDescriptor;
import java.util.List;
import java.util.Objects;

/** Lista tipos de diagrama, opcionalmente filtrados por categoría. */
public final class ListDiagramTypesUseCase {

    private final DiagramTypeRegistry registry;

    public ListDiagramTypesUseCase(DiagramTypeRegistry registry) {
        this.registry = Objects.requireNonNull(registry, "registry");
    }

    public List<DiagramTypeDescriptor> execute() {
        return registry.findAll();
    }

    public List<DiagramTypeDescriptor> execute(DiagramCategoryId categoryId) {
        return registry.findByCategory(Objects.requireNonNull(categoryId, "categoryId"));
    }
}
