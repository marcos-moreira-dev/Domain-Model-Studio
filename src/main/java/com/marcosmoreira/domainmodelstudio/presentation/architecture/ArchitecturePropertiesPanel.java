package com.marcosmoreira.domainmodelstudio.presentation.architecture;

import com.marcosmoreira.domainmodelstudio.presentation.workbench.WorkbenchPanelSupport;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureEdge;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureEdgeKind;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureNode;
import com.marcosmoreira.domainmodelstudio.domain.architecture.ArchitectureNodeKind;
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
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/** Panel derecho de propiedades para C4 y despliegue técnico. */
final class ArchitecturePropertiesPanel {

    private final ArchitectureDiagramViewModel viewModel;
    private final Runnable refreshCanvas;
    private final ScrollPane root;
    private final TableView<ArchitectureEdge> edgeTable = new TableView<>();
    private final ComboBox<ArchitectureNodeKind> nodeKindCombo = new ComboBox<>();
    private final TextField nodeNameField = new TextField();
    private final TextField nodeTechnologyField = new TextField();
    private final TextField nodeOwnerField = new TextField();
    private final TextField nodeEnvironmentField = new TextField();
    private final TextField nodeOrderField = new TextField();
    private final TextArea nodeDescriptionArea = new TextArea();
    private final TextArea nodeNotesArea = new TextArea();
    private final ComboBox<ArchitectureNode> edgeSourceCombo = new ComboBox<>();
    private final ComboBox<ArchitectureNode> edgeTargetCombo = new ComboBox<>();
    private final ComboBox<ArchitectureEdgeKind> edgeKindCombo = new ComboBox<>();
    private final TextField edgeLabelField = new TextField();
    private final TextField edgeProtocolField = new TextField();
    private final TextArea edgeNotesArea = new TextArea();

    ArchitecturePropertiesPanel(ArchitectureDiagramViewModel viewModel, Runnable refreshCanvas) {
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
        content.getStyleClass().addAll("architecture-properties", "diagram-workbench-panel-content");
        content.setPadding(new Insets(10));
        content.getChildren().addAll(buildEdgePanel(), buildNodeForm(), buildEdgeForm());
        return content;
    }

    private void configureRoot() {
        WorkbenchPanelSupport.configurePropertiesScroll(root, "architecture-properties-scroll");
    }

