package com.marcosmoreira.domainmodelstudio.presentation.canvas;

import com.marcosmoreira.domainmodelstudio.application.editing.AddAttributeUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.AddBendPointUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.AddEntityUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.AddRelationshipUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.DuplicateEntityUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.ChangeConnectorAnchorsUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.MoveBendPointUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.MoveConnectorLabelUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.MoveElementUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.RemoveBendPointUseCase;
import com.marcosmoreira.domainmodelstudio.application.editing.RemoveDiagramElementUseCase;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.layout.AnchorSide;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.domain.notation.NotationType;
import com.marcosmoreira.domainmodelstudio.presentation.selection.DiagramSelectionModel;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Objects;
import java.util.function.Consumer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Estado presentable del canvas.
 *
 * <p>Conserva la selección visual y delega el movimiento
 * de figuras al caso de uso de application. La View no modifica directamente el layout:
 * informa el delta de arrastre y el ViewModel obtiene un nuevo {@link DiagramProject}.</p>
 */
public final class DiagramCanvasViewModel {

    private final StringProperty title = new SimpleStringProperty("Inicio");
    private final StringProperty subtitle = new SimpleStringProperty(
            "Prepara, abre o importa un modelo para empezar a trabajar el diagrama."
    );
    private final StringProperty importedSummary = new SimpleStringProperty(
            "La pantalla de inicio reúne las acciones principales del producto."
    );
    private final StringProperty zoomText = new SimpleStringProperty("100%");
    private final ObjectProperty<DiagramProject> currentProject = new SimpleObjectProperty<>();
    private final ObjectProperty<ConnectorBendPointSelection> selectedBendPoint = new SimpleObjectProperty<>();
    private final DiagramSelectionModel selectionModel;
    private final AddEntityUseCase addEntityUseCase;
    private final AddAttributeUseCase addAttributeUseCase;
    private final AddRelationshipUseCase addRelationshipUseCase;
    private final DuplicateEntityUseCase duplicateEntityUseCase;
    private final RemoveDiagramElementUseCase removeDiagramElementUseCase;
    private final MoveElementUseCase moveElementUseCase;
    private final AddBendPointUseCase addBendPointUseCase;
    private final MoveBendPointUseCase moveBendPointUseCase;
    private final MoveConnectorLabelUseCase moveConnectorLabelUseCase;
    private final RemoveBendPointUseCase removeBendPointUseCase;
    private final ChangeConnectorAnchorsUseCase changeConnectorAnchorsUseCase;
    private final Consumer<DiagramElementId> movementListener;
    private Consumer<DiagramProject> structuralEditListener = ignored -> { };
    private final ConceptualCanvasEditHistory editHistory = new ConceptualCanvasEditHistory();
    private PngExportAction pngExportAction;
    private CanvasNavigationAction canvasNavigationAction;
    private WelcomeActions welcomeActions = WelcomeActions.empty();
    private ConceptualCanvasTool activeTool = ConceptualCanvasTool.NONE;
    private DiagramElementId pendingRelationshipSource;

