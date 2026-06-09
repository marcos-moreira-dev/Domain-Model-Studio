package com.marcosmoreira.domainmodelstudio.presentation.dialogs;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.List;
import java.util.Optional;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/** Ayuda académica integrada de Domain Model Studio con navegación estilo CHM. */
public final class ManualDialog {

    private static final double WINDOW_WIDTH = 1060;
    private static final double WINDOW_HEIGHT = 700;

    private ManualDialog() {
    }

    public static void show() {
        show(null);
    }

    public static void showForDiagramType(DiagramTypeId diagramTypeId) {
        show(diagramTypeId);
    }

    private static void show(DiagramTypeId initialDiagramType) {
        List<ManualCategory> categories = ManualContent.categories();
        ManualSearchIndex searchIndex = new ManualSearchIndex(categories);
        Optional<String> initialTitle = ManualContent.sectionTitleForDiagramType(initialDiagramType);

        Stage stage = new Stage();
        stage.setTitle("Guía académica — Domain Model Studio");
        stage.setMinWidth(860);
        stage.setMinHeight(580);

        BorderPane root = new BorderPane();
        root.getStyleClass().add("manual-root");
        root.setTop(buildHeader());

        VBox content = new VBox(12);
        content.getStyleClass().add("manual-content");
        content.setPadding(new Insets(18, 22, 22, 22));

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.getStyleClass().add("manual-scroll");
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        TreeView<ManualNavigationNode> contentTree = new TreeView<>(buildNavigationTree(categories));
        configureContentTree(contentTree, content);

        TabPane navigationTabs = buildNavigationTabs(categories, searchIndex, contentTree, content);
        navigationTabs.setPrefWidth(330);
        navigationTabs.setMinWidth(280);
        navigationTabs.setMaxWidth(390);

        if (initialTitle.isPresent()) {
            selectTopicByTitle(contentTree, initialTitle.get());
        } else {
            selectFirstTopic(contentTree);
        }

        HBox center = new HBox();
        center.getChildren().addAll(navigationTabs, scrollPane);
        HBox.setHgrow(scrollPane, Priority.ALWAYS);
        root.setCenter(center);

        Button closeButton = new Button("Cerrar");
        closeButton.getStyleClass().add("manual-close-button");
        closeButton.setOnAction(event -> stage.close());

        Region spacer = new Region();
        HBox footer = new HBox(10, spacer, closeButton);
        footer.getStyleClass().add("manual-footer");
        footer.setAlignment(Pos.CENTER_RIGHT);
        footer.setPadding(new Insets(8, 12, 8, 12));
        HBox.setHgrow(spacer, Priority.ALWAYS);
        root.setBottom(footer);

        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        java.net.URL stylesheet = ManualDialog.class.getResource("/css/app-light.css");
        if (stylesheet != null) {
            scene.getStylesheets().add(stylesheet.toExternalForm());
        }
        stage.setScene(scene);
        stage.show();
        stage.toFront();
    }

    private static TabPane buildNavigationTabs(
            List<ManualCategory> categories,
            ManualSearchIndex searchIndex,
            TreeView<ManualNavigationNode> contentTree,
            VBox content
    ) {
        TabPane tabs = new TabPane();
        tabs.getStyleClass().add("manual-navigation-tabs");
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab contents = new Tab("Contenido", contentTree);
        contents.setGraphic(ManualIconFactory.closedBook());

        Tab search = new Tab("Buscar", buildSearchPane(searchIndex, content));
        search.setGraphic(ManualIconFactory.page());

        tabs.getTabs().addAll(contents, search);
        return tabs;
    }