    private Parent buildEdgePanel() {
        VBox panel = section("Relaciones");
        edgeTable.setItems(viewModel.edges());
        edgeTable.getStyleClass().add("architecture-edge-table");
        edgeTable.getColumns().add(column("Origen", edge -> viewModel.nodeLabel(edge.sourceNodeId())));
        edgeTable.getColumns().add(column("Tipo", edge -> edge.kind().displayName()));
        edgeTable.getColumns().add(column("Destino", edge -> viewModel.nodeLabel(edge.targetNodeId())));
        edgeTable.getColumns().add(column("Etiqueta", ArchitectureEdge::label));
        SideDockCollectionSizingPolicy.configureTableView(edgeTable);
        panel.getChildren().add(edgeTable);
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
        addRow(grid, row++, "Tecnología", nodeTechnologyField);
        addRow(grid, row++, "Responsable/área", nodeOwnerField);
        addRow(grid, row++, "Ambiente", nodeEnvironmentField);
        addRow(grid, row++, "Orden", nodeOrderField);
        addRow(grid, row++, "Descripción", nodeDescriptionArea);
        addRow(grid, row++, "Notas", nodeNotesArea);
        Button apply = button("Aplicar elemento", "Actualizar elemento seleccionado");
        apply.setOnAction(event -> {
            viewModel.applyNodeChanges(nodeKindCombo.getValue(), nodeNameField.getText(),
                    nodeTechnologyField.getText(), nodeOwnerField.getText(), nodeEnvironmentField.getText(),
                    nodeDescriptionArea.getText(), nodeNotesArea.getText(), parseOrder(nodeOrderField.getText()));
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
        addRow(grid, row++, "Protocolo/canal", edgeProtocolField);
        addRow(grid, row++, "Notas", edgeNotesArea);
        Button apply = button("Aplicar relación", "Actualizar relación seleccionada");
        apply.setOnAction(event -> {
            viewModel.applyEdgeChanges(edgeSourceCombo.getValue(), edgeTargetCombo.getValue(),
                    edgeKindCombo.getValue(), edgeLabelField.getText(), edgeProtocolField.getText(), edgeNotesArea.getText());
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
        viewModel.selectedNodeProperty().addListener((observable, previous, current) -> populateNodeForm(current));
        viewModel.selectedEdgeProperty().addListener((observable, previous, current) -> {
            syncEdgeSelection(current);
            populateEdgeForm(current);
        });
    }

    private void syncEdgeSelection(ArchitectureEdge current) {
        if (current == null) {
            edgeTable.getSelectionModel().clearSelection();
        } else if (edgeTable.getSelectionModel().getSelectedItem() != current) {
            edgeTable.getSelectionModel().select(current);
        }
    }

    private void populateNodeForm(ArchitectureNode node) {
        if (node == null) {
            nodeKindCombo.setValue(viewModel.currentKind().defaultNodeKind());
            nodeNameField.clear();
            nodeTechnologyField.clear();
            nodeOwnerField.clear();
            nodeEnvironmentField.clear();
            nodeOrderField.clear();
            nodeDescriptionArea.clear();
            nodeNotesArea.clear();
            return;
        }
        nodeKindCombo.setValue(node.kind());
        nodeNameField.setText(node.displayName());
        nodeTechnologyField.setText(node.technology());
        nodeOwnerField.setText(node.owner());
        nodeEnvironmentField.setText(node.environment());
        nodeOrderField.setText(String.valueOf(node.orderIndex()));
        nodeDescriptionArea.setText(node.description());
        nodeNotesArea.setText(node.notes());
    }

    private void populateEdgeForm(ArchitectureEdge edge) {
        if (edge == null) {
            edgeSourceCombo.setValue(null);
            edgeTargetCombo.setValue(null);
            edgeKindCombo.setValue(viewModel.currentKind().defaultEdgeKind());
            edgeLabelField.clear();
            edgeProtocolField.clear();
            edgeNotesArea.clear();
            return;
        }
        selectNode(edgeSourceCombo, edge.sourceNodeId());
        selectNode(edgeTargetCombo, edge.targetNodeId());
        edgeKindCombo.setValue(edge.kind());
        edgeLabelField.setText(edge.label());
        edgeProtocolField.setText(edge.protocol());
        edgeNotesArea.setText(edge.notes());
    }

    private void selectNode(ComboBox<ArchitectureNode> combo, String nodeId) {
        combo.getSelectionModel().clearSelection();
        for (ArchitectureNode node : viewModel.nodes()) {
            if (node.id().equals(nodeId)) {
                combo.setValue(node);
                return;
            }
        }
    }

    private int parseOrder(String raw) {
        try {
            return Integer.parseInt(raw == null ? "0" : raw.strip());
        } catch (NumberFormatException exception) {
            return 0;
        }
    }

    private ListCell<ArchitectureNode> nodeCell() {
        return new ListCell<>() {
            @Override
            protected void updateItem(ArchitectureNode item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.displayName());
            }
        };
    }

    private TableColumn<ArchitectureEdge, String> column(
            String title,
            java.util.function.Function<ArchitectureEdge, String> extractor
    ) {
        TableColumn<ArchitectureEdge, String> column = new TableColumn<>(title);
        column.setCellValueFactory(data -> new SimpleStringProperty(extractor.apply(data.getValue())));
        column.setPrefWidth(125);
        return column;
    }

    private VBox section(String title) {
        return WorkbenchPanelSupport.section("architecture-section", "architecture-section-title", title);
    }

    private GridPane grid() {
        return WorkbenchPanelSupport.grid();
    }

    private void addRow(GridPane grid, int row, String labelText, javafx.scene.Node input) {
        WorkbenchPanelSupport.addRow(grid, row, "architecture-form-label", labelText, input);
    }

    private Button button(String text, String tooltip) {
        return WorkbenchPanelSupport.button("architecture-button", text, tooltip);
    }

    private void configureArea(TextArea area, int rows) {
        WorkbenchPanelSupport.configureArea(area, rows);
    }
}
