package com.marcosmoreira.domainmodelstudio.application.editing;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramModel;
import java.text.Normalizer;
import java.util.Locale;

/** Utilidades internas para crear IDs humanos y estables durante edición manual. */
final class ConceptualEditingIds {

    private ConceptualEditingIds() {
        // Utilidad interna.
    }

    static DiagramElementId uniqueElementId(DiagramModel model, String baseText) {
        String slug = slug(baseText);
        String candidate = slug;
        int suffix = 2;
        while (model.containsElement(DiagramElementId.of(candidate))) {
            candidate = slug + "_" + suffix++;
        }
        return DiagramElementId.of(candidate);
    }

    static DiagramElementId connectorId(DiagramElementId baseId, String... parts) {
        StringBuilder builder = new StringBuilder("conn_").append(baseId.value());
        for (String part : parts) {
            builder.append('_').append(slug(part));
        }
        return DiagramElementId.of(builder.toString());
    }

    static String slug(String raw) {
        String value = raw == null ? "elemento" : raw.trim().toLowerCase(Locale.ROOT);
        value = Normalizer.normalize(value, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        value = value.replaceAll("[^a-z0-9]+", "_").replaceAll("^_+|_+$", "");
        return value.isBlank() ? "elemento" : value;
    }
}
