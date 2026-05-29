package com.marcosmoreira.domainmodelstudio.presentation.inspector;

import java.util.List;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/** Panel derecho para revisar y editar propiedades del elemento seleccionado. */
public final class InspectorView {

    private static final List<String> ANCHOR_OPTIONS = List.of(
            "Automática", "Izquierda", "Derecha", "Superior", "Inferior", "Centro"
    );
    private static final List<String> ORIENTATION_OPTIONS = List.of(
            "Automática", "Izquierda", "Derecha", "Arriba", "Abajo"
    );
    private static final List<String> CONNECTION_STYLE_OPTIONS = List.of(
            "Continua", "Punteada", "Puntos"
    );

    private final InspectorViewModel viewModel;
    private final Runnable hidePanelAction;
    private final boolean showHeader;
    private final VBox root = new VBox(0);

    public InspectorView(InspectorViewModel viewModel) {
        this(viewModel, null);
    }

    public InspectorView(InspectorViewModel viewModel, Runnable hidePanelAction) {
        this(viewModel, hidePanelAction, true);
    }

    public InspectorView(InspectorViewModel viewModel, Runnable hidePanelAction, boolean showHeader) {
        this.viewModel = viewModel;
        this.hidePanelAction = hidePanelAction;
        this.showHeader = showHeader;
        build();
    }

    public Parent getRoot() {
        return root;
    }

    private void build() {
        root.getStyleClass().add("inspector-panel");
        if (!showHeader) {
            root.getStyleClass().add("side-dock-content-owns-scroll");
        }
        root.setMinWidth(320);
        root.setPrefWidth(360);
        root.setPadding(new Insets(0));

        BorderPane header = buildHeader();
        VBox content = new VBox(10);
        content.getStyleClass().add("inspector-content");
        content.setPadding(new Insets(10, 10, 12, 10));

        content.getChildren().addAll(
                buildMessageBox(),
                buildDiagramAppearanceSection(),
                buildCategoryStylesSection(),
                buildSelectionCard(),
                buildBasicSection(),
                buildEntitySection(),
                buildAttributeSection(),
                buildRelationshipSection(),
                buildGeometrySection(),
                buildAppearanceSection(),
                buildConnectorSection(),
                buildActions()
        );

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.getStyleClass().add("inspector-scroll");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        if (showHeader) {
            root.getChildren().add(header);
        }
        root.getChildren().add(scrollPane);
    }

    private BorderPane buildHeader() {
        BorderPane header = new BorderPane();
        header.getStyleClass().add("panel-header");

        Label title = new Label("Propiedades");
        title.getStyleClass().add("panel-header-title");
        header.setLeft(title);

        Button hideButton = new Button("▶");
        hideButton.getStyleClass().addAll("panel-header-button", "panel-header-icon-button");
        hideButton.setMnemonicParsing(false);
        hideButton.setTooltip(new Tooltip("Ocultar propiedades"));
        hideButton.setOnAction(event -> {
            if (hidePanelAction != null) {
                hidePanelAction.run();
            }
        });
        header.setRight(hideButton);
        return header;
    }

    private VBox buildMessageBox() {
        VBox box = new VBox(4);
        box.getStyleClass().add("inspector-message-box");

        Label title = new Label();
        title.textProperty().bind(viewModel.selectionTitleProperty());
        title.getStyleClass().add("inspector-selection-title");

        Label summary = new Label();
        summary.textProperty().bind(viewModel.selectionSummaryProperty());
        summary.getStyleClass().add("inspector-selection-summary");
        summary.setWrapText(true);

        box.getChildren().addAll(title, summary);
        return box;
    }

