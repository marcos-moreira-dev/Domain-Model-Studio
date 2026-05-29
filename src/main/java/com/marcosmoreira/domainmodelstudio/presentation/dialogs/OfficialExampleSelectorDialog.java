package com.marcosmoreira.domainmodelstudio.presentation.dialogs;

import com.marcosmoreira.domainmodelstudio.application.examples.OfficialExampleDescriptor;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Window;

/** Selector de ejemplos oficiales incluidos con la aplicación. */
public final class OfficialExampleSelectorDialog {

    private static final double DIALOG_WIDTH = 760;
    private static final double DIALOG_HEIGHT = 520;
    private static final ButtonType OPEN_BUTTON = new ButtonType("Abrir ejemplo", ButtonBar.ButtonData.OK_DONE);
    private static final ButtonType OPEN_ALL_BUTTON = new ButtonType("Abrir todos", ButtonBar.ButtonData.APPLY);

    private OfficialExampleSelectorDialog() {
    }

    public static Optional<OfficialExampleDescriptor> show(
            Window owner,
            List<OfficialExampleDescriptor> examples,
            DiagramTypeId preferredType
    ) {
        Objects.requireNonNull(examples, "examples");
        if (examples.isEmpty()) {
            return Optional.empty();
        }

        return showSelection(owner, examples, preferredType)
                .filter(selection -> !selection.empty())
                .map(selection -> selection.examples().get(0));
    }

    public static Optional<OfficialExampleSelection> showSelection(
            Window owner,
            List<OfficialExampleDescriptor> examples,
            DiagramTypeId preferredType
    ) {
        Objects.requireNonNull(examples, "examples");
        if (examples.isEmpty()) {
            return Optional.empty();
        }

        Dialog<OfficialExampleSelection> dialog = buildDialog(owner);
        ExampleSelectorState state = new ExampleSelectorState(examples, preferredType);
        BorderPane content = buildContent(state);
        dialog.getDialogPane().setContent(content);
        configureOpenButtons(dialog, state);
        dialog.setResultConverter(button -> openResult(button, state));
        return dialog.showAndWait();
    }

    private static Dialog<OfficialExampleSelection> buildDialog(Window owner) {
        Dialog<OfficialExampleSelection> dialog = new Dialog<>();
        dialog.setTitle("Ejemplos oficiales");
        dialog.setHeaderText("Elige un ejemplo Markdown oficial para abrirlo como proyecto, o abre todos los ejemplos importables oficiales.");
        dialog.getDialogPane().getButtonTypes().addAll(OPEN_ALL_BUTTON, OPEN_BUTTON, ButtonType.CANCEL);
        dialog.getDialogPane().setPrefWidth(DIALOG_WIDTH);
        dialog.getDialogPane().setPrefHeight(DIALOG_HEIGHT);
        if (owner != null) {
            dialog.initOwner(owner);
        }
        return dialog;
    }

