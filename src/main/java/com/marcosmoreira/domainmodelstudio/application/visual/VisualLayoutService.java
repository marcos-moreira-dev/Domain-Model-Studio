package com.marcosmoreira.domainmodelstudio.application.visual;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.layout.BendPoint;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorMarker;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorPathKind;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayouts;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramPoint;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramSize;
import com.marcosmoreira.domainmodelstudio.domain.layout.MarkerOrientation;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Objects;
import java.util.Optional;
/**
 * Servicio de layout visual para proyectos especializados.
 *
 * <p>Sincroniza cajas y conectores de documentos como mapa de mÃ³dulos, UML, BPMN,
 * C4 o wireframes usando {@link DiagramLayouts}, sin contaminar los modelos semÃ¡nticos
 * con coordenadas.</p>
 */
public final class VisualLayoutService {
    private static final double BEND_POINT_MATCH_TOLERANCE = 0.0001;
    private final VisualLayoutSpecificationFactory specificationFactory;
    private final DefaultVisualLayoutGenerator layoutGenerator;
    private final VisualCommentLayoutPreserver visualCommentLayoutPreserver = new VisualCommentLayoutPreserver();
    public VisualLayoutService() {
        this(new VisualLayoutSpecificationFactory(), new DefaultVisualLayoutGenerator());
    }
    public VisualLayoutService(VisualLayoutSpecificationFactory specificationFactory,
                               DefaultVisualLayoutGenerator layoutGenerator) {
        this.specificationFactory = Objects.requireNonNull(specificationFactory, "specificationFactory");
        this.layoutGenerator = Objects.requireNonNull(layoutGenerator, "layoutGenerator");
    }
    /**
     * Asegura que el proyecto tenga layout para los elementos visuales esperados.
     *
     * <p>Preserva posiciones manuales y poda layouts que ya no corresponden al documento
     * semÃ¡ntico actual. Es la entrada normal al abrir o refrescar un workspace especializado.</p>
     */
    public DiagramProject ensureVisualLayout(DiagramProject project) {
        Objects.requireNonNull(project, "project");
        VisualLayoutSpecification specification = specificationFactory.fromProject(project);
        if (specification.emptySpecification()) {
            return project;
        }
        DiagramLayout activeLayout = project.layouts().activeLayout();
        DiagramLayout reconciled = reconcile(activeLayout, specification);
        DiagramLayouts layouts = project.layouts().withLayout(reconciled);
        return project.withLayouts(layouts);
    }
    /**
     * Asegura Ãºnicamente los elementos pedidos por una vista o subconjunto visual,
     * preservando layouts ya existentes de otros elementos.
     *
     * <p>Se usa para vistas UML Clases grandes donde abrir una vista parcial no debe
     * obligar a materializar el layout completo del documento. A diferencia de
     * {@link #ensureVisualLayout(DiagramProject)}, esta operaciÃ³n no poda nodos ni
     * conectores ajenos a la especificaciÃ³n actual.</p>
     */
    public DiagramProject ensureAdditionalVisualLayout(DiagramProject project) {
        Objects.requireNonNull(project, "project");
        VisualLayoutSpecification specification = specificationFactory.fromProject(project);
        if (specification.emptySpecification()) {
            return project;
        }
        DiagramLayout activeLayout = project.layouts().activeLayout();
        DiagramLayout reconciled = reconcilePreservingExisting(activeLayout, specification);
        DiagramLayouts layouts = project.layouts().withLayout(reconciled);
        return project.withLayouts(layouts);
    }
    /**
     * Recalcula por completo el layout visual especializado del proyecto activo.
     *
     * <p>A diferencia de {@link #ensureVisualLayout(DiagramProject)}, esta operaciÃ³n no preserva
     * posiciones manuales: se usa para la acciÃ³n explÃ­cita de autoorganizar/reorganizar.
     * Conserva la semÃ¡ntica del documento y reemplaza Ãºnicamente posiciones, tamaÃ±os y rutas
     * visuales del layout activo.</p>
     */
    public DiagramProject regenerateVisualLayout(DiagramProject project) {
        Objects.requireNonNull(project, "project");
        VisualLayoutSpecification specification = specificationFactory.fromProject(project);
        if (specification.emptySpecification()) {
            return project;
        }
        DiagramLayout activeLayout = project.layouts().activeLayout();
        List<NodeLayout> nodes = generatedNodes(specification.nodes());
        List<ConnectorLayout> connectors = specification.connectors().stream()
                .map(this::generatedConnector)
                .toList();
        DiagramLayout regenerated = new DiagramLayout(activeLayout.notation(), nodes, connectors);
        regenerated = new DiagramLayout(
                regenerated.notation(),
                visualCommentLayoutPreserver.appendFrom(activeLayout, regenerated.nodes()),
                regenerated.connectors());
        return project.withLayouts(project.layouts().withLayout(regenerated));
    }
    public Optional<NodeLayout> nodeLayout(DiagramProject project, DiagramElementId elementId) {
        Objects.requireNonNull(project, "project");
        Objects.requireNonNull(elementId, "elementId");
        return ensureVisualLayout(project).layouts().activeLayout().nodeFor(elementId);
    }
    public Optional<ConnectorLayout> connectorLayout(DiagramProject project, DiagramElementId connectorId) {
        Objects.requireNonNull(project, "project");
        Objects.requireNonNull(connectorId, "connectorId");
        return ensureVisualLayout(project).layouts().activeLayout().connectorById(connectorId);
    }
    public Optional<DiagramSize> preferredNodeSize(DiagramProject project, DiagramElementId elementId) {
        Objects.requireNonNull(project, "project");
        Objects.requireNonNull(elementId, "elementId");
        VisualLayoutSpecification specification = specificationFactory.fromProject(project);
        return specification.nodes().stream().filter(reference -> reference.layoutId().equals(elementId))
                .findFirst().map(reference -> DiagramSize.of(reference.preferredWidth(), reference.preferredHeight()));
    }
    public DiagramProject moveNodeTo(DiagramProject project, DiagramElementId elementId, double x, double y) {
        ensureFinite(x, "x");
        ensureFinite(y, "y");
        DiagramProject prepared = ensureVisualLayout(project);
        DiagramLayout updated = prepared.layouts().activeLayout().moveNode(elementId, x, y);
        return prepared.withLayouts(prepared.layouts().withLayout(updated));
    }
    public DiagramProject moveNodeBy(DiagramProject project, DiagramElementId elementId, double deltaX, double deltaY) {
        ensureFinite(deltaX, "deltaX");
        ensureFinite(deltaY, "deltaY");
        DiagramProject prepared = ensureVisualLayout(project);
        NodeLayout current = prepared.layouts().activeLayout().nodeFor(elementId)
                .orElseThrow(() -> new IllegalArgumentException("No existe layout para el nodo: " + elementId));
        return moveNodeTo(prepared, elementId, current.x() + deltaX, current.y() + deltaY);
    }
    public DiagramProject reorderNodes(DiagramProject project, Collection<DiagramElementId> elementIds, VisualLayerOrderCommand command) {
        Objects.requireNonNull(command, "command");
        DiagramProject prepared = ensureVisualLayout(project);
        Set<DiagramElementId> selected = elementIds == null ? Set.of() : Set.copyOf(elementIds);
        if (selected.isEmpty()) { return prepared; }
        DiagramLayout activeLayout = prepared.layouts().activeLayout();
        DiagramLayout reordered = switch (command) {
            case BRING_TO_FRONT -> activeLayout.bringNodesToFront(selected);
            case SEND_TO_BACK -> activeLayout.sendNodesToBack(selected);
            case RAISE -> activeLayout.raiseNodes(selected);
            case LOWER -> activeLayout.lowerNodes(selected);
        };
        return prepared.withLayouts(prepared.layouts().withLayout(reordered));
    }
    public DiagramProject resizeNodeTo(DiagramProject project, DiagramElementId elementId, double width, double height) {
        ensureFinite(width, "width");
        ensureFinite(height, "height");
        DiagramProject prepared = ensureVisualLayout(project);
        NodeLayout current = prepared.layouts().activeLayout().nodeFor(elementId)
                .orElseThrow(() -> new IllegalArgumentException("No existe layout para el nodo: " + elementId));
        DiagramLayout updated = prepared.layouts().activeLayout().withNode(current.resizedTo(width, height));
        return prepared.withLayouts(prepared.layouts().withLayout(updated));
    }
    public DiagramProject addBendPoint(DiagramProject project, DiagramElementId connectorId, double x, double y) {
        DiagramProject prepared = ensureVisualLayout(project);
        ConnectorLayout current = connector(prepared, connectorId);
        return withConnector(prepared, insertBendPointInNearestSegment(
                prepared.layouts().activeLayout(),
                current,
                x,
                y));
    }
    public ConnectorLayout insertBendPointInNearestSegment(
            DiagramLayout layout,
            ConnectorLayout connector,
            double x,
            double y
    ) {
        ensureFinite(x, "x");
        ensureFinite(y, "y");
        Objects.requireNonNull(layout, "layout");
        Objects.requireNonNull(connector, "connector");
        BendPoint point = BendPoint.of(x, y);
        return connector.withBendPoints(bendPointsWithInsertedPoint(layout, connector, point));
    }
    public Optional<Integer> bendPointIndexAt(DiagramProject project, DiagramElementId connectorId, double x, double y) {
        ensureFinite(x, "x");
        ensureFinite(y, "y");
        Objects.requireNonNull(project, "project");
        return project.layouts().activeLayout().connectorById(connectorId)
                .flatMap(connector -> bendPointIndexAt(connector, x, y));
    }
    public Optional<Integer> bendPointIndexAt(ConnectorLayout connector, double x, double y) {
        ensureFinite(x, "x");
        ensureFinite(y, "y");
        Objects.requireNonNull(connector, "connector");
        List<BendPoint> bendPoints = connector.bendPoints();
        for (int index = 0; index < bendPoints.size(); index++) {
            BendPoint point = bendPoints.get(index);
            if (Math.abs(point.x() - x) <= BEND_POINT_MATCH_TOLERANCE
                    && Math.abs(point.y() - y) <= BEND_POINT_MATCH_TOLERANCE) {
                return Optional.of(index);
            }
        }
        return Optional.empty();
    }
    public DiagramProject moveBendPointTo(DiagramProject project, DiagramElementId connectorId,
                                          int bendPointIndex, double x, double y) {
        ensureFinite(x, "x");
        ensureFinite(y, "y");
        DiagramProject prepared = ensureVisualLayout(project);
        ConnectorLayout current = connector(prepared, connectorId);
        BendPoint point = current.bendPoints().get(bendPointIndex);
        return withConnector(prepared, current.withMovedBendPoint(bendPointIndex, x - point.x(), y - point.y()));
    }
    public DiagramProject removeBendPoint(DiagramProject project, DiagramElementId connectorId, int bendPointIndex) {
        DiagramProject prepared = ensureVisualLayout(project);
        ConnectorLayout current = connector(prepared, connectorId);
        return withConnector(prepared, current.withoutBendPoint(bendPointIndex));
    }
    public DiagramProject moveConnectorLabelBy(
            DiagramProject project,
            DiagramElementId connectorId,
            double deltaX,
            double deltaY
    ) {
        ensureFinite(deltaX, "deltaX");
        ensureFinite(deltaY, "deltaY");
        DiagramProject prepared = ensureVisualLayout(project);
        ConnectorLayout current = connector(prepared, connectorId);
        return withConnector(prepared, current.withMovedLabelOffset(deltaX, deltaY, 240.0));
    }
    private DiagramLayout reconcile(DiagramLayout activeLayout, VisualLayoutSpecification specification) {
        List<NodeLayout> nodes = new ArrayList<>();
        Map<DiagramElementId, DiagramPoint> generatedPositions = layoutGenerator.positionsFor(specification.nodes());
        for (VisualNodeReference reference : specification.nodes()) {
            NodeLayout layout = activeLayout.nodeFor(reference.layoutId())
                    .map(existing -> withMinimumPreferredSize(existing, reference))
                    .orElseGet(() -> generatedNode(reference, generatedPositions));
            nodes.add(layout);
        }
        List<ConnectorLayout> connectors = new ArrayList<>();
        for (VisualConnectorReference reference : specification.connectors()) {
            ConnectorLayout layout = activeLayout.connectorById(reference.layoutId())
                    .map(existing -> withCurrentEndpoints(existing, reference))
                    .orElseGet(() -> generatedConnector(reference));
            connectors.add(layout);
        }
        return new DiagramLayout(activeLayout.notation(),
                visualCommentLayoutPreserver.appendFrom(activeLayout, preserveNodeOrder(activeLayout, nodes)),
                connectors);
    }
    private List<NodeLayout> preserveNodeOrder(DiagramLayout activeLayout, List<NodeLayout> reconciledNodes) {
        Map<DiagramElementId, NodeLayout> byId = new LinkedHashMap<>();
        reconciledNodes.forEach(node -> byId.put(node.elementId(), node));
        List<NodeLayout> ordered = new ArrayList<>();
        for (NodeLayout existing : activeLayout.nodes()) {
            NodeLayout replacement = byId.remove(existing.elementId());
            if (replacement != null) { ordered.add(replacement); }
        }
        ordered.addAll(byId.values());
        return ordered;
    }
    private DiagramLayout reconcilePreservingExisting(DiagramLayout activeLayout, VisualLayoutSpecification specification) {
        DiagramLayout reconciled = activeLayout;
        Map<DiagramElementId, DiagramPoint> generatedPositions = layoutGenerator.positionsFor(specification.nodes());
        for (VisualNodeReference reference : specification.nodes()) {
            NodeLayout layout = reconciled.nodeFor(reference.layoutId())
                    .map(existing -> withMinimumPreferredSize(existing, reference))
                    .orElseGet(() -> generatedNode(reference, generatedPositions));
            reconciled = reconciled.withNode(layout);
        }
        for (VisualConnectorReference reference : specification.connectors()) {
            ConnectorLayout layout = reconciled.connectorById(reference.layoutId())
                    .map(existing -> withCurrentEndpoints(existing, reference))
                    .orElseGet(() -> generatedConnector(reference));
            reconciled = reconciled.withConnector(layout);
        }
        return reconciled;
    }
    private List<NodeLayout> generatedNodes(List<VisualNodeReference> references) {
        Map<DiagramElementId, DiagramPoint> generatedPositions = layoutGenerator.positionsFor(references);
        return references.stream()
                .map(reference -> generatedNode(reference, generatedPositions))
                .toList();
    }
    private NodeLayout generatedNode(VisualNodeReference reference) {
        return generatedNode(reference, layoutGenerator.positionsFor(List.of(reference)));
    }
    private NodeLayout generatedNode(VisualNodeReference reference, Map<DiagramElementId, DiagramPoint> generatedPositions) {
        DiagramPoint position = generatedPositions.getOrDefault(reference.layoutId(), layoutGenerator.positionFor(reference));
        return new NodeLayout(
                reference.layoutId(),
                position,
                DiagramSize.of(reference.preferredWidth(), reference.preferredHeight()),
                true,
                false);
    }
    private static NodeLayout withMinimumPreferredSize(NodeLayout existing, VisualNodeReference reference) {
        if (preservesManualSize(reference)) {
            return existing;
        }
        double width = Math.max(existing.width(), readableMinimumWidth(reference));
        double height = Math.max(existing.height(), readableMinimumHeight(reference));
        if (Double.compare(width, existing.width()) == 0 && Double.compare(height, existing.height()) == 0) {
            return existing;
        }
        return existing.resizedTo(width, height);
    }
    private static double readableMinimumWidth(VisualNodeReference reference) { return Math.max(72.0, reference.preferredWidth() * 0.68); }
    private static double readableMinimumHeight(VisualNodeReference reference) { return Math.max(44.0, reference.preferredHeight() * 0.68); }
    private static boolean preservesManualSize(VisualNodeReference reference) {
        String layoutId = reference.layoutId().value();
        return layoutId.startsWith("wireframe-screen:") || layoutId.startsWith("wireframe-component:");
    }
    private ConnectorLayout generatedConnector(VisualConnectorReference reference) {
        return new ConnectorLayout(
                reference.layoutId(),
                reference.sourceLayoutId(),
                reference.targetLayoutId(),
                null,
                null,
                ConnectorPathKind.STRAIGHT,
                List.of(),
                ConnectorMarker.NONE,
                ConnectorMarker.NONE,
                MarkerOrientation.AUTO,
                MarkerOrientation.AUTO,
                true);
    }
    private ConnectorLayout withCurrentEndpoints(ConnectorLayout existing, VisualConnectorReference reference) {
        return new ConnectorLayout(
                existing.connectorId(),
                reference.sourceLayoutId(),
                reference.targetLayoutId(),
                existing.sourceAnchor(),
                existing.targetAnchor(),
                existing.pathKind(),
                existing.bendPoints(),
                existing.sourceMarker(),
                existing.targetMarker(),
                existing.sourceMarkerOrientation(),
                existing.targetMarkerOrientation(),
                existing.labelOffsetX(),
                existing.labelOffsetY(),
                existing.visible());
    }
    private static List<BendPoint> bendPointsWithInsertedPoint(
            DiagramLayout layout,
            ConnectorLayout connector,
            BendPoint point
    ) {
        List<BendPoint> currentPoints = connector.bendPoints();
        if (currentPoints.isEmpty()) {
            return List.of(point);
        }
        Optional<NodeLayout> source = layout.nodeFor(connector.sourceElementId());
        Optional<NodeLayout> target = layout.nodeFor(connector.targetElementId());
        if (source.isEmpty() || target.isEmpty()) {
            return appended(currentPoints, point);
        }
        List<DiagramPoint> route = new ArrayList<>();
        route.add(centerOf(source.get()));
        currentPoints.stream().map(BendPoint::asPoint).forEach(route::add);
        route.add(centerOf(target.get()));
        int segmentIndex = nearestSegmentIndex(point.asPoint(), route);
        int insertIndex = Math.max(0, Math.min(segmentIndex, currentPoints.size()));
        List<BendPoint> updated = new ArrayList<>(currentPoints);
        updated.add(insertIndex, point);
        return updated;
    }
    private static List<BendPoint> appended(List<BendPoint> currentPoints, BendPoint point) {
        List<BendPoint> updated = new ArrayList<>(currentPoints);
        updated.add(point);
        return updated;
    }
    private static DiagramPoint centerOf(NodeLayout layout) {
        return DiagramPoint.of(layout.x() + layout.width() / 2.0, layout.y() + layout.height() / 2.0);
    }
    private static int nearestSegmentIndex(DiagramPoint point, List<DiagramPoint> route) {
        int nearestIndex = 0;
        double nearestDistance = Double.MAX_VALUE;
        for (int index = 0; index < route.size() - 1; index++) {
            double distance = distanceToSegment(point, route.get(index), route.get(index + 1));
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearestIndex = index;
            }
        }
        return nearestIndex;
    }
    private static double distanceToSegment(DiagramPoint point, DiagramPoint a, DiagramPoint b) {
        double dx = b.x() - a.x();
        double dy = b.y() - a.y();
        double lengthSquared = dx * dx + dy * dy;
        if (lengthSquared <= 0.0001) {
            return Math.hypot(point.x() - a.x(), point.y() - a.y());
        }
        double t = ((point.x() - a.x()) * dx + (point.y() - a.y()) * dy) / lengthSquared;
        double clamped = Math.max(0.0, Math.min(1.0, t));
        double projectedX = a.x() + clamped * dx;
        double projectedY = a.y() + clamped * dy;
        return Math.hypot(point.x() - projectedX, point.y() - projectedY);
    }
    private ConnectorLayout connector(DiagramProject project, DiagramElementId connectorId) {
        return project.layouts().activeLayout().connectorById(connectorId)
                .orElseThrow(() -> new IllegalArgumentException("No existe layout para el conector: " + connectorId));
    }
    private DiagramProject withConnector(DiagramProject project, ConnectorLayout connector) {
        DiagramLayout updated = project.layouts().activeLayout().withConnector(connector);
        return project.withLayouts(project.layouts().withLayout(updated));
    }
    private static void ensureFinite(double value, String label) {
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("La coordenada " + label + " debe ser finita.");
        }
    }
}
