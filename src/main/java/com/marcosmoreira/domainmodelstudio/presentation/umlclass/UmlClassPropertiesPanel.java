package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import com.marcosmoreira.domainmodelstudio.presentation.workbench.WorkbenchPanelSupport;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassMember;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassNode;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlClassRelation;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlMemberKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlModuleGroup;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlRelationKind;
import com.marcosmoreira.domainmodelstudio.domain.umlclass.UmlVisibility;
import com.marcosmoreira.domainmodelstudio.presentation.sidedock.SideDockCollectionSizingPolicy;
import java.util.Objects;
import java.util.function.Function;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

/** Panel derecho de propiedades y relaciones de UML Clases. */
final class UmlClassPropertiesPanel {

    private final UmlClassDiagramViewModel viewModel;
    private final Runnable refreshCanvas;
    private final ScrollPane root;
    private final TableView<UmlClassRelation> relationTable = new TableView<>();
    private final UmlClassDisplayLabelPolicy labelPolicy = new UmlClassDisplayLabelPolicy();
    private final UmlClassFullDetailFormatter fullDetailFormatter = new UmlClassFullDetailFormatter();

    private final TextField moduleNameField = new TextField();
    private final TextField modulePathField = new TextField();
    private final TextArea moduleDescriptionArea = new TextArea();
    private final TextArea moduleNotesArea = new TextArea();

    private final ComboBox<UmlModuleGroup> classModuleCombo = new ComboBox<>();
    private final TextField classNameField = new TextField();
    private final TextField packageField = new TextField();
    private final ComboBox<UmlClassKind> classKindCombo = new ComboBox<>();
    private final ComboBox<UmlVisibility> classVisibilityCombo = new ComboBox<>();
    private final TextField responsibilityField = new TextField();
    private final TextArea classDescriptionArea = new TextArea();
    private final TextArea classNotesArea = new TextArea();
    private final TextArea classOriginArea = new TextArea();
    private final TextArea classSourceStatusArea = new TextArea();
    private final TextArea classFullDetailArea = new TextArea();

    private final ComboBox<UmlMemberKind> memberKindCombo = new ComboBox<>();
    private final TextField memberNameField = new TextField();
    private final TextField memberTypeField = new TextField();
    private final TextField memberSignatureField = new TextField();
    private final ComboBox<UmlVisibility> memberVisibilityCombo = new ComboBox<>();
    private final CheckBox staticMemberCheck = new CheckBox("Estático");
    private final TextArea memberDescriptionArea = new TextArea();

    private final ComboBox<UmlClassNode> relationSourceCombo = new ComboBox<>();
    private final ComboBox<UmlClassNode> relationTargetCombo = new ComboBox<>();
    private final ComboBox<UmlRelationKind> relationKindCombo = new ComboBox<>();
    private final TextField relationLabelField = new TextField();
    private final TextArea relationDescriptionArea = new TextArea();
    private final TextArea relationNotesArea = new TextArea();

    UmlClassPropertiesPanel(UmlClassDiagramViewModel viewModel, Runnable refreshCanvas) {
        this.viewModel = Objects.requireNonNull(viewModel, "viewModel");
        this.refreshCanvas = refreshCanvas == null ? () -> { } : refreshCanvas;
        VBox content = buildContent();
        this.root = new ScrollPane(content);
        configureRoot();
        bindViewModel();
    }

    Parent root() {
        return root;
    }

    private VBox buildContent() {
        VBox content = new VBox(12);
        content.getStyleClass().addAll("uml-class-properties", "diagram-workbench-panel-content");
        content.setPadding(new Insets(10));
        content.getChildren().addAll(buildRelationPanel(), buildModuleForm(), buildClassForm(), buildMemberForm(), buildRelationForm());
        return content;
    }

    private void configureRoot() {
        WorkbenchPanelSupport.configurePropertiesScroll(root, "uml-class-properties-scroll");
    }

