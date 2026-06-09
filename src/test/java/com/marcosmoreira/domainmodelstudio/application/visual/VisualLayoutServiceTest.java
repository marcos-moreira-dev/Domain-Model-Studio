package com.marcosmoreira.domainmodelstudio.application.visual;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.layout.AnchorSide;
import com.marcosmoreira.domainmodelstudio.domain.layout.BendPoint;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorMarker;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorPathKind;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayouts;
import com.marcosmoreira.domainmodelstudio.domain.layout.MarkerOrientation;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.domain.notation.NotationType;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.DependencyKind;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleDependency;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleMapDocument;
import com.marcosmoreira.domainmodelstudio.domain.modulemap.ModuleNode;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class VisualLayoutServiceTest {

    private final VisualLayoutService service = new VisualLayoutService();

    @Test
    void createsLayoutForSpecializedModuleMapWithoutPuttingCoordinatesInDomainNodes() {
        DiagramProject prepared = service.ensureVisualLayout(projectWithModuleMap());

        assertTrue(prepared.layouts().activeLayout().nodeFor(VisualElementLayoutIds.module("ventas")).isPresent());
        assertTrue(prepared.layouts().activeLayout().nodeFor(VisualElementLayoutIds.module("inventario")).isPresent());
        assertTrue(prepared.layouts().activeLayout().connectorById(VisualElementLayoutIds.moduleContainment("ventas", "inventario")).isPresent());
        assertTrue(prepared.layouts().activeLayout().connectorById(VisualElementLayoutIds.dependency("dep-ventas-inventario")).isPresent());
    }

    @Test
    void preservesManualNodePositionWhenLayoutIsEnsuredAgain() {
        DiagramProject moved = service.moveNodeTo(projectWithModuleMap(), VisualElementLayoutIds.module("ventas"), 333, 222);
        DiagramProject ensuredAgain = service.ensureVisualLayout(moved);

        var layout = ensuredAgain.layouts().activeLayout().nodeFor(VisualElementLayoutIds.module("ventas")).orElseThrow();
        assertEquals(333, layout.x());
        assertEquals(222, layout.y());
    }

    @Test
    void persistsBendPointsInVisualConnectorLayout() {
        DiagramProject withPoint = service.addBendPoint(
                projectWithModuleMap(),
                VisualElementLayoutIds.dependency("dep-ventas-inventario"),
                190,
                145);
        DiagramProject movedPoint = service.moveBendPointTo(
                withPoint,
                VisualElementLayoutIds.dependency("dep-ventas-inventario"),
                0,
                220,
                160);

        BendPoint point = movedPoint.layouts().activeLayout()
                .connectorById(VisualElementLayoutIds.dependency("dep-ventas-inventario"))
                .orElseThrow()
                .bendPoints()
                .get(0);
        assertEquals(220, point.x());
        assertEquals(160, point.y());
    }

    @Test
    void insertsBendPointInNearestRouteSegmentInsteadOfAlwaysAppending() {
        DiagramProject withFirstPointNearTarget = service.addBendPoint(
                projectWithManualStraightConnector(),
                com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId.of("connector"),
                350,
                40);

        DiagramProject withSecondPointNearSource = service.addBendPoint(
                withFirstPointNearTarget,
                com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId.of("connector"),
                170,
                40);

        List<BendPoint> points = withSecondPointNearSource.layouts().activeLayout()
                .connectorById(com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId.of("connector"))
                .orElseThrow()
                .bendPoints();

        assertEquals(2, points.size());
        assertEquals(170, points.get(0).x());
        assertEquals(350, points.get(1).x());
        assertEquals(0, service.bendPointIndexAt(
                withSecondPointNearSource,
                com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId.of("connector"),
                170,
                40).orElseThrow());
    }

    @Test
    void persistsConnectorLabelOffsetInVisualConnectorLayout() {
        DiagramProject movedLabel = service.moveConnectorLabelBy(
                projectWithModuleMap(),
                VisualElementLayoutIds.dependency("dep-ventas-inventario"),
                32,
                -18);

        var connector = movedLabel.layouts().activeLayout()
                .connectorById(VisualElementLayoutIds.dependency("dep-ventas-inventario"))
                .orElseThrow();
        assertEquals(32, connector.labelOffsetX());
        assertEquals(-18, connector.labelOffsetY());
    }

    private static DiagramProject projectWithManualStraightConnector() {
        DiagramLayout layout = new DiagramLayout(
                NotationType.CHEN,
                List.of(
                        NodeLayout.at("source", 0, 0, 100, 80),
                        NodeLayout.at("target", 400, 0, 100, 80)),
                List.of(new ConnectorLayout(
                        com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId.of("connector"),
                        com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId.of("source"),
                        com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId.of("target"),
                        AnchorSide.AUTO,
                        AnchorSide.AUTO,
                        ConnectorPathKind.STRAIGHT,
                        List.of(),
                        ConnectorMarker.NONE,
                        ConnectorMarker.NONE,
                        MarkerOrientation.AUTO,
                        MarkerOrientation.AUTO,
                        true)));

        return DiagramProject.blank("manual-route", "Manual route", DiagramTypeId.FREE_GRAPH)
                .withLayouts(DiagramLayouts.empty().withLayout(layout));
    }

    private static DiagramProject projectWithModuleMap() {
        ModuleNode sales = ModuleNode.root("ventas", "Ventas");
        ModuleNode inventory = ModuleNode.child("inventario", "Inventario", "ventas");
        ModuleDependency dependency = new ModuleDependency(
                "dep-ventas-inventario",
                sales.id(),
                inventory.id(),
                DependencyKind.USES,
                "Ventas consulta stock.",
                "");
        ModuleMapDocument document = new ModuleMapDocument(
                "Sistema administrativo",
                "borrador",
                LocalDate.of(2026, 5, 1),
                List.of(sales, inventory),
                List.of(dependency),
                "");
        return DiagramProject.blank("mapa", "Mapa", DiagramTypeId.ADMIN_MODULE_MAP)
                .withModuleMap(document);
    }
}
