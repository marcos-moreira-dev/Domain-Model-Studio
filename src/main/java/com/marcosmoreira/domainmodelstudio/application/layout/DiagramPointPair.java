package com.marcosmoreira.domainmodelstudio.application.layout;

import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramPoint;
import java.util.Objects;

/** Segmento entre dos puntos del diagrama usado por el ruteador. */
public record DiagramPointPair(DiagramPoint start, DiagramPoint end) {

    private static final double EPSILON = 0.001;

    public DiagramPointPair {
        Objects.requireNonNull(start, "start");
        Objects.requireNonNull(end, "end");
    }

    public boolean isHorizontal() {
        return Math.abs(start.y() - end.y()) <= EPSILON;
    }

    public boolean isVertical() {
        return Math.abs(start.x() - end.x()) <= EPSILON;
    }
}
