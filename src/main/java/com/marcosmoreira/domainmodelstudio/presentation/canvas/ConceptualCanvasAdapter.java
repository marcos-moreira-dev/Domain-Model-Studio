package com.marcosmoreira.domainmodelstudio.presentation.canvas;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.er.EntityElement;
import com.marcosmoreira.domainmodelstudio.domain.er.RelationshipElement;
import com.marcosmoreira.domainmodelstudio.domain.layout.BendPoint;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasBounds;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasAdapter;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasConnector;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasNode;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasSelection;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.SelectedBendPoint;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasSelectionClipboardPort;
import com.marcosmoreira.domainmodelstudio.presentation.visualtransfer.ProjectVisualSelectionTransferService;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Adaptador del modelo conceptual al contrato de canvas común.
 *
 * <p>No reemplaza todavía a {@link DiagramCanvasView}. Su función es dejar al modelo
 * conceptual como primer cliente contractual del canvas compartido, conservando los
 * renderers Chen y pata de gallo mientras las demás familias se alinean progresivamente.</p>
 */
public final class ConceptualCanvasAdapter implements InteractiveCanvasAdapter, CanvasSelectionClipboardPort {

    private final DiagramCanvasViewModel viewModel;

    public ConceptualCanvasAdapter(DiagramCanvasViewModel viewModel) {
        this.viewModel = Objects.requireNonNull(viewModel, "viewModel");
    }

    @Override
    public DiagramTypeId diagramTypeId() {
        return DiagramTypeId.CONCEPTUAL_MODEL;
    }

    @Override
    public List<InteractiveCanvasNode> nodes() {
        DiagramProject project = viewModel.currentProject();
        if (project == null) {
            return List.of();
        }
        return project.model().entities().stream()
                .map(this::toNode)
                .toList();
    }

    @Override
    public List<InteractiveCanvasConnector> connectors() {
        DiagramProject project = viewModel.currentProject();
        if (project == null) {
            return List.of();
        }
        return project.model().relationships().stream()
                .map(this::toConnector)
                .toList();
    }

    @Override
    public Optional<NodeLayout> layoutForNode(String elementId) {
        DiagramProject project = viewModel.currentProject();
        if (project == null || elementId == null || elementId.isBlank()) {
            return Optional.empty();
        }
        return activeLayout(project).nodeFor(DiagramElementId.of(elementId));
    }

    @Override
    public Optional<ConnectorLayout> layoutForConnector(String connectorId) {
        DiagramProject project = viewModel.currentProject();
        if (project == null || connectorId == null || connectorId.isBlank()) {
            return Optional.empty();
        }
        return activeLayout(project).connectorById(DiagramElementId.of(connectorId));
    }

    @Override
    public InteractiveCanvasSelection selection() {
        DiagramProject project = viewModel.currentProject();
        if (project == null) {
            return InteractiveCanvasSelection.empty();
        }
        Set<String> selectedNodes = new LinkedHashSet<>();
        Set<String> selectedConnectors = new LinkedHashSet<>();
        for (DiagramElementId selectedId : viewModel.selectedElementIds()) {
            if (project.model().entityById(selectedId).isPresent()) {
                selectedNodes.add(selectedId.value());
            } else if (project.model().relationshipById(selectedId).isPresent()) {
                selectedConnectors.add(selectedId.value());
            }
        }
        ConnectorBendPointSelection bendPoint = viewModel.selectedBendPoint();
        SelectedBendPoint selectedBendPoint = bendPoint == null
                ? null
                : new SelectedBendPoint(bendPoint.connectorId().value(), bendPoint.bendPointIndex());
        return new InteractiveCanvasSelection(selectedNodes, selectedConnectors, selectedBendPoint);
    }

    @Override
    public void selectNode(String elementId, boolean additive) {
        selectElement(elementId, additive);
    }

    @Override
    public void selectConnector(String connectorId, boolean additive) {
        selectElement(connectorId, additive);
    }

    @Override
    public void selectNodesInside(CanvasBounds selectionBounds, boolean additive) {
        Objects.requireNonNull(selectionBounds, "selectionBounds");
        DiagramProject project = viewModel.currentProject();
        if (project == null) {
            viewModel.clearSelection();
            return;
        }
        Set<DiagramElementId> selected = new LinkedHashSet<>();
        DiagramLayout layout = activeLayout(project);
        for (EntityElement entity : project.model().entities()) {
            layout.nodeFor(entity.id())
                    .filter(nodeLayout -> CanvasBounds.from(nodeLayout).intersects(selectionBounds))
                    .ifPresent(ignored -> selected.add(entity.id()));
        }
        if (selected.isEmpty() && !additive) {
            viewModel.clearSelection();
        } else if (!selected.isEmpty()) {
            viewModel.selectElements(selected, additive);
        }
    }

