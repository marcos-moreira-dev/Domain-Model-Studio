package com.marcosmoreira.domainmodelstudio.infrastructure.batchexport;

import java.text.Normalizer;

/** Política simple para nombres de carpetas exportadas legibles y seguros. */
public final class ExportFolderNamePolicy {

    public String toFolderName(String value) {
        String base = value == null || value.isBlank() ? "proyecto" : value.strip().toLowerCase();
        String normalized = Normalizer.normalize(base, Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "")
                .replaceAll("[^a-z0-9]+", "_")
                .replaceAll("^_+|_+$", "");
        return normalized.isBlank() ? "proyecto" : normalized;
    }
}
