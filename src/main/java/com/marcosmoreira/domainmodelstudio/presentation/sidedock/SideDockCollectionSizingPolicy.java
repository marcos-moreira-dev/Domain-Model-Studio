package com.marcosmoreira.domainmodelstudio.presentation.sidedock;

import javafx.collections.ListChangeListener;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;

/**
 * Política visual transversal para colecciones mostradas dentro del SideDock.
 *
 * <p>No agrega filas ni modifica los datos del modelo. Solo reserva una altura mínima
 * equivalente a ocho registros para evitar listas y tablas comprimidas verticalmente.</p>
 */
public final class SideDockCollectionSizingPolicy {

    public static final int MIN_VISIBLE_ROWS = 8;
    private static final double LIST_ROW_HEIGHT = 26.0;
    private static final double LIST_EXPANDED_ROW_HEIGHT = 84.0;
    private static final double TABLE_ROW_HEIGHT = 26.0;
    private static final double TABLE_EXPANDED_ROW_HEIGHT = 34.0;
    private static final double TABLE_HEADER_HEIGHT = 30.0;
    private static final double EXTRA_PADDING = 6.0;

    private SideDockCollectionSizingPolicy() {
    }

    public static void configureListView(ListView<?> listView) {
        double minimumHeight = LIST_ROW_HEIGHT * MIN_VISIBLE_ROWS + EXTRA_PADDING;
        applyMinimumHeight(listView, minimumHeight);
        if (!listView.getStyleClass().contains("side-dock-min-visible-rows")) {
            listView.getStyleClass().add("side-dock-min-visible-rows");
        }
    }

    public static void configureTableView(TableView<?> tableView) {
        double minimumHeight = TABLE_HEADER_HEIGHT + TABLE_ROW_HEIGHT * MIN_VISIBLE_ROWS + EXTRA_PADDING;
        applyMinimumHeight(tableView, minimumHeight);
        if (!tableView.getStyleClass().contains("side-dock-min-visible-rows")) {
            tableView.getStyleClass().add("side-dock-min-visible-rows");
        }
    }

    /**
     * Configura una lista para módulos donde el scroll dominante debe ser el del SideDock.
     *
     * <p>La lista reserva altura para todos sus elementos actuales, de modo que su scrollbar
     * interno no compita con el scrollbar externo del workspace lateral. No agrega filas ni
     * cambia los datos; solo recalcula altura cuando cambia la colección.</p>
     */
    public static void configureListViewForExternalSideDockScroll(ListView<?> listView) {
        markExpandedContent(listView, "side-dock-expanded-content-rows");
        updateExpandedListHeight(listView);
        listView.getItems().addListener((ListChangeListener<Object>) change -> updateExpandedListHeight(listView));
    }

    /** Igual que {@link #configureListViewForExternalSideDockScroll(ListView)}, pero para tablas. */
    public static void configureTableViewForExternalSideDockScroll(TableView<?> tableView) {
        markExpandedContent(tableView, "side-dock-expanded-content-rows");
        updateExpandedTableHeight(tableView);
        tableView.getItems().addListener((ListChangeListener<Object>) change -> updateExpandedTableHeight(tableView));
    }

    private static void updateExpandedListHeight(ListView<?> listView) {
        int rows = listView.getItems() == null ? 0 : listView.getItems().size();
        int visibleRows = Math.max(MIN_VISIBLE_ROWS, rows);
        double height = LIST_EXPANDED_ROW_HEIGHT * visibleRows + EXTRA_PADDING;
        listView.setMinHeight(LIST_EXPANDED_ROW_HEIGHT * MIN_VISIBLE_ROWS + EXTRA_PADDING);
        listView.setPrefHeight(height);
    }

    private static void updateExpandedTableHeight(TableView<?> tableView) {
        int rows = tableView.getItems() == null ? 0 : tableView.getItems().size();
        int visibleRows = Math.max(MIN_VISIBLE_ROWS, rows);
        double height = TABLE_HEADER_HEIGHT + TABLE_EXPANDED_ROW_HEIGHT * visibleRows + EXTRA_PADDING;
        tableView.setMinHeight(TABLE_HEADER_HEIGHT + TABLE_EXPANDED_ROW_HEIGHT * MIN_VISIBLE_ROWS + EXTRA_PADDING);
        tableView.setPrefHeight(height);
    }

    private static void markExpandedContent(javafx.scene.control.Control control, String styleClass) {
        if (!control.getStyleClass().contains(styleClass)) {
            control.getStyleClass().add(styleClass);
        }
    }

    private static void applyMinimumHeight(javafx.scene.control.Control control, double minimumHeight) {
        control.setMinHeight(minimumHeight);
        if (control.getPrefHeight() < minimumHeight) {
            control.setPrefHeight(minimumHeight);
        }
    }
}
