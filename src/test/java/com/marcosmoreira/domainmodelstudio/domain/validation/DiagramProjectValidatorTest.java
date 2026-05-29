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
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayout;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayouts;
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

class DiagramProjectValidatorTest {

    private final DiagramProjectValidator validator = new DiagramProjectValidator();

    @Test
    void acceptsProjectWithModelLayoutAndConnectorReferences() {
        DiagramProject project = validProject();

        ValidationResult result = validator.validate(project);

        assertTrue(result.isValid());
    }

    @Test
    void detectsLayoutNodeReferencingUnknownElement() {
        DiagramProject base = validProject();
        DiagramLayout brokenLayout = base.layouts().activeLayout()
                .withNode(NodeLayout.at("fantasma", 10, 10, 120, 60));
        DiagramProject broken = base.withLayouts(base.layouts().withLayout(brokenLayout));

        ValidationResult result = validator.validate(broken);

        assertFalse(result.isValid());
        assertTrue(result.errors().stream().anyMatch(issue -> issue.code() == ValidationCode.LAYOUT_NODE_REFERENCES_UNKNOWN_ELEMENT));
    }

    @Test
    void warnsAboutStyleForUnknownElement() {
        DiagramProject base = validProject();
        DiagramStyleSheet sheet = base.styleSheet().withElementStyle(
                DiagramElementId.of("fantasma"),
                ElementStyle.defaultElement().withFill(FillStyle.of(RgbaColor.fromHex("#FFF7CC")))
        );
        DiagramProject broken = base.withStyleSheet(sheet);

        ValidationResult result = validator.validate(broken);

        assertTrue(result.isValid());
        assertTrue(result.warnings().stream().anyMatch(issue -> issue.code() == ValidationCode.STYLE_REFERENCES_UNKNOWN_ELEMENT));
    }

    private static DiagramProject validProject() {
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
        DiagramLayout chenLayout = new DiagramLayout(
                NotationType.CHEN,
                List.of(
                        NodeLayout.at("producto", 10, 20, 120, 60),
                        NodeLayout.at("producto_id", 20, 100, 110, 40),
                        NodeLayout.at("categoria", 400, 20, 120, 60),
                        NodeLayout.at("pertenece", 220, 20, 120, 70)
                ),
                List.of(
                        ConnectorLayout.straight("c_producto_pertenece", "producto", "pertenece"),
                        ConnectorLayout.straight("c_pertenece_categoria", "pertenece", "categoria")
                )
        );

        return new DiagramProject(
                ProjectMetadata.draft("supermercado_v1", "Modelo conceptual - Supermercado"),
                model,
                new DiagramLayouts(NotationType.CHEN, Map.of(NotationType.CHEN, chenLayout)),
                DiagramStyleSheet.defaults()
        );
    }
}
