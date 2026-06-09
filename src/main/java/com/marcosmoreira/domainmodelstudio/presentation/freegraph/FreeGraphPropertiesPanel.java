package com.marcosmoreira.domainmodelstudio.presentation.freegraph;

import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphEdge;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphEdgeDirection;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphKind;
import com.marcosmoreira.domainmodelstudio.domain.freegraph.FreeGraphNode;
import com.marcosmoreira.domainmodelstudio.presentation.workbench.WorkbenchPanelSupport;
import java.util.Objects;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/** Panel derecho de propiedades del Grafo libre. */
final class FreeGraphPropertiesPanel {

    private final FreeGraphViewModel viewModel;
    private final Runnable refreshCanvas;
    private final ScrollPane root;
    private final TextField projectNameField = new TextField();
    private final TextField versionField = new TextField();
    private final DatePicker documentDatePicker = new DatePicker();
    private final ComboBox<FreeGraphKind> graphKindCombo = new ComboBox<>();
    private final TextArea notesArea = new TextArea();
    private final TextField nodeTitleField = new TextField();
    private final TextArea nodeContentArea = new TextArea();
    private final ComboBox<FreeGraphNode> edgeSourceCombo = new ComboBox<>();
    private final ComboBox<FreeGraphNode> edgeTargetCombo = new ComboBox<>();
    private final ComboBox<FreeGraphEdgeDirection> edgeDirectionCombo = new ComboBox<>();
    private final TextField edgeLabelField = new TextField();
    private final TextArea edgeNotesArea = new TextArea();

    FreeGraphPropertiesPanel(FreeGraphViewModel viewModel, Runnable refreshCanvas) {
        this.viewModel = Objects.requireNonNull(viewModel, "viewModel");
        this.refreshCanvas = refreshCanvas == null ? () -> { } : refreshCanvas;
        this.root = new ScrollPane(buildContent());
        configureRoot();
        bindViewModel();
    }

    Parent root() {
        return root;
    }

    private VBox buildContent() {
        VBox content = new VBox(12);
        content.setPadding(new Insets(10));
        content.getStyleClass().addAll("free-graph-properties", "diagram-workbench-panel-content");
        content.getChildren().addAll(buildDocumentForm(), buildNodeForm(), buildEdgeForm());
        return content;
    }

    private void configureRoot() {
        WorkbenchPanelSupport.configurePropertiesScroll(root, "free-graph-properties-scroll");
    }

    private Parent buildDocumentForm() {
        VBox box = section("Detalles del grafo");
        graphKindCombo.getItems().setAll(FreeGraphKind.values());
        configureEnumCombo(graphKindCombo);
        WorkbenchPanelSupport.configureArea(notesArea, 3);

        GridPane grid = WorkbenchPanelSupport.grid();
        int row = 0;
        addRow(grid, row++, "Nombre", projectNameField);
        addRow(grid, row++, "Versión", versionField);
        addRow(grid, row++, "Fecha", documentDatePicker);
        addRow(grid, row++, "Tipo", graphKindCombo);
        addRow(grid, row++, "Notas", notesArea);

        Button apply = button("Aplicar detalles", "Actualizar metadatos del Grafo libre");
        apply.setOnAction(event -> {
            viewModel.applyDocumentDetails(
                    projectNameField.getText(),
                    versionField.getText(),
                    documentDatePicker.getValue(),
                    graphKindCombo.getValue(),
                    notesArea.getText());
            refreshCanvas.run();
        });
        box.getChildren().addAll(grid, apply);
        return box;
    }

    private Parent buildNodeForm() {
        VBox box = section("Nodo seleccionado");
        WorkbenchPanelSupport.configureArea(nodeContentArea, 4);
        GridPane grid = WorkbenchPanelSupport.grid();
        int row = 0;
        addRow(grid, row++, "Título", nodeTitleField);
        addRow(grid, row++, "Contenido", nodeContentArea);
        HBox actions = new HBox(8);
        Button apply = button("Aplicar nodo", "Actualizar título y contenido del nodo seleccionado");
        apply.setOnAction(event -> {
            viewModel.applyNodeChanges(nodeTitleField.getText(), nodeContentArea.getText());
            refreshCanvas.run();
        });
        actions.getChildren().add(apply);
        box.getChildren().addAll(grid, actions);
        return box;
    }

