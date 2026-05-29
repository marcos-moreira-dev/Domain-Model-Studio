package com.marcosmoreira.domainmodelstudio.presentation.datadictionary;

import com.marcosmoreira.domainmodelstudio.application.datadictionary.AddDataDictionaryEntityUseCase;
import com.marcosmoreira.domainmodelstudio.application.datadictionary.AddDataDictionaryFieldUseCase;
import com.marcosmoreira.domainmodelstudio.application.datadictionary.DataDictionaryValidationResult;
import com.marcosmoreira.domainmodelstudio.application.datadictionary.RemoveDataDictionaryItemUseCase;
import com.marcosmoreira.domainmodelstudio.application.datadictionary.UpdateDataDictionaryEntityUseCase;
import com.marcosmoreira.domainmodelstudio.application.datadictionary.UpdateDataDictionaryFieldUseCase;
import com.marcosmoreira.domainmodelstudio.application.datadictionary.ValidateDataDictionaryUseCase;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryDocument;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryEntity;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryField;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataDictionaryStatus;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.DataEntityKind;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.FieldConstraint;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.FieldVisibility;
import com.marcosmoreira.domainmodelstudio.domain.datadictionary.LogicalDataType;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.presentation.workbench.ProjectChangeSupport;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** ViewModel del documento editable de diccionario de datos. */
public final class DataDictionaryViewModel {

    private final AddDataDictionaryEntityUseCase addEntityUseCase;
    private final AddDataDictionaryFieldUseCase addFieldUseCase;
    private final UpdateDataDictionaryEntityUseCase updateEntityUseCase;
    private final UpdateDataDictionaryFieldUseCase updateFieldUseCase;
    private final RemoveDataDictionaryItemUseCase removeItemUseCase;
    private final ValidateDataDictionaryUseCase validateUseCase;
    private final Consumer<String> statusConsumer;
    private final ObservableList<DataDictionaryEntity> entities = FXCollections.observableArrayList();
    private final ObservableList<DataDictionaryField> fields = FXCollections.observableArrayList();
    private final ObjectProperty<DataDictionaryEntity> selectedEntity = new SimpleObjectProperty<>();
    private final ObjectProperty<DataDictionaryField> selectedField = new SimpleObjectProperty<>();
    private DiagramProject currentProject;
    private DataDictionaryDocument currentDocument;
    private final ProjectChangeSupport projectChangeSupport = new ProjectChangeSupport();

    public DataDictionaryViewModel(
            AddDataDictionaryEntityUseCase addEntityUseCase,
            AddDataDictionaryFieldUseCase addFieldUseCase,
            UpdateDataDictionaryEntityUseCase updateEntityUseCase,
            UpdateDataDictionaryFieldUseCase updateFieldUseCase,
            RemoveDataDictionaryItemUseCase removeItemUseCase,
            ValidateDataDictionaryUseCase validateUseCase,
            Consumer<String> statusConsumer
    ) {
        this.addEntityUseCase = Objects.requireNonNull(addEntityUseCase, "addEntityUseCase");
        this.addFieldUseCase = Objects.requireNonNull(addFieldUseCase, "addFieldUseCase");
        this.updateEntityUseCase = Objects.requireNonNull(updateEntityUseCase, "updateEntityUseCase");
        this.updateFieldUseCase = Objects.requireNonNull(updateFieldUseCase, "updateFieldUseCase");
        this.removeItemUseCase = Objects.requireNonNull(removeItemUseCase, "removeItemUseCase");
        this.validateUseCase = Objects.requireNonNull(validateUseCase, "validateUseCase");
        this.statusConsumer = Objects.requireNonNull(statusConsumer, "statusConsumer");
        selectedEntity.addListener((observable, previous, current) -> refreshFields(current));
    }

    public void registerProjectChangeListener(Consumer<DiagramProject> listener) {
        projectChangeSupport.registerProjectChangeListener(listener);
    }

    public ObservableList<DataDictionaryEntity> entities() {
        return entities;
    }

    public ObservableList<DataDictionaryField> fields() {
        return fields;
    }

    public ObjectProperty<DataDictionaryEntity> selectedEntityProperty() {
        return selectedEntity;
    }

