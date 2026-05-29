package com.marcosmoreira.domainmodelstudio.application.theory;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.Optional;

/** Puerto de lectura para teoría de diagramas. */
public interface TheoryCatalog {

    Optional<TheoryTopic> findById(TheoryTopicId id);

    Optional<TheoryTopic> findByDiagramType(DiagramTypeId diagramTypeId);
}
