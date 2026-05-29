package com.marcosmoreira.domainmodelstudio.infrastructure.json;

import com.marcosmoreira.domainmodelstudio.domain.assets.ProjectAssetCatalog;
import com.marcosmoreira.domainmodelstudio.domain.assets.ProjectAssetKind;
import com.marcosmoreira.domainmodelstudio.domain.assets.ProjectAssetReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/** Lee el bloque opcional assets del formato .dms v3. */
final class DmsProjectAssetsJsonReader {

    ProjectAssetCatalog read(Object rawAssets) {
        if (rawAssets == null) {
            return ProjectAssetCatalog.empty();
        }
        List<ProjectAssetReference> references = new ArrayList<>();
        Object items = rawAssets;
        if (rawAssets instanceof Map<?, ?> rawMap) {
            Map<String, Object> object = asStringObjectMap(rawMap);
            items = object.getOrDefault("items", List.of());
        }
        if (items instanceof List<?> list) {
            for (Object item : list) {
                if (item instanceof Map<?, ?> rawItem) {
                    references.add(readReference(asStringObjectMap(rawItem)));
                }
            }
        }
        return ProjectAssetCatalog.of(references);
    }

    private ProjectAssetReference readReference(Map<String, Object> object) {
        return new ProjectAssetReference(
                text(object, "id"),
                ProjectAssetKind.fromStoredValue(text(object, "kind")),
                text(object, "displayName"),
                text(object, "relativePath"),
                text(object, "mimeType"),
                text(object, "purpose"),
                text(object, "checksum"),
                text(object, "notes")
        );
    }

    private static Map<String, Object> asStringObjectMap(Map<?, ?> raw) {
        java.util.LinkedHashMap<String, Object> copy = new java.util.LinkedHashMap<>();
        for (Map.Entry<?, ?> entry : raw.entrySet()) {
            copy.put(String.valueOf(entry.getKey()), entry.getValue());
        }
        return copy;
    }

    private static String text(Map<String, Object> object, String key) {
        return Objects.toString(object.get(key), "").trim();
    }
}