    public DiagramCanvasViewModel(
            DiagramSelectionModel selectionModel,
            AddEntityUseCase addEntityUseCase,
            AddAttributeUseCase addAttributeUseCase,
            AddRelationshipUseCase addRelationshipUseCase,
            DuplicateEntityUseCase duplicateEntityUseCase,
            RemoveDiagramElementUseCase removeDiagramElementUseCase,
            MoveElementUseCase moveElementUseCase,
            AddBendPointUseCase addBendPointUseCase,
            MoveBendPointUseCase moveBendPointUseCase,
            MoveConnectorLabelUseCase moveConnectorLabelUseCase,
            RemoveBendPointUseCase removeBendPointUseCase,
            ChangeConnectorAnchorsUseCase changeConnectorAnchorsUseCase,
            Consumer<DiagramElementId> movementListener
    ) {
        this.selectionModel = Objects.requireNonNull(selectionModel, "selectionModel");
        this.addEntityUseCase = Objects.requireNonNull(addEntityUseCase, "addEntityUseCase");
        this.addAttributeUseCase = Objects.requireNonNull(addAttributeUseCase, "addAttributeUseCase");
        this.addRelationshipUseCase = Objects.requireNonNull(addRelationshipUseCase, "addRelationshipUseCase");
        this.duplicateEntityUseCase = Objects.requireNonNull(duplicateEntityUseCase, "duplicateEntityUseCase");
        this.removeDiagramElementUseCase = Objects.requireNonNull(removeDiagramElementUseCase, "removeDiagramElementUseCase");
        this.moveElementUseCase = Objects.requireNonNull(moveElementUseCase, "moveElementUseCase");
        this.addBendPointUseCase = Objects.requireNonNull(addBendPointUseCase, "addBendPointUseCase");
        this.moveBendPointUseCase = Objects.requireNonNull(moveBendPointUseCase, "moveBendPointUseCase");
        this.moveConnectorLabelUseCase = Objects.requireNonNull(moveConnectorLabelUseCase, "moveConnectorLabelUseCase");
        this.removeBendPointUseCase = Objects.requireNonNull(removeBendPointUseCase, "removeBendPointUseCase");
        this.changeConnectorAnchorsUseCase = Objects.requireNonNull(changeConnectorAnchorsUseCase, "changeConnectorAnchorsUseCase");
        this.movementListener = movementListener == null ? (ignored -> { }) : movementListener;
    }

    public StringProperty titleProperty() {
        return title;
    }

    public StringProperty subtitleProperty() {
        return subtitle;
    }

    public StringProperty importedSummaryProperty() {
        return importedSummary;
    }

    public StringProperty zoomTextProperty() {
        return zoomText;
    }

    public ObjectProperty<DiagramProject> currentProjectProperty() {
        return currentProject;
    }

    public ObjectProperty<DiagramElementId> selectedElementIdProperty() {
        return selectionModel.selectedElementIdProperty();
    }

    public ObjectProperty<Set<DiagramElementId>> selectedElementIdsProperty() {
        return selectionModel.selectedElementIdsProperty();
    }

    public ObjectProperty<ConnectorBendPointSelection> selectedBendPointProperty() {
        return selectedBendPoint;
    }

    public ConnectorBendPointSelection selectedBendPoint() {
        return selectedBendPoint.get();
    }

    public boolean isSelectedBendPoint(DiagramElementId connectorId, int bendPointIndex) {
        ConnectorBendPointSelection selection = selectedBendPoint.get();
        return selection != null && selection.matches(connectorId, bendPointIndex);
    }

    public DiagramProject currentProject() {
        return currentProject.get();
    }

    public DiagramElementId selectedElementId() {
        return selectionModel.selectedElementId();
    }

    public Set<DiagramElementId> selectedElementIds() {
        return selectionModel.selectedElementIds();
    }

    public boolean isSelected(DiagramElementId elementId) {
        return selectionModel.isSelected(elementId);
    }

    public int selectionCount() {
        return selectionModel.selectionCount();
    }

    public void toggleElementSelection(DiagramElementId elementId) {
        selectedBendPoint.set(null);
        selectionModel.toggle(elementId);
        updateSelectionSummary();
    }

    public void selectElements(Collection<DiagramElementId> elementIds, boolean extendCurrentSelection) {
        selectedBendPoint.set(null);
        if (extendCurrentSelection) {
            selectionModel.addAll(elementIds);
        } else {
            selectionModel.selectAll(elementIds);
        }
        updateSelectionSummary();
    }

    public void showImportedProject(DiagramProject project) {
        Objects.requireNonNull(project, "project");
        applyProjectText(project);
        importedSummary.set(summaryForProject(project));
        selectedBendPoint.set(null);
        selectionModel.clearSelection();
        resetHistory(project);
    }

