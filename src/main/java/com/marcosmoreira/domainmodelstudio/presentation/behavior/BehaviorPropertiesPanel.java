package com.marcosmoreira.domainmodelstudio.presentation.behavior;

import com.marcosmoreira.domainmodelstudio.presentation.workbench.WorkbenchPanelSupport;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorEdge;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorEdgeKind;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNode;
import com.marcosmoreira.domainmodelstudio.domain.behavior.BehaviorNodeKind;
import com.marcosmoreira.domainmodelstudio.presentation.sidedock.SideDockCollectionSizingPolicy;
import java.util.Objects;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/** Panel derecho de propiedades para procesos, actividad, estados y secuencia. */
final class BehaviorPropertiesPanel {

    private final BehaviorDiagramViewModel viewModel;
    private final Runnable refreshCanvas;
    private final ScrollPane root;
    private final TableView<BehaviorEdge> edgeTable = new TableView<>();
    private final ComboBox<BehaviorNodeKind> nodeKindCombo = new ComboBox<>();
    private final TextField nodeNameField = new TextField();
    private final TextField nodeOwnerField = new TextField();
    private final TextField nodeOrderField = new TextField();
    private final TextArea nodeDescriptionArea = new TextArea();
    private final TextArea nodeNotesArea = new TextArea();
    private final ComboBox<BehaviorNode> edgeSourceCombo = new ComboBox<>();
    private final ComboBox<BehaviorNode> edgeTargetCombo = new ComboBox<>();
    private final ComboBox<BehaviorEdgeKind> edgeKindCombo = new ComboBox<>();
    private final TextField edgeLabelField = new TextField();
    private final TextField edgeConditionField = new TextField();
    private final TextArea edgeNotesArea = new TextArea();
    private final HBox sequenceOrderActions = new HBox(6);

    BehaviorPropertiesPanel(BehaviorDiagramViewModel viewModel, Runnable refreshCanvas) {
        this.viewModel = Objects.requireNonNull(viewModel, "viewModel");
        this.refreshCanvas = refreshCanvas == null ? () -> { } : refreshCanvas;
        VBox content = buildContent();
        this.root = new ScrollPane(content);
        configureRoot();
        bindViewModel();
    }

    Parent root() {
        return root;
    }

    private VBox buildContent() {
        VBox content = new VBox(12);
        content.getStyleClass().addAll("behavior-properties", "diagram-workbench-panel-content");
        content.setPadding(new Insets(10));
        content.getChildren().addAll(buildEdgePanel(), buildNodeForm(), buildEdgeForm());
        return content;
    }

    private void configureRoot() {
        WorkbenchPanelSupport.configurePropertiesScroll(root, "behavior-properties-scroll");
    }

    private Parent buildEdgePanel() {
        VBox panel = section("Relaciones");
        edgeTable.setItems(viewModel.edges());
        edgeTable.getStyleClass().add("behavior-edge-table");
        edgeTable.getColumns().add(column("Origen", edge -> viewModel.nodeLabel(edge.sourceNodeId())));
        edgeTable.getColumns().add(column("Tipo", edge -> edge.kind().displayName()));
        edgeTable.getColumns().add(column("Destino", edge -> viewModel.nodeLabel(edge.targetNodeId())));
        edgeTable.getColumns().add(column("Etiqueta", BehaviorEdge::label));
        SideDockCollectionSizingPolicy.configureTableView(edgeTable);
        Button up = button("Subir mensaje", "Mover mensaje una fila temporal arriba");
        Button down = button("Bajar mensaje", "Mover mensaje una fila temporal abajo");
        up.setOnAction(event -> {
            viewModel.moveSelectedSequenceMessageUp();
            refreshCanvas.run();
        });
        down.setOnAction(event -> {
            viewModel.moveSelectedSequenceMessageDown();
            refreshCanvas.run();
        });
        sequenceOrderActions.getChildren().addAll(up, down);
        sequenceOrderActions.getStyleClass().add("behavior-sequence-order-actions");
        panel.getChildren().addAll(edgeTable, sequenceOrderActions);
        refreshSequenceOrderVisibility();
        return panel;
    }

    private Parent buildNodeForm() {
        VBox box = section("Elemento seleccionado");
        nodeKindCombo.setItems(viewModel.availableNodeKinds());
        configureArea(nodeDescriptionArea, 3);
        configureArea(nodeNotesArea, 2);
        GridPane grid = grid();
        int row = 0;
        addRow(grid, row++, "Tipo", nodeKindCombo);
        addRow(grid, row++, "Nombre", nodeNameField);
        addRow(grid, row++, "Responsable/área", nodeOwnerField);
        addRow(grid, row++, "Orden", nodeOrderField);
        addRow(grid, row++, "Descripción", nodeDescriptionArea);
        addRow(grid, row++, "Notas", nodeNotesArea);
        Button apply = button("Aplicar elemento", "Actualizar elemento seleccionado");
        apply.setOnAction(event -> {
            viewModel.applyNodeChanges(
                    nodeKindCombo.getValue(),
                    nodeNameField.getText(),
                    nodeOwnerField.getText(),
                    nodeDescriptionArea.getText(),
                    nodeNotesArea.getText(),
                    parseOrder(nodeOrderField.getText()));
            refreshCanvas.run();
        });
        box.getChildren().addAll(grid, apply);
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
        edgeKindCombo.setItems(viewModel.availableEdgeKinds());
        configureArea(edgeNotesArea, 2);
        GridPane grid = grid();
        int row = 0;
        addRow(grid, row++, "Origen", edgeSourceCombo);
        addRow(grid, row++, "Destino", edgeTargetCombo);
        addRow(grid, row++, "Tipo", edgeKindCombo);
        addRow(grid, row++, "Etiqueta", edgeLabelField);
        addRow(grid, row++, "Condición", edgeConditionField);
        addRow(grid, row++, "Notas", edgeNotesArea);
        Button apply = button("Aplicar relación", "Actualizar relación seleccionada");
        apply.setOnAction(event -> {
            viewModel.applyEdgeChanges(
                    edgeSourceCombo.getValue(),
                    edgeTargetCombo.getValue(),
                    edgeKindCombo.getValue(),
                    edgeLabelField.getText(),
                    edgeConditionField.getText(),
                    edgeNotesArea.getText());
            refreshCanvas.run();
        });
        box.getChildren().addAll(grid, apply);
        return box;
    }