    private Parent buildEdgeForm() {
        VBox box = section("Relación seleccionada");
        edgeSourceCombo.setItems(viewModel.nodes());
        edgeTargetCombo.setItems(viewModel.nodes());
        edgeSourceCombo.setCellFactory(ignored -> nodeCell());
        edgeSourceCombo.setButtonCell(nodeCell());
        edgeTargetCombo.setCellFactory(ignored -> nodeCell());
        edgeTargetCombo.setButtonCell(nodeCell());
        edgeDirectionCombo.getItems().setAll(FreeGraphEdgeDirection.values());
        configureEnumCombo(edgeDirectionCombo);
        WorkbenchPanelSupport.configureArea(edgeNotesArea, 3);

        GridPane grid = WorkbenchPanelSupport.grid();
        int row = 0;
        addRow(grid, row++, "Origen", edgeSourceCombo);
        addRow(grid, row++, "Destino", edgeTargetCombo);
        addRow(grid, row++, "Dirección", edgeDirectionCombo);
        addRow(grid, row++, "Etiqueta", edgeLabelField);
        addRow(grid, row++, "Notas", edgeNotesArea);

        Button apply = button("Aplicar relación", "Actualizar la relación seleccionada");
        apply.setOnAction(event -> {
            viewModel.applyEdgeChanges(
                    edgeSourceCombo.getValue(),
                    edgeTargetCombo.getValue(),
                    edgeDirectionCombo.getValue(),
                    edgeLabelField.getText(),
                    edgeNotesArea.getText());
            refreshCanvas.run();
        });
        box.getChildren().addAll(grid, apply);
        return box;
    }

    private void bindViewModel() {
        viewModel.selectedNodeProperty().addListener((observable, previous, current) -> populateNodeForm(current));
        viewModel.selectedEdgeProperty().addListener((observable, previous, current) -> populateEdgeForm(current));
        viewModel.nodes().addListener((javafx.collections.ListChangeListener<FreeGraphNode>) change -> {
            edgeSourceCombo.setItems(viewModel.nodes());
            edgeTargetCombo.setItems(viewModel.nodes());
            populateDocumentForm();
        });
        populateDocumentForm();
        populateNodeForm(viewModel.selectedNodeProperty().get());
        populateEdgeForm(viewModel.selectedEdgeProperty().get());
    }

    private void populateDocumentForm() {
        if (viewModel.currentDocument() == null) {
            return;
        }
        projectNameField.setText(viewModel.currentDocument().projectName());
        versionField.setText(viewModel.currentDocument().version());
        documentDatePicker.setValue(viewModel.currentDocument().documentDate());
        graphKindCombo.setValue(viewModel.currentDocument().graphKind());
        notesArea.setText(viewModel.currentDocument().notes());
    }

    private void populateNodeForm(FreeGraphNode node) {
        boolean enabled = node != null;
        nodeTitleField.setDisable(!enabled);
        nodeContentArea.setDisable(!enabled);
        if (!enabled) {
            nodeTitleField.clear();
            nodeContentArea.clear();
            return;
        }
        nodeTitleField.setText(node.title());
        nodeContentArea.setText(node.content());
    }

    private void populateEdgeForm(FreeGraphEdge edge) {
        boolean enabled = edge != null;
        edgeSourceCombo.setDisable(!enabled);
        edgeTargetCombo.setDisable(!enabled);
        edgeDirectionCombo.setDisable(!enabled);
        edgeLabelField.setDisable(!enabled);
        edgeNotesArea.setDisable(!enabled);
        if (!enabled) {
            edgeSourceCombo.setValue(null);
            edgeTargetCombo.setValue(null);
            edgeDirectionCombo.setValue(null);
            edgeLabelField.clear();
            edgeNotesArea.clear();
            return;
        }
        edgeSourceCombo.setValue(nodeById(edge.sourceNodeId()));
        edgeTargetCombo.setValue(nodeById(edge.targetNodeId()));
        edgeDirectionCombo.setValue(edge.direction());
        edgeLabelField.setText(edge.label());
        edgeNotesArea.setText(edge.notes());
    }

    private FreeGraphNode nodeById(String nodeId) {
        return viewModel.nodes().stream()
                .filter(node -> node.id().equals(nodeId))
                .findFirst()
                .orElse(null);
    }

    private VBox section(String title) {
        return WorkbenchPanelSupport.section("free-graph-property-section", "free-graph-section-title", title);
    }

    private void addRow(GridPane grid, int row, String label, javafx.scene.Node input) {
        WorkbenchPanelSupport.addRow(grid, row, "free-graph-field-label", label, input);
    }

    private Button button(String text, String tooltip) {
        return WorkbenchPanelSupport.button("free-graph-action-button", text, tooltip);
    }

    private static ListCell<FreeGraphNode> nodeCell() {
        return new ListCell<>() {
            @Override
            protected void updateItem(FreeGraphNode item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setTooltip(null);
                    return;
                }
                setText(item.title());
                setTooltip(new Tooltip(item.id()));
            }
        };
    }

    private static <E extends Enum<E>> void configureEnumCombo(ComboBox<E> comboBox) {
        comboBox.setCellFactory(ignored -> enumCell());
        comboBox.setButtonCell(enumCell());
    }

    private static <E extends Enum<E>> ListCell<E> enumCell() {
        return new ListCell<>() {
            @Override
            protected void updateItem(E item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    return;
                }
                if (item instanceof FreeGraphKind kind) {
                    setText(kind.displayName());
                    return;
                }
                if (item instanceof FreeGraphEdgeDirection direction) {
                    setText(direction.displayName());
                    return;
                }
                setText(item.name());
            }
        };
    }
}
