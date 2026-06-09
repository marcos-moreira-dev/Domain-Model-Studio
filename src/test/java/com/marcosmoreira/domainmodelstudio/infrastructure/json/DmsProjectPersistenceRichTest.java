package com.marcosmoreira.domainmodelstudio.infrastructure.json;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramModel;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.diagram.ProjectMetadata;
import com.marcosmoreira.domainmodelstudio.domain.er.AttributeElement;
import com.marcosmoreira.domainmodelstudio.domain.er.AttributeTag;
import com.marcosmoreira.domainmodelstudio.domain.er.EntityElement;
import com.marcosmoreira.domainmodelstudio.domain.er.RelationshipElement;
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
import com.marcosmoreira.domainmodelstudio.domain.style.DiagramStyleSheet;
import com.marcosmoreira.domainmodelstudio.domain.style.ElementStyle;
import com.marcosmoreira.domainmodelstudio.domain.style.FillStyle;
import com.marcosmoreira.domainmodelstudio.domain.style.RgbaColor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

/**
 * Pruebas de persistencia .dms más ricas que el smoke test inicial.
 *
 * <p>El objetivo es asegurar que el proyecto editable conserve la información que
 * más duele perder: notación activa, layouts separados, puntos intermedios,
 * anclas, marcadores y estilos.</p>
 */
class DmsProjectPersistenceRichTest {

    @Test
    void saveAndOpenPreservesNotationLayoutsConnectorsAndStyles() throws Exception {
        DiagramProject project = richProject();
        Path target = Files.createTempFile("domain-model-studio-rich-", ".dms");

        DmsProjectFileRepository repository = new DmsProjectFileRepository();
        repository.save(project, target);
        DiagramProject opened = repository.open(target);

        assertEquals("supermercado_v2", opened.metadata().id());
        assertEquals(NotationType.CROWS_FOOT, opened.metadata().activeNotation());
        assertEquals(NotationType.CROWS_FOOT, opened.layouts().activeNotation());
        assertEquals(2, opened.model().entityCount());
        assertEquals(1, opened.model().relationshipCount());
        assertTrue(opened.layouts().layoutFor(NotationType.CHEN).isPresent());
        assertTrue(opened.layouts().layoutFor(NotationType.CROWS_FOOT).isPresent());

        ConnectorLayout connector = opened.layouts()
                .layoutFor(NotationType.CROWS_FOOT)
                .orElseThrow()
                .connectorById(DiagramElementId.of("cf_categoria_producto"))
                .orElseThrow();

        assertEquals(AnchorSide.RIGHT, connector.sourceAnchor());
        assertEquals(AnchorSide.LEFT, connector.targetAnchor());
        assertEquals(ConnectorPathKind.POLYLINE, connector.pathKind());
        assertEquals(2, connector.bendPoints().size());
        assertEquals(ConnectorMarker.ONE, connector.sourceMarker());
        assertEquals(ConnectorMarker.OPTIONAL_MANY, connector.targetMarker());
        assertEquals(MarkerOrientation.RIGHT, connector.sourceMarkerOrientation());
        assertEquals(MarkerOrientation.LEFT, connector.targetMarkerOrientation());

        ElementStyle productStyle = opened.styleSheet()
                .explicitStyleFor(DiagramElementId.of("producto"))
                .orElseThrow();
        assertEquals("#FFF7CC", productStyle.fill().color().toHex());
    }

    private static DiagramProject richProject() {
        EntityElement product = EntityElement.strong(
                "producto",
                "Producto",
                List.of(
                        AttributeElement.withTags("producto_id", "pk id", Set.of(AttributeTag.PRIMARY_KEY)),
                        AttributeElement.withTags("codigo_barras", "código de barras", Set.of(AttributeTag.UNIQUE)),
                        AttributeElement.withTags("stock_actual", "stock actual", Set.of(AttributeTag.DERIVED))
                )
        );
        EntityElement category = EntityElement.strong(
                "categoria",
                "Categoría",
                List.of(AttributeElement.withTags("categoria_id", "pk id", Set.of(AttributeTag.PRIMARY_KEY)))
        );
        RelationshipElement belongsTo = RelationshipElement.between(
                "pertenece",
                "Pertenece",
                "producto",
                "categoria",
                "0..M",
                "1"
        );
        DiagramModel model = new DiagramModel(List.of(product, category), List.of(belongsTo));

        DiagramLayout chen = new DiagramLayout(
                NotationType.CHEN,
                List.of(
                        NodeLayout.at("producto", 90, 120, 140, 60),
                        NodeLayout.at("producto_id", 90, 220, 120, 44),
                        NodeLayout.at("codigo_barras", 70, 290, 150, 44),
                        NodeLayout.at("stock_actual", 90, 360, 120, 44),
                        NodeLayout.at("categoria", 550, 120, 140, 60),
                        NodeLayout.at("categoria_id", 550, 220, 120, 44),
                        NodeLayout.at("pertenece", 325, 115, 130, 70)
                ),
                List.of(
                        ConnectorLayout.straight("chen_producto_pertenece", "producto", "pertenece"),
                        ConnectorLayout.straight("chen_pertenece_categoria", "pertenece", "categoria")
                )
        );

        ConnectorLayout crowsConnector = new ConnectorLayout(
                DiagramElementId.of("cf_categoria_producto"),
                DiagramElementId.of("categoria"),
                DiagramElementId.of("producto"),
                AnchorSide.RIGHT,
                AnchorSide.LEFT,
                ConnectorPathKind.POLYLINE,
                List.of(BendPoint.of(360, 140), BendPoint.of(480, 140)),
                ConnectorMarker.ONE,
                ConnectorMarker.OPTIONAL_MANY,
                MarkerOrientation.RIGHT,
                MarkerOrientation.LEFT,
                true
        );
        DiagramLayout crows = new DiagramLayout(
                NotationType.CROWS_FOOT,
                List.of(
                        NodeLayout.at("categoria", 80, 80, 220, 130),
                        NodeLayout.at("producto", 560, 80, 240, 160)
                ),
                List.of(crowsConnector)
        );

        DiagramStyleSheet styleSheet = DiagramStyleSheet.defaults().withElementStyle(
                DiagramElementId.of("producto"),
                ElementStyle.defaultElement().withFill(FillStyle.of(RgbaColor.fromHex("#FFF7CC")))
        );

        return new DiagramProject(
                new ProjectMetadata(
                        "supermercado_v2",
                        "Modelo conceptual - Supermercado",
                        "0.2.0",
                        "test",
                        NotationType.CROWS_FOOT,
                        "examples/markdown/supermercado_multi_notacion.md",
                        "Proyecto de prueba de persistencia rica."
                ),
                model,
                new DiagramLayouts(NotationType.CROWS_FOOT, Map.of(NotationType.CHEN, chen, NotationType.CROWS_FOOT, crows)),
                styleSheet
        );
    }
}