    public ObjectProperty<DataDictionaryField> selectedFieldProperty() {
        return selectedField;
    }

    public DataDictionaryDocument currentDocument() {
        return currentDocument;
    }

    public DiagramProject currentProject() {
        return currentProject;
    }

    public void loadProject(DiagramProject project) {
        projectChangeSupport.runLoading(() -> {
            currentProject = Objects.requireNonNull(project, "project");
            currentDocument = project.dataDictionary()
                    .orElseGet(() -> DataDictionaryDocument.blank(project.metadata().title(), java.time.LocalDate.now()));
            refreshEntities();
            if (!entities.isEmpty()) {
                selectedEntity.set(entities.get(0));
            } else {
                selectedEntity.set(null);
            }
            selectedField.set(null);
        });
    }

    public void clear() {
        projectChangeSupport.runLoading(() -> {
            currentProject = null;
            currentDocument = null;
            entities.clear();
            fields.clear();
            selectedEntity.set(null);
            selectedField.set(null);
        });
    }


    public void applyDocumentChanges(
            String projectName,
            String clientName,
            String organizationName,
            String author,
            String version,
            DataDictionaryStatus status,
            String introduction,
            String logoReference,
            String notes
    ) {
        if (!ensureDocument("No hay diccionario abierto para actualizar propiedades del documento.")) {
            return;
        }
        try {
            applyDocument(currentDocument.withDocumentDetails(projectName, clientName, organizationName,
                    author, version, status, introduction, logoReference, notes), "Documento actualizado.");
        } catch (IllegalArgumentException exception) {
            statusConsumer.accept("No se pudo actualizar documento: " + exception.getMessage());
        }
    }

    public void addEntity() {
        if (!ensureDocument("No hay diccionario abierto para agregar entidad.")) {
            return;
        }
        try {
            applyDocument(addEntityUseCase.add(currentDocument, "Entidad"), "Entidad agregada.");
            selectLastEntity();
        } catch (IllegalArgumentException exception) {
            statusConsumer.accept("No se pudo agregar entidad: " + exception.getMessage());
        }
    }

    public void addField() {
        if (!ensureDocument("No hay diccionario abierto para agregar campo.")) {
            return;
        }
        DataDictionaryEntity entity = selectedEntity.get();
        if (entity == null) {
            statusConsumer.accept("Selecciona una entidad antes de agregar campo.");
            return;
        }
        try {
            applyDocument(addFieldUseCase.add(currentDocument, entity.id(), "campo"), "Campo agregado.");
            restoreEntitySelection(entity.id());
            selectLastField();
        } catch (IllegalArgumentException exception) {
            statusConsumer.accept("No se pudo agregar campo: " + exception.getMessage());
        }
    }

    public void removeSelected() {
        if (!ensureDocument("No hay diccionario abierto para eliminar.")) {
            return;
        }
        DataDictionaryEntity entity = selectedEntity.get();
        DataDictionaryField field = selectedField.get();
        try {
            if (entity != null && field != null) {
                applyDocument(removeItemUseCase.removeField(currentDocument, entity.id(), field.name()), "Campo eliminado.");
                restoreEntitySelection(entity.id());
                return;
            }
            if (entity != null) {
                applyDocument(removeItemUseCase.removeEntity(currentDocument, entity.id()), "Entidad eliminada.");
                if (!entities.isEmpty()) {
                    selectedEntity.set(entities.get(0));
                }
            }
        } catch (IllegalArgumentException exception) {
            statusConsumer.accept("No se pudo eliminar: " + exception.getMessage());
        }
    }

    public void applyEntityChanges(
            String displayName,
            String technicalName,
            String moduleName,
            DataEntityKind kind,
            DataDictionaryStatus status,
            String description,
            String notes
    ) {
        DataDictionaryEntity entity = selectedEntity.get();
        if (entity == null || !ensureDocument("No hay entidad seleccionada.")) {
            return;
        }
        try {
            applyDocument(updateEntityUseCase.update(currentDocument, entity.id(), displayName, technicalName,
                    moduleName, kind, status, description, notes), "Entidad actualizada.");
            restoreEntitySelection(entity.id());
        } catch (IllegalArgumentException exception) {
            statusConsumer.accept("No se pudo actualizar entidad: " + exception.getMessage());
        }
    }

