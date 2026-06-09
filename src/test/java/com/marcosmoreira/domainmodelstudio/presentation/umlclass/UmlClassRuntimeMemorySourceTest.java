package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class UmlClassRuntimeMemorySourceTest {

    private static final Path UML_CLASS_PACKAGE = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass"
    );

    @Test
    void viewModelMustRefreshRuntimeMemoryBeforePublishingCanvasLists() throws IOException {
        String source = Files.readString(UML_CLASS_PACKAGE.resolve("UmlClassDiagramViewModel.java"), StandardCharsets.UTF_8);
        int filtered = source.indexOf("UmlClassDiagramFilterResult filtered = filterEngine.apply");
        int memory = source.indexOf("activeRuntimeMemorySnapshot.set(runtimeMemoryMonitor.snapshot())", filtered);
        int modulesSet = source.indexOf("modules.setAll(filtered.modules())", filtered);
        int classesSet = source.indexOf("classes.setAll(filtered.classes())", filtered);
        int relationsSet = source.indexOf("relations.setAll(filtered.relations())", filtered);

        assertTrue(source.contains("private final UmlClassRuntimeMemoryMonitor runtimeMemoryMonitor"),
                "El ViewModel debe tener un monitor dedicado de memoria/JVM.");
        assertTrue(source.contains("activeRuntimeMemorySnapshotProperty"),
                "El estado de memoria debe exponerse como propiedad observable para la UI.");
        assertTrue(filtered >= 0, "La prueba debe ubicar el bloque de filtrado de la vista activa.");
        assertTrue(memory >= 0, "Debe tomarse una lectura de memoria para la vista UML activa.");
        assertTrue(memory < modulesSet && memory < classesSet && memory < relationsSet,
                "La memoria debe leerse antes de publicar listas que pueden disparar render del canvas.");
    }

    @Test
    void structurePanelMustExposeRuntimeMemoryToTheUser() throws IOException {
        String source = Files.readString(UML_CLASS_PACKAGE.resolve("UmlClassStructurePanel.java"), StandardCharsets.UTF_8);

        assertTrue(source.contains("runtimeMemoryLabel"),
                "El panel izquierdo debe mostrar la presión de memoria/JVM.");
        assertTrue(source.contains("activeRuntimeMemorySnapshotProperty().addListener"),
                "El panel debe reaccionar cuando cambia la lectura de memoria.");
        assertTrue(source.contains("Memoria:"),
                "La etiqueta debe usar lenguaje visible de producto.");
    }
}
