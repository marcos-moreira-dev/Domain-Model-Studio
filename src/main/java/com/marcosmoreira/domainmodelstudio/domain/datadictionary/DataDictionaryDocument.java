package com.marcosmoreira.domainmodelstudio.domain.datadictionary;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/** Documento principal del PDF y Markdown de diccionario de datos. */
public record DataDictionaryDocument(
        String projectName,
        String clientName,
        String organizationName,
        String author,
        String version,
        LocalDate documentDate,
        DataDictionaryStatus status,
        String introduction,
        String logoReference,
        List<DataDictionaryEntity> entities,
        String notes
) {

    public DataDictionaryDocument {
        projectName = requireText(projectName, "projectName");
        clientName = normalizeOptional(clientName);
        organizationName = normalizeOptional(organizationName);
        author = normalizeOptional(author);
        version = normalizeOrDefault(version, "borrador");
        Objects.requireNonNull(documentDate, "documentDate");
        status = status == null ? DataDictionaryStatus.DRAFT : status;
        introduction = normalizeOptional(introduction);
        logoReference = normalizeOptional(logoReference);
        entities = List.copyOf(Objects.requireNonNull(entities, "entities"));
        notes = normalizeOptional(notes);
        ensureUniqueEntityNames(entities);
    }

    public DataDictionaryDocument(
            String projectName,
            String clientName,
            String author,
            String version,
            LocalDate documentDate,
            DataDictionaryStatus status,
            List<DataDictionaryEntity> entities,
            String notes
    ) {
        this(projectName, clientName, "", author, version, documentDate, status, "", "", entities, notes);
    }

    public DataDictionaryDocument(
            String projectName,
            String version,
            LocalDate documentDate,
            List<DataDictionaryEntity> entities
    ) {
        this(projectName, "", "", "", version, documentDate, DataDictionaryStatus.DRAFT, "", "", entities, "");
    }

    public static DataDictionaryDocument blank(String projectName, LocalDate documentDate) {
        return new DataDictionaryDocument(projectName, "", "", "", "borrador", documentDate,
                DataDictionaryStatus.DRAFT, "", "", List.of(), "");
    }

    public DataDictionaryDocument withDocumentDetails(
            String updatedProjectName,
            String updatedClientName,
            String updatedOrganizationName,
            String updatedAuthor,
            String updatedVersion,
            DataDictionaryStatus updatedStatus,
            String updatedIntroduction,
            String updatedLogoReference,
            String updatedNotes
    ) {
        return new DataDictionaryDocument(updatedProjectName, updatedClientName, updatedOrganizationName,
                updatedAuthor, updatedVersion, documentDate, updatedStatus, updatedIntroduction,
                updatedLogoReference, entities, updatedNotes);
    }

    public DataDictionaryDocument withEntity(DataDictionaryEntity entity) {
        Objects.requireNonNull(entity, "entity");
        java.util.ArrayList<DataDictionaryEntity> updated = new java.util.ArrayList<>(entities);
        updated.add(entity);
        return withEntities(updated);
    }

    public DataDictionaryDocument withEntities(List<DataDictionaryEntity> updatedEntities) {
        return new DataDictionaryDocument(projectName, clientName, organizationName, author, version,
                documentDate, status, introduction, logoReference, updatedEntities, notes);
    }

    public java.util.Optional<DataDictionaryEntity> entityById(String entityId) {
        if (entityId == null || entityId.isBlank()) {
            return java.util.Optional.empty();
        }
        String normalized = entityId.strip();
        return entities.stream()
                .filter(entity -> entity.id().equalsIgnoreCase(normalized))
                .findFirst();
    }

    public DataDictionaryDocument withUpdatedEntity(DataDictionaryEntity updatedEntity) {
        Objects.requireNonNull(updatedEntity, "updatedEntity");
        java.util.ArrayList<DataDictionaryEntity> updated = new java.util.ArrayList<>();
        boolean replaced = false;
        for (DataDictionaryEntity entity : entities) {
            if (entity.id().equalsIgnoreCase(updatedEntity.id())) {
                updated.add(updatedEntity);
                replaced = true;
            } else {
                updated.add(entity);
            }
        }
        if (!replaced) {
            throw new IllegalArgumentException("No existe entidad en diccionario: " + updatedEntity.id());
        }
        return withEntities(updated);
    }

    public DataDictionaryDocument withoutEntity(String entityId) {
        String normalized = requireText(entityId, "entityId");
        java.util.ArrayList<DataDictionaryEntity> updated = new java.util.ArrayList<>();
        boolean removed = false;
        for (DataDictionaryEntity entity : entities) {
            if (entity.id().equalsIgnoreCase(normalized)) {
                removed = true;
            } else {
                updated.add(entity);
            }
        }
        if (!removed) {
            throw new IllegalArgumentException("No existe entidad en diccionario: " + normalized);
        }
        return withEntities(updated);
    }

    public int entityCount() {
        return entities.size();
    }

    public int fieldCount() {
        return entities.stream().mapToInt(DataDictionaryEntity::fieldCount).sum();
    }

    public boolean isEmpty() {
        return entities.isEmpty();
    }

    public boolean hasLogoReference() {
        return !logoReference.isBlank();
    }

    public boolean hasIntroduction() {
        return !introduction.isBlank();
    }

    private static void ensureUniqueEntityNames(List<DataDictionaryEntity> entities) {
        Set<String> ids = new LinkedHashSet<>();
        Set<String> technicalNames = new LinkedHashSet<>();
        for (DataDictionaryEntity entity : entities) {
            if (!ids.add(entity.id().toLowerCase())) {
                throw new IllegalArgumentException("Entidad duplicada en diccionario: " + entity.id());
            }
            if (!technicalNames.add(entity.technicalName().toLowerCase())) {
                throw new IllegalArgumentException("Nombre técnico de entidad duplicado: " + entity.technicalName());
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
