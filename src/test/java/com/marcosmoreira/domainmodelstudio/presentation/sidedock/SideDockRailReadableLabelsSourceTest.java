package com.marcosmoreira.domainmodelstudio.presentation.sidedock;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl UX: las pestañas verticales del SideDock deben leerse completas aunque el carril haga scroll. */
class SideDockRailReadableLabelsSourceTest {

    private static final Path RAIL_FACTORY = Path.of(
            "src/main/java/com/marcosmoreira/domainmodelstudio/presentation/sidedock/SideDockRailFactory.java");
    private static final Path WORKBENCH_CSS = Path.of("src/main/resources/css/workbench.css");

    @Test
    void rotatedRailButtonsShouldReserveSpaceForLongModuleTitles() throws IOException {
        String source = Files.readString(RAIL_FACTORY, StandardCharsets.UTF_8);

        assertTrue(source.contains("RAIL_BUTTON_WIDTH = 170.0"),
                "El botón rotado debe reservar ancho suficiente para títulos como Entidades y relaciones.");
        assertTrue(source.contains("RAIL_SLOT_HEIGHT = 182.0"),
                "El slot vertical debe crecer junto con el botón porque el carril del SideDock es scrolleable.");
        assertTrue(source.contains("Entidades y relaciones"),
                "El motivo del tamaño debe quedar documentado para no volver a truncar las pestañas.");
    }

    @Test
    void workbenchCssShouldMirrorReadableRailDimensions() throws IOException {
        String css = Files.readString(WORKBENCH_CSS, StandardCharsets.UTF_8);

        assertTrue(css.contains("-fx-min-width: 170;"),
                "El CSS debe coincidir con la geometría del botón rotado del SideDock.");
        assertTrue(css.contains("-fx-pref-width: 170;"),
                "El CSS debe coincidir con la geometría del botón rotado del SideDock.");
        assertTrue(css.contains("-fx-min-height: 182;"),
                "El slot del carril debe permitir que cada pestaña larga se lea completa.");
        assertTrue(css.contains("-fx-pref-height: 182;"),
                "El slot del carril debe permitir que cada pestaña larga se lea completa.");
    }
}
