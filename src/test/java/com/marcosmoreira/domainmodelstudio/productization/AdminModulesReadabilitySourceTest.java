package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Guardarraíl de legibilidad para módulos administrativos transversales. */
class AdminModulesReadabilitySourceTest {

    private static final Path SCREEN_FLOW_APPLICATION = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/application/screenflow");

    private static final Path ROLES_PERMISSIONS_DOCUMENT = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/domain/rolespermissions/RolesPermissionsDocument.java");

    @Test
    void screenFlowApplicationUseCasesShouldNotBeMinifiedIntoSingleLineClasses() throws IOException {
        try (var paths = Files.list(SCREEN_FLOW_APPLICATION)) {
            List<Path> sources = paths
                    .filter(path -> path.toString().endsWith(".java"))
                    .filter(path -> !path.getFileName().toString().equals("package-info.java"))
                    .toList();

            assertFalse(sources.isEmpty(), "Debe existir código de aplicación para ScreenFlow.");
            for (Path source : sources) {
                List<String> lines = Files.readAllLines(source, StandardCharsets.UTF_8);
                assertTrue(
                        lines.size() >= 10,
                        source.getFileName() + " no debe quedar comprimido como clase de una línea.");
                assertFalse(
                        lines.stream().anyMatch(line -> line.contains("public final class") && line.contains(" public ")),
                        source.getFileName() + " debe separar la declaración de clase de sus métodos públicos.");
            }
        }
    }

    @Test
    void rolesPermissionsDocumentShouldKeepReadablePublicOperations() throws IOException {
        String source = Files.readString(ROLES_PERMISSIONS_DOCUMENT, StandardCharsets.UTF_8);

        assertTrue(source.contains("public Optional<RoleNode> roleById(String id) {"));
        assertTrue(source.contains("public Optional<PermissionNode> permissionById(String id) {"));
        assertTrue(source.contains("public RolesPermissionsDocument withoutRole(String id) {"));
        assertTrue(source.contains("public RolesPermissionsDocument withoutPermission(String id) {"));
        assertFalse(
                source.contains("public Optional<RoleNode> roleById(String id){"),
                "Las operaciones públicas de RolesPermissionsDocument no deben volver al formato comprimido.");
    }
}
