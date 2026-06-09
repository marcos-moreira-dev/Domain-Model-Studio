package com.marcosmoreira.domainmodelstudio.presentation.logicalbusinessgraph;

import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphEdge;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphNode;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphNodeKind;
import com.marcosmoreira.domainmodelstudio.domain.logicalbusinessgraph.LogicalBusinessGraphRelationKind;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;

/** Pestañas con buscador y acordeones para nodos y relaciones del grafo logico. */
final class LogicalBusinessGraphAccordionTabs {

    private final LogicalBusinessGraphViewModel viewModel;
    private final Runnable refreshCanvas;
    private final TextField nodeSearch = searchField("Buscar nodo por ID, prefijo, titulo o texto...");
    private final TextField edgeSearch = searchField("Buscar relacion por ID, tipo, origen, destino o texto...");
    private final VBox nodeSections = new VBox(6);
    private final VBox edgeSections = new VBox(6);
    private final Set<LogicalBusinessGraphNodeKind> expandedNodeKinds = new LinkedHashSet<>();
    private final Set<LogicalBusinessGraphRelationKind> expandedRelationKinds = new LinkedHashSet<>();

    LogicalBusinessGraphAccordionTabs(LogicalBusinessGraphViewModel viewModel, Runnable refreshCanvas) {
        this.viewModel = viewModel;
        this.refreshCanvas = refreshCanvas == null ? () -> { } : refreshCanvas;
    }

    Parent nodeTab() {
        VBox box = tabBox();
        box.getChildren().addAll(nodeSearch, nodeSections);
        return box;
    }

    Parent edgeTab() {
        VBox box = tabBox();
        box.getChildren().addAll(edgeSearch, edgeSections);
        return box;
    }

    void bind() {
        nodeSearch.textProperty().addListener((observable, previous, current) -> refreshNodeSections());
        edgeSearch.textProperty().addListener((observable, previous, current) -> refreshEdgeSections());
        viewModel.nodes().addListener((javafx.collections.ListChangeListener<LogicalBusinessGraphNode>) change -> refreshNodeSections());
        viewModel.edges().addListener((javafx.collections.ListChangeListener<LogicalBusinessGraphEdge>) change -> refreshEdgeSections());
        viewModel.selectedNodeProperty().addListener((observable, previous, current) -> refreshNodeSections());
        viewModel.selectedEdgeProperty().addListener((observable, previous, current) -> refreshEdgeSections());
    }

    void refresh() {
        refreshNodeSections();
        refreshEdgeSections();
    }

    private void refreshNodeSections() {
        nodeSections.getChildren().clear();
        String query = normalizedQuery(nodeSearch);
        boolean searchActive = !query.isBlank();
        boolean firstVisible = true;
        int visible = 0;
        for (LogicalBusinessGraphNodeKind kind : LogicalBusinessGraphNodeKind.values()) {
            List<LogicalBusinessGraphNode> nodes = viewModel.nodes().stream()
                    .filter(node -> node.kind() == kind)
                    .filter(node -> matchesNode(node, query))
                    .toList();
            if (!nodes.isEmpty()) {
                nodeSections.getChildren().add(nodeDisclosure(kind, nodes, firstVisible, searchActive));
                firstVisible = false;
                visible++;
            }
        }
        if (visible == 0) {
            nodeSections.getChildren().add(emptyLabel(searchActive
                    ? "Sin nodos coincidentes para \"" + nodeSearch.getText().strip() + "\"."
                    : "No hay nodos logicos registrados."));
        }
    }

    private void refreshEdgeSections() {
        edgeSections.getChildren().clear();
        String query = normalizedQuery(edgeSearch);
        boolean searchActive = !query.isBlank();
        boolean firstVisible = true;
        int visible = 0;
        for (LogicalBusinessGraphRelationKind kind : LogicalBusinessGraphRelationKind.values()) {
            List<LogicalBusinessGraphEdge> edges = viewModel.edges().stream()
                    .filter(edge -> edge.relationKind() == kind)
                    .filter(edge -> matchesEdge(edge, query))
                    .toList();
            if (!edges.isEmpty()) {
                edgeSections.getChildren().add(edgeDisclosure(kind, edges, firstVisible, searchActive));
                firstVisible = false;
                visible++;
            }
        }
        if (visible == 0) {
            edgeSections.getChildren().add(emptyLabel(searchActive
                    ? "Sin relaciones coincidentes para \"" + edgeSearch.getText().strip() + "\"."
                    : "No hay relaciones logicas registradas."));
        }
    }

    private TitledPane nodeDisclosure(LogicalBusinessGraphNodeKind kind, List<LogicalBusinessGraphNode> nodes,
                                      boolean firstVisible, boolean searchActive) {
        VBox body = LogicalBusinessGraphDisclosure.body();
        body.getChildren().add(compactText(kind.description()));
        nodes.stream().map(this::nodeButton).forEach(body.getChildren()::add);
        boolean expanded = searchActive || expandedNodeKinds.contains(kind) || selectedNodeKind(kind)
                || (expandedNodeKinds.isEmpty() && firstVisible);
        return LogicalBusinessGraphDisclosure.section(kind.displayName(), nodes.size() + " nodos",
                "Prefijo: " + kind.prefix(), body, expanded, open -> rememberNodeKind(kind, open),
                "logical-business-graph-node-family",
                "logical-business-graph-node-family-" + kind.prefix().toLowerCase(Locale.ROOT));
    }

