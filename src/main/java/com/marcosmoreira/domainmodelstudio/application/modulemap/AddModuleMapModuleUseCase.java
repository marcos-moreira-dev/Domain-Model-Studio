package com.marcosmoreira.domainmodelstudio.application.modulemap;

import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleKind;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleMapDocument;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleNode;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleStatus;
import java.text.Normalizer;
import java.util.Locale;
import java.util.Objects;

/** Agrega módulos y submódulos al mapa de módulos. */
public final class AddModuleMapModuleUseCase {

    public ModuleMapDocument addRootModule(ModuleMapDocument document, String displayName) {
        Objects.requireNonNull(document, "document");
        String name = normalizedName(displayName, "Módulo");
        String id = uniqueId(document, slug(name));
        return document.withModule(new ModuleNode(id, name, "", ModuleKind.MAIN, ModuleStatus.PLANNED,
                "", "", java.util.List.of(), ""));
    }

    public ModuleMapDocument addSubmodule(ModuleMapDocument document, String parentModuleId, String displayName) {
        Objects.requireNonNull(document, "document");
        String parentId = normalize(parentModuleId);
        if (parentId.isBlank()) {
            throw new IllegalArgumentException("Selecciona un módulo padre antes de agregar submódulo.");
        }
        document.moduleById(parentId).orElseThrow(() -> new IllegalArgumentException("No existe el módulo padre: " + parentId));
        String name = normalizedName(displayName, "Submódulo");
        String id = uniqueId(document, parentId + "_" + slug(name));
        return document.withModule(new ModuleNode(id, name, parentId, ModuleKind.SUPPORT, ModuleStatus.PLANNED,
                "", "", java.util.List.of(), ""));
    }

    private String uniqueId(ModuleMapDocument document, String base) {
        String normalizedBase = base == null || base.isBlank() ? "modulo" : base;
        String candidate = normalizedBase;
        int counter = 2;
        while (document.moduleById(candidate).isPresent()) {
            candidate = normalizedBase + "_" + counter++;
        }
        return candidate;
    }

    private String normalizedName(String displayName, String fallback) {
        String normalized = normalize(displayName);
        return normalized.isBlank() ? fallback : normalized;
    }

    private String slug(String value) {
        String normalized = Normalizer.normalize(normalize(value), Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "")
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "_")
                .replaceAll("^_+|_+$", "");
        return normalized.isBlank() ? "modulo" : normalized;
    }

    private String normalize(String value) {
        return value == null ? "" : value.strip();
    }
}
