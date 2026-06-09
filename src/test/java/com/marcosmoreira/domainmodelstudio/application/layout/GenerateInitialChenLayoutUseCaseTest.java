package com.marcosmoreira.domainmodelstudio.application.layout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramModel;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.er.AttributeElement;
import com.marcosmoreira.domainmodelstudio.domain.er.EntityElement;
import com.marcosmoreira.domainmodelstudio.domain.er.RelationshipElement;
import com.marcosmoreira.domainmodelstudio.domain.layout.ConnectorPathKind;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayout;
import com.marcosmoreira.domainmodelstudio.domain.notation.NotationType;
import java.util.List;
import org.junit.jupiter.api.Test;

class GenerateInitialChenLayoutUseCaseTest {

    @Test
    void createsNodesAndConnectorsForChenDiagram() {
        DiagramModel model = new DiagramModel(
                List.of(
                        EntityElement.strong("producto", "Producto", List.of(AttributeElement.normal("producto_id", "pk id"))),
                        EntityElement.strong("categoria", "Categoría", List.of(AttributeElement.normal("categoria_id", "pk id")))
                ),
                List.of(RelationshipElement.between("pertenece", "Pertenece", "producto", "categoria", "0..M", "1"))
        );
        DiagramProject project = DiagramProject.blank("test", "Test").withModel(model);

        DiagramProject result = new GenerateInitialChenLayoutUseCase().generate(project);
        DiagramLayout layout = result.layouts().layoutFor(NotationType.CHEN).orElseThrow();

        assertEquals(5, layout.nodeCount());
        assertEquals(4, layout.connectorCount());
        assertTrue(layout.nodeFor(DiagramElementId.of("producto")).isPresent());
        assertTrue(layout.nodeFor(DiagramElementId.of("producto_id")).isPresent());
        assertTrue(layout.nodeFor(DiagramElementId.of("pertenece")).isPresent());
        assertTrue(layout.connectors().stream()
                .filter(connector -> connector.connectorId().value().startsWith("conn_pertenece"))
                .allMatch(connector -> connector.pathKind() == ConnectorPathKind.STRAIGHT
                        || connector.pathKind() == ConnectorPathKind.ORTHOGONAL));
    }
}
