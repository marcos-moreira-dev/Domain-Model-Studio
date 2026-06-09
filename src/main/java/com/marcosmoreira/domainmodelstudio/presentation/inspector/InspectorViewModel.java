package com.marcosmoreira.domainmodelstudio.presentation.inspector;

import com.marcosmoreira.domainmodelstudio.application.editing.ChangeConnectorAnchorsUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.ChangeConnectorMarkerOrientationUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.ChangeDiagramAppearanceUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.ChangeElementStyleUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.RenameElementUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.UpdateNodeLayoutUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.UpdateRelationshipCardinalityUseCase;
import com.marcosmoreira.domainmodelstudio.application.project.SourceMarkdownSyncResult;
import com.marcosmoreira.domainmodelstudio.application.project.SourceMarkdownSynchronizer;
import com.marcosmoreira.domainmodelstudio.application.editing.UpdateElementDescriptionUseCase;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElement;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementType;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.er.AttributeElement;
import com.marcosmoreira.domainmodelstudio.domain.er.AttributeTag;
import com.marcosmoreira.domainmodelstudio.domain.er.EntityElement;
import com.marcosmoreira.domainmodelstudio.domain.er.EntityKind;
import com.marcosmoreira.domainmodelstudio.domain.er.ParticipationType;
import com.marcosmoreira.domainmodelstudio.domain.er.RelationshipElement;
import com.marcosmoreira.domainmodelstudio.domain.er.RelationshipKind;
import com.marcosmoreira.domainmodelstudio.domain.layout.AnchorSide;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.MarkerOrientation;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.domain.style.DiagramAppearance;
import com.marcosmoreira.domainmodelstudio.domain.style.ElementStyle;
import com.marcosmoreira.domainmodelstudio.domain.style.DiagramStyleResolver;
import com.marcosmoreira.domainmodelstudio.domain.style.FillStyle;
import com.marcosmoreira.domainmodelstudio.domain.style.RgbaColor;
import com.marcosmoreira.domainmodelstudio.domain.style.StrokePattern;
import com.marcosmoreira.domainmodelstudio.domain.style.StrokeStyle;
import com.marcosmoreira.domainmodelstudio.domain.style.TextStyle;
import com.marcosmoreira.domainmodelstudio.presentation.selection.DiagramSelectionModel;
import java.text.Normalizer;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * ViewModel del panel de propiedades.
 *
 * <p>La interfaz debe hablar en lenguaje de modelado: entidad, atributo,
 * relación, cardinalidad, conexión y diagrama. Los detalles técnicos quedan
 * fuera del texto visible para el usuario.</p>
 */
public final class InspectorViewModel {

    private final DiagramSelectionModel selectionModel;
    private final Supplier<DiagramProject> projectSupplier;
    private final Consumer<DiagramProject> projectConsumer;
    private final RenameElementUseCase renameElementUseCase;
    private final UpdateNodeLayoutUseCase updateNodeLayoutUseCase;
    private final UpdateElementDescriptionUseCase updateElementDescriptionUseCase;
    private final UpdateRelationshipCardinalityUseCase updateRelationshipCardinalityUseCase;
    private final ChangeElementStyleUseCase changeElementStyleUseCase;
    private final ChangeDiagramAppearanceUseCase changeDiagramAppearanceUseCase;
    private final ChangeConnectorAnchorsUseCase changeConnectorAnchorsUseCase;
    private final ChangeConnectorMarkerOrientationUseCase changeConnectorMarkerOrientationUseCase;
    private final SourceMarkdownSynchronizer sourceMarkdownSynchronizer;
    private final Consumer<String> statusConsumer;
    private final Runnable dirtyMarker;

    private final StringProperty selectedElementId = new SimpleStringProperty("-");
    private final StringProperty selectedElementName = new SimpleStringProperty("Sin selección");
    private final StringProperty selectedElementType = new SimpleStringProperty("Ninguno");
    private final StringProperty selectedNotation = new SimpleStringProperty("Chen");
    private final StringProperty projectTitle = new SimpleStringProperty("Modelo conceptual");
    private final StringProperty projectDescription = new SimpleStringProperty("");
    private final StringProperty selectionTitle = new SimpleStringProperty("Sin selección");
    private final StringProperty selectionSummary = new SimpleStringProperty("Selecciona una entidad, atributo, relación o conector del diagrama.");

    private final StringProperty entityKind = new SimpleStringProperty("—");
    private final StringProperty entityModule = new SimpleStringProperty("—");
    private final StringProperty entityAttributeCount = new SimpleStringProperty("—");
    private final StringProperty entityDescription = new SimpleStringProperty("—");
    private final StringProperty editableDescription = new SimpleStringProperty("");

    private final StringProperty attributeOwner = new SimpleStringProperty("—");
    private final StringProperty attributeTags = new SimpleStringProperty("—");
    private final StringProperty attributeDescription = new SimpleStringProperty("—");

