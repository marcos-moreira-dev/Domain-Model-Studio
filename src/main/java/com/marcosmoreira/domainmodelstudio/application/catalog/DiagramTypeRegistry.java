package com.marcosmoreira.domainmodelstudio.application.catalog;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCategoryId;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeDescriptor;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.List;
import java.util.Optional;

/** Puerto de lectura para tipos de diagrama registrados. */
public interface DiagramTypeRegistry {

    List<DiagramTypeDescriptor> findAll();

    List<DiagramTypeDescriptor> findByCategory(DiagramCategoryId categoryId);

    Optional<DiagramTypeDescriptor> findById(DiagramTypeId id);
}