    public void replaceCurrentProject(DiagramProject project) {
        Objects.requireNonNull(project, "project");
        applyProjectText(project);
        importedSummary.set("Cambios aplicados al diagrama actual.");
        currentProject.set(project);
    }

    public void clearProject() {
        currentProject.set(null);
        selectedBendPoint.set(null);
        selectionModel.clearSelection();
        editHistory.reset();
        title.set("Inicio");
        subtitle.set("Prepara, abre o importa un modelo para empezar a trabajar el diagrama.");
        importedSummary.set("La pantalla de inicio reúne las acciones principales del producto.");
    }

    /**
     * Aplica una edición que debe entrar al historial de undo/redo.
     */
    public void applyEditedProject(DiagramProject project) {
        Objects.requireNonNull(project, "project");
        beginUndoableEdit();
        replaceCurrentProject(project);
    }

    public void applyVisualSelectionPaste(DiagramProject project, String summary) {
        Objects.requireNonNull(project, "project");
        beginUndoableEdit();
        replaceCurrentProject(project);
        selectedBendPoint.set(null);
        selectionModel.clearSelection();
        importedSummary.set(summary == null || summary.isBlank()
                ? "Estado: selección visual pegada."
                : summary);
        structuralEditListener.accept(project);
        movementListener.accept(null);
    }

    public boolean canUndo() {
        return editHistory.canUndo();
    }

    public boolean canRedo() {
        return editHistory.canRedo();
    }

    public boolean undo() {
        if (!editHistory.canUndo() || currentProject.get() == null) {
            return false;
        }
        selectedBendPoint.set(null);
        replaceCurrentProject(editHistory.undo(currentProject.get()));
        importedSummary.set("Proyecto restaurado mediante Deshacer.");
        return true;
    }

    public boolean redo() {
        if (!editHistory.canRedo() || currentProject.get() == null) {
            return false;
        }
        selectedBendPoint.set(null);
        replaceCurrentProject(editHistory.redo(currentProject.get()));
        importedSummary.set("Proyecto restaurado mediante Rehacer.");
        return true;
    }

    private void resetHistory(DiagramProject project) {
        editHistory.reset();
        currentProject.set(project);
    }

    private void beginUndoableEdit() {
        editHistory.prepareEdit(currentProject.get());
    }

    private void applyProjectText(DiagramProject project) {
        title.set(project.metadata().title());
        int entityCount = project.model().entityCount();
        int relationshipCount = project.model().relationshipCount();
        String notation = project.metadata().activeNotation().displayName();
        if (entityCount + relationshipCount == 0) {
            subtitle.set("Proyecto vacío. Importa un modelo Markdown o empieza a preparar la estructura del diagrama.");
            return;
        }
        subtitle.set("Modelo cargado: " + entityCount + " entidades / "
                + relationshipCount + " relaciones. Vista activa: "
                + notation + ".");
    }

    private String summaryForProject(DiagramProject project) {
        if (project.model().entityCount() + project.model().relationshipCount() == 0) {
            return "Estado: proyecto nuevo sin elementos. Las opciones de guardado están disponibles.";
        }
        return "Estado: modelo listo. Puedes mover elementos, revisar propiedades, guardar el proyecto y exportar el diagrama.";
    }

    public void selectElement(DiagramElementId elementId) {
        selectedBendPoint.set(null);
        selectionModel.select(elementId);
        updateSelectionSummary();
    }

    public void selectBendPoint(DiagramElementId connectorId, int bendPointIndex) {
        if (connectorId == null) {
            return;
        }
        ConnectorBendPointSelection selection = new ConnectorBendPointSelection(connectorId, bendPointIndex);
        selectedBendPoint.set(selection);
        selectionModel.select(connectorId);
        importedSummary.set("Estado: punto intermedio seleccionado. Usa Suprimir o el botón Quitar punto de la toolbar para eliminar solo ese punto.");
    }

    public void clearSelection() {
        selectedBendPoint.set(null);
        selectionModel.clearSelection();
        updateSelectionSummary();
    }


