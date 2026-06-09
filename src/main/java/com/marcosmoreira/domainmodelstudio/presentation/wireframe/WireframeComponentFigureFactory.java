package com.marcosmoreira.domainmodelstudio.presentation.wireframe;

import com.marcosmoreira.domainmodelstudio.presentation.drawing.DiagramDrawingFacade;
import com.marcosmoreira.domainmodelstudio.presentation.drawing.DiagramShapeStyle;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.CanvasBounds;
import com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas.InteractiveCanvasNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * Figuras primitivas para wireframes administrativos.
 *
 * <p>Un wireframe no intenta verse como frontend real. Los botones,
 * campos y tablas son solo figuras: borde, etiqueta y una pista mínima del tipo visual.</p>
 *
 * <p>Guardarraíl de producto: botones, campos y tablas son solo figuras;
 * no son controles JavaFX reales ni un constructor tipo Scene Builder.</p>
 */
final class WireframeComponentFigureFactory {

    private static final double SCREEN_HEADER_HEIGHT = 26.0;
    private static final double PADDING = 10.0;

    Node render(
            InteractiveCanvasNode node,
            CanvasBounds bounds,
            boolean selected,
            DiagramDrawingFacade drawingFacade
    ) {
        DiagramDrawingFacade safeDrawingFacade = drawingFacade == null ? DiagramDrawingFacade.defaults() : drawingFacade;
        WireframeFigureKind figureKind = WireframeFigureKind.fromCanvasKind(node.kind());
        Group group = new Group();
        group.setLayoutX(bounds.x());
        group.setLayoutY(bounds.y());
        group.getStyleClass().add("wireframe-figure-group");
        group.getChildren().add(background(node, bounds, selected, safeDrawingFacade));
        if (figureKind == WireframeFigureKind.SCREEN) {
            addScreenChrome(group, node, bounds.width(), bounds.height());
        } else {
            addPrimitiveContent(group, node, figureKind, bounds.width(), bounds.height());
        }
        if (selected) {
            group.getChildren().add(safeDrawingFacade.selection().selectionHalo(0.0, 0.0, bounds.width(), bounds.height()));
        }
        group.setUserData(node.id());
        return group;
    }

    private Node background(
            InteractiveCanvasNode node,
            CanvasBounds bounds,
            boolean selected,
            DiagramDrawingFacade drawingFacade
    ) {
        DiagramShapeStyle style = DiagramShapeStyle
                .node("wireframe-figure-" + node.kind(), selected)
                .withStyleClass("wireframe-figure");
        return drawingFacade.primitives().rectangle(0.0, 0.0, bounds.width(), bounds.height(), style);
    }

    private void addScreenChrome(Group group, InteractiveCanvasNode node, double width, double height) {
        Rectangle header = outlineRectangle(0.0, 0.0, width, Math.min(SCREEN_HEADER_HEIGHT, height));
        header.getStyleClass().add("wireframe-figure-screen-header");
        group.getChildren().add(header);
        addTitle(group, label(node), width, PADDING, 17.0, 56);
        String subtitle = firstSubtitleLine(node.subtitle());
        if (!subtitle.isBlank() && height > 52.0) {
            addSubtitle(group, subtitle, width, PADDING, 42.0, 64);
        }
        addDashboardStub(group, width, height);
    }

    private void addDashboardStub(Group group, double width, double height) {
        if (height <= 70.0) {
            return;
        }
        double top = Math.max(38.0, SCREEN_HEADER_HEIGHT + 14.0);
        Rectangle body = outlineRectangle(PADDING, top, Math.max(12.0, width - PADDING * 2.0), Math.max(12.0, height - top - PADDING));
        body.getStyleClass().add("wireframe-figure-screen-body");
        group.getChildren().add(body);
    }

    private void addPrimitiveContent(Group group, InteractiveCanvasNode node, WireframeFigureKind kind, double width, double height) {
        addTitle(group, label(node), width, PADDING, 17.0, 46);
        String subtitle = firstSubtitleLine(node.subtitle());
        if (!subtitle.isBlank() && height >= 52.0) {
            addSubtitle(group, subtitle, width, PADDING, 36.0, 52);
        }
        double top = subtitle.isBlank() ? 30.0 : 48.0;
        if (height - top < 16.0) {
            return;
        }
        addKindHint(group, kind, top, width, height);
    }

