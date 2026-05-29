package com.marcosmoreira.domainmodelstudio.presentation.rolespermissions;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class RolesPermissionsMatrixEditorSourceTest {

    private static final Path EDITOR = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/rolespermissions/RolesPermissionsEditorView.java");
    private static final Path MATRIX = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/rolespermissions/RolesPermissionsMatrixView.java");
    private static final Path VIEW_MODEL = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/rolespermissions/RolesPermissionsViewModel.java");
    private static final Path MATRIX_EXPORTER = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/exportable/MatrixSnapshotExporter.java");

    @Test
    void rolesPermissionsKeepsMatrixOutsideMainEditor() throws IOException {
        String editor = Files.readString(EDITOR, StandardCharsets.UTF_8);
        String matrix = Files.readString(MATRIX, StandardCharsets.UTF_8);
        String viewModel = Files.readString(VIEW_MODEL, StandardCharsets.UTF_8);
        String matrixExporter = Files.readString(MATRIX_EXPORTER, StandardCharsets.UTF_8);

        assertTrue(editor.contains("RolesPermissionsMatrixView"),
                "El editor debe componer la matriz, no renderizar todas las celdas en la clase principal.");
        assertFalse(editor.contains("private void renderMatrix"),
                "El render de la matriz no debe regresar al editor principal.");
        assertTrue(matrix.contains("Matriz rol × permiso") || matrix.contains("matriz estructurada"),
                "Roles y permisos debe mantenerse como matriz estructurada, no canvas libre.");
        assertTrue(matrixExporter.contains("parameters.setFill(Color.WHITE)"),
                "El PNG de matriz debe usar fondo explícito para evitar capturas negras o transparentes extrañas.");
        assertTrue(viewModel.contains("ensureDocument"),
                "Las acciones del ViewModel deben fallar suavemente cuando no hay documento activo.");
    }
}