    public void applyFieldChanges(
            String displayName,
            String technicalName,
            LogicalDataType logicalType,
            String physicalTypeSuggestion,
            Set<FieldConstraint> constraints,
            String foreignKeyReference,
            String defaultValue,
            String expectedFormat,
            String description,
            String businessRule,
            String validationRule,
            String example,
            Set<FieldVisibility> visibility,
            boolean userEditable,
            String notes
    ) {
        DataDictionaryEntity entity = selectedEntity.get();
        DataDictionaryField field = selectedField.get();
        if (entity == null || field == null || !ensureDocument("No hay campo seleccionado.")) {
            return;
        }
        try {
            applyDocument(updateFieldUseCase.update(currentDocument, entity.id(), field.name(), displayName,
                    technicalName, logicalType, physicalTypeSuggestion, constraints, foreignKeyReference,
                    defaultValue, expectedFormat, description, businessRule, validationRule, example,
                    visibility, userEditable, notes), "Campo actualizado.");
            restoreEntitySelection(entity.id());
            restoreFieldSelection(technicalName.isBlank() ? displayName : technicalName);
        } catch (IllegalArgumentException exception) {
            statusConsumer.accept("No se pudo actualizar campo: " + exception.getMessage());
        }
    }

    public DataDictionaryValidationResult validateDocument() {
        if (!ensureDocument("No hay diccionario abierto para validar.")) {
            return new DataDictionaryValidationResult(java.util.List.of("No hay diccionario abierto."));
        }
        DataDictionaryValidationResult result = validateUseCase.validate(currentDocument);
        statusConsumer.accept(result.summary());
        return result;
    }

    private void applyDocument(DataDictionaryDocument updatedDocument, String statusMessage) {
        currentDocument = updatedDocument;
        refreshEntities();
        if (currentProject != null) {
            currentProject = currentProject.withDataDictionary(updatedDocument);
            projectChangeSupport.notifyChanged(currentProject);
        }
        statusConsumer.accept(statusMessage);
    }

    private void refreshEntities() {
        DataDictionaryEntity previousEntity = selectedEntity.get();
        String previousEntityId = previousEntity == null ? "" : previousEntity.id();
        entities.setAll(currentDocument == null ? java.util.List.of() : currentDocument.entities());
        if (!restoreEntitySelection(previousEntityId)) {
            selectedEntity.set(null);
        }
    }

    private void refreshFields(DataDictionaryEntity entity) {
        DataDictionaryField previousField = selectedField.get();
        String previousFieldName = previousField == null ? "" : previousField.name();
        fields.setAll(entity == null ? java.util.List.of() : entity.fields());
        if (!restoreFieldSelection(previousFieldName)) {
            selectedField.set(null);
        }
    }

    private boolean restoreEntitySelection(String entityId) {
        if (entityId == null || entityId.isBlank()) {
            return false;
        }
        return entities.stream()
                .filter(entity -> entity.id().equalsIgnoreCase(entityId))
                .findFirst()
                .map(entity -> {
                    selectedEntity.set(entity);
                    return true;
                })
                .orElse(false);
    }

    private boolean restoreFieldSelection(String fieldName) {
        if (fieldName == null || fieldName.isBlank()) {
            return false;
        }
        return fields.stream()
                .filter(field -> field.name().equalsIgnoreCase(fieldName)
                        || field.technicalName().equalsIgnoreCase(fieldName)
                        || field.displayName().equalsIgnoreCase(fieldName))
                .findFirst()
                .map(field -> {
                    selectedField.set(field);
                    return true;
                })
                .orElse(false);
    }

    private void selectLastEntity() {
        if (!entities.isEmpty()) {
            selectedEntity.set(entities.get(entities.size() - 1));
        }
    }

    private void selectLastField() {
        if (!fields.isEmpty()) {
            selectedField.set(fields.get(fields.size() - 1));
        }
    }

    private boolean ensureDocument(String message) {
        if (currentDocument == null) {
            statusConsumer.accept(message);
            return false;
        }
        return true;
    }

    public boolean active() {
        return currentProject != null && currentProject.metadata().diagramTypeId().equals(DiagramTypeId.DATA_DICTIONARY);
    }
}
