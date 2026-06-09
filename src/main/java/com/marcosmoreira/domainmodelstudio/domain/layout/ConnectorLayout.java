package com.marcosmoreira.domainmodelstudio.domain.layout;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Layout de una conexión visual entre dos elementos.
 *
 * <p>La conexión puede representar, por ejemplo, entidad-relación, entidad-atributo,
 * relación-entidad o una línea Crow's Foot. La semántica sigue en el modelo; aquí solo
 * se guardan anclas, trayectoria, puntos intermedios, marcadores y el desplazamiento visual
 * de la etiqueta cuando la notación necesita rotular una línea.</p>
 */
public final class ConnectorLayout {

    private final DiagramElementId connectorId;
    private final DiagramElementId sourceElementId;
    private final DiagramElementId targetElementId;
    private final AnchorSide sourceAnchor;
    private final AnchorSide targetAnchor;
    private final ConnectorPathKind pathKind;
    private final List<BendPoint> bendPoints;
    private final ConnectorMarker sourceMarker;
    private final ConnectorMarker targetMarker;
    private final MarkerOrientation sourceMarkerOrientation;
    private final MarkerOrientation targetMarkerOrientation;
    private final double labelOffsetX;
    private final double labelOffsetY;
    private final boolean visible;

    public ConnectorLayout(
            DiagramElementId connectorId,
            DiagramElementId sourceElementId,
            DiagramElementId targetElementId,
            AnchorSide sourceAnchor,
            AnchorSide targetAnchor,
            ConnectorPathKind pathKind,
            List<BendPoint> bendPoints,
            ConnectorMarker sourceMarker,
            ConnectorMarker targetMarker,
            MarkerOrientation sourceMarkerOrientation,
            MarkerOrientation targetMarkerOrientation,
            boolean visible
    ) {
        this(
                connectorId,
                sourceElementId,
                targetElementId,
                sourceAnchor,
                targetAnchor,
                pathKind,
                bendPoints,
                sourceMarker,
                targetMarker,
                sourceMarkerOrientation,
                targetMarkerOrientation,
                0.0,
                0.0,
                visible
        );
    }

    public ConnectorLayout(
            DiagramElementId connectorId,
            DiagramElementId sourceElementId,
            DiagramElementId targetElementId,
            AnchorSide sourceAnchor,
            AnchorSide targetAnchor,
            ConnectorPathKind pathKind,
            List<BendPoint> bendPoints,
            ConnectorMarker sourceMarker,
            ConnectorMarker targetMarker,
            MarkerOrientation sourceMarkerOrientation,
            MarkerOrientation targetMarkerOrientation,
            double labelOffsetX,
            double labelOffsetY,
            boolean visible
    ) {
        this.connectorId = Objects.requireNonNull(connectorId, "El ID del conector no puede ser null");
        this.sourceElementId = Objects.requireNonNull(sourceElementId, "El origen no puede ser null");
        this.targetElementId = Objects.requireNonNull(targetElementId, "El destino no puede ser null");
        this.sourceAnchor = sourceAnchor == null ? AnchorSide.AUTO : sourceAnchor;
        this.targetAnchor = targetAnchor == null ? AnchorSide.AUTO : targetAnchor;
        this.pathKind = pathKind == null ? ConnectorPathKind.STRAIGHT : pathKind;
        this.bendPoints = List.copyOf(bendPoints == null ? List.of() : bendPoints);
        this.sourceMarker = sourceMarker == null ? ConnectorMarker.NONE : sourceMarker;
        this.targetMarker = targetMarker == null ? ConnectorMarker.NONE : targetMarker;
        this.sourceMarkerOrientation = sourceMarkerOrientation == null ? MarkerOrientation.AUTO : sourceMarkerOrientation;
        this.targetMarkerOrientation = targetMarkerOrientation == null ? MarkerOrientation.AUTO : targetMarkerOrientation;
        if (!Double.isFinite(labelOffsetX) || !Double.isFinite(labelOffsetY)) {
            throw new IllegalArgumentException("El desplazamiento de etiqueta debe ser finito");
        }
        this.labelOffsetX = labelOffsetX;
        this.labelOffsetY = labelOffsetY;
        this.visible = visible;
    }