    private final StringProperty relationshipFrom = new SimpleStringProperty("—");
    private final StringProperty relationshipTo = new SimpleStringProperty("—");
    private final StringProperty relationshipCardinalities = new SimpleStringProperty("—");
    private final StringProperty relationshipFromCardinality = new SimpleStringProperty("1");
    private final StringProperty relationshipToCardinality = new SimpleStringProperty("1..M");
    private final StringProperty relationshipParticipation = new SimpleStringProperty("—");
    private final StringProperty relationshipKind = new SimpleStringProperty("—");
    private final StringProperty relationshipDescription = new SimpleStringProperty("—");

    private final StringProperty connectorSummary = new SimpleStringProperty("—");
    private final StringProperty positionX = new SimpleStringProperty("");
    private final StringProperty positionY = new SimpleStringProperty("");
    private final StringProperty width = new SimpleStringProperty("");
    private final StringProperty height = new SimpleStringProperty("");
    private final StringProperty fillColor = new SimpleStringProperty("#FFFFFF");
    private final StringProperty strokeColor = new SimpleStringProperty("#505050");
    private final StringProperty textColor = new SimpleStringProperty("#232323");
    private final StringProperty strokeWidth = new SimpleStringProperty("1");
    private final StringProperty fontFamily = new SimpleStringProperty("Segoe UI");
    private final StringProperty fontSize = new SimpleStringProperty("12");
    private final StringProperty workspaceBackgroundColor = new SimpleStringProperty("#EEF2F6");
    private final StringProperty diagramBackgroundColor = new SimpleStringProperty("#FFFFFF");
    private final StringProperty entityCategoryFillColor = new SimpleStringProperty("#EAF2FF");
    private final StringProperty attributeCategoryFillColor = new SimpleStringProperty("#FFF4D8");
    private final StringProperty derivedAttributeCategoryFillColor = new SimpleStringProperty("#F3E8FF");
    private final StringProperty relationshipCategoryFillColor = new SimpleStringProperty("#EAF7EA");
    private final StringProperty connectorCategoryColor = new SimpleStringProperty("#4F5D75");
    private final StringProperty globalBorderColor = new SimpleStringProperty("#505050");
    private final StringProperty connectorColor = new SimpleStringProperty("#505050");
    private final StringProperty connectorWidth = new SimpleStringProperty("1");
    private final StringProperty connectorPattern = new SimpleStringProperty("Continua");
    private final StringProperty sourceAnchor = new SimpleStringProperty("Automática");
    private final StringProperty targetAnchor = new SimpleStringProperty("Automática");
    private final StringProperty sourceMarkerOrientation = new SimpleStringProperty("Automática");
    private final StringProperty targetMarkerOrientation = new SimpleStringProperty("Automática");
    private final StringProperty inspectorMessage = new SimpleStringProperty("Sin selección. Selecciona una entidad, atributo, relación o conector.");
    private final BooleanProperty projectOpen = new SimpleBooleanProperty(false);
    private final BooleanProperty hasSelection = new SimpleBooleanProperty(false);
    private final BooleanProperty entitySelected = new SimpleBooleanProperty(false);
    private final BooleanProperty attributeSelected = new SimpleBooleanProperty(false);
    private final BooleanProperty relationshipSelected = new SimpleBooleanProperty(false);
    private final BooleanProperty editable = new SimpleBooleanProperty(false);
    private final BooleanProperty nodeEditable = new SimpleBooleanProperty(false);
    private final BooleanProperty connectorEditable = new SimpleBooleanProperty(false);

    public InspectorViewModel(
            DiagramSelectionModel selectionModel,
            Supplier<DiagramProject> projectSupplier,
            Consumer<DiagramProject> projectConsumer,
            RenameElementUseCase renameElementUseCase,
            UpdateNodeLayoutUseCase updateNodeLayoutUseCase,
            UpdateElementDescriptionUseCase updateElementDescriptionUseCase,
            UpdateRelationshipCardinalityUseCase updateRelationshipCardinalityUseCase,
            ChangeElementStyleUseCase changeElementStyleUseCase,
            ChangeDiagramAppearanceUseCase changeDiagramAppearanceUseCase,
            ChangeConnectorAnchorsUseCase changeConnectorAnchorsUseCase,
            ChangeConnectorMarkerOrientationUseCase changeConnectorMarkerOrientationUseCase,
            SourceMarkdownSynchronizer sourceMarkdownSynchronizer,
            Consumer<String> statusConsumer,
            Runnable dirtyMarker
    ) {
        this.selectionModel = Objects.requireNonNull(selectionModel, "selectionModel");
        this.projectSupplier = Objects.requireNonNull(projectSupplier, "projectSupplier");
        this.projectConsumer = Objects.requireNonNull(projectConsumer, "projectConsumer");
        this.renameElementUseCase = Objects.requireNonNull(renameElementUseCase, "renameElementUseCase");
        this.updateNodeLayoutUseCase = Objects.requireNonNull(updateNodeLayoutUseCase, "updateNodeLayoutUseCase");
        this.updateElementDescriptionUseCase = Objects.requireNonNull(updateElementDescriptionUseCase, "updateElementDescriptionUseCase");
        this.updateRelationshipCardinalityUseCase = Objects.requireNonNull(updateRelationshipCardinalityUseCase, "updateRelationshipCardinalityUseCase");
        this.changeElementStyleUseCase = Objects.requireNonNull(changeElementStyleUseCase, "changeElementStyleUseCase");
        this.changeDiagramAppearanceUseCase = Objects.requireNonNull(changeDiagramAppearanceUseCase, "changeDiagramAppearanceUseCase");
        this.changeConnectorAnchorsUseCase = Objects.requireNonNull(changeConnectorAnchorsUseCase, "changeConnectorAnchorsUseCase");
        this.changeConnectorMarkerOrientationUseCase = Objects.requireNonNull(changeConnectorMarkerOrientationUseCase, "changeConnectorMarkerOrientationUseCase");
        this.sourceMarkdownSynchronizer = Objects.requireNonNull(sourceMarkdownSynchronizer, "sourceMarkdownSynchronizer");
        this.statusConsumer = statusConsumer == null ? ignored -> { } : statusConsumer;
        this.dirtyMarker = dirtyMarker == null ? () -> { } : dirtyMarker;
        this.selectionModel.selectedElementIdProperty().addListener((observable, previous, current) -> refreshFromSelection());
    }

