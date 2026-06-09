package com.marcosmoreira.domainmodelstudio.presentation.behavior;

import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import java.util.List;
import java.util.Objects;

/**
 * Geometría temporal para UML Secuencia.
 *
 * <p>El diagrama de secuencia no se resuelve como grafo genérico. Los participantes
 * definen columnas, los mensajes definen filas temporales y los auto-mensajes se
 * dibujan como bucles cortos sobre la misma línea de vida.</p>
 */
final class SequenceMessageGeometry {

    private static final double SELF_MESSAGE_WIDTH = 82.0;
    private static final double SELF_MESSAGE_HEIGHT = 38.0;
    private static final double LIFELINE_INSET = 8.0;

    private SequenceMessageGeometry() {
    }

    static SequenceMessageRoute route(NodeLayout source, NodeLayout target, double y) {
        Objects.requireNonNull(source, "source");
        Objects.requireNonNull(target, "target");
        double startX = centerX(source);
        double endX = centerX(target);
        if (source.elementId().equals(target.elementId())) {
            double loopStartX = startX + LIFELINE_INSET;
            double loopEndX = startX + SELF_MESSAGE_WIDTH;
            double returnY = y + SELF_MESSAGE_HEIGHT;
            return new SequenceMessageRoute(
                    true,
                    List.of(
                            new Point(loopStartX, y),
                            new Point(loopEndX, y),
                            new Point(loopEndX, returnY),
                            new Point(loopStartX, returnY)
                    ),
                    loopStartX,
                    returnY,
                    Math.PI,
                    (loopStartX + loopEndX) / 2.0,
                    y - 27.0
            );
        }
        return new SequenceMessageRoute(
                false,
                List.of(new Point(startX, y), new Point(endX, y)),
                endX,
                y,
                endX < startX ? Math.PI : 0.0,
                (startX + endX) / 2.0,
                y - 27.0
        );
    }

    static boolean returnMessage(String kind) {
        String normalized = kind == null ? "" : kind.toLowerCase(java.util.Locale.ROOT);
        return normalized.contains("return") || normalized.contains("response") || normalized.contains("respuesta")
                || normalized.contains("retorno");
    }

    static boolean asynchronousMessage(String kind) {
        String normalized = kind == null ? "" : java.text.Normalizer.normalize(kind, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(java.util.Locale.ROOT);
        return normalized.contains("async") || normalized.contains("asincrono") || normalized.contains("asynchronous");
    }

    private static double centerX(NodeLayout layout) {
        return layout.x() + layout.width() / 2.0;
    }

    record SequenceMessageRoute(
            boolean selfMessage,
            List<Point> points,
            double arrowX,
            double arrowY,
            double arrowAngle,
            double labelX,
            double labelY
    ) {
    }

    record Point(double x, double y) {
    }
}
