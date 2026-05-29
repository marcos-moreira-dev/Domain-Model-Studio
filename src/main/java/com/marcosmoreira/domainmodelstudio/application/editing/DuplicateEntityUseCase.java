package com.marcosmoreira.domainmodelstudio.application.editing;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.er.AttributeElement;
import com.marcosmoreira.domainmodelstudio.domain.er.EntityElement;
import com.marcosmoreira.domainmodelstudio.domain.layout.NodeLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Duplica una entidad conceptual con sus atributos, sin copiar relaciones. */
public final class DuplicateEntityUseCase {

    public DiagramProject duplicate(DiagramProject project, DiagramElementId entityId) {
        Objects.requireNonNull(project, "El proyecto no puede ser null");
        Objects.requireNonNull(entityId, "El ID de la entidad no puede ser null");
        EntityElement source = project.model().entityById(entityId)
                .orElseThrow(() -> new IllegalArgumentException("Selecciona una entidad para duplicar."));
        DiagramElementId newEntityId = ConceptualEditingIds.uniqueElementId(project.model(), source.id().value() + "_copia");
        List<AttributeElement> copiedAttributes = new ArrayList<>();
        int attributeIndex = 1;
        for (AttributeElement attribute : source.attributes()) {
            DiagramElementId newAttributeId = DiagramElementId.of(newEntityId.value() + "_atributo_" + attributeIndex++);
            copiedAttributes.add(new AttributeElement(newAttributeId, attribute.name(), attribute.tags(), attribute.description()));
        }
        EntityElement copy = new EntityElement(
                newEntityId,
                source.name() + " copia",
                source.kind(),
                source.module(),
                source.description(),
                copiedAttributes
        );
        DiagramProject withModel = project.withModel(project.model().withEntity(copy));
        NodeLayout sourceNode = project.layouts().activeLayout().nodeFor(entityId).orElse(null);
        double x = sourceNode == null ? 220.0 : sourceNode.x() + 48.0;
        double y = sourceNode == null ? 220.0 : sourceNode.y() + 48.0;
        DiagramProject withEntityLayout = withModel.withLayouts(ConceptualLayoutEditingSupport.withEntityNode(withModel, copy, x, y));
        DiagramProject withAttributes = withEntityLayout;
        EntityElement ownerAfter = withAttributes.model().entityById(newEntityId).orElse(copy);
        for (AttributeElement attribute : ownerAfter.attributes()) {
            withAttributes = withAttributes.withLayouts(ConceptualLayoutEditingSupport.withAttributeNode(withAttributes, ownerAfter, attribute));
        }
        return withAttributes;
    }
}
