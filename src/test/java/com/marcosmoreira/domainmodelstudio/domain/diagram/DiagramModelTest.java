package com.marcosmoreira.domainmodelstudio.domain.diagram;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramModel;
import com.marcosmoreira.domainmodelstudio.domain.er.EntityElement;
import com.marcosmoreira.domainmodelstudio.domain.er.RelationshipElement;
import java.util.List;
import org.junit.jupiter.api.Test;

class DiagramModelTest {

    @Test
    void keepsEntitiesAndRelationshipsAvailableById() {
        EntityElement product = EntityElement.strong("producto", "Producto", List.of());
        EntityElement category = EntityElement.strong("categoria", "Categoría", List.of());
        RelationshipElement belongsTo = RelationshipElement.between(
                "pertenece",
                "Pertenece",
                "producto",
                "categoria",
                "0..M",
                "1"
        );

        DiagramModel model = DiagramModel.empty()
                .withEntity(product)
                .withEntity(category)
                .withRelationship(belongsTo);

        assertEquals(2, model.entityCount());
        assertEquals(1, model.relationshipCount());
        assertTrue(model.entityById(DiagramElementId.of("producto")).isPresent());
        assertTrue(model.relationshipById(DiagramElementId.of("pertenece")).isPresent());
    }

    @Test
    void rejectsDuplicatedEntityIds() {
        EntityElement first = EntityElement.strong("producto", "Producto", List.of());
        EntityElement second = EntityElement.strong("producto", "Producto duplicado", List.of());

        assertThrows(IllegalArgumentException.class, () -> new DiagramModel(List.of(first, second), List.of()));
    }
}