    public void registerStructuralEditListener(Consumer<DiagramProject> structuralEditListener) {
        this.structuralEditListener = structuralEditListener == null ? ignored -> { } : structuralEditListener;
    }

    public void beginAddEntityTool() {
        if (currentProject.get() == null) {
            importedSummary.set("Abre o crea un proyecto antes de agregar entidades.");
            return;
        }
        activeTool = ConceptualCanvasTool.ADD_ENTITY;
        pendingRelationshipSource = null;
        importedSummary.set("Herramienta Entidad activa: haz clic en el lienzo para agregar una entidad.");
    }

    public void beginAddRelationshipTool() {
        if (currentProject.get() == null) {
            importedSummary.set("Abre o crea un proyecto antes de agregar relaciones.");
            return;
        }
        activeTool = ConceptualCanvasTool.ADD_RELATIONSHIP;
        pendingRelationshipSource = null;
        importedSummary.set("Herramienta Relación activa: selecciona la entidad origen y luego la entidad destino.");
    }

    public boolean hasActiveCanvasTool() {
        return activeTool != ConceptualCanvasTool.NONE;
    }

    public boolean handleCanvasPrimaryClick(double x, double y) {
        if (currentProject.get() == null || activeTool != ConceptualCanvasTool.ADD_ENTITY) {
            return false;
        }
        DiagramProject updated = addEntityUseCase.add(currentProject.get(), x, y);
        applyStructuralEdit(updated, newestEntityId(updated));
        activeTool = ConceptualCanvasTool.NONE;
        importedSummary.set("Entidad agregada. Ajusta su nombre y descripción desde Propiedades.");
        return true;
    }

    public boolean handleElementPrimaryPressForActiveTool(DiagramElementId elementId) {
        if (currentProject.get() == null || activeTool == ConceptualCanvasTool.NONE || elementId == null) {
            return false;
        }
        if (activeTool == ConceptualCanvasTool.ADD_RELATIONSHIP) {
            if (currentProject.get().model().entityById(elementId).isEmpty()) {
                importedSummary.set("Para crear una relación, selecciona una entidad.");
                return true;
            }
            if (pendingRelationshipSource == null) {
                pendingRelationshipSource = elementId;
                selectionModel.select(elementId);
                importedSummary.set("Entidad origen seleccionada. Ahora selecciona la entidad destino.");
                return true;
            }
            DiagramProject updated = addRelationshipUseCase.add(currentProject.get(), pendingRelationshipSource, elementId);
            applyStructuralEdit(updated, newestRelationshipId(updated));
            activeTool = ConceptualCanvasTool.NONE;
            pendingRelationshipSource = null;
            importedSummary.set("Relación agregada. Revisa nombre y cardinalidades en Propiedades.");
            return true;
        }
        return false;
    }

    public void addAttributeToSelectedEntity() {
        DiagramProject project = currentProject.get();
        DiagramElementId selected = selectionModel.selectedElementId();
        if (project == null || selected == null) {
            importedSummary.set("Selecciona una entidad para agregar atributo.");
            return;
        }
        if (project.model().entityById(selected).isEmpty()) {
            importedSummary.set("La herramienta Atributo necesita una entidad seleccionada.");
            return;
        }
        DiagramProject updated = addAttributeUseCase.add(project, selected);
        applyStructuralEdit(updated, newestAttributeId(updated, selected));
        importedSummary.set("Atributo agregado a la entidad seleccionada.");
    }

    public void duplicateSelectedEntity() {
        DiagramProject project = currentProject.get();
        DiagramElementId selected = selectionModel.selectedElementId();
        if (project == null || selected == null || project.model().entityById(selected).isEmpty()) {
            importedSummary.set("Selecciona una entidad para duplicarla.");
            return;
        }
        DiagramProject updated = duplicateEntityUseCase.duplicate(project, selected);
        applyStructuralEdit(updated, newestEntityId(updated));
        importedSummary.set("Entidad duplicada. Ajusta sus propiedades si lo necesitas.");
    }

