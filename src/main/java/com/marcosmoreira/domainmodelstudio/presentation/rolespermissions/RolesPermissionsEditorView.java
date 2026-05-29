package com.marcosmoreira.domainmodelstudio.presentation.rolespermissions;

import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.PermissionAssignment;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.PermissionNode;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.PermissionScope;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.RoleNode;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.RoleStatus;
import com.marcosmoreira.domainmodelstudio.presentation.workbench.StructuredWorkbenchDescriptor;
import com.marcosmoreira.domainmodelstudio.presentation.workbench.StructuredWorkbenchView;
import com.marcosmoreira.domainmodelstudio.presentation.workspace.WorkspaceKind;
import com.marcosmoreira.domainmodelstudio.presentation.sidedock.SideDockCollectionSizingPolicy;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/** Vista de producto para la matriz visual de roles y permisos. */
public final class RolesPermissionsEditorView {

    private final RolesPermissionsViewModel viewModel;
    private final BorderPane root = new BorderPane();
    private final RolesPermissionsMatrixView matrixView;
    private final TableView<PermissionAssignment> assignmentTable = new TableView<>();

    private final TextField roleName = new TextField();
    private final ComboBox<RoleStatus> roleStatus = new ComboBox<>();
    private final TextArea roleResponsibility = new TextArea();
    private final TextArea roleDescription = new TextArea();
    private final TextArea roleNotes = new TextArea();

    private final TextField permissionName = new TextField();
    private final ComboBox<PermissionScope> permissionScope = new ComboBox<>();
    private final TextField permissionModule = new TextField();
    private final TextField permissionAction = new TextField();
    private final TextArea permissionDescription = new TextArea();
    private final TextArea permissionNotes = new TextArea();

    private final ComboBox<RoleNode> assignmentRole = new ComboBox<>();
    private final ComboBox<PermissionNode> assignmentPermission = new ComboBox<>();
    private final CheckBox assignmentAllowed = new CheckBox("Permitido");
    private final TextField assignmentCondition = new TextField();
    private final TextArea assignmentNotes = new TextArea();

    public RolesPermissionsEditorView(RolesPermissionsViewModel viewModel) {
        this.viewModel = viewModel;
        this.matrixView = new RolesPermissionsMatrixView(viewModel);
        build();
    }

    public Parent getRoot() {
        return root;
    }

    private void build() {
        StructuredWorkbenchDescriptor descriptor = StructuredWorkbenchDescriptor.matrix(
                WorkspaceKind.ROLES_PERMISSIONS_MATRIX,
                "roles-permissions-root",
                "Roles y permisos",
                "Matriz estructurada para revisar qué permisos tiene cada rol operativo; no es un canvas libre.",
                "Centro matricial con estructura izquierda y propiedades editables."
        );
        StructuredWorkbenchView workbench = new StructuredWorkbenchView(
                descriptor,
                buildStructurePanel(),
                buildMatrixPanel(),
                buildPropertiesPanel()
        );
        root.getStyleClass().addAll("roles-permissions-host", "specialized-editor");
        root.setCenter(workbench.getRoot());
        installBindings();
        bindViewModel();
        viewModel.registerPngExportAction(matrixView::exportAsPng);
    }

    private Parent buildStructurePanel() {
        return new RolesPermissionsStructurePanel(viewModel).root();
    }

    private Parent buildMatrixPanel() {
        BorderPane panel = new BorderPane();
        panel.getStyleClass().add("roles-permissions-matrix-panel");
        Label title = new Label("Matriz de permisos");
        title.getStyleClass().add("roles-permissions-section-title");
        BorderPane.setMargin(title, new Insets(0, 0, 8, 0));
        panel.setTop(title);
        panel.setCenter(matrixView.root());
        panel.setPadding(new Insets(10));
        return panel;
    }

    private Parent buildPropertiesPanel() {
        TabPane tabs = new TabPane();
        tabs.getStyleClass().add("roles-permissions-properties-tabs");
        tabs.getTabs().addAll(
                tab("Rol", buildRoleForm()),
                tab("Permiso", buildPermissionForm()),
                tab("Asignación", buildAssignmentPanel()));
        return tabs;
    }

