package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import com.marcosmoreira.domainmodelstudio.domain.style.ElementStyle;
import com.marcosmoreira.domainmodelstudio.domain.style.FillStyle;
import com.marcosmoreira.domainmodelstudio.domain.style.RgbaColor;
import com.marcosmoreira.domainmodelstudio.domain.style.StrokeStyle;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import javafx.application.Platform;
import javafx.util.Duration;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/** Panel lateral para editar apariencia por categorías visuales del canvas. */
public final class CanvasAppearancePanel {

    private static final RgbaColor DEFAULT_FILL = RgbaColor.fromHex("#EAF2FF");
    private static final RgbaColor DEFAULT_STROKE = RgbaColor.fromHex("#4F5D75");
    private static final RgbaColor DEFAULT_TEXT = RgbaColor.fromHex("#232323");
    private static final double DEFAULT_STROKE_WIDTH = 1.0;

    private final Supplier<InteractiveCanvasAdapter> adapterSupplier;
    private final Runnable refreshAction;
    private final VBox root = new VBox(10.0);
    private final Label status = new Label("Edita colores por categoría visual del diagrama.");
    private GridPane categoryGrid;
    private boolean rebuildScheduled;

    public CanvasAppearancePanel(Supplier<InteractiveCanvasAdapter> adapterSupplier, Runnable refreshAction) {
        this.adapterSupplier = Objects.requireNonNull(adapterSupplier, "adapterSupplier");
        this.refreshAction = refreshAction == null ? () -> { } : refreshAction;
        build();
    }

    public Parent root() {
        return root;
    }

    private void build() {
        root.setPadding(new Insets(12.0));
        root.getStyleClass().add("canvas-appearance-panel");

        Label title = new Label("Estilo por categoría");
        title.getStyleClass().add("side-dock-section-title");

        status.setWrapText(true);
        status.getStyleClass().addAll("side-dock-hint", "canvas-appearance-hint");

        Label hint = new Label("Cambia el color y grosor visual de todos los elementos de una misma categoría. Usa selector de color, valor hexadecimal y grosor de línea/borde, con una lógica equivalente al panel de Modelo conceptual.");
        hint.setWrapText(true);
        hint.getStyleClass().addAll("side-dock-hint", "canvas-appearance-hint");

        categoryGrid = new GridPane();
        categoryGrid.getStyleClass().add("inspector-grid");
        categoryGrid.setHgap(8);
        categoryGrid.setVgap(7);

        root.getChildren().addAll(title, hint, categoryGrid, status);
        root.sceneProperty().addListener((observable, previous, current) -> scheduleRebuild());
        root.visibleProperty().addListener((observable, previous, current) -> {
            if (Boolean.TRUE.equals(current)) {
                scheduleRebuild();
            }
        });
        root.parentProperty().addListener((observable, previous, current) -> scheduleRebuild());
        root.setOnMouseEntered(event -> scheduleRebuild());
        root.setOnMousePressed(event -> scheduleRebuild());
        scheduleRebuild();
    }

    private void scheduleRebuild() {
        if (rebuildScheduled) {
            return;
        }
        rebuildScheduled = true;
        Platform.runLater(() -> {
            rebuildScheduled = false;
            rebuildCategoryRows();
            Platform.runLater(this::rebuildCategoryRows);
        });
    }

    private void rebuildCategoryRows() {
        categoryGrid.getChildren().clear();
        InteractiveCanvasAdapter adapter = adapterSupplier.get();
        if (!(adapter instanceof CanvasStylePort stylePort)) {
            status.setText("Este diagrama no expone edición de apariencia.");
            return;
        }
        List<CategoryRow> categories = categoriesFor(adapter);
        if (categories.isEmpty()) {
            status.setText("No hay categorías visuales editables para este diagrama o aún no hay elementos cargados.");
            return;
        }
        int row = 0;
        for (CategoryRow category : categories) {
            addCategoryRow(categoryGrid, row++, category, currentCategoryColor(adapter, stylePort, category),
                    currentCategoryStrokeWidth(adapter, stylePort, category),
                    color -> applyCategoryColor(adapter, stylePort, category, color),
                    width -> applyCategoryStrokeWidth(adapter, stylePort, category, width));
        }
    }

