package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class UmlClassVisualCostSourceTest {

    private static final Path UML_CLASS_PACKAGE = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass"
    );

    @Test
    void viewModelMustEstimateVisibleCostBeforePublishingListsToCanvas() throws IOException {
        String source = Files.readString(UML_CLASS_PACKAGE.resolve("UmlClassDiagramViewModel.java"), StandardCharsets.UTF_8);
        int estimate = source.indexOf("activeVisualCostEstimate.set(visualCostEstimator.estimate");
        int modulesSet = source.indexOf("modules.setAll(filtered.modules())");
        int classesSet = source.indexOf("classes.setAll(filtered.classes())");
        int relationsSet = source.indexOf("relations.setAll(filtered.relations())");

        assertTrue(source.contains("private final UmlClassVisualCostEstimator visualCostEstimator"),
                "El ViewModel debe tener un estimador dedicado para la vista UML activa.");
        assertTrue(estimate >= 0, "Debe calcularse un costo visual para la vista filtrada.");
        assertTrue(estimate < modulesSet && estimate < classesSet && estimate < relationsSet,
                "La estimación debe ocurrir antes de publicar listas que disparan refresco del canvas.");
    }

    @Test
    void structurePanelMustExposeVisualCostToTheUser() throws IOException {
        String source = Files.readString(UML_CLASS_PACKAGE.resolve("UmlClassStructurePanel.java"), StandardCharsets.UTF_8);

        assertTrue(source.contains("visualCostLabel"),
                "El panel izquierdo debe mostrar el costo visual de la vista activa.");
        assertTrue(source.contains("activeVisualCostEstimateProperty().addListener"),
                "El panel debe reaccionar cuando cambia el costo visual por vista/filtro.");
        assertTrue(source.contains("Costo visual:"),
                "La etiqueta debe usar lenguaje de producto, no jerga interna de backend/código.");
    }
}
