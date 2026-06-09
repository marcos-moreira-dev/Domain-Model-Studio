package com.marcosmoreira.domainmodelstudio.application.datadictionary;

import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryDocument;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryEntity;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryStatus;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataEntityKind;
import java.util.List;
import java.util.Objects;

/** Agrega una entidad lógica al diccionario de datos. */
public final class AddDataDictionaryEntityUseCase {

    public DataDictionaryDocument add(DataDictionaryDocument document, String baseName) {
        Objects.requireNonNull(document, "document");
        String name = uniqueName(document, normalizeBaseName(baseName));
        DataDictionaryEntity entity = new DataDictionaryEntity(
                stableId(name),
                name,
                stableTechnicalName(name),
                "",
                "",
                DataEntityKind.MAIN,
                "levantamiento manual",
                DataDictionaryStatus.DRAFT,
                List.of(),
                "");
        return document.withEntity(entity);
    }

    private static String uniqueName(DataDictionaryDocument document, String baseName) {
        String candidate = baseName;
        int suffix = 2;
        while (containsEntity(document, candidate)) {
            candidate = baseName + " " + suffix++;
        }
        return candidate;
    }

    private static boolean containsEntity(DataDictionaryDocument document, String displayName) {
        String normalized = displayName.strip().toLowerCase(java.util.Locale.ROOT);
        return document.entities().stream()
                .anyMatch(entity -> entity.displayName().equalsIgnoreCase(normalized)
                        || entity.id().equalsIgnoreCase(stableId(displayName))
                        || entity.technicalName().equalsIgnoreCase(stableTechnicalName(displayName)));
    }

    private static String normalizeBaseName(String baseName) {
        String normalized = baseName == null ? "" : baseName.strip();
        return normalized.isBlank() ? "Entidad" : normalized;
    }

    private static String stableId(String value) {
        return stableTechnicalName(value).replace('_', '-');
    }

    private static String stableTechnicalName(String value) {
        String normalized = value == null ? "" : value.toLowerCase(java.util.Locale.ROOT)
                .replaceAll("[^a-z0-9áéíóúñ]+", "_")
                .replaceAll("^_+|_+$", "");
        return normalized.isBlank() ? "entidad" : normalized;
    }
}