    public void removeSelectedElement() {
        if (removeSelectedBendPoint()) {
            return;
        }
        DiagramProject project = currentProject.get();
        DiagramElementId selected = selectionModel.selectedElementId();
        if (project == null || selected == null) {
            importedSummary.set("Selecciona un elemento para eliminarlo.");
            return;
        }
        DiagramProject updated = removeDiagramElementUseCase.remove(project, selected);
        applyStructuralEdit(updated, null);
        selectionModel.clearSelection();
        importedSummary.set("Elemento eliminado del modelo conceptual.");
    }

    private void applyStructuralEdit(DiagramProject updatedProject, DiagramElementId selectedElementId) {
        beginUndoableEdit();
        replaceCurrentProject(updatedProject);
        selectedBendPoint.set(null);
        if (selectedElementId != null) {
            selectionModel.select(selectedElementId);
        } else {
            selectionModel.clearSelection();
        }
        structuralEditListener.accept(updatedProject);
        movementListener.accept(selectedElementId);
    }

    private DiagramElementId newestEntityId(DiagramProject project) {
        if (project.model().entities().isEmpty()) {
            return null;
        }
        return project.model().entities().get(project.model().entities().size() - 1).id();
    }

    private DiagramElementId newestRelationshipId(DiagramProject project) {
        if (project.model().relationships().isEmpty()) {
            return null;
        }
        return project.model().relationships().get(project.model().relationships().size() - 1).id();
    }

    private DiagramElementId newestAttributeId(DiagramProject project, DiagramElementId entityId) {
        return project.model().entityById(entityId)
                .filter(entity -> !entity.attributes().isEmpty())
                .map(entity -> entity.attributes().get(entity.attributes().size() - 1).id())
                .orElse(entityId);
    }


    public void selectAllVisibleElements() {
        DiagramProject project = currentProject.get();
        if (project == null) {
            return;
        }
        LinkedHashSet<DiagramElementId> visibleElementIds = new LinkedHashSet<>();
        project.layouts().activeLayout().nodes().forEach(nodeLayout -> visibleElementIds.add(nodeLayout.elementId()));
        selectionModel.selectAll(visibleElementIds);
        updateSelectionSummary();
    }

    public DiagramProject previewMoveSelectedElementBy(double deltaX, double deltaY) {
        Set<DiagramElementId> selected = selectionModel.selectedElementIds();
        if (selected.isEmpty() || currentProject.get() == null) {
            return currentProject.get();
        }
        DiagramProject preview = currentProject.get();
        for (DiagramElementId elementId : selected) {
            try {
                preview = moveElementUseCase.moveBy(
                        preview,
                        activeNotation(),
                        elementId,
                        deltaX,
                        deltaY
                );
            } catch (IllegalArgumentException ignored) {
                // Las conexiones pueden estar seleccionadas, pero solo se previsualizan figuras movibles.
            }
        }
        return preview;
    }

    public DiagramProject previewMoveBendPointBy(DiagramElementId connectorId, int bendPointIndex, double deltaX, double deltaY) {
        if (currentProject.get() == null || connectorId == null) {
            return currentProject.get();
        }
        try {
            return moveBendPointUseCase.moveBy(
                    currentProject.get(),
                    activeNotation(),
                    connectorId,
                    bendPointIndex,
                    deltaX,
                    deltaY
            );
        } catch (IllegalArgumentException exception) {
            return currentProject.get();
        }
    }

