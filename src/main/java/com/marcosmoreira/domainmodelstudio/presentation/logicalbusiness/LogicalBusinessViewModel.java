package com.marcosmoreira.domainmodelstudio.presentation.logicalbusiness;

import com.marcosmoreira.domainmodelstudio.application.logicalbusiness.LogicalBusinessMaturityAssessor;
import com.marcosmoreira.domainmodelstudio.application.logicalbusiness.LogicalBusinessTraceLink;
import com.marcosmoreira.domainmodelstudio.application.logicalbusiness.LogicalBusinessTraceabilityReport;
import com.marcosmoreira.domainmodelstudio.application.logicalbusiness.LogicalBusinessTraceabilityService;
import com.marcosmoreira.domainmodelstudio.application.logicalbusiness.LogicalBusinessValidationService;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessAttributeCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessDocument;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessQuestionPriority;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItemStatus;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessDocumentStatus;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessEntityCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessItem;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessMaturity;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessPendingQuestion;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessRelationshipCandidate;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessSection;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusiness.LogicalBusinessValidationIssue;
import com.marcosmoreira.domainmodelstudio.presentation.sidedock.SideDockModuleId;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/** Estado de presentación del proyecto documental de levantamiento lógico. */
public final class LogicalBusinessViewModel {

    private final Consumer<String> statusUpdater;
    private final LogicalBusinessValidationService validationService = new LogicalBusinessValidationService();
    private final LogicalBusinessMaturityAssessor maturityAssessor = new LogicalBusinessMaturityAssessor(validationService);
    private final LogicalBusinessTraceabilityService traceabilityService = new LogicalBusinessTraceabilityService();
    private final ObjectProperty<DiagramProject> currentProject = new SimpleObjectProperty<>();
    private final ObjectProperty<LogicalBusinessSelection> selection = new SimpleObjectProperty<>(LogicalBusinessSelection.none());
    private final ObjectProperty<SideDockModuleId> requestedSideDockModule = new SimpleObjectProperty<>();
    private Consumer<DiagramProject> projectChangeListener = project -> { };

    /*
     * Seleccion legada: se mantiene para no romper los paneles MVP actuales.
     * Desde esta tanda, la fuente conceptual de selección es `selection`.
     */
    private final StringProperty selectedSectionId = new SimpleStringProperty("");
    private final StringProperty selectedItemId = new SimpleStringProperty("");
    private final StringProperty selectedEntityId = new SimpleStringProperty("");

    public LogicalBusinessViewModel(Consumer<String> statusUpdater) {
        this.statusUpdater = Objects.requireNonNull(statusUpdater, "statusUpdater");
    }

    public void registerProjectChangeListener(Consumer<DiagramProject> listener) {
        this.projectChangeListener = listener == null ? project -> { } : listener;
    }

    public void loadProject(DiagramProject project) {
        Objects.requireNonNull(project, "project");
        LogicalBusinessDocument document = project.logicalBusinessDocument()
                .orElseThrow(() -> new IllegalArgumentException("El proyecto no contiene levantamiento lógico."));
        currentProject.set(project);
        selectInitialContent(document);
        statusUpdater.accept("Levantamiento lógico cargado: " + project.metadata().title());
    }

    public void clear() {
        currentProject.set(null);
        applySelection(LogicalBusinessSelection.none());
    }

    public boolean active() { return currentProject.get() != null; }

    public DiagramProject currentProject() { return currentProject.get(); }

    public LogicalBusinessDocument currentDocument() {
        return currentProject.get() == null
                ? null
                : currentProject.get().logicalBusinessDocument().orElse(null);
    }

    public ObjectProperty<DiagramProject> currentProjectProperty() { return currentProject; }

    public ObjectProperty<LogicalBusinessSelection> selectionProperty() { return selection; }

    public LogicalBusinessSelection selection() { return selection.get(); }

    public ObjectProperty<SideDockModuleId> requestedSideDockModuleProperty() { return requestedSideDockModule; }

