package com.marcosmoreira.domainmodelstudio.domain.datadictionary;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/** Entidad o tabla lógica documentada en el diccionario de datos. */
public record DataDictionaryEntity(
        String id,
        String displayName,
        String technicalName,
        String description,
        String moduleName,
        DataEntityKind kind,
        String origin,
        DataDictionaryStatus status,
        List<DataDictionaryField> fields,
        String notes
) {

    public DataDictionaryEntity {
        id = requireText(id, "id");
        displayName = normalizeOrDefault(displayName, id);
        technicalName = normalizeOrDefault(technicalName, id);
        description = normalizeOptional(description);
        moduleName = normalizeOptional(moduleName);
        kind = kind == null ? DataEntityKind.MAIN : kind;
        origin = normalizeOrDefault(origin, "levantamiento manual");
        status = status == null ? DataDictionaryStatus.DRAFT : status;
        fields = List.copyOf(Objects.requireNonNull(fields, "fields"));
        notes = normalizeOptional(notes);
        ensureUniqueFieldNames(fields);
    }

    public DataDictionaryEntity(
            String name,
            String description,
            List<DataDictionaryField> fields
    ) {
        this(name, name, name, description, "", DataEntityKind.MAIN, "levantamiento manual",
                DataDictionaryStatus.DRAFT, fields, "");
    }

    public DataDictionaryEntity withField(DataDictionaryField field) {
        Objects.requireNonNull(field, "field");
        return withFields(concat(fields, field));
    }

    public DataDictionaryEntity withFields(List<DataDictionaryField> updatedFields) {
        return new DataDictionaryEntity(id, displayName, technicalName, description, moduleName,
                kind, origin, status, updatedFields, notes);
    }

    public java.util.Optional<DataDictionaryField> fieldByName(String fieldName) {
        if (fieldName == null || fieldName.isBlank()) {
            return java.util.Optional.empty();
        }
        String normalized = fieldName.strip();
        return fields.stream()
                .filter(field -> field.name().equalsIgnoreCase(normalized)
                        || field.technicalName().equalsIgnoreCase(normalized))
                .findFirst();
    }

    public DataDictionaryEntity withUpdatedField(String originalFieldName, DataDictionaryField updatedField) {
        Objects.requireNonNull(updatedField, "updatedField");
        String normalized = requireText(originalFieldName, "originalFieldName");
        java.util.ArrayList<DataDictionaryField> updated = new java.util.ArrayList<>();
        boolean replaced = false;
        for (DataDictionaryField field : fields) {
            if (field.name().equalsIgnoreCase(normalized) || field.technicalName().equalsIgnoreCase(normalized)) {
                updated.add(updatedField);
                replaced = true;
            } else {
                updated.add(field);
            }
        }
        if (!replaced) {
            throw new IllegalArgumentException("No existe campo en entidad: " + normalized);
        }
        return withFields(updated);
    }

    public DataDictionaryEntity withoutField(String fieldName) {
        String normalized = requireText(fieldName, "fieldName");
        java.util.ArrayList<DataDictionaryField> updated = new java.util.ArrayList<>();
        boolean removed = false;
        for (DataDictionaryField field : fields) {
            if (field.name().equalsIgnoreCase(normalized) || field.technicalName().equalsIgnoreCase(normalized)) {
                removed = true;
            } else {
                updated.add(field);
            }
        }
        if (!removed) {
            throw new IllegalArgumentException("No existe campo en entidad: " + normalized);
        }
        return withFields(updated);
    }

    public int fieldCount() {
        return fields.size();
    }

    public boolean hasPrimaryKey() {
        return fields.stream().anyMatch(DataDictionaryField::isPrimaryKey);
    }

    private static List<DataDictionaryField> concat(List<DataDictionaryField> base, DataDictionaryField extra) {
        java.util.ArrayList<DataDictionaryField> updated = new java.util.ArrayList<>(base);
        updated.add(extra);
        return List.copyOf(updated);
    }

    private static void ensureUniqueFieldNames(List<DataDictionaryField> fields) {
        Set<String> names = new LinkedHashSet<>();
        Set<String> technicalNames = new LinkedHashSet<>();
        for (DataDictionaryField field : fields) {
            if (!names.add(field.name().toLowerCase())) {
                throw new IllegalArgumentException("Campo duplicado en entidad: " + field.name());
            }
            if (!technicalNames.add(field.technicalName().toLowerCase())) {
                throw new IllegalArgumentException("Nombre técnico de campo duplicado: " + field.technicalName());
            }
        }
    }

    private static String requireText(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName);
        if (value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " no puede estar vacío.");
        }
        return value.strip();
    }

    private static String normalizeOrDefault(String value, String defaultValue) {
        String normalized = normalizeOptional(value);
        return normalized.isBlank() ? defaultValue : normalized;
    }

    private static String normalizeOptional(String value) {
        return value == null ? "" : value.strip();
    }
}
