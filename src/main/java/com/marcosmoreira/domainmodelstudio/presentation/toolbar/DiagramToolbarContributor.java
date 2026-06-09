package com.marcosmoreira.domainmodelstudio.presentation.toolbar;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.List;

/** Aporta el conjunto base de acciones de toolbar para una familia de diagramas. */
interface DiagramToolbarContributor {

    boolean supports(DiagramTypeId diagramTypeId);

    List<DiagramToolbarAction> actionsFor(DiagramTypeId diagramTypeId);
}