    public static ConnectorLayout straight(String connectorId, String sourceElementId, String targetElementId) {
        return new ConnectorLayout(
                DiagramElementId.of(connectorId),
                DiagramElementId.of(sourceElementId),
                DiagramElementId.of(targetElementId),
                AnchorSide.AUTO,
                AnchorSide.AUTO,
                ConnectorPathKind.STRAIGHT,
                List.of(),
                ConnectorMarker.NONE,
                ConnectorMarker.NONE,
                MarkerOrientation.AUTO,
                MarkerOrientation.AUTO,
                true
        );
    }

    public DiagramElementId connectorId() {
        return connectorId;
    }

    public DiagramElementId sourceElementId() {
        return sourceElementId;
    }

    public DiagramElementId targetElementId() {
        return targetElementId;
    }

    public AnchorSide sourceAnchor() {
        return sourceAnchor;
    }

    public AnchorSide targetAnchor() {
        return targetAnchor;
    }

    public ConnectorPathKind pathKind() {
        return pathKind;
    }

    public List<BendPoint> bendPoints() {
        return bendPoints;
    }

    public ConnectorMarker sourceMarker() {
        return sourceMarker;
    }

    public ConnectorMarker targetMarker() {
        return targetMarker;
    }

    public MarkerOrientation sourceMarkerOrientation() {
        return sourceMarkerOrientation;
    }

    public MarkerOrientation targetMarkerOrientation() {
        return targetMarkerOrientation;
    }

    public double labelOffsetX() {
        return labelOffsetX;
    }

    public double labelOffsetY() {
        return labelOffsetY;
    }

    public boolean visible() {
        return visible;
    }

    public ConnectorLayout withAnchors(AnchorSide updatedSourceAnchor, AnchorSide updatedTargetAnchor) {
        return new ConnectorLayout(
                connectorId,
                sourceElementId,
                targetElementId,
                updatedSourceAnchor,
                updatedTargetAnchor,
                pathKind,
                bendPoints,
                sourceMarker,
                targetMarker,
                sourceMarkerOrientation,
                targetMarkerOrientation,
                labelOffsetX,
                labelOffsetY,
                visible
        );
    }

    public ConnectorLayout withMarkers(ConnectorMarker updatedSourceMarker, ConnectorMarker updatedTargetMarker) {
        return new ConnectorLayout(
                connectorId,
                sourceElementId,
                targetElementId,
                sourceAnchor,
                targetAnchor,
                pathKind,
                bendPoints,
                updatedSourceMarker,
                updatedTargetMarker,
                sourceMarkerOrientation,
                targetMarkerOrientation,
                labelOffsetX,
                labelOffsetY,
                visible
        );
    }

    public ConnectorLayout withMarkerOrientations(
            MarkerOrientation updatedSourceOrientation,
            MarkerOrientation updatedTargetOrientation
    ) {
        return new ConnectorLayout(
                connectorId,
                sourceElementId,
                targetElementId,
                sourceAnchor,
                targetAnchor,
                pathKind,
                bendPoints,
                sourceMarker,
                targetMarker,
                updatedSourceOrientation,
                updatedTargetOrientation,
                labelOffsetX,
                labelOffsetY,
                visible
        );
    }

    public ConnectorLayout withPathKind(ConnectorPathKind updatedPathKind) {
        return new ConnectorLayout(
                connectorId,
                sourceElementId,
                targetElementId,
                sourceAnchor,
                targetAnchor,
                updatedPathKind,
                bendPoints,
                sourceMarker,
                targetMarker,
                sourceMarkerOrientation,
                targetMarkerOrientation,
                labelOffsetX,
                labelOffsetY,
                visible
        );
    }

    public ConnectorLayout withBendPoint(BendPoint bendPoint) {
        Objects.requireNonNull(bendPoint, "El punto intermedio no puede ser null");
        List<BendPoint> updated = new ArrayList<>(bendPoints);
        updated.add(bendPoint);
        return withBendPoints(updated);
    }

    public ConnectorLayout withMovedBendPoint(int index, double deltaX, double deltaY) {
        ensureValidBendPointIndex(index);
        if (!Double.isFinite(deltaX) || !Double.isFinite(deltaY)) {
            throw new IllegalArgumentException("Los deltas del punto intermedio deben ser finitos");
        }
        List<BendPoint> updated = new ArrayList<>(bendPoints);
        updated.set(index, updated.get(index).translatedBy(deltaX, deltaY));
        return withBendPoints(updated);
    }

