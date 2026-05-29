package com.marcosmoreira.domainmodelstudio.domain.logicalbusiness;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Agregado raíz inmutable del levantamiento lógico del negocio.
 *
 * <p>Este documento es una fuente lógica canónica del negocio. Reúne secciones,
 * elementos lógicos, entidades candidatas y preguntas pendientes con reglas de
 * integridad propias, sin prometer generación automática de otros artefactos.</p>
 *
 * <p>La instancia indexa elementos y entidades para rechazar duplicados, validar
 * referencias de secciones y mantener coherencia interna del expediente.</p>
 */
public final class LogicalBusinessDocument {

    private final String projectName;
    private final String version;
    private final LocalDate documentDate;
    private final LogicalBusinessDocumentStatus documentStatus;
    private final String mainSource;
    private final List<LogicalBusinessSection> sections;
    private final List<LogicalBusinessItem> items;
    private final List<LogicalBusinessEntityCandidate> entityCandidates;
    private final List<LogicalBusinessPendingQuestion> pendingQuestions;
    private final LogicalBusinessMaturity maturity;
    private final String notes;
    private final Map<String, LogicalBusinessItem> itemsById;
    private final Map<String, LogicalBusinessEntityCandidate> entitiesById;

    public LogicalBusinessDocument(
            String projectName,
            String version,
            LocalDate documentDate,
            LogicalBusinessDocumentStatus documentStatus,
            String mainSource,
            List<LogicalBusinessSection> sections,
            List<LogicalBusinessItem> items,
            List<LogicalBusinessEntityCandidate> entityCandidates,
            List<LogicalBusinessPendingQuestion> pendingQuestions,
            LogicalBusinessMaturity maturity,
            String notes
    ) {
        this.projectName = defaulted(projectName, "Levantamiento lógico");
        this.version = defaulted(version, "v0.1");
        this.documentDate = documentDate == null ? LocalDate.now() : documentDate;
        this.documentStatus = documentStatus == null ? LogicalBusinessDocumentStatus.DRAFT : documentStatus;
        this.mainSource = LogicalBusinessText.normalize(mainSource);
        this.sections = List.copyOf(sections == null ? List.of() : sections);
        this.items = List.copyOf(items == null ? List.of() : items);
        this.entityCandidates = List.copyOf(entityCandidates == null ? List.of() : entityCandidates);
        this.pendingQuestions = List.copyOf(pendingQuestions == null ? List.of() : pendingQuestions);
        this.maturity = maturity == null ? LogicalBusinessMaturity.initial() : maturity;
        this.notes = LogicalBusinessText.normalize(notes);
        this.itemsById = indexItems(this.items);
        this.entitiesById = indexEntities(this.entityCandidates);
        validateSections();
        validateEntityRelationships();
    }

    /**
     * Crea un expediente lógico vacío para iniciar una captura guiada.
     */
    public static LogicalBusinessDocument blank(String projectName) {
        return new LogicalBusinessDocument(projectName, "v0.1", LocalDate.now(), LogicalBusinessDocumentStatus.DRAFT,
                "", List.of(), List.of(), List.of(), List.of(), LogicalBusinessMaturity.initial(), "");
    }

    public String projectName() {
        return projectName;
    }

    public String version() {
        return version;
    }

    public LocalDate documentDate() {
        return documentDate;
    }

    public LogicalBusinessDocumentStatus documentStatus() {
        return documentStatus;
    }

    public String mainSource() {
        return mainSource;
    }

    public List<LogicalBusinessSection> sections() {
        return sections;
    }

    public List<LogicalBusinessItem> items() {
        return items;
    }

    public List<LogicalBusinessEntityCandidate> entityCandidates() {
        return entityCandidates;
    }

    public List<LogicalBusinessPendingQuestion> pendingQuestions() {
        return pendingQuestions;
    }

    public LogicalBusinessMaturity maturity() {
        return maturity;
    }

    public String notes() {
        return notes;
    }

    /**
     * Busca un elemento lógico por ID normalizado.
     */
    public Optional<LogicalBusinessItem> itemById(String id) {
        return Optional.ofNullable(itemsById.get(LogicalBusinessText.normalize(id)));
    }

    /**
     * Busca una entidad candidata por ID normalizado.
     */
    public Optional<LogicalBusinessEntityCandidate> entityById(String id) {
        return Optional.ofNullable(entitiesById.get(LogicalBusinessText.normalize(id)));
    }

    public List<LogicalBusinessItem> itemsByKind(LogicalBusinessItemKind kind) {
        Objects.requireNonNull(kind, "kind");
        return items.stream().filter(item -> item.kind() == kind).toList();
    }

