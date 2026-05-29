package com.marcosmoreira.domainmodelstudio.domain.assets;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** Catálogo inmutable de recursos asociados al proyecto .dms. */
public final class ProjectAssetCatalog {

    private final List<ProjectAssetReference> references;

    public ProjectAssetCatalog(List<ProjectAssetReference> references) {
        LinkedHashMap<String, ProjectAssetReference> byId = new LinkedHashMap<>();
        if (references != null) {
            for (ProjectAssetReference reference : references) {
                if (reference != null) {
                    byId.put(reference.id(), reference);
                }
            }
        }
        this.references = List.copyOf(byId.values());
    }

    public static ProjectAssetCatalog empty() {
        return new ProjectAssetCatalog(List.of());
    }

    public static ProjectAssetCatalog of(List<ProjectAssetReference> references) {
        return new ProjectAssetCatalog(references);
    }

    public List<ProjectAssetReference> references() {
        return references;
    }

    public boolean isEmpty() {
        return references.isEmpty();
    }

    public int size() {
        return references.size();
    }

    public Optional<ProjectAssetReference> byId(String id) {
        String normalized = id == null ? "" : id.trim();
        if (normalized.isEmpty()) {
            return Optional.empty();
        }
        return references.stream()
                .filter(reference -> reference.id().equals(normalized))
                .findFirst();
    }

    public List<ProjectAssetReference> byKind(ProjectAssetKind kind) {
        ProjectAssetKind resolvedKind = kind == null ? ProjectAssetKind.OTHER : kind;
        return references.stream()
                .filter(reference -> reference.kind() == resolvedKind)
                .toList();
    }

    public ProjectAssetCatalog withReference(ProjectAssetReference reference) {
        if (reference == null) {
            return this;
        }
        Map<String, ProjectAssetReference> copy = new LinkedHashMap<>();
        for (ProjectAssetReference current : references) {
            copy.put(current.id(), current);
        }
        copy.put(reference.id(), reference);
        return new ProjectAssetCatalog(new ArrayList<>(copy.values()));
    }

    public ProjectAssetCatalog withoutReference(String id) {
        String normalized = id == null ? "" : id.trim();
        if (normalized.isEmpty()) {
            return this;
        }
        return new ProjectAssetCatalog(references.stream()
                .filter(reference -> !reference.id().equals(normalized))
                .toList());
    }
}