    private static VBox buildSearchPane(ManualSearchIndex searchIndex, VBox content) {
        TextField query = new TextField();
        query.setPromptText("Buscar actor, include, multiplicidad, gateway...");
        query.getStyleClass().add("manual-search-field");

        Label hint = new Label("Busca por concepto, notación, caso especial o herramienta.");
        hint.getStyleClass().add("manual-search-hint");
        hint.setWrapText(true);

        ListView<ManualSearchResult> results = new ListView<>();
        results.getStyleClass().add("manual-search-results");
        results.setCellFactory(list -> searchResultCell());
        results.getItems().setAll(searchIndex.allAlphabetical());
        results.getSelectionModel().selectedItemProperty().addListener((observable, oldResult, selected) -> {
            if (selected != null) {
                renderSection(content, selected.section());
            }
        });
        query.textProperty().addListener((observable, oldText, newText) -> {
            results.getItems().setAll(searchIndex.search(newText));
            if (!results.getItems().isEmpty()) {
                results.getSelectionModel().select(0);
            } else {
                renderEmptySearch(content, newText);
            }
        });

        VBox box = new VBox(8, query, hint, results);
        box.getStyleClass().add("manual-search-pane");
        box.setPadding(new Insets(10));
        VBox.setVgrow(results, Priority.ALWAYS);
        return box;
    }

