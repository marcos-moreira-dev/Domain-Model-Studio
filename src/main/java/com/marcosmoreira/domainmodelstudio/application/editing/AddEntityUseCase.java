package com.marcosmoreira.domainmodelstudio.application.editing;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramModel;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.er.EntityElement;
import com.marcosmoreira.domainmodelstudio.domain.er.EntityKind;
import java.util.List;
import java.util.Objects;

/** Crea una entidad conceptual desde el lienzo y agrega su primer layout visual. */
public final class AddEntityUseCase {

    public DiagramProject add(DiagramProject project, double x, double y) {
        Objects.requireNonNull(project, "El proyecto no puede ser null");
        ensureFinite(x, "x");
        ensureFinite(y, "y");
        DiagramElementId entityId = ConceptualEditingIds.uniqueElementId(project.model(), "nueva_entidad");
        EntityElement entity = new EntityElement(entityId, "Nueva entidad", EntityKind.STRONG, "", "", List.of());
        DiagramModel updatedModel = project.model().withEntity(entity);
        DiagramProject withModel = project.withModel(updatedModel);
        return withModel.withLayouts(ConceptualLayoutEditingSupport.withEntityNode(withModel, entity, x, y));
    }

    private static void ensureFinite(double value, String name) {
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("La coordenada " + name + " debe ser finita");
        }
    }
}