    public void requestSideDockModule(SideDockModuleId moduleId) {
        Objects.requireNonNull(moduleId, "moduleId");
        requestedSideDockModule.set(null);
        requestedSideDockModule.set(moduleId);
        statusUpdater.accept("SideBar: " + logicalBusinessSideDockName(moduleId));
    }

    private String logicalBusinessSideDockName(SideDockModuleId moduleId) {
        return switch (moduleId) {
            case SECTIONS -> "Estructura";
            case PROPERTIES -> "Ficha rápida";
            case PALETTE -> "Elementos lógicos";
            case ENTITIES -> "Entidades y relaciones";
            case TRACEABILITY -> "Impacto y dependencias";
            case HELP -> "Ayuda y glosario";
            default -> moduleId.displayName();
        };
    }

    public StringProperty selectedSectionIdProperty() { return selectedSectionId; }

    public StringProperty selectedItemIdProperty() { return selectedItemId; }

    public StringProperty selectedEntityIdProperty() { return selectedEntityId; }

    public void selectDocument() { applySelection(LogicalBusinessSelection.document()); }

    public void selectGroup(String groupId) {
        applySelection(LogicalBusinessSelection.group(groupId));
    }

    public void selectSection(String sectionId) {
        applySelection(LogicalBusinessSelection.section(sectionId));
    }

    public void selectItem(String itemId) {
        applySelection(LogicalBusinessSelection.item(itemId));
    }

    public void selectEntity(String entityId) {
        applySelection(LogicalBusinessSelection.entity(entityId));
    }

    public void selectAttribute(String entityId, String attributeId) {
        applySelection(LogicalBusinessSelection.attribute(entityId, attributeId));
    }

    public void selectRelationship(String entityId, String relationshipId) {
        applySelection(LogicalBusinessSelection.relationship(entityId, relationshipId));
    }

    public void selectPendingQuestion(String questionId) {
        applySelection(LogicalBusinessSelection.pendingQuestion(questionId));
    }

    public void selectMaturity() { applySelection(LogicalBusinessSelection.maturity()); }

    public void selectReference(String referenceId) {
        applySelection(LogicalBusinessSelectionSupport.referenceSelection(currentDocument(), referenceId));
    }

    void applyTreeSelection(LogicalBusinessSelection treeSelection) {
        applySelection(treeSelection);
    }

    public Optional<LogicalBusinessSection> selectedSection() {
        LogicalBusinessDocument document = currentDocument();
        if (document == null || selectedSectionId.get().isBlank()) {
            return Optional.empty();
        }
        return document.sections().stream()
                .filter(section -> section.id().equals(selectedSectionId.get()))
                .findFirst();
    }

    public Optional<LogicalBusinessItem> selectedItem() {
        LogicalBusinessDocument document = currentDocument();
        return document == null ? Optional.empty() : document.itemById(selectedItemId.get());
    }

    public Optional<LogicalBusinessEntityCandidate> selectedEntity() {
        LogicalBusinessDocument document = currentDocument();
        return document == null ? Optional.empty() : document.entityById(selectedEntityId.get());
    }

    public Optional<LogicalBusinessAttributeCandidate> selectedAttribute() {
        return selectedEntity().flatMap(entity -> entity.attributeById(selection.get().id()));
    }

    public Optional<LogicalBusinessRelationshipCandidate> selectedRelationship() {
        return selectedEntity().flatMap(entity -> entity.relationships().stream()
                .filter(relationship -> relationship.id().equals(selection.get().id()))
                .findFirst());
    }

    public Optional<LogicalBusinessPendingQuestion> selectedPendingQuestion() {
        LogicalBusinessDocument document = currentDocument();
        if (document == null || selection.get().kind() != LogicalBusinessSelectionKind.PENDING_QUESTION) {
            return Optional.empty();
        }
        String selectedId = selection.get().id();
        return document.pendingQuestions().stream()
                .filter(question -> question.id().equals(selectedId))
                .findFirst();
    }

    public List<LogicalBusinessValidationIssue> validationIssues() {
        LogicalBusinessDocument document = currentDocument();
        return document == null ? List.of() : validationService.validate(document);
    }