    public void moveSelectedElementBy(double deltaX, double deltaY) {
        Set<DiagramElementId> selected = selectionModel.selectedElementIds();
        if (selected.isEmpty() || currentProject.get() == null) {
            return;
        }
        beginUndoableEdit();
        DiagramProject moved = currentProject.get();
        int movedCount = 0;
        for (DiagramElementId elementId : selected) {
            try {
                moved = moveElementUseCase.moveBy(
                        moved,
                        activeNotation(),
                        elementId,
                        deltaX,
                        deltaY
                );
                movedCount++;
            } catch (IllegalArgumentException ignored) {
                // Las conexiones pueden estar seleccionadas, pero P-4 mueve figuras del diagrama.
            }
        }
        currentProject.set(moved);
        if (movedCount > 1) {
            importedSummary.set("Estado: selección movida. Revisa la posición y guarda los cambios cuando termines.");
        } else {
            importedSummary.set("Estado: elemento movido. Revisa la posición y guarda los cambios cuando termines.");
        }
        movementListener.accept(selectionModel.selectedElementId());
    }
    private void updateSelectionSummary() {
        if (currentProject.get() == null) {
            return;
        }
        ConnectorBendPointSelection bendPointSelection = selectedBendPoint.get();
        if (bendPointSelection != null) {
            importedSummary.set("Estado: punto intermedio seleccionado en una línea. Suprimir elimina solo ese punto, no toda la relación.");
            return;
        }
        int count = selectionModel.selectionCount();
        if (count == 0) {
            importedSummary.set("Estado: sin selección. Puedes seleccionar una figura o arrastrar un área de selección.");
        } else if (count == 1) {
            importedSummary.set("Estado: 1 elemento seleccionado. Puedes moverlo o revisar sus propiedades.");
        } else {
            importedSummary.set("Estado: " + count + " elementos seleccionados. Arrastra uno de ellos para mover el conjunto.");
        }
    }

    public void addBendPoint(DiagramElementId connectorId, double x, double y) {
        if (currentProject.get() == null || connectorId == null) {
            return;
        }
        beginUndoableEdit();
        int newIndex = bendPointCount(currentProject.get(), connectorId);
        DiagramProject updated = addBendPointUseCase.add(
                currentProject.get(),
                activeNotation(),
                connectorId,
                x,
                y
        );
        selectedBendPoint.set(new ConnectorBendPointSelection(connectorId, newIndex));
        currentProject.set(updated);
        importedSummary.set("Estado: punto intermedio agregado al conector. Puedes moverlo o eliminarlo con Suprimir.");
        selectionModel.select(connectorId);
        movementListener.accept(connectorId);
    }

    public void moveBendPointBy(DiagramElementId connectorId, int bendPointIndex, double deltaX, double deltaY) {
        if (currentProject.get() == null || connectorId == null) {
            return;
        }
        beginUndoableEdit();
        DiagramProject updated = moveBendPointUseCase.moveBy(
                currentProject.get(),
                activeNotation(),
                connectorId,
                bendPointIndex,
                deltaX,
                deltaY
        );
        selectedBendPoint.set(new ConnectorBendPointSelection(connectorId, bendPointIndex));
        currentProject.set(updated);
        importedSummary.set("Estado: punto intermedio movido.");
        selectionModel.select(connectorId);
        movementListener.accept(connectorId);
    }

    public DiagramProject previewMoveConnectorLabelBy(DiagramElementId connectorId, double deltaX, double deltaY) {
        if (currentProject.get() == null || connectorId == null) {
            return currentProject.get();
        }
        try {
            return moveConnectorLabelUseCase.moveBy(
                    currentProject.get(),
                    activeNotation(),
                    connectorId,
                    deltaX,
                    deltaY
            );
        } catch (IllegalArgumentException exception) {
            return currentProject.get();
        }
    }

    public void moveConnectorLabelBy(DiagramElementId connectorId, double deltaX, double deltaY) {
        if (currentProject.get() == null || connectorId == null) {
            return;
        }
        beginUndoableEdit();
        DiagramProject updated = moveConnectorLabelUseCase.moveBy(
                currentProject.get(),
                activeNotation(),
                connectorId,
                deltaX,
                deltaY
        );
        selectedBendPoint.set(null);
        currentProject.set(updated);
        importedSummary.set("Estado: etiqueta de relación movida.");
        selectionModel.select(connectorId);
        movementListener.accept(connectorId);
    }