    private void addCategoryRow(
            GridPane grid,
            int row,
            CategoryRow category,
            RgbaColor initialColor,
            double initialWidth,
            java.util.function.Consumer<RgbaColor> applyColor,
            java.util.function.DoubleConsumer applyWidth
    ) {
        Label label = new Label(category.label() + ":");
        label.getStyleClass().add("inspector-key");

        TextField colorField = new TextField(initialColor.toHex());
        colorField.getStyleClass().addAll("inspector-field", "inspector-color-field");
        colorField.setTooltip(new Tooltip("Color hexadecimal para " + category.label()));

        ColorPicker picker = new ColorPicker(colorFromHex(initialColor.toHex()));
        picker.getStyleClass().add("inspector-color-picker");
        picker.setTooltip(new Tooltip("Seleccionar color para " + category.label()));

        TextField widthField = new TextField(String.format(java.util.Locale.ROOT, "%.1f", Math.max(1.0, initialWidth)));
        widthField.getStyleClass().addAll("inspector-field", "inspector-width-field");
        widthField.setPrefColumnCount(4);
        widthField.setTooltip(new Tooltip(category.connector()
                ? "Grosor de línea para " + category.label()
                : "Grosor de borde para " + category.label()));

        PauseTransition colorDebounce = new PauseTransition(Duration.millis(120));
        colorDebounce.setOnFinished(event -> applyFieldColor(colorField, picker, applyColor));
        picker.valueProperty().addListener((observable, previous, current) -> {
            colorField.setText(toHex(current));
            colorDebounce.playFromStart();
        });
        picker.setOnAction(event -> applyFieldColor(colorField, picker, applyColor));
        colorField.setOnAction(event -> applyFieldColor(colorField, picker, applyColor));
        colorField.focusedProperty().addListener((observable, wasFocused, isFocused) -> {
            if (wasFocused && !isFocused) {
                applyFieldColor(colorField, picker, applyColor);
            }
        });
        widthField.setOnAction(event -> applyFieldWidth(widthField, applyWidth));
        widthField.focusedProperty().addListener((observable, wasFocused, isFocused) -> {
            if (wasFocused && !isFocused) {
                applyFieldWidth(widthField, applyWidth);
            }
        });

        HBox valueBox = new HBox(6, picker, colorField, widthField);
        valueBox.getStyleClass().add("inspector-color-row");
        HBox.setHgrow(colorField, Priority.ALWAYS);

        grid.add(label, 0, row);
        grid.add(valueBox, 1, row);
    }

    private void applyFieldColor(TextField field, ColorPicker picker, java.util.function.Consumer<RgbaColor> apply) {
        try {
            RgbaColor color = RgbaColor.fromHex(field.getText());
            Color fxColor = colorFromHex(color.toHex());
            if (!fxColor.equals(picker.getValue())) {
                picker.setValue(fxColor);
            }
            apply.accept(color);
        } catch (IllegalArgumentException exception) {
            status.setText("Color inválido: usa formato #RRGGBB.");
        }
    }

    private void applyFieldWidth(TextField field, java.util.function.DoubleConsumer apply) {
        try {
            double value = Double.parseDouble(field.getText().replace(',', '.').strip());
            if (!Double.isFinite(value) || value <= 0) {
                throw new IllegalArgumentException("invalid width");
            }
            double normalized = Math.max(0.5, Math.min(12.0, value));
            field.setText(String.format(java.util.Locale.ROOT, "%.1f", normalized));
            apply.accept(normalized);
        } catch (IllegalArgumentException exception) {
            status.setText("Grosor inválido: usa un número mayor que cero.");
        }
    }

    private void applyCategoryColor(InteractiveCanvasAdapter adapter, CanvasStylePort stylePort, CategoryRow category, RgbaColor color) {
        List<String> ids = category.connector() ? connectorIdsOfKind(adapter, category.kind()) : nodeIdsOfKind(adapter, category.kind());
        for (String id : ids) {
            ElementStyle current = stylePort.resolvedStyleForElement(id);
            if (category.connector()) {
                stylePort.applyElementStyle(id, current.withStroke(new StrokeStyle(
                        color,
                        Math.max(1.25, current.stroke().width()),
                        current.stroke().pattern()
                )));
            } else {
                stylePort.applyElementStyle(id, current.withFill(FillStyle.of(color)));
            }
        }
        refreshAction.run();
        status.setText("Color aplicado a " + category.label().toLowerCase() + " (" + ids.size() + " elemento(s)).");
    }

    private void applyCategoryStrokeWidth(InteractiveCanvasAdapter adapter, CanvasStylePort stylePort, CategoryRow category, double width) {
        List<String> ids = category.connector() ? connectorIdsOfKind(adapter, category.kind()) : nodeIdsOfKind(adapter, category.kind());
        for (String id : ids) {
            ElementStyle current = stylePort.resolvedStyleForElement(id);
            stylePort.applyElementStyle(id, current.withStroke(new StrokeStyle(
                    current.stroke().color(),
                    width,
                    current.stroke().pattern()
            )));
        }
        refreshAction.run();
        status.setText("Grosor aplicado a " + category.label().toLowerCase() + " (" + ids.size() + " elemento(s)).");
    }

    private RgbaColor currentCategoryColor(InteractiveCanvasAdapter adapter, CanvasStylePort stylePort, CategoryRow category) {
        List<String> ids = category.connector() ? connectorIdsOfKind(adapter, category.kind()) : nodeIdsOfKind(adapter, category.kind());
        if (ids.isEmpty()) {
            return category.connector() ? DEFAULT_STROKE : DEFAULT_FILL;
        }
        ElementStyle style = stylePort.resolvedStyleForElement(ids.get(0));
        return category.connector() ? style.stroke().color() : style.fill().color();
    }