    private VBox buildDiagramAppearanceSection() {
        VBox section = section("Diagrama");
        GridPane grid = grid();
        addEditableRow(grid, 0, "Nombre del lienzo", viewModel.projectTitleProperty(), viewModel.projectOpenProperty(),
                "Presiona Enter para actualizar el título del lienzo, el diagrama y el archivo Markdown fuente si existe", viewModel::applyDiagramAppearance);
        addEditableTextAreaRow(grid, 1, "Descripción", viewModel.projectDescriptionProperty(), viewModel.projectOpenProperty(),
                "Descripción general del proyecto; se actualiza al perder foco", viewModel::applyDiagramAppearance);
        addColorRow(grid, 2, "Área de trabajo", viewModel.workspaceBackgroundColorProperty(), viewModel.projectOpenProperty(),
                "Color visible del espacio amplio de edición", viewModel::applyDiagramAppearance);
        addColorRow(grid, 3, "Fondo exportado", viewModel.diagramBackgroundColorProperty(), viewModel.projectOpenProperty(),
                "Color usado como fondo al exportar la imagen", viewModel::applyDiagramAppearance);

        section.getChildren().add(grid);
        bindVisible(section, viewModel.projectOpenProperty());
        return section;
    }

    private VBox buildCategoryStylesSection() {
        VBox section = section("Estilo por categoría");
        GridPane grid = grid();
        addColorRow(grid, 0, "Entidades", viewModel.entityCategoryFillColorProperty(), viewModel.projectOpenProperty(),
                "Color de fondo para todas las entidades", viewModel::applyCategoryStyles);
        addColorRow(grid, 1, "Atributos", viewModel.attributeCategoryFillColorProperty(), viewModel.projectOpenProperty(),
                "Color de fondo para todos los atributos normales", viewModel::applyCategoryStyles);
        addColorRow(grid, 2, "Atrib. derivados", viewModel.derivedAttributeCategoryFillColorProperty(), viewModel.projectOpenProperty(),
                "Color de fondo para todos los atributos derivados", viewModel::applyCategoryStyles);
        addColorRow(grid, 3, "Relaciones", viewModel.relationshipCategoryFillColorProperty(), viewModel.projectOpenProperty(),
                "Color de fondo para todas las relaciones", viewModel::applyCategoryStyles);
        addColorRow(grid, 4, "Líneas", viewModel.connectorCategoryColorProperty(), viewModel.projectOpenProperty(),
                "Color para las líneas de conexión visibles", viewModel::applyCategoryStyles);
        addColorRow(grid, 5, "Bordes", viewModel.globalBorderColorProperty(), viewModel.projectOpenProperty(),
                "Color de borde para entidades, atributos y relaciones", viewModel::applyCategoryStyles);

        section.getChildren().add(grid);
        bindVisible(section, viewModel.projectOpenProperty());
        return section;
    }

    private VBox buildSelectionCard() {
        VBox section = section("Elemento seleccionado");
        GridPane grid = grid();
        addBoundLabelRow(grid, 0, "Referencia", viewModel.selectedElementIdProperty());
        addBoundLabelRow(grid, 1, "Tipo", viewModel.selectedElementTypeProperty());
        addBoundLabelRow(grid, 2, "Notación", viewModel.selectedNotationProperty());
        section.getChildren().add(grid);
        bindVisible(section, viewModel.hasSelectionProperty());
        return section;
    }

    private VBox buildBasicSection() {
        VBox section = section("Nombre");
        GridPane grid = grid();
        addEditableRow(grid, 0, "Nombre", viewModel.selectedElementNameProperty(), viewModel.nodeEditableProperty(),
                "Presiona Enter para actualizar el lienzo y el archivo Markdown fuente si existe", viewModel::applyChanges);
        addEditableTextAreaRow(grid, 1, "Descripción", viewModel.editableDescriptionProperty(), viewModel.nodeEditableProperty(),
                "Descripción semántica del elemento; se actualiza al perder foco", viewModel::applyChanges);
        section.getChildren().add(grid);
        bindVisible(section, viewModel.nodeEditableProperty());
        return section;
    }

    private VBox buildEntitySection() {
        VBox section = section("Entidad");
        GridPane grid = grid();
        addBoundLabelRow(grid, 0, "Clase", viewModel.entityKindProperty());
        addBoundLabelRow(grid, 1, "Grupo", viewModel.entityModuleProperty());
        addBoundLabelRow(grid, 2, "Atributos", viewModel.entityAttributeCountProperty());
        addBoundLabelRow(grid, 3, "Descripción", viewModel.entityDescriptionProperty());
        section.getChildren().add(grid);
        bindVisible(section, viewModel.entitySelectedProperty());
        return section;
    }