    public void refreshFromSelection() {
        DiagramProject project = projectSupplier.get();
        DiagramElementId elementId = selectionModel.selectedElementId();
        if (project == null) {
            projectOpen.set(false);
            clearFields("Sin proyecto abierto. Abre o importa un modelo para editar sus elementos.");
            selectedNotation.set("—");
            return;
        }
        projectOpen.set(true);
        loadDiagramAppearance(project);
        selectedNotation.set(displayNotation(project.layouts().activeNotation().displayName()));
        if (elementId == null) {
            clearFields("Sin selección. Selecciona una entidad, atributo, relación o conector.");
            return;
        }

        Optional<ConnectorLayout> connector = project.layouts().activeLayout().connectorById(elementId);
        if (connector.isPresent() && project.layouts().activeLayout().nodeFor(elementId).isEmpty()) {
            loadSelectedConnector(project, elementId, connector.get());
            return;
        }

        Optional<DiagramElement> element = findElement(project, elementId);
        if (element.isPresent()) {
            loadSelectedElement(project, elementId, element.get());
            return;
        }

        if (connector.isPresent()) {
            loadSelectedConnector(project, elementId, connector.get());
            return;
        }

        clearFields("Sin selección. Selecciona una entidad, atributo, relación o conector.");
    }

    public void applyChanges() {
        DiagramProject project = projectSupplier.get();
        DiagramElementId elementId = selectionModel.selectedElementId();
        if (project == null || elementId == null) {
            inspectorMessage.set("No hay elemento seleccionado.");
            return;
        }
        try {
            DiagramProject updated = project;
            if (connectorEditable.get()) {
                updated = changeConnectorAnchorsUseCase.changeAnchors(
                        updated,
                        updated.metadata().activeNotation(),
                        elementId,
                        parseAnchorSide(sourceAnchor.get(), "ancla de inicio"),
                        parseAnchorSide(targetAnchor.get(), "ancla de llegada")
                );
                updated = changeConnectorMarkerOrientationUseCase.changeOrientations(
                        updated,
                        updated.metadata().activeNotation(),
                        elementId,
                        parseMarkerOrientation(sourceMarkerOrientation.get(), "orientación de inicio"),
                        parseMarkerOrientation(targetMarkerOrientation.get(), "orientación de llegada")
                );
                ElementStyle currentConnectorStyle = DiagramStyleResolver.resolvedStyleFor(updated, elementId);
                ElementStyle updatedConnectorStyle = currentConnectorStyle.withStroke(new StrokeStyle(
                        RgbaColor.fromHex(connectorColor.get()),
                        parseDouble(connectorWidth.get(), "grosor de conexión"),
                        parseStrokePattern(connectorPattern.get())
                ));
                updated = changeElementStyleUseCase.changeStyle(updated, elementId, updatedConnectorStyle);
                projectConsumer.accept(updated);
                selectionModel.select(elementId);
                dirtyMarker.run();
                statusConsumer.accept("Cambios de la conexión aplicados correctamente.");
                inspectorMessage.set("Cambios de la conexión aplicados correctamente.");
                return;
            }

            if (!selectedElementName.get().isBlank()) {
                updated = renameElementUseCase.rename(updated, elementId, selectedElementName.get());
            }
            if (nodeEditable.get()) {
                updated = updateElementDescriptionUseCase.update(updated, elementId, editableDescription.get());
            }
            if (relationshipSelected.get()) {
                updated = updateRelationshipCardinalityUseCase.update(
                        updated,
                        elementId,
                        relationshipFromCardinality.get(),
                        relationshipToCardinality.get()
                );
            }
            if (!positionX.get().isBlank() && !positionY.get().isBlank() && !width.get().isBlank() && !height.get().isBlank()) {
                updated = updateNodeLayoutUseCase.update(
                        updated,
                        updated.metadata().activeNotation(),
                        elementId,
                        parseDouble(positionX.get(), "X"),
                        parseDouble(positionY.get(), "Y"),
                        parseDouble(width.get(), "ancho"),
                        parseDouble(height.get(), "alto")
                );
            }
            ElementStyle currentStyle = DiagramStyleResolver.resolvedStyleFor(updated, elementId);
            ElementStyle updatedStyle = new ElementStyle(
                    FillStyle.of(RgbaColor.fromHex(fillColor.get())),
                    StrokeStyle.of(RgbaColor.fromHex(strokeColor.get()), parseDouble(strokeWidth.get(), "grosor del borde")),
                    new TextStyle(
                            fontFamily.get().isBlank() ? currentStyle.text().fontFamily() : fontFamily.get(),
                            parseDouble(fontSize.get(), "tamaño de fuente"),
                            RgbaColor.fromHex(textColor.get()),
                            currentStyle.text().weight(),
                            currentStyle.text().posture()
                    )
            );
            updated = changeElementStyleUseCase.changeStyle(updated, elementId, updatedStyle);
            projectConsumer.accept(updated);
            syncSourceMarkdown(updated);
            selectionModel.select(elementId);
            dirtyMarker.run();
            statusConsumer.accept("Cambios aplicados correctamente.");
            inspectorMessage.set("Cambios aplicados correctamente.");
        } catch (IllegalArgumentException exception) {
            inspectorMessage.set("No se pudo aplicar: " + exception.getMessage());
            statusConsumer.accept("No se pudieron aplicar los cambios: " + exception.getMessage());
        }
    }

