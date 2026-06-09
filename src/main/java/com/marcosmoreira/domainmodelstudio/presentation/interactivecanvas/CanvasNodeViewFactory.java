package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import java.util.Objects;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Ensambla la raíz interactiva de un nodo visual del canvas.
 *
 * <p>Permite que símbolos compuestos —por ejemplo un actor UML formado por
 * cabeza, cuerpo, brazos y piernas— se seleccionen y arrastren como una sola
 * unidad. Las primitivas internas pertenecen al símbolo; la interacción se
 * instala sobre la raíz devuelta por esta fábrica.</p>
 */
public final class CanvasNodeViewFactory {

    public StackPane wrap(
            String id,
            CanvasBounds bounds,
            Node content,
            boolean selected,
            String... styleClasses
    ) {
        Objects.requireNonNull(bounds, "Los límites del nodo no pueden ser null");
        Objects.requireNonNull(content, "El contenido visual del nodo no puede ser null");
        StackPane root = new StackPane();
        root.setAlignment(Pos.CENTER);
        root.setLayoutX(bounds.x());
        root.setLayoutY(bounds.y());
        root.setPrefSize(bounds.width(), bounds.height());
        root.setMinSize(bounds.width(), bounds.height());
        root.setMaxSize(bounds.width(), bounds.height());
        boolean containerLike = isContainerLike(styleClasses);
        // Tanda 15: los contenedores UML/C4/BPMN también deben ser seleccionables
        // desde su área visible, no solo desde una franja superior. La prioridad de
        // capas deja a las clases/nodos reales por encima, por lo que no bloquea el
        // clic directo sobre elementos internos.
        root.setPickOnBounds(true);
        root.setUserData(id);
        root.getStyleClass().add("canvas-node-view-root");
        for (String styleClass : styleClasses) {
            if (styleClass != null && !styleClass.isBlank()) {
                root.getStyleClass().add(styleClass.strip());
            }
        }
        root.setViewOrder(viewOrderFor(styleClasses));
        if (selected) {
            root.getStyleClass().add("canvas-node-view-selected");
        }

        Rectangle hitBox = new Rectangle(bounds.width(), bounds.height());
        hitBox.setFill(Color.TRANSPARENT);
        hitBox.setStroke(Color.TRANSPARENT);
        hitBox.getStyleClass().add("canvas-node-view-hitbox");
        if (containerLike) {
            hitBox.getStyleClass().add("canvas-node-view-container-hitbox");
            StackPane.setAlignment(hitBox, Pos.TOP_LEFT);
        }
        hitBox.setMouseTransparent(false);

        // La interacción se instala en la raíz. Las clases UML participan en picking
        // con todo su contenido; los contenedores mantienen su contenido transparente,
        // pero el hitbox completo permite seleccionar y mover el módulo desde el fondo.
        content.setMouseTransparent(containerLike);
        if (!containerLike) {
            content.setMouseTransparent(false);
        }
        content.getStyleClass().add("canvas-node-view-content");

        root.getChildren().addAll(hitBox, content);
        return root;
    }

    private static double viewOrderFor(String... styleClasses) {
        if (isContainerLike(styleClasses)) {
            return 10.0;
        }
        return 0.0;
    }

    private static boolean isContainerLike(String... styleClasses) {
        if (styleClasses == null) {
            return false;
        }
        for (String styleClass : styleClasses) {
            String normalized = styleClass == null ? "" : styleClass.toLowerCase(java.util.Locale.ROOT);
            if (normalized.contains("module") || normalized.contains("container")
                    || normalized.contains("package") || normalized.contains("group")) {
                return true;
            }
        }
        return false;
    }
}
