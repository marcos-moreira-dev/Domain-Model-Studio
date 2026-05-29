package com.marcosmoreira.domainmodelstudio.application.catalog;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCapability;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCapabilitySet;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;

/** Puerto de lectura para capacidades declaradas por tipo de diagrama. */
public interface DiagramCapabilityCatalog {

    DiagramCapabilitySet capabilitiesOf(DiagramTypeId diagramTypeId);

    default boolean supports(DiagramTypeId diagramTypeId, DiagramCapability capability) {
        return capabilitiesOf(diagramTypeId).has(capability);
    }
}