    private Parent buildRoleForm() {
        VBox box = section("Rol seleccionado");
        configureArea(roleResponsibility, 3);
        configureArea(roleDescription, 3);
        configureArea(roleNotes, 2);
        GridPane grid = grid();
        int row = 0;
        addRow(grid, row++, "Nombre", roleName);
        addRow(grid, row++, "Estado", roleStatus);
        addRow(grid, row++, "Responsabilidad", roleResponsibility);
        addRow(grid, row++, "Descripción", roleDescription);
        addRow(grid, row++, "Notas", roleNotes);
        Button apply = button("Aplicar rol", "Actualizar el rol seleccionado");
        apply.setOnAction(event -> viewModel.applyRoleChanges(
                roleName.getText(), roleStatus.getValue(), roleResponsibility.getText(), roleDescription.getText(), roleNotes.getText()));
        box.getChildren().addAll(grid, apply);
        return scroll(box);
    }

    private Parent buildPermissionForm() {
        VBox box = section("Permiso seleccionado");
        configureArea(permissionDescription, 3);
        configureArea(permissionNotes, 2);
        GridPane grid = grid();
        int row = 0;
        addRow(grid, row++, "Nombre", permissionName);
        addRow(grid, row++, "Alcance", permissionScope);
        addRow(grid, row++, "Módulo", permissionModule);
        addRow(grid, row++, "Acción", permissionAction);
        addRow(grid, row++, "Descripción", permissionDescription);
        addRow(grid, row++, "Notas", permissionNotes);
        Button apply = button("Aplicar permiso", "Actualizar el permiso seleccionado");
        apply.setOnAction(event -> viewModel.applyPermissionChanges(
                permissionName.getText(), permissionScope.getValue(), permissionModule.getText(), permissionAction.getText(),
                permissionDescription.getText(), permissionNotes.getText()));
        box.getChildren().addAll(grid, apply);
        return scroll(box);
    }

    private Parent buildAssignmentPanel() {
        VBox box = section("Asignaciones");
        assignmentTable.setItems(viewModel.assignments());
        assignmentTable.getStyleClass().add("roles-permissions-assignment-table");
        assignmentTable.getColumns().add(column("Rol", assignment -> roleLabel(assignment.roleId())));
        assignmentTable.getColumns().add(column("Permiso", assignment -> permissionLabel(assignment.permissionId())));
        assignmentTable.getColumns().add(column("Decisión", assignment -> assignment.decision().displayName()));
        SideDockCollectionSizingPolicy.configureTableView(assignmentTable);

        assignmentRole.setItems(viewModel.roles());
        assignmentPermission.setItems(viewModel.permissions());
        assignmentRole.setCellFactory(ignored -> roleCell());
        assignmentRole.setButtonCell(roleCell());
        assignmentPermission.setCellFactory(ignored -> permissionCell());
        assignmentPermission.setButtonCell(permissionCell());
        configureArea(assignmentNotes, 2);
        GridPane grid = grid();
        int row = 0;
        addRow(grid, row++, "Rol", assignmentRole);
        addRow(grid, row++, "Permiso", assignmentPermission);
        addRow(grid, row++, "Permitido", assignmentAllowed);
        addRow(grid, row++, "Condición", assignmentCondition);
        addRow(grid, row++, "Observación", assignmentNotes);
        Button apply = button("Aplicar asignación", "Actualizar la asignación seleccionada");
        apply.setOnAction(event -> viewModel.applyAssignmentChanges(
                assignmentRole.getValue(), assignmentPermission.getValue(), assignmentAllowed.isSelected(),
                assignmentCondition.getText(), assignmentNotes.getText()));
        box.getChildren().addAll(assignmentTable, grid, apply);
        return scroll(box);
    }

    private void installBindings() {
        roleStatus.getItems().setAll(RoleStatus.values());
        permissionScope.getItems().setAll(PermissionScope.values());
    }

    private void bindViewModel() {
        viewModel.selectedRoleProperty().addListener((observable, previous, current) -> {
            populateRole(current);
            matrixView.refresh();
        });
        viewModel.selectedPermissionProperty().addListener((observable, previous, current) -> {
            populatePermission(current);
            matrixView.refresh();
        });
        assignmentTable.getSelectionModel().selectedItemProperty().addListener((observable, previous, current) -> {
            viewModel.selectedAssignmentProperty().set(current);
            populateAssignment(current);
            matrixView.refresh();
        });
        viewModel.selectedAssignmentProperty().addListener((observable, previous, current) -> {
            if (assignmentTable.getSelectionModel().getSelectedItem() != current) {
                assignmentTable.getSelectionModel().select(current);
            }
            populateAssignment(current);
            matrixView.refresh();
        });
        viewModel.roles().addListener((ListChangeListener<RoleNode>) change -> matrixView.refresh());
        viewModel.permissions().addListener((ListChangeListener<PermissionNode>) change -> matrixView.refresh());
        viewModel.assignments().addListener((ListChangeListener<PermissionAssignment>) change -> matrixView.refresh());
        matrixView.refresh();
    }

