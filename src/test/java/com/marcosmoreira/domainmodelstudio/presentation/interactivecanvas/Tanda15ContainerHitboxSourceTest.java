package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guardarraíl Tanda 15: los contenedores deben poder seleccionarse y moverse desde su área visible. */
class Tanda15ContainerHitboxSourceTest {

    @Test
    void containerHitboxMustCoverFullContainerWithoutReturningToHeaderOnlyPicking() throws IOException {
        String source = Files.readString(
                Path.of("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/CanvasNodeViewFactory.java"),
                StandardCharsets.UTF_8);

        assertTrue(source.contains("root.setPickOnBounds(true)"));
        assertTrue(source.contains("Rectangle hitBox = new Rectangle(bounds.width(), bounds.height())"));
        assertTrue(source.contains("hitbox completo permite seleccionar y mover el módulo"));
        assertFalse(source.contains("containerLike ? Math.min(32.0, bounds.height()) : bounds.height()"),
                "No debe volver el hitbox limitado a una franja superior para contenedores.");
    }
}