    public LogicalBusinessMaturity assessedMaturity() {
        LogicalBusinessDocument document = currentDocument();
        return document == null ? LogicalBusinessMaturity.initial() : maturityAssessor.assess(document);
    }

    public void applyDocumentHeader(
            String projectName,
            String version,
            LocalDate documentDate,
            LogicalBusinessDocumentStatus status,
            String mainSource,
            String notes
    ) {
        LogicalBusinessDocument document = currentDocument();
        if (document == null) {
            return;
        }
        updateDocumentSafely(() -> document.withHeader(projectName, version, documentDate, status, mainSource, notes),
                "Documento actualizado.");
    }

    public void applySectionEdit(
            LogicalBusinessSection section,
            String title,
            String purpose,
            LogicalBusinessItemStatus status,
            String notes
    ) {
        LogicalBusinessDocument document = currentDocument();
        if (document == null || section == null) {
            return;
        }
        updateDocumentSafely(() -> document.withUpdatedSection(section.withDetails(title, purpose, status, notes)),
                "Sección actualizada: " + section.id());
    }

    public void applyItemEdit(
            LogicalBusinessItem item,
            String title,
            LogicalBusinessItemStatus status,
            String source,
            String description,
            String humanReading,
            String content,
            List<String> referenceIds
    ) {
        LogicalBusinessDocument document = currentDocument();
        if (document == null || item == null) {
            return;
        }
        updateDocumentSafely(() -> document.withUpdatedItem(item.withEditableDetails(title, status, source,
                description, humanReading, content, referenceIds)), "Elemento actualizado: " + item.id());
    }

    public void applyEntityEdit(
            LogicalBusinessEntityCandidate entity,
            String name,
            LogicalBusinessItemStatus status,
            String logicalJustification,
            List<String> sourceReferences,
            List<String> associatedRules,
            List<String> associatedInvariants,
            List<String> createdByUseCases,
            List<String> modifiedByUseCases,
            List<String> queriedByUseCases,
            String modelingRisk
    ) {
        LogicalBusinessDocument document = currentDocument();
        if (document == null || entity == null) {
            return;
        }
        updateDocumentSafely(() -> document.withUpdatedEntityCandidate(entity.withEditableDetails(name, status,
                logicalJustification, sourceReferences, associatedRules, associatedInvariants, createdByUseCases,
                modifiedByUseCases, queriedByUseCases, modelingRisk)), "Entidad actualizada: " + entity.id());
    }

    public void applyAttributeEdit(
            LogicalBusinessEntityCandidate entity,
            LogicalBusinessAttributeCandidate attribute,
            String name,
            String reason,
            String tentativeType,
            boolean calculated,
            String formula,
            String riskIfWrong,
            List<String> sourceReferences,
            List<String> relatedRules,
            List<String> relatedInvariants
    ) {
        LogicalBusinessDocument document = currentDocument();
        if (document == null || entity == null || attribute == null) {
            return;
        }
        updateDocumentSafely(() -> {
            LogicalBusinessAttributeCandidate updated = attribute.withEditableDetails(name, reason, tentativeType,
                    calculated, formula, riskIfWrong, sourceReferences, relatedRules, relatedInvariants);
            return document.withUpdatedEntityCandidate(entity.withUpdatedAttribute(updated));
        }, "Atributo actualizado: " + attribute.id());
    }

    public void applyRelationshipEdit(
            LogicalBusinessEntityCandidate entity,
            LogicalBusinessRelationshipCandidate relationship,
            String sourceEntityId,
            String targetEntityId,
            String name,
            String cardinalityHint,
            String justification,
            List<String> sourceReferences
    ) {
        LogicalBusinessDocument document = currentDocument();
        if (document == null || entity == null || relationship == null) {
            return;
        }
        updateDocumentSafely(() -> {
            LogicalBusinessRelationshipCandidate updated = relationship.withEditableDetails(sourceEntityId,
                    targetEntityId, name, cardinalityHint, justification, sourceReferences);
            return document.withUpdatedEntityCandidate(entity.withUpdatedRelationship(updated));
        }, "Relación actualizada: " + relationship.id());
    }

