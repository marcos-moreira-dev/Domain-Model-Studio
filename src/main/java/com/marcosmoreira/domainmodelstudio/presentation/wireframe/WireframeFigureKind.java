package com.marcosmoreira.domainmodelstudio.presentation.wireframe;

/** Clasificación visual de figuras de wireframe para evitar maquetas genéricas. */
enum WireframeFigureKind {
    SCREEN,
    TOP_BAR,
    SIDEBAR,
    SECTION,
    PANEL,
    CARD,
    FORM,
    FIELD,
    FILTER,
    SEARCH,
    TABLE,
    PAGINATION,
    BUTTON,
    TABS,
    MODAL,
    ALERT,
    CHART,
    REPORT,
    DETAIL,
    MENU,
    STEPPER,
    BADGE,
    EMPTY_STATE,
    DOCUMENT_LIST,
    CALENDAR,
    APPROVAL_PANEL,
    SUMMARY,
    OTHER;

    static WireframeFigureKind fromCanvasKind(String rawKind) {
        String normalized = rawKind == null ? "" : rawKind.strip().toLowerCase(java.util.Locale.ROOT);
        if (normalized.equals("wireframe-screen")) {
            return SCREEN;
        }
        String suffix = normalized.replace("wireframe-component-", "").replace('-', '_').toUpperCase(java.util.Locale.ROOT);
        try {
            return WireframeFigureKind.valueOf(suffix);
        } catch (IllegalArgumentException ignored) {
            return OTHER;
        }
    }

    boolean isFieldLike() {
        return this == FORM || this == FIELD || this == FILTER || this == SEARCH;
    }

    boolean isPanelLike() {
        return this == SECTION || this == PANEL || this == CARD || this == DETAIL || this == SUMMARY || this == APPROVAL_PANEL;
    }
}
