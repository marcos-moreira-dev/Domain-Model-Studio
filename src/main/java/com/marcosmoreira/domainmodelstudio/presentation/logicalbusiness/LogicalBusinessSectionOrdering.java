package com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessSection;

/** Orden natural de secciones canónicas sec-0, sec-1, sec-2... para el árbol y la ficha. */
final class LogicalBusinessSectionOrdering {

    private LogicalBusinessSectionOrdering() {
    }

    static int index(LogicalBusinessSection section) {
        if (section == null || section.id() == null) {
            return Integer.MAX_VALUE;
        }
        String id = section.id().strip().toLowerCase();
        int dash = id.lastIndexOf('-');
        if (dash >= 0 && dash + 1 < id.length()) {
            try {
                return Integer.parseInt(id.substring(dash + 1));
            } catch (NumberFormatException ignored) {
                return Integer.MAX_VALUE;
            }
        }
        return Integer.MAX_VALUE;
    }
}
