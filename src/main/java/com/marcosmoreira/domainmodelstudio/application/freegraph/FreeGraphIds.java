package com.marcosmoreira.domainmodelstudio.application.freegraph;

import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphDocument;
import java.text.Normalizer;
import java.util.Locale;

/**
 * Genera identificadores humanos y estables para elementos de Grafo libre.
 *
 * <p>Esta utilidad pertenece a aplicación, no al dominio, porque la política de nombres
 * depende de flujos de edición/importación y puede cambiar sin alterar las reglas puras del grafo.</p>
 */
public final class FreeGraphIds {

    private FreeGraphIds() {
    }

    public static String uniqueNodeId(FreeGraphDocument document, String title) {
        String base = slug(title, "nodo");
        String candidate = base;
        int counter = 2;
        while (document.nodeById(candidate).isPresent()) {
            candidate = base + "_" + counter++;
        }
        return candidate;
    }

    public static String uniqueEdgeId(FreeGraphDocument document, String sourceNodeId, String targetNodeId, String label) {
        String semanticBase = normalize(label).isBlank()
                ? normalize(sourceNodeId) + "_to_" + normalize(targetNodeId)
                : normalize(sourceNodeId) + "_to_" + normalize(targetNodeId) + "_" + slug(label, "relacion");
        String base = slug(semanticBase, "relacion");
        String candidate = base;
        int counter = 2;
        while (document.edgeById(candidate).isPresent()) {
            candidate = base + "_" + counter++;
        }
        return candidate;
    }

    public static String slug(String value, String fallback) {
        String normalized = Normalizer.normalize(normalize(value), Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "")
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "_")
                .replaceAll("^_+|_+$", "");
        return normalized.isBlank() ? fallback : normalized;
    }

    static String normalize(String value) {
        return value == null ? "" : value.strip();
    }
}
