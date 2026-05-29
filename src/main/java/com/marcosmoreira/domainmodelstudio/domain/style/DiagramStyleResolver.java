package com.marcosmoreira.domainmodelstudio.domain.style;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.er.AttributeElement;
import com.marcosmoreira.domainmodelstudio.domain.er.EntityElement;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorLayout;

/**
 * Resuelve estilos visuales usando primero estilos explícitos y luego una
 * paleta pastel por categoría del modelo conceptual.
 */
public final class DiagramStyleResolver {

    private static final RgbaColor ENTITY_FILL = RgbaColor.fromHex("#EAF2FF");
    private static final RgbaColor ATTRIBUTE_FILL = RgbaColor.fromHex("#FFF4D8");
    private static final RgbaColor DERIVED_ATTRIBUTE_FILL = RgbaColor.fromHex("#F3E8FF");
    private static final RgbaColor RELATIONSHIP_FILL = RgbaColor.fromHex("#EAF7EA");
    private static final RgbaColor CONNECTOR_STROKE = RgbaColor.fromHex("#4F5D75");

    private DiagramStyleResolver() {
    }

    public static ElementStyle resolvedStyleFor(DiagramProject project, DiagramElementId elementId) {
        ElementStyle base = project.styleSheet().resolvedStyleFor(elementId);
        if (project.styleSheet().explicitStyleFor(elementId).isPresent()) {
            return base;
        }
        if (project.model().entityById(elementId).isPresent()) {
            return base.withFill(FillStyle.of(ENTITY_FILL));
        }
        if (project.model().relationshipById(elementId).isPresent()) {
            return base.withFill(FillStyle.of(RELATIONSHIP_FILL));
        }
        for (EntityElement entity : project.model().entities()) {
            for (AttributeElement attribute : entity.attributes()) {
                if (attribute.id().equals(elementId)) {
                    RgbaColor fill = attribute.isDerived() ? DERIVED_ATTRIBUTE_FILL : ATTRIBUTE_FILL;
                    return base.withFill(FillStyle.of(fill));
                }
            }
        }
        for (ConnectorLayout connector : project.layouts().activeLayout().connectors()) {
            if (connector.connectorId().equals(elementId)) {
                return base.withStroke(new StrokeStyle(
                        CONNECTOR_STROKE,
                        base.stroke().width(),
                        base.stroke().pattern()
                ));
            }
        }
        return base;
    }

    public static String defaultEntityFillHex() {
        return ENTITY_FILL.toHex();
    }

    public static String defaultAttributeFillHex() {
        return ATTRIBUTE_FILL.toHex();
    }

    public static String defaultDerivedAttributeFillHex() {
        return DERIVED_ATTRIBUTE_FILL.toHex();
    }

    public static String defaultRelationshipFillHex() {
        return RELATIONSHIP_FILL.toHex();
    }

    public static String defaultConnectorStrokeHex() {
        return CONNECTOR_STROKE.toHex();
    }
}
