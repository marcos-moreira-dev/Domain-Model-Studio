package com.marcosmoreira.domainmodelstudio.application.examples;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.List;
import java.util.Objects;

/** Catálogo de ejemplos oficiales seleccionables desde la interfaz. */
public interface OfficialExampleCatalog {

    List<OfficialExampleDescriptor> findAll();

    default List<OfficialExampleDescriptor> findImportable() {
        return findAll().stream()
                .filter(OfficialExampleDescriptor::importable)
                .toList();
    }

    default List<OfficialExampleDescriptor> findByDiagramType(DiagramTypeId diagramTypeId) {
        Objects.requireNonNull(diagramTypeId, "diagramTypeId");
        return findAll().stream()
                .filter(example -> example.diagramTypeId().equals(diagramTypeId))
                .toList();
    }
}
