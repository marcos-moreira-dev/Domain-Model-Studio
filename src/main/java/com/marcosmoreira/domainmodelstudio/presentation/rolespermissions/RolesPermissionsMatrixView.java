package com.marcosmoreira.domainmodelstudio.presentation.rolespermissions;

import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.PermissionAssignment;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.PermissionDecision;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.PermissionNode;
import com.marcosmoreira.domainmodelstudio.domain.rolespermissions.RoleNode;
import com.marcosmoreira.domainmodelstudio.presentation.exportable.MatrixSnapshotExporter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

/**
 * Matriz rol × permiso.
 *
 * <p>Roles y permisos se entrega como matriz estructurada, no como canvas libre.
 * Esta vista concentra render y PNG para no convertir el editor en una clase
 * encargada de todo.</p>
 */
final class RolesPermissionsMatrixView {

    private final RolesPermissionsViewModel viewModel;
    private final MatrixSnapshotExporter matrixExporter = new MatrixSnapshotExporter();
    private final GridPane matrixGrid = new GridPane();
    private final ScrollPane root = new ScrollPane(matrixGrid);

    RolesPermissionsMatrixView(RolesPermissionsViewModel viewModel) {
        this.viewModel = viewModel;
        build();
    }

    Parent root() {
        return root;
    }

    void refresh() {
        matrixGrid.getChildren().clear();
        if (viewModel.currentDocument() == null || viewModel.roles().isEmpty() || viewModel.permissions().isEmpty()) {
            Label empty = new Label("Agrega roles y permisos para construir la matriz visual.");
            empty.getStyleClass().add("roles-permissions-empty");
            StackPane emptyPane = new StackPane(empty);
            emptyPane.setPrefSize(680, 360);
            matrixGrid.add(emptyPane, 0, 0);
            return;
        }
        addMatrixCell("Rol / permiso", 0, 0, "roles-permissions-matrix-corner");
        renderPermissionHeaders();
        renderRoleRows();
    }

    void exportAsPng(Path targetFile) throws IOException {
        refresh();
        matrixExporter.export(matrixGrid, targetFile);
    }

    private void build() {
        matrixGrid.getStyleClass().add("roles-permissions-matrix");
        matrixGrid.setHgap(6);
        matrixGrid.setVgap(6);
        matrixGrid.setPadding(new Insets(18));
        root.setFitToWidth(false);
        root.setFitToHeight(false);
        root.setPannable(true);
        root.getStyleClass().add("roles-permissions-matrix-scroll");
    }

    private void renderPermissionHeaders() {
        for (int column = 0; column < viewModel.permissions().size(); column++) {
            PermissionNode permission = viewModel.permissions().get(column);
            Label header = addMatrixCell(permission.displayName(), column + 1, 0, "roles-permissions-matrix-permission");
            header.setTooltip(new Tooltip(permission.description().isBlank() ? permission.id() : permission.description()));
            header.setOnMouseClicked(event -> viewModel.selectedPermissionProperty().set(permission));
        }
    }

    private void renderRoleRows() {
        for (int row = 0; row < viewModel.roles().size(); row++) {
            RoleNode role = viewModel.roles().get(row);
            Label roleCell = addMatrixCell(role.displayName(), 0, row + 1, "roles-permissions-matrix-role");
            roleCell.setOnMouseClicked(event -> viewModel.selectedRoleProperty().set(role));
            if (role.equals(viewModel.selectedRoleProperty().get())) {
                roleCell.getStyleClass().add("roles-permissions-matrix-selected");
            }
            renderPermissionCells(row, role);
        }
    }

    private void renderPermissionCells(int row, RoleNode role) {
        for (int column = 0; column < viewModel.permissions().size(); column++) {
            PermissionNode permission = viewModel.permissions().get(column);
            Optional<PermissionAssignment> assignment = assignmentFor(role.id(), permission.id());
            PermissionDecision decision = PermissionDecision.fromAssignment(assignment.orElse(null));
            Label cell = addMatrixCell(decision.matrixSymbol(), column + 1, row + 1, styleClassFor(decision));
            cell.setTooltip(new Tooltip(tooltipText(decision, assignment.orElse(null))));
            cell.setOnMouseClicked(event -> {
                viewModel.selectedRoleProperty().set(role);
                viewModel.selectedPermissionProperty().set(permission);
                assignment.ifPresent(value -> viewModel.selectedAssignmentProperty().set(value));
            });
        }
    }

    private Label addMatrixCell(String text, int column, int row, String styleClass) {
        Label cell = new Label(text == null ? "" : text);
        cell.getStyleClass().addAll("roles-permissions-matrix-cell", styleClass);
        cell.setMinSize(112, 46);
        cell.setPrefSize(column == 0 ? 168 : 132, 46);
        cell.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        cell.setAlignment(Pos.CENTER);
        cell.setWrapText(true);
        matrixGrid.add(cell, column, row);
        return cell;
    }

    private Optional<PermissionAssignment> assignmentFor(String roleId, String permissionId) {
        return viewModel.assignments().stream()
                .filter(assignment -> assignment.roleId().equals(roleId) && assignment.permissionId().equals(permissionId))
                .findFirst();
    }

    private static String styleClassFor(PermissionDecision decision) {
        return switch (decision) {
            case ALLOWED -> "roles-permissions-matrix-granted";
            case CONDITIONAL -> "roles-permissions-matrix-conditional";
            case DENIED -> "roles-permissions-matrix-denied";
            case NOT_APPLICABLE -> "roles-permissions-matrix-not-applicable";
            case NOT_ASSIGNED -> "roles-permissions-matrix-empty-cell";
        };
    }

    private static String tooltipText(PermissionDecision decision, PermissionAssignment assignment) {
        if (assignment == null) {
            return "Sin asignación explícita.";
        }
        String detail = assignment.condition().isBlank() ? assignment.notes() : assignment.condition();
        return detail.isBlank() ? decision.displayName() : decision.displayName() + ": " + detail;
    }
}
