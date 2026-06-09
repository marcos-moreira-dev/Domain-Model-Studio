package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlRelationKind;

/** Estado de filtros visuales aplicado sobre las vistas internas de UML Clases. */
record UmlClassDiagramFilterState(
        String viewId,
        String searchQuery,
        UmlClassKind classKind,
        UmlRelationKind relationKind
) {
    UmlClassDiagramFilterState {
        viewId = normalize(viewId);
        searchQuery = normalize(searchQuery).toLowerCase(java.util.Locale.ROOT);
    }

    static UmlClassDiagramFilterState all() {
        return new UmlClassDiagramFilterState("", "", null, null);
    }

    boolean hasViewFilter() {
        return !viewId.isBlank();
    }

    boolean hasSearchQuery() {
        return !searchQuery.isBlank();
    }

    boolean hasClassKindFilter() {
        return classKind != null;
    }

    boolean hasRelationKindFilter() {
        return relationKind != null;
    }

    boolean active() {
        return hasViewFilter() || hasSearchQuery() || hasClassKindFilter() || hasRelationKindFilter();
    }

    private static String normalize(String value) {
        return value == null ? "" : value.strip();
    }
}
