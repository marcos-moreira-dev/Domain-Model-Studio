package com.marcosmoreira.domainmodelstudio.presentation.sidebar;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.notation.NotationType;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import java.util.function.Consumer;

/** Panel izquierdo con árbol del modelo y vistas futuras. */
public final class ModelTreeView {

    private final ModelTreeViewModel viewModel;
    private final Runnable hidePanelAction;
    private final Consumer<NotationType> notationSwitchAction;
    private final boolean showHeader;
    private final BorderPane root = new BorderPane();
    private TreeView<ModelTreeNode> tree;
    private boolean updatingFromSelectionModel;

    public ModelTreeView(ModelTreeViewModel viewModel) {
        this(viewModel, null, null);
    }

    public ModelTreeView(ModelTreeViewModel viewModel, Runnable hidePanelAction) {
        this(viewModel, hidePanelAction, null);
    }

    public ModelTreeView(
            ModelTreeViewModel viewModel,
            Runnable hidePanelAction,
            Consumer<NotationType> notationSwitchAction
    ) {
        this(viewModel, hidePanelAction, notationSwitchAction, true);
    }

    public ModelTreeView(
            ModelTreeViewModel viewModel,
            Runnable hidePanelAction,
            Consumer<NotationType> notationSwitchAction,
            boolean showHeader
    ) {
        this.viewModel = viewModel;
        this.hidePanelAction = hidePanelAction;
        this.notationSwitchAction = notationSwitchAction == null ? (ignored -> { }) : notationSwitchAction;
        this.showHeader = showHeader;
        build();
        bindSelection();
    }

    public Parent getRoot() {
        return root;
    }

    private void build() {
        root.getStyleClass().add("side-panel");
        if (!showHeader) {
            root.getStyleClass().add("side-dock-content-owns-scroll");
        }
        root.setMinWidth(240);
        root.setPrefWidth(270);

        BorderPane header = new BorderPane();
        header.getStyleClass().add("panel-header");
        BorderPane.setMargin(header, new Insets(0, 0, 4, 0));

        Label title = new Label();
        title.textProperty().bind(viewModel.titleProperty());
        title.getStyleClass().add("panel-header-title");
        header.setLeft(title);

        Button hideButton = new Button("◀");
        hideButton.getStyleClass().addAll("panel-header-button", "panel-header-icon-button");
        hideButton.setMnemonicParsing(false);
        hideButton.setTooltip(new Tooltip("Ocultar estructura del modelo"));
        hideButton.setOnAction(event -> {
            if (hidePanelAction != null) {
                hidePanelAction.run();
            }
        });
        header.setRight(hideButton);

        tree = new TreeView<>(viewModel.rootItem());
        tree.getStyleClass().add("model-tree");
        tree.setShowRoot(true);
        tree.setFixedCellSize(24.0);
        tree.getSelectionModel().selectedItemProperty().addListener((observable, oldItem, newItem) -> {
            if (updatingFromSelectionModel || newItem == null) {
                return;
            }
            ModelTreeNode node = newItem.getValue();
            viewModel.notationFromTree(node).ifPresentOrElse(
                    notationSwitchAction,
                    () -> viewModel.selectFromTree(node)
            );
        });

        if (showHeader) {
            root.setTop(header);
        }
        root.setCenter(tree);
    }

    private void bindSelection() {
        viewModel.selectionModel().selectedElementIdProperty().addListener((observable, previous, current) -> {
            if (current == null) {
                tree.getSelectionModel().clearSelection();
                return;
            }
            selectTreeItem(current);
        });
    }

    private void selectTreeItem(DiagramElementId elementId) {
        TreeItem<ModelTreeNode> item = viewModel.findItemByElementId(elementId);
        if (item == null) {
            return;
        }
        updatingFromSelectionModel = true;
        try {
            expandParents(item);
            tree.getSelectionModel().select(item);
            tree.scrollTo(tree.getRow(item));
        } finally {
            updatingFromSelectionModel = false;
        }
    }

    private void expandParents(TreeItem<ModelTreeNode> item) {
        TreeItem<ModelTreeNode> current = item;
        while (current != null) {
            current.setExpanded(true);
            current = current.getParent();
        }
    }
}