    public void applyDiagramAppearance() {
        DiagramProject project = projectSupplier.get();
        if (project == null) {
            inspectorMessage.set("No hay proyecto abierto.");
            return;
        }
        try {
            DiagramAppearance appearance = new DiagramAppearance(
                    RgbaColor.fromHex(workspaceBackgroundColor.get()),
                    RgbaColor.fromHex(diagramBackgroundColor.get())
            );
            DiagramProject updated = changeDiagramAppearanceUseCase.changeAppearance(project, appearance);
            if (!projectTitle.get().isBlank()) {
                updated = updated.withMetadata(updated.metadata().withTitle(projectTitle.get()));
            }
            updated = updated.withMetadata(updated.metadata().withDescription(projectDescription.get()));
            projectConsumer.accept(updated);
            syncSourceMarkdown(updated);
            dirtyMarker.run();
            statusConsumer.accept("Apariencia del diagrama aplicada correctamente.");
            inspectorMessage.set("Apariencia del diagrama aplicada correctamente.");
        } catch (IllegalArgumentException exception) {
            inspectorMessage.set("No se pudo aplicar: " + exception.getMessage());
            statusConsumer.accept("No se pudo aplicar la apariencia del diagrama: " + exception.getMessage());
        }
    }

    public void applyCategoryStyles() {
        DiagramProject project = projectSupplier.get();
        if (project == null) {
            inspectorMessage.set("No hay proyecto abierto.");
            return;
        }
        try {
            RgbaColor entityFill = RgbaColor.fromHex(entityCategoryFillColor.get());
            RgbaColor attributeFill = RgbaColor.fromHex(attributeCategoryFillColor.get());
            RgbaColor derivedFill = RgbaColor.fromHex(derivedAttributeCategoryFillColor.get());
            RgbaColor relationshipFill = RgbaColor.fromHex(relationshipCategoryFillColor.get());
            RgbaColor connectorStroke = RgbaColor.fromHex(connectorCategoryColor.get());
            RgbaColor border = RgbaColor.fromHex(globalBorderColor.get());

            DiagramProject updated = project;
            for (EntityElement entity : updated.model().entities()) {
                updated = withNodeFillAndBorder(updated, entity.id(), entityFill, border);
                for (AttributeElement attribute : entity.attributes()) {
                    RgbaColor fill = attribute.isDerived() ? derivedFill : attributeFill;
                    updated = withNodeFillAndBorder(updated, attribute.id(), fill, border);
                }
            }
            for (RelationshipElement relationship : updated.model().relationships()) {
                updated = withNodeFillAndBorder(updated, relationship.id(), relationshipFill, border);
            }
            for (ConnectorLayout connector : updated.layouts().activeLayout().connectors()) {
                ElementStyle current = DiagramStyleResolver.resolvedStyleFor(updated, connector.connectorId());
                ElementStyle styled = current.withStroke(new StrokeStyle(
                        connectorStroke,
                        current.stroke().width(),
                        current.stroke().pattern()
                ));
                updated = changeElementStyleUseCase.changeStyle(updated, connector.connectorId(), styled);
            }

            projectConsumer.accept(updated);
            dirtyMarker.run();
            refreshFromSelection();
            statusConsumer.accept("Estilos por categoría aplicados correctamente.");
            inspectorMessage.set("Estilos por categoría aplicados correctamente.");
        } catch (IllegalArgumentException exception) {
            inspectorMessage.set("No se pudo aplicar: " + exception.getMessage());
            statusConsumer.accept("No se pudieron aplicar los estilos por categoría: " + exception.getMessage());
        }
    }

