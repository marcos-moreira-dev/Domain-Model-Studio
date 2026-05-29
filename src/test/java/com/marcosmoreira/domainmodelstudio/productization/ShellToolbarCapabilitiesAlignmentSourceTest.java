package com.marcosmoreira.domainmodelstudio.productization;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl de Tanda 20: shell, toolbar, menú global y capacidades deben decir lo mismo. */
class ShellToolbarCapabilitiesAlignmentSourceTest {

    @Test
    void shellShouldExposeClassicSaveAndSaveAsSemantics() throws Exception {
        String view = Files.readString(Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellView.java"));
        String handler = Files.readString(Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellCommandHandler.java"));
        String saveCoordinator = Files.readString(Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/ProjectSaveCoordinator.java"));
        String session = Files.readString(Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/ProjectSession.java"));

        assertTrue(view.contains("Guardar proyecto como..."), "El menú Archivo debe exponer Guardar como.");
        assertTrue(handler.contains("requestSaveProjectAs"), "El shell debe tener comando explícito Guardar como.");
        assertTrue(handler.contains("saveCurrentProject()"), "Guardar debe pasar por una ruta clásica primero.");
        assertTrue(saveCoordinator.contains("session.projectFile"),
                "La sesión debe recordar la ruta .dms activa mediante el coordinador de guardado.");
        assertTrue(session.contains("Path projectFile"), "La sesión debe persistir la ruta de guardado actual.");
    }

    @Test
    void viewMenuShouldDisableCanvasActionsForNonVisualDocuments() throws Exception {
        String view = Files.readString(Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/shell/MainShellView.java"));

        assertTrue(view.contains("visualNavigationUnavailable()"), "Ajustar/Centrar no deben depender solo de projectClosed.");
        assertTrue(view.contains("autoLayoutUnavailable()"), "Reorganizar debe depender de tipos con auto-layout real.");
        assertTrue(view.contains("supportsVisualNavigation"), "Debe existir contrato de navegación visual por tipo.");
        assertTrue(view.contains("supportsAutoLayout"), "Debe existir contrato de auto-layout por tipo.");
    }

    @Test
    void allVisualToolbarFamiliesShouldExposeDocumentalSvgWhenCatalogPromisesSvg() throws Exception {
        String behavior = Files.readString(Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/toolbar/BehaviorToolbarContributor.java"));
        String architecture = Files.readString(Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/toolbar/ArchitectureToolbarContributor.java"));
        String administrative = Files.readString(Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/toolbar/AdministrativeToolbarContributor.java"));

        assertTrue(behavior.contains("EXPORT_SVG"), "Behavior debe exponer SVG vectorial documental.");
        assertTrue(architecture.contains("EXPORT_SVG"), "Arquitectura debe exponer SVG vectorial documental.");
        assertTrue(administrative.contains("Exportar flujo de pantallas como SVG vectorial documental"));
        assertTrue(administrative.contains("Exportar wireframes como SVG vectorial documental"));
    }
}
