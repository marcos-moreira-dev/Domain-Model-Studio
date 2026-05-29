package com.marcosmoreira.domainmodelstudio.presentation.toolbar;

import com.marcosmoreira.domainmodelstudio.domain.notation.NotationType;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeScreenTemplateKind;
import com.marcosmoreira.domainmodelstudio.presentation.exportable.ExportFormat;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.util.StringConverter;
import javafx.scene.layout.VBox;

/**
 * Barra contextual que muestra herramientas según el artefacto activo.
 *
 * <p>La implementación conserva contratos llamados {@code DiagramToolbar*} por compatibilidad gradual,
 * pero el rol de esta vista ya no se limita a diagramas: también sirve a documentos,
 * matrices, wireframes y artefactos estructurados como Levantamiento lógico.</p>
 */
final class ContextualToolbarView {

    private final MainToolbarViewModel viewModel;
    private final DiagramToolbarActionProvider actionProvider;
    private final VBox root = new VBox();
    private ToggleButton chenToggle;
    private ToggleButton crowsFootToggle;

    ContextualToolbarView(MainToolbarViewModel viewModel, DiagramToolbarActionProvider actionProvider) {
        this.viewModel = Objects.requireNonNull(viewModel, "viewModel");
        this.actionProvider = Objects.requireNonNull(actionProvider, "actionProvider");
        build();
    }

    Parent getRoot() {
        return root;
    }

    private void build() {
        root.getStyleClass().add("diagram-toolbar-area");
        root.visibleProperty().bind(viewModel.projectClosed().not());
        root.managedProperty().bind(root.visibleProperty());
        viewModel.activeDiagramTypeProperty().addListener((observable, previous, current) -> rebuildRows());
        viewModel.activeNotationProperty().addListener((observable, previous, current) -> refreshNotationToggles(current));
        rebuildRows();
    }

    private void rebuildRows() {
        root.getChildren().clear();
        chenToggle = null;
        crowsFootToggle = null;

        List<DiagramToolbarAction> actions = actionProvider.actionsFor(viewModel.activeDiagramTypeProperty().get());
        if (actions.isEmpty()) {
            root.getStyleClass().add("diagram-toolbar-empty");
            return;
        }
        root.getStyleClass().remove("diagram-toolbar-empty");

        Map<DiagramToolbarSection, List<DiagramToolbarAction>> grouped = groupBySection(actions);
        HBox primary = buildRow(grouped, true);
        HBox secondary = buildRow(grouped, false);
        root.getChildren().add(scrollRow(primary, "diagram-toolbar-primary-scroll"));
        if (!secondary.getChildren().isEmpty()) {
            root.getChildren().add(scrollRow(secondary, "diagram-toolbar-secondary-scroll"));
        }
        refreshNotationToggles(viewModel.activeNotationProperty().get());
    }

    private Map<DiagramToolbarSection, List<DiagramToolbarAction>> groupBySection(List<DiagramToolbarAction> actions) {
        Map<DiagramToolbarSection, List<DiagramToolbarAction>> grouped = new EnumMap<>(DiagramToolbarSection.class);
        for (DiagramToolbarAction action : actions) {
            grouped.computeIfAbsent(action.section(), ignored -> new ArrayList<>()).add(action);
        }
        return grouped;
    }

    private HBox buildRow(Map<DiagramToolbarSection, List<DiagramToolbarAction>> grouped, boolean primaryRow) {
        HBox row = new HBox();
        row.getStyleClass().addAll("main-toolbar", primaryRow ? "diagram-toolbar-primary" : "diagram-toolbar-secondary");
        row.setPadding(new Insets(primaryRow ? 4 : 3, 7, primaryRow ? 4 : 3, 7));
        row.setMinWidth(Region.USE_PREF_SIZE);
        row.setPrefWidth(Region.USE_COMPUTED_SIZE);
        row.setFillHeight(true);

        boolean sectionAdded = false;
        for (DiagramToolbarSection section : DiagramToolbarSection.values()) {
            if (section.primaryRow() != primaryRow) {
                continue;
            }
            List<DiagramToolbarAction> sectionActions = grouped.getOrDefault(section, List.of());
            if (sectionActions.isEmpty()) {
                continue;
            }
            if (sectionAdded) {
                row.getChildren().add(separator());
            }
            row.getChildren().add(groupLabel(section.displayName()));
            for (DiagramToolbarAction action : sectionActions) {
                row.getChildren().add(buildActionControl(action));
            }
            sectionAdded = true;
        }
        return row;
    }

    private Parent buildActionControl(DiagramToolbarAction action) {
        if (action.id() == DiagramToolbarActionId.APPLY_WIREFRAME_TEMPLATE) {
            return wireframeTemplateControl(action);
        }
        if (action.id() == DiagramToolbarActionId.SWITCH_TO_CHEN
                || action.id() == DiagramToolbarActionId.SWITCH_TO_CROWS_FOOT) {
            ToggleButton toggle = toggle(action);
            if (action.id() == DiagramToolbarActionId.SWITCH_TO_CHEN) {
                chenToggle = toggle;
            } else {
                crowsFootToggle = toggle;
            }
            return toggle;
        }
        Button button = new Button(action.text());
        button.getStyleClass().add("toolbar-button");
        button.getStyleClass().add(action.width().styleClass());
        applyButtonWidth(button, action.text(), action.width().minWidth(), action.width().prefWidth());
        button.setTooltip(new Tooltip(action.tooltip()));
        button.setGraphic(action.icon().imageView());
        button.setContentDisplay(action.text().isBlank() ? ContentDisplay.GRAPHIC_ONLY : ContentDisplay.LEFT);
        button.setGraphicTextGap(5);
        bindActionAvailability(button, action);
        button.setOnAction(event -> viewModel.executeDiagramAction(action.id()));
        return button;
    }


