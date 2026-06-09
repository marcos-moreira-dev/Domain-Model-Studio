package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class UmlClassOpeningSafetySourceTest {

    private static final Path UML_CLASS_PACKAGE = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass"
    );

    @Test
    void viewModelMustPrepareLayoutOnceBeforeRepeatedCanvasLookups() throws IOException {
        String source = Files.readString(UML_CLASS_PACKAGE.resolve("UmlClassDiagramViewModel.java"), StandardCharsets.UTF_8);

        assertTrue(source.contains("layoutPrepared"), "Debe recordar que el layout ya fue preparado para la apertura actual.");
        assertTrue(source.contains("prepareCurrentLayoutOnce"), "Debe existir una preparación explícita y reutilizable del layout.");
        assertTrue(source.contains("currentProject.layouts().activeLayout().nodeFor"),
                "Las consultas frecuentes del canvas deben leer del layout ya preparado.");
        assertTrue(source.contains("currentProject.layouts().activeLayout().connectorById"),
                "Las consultas frecuentes de relaciones deben leer del layout ya preparado.");
        assertFalse(source.contains("public NodeLayout layoutForClass(UmlClassNode node) {\n        Objects.requireNonNull(node, \"node\");\n        ensureCurrentLayout();"),
                "layoutForClass no debe reasegurar el layout global por cada clase visible.");
        assertFalse(source.contains("public NodeLayout layoutForModule(UmlModuleGroup module) {\n        Objects.requireNonNull(module, \"module\");\n        ensureCurrentLayout();"),
                "layoutForModule no debe reasegurar el layout global por cada módulo visible.");
        assertFalse(source.contains("visualLayoutService.nodeLayout(currentProject"),
                "El ViewModel UML no debe usar nodeLayout del servicio para lecturas repetidas, porque ese método asegura layout internamente.");
        assertFalse(source.contains("visualLayoutService.connectorLayout(currentProject"),
                "El ViewModel UML no debe usar connectorLayout del servicio para lecturas repetidas, porque ese método asegura layout internamente.");
    }

    @Test
    void diagramCenterMustCoalesceRefreshRequestsFromBatchListUpdates() throws IOException {
        String center = Files.readString(UML_CLASS_PACKAGE.resolve("UmlClassDiagramCenter.java"), StandardCharsets.UTF_8);
        String contributor = Files.readString(UML_CLASS_PACKAGE.resolve("UmlClassWorkbenchContributor.java"), StandardCharsets.UTF_8);

        assertTrue(center.contains("canvasRefreshQueued"), "El centro debe recordar si ya hay un refresco encolado.");
        assertTrue(center.contains("void requestCanvasRefresh()"), "Debe existir una ruta liviana para pedir refresco coalescido.");
        assertTrue(center.contains("Platform.runLater(this::runQueuedCanvasRefresh)"),
                "Los cambios de listas/selección deben agruparse en un solo refresco de JavaFX.");
        assertTrue(center.contains("modules().addListener") && center.contains("requestCanvasRefresh()"),
                "Los cambios de módulos/clases/relaciones deben pedir refresco coalescido, no render inmediato repetido.");
        assertTrue(contributor.contains("diagramCenter::requestCanvasRefresh"),
                "Los paneles laterales deben pedir refrescos coalescidos para no multiplicar renders manuales.");
    }

    @Test
    void applyDocumentMustPrepareProjectBeforePublishingFilteredLists() throws IOException {
        String source = Files.readString(UML_CLASS_PACKAGE.resolve("UmlClassDiagramViewModel.java"), StandardCharsets.UTF_8);
        int updateProject = source.indexOf("currentProject = currentProject.withUmlClassDiagram(updated);");
        int refreshLists = source.indexOf("refreshLists();", source.indexOf("private void applyDocument"));

        assertTrue(updateProject >= 0, "applyDocument debe actualizar el proyecto con el documento UML nuevo.");
        assertTrue(refreshLists >= 0, "applyDocument debe refrescar las listas visibles.");
        assertTrue(updateProject < refreshLists,
                "El proyecto/layout preparado debe actualizarse antes de publicar listas que disparan refrescos del canvas.");
    }
}
