package com.marcosmoreira.domainmodelstudio.application.editing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.marcosmoreira.domainmodelstudio.application.layout.GenerateInitialChenLayoutUseCase;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramElementId;
import com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramProject;
import com.marcosmoreira.domainmodelstudio.domain.er.AttributeElement;
import com.marcosmoreira.domainmodelstudio.domain.er.EntityElement;
import com.marcosmoreira.domainmodelstudio.domain.er.RelationshipElement;
import com.marcosmoreira.domainmodelstudio.domain.layout.DiagramLayout;
import com.marcosmoreira.domainmodelstudio.domain.notation.NotationType;
import java.util.List;
import org.junit.jupiter.api.Test;

final class ConceptualManualEditingUseCaseTest {

    @Test
    void addEntityCreatesSemanticEntityAndVisibleNode() {
        DiagramProject project = DiagramProject.blank("manual", "Manual");

        DiagramProject updated = new AddEntityUseCase().add(project, 120.0, 180.0);

        assertEquals(1, updated.model().entityCount());
        DiagramElementId entityId = updated.model().entities().getFirst().id();
        DiagramLayout layout = updated.layouts().activeLayout();
        assertTrue(layout.nodeFor(entityId).isPresent());
        assertEquals(120.0, layout.nodeFor(entityId).orElseThrow().x());
        assertEquals(180.0, layout.nodeFor(entityId).orElseThrow().y());
    }

    @Test
    void addAttributeAddsAttributeNodeAndConnectorInChenLayout() {
        DiagramProject project = new GenerateInitialChenLayoutUseCase().generate(baseProject());
        DiagramElementId entityId = project.model().entities().getFirst().id();

        DiagramProject updated = new AddAttributeUseCase().add(project, entityId);
        DiagramElementId attributeId = updated.model().entityById(entityId).orElseThrow().attributes().get(1).id();

        assertTrue(updated.layouts().layoutFor(NotationType.CHEN).orElseThrow().nodeFor(attributeId).isPresent());
        assertTrue(updated.layouts().layoutFor(NotationType.CHEN).orElseThrow().connectors().stream()
                .anyMatch(connector -> connector.targetElementId().equals(attributeId)));
    }

    @Test
    void addRelationshipCreatesRelationshipAndChenConnectors() {
        DiagramProject project = new GenerateInitialChenLayoutUseCase().generate(baseProject());
        DiagramElementId from = project.model().entities().get(0).id();
        DiagramElementId to = project.model().entities().get(1).id();

        DiagramProject updated = new AddRelationshipUseCase().add(project, from, to);
        RelationshipElement relationship = updated.model().relationships().getFirst();

        assertEquals("Nueva relación", relationship.name());
        assertTrue(updated.layouts().layoutFor(NotationType.CHEN).orElseThrow().nodeFor(relationship.id()).isPresent());
        assertEquals(2, updated.layouts().layoutFor(NotationType.CHEN).orElseThrow().connectors().stream()
                .filter(connector -> connector.sourceElementId().equals(relationship.id())
                        || connector.targetElementId().equals(relationship.id()))
                .count());
    }

    @Test
    void removeEntityAlsoRemovesItsRelationshipsFromModelAndLayout() {
        DiagramProject project = new GenerateInitialChenLayoutUseCase().generate(baseProject());
        DiagramElementId from = project.model().entities().get(0).id();
        DiagramElementId to = project.model().entities().get(1).id();
        DiagramProject withRelationship = new AddRelationshipUseCase().add(project, from, to);
        DiagramElementId relationshipId = withRelationship.model().relationships().getFirst().id();

        DiagramProject updated = new RemoveDiagramElementUseCase().remove(withRelationship, from);

        assertEquals(1, updated.model().entityCount());
        assertEquals(0, updated.model().relationshipCount());
        assertTrue(updated.layouts().layoutFor(NotationType.CHEN).orElseThrow().nodeFor(from).isEmpty());
        assertTrue(updated.layouts().layoutFor(NotationType.CHEN).orElseThrow().nodeFor(relationshipId).isEmpty());
    }

    @Test
    void duplicateEntityCopiesAttributesWithoutCopyingRelationships() {
        DiagramProject project = new GenerateInitialChenLayoutUseCase().generate(baseProject());
        DiagramElementId source = project.model().entities().getFirst().id();

        DiagramProject updated = new DuplicateEntityUseCase().duplicate(project, source);

        assertEquals(3, updated.model().entityCount());
        EntityElement copy = updated.model().entities().get(2);
        assertEquals(1, copy.attributes().size());
        assertNotNull(updated.layouts().layoutFor(NotationType.CHEN).orElseThrow().nodeFor(copy.id()).orElse(null));
    }

    private DiagramProject baseProject() {
        EntityElement customer = EntityElement.strong("cliente", "Cliente", List.of(AttributeElement.normal("cliente_id", "id")));
        EntityElement order = EntityElement.strong("pedido", "Pedido", List.of());
        return DiagramProject.blank("base", "Base").withModel(new com.marcosmoreira.domainmodelstudio.domain.diagram.DiagramModel(
                List.of(customer, order),
                List.of()
        ));
    }
}
