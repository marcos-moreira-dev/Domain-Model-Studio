package com.marcosmoreira.domainmodelstudio.presentation.dialogs;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramCategory;
import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeDescriptor;
import com.marcosmoreira.domainmodelstudio.presentation.newproject.NewProjectCategoryViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.newproject.NewProjectDialogViewModel;
import com.marcosmoreira.domainmodelstudio.presentation.newproject.NewProjectTypeCardViewModel;
import java.util.List;
import java.util.Optional;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/** Diálogo categorizado para elegir el tipo de diagrama de un nuevo proyecto. */
public final class NewProjectDialog {

    private NewProjectDialog() {
    }

    public static Optional<DiagramTypeDescriptor> showAndWait(
            List<DiagramCategory> categories,
            List<DiagramTypeDescriptor> diagramTypes
    ) {
        NewProjectDialogViewModel model = NewProjectDialogViewModel.from(categories, diagramTypes);
        Dialog<DiagramTypeDescriptor> dialog = new Dialog<>();
        dialog.setTitle("Nuevo proyecto");
        dialog.setHeaderText("Elige una categoría y un tipo de diagrama.");

        ButtonType create = new ButtonType("Abrir", ButtonType.OK.getButtonData());
        dialog.getDialogPane().getButtonTypes().setAll(create, ButtonType.CANCEL);

        ListView<NewProjectCategoryViewModel> categoryList = createCategoryList(model);
        ListView<NewProjectTypeCardViewModel> typeList = createTypeList();
        VBox details = createDetailsPane();

        installCategorySelection(model, categoryList, typeList);
        installTypeSelection(typeList, details);

        HBox chooser = new HBox(12, categoryList, typeList, details);
        chooser.setPadding(new Insets(8, 4, 4, 4));
        HBox.setHgrow(categoryList, Priority.NEVER);
        HBox.setHgrow(typeList, Priority.ALWAYS);
        HBox.setHgrow(details, Priority.ALWAYS);

        BorderPane content = new BorderPane(chooser);
        content.setPrefSize(880, 430);
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getStyleClass().add("new-project-dialog");

        Node createButton = dialog.getDialogPane().lookupButton(create);
        typeList.getSelectionModel().selectedItemProperty().addListener((observable, previous, current) -> {
            createButton.setDisable(current == null || !current.selectable());
        });

        selectInitial(model, categoryList, typeList);
        NewProjectTypeCardViewModel selected = typeList.getSelectionModel().getSelectedItem();
        createButton.setDisable(selected == null || !selected.selectable());

        dialog.setResultConverter(button -> {
            if (button != create) {
                return null;
            }
            NewProjectTypeCardViewModel item = typeList.getSelectionModel().getSelectedItem();
            if (item == null || !item.selectable()) {
                return null;
            }
            return model.descriptorFor(item.id()).orElse(null);
        });
        return dialog.showAndWait();
    }

    private static ListView<NewProjectCategoryViewModel> createCategoryList(NewProjectDialogViewModel model) {
        ListView<NewProjectCategoryViewModel> categoryList = new ListView<>();
        categoryList.getItems().setAll(model.categories());
        categoryList.setPrefWidth(230);
        categoryList.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(NewProjectCategoryViewModel item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.displayName());
            }
        });
        return categoryList;
    }

    private static ListView<NewProjectTypeCardViewModel> createTypeList() {
        ListView<NewProjectTypeCardViewModel> typeList = new ListView<>();
        typeList.setPrefWidth(290);
        typeList.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(NewProjectTypeCardViewModel item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setDisable(false);
                    return;
                }
                setText(item.displayName() + "\n" + item.statusLabel());
                setDisable(!item.selectable());
            }
        });
        return typeList;
    }

    private static VBox createDetailsPane() {
        Label title = new Label("Selecciona un tipo de diagrama");
        title.getStyleClass().add("dialog-section-title");

        Label status = new Label("");
        status.getStyleClass().add("dialog-muted-text");

        Label description = new Label("El detalle se mostrará aquí.");
        description.setWrapText(true);
        description.getStyleClass().add("dialog-description");

        Label capabilities = new Label("");
        capabilities.setWrapText(true);
        capabilities.getStyleClass().add("dialog-muted-text");

        Label note = new Label("Los tipos en preparación se conservan como recursos de planificación. "
                + "Al abrirlos se muestra una guía sin editor visual ni exportación todavía.");
        note.setWrapText(true);
        note.getStyleClass().add("dialog-muted-text");

        VBox box = new VBox(10, title, status, description, capabilities, note);
        box.setPadding(new Insets(6));
        box.setPrefWidth(330);
        return box;
    }

    private static void installCategorySelection(
            NewProjectDialogViewModel model,
            ListView<NewProjectCategoryViewModel> categoryList,
            ListView<NewProjectTypeCardViewModel> typeList
    ) {
        categoryList.getSelectionModel().selectedItemProperty().addListener((observable, previous, current) -> {
            if (current == null) {
                typeList.getItems().clear();
                return;
            }
            typeList.getItems().setAll(model.typesFor(current.id()));
            model.initialTypeFor(current.id()).ifPresent(type -> typeList.getSelectionModel().select(type));
        });
    }

    private static void installTypeSelection(ListView<NewProjectTypeCardViewModel> typeList, VBox details) {
        typeList.getSelectionModel().selectedItemProperty().addListener((observable, previous, current) -> {
            updateDetails(details, current);
        });
    }

    private static void selectInitial(
            NewProjectDialogViewModel model,
            ListView<NewProjectCategoryViewModel> categoryList,
            ListView<NewProjectTypeCardViewModel> typeList
    ) {
        model.initialCategory().ifPresent(category -> {
            categoryList.getSelectionModel().select(category);
            typeList.getItems().setAll(model.typesFor(category.id()));
            model.initialTypeFor(category.id()).ifPresent(type -> typeList.getSelectionModel().select(type));
        });
    }

    private static void updateDetails(VBox details, NewProjectTypeCardViewModel type) {
        Label title = (Label) details.getChildren().get(0);
        Label status = (Label) details.getChildren().get(1);
        Label description = (Label) details.getChildren().get(2);
        Label capabilities = (Label) details.getChildren().get(3);

        if (type == null) {
            title.setText("Selecciona un tipo de diagrama");
            status.setText("");
            description.setText("El detalle se mostrará aquí.");
            capabilities.setText("");
            return;
        }
        title.setText(type.displayName());
        status.setText(type.statusLabel());
        description.setText(type.description());
        capabilities.setText("Capacidades: " + String.join(", ", type.capabilities()));
    }
}
