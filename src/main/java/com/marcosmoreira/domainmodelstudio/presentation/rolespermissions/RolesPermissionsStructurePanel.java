package com.marcosmoreira.domainmodelstudio.presentation.rolespermissions;

import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.PermissionAssignment;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.PermissionNode;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.RoleNode;
import com.marcosmoreira.domainmodelstudio.presentation.sidedock.SideDockCollectionSizingPolicy;
import java.util.Optional;
import javafx.collections.ListChangeListener;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/** Panel izquierdo estructural: navegación por roles y permisos, sin herramientas de creación. */
final class RolesPermissionsStructurePanel {

    private final RolesPermissionsViewModel viewModel;
    private final VBox root = new VBox(8);
    private final ListView<RoleNode> roleList = new ListView<>();
    private final ListView<PermissionNode> permissionList = new ListView<>();
    private final ListView<PermissionAssignment> assignmentList = new ListView<>();

    RolesPermissionsStructurePanel(RolesPermissionsViewModel viewModel) {
        this.viewModel = viewModel;
        build();
        bind();
    }

    Parent root() {
        return root;
    }

    private void build() {
        root.getStyleClass().add("roles-permissions-section");
        Label title = new Label("Navega por roles y permisos; selecciona un elemento para editarlo en propiedades.");
        title.getStyleClass().addAll("roles-permissions-section-title", "diagram-workbench-panel-help");
        title.setWrapText(true);
        TabPane tabs = new TabPane();
        tabs.getStyleClass().add("roles-permissions-structure-tabs");
        tabs.getTabs().add(tab("Roles", roleList));
        tabs.getTabs().add(tab("Permisos", permissionList));
        tabs.getTabs().add(tab("Asignaciones", assignmentList));
        VBox.setVgrow(tabs, Priority.ALWAYS);
        roleList.setItems(viewModel.roles());
        SideDockCollectionSizingPolicy.configureListView(roleList);
        roleList.getStyleClass().addAll("roles-permissions-structure-list", "roles-permissions-role-list");
        roleList.setCellFactory(ignored -> new ListCell<>() {
            @Override
            protected void updateItem(RoleNode item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    return;
                }
                setText(item.displayName() + "\n" + item.status().displayName());
            }
        });
        permissionList.setItems(viewModel.permissions());
        SideDockCollectionSizingPolicy.configureListView(permissionList);
        permissionList.getStyleClass().addAll("roles-permissions-structure-list", "roles-permissions-permission-list");
        permissionList.setCellFactory(ignored -> new ListCell<>() {
            @Override
            protected void updateItem(PermissionNode item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    return;
                }
                setText(item.displayName() + "\n" + item.scope().displayName());
            }
        });
        assignmentList.setItems(viewModel.assignments());
        SideDockCollectionSizingPolicy.configureListView(assignmentList);
        assignmentList.getStyleClass().addAll("roles-permissions-structure-list", "roles-permissions-assignment-list");
        assignmentList.setCellFactory(ignored -> new ListCell<>() {
            @Override
            protected void updateItem(PermissionAssignment item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    return;
                }
                setText(roleLabel(item.roleId()) + " → " + permissionLabel(item.permissionId())
                        + "\n" + item.decision().displayName() + (item.condition().isBlank() ? "" : " · " + item.condition()));
            }
        });
        root.getChildren().addAll(title, tabs);
    }

    private void bind() {
        roleList.getSelectionModel().selectedItemProperty().addListener((observable, previous, current) -> {
            viewModel.selectedRoleProperty().set(current);
            if (current != null) {
                assignmentList.getSelectionModel().clearSelection();
            }
        });
        permissionList.getSelectionModel().selectedItemProperty().addListener((observable, previous, current) -> {
            viewModel.selectedPermissionProperty().set(current);
            if (current != null) {
                assignmentList.getSelectionModel().clearSelection();
            }
        });
        assignmentList.getSelectionModel().selectedItemProperty().addListener((observable, previous, current) -> {
            viewModel.selectedAssignmentProperty().set(current);
            if (current != null) {
                roleList.getSelectionModel().clearSelection();
                permissionList.getSelectionModel().clearSelection();
            }
        });
        viewModel.selectedRoleProperty().addListener((observable, previous, current) -> {
            if (roleList.getSelectionModel().getSelectedItem() != current) {
                roleList.getSelectionModel().select(current);
            }
        });
        viewModel.selectedPermissionProperty().addListener((observable, previous, current) -> {
            if (permissionList.getSelectionModel().getSelectedItem() != current) {
                permissionList.getSelectionModel().select(current);
            }
        });
        viewModel.selectedAssignmentProperty().addListener((observable, previous, current) -> {
            if (assignmentList.getSelectionModel().getSelectedItem() != current) {
                assignmentList.getSelectionModel().select(current);
            }
        });
        viewModel.roles().addListener((ListChangeListener<RoleNode>) change -> {
            RoleNode selected = viewModel.selectedRoleProperty().get();
            if (selected != null && !viewModel.roles().contains(selected)) {
                roleList.getSelectionModel().clearSelection();
            }
        });
        viewModel.permissions().addListener((ListChangeListener<PermissionNode>) change -> {
            PermissionNode selected = viewModel.selectedPermissionProperty().get();
            if (selected != null && !viewModel.permissions().contains(selected)) {
                permissionList.getSelectionModel().clearSelection();
            }
        });
        viewModel.assignments().addListener((ListChangeListener<PermissionAssignment>) change -> {
            PermissionAssignment selected = viewModel.selectedAssignmentProperty().get();
            if (selected != null && !viewModel.assignments().contains(selected)) {
                assignmentList.getSelectionModel().clearSelection();
            }
        });
    }


    private String roleLabel(String roleId) {
        Optional<RoleNode> role = viewModel.roles().stream()
                .filter(candidate -> candidate.id().equals(roleId))
                .findFirst();
        return role.map(RoleNode::displayName).orElse(roleId);
    }

    private String permissionLabel(String permissionId) {
        Optional<PermissionNode> permission = viewModel.permissions().stream()
                .filter(candidate -> candidate.id().equals(permissionId))
                .findFirst();
        return permission.map(PermissionNode::displayName).orElse(permissionId);
    }

    private Tab tab(String title, Parent content) {
        Tab tab = new Tab(title, content);
        tab.setClosable(false);
        return tab;
    }
}
