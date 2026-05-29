package com.marcosmoreira.domainmodelstudio.presentation.logicalbusinessgraph;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphEdge;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphNode;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphNodeKind;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphNodeStatus;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphRelationKind;
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
import javafx.scene.layout.VBox;

/** Inspector de propiedades del Grafo lógico del negocio. */
final class LogicalBusinessGraphPropertiesPanel {

    private final LogicalBusinessGraphViewModel viewModel;
    private final Runnable refreshCanvas;
    private final ScrollPane root;
    private final TextField projectNameField = new TextField();
    private final TextField versionField = new TextField();
    private final DatePicker documentDatePicker = new DatePicker();
    private final TextArea notesArea = new TextArea();
    private final TextField nodeCodeField = new TextField();
    private final TextField nodeKindField = new TextField();
    private final TextField nodeTitleField = new TextField();
    private final TextArea nodeDescriptionArea = new TextArea();
    private final ComboBox<LogicalBusinessGraphNodeStatus> nodeStatusCombo = new ComboBox<>();
    private final TextField nodeReferencesField = new TextField();
    private final ComboBox<LogicalBusinessGraphNode> edgeSourceCombo = new ComboBox<>();
    private final ComboBox<LogicalBusinessGraphNode> edgeTargetCombo = new ComboBox<>();
    private final ComboBox<LogicalBusinessGraphRelationKind> edgeRelationCombo = new ComboBox<>();
    private final TextArea edgeDescriptionArea = new TextArea();
    private final Label semanticHint = new Label();

    LogicalBusinessGraphPropertiesPanel(LogicalBusinessGraphViewModel viewModel, Runnable refreshCanvas) {
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
        content.getStyleClass().addAll("logical-business-graph-properties", "diagram-workbench-panel-content");
        content.getChildren().addAll(buildDocumentForm(), buildNodeForm(), buildEdgeForm());
        return content;
    }

    private void configureRoot() {
        WorkbenchPanelSupport.configurePropertiesScroll(root, "logical-business-graph-properties-scroll");
    }

    private Parent buildDocumentForm() {
        VBox box = section("Documento lógico");
        WorkbenchPanelSupport.configureArea(notesArea, 3);
        GridPane grid = WorkbenchPanelSupport.grid();
        int row = 0;
        addRow(grid, row++, "Nombre", projectNameField);
        addRow(grid, row++, "Versión", versionField);
        addRow(grid, row++, "Fecha", documentDatePicker);
        addRow(grid, row++, "Observaciones", notesArea);
        Button apply = button("Aplicar documento", "Actualizar portada y observaciones del grafo lógico");
        apply.setOnAction(event -> {
            viewModel.applyDocumentDetails(projectNameField.getText(), versionField.getText(), documentDatePicker.getValue(), notesArea.getText());
            refreshCanvas.run();
        });
        box.getChildren().addAll(grid, apply);
        return box;
    }