    public ConnectorLayout withoutBendPoint(int index) {
        ensureValidBendPointIndex(index);
        List<BendPoint> updated = new ArrayList<>(bendPoints);
        updated.remove(index);
        ConnectorPathKind updatedPathKind = updated.isEmpty() ? ConnectorPathKind.STRAIGHT : ConnectorPathKind.POLYLINE;
        return new ConnectorLayout(
                connectorId,
                sourceElementId,
                targetElementId,
                sourceAnchor,
                targetAnchor,
                updatedPathKind,
                updated,
                sourceMarker,
                targetMarker,
                sourceMarkerOrientation,
                targetMarkerOrientation,
                labelOffsetX,
                labelOffsetY,
                visible
        );
    }

    public ConnectorLayout withBendPoints(List<BendPoint> updatedBendPoints) {
        return withRoutedBendPoints(ConnectorPathKind.POLYLINE, updatedBendPoints);
    }

    public ConnectorLayout withOrthogonalBendPoints(List<BendPoint> updatedBendPoints) {
        return withRoutedBendPoints(ConnectorPathKind.ORTHOGONAL, updatedBendPoints);
    }

    public ConnectorLayout withRoutedBendPoints(ConnectorPathKind updatedPathKind, List<BendPoint> updatedBendPoints) {
        List<BendPoint> updated = List.copyOf(updatedBendPoints == null ? List.of() : updatedBendPoints);
        ConnectorPathKind resolvedPathKind = updated.isEmpty() ? ConnectorPathKind.STRAIGHT
                : updatedPathKind == null ? ConnectorPathKind.POLYLINE : updatedPathKind;
        return new ConnectorLayout(
                connectorId,
                sourceElementId,
                targetElementId,
                sourceAnchor,
                targetAnchor,
                resolvedPathKind,
                updated,
                sourceMarker,
                targetMarker,
                sourceMarkerOrientation,
                targetMarkerOrientation,
                labelOffsetX,
                labelOffsetY,
                visible
        );
    }

    public ConnectorLayout withLabelOffset(double updatedLabelOffsetX, double updatedLabelOffsetY) {
        return new ConnectorLayout(
                connectorId,
                sourceElementId,
                targetElementId,
                sourceAnchor,
                targetAnchor,
                pathKind,
                bendPoints,
                sourceMarker,
                targetMarker,
                sourceMarkerOrientation,
                targetMarkerOrientation,
                updatedLabelOffsetX,
                updatedLabelOffsetY,
                visible
        );
    }

    public ConnectorLayout withMovedLabelOffset(double deltaX, double deltaY, double maximumDistanceFromLine) {
        if (!Double.isFinite(deltaX) || !Double.isFinite(deltaY)) {
            throw new IllegalArgumentException("Los deltas de etiqueta deben ser finitos");
        }
        double nextX = labelOffsetX + deltaX;
        double nextY = labelOffsetY + deltaY;
        double maxDistance = Double.isFinite(maximumDistanceFromLine) && maximumDistanceFromLine > 0.0
                ? maximumDistanceFromLine
                : 240.0;
        double distance = Math.hypot(nextX, nextY);
        if (distance > maxDistance) {
            double scale = maxDistance / distance;
            nextX *= scale;
            nextY *= scale;
        }
        return withLabelOffset(nextX, nextY);
    }

    private void ensureValidBendPointIndex(int index) {
        if (index < 0 || index >= bendPoints.size()) {
            throw new IllegalArgumentException("Índice de punto intermedio fuera de rango: " + index);
        }
    }

    public ConnectorLayout withVisibility(boolean updatedVisible) {
        return new ConnectorLayout(
                connectorId,
                sourceElementId,
                targetElementId,
                sourceAnchor,
                targetAnchor,
                pathKind,
                bendPoints,
                sourceMarker,
                targetMarker,
                sourceMarkerOrientation,
                targetMarkerOrientation,
                labelOffsetX,
                labelOffsetY,
                updatedVisible
        );
    }
}
