package com.marcosmoreira.domainmodelstudio.domain.assets;

import java.util.Locale;

/** Tipo lógico de recurso empacado/referenciado por un proyecto .dms. */
public enum ProjectAssetKind {
    LOGO,
    IMAGE,
    DOCUMENT,
    EXPORT,
    OTHER;

    public static ProjectAssetKind fromStoredValue(String value) {
        String normalized = value == null ? "" : value.trim().toUpperCase(Locale.ROOT).replace('-', '_');
        for (ProjectAssetKind kind : values()) {
            if (kind.name().equals(normalized)) {
                return kind;
            }
        }
        return OTHER;
    }
}