    private Parent buildRelationPanel() {
        VBox panel = section("Relaciones estructurales");
        relationTable.setItems(viewModel.relations());
        relationTable.getStyleClass().add("uml-class-relation-table");
        relationTable.getColumns().add(column("Origen", r -> viewModel.classLabel(r.sourceClassId())));
        relationTable.getColumns().add(column("Tipo", r -> r.kind().displayName()));
        relationTable.getColumns().add(column("Destino", r -> viewModel.classLabel(r.targetClassId())));
        relationTable.getColumns().add(column("Etiqueta", UmlClassRelation::label));
        SideDockCollectionSizingPolicy.configureTableView(relationTable);
        panel.getChildren().add(relationTable);
        return panel;
    }

    private Parent buildModuleForm() {
        VBox box = section("Propiedades de módulo");
        configureArea(moduleDescriptionArea, 3);
        configureArea(moduleNotesArea, 2);
        GridPane grid = grid();
        int row = 0;
        addRow(grid, row++, "Nombre", moduleNameField);
        addRow(grid, row++, "Ruta/carpeta", modulePathField);
        addRow(grid, row++, "Descripción", moduleDescriptionArea);
        addRow(grid, row++, "Notas", moduleNotesArea);
        Button apply = button("Aplicar módulo", "Actualizar módulo seleccionado");
        apply.setOnAction(event -> {
            viewModel.applyModuleChanges(moduleNameField.getText(), modulePathField.getText(),
                    moduleDescriptionArea.getText(), moduleNotesArea.getText());
            refreshCanvas.run();
        });
        box.getChildren().addAll(grid, apply);
        return box;
    }

    private Parent buildClassForm() {
        VBox box = section("Propiedades de clase");
        classModuleCombo.setItems(viewModel.modules());
        classModuleCombo.setCellFactory(ignored -> moduleCell());
        classModuleCombo.setButtonCell(moduleCell());
        classKindCombo.getItems().setAll(UmlClassKind.values());
        classVisibilityCombo.getItems().setAll(UmlVisibility.values());
        configureArea(classDescriptionArea, 3);
        configureArea(classNotesArea, 2);
        configureReadOnlyArea(classOriginArea, 5);
        configureReadOnlyArea(classSourceStatusArea, 2);
        configureReadOnlyArea(classFullDetailArea, 14);
        GridPane grid = grid();
        int row = 0;
        addRow(grid, row++, "Módulo", classModuleCombo);
        addRow(grid, row++, "Nombre", classNameField);
        addRow(grid, row++, "Paquete", packageField);
        addRow(grid, row++, "Tipo", classKindCombo);
        addRow(grid, row++, "Visibilidad", classVisibilityCombo);
        addRow(grid, row++, "Responsabilidad", responsibilityField);
        addRow(grid, row++, "Descripción", classDescriptionArea);
        addRow(grid, row++, "Notas", classNotesArea);
        addRow(grid, row++, "Origen/metadatos", classOriginArea);
        addRow(grid, row++, "Archivo fuente", classSourceStatusArea);
        addRow(grid, row++, "Detalle completo", classFullDetailArea);
        Button apply = button("Aplicar clase", "Actualizar clase seleccionada");
        apply.setOnAction(event -> {
            viewModel.applyClassChanges(classModuleCombo.getValue(), classNameField.getText(),
                    packageField.getText(), classKindCombo.getValue(), classVisibilityCombo.getValue(),
                    responsibilityField.getText(), classDescriptionArea.getText(), classNotesArea.getText());
            refreshCanvas.run();
        });
        Button openCode = button("Abrir código", "Abrir el archivo fuente de la clase seleccionada en el editor configurado");
        openCode.setOnAction(event -> viewModel.openSelectedSourceInCodeEditor());
        Button systemDefault = button("Predeterminado", "Abrir el archivo con la aplicación predeterminada del sistema");
        systemDefault.setOnAction(event -> viewModel.openSelectedSourceWithSystemDefault());
        Button chooseProgram = button("Elegir programa...", "Abrir el archivo con el selector del sistema, útil si aún no configuraste editor");
        chooseProgram.setOnAction(event -> viewModel.openSelectedSourceWithProgramChooser());
        Button openFolder = button("Abrir carpeta", "Abrir la carpeta que contiene el archivo fuente detectado");
        openFolder.setOnAction(event -> viewModel.openSelectedSourceFolder());
        FlowPane codeActions = new FlowPane(8, 8, apply, openCode, systemDefault, chooseProgram, openFolder);
        codeActions.getStyleClass().add("uml-class-code-actions");
        box.getChildren().addAll(grid, codeActions);
        return box;
    }