    private Parent buildNodeForm() {
        VBox box = section("Nodo seleccionado");
        nodeCodeField.setEditable(false);
        nodeKindField.setEditable(false);
        nodeKindField.setTooltip(new Tooltip("El tipo se deriva del código para mantener trazabilidad lógica."));
        nodeStatusCombo.getItems().setAll(LogicalBusinessGraphNodeStatus.values());
        nodeStatusCombo.setCellFactory(ignored -> statusCell());
        nodeStatusCombo.setButtonCell(statusCell());
        WorkbenchPanelSupport.configureArea(nodeDescriptionArea, 4);
        GridPane grid = WorkbenchPanelSupport.grid();
        int row = 0;
        addRow(grid, row++, "Código", nodeCodeField);
        addRow(grid, row++, "Tipo", nodeKindField);
        addRow(grid, row++, "Título", nodeTitleField);
        addRow(grid, row++, "Descripción", nodeDescriptionArea);
        addRow(grid, row++, "Estado", nodeStatusCombo);
        addRow(grid, row++, "Referencias", nodeReferencesField);
        Button apply = button("Aplicar nodo", "Actualizar título, descripción, estado y referencias del nodo seleccionado");
        apply.setOnAction(event -> {
            viewModel.applyNodeChanges(nodeTitleField.getText(), nodeDescriptionArea.getText(), nodeStatusCombo.getValue(), nodeReferencesField.getText());
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
        edgeRelationCombo.getItems().setAll(LogicalBusinessGraphRelationKind.values());
        edgeRelationCombo.setCellFactory(ignored -> relationCell());
        edgeRelationCombo.setButtonCell(relationCell());
        WorkbenchPanelSupport.configureArea(edgeDescriptionArea, 3);
        semanticHint.setWrapText(true);
        semanticHint.getStyleClass().add("logical-business-graph-semantic-hint");
        GridPane grid = WorkbenchPanelSupport.grid();
        int row = 0;
        addRow(grid, row++, "Origen", edgeSourceCombo);
        addRow(grid, row++, "Relación", edgeRelationCombo);
        addRow(grid, row++, "Destino", edgeTargetCombo);
        addRow(grid, row++, "Descripción", edgeDescriptionArea);
        Button apply = button("Aplicar relación", "Actualizar extremos, tipo semántico y descripción de la relación seleccionada");
        apply.setOnAction(event -> {
            viewModel.applyEdgeChanges(edgeSourceCombo.getValue(), edgeRelationCombo.getValue(), edgeTargetCombo.getValue(), edgeDescriptionArea.getText());
            refreshCanvas.run();
        });
        box.getChildren().addAll(grid, semanticHint, apply);
        return box;
    }

    private void bindViewModel() {
        viewModel.selectedNodeProperty().addListener((observable, previous, current) -> populateNodeForm(current));
        viewModel.selectedEdgeProperty().addListener((observable, previous, current) -> populateEdgeForm(current));
        viewModel.nodes().addListener((javafx.collections.ListChangeListener<LogicalBusinessGraphNode>) change -> {
            edgeSourceCombo.setItems(viewModel.nodes());
            edgeTargetCombo.setItems(viewModel.nodes());
            populateDocumentForm();
        });
        edgeSourceCombo.valueProperty().addListener((observable, previous, current) -> refreshSemanticHint());
        edgeTargetCombo.valueProperty().addListener((observable, previous, current) -> refreshSemanticHint());
        edgeRelationCombo.valueProperty().addListener((observable, previous, current) -> refreshSemanticHint());
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
        notesArea.setText(viewModel.currentDocument().notes());
    }

    private void populateNodeForm(LogicalBusinessGraphNode node) {
        boolean enabled = node != null;
        nodeCodeField.setDisable(!enabled);
        nodeKindField.setDisable(!enabled);
        nodeTitleField.setDisable(!enabled);
        nodeDescriptionArea.setDisable(!enabled);
        nodeStatusCombo.setDisable(!enabled);
        nodeReferencesField.setDisable(!enabled);
        if (!enabled) {
            nodeCodeField.clear();
            nodeKindField.clear();
            nodeTitleField.clear();
            nodeDescriptionArea.clear();
            nodeStatusCombo.setValue(null);
            nodeReferencesField.clear();
            return;
        }
        nodeCodeField.setText(node.code());
        nodeKindField.setText(node.kind().legendEntry());
        nodeTitleField.setText(node.title());
        nodeDescriptionArea.setText(node.description());
        nodeStatusCombo.setValue(node.status());
        nodeReferencesField.setText(String.join(", ", node.sourceReferenceIds()));
    }

    private void populateEdgeForm(LogicalBusinessGraphEdge edge) {
        boolean enabled = edge != null;
        edgeSourceCombo.setDisable(!enabled);
        edgeTargetCombo.setDisable(!enabled);
        edgeRelationCombo.setDisable(!enabled);
        edgeDescriptionArea.setDisable(!enabled);
        if (!enabled) {
            edgeSourceCombo.setValue(null);
            edgeTargetCombo.setValue(null);
            edgeRelationCombo.setValue(null);
            edgeDescriptionArea.clear();
            semanticHint.setText("Selecciona una relación para revisar si su conexión es semánticamente esperada.");
            return;
        }
        edgeSourceCombo.setValue(nodeByCode(edge.sourceCode()));
        edgeTargetCombo.setValue(nodeByCode(edge.targetCode()));
        edgeRelationCombo.setValue(edge.relationKind());
        edgeDescriptionArea.setText(edge.description());
        refreshSemanticHint();
    }

    private void refreshSemanticHint() {
        LogicalBusinessGraphNode source = edgeSourceCombo.getValue();
        LogicalBusinessGraphNode target = edgeTargetCombo.getValue();
        LogicalBusinessGraphRelationKind relation = edgeRelationCombo.getValue();
        if (source == null || target == null || relation == null) {
            semanticHint.setText("Selecciona origen, relación y destino para revisar la coherencia lógica.");
            return;
        }
        boolean expected = relation.canConnect(source.kind(), target.kind());
        semanticHint.setText(expected
                ? "Conexión esperada: " + source.kind().prefix() + " " + relation.code() + " " + target.kind().prefix() + "."
                : "Advertencia: " + relation.code() + " no es la relación semántica usual entre "
                + source.kind().prefix() + " y " + target.kind().prefix() + ".");
    }

    private LogicalBusinessGraphNode nodeByCode(String code) {
        return viewModel.nodes().stream()
                .filter(node -> node.code().equals(code))
                .findFirst()
                .orElse(null);
    }

    private VBox section(String title) {
        return WorkbenchPanelSupport.section("logical-business-graph-property-section", "logical-business-graph-section-title", title);
    }

    private void addRow(GridPane grid, int row, String label, javafx.scene.Node input) {
        WorkbenchPanelSupport.addRow(grid, row, "logical-business-graph-field-label", label, input);
    }

    private Button button(String text, String tooltip) {
        return WorkbenchPanelSupport.button("logical-business-graph-action-button", text, tooltip);
    }

    private static ListCell<LogicalBusinessGraphNode> nodeCell() {
        return new ListCell<>() {
            @Override
            protected void updateItem(LogicalBusinessGraphNode item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setTooltip(null);
                    return;
                }
                setText(item.code() + " — " + item.title());
                setTooltip(new Tooltip(item.kind().legendEntry()));
            }
        };
    }

    private static ListCell<LogicalBusinessGraphRelationKind> relationCell() {
        return new ListCell<>() {
            @Override
            protected void updateItem(LogicalBusinessGraphRelationKind item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setTooltip(null);
                    return;
                }
                setText(item.code());
                setTooltip(new Tooltip(item.description()));
            }
        };
    }

    private static ListCell<LogicalBusinessGraphNodeStatus> statusCell() {
        return new ListCell<>() {
            @Override
            protected void updateItem(LogicalBusinessGraphNodeStatus item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.displayName());
            }
        };
    }
}