    private VBox buildAttributeSection() {
        VBox section = section("Atributo");
        GridPane grid = grid();
        addBoundLabelRow(grid, 0, "Entidad", viewModel.attributeOwnerProperty());
        addBoundLabelRow(grid, 1, "Marcas", viewModel.attributeTagsProperty());
        addBoundLabelRow(grid, 2, "Descripción", viewModel.attributeDescriptionProperty());
        section.getChildren().add(grid);
        bindVisible(section, viewModel.attributeSelectedProperty());
        return section;
    }

    private VBox buildRelationshipSection() {
        VBox section = section("Relación");
        GridPane grid = grid();
        addBoundLabelRow(grid, 0, "Desde", viewModel.relationshipFromProperty());
        addBoundLabelRow(grid, 1, "Hacia", viewModel.relationshipToProperty());
        addBoundLabelRow(grid, 2, "Cardinalidad", viewModel.relationshipCardinalitiesProperty());
        addEditableRow(grid, 3, "Card. desde", viewModel.relationshipFromCardinalityProperty(), viewModel.relationshipSelectedProperty(),
                "Cardinalidad del extremo origen; ejemplos: 1, 0..1, 1..M", viewModel::applyChanges);
        addEditableRow(grid, 4, "Card. hacia", viewModel.relationshipToCardinalityProperty(), viewModel.relationshipSelectedProperty(),
                "Cardinalidad del extremo destino; ejemplos: 1, 0..1, 1..M", viewModel::applyChanges);
        addBoundLabelRow(grid, 5, "Participación", viewModel.relationshipParticipationProperty());
        addBoundLabelRow(grid, 6, "Clase", viewModel.relationshipKindProperty());
        addBoundLabelRow(grid, 7, "Descripción", viewModel.relationshipDescriptionProperty());
        section.getChildren().add(grid);
        bindVisible(section, viewModel.relationshipSelectedProperty());
        return section;
    }

    private VBox buildGeometrySection() {
        VBox section = section("Ubicación y tamaño");
        GridPane grid = grid();
        addEditableRow(grid, 0, "X", viewModel.positionXProperty(), viewModel.nodeEditableProperty(),
                "Posición horizontal en el diagrama", viewModel::applyChanges);
        addEditableRow(grid, 1, "Y", viewModel.positionYProperty(), viewModel.nodeEditableProperty(),
                "Posición vertical en el diagrama", viewModel::applyChanges);
        addEditableRow(grid, 2, "Ancho", viewModel.widthProperty(), viewModel.nodeEditableProperty(),
                "Ancho visual del elemento", viewModel::applyChanges);
        addEditableRow(grid, 3, "Alto", viewModel.heightProperty(), viewModel.nodeEditableProperty(),
                "Alto visual del elemento", viewModel::applyChanges);
        section.getChildren().add(grid);
        bindVisible(section, viewModel.nodeEditableProperty());
        return section;
    }

    private VBox buildAppearanceSection() {
        VBox section = section("Apariencia");
        GridPane grid = grid();
        addColorRow(grid, 0, "Fondo", viewModel.fillColorProperty(), viewModel.nodeEditableProperty(),
                "Color de fondo de la figura", viewModel::applyChanges);
        addColorRow(grid, 1, "Borde", viewModel.strokeColorProperty(), viewModel.nodeEditableProperty(),
                "Color del borde de la figura", viewModel::applyChanges);
        addColorRow(grid, 2, "Texto", viewModel.textColorProperty(), viewModel.nodeEditableProperty(),
                "Color del texto", viewModel::applyChanges);
        addEditableRow(grid, 3, "Grosor del borde", viewModel.strokeWidthProperty(), viewModel.nodeEditableProperty(),
                "Grosor visual del borde", viewModel::applyChanges);
        addFontComboRow(grid, 4, "Fuente", viewModel.fontFamilyProperty(), viewModel.nodeEditableProperty(),
                "Fuente instalada del sistema");
        addEditableRow(grid, 5, "Tamaño", viewModel.fontSizeProperty(), viewModel.nodeEditableProperty(),
                "Tamaño del texto", viewModel::applyChanges);
        section.getChildren().add(grid);
        bindVisible(section, viewModel.nodeEditableProperty());
        return section;
    }

