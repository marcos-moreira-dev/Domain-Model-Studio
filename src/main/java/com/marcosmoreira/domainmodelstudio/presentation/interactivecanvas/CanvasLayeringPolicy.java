package com.marcosmoreira.domainmodelstudio.presentation.interactivecanvas;

import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Orden semántico compartido para nodos del canvas común y su exportación PNG.
 *
 * <p>Las zonas o contenedores de fondo quedan en una capa anterior a los
 * conectores; las tarjetas operables quedan por encima de las relaciones.</p>
 */
public final class CanvasLayeringPolicy {

    private static final CanvasLayeringPolicy STANDARD = new CanvasLayeringPolicy();

    private CanvasLayeringPolicy() {}

    public static CanvasLayeringPolicy standard() {
        return STANDARD;
    }

    public List<InteractiveCanvasNode> orderNodes(List<InteractiveCanvasNode> nodes) {
        return orderNodes(nodes, null);
    }

    public List<InteractiveCanvasNode> orderNodes(List<InteractiveCanvasNode> nodes, InteractiveCanvasModel model) {
        return Objects.requireNonNullElse(nodes, List.<InteractiveCanvasNode>of()).stream()
                .sorted(Comparator
                        .comparingInt(this::zOrder)
                        .thenComparingInt(node -> visualOrderFor(node, model)))
                .toList();
    }

    public int zOrder(InteractiveCanvasNode node) {
        return visualLayerFor(node).order();
    }

    public CanvasVisualLayer visualLayerFor(InteractiveCanvasNode node) {
        return isContainerLike(node) ? CanvasVisualLayer.CONTAINER : CanvasVisualLayer.NODE;
    }

    public boolean isContainerLike(InteractiveCanvasNode node) {
        String kind = node == null ? "" : normalize(node.kind());
        return containsAny(kind,
                "system", "boundary", "limit", "limite", "límite",
                "lane", "carril", "pool", "partition", "particion", "partición",
                "module", "modulo", "módulo", "package", "paquete",
                "screen", "pantalla", "environment", "ambiente", "network", "red",
                "group", "grupo", "zone", "zona", "region", "región");
    }

    private int visualOrderFor(InteractiveCanvasNode node, InteractiveCanvasModel model) {
        if (node == null || model == null) {
            return 0;
        }
        return model.layoutForNode(node.id())
                .map(NodeLayout::zOrder)
                .orElse(0);
    }

    private static boolean containsAny(String value, String... fragments) {
        for (String fragment : fragments) {
            if (value.contains(fragment)) {
                return true;
            }
        }
        return false;
    }

    private static String normalize(String value) {
        return Objects.toString(value, "").toLowerCase(Locale.ROOT);
    }
}