    public void applyPendingQuestionEdit(
            LogicalBusinessPendingQuestion question,
            String questionText,
            String affects,
            LogicalBusinessQuestionPriority priority,
            LogicalBusinessItemStatus status
    ) {
        LogicalBusinessDocument document = currentDocument();
        if (document == null || question == null) {
            return;
        }
        updateDocumentSafely(() -> document.withUpdatedPendingQuestion(question.withEditableDetails(questionText,
                affects, priority, status)), "Pregunta actualizada: " + question.id());
    }

    public List<LogicalBusinessTraceLink> traceabilityLinks() {
        LogicalBusinessDocument document = currentDocument();
        return document == null ? List.of() : traceabilityService.allLinks(document);
    }

    public LogicalBusinessTraceabilityReport traceabilityReport() {
        LogicalBusinessDocument document = currentDocument();
        if (document == null) {
            return new LogicalBusinessTraceabilityReport("", List.of(), List.of(), List.of());
        }
        return traceabilityService.reportFor(document, selectedTraceabilityId());
    }

    private void updateDocumentSafely(Supplier<LogicalBusinessDocument> updateSupplier, String message) {
        try {
            replaceDocument(updateSupplier.get(), message);
        } catch (RuntimeException exception) {
            statusUpdater.accept("No se pudo aplicar el cambio: " + exception.getMessage());
        }
    }

    void replaceDocumentFromCrud(LogicalBusinessDocument updatedDocument, String message) {
        replaceDocument(updatedDocument, message == null || message.isBlank()
                ? "Levantamiento lógico actualizado."
                : message);
    }

    private void replaceDocument(LogicalBusinessDocument updatedDocument, String message) {
        DiagramProject project = currentProject.get();
        if (project == null || updatedDocument == null) {
            return;
        }
        DiagramProject updatedProject = project.withLogicalBusinessDocument(updatedDocument);
        currentProject.set(updatedProject);
        projectChangeListener.accept(updatedProject);
        statusUpdater.accept(message + " Exporta Markdown o guarda el proyecto para persistirlo.");
    }

    public String selectedTraceabilityId() { return selection.get().traceabilityFocusId(); }

    private void selectInitialContent(LogicalBusinessDocument document) {
        document.sections().stream()
                .findFirst()
                .map(LogicalBusinessSection::id)
                .map(LogicalBusinessSelection::section)
                .ifPresentOrElse(this::applySelection, () -> applySelection(LogicalBusinessSelection.document()));
    }

    private void applySelection(LogicalBusinessSelection newSelection) {
        LogicalBusinessSelection normalized = normalizeSelection(newSelection);
        updateLegacySelection(normalized);
        selection.set(normalized);
    }

    private LogicalBusinessSelection normalizeSelection(LogicalBusinessSelection requested) {
        return LogicalBusinessSelectionSupport.normalize(currentDocument(), requested);
    }

    private void updateLegacySelection(LogicalBusinessSelection currentSelection) {
        switch (currentSelection.kind()) {
            case SECTION -> {
                selectedSectionId.set(currentSelection.id());
                selectedItemId.set("");
                selectedEntityId.set("");
            }
            case ITEM -> {
                selectedItemId.set(currentSelection.id());
                selectedEntityId.set("");
                selectedSectionId.set(LogicalBusinessSelectionSupport.selectedSectionForItem(currentDocument(), currentSelection.id())
                        .map(LogicalBusinessSection::id).orElse(""));
            }
            case ENTITY -> {
                selectedSectionId.set("");
                selectedItemId.set("");
                selectedEntityId.set(currentSelection.id());
            }
            case ATTRIBUTE, RELATIONSHIP -> {
                selectedSectionId.set("");
                selectedItemId.set("");
                selectedEntityId.set(currentSelection.ownerId());
            }
            case PENDING_QUESTION, MATURITY, DOCUMENT, GROUP, NONE -> {
                selectedSectionId.set("");
                selectedItemId.set("");
                selectedEntityId.set("");
            }
        }
    }

}