    private static BorderPane buildContent(ExampleSelectorState state) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        root.setTop(buildFilter(state));
        root.setCenter(buildExampleList(state));
        root.setRight(buildPreview(state));
        return root;
    }

    private static Node buildFilter(ExampleSelectorState state) {
        Label label = new Label("Tipo de diagrama");
        label.getStyleClass().add("dialog-field-label");

        ComboBox<DiagramTypeFilter> filter = new ComboBox<>(state.filters());
        filter.setMaxWidth(Double.MAX_VALUE);
        filter.setCellFactory(list -> filterCell());
        filter.setButtonCell(filterCell());
        filter.getSelectionModel().select(state.initialFilter());
        filter.valueProperty().addListener((observable, oldValue, selected) -> state.applyFilter(selected));

        VBox box = new VBox(6, label, filter);
        box.setPadding(new Insets(0, 0, 10, 0));
        return box;
    }

    private static ListCell<DiagramTypeFilter> filterCell() {
        return new ListCell<>() {
            @Override
            protected void updateItem(DiagramTypeFilter item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.label());
            }
        };
    }

    private static Node buildExampleList(ExampleSelectorState state) {
        ListView<OfficialExampleDescriptor> listView = new ListView<>(state.visibleExamples());
        listView.setCellFactory(list -> exampleCell());
        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, selected) -> state.select(selected));
        state.setListView(listView);
        state.selectFirstVisible();
        return listView;
    }

    private static ListCell<OfficialExampleDescriptor> exampleCell() {
        return new ListCell<>() {
            @Override
            protected void updateItem(OfficialExampleDescriptor item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : exampleCellText(item));
            }
        };
    }

    private static String exampleCellText(OfficialExampleDescriptor example) {
        String state = example.importable() ? "Importable" : "Referencia";
        return example.title() + "\n" + example.diagramTypeName() + " · " + state;
    }

    private static Node buildPreview(ExampleSelectorState state) {
        Label title = new Label("Detalle del ejemplo");
        title.getStyleClass().add("dialog-field-label");

        TextArea preview = new TextArea();
        preview.setEditable(false);
        preview.setWrapText(true);
        preview.setPrefWidth(320);
        preview.textProperty().bind(state.previewText());
        VBox.setVgrow(preview, Priority.ALWAYS);

        VBox previewBox = new VBox(8, title, preview);
        previewBox.setPadding(new Insets(0, 0, 0, 12));
        return previewBox;
    }

    private static void configureOpenButtons(Dialog<OfficialExampleSelection> dialog, ExampleSelectorState state) {
        Button openButton = (Button) dialog.getDialogPane().lookupButton(OPEN_BUTTON);
        openButton.disableProperty().bind(Bindings.createBooleanBinding(
                () -> state.selected() == null || !state.selected().importable(),
                state.selectedProperty()));

        Button openAllButton = (Button) dialog.getDialogPane().lookupButton(OPEN_ALL_BUTTON);
        openAllButton.disableProperty().bind(Bindings.createBooleanBinding(
                () -> state.importableExamples().isEmpty(),
                state.selectedProperty()));
    }

    private static OfficialExampleSelection openResult(ButtonType button, ExampleSelectorState state) {
        if (button.equals(OPEN_ALL_BUTTON)) {
            return new OfficialExampleSelection(state.importableExamples());
        }
        if (button.equals(OPEN_BUTTON) && state.selected() != null && state.selected().importable()) {
            return OfficialExampleSelection.single(state.selected());
        }
        return null;
    }


    private record DiagramTypeFilter(DiagramTypeId id, String label) {
        private boolean accepts(OfficialExampleDescriptor example) {
            return id == null || example.diagramTypeId().equals(id);
        }
    }

    private static final class ExampleSelectorState {

        private final List<OfficialExampleDescriptor> allExamples;
        private final ObservableList<OfficialExampleDescriptor> visibleExamples = FXCollections.observableArrayList();
        private final ObservableList<DiagramTypeFilter> filters;
        private final javafx.beans.property.ObjectProperty<OfficialExampleDescriptor> selected =
                new javafx.beans.property.SimpleObjectProperty<>();
        private final javafx.beans.property.StringProperty previewText =
                new javafx.beans.property.SimpleStringProperty("Selecciona un ejemplo oficial.");
        private final DiagramTypeFilter initialFilter;
        private ListView<OfficialExampleDescriptor> listView;

        private ExampleSelectorState(List<OfficialExampleDescriptor> examples, DiagramTypeId preferredType) {
            this.allExamples = List.copyOf(examples);
            this.filters = FXCollections.observableArrayList(buildFilters(this.allExamples));
            this.initialFilter = initialFilter(preferredType);
            applyFilter(initialFilter);
        }

        private ObservableList<DiagramTypeFilter> filters() {
            return filters;
        }

        private DiagramTypeFilter initialFilter() {
            return initialFilter;
        }

        private ObservableList<OfficialExampleDescriptor> visibleExamples() {
            return visibleExamples;
        }

        private List<OfficialExampleDescriptor> importableExamples() {
            return allExamples.stream()
                    .filter(OfficialExampleDescriptor::importable)
                    .toList();
        }

        private OfficialExampleDescriptor selected() {
            return selected.get();
        }

        private javafx.beans.property.ObjectProperty<OfficialExampleDescriptor> selectedProperty() {
            return selected;
        }

        private javafx.beans.property.StringProperty previewText() {
            return previewText;
        }

        private void setListView(ListView<OfficialExampleDescriptor> listView) {
            this.listView = listView;
        }

        private void applyFilter(DiagramTypeFilter filter) {
            visibleExamples.setAll(allExamples.stream()
                    .filter(example -> filter == null || filter.accepts(example))
                    .toList());
            selectFirstVisible();
        }

        private void select(OfficialExampleDescriptor example) {
            selected.set(example);
            previewText.set(example == null ? "Selecciona un ejemplo oficial." : preview(example));
        }

        private void selectFirstVisible() {
            OfficialExampleDescriptor first = visibleExamples.isEmpty() ? null : visibleExamples.get(0);
            if (listView != null) {
                listView.getSelectionModel().select(first);
            } else {
                select(first);
            }
        }

        private DiagramTypeFilter initialFilter(DiagramTypeId preferredType) {
            if (preferredType == null) {
                return filters.get(0);
            }
            return filters.stream()
                    .filter(filter -> preferredType.equals(filter.id()))
                    .findFirst()
                    .orElse(filters.get(0));
        }

        private static List<DiagramTypeFilter> buildFilters(List<OfficialExampleDescriptor> examples) {
            Map<DiagramTypeId, String> labels = new LinkedHashMap<>();
            for (OfficialExampleDescriptor example : examples) {
                labels.putIfAbsent(example.diagramTypeId(), example.diagramTypeName());
            }
            java.util.ArrayList<DiagramTypeFilter> result = new java.util.ArrayList<>();
            result.add(new DiagramTypeFilter(null, "Todos los tipos"));
            labels.forEach((id, label) -> result.add(new DiagramTypeFilter(id, label)));
            return List.copyOf(result);
        }

        private static String preview(OfficialExampleDescriptor example) {
            String availability = example.importable()
                    ? "Este ejemplo puede abrirse directamente."
                    : "Este ejemplo es referencia documental; todavía no se importa directamente.";
            return "Título: " + example.title()
                    + "\nTipo: " + example.diagramTypeName()
                    + "\nRecurso: " + example.sourceName()
                    + "\n\n" + example.summary()
                    + "\n\n" + availability;
        }
    }
}
