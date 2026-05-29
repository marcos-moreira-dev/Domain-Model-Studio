package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl Tanda 11: la búsqueda UML no debe refrescar el canvas por cada tecla. */
class Tanda11UmlClassSearchDebounceSourceTest {

    private static final Path STRUCTURE_PANEL = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassStructurePanel.java");

    @Test
    void searchFieldMustUseDebouncedRefreshInsteadOfImmediateCanvasRefresh() throws IOException {
        String source = Files.readString(STRUCTURE_PANEL, StandardCharsets.UTF_8);
        String normalized = source.replaceAll("\\s+", " ");

        assertTrue(source.contains("PauseTransition"), "La búsqueda debe usar un debounce JavaFX explícito.");
        assertTrue(source.contains("SEARCH_REFRESH_DELAY_MILLIS"), "El retardo debe quedar nombrado para ajuste controlado.");
        assertTrue(source.contains("searchRefreshDebounce.playFromStart()"), "Cada tecla debe reiniciar el debounce.");
        assertTrue(source.contains("applyPendingSearchAndRefreshCanvas"), "El refresh debe quedar diferido y centralizado.");
        assertTrue(normalized.contains(
                "searchField.textProperty().addListener((observable, previous, current) -> scheduleSearchRefresh(current));"));
        assertFalse(normalized.contains(
                "searchField.textProperty().addListener((observable, previous, current) -> { viewModel.applySearchQuery(current); refreshCanvas.run(); })"),
                "No se debe recalcular el canvas completo directamente en cada pulsación.");
    }

    @Test
    void pendingSearchMustFlushBeforeImmediateNavigationOrFilterActions() throws IOException {
        String source = Files.readString(STRUCTURE_PANEL, StandardCharsets.UTF_8);

        assertTrue(source.contains("flushPendingSearchWithoutRefresh();\n            viewModel.selectNextVisibleClass();"),
                "Buscar siguiente debe aplicar primero la búsqueda pendiente.");
        assertTrue(source.contains("flushPendingSearchWithoutRefresh();\n            viewModel.applyViewFilter(current);"),
                "Cambiar vista debe aplicar primero la búsqueda pendiente.");
        assertTrue(source.contains("flushPendingSearchWithoutRefresh();\n            viewModel.applyClassKindFilter(current);"),
                "Cambiar filtro de clase debe aplicar primero la búsqueda pendiente.");
        assertTrue(source.contains("flushPendingSearchWithoutRefresh();\n            viewModel.applyRelationKindFilter(current);"),
                "Cambiar filtro de relación debe aplicar primero la búsqueda pendiente.");
    }

    @Test
    void clearFiltersMustCancelPendingSearchDebounce() throws IOException {
        String source = Files.readString(STRUCTURE_PANEL, StandardCharsets.UTF_8);

        assertTrue(source.contains("clearPendingSearchRefresh();"),
                "Limpiar filtros debe cancelar cualquier búsqueda diferida pendiente.");
        assertTrue(source.contains("searchRefreshDebounce.stop();"),
                "La transición pendiente debe detenerse al limpiar filtros o aplicar búsqueda inmediata.");
        assertTrue(source.contains("pendingSearchQuery = \"\";"),
                "La búsqueda pendiente debe resetearse al limpiar filtros.");
    }
}