    private static ListCell<ManualSearchResult> searchResultCell() {
        return new ListCell<>() {
            @Override
            protected void updateItem(ManualSearchResult item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    return;
                }
                Label title = new Label(item.title());
                title.getStyleClass().add("manual-search-result-title");
                title.setWrapText(true);
                Label category = new Label(item.categoryTitle());
                category.getStyleClass().add("manual-search-result-category");
                category.setWrapText(true);
                VBox wrapper = new VBox(2, title, category);
                wrapper.getStyleClass().add("manual-search-result");
                setText(null);
                setGraphic(wrapper);
            }
        };
    }

    private static TreeItem<ManualNavigationNode> buildNavigationTree(List<ManualCategory> categories) {
        TreeItem<ManualNavigationNode> root = new TreeItem<>(ManualNavigationNode.root());
        root.setExpanded(true);
        for (ManualCategory category : categories) {
            TreeItem<ManualNavigationNode> categoryItem = new TreeItem<>(ManualNavigationNode.category(category));
            categoryItem.setExpanded(true);
            for (ManualSection section : category.sections()) {
                categoryItem.getChildren().add(new TreeItem<>(ManualNavigationNode.section(section)));
            }
            root.getChildren().add(categoryItem);
        }
        return root;
    }

    private static void configureContentTree(TreeView<ManualNavigationNode> topics, VBox content) {
        topics.getStyleClass().add("manual-topic-list");
        topics.setShowRoot(false);
        topics.setCellFactory(treeView -> new TreeCell<>() {
            @Override
            protected void updateItem(ManualNavigationNode item, boolean empty) {
                super.updateItem(item, empty);
                getStyleClass().removeAll("manual-tree-category", "manual-tree-topic");
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    return;
                }
                setText(item.title());
                if (item.category()) {
                    getStyleClass().add("manual-tree-category");
                    setGraphic(ManualIconFactory.closedBook());
                } else {
                    getStyleClass().add("manual-tree-topic");
                    setGraphic(ManualIconFactory.page());
                }
            }
        });
        topics.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, selected) -> {
            if (selected != null && selected.getValue() != null) {
                renderNode(content, selected.getValue());
            }
        });
    }

    private static void selectFirstTopic(TreeView<ManualNavigationNode> topics) {
        TreeItem<ManualNavigationNode> root = topics.getRoot();
        if (root == null || root.getChildren().isEmpty()) {
            return;
        }
        TreeItem<ManualNavigationNode> firstCategory = root.getChildren().get(0);
        if (!firstCategory.getChildren().isEmpty()) {
            topics.getSelectionModel().select(firstCategory.getChildren().get(0));
        } else {
            topics.getSelectionModel().select(firstCategory);
        }
    }

    private static void selectTopicByTitle(TreeView<ManualNavigationNode> topics, String title) {
        TreeItem<ManualNavigationNode> match = findTopicByTitle(topics.getRoot(), title);
        if (match != null) {
            topics.getSelectionModel().select(match);
        } else {
            selectFirstTopic(topics);
        }
    }

    private static TreeItem<ManualNavigationNode> findTopicByTitle(TreeItem<ManualNavigationNode> item, String title) {
        if (item == null || title == null) {
            return null;
        }
        if (item.getValue() != null && title.equals(item.getValue().title())) {
            return item;
        }
        for (TreeItem<ManualNavigationNode> child : item.getChildren()) {
            TreeItem<ManualNavigationNode> match = findTopicByTitle(child, title);
            if (match != null) {
                return match;
            }
        }
        return null;
    }

    private static VBox buildHeader() {
        Label eyebrow = new Label("REFERENCIA ACADÉMICA DE DIAGRAMAS");
        eyebrow.getStyleClass().add("manual-eyebrow");

        Label title = new Label("Guía académica de Domain Model Studio");
        title.getStyleClass().add("manual-title");

        Label subtitle = new Label("Teoría, notación, ejemplos, casos especiales y errores comunes para las herramientas de modelado del programa.");
        subtitle.getStyleClass().add("manual-subtitle");
        subtitle.setWrapText(true);

        VBox header = new VBox(3, eyebrow, title, subtitle);
        header.getStyleClass().add("manual-header");
        header.setPadding(new Insets(14, 18, 14, 18));
        return header;
    }

    private static void renderNode(VBox content, ManualNavigationNode node) {
        if (node.category()) {
            renderCategory(content, node.categoryModel());
        } else {
            renderSection(content, node.section());
        }
    }

    private static void renderCategory(VBox content, ManualCategory category) {
        content.getChildren().clear();

        Label title = new Label(category.title());
        title.getStyleClass().add("manual-section-title");
        title.setWrapText(true);

        Label summary = new Label(category.summary());
        summary.getStyleClass().add("manual-section-summary");
        summary.setWrapText(true);

        content.getChildren().addAll(title, summary);
        for (ManualSection section : category.sections()) {
            Label topic = new Label("• " + section.title() + " — " + section.summary());
            topic.getStyleClass().add("manual-bullet");
            topic.setWrapText(true);
            content.getChildren().add(topic);
        }
    }

    private static void renderSection(VBox content, ManualSection section) {
        content.getChildren().clear();

        Label title = new Label(section.title());
        title.getStyleClass().add("manual-section-title");
        title.setWrapText(true);

        Label summary = new Label(section.summary());
        summary.getStyleClass().add("manual-section-summary");
        summary.setWrapText(true);

        content.getChildren().addAll(title, summary);
        for (ManualBlock block : section.blocks()) {
            content.getChildren().add(renderBlock(block));
        }
    }

    private static void renderEmptySearch(VBox content, String query) {
        content.getChildren().clear();
        Label title = new Label("Sin resultados");
        title.getStyleClass().add("manual-section-title");
        Label summary = new Label("No se encontraron temas para: " + (query == null ? "" : query));
        summary.getStyleClass().add("manual-section-summary");
        summary.setWrapText(true);
        content.getChildren().addAll(title, summary);
    }

    private static VBox renderBlock(ManualBlock block) {
        Label title = new Label(block.title());
        title.getStyleClass().add("manual-block-title");
        title.setWrapText(true);

        VBox box = new VBox(7);
        box.getStyleClass().add("manual-block");
        box.getChildren().add(title);

        for (String line : block.lines()) {
            Label label = ManualTheoryLineRenderer.render(line);
            if (label != null) {
                box.getChildren().add(label);
            }
        }
        if (block.hasFigures()) {
            for (var figure : block.figures()) {
                box.getChildren().add(ManualFigureNodeFactory.create(figure));
            }
        }
        if (block.hasDiagram()) {
            VBox diagram = new VBox(2);
            diagram.getStyleClass().add("manual-mini-diagram");
            for (String line : block.diagramLines()) {
                Label label = new Label(line);
                label.getStyleClass().add("manual-mini-diagram-line");
                label.setWrapText(false);
                diagram.getChildren().add(label);
            }
            box.getChildren().add(diagram);
        }
        return box;
    }

    private record ManualNavigationNode(
            String title,
            boolean category,
            ManualCategory categoryModel,
            ManualSection section
    ) {

        static ManualNavigationNode root() {
            return new ManualNavigationNode("Guía académica", true, new ManualCategory("Guía académica", "", List.of()), null);
        }

        static ManualNavigationNode category(ManualCategory category) {
            return new ManualNavigationNode(category.title(), true, category, null);
        }

        static ManualNavigationNode section(ManualSection section) {
            return new ManualNavigationNode(section.title(), false, null, section);
        }
    }
}