    private Parent buildMemberForm() {
        VBox box = section("Propiedades de miembro");
        memberKindCombo.getItems().setAll(UmlMemberKind.values());
        memberVisibilityCombo.getItems().setAll(UmlVisibility.values());
        configureArea(memberDescriptionArea, 2);
        GridPane grid = grid();
        int row = 0;
        addRow(grid, row++, "Tipo", memberKindCombo);
        addRow(grid, row++, "Nombre", memberNameField);
        addRow(grid, row++, "Dato/retorno", memberTypeField);
        addRow(grid, row++, "Firma", memberSignatureField);
        addRow(grid, row++, "Visibilidad", memberVisibilityCombo);
        addRow(grid, row++, "Estático", staticMemberCheck);
        addRow(grid, row++, "Descripción", memberDescriptionArea);
        Button apply = button("Aplicar miembro", "Actualizar atributo o método seleccionado");
        apply.setOnAction(event -> {
            viewModel.applyMemberChanges(memberKindCombo.getValue(), memberNameField.getText(),
                    memberTypeField.getText(), memberSignatureField.getText(), memberVisibilityCombo.getValue(),
                    staticMemberCheck.isSelected(), memberDescriptionArea.getText());
            refreshCanvas.run();
        });
        box.getChildren().addAll(grid, apply);
        return box;
    }

    private Parent buildRelationForm() {
        VBox box = section("Propiedades de relación");
        relationSourceCombo.setItems(viewModel.classes());
        relationTargetCombo.setItems(viewModel.classes());
        relationSourceCombo.setCellFactory(ignored -> classCell());
        relationSourceCombo.setButtonCell(classCell());
        relationTargetCombo.setCellFactory(ignored -> classCell());
        relationTargetCombo.setButtonCell(classCell());
        relationKindCombo.getItems().setAll(UmlRelationKind.values());
        configureArea(relationDescriptionArea, 2);
        configureArea(relationNotesArea, 2);
        GridPane grid = grid();
        int row = 0;
        addRow(grid, row++, "Origen", relationSourceCombo);
        addRow(grid, row++, "Destino", relationTargetCombo);
        addRow(grid, row++, "Tipo", relationKindCombo);
        addRow(grid, row++, "Etiqueta", relationLabelField);
        addRow(grid, row++, "Descripción", relationDescriptionArea);
        addRow(grid, row++, "Notas", relationNotesArea);
        Button apply = button("Aplicar relación", "Actualizar relación seleccionada");
        apply.setOnAction(event -> {
            viewModel.applyRelationChanges(relationSourceCombo.getValue(), relationTargetCombo.getValue(),
                    relationKindCombo.getValue(), relationLabelField.getText(),
                    relationDescriptionArea.getText(), relationNotesArea.getText());
            refreshCanvas.run();
        });
        box.getChildren().addAll(grid, apply);
        return box;
    }

    private void bindViewModel() {
        relationTable.getSelectionModel().selectedItemProperty().addListener((observable, previous, current) -> {
            if (current != null) {
                viewModel.selectRelationByIdForCanvas(current.id());
            }
            refreshCanvas.run();
        });
        viewModel.selectedModuleProperty().addListener((observable, previous, current) -> populateModuleForm(current));
        viewModel.selectedClassProperty().addListener((observable, previous, current) -> populateClassForm(current));
        viewModel.selectedMemberProperty().addListener((observable, previous, current) -> populateMemberForm(current));
        viewModel.selectedRelationProperty().addListener((observable, previous, current) -> {
            syncRelationSelection(current);
            populateRelationForm(current);
        });
    }

    private void syncRelationSelection(UmlClassRelation current) {
        if (current == null) {
            relationTable.getSelectionModel().clearSelection();
        } else if (relationTable.getSelectionModel().getSelectedItem() != current) {
            relationTable.getSelectionModel().select(current);
        }
    }

    private void populateModuleForm(UmlModuleGroup module) {
        moduleNameField.setText(module == null ? "" : module.displayName());
        modulePathField.setText(module == null ? "" : module.path());
        moduleDescriptionArea.setText(module == null ? "" : module.description());
        moduleNotesArea.setText(module == null ? "" : module.notes());
    }