    private VBox buildConnectorSection() {
        VBox section = section("Conexión");
        GridPane grid = grid();
        addBoundLabelRow(grid, 0, "Une", viewModel.connectorSummaryProperty());
        addColorRow(grid, 1, "Color", viewModel.connectorColorProperty(), viewModel.connectorEditableProperty(),
                "Color visible de la conexión", viewModel::applyChanges);
        addEditableRow(grid, 2, "Grosor", viewModel.connectorWidthProperty(), viewModel.connectorEditableProperty(),
                "Grosor visual de la conexión", viewModel::applyChanges);
        addComboRow(grid, 3, "Estilo", viewModel.connectorPatternProperty(), CONNECTION_STYLE_OPTIONS,
                "Forma visual de la conexión");
        addComboRow(grid, 4, "Ancla inicio", viewModel.sourceAnchorProperty(), ANCHOR_OPTIONS,
                "Punto desde donde sale la conexión");
        addComboRow(grid, 5, "Ancla llegada", viewModel.targetAnchorProperty(), ANCHOR_OPTIONS,
                "Punto donde llega la conexión");
        addComboRow(grid, 6, "Marca inicio", viewModel.sourceMarkerOrientationProperty(), ORIENTATION_OPTIONS,
                "Orientación de la marca inicial");
        addComboRow(grid, 7, "Marca llegada", viewModel.targetMarkerOrientationProperty(), ORIENTATION_OPTIONS,
                "Orientación de la marca final");
        section.getChildren().add(grid);
        bindVisible(section, viewModel.connectorEditableProperty());
        return section;
    }

    private VBox buildActions() {
        VBox box = new VBox(8);
        box.getStyleClass().add("inspector-actions-box");
        bindVisible(box, viewModel.editableProperty());

        Label note = new Label();
        note.textProperty().bind(viewModel.inspectorMessageProperty());
        note.getStyleClass().add("inspector-note");
        note.setWrapText(true);

        box.getChildren().add(note);
        return box;
    }

    private VBox section(String titleText) {
        VBox section = new VBox(7);
        section.getStyleClass().add("inspector-section");
        Label title = new Label(titleText);
        title.getStyleClass().add("inspector-section-title");
        section.getChildren().add(title);
        return section;
    }

    private GridPane grid() {
        GridPane grid = new GridPane();
        grid.getStyleClass().add("inspector-grid");
        grid.setHgap(8);
        grid.setVgap(7);
        return grid;
    }

    private void addBoundLabelRow(GridPane grid, int row, String key, StringProperty value) {
        Label valueLabel = valueLabel();
        valueLabel.textProperty().bind(value);
        valueLabel.setWrapText(true);
        grid.add(keyLabel(key), 0, row);
        grid.add(valueLabel, 1, row);
    }

    private void addEditableRow(GridPane grid, int row, String key, StringProperty value, BooleanProperty editable, String tooltip, Runnable applyAction) {
        TextField field = new TextField();
        field.getStyleClass().add("inspector-field");
        field.textProperty().bindBidirectional(value);
        field.disableProperty().bind(editable.not());
        field.setTooltip(new Tooltip(tooltip));
        field.setOnAction(event -> applyAction.run());
        field.focusedProperty().addListener((observable, wasFocused, isFocused) -> {
            if (wasFocused && !isFocused && !field.isDisabled()) {
                applyAction.run();
            }
        });
        grid.add(keyLabel(key), 0, row);
        grid.add(field, 1, row);
    }

    private void addEditableTextAreaRow(GridPane grid, int row, String key, StringProperty value, BooleanProperty editable, String tooltip, Runnable applyAction) {
        TextArea area = new TextArea();
        area.getStyleClass().add("inspector-field");
        area.textProperty().bindBidirectional(value);
        area.disableProperty().bind(editable.not());
        area.setTooltip(new Tooltip(tooltip));
        area.setPrefRowCount(3);
        area.setWrapText(true);
        area.focusedProperty().addListener((observable, wasFocused, isFocused) -> {
            if (wasFocused && !isFocused && !area.isDisabled()) {
                applyAction.run();
            }
        });
        grid.add(keyLabel(key), 0, row);
        grid.add(area, 1, row);
    }