    private DiagramProject withNodeFillAndBorder(
            DiagramProject project,
            DiagramElementId elementId,
            RgbaColor fill,
            RgbaColor border
    ) {
        ElementStyle current = DiagramStyleResolver.resolvedStyleFor(project, elementId);
        ElementStyle styled = current
                .withFill(FillStyle.of(fill))
                .withStroke(new StrokeStyle(border, current.stroke().width(), current.stroke().pattern()));
        return changeElementStyleUseCase.changeStyle(project, elementId, styled);
    }

    public StringProperty selectedElementIdProperty() { return selectedElementId; }
    public StringProperty selectedElementNameProperty() { return selectedElementName; }
    public StringProperty selectedElementTypeProperty() { return selectedElementType; }
    public StringProperty selectedNotationProperty() { return selectedNotation; }
    public StringProperty projectTitleProperty() { return projectTitle; }
    public StringProperty projectDescriptionProperty() { return projectDescription; }
    public StringProperty selectionTitleProperty() { return selectionTitle; }
    public StringProperty selectionSummaryProperty() { return selectionSummary; }
    public StringProperty entityKindProperty() { return entityKind; }
    public StringProperty entityModuleProperty() { return entityModule; }
    public StringProperty entityAttributeCountProperty() { return entityAttributeCount; }
    public StringProperty entityDescriptionProperty() { return entityDescription; }
    public StringProperty editableDescriptionProperty() { return editableDescription; }
    public StringProperty attributeOwnerProperty() { return attributeOwner; }
    public StringProperty attributeTagsProperty() { return attributeTags; }
    public StringProperty attributeDescriptionProperty() { return attributeDescription; }
    public StringProperty relationshipFromProperty() { return relationshipFrom; }
    public StringProperty relationshipToProperty() { return relationshipTo; }
    public StringProperty relationshipCardinalitiesProperty() { return relationshipCardinalities; }
    public StringProperty relationshipFromCardinalityProperty() { return relationshipFromCardinality; }
    public StringProperty relationshipToCardinalityProperty() { return relationshipToCardinality; }
    public StringProperty relationshipParticipationProperty() { return relationshipParticipation; }
    public StringProperty relationshipKindProperty() { return relationshipKind; }
    public StringProperty relationshipDescriptionProperty() { return relationshipDescription; }
    public StringProperty connectorSummaryProperty() { return connectorSummary; }
    public StringProperty positionXProperty() { return positionX; }
    public StringProperty positionYProperty() { return positionY; }
    public StringProperty widthProperty() { return width; }
    public StringProperty heightProperty() { return height; }
    public StringProperty fillColorProperty() { return fillColor; }
    public StringProperty strokeColorProperty() { return strokeColor; }
    public StringProperty textColorProperty() { return textColor; }
    public StringProperty strokeWidthProperty() { return strokeWidth; }
    public StringProperty fontFamilyProperty() { return fontFamily; }
    public StringProperty fontSizeProperty() { return fontSize; }
    public StringProperty workspaceBackgroundColorProperty() { return workspaceBackgroundColor; }
    public StringProperty diagramBackgroundColorProperty() { return diagramBackgroundColor; }
    public StringProperty entityCategoryFillColorProperty() { return entityCategoryFillColor; }
    public StringProperty attributeCategoryFillColorProperty() { return attributeCategoryFillColor; }
    public StringProperty derivedAttributeCategoryFillColorProperty() { return derivedAttributeCategoryFillColor; }
    public StringProperty relationshipCategoryFillColorProperty() { return relationshipCategoryFillColor; }
    public StringProperty connectorCategoryColorProperty() { return connectorCategoryColor; }
    public StringProperty globalBorderColorProperty() { return globalBorderColor; }
    public StringProperty connectorColorProperty() { return connectorColor; }
    public StringProperty connectorWidthProperty() { return connectorWidth; }
    public StringProperty connectorPatternProperty() { return connectorPattern; }
    public StringProperty sourceAnchorProperty() { return sourceAnchor; }
    public StringProperty targetAnchorProperty() { return targetAnchor; }
    public StringProperty sourceMarkerOrientationProperty() { return sourceMarkerOrientation; }
    public StringProperty targetMarkerOrientationProperty() { return targetMarkerOrientation; }
    public StringProperty inspectorMessageProperty() { return inspectorMessage; }
    public BooleanProperty projectOpenProperty() { return projectOpen; }
    public BooleanProperty hasSelectionProperty() { return hasSelection; }
    public BooleanProperty entitySelectedProperty() { return entitySelected; }
    public BooleanProperty attributeSelectedProperty() { return attributeSelected; }
    public BooleanProperty relationshipSelectedProperty() { return relationshipSelected; }
    public BooleanProperty editableProperty() { return editable; }
    public BooleanProperty nodeEditableProperty() { return nodeEditable; }
    public BooleanProperty connectorEditableProperty() { return connectorEditable; }

