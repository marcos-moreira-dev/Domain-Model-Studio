package com.marcosmoreira.domainmodelstudio.domain.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
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
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

/** Pruebas de validación de proyecto completo: modelo + layout + estilos. */
class DiagramProjectValidatorRichTest {

    private final DiagramProjectValidator validator = new DiagramProjectValidator();

    @Test
    void acceptsStylesAppliedToConnectorIdsBecauseConnectorsBelongToLayout() {
        DiagramProject project = baseProject();
        DiagramStyleSheet styles = project.styleSheet().withElementStyle(
                DiagramElementId.of("cf_categoria_producto"),
                ElementStyle.defaultElement().withFill(FillStyle.of(RgbaColor.fromHex("#EEF4FF")))
        );

        ValidationResult result = validator.validate(project.withStyleSheet(styles));

        assertTrue(result.isValid());
        assertFalse(result.warnings().stream().anyMatch(
                issue -> issue.code() == ValidationCode.STYLE_REFERENCES_UNKNOWN_ELEMENT
        ));
    }

    @Test
    void detectsConnectorWithUnknownSourceAndUnknownTarget() {
        DiagramProject project = baseProject();
        ConnectorLayout brokenConnector = ConnectorLayout.straight(
                "conector_roto",
                "origen_fantasma",
                "destino_fantasma"
        );
        DiagramLayout brokenChen = project.layouts().layoutFor(NotationType.CHEN).orElseThrow()
                .withConnector(brokenConnector);
        DiagramProject broken = project.withLayouts(project.layouts().withLayout(brokenChen));

        ValidationResult result = validator.validate(broken);

        assertFalse(result.isValid());
        assertTrue(result.errors().stream().anyMatch(
                issue -> issue.code() == ValidationCode.LAYOUT_CONNECTOR_REFERENCES_UNKNOWN_SOURCE
        ));
        assertTrue(result.errors().stream().anyMatch(
                issue -> issue.code() == ValidationCode.LAYOUT_CONNECTOR_REFERENCES_UNKNOWN_TARGET
        ));
    }

    @Test
    void validatesSeveralNotationLayoutsAgainstSameSemanticModel() {
        DiagramProject project = baseProject();

        ValidationResult result = validator.validate(project);

        assertTrue(result.isValid());
        assertTrue(project.layouts().layoutFor(NotationType.CHEN).isPresent());
        assertTrue(project.layouts().layoutFor(NotationType.CROWS_FOOT).isPresent());
    }

    private static DiagramProject baseProject() {
        EntityElement product = EntityElement.strong(
                "producto",
                "Producto",
                List.of(AttributeElement.withTags("producto_id", "pk id", Set.of(AttributeTag.PRIMARY_KEY)))
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
                        NodeLayout.at("producto", 80, 120, 140, 60),
                        NodeLayout.at("producto_id", 90, 210, 120, 44),
                        NodeLayout.at("categoria", 520, 120, 140, 60),
                        NodeLayout.at("pertenece", 310, 115, 130, 70)
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
                List.of(BendPoint.of(360, 145)),
                ConnectorMarker.ONE,
                ConnectorMarker.OPTIONAL_MANY,
                MarkerOrientation.RIGHT,
                MarkerOrientation.LEFT,
                true
        );
        DiagramLayout crows = new DiagramLayout(
                NotationType.CROWS_FOOT,
                List.of(
                        NodeLayout.at("categoria", 80, 90, 220, 120),
                        NodeLayout.at("producto", 520, 90, 220, 120)
                ),
                List.of(crowsConnector)
        );

        return new DiagramProject(
                ProjectMetadata.draft("supermercado_v1", "Modelo conceptual - Supermercado"),
                model,
                new DiagramLayouts(NotationType.CROWS_FOOT, Map.of(NotationType.CHEN, chen, NotationType.CROWS_FOOT, crows)),
                DiagramStyleSheet.defaults()
        );
    }
}
