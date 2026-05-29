package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Contrato mínimo para evitar selección visual stale cuando filtros/vistas ocultan el elemento seleccionado. */
class Tanda10UmlClassSelectionFilterSourceTest {

    private static final Path ADAPTER = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassCanvasAdapter.java");
    private static final Path VIEW_MODEL = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassDiagramViewModel.java");

    @Test
    void canvasAdapterMustClearVisualSelectionWhenViewModelHasNoVisibleSelection() throws IOException {
        String normalized = Files.readString(ADAPTER, StandardCharsets.UTF_8).replaceAll("\\s+", " ");

        assertTrue(normalized.contains("interactionState.syncSingleConnector(selectedId); return; } interactionState.clearSelection();"),
                "Si filtros/vistas dejan sin selección semántica visible, el canvas debe limpiar la selección visual.");
    }

    @Test
    void filtersMustRefreshListsPreservingOnlyStillVisibleSelection() throws IOException {
        String source = Files.readString(VIEW_MODEL, StandardCharsets.UTF_8);

        assertTrue(source.contains("applyViewFilter"));
        assertTrue(source.contains("applySearchQuery"));
        assertTrue(source.contains("applyClassKindFilter"));
        assertTrue(source.contains("applyRelationKindFilter"));
        assertTrue(source.contains("refreshListsPreservingSelection"));
        assertTrue(source.contains("selectedClass.set(classes.stream().filter"),
                "La selección de clase debe sobrevivir solo si la clase sigue en la lista visible.");
        assertTrue(source.contains("selectedRelation.set(relations.stream().filter"),
                "La selección de relación debe sobrevivir solo si la relación sigue visible.");
    }
}