    private void loadSelectedElement(DiagramProject project, DiagramElementId elementId, DiagramElement element) {
        selectedElementId.set(elementId.value());
        selectedElementName.set(element.name());
        selectedElementType.set(toDisplayType(element.type()));
        project.layouts().activeLayout().nodeFor(elementId).ifPresentOrElse(this::loadNodeLayout, this::clearLayoutFields);
        loadStyle(project, elementId);
        loadElementContext(project, element);
        hasSelection.set(true);
        editable.set(true);
        nodeEditable.set(true);
        connectorEditable.set(false);
        inspectorMessage.set(toDisplayType(element.type()) + " seleccionada: " + element.name());
        statusConsumer.accept(toDisplayType(element.type()) + " seleccionada: " + element.name() + ".");
    }

    private void loadSelectedConnector(DiagramProject project, DiagramElementId elementId, ConnectorLayout connector) {
        clearSpecificContext();
        selectedElementId.set(elementId.value());
        selectedElementName.set("Conexión");
        selectedElementType.set("Conexión");
        selectionTitle.set("Conexión seleccionada");
        selectionSummary.set("Ajusta sus anclas solo cuando la unión visual necesite una corrección puntual.");
        connectorSummary.set(describeConnector(project, connector));
        clearLayoutFields();
        loadConnectorLayout(connector);
        loadConnectorStyle(project, elementId);
        hasSelection.set(true);
        editable.set(true);
        nodeEditable.set(false);
        connectorEditable.set(true);
        inspectorMessage.set("Conexión seleccionada. Mantén la opción automática salvo que necesites corregir la unión.");
        statusConsumer.accept("Conexión seleccionada.");
    }

    private void clearFields(String message) {
        selectedElementId.set("-");
        selectedElementName.set("Sin selección");
        selectedElementType.set("Ninguno");
        selectionTitle.set(projectOpen.get() ? "Sin selección" : "Sin proyecto abierto");
        selectionSummary.set(projectOpen.get()
                ? "Selecciona una entidad, atributo, relación o conector del diagrama."
                : "Abre o importa un modelo para revisar y editar sus propiedades.");
        clearSpecificContext();
        clearLayoutFields();
        sourceAnchor.set("Automática");
        targetAnchor.set("Automática");
        sourceMarkerOrientation.set("Automática");
        targetMarkerOrientation.set("Automática");
        connectorColor.set("#505050");
        connectorWidth.set("1");
        connectorPattern.set("Continua");
        fillColor.set("#FFFFFF");
        strokeColor.set("#505050");
        textColor.set("#232323");
        strokeWidth.set("1");
        fontFamily.set("Segoe UI");
        fontSize.set("12");
        hasSelection.set(false);
        editable.set(false);
        nodeEditable.set(false);
        connectorEditable.set(false);
        inspectorMessage.set(message);
    }

    private void clearSpecificContext() {
        entitySelected.set(false);
        attributeSelected.set(false);
        relationshipSelected.set(false);
        entityKind.set("—");
        entityModule.set("—");
        entityAttributeCount.set("—");
        entityDescription.set("—");
        attributeOwner.set("—");
        attributeTags.set("—");
        attributeDescription.set("—");
        relationshipFrom.set("—");
        relationshipTo.set("—");
        relationshipCardinalities.set("—");
        relationshipParticipation.set("—");
        relationshipKind.set("—");
        relationshipDescription.set("—");
        editableDescription.set("");
        connectorSummary.set("—");
    }

    private void clearLayoutFields() {
        positionX.set("");
        positionY.set("");
        width.set("");
        height.set("");
    }

    private void loadNodeLayout(NodeLayout node) {
        positionX.set(format(node.x()));
        positionY.set(format(node.y()));
        width.set(format(node.width()));
        height.set(format(node.height()));
    }

    private void loadConnectorLayout(ConnectorLayout connector) {
        sourceAnchor.set(displayAnchorSide(connector.sourceAnchor()));
        targetAnchor.set(displayAnchorSide(connector.targetAnchor()));
        sourceMarkerOrientation.set(displayMarkerOrientation(connector.sourceMarkerOrientation()));
        targetMarkerOrientation.set(displayMarkerOrientation(connector.targetMarkerOrientation()));
    }

    private void loadConnectorStyle(DiagramProject project, DiagramElementId connectorId) {
        ElementStyle style = DiagramStyleResolver.resolvedStyleFor(project, connectorId);
        connectorColor.set(style.stroke().color().toHex());
        connectorWidth.set(format(style.stroke().width()));
        connectorPattern.set(displayStrokePattern(style.stroke().pattern()));
    }

    private void loadStyle(DiagramProject project, DiagramElementId elementId) {
        ElementStyle style = DiagramStyleResolver.resolvedStyleFor(project, elementId);
        fillColor.set(style.fill().color().toHex());
        strokeColor.set(style.stroke().color().toHex());
        textColor.set(style.text().color().toHex());
        strokeWidth.set(format(style.stroke().width()));
        fontFamily.set(style.text().fontFamily());
        fontSize.set(format(style.text().fontSize()));
    }