    private void addKindHint(Group group, WireframeFigureKind kind, double top, double width, double height) {
        double left = PADDING;
        double right = Math.max(PADDING + 12.0, width - PADDING);
        double usable = Math.max(18.0, right - left);
        switch (kind) {
            case TABLE -> addTableHint(group, top, left, usable, height);
            case REPORT -> addReport(group, top, left, usable, height);
            case FORM, FIELD, FILTER, SEARCH, DETAIL -> addFieldHint(group, top, left, usable, height);
            case BUTTON -> addButtonHint(group, top, left, usable);
            case TABS, MENU, TOP_BAR -> addTabsHint(group, top, left, usable);
            case MODAL -> addModalChrome(group, top, left, usable, height);
            case SIDEBAR, SECTION, PANEL, CARD, SUMMARY, APPROVAL_PANEL -> addPanelHint(group, top, left, usable, height);
            case PAGINATION -> addPagination(group, top, left, usable);
            case ALERT, BADGE, EMPTY_STATE -> addMessageHint(group, top, left, usable);
            case CHART -> addChartHint(group, top, left, usable, height);
            case STEPPER -> addStepperHint(group, top, left, usable);
            case DOCUMENT_LIST -> addListHint(group, top, left, usable, height);
            case CALENDAR -> addCalendarHint(group, top, left, usable, height);
            default -> addPanelHint(group, top, left, usable, height);
        }
    }

    private void addTableHint(Group group, double top, double left, double usable, double height) {
        double rowHeight = 12.0;
        group.getChildren().add(guideLine(left, top, left + usable, top));
        group.getChildren().add(guideLine(left, top + rowHeight, left + usable, top + rowHeight));
        group.getChildren().add(guideLine(left + usable * 0.34, top, left + usable * 0.34, Math.min(height - PADDING, top + rowHeight * 4.0)));
        group.getChildren().add(guideLine(left + usable * 0.68, top, left + usable * 0.68, Math.min(height - PADDING, top + rowHeight * 4.0)));
        for (int row = 2; row <= 4; row++) {
            double y = top + row * rowHeight;
            if (y < height - PADDING) {
                group.getChildren().add(guideLine(left, y, left + usable, y));
            }
        }
    }

    private void addFieldHint(Group group, double top, double left, double usable, double height) {
        int rows = height > 88.0 ? 3 : 2;
        for (int row = 0; row < rows; row++) {
            double y = top + row * 16.0;
            if (y > height - 16.0) {
                break;
            }
            group.getChildren().add(guideLine(left, y, left + Math.min(54.0, usable * 0.32), y));
            group.getChildren().add(outlineRectangle(left + Math.min(64.0, usable * 0.38), y - 7.0,
                    Math.max(32.0, usable - Math.min(70.0, usable * 0.42)), 13.0));
        }
    }

    private void addButtonHint(Group group, double top, double left, double usable) {
        Rectangle button = outlineRectangle(left, top - 6.0, Math.min(usable, 118.0), 22.0);
        button.getStyleClass().add("wireframe-figure-button-hint");
        group.getChildren().add(button);
    }

    private void addTabsHint(Group group, double top, double left, double usable) {
        double tabWidth = Math.min(68.0, Math.max(36.0, usable / 3.0));
        for (int index = 0; index < 3; index++) {
            group.getChildren().add(outlineRectangle(left + index * (tabWidth + 6.0), top - 6.0, tabWidth, 18.0));
        }
    }

    private void addPanelHint(Group group, double top, double left, double usable, double height) {
        double panelHeight = Math.max(18.0, Math.min(54.0, height - top - PADDING));
        group.getChildren().add(outlineRectangle(left, top - 6.0, usable, panelHeight));
    }

    private void addModalChrome(Group group, double top, double left, double usable, double height) {
        double panelHeight = Math.max(24.0, Math.min(66.0, height - top - PADDING));
        Rectangle modal = outlineRectangle(left, top - 6.0, usable, panelHeight);
        modal.getStyleClass().add("wireframe-figure-modal-hint");
        group.getChildren().add(modal);
        if (panelHeight > 34.0) {
            group.getChildren().add(guideLine(left, top + 12.0, left + usable, top + 12.0));
        }
    }

    private void addPagination(Group group, double top, double left, double usable) {
        int pages = Math.max(3, Math.min(5, (int) Math.floor(usable / 18.0)));
        for (int index = 0; index < pages; index++) {
            group.getChildren().add(outlineRectangle(left + index * 18.0, top - 5.0, 12.0, 12.0));
        }
    }