    private void bindViewModel() {
        edgeTable.getSelectionModel().selectedItemProperty().addListener((observable, previous, current) -> {
            if (current != null) {
                viewModel.selectEdgeById(current.id());
            }
            refreshCanvas.run();
        });
        viewModel.selectedNodeProperty().addListener((observable, previous, current) -> {
            populateNodeForm(current);
            refreshSequenceOrderVisibility();
        });
        viewModel.selectedEdgeProperty().addListener((observable, previous, current) -> {
            syncEdgeSelection(current);
            populateEdgeForm(current);
            refreshSequenceOrderVisibility();
        });
    }

    private void syncEdgeSelection(BehaviorEdge current) {
        if (current == null) {
            edgeTable.getSelectionModel().clearSelection();
        } else if (edgeTable.getSelectionModel().getSelectedItem() != current) {
            edgeTable.getSelectionModel().select(current);
        }
    }

    private void populateNodeForm(BehaviorNode node) {
        if (node == null) {
            nodeKindCombo.setValue(viewModel.currentKind().defaultNodeKind());
            nodeNameField.clear();
            nodeOwnerField.clear();
            nodeOrderField.clear();
            nodeDescriptionArea.clear();
            nodeNotesArea.clear();
            return;
        }
        nodeKindCombo.setValue(node.kind());
        nodeNameField.setText(node.displayName());
        nodeOwnerField.setText(node.owner());
        nodeOrderField.setText(String.valueOf(node.orderIndex()));
        nodeDescriptionArea.setText(node.description());
        nodeNotesArea.setText(node.notes());
    }

    private void populateEdgeForm(BehaviorEdge edge) {
        if (edge == null) {
            edgeSourceCombo.setValue(null);
            edgeTargetCombo.setValue(null);
            edgeKindCombo.setValue(viewModel.currentKind().defaultEdgeKind());
            edgeLabelField.clear();
            edgeConditionField.clear();
            edgeNotesArea.clear();
            return;
        }
        selectNode(edgeSourceCombo, edge.sourceNodeId());
        selectNode(edgeTargetCombo, edge.targetNodeId());
        edgeKindCombo.setValue(edge.kind());
        edgeLabelField.setText(edge.label());
        edgeConditionField.setText(edge.condition());
        edgeNotesArea.setText(edge.notes());
    }

    private void selectNode(ComboBox<BehaviorNode> combo, String nodeId) {
        combo.getSelectionModel().clearSelection();
        for (BehaviorNode node : viewModel.nodes()) {
            if (node.id().equals(nodeId)) {
                combo.setValue(node);
                return;
            }
        }
    }

    private void refreshSequenceOrderVisibility() {
        boolean visible = viewModel.sequenceDiagramActive();
        sequenceOrderActions.setVisible(visible);
        sequenceOrderActions.setManaged(visible);
    }

    private int parseOrder(String raw) {
        try {
            return Integer.parseInt(raw == null ? "0" : raw.strip());
        } catch (NumberFormatException exception) {
            return 0;
        }
    }

    private ListCell<BehaviorNode> nodeCell() {
        return new ListCell<>() {
            @Override
            protected void updateItem(BehaviorNode item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.displayName());
            }
        };
    }

    private TableColumn<BehaviorEdge, String> column(String title, java.util.function.Function<BehaviorEdge, String> extractor) {
        TableColumn<BehaviorEdge, String> column = new TableColumn<>(title);
        column.setCellValueFactory(data -> new SimpleStringProperty(extractor.apply(data.getValue())));
        column.setPrefWidth(120);
        return column;
    }

    private VBox section(String title) {
        return WorkbenchPanelSupport.section("behavior-section", "behavior-section-title", title);
    }

    private GridPane grid() {
        return WorkbenchPanelSupport.grid();
    }

    private void addRow(GridPane grid, int row, String labelText, javafx.scene.Node input) {
        WorkbenchPanelSupport.addRow(grid, row, "behavior-form-label", labelText, input);
    }

    private Button button(String text, String tooltip) {
        return WorkbenchPanelSupport.button("behavior-button", text, tooltip);
    }

    private void configureArea(TextArea area, int rows) {
        WorkbenchPanelSupport.configureArea(area, rows);
    }
}