    private void loadDiagramAppearance(DiagramProject project) {
        DiagramAppearance appearance = project.styleSheet().appearance();
        projectTitle.set(project.metadata().title());
        projectDescription.set(project.metadata().description());
        workspaceBackgroundColor.set(appearance.workspaceBackground().toHex());
        diagramBackgroundColor.set(appearance.diagramBackground().toHex());
    }

    private void loadElementContext(DiagramProject project, DiagramElement element) {
        clearSpecificContext();
        selectionTitle.set(toDisplayType(element.type()) + ": " + element.name());
        if (element instanceof EntityElement entity) {
            entitySelected.set(true);
            selectionSummary.set("Entidad del modelo conceptual con sus atributos asociados.");
            entityKind.set(displayEntityKind(entity.kind()));
            entityModule.set(blankAsDash(entity.module()));
            entityAttributeCount.set(entity.attributes().size() == 1 ? "1 atributo" : entity.attributes().size() + " atributos");
            entityDescription.set(blankAsDash(entity.description()));
            editableDescription.set(entity.description());
        } else if (element instanceof AttributeElement attribute) {
            attributeSelected.set(true);
            selectionSummary.set("Atributo que describe información propia de una entidad.");
            attributeOwner.set(entityNameOwningAttribute(project, attribute.id()).orElse("—"));
            attributeTags.set(displayTags(attribute.tags()));
            attributeDescription.set(blankAsDash(attribute.description()));
            editableDescription.set(attribute.description());
        } else if (element instanceof RelationshipElement relationship) {
            relationshipSelected.set(true);
            selectionSummary.set("Relación conceptual entre dos entidades del modelo.");
            String fromName = entityNameById(project, relationship.fromEntityId()).orElse(relationship.fromEntityId().value());
            String toName = entityNameById(project, relationship.toEntityId()).orElse(relationship.toEntityId().value());
            relationshipFrom.set(fromName);
            relationshipTo.set(toName);
            relationshipCardinalities.set(fromName + " " + relationship.fromCardinality().displayText()
                    + " / " + toName + " " + relationship.toCardinality().displayText());
            relationshipFromCardinality.set(relationship.fromCardinality().displayText());
            relationshipToCardinality.set(relationship.toCardinality().displayText());
            relationshipParticipation.set(displayParticipation(relationship.fromParticipation())
                    + " / " + displayParticipation(relationship.toParticipation()));
            relationshipKind.set(displayRelationshipKind(relationship.kind()));
            relationshipDescription.set(blankAsDash(relationship.description()));
            editableDescription.set(relationship.description());
        }
    }

    private Optional<DiagramElement> findElement(DiagramProject project, DiagramElementId elementId) {
        Optional<EntityElement> entity = project.model().entityById(elementId);
        if (entity.isPresent()) {
            return Optional.of(entity.get());
        }
        Optional<RelationshipElement> relationship = project.model().relationshipById(elementId);
        if (relationship.isPresent()) {
            return Optional.of(relationship.get());
        }
        for (EntityElement item : project.model().entities()) {
            Optional<AttributeElement> attribute = item.attributeById(elementId);
            if (attribute.isPresent()) {
                return Optional.of(attribute.get());
            }
        }
        return Optional.empty();
    }

    private Optional<String> entityNameById(DiagramProject project, DiagramElementId entityId) {
        return project.model().entityById(entityId).map(EntityElement::name);
    }

    private Optional<String> entityNameOwningAttribute(DiagramProject project, DiagramElementId attributeId) {
        return project.model().entities().stream()
                .filter(entity -> entity.attributeById(attributeId).isPresent())
                .map(EntityElement::name)
                .findFirst();
    }

    private String describeConnector(DiagramProject project, ConnectorLayout connector) {
        String source = readableElementName(project, connector.sourceElementId()).orElse(connector.sourceElementId().value());
        String target = readableElementName(project, connector.targetElementId()).orElse(connector.targetElementId().value());
        return source + " → " + target;
    }

    private Optional<String> readableElementName(DiagramProject project, DiagramElementId elementId) {
        return findElement(project, elementId).map(DiagramElement::name);
    }

    private String toDisplayType(DiagramElementType type) {
        return switch (type) {
            case ENTITY -> "Entidad";
            case ATTRIBUTE -> "Atributo";
            case RELATIONSHIP -> "Relación";
        };
    }

    private String displayEntityKind(EntityKind kind) {
        return switch (kind) {
            case STRONG -> "Fuerte";
            case WEAK -> "Débil";
        };
    }

    private String displayRelationshipKind(RelationshipKind kind) {
        return switch (kind) {
            case REGULAR -> "Regular";
            case IDENTIFYING -> "Identificadora";
            case ASSOCIATIVE -> "Asociativa";
        };
    }

