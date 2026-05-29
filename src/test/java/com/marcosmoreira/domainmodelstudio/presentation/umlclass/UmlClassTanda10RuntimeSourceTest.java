package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class UmlClassTanda10RuntimeSourceTest {

    @Test
    void umlToolbarExposesSvgMarkdownAndPng() throws Exception {
        String provider = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/toolbar/UmlClassToolbarContributor.java"),
                StandardCharsets.UTF_8);
        assertTrue(provider.contains("private List<DiagramToolbarAction> umlClassActions()"));
        assertTrue(provider.contains("DiagramToolbarActionId.EXPORT_SVG"),
                "UML Clases debe exponer SVG porque el catálogo promete SVG para diagramas visuales.");
        assertTrue(provider.contains("DiagramToolbarActionId.EXPORT_MARKDOWN"));
        assertTrue(provider.contains("DiagramToolbarActionId.EXPORT_PNG"));
    }

    @Test
    void rectangleSelectionFocusesClassBeforeModuleForSourceNavigation() throws Exception {
        String adapter = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassCanvasAdapter.java"),
                StandardCharsets.UTF_8);

        assertTrue(adapter.contains(".filter(id -> normalize(id).startsWith(CLASS_PREFIX))"),
                "Si una selección rectangular incluye módulos y clases, el inspector debe enfocar primero una clase.");
        assertTrue(adapter.contains("selectFirstPropertyNode"),
                "El foco semántico posterior permite Abrir código sin depender solo de selección visual.");
    }

    @Test
    void directClickFocusesNodeSemanticallyWithoutBreakingGroupDrag() throws Exception {
        String surface = Files.readString(Path.of(
                "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/CanvasNodeInteractionCoordinator.java"),
                StandardCharsets.UTF_8);

        assertTrue(surface.contains("adapter.selectNode(node.id(), event.isShiftDown());"),
                "El clic directo simple no debe quedarse solo en estilo local; debe actualizar el foco semántico.");
        assertTrue(surface.contains("shouldPreserveSelectionGroup"),
                "Si el nodo ya pertenece a una selección múltiple, el drag debe conservar el grupo seleccionado.");
        assertTrue(surface.contains("no la destruimos"),
                "La razón de preservar selección múltiple debe quedar documentada para evitar regresiones.");
    }
}