    private double currentCategoryStrokeWidth(InteractiveCanvasAdapter adapter, CanvasStylePort stylePort, CategoryRow category) {
        List<String> ids = category.connector() ? connectorIdsOfKind(adapter, category.kind()) : nodeIdsOfKind(adapter, category.kind());
        if (ids.isEmpty()) {
            return category.connector() ? DEFAULT_STROKE_WIDTH : ElementStyle.defaultElement().stroke().width();
        }
        return stylePort.resolvedStyleForElement(ids.get(0)).stroke().width();
    }

    private static List<CategoryRow> categoriesFor(InteractiveCanvasAdapter adapter) {
        List<CategoryRow> rows = new ArrayList<>();
        Set<String> nodeKinds = new LinkedHashSet<>();
        Set<String> connectorKinds = new LinkedHashSet<>();
        for (InteractiveCanvasNode node : adapter.nodes()) {
            if (!node.kind().isBlank()) {
                nodeKinds.add(node.kind());
            }
        }
        for (InteractiveCanvasConnector connector : adapter.connectors()) {
            if (!connector.kind().isBlank()) {
                connectorKinds.add(connector.kind());
            }
        }
        for (String kind : nodeKinds) {
            rows.add(new CategoryRow(kind, labelForNodeKind(kind), false));
        }
        for (String kind : connectorKinds) {
            rows.add(new CategoryRow(kind, labelForConnectorKind(kind), true));
        }
        return rows;
    }

    private static List<String> nodeIdsOfKind(InteractiveCanvasAdapter adapter, String kind) {
        List<String> ids = new ArrayList<>();
        for (InteractiveCanvasNode node : adapter.nodes()) {
            if (Objects.equals(node.kind(), kind)) {
                ids.add(node.id());
            }
        }
        return ids;
    }

    private static List<String> connectorIdsOfKind(InteractiveCanvasAdapter adapter, String kind) {
        List<String> ids = new ArrayList<>();
        for (InteractiveCanvasConnector connector : adapter.connectors()) {
            if (Objects.equals(connector.kind(), kind)) {
                ids.add(connector.id());
            }
        }
        return ids;
    }

    private static String labelForNodeKind(String kind) {
        return switch (cleanKind(kind)) {
            case "module", "module-root" -> "Módulos principales";
            case "module-child" -> "Submódulos";
            case "submodule" -> "Submódulos";
            case "screen" -> "Pantallas";
            case "component" -> "Componentes";
            case "actor" -> "Actores";
            case "use-case", "usecase" -> "Casos de uso";
            case "boundary", "limit", "system-boundary" -> "Límites";
            case "class" -> "Clases";
            case "interface" -> "Interfaces";
            case "activity", "task", "step" -> "Actividades";
            case "decision", "gateway" -> "Decisiones";
            case "state" -> "Estados";
            case "initial", "final" -> "Inicio / fin";
            case "environment" -> "Ambientes";
            case "server" -> "Servidores";
            case "client" -> "Clientes";
            case "service" -> "Servicios";
            case "database" -> "Bases de datos";
            case "network" -> "Redes";
            case "artifact" -> "Artefactos";
            default -> readableKind(kind) + "s";
        };
    }

    private static String labelForConnectorKind(String kind) {
        return switch (cleanKind(kind)) {
            case "dependency", "module-dependency" -> "Dependencias";
            case "module-containment" -> "Contención de módulos";
            case "association" -> "Asociaciones";
            case "include" -> "Include";
            case "extend" -> "Extend";
            case "generalization" -> "Generalizaciones";
            case "message" -> "Mensajes";
            case "transition" -> "Transiciones";
            case "flow", "sequence-flow" -> "Flujos";
            case "connector", "connection" -> "Conexiones";
            default -> "Líneas " + readableKind(kind);
        };
    }

    private static String cleanKind(String kind) {
        return Objects.toString(kind, "").strip().toLowerCase().replace('_', '-');
    }

    private static String readableKind(String kind) {
        String[] parts = cleanKind(kind).split("-");
        StringBuilder builder = new StringBuilder();
        for (String part : parts) {
            if (part.isBlank()) {
                continue;
            }
            if (!builder.isEmpty()) {
                builder.append(' ');
            }
            builder.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1));
        }
        return builder.isEmpty() ? "Elementos" : builder.toString();
    }

    private static Color colorFromHex(String value) {
        try {
            return Color.web(value == null || value.isBlank() ? DEFAULT_FILL.toHex() : value.trim());
        } catch (IllegalArgumentException exception) {
            return Color.WHITE;
        }
    }

    private static String toHex(Color color) {
        int red = (int) Math.round(color.getRed() * 255.0);
        int green = (int) Math.round(color.getGreen() * 255.0);
        int blue = (int) Math.round(color.getBlue() * 255.0);
        return String.format("#%02X%02X%02X", red, green, blue);
    }

    private record CategoryRow(String kind, String label, boolean connector) { }
}