    /**
     * Valida bloqueos estructurales mínimos que impiden tratar el documento como fuente lógica consistente.
     */
    public List<LogicalBusinessValidationIssue> structuralIssues() {
        List<LogicalBusinessValidationIssue> issues = new ArrayList<>();
        entityCandidates.stream()
                .filter(entity -> entity.sourceReferences().isEmpty())
                .forEach(entity -> issues.add(LogicalBusinessValidationIssue.blocking(entity.id(),
                        "La entidad candidata debe conservar al menos una fuente lógica.")));
        pendingQuestions.stream()
                .filter(question -> question.priority() == LogicalBusinessQuestionPriority.CRITICAL)
                .filter(question -> question.status() != LogicalBusinessItemStatus.VALIDATED)
                .forEach(question -> issues.add(LogicalBusinessValidationIssue.blocking(question.id(),
                        "La pregunta crítica sigue pendiente.")));
        return List.copyOf(issues);
    }


    /**
     * Devuelve una nueva versión del documento con metadatos editoriales actualizados.
     */
    public LogicalBusinessDocument withHeader(
            String updatedProjectName,
            String updatedVersion,
            LocalDate updatedDocumentDate,
            LogicalBusinessDocumentStatus updatedDocumentStatus,
            String updatedMainSource,
            String updatedNotes
    ) {
        return new LogicalBusinessDocument(updatedProjectName, updatedVersion, updatedDocumentDate,
                updatedDocumentStatus, updatedMainSource, sections, items, entityCandidates, pendingQuestions,
                maturity, updatedNotes);
    }

    public LogicalBusinessDocument withUpdatedSection(LogicalBusinessSection updatedSection) {
        Objects.requireNonNull(updatedSection, "updatedSection");
        return copyWith(replaceById(sections, updatedSection), items, entityCandidates, pendingQuestions, maturity);
    }

    public LogicalBusinessDocument withUpdatedEntityCandidate(LogicalBusinessEntityCandidate updatedEntity) {
        Objects.requireNonNull(updatedEntity, "updatedEntity");
        return copyWith(sections, items, replaceById(entityCandidates, updatedEntity), pendingQuestions, maturity);
    }

    public LogicalBusinessDocument withUpdatedPendingQuestion(LogicalBusinessPendingQuestion updatedQuestion) {
        Objects.requireNonNull(updatedQuestion, "updatedQuestion");
        return copyWith(sections, items, entityCandidates, replaceById(pendingQuestions, updatedQuestion), maturity);
    }

    /**
     * Agrega un elemento lógico nuevo y rechaza IDs duplicados.
     */
    public LogicalBusinessDocument withItem(LogicalBusinessItem item) {
        Objects.requireNonNull(item, "item");
        if (itemsById.containsKey(item.id())) {
            throw new IllegalArgumentException("Ya existe un elemento lógico con ID: " + item.id());
        }
        List<LogicalBusinessItem> updated = new ArrayList<>(items);
        updated.add(item);
        return copyWith(sections, updated, entityCandidates, pendingQuestions, maturity);
    }

    public LogicalBusinessDocument withUpdatedItem(LogicalBusinessItem updatedItem) {
        Objects.requireNonNull(updatedItem, "updatedItem");
        return copyWith(sections, replaceById(items, updatedItem), entityCandidates, pendingQuestions, maturity);
    }

    /**
     * Agrega una entidad candidata con fuente lógica y reglas de identidad válidas.
     */
    public LogicalBusinessDocument withEntityCandidate(LogicalBusinessEntityCandidate entityCandidate) {
        Objects.requireNonNull(entityCandidate, "entityCandidate");
        if (entitiesById.containsKey(entityCandidate.id())) {
            throw new IllegalArgumentException("Ya existe una entidad candidata con ID: " + entityCandidate.id());
        }
        List<LogicalBusinessEntityCandidate> updated = new ArrayList<>(entityCandidates);
        updated.add(entityCandidate);
        return copyWith(sections, items, updated, pendingQuestions, maturity);
    }

    /**
     * Agrega una pregunta pendiente que conserva su prioridad, estado y elementos afectados.
     */
    public LogicalBusinessDocument withPendingQuestion(LogicalBusinessPendingQuestion question) {
        Objects.requireNonNull(question, "question");
        if (pendingQuestions.stream().anyMatch(existing -> existing.id().equals(question.id()))) {
            throw new IllegalArgumentException("Ya existe una pregunta pendiente con ID: " + question.id());
        }
        List<LogicalBusinessPendingQuestion> updated = new ArrayList<>(pendingQuestions);
        updated.add(question);
        return copyWith(sections, items, entityCandidates, updated, maturity);
    }

    /**
     * Actualiza el semáforo de madurez sin cambiar el contenido documental.
     */
    public LogicalBusinessDocument withMaturity(LogicalBusinessMaturity updatedMaturity) {
        return copyWith(sections, items, entityCandidates, pendingQuestions, updatedMaturity);
    }

