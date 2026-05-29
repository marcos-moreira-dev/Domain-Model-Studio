package com.marcosmoreira.domainmodelstudio.presentation.shell.tabs;

import com.marcosmoreira.domainmodelstudio.presentation.shell.EditorTabViewState;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;

/**
 * Barra horizontal escrolleable para pestañas de proyectos abiertos.
 *
 * <p>La barra acepta muchas pestañas sin estirar el shell. El scroll vertical
 * del mouse también desplaza horizontalmente, igual que las toolbars largas.</p>
 */
public final class ScrollableEditorTabBarView {

    private final ObservableList<EditorTabViewState> tabs;
    private final ReadOnlyStringProperty activeTabId;
    private final Consumer<String> activateTab;
    private final Consumer<String> closeTab;
    private final BiConsumer<String, String> reorderTabAfter;
    private final HBox tabStrip = new HBox();
    private final ScrollPane root = new ScrollPane(tabStrip);

    public ScrollableEditorTabBarView(
            ObservableList<EditorTabViewState> tabs,
            ReadOnlyStringProperty activeTabId,
            Consumer<String> activateTab,
            Consumer<String> closeTab,
            BiConsumer<String, String> reorderTabAfter
    ) {
        this.tabs = Objects.requireNonNull(tabs, "tabs");
        this.activeTabId = Objects.requireNonNull(activeTabId, "activeTabId");
        this.activateTab = Objects.requireNonNull(activateTab, "activateTab");
        this.closeTab = Objects.requireNonNull(closeTab, "closeTab");
        this.reorderTabAfter = Objects.requireNonNull(reorderTabAfter, "reorderTabAfter");
        build();
    }

    public Parent getRoot() {
        return root;
    }

    public void refresh() {
        tabStrip.getChildren().clear();
        String activeId = activeTabId.get();
        for (EditorTabViewState tab : tabs) {
            tabStrip.getChildren().add(new EditorTabCellView(
                    tab,
                    tab.id().equals(activeId),
                    activateTab,
                    closeTab,
                    reorderTabAfter
            ).getRoot());
        }
        scrollActiveTabIntoView();
    }

    private void scrollActiveTabIntoView() {
        if (tabs.size() <= 1) {
            root.setHvalue(0.0);
            return;
        }
        String activeId = activeTabId.get();
        int activeIndex = 0;
        for (int index = 0; index < tabs.size(); index++) {
            if (tabs.get(index).id().equals(activeId)) {
                activeIndex = index;
                break;
            }
        }
        double targetHValue = Math.max(0.0, Math.min(1.0, activeIndex / (double) (tabs.size() - 1)));
        Platform.runLater(() -> root.setHvalue(targetHValue));
    }

    private void build() {
        root.getStyleClass().add("editor-tab-scroll");
        tabStrip.getStyleClass().add("editor-tab-bar");
        root.setMinHeight(42.0);
        root.setPrefHeight(42.0);
        root.setMaxHeight(42.0);
        tabStrip.setMinHeight(40.0);
        tabStrip.setPrefHeight(40.0);
        tabStrip.setMaxHeight(40.0);
        root.setFitToHeight(true);
        root.setFitToWidth(false);
        root.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        root.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        root.setPannable(false);
        root.addEventFilter(ScrollEvent.SCROLL, event -> {
            double delta = Math.abs(event.getDeltaX()) > Math.abs(event.getDeltaY())
                    ? event.getDeltaX()
                    : event.getDeltaY();
            if (Math.abs(delta) < 0.01) {
                return;
            }
            root.setHvalue(Math.max(0.0, Math.min(1.0, root.getHvalue() - delta / 900.0)));
            event.consume();
        });
        tabs.addListener((ListChangeListener<EditorTabViewState>) change -> refresh());
        activeTabId.addListener((observable, previous, current) -> refresh());
        refresh();
    }
}