    private void addReport(Group group, double top, double left, double usable, double height) {
        double titleWidth = Math.min(usable * 0.64, 140.0);
        group.getChildren().add(guideLine(left, top, left + titleWidth, top));
        double chartTop = top + 16.0;
        double base = Math.min(height - PADDING, chartTop + 36.0);
        for (int index = 0; index < 3; index++) {
            double barHeight = 10.0 + index * 7.0;
            group.getChildren().add(outlineRectangle(left + index * 20.0, base - barHeight, 12.0, barHeight));
        }
        double tableLeft = left + Math.min(90.0, usable * 0.44);
        if (tableLeft + 34.0 < left + usable) {
            group.getChildren().add(guideLine(tableLeft, chartTop, left + usable, chartTop));
            group.getChildren().add(guideLine(tableLeft, chartTop + 14.0, left + usable, chartTop + 14.0));
        }
    }

    private void addMessageHint(Group group, double top, double left, double usable) {
        Rectangle message = outlineRectangle(left, top - 6.0, Math.min(usable, 160.0), 22.0);
        message.getStyleClass().add("wireframe-figure-message-hint");
        group.getChildren().add(message);
    }

    private void addChartHint(Group group, double top, double left, double usable, double height) {
        double base = Math.min(height - PADDING, top + 44.0);
        for (int index = 0; index < 4; index++) {
            double barHeight = 10.0 + index * 6.0;
            group.getChildren().add(outlineRectangle(left + index * 18.0, base - barHeight, 10.0, barHeight));
        }
    }

    private void addStepperHint(Group group, double top, double left, double usable) {
        double gap = Math.min(42.0, Math.max(24.0, usable / 4.0));
        for (int index = 0; index < 4; index++) {
            double x = left + index * gap;
            group.getChildren().add(outlineRectangle(x, top - 5.0, 13.0, 13.0));
            if (index < 3) {
                group.getChildren().add(guideLine(x + 13.0, top + 1.5, x + gap, top + 1.5));
            }
        }
    }

    private void addListHint(Group group, double top, double left, double usable, double height) {
        for (int row = 0; row < 3; row++) {
            double y = top + row * 14.0;
            if (y > height - 12.0) {
                break;
            }
            group.getChildren().add(outlineRectangle(left, y - 7.0, 10.0, 10.0));
            group.getChildren().add(guideLine(left + 16.0, y - 2.0, left + usable, y - 2.0));
        }
    }

    private void addCalendarHint(Group group, double top, double left, double usable, double height) {
        double cellWidth = Math.min(32.0, Math.max(18.0, usable / 4.0));
        double cellHeight = 14.0;
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 4; col++) {
                double y = top + row * (cellHeight + 4.0);
                if (y > height - 14.0) {
                    continue;
                }
                group.getChildren().add(outlineRectangle(left + col * (cellWidth + 4.0), y - 5.0, cellWidth, cellHeight));
            }
        }
    }

    private void addTitle(Group group, String title, double width, double x, double y, int max) {
        Text text = new Text(clamp(title, max));
        text.getStyleClass().add("wireframe-figure-title");
        text.setWrappingWidth(Math.max(20.0, width - x - PADDING));
        text.relocate(x, y);
        group.getChildren().add(text);
    }

    private void addSubtitle(Group group, String subtitle, double width, double x, double y, int max) {
        Text text = new Text(clamp(subtitle, max));
        text.getStyleClass().add("wireframe-figure-subtitle");
        text.setWrappingWidth(Math.max(20.0, width - x - PADDING));
        text.relocate(x, y);
        group.getChildren().add(text);
    }

    private Line guideLine(double startX, double startY, double endX, double endY) {
        Line line = new Line(startX, startY, endX, endY);
        line.getStyleClass().add("wireframe-figure-line");
        return line;
    }

    private Rectangle outlineRectangle(double x, double y, double width, double height) {
        Rectangle rectangle = new Rectangle(x, y, Math.max(1.0, width), Math.max(1.0, height));
        rectangle.setFill(Color.TRANSPARENT);
        rectangle.getStyleClass().add("wireframe-figure-mini-rect");
        rectangle.setArcWidth(0.0);
        rectangle.setArcHeight(0.0);
        return rectangle;
    }

    private static String label(InteractiveCanvasNode node) {
        String title = node == null ? "" : node.title();
        if (title != null && !title.isBlank()) {
            return title;
        }
        return node == null ? "" : node.id();
    }

    private static String firstSubtitleLine(String subtitle) {
        if (subtitle == null || subtitle.isBlank()) {
            return "";
        }
        return subtitle.split("\\R", 2)[0];
    }

    private static String clamp(String value, int max) {
        String clean = value == null ? "" : value.strip();
        if (clean.length() <= max) {
            return clean;
        }
        return clean.substring(0, Math.max(0, max - 1)) + "…";
    }
}