    @Override
    public void clearSelection() {
        viewModel.clearSelection();
    }

    @Override
    public void moveNode(String elementId, double x, double y) {
        DiagramProject project = viewModel.currentProject();
        if (project == null || elementId == null || elementId.isBlank()) {
            return;
        }
        DiagramElementId id = DiagramElementId.of(elementId);
        Optional<NodeLayout> layout = activeLayout(project).nodeFor(id);
        if (layout.isEmpty()) {
            return;
        }
        viewModel.selectElement(id);
        viewModel.moveSelectedElementBy(x - layout.get().x(), y - layout.get().y());
    }

    @Override
    public void moveSelectedNodesBy(double deltaX, double deltaY) {
        viewModel.moveSelectedElementBy(deltaX, deltaY);
    }

    @Override
    public void addBendPoint(String connectorId, double x, double y) {
        if (connectorId == null || connectorId.isBlank()) {
            return;
        }
        viewModel.addBendPoint(DiagramElementId.of(connectorId), x, y);
    }

    @Override
    public void moveBendPoint(String connectorId, int index, double x, double y) {
        DiagramProject project = viewModel.currentProject();
        if (project == null || connectorId == null || connectorId.isBlank()) {
            return;
        }
        DiagramElementId id = DiagramElementId.of(connectorId);
        Optional<ConnectorLayout> layout = activeLayout(project).connectorById(id);
        if (layout.isEmpty() || index < 0 || index >= layout.get().bendPoints().size()) {
            return;
        }
        BendPoint current = layout.get().bendPoints().get(index);
        viewModel.moveBendPointBy(id, index, x - current.x(), y - current.y());
    }

    @Override
    public void selectBendPoint(String connectorId, int index) {
        if (connectorId == null || connectorId.isBlank()) {
            return;
        }
        viewModel.selectBendPoint(DiagramElementId.of(connectorId), index);
    }

    @Override
    public void removeSelectedBendPoint() {
        viewModel.removeSelectedBendPoint();
    }

    @Override
    public void markDirty() {
        // Las operaciones del ViewModel ya pasan por historial/dirty state del shell.
    }

    private void selectElement(String rawId, boolean additive) {
        if (rawId == null || rawId.isBlank()) {
            return;
        }
        DiagramElementId id = DiagramElementId.of(rawId);
        if (additive) {
            viewModel.toggleElementSelection(id);
        } else {
            viewModel.selectElement(id);
        }
    }

    private DiagramLayout activeLayout(DiagramProject project) {
        return project.layouts()
                .layoutFor(viewModel.activeNotation())
                .orElseGet(project.layouts()::activeLayout);
    }

    private InteractiveCanvasNode toNode(EntityElement entity) {
        return new InteractiveCanvasNode(
                entity.id().value(),
                entity.name(),
                entity.module(),
                kindName(entity.kind()),
                true,
                false
        );
    }

    private InteractiveCanvasConnector toConnector(RelationshipElement relationship) {
        return new InteractiveCanvasConnector(
                relationship.id().value(),
                relationship.fromEntityId().value(),
                relationship.toEntityId().value(),
                relationship.name(),
                kindName(relationship.kind()),
                true
        );
    }

    private static String kindName(Enum<?> value) {
        return value == null ? "conceptual" : value.name().toLowerCase(Locale.ROOT).replace('_', '-');
    }

    @Override
    public boolean copySelectionToClipboard() {
        return ProjectVisualSelectionTransferService.copySelectionToClipboard(
                viewModel.currentProject(),
                diagramTypeId(),
                selection(),
                connectors(),
                this::layoutForNode,
                this::layoutForConnector);
    }

    @Override
    public boolean pasteSelectionFromClipboard() {
        return ProjectVisualSelectionTransferService.pasteSelectionFromClipboard(viewModel.currentProject())
                .map(result -> {
                    viewModel.applyVisualSelectionPaste(result.project(), result.message());
                    return true;
                })
                .orElse(false);
    }

}