    private String displayParticipation(ParticipationType participation) {
        return switch (participation) {
            case UNSPECIFIED -> "No indicada";
            case PARTIAL -> "Parcial";
            case TOTAL -> "Total";
        };
    }

    private String displayTags(Set<AttributeTag> tags) {
        if (tags == null || tags.isEmpty()) {
            return "Sin marcas especiales";
        }
        return tags.stream()
                .map(this::displayTag)
                .sorted()
                .collect(Collectors.joining(", "));
    }

    private String displayTag(AttributeTag tag) {
        return switch (tag) {
            case PRIMARY_KEY -> "Clave principal";
            case PARTIAL_KEY -> "Clave parcial";
            case OPTIONAL -> "Opcional";
            case DERIVED -> "Derivado";
            case UNIQUE -> "Único";
            case MULTIVALUED -> "Multivaluado";
            case COMPOSITE -> "Compuesto";
            case SENSITIVE -> "Sensible";
        };
    }


    private void syncSourceMarkdown(DiagramProject project) {
        SourceMarkdownSyncResult result = sourceMarkdownSynchronizer.synchronize(project);
        if (result.shouldNotifyUser()) {
            statusConsumer.accept(result.message());
        }
    }

    private AnchorSide parseAnchorSide(String rawValue, String fieldName) {
        return switch (normalizeOption(rawValue)) {
            case "AUTO", "AUTOMATICA", "AUTOMATICO" -> AnchorSide.AUTO;
            case "IZQUIERDA", "LEFT" -> AnchorSide.LEFT;
            case "DERECHA", "RIGHT" -> AnchorSide.RIGHT;
            case "SUPERIOR", "ARRIBA", "TOP" -> AnchorSide.TOP;
            case "INFERIOR", "ABAJO", "BOTTOM" -> AnchorSide.BOTTOM;
            case "CENTRO", "CENTER" -> AnchorSide.CENTER;
            default -> throw new IllegalArgumentException("Valor inválido en " + fieldName + ": " + rawValue);
        };
    }

    private MarkerOrientation parseMarkerOrientation(String rawValue, String fieldName) {
        return switch (normalizeOption(rawValue)) {
            case "AUTO", "AUTOMATICA", "AUTOMATICO" -> MarkerOrientation.AUTO;
            case "IZQUIERDA", "LEFT" -> MarkerOrientation.LEFT;
            case "DERECHA", "RIGHT" -> MarkerOrientation.RIGHT;
            case "ARRIBA", "SUPERIOR", "UP" -> MarkerOrientation.UP;
            case "ABAJO", "INFERIOR", "DOWN" -> MarkerOrientation.DOWN;
            default -> throw new IllegalArgumentException("Valor inválido en " + fieldName + ": " + rawValue);
        };
    }

    private StrokePattern parseStrokePattern(String rawValue) {
        return switch (normalizeOption(rawValue)) {
            case "PUNTEADA", "DASHED", "GUIONES" -> StrokePattern.DASHED;
            case "PUNTOS", "DOTTED" -> StrokePattern.DOTTED;
            default -> StrokePattern.SOLID;
        };
    }

    private String displayStrokePattern(StrokePattern pattern) {
        return switch (pattern == null ? StrokePattern.SOLID : pattern) {
            case SOLID -> "Continua";
            case DASHED -> "Punteada";
            case DOTTED -> "Puntos";
        };
    }

    private String normalizeOption(String rawValue) {
        String value = rawValue == null ? "AUTO" : rawValue.trim().toUpperCase(Locale.ROOT);
        return Normalizer.normalize(value, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
    }

    private String displayNotation(String notation) {
        if (notation == null || notation.isBlank()) {
            return "—";
        }
        return notation.toLowerCase(Locale.ROOT).contains("crow") ? "Pata de gallo" : notation.trim();
    }

    private String displayAnchorSide(AnchorSide anchorSide) {
        return switch (anchorSide) {
            case AUTO -> "Automática";
            case LEFT -> "Izquierda";
            case RIGHT -> "Derecha";
            case TOP -> "Superior";
            case BOTTOM -> "Inferior";
            case CENTER -> "Centro";
        };
    }

    private String displayMarkerOrientation(MarkerOrientation orientation) {
        return switch (orientation) {
            case AUTO -> "Automática";
            case LEFT -> "Izquierda";
            case RIGHT -> "Derecha";
            case UP -> "Arriba";
            case DOWN -> "Abajo";
        };
    }

    private double parseDouble(String rawValue, String fieldName) {
        try {
            double value = Double.parseDouble(rawValue.trim().replace(',', '.'));
            if (!Double.isFinite(value)) {
                throw new NumberFormatException("no finito");
            }
            return value;
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Valor numérico inválido en " + fieldName + ": " + rawValue);
        }
    }

    private String format(double value) {
        if (Math.rint(value) == value) {
            return Long.toString(Math.round(value));
        }
        return String.format(Locale.ROOT, "%.2f", value);
    }

    private String blankAsDash(String value) {
        return value == null || value.isBlank() ? "—" : value.trim();
    }
}