    private TitledPane edgeDisclosure(LogicalBusinessGraphRelationKind kind, List<LogicalBusinessGraphEdge> edges,
                                      boolean firstVisible, boolean searchActive) {
        VBox body = LogicalBusinessGraphDisclosure.body();
        body.getChildren().add(compactText(kind.description()));
        edges.stream().map(this::edgeButton).forEach(body.getChildren()::add);
        boolean expanded = searchActive || expandedRelationKinds.contains(kind) || selectedEdgeKind(kind)
                || (expandedRelationKinds.isEmpty() && firstVisible);
        return LogicalBusinessGraphDisclosure.section(kind.code(), edges.size() + " relaciones",
                kind.description(), body, expanded, open -> rememberRelationKind(kind, open),
                "logical-business-graph-edge-family");
    }

    private Button nodeButton(LogicalBusinessGraphNode node) {
        Button button = navigationButton(node.code() + " - " + node.title(), "logical-business-graph-node-button");
        if (node.equals(viewModel.selectedNodeProperty().get())) {
            button.getStyleClass().add("selected");
        }
        button.setTooltip(new Tooltip(node.compactLabel()));
        button.setOnAction(event -> {
            viewModel.selectNodeByCode(node.code());
            refreshCanvas.run();
        });
        return button;
    }

    private Button edgeButton(LogicalBusinessGraphEdge edge) {
        Button button = navigationButton(edge.id() + " - " + viewModel.nodeLabel(edge.sourceCode()) + " -> "
                + viewModel.nodeLabel(edge.targetCode()), "logical-business-graph-edge-button");
        if (edge.equals(viewModel.selectedEdgeProperty().get())) {
            button.getStyleClass().add("selected");
        }
        button.setTooltip(new Tooltip(edge.id() + " - " + edge.relationKind().code()));
        button.setOnAction(event -> {
            viewModel.selectEdgeById(edge.id());
            refreshCanvas.run();
        });
        return button;
    }

    private Button navigationButton(String text, String styleClass) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setWrapText(true);
        button.getStyleClass().addAll("logical-business-graph-navigation-button", styleClass);
        return button;
    }

    private Label compactText(String text) {
        Label label = new Label(text);
        label.setWrapText(true);
        label.getStyleClass().add("logical-business-graph-list-item-detail");
        return label;
    }

    private boolean selectedNodeKind(LogicalBusinessGraphNodeKind kind) {
        LogicalBusinessGraphNode selected = viewModel.selectedNodeProperty().get();
        return selected != null && selected.kind() == kind;
    }

    private boolean selectedEdgeKind(LogicalBusinessGraphRelationKind kind) {
        LogicalBusinessGraphEdge selected = viewModel.selectedEdgeProperty().get();
        return selected != null && selected.relationKind() == kind;
    }

    private void rememberNodeKind(LogicalBusinessGraphNodeKind kind, boolean expanded) {
        if (expanded) {
            expandedNodeKinds.add(kind);
        } else {
            expandedNodeKinds.remove(kind);
        }
    }

    private void rememberRelationKind(LogicalBusinessGraphRelationKind kind, boolean expanded) {
        if (expanded) {
            expandedRelationKinds.add(kind);
        } else {
            expandedRelationKinds.remove(kind);
        }
    }

    private boolean matchesNode(LogicalBusinessGraphNode node, String query) {
        return query.isBlank() || contains(query, node.code(), node.kind().prefix(), node.kind().displayName(),
                node.kind().description(), node.title(), node.description(), node.status().displayName(),
                String.join(" ", node.sourceReferenceIds()));
    }

    private boolean matchesEdge(LogicalBusinessGraphEdge edge, String query) {
        return query.isBlank() || contains(query, edge.id(), edge.sourceCode(), edge.targetCode(),
                edge.relationKind().code(), edge.relationKind().description(), viewModel.nodeLabel(edge.sourceCode()),
                viewModel.nodeLabel(edge.targetCode()), edge.description());
    }

    private boolean contains(String query, String... values) {
        for (String value : values) {
            if (value != null && value.toLowerCase(Locale.ROOT).contains(query)) {
                return true;
            }
        }
        return false;
    }

    private static VBox tabBox() {
        VBox box = new VBox(8);
        box.setPadding(new Insets(6));
        box.getStyleClass().add("logical-business-graph-accordion-tab");
        return box;
    }

    private static Label emptyLabel(String text) {
        Label label = new Label(text);
        label.setWrapText(true);
        label.getStyleClass().add("logical-business-graph-empty");
        return label;
    }

    private static TextField searchField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.getStyleClass().add("logical-business-graph-search-field");
        return field;
    }

    private static String normalizedQuery(TextField field) {
        String text = field.getText();
        return text == null ? "" : text.strip().toLowerCase(Locale.ROOT);
    }
}