    public boolean removeSelectedBendPoint() {
        ConnectorBendPointSelection selection = selectedBendPoint.get();
        if (selection == null) {
            return false;
        }
        removeBendPoint(selection.connectorId(), selection.bendPointIndex());
        return true;
    }

    public void removeBendPoint(DiagramElementId connectorId, int bendPointIndex) {
        if (currentProject.get() == null || connectorId == null) {
            return;
        }
        beginUndoableEdit();
        DiagramProject updated = removeBendPointUseCase.remove(
                currentProject.get(),
                activeNotation(),
                connectorId,
                bendPointIndex
        );
        selectedBendPoint.set(null);
        currentProject.set(updated);
        importedSummary.set("Estado: punto intermedio eliminado. La relación se conserva.");
        selectionModel.select(connectorId);
        movementListener.accept(connectorId);
    }

    private int bendPointCount(DiagramProject project, DiagramElementId connectorId) {
        if (project == null || connectorId == null) {
            return 0;
        }
        return project.layouts()
                .layoutFor(activeNotation())
                .flatMap(layout -> layout.connectorById(connectorId))
                .map(connector -> connector.bendPoints().size())
                .orElse(0);
    }
    public DiagramProject previewMoveConnectorEndpointTo(DiagramElementId connectorId, boolean sourceEndpoint, double x, double y) {
        DiagramProject project = currentProject.get();
        if (project == null || connectorId == null) {
            return project;
        }
        DiagramLayout layout = project.layouts()
                .layoutFor(activeNotation())
                .orElse(null);
        if (layout == null) {
            return project;
        }
        ConnectorLayout connector = layout.connectorById(connectorId).orElse(null);
        if (connector == null) {
            return project;
        }
        DiagramElementId nodeId = sourceEndpoint ? connector.sourceElementId() : connector.targetElementId();
        NodeLayout node = layout.nodeFor(nodeId).orElse(null);
        if (node == null) {
            return project;
        }
        AnchorSide selectedAnchor = ConceptualAnchorResolver.nearestAnchor(node, x, y);
        AnchorSide sourceAnchor = sourceEndpoint ? selectedAnchor : connector.sourceAnchor();
        AnchorSide targetAnchor = sourceEndpoint ? connector.targetAnchor() : selectedAnchor;
        try {
            return changeConnectorAnchorsUseCase.changeAnchors(
                    project,
                    activeNotation(),
                    connectorId,
                    sourceAnchor,
                    targetAnchor
            );
        } catch (IllegalArgumentException exception) {
            return project;
        }
    }

    public void moveConnectorEndpointTo(DiagramElementId connectorId, boolean sourceEndpoint, double x, double y) {
        if (currentProject.get() == null || connectorId == null) {
            return;
        }
        DiagramProject project = currentProject.get();
        DiagramLayout layout = project.layouts()
                .layoutFor(activeNotation())
                .orElse(null);
        if (layout == null) {
            return;
        }
        ConnectorLayout connector = layout.connectorById(connectorId).orElse(null);
        if (connector == null) {
            return;
        }
        DiagramElementId nodeId = sourceEndpoint ? connector.sourceElementId() : connector.targetElementId();
        NodeLayout node = layout.nodeFor(nodeId).orElse(null);
        if (node == null) {
            return;
        }
        AnchorSide selectedAnchor = ConceptualAnchorResolver.nearestAnchor(node, x, y);
        AnchorSide sourceAnchor = sourceEndpoint ? selectedAnchor : connector.sourceAnchor();
        AnchorSide targetAnchor = sourceEndpoint ? connector.targetAnchor() : selectedAnchor;
        beginUndoableEdit();
        DiagramProject updated = changeConnectorAnchorsUseCase.changeAnchors(
                project,
                activeNotation(),
                connectorId,
                sourceAnchor,
                targetAnchor
        );
        selectedBendPoint.set(null);
        currentProject.set(updated);
        importedSummary.set("Estado: extremo de conexión ajustado.");
        selectionModel.select(connectorId);
        movementListener.accept(connectorId);
    }