    private void addFontComboRow(GridPane grid, int row, String key, StringProperty value, BooleanProperty editable, String tooltip) {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().setAll(Font.getFamilies());
        comboBox.valueProperty().bindBidirectional(value);
        comboBox.disableProperty().bind(editable.not());
        comboBox.setTooltip(new Tooltip(tooltip));
        comboBox.setMaxWidth(Double.MAX_VALUE);
        comboBox.getStyleClass().add("inspector-combo");
        comboBox.setOnAction(event -> {
            if (!comboBox.isDisabled()) {
                viewModel.applyChanges();
            }
        });
        grid.add(keyLabel(key), 0, row);
        grid.add(comboBox, 1, row);
    }

    private void addColorRow(GridPane grid, int row, String key, StringProperty value, BooleanProperty editable, String tooltip, Runnable applyAction) {
        TextField field = new TextField();
        field.getStyleClass().addAll("inspector-field", "inspector-color-field");
        field.textProperty().bindBidirectional(value);
        field.disableProperty().bind(editable.not());
        field.setTooltip(new Tooltip(tooltip));

        ColorPicker picker = new ColorPicker(colorFromHex(value.get()));
        picker.getStyleClass().add("inspector-color-picker");
        picker.disableProperty().bind(editable.not());
        picker.setTooltip(new Tooltip(tooltip));
        picker.valueProperty().addListener((observable, previous, current) -> value.set(toHex(current)));
        picker.setOnAction(event -> applyAction.run());
        field.setOnAction(event -> applyAction.run());
        field.focusedProperty().addListener((observable, wasFocused, isFocused) -> {
            if (wasFocused && !isFocused && !field.isDisabled()) {
                applyAction.run();
            }
        });
        value.addListener((observable, previous, current) -> {
            Color parsed = colorFromHexOrNull(current);
            if (parsed != null && !parsed.equals(picker.getValue())) {
                picker.setValue(parsed);
            }
        });

        HBox valueBox = new HBox(6, picker, field);
        valueBox.getStyleClass().add("inspector-color-row");
        HBox.setHgrow(field, Priority.ALWAYS);

        grid.add(keyLabel(key), 0, row);
        grid.add(valueBox, 1, row);
    }

    private Color colorFromHex(String value) {
        Color parsed = colorFromHexOrNull(value);
        return parsed == null ? Color.WHITE : parsed;
    }

    private Color colorFromHexOrNull(String value) {
        try {
            if (value == null || value.isBlank()) {
                return null;
            }
            return Color.web(value.trim());
        } catch (IllegalArgumentException exception) {
            return null;
        }
    }

    private String toHex(Color color) {
        int red = (int) Math.round(color.getRed() * 255.0);
        int green = (int) Math.round(color.getGreen() * 255.0);
        int blue = (int) Math.round(color.getBlue() * 255.0);
        return String.format("#%02X%02X%02X", red, green, blue);
    }

    private void addComboRow(GridPane grid, int row, String key, StringProperty value, List<String> options, String tooltip) {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll(options);
        comboBox.valueProperty().bindBidirectional(value);
        comboBox.disableProperty().bind(viewModel.connectorEditableProperty().not());
        comboBox.setTooltip(new Tooltip(tooltip));
        comboBox.setMaxWidth(Double.MAX_VALUE);
        comboBox.getStyleClass().add("inspector-combo");
        comboBox.setOnAction(event -> viewModel.applyChanges());
        grid.add(keyLabel(key), 0, row);
        grid.add(comboBox, 1, row);
    }

    private Label keyLabel(String text) {
        Label label = new Label(text + ":");
        label.getStyleClass().add("inspector-key");
        return label;
    }

    private Label valueLabel() {
        Label label = new Label();
        label.getStyleClass().add("inspector-value");
        return label;
    }

    private void bindVisible(Node node, BooleanExpression visibleWhen) {
        node.visibleProperty().bind(visibleWhen);
        node.managedProperty().bind(visibleWhen);
    }
}