    private void populateRole(RoleNode role) {
        if (role == null) {
            roleName.clear();
            roleStatus.setValue(RoleStatus.PLANNED);
            roleResponsibility.clear();
            roleDescription.clear();
            roleNotes.clear();
            return;
        }
        roleName.setText(role.displayName());
        roleStatus.setValue(role.status());
        roleResponsibility.setText(role.responsibility());
        roleDescription.setText(role.description());
        roleNotes.setText(role.notes());
    }

    private void populatePermission(PermissionNode permission) {
        if (permission == null) {
            permissionName.clear();
            permissionScope.setValue(PermissionScope.ACTION);
            permissionModule.clear();
            permissionAction.clear();
            permissionDescription.clear();
            permissionNotes.clear();
            return;
        }
        permissionName.setText(permission.displayName());
        permissionScope.setValue(permission.scope());
        permissionModule.setText(permission.moduleName());
        permissionAction.setText(permission.actionName());
        permissionDescription.setText(permission.description());
        permissionNotes.setText(permission.notes());
    }

    private void populateAssignment(PermissionAssignment assignment) {
        if (assignment == null) {
            assignmentRole.setValue(null);
            assignmentPermission.setValue(null);
            assignmentAllowed.setSelected(true);
            assignmentCondition.clear();
            assignmentNotes.clear();
            return;
        }
        assignmentRole.setValue(viewModel.roles().stream().filter(role -> role.id().equals(assignment.roleId())).findFirst().orElse(null));
        assignmentPermission.setValue(viewModel.permissions().stream().filter(permission -> permission.id().equals(assignment.permissionId())).findFirst().orElse(null));
        assignmentAllowed.setSelected(assignment.allowed());
        assignmentCondition.setText(assignment.condition());
        assignmentNotes.setText(assignment.notes());
    }

    private String roleLabel(String id) {
        return viewModel.roles().stream().filter(role -> role.id().equals(id)).map(RoleNode::displayName).findFirst().orElse(id);
    }

    private String permissionLabel(String id) {
        return viewModel.permissions().stream().filter(permission -> permission.id().equals(id)).map(PermissionNode::displayName).findFirst().orElse(id);
    }

    private Tab tab(String title, Parent content) {
        Tab tab = new Tab(title, content);
        tab.setClosable(false);
        return tab;
    }

    private VBox section(String title) {
        VBox box = new VBox(8);
        box.getStyleClass().add("roles-permissions-section");
        Label label = new Label(title);
        label.getStyleClass().add("roles-permissions-section-title");
        box.getChildren().add(label);
        return box;
    }

    private GridPane grid() {
        GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(8);
        return grid;
    }

    private void addRow(GridPane grid, int row, String labelText, javafx.scene.Node input) {
        Label label = new Label(labelText);
        label.getStyleClass().add("roles-permissions-field-label");
        grid.add(label, 0, row);
        grid.add(input, 1, row);
        GridPane.setHgrow(input, Priority.ALWAYS);
        if (input instanceof TextField textField) {
            textField.setMaxWidth(Double.MAX_VALUE);
        }
        if (input instanceof ComboBox<?> comboBox) {
            comboBox.setMaxWidth(Double.MAX_VALUE);
        }
    }

    private Parent scroll(Parent content) {
        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.getStyleClass().add("roles-permissions-properties-scroll");
        return scroll;
    }

    private Button button(String text, String tooltip) {
        Button button = new Button(text);
        button.getStyleClass().add("roles-permissions-action-button");
        button.setTooltip(new Tooltip(tooltip));
        return button;
    }

    private void configureArea(TextArea area, int rows) {
        area.setPrefRowCount(rows);
        area.setWrapText(true);
        area.setMaxWidth(Double.MAX_VALUE);
    }

    private TableColumn<PermissionAssignment, String> column(String title, java.util.function.Function<PermissionAssignment, String> extractor) {
        TableColumn<PermissionAssignment, String> column = new TableColumn<>(title);
        column.setCellValueFactory(cell -> new SimpleStringProperty(extractor.apply(cell.getValue())));
        column.setPrefWidth(120);
        return column;
    }

    private ListCell<RoleNode> roleCell() {
        return new ListCell<>() {
            @Override
            protected void updateItem(RoleNode item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.displayName());
            }
        };
    }

    private ListCell<PermissionNode> permissionCell() {
        return new ListCell<>() {
            @Override
            protected void updateItem(PermissionNode item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.displayName());
            }
        };
    }
}
