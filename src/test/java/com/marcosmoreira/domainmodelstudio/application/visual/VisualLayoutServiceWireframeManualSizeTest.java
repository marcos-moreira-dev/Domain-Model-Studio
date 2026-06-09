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
import java.util.List;
import org.junit.jupiter.api.Test;

class VisualLayoutServiceWireframeManualSizeTest {

    private final VisualLayoutService service = new VisualLayoutService();

    @Test
    void ensureVisualLayoutPreservesManualWireframeSizes() {
        WireframeScreen screen = new WireframeScreen("screen-1", "Pantalla chica", "", "", "");
        WireframeComponent component = new WireframeComponent(
                "component-1",
                screen.id(),
                WireframeComponentKind.TABLE,
                "Tabla compacta",
                1,
                "",
                "",
                "");
        WireframeDocument document = new WireframeDocument(
                "Wireframes",
                "0.1.0",
                java.time.LocalDate.of(2026, 5, 17),
                List.of(screen),
                List.of(component));

        NodeLayout manualScreen = new NodeLayout(
                VisualElementLayoutIds.wireframeScreen(screen.id()),
                DiagramPoint.of(80.0, 64.0),
                DiagramSize.of(190.0, 130.0),
                true,
                false);
        NodeLayout manualComponent = new NodeLayout(
                VisualElementLayoutIds.wireframeComponent(component.id()),
                DiagramPoint.of(110.0, 120.0),
                DiagramSize.of(70.0, 28.0),
                true,
                false);
        DiagramLayouts layouts = DiagramLayouts.empty().withLayout(new DiagramLayout(
                NotationType.CHEN,
                List.of(manualScreen, manualComponent),
                List.of()));
        DiagramProject project = DiagramProject.blank("wf", "Wireframes", DiagramTypeId.ADMIN_WIREFRAMES)
                .withWireframe(document)
                .withLayouts(layouts);

        DiagramProject prepared = service.ensureVisualLayout(project);

        NodeLayout preparedScreen = prepared.layouts().activeLayout()
                .nodeFor(VisualElementLayoutIds.wireframeScreen(screen.id()))
                .orElseThrow();
        NodeLayout preparedComponent = prepared.layouts().activeLayout()
                .nodeFor(VisualElementLayoutIds.wireframeComponent(component.id()))
                .orElseThrow();
        assertEquals(190.0, preparedScreen.width());
        assertEquals(130.0, preparedScreen.height());
        assertEquals(70.0, preparedComponent.width());
        assertEquals(28.0, preparedComponent.height());
    }
}
