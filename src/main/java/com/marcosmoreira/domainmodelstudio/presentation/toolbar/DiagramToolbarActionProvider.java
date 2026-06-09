package com.marcosmoreira.domainmodelstudio.presentation.toolbar;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.List;

/** Provee las herramientas visibles de acuerdo con el tipo de diagrama activo. */
public interface DiagramToolbarActionProvider {

    List<DiagramToolbarAction> actionsFor(DiagramTypeId diagramTypeId);
}