    private void populateClassForm(UmlClassNode node) {
        classModuleCombo.setValue(node == null ? null : viewModel.modules().stream()
                .filter(module -> module.id().equals(node.moduleId()))
                .findFirst()
                .orElse(null));
        classNameField.setText(node == null ? "" : node.displayName());
        packageField.setText(node == null ? "" : node.packageName());
        classKindCombo.setValue(node == null ? UmlClassKind.CLASS : node.kind());
        classVisibilityCombo.setValue(node == null ? UmlVisibility.PUBLIC : node.visibility());
        responsibilityField.setText(node == null ? "" : node.responsibility());
        classDescriptionArea.setText(node == null ? "" : node.description());
        classNotesArea.setText(node == null ? "" : node.notes());
        classOriginArea.setText(labelPolicy.classMetadataPanel(node));
        classSourceStatusArea.setText(viewModel.sourceStatusSummary(node));
        classFullDetailArea.setText(fullDetailFormatter.format(node));
    }

    private void populateMemberForm(UmlClassMember member) {
        memberKindCombo.setValue(member == null ? UmlMemberKind.ATTRIBUTE : member.kind());
        memberNameField.setText(member == null ? "" : member.name());
        memberTypeField.setText(member == null ? "" : member.type());
        memberSignatureField.setText(member == null ? "" : member.signature());
        memberVisibilityCombo.setValue(member == null ? UmlVisibility.PUBLIC : member.visibility());
        staticMemberCheck.setSelected(member != null && member.staticMember());
        memberDescriptionArea.setText(member == null ? "" : member.description());
    }

    private void populateRelationForm(UmlClassRelation relation) {
        relationSourceCombo.setValue(relation == null ? null : viewModel.classes().stream()
                .filter(candidate -> candidate.id().equals(relation.sourceClassId()))
                .findFirst()
                .orElse(null));
        relationTargetCombo.setValue(relation == null ? null : viewModel.classes().stream()
                .filter(candidate -> candidate.id().equals(relation.targetClassId()))
                .findFirst()
                .orElse(null));
        relationKindCombo.setValue(relation == null ? UmlRelationKind.DEPENDENCY : relation.kind());
        relationLabelField.setText(relation == null ? "" : relation.label());
        relationDescriptionArea.setText(relation == null ? "" : relation.description());
        relationNotesArea.setText(relation == null ? "" : relation.notes());
    }

    private VBox section(String title) {
        return WorkbenchPanelSupport.section("uml-class-section", "uml-class-section-title", title);
    }

    private GridPane grid() {
        return WorkbenchPanelSupport.grid();
    }

    private void addRow(GridPane grid, int row, String labelText, javafx.scene.Node input) {
        WorkbenchPanelSupport.addRow(grid, row, "uml-class-form-label", labelText, input);
    }

    private void configureArea(TextArea area, int rows) {
        WorkbenchPanelSupport.configureArea(area, rows);
    }

    private void configureReadOnlyArea(TextArea area, int rows) {
        configureArea(area, rows);
        area.setEditable(false);
        area.setFocusTraversable(false);
        area.getStyleClass().add("uml-class-readonly-metadata");
    }

    private Button button(String text, String tooltip) {
        return WorkbenchPanelSupport.button("uml-class-action-button", text, tooltip);
    }

    private ListCell<UmlModuleGroup> moduleCell() {
        return new ListCell<>() {
            @Override
            protected void updateItem(UmlModuleGroup item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "Sin módulo" : item.displayName());
            }
        };
    }

    private ListCell<UmlClassNode> classCell() {
        return new ListCell<>() {
            @Override
            protected void updateItem(UmlClassNode item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : labelPolicy.comboClassLabel(item));
                setTooltip(empty || item == null ? null : new Tooltip(labelPolicy.classTooltip(item)));
            }
        };
    }

    private TableColumn<UmlClassRelation, String> column(String title, Function<UmlClassRelation, String> extractor) {
        TableColumn<UmlClassRelation, String> column = new TableColumn<>(title);
        column.setCellValueFactory(data -> new SimpleStringProperty(extractor.apply(data.getValue())));
        column.setPrefWidth(130);
        return column;
    }
}