    public NotationType activeNotation() {
        DiagramProject project = currentProject.get();
        return project == null ? NotationType.CHEN : project.metadata().activeNotation();
    }

    public void registerWelcomeActions(WelcomeActions welcomeActions) {
        this.welcomeActions = welcomeActions == null ? WelcomeActions.empty() : welcomeActions;
    }

    public void requestNewProjectFromWelcome() {
        welcomeActions.newProject().run();
    }

    public void requestOpenProjectFromWelcome() {
        welcomeActions.openProject().run();
    }

    public void requestImportMarkdownFromWelcome() {
        welcomeActions.importMarkdown().run();
    }

    public void requestOpenExampleFromWelcome() {
        welcomeActions.openExample().run();
    }

    public void requestOpenManualFromWelcome() {
        welcomeActions.openManual().run();
    }

    public void registerPngExportAction(PngExportAction pngExportAction) {
        this.pngExportAction = pngExportAction;
    }

    public void registerCanvasNavigationAction(CanvasNavigationAction canvasNavigationAction) {
        this.canvasNavigationAction = canvasNavigationAction;
    }

    public void requestZoomIn() {
        if (canvasNavigationAction != null) {
            canvasNavigationAction.zoomIn();
        }
    }

    public void requestZoomOut() {
        if (canvasNavigationAction != null) {
            canvasNavigationAction.zoomOut();
        }
    }

    public void requestResetZoom() {
        if (canvasNavigationAction != null) {
            canvasNavigationAction.resetZoom();
        }
    }

    public void requestFitToContent() {
        if (canvasNavigationAction != null) {
            canvasNavigationAction.fitToContent();
        }
    }

    public void requestCenterDiagram() {
        if (canvasNavigationAction != null) {
            canvasNavigationAction.centerDiagram();
        }
    }

    public void requestCenterSelection() {
        if (canvasNavigationAction != null) {
            canvasNavigationAction.centerSelection();
        }
    }

    public void updateZoomFactor(double zoomFactor) {
        int percent = (int) Math.round(zoomFactor * 100.0);
        zoomText.set(percent + "%");
    }

    public void exportVisibleCanvasAsPng(Path targetFile) throws IOException {
        Objects.requireNonNull(targetFile, "targetFile");
        if (currentProject.get() == null) {
            throw new IllegalStateException("No hay proyecto abierto para exportar PNG");
        }
        if (pngExportAction == null) {
            throw new IllegalStateException("El exportador PNG del área de trabajo aún no está registrado");
        }
        pngExportAction.export(targetFile);
    }

    public record WelcomeActions(
            Runnable newProject,
            Runnable openProject,
            Runnable importMarkdown,
            Runnable openExample,
            Runnable openManual
    ) {
        public WelcomeActions {
            newProject = newProject == null ? noop() : newProject;
            openProject = openProject == null ? noop() : openProject;
            importMarkdown = importMarkdown == null ? noop() : importMarkdown;
            openExample = openExample == null ? noop() : openExample;
            openManual = openManual == null ? noop() : openManual;
        }

        public static WelcomeActions empty() {
            Runnable noop = noop();
            return new WelcomeActions(noop, noop, noop, noop, noop);
        }

        private static Runnable noop() {
            return () -> { };
        }
    }

    @FunctionalInterface
    public interface PngExportAction {
        void export(Path targetFile) throws IOException;
    }

    public interface CanvasNavigationAction {
        void zoomIn();

        void zoomOut();

        void resetZoom();

        void fitToContent();

        void centerDiagram();

        void centerSelection();
    }

    private enum ConceptualCanvasTool {
        NONE,
        ADD_ENTITY,
        ADD_RELATIONSHIP
    }

}
