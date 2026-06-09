package com.marcosmoreira.domainmodelstudio.application.catalog;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCategory;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCategoryId;
import java.util.List;
import java.util.Optional;

/** Puerto de lectura para categorías de diagramas. */
public interface DiagramCategoryCatalog {

    List<DiagramCategory> findAll();

    Optional<DiagramCategory> findById(DiagramCategoryId id);
}