    private void applyButtonWidth(javafx.scene.control.ButtonBase control, String text, double minWidth, double prefWidth) {
        double computedMinWidth = ToolbarButtonSizingPolicy.minimumWidth(text, minWidth);
        double computedPrefWidth = ToolbarButtonSizingPolicy.preferredWidth(text, prefWidth);
        control.setMinWidth(computedMinWidth);
        control.setPrefWidth(computedPrefWidth);
    }

    private void bindActionAvailability(javafx.scene.control.ButtonBase control, DiagramToolbarAction action) {
        if (!viewModel.canExecuteDiagramAction(action.id())) {
            control.setDisable(true);
            control.setTooltip(new Tooltip(action.tooltip()
                    + "\nAcción no conectada en esta versión; no debe quedar clicable."));
            return;
        }
        exportFormatFor(action.id()).ifPresentOrElse(
                format -> control.disableProperty().bind(viewModel.exportUnavailable(format)),
                () -> control.disableProperty().bind(viewModel.diagramActionUnavailable(action.id())));
    }


    private Optional<ExportFormat> exportFormatFor(DiagramToolbarActionId actionId) {
        return switch (actionId) {
            case EXPORT_SVG -> Optional.of(ExportFormat.SVG);
            case EXPORT_PNG -> Optional.of(ExportFormat.PNG);
            case EXPORT_MARKDOWN -> Optional.of(ExportFormat.MARKDOWN);
            case EXPORT_DICTIONARY_PDF -> Optional.of(ExportFormat.PDF);
            default -> Optional.empty();
        };
    }

    private Parent wireframeTemplateControl(DiagramToolbarAction action) {
        ComboBox<WireframeScreenTemplateKind> comboBox = new ComboBox<>();
        comboBox.getStyleClass().addAll("toolbar-combo", "wireframe-template-toolbar-combo");
        comboBox.getItems().setAll(WireframeScreenTemplateKind.values());
        comboBox.valueProperty().bindBidirectional(viewModel.selectedWireframeTemplateProperty());
        comboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(WireframeScreenTemplateKind value) {
                return value == null ? "" : value.displayName();
            }

            @Override
            public WireframeScreenTemplateKind fromString(String value) {
                return null;
            }
        });
        comboBox.setTooltip(new Tooltip("Plantilla de pantalla para insertar"));
        comboBox.setMinWidth(150);
        comboBox.setPrefWidth(180);
        comboBox.disableProperty().bind(viewModel.projectClosed());

        Button button = new Button(action.text());
        button.getStyleClass().add("toolbar-button");
        button.getStyleClass().add(action.width().styleClass());
        applyButtonWidth(button, action.text(), action.width().minWidth(), action.width().prefWidth());
        button.setTooltip(new Tooltip(action.tooltip()));
        button.setGraphic(action.icon().imageView());
        button.setContentDisplay(ContentDisplay.LEFT);
        button.setGraphicTextGap(5);
        bindActionAvailability(button, action);
        button.setOnAction(event -> viewModel.executeDiagramAction(action.id()));

        HBox wrapper = new HBox(6, comboBox, button);
        wrapper.getStyleClass().add("toolbar-combo-action");
        return wrapper;
    }

    private ToggleButton toggle(DiagramToolbarAction action) {
        ToggleButton toggle = new ToggleButton(action.text());
        toggle.getStyleClass().addAll("toolbar-button", "notation-toggle");
        toggle.getStyleClass().add(action.width().styleClass());
        applyButtonWidth(toggle, action.text(), action.width().minWidth(), action.width().prefWidth());
        toggle.setTooltip(new Tooltip(action.tooltip()));
        toggle.setGraphic(action.icon().imageView());
        toggle.setContentDisplay(ContentDisplay.LEFT);
        toggle.setGraphicTextGap(5);
        bindActionAvailability(toggle, action);
        toggle.setOnAction(event -> viewModel.executeDiagramAction(action.id()));
        ToggleGroup group = notationGroup();
        toggle.setToggleGroup(group);
        return toggle;
    }

    private ToggleGroup notationGroup() {
        if (chenToggle != null && chenToggle.getToggleGroup() != null) {
            return chenToggle.getToggleGroup();
        }
        if (crowsFootToggle != null && crowsFootToggle.getToggleGroup() != null) {
            return crowsFootToggle.getToggleGroup();
        }
        return new ToggleGroup();
    }

    private void refreshNotationToggles(NotationType activeNotation) {
        if (chenToggle != null) {
            chenToggle.setSelected(activeNotation == NotationType.CHEN);
        }
        if (crowsFootToggle != null) {
            crowsFootToggle.setSelected(activeNotation == NotationType.CROWS_FOOT);
        }
    }

    private ScrollPane scrollRow(HBox row, String styleClass) {
        ScrollPane scrollPane = new ScrollPane(row);
        scrollPane.getStyleClass().addAll("main-toolbar-scroll", "diagram-toolbar-scroll", styleClass);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(false);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setPannable(false);
        scrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
            double delta = Math.abs(event.getDeltaX()) > Math.abs(event.getDeltaY()) ? event.getDeltaX() : event.getDeltaY();
            if (Math.abs(delta) < 0.01) {
                return;
            }
            scrollPane.setHvalue(Math.max(0.0, Math.min(1.0, scrollPane.getHvalue() - delta / 900.0)));
            event.consume();
        });
        return scrollPane;
    }

    private Separator separator() {
        Separator separator = new Separator(Orientation.VERTICAL);
        separator.getStyleClass().add("toolbar-separator");
        return separator;
    }

    private Label groupLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("toolbar-group-label");
        return label;
    }
}
