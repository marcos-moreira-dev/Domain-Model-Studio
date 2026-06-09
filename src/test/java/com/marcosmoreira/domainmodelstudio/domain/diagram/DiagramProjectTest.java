package com.marcosmoreira.domainmodelstudio.domain.diagram;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramModel;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.er.EntityElement;
import java.util.List;
import org.junit.jupiter.api.Test;

class DiagramProjectTest {

    @Test
    void createsBlankProjectWithEmptyModel() {
        DiagramProject project = DiagramProject.blank("supermercado_v1", "Modelo conceptual - Supermercado");

        assertEquals("supermercado_v1", project.metadata().id());
        assertTrue(project.model().isEmpty());
    }

    @Test
    void replacesModelWithoutChangingMetadata() {
        DiagramProject project = DiagramProject.blank("supermercado_v1", "Modelo conceptual - Supermercado");
        DiagramModel model = DiagramModel.empty().withEntity(EntityElement.strong("producto", "Producto", List.of()));

        DiagramProject updated = project.withModel(model);

        assertEquals("supermercado_v1", updated.metadata().id());
        assertEquals(1, updated.model().entityCount());
    }
}
