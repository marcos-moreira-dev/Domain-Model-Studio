package com.marcosmoreira.domainmodelstudio.application.catalog;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCategory;
import java.util.List;
import java.util.Objects;

/** Lista categorías disponibles para el diálogo Nuevo proyecto. */
public final class ListDiagramCategoriesUseCase {

    private final DiagramCategoryCatalog catalog;

    public ListDiagramCategoriesUseCase(DiagramCategoryCatalog catalog) {
        this.catalog = Objects.requireNonNull(catalog, "catalog");
    }

    public List<DiagramCategory> execute() {
        return catalog.findAll();
    }
}