    private LogicalBusinessDocument copyWith(
            List<LogicalBusinessSection> updatedSections,
            List<LogicalBusinessItem> updatedItems,
            List<LogicalBusinessEntityCandidate> updatedEntities,
            List<LogicalBusinessPendingQuestion> updatedQuestions,
            LogicalBusinessMaturity updatedMaturity
    ) {
        return new LogicalBusinessDocument(projectName, version, documentDate, documentStatus, mainSource,
                updatedSections, updatedItems, updatedEntities, updatedQuestions, updatedMaturity, notes);
    }

    private List<LogicalBusinessItem> replaceById(List<LogicalBusinessItem> source, LogicalBusinessItem replacement) {
        List<LogicalBusinessItem> updated = new ArrayList<>();
        boolean replaced = false;
        for (LogicalBusinessItem item : source) {
            if (item.id().equals(replacement.id())) {
                updated.add(replacement);
                replaced = true;
            } else {
                updated.add(item);
            }
        }
        if (!replaced) {
            throw new IllegalArgumentException("No existe elemento lógico para actualizar: " + replacement.id());
        }
        return updated;
    }

    private List<LogicalBusinessSection> replaceById(List<LogicalBusinessSection> source, LogicalBusinessSection replacement) {
        List<LogicalBusinessSection> updated = new ArrayList<>();
        boolean replaced = false;
        for (LogicalBusinessSection section : source) {
            if (section.id().equals(replacement.id())) {
                updated.add(replacement);
                replaced = true;
            } else {
                updated.add(section);
            }
        }
        if (!replaced) {
            throw new IllegalArgumentException("No existe sección lógica para actualizar: " + replacement.id());
        }
        return updated;
    }

    private List<LogicalBusinessEntityCandidate> replaceById(
            List<LogicalBusinessEntityCandidate> source,
            LogicalBusinessEntityCandidate replacement
    ) {
        List<LogicalBusinessEntityCandidate> updated = new ArrayList<>();
        boolean replaced = false;
        for (LogicalBusinessEntityCandidate entity : source) {
            if (entity.id().equals(replacement.id())) {
                updated.add(replacement);
                replaced = true;
            } else {
                updated.add(entity);
            }
        }
        if (!replaced) {
            throw new IllegalArgumentException("No existe entidad candidata para actualizar: " + replacement.id());
        }
        return updated;
    }

    private List<LogicalBusinessPendingQuestion> replaceById(
            List<LogicalBusinessPendingQuestion> source,
            LogicalBusinessPendingQuestion replacement
    ) {
        List<LogicalBusinessPendingQuestion> updated = new ArrayList<>();
        boolean replaced = false;
        for (LogicalBusinessPendingQuestion question : source) {
            if (question.id().equals(replacement.id())) {
                updated.add(replacement);
                replaced = true;
            } else {
                updated.add(question);
            }
        }
        if (!replaced) {
            throw new IllegalArgumentException("No existe pregunta pendiente para actualizar: " + replacement.id());
        }
        return updated;
    }

    private void validateSections() {
        for (LogicalBusinessSection section : sections) {
            for (String itemId : section.itemIds()) {
                if (!itemsById.containsKey(itemId)) {
                    throw new IllegalArgumentException("La sección " + section.id()
                            + " referencia un elemento inexistente: " + itemId);
                }
            }
        }
    }

    private void validateEntityRelationships() {
        for (LogicalBusinessEntityCandidate entity : entityCandidates) {
            for (LogicalBusinessRelationshipCandidate relationship : entity.relationships()) {
                requireEntity(relationship.sourceEntityId(), relationship.id());
                requireEntity(relationship.targetEntityId(), relationship.id());
            }
        }
    }

    private void requireEntity(String entityId, String relationshipId) {
        if (!entitiesById.containsKey(entityId)) {
            throw new IllegalArgumentException("La relación " + relationshipId
                    + " referencia una entidad inexistente: " + entityId);
        }
    }

    private Map<String, LogicalBusinessItem> indexItems(List<LogicalBusinessItem> values) {
        Map<String, LogicalBusinessItem> indexed = new LinkedHashMap<>();
        for (LogicalBusinessItem item : values) {
            if (indexed.put(item.id(), item) != null) {
                throw new IllegalArgumentException("ID lógico duplicado: " + item.id());
            }
        }
        return Map.copyOf(indexed);
    }

    private Map<String, LogicalBusinessEntityCandidate> indexEntities(List<LogicalBusinessEntityCandidate> values) {
        Map<String, LogicalBusinessEntityCandidate> indexed = new LinkedHashMap<>();
        for (LogicalBusinessEntityCandidate entity : values) {
            if (indexed.put(entity.id(), entity) != null) {
                throw new IllegalArgumentException("Entidad candidata duplicada: " + entity.id());
            }
        }
        return Map.copyOf(indexed);
    }

    private String defaulted(String value, String fallback) {
        String normalized = LogicalBusinessText.normalize(value);
        return normalized.isBlank() ? fallback : normalized;
    }
}
