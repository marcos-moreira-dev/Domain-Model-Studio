package com.marcosmoreira.domainmodelstudio.presentation.umlclass;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Regresiones fuente post-smoke manual: relación visible, texto contenido y IDs separados. */
class UmlClassRuntimeSmokeRegressionSourceTest {

    @Test
    void classCardsClipOverflowingInternalText() throws IOException {
        String source = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassRenderKit.java");
        assertTrue(source.contains("card.setClip(new Rectangle(bounds.width(), bounds.height()))"),
                "La tarjeta UML debe cortar contenido interno para que métodos/atributos no se salgan del cuadro.");
        assertTrue(source.contains("OverrunStyle.ELLIPSIS"),
                "Las líneas internas deben usar elipsis para nombres largos.");
    }

    @Test
    void umlRelationGlyphsHaveVisibleStrokeForHollowArrows() throws IOException {
        String css = read("src/main/resources/css/uml-class.css");
        assertTrue(css.contains(".uml-class-canvas-arrow-head.diagram-connector-arrow-hollow"));
        assertTrue(css.contains("-fx-stroke: #111827"),
                "Herencia, implementación y agregación necesitan borde visible, no solo relleno.");
    }

    @Test
    void canvasAdapterUsesDedicatedElementIdHelper() throws IOException {
        String source = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/umlclass/UmlClassCanvasAdapter.java");
        assertTrue(source.contains("UmlClassCanvasElementIds"),
                "Los IDs visuales UML deben estar separados para mantener el adapter dentro del límite de revisión humana.");
    }


    @Test
    void umlNodesUseDeterministicPickingAndLayerPriority() throws IOException {
        String source = read("src/main/java/com/marcosmoreira/domainmodelstudio/presentation/interactivecanvas/CanvasNodeViewFactory.java");
        assertTrue(source.contains("content.setMouseTransparent(false)"),
                "El texto interno de la clase UML debe participar en picking para que el clic llegue al nodo raíz.");
        assertTrue(source.contains("root.setViewOrder(viewOrderFor(styleClasses))"),
                "Los contenedores UML deben quedar detrás de las clases de forma determinista.");
    }

    private static String read(String path) throws IOException {
        return Files.readString(Path.of(path), StandardCharsets.UTF_8);
    }
}
