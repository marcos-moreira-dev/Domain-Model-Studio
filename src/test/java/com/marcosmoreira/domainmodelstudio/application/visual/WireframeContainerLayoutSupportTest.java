package com.marcosmoreira.domainmodelstudio.application.visual;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.marcosmoreira.domainmodelstudio.domain.catalog.DiagramTypeId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayouts;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramPoint;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramSize;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import com.marcosmoreira.domainmodelstudio.domain.notation.NotationType;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeComponent;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeComponentKind;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeDocument;
import com.marcosmoreira.domainmodelstudio.domain.wireframe.WireframeScreen;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class WireframeContainerLayoutSupportTest {

    private final WireframeContainerLayoutSupport support = new WireframeContainerLayoutSupport();

    @Test
    void moveScreenWithComponentsPreservesManualScreenSize() {
        Fixture fixture = fixture();

        DiagramProject moved = support.moveScreenWithComponents(
                fixture.project(),
                fixture.screen().id(),
                180.0,
                140.0,
                List.of(fixture.component()));

        NodeLayout screenLayout = moved.layouts().activeLayout()
                .nodeFor(VisualElementLayoutIds.wireframeScreen(fixture.screen().id()))
                .orElseThrow();
        NodeLayout componentLayout = moved.layouts().activeLayout()
                .nodeFor(VisualElementLayoutIds.wireframeComponent(fixture.component().id()))
                .orElseThrow();

        assertEquals(180.0, screenLayout.x());
        assertEquals(140.0, screenLayout.y());
        assertEquals(260.0, screenLayout.width());
        assertEquals(180.0, screenLayout.height());
        assertEquals(220.0, componentLayout.x());
        assertEquals(250.0, componentLayout.y());
    }

    @Test
    void expandScreenFitsContentOnlyWhenExplicitlyRequested() {
        Fixture fixture = fixture();

        DiagramProject fitted = support.expandScreen(
                fixture.project(),
                fixture.screen().id(),
                List.of(fixture.component()));

        NodeLayout screenLayout = fitted.layouts().activeLayout()
                .nodeFor(VisualElementLayoutIds.wireframeScreen(fixture.screen().id()))
                .orElseThrow();

        assertEquals(96.0, screenLayout.x());
        assertEquals(124.0, screenLayout.y());
        assertEquals(388.0, screenLayout.width());
        assertEquals(228.0, screenLayout.height());
    }

    private static Fixture fixture() {
        WireframeScreen screen = new WireframeScreen("screen-1", "Orden", "Ventas", "Registrar pedido", "");
        WireframeComponent component = new WireframeComponent(
                "component-1",
                screen.id(),
                WireframeComponentKind.TABLE,
                "Detalle de productos",
                1,
                "pedido.detalle",
                "",
                "");
        WireframeDocument document = new WireframeDocument(
                "Wireframes",
                "0.1.0",
                LocalDate.of(2026, 5, 17),
                List.of(screen),
                List.of(component));
        NodeLayout screenLayout = new NodeLayout(
                VisualElementLayoutIds.wireframeScreen(screen.id()),
                DiagramPoint.of(100.0, 100.0),
                DiagramSize.of(260.0, 180.0),
                true,
                false);
        NodeLayout componentLayout = new NodeLayout(
                VisualElementLayoutIds.wireframeComponent(component.id()),
                DiagramPoint.of(140.0, 210.0),
                DiagramSize.of(300.0, 90.0),
                true,
                false);
        DiagramProject project = DiagramProject.blank("wf", "Wireframes", DiagramTypeId.ADMIN_WIREFRAMES)
                .withWireframe(document)
                .withLayouts(DiagramLayouts.empty().withLayout(new DiagramLayout(
                        NotationType.CHEN,
                        List.of(screenLayout, componentLayout),
                        List.of())));
        return new Fixture(project, screen, component);
    }

    private record Fixture(DiagramProject project, WireframeScreen screen, WireframeComponent component) { }
}
